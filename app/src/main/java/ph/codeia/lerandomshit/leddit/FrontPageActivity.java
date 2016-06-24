package ph.codeia.lerandomshit.leddit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import ph.codeia.lerandomshit.LeRandomShit;
import ph.codeia.lerandomshit.R;
import ph.codeia.lerandomshit.di.CurrentContext;

public class FrontPageActivity extends AppCompatActivity {

    @Inject
    FrontPage.Synchronization presenter;

    @Inject
    FrontPage.Display view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leddit_front_page);
        DaggerLedditInjector.builder()
                .globals(LeRandomShit.globals())
                .currentContext(new CurrentContext(this))
                .build()
                .inject(this);
        presenter.bind(view);
        presenter.fetchPage(FrontPage.Page.TOP);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.bind(null);
    }
}
