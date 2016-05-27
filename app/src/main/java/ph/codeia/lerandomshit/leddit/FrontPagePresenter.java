package ph.codeia.lerandomshit.leddit;

import javax.inject.Inject;

import ph.codeia.lerandomshit.util.PerActivity;

/**
 * This file is a part of the Le Random Shit project.
 */
@PerActivity
public class FrontPagePresenter
        implements FrontPageContract.Interaction, FrontPageContract.Synchronization
{
    @Inject
    FrontPageContract.Service source;

    private FrontPageContract.Display view;

    @Inject
    public FrontPagePresenter() {}


    @Override
    public void didChooseStory(int i) {

    }

    @Override
    public void didUpboat(int i) {

    }

    @Override
    public void didDownboat(int i) {

    }

    @Override
    public void bind(FrontPageContract.Display view) {
        this.view = view;
    }

    @Override
    public void fetchFrontPage() {
        source.getTopStories(stories -> {
            if (view != null) {
                view.refresh();
            }
        });
    }
}
