package my.duyrau.ledis.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by duyrau on 3/1/17.
 */
public class CommandUtil {

    private static String[] stringCommands = {Constant.GET, Constant.SET};

    private static String[] listCommands = {Constant.LLEN, Constant.RPUSH, Constant.LPOP, Constant.RPOP, Constant.LRANGE};

    private static String[] setCommands = {Constant.SADD, Constant.SCARD, Constant.SMEMBERS, Constant.SREM, Constant.SINTER};

    private static String[] dataExpirationCommands = {Constant.KEYS, Constant.DEL, Constant.FLUSHDB, Constant.EXPIRE, Constant.TTL};

    public static final Set<String> STRING_COMMANDS = new HashSet<>(Arrays.asList(stringCommands));

    public static final Set<String> LIST_COMMANDS = new HashSet<>(Arrays.asList(listCommands));

    public static final Set<String> SET_COMMANDS = new HashSet<>(Arrays.asList(setCommands));

    public static final Set<String> DATA_EXPIRATION_COMMANDS = new HashSet<>(Arrays.asList(dataExpirationCommands));

}
