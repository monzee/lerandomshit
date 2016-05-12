package ph.codeia.lerandomshit.calculator;

import android.app.Activity;

import butterknife.ButterKnife;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import ph.codeia.lerandomshit.util.PerActivity;

public abstract class CalculatorWiring {
    @Module
    public static class Scope {
        private Activity activity;

        public Scope(Activity activity) {
            this.activity = activity;
        }

        @Provides @PerActivity
        CalcContract.Display view(CalculatorView view) {
            ButterKnife.bind(view, activity);
            return view;
        }

        @Provides @PerActivity
        CalcContract.Interaction presenter(CalculatorPresenter presenter) {
            return presenter;
        }

        @Provides
        CalcContract.State model(CalculatorModel model) {
            return model;
        }
    }

    @PerActivity
    @Subcomponent(modules = Scope.class)
    public interface Injector {
        void inject(CalculatorActivity activity);
    }
}
