package my.duyrau.ledis.parser;

import my.duyrau.ledis.util.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by duyrau on 3/1/17.
 */
public class Parser {

    private String[] tokens;

    private String[] remainingArgumentsFromIndexTwo;

    private String[] remainingArgumentsFromIndexOne;

    private String error = Constant.NO_PARSING_ERROR;

    private int len;

    private String[] tokenize(String str) {
        String regex = "\"([^\"]*)\"|(\\S+)";
        Matcher m = Pattern.compile(regex).matcher(str);
        List<String> result = new ArrayList<>();
        while (m.find()) {
            if (m.group(1) != null) {
                result.add(m.group(1));
            } else {
                result.add(m.group(2));
            }
        }
        return result.toArray(new String[0]);
    }

    private String[] tokenizeWithHTML(String str) {
        int secondSpaceIdx = str.indexOf(Constant.DELIMITER, str.indexOf(Constant.DELIMITER) + 1);
        int secondDoubleQuoteIdx = str.indexOf(Constant.DOUBLE_QUOTES, str.indexOf(Constant.DOUBLE_QUOTES) + 1);

        // only one space in commad => GET
        if (secondSpaceIdx == -1) {
            return str.split(Constant.DELIMITER);
        } // two spaces, doesn't contain (") => SET
        else if (!str.contains("\"")) {
            return str.split(Constant.DELIMITER);
        } // two spaces => SET, but contains only one (") => Invalid argument
        else if (secondDoubleQuoteIdx == -1) {
            error = Constant.ERROR_INVALID_ARGUMENT;
            return str.split(Constant.DELIMITER);
        } // SET and value is contained by quotes, value can has quotes inside.
        else {
            String nameAndKey = str.substring(0, secondSpaceIdx);

            // remove the first and the last (") of the string.
            String value = str.substring(secondSpaceIdx + 2, str.length() - 1);

            String[] tmpToken = nameAndKey.split(Constant.DELIMITER);
            int tmpTokenLen = tmpToken.length;
            String[] result = new String[tmpTokenLen + 1];
            for (int i = 0; i < tmpTokenLen; i++) {
                result[i] = tmpToken[i];
            }
            result[tmpTokenLen] = value;
            return result;
        }
    }

    private String extractCommandName(String cmd) {
        int spaceIdx = cmd.indexOf(Constant.DELIMITER);
        if (spaceIdx == -1) {
            return cmd;
        } else {
            return cmd.substring(0, spaceIdx);
        }
    }

    public void parse(String command) {
        String cmdName = extractCommandName(command).toUpperCase();
        if (Constant.GET.equals(cmdName) || Constant.SET.equals(cmdName)) {
            tokens = tokenizeWithHTML(command);
        } else {
            tokens = tokenize(command);
        }
        len = tokens.length;
        if (len > 1) {
            remainingArgumentsFromIndexOne = Arrays.copyOfRange(tokens, 1, tokens.length);
        }
        if (len > 2) {
            remainingArgumentsFromIndexTwo = Arrays.copyOfRange(tokens, 2, tokens.length);
        }
    }

    public String getCommandName() {
        return tokens[0];
    }

    public String getKey() {
        return tokens[1];
    }

    /**
     * Get list of values in commands that have key and values (return values from 2nd index of array)
     * RPUSH key 0 1 2 => return [0, 1, 2]
     * @return array of values
     */
    public String[] getRemainingArgFromTwo() {
        return remainingArgumentsFromIndexTwo;
    }

    /**
     * Get list of values in commands that have list of keys (return values from 1nd index of array)
     * SINTER set1 set2 set3 => return [set1, set2, set3]
     * @return array of values
     */
    public String[] getRemainingArgFromOne() {
        return remainingArgumentsFromIndexOne;
    }

    public String getError() {
        return error;
    }

    public int getLength() {
        return len;
    }
}
