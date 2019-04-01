package sample;

import Helpers.DatabaseConnector;
import Helpers.DatabaseHelper;
import Helpers.DbSaver;
import Helpers.Utils;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class Main extends Application {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ArrayList<NodeB> nodeBList;
    ArrayList<LSite> lSitesList;
    ArrayList<USite> uSitesList;
    ArrayList<GSite> gSitesList;
    ArrayList<USite> thirdCarrierList;
    ArrayList<USite> u900List;
    ArrayList<NodeBHW> nodeBHWList;
    HashMap<String, BtsHW> btsHwHashMap;
    HashMap<String, NodeBHW> nodeBHWHashMap;
    HashMap<String, ENodeBHW> eNodeBHWHashMap;
    HashMap<String, ArrayList<BCF>> gSiteBCFs;

    private Button exportDashboardBu, load2R1DumpBu, load2R2DumpBu, load3R1DumpBu, load3R2DumpBu, load4R1DumpBu, load4R2DumpBu,
            saveDatabase, saveSummary, exportChangesBu, exportDumpsheetsBu, load2GXMLs, export2GHardware, load4GXMLs, export4GHardware,
            load3GXMLs, export3GHardware;
    Calendar calendar;
    ResultSet trxResultSet1, trxResultSet2, uCellsResultSet1, uCellsResultSet2;
    static String excelFileName = "C:\\Users\\Ater\\Desktop\\Dashboard.xlsx";
    static String TRXFileName = "C:\\Users\\Ater\\Desktop\\Dashboard_TRX.xlsx";
    String currentTable, oldTable;
    String identifiersPath;
    String imagePath;
    private String dump2R1Path, dump4R2Path, dump2R2Path, dump3R1Path, dump3R2Path, dump4R1Path;
    private HashMap<String, BCF> bcfs;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("RAN Tool");
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        initializeButtonsAndArrays(scene);
        calendar = Calendar.getInstance();

        identifiersPath = "D:/RAN Tool/NokiaDumpToolHistory.db";
        imagePath = "D:/RAN Tool/Databases/";


        // load 2G RAN1 Dump from identifiersPath from the machine
        load2R1DumpBu.setOnAction(event -> {
            dump2R1Path = Utils.loadDumpFromMachine(primaryStage);
            if (dump2R1Path != null) {
                DatabaseConnector databaseConnector = new DatabaseConnector(dump2R1Path);
                process2GDump(databaseConnector, 1);
                System.out.println("Loading Complete..");
            }
        });
        load2R2DumpBu.setOnAction(event -> {
            dump2R2Path = Utils.loadDumpFromMachine(primaryStage);
            if (dump2R2Path != null) {
                DatabaseConnector databaseConnector = new DatabaseConnector(dump2R2Path);
                process2GDump(databaseConnector, 2);
            }
        });

        load3R1DumpBu.setOnAction(event -> {
            dump3R1Path = Utils.loadDumpFromMachine(primaryStage);
            if (dump3R1Path != null) {
                DatabaseConnector databaseConnector = new DatabaseConnector(dump3R1Path);
                process3GDump(databaseConnector, 1);
            }
        });
        load3R2DumpBu.setOnAction(event -> {
            dump3R2Path = Utils.loadDumpFromMachine(primaryStage);
            if (dump3R2Path != null) {
                DatabaseConnector databaseConnector = new DatabaseConnector(dump3R2Path);
                process3GDump(databaseConnector, 2);
            }
        });

        load4R1DumpBu.setOnAction(event -> {
            dump4R1Path = Utils.loadDumpFromMachine(primaryStage);
            if (dump4R1Path != null) {
                DatabaseConnector databaseConnector = new DatabaseConnector(dump4R1Path);
                process4GDump(databaseConnector, 1);
            }
        });

        load4R2DumpBu.setOnAction(event -> {
            dump4R2Path = Utils.loadDumpFromMachine(primaryStage);
            if (dump4R2Path != null) {
                DatabaseConnector databaseConnector = new DatabaseConnector(dump4R2Path);
                process4GDump(databaseConnector, 2);
            }
        });

        saveDatabase.setOnAction(event -> {

            TextField textField = new TextField();
            Button button = new Button();
            button.setOnAction(event1 -> {
                String tableName = textField.getText();
                DbSaver dbSaver = new DbSaver(imagePath + tableName + ".db");
                System.out.println("Storing dump data..." + getTime());
                dbSaver.store(gSitesList, uSitesList, lSitesList);
                System.out.println("Dump data have been stored.." + getTime());
//                createIdentifierTable(tableName);
                clearLists();
            });
            VBox vBox = new VBox();
            vBox.getChildren().add(textField);
            vBox.getChildren().add(button);
            vBox.setSpacing(10);
            Scene scene2 = new Scene(vBox, 300, 200);
            Stage stage2 = new Stage();
            stage2.setScene(scene2);
            stage2.show();


        });

        saveSummary.setOnAction(event -> {

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
                createIdentifierTable(tableName);
            });


        });

        exportChangesBu.setOnAction(event -> {
            ArrayList<String> tableNames = new ArrayList<>();
            DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath);
            tableNames = databaseHelper.getTableNames();
            createDumpsSelector(tableNames);

        });

        // export dashboard button
        exportDashboardBu.setOnAction(event -> {
            try {
                // prepare the excel file that will have the output
                Exporter.getWorkbook();
                export2G();
                export3G();
                export4G();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        exportDumpsheetsBu.setOnAction(event -> {
            try {
                System.out.println("Exporting TRX Sheet1..." + getTime());
                Exporter.exportTRXSheet(trxResultSet1, 1);
                System.out.println("Exporting TRX Sheet2..." + getTime());
                Exporter.exportTRXSheet(trxResultSet2, 2);
                System.out.println("Exporting 3G Cells RAN1..." + getTime());
                Exporter.exportUcellsSheet(uCellsResultSet1, 1);
                System.out.println("Exporting 3G Cells RAN2..." + getTime());
                Exporter.exportUcellsSheet(uCellsResultSet2, 2);
                System.out.println("Exporting Dump Sheets is done..." + getTime());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        load2GXMLs.setOnAction(event -> {
            ArrayList<File> xmlFiles = Utils.loadXMLsFromMachine(primaryStage);
            for (File xmlFile : xmlFiles) {
                try {
                    BtsHW btsHW = parse2GHardwareXML(xmlFile);
//                    nodeBHWList.add(nodeBHW);
                    String key = btsHW.getBscId() + "_" + btsHW.getBcfId();
                    btsHwHashMap.put(key, btsHW);

                } catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
                }
            }
            get2GBCFs();
            addHWto2GBCFs();
            addHWto2GSites();


            System.out.println("Number of hw from xml files  " + btsHwHashMap.size());
            System.out.println("Number of active bcfs  " + bcfs.size());
        });

        export2GHardware.setOnAction(event -> {
            try {
                Exporter.export2GHWfromXML(bcfs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        load3GXMLs.setOnAction(event -> {
            ArrayList<File> xmlFiles = Utils.loadXMLsFromMachine(primaryStage);


            for (File xmlFile : xmlFiles) {
                try {
                    NodeBHW nodeBHW = parse3GHardwareXML(xmlFile);
//                    nodeBHWList.add(nodeBHW);
                    String key = nodeBHW.getRncID() + "_" + nodeBHW.getWBTSId();
                    nodeBHWHashMap.put(key, nodeBHW);

                } catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
                }

            }
            System.out.println(nodeBHWHashMap.size());

            addHWto3GSites();
        });


        export3GHardware.setOnAction(event -> {
            try {
                Exporter.export3GHWfromXML(nodeBList, nodeBHWHashMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        load4GXMLs.setOnAction(event -> {
            ArrayList<File> xmlFiles = Utils.loadXMLsFromMachine(primaryStage);


            for (File xmlFile : xmlFiles) {
                try {
                    ENodeBHW eNodeBHW = parse4GHardwareXML(xmlFile);
                    String key = eNodeBHW.getMrbtsId();
                    eNodeBHWHashMap.put(key, eNodeBHW);

                } catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
                }

            }
            System.out.println(eNodeBHWHashMap.size());
            addHWto4GSites();
        });

        export4GHardware.setOnAction(event -> {
            try {
                Exporter.export4GHWfromXML(lSitesList, eNodeBHWHashMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void addHWto4GSites() {
        for (int i = 0; i < lSitesList.size(); i++) {
            LSite lSite = lSitesList.get(i);
            ENodeBHW enodeBHW = eNodeBHWHashMap.get(lSite.getENodeBId());
            lSite.setLHardware(enodeBHW);
            lSitesList.set(i, lSite);
        }
        System.out.println(lSitesList.size());
    }

    private void addHWto3GSites() {
        for (int i = 0; i < nodeBList.size(); i++) {
            NodeB nodeB = nodeBList.get(i);
            NodeBHW nodeBHW = nodeBHWHashMap.get(nodeB.getNodeBRncId() + "_" + nodeB.getNodeBWbtsId());
            nodeB.setUHardware(nodeBHW);
            nodeBList.set(i, nodeB);
        }
        System.out.println(nodeBList.size());
    }

    private void addHWto2GSites() {

        // creating gSiteBCF hashmap with key of siteName, and array list of BCFs
        gSiteBCFs = new HashMap<>();
        bcfs.forEach((key, value) ->
        {
            if (!gSiteBCFs.containsKey(value.getBcfName())) {
                ArrayList<BCF> bcfArrayList = new ArrayList<>();
                gSiteBCFs.put(value.getBcfName(), bcfArrayList);
            }
            ArrayList<BCF> b = gSiteBCFs.get(value.getBcfName());
            b.add(value);
            gSiteBCFs.put(value.getBcfName(), b);
        });
        System.out.println(gSitesList.size());
        // adding the HW to main 2G site list
        for (int i = 0; i < gSitesList.size(); i++) {
            GSite gSite = gSitesList.get(i);
            gSite.setGHardware(gSiteBCFs.get(gSite.getSiteName()));
            gSitesList.set(i, gSite);
        }
        System.out.println(gSitesList.size());
    }

    private void addHWto2GBCFs() {
// adding HW from HW hashmap to the bcf hashmap
        bcfs.forEach((key, value) ->
        {
            BtsHW btsHW = btsHwHashMap.get(key);
            if (btsHW != null)
                value.setHwItems(btsHW.getHwItems());
            btsHwHashMap.remove(key);
        });
    }

    private void get2GBCFs() {
        // getting bcf Hashmap with key of BSCId,BCFId
        DatabaseConnector databaseConnector2 = new DatabaseConnector(dump2R2Path);
        bcfs = databaseConnector2.get2GBCFs(new HashMap<>());
        DatabaseConnector databaseConnector1 = new DatabaseConnector(dump2R1Path);
        bcfs = databaseConnector1.get2GBCFs(bcfs);
    }

    private ENodeBHW parse4GHardwareXML(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
        ENodeBHW eNodeBHW = new ENodeBHW();
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

        eNodeBHW.setMrbtsId(mrbtsId);

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
                                eNodeBHW.addHwItem(hwItem);
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
                    eNodeBHW.addHwItem(hwItem);
                }
            }
        }
        return eNodeBHW;
    }

    private BtsHW parse2GHardwareXML(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
        BtsHW btsHW = new BtsHW();


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

        btsHW.setBscId(BscId);
        btsHW.setBcfId(bcfId);

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
                            btsHW.addHwItem(hwItem);
                        }
                    }
                }
            }
        }
        return btsHW;
    }

    private static NodeBHW parse3GHardwareXML(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
        NodeBHW nodeBHW = new NodeBHW();

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

        nodeBHW.setRncID(RNC);
        nodeBHW.setWBTSId(WBTS);

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
                                nodeBHW.addHwItem(hwItem);
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
                    nodeBHW.addHwItem(hwItem);
                }

            }

        }
        return nodeBHW;
    }


    private void createIdentifierTable(String tableName) {
        DatabaseHelper databaseHelper = new DatabaseHelper(identifiersPath, tableName);
        System.out.println("Storing identifiers Table..." + getTime());
        if (gSitesList.size() != 0 && uSitesList.size() != 0 && lSitesList.size() != 0)
            databaseHelper.insertIdentifiers(gSitesList, uSitesList, lSitesList);
        else {
            DbSaver dbSaver = new DbSaver(imagePath + tableName + ".db");
            ResultSet gResultSet = dbSaver.loadGIdentifiers();
            ResultSet uResultSet = dbSaver.loadUIdentifiers();
            ResultSet lResultSet = dbSaver.loadLIdentifiers();
            databaseHelper.insertIdentifiers(gResultSet, uResultSet, lResultSet);
        }
        System.out.println("Identifiers table is done..." + getTime());
    }

    private void clearLists() {
        gSitesList = new ArrayList<>();
        uSitesList = new ArrayList<>();
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
                    Exporter.exportChangesSheet(resultSet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                stage.close();
            }
        });


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
        System.out.println("Exporting 2G..." + getTime());
        Exporter.export2GSitesList(gSitesList, "2G Sites");
        System.out.println("Exporting 2G HW..." + getTime());
        Exporter.export2GHardWare(gSitesList, "2G HW");
        System.out.println("2G Dashboard is ready.." + getTime());
    }

    private void export3G() throws IOException {
        System.out.println("Exporting 3G...");
        Exporter.exportNodeBList(nodeBList, "NodeBs");
        Exporter.exportNodeBHardWare(nodeBList, "NodeB HW");
        Exporter.export3GSitesList(uSitesList, "Sites");
        Exporter.exportCarrierList(thirdCarrierList, "3rd Carrier");
        Exporter.exportCarrierList(u900List, "U900");
//        Exporter.export3GHardWare(uSitesList, "3G HW");
        System.out.println("3G Dashboard is ready.." + getTime());
    }

    private void export4G() throws IOException {
        System.out.println("Exporting 4G..." + getTime());
        Exporter.export4GSitesList(lSitesList, "LTE");
        Exporter.export4GHardWare(lSitesList, "4G HW");
        System.out.println("4G Dashboard is ready.." + getTime());
    }

    private void process2GDump(DatabaseConnector databaseConnector, int ran) {
        try {
            // get a list of 2G sites and their parameters
            gSitesList = databaseConnector.get2GSites(gSitesList);
            if (ran == 1)
                trxResultSet1 = databaseConnector.getTRXSheet();
            else
                trxResultSet2 = databaseConnector.getTRXSheet();
            System.out.println(gSitesList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process3GDump(DatabaseConnector databaseConnector, int ran) {
        try {
            nodeBList = databaseConnector.getNodeBs(ran, nodeBList);
            uSitesList = databaseConnector.get3GSites(ran, uSitesList);
            thirdCarrierList = databaseConnector.getThirdCarrierSites(thirdCarrierList);
            u900List = databaseConnector.getU900List(u900List);
            if (ran == 1)
                uCellsResultSet1 = databaseConnector.getUcellsSheet();
            else
                uCellsResultSet2 = databaseConnector.getUcellsSheet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process4GDump(DatabaseConnector databaseConnector, int ran) {
        try {
            lSitesList = databaseConnector.get4GSites(ran, lSitesList);
            System.out.println(lSitesList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        nodeBList = new ArrayList<>();
        gSitesList = new ArrayList<>();
        uSitesList = new ArrayList<>();
        thirdCarrierList = new ArrayList<>();
        u900List = new ArrayList<>();
        lSitesList = new ArrayList<>();
        nodeBHWList = new ArrayList<>();
        btsHwHashMap = new HashMap<>();
        nodeBHWHashMap = new HashMap<>();
        eNodeBHWHashMap = new HashMap<>();

    }

    private void chart() {
        int deface = 1;
        int rowNum = 7;
        try {
            Workbook workbook = new XSSFWorkbook(OPCPackage.open(new FileInputStream("D:\\RAN Tool\\ChartSample.xlsx")));
            CreationHelper creationHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.getSheetAt(0);
            String sheetName = sheet.getSheetName();

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("d/m/yyyy"));
            sheet.getRow(1).getCell(0).setCellValue("");
            sheet.getRow(1).getCell(1).setCellValue("");
            sheet.createRow(0).createCell(2).setCellValue("Date");
            sheet.getRow(0).createCell(3).setCellValue("Sales");

            Cell dateCell = null;
            Cell salesCell = null;


            for (int i = 1; i <= 13; i++) {
                Row r = sheet.getRow(i);
                if (r == null)
                    r = sheet.createRow(i);
                dateCell = r.getCell(2);
                salesCell = r.getCell(3);
                switch (i) {
                    case 1:
                        if (dateCell == null) {
                            dateCell = r.createCell(2);
                            dateCell.setCellValue("1/1/2012");
                            dateCell.setCellStyle(cellStyle);
                        } else {
                            dateCell.setCellValue("1/1/2012");
                            dateCell.setCellStyle(cellStyle);
                        }

                        if (salesCell == null)
                            r.createCell(3).setCellValue(2000);
                        else salesCell.setCellValue(2000);
                        break;
                    case 2:
                        if (dateCell == null) {
                            dateCell = r.createCell(2);
                            dateCell.setCellValue("1/2/2012");
                            dateCell.setCellStyle(cellStyle);
                        } else {
                            dateCell.setCellValue("1/2/2012");
                            dateCell.setCellStyle(cellStyle);
                        }

                        if (salesCell == null)
                            r.createCell(3).setCellValue(1000);
                        else salesCell.setCellValue(1000);
                        break;
                    case 3:
                        if (dateCell == null) {
                            dateCell = r.createCell(2);
                            dateCell.setCellValue("1/3/2012");
                            dateCell.setCellStyle(cellStyle);
                        } else {
                            dateCell.setCellValue("1/3/2012");
                            dateCell.setCellStyle(cellStyle);
                        }

                        if (salesCell == null)
                            r.createCell(3).setCellValue(4000);
                        else salesCell.setCellValue(4000);
                        break;
                    case 4:
                        if (dateCell == null) {
                            dateCell = r.createCell(2);
                            dateCell.setCellValue("1/4/2012");
                            dateCell.setCellStyle(cellStyle);
                        } else {
                            dateCell.setCellValue("1/4/2012");
                            dateCell.setCellStyle(cellStyle);
                        }

                        if (salesCell == null)
                            r.createCell(3).setCellValue(2500);
                        else salesCell.setCellValue(2500);
                        break;
                    case 5:
                        if (dateCell == null) {
                            dateCell = r.createCell(2);
                            dateCell.setCellValue("1/5/2012");
                            dateCell.setCellStyle(cellStyle);
                        } else {
                            dateCell.setCellValue("1/5/2012");
                            dateCell.setCellStyle(cellStyle);
                        }

                        if (salesCell == null)
                            r.createCell(3).setCellValue(2000);
                        else salesCell.setCellValue(2000);
                        break;
                    case 6:
                        if (dateCell == null) {
                            dateCell = r.createCell(2);
                            dateCell.setCellValue("1/6/2012");
                            dateCell.setCellStyle(cellStyle);
                        } else {
                            dateCell.setCellValue("1/6/2012");
                            dateCell.setCellStyle(cellStyle);
                        }

                        if (salesCell == null)
                            r.createCell(3).setCellValue(2000);
                        else salesCell.setCellValue(2000);
                        break;
                    case 7:
                        if (dateCell == null) {
                            dateCell = r.createCell(2);
                            dateCell.setCellValue("1/7/2012");
                            dateCell.setCellStyle(cellStyle);
                        } else {
                            dateCell.setCellValue("1/7/2012");
                            dateCell.setCellStyle(cellStyle);
                        }

                        if (salesCell == null)
                            r.createCell(3).setCellValue(2000);
                        else salesCell.setCellValue(2000);
                        break;
                    default:
                        break;
                }
            }

            Name rangeCell = workbook.getName("Date");
            String reference = sheetName + "!$C$" + (deface + 1) + ":$C$" + (rowNum + deface);
            rangeCell.setRefersToFormula(reference);
            rangeCell = workbook.getName("Sales");
            reference = sheetName + "!$D$" + (deface + 1) + ":$D$" + (rowNum + deface);
            rangeCell.setRefersToFormula(reference);
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\RAN Tool\\CharOut.xlsx");
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}
