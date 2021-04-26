import java.util.ArrayList;

public class NameParser {
    private ArrayList<String> headerFooterLines = new ArrayList<>();
    private ArrayList<String> headerFooterLinesExtended = new ArrayList<>();
    private int titleProbability;
    private String name = "";

    public ArrayList<String> getHeaderFooterLines() {
        return this.headerFooterLines;
    }

    public ArrayList<String> getHeaderFooterLinesExtended() {
        return this.headerFooterLinesExtended;
    }

    public String getName() {
        return this.name;
    }

    public void findHeaderFooter(ArrayList<String> lines) {
        for (int i = 5; i < lines.size(); i++) {
            if (lines.get(i).contains("\f")) {
                for (int j = 0; j < 4; j++) {
                    if (headerFooterLines.contains(lines.get(j))) {
                        titleProbability++;
                    } else {
                        headerFooterLines.add(lines.get(j));
                    }
                }
                for (int j = 4; j < 10; j++) {
                    if (!headerFooterLinesExtended.contains(lines.get(j))) {
                        headerFooterLinesExtended.add(lines.get(j));
                    }
                }
            }
        }
    }

    public void findName(ArrayList<String> lines) {
        if (!name.isEmpty()) {
            return;
        }
        int x = 0;
        for (String line : lines) {
            if (line.contains("/f")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }
            if (x < 20) {
                if (headerFooterLines.contains(line) && !name.contains(line.trim())) {
                    if (!name.isEmpty()) {
                        name = name.concat(" ").concat(line.trim());
                    } else {
                        name = name.concat(line.trim());
                    }
                }
                if (headerFooterLinesExtended.contains(line) && !name.contains(line.trim())) {
                    if (!name.isEmpty()) {
                        name = name.concat(" ").concat(line.trim());
                    } else {
                        name = name.concat(line.trim());
                    }
                }
            }
            x++;
        }
        if (!name.isEmpty()) {
            return;
        }
        if (titleProbability > 4) {
            for (String headerFooterLine : headerFooterLines) {
                name = name.concat(headerFooterLine.trim().concat(" "));
            }
            return;
        }
        for (String line : lines) {
            if (line.contains("Title")) {
                line = line.replace("Title", "");
                line = line.trim();
                name = line;
            }
        }
    }
}
