package ph.codeia.lerandomshit;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ph.codeia.lerandomshit.leddit.FrontPage;

/**
 * This file is a part of the Le Random Shit project.
 */
@Module
public class SessionScope {
    @Provides @Singleton @Named("top_posts")
    List<FrontPage.Post> topPosts() {
        return new ArrayList<>();
    }
}
