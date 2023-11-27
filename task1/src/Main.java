import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();
        String[] dirs = {"\\src", "\\res", "\\savegames", "\\temp", "\\src\\main", "\\src\\test",
                "\\res\\drawbles", "\\res\\vectors", "\\res\\icons"};
        String[][] files = {{"\\src\\main", "Main.java"}, {"\\src\\main", "Utils.java"},
                {"\\temp", "temp.txt"}};
        String root = "C:\\Users\\ALFA\\Desktop\\Games";

        for (String folder : dirs) {
            var dir = new File(root + folder);
            if (dir.mkdir()) {
                builder.append("Directory ")
                        .append(root)
                        .append(folder)
                        .append(" was created.\n");
            } else {
                builder.append("ERROR. Directory ")
                        .append(root)
                        .append(folder)
                        .append(" wasn't created.\n");
            }
        }
        for (String[] fileInfo : files) {
            var file = new File(root + fileInfo[0], fileInfo[1]);
            try {
                if (file.createNewFile()) {
                    builder.append("ERROR. File ")
                            .append(fileInfo[1])
                            .append(" in directory ")
                            .append(root)
                            .append(fileInfo[0])
                            .append(" wasn't created.\n");
                } else {
                    builder.append("File ")
                            .append(fileInfo[1])
                            .append(" in directory ")
                            .append(root)
                            .append(fileInfo[0])
                            .append(" was created.\n");
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        try (FileWriter writer = new FileWriter(root + "\\temp\\temp.txt", false)) {
            writer.write(builder.toString());
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}