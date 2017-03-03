package my.duyrau.ledis.core;

import my.duyrau.ledis.parser.Parser;
import my.duyrau.ledis.util.Constant;

/**
 * Created by duyrau on 3/1/17.
 */
public class StringType implements Command {

//    public static final HashMap<String, String> table = new HashMap<>();

    private DataStore dataStore = new DataStore();

    private String set(String key, String value) {
        dataStore.put(key, value);
        return Constant.RESPONSE_OK;
    }

    private String get(String key) {
        String result = (String) dataStore.get(key);
        return result == null ? Constant.NIL : result;
    }

    @Override
    public String execute(Parser parser) {
        if (parser != null) {
            if (!parser.getError().equals(Constant.NO_PARSING_ERROR)) {
                return parser.getError();
            }
            if (parser.getCommandName().equalsIgnoreCase(Constant.GET)) {
                return get(parser.getKey());
            } else {
                String[] value = parser.getRemainingArgFromTwo();
                return set(parser.getKey(), value[0]);
            }
        } else {
            return Constant.ERROR_COMMAND_NOT_FOUND;
        }
    }
}
