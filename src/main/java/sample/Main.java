package sample;

import Helpers.DatabaseConnector;
import Helpers.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;


public class Main extends Application {

    ArrayList<NodeB> nodeBList;
    ArrayList<LSite> lSitesList;
    ArrayList<USite> uSitesList;
    ArrayList<GSite> gSitesList;
    ArrayList<USite> thirdCarrierList;
    ArrayList<USite> u900List;
    Button exportBu, load2R1DumpBu, load2R2DumpBu, load3R1DumpBu, load3R2DumpBu, load4R1DumpBu, load4R2DumpBu;
    Calendar calendar;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("RAN Tool");
        Scene scene = new Scene(root, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
        initializeButtonsAndArrays(scene);
        calendar = Calendar.getInstance();

//        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection Conn = DriverManager.getConnection
                ("jdbc:sqlite:D:/RAN Tool/test.db", "r", "s");
        System.out.println("A new database has been created.");
        Statement s = Conn.createStatement();
        String sql = "create table if not exists Sites (id integer PRIMARY KEY,siteName text ,Code text)";
        s.execute(sql);


        // load 2G RAN1 Dump from path from the machine
        load2R1DumpBu.setOnAction(event -> {
            String dump2R1Path = Utils.loadDumpFromMachine(primaryStage);
            if (dump2R1Path != null) {
                DatabaseConnector databaseConnector = new DatabaseConnector(dump2R1Path);
                process2GDump(databaseConnector, 1);
                System.out.println("Loading Complete..");
            }
        });
        load2R2DumpBu.setOnAction(event -> {
            String dump2R2Path = Utils.loadDumpFromMachine(primaryStage);
            if (dump2R2Path != null) {
                DatabaseConnector databaseConnector = new DatabaseConnector(dump2R2Path);
                process2GDump(databaseConnector, 2);
            }
        });

        load3R1DumpBu.setOnAction(event -> {
            String dump3R1Path = Utils.loadDumpFromMachine(primaryStage);
            if (dump3R1Path != null) {
                DatabaseConnector databaseConnector = new DatabaseConnector(dump3R1Path);
                process3GDump(databaseConnector, 1);
            }
        });
        load3R2DumpBu.setOnAction(event -> {
            String dump3R2Path = Utils.loadDumpFromMachine(primaryStage);
            if (dump3R2Path != null) {
                DatabaseConnector databaseConnector = new DatabaseConnector(dump3R2Path);
                process3GDump(databaseConnector, 2);
            }
        });

        load4R1DumpBu.setOnAction(event -> {
            String dump4R1Path = Utils.loadDumpFromMachine(primaryStage);
            if (dump4R1Path != null) {
                DatabaseConnector databaseConnector = new DatabaseConnector(dump4R1Path);
                process4GDump(databaseConnector, 1);
            }
        });

        load4R2DumpBu.setOnAction(event -> {
            String dump4R2Path = Utils.loadDumpFromMachine(primaryStage);
            if (dump4R2Path != null) {
                DatabaseConnector databaseConnector = new DatabaseConnector(dump4R2Path);
                process4GDump(databaseConnector, 2);
            }
        });

        // export dahboard button
        exportBu.setOnAction(event -> {
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
    }

    private void export2G() throws IOException {
        System.out.println("Exporting 2G..." + calendar.getTime());
        Exporter.export2GSitesList(gSitesList, "2G Sites");
        Exporter.export2GHardWare(gSitesList, "new 2G HW");
        System.out.println("2G Dashboard is ready.." + calendar.getTime());
    }

    private void export3G() throws IOException {
        System.out.println("Exporting 3G...");
//        Exporter.exportNodeBList(nodeBList, "NodeBs");
        Exporter.export3GSitesList(uSitesList, "Sites");
        Exporter.exportCarrierList(thirdCarrierList, "3rd Carrier");
        Exporter.exportCarrierList(u900List, "U900");
        Exporter.export3GHardWare(uSitesList, "new 3G HW");
        System.out.println("3G Dashboard is ready.." + calendar.getTime());
    }

    private void export4G() throws IOException {
        System.out.println("Exporting 4G..." + calendar.getTime());
        Exporter.export4GSitesList(lSitesList, "LTE");
        Exporter.export4GHardWare(lSitesList, "new 4G HW");
        System.out.println("4G Dashboard is ready.." + calendar.getTime());
    }

    private void process2GDump(DatabaseConnector databaseConnector, int ran) {
        try {
            // get a list of 2G sites and their parameters
            gSitesList = databaseConnector.get2GSites(gSitesList);
            System.out.println(gSitesList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process3GDump(DatabaseConnector databaseConnector, int ran) {
        try {
//            nodeBList = databaseConnector.getNodeBs();
            uSitesList = databaseConnector.get3GSites(ran, uSitesList);
            thirdCarrierList = databaseConnector.getThirdCarrierSites(thirdCarrierList);
            u900List = databaseConnector.getU900List(u900List);
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
        exportBu = (Button) scene.lookup("#exportBu");
        load2R1DumpBu = (Button) scene.lookup("#load2R1DumpBu");
        load2R2DumpBu = (Button) scene.lookup("#load2R2DumpBu");
        load3R1DumpBu = (Button) scene.lookup("#load3R1DumpBu");
        load3R2DumpBu = (Button) scene.lookup("#load3R2DumpBu");
        load4R1DumpBu = (Button) scene.lookup("#load4R1DumpBu");
        load4R2DumpBu = (Button) scene.lookup("#load4R2DumpBu");
        gSitesList = new ArrayList<>();
        uSitesList = new ArrayList<>();
        thirdCarrierList = new ArrayList<>();
        u900List = new ArrayList<>();
        lSitesList = new ArrayList<>();
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
