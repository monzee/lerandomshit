package ph.codeia.lerandomshit.leddit;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import ph.codeia.lerandomshit.util.Consumer;
import ph.codeia.lerandomshit.util.Logging;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * This file is a part of the Le Random Shit project.
 */
public class RestService implements FrontPageContract.Service {

    public interface Api {
        @GET("topstories.json")
        Call<List<Integer>> topStories();

        @GET("newstories.json")
        Call<List<Integer>> newStories();

        @GET("beststories.json")
        Call<List<Integer>> bestStories();

        @GET("item/{id}.json")
        Call<Hn.Story> story(@Path("id") int id);
    }

    @Inject
    Logging log;

    @Inject @Named("worker")
    Executor worker;

    @Inject @Named("ui")
    Executor ui;

    @Inject @Named("page_size")
    int perPage = 20;

    private final Api get;

    @Inject
    public RestService(Retrofit retrofit) {
        get = retrofit.create(Api.class);
    }

    public void getStory(int id, Consumer<Hn.Story> then) {
        get.story(id).enqueue(new Callback<Hn.Story>() {
            @Override
            public void onResponse(Call<Hn.Story> call, Response<Hn.Story> response) {
                then.accept(response.body());
            }

            @Override
            public void onFailure(Call<Hn.Story> call, Throwable t) {
                log.e("mz", "getStory failed.", t);
                then.accept(null);
            }
        });
    }

    @Override
    public void getTopStories(Consumer<List<Hn.Story>> then) {
        get.topStories().enqueue(new MakeStoriesFromIds(then, "getTopStories"));
    }

    @Override
    public void getNewStories(Consumer<List<Hn.Story>> then) {
        get.newStories().enqueue(new MakeStoriesFromIds(then, "getNewStories"));
    }

    @Override
    public void getBestStories(Consumer<List<Hn.Story>> then) {
        get.bestStories().enqueue(new MakeStoriesFromIds(then, "getBestStories"));
    }

    private class MakeStoriesFromIds implements Callback<List<Integer>> {
        private final String caller;
        private final Consumer<List<Hn.Story>> then;

        private MakeStoriesFromIds(Consumer<List<Hn.Story>> then, String caller) {
            this.caller = caller;
            this.then = then;
        }

        @Override
        public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
            List<Integer> ids = response.body();
            List<Hn.Story> stories = Arrays.asList(new Hn.Story[perPage]);
            mapIdsToStories(ids, stories, then);
        }

        @Override
        public void onFailure(Call<List<Integer>> call, Throwable t) {
            log.e("mz", caller + " failed.", t);
            then.accept(null);
        }

        private void mapIdsToStories(
                List<Integer> ids,
                List<Hn.Story> stories,
                Consumer<List<Hn.Story>> then
        ) {
            int count = stories.size();
            CountDownLatch barrier = new CountDownLatch(count);
            Set<Thread> pending = Collections.synchronizedSet(new HashSet<>());
            worker.execute(() -> {
                try {
                    if (!barrier.await(30, TimeUnit.SECONDS)) {
                        for (Thread t : pending) {
                            t.interrupt();
                            pending.remove(t);
                        }
                    }
                    ui.execute(() -> then.accept(stories));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    for (Thread t : pending) {
                        t.interrupt();
                        pending.remove(t);
                    }
                }
            });
            for (int i = 0; i < count; i++) {
                final int index = i;
                worker.execute(() -> {
                    Thread t = Thread.currentThread();
                    pending.add(t);
                    CountDownLatch request = new CountDownLatch(1);
                    Call<Hn.Story> call = get.story(ids.get(index));
                    call.enqueue(new Callback<Hn.Story>() {
                        @Override
                        public void onResponse(Call<Hn.Story> call, Response<Hn.Story> response) {
                            stories.set(index, response.body());
                            request.countDown();
                        }

                        @Override
                        public void onFailure(Call<Hn.Story> call, Throwable t) {
                            log.e("get story failed.", t);
                            request.countDown();
                        }
                    });
                    try {
                        request.await();
                    } catch (InterruptedException e) {
                        call.cancel();
                    } finally {
                        barrier.countDown();
                        pending.remove(t);
                    }
                });
            }
        }
    }
}
