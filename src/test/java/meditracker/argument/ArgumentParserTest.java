package meditracker.argument;

import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.UnknownArgumentFoundException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArgumentParserTest {
    ArgumentList testArgumentList = new ArgumentList(
            new NameArgument(false),
            new QuantityArgument(false),
            new DosageMorningArgument(false),
            new RemarksArgument(false)
    );

    @Test
    void argumentParser_validArgumentString_parsesCorrectly() {
        String name = "Medication";
        String dosage = "100";
        String quantity = "2000";
        String remarks = "Take before meals";
        String testArgumentString = String.format("-n %s -dM %s -q %s -r %s",
                name,
                dosage,
                quantity,
                remarks);

        Map<ArgumentName, String> parsedArgs;
        try {
            parsedArgs = testArgumentList.parse(testArgumentString);
        } catch (ArgumentNotFoundException | DuplicateArgumentFoundException | HelpInvokedException |
                 ArgumentNoValueException | UnknownArgumentFoundException e) {
            throw new RuntimeException(e);
        }

        assertEquals(parsedArgs.get(ArgumentName.NAME), name);
        assertEquals(parsedArgs.get(ArgumentName.DOSAGE_MORNING), dosage);
        assertEquals(parsedArgs.get(ArgumentName.QUANTITY), quantity);
        assertEquals(parsedArgs.get(ArgumentName.REMARKS), remarks);
    }

    @Test
    void argumentParser_outOfOrderArgument_parsesCorrectly() {
        String name = "Medication";
        String dosage = "100";
        String quantity = "2000";
        String remarks = "Take before meals";
        String testArgumentString = String.format("-n %s -r %s -q %s -dM %s",
                name,
                remarks,
                quantity,
                dosage);

        Map<ArgumentName, String> parsedArgs;
        try {
            parsedArgs = testArgumentList.parse(testArgumentString);
        } catch (ArgumentNotFoundException | DuplicateArgumentFoundException | HelpInvokedException |
                 ArgumentNoValueException | UnknownArgumentFoundException e) {
            throw new RuntimeException(e);
        }

        assertEquals(parsedArgs.get(ArgumentName.NAME), name);
        assertEquals(parsedArgs.get(ArgumentName.DOSAGE_MORNING), dosage);
        assertEquals(parsedArgs.get(ArgumentName.QUANTITY), quantity);
        assertEquals(parsedArgs.get(ArgumentName.REMARKS), remarks);
    }

    @Test
    void argumentParser_additionalSpacesInArguments_parsesCorrectly() {
        String name = "Medication";
        String dosage = "100";
        String quantity = "2000";
        String remarks = "Take before meals";
        String testArgumentString = String.format("-n      %s     -r    %s    -q     %s  -dM  %s    ",
                name,
                remarks,
                quantity,
                dosage);

        Map<ArgumentName, String> parsedArgs;
        try {
            parsedArgs = testArgumentList.parse(testArgumentString);
        } catch (ArgumentNotFoundException | DuplicateArgumentFoundException | HelpInvokedException |
                 ArgumentNoValueException | UnknownArgumentFoundException e) {
            throw new RuntimeException(e);
        }

        assertEquals(parsedArgs.get(ArgumentName.NAME), name);
        assertEquals(parsedArgs.get(ArgumentName.DOSAGE_MORNING), dosage);
        assertEquals(parsedArgs.get(ArgumentName.QUANTITY), quantity);
        assertEquals(parsedArgs.get(ArgumentName.REMARKS), remarks);
    }

    @Test
    void argumentParser_duplicateArgumentFlags_duplicateArgumentFoundException() {
        String name = "Medication";
        String dosage = "100";
        String quantity = "2000";
        String remarks = "Take before meals";
        String testArgumentString = String.format("-n %s -dM %s -q %s -r %s -n %s",
                name,
                dosage,
                quantity,
                remarks,
                name);

        assertThrows(
                DuplicateArgumentFoundException.class,
                () -> testArgumentList.parse(testArgumentString)
        );
    }

    @Test
    void argumentParser_missingArgumentFlags_argumentNotFoundException() {
        String name = "Medication";
        String quantity = "2000";
        String remarks = "Take before meals";
        String testArgumentString = String.format("-n %s -q %s -r %s",
                name,
                quantity,
                remarks);

        assertThrows(
                ArgumentNotFoundException.class,
                () -> testArgumentList.parse(testArgumentString)
        );
    }

    @Test
    void argumentParser_missingArgumentValues_argumentNoValueException() {
        String name = "Medication";
        String dosage = "100";
        String quantity = "2000";
        String testArgumentString = String.format("-n %s -dM %s -q %s -r",
                name,
                dosage,
                quantity);

        assertThrows(
                ArgumentNoValueException.class,
                () -> testArgumentList.parse(testArgumentString)
        );
    }
}
