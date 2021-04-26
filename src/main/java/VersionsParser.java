import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class VersionsParser {
    private  final HashSet<String> ealOptions = new HashSet<>(Arrays.asList("EAL1", "EAL 1.", "EAL1+", "EAL 1+", "EAL2", "EAL 2", "EAL2+", "EAL 2+", "EAL3", "EAL 3", "EAL3+", "EAL 3+", "EAL4", "EAL 4", "EAL4+", "EAL 4+", "EAL5", "EAL 5", "EAL5+", "EAL 5+", "EAL6", "EAL 6", "EAL6+", "EAL 6+", "EAL7", "EAL 7", "EAL7+", "EAL 7+"));
    private  final HashSet<String> shaOptions = new HashSet<>(Arrays.asList("SHA1", "SHA 1", "SHA-1", "SHA-2", "SHA224", "SHA 224", "SHA-224", "SHA256", "SHA 256", "SHA-256", "SHA384", "SHA 384", "SHA-384", "SHA512", "SHA 512", "SHA-512"));
    private  final HashSet<String> javaOptions = new HashSet<>(Arrays.asList("Java Card 3.1", "Java Card 3.0.5", "Java Card 3.0.4", "Java Card 3.0.1", "Java Card 3.0", "Java Card 2.2.2", "Java Card 2.2.1", "Java Card 2.2", "Java Card 2.1.1", "Java Card 2.1"));
    private  final HashSet<String> gpOptions = new HashSet<>(Arrays.asList("GlobalPlatform 2.3", "GlobalPlatform 2.2.1"));
    private  final HashSet<String> desOptions = new HashSet<>(Arrays.asList("3DES", "Triple-DES", "Triple Des", "triple-DES", "DES3", "single-des", "TripleDES", "TDES"));
    private  final HashSet<String> eccOptions = new HashSet<>(Arrays.asList("ECC 192", "ECC 224", "ECC 256", "ECC 384", "ECC 521", "ECC-192", "ECC-224", "ECC-256", "ECC-384", "ECC-521"));
    private  final HashSet<String> rsaOptions = new HashSet<>(Arrays.asList("RSA-100", "RSA-110", "RSA-120", "RSA-129", "RSA-130", "RSA-140", "RSA-150", "RSA-155", "RSA-160", "RSA-170", "RSA-576", "RSA-180", "RSA-190", "RSA-640", "RSA-200", "RSA-210", "RSA-704", "RSA-220", "RSA-230", "RSA-232", "RSA-768", "RSA-240", "RSA-250", "RSA-260", "RSA-270", "RSA-896", "RSA-280", "RSA-290", "RSA-300", "RSA-309", "RSA-1024", "RSA-310", "RSA-320", "RSA-330", "RSA-340", "RSA-350", "RSA-360", "RSA-370", "RSA-380", "RSA-390", "RSA-400", "RSA-410", "RSA-420", "RSA-430", "RSA-440", "RSA-450", "RSA-460", "RSA-1536", "RSA-470", "RSA-480", "RSA-490", "RSA-500", "RSA-617", "RSA-2048", "RSA-4096", "RSA 100", "RSA 110", "RSA 120", "RSA 129", "RSA 130", "RSA 140", "RSA 150", "RSA 155", "RSA 160", "RSA 170", "RSA 576", "RSA 180", "RSA 190", "RSA 640", "RSA 200", "RSA 210", "RSA 704", "RSA 220", "RSA 230", "RSA 232", "RSA 768", "RSA 240", "RSA 250", "RSA 260", "RSA 270", "RSA 896", "RSA 280", "RSA 290", "RSA 300", "RSA 309", "RSA 1024", "RSA 310", "RSA 320", "RSA 330", "RSA 340", "RSA 350", "RSA 360", "RSA 370", "RSA 380", "RSA 390", "RSA 400", "RSA 410", "RSA 420", "RSA 430", "RSA 440", "RSA 450", "RSA 460", "RSA 1536", "RSA 470", "RSA 480", "RSA 490", "RSA 500", "RSA 617", "RSA 2048", "RSA 4096", "RSA2048", "RSA4096"));
    private HashSet<String> ealVersions = new HashSet<>();
    private HashSet<String> shaVersions = new HashSet<>();
    private HashSet<String> javaVersions = new HashSet<>();
    private HashSet<String> gpVersions = new HashSet<>();
    private HashSet<String> desVersions = new HashSet<>();
    private HashSet<String> eccVersions = new HashSet<>();
    private HashSet<String> rsaVersions = new HashSet<>();

    public HashSet<String> getEalOptions()  { return this.ealOptions; }
    public HashSet<String> getShaOptions()  { return this.shaOptions; }
    public HashSet<String> getJavaOptions() { return this.javaOptions; }
    public HashSet<String> getGpOptions()   { return this.gpOptions; }
    public HashSet<String> getDesOptions()  { return this.desOptions; }
    public HashSet<String> getEccOptions()  { return this.eccOptions; }
    public HashSet<String> getRsaOptions()  { return this.rsaOptions; }

    public HashSet<String> getEalVersions()  { return this.ealVersions; }
    public HashSet<String> getShaVersions() { return this.shaVersions; }
    public HashSet<String> getJavaVersions() { return this.javaVersions; }
    public HashSet<String> getGpVersions()   { return this.gpVersions; }
    public HashSet<String> getDesVersions()  { return this.desVersions; }
    public HashSet<String> getEccVersions()  { return this.eccVersions; }
    public HashSet<String> getRsaVersions()  { return this.rsaVersions; }

    public void findVersions(HashSet<String> options, HashSet<String> found, ArrayList<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
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


}
