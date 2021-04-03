

import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Parser {
    private ArrayList<String> lines = new ArrayList<>();
    private ArrayList<String> bibliographyLines = new ArrayList<>();
    private static final HashSet<String> ealOptions = new HashSet<>(Arrays.asList("EAL1", "EAL 1.", "EAL1+", "EAL 1+", "EAL2", "EAL 2", "EAL2+", "EAL 2+", "EAL3", "EAL 3", "EAL3+", "EAL 3+", "EAL4", "EAL 4", "EAL4+", "EAL 4+", "EAL5", "EAL 5", "EAL5+", "EAL 5+", "EAL6", "EAL 6", "EAL6+", "EAL 6+", "EAL7", "EAL 7", "EAL7+", "EAL 7+"
    ));
    private static final HashSet<String> shaOptions = new HashSet<>(Arrays.asList("SHA1", "SHA 1", "SHA-1", "SHA224", "SHA 224", "SHA-224", "SHA256", "SHA 256", "SHA-256", "SHA384", "SHA 384", "SHA-384", "SHA512", "SHA 512", "SHA-512"
    ));
    private static final HashSet<String> javaOptions = new HashSet<>(Arrays.asList("Java Card 3.1","Java Card 3.0.5","Java Card 3.0.4","Java Card 3.0.1","Java Card 3.0","Java Card 2.2.2","Java Card 2.2.1","Java Card 2.2","Java Card 2.1.1","Java Card 2.1"
    ));
    private static final HashSet<String> gpOptions = new HashSet<>(Arrays.asList("GlobalPlatform 2.3", "GlobalPlatform 2.2.1"));
    private static final HashSet<String> desOptions = new HashSet<>(Arrays.asList("3DES", "Triple-DES", "Triple Des", "triple-DES", "TDES", "DES3", "single-des", "TripleDES", "TDES"));
    private static final HashSet<String> eccOptions = new HashSet<>(Arrays.asList("ECC 192","ECC 224","ECC 256","ECC 384","ECC 521","ECC-192","ECC-224","ECC-256","ECC-384","ECC-521"));
    private static final HashSet<String> rsaOptions = new HashSet<>(Arrays.asList("RSA-100","RSA-110","RSA-120","RSA-129","RSA-130","RSA-140","RSA-150","RSA-155","RSA-160","RSA-170","RSA-576","RSA-180","RSA-190","RSA-640","RSA-200","RSA-210","RSA-704","RSA-220","RSA-230","RSA-232","RSA-768","RSA-240","RSA-250","RSA-260","RSA-270","RSA-896","RSA-280","RSA-290","RSA-300","RSA-309","RSA-1024","RSA-310","RSA-320","RSA-330","RSA-340","RSA-350","RSA-360","RSA-370","RSA-380","RSA-390","RSA-400","RSA-410","RSA-420","RSA-430","RSA-440","RSA-450","RSA-460","RSA-1536","RSA-470","RSA-480","RSA-490","RSA-500","RSA-617","RSA-2048","RSA-4096","RSA 100","RSA 110","RSA 120","RSA 129","RSA 130","RSA 140","RSA 150","RSA 155","RSA 160","RSA 170","RSA 576","RSA 180","RSA 190","RSA 640","RSA 200","RSA 210","RSA 704","RSA 220","RSA 230","RSA 232","RSA 768","RSA 240","RSA 250","RSA 260","RSA 270","RSA 896","RSA 280","RSA 290","RSA 300","RSA 309","RSA 1024","RSA 310","RSA 320","RSA 330","RSA 340","RSA 350","RSA 360","RSA 370","RSA 380","RSA 390","RSA 400","RSA 410","RSA 420","RSA 430","RSA 440","RSA 450","RSA 460","RSA 1536","RSA 470","RSA 480","RSA 490","RSA 500","RSA 617","RSA 2048","RSA 4096"));
    private HashSet<String> ealVersions = new HashSet<>();
    private HashSet<String> shaVersions = new HashSet<>();
    private HashSet<String> javaVersions = new HashSet<>();
    private HashSet<String> gpVersions = new HashSet<>();
    private HashSet<String> desVersions = new HashSet<>();
    private HashSet<String> eccVersions = new HashSet<>();
    private HashSet<String> rsaVersions = new HashSet<>();
    private int referenceStart;
    private String name;
    private JSONObject jsonObject;
    public static void main(String[] args) {
        System.out.println(Character.isWhitespace(32));
        Parser parser = new Parser();
        parser.readFile();
        parser.parseReferences();
        parser.findVersions(ealOptions, parser.ealVersions);
        parser.findVersions(shaOptions, parser.shaVersions);
        parser.findVersions(javaOptions, parser.javaVersions);
        parser.findVersions(gpOptions, parser.gpVersions);
        parser.findVersions(desOptions, parser.desVersions);
        parser.findVersions(eccOptions, parser.eccVersions);
        parser.findVersions(rsaOptions, parser.rsaVersions);
        parser.findName();
        parser.makeJsonStructure();
        System.out.println(parser.jsonObject.toString(4));
        System.out.println("Ahoj");

    }

    private  void readFile() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "files/example1.txt"));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
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
                        startingLine = j;
                        break;
                    }
                }
            }
        }
        return startingLine;
    }
    private void parseReferences() {
        boolean refClosed = true;
        referenceStart = findReferenceIndex();
        String reference = "";
        int noBrackets = 0;
        for(int i = referenceStart; i< lines.size(); i++) {
            String line = lines.get(i);
            if(line.contains("•")) {
                noBrackets = 0;
                continue;
            }
            if(noBrackets >= 20) {
                break;
            }
            if (line.contains("[") && line.contains("]")) {
                noBrackets= 0;
                if (reference.length() > 0) {
                    bibliographyLines.add(reference);
                    reference = "";
                }
                reference = reference.concat(line).trim();
                refClosed = false;
            }
            else if (line.isBlank() ) {
                refClosed = true;
                ++noBrackets;
                if (!reference.isBlank()) {
                    bibliographyLines.add(reference);
                    reference = "";
                }
            }
            else if (!refClosed) {
                reference = reference.concat(" ").concat(line.trim());
                ++noBrackets;
            }

            else {
                ++noBrackets;
            }
        }
    }
    private void findVersions(HashSet<String> options, HashSet<String> found) {
        for (int i = 0; i < referenceStart; i++) {
            String line = lines.get(i);
            for (String option: options) {
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
            if(line.contains("Title")) {
                line = line.replace("Title", "");
                name = line.trim();
                return;
            }
        }
    }
    private void makeJsonStructure () {
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
        JSONObject bibliography = new JSONObject();
        for (String item : bibliographyLines) {
            String[] parsed = parseReferenceLine(item);
            bibliography.put(parsed[0], parsed[1]);
        }
        obj.put("bibliography", bibliography);
        jsonObject = obj;

    }
    private String[] parseReferenceLine(String line){
        String[] temp = line.split("]", 2);
        temp[1] = temp[1].trim();
        temp[0] = temp[0].concat("]");
        return temp;

    }

}
