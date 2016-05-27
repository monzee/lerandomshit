package ph.codeia.lerandomshit.launcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.codeia.lerandomshit.R;
import ph.codeia.lerandomshit.calculator.CalculatorActivity;
import ph.codeia.lerandomshit.leddit.FrontPageActivity;

public class LauncherActivity extends AppCompatActivity implements LaunchContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.do_launch_calculator, R.id.do_launch_leddit})
    public void launch(View view) {
        switch (view.getId()) {
            case R.id.do_launch_calculator:
                launch(LaunchContract.Feature.CALCULATOR);
                break;
            case R.id.do_launch_leddit:
                launch(LaunchContract.Feature.LEDDIT);
                break;
            default:
                break;
        }
    }

    @Override
    public void launch(LaunchContract.Feature feature) {
        Intent i;
        Context c = getApplicationContext();
        switch (feature) {
            case CALCULATOR:
                i = new Intent(c, CalculatorActivity.class);
                startActivity(i);
                break;
            case LEDDIT:
                i = new Intent(c, FrontPageActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
