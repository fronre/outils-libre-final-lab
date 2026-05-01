package com.pricing;

/**
 * Data class representing a pricing invoice.
 * Holds all calculation results for a pricing operation.
 */
public class Invoice {
    private final double[] prices;
    private final int[] quantities;
    private final String customerType;
    private final String discountCode;
    private final double subtotal;
    private final double discountAmount;
    private final double tax;
    private final double finalPrice;
    
    public Invoice(double[] prices, int[] quantities, String customerType, String discountCode,
                   double subtotal, double discountAmount, double tax, double finalPrice) {
        this.prices = prices;
        this.quantities = quantities;
        this.customerType = customerType;
        this.discountCode = discountCode;
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.tax = tax;
        this.finalPrice = finalPrice;
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
        return "Invoice{" +
                "customerType='" + customerType + '\'' +
                ", subtotal=" + subtotal +
                ", discountAmount=" + discountAmount +
                ", tax=" + tax +
                ", finalPrice=" + finalPrice +
                '}';
    }
}
