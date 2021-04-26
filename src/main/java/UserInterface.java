import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
    private Scanner scan = new Scanner(System.in);
    private Parser parser = new Parser();
    private final ReadWriteFile readWriteFile = new ReadWriteFile();
    private ArrayList<String> allFilesNames = new ArrayList<>();
    private int userInputInt;
    private int parsingOption;

    public void menuDescription() {
        System.out.print("Please choose one option.\n");
        System.out.print("1 - Parse all json files in input folder\n2 - Parse particular file in input folder\n3 - Parse particular file specified by path\n0 - Quit\n\n");
        System.out.print("Your option: ");
    }

    public void parsingOption() {
        System.out.print("\nSelect what you want to parse.\n\n");
        System.out.print("1 - Parse everything (Title, table of contents, bibliography, versions and revisions)\n2 - Table of contents\n3 - Bibliography\n4 - Versions\n5 - Revisions\n\nRemark: Title is always parsed.\n\n");
        System.out.print("Your option: ");
        parsingOption = getUserInputInt();
        while (parsingOption < 1 || parsingOption > 5) {
            System.out.print("\nIncorrect option!");
            System.out.print("\nYour input: ");
            parsingOption = getUserInputInt();
        }
    }

    public int getUserInputInt() {
        while (!scan.hasNextInt()) {
            scan.next();
            System.out.print("\nIncorrect input, your option: ");
        }
        userInputInt = scan.nextInt();
        return userInputInt;
    }

    public String getUserInputString() {
        String userInputString = scan.next();
        return userInputString;

    }

    public void parseAllInInputFolder() {

        if (!getFileNames()) {
            return;
        }
        System.out.println("\n\nList of correct files: \n");
        for (int i = 0; i < allFilesNames.size(); i++) {
            System.out.println(i + 1);
            parser = new Parser();
            readWriteFile.writeToFile(parser.parsing(readWriteFile.readFile(allFilesNames.get(i), "input/"), parsingOption), allFilesNames.get(i));
        }
    }

    public void parseOneInInputFolder() {
        if (!getFileNames()) {
            return;
        }
        System.out.println("\n\nList of correct files: \n");
        for (int i = 0; i < allFilesNames.size(); i++) {
            System.out.println(i + 1 + ": " + allFilesNames.get(i));
        }
        System.out.print("\n\nPlease select index of file which you want to parse: ");
        getUserInputInt();
        while (userInputInt < 1 || userInputInt > allFilesNames.size()) {
            System.out.print("\nIncorrect index!");
            System.out.print("\nYour input: ");
            getUserInputInt();
        }
        String nameOfFile = allFilesNames.get(userInputInt - 1);
        clearConsole();
        parser = new Parser();
        readWriteFile.writeToFile(parser.parsing(readWriteFile.readFile(nameOfFile, "input/"), parsingOption), nameOfFile);
    }

    public void parseOneSpecifiedByPath() {
        if (!getFileNames()) {
            return;
        }
        clearConsole();
        String fileExtension;
        System.out.print("Please type name of file which you want to parse: ");
        String nameOfFile = getUserInputString();
        fileExtension = readWriteFile.getFileExtension(nameOfFile);
        if (!fileExtension.equals("txt") || nameOfFile.length() < 5) {
            System.out.print("\nOnly .txt files are allowed for parsing! \n\n");
            return;
        }
        System.out.print("\ne.g. of file path: C:/Users/computer/Desktop/ ");
        System.out.print("\nPlease specify absolut path of file: ");
        String path = getUserInputString();
        parser = new Parser();
        readWriteFile.writeToFile(parser.parsing(readWriteFile.readFile(nameOfFile, path), parsingOption), nameOfFile);
    }

    public boolean getFileNames() {
        clearConsole();
        allFilesNames = readWriteFile.getAllFilesNames();
        var wrongFilesNames = readWriteFile.wrongFilesNames();
        if (allFilesNames.isEmpty()) {
            clearConsole();
            System.out.print("\nThere are no files in input folders!\n\n");
            return false;
        }

        if (wrongFilesNames.size() > 0) {
            for (String fileName : wrongFilesNames) {
                System.out.print("There is/are " + wrongFilesNames.size() + " file/files that has/have incorrect extension!\nThese files will not be parsed.\nOnly .txt files are accepted.\n\n");
                System.out.println("List of incorrect files: ");
                System.out.println(fileName);
            }
        }
        return true;
    }

    public void clearConsole() {
        try {
            Robot robot = new Robot();

            robot.keyPress(KeyEvent.VK_SHIFT);
            Thread.sleep(5);
            robot.keyPress(KeyEvent.VK_L);
            Thread.sleep(5);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            Thread.sleep(5);
            robot.keyRelease(KeyEvent.VK_L);
            Thread.sleep(5);

        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
