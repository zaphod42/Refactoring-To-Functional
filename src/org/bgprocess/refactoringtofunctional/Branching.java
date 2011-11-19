package org.bgprocess.refactoringtofunctional;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.find;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;

public class Branching {
    @Test public void
    if_else_structures_to_figure_out_what_to_do() {
        int value = 4;
        int result = 0;
        if (value == 0) {
            result = value + 1;
        } else if (value < 5) {
            result = value - 1;
        } else if (value == 5 || value > 6) {
            result = value + 2;
        } else if (value == 6) {
            result = value - 3;
        }
        
        assertThat(result, is(3));
    }
    
    @Test public void
    dispatch_from_predicates() {
        ImmutableMap<Predicate<Integer>, Function<Integer, Integer>> thingsToDo
            = ImmutableMap.of(equalTo(0), add(1),
                              lessThan(5), add(-1), 
                              or(equalTo(5), greaterThan(6)), add(2),
                              equalTo(6), add(-3));
        
        int value = 4;
        Function<Integer, Integer> theAction = findValue(thingsToDo, handles(value));
        
        assertThat(theAction.apply(value), is(3));
    }

    private <K, V> V findValue(Map<K, V> map, final Predicate<K> predicate) {
        return find(map.entrySet(), new Predicate<Entry<K, V>>() {
            @Override public boolean apply(Entry<K, V> entry) {
                return predicate.apply(entry.getKey());
            }
        }).getValue();
    }
    
    private Predicate<Predicate<Integer>> handles(final int theValue) {
        return new Predicate<Predicate<Integer>>() {
            @Override public boolean apply(Predicate<Integer> predicate) {
                return predicate.apply(theValue);
            }
        };
    }
    
    public static Function<Integer, Integer> add(final int amount) {
        return new Function<Integer, Integer>() {
            @Override public Integer apply(Integer input) {
                return input + amount;
            }
        };
    }
    
    public static Predicate<Integer> greaterThan(final int value) {
        return new Predicate<Integer>() {
            @Override public boolean apply(Integer input) {
                return input > value;
            }
        };
    }
    
    public static Predicate<Integer> lessThan(final int value) {
        return new Predicate<Integer>() {
            @Override public boolean apply(Integer input) {
                return input < value;
            }
        };
    }
}
