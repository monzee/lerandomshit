package ph.codeia.lerandomshit.calculator;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.BindBool;
import butterknife.OnClick;
import ph.codeia.lerandomshit.R;

public class CalculatorView implements CalcContract.View {
    @BindBool(R.bool.bound)
    boolean isBound;

    @Bind(R.id.display)
    TextView display;

    @Bind(R.id.do_add)
    Button addButton;

    @Bind(R.id.do_subtract)
    Button subButton;

    @Bind(R.id.do_multiply)
    Button mulButton;

    @Bind(R.id.do_divide)
    Button divButton;

    private final CalcContract.Presenter presenter;

    public CalculatorView(CalcContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void show(String digits) {
        if (isBound) {
            display.setText(digits);
        }
    }

    @Override
    public void highlight(CalcContract.Highlightable operator) {
        if (isBound) {
            addButton.setPressed(false);
            subButton.setPressed(false);
            mulButton.setPressed(false);
            divButton.setPressed(false);
            if (operator != null) {
                switch (operator) {
                    case PLUS:
                        addButton.postOnAnimation(() -> addButton.setPressed(true));
                        break;
                    case MINUS:
                        subButton.postOnAnimation(() -> subButton.setPressed(true));
                        break;
                    case TIMES:
                        mulButton.postOnAnimation(() -> mulButton.setPressed(true));
                        break;
                    case DIVIDE:
                        divButton.postOnAnimation(() -> divButton.setPressed(true));
                        break;
                    case SOMETHING:
                        // shouldn't have called this in the first place if it's
                        // not one of the 4 above
                        break;
                }
            }
        }
    }

    @OnClick(value = {
            R.id.do_digit_0, R.id.do_digit_1, R.id.do_digit_2, R.id.do_digit_3, R.id.do_digit_4,
            R.id.do_digit_5, R.id.do_digit_6, R.id.do_digit_7, R.id.do_digit_8, R.id.do_digit_9,
            R.id.do_add, R.id.do_subtract, R.id.do_multiply, R.id.do_divide,
            R.id.do_decimal_point, R.id.do_equals, R.id.do_clear, R.id.do_backspace
    })
    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.do_digit_0:
                presenter.didPressDigit(0);
                break;
            case R.id.do_digit_1:
                presenter.didPressDigit(1);
                break;
            case R.id.do_digit_2:
                presenter.didPressDigit(2);
                break;
            case R.id.do_digit_3:
                presenter.didPressDigit(3);
                break;
            case R.id.do_digit_4:
                presenter.didPressDigit(4);
                break;
            case R.id.do_digit_5:
                presenter.didPressDigit(5);
                break;
            case R.id.do_digit_6:
                presenter.didPressDigit(6);
                break;
            case R.id.do_digit_7:
                presenter.didPressDigit(7);
                break;
            case R.id.do_digit_8:
                presenter.didPressDigit(8);
                break;
            case R.id.do_digit_9:
                presenter.didPressDigit(9);
                break;
            case R.id.do_add:
                presenter.didPressOperator(CalcContract.Highlightable.PLUS);
                break;
            case R.id.do_subtract:
                presenter.didPressOperator(CalcContract.Highlightable.MINUS);
                break;
            case R.id.do_multiply:
                presenter.didPressOperator(CalcContract.Highlightable.TIMES);
                break;
            case R.id.do_divide:
                presenter.didPressOperator(CalcContract.Highlightable.DIVIDE);
                break;
            case R.id.do_decimal_point:
                presenter.didPressDecimalPoint();
                break;
            case R.id.do_equals:
                presenter.didPressEquals();
                break;
            case R.id.do_clear:
                presenter.didPressClear();
                break;
            case R.id.do_backspace:
                presenter.didPressBackspace();
                break;
        }
    }
}
