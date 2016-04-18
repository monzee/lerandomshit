package ph.codeia.lerandomshit.launcher;

public abstract class LaunchContract {
    public interface View {
        void launch(Feature feature);
    }

    public enum Feature {
        CALCULATOR,
    }
}
