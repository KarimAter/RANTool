package sample;

import Helpers.DatabaseConnector;
import Helpers.DatabaseHelper;
import Helpers.Utils;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class Main extends Application {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ArrayList<Cabinet> bcfs;
    private ArrayList<NodeB> nodeBList;
    private ArrayList<EnodeB> lSitesList;
    //    ArrayList<USite> uSitesList;
//    ArrayList<GSite> gSitesList;
    private ArrayList<USite> thirdCarrierList;
    private ArrayList<USite> u900List;
    private HashMap<String, Hardware> btsHwHashMap;
    private HashMap<String, Hardware> nodeBHWHashMap;
    private HashMap<String, Hardware> lteHwMap;


    private Button exportDashboardBu, load2R1DumpBu, load2R2DumpBu, load3R1DumpBu, load3R2DumpBu, load4R1DumpBu, load4R2DumpBu,
            saveDatabase, saveSummary, exportChangesBu, exportDumpsheetsBu, load2GXMLs, export2GHardware, load4GXMLs, export4GHardware,
            load3GXMLs, export3GHardware, processBu, pickDateBu;
    private Calendar calendar;
    private ResultSet trxResultSet1, trxResultSet2, uCellsResultSet1, uCellsResultSet2;
    private String currentTable, oldTable;
    private String identifiersPath;
    private String imagePath;
    private String dump2R1Path, dump4R2Path, dump2R2Path, dump3R1Path, dump3R2Path, dump4R1Path;

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
        calendar = Calendar.getInstance();
        weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        weekName = "W" + weekNumber;
        identifiersPath = "D:/RAN Tool/NokiaDumpToolHistory.db";
        imagePath = "D:/RAN Tool/Databases/";


        // load 2G RAN1 Dump from identifiersPath from the machine
        load2R1DumpBu.setOnAction(event -> {
            dump4R1Path = dump3R1Path = dump2R1Path = Utils.loadDumpFromMachine(primaryStage);

        });
        load2R2DumpBu.setOnAction(event -> {
            dump2R2Path = Utils.loadDumpFromMachine(primaryStage);
        });

        load3R1DumpBu.setOnAction(event -> {
            dump3R1Path = Utils.loadDumpFromMachine(primaryStage);

        });
        load3R2DumpBu.setOnAction(event -> {
            dump3R2Path = Utils.loadDumpFromMachine(primaryStage);
        });

        load4R1DumpBu.setOnAction(event -> {
            dump4R1Path = Utils.loadDumpFromMachine(primaryStage);
        });

        load4R2DumpBu.setOnAction(event -> {
            dump4R2Path = Utils.loadDumpFromMachine(primaryStage);
        });

        processBu.setOnAction(event -> {

//            processDump(dump2R1Path, dump2R2Path, (r1, r2) -> {
//                DatabaseConnector databaseConnector1 = new DatabaseConnector(r1);
//                DatabaseConnector databaseConnector2 = new DatabaseConnector(r2);
//                try {
//                    // get a list of 2G sites and their parameters
//                    ArrayList<Cabinet> cabinets = get(databaseConnector1, databaseConnector2);
//                    insert(cabinets);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//        });

//            process2GDump();
            process3GDump();
//            process4GDump();
        });

        saveDatabase.setOnAction(event ->

        {

//            TextField textField = new TextField();
//            Button button = new Button();
//            button.setOnAction(event1 -> {
//                String tableName = textField.getText();
//                DbSaver dbSaver = new DbSaver(imagePath + tableName + ".db");
//                System.out.println("Storing dump data..." + getTime());
//                dbSaver.store(gSitesList, uSitesList, lSitesList);
//                System.out.println("Dump data have been stored.." + getTime());
////                createIdentifierTable(tableName);
//                clearLists();
//            });
//            VBox vBox = new VBox();
//            vBox.getChildren().add(textField);
//            vBox.getChildren().add(button);
//            vBox.setSpacing(10);
//            Scene scene2 = new Scene(vBox, 300, 200);
//            Stage stage2 = new Stage();
//            stage2.setScene(scene2);
//            stage2.show();


        });

        saveSummary.setOnAction(event ->

        {

            TextField textField = new TextField();
            Button button = new Button();
            VBox vBox = new VBox();
            vBox.getChildren().add(textField);
            vBox.getChildren().add(button);
            vBox.setSpacing(10);
            Scene scene2 = new Scene(vBox, 300, 200);
            Stage stage2 = new Stage();
            stage2.setScene(scene2);
            stage2.show();
            button.setOnAction(event1 -> {
//                String tableName = textField.getText();
                String databaseName = Utils.loadDatabaseFromMachine(stage2);
                String tableName = databaseName.replace(".db", "");
//              String [] parts = dbPath.split("/");
//              String tableName=
            });


        });

        exportChangesBu.setOnAction(event ->

        {
            ArrayList<String> tableNames = new ArrayList<>();
            DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath);
            tableNames = databaseHelper.getTableNames();
            createDumpsSelector(tableNames);

        });

        // export dashboard button
        exportDashboardBu.setOnAction(event ->

        {
            try {
                // prepare the excel file that will have the output
//                Exporter exporter = new Exporter();
//               XSSFWorkbook wb= exporter.prepareWorkbooks(weekName);
//                export2G();
                export3G();
//                export4G();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        exportDumpsheetsBu.setOnAction(event ->

        {
            try {
                System.out.println("Exporting TRX Sheet1..." + getTime());
                Exporter exporter = new Exporter();
//                exporter.prepareTrxSheetNames(weekName);
                exporter.exportTRXSheet(dump2R1Path, dump2R2Path, weekName);
                System.out.println("Exporting 3G Cells RAN1..." + getTime());
                exporter.exportUcellsSheet(dump3R1Path, dump3R2Path, weekName);
                System.out.println("Exporting Dump Sheets is done..." + getTime());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        load2GXMLs.setOnAction(event ->

        {
            ArrayList<File> xmlFiles = Utils.loadXMLsFromMachine(primaryStage);
            for (File xmlFile : xmlFiles) {
                try {
                    Hardware hardware = parse2GHardwareXML(xmlFile);
                    btsHwHashMap.put(hardware.getUniqueName(), hardware);

                } catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Number of 2G XML files: " + btsHwHashMap.size());
        });

        export2GHardware.setOnAction(event ->

        {
//            try {
//                DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath, weekName);
//                ArrayList<Cabinet> bcfs = databaseHelper.loadCabinets();
//                addHWtoCabinets(bcfs);
//                Exporter exporter = new Exporter();
//                exporter.export2GHWfromXML(bcfs, weekName);
//            } catch (IOException | SQLException e) {
//                e.printStackTrace();
//            }
        });


        load3GXMLs.setOnAction(event ->

        {
            ArrayList<File> xmlFiles = Utils.loadXMLsFromMachine(primaryStage);
            for (File xmlFile : xmlFiles) {
                try {
                    Hardware hardware = parse3GHardwareXML(xmlFile);

                    nodeBHWHashMap.put(hardware.getUniqueName(), hardware);

                } catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Number of 3G XML files: " + nodeBHWHashMap.size());
        });


        export3GHardware.setOnAction(event ->

        {
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath, weekName);
                ArrayList<NodeB> nodeBs = databaseHelper.loadNodeBs();
                addHWtoNodeBs(nodeBs);
                Exporter exporter = new Exporter();
                exporter.export3GHWfromXML(nodeBs, weekName);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        });

        load4GXMLs.setOnAction(event ->

        {
            ArrayList<File> xmlFiles = Utils.loadXMLsFromMachine(primaryStage);


            for (File xmlFile : xmlFiles) {
                try {
                    Hardware hardware = parse4GHardwareXML(xmlFile);
//                    String key = eNodeBHW.getMrbtsId();
                    lteHwMap.put(hardware.getUniqueName(), hardware);

                } catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
                }

            }
            System.out.println("Number of 4G XML files: " + lteHwMap.size());
        });

        export4GHardware.setOnAction(event ->

        {
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath, weekName);
                ArrayList<EnodeB> eNodeBs = databaseHelper.loadENodeBs();
                addHWto4GSites(eNodeBs);
                Exporter exporter = new Exporter();
                exporter.export4GHWfromXML(eNodeBs, weekName);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        });

        pickDateBu.setOnAction(event ->

        {
            showCalendar();
        });
    }

//    private ArrayList<Cabinet> get(DatabaseConnector databaseConnector1, DatabaseConnector databaseConnector2) {
//        ArrayList<Cabinet> cabinets = databaseConnector1.get2GBCFs(weekName);
//        cabinets.addAll(databaseConnector2.get2GBCFs(weekName));
//        addHWtoCabinets(cabinets);
//        return cabinets;
//    }
//
//    private void insert(ArrayList<Cabinet> cabinets) throws SQLException {
//        DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath, weekName);
//        if (databaseHelper.isWeekInserted(cabinets.get(0).getTechnology()))
//            databaseHelper.insertCabinets(cabinets);
//    }

    private void process2GDump() {
        DatabaseConnector databaseConnector1 = new DatabaseConnector(dump2R1Path);
        DatabaseConnector databaseConnector2 = new DatabaseConnector(dump2R2Path);

//        ArrayList<Cabinet> b =new ArrayList<BCF>();

        try {
            // get a list of 2G sites and their parameters
            ArrayList<Cabinet> bcfs = databaseConnector1.get2GBCFs();
            bcfs.addAll(databaseConnector2.get2GBCFs());
            addHWtoCabinets(bcfs, btsHwHashMap);
            DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath, weekName);
            if (databaseHelper.isWeekInserted(2))
                databaseHelper.insertCabinets(bcfs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    private void processDump(String c, String d, BiConsumer<String, String> x) {
//        x.accept(c, d);
//        ;
//
////        ArrayList<Cabinet> b =new ArrayList<BCF>();
//
//
//    }


    private void process3GDump() {
        try {
            DatabaseConnector databaseConnector1 = new DatabaseConnector(dump3R1Path);
            DatabaseConnector databaseConnector2 = new DatabaseConnector(dump3R2Path);
            ArrayList<Cabinet> nodeBs = databaseConnector1.getNodeBs();
            nodeBs.addAll(databaseConnector2.getNodeBs());
            addHWtoCabinets(nodeBs, nodeBHWHashMap);
            DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath, weekName);
            if (databaseHelper.isWeekInserted(3))
                databaseHelper.insertCabinets(nodeBs);
//            uSitesList = databaseConnector.get3GSites(ran, uSitesList);
//            thirdCarrierList = databaseConnector.getThirdCarrierSites(thirdCarrierList);
//            u900List = databaseConnector.getU900List(u900List);
//            if (ran == 1)
//                uCellsResultSet1 = databaseConnector.getUcellsSheet();
//            else
//                uCellsResultSet2 = databaseConnector.getUcellsSheet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process4GDump() {
        try {
            DatabaseConnector databaseConnector1 = new DatabaseConnector(dump4R1Path);
            DatabaseConnector databaseConnector2 = new DatabaseConnector(dump4R2Path);
            lSitesList = databaseConnector1.get4GSites(weekName);
            lSitesList.addAll(databaseConnector2.get4GSites(weekName));
            addHWto4GSites(lSitesList);
            DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath, weekName);
            if (databaseHelper.isWeekInserted(4))
                databaseHelper.insertEnodeBs(lSitesList);
            System.out.println(lSitesList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addHWtoCabinets(ArrayList<Cabinet> cabinets, HashMap<String, Hardware> hardwareMap) {
// adding HW from HW hashmap to the bcf hashmap
        cabinets.forEach(cabinet ->
        {
            String uniqueName = cabinet.getUniqueName();
            Hardware hardware = hardwareMap.get(uniqueName);
            if (hardware != null)
//                ArrayList<HwItem> hwItems = enodeBHW.getHwItems();
//                if (hwItems.size() != 0)
//                    lSite.setLHardware(enodeBHW, weekName);
                cabinet.setHardware(hardware);
//                else {
//                    DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath);
//                    databaseHelper.getMissing4gHW(lSite, weekNumber - 1);
//                }
            else {
                DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath);
                cabinet.setHardware(databaseHelper.getMissingHW(uniqueName, weekNumber - 1));
            }
//            lSites.set(i, lSite);
        });
        System.out.println(cabinets.size());
    }

    private void addHWtoNodeBs(ArrayList<NodeB> nodeBList) {
        for (int i = 0; i < nodeBList.size(); i++) {
            NodeB nodeB = nodeBList.get(i);
//            String key = nodeB.getRncId() + "_" + nodeB.getWbtsId();
            String uniqueName = nodeB.getUniqueName();
            Hardware hardware = nodeBHWHashMap.get(uniqueName);
            if (hardware != null)
//                ArrayList<HwItem> hwItems = enodeBHW.getHwItems();
//                if (hwItems.size() != 0)
//                    lSite.setLHardware(enodeBHW, weekName);
                nodeB.setHardware(hardware);
//                else {
//                    DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath);
//                    databaseHelper.getMissing4gHW(lSite, weekNumber - 1);
//                }
            else {
                DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath);
                nodeB.setHardware(databaseHelper.getMissingHW(uniqueName, weekNumber - 1));
            }
            nodeBList.set(i, nodeB);
        }
        System.out.println(nodeBList.size());
    }

    private void addHWto4GSites(ArrayList<EnodeB> enodeBS) {
        for (int i = 0; i < enodeBS.size(); i++) {
            EnodeB enodeB = enodeBS.get(i);
            String uniqueName = enodeB.getUniqueName();
            Hardware hardware = lteHwMap.get(uniqueName);
            if (hardware != null)
//                ArrayList<HwItem> hwItems = enodeBHW.getHwItems();
//                if (hwItems.size() != 0)
//                    enodeB.setLHardware(enodeBHW, weekName);
                enodeB.setHardware(hardware);
//                else {
//                    DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath);
//                    databaseHelper.getMissing4gHW(enodeB, weekNumber - 1);
//                }
            else {
                DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath);
                enodeB.setHardware(databaseHelper.getMissingHW(uniqueName, weekNumber - 1));
            }
            enodeBS.set(i, enodeB);
        }
        System.out.println(enodeBS.size());
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
                            hwItem.setUserLabel(unitElement.getAttribute("unitTypeActual"));
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
        hardware.setWeek(weekName);
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
        hardware.setWeek(weekName);
        return hardware;
    }

    private Hardware parse4GHardwareXML(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
//        ENodeBHW eNodeBHW = new ENodeBHW();
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

        Hardware hardware = new Hardware(hwItems);
        hardware.setUniqueName("4G_" + mrbtsId);
        hardware.setWeek(weekName);
        return hardware;
    }


    private void clearLists() {
//        gSitesList = new ArrayList<>();
//        uSitesList = new ArrayList<>();
        lSitesList = new ArrayList<>();
        u900List = new ArrayList<>();
        thirdCarrierList = new ArrayList<>();
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
                DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath);
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
        DatePicker d = new DatePicker();
        d.setShowWeekNumbers(true);
        d.setOnAction(event -> {
            LocalDate date = d.getValue();
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            weekNumber = date.get(weekFields.weekOfWeekBasedYear());
            weekName = "W" + weekNumber;
            System.out.println(weekNumber);
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

    private String getTime() {
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    private void export2G() throws IOException {
//        System.out.println("Exporting 2G..." + getTime());
//        Exporter.export2GSitesList(gSitesList, "2G Sites");
        System.out.println("Exporting BCFs..." + getTime());
        if (bcfs == null) {
            DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath, weekName);
            try {
                ArrayList<Cabinet> bcfs = databaseHelper.loadCabinets(2);
                Exporter exporter = new Exporter(weekName);
                exporter.exportBcfList(bcfs);
                System.out.println("Exporting 2G HW..." + getTime());
//        Exporter.export2GHardWare(gSitesList, "2G HW");
                System.out.println("Exporting BCFs HW..." + getTime());
                exporter.exportBcfHardWare(bcfs);
                System.out.println("2G Dashboard is ready.." + getTime());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            //todo: export direct
        }

    }

    private void export3G() throws IOException {
        System.out.println("Exporting 3G...");
        if (nodeBList == null) {
            DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath, weekName);
            try {
                ArrayList<Cabinet> nodeBList = databaseHelper.loadCabinets(3);
                Exporter exporter = new Exporter(weekName);
                exporter.exportNodeBList(nodeBList);
                exporter.exportNodeBHardWare(nodeBList);
                exporter.exportCarrierList(dump3R1Path, dump3R2Path, "3rd Carrier");
                exporter.exportCarrierList(dump3R1Path, dump3R2Path, "U900");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


//        Exporter.export3GSitesList(uSitesList, "Sites");


//        Exporter.export3GHardWare(uSitesList, "3G HW");
        System.out.println("3G Dashboard is ready.." + getTime());
    }

    private void export4G() throws IOException {
        System.out.println("Exporting 4G..." + getTime());
        if (lSitesList == null) {
            DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath, weekName);
            try {
                ArrayList<EnodeB> lSitesList = databaseHelper.loadENodeBs();
                Exporter exporter = new Exporter();
//                exporter.export4GSitesList(lSitesList);
                exporter.export4GHardWare(lSitesList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("4G Dashboard is ready.." + getTime());
    }


    private void initializeButtonsAndArrays(Scene scene) {
        exportDashboardBu = (Button) scene.lookup("#exportDashboardBu");
        load2R1DumpBu = (Button) scene.lookup("#load2R1DumpBu");
        load2R2DumpBu = (Button) scene.lookup("#load2R2DumpBu");
        load3R1DumpBu = (Button) scene.lookup("#load3R1DumpBu");
        load3R2DumpBu = (Button) scene.lookup("#load3R2DumpBu");
        load4R1DumpBu = (Button) scene.lookup("#load4R1DumpBu");
        load4R2DumpBu = (Button) scene.lookup("#load4R2DumpBu");
        saveDatabase = (Button) scene.lookup("#saveDatabase");
        saveSummary = (Button) scene.lookup("#saveSummary");
        exportChangesBu = (Button) scene.lookup("#exportChangesBu");
        exportDumpsheetsBu = (Button) scene.lookup("#exportDumpsheetsBu");
        load2GXMLs = (Button) scene.lookup("#load2GXMLs");
        load3GXMLs = (Button) scene.lookup("#load3GXMLs");
        load4GXMLs = (Button) scene.lookup("#load4GXMLs");
        export2GHardware = (Button) scene.lookup("#export2GHardware");
        export3GHardware = (Button) scene.lookup("#export3GHardware");
        export4GHardware = (Button) scene.lookup("#export4GHardware");
        processBu = (Button) scene.lookup("#processBu");
        pickDateBu = (Button) scene.lookup("#pickDateBu");
        thirdCarrierList = new ArrayList<>();
        u900List = new ArrayList<>();
        btsHwHashMap = new HashMap<String, Hardware>();
        nodeBHWHashMap = new HashMap<>();
        lteHwMap = new HashMap<>();
//        bcfs = new HashMap<>();

    }


    public static void main(String[] args) {
        launch(args);
    }

}
