package ph.codeia.lerandomshit.calculator;

import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CalculatorPresenter implements CalcContract.Presenter {
    private final StringBuilder buffer = new StringBuilder();
    private final List<ShuntingYard.Command> equation = new ArrayList<>();
    private boolean hasDecimalPoint = false;
    private boolean shouldClearOnNextDigit = false;
    private CalcContract.Highlightable highlightedKey;
    private CalcContract.View view;

    @Override
    public void bind(CalcContract.View view) {
        this.view = view;
        update();
    }

    @Override
    public void unbind() {
        view = null;
    }

    @Override
    public void didPressDigit(int n) {
        if (highlightedKey != null && highlightedKey != CalcContract.Highlightable.SOMETHING) {
            switch (highlightedKey) {
                case PLUS:
                    equation.add(ShuntingYard.Apply.PLUS);
                    break;
                case MINUS:
                    equation.add(ShuntingYard.Apply.MINUS);
                    break;
                case TIMES:
                    equation.add(ShuntingYard.Apply.TIMES);
                    break;
                case DIVIDE:
                    equation.add(ShuntingYard.Apply.DIVIDE);
                    break;
            }
            highlightedKey = CalcContract.Highlightable.SOMETHING;
        }
        if (shouldClearOnNextDigit) {
            buffer.setLength(0);
            hasDecimalPoint = false;
            shouldClearOnNextDigit = false;
        }
        if (n != 0 || buffer.length() > 0) {
            buffer.append(n);
            update();
        }
    }

    @Override
    public void didPressDecimalPoint() {
        if (hasDecimalPoint) {
            return;
        }
        if (shouldClearOnNextDigit) {
            buffer.setLength(0);
        }
        if (buffer.length() == 0) {
            didPressDigit(0);
        }
        buffer.append('.');
        hasDecimalPoint = true;
        update();
    }

    @Override
    public void didPressOperator(CalcContract.Highlightable key) {
        if (buffer.length() > 0) {
            flush();
        } else {
            equation.add(new ShuntingYard.Push(BigDecimal.ZERO));
        }
        highlightedKey = key;
        shouldClearOnNextDigit = true;
        update();
    }

    @Override
    public void didPressEquals() {
        if (buffer.length() > 0) {
            flush();
            compute();
        }
    }

    @Override
    public void didPressClear() {
        highlightedKey = null;
        hasDecimalPoint = false;
        shouldClearOnNextDigit = false;
        buffer.setLength(0);
        equation.clear();
        update();
    }

    @Override
    public void didPressBackspace() {
        if (buffer.length() > 0) {
            int last = buffer.length() - 1;
            if (buffer.charAt(last) == '.') {
                hasDecimalPoint = false;
            }
            buffer.deleteCharAt(last);
            shouldClearOnNextDigit = false;
            update();
        }
    }

    private void flush() {
        equation.add(new ShuntingYard.Push(new BigDecimal(buffer.toString())));
        shouldClearOnNextDigit = true;
    }

    private void compute() {
        ShuntingYard.Command[] nodes = equation.toArray(new ShuntingYard.Command[equation.size()]);
        ShuntingYard calc = new ShuntingYard(nodes);
        StringBuilder sb = new StringBuilder("equation: ");
        for (ShuntingYard.Command c : nodes) {
            sb.append(c.toString()).append(' ');
        }
        Log.d("calc", sb.toString());
        String answer = calc.evaluate().toString();
        Log.d("calc", "answer: " + answer);
        equation.clear();
        buffer.setLength(0);
        buffer.append(answer);
        shouldClearOnNextDigit = true;
        highlightedKey = null;
        update();
    }

    private void update() {
        if (view != null) {
            if (buffer.length() > 0) {
                view.show(buffer.toString());
            } else {
                view.show("0");
            }
            if (highlightedKey == null) {
                view.highlight(null);
            } else if (highlightedKey != CalcContract.Highlightable.SOMETHING){
                view.highlight(highlightedKey);
            }
        }
    }
}
