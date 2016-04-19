package ph.codeia.lerandomshit.calculator;

import android.app.Activity;

import butterknife.ButterKnife;
import dagger.Module;
import dagger.Provides;
import ph.codeia.lerandomshit.util.PerActivity;

@Module
public class CalculatorModule {
    private Activity activity;

    public CalculatorModule(Activity activity) {
        this.activity = activity;
    }

    @PerActivity @Provides CalcContract.View provideView(CalcContract.Presenter presenter) {
        CalcContract.View view = new CalculatorView(presenter);
        ButterKnife.bind(view, activity);
        presenter.bind(view);
        return view;
    }
}
