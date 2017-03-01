package my.duyrau.ledis.core;

import my.duyrau.ledis.parser.Parser;

/**
 * Created by duyrau on 3/1/17.
 */
public interface Command {

    String execute(Parser parser);
}
