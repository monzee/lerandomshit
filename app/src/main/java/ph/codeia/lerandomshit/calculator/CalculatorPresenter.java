package ph.codeia.lerandomshit.calculator;

public class CalculatorPresenter implements CalcContract.Presenter {
    private final StringBuilder buffer = new StringBuilder();
    private CalcContract.View view;
    private boolean hasDecimalPoint = false;

    @Override
    public void bind(CalcContract.View view) {
        this.view = view;
    }

    @Override
    public void didPressDigit(int n) {
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
        view.highlight(operator);
    }

    @Override
    public void didPressEquals() {

    }

    @Override
    public void didPressClear() {

    }

    @Override
    public void didPressBackspace() {

    }

    private void update() {
        if (view != null) {
            view.show(buffer.toString());
        }
    }
}
