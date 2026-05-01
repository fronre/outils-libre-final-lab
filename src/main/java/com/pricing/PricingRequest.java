package com.pricing;

/**
 * Parameter object that encapsulates all inputs for a pricing calculation.
 * This pattern reduces method complexity and improves maintainability.
 */
public class PricingRequest {
    private final double[] prices;
    private final int[] quantities;
    private final String customerType;
    private final String discountCode;
    
    private PricingRequest(Builder builder) {
        this.prices = builder.prices;
        this.quantities = builder.quantities;
        this.customerType = builder.customerType;
        this.discountCode = builder.discountCode;
    }
    
    // Getters
    public double[] getPrices() {
        return prices;
    }
    
    public int[] getQuantities() {
        return quantities;
    }
    
    public String getCustomerType() {
        return customerType;
    }
    
    public String getDiscountCode() {
        return discountCode;
    }
    
    @Override
    public String toString() {
        return "PricingRequest{" +
                "customerType='" + customerType + '\'' +
                ", discountCode='" + discountCode + '\'' +
                '}';
    }
    
    /**
     * Builder for PricingRequest.
     */
    public static class Builder {
        private double[] prices;
        private int[] quantities;
        private String customerType = PricingConstants.CUSTOMER_TYPE_REGULAR;
        private String discountCode = "";
        
        public Builder prices(double[] prices) {
            this.prices = prices;
            return this;
        }
        
        public Builder quantities(int[] quantities) {
            this.quantities = quantities;
            return this;
        }
        
        public Builder customerType(String customerType) {
            this.customerType = customerType;
            return this;
        }
        
        public Builder discountCode(String discountCode) {
            this.discountCode = discountCode != null ? discountCode : "";
            return this;
        }
        
        public PricingRequest build() {
            if (prices == null || quantities == null) {
                throw new IllegalArgumentException("Prices and quantities are required");
            }
            return new PricingRequest(this);
        }
    }
}
