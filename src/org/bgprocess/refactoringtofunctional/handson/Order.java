package org.bgprocess.refactoringtofunctional.handson;

public class Order extends Equalable<Order> {
    public final Product product;
    public final Quantity quantity;
    public final Price price;

    public Order(Product product, Quantity quantity, Price price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    protected Object[] identificationParts() {
        return new Object[] { product, quantity, price };
    }
}
