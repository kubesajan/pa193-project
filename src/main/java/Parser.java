import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Parser {
    private final VersionsParser verParser = new VersionsParser();
    private final NameParser nameParser = new NameParser();
    private final BibliographyParser bibParser = new BibliographyParser();
    private final RevisionsParser revParser = new RevisionsParser();
    private final TocParser tocParser = new TocParser();
    private JSONObject jsonObject = new JSONObject();
    private ArrayList<String> lines = new ArrayList<>();
    private int parsingOption;


    public String parsing(ArrayList<String> linesFromFile, int option) throws JSONException {
        if (linesFromFile.isEmpty()) {
            return "";
        }
        lines = linesFromFile;
        parsingOption = option;
        nameParser.findHeaderFooter(lines);
        nameParser.findName(lines);
        switch (parsingOption) {
            case 1:
                tocParser.parseTOC(lines);
                bibParser.parseBibliography(lines);
                parseVersions();
                revParser.findRevisions(lines);
                break;
            case 2:
                tocParser.parseTOC(lines);
                break;
            case 3:
                bibParser.parseBibliography(lines);
                break;
            case 4:
                parseVersions();
                break;
            case 5:
                revParser.findRevisions(lines);
                break;
            default:
        }
        return makeJsonStructure().toString(4);
    }

    private void parseVersions() {
        verParser.findVersions(verParser.getEalOptions(), verParser.getEalVersions(), lines);
        verParser.findVersions(verParser.getShaOptions(), verParser.getShaVersions(), lines);
        verParser.findVersions(verParser.getJavaOptions(), verParser.getJavaVersions(), lines);
        verParser.findVersions(verParser.getGpOptions(), verParser.getGpVersions(), lines);
        verParser.findVersions(verParser.getDesOptions(), verParser.getDesVersions(), lines);
        verParser.findVersions(verParser.getEccOptions(), verParser.getEccVersions(), lines);
        verParser.findVersions(verParser.getRsaOptions(), verParser.getRsaVersions(), lines);
    }

    private JSONObject makeJsonStructure() throws JSONException {
        jsonObject.put("title", nameParser.getName());
        switch (parsingOption) {
            case 1 -> {
                makeTableOfContentsJSON();
                makeBibliographyJSON();
                makeVersionsJSON();
                makeRevisionsJSON();
            }
            case 2 -> makeTableOfContentsJSON();
            case 3 -> makeBibliographyJSON();
            case 4 -> makeVersionsJSON();
            case 5 -> makeRevisionsJSON();
        }

        return jsonObject;
    }

    private void makeTableOfContentsJSON() {

        jsonObject.put("table_of_contents", new JSONArray(tocParser.getTocLines()));
    }

    private void makeBibliographyJSON() {
        JSONObject bibliography = new JSONObject();
        for (String item : bibParser.getBibliographyLines()) {
            String[] parsed = parseReferenceLine(item);
            bibliography.put(parsed[0], parsed[1]);
        }
        jsonObject.put("bibliography", bibliography);
    }

    private void makeVersionsJSON() {

        JSONObject versions = new JSONObject();
        if (!verParser.getEalVersions().isEmpty())
            versions.put("eal", verParser.getEalVersions());
        if (!verParser.getGpVersions().isEmpty())
            versions.put("global_platform", new JSONArray(verParser.getGpVersions()));
        if (!verParser.getJavaVersions().isEmpty())
            versions.put("java_card", new JSONArray(verParser.getJavaVersions()));
        if (!verParser.getShaVersions().isEmpty())
            versions.put("sha", new JSONArray(verParser.getShaVersions()));
        if (!verParser.getRsaVersions().isEmpty())
            versions.put("rsa", new JSONArray(verParser.getRsaVersions()));
        if (!verParser.getEccVersions().isEmpty())
            versions.put("ecc", new JSONArray(verParser.getEccVersions()));
        if (!verParser.getDesVersions().isEmpty())
            versions.put("des", new JSONArray(verParser.getDesVersions()));
        jsonObject.put("versions", versions);
    }

    private void makeRevisionsJSON() {
        JSONArray revisions = new JSONArray();
        for (int i = 0; i < revParser.getRevisionVersions().size(); i++) {
            JSONObject revision = new JSONObject();
            revision.put("version", revParser.getRevisionVersions().get(i));
            revision.put("date", revParser.getRevisionDates().get(i));
            revision.put("description", revParser.getRevisionDescriptions().get(i));
            revisions.put(revision);
        }
        jsonObject.put("revisions", revisions);

    }

    private String[] parseReferenceLine(String line) {
        String[] temp = line.split("]", 2);
        temp[1] = temp[1].trim();
        temp[0] = temp[0].concat("]");
        return temp;
    }
}