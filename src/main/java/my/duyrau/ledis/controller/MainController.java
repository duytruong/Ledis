package my.duyrau.ledis.controller;

import my.duyrau.ledis.core.*;
import my.duyrau.ledis.json.CommandDTO;
import my.duyrau.ledis.parser.Parser;
import my.duyrau.ledis.util.CommandUtil;
import my.duyrau.ledis.util.Constant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by duyrau on 2/28/17.
 */

@RestController
public class MainController {

    private static final Map<String, Command> dataTypes;

    static {
        dataTypes = new HashMap<>();
        dataTypes.put(Constant.STRING_TYPE, new StringType());
        dataTypes.put(Constant.LIST_TYPE, new ListType());
        dataTypes.put(Constant.SET_TYPE, new SetType());
        dataTypes.put(Constant.DATA_EXPIRATION_TYPE, new KeyOperator());
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String executeCommand(@RequestBody CommandDTO commandDTO) {
        if (commandDTO.getCommand() == null) {
            return Constant.ERROR_COMMAND_NOT_FOUND;
        }
        String command = commandDTO.getCommand();
        Parser parser = new Parser();
        parser.parse(command);

        String commandName = parser.getCommandName().toUpperCase();
        String type;
        if (CommandUtil.STRING_COMMANDS.contains(commandName)) {
            type = Constant.STRING_TYPE;
        } else if (CommandUtil.LIST_COMMANDS.contains(commandName)) {
            type = Constant.LIST_TYPE;
        } else if (CommandUtil.SET_COMMANDS.contains(commandName)) {
            type = Constant.SET_TYPE;
        } else if (CommandUtil.DATA_EXPIRATION_COMMANDS.contains(commandName)) {
            type = Constant.DATA_EXPIRATION_TYPE;
        } else {
            return Constant.ERROR_UNKNOWN_COMMAND + commandName;
        }
        return dataTypes.get(type).execute(parser);
    }
}
