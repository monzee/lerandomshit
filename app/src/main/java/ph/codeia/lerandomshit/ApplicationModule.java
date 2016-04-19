package ph.codeia.lerandomshit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ph.codeia.lerandomshit.calculator.CalcContract;
import ph.codeia.lerandomshit.calculator.CalculatorPresenter;

@Module
public class ApplicationModule {
    @Singleton @Provides CalcContract.Presenter providePresenter() {
        return new CalculatorPresenter();
    }
}
