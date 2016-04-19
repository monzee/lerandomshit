package ph.codeia.lerandomshit.calculator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CalculatorModel implements CalcContract.Model {
    private final StringBuilder buffer = new StringBuilder();
    private final List<ShuntingYard.Command> equation = new ArrayList<>();
    private CalcContract.Highlightable highlighted;
    private boolean awaitingOperator = true;
    private boolean hasDecimalPoint = false;

    @Override
    public ShuntingYard.Command[] dump() {
        equation.add(flush());
        ShuntingYard.Command[] result = new ShuntingYard.Command[equation.size()];
        equation.toArray(result);
        equation.clear();
        highlighted = null;
        awaitingOperator = false;
        return result;
    }

    @Override
    public void enqueue(char c) {
        if (!hasDecimalPoint) {
            buffer.append(c);
            hasDecimalPoint = true;
        }
        awaitingOperator = true;
    }

    @Override
    public void enqueue(int n) {
        if (n > 0 || (n == 0 && !isEmpty())) {
            buffer.append(n);
        } else if (!isEmpty()) {
            int last = buffer.length() - 1;
            hasDecimalPoint = buffer.charAt(last) == '.';
            buffer.deleteCharAt(last);
        }
        awaitingOperator = true;
    }

    @Override
    public void enqueue(ShuntingYard.Apply operator) {
        if (awaitingOperator || equation.isEmpty()) {
            equation.add(flush());
            equation.add(operator);
            awaitingOperator = false;
        } else {
            equation.set(equation.size() - 1, operator);
        }
    }

    @Override
    public boolean isEmpty() {
        return buffer.length() == 0;
    }

    @Override
    public boolean shouldClearDisplay() {
        return !awaitingOperator;
    }

    @NonNull
    @Override
    public String getBuffer() {
        return isEmpty()? "0" : buffer.toString();
    }

    @Override
    public void setBuffer(@NonNull String digits) {
        buffer.setLength(0);
        buffer.append(digits);
        hasDecimalPoint = digits.contains(".");
    }

    @Nullable
    @Override
    public CalcContract.Highlightable getHighlighted() {
        return highlighted;
    }

    @Override
    public void setHighlighted(@Nullable CalcContract.Highlightable key) {
        highlighted = key;
    }

    private ShuntingYard.Push flush() {
        return new ShuntingYard.Push(new BigDecimal(getBuffer()));
    }
}
