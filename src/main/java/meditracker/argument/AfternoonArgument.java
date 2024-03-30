package meditracker.argument;

/**
 * Argument to represent afternoon
 */
public class AfternoonArgument extends Argument {
    public AfternoonArgument(boolean isOptional) {
        super(
                ArgumentName.AFTERNOON,
                "-a",
                null,
                "Time of day: Afternoon",
                isOptional,
                false
        );
    }
}