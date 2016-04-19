package ph.codeia.lerandomshit.calculator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class CalcContract {
    public interface View {
        void show(String digits);
        void highlight(Highlightable key);
    }

    public interface Presenter {
        void bind(View view);
        void didPressDigit(int n);
        void didPressDecimalPoint();
        void didPressOperator(Highlightable key);
        void didPressEquals();
        void didPressClear();
        void didPressBackspace();
    }

    public interface Model {
        ShuntingYard.Command[] dump();
        void enqueue(char c);
        void enqueue(int n);
        void enqueue(ShuntingYard.Apply operator);
        boolean isEmpty();
        boolean shouldClearDisplay();
        @NonNull String getBuffer();
        void setBuffer(@NonNull String digits);
        @Nullable Highlightable getHighlighted();
        void setHighlighted(@Nullable Highlightable key);
    }

    public enum Highlightable { PLUS, MINUS, TIMES, DIVIDE }
}
