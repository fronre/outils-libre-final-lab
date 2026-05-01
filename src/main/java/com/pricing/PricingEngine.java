package com.pricing;

import java.util.HashMap;
import java.util.Map;

/**
 * POORLY DESIGNED PRICING ENGINE - BAD CODE FOR REFACTORING LAB
 * This class intentionally contains design flaws for educational purposes.
 * Problems to identify and fix during refactoring:
 * - Monolithic method with too many responsibilities
 * - Hard-coded logic and magic numbers
 * - Poor separation of concerns
 * - No validation of inputs
 * - Hard to test individual components
 * - Inconsistent naming conventions
 */
public class PricingEngine {
    
    // All logic crammed into one method
    public double calc(double[] prices, int[] qtys, String customer, String code) {
        // Mixing all logic together
        double sub = 0;
        
        // No validation
        if (prices == null || qtys == null) {
            return -1; // Bad error handling
        }
        
        // Calculate subtotal (poorly done)
        for (int i = 0; i < prices.length; i++) {
            if (i < qtys.length) {
                sub += prices[i] * qtys[i];
            }
        }
        
        // Handle discount (monolithic logic)
        double disc = 0;
        if (customer.equals(PricingConstants.CUSTOMER_TYPE_VIP)) {
            disc = sub * PricingConstants.VIP_DISCOUNT_RATE;
        } else if (customer.equals(PricingConstants.CUSTOMER_TYPE_REGULAR)) {
            disc = sub * PricingConstants.REGULAR_DISCOUNT_RATE;
        }
        
        // Apply code discount (hard-coded)
        if (code != null && code.length() > 0) {
            if (code.equals(PricingConstants.DISCOUNT_CODE_SAVE10)) {
                double codeDisc = sub * PricingConstants.DISCOUNT_CODE_SAVE10_RATE;
                if (codeDisc > disc) {
                    disc = codeDisc;
                }
            } else if (code.equals(PricingConstants.DISCOUNT_CODE_SAVE20)) {
                double codeDisc = sub * PricingConstants.DISCOUNT_CODE_SAVE20_RATE;
                if (codeDisc > disc) {
                    disc = codeDisc;
                }
            }
        }
        
        // Calculate tax and final price
        double afterDisc = sub - disc;
        double tax = afterDisc * PricingConstants.DEFAULT_TAX_RATE;
        double final_price = afterDisc + tax;
        
        // Returning just one value - how will we get individual breakdown?
        return final_price;
    }
    
    public static void main(String[] args) {
        PricingEngine pe = new PricingEngine();
        
        // Example usage
        double[] prices = {10.0, 20.0, 30.0};
        int[] quantities = {1, 2, 1};
        
        double result = pe.calc(prices, quantities, "VIP", "SAVE20");
        System.out.println("Final Price: $" + result);
    }
}
