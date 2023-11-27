import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static boolean saveGame(String path, GameProgress game) {
        boolean result = false;
        var save = new File(path);
        try (FileOutputStream fos = new FileOutputStream(save);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
            result = true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public static GameProgress openProgress(String savePath) {
        GameProgress progress = null;
        try (FileInputStream fis = new FileInputStream(savePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            progress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return progress;
    }

    public static boolean zipFiles(String path, String[] files) {
        boolean result = false;
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path))) {
            for (String fileName : files) {
                String[] filePathEls = fileName.split("\\\\");
                String name = filePathEls[filePathEls.length - 1];
                try (FileInputStream fis = new FileInputStream(fileName)) {
                    ZipEntry entry = new ZipEntry(name);
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                    result = true;
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public static boolean openZip(String zipPath, String unPackPath) {
        boolean result = false;
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                try (FileOutputStream fout = new FileOutputStream(unPackPath + "//" + name)) {
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }
                    fout.flush();
                    zin.closeEntry();
                    result = true;
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public static void main(String[] args) {

// Входные данные

        String saveRoot = "C:\\Users\\ALFA\\Desktop\\Games\\savegames";
        GameProgress first = new GameProgress(100, 5, 10, 9.5);
        GameProgress second = new GameProgress(90, 10, 17, 0.5);
        GameProgress third = new GameProgress(50, 7, 5, 19.5);
        GameProgress[] progressArr = {first, second, third};
        String[] fileNames = {"\\save1.dat", "\\save2.dat", "\\save3.dat"};

// Реализация создания сохранений

        for (int i = 0; i < fileNames.length; i++) {
            if (saveGame(saveRoot + fileNames[i], progressArr[i])) {
                System.out.println("Saved.");
            } else {
                System.out.println("Not saved.");
            }
            fileNames[i] = saveRoot + fileNames[i];
        }

// Удаление фалов сохранений после получения ответа об успешной архивации

        if (zipFiles(saveRoot + "\\zip.zip", fileNames)) {
            for (String fileName : fileNames) {
                File file = new File(fileName);
                file.delete();
            }
        }

// метод распаковки архива

        openZip(saveRoot + "\\zip.zip", saveRoot);

// метод для десереализации сохранений

        System.out.println(openProgress(saveRoot + "\\save1.dat"));
        System.out.println(openProgress(saveRoot + "\\save2.dat"));
        System.out.println(openProgress(saveRoot + "\\save3.dat"));
    }
}