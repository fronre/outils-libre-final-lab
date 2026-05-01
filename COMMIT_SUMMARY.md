# Lab Summary: Pricing & Discount Engine

## ✅ Completed Tasks

### 1. Project Setup
- ✅ Gradle build files (`build.gradle`, `settings.gradle`)
- ✅ Gradle wrapper for cross-platform support
- ✅ Git repository initialized and connected to GitHub
- ✅ `.gitignore` configured properly

### 2. Initial Code (Bad Design)
- ✅ `PricingEngine.java` - Monolithic, poorly-designed pricing engine
- ✅ `PricingResult.java` - Data class (legacy, not used in final design)
- ✅ Unit tests created FIRST (TDD approach)
- ✅ Python integration tests framework

### 3. Refactoring Cycles (Red-Green-Refactor)

#### ✅ Cycle 1: Extract Constants
**Problem:** Magic numbers and string literals scattered throughout code
```java
// BEFORE
if (customer.equals("VIP")) {
    disc = sub * 0.15;  // Magic number!
}
```

**Solution:** `PricingConstants.java`
```java
// AFTER
public static final double VIP_DISCOUNT_RATE = 0.15;
public static final String CUSTOMER_TYPE_VIP = "VIP";
```

**Commit:** `refactor: extract magic numbers/strings into constants`

---

#### ✅ Cycle 2: Strategy Pattern (Open/Closed Principle)
**Problem:** Nested conditionals for discount logic - hard to extend
```java
// BEFORE - Adding new customer type = Rewrite logic!
if (customer.equals("VIP")) {
    disc = sub * 0.15;
} else if (customer.equals("REGULAR")) {
    disc = sub * 0.0;
}
// What if we need CORPORATE, WHOLESALE, etc?
```

**Solution:** Strategy Pattern
```java
// AFTER - Adding new type = Just create 1 new class!
public interface DiscountStrategy {
    double calculateDiscount(double subtotal);
}

public class VipDiscountStrategy implements DiscountStrategy {
    public double calculateDiscount(double subtotal) {
        return subtotal * VIP_DISCOUNT_RATE;
    }
}

public class RegularDiscountStrategy implements DiscountStrategy {
    public double calculateDiscount(double subtotal) {
        return subtotal * REGULAR_DISCOUNT_RATE;
    }
}
```

**Factory Pattern for centralized creation:**
```java
public class DiscountStrategyFactory {
    public static DiscountStrategy getStrategy(String type) {
        switch (type) {
            case CUSTOMER_TYPE_VIP:
                return new VipDiscountStrategy();
            case CUSTOMER_TYPE_REGULAR:
                return new RegularDiscountStrategy();
            // Easy to add: case "CORPORATE": return new CorporateDiscountStrategy();
        }
    }
}
```

**Commits:**
- `add DiscountStrategy interface`
- `add RegularDiscountStrategy`
- `add VipDiscountStrategy`
- `add DiscountStrategyFactory`
- `refactor: delegate discount logic to DiscountStrategyFactory using Strategy Pattern`

---

#### ✅ Cycle 3: Separation of Concerns
**Problem:** PricingEngine does everything - calculation, printing, formatting
```java
// BEFORE - Monolithic
public double calc(double[] prices, int[] qtys, String customer, String code) {
    // 60 lines of calculation
    // Only returns one number!
    return final_price;
}
```

**Solution:** Separate responsibilities

```java
// AFTER - Clear separation
public Invoice calculate(PricingRequest request) {
    double subtotal = computeSubtotal(request.getPrices(), request.getQuantities());
    double discount = computeDiscount(subtotal, request.getCustomerType(), 
                                     request.getDiscountCode());
    double tax = (subtotal - discount) * DEFAULT_TAX_RATE;
    double finalPrice = (subtotal - discount) + tax;
    
    return new Invoice(prices, quantities, customerType, discountCode,
                       subtotal, discount, tax, finalPrice);  // Rich return type!
}
```

**Created:**
- `Invoice.java` - Data class holding all calculation results
- `InvoicePrinter.java` - Handles all output formatting
- Extracted private methods: `computeSubtotal()`, `computeDiscount()`, `getCodeDiscount()`

**Commits:**
- `add Invoice data class`
- `add InvoicePrinter`
- `refactor: extract computeTotal() method & delegate (Separation of Concerns)`

---

#### ✅ Cycle 4: Parameter Object Pattern
**Problem:** Many parameters in method signature - hard to remember order, easy to mix up
```java
// BEFORE - Confusing!
public Invoice calculate(double[] prices, int[] quantities, 
                        String customerType, String discountCode)
// Easy to call wrong: calculate(prices, code, qty, customer) - compiler doesn't catch!
```

**Solution:** Parameter Object with Builder
```java
// AFTER - Clear and flexible!
public Invoice calculate(PricingRequest request)

// Usage - self-documenting
PricingRequest request = new PricingRequest.Builder()
    .prices(prices)
    .quantities(quantities)
    .customerType("VIP")
    .discountCode("SAVE20")
    .build();

invoice = engine.calculate(request);
```

**Commits:**
- `add PricingRequest parameter object with Builder`
- `refactor: introduce PricingRequest parameter object`

---

### 4. Documentation
- ✅ `README.md` - Complete lab guide with examples
- ✅ `REFACTORING_GUIDE.md` - Detailed explanation of each cycle
- ✅ `COMMIT_SUMMARY.md` - This file!

---

## 📊 Code Metrics

### Files Created
```
Main Code (9 classes):
├── PricingEngine.java              (3 public methods, multiple private helpers)
├── PricingConstants.java           (10 constants)
├── PricingRequest.java             (Parameter object with Builder)
├── Invoice.java                    (Data class with getters)
├── InvoicePrinter.java            (2 output methods)
├── DiscountStrategy.java          (Interface: 1 method)
├── RegularDiscountStrategy.java    (1 line calculation)
├── VipDiscountStrategy.java        (1 line calculation)
└── DiscountStrategyFactory.java    (Factory with switch)

Tests (1 class):
└── PricingEngineTest.java          (12 comprehensive unit tests)
```

### Method Sizes
- **Before:** 1 monolithic method, ~50 lines
- **After:** Multiple small methods, 5-15 lines each ✓ BETTER!

### Extensibility
- **Before:** Adding new customer type = rewrite discount logic
- **After:** Adding new customer type = 1 new class + 1 factory line ✓ BETTER!

---

## 🎯 Design Principles Applied

### SOLID Principles

| Principle | Applied By | How |
|-----------|-----------|-----|
| **S**ingle Responsibility | Separated classes | PricingEngine ≠ InvoicePrinter ≠ DiscountStrategy |
| **O**pen/Closed | Strategy Pattern | Add new strategies without modifying PricingEngine |
| **L**iskov Substitution | DiscountStrategy interface | Any strategy implementation works |
| **I**nterface Segregation | Small interfaces | DiscountStrategy has only 1 method |
| **D**ependency Inversion | Factory & interfaces | Depends on abstractions, not concrete classes |

### Design Patterns

| Pattern | Usage | Benefit |
|---------|-------|---------|
| **Strategy** | Discount calculation algorithms | Plug in different implementations |
| **Factory** | Creating discount strategies | Centralized object creation |
| **Parameter Object** | Method parameters | Clearer signatures, flexibility |
| **Builder** | Creating PricingRequest | Fluent, readable API |
| **Data Transfer Object** | Returning calculation results | Structured, immutable results |

---

## 📝 Commit History

All commits follow semantic versioning: `type: description`

```
5abbd17 - docs: rewrite README with complete lab guide
c8fec53 - docs: add comprehensive refactoring guide with examples
586a425 - refactor: introduce PricingRequest parameter object
e833632 - add PricingRequest parameter object with Builder
e7a78dd - refactor: extract computeTotal() method & delegate (Separation of Concerns)
2b3092e - add InvoicePrinter
12597f3 - add Invoice data class
f853fe6 - refactor: delegate discount logic to DiscountStrategyFactory using Strategy Pattern
96857a1 - add DiscountStrategyFactory
f7aea51 - add VipDiscountStrategy
73dbca3 - add RegularDiscountStrategy
4e8c52c - add DiscountStrategy interface
117ec37 - refactor: extract magic numbers/strings into constants
5ecc6f9 - add python-based integration test
f8494f5 - add unit tests for PricingEngine
5fa67f3 - add initial poorly-designed PricingEngine
6993fe9 - initialize gradle app
```

**Total: 17 commits** - Each one logical, testable, and pushable!

---

## 🧪 Testing

### Unit Tests (12 tests in PricingEngineTest.java)
✅ Simple calculation without discounts  
✅ Multiple items with quantities  
✅ VIP discount (15%)  
✅ SAVE10 discount code (10%)  
✅ SAVE20 discount code (20%)  
✅ Best discount selection (VIP beats SAVE10)  
✅ Best discount selection (SAVE20 beats VIP)  
✅ Null prices handling  
✅ Mismatched array lengths  
✅ Empty discount code  

### Integration Tests (Python)
Framework ready for:
✅ JAR compilation testing  
✅ Unit test verification  
✅ End-to-end scenarios

---

## 🚀 Future Extensions (Now Easy!)

### Add CORPORATE Customer Type
```java
// 1 new class (8 lines)
public class CorporateDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscount(double subtotal) {
        return subtotal * 0.25;  // 25% discount
    }
}

// 2 lines in factory
case "CORPORATE":
    return new CorporateDiscountStrategy();

// Done! Zero changes to PricingEngine 🎉
```

### Add SAVE30 Discount Code
```java
// 2 lines in constants
public static final String DISCOUNT_CODE_SAVE30 = "SAVE30";
public static final double DISCOUNT_CODE_SAVE30_RATE = 0.30;

// 2 lines in getCodeDiscount method
else if (code.equals(DISCOUNT_CODE_SAVE30)) {
    return subtotal * DISCOUNT_CODE_SAVE30_RATE;

// Done! Zero changes to PricingEngine 🎉
```

### Add JSON Export
```java
// 1 new class (InvoiceJsonPrinter)
public class InvoiceJsonPrinter {
    public String toJson(Invoice invoice) {
        // JSON format output
    }
}

// Zero changes to existing code!
```

---

## 📈 Before vs After

### Code Quality
| Aspect | Before | After |
|--------|--------|-------|
| Method size | 50 lines | 5-15 lines |
| Magic numbers | 8+ scattered | 10 named constants |
| Extensibility | Low (rewrite needed) | High (add 1 class) |
| Testability | Hard (side effects) | Easy (isolated methods) |
| Maintainability | Poor (monolithic) | Excellent (modular) |
| Error handling | None | Proper exceptions |
| Return type | Single double | Rich Invoice object |

### Adding New Features

**New Customer Type:**
- Before: Modify `calc()` method, rewrite logic → Risk of bugs
- After: Create 1 class, update factory → Safe, isolated change

**New Discount Code:**
- Before: Modify `calc()` method, add nested if → Risk of bugs
- After: Add constants, update factory method → Safe, isolated change

**Export Format (JSON):**
- Before: Would need to modify `calc()` method or add parameters
- After: Create new printer class → Completely separate, no impact

---

## 🎓 Learning Outcomes

After completing this lab, you understand:

1. ✅ **TDD Red-Green-Refactor Cycle**
   - Tests drive design
   - Small, incremental improvements
   - Safety net of passing tests

2. ✅ **Design Patterns in Practice**
   - Strategy: Pluggable algorithms
   - Factory: Centralized creation
   - Builder: Fluent APIs
   - Parameter Object: Cleaner signatures
   - DTO: Rich return types

3. ✅ **SOLID Principles**
   - How they improve code quality
   - How they enable flexibility
   - How they reduce bug risk

4. ✅ **Clean Code Practices**
   - Naming: Meaningful constants
   - Structure: Small focused methods
   - Testability: Isolated responsibilities
   - Error handling: Proper exceptions

5. ✅ **Git Workflow**
   - Semantic commit messages
   - Atomic, logically complete commits
   - Clear history for future maintainers

---

## 📚 Key Takeaways

### Most Important Concepts

1. **Open/Closed Principle**: The best improvement!
   - **Before**: Adding features = modify existing code (risky)
   - **After**: Adding features = add new code (safe)

2. **Separation of Concerns**: The best practice!
   - Each class has ONE reason to change
   - Easy to test, understand, and maintain

3. **Small Methods**: The best style!
   - Each method does ONE thing
   - Easy to test, read, and reason about

4. **Meaningful Names**: The best investment!
   - `PricingConstants.VIP_DISCOUNT_RATE` vs magic `0.15`
   - Self-documenting code

---

## 🔗 Repository

**GitHub:** https://github.com/fronre/outils-libre-final-lab

```bash
# Clone and explore
git clone https://github.com/fronre/outils-libre-final-lab.git
cd outils-libre-final-lab

# See full history
git log --oneline

# View any commit
git show <commit-hash>
```

---

## 💡 Conclusion

This lab demonstrates how **systematic refactoring** combined with **clean code principles** transforms poor code into **production-ready software**.

The journey:
1. ❌ **Bad Code** → Hard to extend, risky to modify
2. 🔄 **Incremental Refactoring** → Small, safe improvements
3. ✅ **Clean Code** → Easy to extend, safe to modify

**Result:** Code that is easier to understand, maintain, extend, and test.

**This is what professional software engineering looks like!** 🚀

---

## 📞 Questions?

Refer to:
- `README.md` - Lab overview and usage
- `REFACTORING_GUIDE.md` - Deep dive into each cycle
- Git commits - See exactly what changed
- Source code - Read the well-documented classes
