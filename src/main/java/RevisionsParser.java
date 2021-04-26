import java.util.ArrayList;

public class RevisionsParser {
    private ArrayList<String> revisionVersions = new ArrayList<>();
    private ArrayList<String> revisionDates = new ArrayList<>();
    private ArrayList<String> revisionDescriptions = new ArrayList<>();

    public ArrayList<String> getRevisionVersions() { return this.revisionVersions; }
    public ArrayList<String> getRevisionDates() { return this.revisionDates; }
    public ArrayList<String> getRevisionDescriptions() { return this.revisionDescriptions; }


    public void findRevisions(ArrayList<String> lines) {
        String version = "";
        String date = "";
        String description = "";
        int currentPage = 1;
        int candidatePage = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("\f")) {
                currentPage++;
            } else if (line.toLowerCase().contains("rev")) {
                candidatePage = currentPage;
            } else if ((candidatePage == currentPage) && line.contains("1.0")) {
                while (!line.contains("\f")) {
                    if (!line.isBlank()) {
                        String[] splitRevision = line.split("\\s{2,100}");
                        if (splitRevision.length == 1) {
                            description = description.concat(splitRevision[0].trim());
                        } else {
                            if (!version.isBlank() && version.length() < 6) {
                                if (!revisionVersions.contains(version)) {
                                    revisionVersions.add(version);
                                    revisionDates.add(date);
                                    revisionDescriptions.add(description);
                                }
                            }

                            description = splitRevision[splitRevision.length - 1].trim();
                            for (int j = 0; j < splitRevision.length - 1; j++) {
                                if (splitRevision[j].contains("."))
                                    version = splitRevision[j].trim();
                                else if (splitRevision[j].contains("-") || splitRevision[j].contains("/"))
                                    date = splitRevision[j].trim();
                            }
                        }
                    }
                    i++;
                    line = lines.get(i);
                }
                if ((revisionVersions.size() != 0 && !version.equals(revisionVersions.get(revisionVersions.size() - 1))) && version.length() < 6) {
                    if (!revisionVersions.contains(version)) {
                        revisionVersions.add(version);
                        revisionDates.add(date);
                        revisionDescriptions.add(description);
                    }
                }
                return;
            }
        }
    }
}
