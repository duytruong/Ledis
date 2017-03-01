package my.duyrau.ledis.parser;

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

    public void parse(String command) {
        tokens = tokenize(command);
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

    public int getLength() {
        return len;
    }
}
