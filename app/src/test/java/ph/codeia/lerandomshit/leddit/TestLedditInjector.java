package ph.codeia.lerandomshit.leddit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Named;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import ph.codeia.lerandomshit.LeRandomShit;
import ph.codeia.lerandomshit.util.Logging;
import ph.codeia.lerandomshit.util.PerActivity;

/**
 * This file is a part of the Le Random Shit project.
 */
@PerActivity
@Component(modules = {
        LedditInjector.Scope.class,
        TestLedditInjector.Threading.class,
        TestLedditInjector.FrontPageModule.class,
}) public interface TestLedditInjector {
    void inject(RestServiceTest t);
    void inject(MaterializeBadOffsetsTest t);

    @Module
    class Threading {
        @Provides @Named("worker") @Reusable
        static Executor worker() {
            return LeRandomShit.worker();
        }

        @Provides @Named("ui") @Reusable
        static Executor ui() {
            return Executors.newSingleThreadExecutor();
        }

        @Provides @Reusable
        static Logging logger() {
            Logger log = LoggerFactory.getLogger("mz");
            return new Logging() {
                @Override
                public void v(String text, Object... fmtArgs) {
                    log.trace(String.format(text, fmtArgs));
                }

                @Override
                public void v(String text, Throwable t) {
                    log.trace(text, t);
                }

                @Override
                public void d(String text, Object... fmtArgs) {
                    log.debug(String.format(text, fmtArgs));
                }

                @Override
                public void i(String text, Object... fmtArgs) {
                    log.info(String.format(text, fmtArgs));
                }

                @Override
                public void w(String text, Object... fmtArgs) {
                    log.warn(String.format(text, fmtArgs));
                }

                @Override
                public void e(String text, Object... fmtArgs) {
                    log.error(String.format(text, fmtArgs));
                }

                @Override
                public void e(String text, Throwable t) {
                    log.error(text, t);
                }
            };
        }
    }

    @Module
    class FrontPageModule {

        @Provides
        static FrontPage.Synchronization synchronization() {
            return new FrontPage.Synchronization() {
                @Override
                public void bind(FrontPage.Display view) {

                }

                @Override
                public void fetchPage(FrontPage.Page page) {

                }

                @Override
                public void fetchMore() {

                }

                @Override
                public void pageFetched(List<FrontPage.Post> posts) {

                }
            };
        }
    }
}
