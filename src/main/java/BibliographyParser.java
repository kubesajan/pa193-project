import java.util.ArrayList;

public class BibliographyParser {
    public static final int MAX_NO_BRACKETS = 20;
    private ArrayList<String> bibliographyLines = new ArrayList<>();

    public ArrayList<String> getBibliographyLines() {
        return this.bibliographyLines;
    }

    public void parseBibliography(ArrayList<String> lines) {
        bibliographyLines = new ArrayList<>();
        boolean refClosed = true;
        int referenceStart = findReferenceIndex(lines);
        String reference = "";
        int noBrackets = 0;
        for (int i = referenceStart; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("•")) {
                noBrackets = 0;
                continue;
            }
            if (noBrackets >= MAX_NO_BRACKETS) {
                break;
            }
            if (line.contains("[") && line.contains("]")) {
                noBrackets = 0;
                if (reference.length() > 0) {
                    bibliographyLines.add(reference);
                    reference = "";
                }
                reference = reference.concat(line).trim();
                refClosed = false;
            } else if (line.isBlank()) {
                refClosed = true;
                ++noBrackets;
                if (!reference.isBlank()) {
                    bibliographyLines.add(reference);
                    reference = "";
                }
            } else if (!refClosed && Character.isWhitespace(line.charAt(0))) {
                reference = reference.concat(" ").concat(line.trim());
                ++noBrackets;
            } else {
                ++noBrackets;
            }
        }
    }

    private int findReferenceIndex(ArrayList<String> lines) {
        int startingLine = 0;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).toLowerCase().contains("references") || lines.get(i).toLowerCase().contains("bibliography") || lines.get(i).toLowerCase().contains("referenced literature")) {
                for (int j = i + 1; j < i + 6; j++) {
                    if (lines.size() > j && lines.get(j).contains("[") && lines.get(j).contains("]")) {
                        startingLine = j;
                        break;
                    }
                }
            }
        }
        return startingLine;
    }
}
