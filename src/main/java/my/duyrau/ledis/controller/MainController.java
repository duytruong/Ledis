package my.duyrau.ledis.controller;

import my.duyrau.ledis.core.Command;
import my.duyrau.ledis.core.ListType;
import my.duyrau.ledis.core.StringType;
import my.duyrau.ledis.json.CommandDTO;
import my.duyrau.ledis.parser.Parser;
import my.duyrau.ledis.util.CommandUtil;
import my.duyrau.ledis.util.Constant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by duyrau on 2/28/17.
 */

@RestController
public class MainController {

    private Command commander;

    private StringType stringType = new StringType();

    private ListType listType = new ListType();

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String executeCommand(@RequestBody CommandDTO commandDTO) {
        if (commandDTO.getCommand() == null) {
            return Constant.ERROR_COMMAND_NOT_FOUND;
        }
        String command = commandDTO.getCommand();
        Parser parser = new Parser();
        parser.parse(command);

        String commandName = parser.getCommandName().toUpperCase();
        if (CommandUtil.STRING_COMMANDS.contains(commandName)) {
            commander = stringType;
            return commander.execute(parser);
        } else if (CommandUtil.LIST_COMMANDS.contains(commandName)) {
            commander = listType;
            return commander.execute(parser);
        }
        return Constant.ERROR_UNKNOWN_COMMAND + commandName;
    }
}
