package com.pricing;

/**
 * Responsibility: Print invoices in human-readable format.
 * Separated from pricing logic for better maintainability and testability.
 */
public class InvoicePrinter {
    
    /**
     * Print a formatted invoice to the console.
     * 
     * @param invoice the invoice to print
     */
    public void print(Invoice invoice) {
        System.out.println("========================================");
        System.out.println("            PRICING INVOICE");
        System.out.println("========================================");
        System.out.println();
        
        System.out.printf("Customer Type:    %s%n", invoice.getCustomerType());
        if (invoice.getDiscountCode() != null && !invoice.getDiscountCode().isEmpty()) {
            System.out.printf("Discount Code:    %s%n", invoice.getDiscountCode());
        }
        System.out.println();
        
        System.out.println("----------------------------------------");
        System.out.printf("Subtotal:         $%.2f%n", invoice.getSubtotal());
        System.out.printf("Discount:        -$%.2f%n", invoice.getDiscountAmount());
        System.out.printf("Tax (8%%):         +$%.2f%n", invoice.getTax());
        System.out.println("----------------------------------------");
        System.out.printf("FINAL PRICE:      $%.2f%n", invoice.getFinalPrice());
        System.out.println("========================================");
        System.out.println();
    }
    
    /**
     * Format an invoice as a string.
     * 
     * @param invoice the invoice to format
     * @return formatted invoice string
     */
    public String format(Invoice invoice) {
        return String.format(
            "Subtotal: $%.2f | Discount: -$%.2f | Tax: +$%.2f | Final: $%.2f",
            invoice.getSubtotal(),
            invoice.getDiscountAmount(),
            invoice.getTax(),
            invoice.getFinalPrice()
        );
    }
}
