package Helpers;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class Utils {

    public static String loadDumpFromMachine(Stage stage) {
        FileChooser fileChooser = getFileChooser("*.mdb");
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            return file.getPath();
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

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Load Dump");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );

    }

    public static String extractSiteCode(String siteName) {
        String siteCode = "";
        if (siteName != null) {
            int siteNameLength = siteName.length();

            if (siteName.contains("_X")) {
                siteCode = siteName.substring(siteNameLength - 8, siteNameLength - 2);
            } else
                siteCode = siteName.substring(siteNameLength - 6);
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
}
