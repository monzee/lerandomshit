package ph.codeia.lerandomshit.leddit;

import java.util.Date;
import java.util.List;

import ph.codeia.lerandomshit.util.BiConsumer;
import ph.codeia.lerandomshit.util.Consumer;

/**
 * This file is a part of the Le Random Shit project.
 */
public final class FrontPage {
    private FrontPage() {}

    public interface Display {
        void tell(String message);
        void refresh();
        void fetching(int start, int count);
        void fetched();
        void fetched(int index);
    }

    public interface Interaction {
        void didPressRefresh();
        void didChoosePost(int index);
    }

    public interface Synchronization {
        void bind(Display view);
        void fetchPage(Page page);
        void fetchMore();
        void pageFetched(List<Post> posts);
    }

    public interface DataSource {
        void getPage(Page which, Consumer<List<Long>> then);
        <T extends Post> void getPost(int id, Consumer<T> then);
        void materialize(
                List<Long> ids,
                int start,
                int endExclusive,
                Consumer<List<Post>> then);
        void materialize(
                List<Long> ids,
                int start,
                int endExclusive,
                BiConsumer<Post, Integer> eachWithIndex,
                Consumer<List<Post>> then);
    }

    public interface Post {
        long getId();
        String getTitle();
        String getBy();
        Date getDate();
    }

    public enum Page { TOP, LATEST, BEST, SHOW, ASK, JOB }
}
