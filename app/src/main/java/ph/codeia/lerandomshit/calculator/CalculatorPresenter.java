package ph.codeia.lerandomshit.calculator;

import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CalculatorPresenter implements CalcContract.Presenter {
    private final StringBuilder buffer = new StringBuilder();
    private final List<ShuntingYard.Command> equation = new ArrayList<>();
    private boolean hasDecimalPoint = false;
    private CalcContract.Operator op;
    private BigDecimal lastAnswer;
    private CalcContract.View view;

    @Override
    public void bind(CalcContract.View view) {
        this.view = view;
    }

    @Override
    public void didPressDigit(int n) {
        if (op != null) {
            switch (op) {
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
            op = null;
        }
        buffer.append(n);
        update();
    }

    @Override
    public void didPressDecimalPoint() {
        if (hasDecimalPoint) {
            return;
        }
        if (buffer.length() == 0) {
            didPressDigit(0);
        }
        buffer.append('.');
        hasDecimalPoint = true;
        update();
    }

    @Override
    public void didPressOperator(CalcContract.Operator operator) {
        if (buffer.length() > 0) {
            flush();
        } else if (lastAnswer != null) {
            equation.add(new ShuntingYard.Push(lastAnswer));
            lastAnswer = null;
        } else {
            equation.add(new ShuntingYard.Push(BigDecimal.ZERO));
        }
        op = operator;
        update();
    }

    @Override
    public void didPressEquals() {
        if (buffer.length() > 0) {
            flush();
            compute();
            view.highlight(null);
        }
    }

    @Override
    public void didPressClear() {
        op = null;
        hasDecimalPoint = false;
        lastAnswer = null;
        buffer.setLength(0);
        equation.clear();
        update();
    }

    @Override
    public void didPressBackspace() {

    }

    private void flush() {
        equation.add(new ShuntingYard.Push(new BigDecimal(buffer.toString())));
        buffer.setLength(0);
        hasDecimalPoint = false;
    }

    private void compute() {
        ShuntingYard.Command[] nodes = equation.toArray(new ShuntingYard.Command[equation.size()]);
        ShuntingYard calc = new ShuntingYard(nodes);
        StringBuilder sb = new StringBuilder("equation: ");
        for (ShuntingYard.Command c : nodes) {
            sb.append(c.toString()).append(' ');
        }
        Log.d("calc", sb.toString());
        lastAnswer = calc.evaluate();
        Log.d("calc", "answer: " + lastAnswer);
        equation.clear();
        if (view != null) {
            view.show(lastAnswer.toString());
        }
    }

    private void update() {
        if (view != null) {
            if (buffer.length() > 0) {
                view.show(buffer.toString());
            } else if (op != null) {
                view.highlight(op);
            } else {
                view.highlight(null);
                view.show("0");
            }
        }
    }
}
