package Helpers;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Utils {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static File defaultPath = new File(System.getProperty("user.home"));

    public static ArrayList<File> loadXMLsFromMachine(Stage stage) {
        ArrayList<File> fileList = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        configureFileChooser("Load XMLs..", fileChooser);
        List<File> list = fileChooser.showOpenMultipleDialog(stage);
        if (list != null) {
            for (File file : list) {
                if (file.isFile() && file.getName().endsWith(".xml"))
                    fileList.add(file);
            }
        }
        return fileList;
    }

    public static File loadXMLFromMachine(Stage stage) {
        FileChooser fileChooser = getFileChooser("*.xml");
        configureFileChooser("", fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            defaultPath = new File(file.getParentFile().getPath());
            return file;
        }
        return null;
    }

    public static String loadDumpFromMachine(Stage stage) {
        FileChooser fileChooser = getFileChooser("*.mdb");
        configureFileChooser("Load dump..", fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            defaultPath = new File(file.getParentFile().getPath());
            return file.getPath();
        }
        return null;
    }

    public static String loadDatabaseFromMachine(Stage stage) {
        FileChooser fileChooser = getFileChooser("*.db");
        configureFileChooser("load database", fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            defaultPath = new File(file.getParentFile().getPath());
            return file.getName();
        }
        return null;
    }

    private static FileChooser getFileChooser(String extensions) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("." + extensions + " files", extensions);
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser;
    }

    private static void configureFileChooser(String title, final FileChooser fileChooser) {
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(defaultPath);

    }

    public static String extractSiteCode(String siteName) {
        String siteCode = "";
        try {
            if (siteName != null) {
                int siteNameLength = siteName.length();

                if (siteName.contains("_X")) {
                    siteCode = siteName.substring(siteNameLength - 8, siteNameLength - 2);
                } else
                    siteCode = siteName.substring(siteNameLength - 6);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return siteCode;
    }


    public static String extractRegion(String siteCode) {
        if (siteCode != null) {
            if (siteCode.length() < 4)
                return "";
            else {
                String region = siteCode.substring(4);
                if (region.equalsIgnoreCase("UP") || region.equalsIgnoreCase("SI") || region.equalsIgnoreCase("RE")
                        || region.equalsIgnoreCase("DE") || region.equalsIgnoreCase("AL"))
                    return region;
                else return "";
            }
        }
        return "";
    }

    public static int extractRegionId(String region) {
        int regionId;
        if (region != null) {

            switch (region) {
                case "AL":
                    regionId = 11;
                    break;
                case "DE":
                    regionId = 22;
                    break;
                case "SI":
                    regionId = 33;
                    break;
                case "RE":
                    regionId = 44;
                    break;
                case "UP":
                    regionId = 55;
                    break;
                default:
                    regionId = 99;
            }
        } else regionId = 99;
        return regionId;
    }

    public static void showErrorMessage(String header, String body) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(body);
        errorAlert.showAndWait();
    }

    public static String getTime() {
        return dateFormat.format(Calendar.getInstance().getTime());
    }
}
