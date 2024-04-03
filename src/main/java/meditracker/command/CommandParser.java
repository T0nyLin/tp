package meditracker.command;

import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.CommandNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;

/**
 * The Parser class parses user input commands into Command objects.
 */
public class CommandParser {
    private final CommandName commandName;
    private final String arguments;

    /**
     * Parses a full command string into a CommandName enum and arguments.
     *
     * @param fullCommand The full command string entered by the user.
     * @throws CommandNotFoundException Command specified not found
     */
    public CommandParser(String fullCommand) throws CommandNotFoundException {
        if (fullCommand.isEmpty()) {
            throw new CommandNotFoundException();
        }

        String[] commands = fullCommand.split(" ", 2);
        arguments = (commands.length == 2) ? commands[1] : "";
        commandName = CommandName.valueOfLabel(commands[0]);
    }

    /**
     * Gets the Command object based on the CommandName enum type
     *
     * @return A Command object corresponding to the parsed command.
     * @throws ArgumentNotFoundException When argument required not found
     * @throws DuplicateArgumentFoundException When duplicate argument found
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws CommandNotFoundException Command specified not found
     */
    public Command getCommand()
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException,
            CommandNotFoundException {
        switch (commandName) {
        case EXIT:
            return new ExitCommand();
        case ADD:
            return new AddCommand(arguments);
        case MODIFY:
            return new ModifyCommand(arguments);
        case LIST:
            return new ListCommand(arguments);
        case MORE:
            return new MoreCommand(arguments);
        case DELETE:
            return new DeleteCommand(arguments);
        case SEARCH:
            return new SearchCommand(arguments);
        case TAKE:
            return new TakeCommand(arguments);
        case UNTAKE:
            return new UntakeCommand(arguments);
        case SAVE:
            return new SaveCommand(arguments);
        case LOAD:
            return new LoadCommand(arguments);
        case UNKNOWN:
            // fall through
        default:
            throw new CommandNotFoundException();
        }
    }

    public CommandName getCommandName() {
        return commandName;
    }
}
