package org.bgprocess.refactoringtofunctional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.common.base.Function;


public class Functions {
    /**
     * Pure functions always return the same thing given the same inputs.
     */
    @Test public void
    pure_function() {
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override public Integer apply(Integer input) { return input + 1; } 
        };
        
        assertThat(addOne.apply(1), is(2));
    }
    
    /**
     * Impure functions can return different results given the same inputs.
     */
    @Test public void
    impure_function() {
        Function<Integer, Integer> increaseBy = new Function<Integer, Integer>() {
            @Override public Integer apply(Integer input) {
                return (int) (Math.random() * 10 + input);
            }
        };
        
        // This will sometimes fail!!
        assertThat(increaseBy.apply(2), not(is(increaseBy.apply(2))));
    }
}
