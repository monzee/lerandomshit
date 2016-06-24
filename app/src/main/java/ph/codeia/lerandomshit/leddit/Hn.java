package ph.codeia.lerandomshit.leddit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;
import java.util.List;

/**
 * This file is a part of the Le Random Shit project.
 */
public final class Hn {
    private Hn() {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Hn.Story.class, name = "story"),
            @JsonSubTypes.Type(value = Hn.Job.class, name = "job"),
            // TODO: show, ask, job, poll
    })
    public interface Post extends FrontPage.Post {
        // duplicated here because i don't want jackson dependencies
        // in the contract interface
    }

    public static class Story implements Post {
        public long id;
        /** username of the author */
        public String by;
        /** total number of comments */
        public int descendants;
        /** ids of immediate child comments, ordered by score? */
        public int[] kids;
        /** net upboats */
        public int score;
        public Date time;
        public String title;
        public String url;

        @Override
        public long getId() {
            return id;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getBy() {
            return by;
        }

        @Override
        public Date getDate() {
            return time;
        }
    }

    public static class Job implements Post {
        public long id;
        public String by;
        public int score;
        public String text;
        public Date time;
        public String title;
        public String url;

        @Override
        public long getId() {
            return id;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getBy() {
            return by;
        }

        @Override
        public Date getDate() {
            return time;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Comment {
        public long id;
        /** username of author */
        public String by;
        /** ids of immediate child comments */
        public int[] kids;
        /** id of parent comment or story */
        public int parent;
        /** net upboats */
        public int score;
        public String text;
        public Date time;
    }

    public static class Thread {
        // tree of comments
        public Comment root;
        public List<Thread> children;
    }
}
