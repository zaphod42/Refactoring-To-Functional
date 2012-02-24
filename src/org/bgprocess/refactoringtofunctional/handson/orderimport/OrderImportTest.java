package org.bgprocess.refactoringtofunctional.handson.orderimport;

import static org.junit.Assert.assertEquals;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class OrderImportTest {
    private static final Product WHEEL = Product.of("Wheel");
    private static final Product FRAME = Product.of("Frame");
    private static final Product CHAIN = Product.of("Chain");
    
    private final Mockery context = new Mockery();
    private final OrderBook book = context.mock(OrderBook.class);
    private final Catalog catalog = context.mock(Catalog.class);
    private final OrderImport orderImport = new OrderImport(book, catalog);
    
    @Before public void knows_some_basic_products() {
        context.checking(new Expectations() {{
            allowing(catalog).exists(CHAIN); will(returnValue(true));
            allowing(catalog).exists(FRAME); will(returnValue(true));
            allowing(catalog).exists(WHEEL); will(returnValue(true));
        }});
    }
    
    @Test public void
    empty_file_imports_no_orders() {
        context.checking(new Expectations() {{
            never(book);
        }});
        
        assertSuccessfullyImports("");
    }
    
    @Test public void
    imports_a_single_order_into_the_order_book() {
        context.checking(new Expectations() {{
            oneOf(book).record(new Order(CHAIN, Quantity.of(2), Price.of(2.2)));
        }});
        
        assertSuccessfullyImports("Chain,2,2.2");
    }
    
    @Test public void
    imports_orders_from_several_lines_into_the_order_book() {
        context.checking(new Expectations() {{
            oneOf(book).record(new Order(CHAIN, Quantity.of(2), Price.of(2.2)));
            oneOf(book).record(new Order(WHEEL, Quantity.of(4), Price.of(50.0)));
            oneOf(book).record(new Order(FRAME, Quantity.of(2), Price.of(2500.0)));
        }});
        
        assertSuccessfullyImports("Chain,2,2.2\nWheel,4,50\nFrame,2,2500");
    }
    
    @Test public void
    ignores_trailing_empty_lines() {
        context.checking(new Expectations() {{
            allowing(book).record(with(any(Order.class)));
        }});
        
        assertSuccessfullyImports("Chain,2,2.2\n\n");
    }
    
    @Test public void
    ignores_leading_empty_lines() {
        context.checking(new Expectations() {{
            allowing(book).record(with(any(Order.class)));
        }});
        
        assertSuccessfullyImports("\n\nChain,2,2.2");
    }
    
    @Test public void
    does_not_import_anything_if_there_is_a_non_numeric_quantity() {
        context.checking(new Expectations() {{
            never(book);
        }});
        
        assertImportsWithError("Chain,2,2.2\nChain,Two,5.0\nFrame,1,2500");
    }
    
    @Test public void
    does_not_import_anything_if_there_is_a_non_numeric_price() {
        context.checking(new Expectations() {{
            never(book);
        }});
        
        assertImportsWithError("Chain,2,2.2\nChain,2,Five\nFrame,1,2500");
    }
    
    @Test public void
    does_not_import_anything_if_there_is_an_unknown_product() {
        context.checking(new Expectations() {{
            allowing(catalog).exists(Product.of("AnUnknownProduct")); will(returnValue(false));
            never(book);
        }});
        
        assertImportsWithError("Chain,2,2.2\nAnUnknownProduct,2,5.0\nFrame,1,2500");
    }

    private void assertImportsWithError(String input) {
        assertEquals(Result.ERROR, orderImport.importFrom(input));
    }

    private void assertSuccessfullyImports(String input) {
        assertEquals(Result.SUCCESS, orderImport.importFrom(input));
    }
}
