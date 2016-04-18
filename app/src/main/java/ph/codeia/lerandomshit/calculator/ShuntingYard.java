package ph.codeia.lerandomshit.calculator;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

import ph.codeia.lerandomshit.util.BinaryOperator;

public class ShuntingYard {
    public interface Command {
        void toPostFix(Queue<Command> machine, Deque<Apply> operators);
        void exec(Deque<BigDecimal> output);
    }

    public static class Push implements Command {
        private final BigDecimal value;

        public Push(BigDecimal value) {
            this.value = value;
        }

        public Push(int value) {
            this(BigDecimal.valueOf(value));
        }

        @Override
        public void toPostFix(Queue<Command> machine, Deque<Apply> operators) {
            machine.add(this);
        }

        @Override
        public void exec(Deque<BigDecimal> output) {
            output.add(value);
        }
    }

    public enum Apply implements Command {
        PLUS(1, BigDecimal::add),
        MINUS(1, BigDecimal::subtract),
        TIMES(2, BigDecimal::multiply),
        DIVIDE(2, BigDecimal::divide);

        private final int precedence;
        private final BinaryOperator<BigDecimal> operation;

        Apply(int precedence, BinaryOperator<BigDecimal> operation) {
            this.precedence = precedence;
            this.operation = operation;
        }

        @Override
        public void toPostFix(Queue<Command> machine, Deque<Apply> operators) {
            Apply op;
            while ((op = operators.peek()) != null && precedence <= op.precedence) {
                operators.pop();
                machine.add(op);
            }
            operators.push(this);
        }

        @Override
        public void exec(Deque<BigDecimal> output) {
            BigDecimal right = output.pop();
            BigDecimal left = output.pop();
            output.push(operation.apply(left, right));
        }
    }

    private final Queue<Command> machine = new ArrayDeque<>();
    private final Deque<BigDecimal> output = new ArrayDeque<>();

    public ShuntingYard(Command... infix) {
        Deque<Apply> operators = new ArrayDeque<>();
        for (Command c : infix) {
            c.toPostFix(machine, operators);
        }
        for (Apply op : operators) {
            machine.add(op);
        }
    }

    BigDecimal evaluate() {
        if (output.isEmpty()) {
            for (Command c : machine) {
                c.exec(output);
            }
        }
        return output.peek();
    }
}
