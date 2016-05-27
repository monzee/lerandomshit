package ph.codeia.lerandomshit.leddit;

import java.util.List;

import ph.codeia.lerandomshit.util.Consumer;

/**
 * This file is a part of the Le Random Shit project.
 */
public abstract class StoryContract {
    public interface Display {
        void tell(String message);
        void show(Kind kind);
    }

    public interface Interaction {
        void didPressTheFuckingArticle();
        void didUpboat(int commentId);
        void didDownboat(int commentId);
    }

    public interface Synchronization {
        void bind(Display view);
        void fetchComments(int maxLevel);
    }

    public interface Service {
        void getThread(int[] eldest, Consumer<List<Hn.Thread>> then);
    }

    public enum Kind { STORY, ASK, JOB, POLL }
}
