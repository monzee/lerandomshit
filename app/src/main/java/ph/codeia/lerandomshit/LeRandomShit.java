package ph.codeia.lerandomshit;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import ph.codeia.lerandomshit.calculator.CalculatorWiring;
import ph.codeia.lerandomshit.leddit.LedditSession;
import ph.codeia.lerandomshit.util.Logging;

@Module
public class LeRandomShit extends Application {
    private static Injector injector;
    private static Globals globals;

    @Singleton
    @Component(modules = LeRandomShit.class)
    public interface Injector {
        CalculatorWiring.Injector calculator(CalculatorWiring.Scope s);
    }

    @Singleton
    @Component(modules = {LeRandomShit.class, SessionScope.class})
    public interface Globals {
        @Named("worker") Executor worker();
        @Named("ui") Executor ui();
        Logging log();
        @Named("top") LedditSession.State topStories();
        @Named("latest") LedditSession.State newStories();
        @Named("best") LedditSession.State bestStories();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        injector = DaggerLeRandomShit_Injector.builder().leRandomShit(this).build();
    }

    public static Injector getInjector() {
        return injector;
    }

    @Provides
    public static Globals globals() {
        if (globals == null) {
            globals = DaggerLeRandomShit_Globals.create();
        }
        return globals;
    }

    @Provides
    public Context context() {
        return this;
    }

    @Provides @Singleton
    public static Logging logger() {
        String tag = "mz";
        return new Logging() {
            @Override
            public void v(String text, Object... fmtArgs) {
                Log.v(tag, String.format(text, fmtArgs));
            }

            @Override
            public void v(String text, Throwable t) {
                Log.v(tag, text, t);
            }

            @Override
            public void d(String text, Object... fmtArgs) {
                Log.d(tag, String.format(text, fmtArgs));
            }

            @Override
            public void i(String text, Object... fmtArgs) {
                Log.i(tag, String.format(text, fmtArgs));
            }

            @Override
            public void w(String text, Object... fmtArgs) {
                Log.w(tag, String.format(text, fmtArgs));
            }

            @Override
            public void e(String text, Object... fmtArgs) {
                Log.e(tag, String.format(text, fmtArgs));
            }

            @Override
            public void e(String text, Throwable t) {
                Log.e(tag, text, t);
            }
        };
    }

    @Provides @Named("worker") @Singleton
    public static Executor worker() {
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(
                processors * 3,
                processors * 3,
                15, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>());
        tpe.allowCoreThreadTimeOut(true);
        return tpe;
    }

    @Provides @Named("ui") @Singleton
    public static Executor ui() {
        Looper main = Looper.getMainLooper();
        Handler handler = new Handler(main);
        Thread uiThread = main.getThread();
        return runnable -> {
            if (Thread.currentThread() == uiThread) {
                runnable.run();
            } else {
                handler.post(runnable);
            }
        };
    }
}
