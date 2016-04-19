package ph.codeia.lerandomshit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ph.codeia.lerandomshit.calculator.CalcContract;
import ph.codeia.lerandomshit.calculator.CalculatorModel;

@Module
public class ApplicationModule {
    @Singleton @Provides CalcContract.Model provideCalcModel() {
        return new CalculatorModel();
    }
}
