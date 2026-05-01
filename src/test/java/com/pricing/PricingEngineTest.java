package com.pricing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PricingEngine Tests (Initial Bad Design)")
class PricingEngineTest {
    
    private PricingEngine engine;
    
    @BeforeEach
    void setup() {
        engine = new PricingEngine();
    }
    
    @Test
    @DisplayName("Calculate simple purchase without discount")
    void testSimpleCalculation() {
        double[] prices = {10.0, 20.0};
        int[] quantities = {1, 1};
        
        double result = engine.calc(prices, quantities, "REGULAR", null);
        
        // Subtotal: 10 + 20 = 30
        // Discount: 0 (REGULAR customer, no code)
        // Tax: 30 * 0.08 = 2.40
        // Final: 30 + 2.40 = 32.40
        assertThat(result).isCloseTo(32.40, withTolerance(0.01));
    }
    
    @Test
    @DisplayName("Calculate with multiple items and quantities")
    void testMultipleItemsWithQuantities() {
        double[] prices = {10.0, 20.0, 30.0};
        int[] quantities = {1, 2, 1};
        
        double result = engine.calc(prices, quantities, "REGULAR", null);
        
        // Subtotal: (10*1) + (20*2) + (30*1) = 10 + 40 + 30 = 80
        // Discount: 0 (REGULAR)
        // Tax: 80 * 0.08 = 6.40
        // Final: 80 + 6.40 = 86.40
        assertThat(result).isCloseTo(86.40, withTolerance(0.01));
    }
    
    @Test
    @DisplayName("Apply VIP discount (15%)")
    void testVIPDiscount() {
        double[] prices = {100.0};
        int[] quantities = {1};
        
        double result = engine.calc(prices, quantities, "VIP", null);
        
        // Subtotal: 100
        // VIP Discount: 100 * 0.15 = 15
        // After Discount: 100 - 15 = 85
        // Tax: 85 * 0.08 = 6.80
        // Final: 85 + 6.80 = 91.80
        assertThat(result).isCloseTo(91.80, withTolerance(0.01));
    }
    
    @Test
    @DisplayName("Apply SAVE10 discount code")
    void testSave10DiscountCode() {
        double[] prices = {100.0};
        int[] quantities = {1};
        
        double result = engine.calc(prices, quantities, "REGULAR", "SAVE10");
        
        // Subtotal: 100
        // Discount: 100 * 0.10 = 10 (SAVE10 code)
        // Tax: 90 * 0.08 = 7.20
        // Final: 90 + 7.20 = 97.20
        assertThat(result).isCloseTo(97.20, withTolerance(0.01));
    }
    
    @Test
    @DisplayName("Apply SAVE20 discount code")
    void testSave20DiscountCode() {
        double[] prices = {100.0};
        int[] quantities = {1};
        
        double result = engine.calc(prices, quantities, "REGULAR", "SAVE20");
        
        // Subtotal: 100
        // Discount: 100 * 0.20 = 20 (SAVE20 code)
        // Tax: 80 * 0.08 = 6.40
        // Final: 80 + 6.40 = 86.40
        assertThat(result).isCloseTo(86.40, withTolerance(0.01));
    }
    
    @Test
    @DisplayName("Use best discount: VIP vs code (VIP should win if 15% > code)")
    void testBestDiscountLogic() {
        double[] prices = {100.0};
        int[] quantities = {1};
        
        // VIP gets 15%, SAVE10 gets 10% - VIP should be used
        double result = engine.calc(prices, quantities, "VIP", "SAVE10");
        
        // Subtotal: 100
        // Discount: max(15, 10) = 15
        // Tax: 85 * 0.08 = 6.80
        // Final: 85 + 6.80 = 91.80
        assertThat(result).isCloseTo(91.80, withTolerance(0.01));
    }
    
    @Test
    @DisplayName("Use best discount: SAVE20 > VIP")
    void testBestDiscountLogicCodeWins() {
        double[] prices = {100.0};
        int[] quantities = {1};
        
        // SAVE20 gets 20%, VIP gets 15% - SAVE20 should be used
        double result = engine.calc(prices, quantities, "VIP", "SAVE20");
        
        // Subtotal: 100
        // Discount: max(15, 20) = 20
        // Tax: 80 * 0.08 = 6.40
        // Final: 80 + 6.40 = 86.40
        assertThat(result).isCloseTo(86.40, withTolerance(0.01));
    }
    
    @Test
    @DisplayName("Handle null prices array gracefully")
    void testNullPrices() {
        int[] quantities = {1};
        
        double result = engine.calc(null, quantities, "REGULAR", null);
        
        // Should return error indicator (-1)
        assertThat(result).isEqualTo(-1);
    }
    
    @Test
    @DisplayName("Handle mismatched array lengths")
    void testMismatchedArrayLengths() {
        double[] prices = {10.0, 20.0, 30.0};
        int[] quantities = {1, 2}; // Shorter than prices
        
        double result = engine.calc(prices, quantities, "REGULAR", null);
        
        // Should handle gracefully (skip items without quantity)
        // Subtotal: (10*1) + (20*2) = 50
        // Tax: 50 * 0.08 = 4.00
        // Final: 54.00
        assertThat(result).isCloseTo(54.00, withTolerance(0.01));
    }
    
    @Test
    @DisplayName("Handle empty discount code")
    void testEmptyDiscountCode() {
        double[] prices = {100.0};
        int[] quantities = {1};
        
        double result = engine.calc(prices, quantities, "REGULAR", "");
        
        // Empty string should be treated as no discount
        // Tax: 100 * 0.08 = 8.00
        // Final: 108.00
        assertThat(result).isCloseTo(108.00, withTolerance(0.01));
    }
}
