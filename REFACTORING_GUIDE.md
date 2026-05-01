# Refactoring Guide: Pricing & Discount Engine

## Overview
This document walks through the refactoring of a poorly-designed `PricingEngine` using the **Red-Green-Refactor (TDD)** methodology. Each refactoring cycle improves code quality, maintainability, and extensibility.

---

## Cycle 1: Extract Constants
**Commits:**
- `refactor: extract magic numbers/strings into constants`

### Problem
- Magic numbers (0.08, 0.15, etc.) scattered throughout the code
- String literals ("VIP", "REGULAR", "SAVE10") hard-coded in multiple places
- Difficult to change configuration globally

### Solution
**Created: `PricingConstants.java`**
```java
public class PricingConstants {
    public static final double DEFAULT_TAX_RATE = 0.08;
    public static final String CUSTOMER_TYPE_REGULAR = "REGULAR";
    public static final String CUSTOMER_TYPE_VIP = "VIP";
    public static final double VIP_DISCOUNT_RATE = 0.15;
    // ... more constants
}
```

### Benefits
✓ Single source of truth for configuration values  
✓ Easy to change tax rates or discount percentages globally  
✓ Self-documenting code: `PricingConstants.VIP_DISCOUNT_RATE` vs magic `0.15`  
✓ Can support external configuration (properties files) in future

---

## Cycle 2: Strategy Pattern for Discounts
**Commits:**
- `add DiscountStrategy interface`
- `add RegularDiscountStrategy`
- `add VipDiscountStrategy`
- `add DiscountStrategyFactory`
- `refactor: delegate discount logic to DiscountStrategyFactory using Strategy Pattern`

### Problem
```java
// BEFORE: Monolithic discount logic
if (customer.equals("VIP")) {
    disc = sub * 0.15;
} else if (customer.equals("REGULAR")) {
    disc = sub * 0.0;
}
// Hard to add new customer types!
```

### Solution
**Strategy Pattern:** Each customer type has its own discount calculator

```java
public interface DiscountStrategy {
    double calculateDiscount(double subtotal);
}

public class VipDiscountStrategy implements DiscountStrategy {
    public double calculateDiscount(double subtotal) {
        return subtotal * PricingConstants.VIP_DISCOUNT_RATE;
    }
}
```

**Factory:** Centralized strategy creation
```java
public class DiscountStrategyFactory {
    public static DiscountStrategy getStrategy(String customerType) {
        switch (customerType) {
            case "VIP":
                return new VipDiscountStrategy();
            case "REGULAR":
                return new RegularDiscountStrategy();
            // ... etc
        }
    }
}
```

### Benefits
✓ **Open/Closed Principle**: Adding a new customer type requires only:
  - Create new `*DiscountStrategy` class
  - Add case to factory
  - Zero changes to `PricingEngine`!

✓ **Testability**: Each strategy can be tested independently  
✓ **Flexibility**: Strategies can be injected, mocked, or composed  
✓ **Maintainability**: Discount logic isolated and organized

---

## Cycle 3: Separation of Concerns
**Commits:**
- `add Invoice data class`
- `add InvoicePrinter`
- `refactor: extract computeTotal() method & delegate to Invoice/InvoicePrinter (Separation of Concerns)`

### Problem
```java
// BEFORE: PricingEngine does everything
public double calc(double[] prices, int[] qtys, String customer, String code) {
    // ... Calculate subtotal
    // ... Calculate discount
    // ... Calculate tax
    // ... Print invoice? Format invoice? Return what?
    return final_price; // Only returns one number!
}
```

Issues:
- Cannot see full invoice breakdown (subtotal, discount, tax separately)
- Printing logic mixed with calculation logic
- Hard to extend with new output formats

### Solution

**Data Class: `Invoice.java`**
```java
public class Invoice {
    private final double subtotal;
    private final double discountAmount;
    private final double tax;
    private final double finalPrice;
    // ... getters
}
```

**Printer: `InvoicePrinter.java`**
```java
public class InvoicePrinter {
    public void print(Invoice invoice) {
        // Formatted console output
    }
    
    public String format(Invoice invoice) {
        // Alternative formats
    }
}
```

**Refactored Engine:**
```java
public Invoice calculate(double[] prices, int[] quantities, 
                        String customerType, String discountCode) {
    double subtotal = computeSubtotal(prices, quantities);
    double discount = computeDiscount(subtotal, customerType, discountCode);
    double tax = (subtotal - discount) * TAX_RATE;
    double finalPrice = (subtotal - discount) + tax;
    
    return new Invoice(prices, quantities, customerType, discountCode,
                       subtotal, discount, tax, finalPrice);
}
```

### Benefits
✓ **Single Responsibility**: PricingEngine calculates, InvoicePrinter prints  
✓ **Richer Output**: Full invoice breakdown available  
✓ **Extensibility**: Easy to add JSON, PDF, or email formats  
✓ **Testability**: Can verify calculations without printing side effects  
✓ **Better API**: Clear return types vs cryptic double values

---

## Cycle 4: Parameter Object Pattern
**Commits:**
- `add PricingRequest parameter object with Builder`
- `refactor: introduce PricingRequest parameter object`

### Problem
```java
// BEFORE: Many parameters = confusing method signature
public Invoice calculate(double[] prices, int[] quantities, 
                        String customerType, String discountCode)
```

Issues:
- Hard to remember parameter order
- Easy to pass arguments in wrong order (compiler won't catch it)
- Difficult to add optional parameters
- Reduces method flexibility

### Solution
**Parameter Object with Builder:**
```java
public class PricingRequest {
    private final double[] prices;
    private final int[] quantities;
    private final String customerType;
    private final String discountCode;
    
    // ... Builder pattern for fluent API
    public static class Builder {
        public Builder prices(double[] prices) { ... }
        public Builder quantities(int[] quantities) { ... }
        public Builder customerType(String customerType) { ... }
        public Builder discountCode(String discountCode) { ... }
        public PricingRequest build() { ... }
    }
}
```

**Simplified API:**
```java
// BEFORE
invoice = engine.calculate(prices, quantities, "VIP", "SAVE20");

// AFTER - clearer!
PricingRequest request = new PricingRequest.Builder()
    .prices(prices)
    .quantities(quantities)
    .customerType("VIP")
    .discountCode("SAVE20")
    .build();
invoice = engine.calculate(request);
```

### Benefits
✓ **Self-Documenting**: Field names explain what each value means  
✓ **Type Safe**: Prevents parameter order mistakes  
✓ **Flexible**: Easy to add optional parameters via Builder  
✓ **Maintainable**: Method signature won't change when adding fields  
✓ **Testable**: Can create test requests easily

---

## Comparing Before & After

### Before (Bad Design)
```java
public class PricingEngine {
    public double calc(double[] prices, int[] qtys, String customer, String code) {
        double sub = 0;
        for (int i = 0; i < prices.length; i++) {
            if (i < qtys.length) sub += prices[i] * qtys[i];
        }
        
        double disc = 0;
        if (customer.equals("VIP")) {
            disc = sub * 0.15;
        } else if (customer.equals("REGULAR")) {
            disc = sub * 0.0;
        }
        
        if (code != null && code.length() > 0) {
            if (code.equals("SAVE10")) {
                double codeDisc = sub * 0.10;
                if (codeDisc > disc) disc = codeDisc;
            }
            // ... more nested ifs
        }
        
        double afterDisc = sub - disc;
        double tax = afterDisc * 0.08;
        return afterDisc + tax;
    }
}
```

❌ **Problems:**
- One 60-line method doing everything
- Magic numbers everywhere
- Nested conditionals for discount logic
- Only returns final price (no breakdown)
- Adding new customer types = modify core logic
- Cannot print invoice
- Hard to test individual parts
- No error handling

### After (Clean Design)
```java
public class PricingEngine {
    public Invoice calculate(PricingRequest request) {
        double subtotal = computeSubtotal(request.getPrices(), 
                                          request.getQuantities());
        double discount = computeDiscount(subtotal, 
                                         request.getCustomerType(),
                                         request.getDiscountCode());
        double tax = (subtotal - discount) * PricingConstants.DEFAULT_TAX_RATE;
        double finalPrice = (subtotal - discount) + tax;
        
        return new Invoice(...);
    }
    
    private double computeSubtotal(double[] prices, int[] quantities) { ... }
    private double computeDiscount(double sub, String type, String code) { ... }
    private double getCodeDiscount(double sub, String code) { ... }
}

// Usage
PricingRequest request = new PricingRequest.Builder()
    .prices(prices)
    .quantities(quantities)
    .customerType("VIP")
    .discountCode("SAVE20")
    .build();

Invoice invoice = engine.calculate(request);
new InvoicePrinter().print(invoice);
```

✓ **Benefits:**
- Clear responsibility separation
- Easy to read and understand
- Named constants for configuration
- Strategy pattern for extensibility
- Full invoice breakdown available
- Fluent API with parameter object
- Proper error handling
- Easily testable components
- Adding new customer types = 1 new class + 1 factory line

---

## Design Patterns Used

| Pattern | Purpose | Benefit |
|---------|---------|---------|
| **Strategy** | Different discount algorithms | Add new types without modifying existing code |
| **Factory** | Create strategies centrally | Localize object creation logic |
| **Parameter Object** | Group related parameters | Clearer method signatures, flexibility |
| **Data Transfer Object (DTO)** | Hold calculation results | Rich return types, immutability |
| **Builder** | Fluent object creation | Flexible, readable API |

---

## SOLID Principles Applied

### S - Single Responsibility
- `DiscountStrategy`: Calculate discounts only
- `InvoicePrinter`: Format output only
- `PricingEngine`: Coordinate calculations only

### O - Open/Closed
- **Open for extension**: Add new `*DiscountStrategy` without changing existing code
- **Closed for modification**: Core `PricingEngine` unchanged when adding features

### L - Liskov Substitution
- Any `DiscountStrategy` implementation can be swapped without breaking code

### I - Interface Segregation
- `DiscountStrategy` has single method: `calculateDiscount()`
- Clients don't depend on unused methods

### D - Dependency Inversion
- `PricingEngine` depends on `DiscountStrategy` interface, not concrete implementations
- `DiscountStrategyFactory` abstracts strategy creation

---

## Testing

### Unit Tests
Each component can be tested independently:
```java
@Test
void testVipDiscount() {
    DiscountStrategy strategy = new VipDiscountStrategy();
    double discount = strategy.calculateDiscount(100.0);
    assertEquals(15.0, discount);
}

@Test
void testInvoicePrinter() {
    Invoice invoice = new Invoice(...);
    String output = new InvoicePrinter().format(invoice);
    assertTrue(output.contains("$"));
}
```

### Integration Tests
Full end-to-end tests with `PricingEngine`:
```java
@Test
void testVipCustomerWithSave20Code() {
    PricingRequest request = new PricingRequest.Builder()
        .prices(new double[]{100.0})
        .quantities(new int[]{1})
        .customerType("VIP")
        .discountCode("SAVE20")
        .build();
    
    Invoice invoice = engine.calculate(request);
    assertEquals(80.0, invoice.getSubtotal());
    assertEquals(20.0, invoice.getDiscountAmount()); // SAVE20 wins
    assertEquals(6.40, invoice.getTax(), 0.01);
    assertEquals(86.40, invoice.getFinalPrice(), 0.01);
}
```

---

## Future Enhancements

### Easy to Add (because of current design)
- [ ] New customer types (e.g., CORPORATE, WHOLESALE) → Just 1 new class
- [ ] Cumulative discounts (customer type + code) → Modify `computeDiscount()` only
- [ ] Multiple discount codes → Extend `PricingRequest`
- [ ] Invoice export formats (JSON, PDF, XML) → New `InvoicePrinter` implementations
- [ ] Tax calculation strategy → New strategy interface
- [ ] Discount code validation → New validator class

### Harder to Add (with bad design)
- Would require rewriting the monolithic `calc()` method
- Risk of breaking existing logic
- Nested conditionals become unmaintainable

---

## Conclusion

Through systematic refactoring using TDD and SOLID principles, we transformed:
- **Bad**: Monolithic, brittle, hard-to-test code
- **Good**: Modular, flexible, well-tested components

Each cycle added value:
1. **Constants** → Configurability
2. **Strategy** → Extensibility (Open/Closed)
3. **Separation** → Testability & Maintainability
4. **Parameter Object** → Clarity & Flexibility

The result is **production-ready**, **maintainable**, and **scalable** code.
