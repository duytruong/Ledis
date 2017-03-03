package my.duyrau.ledis.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by duyrau on 3/3/17.
 */
public class DataStore {

    private static final Map<String, Object> table = new HashMap<>();

    public boolean containsKey(String key) {
        return table.containsKey(key);
    }

    public Object get(String key) {
        return table.get(key);
    }

    public Object put(String key, Object value) {
        return table.put(key, value);
    }

    public String listAllKeys() {
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Object> entry : table.entrySet()) {
            result.add(entry.getKey());
        }
        return result.toString();
    }

    /**
     * Check value in given key is not a list.
     * @param key
     * @return true if the value that key holding is not a list, false otherwise or key is not exist.
     */
    public boolean valueIsNotList(String key) {
        if (containsKey(key)) {
            Object value = table.get(key);
            return !(value instanceof my.duyrau.ledis.lib.Deque<?>);
        }
        return false;
    }

    /**
     * Check value in given key is not a set.
     * @param key
     * @return true if the value that key holding is not a set, false otherwise or key is not exist.
     */
    public boolean valueIsNotSet(String key) {
        if (containsKey(key)) {
            Object value = table.get(key);
            return !(value instanceof Set<?>);
        }
        return false;
    }
}
