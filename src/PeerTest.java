import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PeerTest {
    private static final int TIMEOUT = 200;
    private ExternalChainingHashMap<Integer, String> map1;
    private ExternalChainingHashMap<String, String> map2;

    @Before
    public void setUp() {
        map1 = new ExternalChainingHashMap<>();
        map2 = new ExternalChainingHashMap<>();
    }

    @Test(timeout = TIMEOUT)
    public void initialization() {
        assertEquals(0, map1.size());
        assertNull(map1.getTable()[0]);
        assertEquals(13, map1.getTable().length);
    }

    @Test(timeout = TIMEOUT)
    @SuppressWarnings("unchecked")
    public void testPut() {
        assertThrows(IllegalArgumentException.class, () -> map1.put(null, "Johnny"));
        assertThrows(IllegalArgumentException.class, () -> map1.put(2, null));
        assertThrows(IllegalArgumentException.class, () -> map1.put(null, null));

        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(3, "d"));
        assertNull(map1.put(4, "e"));
        assertNull(map1.put(5, "f"));
        assertNull(map1.put(6, "h"));
        assertEquals(7, map1.size());
        assertEquals("h", map1.put(6,"g"));
        assertEquals(7, map1.size());
        assertNull(map1.put(7, "h"));
        assertEquals(8, map1.size());
        assertEquals(13, map1.getTable().length);

        assertNull(map1.put(8, "i"));
        assertEquals(9, map1.size());
        assertEquals(27, map1.getTable().length);

        map1.clear();

        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(26, "b")); //@ index 0
        assertNull(map1.put(39, "c")); //@ index 0
        assertNull(map1.put(52, "d")); //@ index 0
        assertEquals(6, map1.size());
        assertEquals(13, map1.getTable().length);
        assertEquals("d", map1.getTable()[0].getValue());
        assertEquals("c", map1.getTable()[0].getNext().getValue());
        assertEquals("b", map1.getTable()[0].getNext().getNext().getValue());
        assertEquals("a", map1.getTable()[0].getNext().getNext().getNext().getValue());
        assertEquals("c", map1.put(39, "z"));
        assertEquals(6, map1.size());
        assertEquals("z", map1.getTable()[0].getNext().getValue());
        assertNull(map1.put(28, "g")); //@ index 2
        assertNull(map1.put(41, "l")); //@ index 2
        assertEquals("l", map1.getTable()[2].getValue());
        assertEquals("g", map1.getTable()[2].getNext().getValue());
        assertEquals("c", map1.getTable()[2].getNext().getNext().getValue());
        assertEquals(8, map1.size());
        assertNull(map1.put(4, "p"));
        assertEquals(9, map1.size());

        ExternalChainingMapEntry<Integer, String>[] arr = map1.getTable();
        for(int i = 0; i < arr.length; i++) {
            if (i == 1) {
                continue;
            }
            if (arr[i] != null) {
                assertNull(arr[i].getNext());
            }
        }
        assertEquals("g", arr[1].getValue());
        assertEquals("b", arr[1].getNext().getValue());
        assertNull(arr[1].getNext().getNext());
        assertEquals("a", arr[0].getValue());
        assertEquals("d", arr[25].getValue());
    }

    @Test(timeout = TIMEOUT)
    public void testResize() {
        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(3, "d"));
        assertNull(map1.put(4, "e"));
        assertNull(map1.put(5, "f"));
        assertNull(map1.put(6, "h"));

        assertThrows(IllegalArgumentException.class, () -> map1.resizeBackingTable(3));
        map1.resizeBackingTable(7);
        ExternalChainingMapEntry<Integer, String>[] arr = map1.getTable();
        assertEquals(7, map1.getTable().length);
        assertEquals(7, map1.size());
        assertEquals("a", arr[0].getValue());
        assertEquals("b", arr[1].getValue());
        assertEquals("c", arr[2].getValue());
        assertEquals("d", arr[3].getValue());
        assertEquals("e", arr[4].getValue());
        assertEquals("f", arr[5].getValue());
        assertEquals("h", arr[6].getValue());

        map1.clear();

        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(26, "b")); //2
        assertNull(map1.put(39, "c")); //3
        assertNull(map1.put(52, "d")); //4
        map1.resizeBackingTable(6);
        ExternalChainingMapEntry<Integer, String>[] arr1 = map1.getTable();
        assertEquals("a", arr1[0].getValue());
        assertEquals("b", arr1[1].getValue());
        assertEquals("c", arr1[2].getValue());
        assertEquals("b", arr1[2].getNext().getValue());
        assertEquals("c", arr1[3].getValue());
        assertEquals("d", arr1[4].getValue());
        for(int i = 0; i < arr1.length; i++) {
            if (i == 2) {
                continue;
            }
            if (arr[i] != null) {
                assertNull(arr[i].getNext());
            }
        }
    }

    @Test(timeout = TIMEOUT)
    public void testRemove() {
        assertThrows(IllegalArgumentException.class, () -> map1.remove(null));
        assertThrows(NoSuchElementException.class, () -> map1.remove(5));

        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(3, "d"));
        assertNull(map1.put(4, "e"));
        assertNull(map1.put(5, "f"));
        assertNull(map1.put(6, "h"));


        assertEquals(13, map1.getTable().length);
        assertEquals("h", map1.remove(6));
        assertEquals(6, map1.size());
        assertNull(map1.getTable()[6]);
        assertEquals("f", map1.remove(5));
        assertEquals(5, map1.size());
        assertNull(map1.getTable()[5]);
        assertEquals("e", map1.remove(4));
        assertEquals(4, map1.size());
        assertNull(map1.getTable()[4]);
        assertEquals("d", map1.remove(3));
        assertEquals(3, map1.size());
        assertNull(map1.getTable()[3]);
        assertEquals("c", map1.remove(2));
        assertEquals(2, map1.size());
        assertNull(map1.getTable()[2]);
        assertEquals("b", map1.remove(1));
        assertEquals(1, map1.size());
        assertNull(map1.getTable()[1]);
        assertEquals("a", map1.remove(0));
        assertNull(map1.getTable()[0]);
        assertEquals(0, map1.size());
        assertEquals(13, map1.getTable().length);

        map1.clear();

        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(26, "b"));
        assertNull(map1.put(39, "c"));
        assertNull(map1.put(52, "d"));
        assertEquals("c", map1.getTable()[0].getNext().getValue());
        assertEquals("c", map1.remove(39));
        assertEquals("b", map1.getTable()[0].getNext().getValue());
        assertEquals("a", map1.getTable()[0].getNext().getNext().getValue());
        assertNull(map1.getTable()[0].getNext().getNext().getNext());
        assertEquals(5, map1.size());
        assertEquals("d", map1.remove(52));
        assertEquals("a", map1.getTable()[0].getNext().getValue());
        assertEquals("b", map1.getTable()[0].getValue());
        assertNull(map1.put(52, "d"));
        assertNull(map1.put(39, "c")); // @index 0, c -> d -> b -> a
        assertEquals("b", map1.remove(26));
        assertEquals("a", map1.getTable()[0].getNext().getNext().getValue());
        map1.put(26, "b");
        assertEquals("b", map1.remove(26));
        assertEquals("a", map1.getTable()[0].getNext().getNext().getValue());
        map1.remove(52);
        map1.remove(39);
        map1.remove(0);
        assertNull(map1.getTable()[0]);
    }

    @Test(timeout = TIMEOUT)
    public void getTest() {
        assertThrows(IllegalArgumentException.class, () -> map1.get(null));
        assertThrows(NoSuchElementException.class, () -> map1.get(5));

        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(3, "d"));
        assertNull(map1.put(4, "e"));
        assertNull(map1.put(5, "f"));
        assertNull(map1.put(6, "h"));

        assertEquals("a", map1.get(0));
        assertEquals("b", map1.get(1));
        assertEquals("c", map1.get(2));
        assertEquals("d", map1.get(3));
        assertEquals("e", map1.get(4));
        assertEquals("f", map1.get(5));
        assertEquals("h", map1.get(6));
        assertEquals(7, map1.size());

        map1.clear();

        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(26, "b"));
        assertNull(map1.put(39, "c"));
        assertNull(map1.put(52, "d"));

        assertEquals(6, map1.size());
        assertEquals("b", map1.get(26));
        assertEquals("c", map1.get(39));
        assertEquals("d", map1.get(52));
        assertEquals(6, map1.size());

        assertThrows(NoSuchElementException.class, () -> map1.get(69));
        assertThrows(NoSuchElementException.class, () -> map1.get(78));
    }

    @Test(timeout = TIMEOUT)
    public void containsTest() {
        assertThrows(IllegalArgumentException.class, () -> map1.containsKey(null));

        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(3, "d"));
        assertNull(map1.put(4, "e"));
        assertNull(map1.put(5, "f"));
        assertNull(map1.put(6, "h"));

        assertTrue(map1.containsKey(0));
        assertTrue(map1.containsKey(1));
        assertTrue(map1.containsKey(2));
        assertTrue(map1.containsKey(3));
        assertTrue(map1.containsKey(4));
        assertTrue(map1.containsKey(5));
        assertTrue(map1.containsKey(6));
        assertFalse(map1.containsKey(69));
        assertFalse(map1.containsKey(59));
        assertFalse(map1.containsKey(54));

        map1.clear();

        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(26, "b"));
        assertNull(map1.put(39, "c"));
        assertNull(map1.put(52, "d"));

        assertTrue(map1.containsKey(26));
        assertTrue(map1.containsKey(39));
        assertTrue(map1.containsKey(52));
        assertTrue(map1.containsKey(0));
    }

    @Test(timeout = TIMEOUT)
    public void keySetTest() {
        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(3, "d"));
        assertNull(map1.put(4, "e"));
        assertNull(map1.put(5, "f"));
        assertNull(map1.put(6, "h"));

        Set<Integer> keySet = map1.keySet();

        assertTrue(keySet.size() == map1.size());
        for(Integer a : keySet) {
            assertTrue(map1.containsKey(a));
        }

        map1.clear();

        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(26, "b"));
        assertNull(map1.put(39, "c"));
        assertNull(map1.put(52, "d"));

        keySet = map1.keySet();
        assertTrue(keySet.size() == map1.size());
        for(Integer a : keySet) {
            assertTrue(map1.containsKey(a));
        }
    }

    @Test(timeout = TIMEOUT)
    public void valuesTest() {
        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(3, "d"));
        assertNull(map1.put(4, "e"));
        assertNull(map1.put(5, "f"));
        assertNull(map1.put(6, "h"));

        List<String> values = map1.values();

        assertTrue(values.size() == map1.size());
        assertEquals("a", values.get(0));
        assertEquals("b", values.get(1));
        assertEquals("c", values.get(2));
        assertEquals("d", values.get(3));
        assertEquals("e", values.get(4));
        assertEquals("f", values.get(5));

        map1.clear();

        assertNull(map1.put(0, "a"));
        assertNull(map1.put(1, "b"));
        assertNull(map1.put(2, "c"));
        assertNull(map1.put(26, "b"));
        assertNull(map1.put(39, "c"));
        assertNull(map1.put(52, "d"));

        values = map1.values();
        assertTrue(values.size() == map1.size());
        assertEquals("d", values.get(0));
        assertEquals("c", values.get(1));
        assertEquals("b", values.get(2));
        assertEquals("a", values.get(3));
        assertEquals("b", values.get(4));
        assertEquals("c", values.get(5));
    }
}