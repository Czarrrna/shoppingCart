package org.example;

public class ShoppingCartService {
    public double calculateGrandTotal(ShoppingCart cart) {
        // implementation
        return 0;
    }

        public double calculateTotalPrice(ShoppingCart cart) {
            if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
                return 0.0;
            }

            return cart.getItems()
                    .stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();
        }

    public void validatePayload(ShoppingCart cart) throws InvalidCartException {
        // implementation
    }

}