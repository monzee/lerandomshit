package ph.codeia.lerandomshit.leddit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import ph.codeia.lerandomshit.util.BiConsumer;
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
public class RestService implements FrontPage.DataSource {

    public interface Api {
        @GET("topstories.json")
        Call<List<Long>> topStories();

        @GET("newstories.json")
        Call<List<Long>> newStories();

        @GET("beststories.json")
        Call<List<Long>> bestStories();

        @GET("item/{id}.json")
        Call<Hn.Story> story(@Path("id") long id);

        @GET("item/{id}.json")
        Call<Hn.Post> post(@Path("id") long id);
    }

    public static class NotFound extends RuntimeException {}
    public static class Denied extends RuntimeException {}

    private class OnResponse<T> implements Callback<T> {
        private final Consumer<T> okHandler;
        private final Consumer<Throwable> errorHandler;

        private OnResponse(@NonNull Consumer<T> okHandler) {
            this(okHandler, null);
        }

        private OnResponse(
                @NonNull Consumer<T> okHandler,
                @Nullable Consumer<Throwable> errorHandler
        ) {
            this.okHandler = okHandler;
            this.errorHandler = errorHandler;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            int code = response.code();
            if (response.isSuccessful()) {
                okHandler.accept(response.body());
            } else if (code == 404 || code == 410) {
                onFailure(call, new NotFound());
            } else if (code == 400 || code == 403) {
                onFailure(call, new Denied());
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            if (errorHandler != null) {
                errorHandler.accept(t);
            } else {
                log.e("api call failed.", t);
            }
        }
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

    @Override
    public void getPage(FrontPage.Page which, Consumer<List<Long>> then) {
        switch (which) {
            case TOP:
                get.topStories().enqueue(new OnResponse<>(then));
                break;
            case LATEST:
                get.newStories().enqueue(new OnResponse<>(then));
                break;
            case BEST:
                get.bestStories().enqueue(new OnResponse<>(then));
                break;
            default:
                break;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends FrontPage.Post> void getPost(int id, Consumer<T> then) {
        get.post(id).enqueue(new OnResponse<>(p -> then.accept((T) p)));
    }

    @Override
    public void materialize(
            List<Long> ids,
            int start,
            int endExclusive,
            Consumer<List<FrontPage.Post>> then
    ) {
        materialize(ids, start, endExclusive, null, then);
    }

    @Override
    public void materialize(
            List<Long> ids,
            int start,
            int endExclusive,
            BiConsumer<FrontPage.Post, Integer> eachWithIndex,
            Consumer<List<FrontPage.Post>> then
    ) {
        if (endExclusive <= start) {
            throw new IndexOutOfBoundsException("zero or negative size result");
        }
        if (start >= ids.size() || start + endExclusive >= ids.size()) {
            throw new IndexOutOfBoundsException("id list not long enough");
        }
        int count = endExclusive - start;
        List<FrontPage.Post> items = Arrays.asList(new FrontPage.Post[count]);
        CountDownLatch jobs = new CountDownLatch(count);
        Set<Thread> inProgress = Collections.synchronizedSet(new HashSet<>());
        worker.execute(() -> {
            try {
                if (jobs.await(30, TimeUnit.SECONDS)) {
                    ui.execute(() -> then.accept(items));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                for (Iterator<Thread> it = inProgress.iterator(); it.hasNext();) {
                    it.next().interrupt();
                    it.remove();
                }
            }
        });
        for (int i = start; i < endExclusive; i++) {
            final int index = i;
            worker.execute(() -> {
                Thread t = Thread.currentThread();
                inProgress.add(t);
                CountDownLatch job = new CountDownLatch(1);
                Call<Hn.Post> request = get.post(ids.get(index));
                request.enqueue(new OnResponse<>(post -> {
                    items.set(index - start, post);
                    if (eachWithIndex != null) {
                        ui.execute(() -> eachWithIndex.accept(post, index));
                    }
                    job.countDown();
                }, e -> {
                    log.e("get story failed", e);
                    job.countDown();
                }));
                try {
                    job.await();
                } catch (InterruptedException e) {
                    request.cancel();
                } finally {
                    jobs.countDown();
                    inProgress.remove(t);
                }
            });
        }
    }
}
