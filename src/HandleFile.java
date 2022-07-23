import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class HandleFile {
    private final String name;
    private final FileWriter fileWriter;
    private final PrintWriter printWriter;

    public HandleFile(String name) throws IOException {
        this.name = name;
        fileWriter = new FileWriter(name);
        printWriter = new PrintWriter(fileWriter);
    }

    public void write(String s) {
        printWriter.print(s);
    }

    public void close() throws IOException {
        printWriter.close();
        fileWriter.close();
    }
}
