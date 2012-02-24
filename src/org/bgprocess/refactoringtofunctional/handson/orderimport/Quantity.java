package org.bgprocess.refactoringtofunctional.handson.orderimport;

public class Quantity extends Equalable<Quantity> {

    private final int number;

    private Quantity(int number) {
        this.number = number;
    }

    public static Quantity of(int number) {
        return new Quantity(number);
    }

    @Override
    protected Object[] identificationParts() {
        return new Object[] { number };
    }
}
