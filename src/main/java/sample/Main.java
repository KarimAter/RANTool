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
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main extends Application {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ArrayList<EnodeB> lSitesList;
    private ArrayList<USite> thirdCarrierList;
    private ArrayList<USite> u900List;
    private HashMap<String, Hardware> hwHashMap;
    private HashMap<String, Hardware> btsHwHashMap;
    private HashMap<String, Hardware> nodeBHWHashMap;
    private HashMap<String, Hardware> lteHwMap;


    private Button exportDashboardBu, load2R1DumpBu, load2R2DumpBu, load3R2DumpBu, load4R2DumpBu,
            loadXMls, saveSummary, exportChangesBu, exportDumpsheetsBu, load2GXMLs, export2GHardware, load4GXMLs, export4GHardware,
            load3GXMLs, export3GHardware, processBu, pickDateBu;
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
        Calendar calendar = Calendar.getInstance();
//        weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
//        weekName = "W" + weekNumber;
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


//                int x = 1;
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
                exporter.exportSitesPivot(SiteMapper.getUSitesMap(uCabinets), 6, "3G Sites");
                // Exporting 4G configuration..
                exporter.exportConfPivot(SiteMapper.getLConfigMap(lCabinets), 8, "LTE");
                //Exporting U900 list
                exporter.exportCarr(SiteMapper.getCarriersMap(uCabinets, cabinet -> ((NodeB) cabinet).isU900()), "U900 Sites");
                //Exporting 3rd Carrier list
                exporter.exportCarr(SiteMapper.getCarriersMap(uCabinets, cabinet -> ((NodeB) cabinet).isThirdCarrier()), "3rd Carrier Sites");

                SerialDatabaseSaver serialDatabaseSaver = new SerialDatabaseSaver(serialDatabasePath);

                exporter.exportHardwareCount(serialDatabaseSaver.getHardwareCount(), "Hardware Count");
            } catch (SQLException e) {
                e.printStackTrace();
            }


            String techs = "2__3__4";
            String hardwares = "0.1.0.0.0.0.0.0.0.0.0.0.0.0.0__0.1.0.0.0.0.0.0.0.0.0.2.0.0.0__0.0.0.0.0.1.0.0.0.0.0.0.0.0.0";
            Pattern pattern = Pattern.compile("__");
            int[] ints = pattern.splitAsStream(techs).mapToInt(Integer::parseInt).toArray();


//            String[] strings = pattern.splitAsStream(hardwares).toArray(String[]::new);
            List<String> strings = pattern.splitAsStream(hardwares).collect(Collectors.toList());

            List<String> gCollects = IntStream.range(0, ints.length)
                    .filter(i -> i == 0)
                    .mapToObj(strings::get)
                    .collect(Collectors.toList());

            int xqq = 1;

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
                        hwHashMap.putAll(nodeBHWHashMap);
                    } else if (fileName.contains("MRBTS")) {
                        Hardware hardware = parse4GHardwareXML(xmlFile);
                        lteHwMap.put(hardware.getUniqueName(), hardware);
                        hwHashMap.putAll(lteHwMap);
                    } else {
                        Hardware hardware = parse2GHardwareXML(xmlFile);
                        btsHwHashMap.put(hardware.getUniqueName(), hardware);
                        hwHashMap.putAll(btsHwHashMap);
                    }

                } catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Utils.getTime());
            System.out.println("Number of 2G XML files: " + btsHwHashMap.size());
            System.out.println("Number of 3G XML files: " + nodeBHWHashMap.size());
            System.out.println("Number of 4G XML files: " + lteHwMap.size());
            System.out.println("Total number of XML files: " + hwHashMap.size());
            System.out.println(Utils.getTime());
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
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
                ArrayList<Cabinet> bcfs = databaseHelper.loadCabinets(2);
                addHWtoCabinets(bcfs, btsHwHashMap);
                Exporter exporter = new Exporter();
                exporter.export2GHWfromXML(bcfs, weekName);

                Map<String, List<Hardware>> collect = bcfs.parallelStream().
                        filter(cabinet -> cabinet.getOnAir() == 1).
                        map(Cabinet::getHardware).
                        collect(Collectors.groupingBy(Hardware::getCode));


                Map<String, Map<String, Long>> collect1 = bcfs.parallelStream().
                        filter(cabinet -> cabinet.getOnAir() == 1).
                        map(Cabinet::getHardware).

                        collect(Collectors.toMap(Hardware::getCode, Hardware::getModules));

                Map<String, Map<String, Long>> collect2 = collect1.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, stringMapEntry ->

                        stringMapEntry.getValue().entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)))));

                ;
//                collect1.entrySet()
//                        .stream().collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,l -> l.getValue()
//                        .stream().sum()))
//
//                collect1.entrySet().stream().
//                        collect(Collectors.groupingBy(entry->entry.getValue().entrySet().stream().collect(Collectors.groupingBy(entry.getKey()))));

//                .entrySet().stream()
//                        .collect(HashMap::new,
//                                (map, e) -> map.put(e.getKey(), e.getValue().
//                                        stream().
//                                        map(Hardware::getModules).
//                                        collect(Collectors.toList())), Map::putAll);

//
//                        .map(stringListEntry -> {
//                            stringListEntry.getValue().stream().collect(Collectors.toList());
//
//                        })

//
//                Map<String, Integer> output = input.entrySet().stream().collect(
//                        HashMap::new,
//                        (map,e)->{ int i=Integer.parseInt(e.getValue()); if(i%2==0) map.put(e.getKey(), i); },
//                        Map::putAll);
//
//                collect.entrySet().parallelStream()
//                        .collect(Collectors.groupingBy(Hardware::))


//                Map<String, Hardware> collect1 = bcfs.parallelStream().collect(Collectors.toMap(Cabinet::getCode, Cabinet::getHardware));
//                collect1.entrySet().parallelStream()
//                        .map(stringListEntry ->)


            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
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
//            try {
            DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
//                ArrayList<Cabinet> nodeBs = databaseHelper.loadCabinets(3);
//                addHWtoNodeBs(nodeBs);
//                Exporter exporter = new Exporter();
//                exporter.export3GHWfromXML(nodeBs, weekName);
//            } catch (IOException | SQLException e) {
//                e.printStackTrace();
//            }
        });

        load4GXMLs.setOnAction(event ->

        {
            ArrayList<File> xmlFiles = Utils.loadXMLsFromMachine(primaryStage);


            for (File xmlFile : xmlFiles) {
                try {
                    Hardware hardware = parse4GHardwareXML(xmlFile);
                    lteHwMap.put(hardware.getUniqueName(), hardware);

                } catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
                }

            }
            System.out.println("Number of 4G XML files: " + lteHwMap.size());
        });

        export4GHardware.setOnAction(event ->

        {
//            try {
//                DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
//                ArrayList<Cabinet> eNodeBs = databaseHelper.loadCabinets(4);
//                addHWtoCabinets(eNodeBs,lteHwMap);
//                Exporter exporter = new Exporter();
//                exporter.export4GHWfromXML(eNodeBs, weekName);
//            } catch (IOException | SQLException e) {
//                e.printStackTrace();
//            }
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

        List<Hardware> gSites = gHardware.entrySet().stream().map(getHwItemsMapper(2)).collect(Collectors.toList());
        List<Hardware> uSites = uHardware.entrySet().stream().map(getHwItemsMapper(3)).collect(Collectors.toList());
        List<Hardware> lSites = lHardware.entrySet().stream().map(getHwItemsMapper(4)).collect(Collectors.toList());

        List<Hardware> hardwareList = new ArrayList<>();
        hardwareList.addAll(gSites);
        hardwareList.addAll(uSites);
        hardwareList.addAll(lSites);

        return hardwareList.stream().sorted(Comparator.comparing(Hardware::getTech))
                .collect(Collectors.groupingBy(Hardware::getCode));
    }


    private Function<Map.Entry<String, List<Cabinet>>, Hardware> getHwItemsMapper(int tech) {
        return cabins -> {
            ArrayList<HwItem> hwItems = new ArrayList<>();
            cabins.getValue().forEach(cabinet -> {
                hwItems.addAll(cabinet.getHardware().getHwItems());
            });
            Hardware hardware = new Hardware(hwItems);
            hardware.setCode(cabins.getKey());
            hardware.setName(cabins.getValue().get(0).getName());
            hardware.setTech(tech);
            return hardware;
        };
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
//            System.out.println("Exporting 3rd Carrier..." + Utils.getTime());
//            exporter.exportCarrierList(dumpR1Path, dump3R2Path, "3rd Carrier");
//            System.out.println("Exporting U900..." + Utils.getTime());
//            exporter.exportCarrierList(dumpR1Path, dump3R2Path, "U900");
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


//    private ArrayList<Cabinet> get(DatabaseConnector databaseConnector1, DatabaseConnector databaseConnector2) {
//        ArrayList<Cabinet> cabinets = databaseConnector1.get2GBCFs(weekName);
//        cabinets.addAll(databaseConnector2.get2GBCFs(weekName));
//        addHWtoCabinets(cabinets);
//        return cabinets;
//    }
//
//    private void insertAndRemove(ArrayList<Cabinet> cabinets) throws SQLException {
//        DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
//        if (databaseHelper.isWeekInserted(cabinets.get(0).getTechnology()))
//            databaseHelper.insertAndRemove(cabinets);
//    }

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
            if (eNodeBs.size() == 0) {
                Utils.showErrorMessage("Empty dump", "Please check 4G RAN1 dump");
                return false;
            }
            ArrayList<Cabinet> eNodeBs2 = databaseConnector2.getEnodeBs();
            if (eNodeBs2.size() == 0) {
                Utils.showErrorMessage("Empty dump", "Please check 4G RAN2 dump");
                return false;
            }
            eNodeBs.addAll(eNodeBs2);
            addHWtoCabinets(eNodeBs, lteHwMap);
            DatabaseHelper databaseHelper = new DatabaseHelper(databasePath, weekName);
            if (!databaseHelper.isWeekInserted(4))
                databaseHelper.insertCabinets(eNodeBs);
            System.out.println(eNodeBs.size());
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
            if (hardware != null) {
                cabinet.setHardware(hardware);
            } else {
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
//                    DatabaseHelper databaseHelper = new DatabaseHelper(databasePath);
//                    databaseHelper.isDatabaseEmpty(lSite, weekNumber - 1);
//                }
            else {
                DatabaseHelper databaseHelper = new DatabaseHelper(databasePath);
                nodeB.setHardware(databaseHelper.getMissingHW(uniqueName, ToolCalendar.getPreviousWeek(weekName)));
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
//                    DatabaseHelper databaseHelper = new DatabaseHelper(databasePath);
//                    databaseHelper.isDatabaseEmpty(enodeB, weekNumber - 1);
//                }
            else {
                DatabaseHelper databaseHelper = new DatabaseHelper(databasePath);
                enodeB.setHardware(databaseHelper.getMissingHW(uniqueName, ToolCalendar.getPreviousWeek(weekName)));
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
        hardware.setWeek('W' + weekName);
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
//        load3R1DumpBu = (Button) scene.lookup("#load3R1DumpBu");
        load3R2DumpBu = (Button) scene.lookup("#load3R2DumpBu");
//        load4R1DumpBu = (Button) scene.lookup("#load4R1DumpBu");
        load4R2DumpBu = (Button) scene.lookup("#load4R2DumpBu");
        loadXMls = (Button) scene.lookup("#loadXMLs");
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
        btsHwHashMap = new HashMap<>();
        nodeBHWHashMap = new HashMap<>();
        lteHwMap = new HashMap<>();
        hwHashMap = new HashMap<>();
//        bcfs = new HashMap<>();

    }


    public static void main(String[] args) {
        launch(args);
    }

}

//            Pattern pattern2 = Pattern.compile("\\.");
//            strings.forEach(x -> {
//                List<Integer> collect = pattern2.splitAsStream(x).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
//            });


//            TextField textField = new TextField();
//            Button button = new Button();
//            VBox vBox = new VBox();
//            vBox.getChildren().add(textField);
//            vBox.getChildren().add(button);
//            vBox.setSpacing(10);
//            Scene scene2 = new Scene(vBox, 300, 200);
//            Stage stage2 = new Stage();
//            stage2.setScene(scene2);
//            stage2.show();
//            button.setOnAction(event1 -> {
////                String tableName = textField.getText();
//                String databaseName = Utils.loadDatabaseFromMachine(stage2);
//                String tableName = databaseName.replace(".db", "");
////              String [] parts = dbPath.split("/");
////              String tableName=
//            });

