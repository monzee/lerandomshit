package ph.codeia.lerandomshit.calculator;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.OnClick;
import ph.codeia.lerandomshit.R;

public class CalculatorView implements CalcContract.Display {
    @BindBool(R.bool.yes)
    boolean isBound;

    @BindView(R.id.display)
    TextView display;

    @BindView(R.id.do_add)
    Button addButton;

    @BindView(R.id.do_subtract)
    Button subButton;

    @BindView(R.id.do_multiply)
    Button mulButton;

    @BindView(R.id.do_divide)
    Button divButton;

    private final CalcContract.Interaction user;

    @Inject
    public CalculatorView(CalcContract.Interaction presenter) {
        user = presenter;
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
                user.didPressDigit(0);
                break;
            case R.id.do_digit_1:
                user.didPressDigit(1);
                break;
            case R.id.do_digit_2:
                user.didPressDigit(2);
                break;
            case R.id.do_digit_3:
                user.didPressDigit(3);
                break;
            case R.id.do_digit_4:
                user.didPressDigit(4);
                break;
            case R.id.do_digit_5:
                user.didPressDigit(5);
                break;
            case R.id.do_digit_6:
                user.didPressDigit(6);
                break;
            case R.id.do_digit_7:
                user.didPressDigit(7);
                break;
            case R.id.do_digit_8:
                user.didPressDigit(8);
                break;
            case R.id.do_digit_9:
                user.didPressDigit(9);
                break;
            case R.id.do_add:
                user.didPressOperator(CalcContract.Highlightable.PLUS);
                break;
            case R.id.do_subtract:
                user.didPressOperator(CalcContract.Highlightable.MINUS);
                break;
            case R.id.do_multiply:
                user.didPressOperator(CalcContract.Highlightable.TIMES);
                break;
            case R.id.do_divide:
                user.didPressOperator(CalcContract.Highlightable.DIVIDE);
                break;
            case R.id.do_decimal_point:
                user.didPressDecimalPoint();
                break;
            case R.id.do_equals:
                user.didPressEquals();
                break;
            case R.id.do_clear:
                user.didPressClear();
                break;
            case R.id.do_backspace:
                user.didPressBackspace();
                break;
        }
    }
}
