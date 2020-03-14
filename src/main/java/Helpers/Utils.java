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

    public static String convertLTEMIMO(int mimo) {
        switch (mimo) {
            case 0:

                return "SISO";
            case 30:
            case 40:
                return "2X2";
            case 43:
                return "4X4";
            default:
                return "";
        }
    }

    public static String convertLTEBW(int bw) {
        switch (bw) {
            case 0:
                return "0";
            case 50:
                return "5M";
            case 100:
                return "10M";
            case 150:
                return "15M";
            default:
                return "";
        }
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

    public static String mapToRegion(String controller) {
        String region;

        switch (controller) {

            case "16":
            case "22":
            case "30":
            case "38":
            case "48":
            case "64":
            case "AMERYA01":
            case "MAMOURA01":
            case "MANSHIA01":
            case "MANSHIA02":
            case "MIAMI01":
            case "NEWAWAYED01":
            case "NEWAWAYED02":
            case "SEMOUHA02":
            case "SMOUHA01":
                region = "AL";
                break;
            case "4":
            case "12":
            case "24":
            case "36":
            case "54":
            case "28":
            case "46":
            case "52":
            case "44":
            case "56":
            case "Banha mcBSC":
            case "BANHA01":
            case "DAMANHOUR01":
            case "DAMAS01":
            case "DUMYAT01":
            case "FAQOUS01":
            case "HIHYA01":
            case "KFDW01":
            case "KFRELSHKH01":
            case "MAHALLA01":
            case "MANSOURA01":
            case "MINUF01":
            case "Port Said":
            case "SHIBIN01":
            case "TANTA01":
            case "ZAGAZIG01":
            case "ZAGAZIG02":
                region = "DE";
                break;
            case "26":
            case "18":
            case "SHARM01":
                region = "RE";
                break;
            case "2":
            case "14":
            case "34":
            case "50":
            case "58":
            case "60":
            case "62":
            case "ASWAN01":
            case "ASWAN02":
            case "ASYOUT01":
            case "ASYOUT02":
            case "BENISUEF01":
            case "BNISUEF02":
            case "MINYA01":
            case "QENA01":
            case "QENA02":
            case "SOHAG01":
            case "SOHAG02":
                region = "UP";
                break;
            default:
                region = "";
                break;
        }
        return region;
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
