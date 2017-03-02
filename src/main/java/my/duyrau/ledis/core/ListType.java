package my.duyrau.ledis.core;

import my.duyrau.ledis.lib.Deque;
import my.duyrau.ledis.parser.Parser;
import my.duyrau.ledis.util.CommandUtil;
import my.duyrau.ledis.util.Constant;

import java.util.HashMap;

/**
 * Created by duyrau on 3/1/17.
 */
public class ListType implements Command {

    public static final HashMap<String, Deque<String>> table = new HashMap<>();

    // data structure for list
    private Deque<String> deque;

    public int llen(String key) {
        if (CommandUtil.valueIsNotList(key)) {
            return -1;
        }
        if (table.containsKey(key)) {
            return table.get(key).size();
        } else {
            return 0;
        }
    }

    public int rpush(String key, String[] values) {
        if (CommandUtil.valueIsNotList(key)) {
            return -1;
        } else {
            if (table.containsKey(key)) {
                deque = table.get(key);
                for (String value : values) {
                    deque.addLast(value);
                }
            } else {
                deque = new Deque<>();
                for (String value : values) {
                    deque.addLast(value);
                }
                table.put(key, deque);
            }
            return deque.size();
        }
    }

    public String lpop(String key) {
        if (table.containsKey(key)) {
            deque = table.get(key);
            String result = deque.removeFirst();
            return result == null ? Constant.NIL : result;
        } else {
            return Constant.NIL;
        }
    }

    public String rpop(String key) {
        if (table.containsKey(key)) {
            deque = table.get(key);
            String result = deque.removeLast();
            return result == null ? Constant.NIL : result;
        } else {
            return Constant.NIL;
        }
    }

    public String lrange(String key, int start, int stop) {
        if (CommandUtil.valueIsNotList(key)) {
            return Constant.WRONG_KIND_OF_VALUE;
        }
        if (table.containsKey(key)) {
            deque = table.get(key);
            return deque.print(start, stop);
        } else {
            return Constant.EMPTY_LIST_OR_SET;
        }
    }

    @Override
    public String execute(Parser parser) {
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
    }
}
