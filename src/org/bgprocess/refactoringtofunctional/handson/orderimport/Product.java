package org.bgprocess.refactoringtofunctional.handson.orderimport;

public class Product extends Equalable<Product> {
    private final String name;

    private Product(String string) {
        this.name = string;
    }

    public static Product of(String name) {
        return new Product(name);
    }

    @Override
    protected Object[] identificationParts() {
        return new Object[] { name };
    }
}
