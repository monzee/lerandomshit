package ph.codeia.lerandomshit.calculator;

import javax.inject.Inject;

public class CalculatorPresenter implements CalcContract.Interaction {
    private final CalcContract.State model;
    private CalcContract.Display view;

    @Inject
    public CalculatorPresenter(CalcContract.State model) {
        this.model = model;
    }

    @Override
    public void bind(CalcContract.Display view) {
        this.view = view;
        update();
    }

    @Override
    public void didPressDigit(int n) {
        if (model.shouldClearDisplay()) {
            model.setBuffer("");
        }
        model.enqueue(n);
        update(true);
    }

    @Override
    public void didPressDecimalPoint() {
        if (model.shouldClearDisplay()) {
            model.setBuffer("");
        }
        if (model.isEmpty()) {
            model.setBuffer("0");
        }
        model.enqueue('.');
        update(true);
    }

    @Override
    public void didPressOperator(CalcContract.Highlightable key) {
        model.setHighlighted(key);
        switch (key) {
            case PLUS:
                model.enqueue(ShuntingYard.Apply.PLUS);
                break;
            case MINUS:
                model.enqueue(ShuntingYard.Apply.MINUS);
                break;
            case TIMES:
                model.enqueue(ShuntingYard.Apply.TIMES);
                break;
            case DIVIDE:
                model.enqueue(ShuntingYard.Apply.DIVIDE);
                break;
        }
        update();
    }

    @Override
    public void didPressEquals() {
        ShuntingYard.Command[] equation = model.dump();
        ShuntingYard answer = new ShuntingYard(equation);
        model.setBuffer(answer.evaluate().toString());
        update();
    }

    @Override
    public void didPressClear() {
        model.setBuffer("");
        model.setHighlighted(null);
        update();
    }

    @Override
    public void didPressBackspace() {
        model.enqueue(-1);
        update(true);
    }

    private void update() {
        update(false);
    }

    private void update(boolean displayOnly) {
        if (view != null) {
            view.show(model.getBuffer());
            if (displayOnly) {
                return;
            }
            view.highlight(model.getHighlighted());
        }
    }
}
