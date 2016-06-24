package ph.codeia.lerandomshit.leddit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * This file is a part of the Le Random Shit project.
 */
@RunWith(Parameterized.class)
public class MaterializeBadOffsetsTest {
    private static TestLedditInjector injector;

    @BeforeClass
    public static void setupInjector() {
        injector = DaggerTestLedditInjector.create();
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> params() {
        return Arrays.asList(new Object[][]{
                {"start < end", 0, -1},
                {"start == end", 1, 1},
                {"start == size", 6, 7},
                {"start > size", 7, 10},
        });
    }

    @Parameterized.Parameter(0)
    public String description;

    @Parameterized.Parameter(1)
    public int start;

    @Parameterized.Parameter(2)
    public int endExclusive;

    @Inject
    RestService service;

    private final List<Long> ids = Arrays.asList(0L, 1L, 2L, 3L, 4L, 5L);

    @Before
    public void setup() {
        injector.inject(this);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void doesntFireCallbacks() {
        service.materialize(ids, start, endExclusive, this::noop);
    }

    private void noop(List iDontCare) {
        Assert.fail("this shouldn't have been called.");
    }
}
