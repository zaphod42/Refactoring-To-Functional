package org.bgprocess.refactoringtofunctional.handson;

import java.util.Arrays;

import com.google.common.base.Joiner;

public abstract class Equalable<T> {
    private static final Joiner COMMA_SEPARATED = Joiner.on(", ");

    protected abstract Object[] identificationParts();
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass().equals(obj.getClass())) {
            Object[] mine = identificationParts();
            Object[] theirs = ((Equalable<T>)obj).identificationParts();
            return Arrays.equals(mine, theirs);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(identificationParts());
    }
    
    @Override
    public String toString() {
        return COMMA_SEPARATED.join(identificationParts());
    }
}
