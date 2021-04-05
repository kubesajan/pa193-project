import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class ReadWriteFile {
    private ArrayList<String> lines = new ArrayList<>();
    private static FileWriter file;

    public  ArrayList<String> readFile(String nameOfFile) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "input/" + nameOfFile));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File reading has failed !");
        }
        return lines;
    }
    public void WriteToFile(JSONObject obj, String nameOfFile)
    {
        nameOfFile = nameOfFile.replaceFirst(".txt", ".json");
        try {
            file = new FileWriter("output/"+ nameOfFile);
            file.write(obj.toString(4));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            System.out.println("Writing to file has failed !");

        } finally {

            try {
                file.flush();
                file.close();
                System.out.println("Writing to file has been successfully finished !");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Writing to file has failed !");
            }
        }
    }

    public ArrayList<String> GetAllFilesNames()
    {
        ArrayList<String> allFilesNames = new ArrayList<>();
        File folder = new File("input");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                allFilesNames.add(listOfFiles[i].getName());
            }
        }

        return allFilesNames;
    }
}
