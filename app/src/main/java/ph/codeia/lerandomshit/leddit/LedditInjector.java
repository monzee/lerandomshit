package ph.codeia.lerandomshit.leddit;

import java.util.List;

import javax.inject.Named;

import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import ph.codeia.lerandomshit.ActivityScope;
import ph.codeia.lerandomshit.LeRandomShit;
import ph.codeia.lerandomshit.util.PerActivity;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * This file is a part of the Le Random Shit project.
 */
@PerActivity
@Component(dependencies = LeRandomShit.Globals.class, modules = {
        LedditInjector.Scope.class,
        ActivityScope.class,
}) public interface LedditInjector {
    void inject(FrontPageActivity a);

    @Module
    abstract class Scope {
        @Provides
        static Retrofit retrofit() {
            return new Retrofit.Builder()
                    .baseUrl("https://hacker-news.firebaseio.com/v0/")
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }

        @Provides @Named("page_size")
        static int pageSize() {
            return 20;
        }

        @Binds
        abstract FrontPageContract.Interaction interaction(FrontPagePresenter p);

        @Binds
        abstract FrontPageContract.Synchronization sync(FrontPagePresenter p);

        @Binds
        abstract FrontPageContract.Service service(LedditSession s);

        @Provides
        static List<Hn.Story> stories(@Named("top") LedditSession.State state) {
            return state.stories;
        }
    }
}
