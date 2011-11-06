package org.bgprocess.refactoringtofunctional;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Function;

public class Extraction {

    /**
     * 
     */
    @Test public void
    iteration_for_the_same_problem() {
        List<Integer> input = asList(1, 2, 3, 4, 5);
        List<Integer> results = new ArrayList<Integer>();
        for (Integer value : input) {
            if (value % 2 == 1) {
                results.add(value);
            }
        }
        
        assertThat(results, contains(1, 3, 5));
    }
    
    /**
     * If we look closely at the above example, we notice that there are two things at play
     *    1: The definition of what we want to keep (odd numbers), this is called a predicate
     *    2: Iterating over the list and retaining the elements that we want to keep, this is called a filter
     */
    @Test public void
    pulling_apart_the_business_valueable_pieces() {
        final Function<Integer, Boolean> isOdd = new Function<Integer, Boolean>() {
            @Override public Boolean apply(Integer input) {
                return input % 2 == 1;
            }
        };
        
        Function<List<Integer>, List<Integer>> findOdds = filter(isOdd);
        
        assertThat(findOdds.apply(asList(1, 2, 3, 4, 5)), contains(1, 3, 5));
    }

    private Function<List<Integer>, List<Integer>> filter(final Function<Integer, Boolean> predicate) {
        return new Function<List<Integer>, List<Integer>>() {
            @Override public List<Integer> apply(List<Integer> input) {
                List<Integer> results = new ArrayList<Integer>();
                for (Integer value : input) {
                    if (predicate.apply(value)) {
                        results.add(value);
                    }
                }
                return results;
            }
        };
    }
}
