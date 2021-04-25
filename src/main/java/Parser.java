import org.apache.commons.io.filefilter.FalseFileFilter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Parser {
    private int referenceStart;
    private int titleProbability;
    private JSONObject jsonObject;
    private ArrayList<String> lines = new ArrayList<>();
    private ArrayList<String> revisionVersions = new ArrayList<>();
    private ArrayList<String> revisionDates = new ArrayList<>();
    private ArrayList<String> revisionDescriptions = new ArrayList<>();
    private ArrayList<String> bibliographyLines = new ArrayList<>();
    private ArrayList<List<String>> tocLines = new ArrayList<List<String>>();
    private ArrayList<String> headerFooterLines = new ArrayList<>();
    private ArrayList<String> headerFooterLinesExtended = new ArrayList<>();
    private String name = "";
    private static final HashSet<String> ealOptions = new HashSet<>(Arrays.asList("EAL1", "EAL 1.", "EAL1+", "EAL 1+", "EAL2", "EAL 2", "EAL2+", "EAL 2+", "EAL3", "EAL 3", "EAL3+", "EAL 3+", "EAL4", "EAL 4", "EAL4+", "EAL 4+", "EAL5", "EAL 5", "EAL5+", "EAL 5+", "EAL6", "EAL 6", "EAL6+", "EAL 6+", "EAL7", "EAL 7", "EAL7+", "EAL 7+"));
    private static final HashSet<String> shaOptions = new HashSet<>(Arrays.asList("SHA1", "SHA 1", "SHA-1", "SHA224", "SHA 224", "SHA-224", "SHA256", "SHA 256", "SHA-256", "SHA384", "SHA 384", "SHA-384", "SHA512", "SHA 512", "SHA-512"));
    private static final HashSet<String> javaOptions = new HashSet<>(Arrays.asList("Java Card 3.1", "Java Card 3.0.5", "Java Card 3.0.4", "Java Card 3.0.1", "Java Card 3.0", "Java Card 2.2.2", "Java Card 2.2.1", "Java Card 2.2", "Java Card 2.1.1", "Java Card 2.1"));
    private static final HashSet<String> gpOptions = new HashSet<>(Arrays.asList("GlobalPlatform 2.3", "GlobalPlatform 2.2.1"));
    private static final HashSet<String> desOptions = new HashSet<>(Arrays.asList("3DES", "Triple-DES", "Triple Des", "triple-DES", "TDES", "DES3", "single-des", "TripleDES", "TDES"));
    private static final HashSet<String> eccOptions = new HashSet<>(Arrays.asList("ECC 192", "ECC 224", "ECC 256", "ECC 384", "ECC 521", "ECC-192", "ECC-224", "ECC-256", "ECC-384", "ECC-521"));
    private static final HashSet<String> rsaOptions = new HashSet<>(Arrays.asList("RSA-100", "RSA-110", "RSA-120", "RSA-129", "RSA-130", "RSA-140", "RSA-150", "RSA-155", "RSA-160", "RSA-170", "RSA-576", "RSA-180", "RSA-190", "RSA-640", "RSA-200", "RSA-210", "RSA-704", "RSA-220", "RSA-230", "RSA-232", "RSA-768", "RSA-240", "RSA-250", "RSA-260", "RSA-270", "RSA-896", "RSA-280", "RSA-290", "RSA-300", "RSA-309", "RSA-1024", "RSA-310", "RSA-320", "RSA-330", "RSA-340", "RSA-350", "RSA-360", "RSA-370", "RSA-380", "RSA-390", "RSA-400", "RSA-410", "RSA-420", "RSA-430", "RSA-440", "RSA-450", "RSA-460", "RSA-1536", "RSA-470", "RSA-480", "RSA-490", "RSA-500", "RSA-617", "RSA-2048", "RSA-4096", "RSA 100", "RSA 110", "RSA 120", "RSA 129", "RSA 130", "RSA 140", "RSA 150", "RSA 155", "RSA 160", "RSA 170", "RSA 576", "RSA 180", "RSA 190", "RSA 640", "RSA 200", "RSA 210", "RSA 704", "RSA 220", "RSA 230", "RSA 232", "RSA 768", "RSA 240", "RSA 250", "RSA 260", "RSA 270", "RSA 896", "RSA 280", "RSA 290", "RSA 300", "RSA 309", "RSA 1024", "RSA 310", "RSA 320", "RSA 330", "RSA 340", "RSA 350", "RSA 360", "RSA 370", "RSA 380", "RSA 390", "RSA 400", "RSA 410", "RSA 420", "RSA 430", "RSA 440", "RSA 450", "RSA 460", "RSA 1536", "RSA 470", "RSA 480", "RSA 490", "RSA 500", "RSA 617", "RSA 2048", "RSA 4096"));
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
        findHeaderFooter();
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
        parseTOC();

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
                                else if (splitRevision[j].contains("-") || splitRevision[j].contains("/"))
                                    date = splitRevision[j].trim();
                            }
                        }
                    }
                    i++;
                    line = lines.get(i);
                }
                if ((revisionVersions.size() != 0 && !version.equals(revisionVersions.get(revisionVersions.size() - 1)) ) && version.length() < 6) {
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
            }
            x++;
        }
        if (!name.isEmpty()) {
            return;
        }
        x = 0;
        for (String line : lines) {
            if (line.contains("/f")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }
            if (x < 20) {
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
            for (int i = 0; i < headerFooterLines.size(); i++) {
                name = name.concat(headerFooterLines.get(i).trim().concat(" "));
            }
            return;
        }
        //this part is buggy, sadly
        for (int i = 0; i < referenceStart; i++) {
            String line = lines.get(i);
            if (line.contains("Title")) {
                line = line.replace("Title", "");
                line = line.trim();
                name = line;
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
        obj.put("table_of_contents", new JSONArray(tocLines));
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
    private int findTOCIndex() {
        int startingLine = 0;
        char ch = (char) 0;
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
    private void parseTOC() {
        int tocStart = findTOCIndex();
        int skipped = 0;
        for (int i = tocStart; i < lines.size(); i++) {
            boolean needEmpty = false;
            boolean needSpace = false;
            if (skipped > 4) {
                break;
            }
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                skipped += 1;
                continue;
            }
            if (headerFooterLines.contains(line) ||headerFooterLinesExtended.contains(line) || name.contains(line)) {
                continue;
            }
            char ch = line.charAt(0);
            if (!Character.isDigit(ch)){
                if ((Character.isUpperCase(ch) && Character.isAlphabetic(line.charAt(1)))){
                    needEmpty = true;
                }
            }
            List<String> tmpList = new ArrayList<String>(Arrays.asList(line.trim().split("[.\\s+]"))); //Remove dots and spaces
            tmpList.removeAll(Arrays.asList(""));
            List<String> splitLine = new ArrayList<String>();
            String tmpString = tmpList.get(0);
            int k = 1;
            if (needEmpty) {
                splitLine.add("");
                k = 0;
                needSpace = false;
            } else {
                for (int j = 1; j < tmpList.size() - 1; j++) { //Join back the initial numbers.
                    ch = tmpList.get(j).charAt(0);
                    if (Character.isDigit(ch)){
                        needSpace = false;
                        tmpString = tmpString.concat(".").concat(tmpList.get(j));
                    }
                }
                splitLine.add(tmpString);
            }
            tmpString = "";
            while(k < tmpList.size() - 1) { //Join back text.
                if (needSpace) {
                    tmpString = tmpString.concat(" ");
                }
                String tmpLine = tmpList.get(k);
                ch = tmpLine.charAt(0);
                if (!Character.isDigit(ch)) {
                    tmpString = tmpString.concat(tmpLine);
                    needSpace = true;
                }
                ++k;
            }
            splitLine.add(tmpString);
            splitLine.add(tmpList.get(tmpList.size()-1));
            tocLines.add(splitLine);
        }
    }

    private void findHeaderFooter() {
        for (int i = 5; i < lines.size(); i++) {
            if (lines.get(i).contains("\f")) {
                for (int j = 0; j < 4; j++) {
                    if (headerFooterLines.contains(lines.get(j))) {
                        titleProbability++;
                    } else{
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


}