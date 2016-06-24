package ph.codeia.lerandomshit.di;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

/**
 * This file is a part of the Le Random Shit project.
 */
@Module
public class CurrentContext {
    private final AppCompatActivity activity;
    private final Application application;

    public CurrentContext(AppCompatActivity activity) {
        this.activity = activity;
        application = activity.getApplication();
    }

    @Provides
    Application application() {
        return application;
    }

    @Provides
    AppCompatActivity activity() {
        return activity;
    }

    @Provides
    Context context() {
        return application;
    }
}
