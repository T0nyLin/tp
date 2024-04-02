package meditracker.argument;

import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.UnknownArgumentFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ArgumentList class for managing a list of Argument
 * Calls on ArgumentParser when parse method is invoked
 * @see ArgumentParser
 */
public class ArgumentList {
    private final List<Argument> arguments;

    /**
     * Constructs ArgumentList to take in variable length of Argument
     * Assertion test is used to check no flag collision for the
     * arguments specified
     *
     * @param arguments Arguments to be included in the list
     */
    public ArgumentList(Argument... arguments) {
        List<Argument> newArguments = new ArrayList<>(List.of(arguments));
        newArguments.add(new HelpArgument());
        this.arguments = newArguments;

        // assertion test: check for flag collisions
        Set<String> flags = new HashSet<>();
        for (Argument argument: arguments) {
            String flag = argument.getFlag();
            assert !flags.contains(flag);
            flags.add(flag);
        }
    }

    /**
     * Parses user raw input arguments into ArgumentName and
     * corresponding argument value
     *
     * @param rawInput Raw input to be parsed
     * @return A map of argument name as key and the corresponding value
     * @throws ArgumentNotFoundException When argument required not found
     * @throws ArgumentNoValueException When argument requires value but no value specified
     * @throws DuplicateArgumentFoundException When duplicate argument found
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws UnknownArgumentFoundException When unknown argument flags found in user input
     * @see ArgumentParser
     */
    public Map<ArgumentName, String> parse(String rawInput)
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
        ArgumentParser argumentParser = new ArgumentParser(this, rawInput);

        boolean hasCalledForHelp = argumentParser.parsedArguments.get(ArgumentName.HELP) != null;
        if (hasCalledForHelp) {
            throw new HelpInvokedException();
        }

        argumentParser.checkForMissingRequiredArguments(); // throws ArgumentNotFoundException
        return argumentParser.parsedArguments;
    }

    public List<Argument> getArguments() {
        return arguments;
    }
}
