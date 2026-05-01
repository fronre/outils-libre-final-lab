package com.pricing;

/**
 * Strategy interface for calculating discounts.
 * Allows different discount calculation strategies to be plugged in.
 */
public interface DiscountStrategy {
    
    /**
     * Calculate the discount amount based on the subtotal.
     * 
     * @param subtotal the subtotal before discount
     * @return the discount amount
     */
    double calculateDiscount(double subtotal);
}
