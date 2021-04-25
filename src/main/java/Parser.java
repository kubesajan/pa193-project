import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Parser {
    private int referenceStart;
    private JSONObject jsonObject;
    private ArrayList<String> lines = new ArrayList<>();
    private ArrayList<String> revisionVersions = new ArrayList<>();
    private ArrayList<String> revisionDates = new ArrayList<>();
    private ArrayList<String> revisionDescriptions = new ArrayList<>();
    private ArrayList<String> bibliographyLines = new ArrayList<>();
    private String name = "";
    private final HashSet<String> ealOptions = new HashSet<>(Arrays.asList("EAL1", "EAL 1.", "EAL1+", "EAL 1+", "EAL2", "EAL 2", "EAL2+", "EAL 2+", "EAL3", "EAL 3", "EAL3+", "EAL 3+", "EAL4", "EAL 4", "EAL4+", "EAL 4+", "EAL5", "EAL 5", "EAL5+", "EAL 5+", "EAL6", "EAL 6", "EAL6+", "EAL 6+", "EAL7", "EAL 7", "EAL7+", "EAL 7+"));
    private final HashSet<String> shaOptions = new HashSet<>(Arrays.asList("SHA1", "SHA 1", "SHA-1", "SHA224", "SHA 224", "SHA-224", "SHA256", "SHA 256", "SHA-256", "SHA384", "SHA 384", "SHA-384", "SHA512", "SHA 512", "SHA-512"));
    private final HashSet<String> javaOptions = new HashSet<>(Arrays.asList("Java Card 3.1", "Java Card 3.0.5", "Java Card 3.0.4", "Java Card 3.0.1", "Java Card 3.0", "Java Card 2.2.2", "Java Card 2.2.1", "Java Card 2.2", "Java Card 2.1.1", "Java Card 2.1"));
    private final HashSet<String> gpOptions = new HashSet<>(Arrays.asList("GlobalPlatform 2.3", "GlobalPlatform 2.2.1"));
    private final HashSet<String> desOptions = new HashSet<>(Arrays.asList("3DES", "Triple-DES", "Triple Des", "triple-DES", "TDES", "DES3", "single-des", "TripleDES", "TDES"));
    private final HashSet<String> eccOptions = new HashSet<>(Arrays.asList("ECC 192", "ECC 224", "ECC 256", "ECC 384", "ECC 521", "ECC-192", "ECC-224", "ECC-256", "ECC-384", "ECC-521"));
    private final HashSet<String> rsaOptions = new HashSet<>(Arrays.asList("RSA-100", "RSA-110", "RSA-120", "RSA-129", "RSA-130", "RSA-140", "RSA-150", "RSA-155", "RSA-160", "RSA-170", "RSA-576", "RSA-180", "RSA-190", "RSA-640", "RSA-200", "RSA-210", "RSA-704", "RSA-220", "RSA-230", "RSA-232", "RSA-768", "RSA-240", "RSA-250", "RSA-260", "RSA-270", "RSA-896", "RSA-280", "RSA-290", "RSA-300", "RSA-309", "RSA-1024", "RSA-310", "RSA-320", "RSA-330", "RSA-340", "RSA-350", "RSA-360", "RSA-370", "RSA-380", "RSA-390", "RSA-400", "RSA-410", "RSA-420", "RSA-430", "RSA-440", "RSA-450", "RSA-460", "RSA-1536", "RSA-470", "RSA-480", "RSA-490", "RSA-500", "RSA-617", "RSA-2048", "RSA-4096", "RSA 100", "RSA 110", "RSA 120", "RSA 129", "RSA 130", "RSA 140", "RSA 150", "RSA 155", "RSA 160", "RSA 170", "RSA 576", "RSA 180", "RSA 190", "RSA 640", "RSA 200", "RSA 210", "RSA 704", "RSA 220", "RSA 230", "RSA 232", "RSA 768", "RSA 240", "RSA 250", "RSA 260", "RSA 270", "RSA 896", "RSA 280", "RSA 290", "RSA 300", "RSA 309", "RSA 1024", "RSA 310", "RSA 320", "RSA 330", "RSA 340", "RSA 350", "RSA 360", "RSA 370", "RSA 380", "RSA 390", "RSA 400", "RSA 410", "RSA 420", "RSA 430", "RSA 440", "RSA 450", "RSA 460", "RSA 1536", "RSA 470", "RSA 480", "RSA 490", "RSA 500", "RSA 617", "RSA 2048", "RSA 4096"));
    private HashSet<String> ealVersions = new HashSet<>();
    private HashSet<String> shaVersions = new HashSet<>();
    private HashSet<String> javaVersions = new HashSet<>();
    private HashSet<String> gpVersions = new HashSet<>();
    private HashSet<String> desVersions = new HashSet<>();
    private HashSet<String> eccVersions = new HashSet<>();
    private HashSet<String> rsaVersions = new HashSet<>();


    public JSONObject parsing(ArrayList<String> linesFromFile) throws JSONException {
        if (linesFromFile.isEmpty()) {
            return new JSONObject();
        }
        lines = linesFromFile;
        parseReferences();
        findVersions(ealOptions, ealVersions);
        findVersions(shaOptions, shaVersions);
        findVersions(javaOptions, javaVersions);
        findVersions(gpOptions, gpVersions);
        findVersions(desOptions, desVersions);
        findVersions(eccOptions, eccVersions);
        findVersions(rsaOptions, rsaVersions);
        findRevisions();
        findName();

        return makeJsonStructure();
    }

    private void findRevisions(){
        String version = "";
        String date = "";
        String description = "";
        int currentPage = 1;
        int candidatePage = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("\f")) {
                currentPage++;
            }
            else if (line.toLowerCase().contains("rev")) {
                candidatePage = currentPage;
            }
            else if ((candidatePage == currentPage) && line.contains("1.0")) {
                while (!line.contains("\f")) {
                    if (!line.isBlank()) {
                        String[] splitRevision = line.split("\\s{2,100}");
                        if (splitRevision.length == 1) {
                            description = description.concat(splitRevision[0].trim());
                        }
                        else {
                            if (!version.isBlank() && version.length() < 6) {
                                if (!revisionVersions.contains(version)) {
                                    revisionVersions.add(version);
                                    revisionDates.add(date);
                                    revisionDescriptions.add(description);
                                }
                            }

                            description = splitRevision[splitRevision.length-1].trim();
                            for (int j = 0; j < splitRevision.length - 1; j++) {
                                if (splitRevision[j].contains("."))
                                    version = splitRevision[j].trim();
                                else if (splitRevision[j].contains("-"))
                                    date = splitRevision[j].trim();
                            }
                        }
                    }
                    i++;
                    line = lines.get(i);
                }
                if (!version.equals(revisionVersions.get(revisionVersions.size() - 1)) && version.length() < 6) {
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

    private void parseReferences() {
        bibliographyLines = new ArrayList<>();
        boolean refClosed = true;
        referenceStart = findReferenceIndex();
        String reference = "";
        int noBrackets = 0;
        for (int i = referenceStart; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("•")) {
                noBrackets = 0;
                continue;
            }
            if (noBrackets >= 20) {
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

    private int findReferenceIndex() {
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

    private void findVersions(HashSet<String> options, HashSet<String> found) {
        for (int i = 0; i < referenceStart; i++) {
            String line = lines.get(i);
            for (String option : options) {
                if (line.contains(option)) {
                    if (!line.contains(option.concat("+"))) {
                        found.add(option);
                    }

                }
            }
        }
    }

    private void findName() {
        for (int i = 0; i < referenceStart; i++) {
            String line = lines.get(i);
            if (line.contains("Title")) {
                line = line.replace("Title", "");
                line = line.trim();
                name = line;
                return;
            }
        }
    }

    private JSONObject makeJsonStructure() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("title", name);
        JSONObject versions = new JSONObject();
        versions.put("eal", ealVersions);
        versions.put("global_platform", new JSONArray(gpVersions));
        versions.put("java_card", new JSONArray(javaVersions));
        versions.put("sha", new JSONArray(shaVersions));
        versions.put("rsa", new JSONArray(rsaVersions));
        versions.put("ecc", new JSONArray(eccVersions));
        versions.put("des", new JSONArray(desVersions));
        obj.put("versions", versions);
        JSONArray revisions = new JSONArray();
        for (int i = 0; i < revisionVersions.size(); i++) {
            JSONObject revision = new JSONObject();
            revision.put("version", revisionVersions.get(i));
            revision.put("date", revisionDates.get(i));
            revision.put("description", revisionDescriptions.get(i));
            revisions.put(revision);
        }
        obj.put("revisions", revisions);
        JSONObject bibliography = new JSONObject();
        for (String item : bibliographyLines) {
            String[] parsed = parseReferenceLine(item);
            bibliography.put(parsed[0], parsed[1]);
        }
        obj.put("bibliography", bibliography);
        jsonObject = obj;
        return jsonObject;
    }

    private String[] parseReferenceLine(String line) {
        String[] temp = line.split("]", 2);
        temp[1] = temp[1].trim();
        temp[0] = temp[0].concat("]");
        return temp;
    }
}