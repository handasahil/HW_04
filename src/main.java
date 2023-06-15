import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class main {
    public static void main(String[] args) {
        ExternalChainingHashMap<Integer, String> presetMap = new ExternalChainingHashMap<>();

        presetMap.put(13, "A");
        presetMap.put(26, "B");
        presetMap.put(39, "C");
        presetMap.put(14, "D");
        presetMap.put(27, "E");
        presetMap.put(40, "F");
        presetMap.put(15, "G");
        presetMap.put(28, "H");


        ExternalChainingHashMap<Integer, String> map = new ExternalChainingHashMap<>();

        for (int i = 0; i < 26; i++) {
            map.put(i * 13, (char) (i + 65) + "");
        }

        System.out.println(map.get(104));
        System.out.println(map.getTable().length);
        System.out.println(map.keySet());
        System.out.println(map.values());



        ExternalChainingHashMap<Integer, String> get = new ExternalChainingHashMap<>();
        get.put(2, "C");
        get.put(0, "A");
        get.put(1, "B");
        get.put(3, "D");
        get.put(4, "E");
        get.put(14, "F");
        System.out.println(get.get(1));


    }
}
