import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TocParser {
    private NameParser nameParser = new NameParser();
    private ArrayList<List<String>> tocLines = new ArrayList<>();

    public ArrayList<List<String>> getTocLines() {
        return tocLines;
    }

    public void parseTOC(ArrayList<String> lines) {
        int tocStart = findTOCIndex(lines);
        int skipped = 0;
        for (int i = tocStart; i < lines.size(); i++) {
            boolean needEmpty = false;
            if (skipped > 6) {
                break;
            }
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                skipped += 1;
                continue;
            }
            if (nameParser.getHeaderFooterLines().contains(line) ||
                    nameParser.getHeaderFooterLinesExtended().contains(line) ||
                    nameParser.getName().contains(line) ||
                    line.contains("Page:") ||
                    line.contains("BSI-DSZ-CC") ||
                    (Character.isDigit(line.charAt(0)) && line.contains("/")) ||
                    line.contains("www")) {
                continue;
            }
            char ch = line.charAt(0);
            if (!Character.isDigit(ch)) {
                if ((Character.isUpperCase(ch) && Character.isAlphabetic(line.charAt(1)))) {
                    needEmpty = true;
                }
            }
            List<String> tmpList = new ArrayList<>(Arrays.asList(line.trim().split("[.\\s+]"))); //Remove dots and spaces
            tmpList.removeAll(Collections.singletonList(""));
            List<String> splitLine = new ArrayList<>();
            if (!Character.isDigit(tmpList.get(tmpList.size() - 1).charAt(0))) {
                continue;
            }
            parseToCLine(tmpList, splitLine, needEmpty, false);
        }
    }

    private void parseToCLine(List<String> tmpList, List<String> splitLine, boolean needEmpty, boolean needSpace) {
        String tmpString = tmpList.get(0);
        int k = 1;
        if (needEmpty) {
            splitLine.add("");
            k = 0;
            needSpace = false;
        } else {
            for (int j = 1; j < tmpList.size() - 1; j++) { //Join back the initial numbers.
                char ch = tmpList.get(j).charAt(0);
                if (Character.isDigit(ch)) {
                    needSpace = false;
                    tmpString = tmpString.concat(".").concat(tmpList.get(j));
                }
            }
            splitLine.add(tmpString);
        }
        tmpString = "";
        while (k < tmpList.size() - 1) { //Join back text.
            if (needSpace) {
                tmpString = tmpString.concat(" ");
            }
            String tmpLine = tmpList.get(k);
            char ch = tmpLine.charAt(0);
            if (!Character.isDigit(ch)) {
                tmpString = tmpString.concat(tmpLine);
                needSpace = true;
            }
            ++k;
        }
        splitLine.add(tmpString);
        splitLine.add(tmpList.get(tmpList.size() - 1));
        tocLines.add(splitLine);
    }

    private int findTOCIndex(ArrayList<String> lines) {
        int startingLine = 0;
        char ch;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).toLowerCase().contains("content") && startingLine == 0) {
                for (int j = i + 1; j < i + 3; j++) {
                    if (lines.get(j).isEmpty()) {
                        continue;
                    } else {
                        ch = lines.get(j).trim().charAt(0);
                    }
                    if (Character.isUpperCase(ch) || Character.isDigit(ch)) {
                        startingLine = j;
                        break;
                    }
                }
            }
        }
        return startingLine;
    }
}
