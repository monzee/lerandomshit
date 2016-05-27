package ph.codeia.lerandomshit.leddit;

import java.util.List;

import ph.codeia.lerandomshit.util.Consumer;

/**
 * This file is a part of the Le Random Shit project.
 */
public abstract class FrontPageContract {
    public interface Display {
        void tell(String message);
        void refresh();
        void drillInto(int storyId);
    }

    public interface Interaction {
        void didChooseStory(int i);
        void didUpboat(int i);
        void didDownboat(int i);
    }

    public interface Synchronization {
        void bind(Display view);
        void fetchFrontPage();
    }

    public interface Service {
        void getTopStories(Consumer<List<Hn.Story>> then);
        void getNewStories(Consumer<List<Hn.Story>> then);
        void getBestStories(Consumer<List<Hn.Story>> then);
    }
}
