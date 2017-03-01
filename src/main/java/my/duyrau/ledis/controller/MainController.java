package my.duyrau.ledis.controller;

import my.duyrau.ledis.core.Command;
import my.duyrau.ledis.core.StringType;
import my.duyrau.ledis.json.CommandDTO;
import my.duyrau.ledis.parser.Parser;
import my.duyrau.ledis.util.CommandUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static my.duyrau.ledis.util.CommandUtil.ERROR_PREFIX;

/**
 * Created by duyrau on 2/28/17.
 */

@RestController
public class MainController {

    private Command commander;

    private StringType stringType = new StringType();

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String executeCommand(@RequestBody CommandDTO commandDTO) {
        if (commandDTO.getCommand() == null) {
            return ERROR_PREFIX + ": command not found";
        }
        String command = commandDTO.getCommand();
        Parser parser = new Parser();
        parser.parse(command);

        String commandName = parser.getCommandName().toUpperCase();
        if (CommandUtil.STRING_COMMANDS.contains(commandName)) {
            commander = stringType;
            return commander.execute(parser);
        }
        return ERROR_PREFIX + ": Unknown command " + commandName;
    }
}
