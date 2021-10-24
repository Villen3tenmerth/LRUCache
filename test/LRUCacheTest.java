import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LRUCacheTest {
    private LRUCache<Integer, Integer> cache;
    private final static int DEFAULT_CAPACITY = 10;

    private void createCache(int capacity) {
        cache = new LRUCache<>(capacity);
    }

    @BeforeEach
    public void initCache() {
        createCache(DEFAULT_CAPACITY);
    }

    @Test
    public void emptyTest() {
        assertTrue(cache.isEmpty());
    }

    @Test
    public void addTest() {
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);
        assertEquals(10, cache.get(1));
        assertEquals(20, cache.get(2));
        assertEquals(30, cache.get(3));
    }

    @Test
    public void replaceTest() {
        assertNull(cache.put(1, 10));
        assertEquals(10, cache.put(1, 20));
    }

    @Test
    public void mostRecentTest() {
        cache.put(1, 10);
        cache.put(2, 20);
        assertEquals(2, cache.getMostRecentKey());
        assertEquals(20, cache.getMostRecentValue());
        cache.get(1);
        assertEquals(1, cache.getMostRecentKey());
        assertEquals(10, cache.getMostRecentValue());
    }

    @Test
    public void displaceTest() {
        createCache(2);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);
        assertNull(cache.get(1));
        assertEquals(20, cache.get(2));
        assertEquals(30, cache.get(3));
    }
}
