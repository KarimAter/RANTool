package sample;

import Helpers.DatabaseConnector;
import Helpers.DatabaseHelper;
import Helpers.SerialDatabaseSaver;
import Helpers.Utils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Exporter {

    static String[] carrierHeader = {"Code", "Name", "Rnc"};
    static String orginalFileName = "C:/Ater/Development/RAN Tool/Dashboard.xlsx";
    static String updatedFileName = "";
    static String updatedSiteMapName = "";
    static String originalSiteMapName = "C:/Ater/Development/RAN Tool/SitesMap.xlsx";
    private static String TRX1FileName, gCells1FileName, uCells1FileName, uCells2FileName, lCells1FileName, lCells2FileName;
    private static String TRX2FileName, gCells2FileName;
    static String changesFileName = "C:\\Ater\\NetworkChanges.xlsx";
    static String gHardwareFileName = "C:\\Ater\\2GHW W";
    static String uHardwareFileName = "C:\\Ater\\3GHW W";
    static String lHardwareFileName = "C:\\Ater\\4GHW W";
    static String sbtsHardwareFileName = "C:\\Ater\\SbtsHW W";
    XSSFWorkbook wb;
    XSSFWorkbook changesWrb;
    private String weekName;

    Exporter(String weekName) {
        this.weekName = weekName;
        wb = prepareWorkbooks(orginalFileName);
        updatedFileName = prepareDashboardName(weekName);
        updatedSiteMapName = prepareSiteMapName(weekName);
    }

    Exporter() {
        wb = prepareWorkbooks(updatedFileName);
    }

    void exportBcfList(ArrayList<Cabinet> bcfList) throws IOException {
        ZipSecureFile.setMinInflateRatio(0);
        int numOfColumns = 26;
        XSSFSheet sheet = wb.getSheet("BCFs");
        final int[] r = {1};

        bcfList.forEach(value -> {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }

            BCF bcf = (BCF) value;
            cells.get(0).setCellValue(bcf.getCode());
            cells.get(1).setCellValue(bcf.getName());
            cells.get(2).setCellValue(bcf.getRegion());
            cells.get(3).setCellValue(bcf.getBSCName());
            cells.get(4).setCellValue(Integer.valueOf(bcf.getBscId()));
            cells.get(5).setCellValue(Integer.valueOf(bcf.getBcfId()));
            cells.get(6).setCellValue(bcf.getKey());
            cells.get(7).setCellValue(bcf.getHardware().getRfString());
            cells.get(8).setCellValue(bcf.getHardware().getSmString());
            cells.get(9).setCellValue(value.getOnAir() == 1);
            cells.get(10).setCellValue(bcf.getNumberOfTRXs());
            cells.get(11).setCellValue(bcf.getNewCellCount());
            cells.get(12).setCellValue(bcf.getNewOnAirCount());
            cells.get(13).setCellValue(bcf.getNumberOfDCSCells());
            cells.get(14).setCellValue(bcf.getNumberOfGSMCells());
            cells.get(15).setCellValue(bcf.getTxMode());
            cells.get(16).setCellValue(bcf.getNumberOfE1s());
            cells.get(17).setCellValue(bcf.getNumberOfGTRXs());
            cells.get(18).setCellValue(Integer.valueOf(bcf.getLac()));
            cells.get(19).setCellValue(Integer.valueOf(bcf.getRac()));
            cells.get(20).setCellValue(bcf.getVersion());
            cells.get(21).setCellValue(bcf.getCtrlIp());
            cells.get(22).setCellValue(bcf.getManIp());
            try {
                cells.get(23).setCellValue(Integer.valueOf(bcf.getUsedETP()));
            } catch (NumberFormatException e) {
                cells.get(23).setCellValue(bcf.getUsedETP());
            }
            try {
                cells.get(24).setCellValue(Integer.valueOf(bcf.getgConf()));
            } catch (Exception e) {
                cells.get(24).setCellValue(bcf.getgConf());
            }
            try {
                cells.get(25).setCellValue(Integer.valueOf(bcf.getdConf()));
            } catch (Exception e) {
                cells.get(25).setCellValue(bcf.getdConf());
            }
            r[0]++;
        });

        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
//        wb.close();
    }

    void exportBcfHardWare(ArrayList<Cabinet> bcfList) throws IOException {
        int numOfColumns = 22;
        XSSFSheet sheet = wb.getSheet("BCFs HW");
        final int[] r = {1};
        bcfList.forEach(cabinet -> {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            BCF bcf = (BCF) cabinet;
            cells.get(0).setCellValue(bcf.getCode());
            cells.get(1).setCellValue(bcf.getName());
            cells.get(2).setCellValue(bcf.getRegion());
            cells.get(3).setCellValue(Integer.valueOf(bcf.getBscId()));
            cells.get(4).setCellValue(Integer.valueOf(bcf.getBcfId()));
            cells.get(5).setCellValue(bcf.getKey());
            Hardware gHardware = bcf.getHardware();
            cells.get(6).setCellValue(gHardware.getRfString());
            cells.get(7).setCellValue(gHardware.getSmString());
            cells.get(8).setCellValue(gHardware.getTxString());
            cells.get(9).setCellValue(gHardware.getWeek());

            cells.get(10).setCellValue(gHardware.getModuleValue("ESMB"));
            cells.get(11).setCellValue(gHardware.getModuleValue("ESMC"));
            cells.get(12).setCellValue(gHardware.getModuleValue("FIQA"));
            cells.get(13).setCellValue(gHardware.getModuleValue("FIQB"));
            cells.get(14).setCellValue(gHardware.getModuleValue("FSMF"));
            cells.get(15).setCellValue(gHardware.getModuleValue("FTIF"));
            cells.get(16).setCellValue(gHardware.getModuleValue("FXDA"));
            cells.get(17).setCellValue(gHardware.getModuleValue("FXDB"));
            cells.get(18).setCellValue(gHardware.getModuleValue("FXEA"));
            cells.get(19).setCellValue(gHardware.getModuleValue("FXEB"));
            cells.get(20).setCellValue(gHardware.getModuleValue("FXX"));
            cells.get(21).setCellValue(gHardware.getModuleValue("FXED"));
            r[0]++;
        });

        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }


    void exportNodeBList(ArrayList<Cabinet> nodeBList) throws IOException {
        int numOfColumns = 42;
        XSSFSheet sheet = wb.getSheet("NodeBs");
        final int[] r = {1};
        nodeBList.forEach(cabinet -> {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            NodeB nodeB = (NodeB) cabinet;
            cells.get(0).setCellValue(nodeB.getCode());
            cells.get(1).setCellValue(nodeB.getName());
            cells.get(2).setCellValue(nodeB.getRegion());
            cells.get(3).setCellValue(Integer.valueOf(nodeB.getRncId()));
            cells.get(4).setCellValue(Integer.valueOf(nodeB.getWbtsId()));
            cells.get(5).setCellValue(nodeB.getKey());
            cells.get(6).setCellValue(nodeB.getHardware().getRfString());
            cells.get(7).setCellValue(nodeB.getHardware().getSmString());
            cells.get(8).setCellValue(nodeB.getNumberOfCells());
            cells.get(9).setCellValue(nodeB.getNumberOfOnAirCells());
            Map<String, Integer> cellsCountMap = nodeB.getCellsCountMap();
            IntStream.range(10, 20).forEach(i -> cells.get(i).setCellValue(cellsCountMap.get(NodeB.cellsCountNames[i - 10])));
            cells.get(20).setCellValue(nodeB.getVersion());
            cells.get(21).setCellValue(nodeB.getTxMode());
            Map<String, Integer> r99Map = nodeB.getR99CountMap();
            IntStream.range(22, 27).forEach(i -> cells.get(i).setCellValue(r99Map.get(NodeB.r99ParametersNames[i - 22])));
//            cells.get(22).setCellValue(nodeB.getNumberOfHSDPASet1());
//            cells.get(23).setCellValue(nodeB.getNumberOfHSDPASet2());
//            cells.get(24).setCellValue(nodeB.getNumberOfHSDPASet3());
//            cells.get(25).setCellValue(nodeB.getNumberOfHSUPASet1());
//            cells.get(26).setCellValue(nodeB.getNumberOfChannelElements());
            cells.get(27).setCellValue(nodeB.getNumberOfE1s());
            cells.get(28).setCellValue(nodeB.getNumberOfSectors());
            cells.get(29).setCellValue(nodeB.getNumberOfCarriers());
            cells.get(30).setCellValue(nodeB.isStandAloneU900());
            cells.get(31).setCellValue(nodeB.isRfSharing());
            try {
                cells.get(32).setCellValue(Double.valueOf(nodeB.getPower()));
            } catch (Exception e) {
                cells.get(32).setCellValue(nodeB.getPower());
            }
            try {
                cells.get(33).setCellValue(Double.valueOf(nodeB.getU900Power()));
            } catch (Exception e) {
                cells.get(33).setCellValue(nodeB.getU900Power());
            }
            cells.get(34).setCellValue(Integer.valueOf(nodeB.getLac()));
            cells.get(35).setCellValue(Integer.valueOf(nodeB.getRac()));
            cells.get(36).setCellValue(nodeB.getNodeBIP());
            try {
                cells.get(37).setCellValue(Integer.valueOf(nodeB.getSfp()));
            } catch (NumberFormatException e) {
                cells.get(37).setCellValue(nodeB.getSfp());
            }
            cells.get(38).setCellValue(nodeB.getNumberOfLCGs());
            cells.get(39).setCellValue(nodeB.getNumberOfChains());
            NodeConfiguration nodeConfiguration = nodeB.getNodeConfiguration();
            if (nodeConfiguration != null) {
                cells.get(40).setCellValue(nodeConfiguration.getSectorsMapping());
                cells.get(41).setCellValue(nodeConfiguration.getLinksMappingString());
            }
            r[0]++;
        });


        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("NodeB list done..");

    }

    void exportNodeBHardWare(ArrayList<Cabinet> nodeBList) throws IOException {
        int numOfColumns = 30;
        XSSFSheet sheet = wb.getSheet("NodeB HW");
        final int[] r = {1};

        nodeBList.forEach(cabinet -> {

            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            NodeB nodeB = (NodeB) cabinet;
            Hardware nHardware = nodeB.getHardware();
            cells.get(0).setCellValue(nodeB.getCode());
            cells.get(1).setCellValue(nodeB.getName());
            cells.get(2).setCellValue(nodeB.getRegion());
            cells.get(3).setCellValue(Integer.valueOf(nodeB.getRncId()));
            cells.get(4).setCellValue(Integer.valueOf(nodeB.getWbtsId()));
            cells.get(5).setCellValue(nodeB.getKey());
            cells.get(6).setCellValue(nHardware.getWeek());
            cells.get(7).setCellValue(nHardware.getRfString());
            cells.get(8).setCellValue(nHardware.getSmString());
            cells.get(9).setCellValue(nHardware.getTxString());
            cells.get(10).setCellValue(nHardware.getModuleValue("FBBA"));
            cells.get(11).setCellValue(nHardware.getModuleValue("FRGC"));
            cells.get(12).setCellValue(nHardware.getModuleValue("FRGD"));
            cells.get(13).setCellValue(nHardware.getModuleValue("FRGF"));
            cells.get(14).setCellValue(nHardware.getModuleValue("FRGL"));
            cells.get(15).setCellValue(nHardware.getModuleValue("FRGM"));
            cells.get(16).setCellValue(nHardware.getModuleValue("FRGP"));
            cells.get(17).setCellValue(nHardware.getModuleValue("FRGT"));
            cells.get(18).setCellValue(nHardware.getModuleValue("FRGU"));
            cells.get(19).setCellValue(nHardware.getModuleValue("FRGX"));
            cells.get(20).setCellValue(nHardware.getModuleValue("FSMB"));
            cells.get(21).setCellValue(nHardware.getModuleValue("FSMD"));
            cells.get(22).setCellValue(nHardware.getModuleValue("FSME"));
            cells.get(23).setCellValue(nHardware.getModuleValue("FSMF"));
            cells.get(24).setCellValue(nHardware.getModuleValue("FTIA"));
            cells.get(25).setCellValue(nHardware.getModuleValue("FTIB"));
            cells.get(26).setCellValue(nHardware.getModuleValue("FTIF"));
            cells.get(27).setCellValue(nHardware.getModuleValue("FTPB"));
            cells.get(28).setCellValue(nHardware.getModuleValue("FXDA"));
            cells.get(29).setCellValue(nHardware.getModuleValue("FXDB"));
            r[0]++;
        });
        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    void exportEnodeBList(ArrayList<Cabinet> eNodeBList) throws IOException {

        int numOfColumns = 17;
        XSSFSheet sheet = wb.getSheet("LTE");
        final int[] r = {1};
        eNodeBList.forEach(cabinet -> {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            EnodeB enodeB = (EnodeB) cabinet;
            cells.get(0).setCellValue(enodeB.getCode());
            cells.get(1).setCellValue(enodeB.getName());
            cells.get(2).setCellValue(Integer.valueOf(enodeB.getNodeId()));
            cells.get(3).setCellValue(enodeB.getRegion());
            cells.get(4).setCellValue(enodeB.getHardware().getRfString());
            cells.get(5).setCellValue(enodeB.getHardware().getSmString());
            cells.get(6).setCellValue(enodeB.getNumberOfCells());
            cells.get(7).setCellValue(enodeB.getNumberOfOnAirCells());
            cells.get(8).setCellValue(enodeB.getVersion());
            cells.get(9).setCellValue(Utils.convertLTEBW(enodeB.getBw()));
            cells.get(10).setCellValue(Utils.convertLTEMIMO(enodeB.getMimo()));
            cells.get(12).setCellValue(enodeB.isCarrierAggregation());
            cells.get(13).setCellValue(enodeB.getManIp());
            cells.get(14).setCellValue(enodeB.getS1Ip());
            cells.get(15).setCellValue(enodeB.getSecIp());
            cells.get(16).setCellValue(enodeB.getSecGw());
            try {
                cells.get(11).setCellValue(Integer.valueOf(enodeB.getTac()));
            } catch (NumberFormatException e) {
                cells.get(11).setCellValue(enodeB.getTac());
            }

            r[0]++;
        });
        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("4G Site list done..");
//        wb.close();
    }

    void exportSBTSList(ArrayList<Cabinet> sBtsList) throws IOException {
        int numOfColumns = 17;
        XSSFSheet sheet = wb.getSheet("SBTS");
        final int[] r = {1};
        sBtsList.forEach(cabinet -> {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            //todo: resume
            SBTS sbts = (SBTS) cabinet;
            cells.get(0).setCellValue(sbts.getCode());
            cells.get(1).setCellValue(sbts.getName());
            cells.get(2).setCellValue(Integer.valueOf(sbts.getNodeId()));
            cells.get(3).setCellValue(sbts.getRegion());
            cells.get(4).setCellValue(sbts.getHardware().getRfString());
            cells.get(5).setCellValue(sbts.getHardware().getSmString());
//            cells.get(6).setCellValue(enodeB.getNumberOfCells());
//            cells.get(7).setCellValue(enodeB.getNumberOfOnAirCells());
            cells.get(8).setCellValue(sbts.getVersion());
//            cells.get(9).setCellValue(Utils.convertLTEBW(enodeB.getBw()));
//            cells.get(10).setCellValue(Utils.convertLTEMIMO(enodeB.getMimo()));
//            cells.get(12).setCellValue(enodeB.isCarrierAggregation());
//            cells.get(13).setCellValue(enodeB.getManIp());
//            cells.get(14).setCellValue(enodeB.getS1Ip());
//            cells.get(15).setCellValue(enodeB.getSecIp());
//            cells.get(16).setCellValue(enodeB.getSecGw());
//            try {
//                cells.get(11).setCellValue(Integer.valueOf(enodeB.getTac()));
//            } catch (NumberFormatException e) {
//                cells.get(11).setCellValue(enodeB.getTac());
//            }

            r[0]++;
        });
        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("Exporting SBTS list done..");
//        wb.close();
    }





    void exportEnodeBHardWare(ArrayList<Cabinet> eNodeBList) throws IOException {
        int numOfColumns = 16;
        XSSFSheet sheet = wb.getSheet("4G HW");
        final int[] r = {1};

        eNodeBList.forEach(cabinet -> {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }

            EnodeB enodeB = (EnodeB) cabinet;
            Hardware lHardware = enodeB.getHardware();
            cells.get(0).setCellValue(enodeB.getCode());
            cells.get(1).setCellValue(enodeB.getName());
            cells.get(2).setCellValue(Integer.valueOf(enodeB.getMrbtsId()));
            cells.get(3).setCellValue(enodeB.getRegion());
            cells.get(4).setCellValue(lHardware.getRfString());
            cells.get(5).setCellValue(lHardware.getSmString());
            cells.get(6).setCellValue(lHardware.getTxString());
            cells.get(7).setCellValue(lHardware.getWeek());
            cells.get(8).setCellValue(lHardware.getModuleValue("FBBA"));
            cells.get(9).setCellValue(lHardware.getModuleValue("FBBC"));
            cells.get(10).setCellValue(lHardware.getModuleValue("FRGT"));
            cells.get(11).setCellValue(lHardware.getModuleValue("FRGU"));
            cells.get(12).setCellValue(lHardware.getModuleValue("FSMF"));
            cells.get(13).setCellValue(lHardware.getModuleValue("FTIF"));
            cells.get(14).setCellValue(lHardware.getModuleValue("FXEB"));
            cells.get(15).setCellValue(lHardware.getModuleValue("FXED"));
            r[0]++;
        });
        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        wb.close();
    }

    public void exportSBTSHardWare(ArrayList<Cabinet> sbtsList) throws IOException {

        int numOfColumns = 16;
        XSSFSheet sheet = wb.getSheet("SBTS HW");
        final int[] r = {1};

        sbtsList.forEach(cabinet -> {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }

            SBTS sbts = (SBTS) cabinet;
            Hardware lHardware = sbts.getHardware();
            cells.get(0).setCellValue(sbts.getCode());
            cells.get(1).setCellValue(sbts.getName());
            cells.get(2).setCellValue(Integer.valueOf(sbts.getNodeId()));
            cells.get(3).setCellValue(sbts.getRegion());
            cells.get(4).setCellValue(lHardware.getRfString());
            cells.get(5).setCellValue(lHardware.getSmString());
            cells.get(6).setCellValue(lHardware.getTxString());
            cells.get(7).setCellValue(lHardware.getWeek());
            cells.get(8).setCellValue(lHardware.getModuleValue("FBBA"));
            cells.get(9).setCellValue(lHardware.getModuleValue("FBBC"));
            cells.get(10).setCellValue(lHardware.getModuleValue("FRGT"));
            cells.get(11).setCellValue(lHardware.getModuleValue("FRGU"));
            cells.get(12).setCellValue(lHardware.getModuleValue("FSMF"));
            cells.get(13).setCellValue(lHardware.getModuleValue("FTIF"));
            cells.get(14).setCellValue(lHardware.getModuleValue("FXEB"));
            cells.get(15).setCellValue(lHardware.getModuleValue("FXED"));
            // TODO: Resume
            r[0]++;
        });
        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        wb.close();
    }

    void exportSiteHardwareMap(Map<String, List<Hardware>> sitesList) {
        System.out.println("Exporting HW Map.. " + Utils.getTime());
        XSSFSheet sheet1;
        XSSFWorkbook mapWb;

        try {
            mapWb = lambda.prepareWB(updatedSiteMapName, originalSiteMapName);
            sheet1 = mapWb.getSheet("HW");
            int numOfColumns = 8;
            final int[] r = {2};
            sitesList.forEach((key, value) -> {
                ArrayList<XSSFCell> cells = new ArrayList<>();
                XSSFRow row = sheet1.createRow(r[0]);
                //iterating c number of columns
                for (int i = 0; i < numOfColumns; i++) {
                    XSSFCell cell = row.createCell(i);
                    cells.add(i, cell);
                }
                value.forEach(h -> {
                    switch (h.getTech()) {
                        case 2:
                            cells.get(2).setCellValue(h.getSmString());
                            cells.get(3).setCellValue(h.getRfString());
                            break;
                        case 3:
                            cells.get(4).setCellValue(h.getSmString());
                            cells.get(5).setCellValue(h.getRfString());
                            break;
                        case 4:
                            cells.get(6).setCellValue(h.getSmString());
                            cells.get(7).setCellValue(h.getRfString());
                            break;

                    }
                });
                cells.get(0).setCellValue(value.get(0).getName());
                cells.get(1).setCellValue(value.get(0).getCode());
                r[0]++;
            });
            FileOutputStream fileOut;
            fileOut = new FileOutputStream(prepareSitesMapName(weekName));
            mapWb.write(fileOut);
            fileOut.flush();
            fileOut.close();
            mapWb.close();
            System.out.println("HW map done.. " + Utils.getTime());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    void exportConfPivot(List<StatBox> configMap, int numOfColumns, String sheetName) {
        System.out.println("Exporting " + sheetName + " configuration pivots.. " + Utils.getTime());
        XSSFSheet sheet;
        XSSFWorkbook mapWb;
        try {
            mapWb = lambda.prepareWB(updatedSiteMapName, originalSiteMapName);
            sheet = mapWb.getSheet(sheetName);
//             int numOfColumns = 4;
            final int[] r = {1};
            configMap.forEach(statBox -> {
                ArrayList<XSSFCell> cells = new ArrayList<>();
                XSSFRow row = sheet.createRow(r[0]);
                //iterating c number of columns
                for (int i = 0; i < numOfColumns; i++) {
                    XSSFCell cell = row.createCell(i);
                    cells.add(i, cell);
                }
                cells.get(0).setCellValue(statBox.getRegion());
                try {
                    cells.get(1).setCellValue(Integer.valueOf(statBox.getControllerId()));
                } catch (NumberFormatException e) {
                    cells.get(1).setCellValue(statBox.getControllerId());
                }
                ArrayList<Integer> param = statBox.getParam();
                for (int i = 0; i < param.size(); i++) {
                    cells.get(i + 2).setCellValue(param.get(i));
                }
                r[0]++;
            });
            FileOutputStream fileOut;
            fileOut = new FileOutputStream(updatedSiteMapName);
            mapWb.write(fileOut);
            fileOut.flush();
            fileOut.close();
            mapWb.close();
            System.out.println(sheetName + " pivots done.. " + Utils.getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void exportCarr(List<StatBox> configMap, String sheetName) {
        System.out.println("Exporting " + sheetName + " configuration pivots.. " + Utils.getTime());
        XSSFSheet sheet;
        XSSFWorkbook mapWb;
        int numOfColumns = 3;
        try {
            mapWb = lambda.prepareWB(updatedSiteMapName, originalSiteMapName);
            sheet = mapWb.getSheet(sheetName);
//             int numOfColumns = 4;
            final int[] r = {1};
            configMap.forEach(statBox -> {
                ArrayList<XSSFCell> cells = new ArrayList<>();
                XSSFRow row = sheet.createRow(r[0]);
                //iterating c number of columns
                for (int i = 0; i < numOfColumns; i++) {
                    XSSFCell cell = row.createCell(i);
                    cells.add(i, cell);
                }
                try {
                    cells.get(2).setCellValue(Integer.valueOf(statBox.getControllerId()));
                } catch (NumberFormatException e) {
                    cells.get(2).setCellValue(statBox.getControllerId());
                }
                cells.get(0).setCellValue(statBox.getCode());
                cells.get(1).setCellValue(statBox.getName());
                r[0]++;
            });
            FileOutputStream fileOut;
            fileOut = new FileOutputStream(updatedSiteMapName);
            mapWb.write(fileOut);
            fileOut.flush();
            fileOut.close();
            mapWb.close();
            System.out.println(sheetName + " pivots done.. " + Utils.getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void exportHardwareCount(Map<String, Long> hardwareCount, String sheetName) {
        System.out.println("Exporting " + sheetName + " configuration pivots.. " + Utils.getTime());
        XSSFSheet sheet;
        XSSFWorkbook mapWb;
        int numOfColumns = 2;
        try {
            mapWb = lambda.prepareWB(updatedSiteMapName, originalSiteMapName);
            sheet = mapWb.getSheet(sheetName);
//             int numOfColumns = 4;

            final int[] r = {1};
            hardwareCount.forEach((hardwareType, count) -> {
                ArrayList<XSSFCell> cells = new ArrayList<>();
                XSSFRow row = sheet.createRow(r[0]);
                //iterating c number of columns
                for (int i = 0; i < numOfColumns; i++) {
                    XSSFCell cell = row.createCell(i);
                    cells.add(i, cell);
                }

                cells.get(0).setCellValue(hardwareType);
                cells.get(1).setCellValue(count);
                r[0]++;
            });
            FileOutputStream fileOut;
            fileOut = new FileOutputStream(updatedSiteMapName);
            mapWb.write(fileOut);
            fileOut.flush();
            fileOut.close();
            mapWb.close();
            System.out.println(sheetName + " pivots done.. " + Utils.getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//    void exportUConfPivot(List<StatBox> uTable) {
//
//        System.out.println("Exporting pivots.. " + Utils.getTime());
//        XSSFSheet sheet2;
//        XSSFWorkbook mapWb;
//        try {
//            mapWb = lambda.prepareWB("C:/Ater/Development/RAN Tool/SitesMap.xlsx");
//            sheet2 = mapWb.getSheet("Sheet2");
//            int numOfColumns = 9;
//            final int[] r = {1};
//            uTable.forEach(statBox -> {
//                ArrayList<XSSFCell> cells = new ArrayList<>();
//                XSSFRow row = sheet2.createRow(r[0]);
//                //iterating c number of columns
//                for (int i = 0; i < numOfColumns; i++) {
//                    XSSFCell cell = row.createCell(i);
//                    cells.add(i, cell);
//                }
//
//                cells.get(0).setCellValue(Integer.valueOf(statBox.getControllerId()));
//                ArrayList<Integer> param = statBox.getParam();
//                for (int i = 0; i < param.size(); i++) {
//                    cells.get(i + 1).setCellValue(param.get(i));
//                }
//                r[0]++;
//            });
//            FileOutputStream fileOut;
//            fileOut = new FileOutputStream(updatedSiteMapName);
//            mapWb.write(fileOut);
//            fileOut.flush();
//            fileOut.close();
//            mapWb.close();
//            System.out.println("pivots done.. " + Utils.getTime());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    public void exportSitesPivot(Map<String, List<Integer>> sitesTable, int numOfColumns, String sheetName) {
        System.out.println("Exporting " + sheetName + " pivots.. " + Utils.getTime());
        XSSFSheet sheet2;
        XSSFWorkbook mapWb;

        try {
            mapWb = lambda.prepareWB(updatedSiteMapName, originalSiteMapName);
            sheet2 = mapWb.getSheet(sheetName);
//            int numOfColumns = 4;
            final int[] r = {1};
            sitesTable.forEach((controller, counts) -> {
                ArrayList<XSSFCell> cells = new ArrayList<>();
                XSSFRow row = sheet2.createRow(r[0]);
                //iterating c number of columns
                for (int i = 0; i < numOfColumns; i++) {
                    XSSFCell cell = row.createCell(i);
                    cells.add(i, cell);
                }
                cells.get(0).setCellValue(Utils.mapToRegion(controller));
                try {
                    cells.get(1).setCellValue(Integer.valueOf(controller));
                } catch (NumberFormatException e) {
                    cells.get(1).setCellValue(controller);
                }
                for (int i = 0; i < counts.size(); i++) {
                    try {
                        cells.get(i + 2).setCellValue(counts.get(i));
                    } catch (NullPointerException e) {
                        cells.get(i + 2).setCellValue(0);
                    }
                }
                r[0]++;
            });
            FileOutputStream fileOut;
            fileOut = new FileOutputStream(updatedSiteMapName);
            mapWb.write(fileOut);
            fileOut.flush();
            fileOut.close();
            mapWb.close();
            System.out.println(sheetName + "pivots done.. " + Utils.getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private XSSFWorkbook prepareWorkbooks(String path) {
        File file = new File(path);
        InputStream ExcelFileToRead;
        XSSFWorkbook workbook = null;
        if (file.exists()) {
            ZipSecureFile.setMinInflateRatio(0);
            try {
                ExcelFileToRead = new FileInputStream(file);
                workbook = new XSSFWorkbook(ExcelFileToRead);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }

    private String prepareSiteMapName(String weekName) {
        return "C:\\Ater\\SitesMap W" + weekName + ".xlsx";
    }

    private String prepareDashboardName(String weekName) {
        return "C:\\Ater\\Dashboard W" + weekName + ".xlsx";
    }

    private void prepareTrxSheetNames(String weekName) {
        TRX1FileName = "C:\\Ater\\TRXSheet1 W" + weekName + ".xlsx";
        TRX2FileName = "C:\\Ater\\TRXSheet2 W" + weekName + ".xlsx";

    }

    private void prepare2GcellsSheetNames(String weekName) {
        gCells1FileName = "C:\\Ater\\2G Cells1 W" + weekName + ".xlsx";
        gCells2FileName = "C:\\Ater\\2G Cells2 W" + weekName + ".xlsx";

    }

    private void prepare3GcellsSheetNames(String weekName) {
        uCells1FileName = "C:\\Ater\\3G Cells1 W" + weekName + ".xlsx";
        uCells2FileName = "C:\\Ater\\3G Cells2 W" + weekName + ".xlsx";
    }

    private void prepare4GcellsSheetNames(String weekName) {
        lCells1FileName = "C:\\Ater\\4G Cells1 W" + weekName + ".xlsx";
        lCells2FileName = "C:\\Ater\\4G Cells2 W" + weekName + ".xlsx";
    }

    private String prepareSitesMapName(String weekName) {
        return "C:\\Ater\\SitesMap W" + weekName + ".xlsx";
    }

    private XSSFWorkbook get2GHWWorkbook() throws IOException {
        File file = new File("C:/Ater/Development/RAN Tool/2GHWList.xlsx");
        InputStream ExcelFileToRead;
        XSSFWorkbook workbook;
        if (file.exists()) {
            ZipSecureFile.setMinInflateRatio(0);
            ExcelFileToRead = new FileInputStream(file);
            workbook = new XSSFWorkbook(ExcelFileToRead);
        } else {
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }

    getWorkbook lambda = (templatePath, originalPath) -> {

        InputStream ExcelFileToRead;
        XSSFWorkbook workbook;
        File file = new File(templatePath);
        if (file.exists()) {
            ZipSecureFile.setMinInflateRatio(0);
            ExcelFileToRead = new FileInputStream(file);
            workbook = new XSSFWorkbook(ExcelFileToRead);
        } else {
            ZipSecureFile.setMinInflateRatio(0);
            ExcelFileToRead = new FileInputStream(new File(originalPath));
            workbook = new XSSFWorkbook(ExcelFileToRead);
        }
        return workbook;
    };


    private interface getWorkbook {
        XSSFWorkbook prepareWB(String templatePath, String originalPath) throws IOException;

    }

    private static XSSFWorkbook getGcellsWorkbook() throws IOException {
        File file = new File("C:/Ater/Development/RAN Tool/2G Cells.xlsx");
        InputStream ExcelFileToRead;
        XSSFWorkbook workbook;
        if (file.exists()) {
            ZipSecureFile.setMinInflateRatio(0);
            ExcelFileToRead = new FileInputStream(file);
            workbook = new XSSFWorkbook(ExcelFileToRead);
        } else {
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }

    private static XSSFWorkbook getUcellsWorkbook() throws IOException {
        File file = new File("C:/Ater/Development/RAN Tool/3G Cells.xlsx");
        InputStream ExcelFileToRead;
        XSSFWorkbook workbook;
        if (file.exists()) {
            ZipSecureFile.setMinInflateRatio(0);
            ExcelFileToRead = new FileInputStream(file);
            workbook = new XSSFWorkbook(ExcelFileToRead);
        } else {
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }

    private static XSSFWorkbook getLcellsWorkbook() throws IOException {
        File file = new File("C:/Ater/Development/RAN Tool/4G Cells.xlsx");
        InputStream ExcelFileToRead;
        XSSFWorkbook workbook;
        if (file.exists()) {
            ZipSecureFile.setMinInflateRatio(0);
            ExcelFileToRead = new FileInputStream(file);
            workbook = new XSSFWorkbook(ExcelFileToRead);
        } else {
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }


    // Todo: store dump path in shared pref
    void exportTRXSheet(String dump2R1Path, String dump2R2Path, String weekName) throws SQLException, IOException {
        prepareTrxSheetNames(weekName);
        DatabaseConnector databaseConnector1 = new DatabaseConnector(dump2R1Path);
        exportTrx(databaseConnector1.getTRXSheet(), TRX1FileName);
        DatabaseConnector databaseConnector2 = new DatabaseConnector(dump2R2Path);
        exportTrx(databaseConnector2.getTRXSheet(), TRX2FileName);
    }

    private void exportTrx(ResultSet resultSet, String trxFileName) throws IOException, SQLException {
        XSSFSheet sheet1;
        XSSFWorkbook trxWb;
//        trxWb = getTRXWorkbook();
        trxWb = lambda.prepareWB(TRX1FileName, "C:/Ater/Development/RAN Tool/Dashboard_TRX1.xlsx");
        sheet1 = trxWb.getSheet("TRX");
        int numOfColumns = 14;
        int r = 1;
        while (resultSet.next()) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet1.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            String siteName = resultSet.getString(10);
            cells.get(0).setCellValue(Integer.valueOf(resultSet.getString(11)));
            cells.get(1).setCellValue(siteName);
            cells.get(2).setCellValue(Utils.extractSiteCode(siteName));
            cells.get(3).setCellValue(Integer.valueOf(resultSet.getString(1)));
            cells.get(4).setCellValue(Integer.valueOf(resultSet.getString(2)));
            cells.get(5).setCellValue(Integer.valueOf(resultSet.getString(3)));
            try {
                cells.get(6).setCellValue(Integer.valueOf(resultSet.getString(13)));
            } catch (NumberFormatException e) {
                cells.get(6).setCellValue("");
            }
            try {
                cells.get(7).setCellValue(Integer.valueOf(resultSet.getString(4)));
            } catch (Exception e) {
                cells.get(7).setCellValue("");
            }
            try {
                cells.get(8).setCellValue(Integer.valueOf(resultSet.getString(5)));
            } catch (Exception e) {
                cells.get(8).setCellValue("");
            }
            try {
                cells.get(9).setCellValue(Integer.valueOf(resultSet.getString(6)));
            } catch (Exception e) {
                cells.get(9).setCellValue("");
            }
            cells.get(10).setCellValue(resultSet.getString(7));

            try {
                cells.get(11).setCellValue(Integer.valueOf(resultSet.getString(8)));
            } catch (Exception e) {
                cells.get(11).setCellValue("");
            }

            try {
                cells.get(12).setCellValue(Integer.valueOf(resultSet.getString(9)));
            } catch (Exception e) {
                cells.get(12).setCellValue("");
            }
            if (resultSet.getInt(12) == 0)
                cells.get(13).setCellValue("GSM");
            else cells.get(13).setCellValue("DCS");


            r++;
        }
        FileOutputStream fileOut;
        fileOut = new FileOutputStream(trxFileName);
        trxWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        trxWb.close();
    }

    void exportGcellsSheet(String dump2R1Path, String dump2R2Path, String weekName) throws SQLException, IOException {
        prepare2GcellsSheetNames(weekName);
        DatabaseConnector databaseConnector1 = new DatabaseConnector(dump2R1Path);
        export2GCells(databaseConnector1.getGcellsSheet(), gCells1FileName);
        DatabaseConnector databaseConnector2 = new DatabaseConnector(dump2R2Path);
        export2GCells(databaseConnector2.getGcellsSheet(), gCells2FileName);
    }

    private void export2GCells(ResultSet resultSet, String gCellsFileName) throws IOException, SQLException {
        XSSFSheet sheet1;
        XSSFWorkbook gCellsWb;
        gCellsWb = getGcellsWorkbook();
        sheet1 = gCellsWb.getSheet("Cells");
        int numOfColumns = 14;
        int r = 1;
        while (resultSet.next()) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet1.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
//            String siteName = resultSet.getString(10);
            cells.get(0).setCellValue(Utils.extractSiteCode(resultSet.getString(10)));
            cells.get(1).setCellValue(resultSet.getString(10));
            cells.get(2).setCellValue(resultSet.getString(11));
            cells.get(3).setCellValue(Integer.valueOf(resultSet.getString(1)));
            cells.get(4).setCellValue(Integer.valueOf(resultSet.getString(2)));
            cells.get(5).setCellValue(Integer.valueOf(resultSet.getString(3)));
            cells.get(6).setCellValue(Integer.valueOf(resultSet.getString(9)));
            cells.get(7).setCellValue(Integer.valueOf(resultSet.getString(4)));
            cells.get(8).setCellValue(Integer.valueOf(resultSet.getString(5)));
            cells.get(9).setCellValue(Integer.valueOf(resultSet.getString(7)));
            cells.get(10).setCellValue(Integer.valueOf(resultSet.getString(8)));
            if (resultSet.getInt(6) == 0)
                cells.get(11).setCellValue("GSM");
            else cells.get(11).setCellValue("DCS");
            try {
                cells.get(12).setCellValue(Integer.valueOf(resultSet.getString(12)));
            } catch (NumberFormatException e) {
                cells.get(12).setCellValue(resultSet.getString(12));
            }
            try {
                cells.get(13).setCellValue(Integer.valueOf(resultSet.getString(13)));
            } catch (NumberFormatException e) {
                cells.get(12).setCellValue(resultSet.getString(13));
            }
            r++;
        }
        FileOutputStream fileOut;

        fileOut = new FileOutputStream(gCellsFileName);
        gCellsWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        gCellsWb.close();
    }

    void exportUcellsSheet(String dump3R1Path, String dump3R2Path, String weekName) throws SQLException, IOException {

        DatabaseConnector databaseConnector1 = new DatabaseConnector(dump3R1Path);
        DatabaseConnector databaseConnector2 = new DatabaseConnector(dump3R2Path);
        ResultSet uCellsResultSet1 = databaseConnector1.getUcellsSheet();
        ResultSet uCellsResultSet2 = databaseConnector2.getUcellsSheet();
        prepare3GcellsSheetNames(weekName);
        export3GCells(uCellsResultSet1, uCells1FileName);
        export3GCells(uCellsResultSet2, uCells2FileName);

    }

    void exportLcellsSheet(String dump4R1Path, String dump4R2Path, String weekName) throws SQLException, IOException {

        DatabaseConnector databaseConnector1 = new DatabaseConnector(dump4R1Path);
        DatabaseConnector databaseConnector2 = new DatabaseConnector(dump4R2Path);
        ResultSet lCellsResultSet1 = databaseConnector1.getLcellsSheet();
        ResultSet lCellsResultSet2 = databaseConnector2.getLcellsSheet();
        prepare4GcellsSheetNames(weekName);
        export4GCells(lCellsResultSet1, lCells1FileName);
        export4GCells(lCellsResultSet2, lCells2FileName);

    }

    private static void export4GCells(ResultSet resultSet, String lCellsFileName) throws IOException, SQLException {
        XSSFSheet sheet1;
        XSSFWorkbook lCellsWb;
        lCellsWb = getLcellsWorkbook();
        sheet1 = lCellsWb.getSheet("Cells");
        int numOfColumns = 10;
        int r = 1;
        while (resultSet.next()) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet1.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
//            String siteName = resultSet.getString(10);
            cells.get(0).setCellValue(Utils.extractSiteCode(resultSet.getString(1)));
            cells.get(1).setCellValue(resultSet.getString(1));
            cells.get(2).setCellValue(resultSet.getInt(2));
            cells.get(3).setCellValue(resultSet.getInt(3));
            cells.get(4).setCellValue(resultSet.getInt(4));
            cells.get(5).setCellValue(resultSet.getInt(5));
            cells.get(6).setCellValue(resultSet.getInt(6));
            cells.get(7).setCellValue(resultSet.getInt(7));
            cells.get(8).setCellValue(resultSet.getInt(8));
            cells.get(9).setCellValue(resultSet.getString(9));
            r++;
        }
        FileOutputStream fileOut;

        fileOut = new FileOutputStream(lCellsFileName);
        lCellsWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        lCellsWb.close();
    }

    private static void export3GCells(ResultSet resultSet, String uCellsFileName) throws IOException, SQLException {
        XSSFSheet sheet1;
        XSSFWorkbook uCellsWb;
        uCellsWb = getUcellsWorkbook();
        sheet1 = uCellsWb.getSheet("Cells");
        int numOfColumns = 13;
        int r = 1;
        while (resultSet.next()) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet1.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
//            String siteName = resultSet.getString(10);
            cells.get(0).setCellValue(resultSet.getString(1));
            cells.get(1).setCellValue(resultSet.getString(2));
            cells.get(2).setCellValue(Integer.valueOf(resultSet.getString(3)));
            cells.get(3).setCellValue(Integer.valueOf(resultSet.getString(4)));

            cells.get(4).setCellValue(resultSet.getInt(5));
            cells.get(5).setCellValue(resultSet.getInt(6));
            cells.get(6).setCellValue(resultSet.getInt(7));
            int dbPower = resultSet.getInt(8);
            cells.get(7).setCellValue(dbPower);
            cells.get(8).setCellValue(resultSet.getInt(9));
            cells.get(9).setCellValue(resultSet.getInt(10));
            cells.get(10).setCellValue(resultSet.getString(11));
            int vamEnabled;
            try {
                vamEnabled = resultSet.getInt(12);
            } catch (NumberFormatException e) {
                vamEnabled = 0;
            }
            cells.get(11).setCellValue(vamEnabled);
            cells.get(12).setCellValue(Utils.convertPower(dbPower, vamEnabled));

            r++;
        }
        FileOutputStream fileOut;

        fileOut = new FileOutputStream(uCellsFileName);
        uCellsWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        uCellsWb.close();
    }

    private static XSSFWorkbook getChangesWorkbook(String trxFileName) throws IOException {
        File file = new File(trxFileName);
        InputStream ExcelFileToRead;
        XSSFWorkbook workbook;
        if (file.exists()) {
            ZipSecureFile.setMinInflateRatio(0);
            ExcelFileToRead = new FileInputStream(file);
            workbook = new XSSFWorkbook(ExcelFileToRead);
        } else {
            workbook = new XSSFWorkbook();
        }
        return workbook;

    }

    void exportChangesSheet(ResultSet resultSet) throws SQLException, IOException {
        XSSFSheet newSitesSheet, trxSheet, gTrxSheet, psSheet, carrSheet, ceSheet, paSheet, bwSheet;
        changesWrb = getChangesWorkbook(changesFileName);
        int newSitesColumns = 3;
        int trxColumns = 10;
        int gTxModeColumns = 2;
        int powerColumns = 9;
        int tokenColumns = 9;
        int carrColumns = 8;
        int ceColumns = 8;
        int bwColumns = 5;
        int newSitesCursor = 1, trxCursor = 1, gTxModeCursor = 1, psCursor = 1, carrCursor = 1, ceCursor = 1, paCursor = 1, bwCursor = 1;
        while (resultSet.next()) {
            String siteName = resultSet.getString(2);
            String siteCode = resultSet.getString(3);

            if (resultSet.getString(21) == null && resultSet.getInt(5) > 0) {
                newSitesSheet = changesWrb.getSheet("New Sites");
                ArrayList<XSSFCell> cells = new ArrayList<>();
                XSSFRow row = newSitesSheet.createRow(newSitesCursor);
                //iterating c number of columns
                for (int i = 0; i < newSitesColumns; i++) {
                    XSSFCell cell = row.createCell(i);
                    cells.add(i, cell);
                }
                cells.get(0).setCellValue(siteName);
                cells.get(1).setCellValue(siteCode);
                switch (resultSet.getInt(4)) {
                    case 2:
                        cells.get(2).setCellValue("2G");
                        break;
                    case 3:
                        cells.get(2).setCellValue("3G");
                        break;
                    case 4:
                        cells.get(2).setCellValue("4G");
                        break;
                }
                newSitesCursor++;
            } else {
                switch (resultSet.getInt(4)) {
                    case 2: {
                        String trxComment = ChangesDetector.getTrxComment(resultSet);
                        String gTxModeComment = ChangesDetector.getTxModeComment(resultSet);
                        int oldTrx = resultSet.getInt(26);
                        if (trxComment.contains("grade") && oldTrx != 0) {
                            trxSheet = changesWrb.getSheet("TRX");
                            ArrayList<XSSFCell> cells = new ArrayList<>();
                            XSSFRow row = trxSheet.createRow(trxCursor);
                            //iterating c number of columns
                            for (int i = 0; i < trxColumns; i++) {
                                XSSFCell cell = row.createCell(i);
                                cells.add(i, cell);
                            }
                            cells.get(0).setCellValue(siteName);
                            cells.get(1).setCellValue(siteCode);
                            cells.get(2).setCellValue(trxComment);
                            cells.get(3).setCellValue(oldTrx);
                            cells.get(4).setCellValue(resultSet.getInt(6));
                            String currentSm = resultSet.getString(14);
                            String oldSm = resultSet.getString(34);
                            String currentRf = resultSet.getString(13);
                            String oldRf = resultSet.getString(33);
                            cells.get(5).setCellValue(oldSm);
                            cells.get(6).setCellValue(currentSm);
                            cells.get(7).setCellValue(oldRf);
                            cells.get(8).setCellValue(currentRf);
                            if (oldSm.equals("") || currentSm.equals("") || oldRf.equals("") || currentRf.equals(""))
                                cells.get(9).setCellValue("");
                            else if (oldSm.equals(currentSm) && oldRf.equals(currentRf))
                                cells.get(9).setCellValue("Soft");
                            else
                                cells.get(9).setCellValue("Hard");
                            trxCursor++;
                        }

                        int oldTxMode = resultSet.getInt(27);
                        if (gTxModeComment.contains("grade")) {
                            gTrxSheet = changesWrb.getSheet("PAbis");
                            ArrayList<XSSFCell> cells = new ArrayList<>();
                            XSSFRow row = gTrxSheet.createRow(gTxModeCursor);
                            //iterating c number of columns
                            for (int i = 0; i < gTxModeColumns; i++) {
                                XSSFCell cell = row.createCell(i);
                                cells.add(i, cell);
                            }
                            cells.get(0).setCellValue(siteName);
                            cells.get(1).setCellValue(siteCode);
//                            cells.get(2).setCellValue(gTxModeComment);
//                            cells.get(3).setCellValue(oldTxMode);
//                            cells.get(4).setCellValue(resultSet.getInt(7));
                            gTxModeCursor++;
                        }
                    }
                    break;
                    case 3: {
                        boolean psChange = ChangesDetector.getTokenChanges(resultSet).isChange();
                        boolean carrChange = ChangesDetector.getCarriersChanges(resultSet).isChange();
                        String ceComment = ChangesDetector.getChannelElementsComment(resultSet);
                        boolean paChange = ChangesDetector.getPowerChanges(resultSet).isChange();
                        if (psChange) {
                            ChangesDetector.ChangeValues tokenChanges = ChangesDetector.getTokenChanges(resultSet);
                            String[] tknComments = tokenChanges.getComment();
                            String[] tknActionTypes = tokenChanges.getActionType();
                            for (int j = 0; j < tknComments.length; j++) {
                                if (tknComments[j].contains("grade")) {
                                    psSheet = changesWrb.getSheet("Processing Sets");
                                    ArrayList<XSSFCell> cells = new ArrayList<>();
                                    XSSFRow row = psSheet.createRow(psCursor);
                                    //iterating c number of columns
                                    for (int i = 0; i < tokenColumns; i++) {
                                        XSSFCell cell = row.createCell(i);
                                        cells.add(i, cell);
                                    }
                                    cells.get(0).setCellValue(siteName);
                                    cells.get(1).setCellValue(siteCode);
                                    cells.get(2).setCellValue(tknComments[j]);
//                                ChangesDetector.ChangeValues tokenChanges = ChangesDetector.getTokenChanges(resultSet);
                                    cells.get(3).setCellValue(tknActionTypes[j]);
                                    cells.get(4).setCellValue((int) tokenChanges.getOld()[j]);
                                    cells.get(5).setCellValue((int) tokenChanges.getCurrent()[j]);
//                                cells.get(6).setCellValue((int) tokenChanges.getOld()[1]);
//                                cells.get(7).setCellValue((int) tokenChanges.getCurrent()[1]);
//                                cells.get(8).setCellValue((int) tokenChanges.getOld()[2]);
//                                cells.get(9).setCellValue((int) tokenChanges.getCurrent()[2]);
//                                cells.get(10).setCellValue((int) tokenChanges.getOld()[3]);
//                                cells.get(11).setCellValue((int) tokenChanges.getCurrent()[3]);
                                    String currentSm = resultSet.getString(17);
                                    String oldSm = resultSet.getString(37);
                                    cells.get(6).setCellValue(oldSm);
                                    cells.get(7).setCellValue(currentSm);
                                    if (oldSm.equals("") || currentSm.equals(""))
                                        cells.get(8).setCellValue("");
                                    else if (oldSm.equals(currentSm))
                                        cells.get(8).setCellValue("Soft");
                                    else
                                        cells.get(8).setCellValue("Hard");
                                    psCursor++;
                                }
                            }
                        }

                        if (carrChange) {
                            ChangesDetector.ChangeValues carrierChanges = ChangesDetector.getCarriersChanges(resultSet);
                            String[] carrComments = carrierChanges.getComment();
                            String[] carrActionTypes = carrierChanges.getActionType();
                            for (int j = 0; j < carrComments.length; j++) {
                                if (carrComments[j].contains("grade")) {
                                    carrSheet = changesWrb.getSheet("New Carriers");
                                    ArrayList<XSSFCell> cells = new ArrayList<>();
                                    XSSFRow row = carrSheet.createRow(carrCursor);


                                    //iterating c number of columns
                                    for (int i = 0; i < carrColumns; i++) {
                                        XSSFCell cell = row.createCell(i);
                                        cells.add(i, cell);
                                    }
                                    cells.get(0).setCellValue(siteName);
                                    cells.get(1).setCellValue(siteCode);
                                    cells.get(2).setCellValue(carrActionTypes[j]);
                                    String currentSm = resultSet.getString(17);
                                    String oldSm = resultSet.getString(37);
                                    String currentRf = resultSet.getString(16);
                                    String oldRf = resultSet.getString(36);
                                    cells.get(3).setCellValue(oldRf);
                                    cells.get(4).setCellValue(currentRf);
                                    cells.get(5).setCellValue(oldSm);
                                    cells.get(6).setCellValue(currentSm);
                                    if (oldSm.equals("") || currentSm.equals(""))
                                        cells.get(7).setCellValue("");
                                    else if (oldSm.equals(currentSm) && oldRf.equals(currentRf))
                                        cells.get(7).setCellValue("Soft");
                                    else
                                        cells.get(7).setCellValue("Hard");

                                    carrCursor++;
                                }
                            }
                        }


                        int oldCEs = resultSet.getInt(29);
                        if (ceComment.contains("grade") && oldCEs != 0) {
                            ceSheet = changesWrb.getSheet("CEs");
                            ArrayList<XSSFCell> cells = new ArrayList<>();
                            XSSFRow row = ceSheet.createRow(ceCursor);

                            //iterating c number of columns
                            for (int i = 0; i < ceColumns; i++) {
                                XSSFCell cell = row.createCell(i);
                                cells.add(i, cell);
                            }
                            cells.get(0).setCellValue(siteName);
                            cells.get(1).setCellValue(siteCode);
                            cells.get(2).setCellValue(ceComment);
                            cells.get(3).setCellValue(oldCEs);
                            cells.get(4).setCellValue(resultSet.getInt(9));
                            String currentSm = resultSet.getString(17);
                            String oldSm = resultSet.getString(37);
                            cells.get(5).setCellValue(oldSm);
                            cells.get(6).setCellValue(currentSm);
                            if (oldSm.equals("") || currentSm.equals(""))
                                cells.get(7).setCellValue("");
                            else if (oldSm.equals(currentSm))
                                cells.get(7).setCellValue("Soft");
                            else
                                cells.get(7).setCellValue("Hard");
                            ceCursor++;
                        }


                        if (paChange) {
                            ChangesDetector.ChangeValues powerChanges = ChangesDetector.getPowerChanges(resultSet);
                            String[] paComments = powerChanges.getComment();
                            String[] paActionTypes = powerChanges.getActionType();
                            for (int j = 0; j < paComments.length; j++) {
                                if (paComments[j].contains("grade")) {
                                    paSheet = changesWrb.getSheet("PA");
                                    ArrayList<XSSFCell> cells = new ArrayList<>();
                                    XSSFRow row = paSheet.createRow(paCursor);
                                    //iterating c number of columns
                                    for (int i = 0; i < powerColumns; i++) {
                                        XSSFCell cell = row.createCell(i);
                                        cells.add(i, cell);
                                    }
                                    cells.get(0).setCellValue(siteName);
                                    cells.get(1).setCellValue(siteCode);
                                    cells.get(2).setCellValue(paComments[j]);
//                                ChangesDetector.ChangeValues powerChanges = ChangesDetector.getPowerChanges(resultSet);
                                    cells.get(3).setCellValue(paActionTypes[j]);
                                    cells.get(4).setCellValue(powerChanges.getOld()[j]);
                                    cells.get(5).setCellValue(powerChanges.getCurrent()[j]);
//                                cells.get(6).setCellValue(powerChanges.getOld()[1]);
//                                cells.get(7).setCellValue(powerChanges.getCurrent()[1]);
                                    String currentRF = resultSet.getString(16);
                                    String oldRF = resultSet.getString(36);
                                    cells.get(6).setCellValue(oldRF);
                                    cells.get(7).setCellValue(currentRF);
                                    if (oldRF.equals("") || currentRF.equals(""))
                                        cells.get(8).setCellValue("");
                                    else if (oldRF.equals(currentRF))
                                        cells.get(8).setCellValue("Soft");
                                    else
                                        cells.get(8).setCellValue("Hard");
                                    paCursor++;
                                }
                            }
                        }
                    }
                    break;
                    case 4: {
                        String bwComment = ChangesDetector.getBWComment(resultSet);
                        if (bwComment.contains("grade")) {
                            bwSheet = changesWrb.getSheet("BW");
                            ArrayList<XSSFCell> cells = new ArrayList<>();
                            XSSFRow row = bwSheet.createRow(bwCursor);
                            //iterating c number of columns
                            for (int i = 0; i < bwColumns; i++) {
                                XSSFCell cell = row.createCell(i);
                                cells.add(i, cell);
                            }
                            cells.get(0).setCellValue(siteName);
                            cells.get(1).setCellValue(siteCode);
                            cells.get(2).setCellValue(bwComment);
                            cells.get(3).setCellValue(resultSet.getInt(32));
                            cells.get(4).setCellValue(resultSet.getInt(12));
                            bwCursor++;
                        }
                    }
                    break;

                }
            }

        }

        FileOutputStream fileOut;
        fileOut = new FileOutputStream(changesFileName);
        changesWrb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("Changes sheet is done..");
    }

    //  2G HW list
    void export2GHWfromXML(ArrayList<Cabinet> bcfs, String weekName) throws IOException {
        XSSFWorkbook gHardwareWb;
//        gHardwareWb = get2GHWWorkbook();

        gHardwareWb = lambda.prepareWB(gHardwareFileName, "C:/Ater/Development/RAN Tool/2GHWList.xlsx");
        int numOfColumns = 8;
        XSSFSheet sheet = gHardwareWb.getSheet("Sheet1");
        final int[] r = {1};

        bcfs.forEach(bcf ->
        {
            ArrayList<HwItem> hwItems = bcf.getHardware().getHwItems();
            for (HwItem hwItem : hwItems) {
                ArrayList<XSSFCell> cells = new ArrayList<>();
                XSSFRow row = sheet.createRow(r[0]);
                //iterating c number of columns
                for (int i = 0; i < numOfColumns; i++) {
                    XSSFCell cell = row.createCell(i);
                    cells.add(i, cell);
                }
                cells.get(0).setCellValue(bcf.getCode());
                cells.get(1).setCellValue(bcf.getName());
                cells.get(2).setCellValue(Integer.valueOf(bcf.getControllerId()));
                cells.get(3).setCellValue(Integer.valueOf(bcf.getNodeId()));
                cells.get(4).setCellValue(hwItem.getUserLabel());
                cells.get(5).setCellValue(hwItem.getSerialNumber());
                cells.get(6).setCellValue(hwItem.getIdentificationCode());
                cells.get(7).setCellValue(bcf.getHardware().getWeek());
                r[0]++;
            }
        });
        gHardwareFileName = gHardwareFileName + weekName + ".xlsx";
        FileOutputStream fileOut = new FileOutputStream(gHardwareFileName);
        //write this workbook to an Outputstream.
        gHardwareWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("2G hardwarefile is done");
    }

    void exportHwFromDatabase() throws IOException, SQLException {
        DatabaseHelper databaseHelper = new DatabaseHelper("C:/Ater/Development/RAN Tool/NokiaDumpToolHistory.db", weekName);
        HashMap<String, Hardware> dumpHwMap = databaseHelper.loadDumpHwMap();

        exportGandU(dumpHwMap, weekName, gHardwareFileName, "C:/Ater/Development/RAN Tool/2GHWListDb.xlsx", 2);
        exportGandU(dumpHwMap, weekName, uHardwareFileName, "C:/Ater/Development/RAN Tool/3GHWListDb.xlsx", 3);
        exLte(dumpHwMap, weekName);
        exSbts(dumpHwMap, weekName);

    }

    private void exSbts(HashMap<String, Hardware> dumpHwMap, String weekName) throws IOException, SQLException {
        List<String> foundKeys = new ArrayList<>();
        SerialDatabaseSaver serialDatabaseSaver = new SerialDatabaseSaver("C:/Ater/Development/RAN Tool/serials.db");
        ResultSet resultSet = serialDatabaseSaver.load("SBTS");
        XSSFWorkbook hardwareWb;
        hardwareWb = lambda.prepareWB(sbtsHardwareFileName, "C:/Ater/Development/RAN Tool/SbtsHWListDb.xlsx");
        int numOfColumns = 8;
        XSSFSheet sheet = hardwareWb.getSheet("Sheet1");
        final int[] r = {1};
        while (resultSet.next()) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            String key = resultSet.getString(1);
            if (dumpHwMap.containsKey(key)) {
                cells.get(0).setCellValue(resultSet.getString(2));
                cells.get(1).setCellValue(resultSet.getString(3));

                Hardware hardware = dumpHwMap.get(key);
                if (hardware != null) {
                    cells.get(6).setCellValue(hardware.getWeek());
                    String[] parts = key.split("_");
                    cells.get(2).setCellValue(Integer.valueOf(parts[1]));
                }
                cells.get(3).setCellValue(resultSet.getString(4));
                cells.get(4).setCellValue(resultSet.getString(5));
                cells.get(5).setCellValue(resultSet.getString(6));
                r[0]++;
            }
        }

        // filter hwMap to export empty hw sites
        Map<String, Hardware> missingKeys = dumpHwMap.entrySet().stream().
                filter(x -> !foundKeys.contains(x.getKey()) && x.getKey().contains("SBTS")).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // printing empty hw sites
        missingKeys.forEach((s, hardware) -> {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            cells.get(0).setCellValue(hardware.getCode());
            cells.get(1).setCellValue(hardware.getName());
//            System.out.println(hardware.getCode() + hardware.getCode());
            String[] parts = s.split("_");
            cells.get(2).setCellValue(Integer.valueOf(parts[1]));
            cells.get(6).setCellValue(hardware.getWeek());
            r[0]++;


        });

        sbtsHardwareFileName = sbtsHardwareFileName + weekName + ".xlsx";
        FileOutputStream fileOut = new FileOutputStream(sbtsHardwareFileName);
        //write this workbook to an Outputstream.
        hardwareWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("SBTS hardwarefile is done");

    }

    private void exLte(HashMap<String, Hardware> dumpHwMap, String weekName) throws IOException, SQLException {
        List<String> foundKeys = new ArrayList<>();
        SerialDatabaseSaver serialDatabaseSaver = new SerialDatabaseSaver("C:/Ater/Development/RAN Tool/serials.db");
        ResultSet resultSet = serialDatabaseSaver.load("4G");
        XSSFWorkbook hardwareWb;
        hardwareWb = lambda.prepareWB(lHardwareFileName, "C:/Ater/Development/RAN Tool/4GHWListDb.xlsx");
        int numOfColumns = 8;
        XSSFSheet sheet = hardwareWb.getSheet("Sheet1");
        final int[] r = {1};
        while (resultSet.next()) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            String key = resultSet.getString(1);
            if (dumpHwMap.containsKey(key)) {
                cells.get(0).setCellValue(resultSet.getString(2));
                cells.get(1).setCellValue(resultSet.getString(3));

                Hardware hardware = dumpHwMap.get(key);
                if (hardware != null) {
                    cells.get(6).setCellValue(hardware.getWeek());
                    String[] parts = key.split("_");
                    cells.get(2).setCellValue(Integer.valueOf(parts[1]));
                }
                cells.get(3).setCellValue(resultSet.getString(4));
                cells.get(4).setCellValue(resultSet.getString(5));
                cells.get(5).setCellValue(resultSet.getString(6));
                r[0]++;
            }
        }

        // filter hwMap to export empty hw sites
        Map<String, Hardware> missingKeys = dumpHwMap.entrySet().stream().
                filter(x -> !foundKeys.contains(x.getKey()) && x.getKey().contains("4G")).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // printing empty hw sites
        missingKeys.forEach((s, hardware) -> {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            cells.get(0).setCellValue(hardware.getCode());
            cells.get(1).setCellValue(hardware.getName());
//            System.out.println(hardware.getCode() + hardware.getCode());
            String[] parts = s.split("_");
            cells.get(2).setCellValue(Integer.valueOf(parts[1]));
            cells.get(6).setCellValue(hardware.getWeek());
            r[0]++;


        });

        lHardwareFileName = lHardwareFileName + weekName + ".xlsx";
        FileOutputStream fileOut = new FileOutputStream(lHardwareFileName);
        //write this workbook to an Outputstream.
        hardwareWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("4G hardwarefile is done");

    }

    private void exportGandU(HashMap<String, Hardware> dumpHwMap, String weekName,
                             String optFileName, String iptFileName, int tech) throws IOException, SQLException {
        List<String> foundKeys = new ArrayList<>();

        SerialDatabaseSaver serialDatabaseSaver = new SerialDatabaseSaver("C:/Ater/Development/RAN Tool/serials.db");
        ResultSet resultSet = serialDatabaseSaver.load(tech + "G");
        XSSFWorkbook hardwareWb;
        hardwareWb = lambda.prepareWB(uHardwareFileName, iptFileName);
        int numOfColumns = 8;
        XSSFSheet sheet = hardwareWb.getSheet("Sheet1");
        final int[] r = {1};
        while (resultSet.next()) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            String key = resultSet.getString(1);
            if (dumpHwMap.containsKey(key)) {

                cells.get(0).setCellValue(resultSet.getString(2));
                cells.get(1).setCellValue(resultSet.getString(3));
                Hardware hardware = dumpHwMap.get(key);
                if (hardware != null) {
                    cells.get(7).setCellValue(hardware.getWeek());
                    String[] parts = key.split("_");
                    cells.get(2).setCellValue(Integer.valueOf(parts[1]));
                    cells.get(3).setCellValue(Integer.valueOf(parts[2]));
                }
                cells.get(4).setCellValue(resultSet.getString(4));
                cells.get(5).setCellValue(resultSet.getString(5));
                cells.get(6).setCellValue(resultSet.getString(6));
                foundKeys.add(key);
                r[0]++;
            }
        }
        // filter hwMap to export empty hw sites
        Map<String, Hardware> missingKeys = dumpHwMap.entrySet().stream().
                filter(x -> !foundKeys.contains(x.getKey()) && x.getKey().contains(tech + "G")).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // printing empty hw sites
        missingKeys.forEach((s, hardware) -> {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r[0]);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            cells.get(0).setCellValue(hardware.getCode());
            cells.get(1).setCellValue(hardware.getName());
//                System.out.println(hardware.getCode() + hardware.getCode());
            String[] parts = s.split("_");
            cells.get(2).setCellValue(Integer.valueOf(parts[1]));
            cells.get(3).setCellValue(Integer.valueOf(parts[2]));
            cells.get(7).setCellValue(hardware.getWeek());
            r[0]++;


        });


        optFileName = optFileName + weekName + ".xlsx";
        FileOutputStream fileOut = new FileOutputStream(optFileName);
        //write this workbook to an Outputstream.
        hardwareWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println(tech + "G hardwarefile is done");
    }

//
//    // 3G HW list
//    void export3GHWfromXML(ArrayList<NodeB> nodeBList, String weekName) throws IOException {
//
//        XSSFWorkbook uHardwareWb;
////        uHardwareWb = get3GHWWorkbook();
//        uHardwareWb = lambda.prepareWB("C:/Ater/Development/RAN Tool/3GHWList.xlsx");
//        int numOfColumns = 8;
//        XSSFSheet sheet = uHardwareWb.getSheet("Sheet1");
//        int r = 1;
//        for (NodeB nodeB : nodeBList) {
//            for (HwItem hwItem : nodeB.getHardware().getHwItems()) {
//                ArrayList<XSSFCell> cells = new ArrayList<>();
//                XSSFRow row = sheet.createRow(r);
//                //iterating c number of columns
//                for (int i = 0; i < numOfColumns; i++) {
//                    XSSFCell cell = row.createCell(i);
//                    cells.add(i, cell);
//                }
//                cells.get(0).setCellValue(nodeB.getCode());
//                cells.get(1).setCellValue(nodeB.getCode());
//                cells.get(2).setCellValue(Integer.valueOf(nodeB.getRncId()));
//                cells.get(3).setCellValue(Integer.valueOf(nodeB.getWbtsId()));
//                cells.get(4).setCellValue(hwItem.getUserLabel());
//                cells.get(5).setCellValue(hwItem.getSerialNumber());
//                cells.get(6).setCellValue(hwItem.getIdentificationCode());
//                cells.get(7).setCellValue(nodeB.getHardware().getWeek());
//
//                r++;
////                }
//            }
//        }
//        uHardwareFileName = uHardwareFileName + weekName + ".xlsx";
//        FileOutputStream fileOut = new FileOutputStream(uHardwareFileName);
//        //write this workbook to an Outputstream.
//        uHardwareWb.write(fileOut);
//        fileOut.flush();
//        fileOut.close();
//        System.out.println("3G hardwarefile is done");
//    }
//
//    // 4G HW list
//    void export4GHWfromXML(ArrayList<EnodeB> lSitesList, String weekName) throws IOException {
//
//        XSSFWorkbook lHardwareWb;
////        lHardwareWb = get4GHWWorkbook();
//        lHardwareWb = lambda.prepareWB("C:/Ater/Development/RAN Tool/4GHWList.xlsx");
//        int numOfColumns = 7;
//        XSSFSheet sheet = lHardwareWb.getSheet("Sheet1");
//        int r = 1;
//        for (EnodeB enodeB : lSitesList) {
//            for (HwItem hwItem : enodeB.getHardware().getHwItems()) {
//                ArrayList<XSSFCell> cells = new ArrayList<>();
//                XSSFRow row = sheet.createRow(r);
//                //iterating c number of columns
//                for (int i = 0; i < numOfColumns; i++) {
//                    XSSFCell cell = row.createCell(i);
//                    cells.add(i, cell);
//                }
//                cells.get(0).setCellValue(enodeB.getENodeBCode());
//                cells.get(1).setCellValue(enodeB.getENodeBName());
//                cells.get(2).setCellValue(Integer.valueOf(enodeB.getENodeBId()));
//                cells.get(3).setCellValue(hwItem.getUserLabel());
//                cells.get(4).setCellValue(hwItem.getSerialNumber());
//                cells.get(5).setCellValue(hwItem.getIdentificationCode());
//                cells.get(6).setCellValue(enodeB.getHardware().getWeek());
//                r++;
//            }
//        }
//        lHardwareFileName = lHardwareFileName + weekName + ".xlsx";
//        FileOutputStream fileOut = new FileOutputStream(lHardwareFileName);
//        //write this workbook to an Outputstream.
//        lHardwareWb.write(fileOut);
//        fileOut.flush();
//        fileOut.close();
//        System.out.println("4G hardwarefile is done");
//    }


}



