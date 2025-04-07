package modmate.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    public static final String FLAG_PREFIX = "--";

    private static final String REGEX = "^(?<command>\\S+)(?:\\s+(?<argument>(?:(?!"
        + FLAG_PREFIX + ")\\S+(?:\\s+(?!" + FLAG_PREFIX + ")\\S+)*)))?(?<flags>(?:\\s+"
        + FLAG_PREFIX + "\\S+(?:\\s+\\S+)*)*)$";

    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static Input parse(String inputString) throws IllegalArgumentException {
        if (inputString.isEmpty()) {
            throw new IllegalArgumentException("Input error: Input cannot be empty");
        }

        Matcher matcher = PATTERN.matcher(inputString);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Input error: Input does not match the required format");
        }

        // Extract command and argument
        String command = matcher.group("command");
        String argument = Optional.ofNullable(matcher.group("argument"))
            .map(str -> str.trim())
            .orElse("");

        // Extract flags and parameters
        String flagsSubstring = Optional.ofNullable(matcher.group("flags"))
            .map(str -> str.trim())
            .orElse("");

        if (flagsSubstring.isEmpty()) {
            return new Input(command, argument);
        }

        Map<String, String> flagsMap = parseFlags(flagsSubstring);
        return new Input(command, argument, flagsMap);
    }

    private static Map<String, String> parseFlags(String flagsSubstring) {
        Map<String, String> flagsMap = new HashMap<>();
        int endIndex = 0;

        while (endIndex < flagsSubstring.length()) {
            int flagStart = flagsSubstring.indexOf(FLAG_PREFIX, endIndex);
            if (flagStart == -1) {
                break;
            }

            flagStart += FLAG_PREFIX.length();
            int nextFlagStart = flagsSubstring.indexOf(FLAG_PREFIX, flagStart);

            String flagPair = (nextFlagStart == -1
                ? flagsSubstring.substring(flagStart)
                : flagsSubstring.substring(flagStart, nextFlagStart)).trim();

            int spaceIndex = flagPair.indexOf(' ');
            String flagName = flagPair.substring(0, spaceIndex);

            if (spaceIndex == -1 || flagName.isEmpty()) {
                throw new IllegalArgumentException("Missing or invalid parameter for flag: " + flagPair);
            }

            String flagValue = flagPair.substring(spaceIndex + 1).trim();
            flagsMap.put(flagName, flagValue);

            endIndex = nextFlagStart == -1 ? flagsSubstring.length() : nextFlagStart;
        }

        return flagsMap;
    }
}
