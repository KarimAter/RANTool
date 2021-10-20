package sample;

import Helpers.*;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static Helpers.Constants.hwShortNameConversionMap;


public class Main extends Application {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ArrayList<EnodeB> lSitesList;
    private HashMap<String, Hardware> hwHashMap;
    private HashMap<String, Hardware> btsHwHashMap;
    private HashMap<String, Hardware> nodeBHWHashMap;
    private HashMap<String, Hardware> lteHwMap;
    private HashMap<String, Hardware> sbtsHwMap;
    private Button exportDashboardBu, load2R1DumpBu, load2R2DumpBu, load3R2DumpBu, load4R2DumpBu,
            loadXMls, saveSummary, exportChangesBu, exportDumpsheetsBu, loadExcelSheet, processBu, pickDateBu;
    private String currentTable, oldTable;
    private String databasePath;
    private String serialDatabasePath;
    private String imagePath;
    private String dumpR1Path, dump4R2Path, dump2R2Path, dump3R2Path;
    private String weekName;
    private int weekNumber;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("RAN Tool");
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        initializeButtonsAndArrays(scene);
        databasePath = "C:/Ater/Development/RAN Tool/NokiaDumpToolHistory.db";
        serialDatabasePath = "C:/Ater/Development/RAN Tool/serials.db";
        imagePath = "D:/RAN Tool/Databases/";


        // load 2G RAN1 Dump from databasePath from the machine
        load2R1DumpBu.setOnAction(event -> {
            dumpR1Path = Utils.loadDumpFromMachine(primaryStage);
            System.out.println(dumpR1Path);

        });
        load2R2DumpBu.setOnAction(event -> {
            dump2R2Path = Utils.loadDumpFromMachine(primaryStage);
            System.out.println(dump2R2Path);
        });

        load3R2DumpBu.setOnAction(event -> {
            dump3R2Path = Utils.loadDumpFromMachine(primaryStage);
            System.out.println(dump3R2Path);
        });

        load4R2DumpBu.setOnAction(event -> {
            dump4R2Path = Utils.loadDumpFromMachine(primaryStage);
            System.out.println(dump4R2Path);
        });

        processBu.setOnAction(event -> {

            if (weekName == null)
                Utils.showErrorMessage("No Week", "Please select week");
            else {
                boolean gCheck = process2GDump();
                boolean uCheck = process3GDump();
                boolean lCheck = process4GDump();

                //creates serial database for the first time..

                if (gCheck || uCheck || lCheck) {

                    System.out.println("Inserting serials in serial database. " + Utils.getTime());
                    SerialDatabaseSaver serialDatabaseSaver = new SerialDatabaseSaver(serialDatabasePath);
                    serialDatabaseSaver.insertAndRemove(hwHashMap, weekName);
                    serialDatabaseSaver.updateSiteNames(weekName);
                    System.out.println("Done.");
                    System.out.println(Utils.getTime());
                }
            }

        });


        saveSummary.setOnAction(event ->

        {
            DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
            try {
                ArrayList<Cabinet> gCabinets = databaseHelper.loadCabinets(2);
                ArrayList<Cabinet> uCabinets = databaseHelper.loadCabinets(3);
                ArrayList<Cabinet> lCabinets = databaseHelper.loadCabinets(4);
                ArrayList<Cabinet> sbtsCabinets = databaseHelper.loadCabinets(1);


                int x = 1;
                Exporter exporter = new Exporter(weekName);
                // Exporting Hardware Map..
                exporter.exportSiteHardwareMap(getHwMap());
                // Exporting 2G configuration..
                exporter.exportConfPivot(SiteMapper.getGConfigMap(gCabinets), 5, "BCFs");
                // Exporting GSM sites map..
                exporter.exportSitesPivot(SiteMapper.getGSitesMap(gCabinets), 4, "2G Sites");
                // Exporting 3G configuration..
                exporter.exportConfPivot(SiteMapper.getUConfigMap(uCabinets), 9, "NodeBs");
                // Exporting UMTS sites map..
                exporter.exportSitesPivot(SiteMapper.getUSitesMap(uCabinets), 7, "3G Sites");
                // Exporting 4G configuration..
                exporter.exportConfPivot(SiteMapper.getLConfigMap(lCabinets), 9, "LTE");
                //Exporting U900 list
                exporter.exportCarr(SiteMapper.getCarriersMap(uCabinets, cabinet -> ((NodeB) cabinet).isU900()), "U900 Sites");
                //Exporting 3rd Carrier list
                exporter.exportCarr(SiteMapper.getCarriersMap(uCabinets, cabinet -> ((NodeB) cabinet).isThirdCarrier()), "3rd Carrier Sites");

                SerialDatabaseSaver serialDatabaseSaver = new SerialDatabaseSaver(serialDatabasePath);

                exporter.exportHardwareCount(serialDatabaseSaver.getHardwareCount(), "Hardware Count");


                try {

                    List<Cabinet> sRanBcfs = gCabinets.stream().filter(cabinet -> !(((BCF) cabinet).getSbtsId().equals("null")))
                            .collect(Collectors.toList());

                    List<Cabinet> sRanNodeBs = uCabinets.stream().filter(cabinet -> !(((NodeB) cabinet).getSbtsId().equals("null")))
                            .collect(Collectors.toList());


//                    exporter.exportBcfTx(gCabinets);
                    exporter.exportNodeBTx(uCabinets);
                    exporter.exportEnodeBTx(sRanBcfs, sRanNodeBs, lCabinets);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        exportChangesBu.setOnAction(event ->

        {
            ArrayList<String> tableNames;
            DatabaseHelper databaseHelper = new DatabaseHelper(databasePath);
            tableNames = databaseHelper.getTableNames();
            createDumpsSelector(tableNames);

        });

        // export dashboard button
        exportDashboardBu.setOnAction(event ->

        {
            if (weekName == null)
                Utils.showErrorMessage("No week", "Please select week");
            else {
                try {
                    export2G();
                    export3G();
                    export4G();
                    exportSBTS();
                    Exporter exporter = new Exporter(weekName);
                    exporter.exportHwFromDatabase();
                    System.out.println("Done..." + Utils.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        exportDumpsheetsBu.setOnAction(event ->

        {
            try {
                System.out.println("Exporting Dump Sheets..." + Utils.getTime());
                Exporter exporter = new Exporter();
                System.out.println("Exporting TRX Sheet1..." + Utils.getTime());
                exporter.exportTRXSheet(dumpR1Path, dump2R2Path, weekName);
                System.out.println("Exporting 2G Cells ..." + Utils.getTime());
                exporter.exportGcellsSheet(dumpR1Path, dump2R2Path, weekName);
                System.out.println("Exporting 3G Cells ..." + Utils.getTime());
                exporter.exportUcellsSheet(dumpR1Path, dump3R2Path, weekName);
                System.out.println("Exporting 4G Cells ..." + Utils.getTime());
                exporter.exportLcellsSheet(dumpR1Path, dump4R2Path, weekName);
                System.out.println("Exporting Dump Sheets is done..." + Utils.getTime());

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        loadXMls.setOnAction(event ->
        {
            System.out.println(Utils.getTime());
            ArrayList<File> xmlFiles = Utils.loadXMLsFromMachine(primaryStage);
            for (File xmlFile : xmlFiles) {
                try {
                    String fileName = xmlFile.getName();
                    if (fileName.contains("RNC")) {
                        Hardware hardware = parse3GHardwareXML(xmlFile);
                        nodeBHWHashMap.put(hardware.getUniqueName(), hardware);

                    } else if (fileName.contains("SRAN")) {
                        Hardware hardware = parseSBTSHardwareXML(xmlFile, "SBTS");
                        sbtsHwMap.put(hardware.getUniqueName(), hardware);

                    } else if (fileName.contains("MRBTS")) {
                        if (fileName.contains("lte")) {
                            Hardware hardware = parse4GHardwareXML(xmlFile);
                            lteHwMap.put(hardware.getUniqueName(), hardware);
                        } else {
                            Hardware hardware = parseSBTSHardwareXML(xmlFile, "4G");
                            lteHwMap.put(hardware.getUniqueName(), hardware);
                        }

                    } else {
                        Hardware hardware = parse2GHardwareXML(xmlFile);
                        btsHwHashMap.put(hardware.getUniqueName(), hardware);

                    }

                } catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
                    System.out.println("XML parsing problem in: " + xmlFile.getName());
                    e.printStackTrace();
                }
            }
            hwHashMap.putAll(nodeBHWHashMap);
            hwHashMap.putAll(sbtsHwMap);
            hwHashMap.putAll(lteHwMap);
            hwHashMap.putAll(btsHwHashMap);
            System.out.println(Utils.getTime());
            System.out.println("Number of 2G XML files: " + btsHwHashMap.size());
            System.out.println("Number of 3G XML files: " + nodeBHWHashMap.size());
            System.out.println("Number of 4G XML files: " + lteHwMap.size());
            System.out.println("Number of SBTS XML files: " + sbtsHwMap.size());
            System.out.println("Total number of XML files: " + hwHashMap.size());
//            hwHashMap.forEach((s, hardware) -> System.out.println(s));
            System.out.println(Utils.getTime());
        });


        loadExcelSheet.setOnAction(event ->

        {

        });


        pickDateBu.setOnAction(event ->

        {
            showCalendar();
        });
    }


    private Map<String, List<Hardware>> getHwMap() throws SQLException {
        DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
        Map<String, List<Cabinet>> gHardware = databaseHelper.loadCabinets(2).stream().
                collect(Collectors.groupingBy(Cabinet::getCode));

        Map<String, List<Cabinet>> uHardware = databaseHelper.loadCabinets(3).stream().
                collect(Collectors.groupingBy(Cabinet::getCode));

        Map<String, List<Cabinet>> lHardware = databaseHelper.loadCabinets(4).stream().
                collect(Collectors.groupingBy(Cabinet::getCode));

        List<Hardware> gSites = gHardware.entrySet().stream().map(Hardware.HwItemsAdder(2)).collect(Collectors.toList());
        List<Hardware> uSites = uHardware.entrySet().stream().map(Hardware.HwItemsAdder(3)).collect(Collectors.toList());
        List<Hardware> lSites = lHardware.entrySet().stream().map(Hardware.HwItemsAdder(4)).collect(Collectors.toList());

        List<Hardware> hardwareList = new ArrayList<>();
        hardwareList.addAll(gSites);
        hardwareList.addAll(uSites);
        hardwareList.addAll(lSites);

        return hardwareList.stream().sorted(Comparator.comparing(Hardware::getTech))
                .collect(Collectors.groupingBy(Hardware::getCode));
    }


    private Function<Map.Entry<String, HashMap<String, List<Cabinet>>>, StatBox> getCellsMapper() {
        return cabin -> {
            StatBox statBox = new StatBox();
            statBox.setControllerId(cabin.getKey());

//            cabin.getValue().forEach(cabinet -> {
//                statBox.setParam1(statBox.getParam1()+cabinet.getNumberOfCells());
//                statBox.setParam2(statBox.getParam2()+cabinet.getNumberOfOnAirCells());
//            });
            return statBox;
        };
    }


    private void export2G() throws IOException {

        System.out.println("Exporting BCFs..." + Utils.getTime());

        DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
        try {
            ArrayList<Cabinet> bcfs = databaseHelper.loadCabinets(2);
            Exporter exporter = new Exporter(weekName);
            System.out.println("Exporting BCFs..." + Utils.getTime());
            exporter.exportBcfList(bcfs);
            System.out.println("Exporting BCFs HW..." + Utils.getTime());
            exporter.exportBcfHardWare(bcfs);
            System.out.println("2G Dashboard is ready.." + Utils.getTime());
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    private void export3G() throws IOException {
        System.out.println("Exporting 3G...");
        DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
        try {
            ArrayList<Cabinet> nodeBList = databaseHelper.loadCabinets(3);
            Exporter exporter = new Exporter();
            System.out.println("Exporting NodeBs..." + Utils.getTime());
            exporter.exportNodeBList(nodeBList);
            System.out.println("Exporting NodeBs Hardware..." + Utils.getTime());
            exporter.exportNodeBHardWare(nodeBList);
            System.out.println("3G Dashboard is ready.." + Utils.getTime());
        } catch (SQLException e) {
            e.printStackTrace();
        }


//        Exporter.export3GHardWare(uSitesList, "3G HW");
        System.out.println("3G Dashboard is ready.." + Utils.getTime());
    }

    private void export4G() throws IOException {
        System.out.println("Exporting 4G..." + Utils.getTime());
        if (lSitesList == null) {
            DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
            try {
                ArrayList<Cabinet> lSitesList = databaseHelper.loadCabinets(4);
                Exporter exporter = new Exporter();
                System.out.println("Exporting eNodeBs..." + Utils.getTime());
                exporter.exportEnodeBList(lSitesList);
                System.out.println("Exporting eNodeBs hardware..." + Utils.getTime());
                exporter.exportEnodeBHardWare(lSitesList);
                System.out.println("4G Dashboard is ready.." + Utils.getTime());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("4G Dashboard is ready.." + Utils.getTime());
    }

    private void exportSBTS() throws IOException {

        System.out.println("Exporting SBTS..." + Utils.getTime());
        DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
        try {
            ArrayList<Cabinet> sbtsList = databaseHelper.loadCabinets(1);
            Exporter exporter = new Exporter();
            System.out.println("Exporting SBTS..." + Utils.getTime());
            exporter.exportSBTSList(sbtsList);
            System.out.println("Exporting SBTS hardware..." + Utils.getTime());
            exporter.exportSBTSHardWare(sbtsList);
            System.out.println("SRAN Dashboard is ready.." + Utils.getTime());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        System.out.println("4G Dashboard is ready.." + Utils.getTime());
    }

    private boolean process2GDump() {
        if (dumpR1Path == null || dump2R2Path == null) {
            Utils.showErrorMessage("No dumps selected..", "Please enter 2G RAN1 and RAN2 dumps");
        } else if (hwHashMap == null) {
            Utils.showErrorMessage("No XMLs", "Please add hardware XMLs");
        } else {
            DatabaseConnector databaseConnector1 = new DatabaseConnector(dumpR1Path);
            DatabaseConnector databaseConnector2 = new DatabaseConnector(dump2R2Path);

            try {
                // get a list of 2G sites and their parameters
                ArrayList<Cabinet> bcfs = databaseConnector1.get2GBCFs();
                if (bcfs.size() == 0) {
                    Utils.showErrorMessage("Empty dump", "Please check 2G RAN1 dump");
                    return false;
                }

                ArrayList<Cabinet> bcfs2 = databaseConnector2.get2GBCFs();
                if (bcfs2.size() == 0) {
                    Utils.showErrorMessage("Empty dump", "Please check 2G RAN2 dump");
                    return false;
                }
                bcfs.addAll(bcfs2);
                addHWtoCabinets(bcfs, btsHwHashMap);
                DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
                if (!databaseHelper.isWeekInserted(2)) {
                    System.out.println("inserting cabinets in db history" + Utils.getTime());
                    databaseHelper.insertCabinets(bcfs);
                    System.out.println("insertion done.." + Utils.getTime());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private boolean process3GDump() {
        try {
            DatabaseConnector databaseConnector1 = new DatabaseConnector(dumpR1Path);
            DatabaseConnector databaseConnector2 = new DatabaseConnector(dump3R2Path);
            ArrayList<Cabinet> nodeBs = databaseConnector1.getNodeBs();
            if (nodeBs.size() == 0) {
                Utils.showErrorMessage("Empty dump", "Please check 3G RAN1 dump");
                return false;
            }
            ArrayList<Cabinet> nodeBs2 = databaseConnector2.getNodeBs();
            if (nodeBs2.size() == 0) {
                Utils.showErrorMessage("Empty dump", "Please check 3G RAN2 dump");
                return false;
            }
            nodeBs.addAll(nodeBs2);
            addHWtoCabinets(nodeBs, nodeBHWHashMap);
            DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
            if (!databaseHelper.isWeekInserted(3))
                databaseHelper.insertCabinets(nodeBs);
            else System.out.println("Week already inserted");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean process4GDump() {
        try {
            DatabaseConnector databaseConnector1 = new DatabaseConnector(dumpR1Path);
            DatabaseConnector databaseConnector2 = new DatabaseConnector(dump4R2Path);
            ArrayList<Cabinet> eNodeBs = databaseConnector1.getEnodeBs();
            ArrayList<Cabinet> sBtsList = databaseConnector1.getSBTSs();
            if (eNodeBs.size() == 0) {
                Utils.showErrorMessage("Empty dump", "Please check 4G RAN1 dump");
                return false;
            }
            ArrayList<Cabinet> eNodeBs2 = databaseConnector2.getEnodeBs();
            ArrayList<Cabinet> sBts2 = databaseConnector2.getSBTSs();
            if (eNodeBs2.size() == 0) {
                Utils.showErrorMessage("Empty dump", "Please check 4G RAN2 dump");
                return false;
            }
            eNodeBs.addAll(eNodeBs2);
            sBtsList.addAll(sBts2);
            addHWtoCabinets(eNodeBs, lteHwMap);
            addHWtoCabinets(sBtsList, sbtsHwMap);
            DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
            if (!databaseHelper.isWeekInserted(4)) {
                databaseHelper.insertCabinets(eNodeBs);
                System.out.println("Total Number of eNodeBs: " + eNodeBs.size());
            }
            if (!databaseHelper.isWeekInserted(1)) {
                databaseHelper.insertCabinets(sBtsList);
                System.out.println("Total Number of SBTSs: " + sBtsList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void addHWtoCabinets(ArrayList<Cabinet> cabinets, HashMap<String, Hardware> hardwareMap) {
        // adding HW from HW hashmap to the bcf hashmap
        cabinets.forEach(cabinet ->
        {
            String uniqueName = cabinet.getUniqueName();
            Hardware hardware = hardwareMap.get(uniqueName);
            if (hardware != null) cabinet.setHardware(hardware);
            else {
                SerialDatabaseSaver serialDatabaseSaver = new SerialDatabaseSaver(serialDatabasePath);
                Hardware serialsDbHardware = serialDatabaseSaver.getMissinHw(uniqueName,
                        ToolCalendar.getPreviousWeek(weekName));
                if (serialsDbHardware.getHwItems().size() != 0)
                    cabinet.setHardware(serialsDbHardware);
                else {
                    DatabaseHelper databaseHelper = new DatabaseHelper(databasePath);
                    cabinet.setHardware(databaseHelper.getMissingHW(uniqueName, ToolCalendar.getPreviousWeek(weekName)));
                }
            }
        });
        System.out.println(cabinets.size());
    }

    private Hardware parse2GHardwareXML(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
        ArrayList<HwItem> hwItems = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();

        NodeList nList = document.getElementsByTagName("HWData");
        Node hwData = nList.item(0);

        NodeList hwList = hwData.getChildNodes();
        Node ne = hwList.item(3);

        Element neElement = (Element) ne;
        String rncwbtsid = neElement.getAttribute("MOID");

        String[] segments = rncwbtsid.split("/");
        String BscId = segments[0].split("-")[2];
        String bcfId = segments[1].split("-")[2];

        String uniqueName = "2G_" + BscId + "_" + bcfId;

        NodeList eqhoList = ne.getChildNodes();
        ArrayList<String> bcfSerials = new ArrayList<>();

        for (int i = 0; i < eqhoList.getLength(); i++) {

            Node eqho = eqhoList.item(i);
            if (eqho.getNodeType() == Node.ELEMENT_NODE) {
                Element eqhoElement = (Element) eqho;

                NodeList unitList = eqhoElement.getChildNodes();
                for (int j = 0; j < unitList.getLength(); j++) {
                    Node unit = unitList.item(j);
                    if (unit.getNodeType() == Node.ELEMENT_NODE) {
                        Element unitElement = (Element) unit;
                        String unitSerial = unitElement.getAttribute("serialNumber");
                        if (!bcfSerials.contains(unitSerial) && !unitSerial.equals("")) {
                            HwItem hwItem = new HwItem();
                            bcfSerials.add(unitSerial);
                            String userLabel = unitElement.getAttribute("unitTypeActual");
                            if (userLabel.equals("FXX")) {
                                int x = 1;
                            }
                            hwItem.setUserLabel(userLabel);
                            hwItem.setSerialNumber(unitSerial);
                            hwItem.setIdentificationCode(unitElement.getAttribute("identificationCode"));
                            hwItems.add(hwItem);
                        }
                    }
                }
            }
        }
        Hardware hardware = new Hardware(hwItems);
        hardware.setUniqueName(uniqueName);
        hardware.setWeek('W' + weekName);
        return hardware;
    }

    private Hardware parse3GHardwareXML(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
//        NodeBHW nodeBHW = new NodeBHW();
        ArrayList<HwItem> hwItems = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();


        NodeList nList = document.getElementsByTagName("HWData");
        Node hwData = nList.item(0);

        NodeList hwList = hwData.getChildNodes();
        Node ne = hwList.item(1);

        String rncwbtsid = ((Element) ne).getAttribute("MOID");

        String[] segments = rncwbtsid.split("/");
        String RNC = segments[0].split("-")[2];
        String WBTS = segments[1].split("-")[2];

        String uniqueName = "3G_" + RNC + "_" + WBTS;

        NodeList eqhoList = ne.getChildNodes();
        ArrayList<String> nobeBSerials = new ArrayList<>();

        for (int i = 0; i < eqhoList.getLength(); i++) {

            Node eqho = eqhoList.item(i);
            if (eqho.getNodeType() == Node.ELEMENT_NODE) {
                Element eqhoElement = (Element) eqho;
                if (eqhoElement.hasChildNodes()) {
                    NodeList unitList = eqhoElement.getChildNodes();
                    for (int j = 0; j < unitList.getLength(); j++) {
                        Node unit = unitList.item(j);
                        if (unit.getNodeType() == Node.ELEMENT_NODE) {
                            Element unitElement = (Element) unit;
                            String unitSerial = unitElement.getAttribute("serialNumber");
                            if (!nobeBSerials.contains(unitSerial) && !unitSerial.equals("")) {
                                HwItem hwItem = new HwItem();
                                nobeBSerials.add(unitSerial);
                                hwItem.setUserLabel(unitElement.getAttribute("unitTypeActual"));
                                hwItem.setSerialNumber(unitSerial);
                                hwItem.setIdentificationCode(unitElement.getAttribute("identificationCode"));
                                hwItems.add(hwItem);
                            }
                        }
                    }
                }
                String eqhoSerial = eqhoElement.getAttribute("serialNumber");
                if (!nobeBSerials.contains(eqhoSerial) && !eqhoSerial.equals("")) {
                    HwItem hwItem = new HwItem();
                    nobeBSerials.add(eqhoSerial);
                    hwItem.setUserLabel(eqhoElement.getAttribute("userLabel"));
                    hwItem.setSerialNumber(eqhoSerial);
                    hwItem.setIdentificationCode(eqhoElement.getAttribute("identificationCode"));
                    hwItems.add(hwItem);
                }

            }

        }
        Hardware hardware = new Hardware(hwItems);
        hardware.setUniqueName(uniqueName);
        hardware.setWeek('W' + weekName);
        return hardware;
    }

    private Hardware parse4GHardwareXML(File xmlFile) throws ParserConfigurationException, SAXException, IOException,
            NullPointerException {
        ArrayList<HwItem> hwItems = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();


        NodeList nList = document.getElementsByTagName("HWData");
        Node hwData = nList.item(0);

        NodeList hwList = hwData.getChildNodes();
        Node ne = hwList.item(3);

        Element neElement = (Element) ne;
        String moid = neElement.getAttribute("MOID");
        String[] segments = moid.split("/");

        String mrbtsId = segments[1].split("-")[1];

//        eNodeBHW.setMrbtsId(mrbtsId);

        NodeList eqhoList = ne.getChildNodes();
        ArrayList<String> eNobeBSerials = new ArrayList<>();

        for (int i = 0; i < eqhoList.getLength(); i++) {

            Node eqho = eqhoList.item(i);
            if (eqho.getNodeType() == Node.ELEMENT_NODE) {
                Element eqhoElement = (Element) eqho;
                if (eqhoElement.hasChildNodes()) {
                    NodeList unitList = eqhoElement.getChildNodes();
                    for (int j = 0; j < unitList.getLength(); j++) {
                        Node unit = unitList.item(j);
                        if (unit.getNodeType() == Node.ELEMENT_NODE) {
                            Element unitElement = (Element) unit;
                            String unitSerial = unitElement.getAttribute("serialNumber");
                            if (!eNobeBSerials.contains(unitSerial) && !unitSerial.equals("")) {
                                HwItem hwItem = new HwItem();
                                eNobeBSerials.add(unitSerial);
                                hwItem.setUserLabel(unitElement.getAttribute("unitTypeActual"));
                                hwItem.setSerialNumber(unitSerial);
                                hwItem.setIdentificationCode(unitElement.getAttribute("identificationCode"));
//                                eNodeBHW.addHwItem(hwItem);
                                hwItems.add(hwItem);
                            }
                        }
                    }
                }
                String eqhoSerial = eqhoElement.getAttribute("serialNumber");
                if (!eNobeBSerials.contains(eqhoSerial) && !eqhoSerial.equals("")) {
                    HwItem hwItem = new HwItem();
                    eNobeBSerials.add(eqhoSerial);
                    hwItem.setUserLabel(eqhoElement.getAttribute("userLabel"));
                    hwItem.setSerialNumber(eqhoSerial);
                    hwItem.setIdentificationCode(eqhoElement.getAttribute("identificationCode"));
//                    eNodeBHW.addHwItem(hwItem);
                    hwItems.add(hwItem);
                }
            }
        }
        Hardware hardware = new Hardware(hwItems.stream().filter(hwItem -> !hwItem.getUserLabel().equals("CABINET") ||
                !hwItem.getUserLabel().equals("RET") ||
                !hwItem.getUserLabel().equals("SAD") ||
                !hwItem.getUserLabel().equals("LNA") ||
                !hwItem.getUserLabel().equals("Other") ||
                !hwItem.getUserLabel().equals("INET") ||
                !hwItem.getUserLabel().equals("-ODM") ||
                !hwItem.getUserLabel().equals("vice") ||
                !hwItem.getUserLabel().equals("MAD") ||
                !hwItem.getUserLabel().equals("WMHD")).collect(Collectors.toList()));
        hardware.setUniqueName("4G_" + mrbtsId);
        hardware.setWeek('W' + weekName);
        return hardware;
    }

    private Hardware parseSBTSHardwareXML(File xmlFile, String type) throws ParserConfigurationException, SAXException, IOException {
        ArrayList<HwItem> hwItems = new ArrayList<>();
        String sbtsId = "";
        boolean sbtsIdExtracted = false;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();

        NodeList nList = document.getElementsByTagName("raml");
        Node hwData = nList.item(0);

        NodeList hwList = hwData.getChildNodes();

        Node cmdata = hwList.item(1);
        NodeList childNodes = cmdata.getChildNodes();
        int managedObjectsSize = childNodes.getLength();
        int w = 0;

        for (int i = 0; i < managedObjectsSize; i++) {
            Node item = childNodes.item(i);
            if (item.getNodeName().equals("managedObject")) {
                NamedNodeMap attributes = item.getAttributes();
                String className = attributes.getNamedItem("class").getNodeValue();
                if (!sbtsIdExtracted) {
                    String mrbtsLongName = attributes.getNamedItem("distName").getNodeValue();
                    String[] parts = mrbtsLongName.split("/");
                    for (String part : parts) {
                        if (part.contains("MRBTS-"))
                            sbtsId = part.replace("MRBTS-", "");
                    }
                    sbtsIdExtracted = true;
                }
                if ((!className.equalsIgnoreCase("SMOD_CORE") && className.contains("MOD"))
                        || className.contains("INVUNIT")) {
                    HashMap<String, String> kvalue = new HashMap<>();
                    w++;
                    NodeList childNodes1 = item.getChildNodes();
                    int length = childNodes1.getLength();
                    for (int j = 0; j < length; j++) {
                        Node item1 = childNodes1.item(j);
                        if (item1 != null) {
                            NamedNodeMap attributes1 = item1.getAttributes();
                            if (attributes1 != null) {
                                for (int k = 0; k < attributes1.getLength(); k++) {
                                    Node item2 = attributes1.item(k);
                                    String x = item2.getNodeValue();
                                    String y = "";
                                    Node firstChild = item1.getFirstChild();
                                    if (firstChild != null)
                                        y = firstChild.getNodeValue();
                                    if (x != null)
                                        kvalue.put(x, y);
                                }
                            }
                        }
                    }

                    HwItem hwItem = new HwItem();
                    String hwDescription = null;
                    try {
                        hwDescription = kvalue.get("inventoryUnitType");
                        if (!hwDescription.equals("CORE_Flexi System Module Outdoor FSMF")) {
                            try {
                                try {
                                    hwItem.setUserLabel(hwShortNameConversionMap.get(hwDescription));
                                } catch (NullPointerException e) {
                                    hwItem.setUserLabel(hwDescription);
                                }
                                String serialNumber = kvalue.get("serialNumber");
                                if (serialNumber != null)
                                    hwItem.setSerialNumber(serialNumber);
                                else hwItem.setSerialNumber("");
                                hwItem.setIdentificationCode(kvalue.get("vendorUnitTypeNumber"));
                                hwItems.add(hwItem);
                            } catch (Exception e) {

                            }
                        }
                    } catch (Exception e) {

                    }

                }
            }
        }
        Hardware hardware = new Hardware(hwItems.stream().filter(hwItem -> !(hwItem.getUserLabel().equals("CABINET") ||
                hwItem.getUserLabel().equals("RET") ||
                hwItem.getUserLabel().equals("SAD") ||
                hwItem.getUserLabel().equals("LNA") ||
                hwItem.getUserLabel().equals("Other") ||
                hwItem.getUserLabel().equals("INET") ||
                hwItem.getUserLabel().equals("-ODM") ||
                hwItem.getUserLabel().equals("vice") ||
                hwItem.getUserLabel().equals("MAD") ||
                hwItem.getUserLabel().equals("FAN") ||
                hwItem.getUserLabel().equals("WMHD"))).collect(Collectors.toList()));
        hardware.setUniqueName(type + "_" + sbtsId);
        hardware.setWeek('W' + weekName);
        return hardware;
    }


    private void createDumpsSelector(ArrayList<String> tableNames) {
        Button currentBu = new Button("Load current dump");
        Button oldBu = new Button("Load old dump");
        Button compareBu = new Button("Compare Dumps");
        VBox vbox = new VBox(currentBu, oldBu, compareBu);
        Scene scene = new Scene(vbox, 300, 300);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        currentBu.setOnAction(event -> {
            createListView(tableNames, 0);
        });

        oldBu.setOnAction(event -> {
            createListView(tableNames, 1);
//            stage.close();
        });
        compareBu.setOnAction(event -> {
            if (!currentTable.equals(oldTable)) {
                DatabaseHelper databaseHelper = new DatabaseHelper(databasePath);
                ResultSet resultSet = databaseHelper.compareDumps(currentTable, oldTable);
                try {
//                    Exporter.exportChangesSheet(resultSet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                stage.close();
            }
        });


    }


    private void showCalendar() {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 400, 400);
        DatePicker d = new DatePicker(LocalDate.now());
        d.setShowWeekNumbers(true);
        d.setOnAction(event -> {
            LocalDate localDate = d.getValue();

//            WeekFields weekFields = WeekFields.of(Locale.getDefault());
//            weekNumber = date.get(weekFields.weekOfWeekBasedYear());
            weekName = ToolCalendar.getWeekName(localDate);
            System.out.println(weekName);
        });

        DatePickerSkin datePickerSkin = new DatePickerSkin(d);

        javafx.scene.Node popupContent = datePickerSkin.getPopupContent();

        root.setCenter(popupContent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private void createListView(ArrayList<String> tableNames, int oc) {
        ListView listView = new ListView();
        VBox vbox = new VBox(listView);

        Scene scene = new Scene(vbox, 300, 300);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        listView.getItems().addAll(tableNames);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int index = listView.getSelectionModel().getSelectedIndex();
                if (oc == 0)
                    currentTable = tableNames.get(index);
                else
                    oldTable = tableNames.get(index);
                stage.close();
            }
        });
    }


    private void initializeButtonsAndArrays(Scene scene) {
        exportDashboardBu = (Button) scene.lookup("#exportDashboardBu");
        load2R1DumpBu = (Button) scene.lookup("#load2R1DumpBu");
        load2R2DumpBu = (Button) scene.lookup("#load2R2DumpBu");
        load3R2DumpBu = (Button) scene.lookup("#load3R2DumpBu");
        load4R2DumpBu = (Button) scene.lookup("#load4R2DumpBu");
        loadXMls = (Button) scene.lookup("#loadXMLs");
        saveSummary = (Button) scene.lookup("#saveSummary");
        exportChangesBu = (Button) scene.lookup("#exportChangesBu");
        exportDumpsheetsBu = (Button) scene.lookup("#exportDumpsheetsBu");
        loadExcelSheet = (Button) scene.lookup("#loadExcelSheet");
        processBu = (Button) scene.lookup("#processBu");
        pickDateBu = (Button) scene.lookup("#pickDateBu");
        btsHwHashMap = new HashMap<>();
        nodeBHWHashMap = new HashMap<>();
        lteHwMap = new HashMap<>();
        sbtsHwMap = new HashMap<>();
        hwHashMap = new HashMap<>();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
