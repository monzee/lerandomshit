package ph.codeia.lerandomshit.leddit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import ph.codeia.lerandomshit.util.Logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This file is a part of the Le Random Shit project.
 */
public class RestServiceTest {
    private static TestLedditInjector injector;

    @Inject
    RestService service;

    @Inject @Named("page_size")
    int pageSize;

    @Inject
    Logging log;

    @BeforeClass
    public static void setupInjector() {
        injector = DaggerTestLedditInjector.create();
    }

    @Before
    public void setup() {
        injector.inject(this);
    }

    @Test
    public void getOneStory() {
        CountDownLatch c = new CountDownLatch(1);
        service.getStory(8863, story -> {
            c.countDown();
            assertEquals("dhouston", story.by);
            assertEquals("My YC app: Dropbox - Throw away your USB drive", story.title);
        });
        try {
            assertTrue("timeout", c.await(30, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("interrupted.");
        }
    }

    @Test
    public void topStories() {
        CountDownLatch c = new CountDownLatch(1);
        service.getTopStories(xs -> {
            c.countDown();
            assertEquals(pageSize, xs.size());
            for (Hn.Story s : xs) {
                assertTrue(s.id > 0);
                assertNotNull(s.title);
            }
        });
        try {
            assertTrue("timeout", c.await(30, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("interrupted.");
        }
    }

    @Test
    public void newStories() {
        CountDownLatch c = new CountDownLatch(1);
        service.getNewStories(xs -> {
            c.countDown();
            assertEquals(pageSize, xs.size());
            for (Hn.Story s : xs) {
                assertTrue(s.id > 0);
                assertNotNull(s.title);
                log.i("%d: %s", s.id, s.title);
            }
        });
        try {
            assertTrue("timeout", c.await(30, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("interrupted.");
        }
    }

    @Test
    public void bestStories() {
        CountDownLatch c = new CountDownLatch(1);
        service.getBestStories(xs -> {
            c.countDown();
            assertEquals(pageSize, xs.size());
            for (Hn.Story s : xs) {
                assertTrue(s.id > 0);
                assertNotNull(s.title);
            }
        });
        try {
            assertTrue("timeout", c.await(30, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("interrupted.");
        }
    }
}
