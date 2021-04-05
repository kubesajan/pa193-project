import org.json.JSONException;

public class ProjectMainClass {
    public static void main(String[] args) throws JSONException {
        Parser parser = new Parser();
        ReadWriteFile readWriteFile = new ReadWriteFile();
        var allFilesNames = readWriteFile.GetAllFilesNames();
        for (String fileName: allFilesNames) {
            readWriteFile.WriteToFile(parser.Inicialize(readWriteFile.readFile(fileName)), fileName);
        }
    }
}
