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
    
    private InvoicePrinter invoicePrinter;
    
    public PricingEngine() {
        this.invoicePrinter = new InvoicePrinter();
    }
    
    /**
     * Calculate pricing and return detailed invoice.
     */
    public Invoice calculate(PricingRequest request) {
        // Validation
        if (request.getPrices() == null || request.getQuantities() == null) {
            throw new IllegalArgumentException("Prices and quantities cannot be null");
        }
        
        // Calculate subtotal
        double subtotal = computeSubtotal(request.getPrices(), request.getQuantities());
        
        // Calculate discount
        double discount = computeDiscount(subtotal, request.getCustomerType(), request.getDiscountCode());
        
        // Calculate tax and final price
        double afterDiscount = subtotal - discount;
        double tax = afterDiscount * PricingConstants.DEFAULT_TAX_RATE;
        double finalPrice = afterDiscount + tax;
        
        // Create and return invoice
        return new Invoice(request.getPrices(), request.getQuantities(), 
                           request.getCustomerType(), request.getDiscountCode(),
                           subtotal, discount, tax, finalPrice);
    }
    
    /**
     * Legacy method overload: calculate from individual parameters.
     * Uses PricingRequest internally.
     */
    public Invoice calculate(double[] prices, int[] quantities, String customerType, String discountCode) {
        PricingRequest request = new PricingRequest.Builder()
            .prices(prices)
            .quantities(quantities)
            .customerType(customerType)
            .discountCode(discountCode)
            .build();
        return calculate(request);
    }
    
    /**
     * Calculate subtotal from prices and quantities.
     */
    private double computeSubtotal(double[] prices, int[] quantities) {
        double subtotal = 0;
        for (int i = 0; i < prices.length; i++) {
            if (i < quantities.length) {
                subtotal += prices[i] * quantities[i];
            }
        }
        return subtotal;
    }
    
    /**
     * Calculate the best available discount.
     */
    private double computeDiscount(double subtotal, String customerType, String discountCode) {
        // Get customer type discount
        DiscountStrategy customerStrategy = DiscountStrategyFactory.getStrategy(customerType);
        double customerDiscount = customerStrategy.calculateDiscount(subtotal);
        double bestDiscount = customerDiscount;
        
        // Compare with code discount if provided
        if (discountCode != null && !discountCode.isEmpty()) {
            double codeDiscount = getCodeDiscount(subtotal, discountCode);
            bestDiscount = Math.max(bestDiscount, codeDiscount);
        }
        
        return bestDiscount;
    }
    
    /**
     * Get discount amount for a discount code.
     */
    private double getCodeDiscount(double subtotal, String code) {
        if (code.equals(PricingConstants.DISCOUNT_CODE_SAVE10)) {
            return subtotal * PricingConstants.DISCOUNT_CODE_SAVE10_RATE;
        } else if (code.equals(PricingConstants.DISCOUNT_CODE_SAVE20)) {
            return subtotal * PricingConstants.DISCOUNT_CODE_SAVE20_RATE;
        }
        return 0;
    }
    
    /**
     * Legacy method: calculate final price (keeps backward compatibility).
     */
    public double calc(double[] prices, int[] qtys, String customer, String code) {
        try {
            Invoice invoice = calculate(prices, qtys, customer, code);
            return invoice.getFinalPrice();
        } catch (IllegalArgumentException e) {
            return -1; // Error indicator
        }
    }
    
    public static void main(String[] args) {
        PricingEngine engine = new PricingEngine();
        InvoicePrinter printer = new InvoicePrinter();
        
        // Example usage
        double[] prices = {10.0, 20.0, 30.0};
        int[] quantities = {1, 2, 1};
        
        Invoice invoice = engine.calculate(prices, quantities, "VIP", "SAVE20");
        printer.print(invoice);
    }
}
