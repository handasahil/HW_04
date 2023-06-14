import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This is a basic set of unit tests for ExternalChainingHashMap.
 *
 * Passing these tests doesn't guarantee any grade on these assignments. These
 * student JUnits that we provide should be thought of as a sanity check to
 * help you get started on the homework and writing JUnits in general.
 *
 * We highly encourage you to write your own set of JUnits for each homework
 * to cover edge cases you can think of for each data structure. Your code must
 * work correctly and efficiently in all cases, which is why it's important
 * to write comprehensive tests to cover as many cases as possible.
 *
 * @author Prajval Manivannan
 * @version 1.0
 */
public class PeerTest {

    private static final int TIMEOUT = 200;
    private ExternalChainingHashMap<Integer, String> map;

    private ExternalChainingHashMap<Integer, String> presetMap;
    private ExternalChainingMapEntry<Integer, String>[] expectedPreset;

    @Before
    public void setUp() {
        map = new ExternalChainingHashMap<>();

        presetMap = new ExternalChainingHashMap<>();
        presetMap.put(13, "A");
        presetMap.put(26, "B");
        presetMap.put(39, "C");
        presetMap.put(14, "D");
        presetMap.put(27, "E");
        presetMap.put(40, "F");
        presetMap.put(15, "G");
        presetMap.put(28, "H");
        //A "full" hashmap given LF = 0.67

        expectedPreset =
                (ExternalChainingMapEntry<Integer, String>[])
                        new ExternalChainingMapEntry[13];

        ExternalChainingMapEntry<Integer, String> firstRow =
                new ExternalChainingMapEntry<>(39, "C");
        firstRow.setNext(new ExternalChainingMapEntry<>(26, "B"));
        firstRow.getNext().setNext(new ExternalChainingMapEntry<>(13, "A"));

        ExternalChainingMapEntry<Integer, String> secondRow =
                new ExternalChainingMapEntry<>(40, "F");
        secondRow.setNext(new ExternalChainingMapEntry<>(27, "E"));
        secondRow.getNext().setNext(new ExternalChainingMapEntry<>(14, "D"));

        ExternalChainingMapEntry<Integer, String> thirdRow =
                new ExternalChainingMapEntry<>(28, "H");
        thirdRow.setNext(new ExternalChainingMapEntry<>(15, "G"));

        expectedPreset[0] = firstRow;
        expectedPreset[1] = secondRow;
        expectedPreset[2] = thirdRow;

        assertMapEquals(expectedPreset, presetMap.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testInitialization() {
        assertEquals(0, map.size());
        assertMapEquals(new ExternalChainingMapEntry[ExternalChainingHashMap
                .INITIAL_CAPACITY], map.getTable());
    }

    //Level 1 of Testing Put - No Chaining But Resizing
    @Test(timeout = TIMEOUT)
    public void testPutTheEnglishAlphabet() {
        // [(1, A), (2, B), (3, C), (4, D), (5, E), _, _, _, _, _, _, _, _]
        ExternalChainingMapEntry<Integer, String>[] expected =
                (ExternalChainingMapEntry<Integer, String>[])
                        new ExternalChainingMapEntry[ExternalChainingHashMap
                                .INITIAL_CAPACITY];
        for (int i = 1; i < 9; i++) {
            expected[i] = new ExternalChainingMapEntry<>(i,
                    (((String.valueOf(((char) (64 + i))))).toString()));
        }

        assertNull(map.put(1, "A"));
        assertNull(map.put(2, "B"));
        assertNull(map.put(3, "C"));
        assertNull(map.put(4, "D"));
        assertNull(map.put(5, "E"));
        assertNull(map.put(6, "F"));
        assertNull(map.put(7, "G"));
        assertNull(map.put(8, "H"));
        assertMapEquals(expected, map.getTable());
        //Resizing of backing table should occur here before LF = 8.71.
        expected =
                (ExternalChainingMapEntry<Integer, String>[])
                        new ExternalChainingMapEntry[27];
        for (int i = 1; i < 19; i++) {
            expected[i] = new ExternalChainingMapEntry<>(i,
                    (((String.valueOf(((char) (64 + i)))))));
        }

        assertNull(map.put(9, "I"));
        assertNull(map.put(10, "J"));
        assertNull(map.put(11, "K"));
        assertNull(map.put(12, "L"));
        assertNull(map.put(13, "M"));
        assertNull(map.put(14, "N"));
        assertNull(map.put(15, "O"));
        assertNull(map.put(16, "P"));
        assertNull(map.put(17, "Q"));
        assertNull(map.put(18, "R"));
        //Resizing of backing table should occur here before size exceeds 18.09.
        assertMapEquals(expected, map.getTable());
        expected =
                (ExternalChainingMapEntry<Integer, String>[])
                        new ExternalChainingMapEntry[55];
        /*
        Resizing of backing table would occur before size exceeds 36.85 --
        which it will not reach for this test. This is the last resize.
         */
        for (int i = 1; i < 27; i++) {
            expected[i] = new ExternalChainingMapEntry<>(i,
                    (((String.valueOf(((char) (64 + i)))))));
        }
        assertNull(map.put(19, "S"));
        assertNull(map.put(20, "T"));
        assertNull(map.put(21, "U"));
        assertNull(map.put(22, "V"));
        assertNull(map.put(23, "W"));
        assertNull(map.put(24, "X"));
        assertNull(map.put(25, "Y"));
        assertNull(map.put(26, "Z"));
        assertMapEquals(expected, map.getTable());

        assertEquals(26, map.size());
    }

    //Level 2 of Testing Put (Larger Numbers) - Chaining and Resizing
    @Test(timeout = TIMEOUT)
    public void testPutPokedexEntries() {
        assertNull(map.put(151, "Mew")); //Index 8
        assertNull(map.put(150, "Mewtwo")); //Index 7
        assertNull(map.put(149, "Dragonite")); //Index 6
        assertNull(map.put(6, "Charizard")); //Index 6
        assertNull(map.put(15, "Beedrill")); //Index 2
        assertNull(map.put(125, "Electabuzz")); //Index 8
        assertNull(map.put(28, "Sand")); //Index 2
        assertEquals("Sand", map.put(28, "Sandslash")); //Index 2
        assertNull(map.put(145, "Zapdos")); //Index 2
        //Resize of map occurs after adding here
        ExternalChainingMapEntry<Integer, String>[] expected =
                (ExternalChainingMapEntry<Integer, String>[])
                        new ExternalChainingMapEntry[ExternalChainingHashMap
                                .INITIAL_CAPACITY];
        /*
         null
         null
         Zapdos ~> Sandslash ~> Beedrill
         null
         null
         null
         Charizard ~> Dragonite
         Mewtwo
         Electabuzz ~> Mew
         null
         null
         null
         null
         null
         */
        expected[2] = new ExternalChainingMapEntry<>(145, "Zapdos");
        expected[2].setNext(new ExternalChainingMapEntry<>(28, "Sandslash"));
        expected[2].getNext().setNext(new ExternalChainingMapEntry<>(15,
                "Beedrill"));

        expected[6] = new ExternalChainingMapEntry<>(6, "Charizard");
        expected[6].setNext(new ExternalChainingMapEntry<>(149, "Dragonite"));

        expected[7] = new ExternalChainingMapEntry<>(150, "Mewtwo");

        expected[8] = new ExternalChainingMapEntry<>(125, "Electabuzz");
        expected[8].setNext(new ExternalChainingMapEntry<>(151, "Mew"));
        assertMapEquals(expected, map.getTable());
        assertEquals(8, map.size());
        assertEquals(13, map.getTable().length);
        assertNull(map.put(258, "Mudkip")); //Index 15 (in resized HashMap)
        assertEquals(9, map.size());
        //Resizes from 13 to 27 when size > 8.71
        expected =
                (ExternalChainingMapEntry<Integer, String>[])
                        new ExternalChainingMapEntry[27];

        assertEquals(27, map.getTable().length);
        expected[10] = new ExternalChainingMapEntry<>(145, "Zapdos");
        expected[1] = new ExternalChainingMapEntry<>(28, "Sandslash");

        expected[6] = new ExternalChainingMapEntry<>(6, "Charizard");
        expected[14] = new ExternalChainingMapEntry<>(149, "Dragonite");

        ExternalChainingMapEntry<Integer, String> mudkip =
                new ExternalChainingMapEntry<>(258, "Mudkip");
        ExternalChainingMapEntry<Integer, String> mewtwo =
                new ExternalChainingMapEntry<>(150, "Mewtwo");
        ExternalChainingMapEntry<Integer, String> beedrill =
                new ExternalChainingMapEntry<>(15, "Beedrill");

        mudkip.setNext(mewtwo);
        mewtwo.setNext(beedrill);
        expected[15] = mudkip;

        expected[17] = new ExternalChainingMapEntry<>(125, "Electabuzz");
        expected[16] = new ExternalChainingMapEntry<>(151, "Mew");

        //printHashTable();

        assertMapEquals(expected, map.getTable());
        assertEquals(9, map.size());
        /*
         null
         Sandslash
         null
         null
         null
         null
         Charizard
         null
         null
         null
         Zapdos
         null
         null
         null
         Dragonite
         Mudkip ~> Mewtwo ~> Beedrill
         Mew
         Electabuzz
         null
         null
         null
         null
         null
         null
         null
         null
         null
         */
    }

    @Test(timeout = TIMEOUT)
    public void testRemove() {
        //We want to remove keys (in this order): 13, 27, 28, 26, 39, 14, 40, 15
        //Removing 13
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals("A", presetMap.remove(13));
        expectedPreset[0].getNext().setNext(null);
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals(7, presetMap.size());

        //Removing 27
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals("E", presetMap.remove(27));
        expectedPreset[1].setNext(expectedPreset[1].getNext().getNext());
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals(6, presetMap.size());

        //Removing 28
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals("H", presetMap.remove(28));
        expectedPreset[2] = expectedPreset[2].getNext();
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals(5, presetMap.size());

        //Removing 26
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals("B", presetMap.remove(26));
        expectedPreset[0].setNext(null);
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals(4, presetMap.size());

        //Removing 39
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals("C", presetMap.remove(39));
        expectedPreset[0] = null;
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals(3, presetMap.size());

        //Removing 14
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals("D", presetMap.remove(14));
        expectedPreset[1].setNext(null);
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals(2, presetMap.size());

        //Removing 40
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals("F", presetMap.remove(40));
        expectedPreset[1] = null;
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals(1, presetMap.size());

        //Removing 15
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals("G", presetMap.remove(15));
        expectedPreset[2] = null;
        assertMapEquals(expectedPreset, presetMap.getTable());
        assertEquals(0, presetMap.size());

        expectedPreset =
                new ExternalChainingMapEntry[ExternalChainingHashMap.INITIAL_CAPACITY];

        assertMapEquals(expectedPreset, presetMap.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testGet() {
        // [(0, A), (1, B), (2, C), (3, D), (4, E), _, _, _, _, _, _, _, _]
        assertEquals("C", presetMap.get(39));
        assertEquals("B", presetMap.get(26));
        assertEquals("A", presetMap.get(13));
        assertEquals("F", presetMap.get(40));
        assertEquals("E", presetMap.get(27));
        assertEquals("D", presetMap.get(14));
        assertEquals("H", presetMap.get(28));
        assertEquals("G", presetMap.get(15));
        assertThrows(IllegalArgumentException.class, () -> presetMap.get(null));
        assertThrows(NoSuchElementException.class, () -> presetMap.get(41));

        ExternalChainingMapEntry<Integer, String>[] map = presetMap.getTable();

        for (int i = 3; i < map.length; i++) {
            assertNull(map[i]);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testContainsKey() {
        assertThrows(IllegalArgumentException.class, () -> presetMap.containsKey(null));

        Set<Integer> expectedKeys = new HashSet<>();
        expectedKeys.add(39);
        expectedKeys.add(26);
        expectedKeys.add(13);
        expectedKeys.add(40);
        expectedKeys.add(27);
        expectedKeys.add(14);
        expectedKeys.add(28);
        expectedKeys.add(15);

        for (Integer key: expectedKeys) {
            if (!presetMap.containsKey(key)) {
                throw new AssertionError("Preset Map not contain Key: " + key);
            }
        }
    }

    @Test(timeout = TIMEOUT)
    public void testKeySet() {
        Set<Integer> expectedKeys = new HashSet<>();
        expectedKeys.add(39);
        expectedKeys.add(26);
        expectedKeys.add(13);
        expectedKeys.add(40);
        expectedKeys.add(27);
        expectedKeys.add(14);
        expectedKeys.add(28);
        expectedKeys.add(15);

        assertEquals(expectedKeys, presetMap.keySet());
    }

    @Test(timeout = TIMEOUT)
    public void testValues() {
        List<String> expected = new LinkedList<>();
        expected.add("C");
        expected.add("B");
        expected.add("A");
        expected.add("F");
        expected.add("E");
        expected.add("D");
        expected.add("H");
        expected.add("G");

        assertEquals(expected, presetMap.values());
    }

    @Test(timeout = TIMEOUT)
    public void testResize() {
        assertNull(map.put(0, "A"));
        assertNull(map.put(1, "B"));
        assertNull(map.put(2, "C"));
        assertNull(map.put(3, "D"));
        assertNull(map.put(4, "E"));
        assertEquals(5, map.size());

        assertThrows(IllegalArgumentException.class, () -> map.resizeBackingTable(4));
        assertThrows(IllegalArgumentException.class, () -> map.resizeBackingTable(-4));

        map.resizeBackingTable(5);
        double loadFactor = (map.size() * 1.0) / map.getTable().length;
        assertEquals(1.0, loadFactor, 0);
        assertEquals(5, map.size());

        ExternalChainingMapEntry<Integer, String>[] expected =
                (ExternalChainingMapEntry<Integer, String>[])
                        new ExternalChainingMapEntry[5];
        expected[0] = new ExternalChainingMapEntry<>(0, "A");
        expected[1] = new ExternalChainingMapEntry<>(1, "B");
        expected[2] = new ExternalChainingMapEntry<>(2, "C");
        expected[3] = new ExternalChainingMapEntry<>(3, "D");
        expected[4] = new ExternalChainingMapEntry<>(4, "E");
        assertMapEquals(expected, map.getTable());

        map.resizeBackingTable(10);
        loadFactor = (map.size() * 1.0) / map.getTable().length;
        assertEquals(0.5, loadFactor, 0);
        assertEquals(5, map.size());

        map.resizeBackingTable(15);
        loadFactor = (map.size() * 1.0) / map.getTable().length;
        assertEquals((5.0 / 15), loadFactor, 0);
        assertEquals(5, map.size());

        map.resizeBackingTable(20);
        loadFactor = (map.size() * 1.0) / map.getTable().length;
        assertEquals((5.0 / 20), loadFactor, 0);
        assertEquals(5, map.size());

        //Now let's try with our preset map
        assertMapEquals(expectedPreset, presetMap.getTable());

        assertThrows(IllegalArgumentException.class, () -> presetMap.resizeBackingTable(7));
        presetMap.resizeBackingTable(10);
        loadFactor = (presetMap.size() * 1.0) / presetMap.getTable().length;
        assertEquals((8.0 / 10), loadFactor, 0);
        assertEquals(8, presetMap.size());

        presetMap.resizeBackingTable(15);
        loadFactor = (presetMap.size() * 1.0) / presetMap.getTable().length;
        assertEquals((8.0 / 15), loadFactor, 0);
        assertEquals(8, presetMap.size());

        presetMap.resizeBackingTable(20);
        loadFactor = (presetMap.size() * 1.0) / presetMap.getTable().length;
        assertEquals((8.0 / 20), loadFactor, 0);
        assertEquals(8, presetMap.size());

        presetMap.resizeBackingTable(13);
        loadFactor = (presetMap.size() * 1.0) / presetMap.getTable().length;
        assertEquals((8.0 / 13), loadFactor, 0);
        assertEquals(8, presetMap.size());

        presetMap.put(41, "I");
        loadFactor = (presetMap.size() * 1.0) / presetMap.getTable().length;
        assertEquals((9.0 / 27), loadFactor, 0);
        assertEquals(9, presetMap.size());

        //Let's test our map if this is resized properly
        expected = (ExternalChainingMapEntry<Integer, String>[])
                new ExternalChainingMapEntry[27];

        expected[0] = new ExternalChainingMapEntry<>(27, "E");
        expected[1] = new ExternalChainingMapEntry<>(28, "H");
        expected[12] = new ExternalChainingMapEntry<>(39, "C");
        expected[13] = new ExternalChainingMapEntry<>(40, "F");
        expected[13].setNext(new ExternalChainingMapEntry<>(13, "A"));
        expected[14] = new ExternalChainingMapEntry<>(41, "I");
        expected[14].setNext(new ExternalChainingMapEntry<>(14, "D"));
        expected[15] = new ExternalChainingMapEntry<>(15, "G");
        expected[26] = new ExternalChainingMapEntry<>(26, "B");
        //printHashTable(presetMap.getTable());
        assertMapEquals(expected, presetMap.getTable());

        presetMap.put(42, "J");
        expected[15] = new ExternalChainingMapEntry<>(42, "J");
        expected[15].setNext(new ExternalChainingMapEntry<>(15, "G"));
        assertMapEquals(expected, presetMap.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testClear() {
        // [(0, A), (1, B), (2, C), (3, D), (4, E), _, _, _, _, _, _, _, _]
        assertNull(map.put(0, "A"));
        assertNull(map.put(1, "B"));
        assertNull(map.put(2, "C"));
        assertNull(map.put(3, "D"));
        assertNull(map.put(4, "E"));
        assertEquals(5, map.size());

        map.clear();
        assertEquals(0, map.size());
        assertMapEquals(new ExternalChainingMapEntry[ExternalChainingHashMap
                .INITIAL_CAPACITY], map.getTable());
    }

    /**
     * Prints out the HashMap for easy display for user.
     * @param table the inputted HashMap
     */
    public void printHashTable(ExternalChainingMapEntry<Integer, String>[] table) {
        for (int i = 0; i < table.length; i++) {
            ExternalChainingMapEntry<Integer, String> entry = table[i];
            while (entry != null) {
                System.out.print(entry + " ~> ");
                entry = entry.getNext();
            }
            System.out.println();
        }
    }

    /**
     * This method verifies whether the backing array of the HashMap is
     * equivalent to a passed in expected array. The method iterates through
     * the expected array, and checks each node and verifies whether it exists
     * in the actual backing array -- if not, then throw AssertionError!
     *
     * @param expected the expected layout of the HashMap
     * @param actual the actual layout of the HashMap
     * @throws AssertionError if the HashMaps are not "equal" in content
     */
    public void assertMapEquals(ExternalChainingMapEntry<Integer, String>[] expected,
                                ExternalChainingMapEntry<Integer, String>[] actual) {

        if (expected.length != actual.length) {
            throw new AssertionError("Map backing lengths are different");
        }

        for (int i = 0; i < expected.length; i++) {
            ExternalChainingMapEntry<Integer, String> expectedEntry =
                    expected[i];
            ExternalChainingMapEntry<Integer, String> actualEntry =
                    actual[i];
            while (expectedEntry != null || actualEntry != null) {
                try {
                    if (!expectedEntry.equals(actualEntry)) {
                        throw new AssertionError("Maps are not equal at " + i
                                + "th index");
                    }
                } catch (NullPointerException ex) {
                    throw new AssertionError("Maps are not equal at " + i
                            + "th index");
                }
                expectedEntry = expectedEntry.getNext();
                actualEntry = actualEntry.getNext();
            }
        }
    }

}