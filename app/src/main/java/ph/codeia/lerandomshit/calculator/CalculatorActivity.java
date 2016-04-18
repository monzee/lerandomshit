package ph.codeia.lerandomshit.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import ph.codeia.lerandomshit.LeRandomShit;
import ph.codeia.lerandomshit.R;

public class CalculatorActivity extends AppCompatActivity {
    @Inject
    CalcContract.View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        ((LeRandomShit) getApplication()).getInjector()
                .calculator(new CalculatorModule(this))
                .inject(this);
    }
}
