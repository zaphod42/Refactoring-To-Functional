package org.bgprocess.refactoringtofunctional.handson;

import static org.bgprocess.refactoringtofunctional.handson.StockKeeping.StockChange.changeOf;
import static org.bgprocess.refactoringtofunctional.handson.StockKeepingTest.StockChangeScenario.theCostOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;

import org.bgprocess.refactoringtofunctional.handson.StockKeeping.StockChange;
import org.junit.Test;

import com.google.common.collect.Lists;


public class StockKeepingTest {
    private static final StockChange[] NO_CHANGES = new StockChange[0];

    private static final StockChangeScenario[] SCENARIOS = {
        theCostOf(0).is(0).given(NO_CHANGES),
        theCostOf(0).is(0).given(changeOf(1, 1)),
        
        theCostOf(1).is(1).given(changeOf(1, 1)),
        theCostOf(2).is(2).given(changeOf(2, 1)),
        
        theCostOf(2).is(3).given(changeOf(1, 1), changeOf(1, 2)),
        theCostOf(2).is(3).given(changeOf(2, 1), changeOf(-1, 1), changeOf(1, 2)),
        
        theCostOf(1).is(1).given(changeOf(2, 1), changeOf(-1, 3), changeOf(1, 2)),
        theCostOf(2).is(3).given(changeOf(2, 1), changeOf(-1, 3), changeOf(1, 2)),
        
        theCostOf(1).is(2).given(changeOf(1, 1), changeOf(1, 1), changeOf(-2, 3), changeOf(1, 2)),
    };
    
    @Test
    public void calculates_the_cost_of_remaining_stock() {
        for (StockChangeScenario scenario : SCENARIOS) {
            assertThat(scenario.toString(),
                    new StockKeeping(Lists.newArrayList(scenario.changes)).costOf(scenario.number),
                    is(scenario.cost));
        }
    }
    
    public static class StockChangeScenario {
        private final int number;
        private int cost;
        private StockChange[] changes;

        public StockChangeScenario(int number) {
            this.number = number;
        }

        public StockChangeScenario given(StockChange... changes) {
            this.changes = changes;
            return this;
        }

        public StockChangeScenario is(int cost) {
            this.cost = cost;
            return this;
        }

        public static StockChangeScenario theCostOf(int number) {
            return new StockChangeScenario(number);
        }
        
        @Override
        public String toString() {
            return String.format("The cost of %d should be %d from the changes: %s", number, cost, Arrays.toString(changes));
        }
    }
}
