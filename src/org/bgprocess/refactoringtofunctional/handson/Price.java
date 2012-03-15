package org.bgprocess.refactoringtofunctional.handson;

public class Price extends Equalable<Price> {

    public final double price;

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
