package ph.codeia.lerandomshit.di;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ph.codeia.lerandomshit.util.Logging;

/**
 * This file is a part of the Le Random Shit project.
 */
@Module
public abstract class GlobalModule {

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
