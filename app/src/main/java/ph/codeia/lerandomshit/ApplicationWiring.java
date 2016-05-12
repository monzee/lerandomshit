package ph.codeia.lerandomshit;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import ph.codeia.lerandomshit.calculator.CalculatorWiring;

public abstract class ApplicationWiring {
    @Module
    public static class Scope {
        private final Application application;

        public Scope(Application application) {
            this.application = application;
        }

        @Provides
        Context context() {
            return application;
        }
    }

    @Singleton
    @Component(modules = Scope.class)
    public interface Injector {
        CalculatorWiring.Injector calculator(CalculatorWiring.Scope c);
    }
}
