package ph.codeia.lerandomshit.leddit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import ph.codeia.lerandomshit.util.Logging;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


/**
 * This file is a part of the Le Random Shit project.
 */
public class RestServiceTest {
    private static TestLedditInjector injector;

    @Inject
    RestService service;

    @Inject
    Logging log;

    @Rule
    public Timeout globalTimeout = new Timeout(15, TimeUnit.SECONDS);

    private final CountDownLatch done = new CountDownLatch(1);
    private final int pageSize = 10;

    @BeforeClass
    public static void setupInjector() {
        injector = DaggerTestLedditInjector.create();
    }

    @Before
    public void setup() {
        injector.inject(this);
    }

    @Test
    public void getOneStory() throws InterruptedException {
        service.<Hn.Story>getPost(8863, story -> {
            done.countDown();
            assertEquals("dhouston", story.by);
            assertEquals("My YC app: Dropbox - Throw away your USB drive", story.title);
        });
        done.await();
    }

    @Test
    public void getTopPostIds() throws InterruptedException {
        service.getPage(FrontPage.Page.TOP, ids -> {
            done.countDown();
            assertTrue(ids.size() >= 200);
        });
        done.await();
    }

    @Test
    public void materializeTopTenPosts() throws InterruptedException {
        service.getPage(FrontPage.Page.TOP, ids -> service.materialize(ids, 0, pageSize, posts -> {
            done.countDown();
            assertThat(posts.size(), is(pageSize));
            int i = 0;
            for (FrontPage.Post p : posts) {
                assertThat(p.getId(), is(ids.get(i++)));
                assertNotNull(p.getTitle());
                assertNotNull(p.getBy());
                assertNotNull(p.getDate());
                assertThat(p, is(instanceOf(Hn.Story.class)));
            }
        }));
        done.await();
    }

    @Test
    public void materializeNextTenPosts() throws InterruptedException {
        service.getPage(FrontPage.Page.TOP, ids -> {
            service.materialize(ids, pageSize, pageSize * 2, posts -> {
                done.countDown();
                assertThat(posts.size(), is(pageSize));
                int i = 0;
                for (FrontPage.Post p : posts) {
                    assertThat(p.getId(), is(ids.get(pageSize + i++)));
                    assertNotNull(p.getTitle());
                    assertNotNull(p.getBy());
                    assertNotNull(p.getDate());
                    assertThat(p, is(instanceOf(FrontPage.Post.class)));
                }
            });
        });
        done.await();
    }

    @Test
    public void materializeOneByOne() throws InterruptedException {
        final AtomicInteger pending = new AtomicInteger(pageSize);
        service.getPage(FrontPage.Page.TOP, ids -> {
            service.materialize(ids, 0, pageSize, (post, i) -> {
                assertThat(post.getId(), is(ids.get(i)));
                pending.decrementAndGet();
            }, posts -> {
                done.countDown();
                assertThat(pending.get(), is(0));
            });
        });
        done.await();
    }
}
