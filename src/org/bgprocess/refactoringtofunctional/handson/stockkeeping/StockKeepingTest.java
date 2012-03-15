package org.bgprocess.refactoringtofunctional.handson.stockkeeping;

import static org.bgprocess.refactoringtofunctional.handson.stockkeeping.StockKeepingTest.StockChangeScenario.theCostOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.bgprocess.refactoringtofunctional.handson.StockChange;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

@RunWith(Theories.class)
public class StockKeepingTest {
    private static final StockChange[] NO_CHANGES = new StockChange[0];

    @DataPoints
    public static final StockChangeScenario[] SCENARIOS = {
        theCostOf(0).is(0).given(NO_CHANGES),
        theCostOf(0).is(0).given(StockChange.of(1, 1)),
        
        theCostOf(1).is(1).given(StockChange.of(1, 1)),
        theCostOf(2).is(2).given(StockChange.of(2, 1)),
        
        theCostOf(2).is(3).given(StockChange.of(1, 1), StockChange.of(1, 2)),
        theCostOf(2).is(3).given(StockChange.of(2, 1), StockChange.of(-1, 1), StockChange.of(1, 2)),
        
        theCostOf(1).is(1).given(StockChange.of(2, 1), StockChange.of(-1, 3), StockChange.of(1, 2)),
        theCostOf(2).is(3).given(StockChange.of(2, 1), StockChange.of(-1, 3), StockChange.of(1, 2)),
        
        theCostOf(1).is(2).given(StockChange.of(1, 1), StockChange.of(1, 1), StockChange.of(-2, 3), StockChange.of(1, 2)),
    };
    
    @Theory
    public void calculates_the_cost_of_remaining_stock(StockChangeScenario scenario) {
        assertThat(scenario.toString(),
                new StockKeeping(scenario.changes).costOf(scenario.numberOfUnitsToCost),
                is(scenario.cost));
    }
    
    public static class StockChangeScenario {
        private final int numberOfUnitsToCost;
        private int cost;
        private Iterable<StockChange> changes;

        public StockChangeScenario(int number) {
            this.numberOfUnitsToCost = number;
        }

        public StockChangeScenario given(StockChange... changes) {
            this.changes = Lists.newArrayList(changes);
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
            return String.format("The cost of %d should be %d from the changes: %s", numberOfUnitsToCost, cost, Joiner.on(", ").join(changes));
        }
    }
}
