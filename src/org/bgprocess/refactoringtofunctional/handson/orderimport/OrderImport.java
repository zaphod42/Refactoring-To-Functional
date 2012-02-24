package org.bgprocess.refactoringtofunctional.handson.orderimport;

import java.util.List;

import com.google.common.collect.Lists;

public class OrderImport {
    private final OrderBook book;
    private final Catalog catalog;

    public OrderImport(OrderBook book, Catalog catalog) {
        this.book = book;
        this.catalog = catalog;
    }

    public Result importFrom(String input) {
        List<OrderRecord> records = Lists.newArrayList();
        List<Order> orders = Lists.newArrayList();
        for (String line : input.split("\n")) {
            if (!line.isEmpty()) {
                OrderRecord record = parseToRecord(line);
                records.add(record);
            }
        }
        
        for (OrderRecord record : records) {
            if (record.isValid(catalog)) {
                orders.add(record.toOrder());
            } else {
                return Result.ERROR;
            }
        }
        
        for (Order order : orders) {
            book.record(order);
        }
        
        return Result.SUCCESS;
    }

    private OrderRecord parseToRecord(String input) {
        String[] fields = input.split(",");
        return new OrderRecord(fields[0], fields[1], fields[2]);
    }
    
    private static class OrderRecord {
        public final String product;
        public final String quantity;
        public final String price;
        
        public OrderRecord(String product, String quantity, String price) {
            this.product = product;
            this.quantity = quantity;
            this.price = price;
        }

        public boolean isValid(Catalog catalog) {
            try {
                return catalog.exists(toOrder().product);
            } catch(NumberFormatException e) {
                return false;
            }
        }

        public Order toOrder() {
            return new Order(Product.of(product), Quantity.of(Integer.parseInt(quantity)), Price.of(Double.parseDouble(price)));
        }
    }
}
