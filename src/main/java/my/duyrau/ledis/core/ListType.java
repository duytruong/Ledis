package my.duyrau.ledis.core;

import my.duyrau.ledis.lib.Deque;
import my.duyrau.ledis.parser.Parser;
import my.duyrau.ledis.util.Constant;

/**
 * Created by duyrau on 3/1/17.
 */
public class ListType implements Command {

//    public static final HashMap<String, Deque<String>> dataStore = new HashMap<>();

    private DataStore dataStore = new DataStore();
    
    // data structure for list
    private Deque<String> deque;

    private int llen(String key) {
        if (dataStore.valueIsNotList(key)) {
            return -1;
        }
        if (dataStore.containsKey(key)) {
            return ((Deque<String>)dataStore.get(key)).size();
        } else {
            return 0;
        }
    }

    private int rpush(String key, String[] values) {
        if (dataStore.valueIsNotList(key)) {
            return -1;
        } else {
            if (dataStore.containsKey(key)) {
                deque = (Deque<String>)dataStore.get(key);
                for (String value : values) {
                    deque.addLast(value);
                }
            } else {
                deque = new Deque<>();
                for (String value : values) {
                    deque.addLast(value);
                }
                dataStore.put(key, deque);
            }
            return deque.size();
        }
    }

    private String lpop(String key) {
        if (dataStore.containsKey(key)) {
            deque = (Deque<String>)dataStore.get(key);
            String result = deque.removeFirst();
            return result == null ? Constant.NIL : result;
        } else {
            return Constant.NIL;
        }
    }

    private String rpop(String key) {
        if (dataStore.containsKey(key)) {
            deque = (Deque<String>)dataStore.get(key);
            String result = deque.removeLast();
            return result == null ? Constant.NIL : result;
        } else {
            return Constant.NIL;
        }
    }

    private String lrange(String key, int start, int stop) {
        if (dataStore.valueIsNotList(key)) {
            return Constant.WRONG_KIND_OF_VALUE;
        }
        if (dataStore.containsKey(key)) {
            deque = (Deque<String>)dataStore.get(key);
            return deque.print(start, stop);
        } else {
            return Constant.EMPTY_LIST_OR_SET;
        }
    }

    @Override
    public String execute(Parser parser) {
        if (parser != null) {
            if (!parser.getError().equals("")) {
                return parser.getError();
            }
            if (Constant.RPUSH.equalsIgnoreCase(parser.getCommandName())) {
                int result = rpush(parser.getKey(), parser.getRemainingArgFromTwo());
                return result == -1 ? Constant.WRONG_KIND_OF_VALUE : String.valueOf(result);
            } else if (Constant.LPOP.equalsIgnoreCase(parser.getCommandName())) {
                return lpop(parser.getKey());
            } else if (Constant.LRANGE.equalsIgnoreCase(parser.getCommandName())) {
                String[] values = parser.getRemainingArgFromTwo();
                return lrange(parser.getKey(), Integer.parseInt(values[0]), Integer.parseInt(values[1]));
            } else if (Constant.RPOP.equalsIgnoreCase(parser.getCommandName())) {
                return rpop(parser.getKey());
            } else {
                int result = llen(parser.getKey());
                return result == -1 ? Constant.WRONG_KIND_OF_VALUE : String.valueOf(result);
            }
        } else {
            return Constant.ERROR_COMMAND_NOT_FOUND;
        }
    }
}
