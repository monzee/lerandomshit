package ph.codeia.lerandomshit;

import javax.inject.Singleton;

import dagger.Component;
import ph.codeia.lerandomshit.calculator.CalculatorModule;
import ph.codeia.lerandomshit.calculator.CalculatorScope;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationScope {
    CalculatorScope calculator(CalculatorModule module);
}
