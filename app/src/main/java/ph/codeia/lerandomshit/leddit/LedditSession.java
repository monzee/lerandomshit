package ph.codeia.lerandomshit.leddit;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ph.codeia.lerandomshit.util.Consumer;

/**
 * This file is a part of the Le Random Shit project.
 */
public class LedditSession implements FrontPageContract.Service {
    public static class State {
        public final List<Hn.Story> stories = new ArrayList<>();
        public boolean isStale = true;

        @Inject public State() {}
    }

    @Inject
    RestService remote;

    @Inject @Named("top")
    State top;

    @Inject @Named("latest")
    State latest;

    @Inject @Named("best")
    State best;

    @Inject
    public LedditSession() {}

    @Override
    public void getTopStories(Consumer<List<Hn.Story>> then) {
        if (top.isStale) {
            remote.getTopStories(stories -> {
                top.stories.clear();
                top.stories.addAll(stories);
                top.isStale = false;
                then.accept(stories);
            });
        }
    }

    @Override
    public void getNewStories(Consumer<List<Hn.Story>> then) {
        if (latest.isStale) {
            remote.getNewStories(stories -> {
                latest.stories.clear();
                latest.stories.addAll(stories);
                latest.isStale = false;
                then.accept(stories);
            });
        }
    }

    @Override
    public void getBestStories(Consumer<List<Hn.Story>> then) {
        if (best.isStale) {
            remote.getBestStories(stories -> {
                best.stories.clear();
                best.stories.addAll(stories);
                best.isStale = false;
                then.accept(stories);
            });
        }
    }
}
