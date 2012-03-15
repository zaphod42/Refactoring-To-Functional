package org.bgprocess.refactoringtofunctional.handson;

public class StockChange {
    private int amount;
    private double price;
    
    private StockChange(int amount, double price) {
        this.amount = amount;
        this.price = price;
    }
    
    public int amount() {
        return amount;
    }

    public double price() {
        return price;
    }

    public static StockChange of(int amount, double price) {
        return new StockChange(amount, price);
    }
    
    @Override
    public String toString() {
        return String.format("(%d @ %f)", amount(), price());
    }
}