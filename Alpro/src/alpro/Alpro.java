package alpro;

import java.io.File;

public class Alpro {

    public static void main(String args[]) {
//        Editor e = new Editor();
//        e.main();
        File folder = new File("src/File");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    System.out.println("File " + listOfFiles[i].getName());
                } else if (listOfFiles[i].isDirectory()) {
                    System.out.println("Directory " + listOfFiles[i].getName());
                }
            }
        }
        MainMenu e = new MainMenu();
        e.main();
    }

}
