package ph.codeia.lerandomshit.di;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ph.codeia.lerandomshit.util.PerActivity;

/**
 * This file is a part of the Le Random Shit project.
 */
@Module
public abstract class ActivityModule {

    @Binds @PerActivity
    abstract Activity activity(AppCompatActivity a);

    @Provides
    static LinearLayoutManager linear(Activity a) {
        return new LinearLayoutManager(a);
    }

    @Provides
    static LayoutInflater inflater(Activity a) {
        return LayoutInflater.from(a);
    }
}
