package my.duyrau.ledis.core;

import my.duyrau.ledis.parser.Parser;
import my.duyrau.ledis.util.Constant;

/**
 * Created by duyrau on 3/3/17.
 */
public class KeyOperator implements Command {

    private DataStore dataStore = new DataStore();

    private String keys() {
        return dataStore.listAllKeys();
    }

    private String del(String key) {
        dataStore.clearTimeoutOfOneKey(key);
        return String.valueOf(dataStore.deleteKey(key));
    }

    private String flushdb() {
        dataStore.clearTimeoutOfAllKeys();
        return String.valueOf(dataStore.deleteAllKeys());
    }

    private String expire(String key, int seconds) {
        return String.valueOf(dataStore.setTimeoutOnKey(key, seconds));
    }

    @Override
    public String execute(Parser parser) {
        if (parser != null) {
            if (!parser.getError().equals(Constant.NO_PARSING_ERROR)) {
                return parser.getError();
            }
            if (Constant.KEYS.equalsIgnoreCase(parser.getCommandName())) {
                return keys();
            } else if (Constant.DEL.equalsIgnoreCase(parser.getCommandName())) {
                return del(parser.getKey());
            } else if (Constant.FLUSHDB.equalsIgnoreCase(parser.getCommandName())) {
                return flushdb();
            } else if (Constant.EXPIRE.equalsIgnoreCase(parser.getCommandName())) {
                return expire(parser.getKey(), Integer.parseInt(parser.getRemainingArgFromTwo()[0]));
            } else {
                return String.valueOf(dataStore.getTimeToLive(parser.getKey()));
            }
        } else {
            return Constant.ERROR_COMMAND_NOT_FOUND;
        }
    }
}
