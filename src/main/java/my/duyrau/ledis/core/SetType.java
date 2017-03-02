package my.duyrau.ledis.core;

import my.duyrau.ledis.parser.Parser;
import my.duyrau.ledis.util.Constant;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by duyrau on 3/2/17.
 */
public class SetType implements Command {

    public static final HashMap<String, HashSet<String>> table = new HashMap<>();

    private HashSet<String> set;

    private boolean keyDoesNotContainSET(String key) {
        return StringType.table.containsKey(key) || ListType.table.containsKey(key);
    }

    private int sadd(String key, String[] values) {
        if (keyDoesNotContainSET(key)) {
            return -1;
        }
        int addedItems = 0;
        if (table.containsKey(key)) {
            set = table.get(key);
        } else {
            set = new HashSet<>();
            table.put(key, set);
        }
        for (int i = 0; i < values.length; i++) {
            if (set.add(values[i])) {
                addedItems++;
            }
        }
        return addedItems;
    }

    private int scard(String key) {
        if (keyDoesNotContainSET(key)) {
            return -1;
        }
        if (table.containsKey(key)) {
            return table.get(key).size();
        } else {
            return 0;
        }
    }

    private String smembers(String key) {
        if (table.containsKey(key)) {
            return table.get(key).toString();
        } else {
            return Constant.EMPTY_LIST_OR_SET;
        }
    }

    private int srem(String key, String[] values) {
        if (table.containsKey(key)) {
            int removedItems = 0;
            set = table.get(key);
            for (int i = 0; i < values.length; i++) {
                if (set.remove(values[i])) {
                    removedItems++;
                }
            }
            return removedItems;
        } else {
            return 0;
        }
    }

    private boolean oneOfKeysIsNotExist(String[] keys) {
        for (int i = 0; i < keys.length; i++) {
            if (!table.containsKey(keys[i])) {
                return true;
            }
        }
        return false;
    }

    private String sinter(String[] keys) {
        if (oneOfKeysIsNotExist(keys)) {
            return Constant.EMPTY_LIST_OR_SET;
        } else {
            HashSet<String> commons = new HashSet<>();
            commons.addAll(table.get(keys[0]));
            for (int i = 1; i < keys.length; i++) {
                commons.retainAll(table.get(keys[i]));
            }
            return commons.toString();
        }
    }

    @Override
    public String execute(Parser parser) {
        if (parser != null) {
            if (!parser.getError().equals("")) {
                return parser.getError();
            }
            if (Constant.SADD.equalsIgnoreCase(parser.getCommandName())) {
                int result = sadd(parser.getKey(), parser.getRemainingArgFromTwo());
                return result == -1 ? Constant.WRONG_KIND_OF_VALUE : String.valueOf(result);
            } else if (Constant.SCARD.equalsIgnoreCase(parser.getCommandName())) {
                int result = scard(parser.getKey());
                return result == -1 ? Constant.WRONG_KIND_OF_VALUE : String.valueOf(result);
            } else if (Constant.SMEMBERS.equalsIgnoreCase(parser.getCommandName())) {
                return smembers(parser.getKey());
            } else if (Constant.SREM.equalsIgnoreCase(parser.getCommandName())) {
                int result = srem(parser.getKey(), parser.getRemainingArgFromTwo());
                return String.valueOf(result);
            } else {
                return sinter(parser.getRemainingArgFromOne());
            }
        } else {
            return Constant.ERROR_COMMAND_NOT_FOUND;
        }
    }
}
