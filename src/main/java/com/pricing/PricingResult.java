package com.pricing;

/**
 * Data class to hold pricing calculation results.
 * This will be useful after refactoring.
 */
public class PricingResult {
    private final double subtotal;
    private final double discountAmount;
    private final double tax;
    private final double finalPrice;
    
    public PricingResult(double subtotal, double discountAmount, double tax, double finalPrice) {
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.tax = tax;
        this.finalPrice = finalPrice;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public double getDiscountAmount() {
        return discountAmount;
    }
    
    public double getTax() {
        return tax;
    }
    
    public double getFinalPrice() {
        return finalPrice;
    }
    
    @Override
    public String toString() {
        return "PricingResult{" +
                "subtotal=" + subtotal +
                ", discountAmount=" + discountAmount +
                ", tax=" + tax +
                ", finalPrice=" + finalPrice +
                '}';
    }
}
