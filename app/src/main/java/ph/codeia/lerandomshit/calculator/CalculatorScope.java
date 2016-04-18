package ph.codeia.lerandomshit.calculator;

import dagger.Subcomponent;
import ph.codeia.lerandomshit.util.PerActivity;

@PerActivity
@Subcomponent(modules = CalculatorModule.class)
public interface CalculatorScope {
    void inject(CalculatorActivity activity);
}
