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

    @PerActivity @Provides CalcContract.View view(CalcContract.Presenter p) {
        CalcContract.View view = new CalculatorView(p);
        ButterKnife.bind(view, activity);
        return view;
    }

    @PerActivity @Provides CalcContract.Presenter presenter() {
        return new CalculatorPresenter();
    }
}
