package ph.codeia.lerandomshit;

import android.app.Application;

public class LeRandomShit extends Application {
    private ApplicationScope injector;

    @Override
    public void onCreate() {
        super.onCreate();
        injector = DaggerApplicationScope.create();
    }

    public ApplicationScope getInjector() {
        return injector;
    }
}
