

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    private ArrayList<String> lines = new ArrayList<String>();
    public static void main(String[] args) {
        Parser parser = new Parser();
        parser.readFile();
        parser.parseReferences();

    }

    private  void readFile() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "files/example2.txt"));
            String line = reader.readLine();
            while (line != null) {
                if (line.trim().length() > 0) {
                    lines.add(line.trim());
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int findReferenceIndex(){
        int startingLine = 0;
        for(int i = 0; i< lines.size(); i++) {
            if (lines.get(i).toLowerCase().contains("references") || lines.get(i).toLowerCase().contains("bibliography")) {
                for (int j = i + 1; j < i + 6; j++) {
                    if (lines.size() > j && lines.get(j).contains("[") && lines.get(j).contains("]")) {
                        System.out.println(lines.get(j));
                        startingLine = j;
                        break;
                    }
                }
            }
        }
        return startingLine;
    }
    private void parseReferences() {
        int currentLine = findReferenceIndex();
    }

}
