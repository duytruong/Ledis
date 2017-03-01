package my.duyrau.ledis.json;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by duyrau on 3/1/17.
 */
public class CommandDTO {

    @NotEmpty
    private String command;

    public CommandDTO() {}

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
