package ph.codeia.lerandomshit.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.codeia.lerandomshit.R;
import ph.codeia.lerandomshit.calculator.CalculatorActivity;

public class LauncherActivity extends AppCompatActivity implements LaunchContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.do_launch_calculator)
    public void launch(View view) {
        launch(LaunchContract.Feature.CALCULATOR);
    }

    @Override
    public void launch(LaunchContract.Feature feature) {
        switch (feature) {
            case CALCULATOR:
                Intent i = new Intent(getApplicationContext(), CalculatorActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
