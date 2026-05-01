package com.pricing;

/**
 * VIP customer discount strategy.
 * VIP customers get 15% discount.
 */
public class VipDiscountStrategy implements DiscountStrategy {
    
    @Override
    public double calculateDiscount(double subtotal) {
        return subtotal * PricingConstants.VIP_DISCOUNT_RATE;
    }
}
