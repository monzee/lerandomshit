package ph.codeia.lerandomshit.calculator;

public abstract class CalcContract {
    public interface View {
        void show(String digits);
        void highlight(Operator operator);
    }

    public interface Presenter {
        void bind(View view);
        void didPressDigit(int n);
        void didPressDecimalPoint();
        void didPressOperator(Operator operator);
        void didPressEquals();
        void didPressClear();
        void didPressBackspace();
    }

    public enum Operator { PLUS, MINUS, TIMES, DIVIDE }
}
