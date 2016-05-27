package ph.codeia.lerandomshit;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import dagger.Module;
import dagger.Provides;

/**
 * This file is a part of the Le Random Shit project.
 */
@Module
public class ActivityScope {
    private final AppCompatActivity activity;

    public ActivityScope(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    public Activity activity() {
        return activity;
    }

    @Provides
    public LayoutInflater inflater() {
        return LayoutInflater.from(activity);
    }
}
