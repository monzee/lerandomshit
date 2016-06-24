package ph.codeia.lerandomshit.leddit;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ph.codeia.lerandomshit.util.Logging;
import ph.codeia.lerandomshit.util.PerActivity;

/**
 * This file is a part of the Le Random Shit project.
 */
@PerActivity
public class FrontPagePresenter implements FrontPage.Synchronization, FrontPage.Interaction {

    @Inject
    FrontPage.DataSource leddit;

    @Inject @Named("top_posts")
    List<FrontPage.Post> posts;

    @Inject @Named("page_size")
    int pageSize = 20;

    @Inject
    Logging log;

    private FrontPage.Display view;
    private List<Long> storyIds;
    private int lastFetchedIndex;
    // TODO: should remember storyIds and lastFetchedIndex

    @Inject
    public FrontPagePresenter() {}

    @Override
    public void bind(FrontPage.Display view) {
        this.view = view;
    }

    @Override
    public void fetchPage(FrontPage.Page page) {
        posts.clear();
        posts.addAll(Arrays.asList(new FrontPage.Post[pageSize]));
        view.fetching(0, pageSize);
        leddit.getPage(page, ids -> {
            storyIds = ids;
            leddit.materialize(ids, 0, pageSize, this::showItem, this::pageFetched);
        });
    }

    @Override
    public void fetchMore() {
        posts.addAll(Arrays.asList(new FrontPage.Post[pageSize]));
        view.fetching(lastFetchedIndex, pageSize);
        int start = lastFetchedIndex;
        int endExclusive = start + pageSize;
        leddit.materialize(storyIds, start, endExclusive, this::showItem, this::pageFetched);
    }

    @Override
    public void pageFetched(List<FrontPage.Post> posts) {
        lastFetchedIndex += pageSize;
    }

    @Override
    public void didPressRefresh() {

    }

    @Override
    public void didChoosePost(int index) {

    }

    private void showItem(FrontPage.Post p, int i) {
        if (view != null) {
            posts.set(i, p);
            view.fetched(i);
        }
    }
}
