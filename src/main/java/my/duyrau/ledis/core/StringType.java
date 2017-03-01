package my.duyrau.ledis.core;

import my.duyrau.ledis.parser.Parser;
import my.duyrau.ledis.util.Constant;

import java.util.Hashtable;

/**
 * Created by duyrau on 3/1/17.
 */
public class StringType implements Command {

    private Hashtable<String, String> table;

    public StringType() {
        table = new Hashtable<>();
    }

    public String set(String key, String value) {
        table.put(key, value);
        return "OK";
    }

    public String get(String key) {
        String result = table.get(key);
        return result == null ? "(nil)" : result;
    }

    @Override
    public String execute(Parser parser) {
        if (parser.getCommandName().equalsIgnoreCase(Constant.GET)) {
            return get(parser.getKey());
        } else {
            String[] value = parser.getRemainingArgFromTwo();
            return set(parser.getKey(), value[0]);
        }
    }
}
