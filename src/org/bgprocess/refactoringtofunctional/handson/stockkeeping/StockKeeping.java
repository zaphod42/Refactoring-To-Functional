package org.bgprocess.refactoringtofunctional.handson.stockkeeping;

import java.util.Deque;
import java.util.List;

import com.google.common.collect.Lists;

public class StockKeeping {
    private final Iterable<StockChange> changes;

    public StockKeeping(Iterable<StockChange> changes) {
        this.changes = changes;
    }
    
    public int costOf(int itemCount) {
        Deque<StockChange> increases = Lists.newLinkedList();
        List<StockChange> decreases = Lists.newArrayList();
        for (StockChange change : changes) {
            if (change.amount >= 0) {
                increases.add(change);
            } else {
                decreases.add(change);
            }
        }
        
        for (StockChange decrease : decreases) {
            int amountRemaining = decrease.amount;
            while (amountRemaining < 0) {
                StockChange increase = increases.pop();
                amountRemaining += increase.amount;
                if (amountRemaining > 0) {
                    increases.offerFirst(StockChange.changeOf(amountRemaining, increase.price));
                }
            }
        }
        
        int totalCost = 0;
        int numberRemaining = itemCount;
        for (StockChange change : increases) {
            if (numberRemaining > change.amount) { 
                totalCost += change.price * change.amount;
                numberRemaining -= change.amount;
            } else {
                totalCost += change.price * numberRemaining;
                break;
            }
        }
        return totalCost;
    }
    

    public static class StockChange {
        private int amount;
        private int price;
        
        private StockChange(int amount, int price) {
            this.amount = amount;
            this.price = price;
        }
        
        public static StockChange changeOf(int amount, int price) {
            return new StockChange(amount, price);
        }
        
        @Override
        public String toString() {
            return String.format("(%d @ %d)", amount, price);
        }
    }
}
