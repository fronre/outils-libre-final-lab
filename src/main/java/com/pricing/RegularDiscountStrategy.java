package com.pricing;

/**
 * Regular customer discount strategy.
 * Regular customers don't get a discount.
 */
public class RegularDiscountStrategy implements DiscountStrategy {
    
    @Override
    public double calculateDiscount(double subtotal) {
        return subtotal * PricingConstants.REGULAR_DISCOUNT_RATE;
    }
}
