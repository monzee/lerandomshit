package ph.codeia.lerandomshit.leddit;

import javax.inject.Named;

import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import ph.codeia.lerandomshit.LeRandomShit;
import ph.codeia.lerandomshit.di.ActivityModule;
import ph.codeia.lerandomshit.di.CurrentContext;
import ph.codeia.lerandomshit.util.PerActivity;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * This file is a part of the Le Random Shit project.
 */
@PerActivity
@Component(dependencies = LeRandomShit.Globals.class, modules = {
        LedditInjector.Scope.class,
        ActivityModule.class,
        CurrentContext.class,
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
            return 30;
        }

        @Binds
        abstract FrontPage.Synchronization synchronization(FrontPagePresenter p);

        @Binds
        abstract FrontPage.Interaction interaction(FrontPagePresenter p);

        @Binds
        abstract FrontPage.Display display(FrontPageView v);

        @Binds
        abstract FrontPage.DataSource dataSource(RestService s);
    }
}
