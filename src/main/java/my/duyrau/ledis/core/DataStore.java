package my.duyrau.ledis.core;

import my.duyrau.ledis.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by duyrau on 3/3/17.
 */
public class DataStore {

    private static final Map<String, Object> table = new HashMap<>();

    /**
     * Store the moment when timeout is set (1st item) and the timeout (2nd item)
     */
    private static final Map<String, Long[]> timeout = new HashMap<>();

    private boolean outOfTime(String key) {
        if (timeout.containsKey(key)) {
            long currentMilliseconds = System.currentTimeMillis();
            long momentWhenSet = timeout.get(key)[0];
            long timeOut = timeout.get(key)[1] * 1000;
            if (currentMilliseconds - momentWhenSet > timeOut) {
                return true;
            }
        }
        return false;
    }

    public boolean containsKey(String key) {
        if (outOfTime(key)) {
            table.remove(key);
        }
        return table.containsKey(key);
    }

    public Object get(String key) {
        return table.get(key);
    }

    public Object put(String key, Object value) {
        timeout.remove(key); // clear timeout
        return table.put(key, value);
    }

    public void clearTimeoutOfOneKey(String key) {
        timeout.remove(key);
    }

    public void clearTimeoutOfAllKeys() {
        timeout.clear();
    }

    /**
     * Return all available keys
     * @return list of keys
     */
    public String listAllKeys() {
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Object> entry : table.entrySet()) {
            // if key expired, don't add
            if (table.containsKey(entry.getKey())) {
                result.add(entry.getKey());
            }
        }
        return result.toString();
    }

    /**
     * Delete a key
     * @param key the key
     * @return 1 if the key were removed, 0 if the key doesn't exist
     */
    public int deleteKey(String key) {
        if (containsKey(key)) {
            table.remove(key);
            return 1;
        }
        return 0;
    }

    /***
     * Delete all keys
     * @return OK
     */
    public String deleteAllKeys() {
        table.clear();
        return Constant.RESPONSE_OK;
    }

    /**
     * Set timeout on a key
     * @param key the key
     * @param seconds the timeout
     * @return 1 if the timeout is set, 0 if key doesn't exist or the timeout could not be set
     */
    public Long setTimeoutOnKey(String key, int seconds) {
        if (containsKey(key)) {
            Long[] item = new Long[] {System.currentTimeMillis(), new Long(seconds)};
            timeout.put(key, item);
            return 1L;
        }
        return 0L;
    }

    public long getTimeToLive(String key) {
        if (table.containsKey(key) && !timeout.containsKey(key)) {
            return -1;
        }
        if (!table.containsKey(key)) {
            return -2;
        }
        long currentMilliseconds = System.currentTimeMillis();
        long momentWhenSet = timeout.get(key)[0];
        long timeOut = timeout.get(key)[1] * 1000;
        if (currentMilliseconds - momentWhenSet > timeOut) {
            return -2;
        } else {
            return (timeOut - (currentMilliseconds - momentWhenSet)) / 1000;
        }
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
