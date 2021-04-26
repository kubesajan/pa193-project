import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.nio.file.Files;

public class ReadWriteFile {
    private ArrayList<String> wrongFilesNames = new ArrayList<>();
    private static FileWriter file;

    public ArrayList<String> readFile(String nameOfFile, String path) {
        BufferedReader reader;
        ArrayList<String> lines = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(
                    path + nameOfFile));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("\nFile reading has failed !");
            return new ArrayList<>();
        }
        return lines;
    }

    public void writeToFile(String jsonString, String nameOfFile) throws IOException {
        if (jsonString.isEmpty()) {
            System.out.print("\n\nFile does not exist or is empty!\n\n");
            return;
        }
        Path path = Paths.get("output/");
        nameOfFile = nameOfFile.replaceFirst(".txt", ".json");
        String fileLocation = "";
        if(!Files.exists(path)) {
            Files.createDirectory(path);
        }
        try {
            file = new FileWriter("output/" + nameOfFile);
            file.write(jsonString);
            File f = new File(nameOfFile);
            fileLocation = "File is located in: " + f.getAbsolutePath() + "\n";
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            System.out.println("Writing to file has failed !");

        } finally {

            try {
                nameOfFile = "\033[3m" + nameOfFile + "\033[0m";

                file.flush();
                file.close();
                System.out.print("\nJSON file " + nameOfFile + " has been successfully created!\n");
                System.out.print(fileLocation + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Writing to file has failed !");
            }
        }
    }

    public ArrayList<String> getAllFilesNames() throws IOException {
        Path pathInput = Paths.get("input/");
        if(!Files.exists(pathInput)) {
            Files.createDirectory(pathInput);
        }
        ArrayList<String> allFilesNames = new ArrayList<>();
        File folder = new File("input");
        File[] listOfFiles = folder.listFiles();
        String fileExtension;
        String fileName;

        if (listOfFiles == null) {
            return new ArrayList<>();
        }
        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileName = file.getName();
                fileExtension = getFileExtension(fileName);
                if (fileExtension.equals("txt")) {
                    allFilesNames.add(fileName);
                } else {
                    wrongFilesNames.add(fileName);
                }

            }
        }
        return allFilesNames;

    }

    public ArrayList<String> wrongFilesNames() {
        return wrongFilesNames;
    }

    public String getFileExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }


}
