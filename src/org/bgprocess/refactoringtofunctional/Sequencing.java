package org.bgprocess.refactoringtofunctional;

import static org.bgprocess.refactoringtofunctional.Branching.add;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Functions;

public class Sequencing {
    @Test public void
    implicit_sequence() {
        Integer value = 1;
        value += 1;
        String stringForm = value.toString();
        Integer result = Integer.parseInt(stringForm);
        result -= 1;
        
        assertThat(result, is(1));
    }
    
    @Test public void
    explicit_sequences() {
        Function<Integer, Integer> addOne = add(1);
        Function<Object, String> toString = Functions.toStringFunction();
        Function<Integer, Integer> minusOne = add(-1);
        Function<String, Integer> fromString = new Function<String, Integer>() {
            @Override public Integer apply(String input) { return Integer.parseInt(input); }
        };
        
        Function<Integer, String> plusOneToString = Sequence.from(addOne).andThen(toString);
        Function<String, Integer> fromStringToMinusOne = Sequence.from(fromString).andThen(minusOne);
        
        assertThat(Sequence.from(plusOneToString).andThen(fromStringToMinusOne).apply(1), is(1));
    }
    
    public static class Sequence<IN, OUT> implements Function<IN, OUT> {
        @SuppressWarnings("rawtypes")
        private final List<Function> steps;
        
        @SuppressWarnings("rawtypes")
        private Sequence(Function<?, ?> step) { this.steps = Arrays.asList((Function)step); }
        @SuppressWarnings("rawtypes")
        private Sequence(Function<?, ?> step, Sequence<?, ?> previous) {
            this.steps = new ArrayList<Function>();
            this.steps.addAll(previous.steps);
            this.steps.add(step);
        }
        
        public static <IN, OUT> Sequence<IN, OUT> from(Function<IN, OUT> step) { 
            return new Sequence<IN, OUT>(step); 
        }
        
        public <NEW_OUT> Sequence<IN, NEW_OUT> andThen(Function<? super OUT, NEW_OUT> step) { 
            return new Sequence<IN, NEW_OUT>(step, this); 
        }
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public OUT apply(IN input) {
            Object result = input;
            for (Function step : steps) {
                result = step.apply(result);
            }
            return (OUT) result;
        }
    }
}
