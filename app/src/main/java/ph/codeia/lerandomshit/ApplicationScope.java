package ph.codeia.lerandomshit;

import dagger.Component;
import ph.codeia.lerandomshit.calculator.CalculatorModule;
import ph.codeia.lerandomshit.calculator.CalculatorScope;
import ph.codeia.lerandomshit.util.PerActivity;

@Component(modules = ApplicationModule.class)
public interface ApplicationScope {
   @PerActivity CalculatorScope calculator(CalculatorModule module);
}
