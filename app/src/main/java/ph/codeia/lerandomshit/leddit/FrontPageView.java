package ph.codeia.lerandomshit.leddit;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.codeia.lerandomshit.R;
import ph.codeia.lerandomshit.util.HairLineDivider;
import ph.codeia.lerandomshit.util.PerActivity;

/**
 * This file is a part of the Le Random Shit project.
 */
@PerActivity
public class FrontPageView implements FrontPage.Display {

    @Inject
    Activity activity;

    @Inject
    FrontPageAdapter adapter;

    @Inject
    LinearLayoutManager layoutManager;

    @BindView(R.id.list_stories)
    RecyclerView stories;

    @Inject
    HairLineDivider divider;

    private int fetchStart;
    private int fetchCount;

    @Inject
    public FrontPageView() {}

    @Inject
    void init() {
        ButterKnife.bind(this, activity);
        stories.setAdapter(adapter);
        stories.setLayoutManager(layoutManager);
        stories.addItemDecoration(divider);
    }

    @Override
    public void tell(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void refresh() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void fetching(int start, int count) {
        if (count > 0) {
            adapter.notifyItemRangeInserted(start, count);
            fetchStart = start;
            fetchCount = count;
        }
    }

    @Override
    public void fetched() {
        if (fetchCount > 0) {
            adapter.notifyItemRangeChanged(fetchStart, fetchCount);
            fetchCount = 0;
        }
    }

    @Override
    public void fetched(int index) {
        adapter.notifyItemChanged(index);
    }
}
