package org.bgprocess.refactoringtofunctional.handson;

public class Quantity extends Equalable<Quantity> {

    public final int number;

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
