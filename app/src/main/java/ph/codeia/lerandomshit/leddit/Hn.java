package ph.codeia.lerandomshit.leddit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

/**
 * This file is a part of the Le Random Shit project.
 */
public final class Hn {
    private Hn() {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Story {
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
