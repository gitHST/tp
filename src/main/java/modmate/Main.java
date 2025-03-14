package modmate;

import java.util.List;
import java.util.Scanner;

import modmate.course.Course;
import modmate.log.Log;

/**
 * Main class that runs the ModMate application. This application interacts with a list of mods,
 * allowing the user to enter commands and view or manipulate the mod data.
 * It supports logging, handling commands, and printing the mod list.
 */
public class Main {

    /**
     * A string containing the help message for the application, detailing available commands.
     * The commands include options to display help, exit the application, and print the mod list.
     */
    static String helpMessage = """
        Commands:
        -h: Display this help message
        exit: Exit the application
        printmods: Print the list of mods""";

    /**
     * The entry point of the ModMate application.
     * Processes command-line arguments, manages logging, and runs the main command loop.
     * It continuously prompts the user for commands and handles input based on predefined options.
     *
     * @param args Command-line arguments. The `--log` flag can be provided to enable logging.
     *             If provided, the flag should be followed by a boolean value ("true" or "false").
     */
    public static void main(String[] args) {

        // Parse command-line arguments to check for logging flag
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--log") && i + 1 < args.length) {
                Log.setLoggingEnabled(Boolean.parseBoolean(args[i + 1]));
            }
        }

        // Get a list of courses (mods)
        List<Course> courses = SampleCourses.getCourses();

        // Enable logging if specified and print welcome message
        Log.printLog("Logging is enabled.");
        System.out.println("Welcome to ModMate!");

        // Create scanner object to read user input
        Scanner scanner = new Scanner(System.in);

        // Main command loop
        while (true) {
            System.out.println("Enter command ('exit' to quit, '-h' for help):");
            String input = scanner.nextLine().trim().toLowerCase();
            Log.saveLog("\n[MAIN]   Received input: " + input);
            switch (input) {
            case "exit" -> {
                // Exit the application
                Log.saveLog("[MAIN]   Exiting application.");
                System.out.println("Exiting...");
                scanner.close();
                return;
            }
            case "-h" -> {
                // Display help message
                Log.saveLog("[MAIN]   Printing help message.");
                System.out.println(helpMessage);
            }
            case "printmods" -> {
                // Print list of courses
                Log.saveLog("[MAIN]   Displaying mod list.");
                System.out.println("Mods list:");
                for (Course course : courses) {
                    System.out.println(course);
                }
            }
            default -> Log.saveLog("[MAIN]   Command: " + input + " is invalid");
            }
        }
    }
}
