package ph.codeia.lerandomshit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ph.codeia.lerandomshit.leddit.LedditSession;

/**
 * This file is a part of the Le Random Shit project.
 */
@Module
public class SessionScope {
    @Provides @Singleton @Named("top")
    LedditSession.State topStories() {
        return new LedditSession.State();
    }

    @Provides @Singleton @Named("latest")
    LedditSession.State newStories() {
        return new LedditSession.State();
    }

    @Provides @Singleton @Named("best")
    LedditSession.State bestStories() {
        return new LedditSession.State();
    }
}
