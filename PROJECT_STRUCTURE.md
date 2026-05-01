# 📚 Project Structure Guide

## 📁 Directory Layout

```
outils-libre-final-lab/
│
├── 📄 README.md                  ← START HERE! Complete lab guide
├── 📄 REFACTORING_GUIDE.md       ← Deep dive: Each refactoring cycle
├── 📄 COMMIT_SUMMARY.md          ← This file: What was built & why
├── 📄 PROJECT_STRUCTURE.md       ← Navigation guide (this file)
│
├── build.gradle                  ← Gradle build configuration
├── settings.gradle               ← Gradle project settings
├── gradle/                       ← Gradle wrapper files
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew & gradlew.bat         ← Gradle wrapper scripts
│
├── build.bat & build.sh          ← Simple build scripts
│
├── src/
│   ├── main/java/com/pricing/    ← MAIN SOURCE CODE
│   │   ├── PricingEngine.java              (Core orchestrator)
│   │   ├── PricingConstants.java           (Configuration)
│   │   ├── PricingRequest.java             (Parameter object)
│   │   ├── Invoice.java                    (Data class)
│   │   ├── InvoicePrinter.java            (Output formatting)
│   │   ├── DiscountStrategy.java           (Strategy interface)
│   │   ├── RegularDiscountStrategy.java    (Implementation)
│   │   ├── VipDiscountStrategy.java        (Implementation)
│   │   ├── DiscountStrategyFactory.java    (Factory pattern)
│   │   └── PricingResult.java              (Legacy - not used)
│   │
│   └── test/java/com/pricing/   ← TESTS
│       └── PricingEngineTest.java          (12 unit tests)
│
└── .gitignore                    ← Git ignore configuration
```

---

## 🎯 Quick Navigation

### 🔴 For Getting Started
**Read in this order:**
1. `README.md` - Overview and usage examples
2. `REFACTORING_GUIDE.md` - See each refactoring cycle
3. `src/main/java/com/pricing/PricingEngine.java` - Core logic

### 🟢 For Understanding Design Decisions
**Read these files:**
1. `COMMIT_SUMMARY.md` - "Before vs After" comparisons
2. `REFACTORING_GUIDE.md` - Why each pattern was used
3. Git log - `git log --oneline` to see commit history

### 🔵 For Code Review
**Check these files:**
1. All classes in `src/main/java/com/pricing/` (9 classes)
2. `PricingEngineTest.java` - Test coverage
3. Run: `git show <commit-hash>` for specific changes

---

## 📖 File Descriptions

### Main Source Files (9 classes)

#### `PricingEngine.java` ⭐
**The Core Orchestrator**
- Public method: `calculate(PricingRequest)` → Returns `Invoice`
- Public method: `calc(arrays, customer, code)` → Returns double (legacy support)
- Private methods: `computeSubtotal()`, `computeDiscount()`, `getCodeDiscount()`

**Key Improvements:**
- Delegates discount logic to strategies (Strategy Pattern)
- Delegates printing to InvoicePrinter (Separation of Concerns)
- Returns rich Invoice object instead of single double
- Proper error handling with exceptions

**Refactoring Cycles:** 3, 4

---

#### `PricingConstants.java`
**Configuration Hub**
- All magic numbers replaced with named constants
- 10 constants: tax rates, customer types, discount codes

**Values:**
```java
DEFAULT_TAX_RATE = 0.08
VIP_DISCOUNT_RATE = 0.15
REGULAR_DISCOUNT_RATE = 0.0
DISCOUNT_CODE_SAVE10_RATE = 0.10
DISCOUNT_CODE_SAVE20_RATE = 0.20
// ... and more
```

**Why:** Single source of truth - change tax rate in one place!

**Refactoring Cycles:** 1

---

#### `PricingRequest.java`
**Parameter Object with Builder**
- Encapsulates all pricing inputs
- Uses Builder Pattern for fluent API
- Type-safe, self-documenting

**Usage:**
```java
PricingRequest request = new PricingRequest.Builder()
    .prices(new double[]{100.0})
    .quantities(new int[]{1})
    .customerType("VIP")
    .discountCode("SAVE20")
    .build();
```

**Why:** Cleaner method signatures, easier to extend

**Refactoring Cycles:** 4

---

#### `Invoice.java`
**Data Transfer Object (DTO)**
- Immutable data class
- Holds complete calculation results
- Enables rich return types from methods

**Properties:**
- subtotal, discountAmount, tax, finalPrice
- prices[], quantities[], customerType, discountCode

**Why:** Return structured data instead of single numbers

**Refactoring Cycles:** 3

---

#### `InvoicePrinter.java`
**Output Formatting**
- Separated from calculation logic
- Two methods:
  - `print(Invoice)` - Print to console
  - `format(Invoice)` - Return formatted string

**Why:** Separation of Concerns - easy to add JSON/PDF printers

**Refactoring Cycles:** 3

---

#### `DiscountStrategy.java`
**Strategy Pattern Interface**
- Single method: `calculateDiscount(double subtotal) → double`
- Enables pluggable discount algorithms
- Foundation for extensibility

**Why:** Add new customer types without modifying existing code

**Refactoring Cycles:** 2

---

#### `RegularDiscountStrategy.java`
**Regular Customer Discount**
- Implements DiscountStrategy
- Returns 0% discount

**Line Count:** 13 lines (including javadoc)

**Why:** Concrete implementation of Strategy

**Refactoring Cycles:** 2

---

#### `VipDiscountStrategy.java`
**VIP Customer Discount**
- Implements DiscountStrategy
- Returns 15% discount

**Line Count:** 13 lines (including javadoc)

**Why:** Concrete implementation of Strategy

**Refactoring Cycles:** 2

---

#### `DiscountStrategyFactory.java`
**Factory Pattern**
- Static method: `getStrategy(String customerType) → DiscountStrategy`
- Centralizes strategy selection
- Easy to extend with new types

**Usage:**
```java
DiscountStrategy strategy = DiscountStrategyFactory.getStrategy("VIP");
double discount = strategy.calculateDiscount(100.0); // Returns 15.0
```

**Why:** Localize object creation - add new type = 1 factory case

**Refactoring Cycles:** 2

---

#### `PricingResult.java`
**Legacy Data Class**
- Originally designed as return type
- No longer used (replaced by Invoice)
- Kept for backward compatibility

**Status:** Not in use - can be removed in future

---

### Test Files

#### `PricingEngineTest.java`
**Unit Tests (12 tests)**

Tests cover:
```
✅ Simple calculation (REGULAR customer, no code)
✅ Multiple items with quantities
✅ VIP customer (15% discount)
✅ SAVE10 code (10% discount)
✅ SAVE20 code (20% discount)
✅ Best discount: VIP 15% > SAVE10 10%
✅ Best discount: SAVE20 20% > VIP 15%
✅ Error: Null prices
✅ Error: Mismatched array lengths
✅ Edge case: Empty discount code
✅ Proper invoice breakdown
✅ Field-by-field assertions (subtotal, discount, tax, final)
```

**Test Framework:** JUnit 5 (Jupiter)
**Assertions:** AssertJ for fluent syntax

**Why:** TDD - Tests written first, then code

**Refactoring Cycles:** Foundation (written before refactoring)

---

### Documentation Files

#### `README.md` (400+ lines)
**Main Lab Guide**
- Overview and objectives
- Project structure
- Usage examples
- Design patterns explained
- Future extensions
- Learning outcomes

**Start here for:** Quick understanding of the project

---

#### `REFACTORING_GUIDE.md` (450+ lines)
**Deep Dive into Each Cycle**
- Cycle 1: Extract Constants
- Cycle 2: Strategy Pattern
- Cycle 3: Separation of Concerns
- Cycle 4: Parameter Object
- Before/After comparisons
- Benefits for each cycle

**Read this for:** Understanding why each change was made

---

#### `COMMIT_SUMMARY.md` (450+ lines)
**What Was Built & Why**
- Completed tasks checklist
- Code metrics (before/after)
- Design principles applied
- Complete commit history
- Testing coverage
- Future extensions (now easy!)
- Learning outcomes

**Read this for:** Comprehensive overview of improvements

---

### Build & Configuration

#### `build.gradle` (30 lines)
**Gradle Build Configuration**
- Dependencies: JUnit 5, AssertJ
- Java compatibility: 11+
- Task configuration for tests

**Key Tasks:**
- `gradle clean build`
- `gradle test`
- `gradle run`

---

#### `settings.gradle` (1 line)
**Project Name Configuration**
```gradle
rootProject.name = 'pricing-discount-engine'
```

---

#### `gradlew` & `gradlew.bat`
**Gradle Wrapper Scripts**
- Cross-platform support (Windows, Linux, Mac)
- Don't need Gradle installed
- Usage: `./gradlew build` or `gradlew.bat build`

---

#### `.gitignore` (25 lines)
**Git Ignore Rules**
- Gradle build artifacts (`build/`, `.gradle/`)
- Java compilation files (`*.class`, `*.jar`)
- IDE files (`.idea/`, `.vscode/`)
- OS files (`.DS_Store`, `Thumbs.db`)
- Python cache (`__pycache__/`, `*.pyc`)

---

## 🔄 Refactoring Flow

### Phase 1: Setup
```
initialize gradle app
    ↓
add initial poorly-designed PricingEngine
    ↓
add unit tests for PricingEngine
    ↓
add python-based integration test
```

### Phase 2: Refactoring Cycles

#### Cycle 1: Constants
```
refactor: extract magic numbers/strings into constants
    ↓
PricingConstants.java created
    ↓
Tests still pass ✓
```

#### Cycle 2: Strategy Pattern
```
add DiscountStrategy interface
    ↓
add RegularDiscountStrategy
    ↓
add VipDiscountStrategy
    ↓
add DiscountStrategyFactory
    ↓
refactor: delegate discount logic to DiscountStrategyFactory
    ↓
Tests still pass ✓
```

#### Cycle 3: Separation of Concerns
```
add Invoice data class
    ↓
add InvoicePrinter
    ↓
refactor: extract computeTotal() & delegate
    ↓
Return Invoice instead of double
    ↓
Tests still pass ✓
```

#### Cycle 4: Parameter Object
```
add PricingRequest with Builder
    ↓
refactor: introduce PricingRequest parameter object
    ↓
Simpler method signatures
    ↓
Tests still pass ✓
```

### Phase 3: Documentation
```
docs: add comprehensive refactoring guide
    ↓
docs: rewrite README with complete lab guide
    ↓
docs: add detailed commit summary
```

---

## 🎯 How to Use This Project

### 1. Understand the Architecture
```bash
# Read documentation first
cat README.md
cat REFACTORING_GUIDE.md

# Explore the main code
cat src/main/java/com/pricing/PricingEngine.java
```

### 2. Review Commits
```bash
# See full history
git log --oneline

# See specific change
git show 117ec37    # Constants extraction
git show f853fe6    # Strategy pattern

# See file evolution
git log -p src/main/java/com/pricing/PricingEngine.java
```

### 3. Build & Test
```bash
# Compile
javac -d build/classes src/main/java/com/pricing/*.java

# Run tests (if Gradle available)
./gradlew test
```

### 4. Extend the Code
```
Add new customer type:
1. Create NewTypeDiscountStrategy.java
2. Update DiscountStrategyFactory.java
3. Done! Zero changes to PricingEngine! ✓

Add new discount code:
1. Add constant to PricingConstants.java
2. Update getCodeDiscount() in PricingEngine.java
3. Done!
```

---

## 📊 Summary Statistics

| Metric | Count |
|--------|-------|
| **Total Commits** | 19 |
| **Main Classes** | 9 |
| **Test Classes** | 1 |
| **Unit Tests** | 12 |
| **Documentation Files** | 4 |
| **Refactoring Cycles** | 4 |
| **Design Patterns** | 5 |
| **SOLID Principles Applied** | 5 |
| **Lines of Code (Main)** | ~400 |
| **Lines of Code (Tests)** | ~200 |

---

## 🚀 Quick Start

1. **Read:** `README.md`
2. **Explore:** `src/main/java/com/pricing/PricingEngine.java`
3. **Understand:** `REFACTORING_GUIDE.md`
4. **Review:** `git log --oneline`
5. **Extend:** Add your own customer type or discount code!

---

## 📞 Questions?

- **What was changed?** → Check `git log`
- **Why was it changed?** → Read `REFACTORING_GUIDE.md`
- **How does it work?** → Check `README.md` + source code
- **How to extend?** → See "Future Extensions" in `README.md`

**Remember:** Good code tells a story through commits, tests, and documentation! 📖

