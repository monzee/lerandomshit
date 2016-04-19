package ph.codeia.lerandomshit.calculator;

public abstract class CalcContract {
    public interface View {
        void show(String digits);
        void highlight(Highlightable operator);
    }

    public interface Presenter {
        void bind(View view);
        void unbind();
        void didPressDigit(int n);
        void didPressDecimalPoint();
        void didPressOperator(Highlightable operator);
        void didPressEquals();
        void didPressClear();
        void didPressBackspace();
    }

    public enum Highlightable { PLUS, MINUS, TIMES, DIVIDE, SOMETHING }
}
