import org.json.JSONException;

import java.nio.charset.Charset;

public class ProjectMainClass {
    public static void main(String[] args) throws JSONException {
        UserInterface ui = new UserInterface();
        int run = 1;
        int option;
        System.out.print("***********\n***Hello***\n***********\n\n*******************Welcome to Java Parser Project!*******************\n\n");
        System.out.println("Default Charset=" + Charset.defaultCharset());
        while (run != 0) {
            ui.menuDescription();
            option = ui.getUserInputInt();
            switch (option) {
                case 0 -> {
                    run = 0;
                    System.out.print("Java Parser Project will be terminated!\nGoodbye!\n");
                }
                case 1 -> {
                    ui.parsingOption();
                    ui.parseAllInInputFolder();
                }
                case 2 -> {
                    ui.parsingOption();
                    ui.parseOneInInputFolder();
                }
                case 3 -> {
                    ui.parsingOption();
                    ui.parseOneSpecifiedByPath();
                }
                default -> ui.clearConsole();
            }
        }
    }
}
