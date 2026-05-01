# 🚀 Pricing & Discount Engine Lab - COMPLETE

## ✅ Project Successfully Completed!

This is a **production-ready refactoring lab** demonstrating how to transform poorly-designed code into clean, maintainable, extensible software.

---

## 📚 Getting Started (5-Minute Quick Start)

### 1. **Understand the Problem** (2 min)
Read: [`README.md`](README.md) - Complete overview and examples

### 2. **See the Solution** (2 min)
Check: `src/main/java/com/pricing/PricingEngine.java` - Final cleaned code

### 3. **Learn How It Happened** (1 min)
Explore: [`REFACTORING_GUIDE.md`](REFACTORING_GUIDE.md) - Each cycle explained

---

## 📖 Documentation (Choose Your Path)

### 🟢 **Path 1: I'm New - Show Me Everything**
1. ✅ [`README.md`](README.md) - Start here! 15-min read
2. ✅ [`REFACTORING_GUIDE.md`](REFACTORING_GUIDE.md) - See each refactoring cycle 20-min read
3. ✅ Explore source code in `src/main/java/com/pricing/`
4. ✅ Run: `javac -d build/classes src/main/java/com/pricing/*.java`

### 🔵 **Path 2: I'm a Reviewer - Give Me Details**
1. ✅ [`COMMIT_SUMMARY.md`](COMMIT_SUMMARY.md) - Before/After analysis 15-min read
2. ✅ `git log --oneline` - See all 20 commits
3. ✅ `git show <hash>` - Review specific changes
4. ✅ [`PROJECT_STRUCTURE.md`](PROJECT_STRUCTURE.md) - File descriptions 10-min read

### 🔴 **Path 3: I Want Code - Show Me Files**
1. ✅ [`PROJECT_STRUCTURE.md`](PROJECT_STRUCTURE.md) - File navigation guide
2. ✅ `src/main/java/com/pricing/*.java` - 9 clean classes
3. ✅ `src/test/java/com/pricing/PricingEngineTest.java` - 12 unit tests
4. ✅ `build.gradle` - Build configuration

---

## 🎯 What Was Built

### The Problem
```java
// BEFORE: Monolithic, 60-line method
public double calc(double[] prices, int[] qtys, String customer, String code) {
    // Everything mixed together:
    // - Calculate subtotal
    // - Calculate discount (hard-coded logic)
    // - Calculate tax
    // - Return single number (no breakdown!)
}
```

### The Solution
```java
// AFTER: Clean, focused responsibilities
public Invoice calculate(PricingRequest request) {
    double subtotal = computeSubtotal(...);        // Subtotal
    double discount = computeDiscount(...);         // Best discount
    double tax = (subtotal - discount) * TAX_RATE; // Tax
    double finalPrice = (subtotal - discount) + tax; // Final
    
    return new Invoice(...);  // Rich return type!
}

// Usage
PricingRequest request = new PricingRequest.Builder()
    .prices(prices).quantities(quantities)
    .customerType("VIP").discountCode("SAVE20")
    .build();

Invoice invoice = engine.calculate(request);
new InvoicePrinter().print(invoice);  // Pretty output
```

---

## 📊 By The Numbers

| Metric | Value |
|--------|-------|
| **Total Commits** | 20 commits (each atomic & pushable) |
| **Refactoring Cycles** | 4 complete Red-Green-Refactor cycles |
| **Main Classes** | 9 clean, focused classes |
| **Unit Tests** | 12 comprehensive tests |
| **Documentation** | 4 detailed guides (52+ KB) |
| **Design Patterns** | 5 (Strategy, Factory, Builder, DTO, Parameter Object) |
| **SOLID Principles** | All 5 applied |
| **Repository** | https://github.com/fronre/outils-libre-final-lab |

---

## 🏆 Key Achievements

### ✅ Code Quality
- ✓ Extract constants → Named values instead of magic numbers
- ✓ Strategy pattern → Extensible without modifying core logic
- ✓ Separation of concerns → Each class has ONE responsibility
- ✓ Parameter object → Cleaner method signatures
- ✓ Proper error handling → Exceptions instead of return codes

### ✅ Architecture
- ✓ Open/Closed Principle → Easy to extend, hard to break
- ✓ Single Responsibility → Each class has one reason to change
- ✓ Dependency Inversion → Depends on abstractions, not implementations
- ✓ Small focused methods → Easy to test and understand
- ✓ Meaningful naming → Self-documenting code

### ✅ Extensibility
Adding new features is now **trivial**:

**Before:** Add new customer type = Rewrite core `calc()` method (risky!)

**After:** Add new customer type = Create 1 class + 1 factory line (safe!)

```java
// Add CORPORATE customer type in 3 steps:

// Step 1: Create strategy class
public class CorporateDiscountStrategy implements DiscountStrategy {
    public double calculateDiscount(double subtotal) {
        return subtotal * 0.25;  // 25% discount
    }
}

// Step 2: Update factory
case "CORPORATE": return new CorporateDiscountStrategy();

// Step 3: Done! Zero changes to PricingEngine! 🎉
```

---

## 🔄 Refactoring Journey

### Cycle 1: Extract Constants
```
✓ Replaced 8+ magic numbers with PricingConstants
✓ Single source of truth for configuration
✓ Tests still pass!
```

### Cycle 2: Strategy Pattern
```
✓ Created DiscountStrategy interface
✓ Implemented 2 strategies (Regular, VIP)
✓ Added DiscountStrategyFactory
✓ Replaced nested conditionals with polymorphism
✓ Tests still pass! ✓ Adding new type is now safe!
```

### Cycle 3: Separation of Concerns
```
✓ Created Invoice data class
✓ Created InvoicePrinter for output
✓ Extracted compute methods
✓ Return rich Invoice instead of single double
✓ Tests still pass! ✓ Output formatting is now separate!
```

### Cycle 4: Parameter Object
```
✓ Created PricingRequest with Builder
✓ Simplified method signatures
✓ Fluent, self-documenting API
✓ Tests still pass! ✓ Adding parameters is now flexible!
```

---

## 📝 Commit History

All 20 commits are atomic and well-described:

```
3ae7da2 - docs: add comprehensive project structure guide
c1a62a3 - docs: add detailed commit summary and learning outcomes
5abbd17 - docs: rewrite README with complete lab guide
c8fec53 - docs: add comprehensive refactoring guide with examples
586a425 - refactor: introduce PricingRequest parameter object
e833632 - add PricingRequest parameter object with Builder
e7a78dd - refactor: extract computeTotal() & delegate (Separation of Concerns)
2b3092e - add InvoicePrinter
12597f3 - add Invoice data class
f853fe6 - refactor: delegate discount logic to DiscountStrategyFactory
96857a1 - add DiscountStrategyFactory
f7aea51 - add VipDiscountStrategy
73dbca3 - add RegularDiscountStrategy
4e8c52c - add DiscountStrategy interface
117ec37 - refactor: extract magic numbers/strings into constants
5ecc6f9 - add python-based integration test
f8494f5 - add unit tests for PricingEngine
5fa67f3 - add initial poorly-designed PricingEngine
6993fe9 - initialize gradle app
6ba70b4 - Create README.md (initial)
```

**Each commit is:**
- ✓ Atomic (one logical change)
- ✓ Testable (tests pass after each commit)
- ✓ Pushable (can be pushed individually)
- ✓ Reversible (can be reverted if needed)

---

## 📁 Project Files

**Documentation (Start Here):**
- 📖 [`README.md`](README.md) - Complete lab guide (12 KB)
- 📖 [`REFACTORING_GUIDE.md`](REFACTORING_GUIDE.md) - Cycle explanations (13 KB)
- 📖 [`COMMIT_SUMMARY.md`](COMMIT_SUMMARY.md) - Before/After analysis (13 KB)
- 📖 [`PROJECT_STRUCTURE.md`](PROJECT_STRUCTURE.md) - File guide (12 KB)

**Source Code (Main):**
- 🟢 `PricingEngine.java` - Core orchestrator
- 🔵 `PricingConstants.java` - Configuration
- 🟡 `PricingRequest.java` - Parameter object
- 🟠 `Invoice.java` - Data class
- 🟣 `InvoicePrinter.java` - Output formatting
- ⚫ `DiscountStrategy.java` - Interface
- ⚪ `RegularDiscountStrategy.java` - Implementation
- ⚪ `VipDiscountStrategy.java` - Implementation
- ⚫ `DiscountStrategyFactory.java` - Factory

**Tests:**
- 🧪 `PricingEngineTest.java` - 12 comprehensive unit tests

**Build:**
- 🔧 `build.gradle` - Dependencies & tasks
- 🔧 `settings.gradle` - Project config
- 🔧 `gradlew` & `gradlew.bat` - Gradle wrapper

---

## 🎓 Learning Outcomes

After studying this project, you'll understand:

1. ✅ **Test-Driven Development (TDD)**
   - Red-Green-Refactor cycle
   - Tests as a safety net
   - Incremental improvements

2. ✅ **Design Patterns**
   - Strategy (pluggable algorithms)
   - Factory (centralized creation)
   - Builder (fluent APIs)
   - Parameter Object (cleaner signatures)
   - DTO (structured data)

3. ✅ **SOLID Principles**
   - **S**: Single Responsibility
   - **O**: Open/Closed (extend without modify)
   - **L**: Liskov Substitution (swap implementations)
   - **I**: Interface Segregation (focused contracts)
   - **D**: Dependency Inversion (depend on abstractions)

4. ✅ **Clean Code**
   - Naming: Meaningful identifiers
   - Structure: Small focused methods
   - Testability: Isolated responsibilities
   - Error handling: Proper exceptions
   - Documentation: Self-explanatory code

5. ✅ **Git Workflow**
   - Atomic commits
   - Semantic messages
   - Clear history
   - Clean log

---

## 🚀 Next Steps

### To Understand This Project
1. Read `README.md` (15 min)
2. Read `REFACTORING_GUIDE.md` (20 min)
3. Explore source code (15 min)
4. Run `git log --oneline` (2 min)

### To Extend This Project
Add new customer type:
```java
// 1. Create: CorporateDiscountStrategy.java
// 2. Update: DiscountStrategyFactory.getStrategy()
// Done! Zero changes to PricingEngine!
```

Add new discount code:
```java
// 1. Update: PricingConstants.java
// 2. Update: PricingEngine.getCodeDiscount()
// Done!
```

### To Use This as a Template
1. Clone the repo
2. Adapt PricingEngine for your domain
3. Create new strategies for your logic
4. Extend with new patterns as needed

---

## 💡 Key Insights

### Most Important
> **The Open/Closed Principle is your best friend!**
>
> Your code should be:
> - ✓ **Open for extension**: Easy to add new features
> - ✓ **Closed for modification**: Don't need to change existing code
>
> This is what Strategy Pattern and Factory achieve!

### Most Practical
> **Small focused methods are worth their weight in gold!**
>
> Methods that do one thing:
> - ✓ Are easy to test
> - ✓ Are easy to understand
> - ✓ Are easy to reuse
> - ✓ Are easy to debug

### Most Valuable
> **Good code tells a story through commits, tests, and documentation!**
>
> Each commit shows:
> - What changed
> - Why it changed
> - That tests still pass

---

## 🔗 Resources

**Repository:** https://github.com/fronre/outils-libre-final-lab

**Technologies Used:**
- Java 11+
- JUnit 5 (Jupiter)
- AssertJ (assertions)
- Gradle (build system)
- Git (version control)

**Principles & Patterns:**
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)
- [Design Patterns](https://en.wikipedia.org/wiki/Design_Patterns)
- [Clean Code](https://en.wikipedia.org/wiki/The_Clean_Coder)
- [TDD](https://en.wikipedia.org/wiki/Test-driven_development)

---

## ✨ Summary

This lab demonstrates:

1. **How to refactor** poorly-designed code systematically
2. **When to use** classic design patterns effectively
3. **Why SOLID** principles matter in practice
4. **How to write** clean, maintainable, extensible code
5. **How important** tests and documentation are

The result is **professional-grade software** that is:
- ✓ Easy to understand
- ✓ Easy to test
- ✓ Easy to extend
- ✓ Easy to maintain
- ✓ Production-ready

---

## 🎉 Congratulations!

You have a **complete, well-documented refactoring lab** with:
- ✅ 20 atomic commits
- ✅ 4 refactoring cycles
- ✅ 9 clean classes
- ✅ 12 unit tests
- ✅ 4 comprehensive guides
- ✅ Clear GitHub history

**This is what professional software engineering looks like!** 🚀

---

**Ready to dive in?** Start with [`README.md`](README.md) →
