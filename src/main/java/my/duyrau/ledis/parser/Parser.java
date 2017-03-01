package my.duyrau.ledis.parser;

import my.duyrau.ledis.util.CommandUtil;

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

    private String error = "";

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

    private String[] tokenize2(String str) {
        int secondSpaceIdx = str.indexOf(" ", str.indexOf(" ") + 1);
        int secondDoubleQuoteIdx = str.indexOf("\"", str.indexOf("\"") + 1);

        // only one space in commad => GET
        if (secondSpaceIdx == -1) {
            return str.split(CommandUtil.DELIMITER);
        } // two spaces, doesn't contain (") => SET
        else if (!str.contains("\"")) {
            return str.split(CommandUtil.DELIMITER);
        } // two spaces => SET, but contains only one (") => Invalid argument
        else if (secondDoubleQuoteIdx == -1) {
            error = "Invalid argument(s)";
            return str.split(CommandUtil.DELIMITER);
        } // SET and value is contained by quotes, value can has quotes inside.
        else {
            String nameAndKey = str.substring(0, secondSpaceIdx);

            // remove the first and the last (") of the string.
            String value = str.substring(secondSpaceIdx + 2, str.length() - 1);

            String[] tmpToken = nameAndKey.split(CommandUtil.DELIMITER);
            int tmpTokenLen = tmpToken.length;
            String[] result = new String[tmpTokenLen + 1];
            for (int i = 0; i < tmpTokenLen; i++) {
                result[i] = tmpToken[i];
            }
            result[tmpTokenLen] = value;
            return result;
        }
    }

    public void parse(String command) {
        tokens = tokenize2(command);
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

    public String[] getRemainingArgFromTwo() {
        return remainingArgumentsFromIndexTwo;
    }

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
