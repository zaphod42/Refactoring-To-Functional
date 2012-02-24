package org.bgprocess.refactoringtofunctional.handson.orderimport;

public class Price extends Equalable<Price> {

    private final double price;

    private Price(double price) {
        this.price = price;
    }

    public static Price of(double price) {
        return new Price(price);
    }

    @Override
    protected Object[] identificationParts() {
        return new Object[] { price };
    }

}
