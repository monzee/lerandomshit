package ph.codeia.lerandomshit;

import android.app.Application;

public class LeRandomShit extends Application {
    private ApplicationWiring.Injector injector;

    @Override
    public void onCreate() {
        super.onCreate();
        injector = DaggerApplicationWiring_Injector.create();
    }

    public ApplicationWiring.Injector getInjector() {
        return injector;
    }
}
