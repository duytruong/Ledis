package my.duyrau.ledis.util;

/**
 * Created by duyrau on 3/1/17.
 */
public class Constant {

    public static final String GET = "GET";
    public static final String SET = "SET";

    public static final String LLEN = "LLEN";
    public static final String RPUSH = "RPUSH";
    public static final String LPOP = "LPOP";
    public static final String RPOP = "RPOP";
    public static final String LRANGE = "LRANGE";

    public static final String SADD = "SADD";
    public static final String SCARD = "SCARD";
    public static final String SMEMBERS = "SMEMBERS";
    public static final String SREM = "SREM";
    public static final String SINTER = "SINTER";

    public static final String EMPTY_STRING = "";
    public static final String ERROR_PREFIX = "ERROR";
    public static final String DELIMITER = " ";
    public static final String DOUBLE_QUOTES = "\"";
    public static final String EMPTY_LIST_OR_SET = "(Empty list or set)";
    public static final String WRONG_KIND_OF_VALUE = "WRONGTYPE Operation against a key holding the wrong kind of value";
    public static final String NIL = "(nil)";
    public static final String ERROR_COMMAND_NOT_FOUND = ERROR_PREFIX + ": command not found";
    public static final String ERROR_UNKNOWN_COMMAND = ERROR_PREFIX + ": Unknown command";
    public static final String ERROR_INVALID_ARGUMENT = "Invalid argument(s)";
}
