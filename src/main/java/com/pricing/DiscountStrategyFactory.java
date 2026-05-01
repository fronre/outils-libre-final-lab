package com.pricing;

/**
 * Factory for creating discount strategy instances.
 * Centralizes strategy selection logic.
 */
public class DiscountStrategyFactory {
    
    /**
     * Get the appropriate discount strategy for a customer type.
     * 
     * @param customerType the customer type (REGULAR, VIP, etc.)
     * @return the appropriate DiscountStrategy
     * @throws IllegalArgumentException if customer type is not recognized
     */
    public static DiscountStrategy getStrategy(String customerType) {
        if (customerType == null || customerType.isEmpty()) {
            return new RegularDiscountStrategy();
        }
        
        switch (customerType) {
            case PricingConstants.CUSTOMER_TYPE_REGULAR:
                return new RegularDiscountStrategy();
            case PricingConstants.CUSTOMER_TYPE_VIP:
                return new VipDiscountStrategy();
            default:
                throw new IllegalArgumentException("Unknown customer type: " + customerType);
        }
    }
}
