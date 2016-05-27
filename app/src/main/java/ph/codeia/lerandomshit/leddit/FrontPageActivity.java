package ph.codeia.lerandomshit.leddit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.codeia.lerandomshit.ActivityScope;
import ph.codeia.lerandomshit.LeRandomShit;
import ph.codeia.lerandomshit.R;

public class FrontPageActivity extends AppCompatActivity implements FrontPageContract.Display {

    @Inject
    FrontPageContract.Interaction user;

    @Inject
    FrontPageContract.Synchronization presenter;

    @Inject
    FrontPageAdapter adapter;

    @BindView(R.id.list_stories)
    RecyclerView storiesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leddit_front_page);
        DaggerLedditInjector.builder()
                .globals(LeRandomShit.globals())
                .activityScope(new ActivityScope(this))
                .build()
                .inject(this);
        ButterKnife.bind(this);
        storiesView.setLayoutManager(new LinearLayoutManager(this));
        storiesView.setAdapter(adapter);
        presenter.bind(this);
        presenter.fetchFrontPage();
    }

    @Override
    public void tell(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refresh() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void drillInto(int storyId) {
        user.didChooseStory(storyId);
    }
}
