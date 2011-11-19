package org.bgprocess.refactoringtofunctional;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class ListProcessing {
    private static final List<Integer> input = asList(1, 2, 3, 4, 5);

    @Test public void
    sometimes_you_only_want_certain_things() {
        List<Integer> odds = Lists.newArrayList(input);
        for (Integer value : input) {
            if (value % 2 == 0) {
                odds.remove(value);
            }
        }
        
        assertThat(odds, contains(1, 3, 5));
    }
    
    @Test public void
    we_can_do_that_without_changing_our_source() {
        List<Integer> odds = Lists.newArrayList();
        for (Integer value : input) {
            if (value % 2 == 1) {
                odds.add(value);
            }
        }
        
        assertThat(odds, contains(1, 3, 5));
    }
    
    @Test public void
    finding_stuff_without_changing_things() {
        final Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override public boolean apply(Integer value) {
                return value % 2 == 1;
            }
        };
        
        Iterable<Integer> odds = filter(input, isOdd);
        
        assertThat(odds, contains(1, 3, 5));
    }
    
    @Test public void
    sometimes_you_need_to_change_from_one_thing_to_another() {
        List<Integer> doubled = Lists.newArrayList();
        for (Integer value : input) {
            doubled.add(value * 2);
        }
        
        assertThat(doubled, contains(2, 4, 6, 8, 10));
    }
    
    @Test public void
    transforming_elements_is_easy() {
        final Function<Integer, Integer> toDoubled = new Function<Integer, Integer>() {
            @Override public Integer apply(Integer value) {
                return value * 2;
            }
        };
        
        Iterable<Integer> doubled = transform(input, toDoubled);
        
        assertThat(doubled, contains(2, 4, 6, 8, 10));
    }
    
    @Test public void
    ok_smart_guy_what_about_when_I_have_keep_track_of_something() {
        int sum = 0;
        for (Integer value : input) {
            sum += (value * 2);
        }
        
        assertThat(sum, is(30));
    }
    
    @Test public void
    we_can_also_change_things_without_changing_things() {
        final Reducer<Integer, Integer> add = new Reducer<Integer, Integer>() {
            @Override public Integer apply(Integer accumulator, Integer value) {
                return accumulator + value;
            }
        };
        
        final Function<Integer, Integer> toDoubled = new Function<Integer, Integer>() {
            @Override public Integer apply(Integer value) {
                return value * 2;
            }
        };
        
        Integer sum = reduce(transform(input, toDoubled), 0, add);
        
        assertThat(sum, is(30));
    }
    
    public static <I, O> O reduce(Iterable<I> values, O init, Reducer<I, O> reducer) {
        O result = init;
        for (I value : values) {
            result = reducer.apply(result, value);
        }
        return result;
    }

    public interface Reducer<I, O> {
        O apply(O accumulator, I input);
    }
}
