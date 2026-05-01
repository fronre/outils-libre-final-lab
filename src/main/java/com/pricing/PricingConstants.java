package com.pricing;

/**
 * Constants used throughout the pricing engine.
 * Extracted to support easy configuration changes.
 */
public class PricingConstants {
    
    // Tax configuration
    public static final double DEFAULT_TAX_RATE = 0.08;
    
    // Customer types
    public static final String CUSTOMER_TYPE_REGULAR = "REGULAR";
    public static final String CUSTOMER_TYPE_VIP = "VIP";
    
    // Discount rates by customer type
    public static final double VIP_DISCOUNT_RATE = 0.15;
    public static final double REGULAR_DISCOUNT_RATE = 0.0;
    
    // Discount codes and their rates
    public static final String DISCOUNT_CODE_SAVE10 = "SAVE10";
    public static final double DISCOUNT_CODE_SAVE10_RATE = 0.10;
    
    public static final String DISCOUNT_CODE_SAVE20 = "SAVE20";
    public static final double DISCOUNT_CODE_SAVE20_RATE = 0.20;
    
    private PricingConstants() {
        // Utility class - prevent instantiation
    }
}
