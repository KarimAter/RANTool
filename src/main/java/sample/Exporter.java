package sample;

import Helpers.DatabaseConnector;
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

class Exporter {

    static String[] carrierHeader = {"Code", "Name", "Rnc"};
    static String orginalFileName = "D:\\RAN Tool\\Dashboard.xlsx";
    static String updatedFileName = "D:\\RAN Tool\\Dashboard.xlsx";
    static String TRX1FileName;
    static String TRX2FileName;
    static String uCells1FileName;
    static String uCells2FileName;
    static String changesFileName = "C:\\Users\\Ater\\Desktop\\NetworkChanges.xlsx";
    static String gHardwareFileName = "C:\\Users\\Ater\\Desktop\\2GHW";
    static String uHardwareFileName = "C:\\Users\\Ater\\Desktop\\3GHW";
    static String lHardwareFileName = "C:\\Users\\Ater\\Desktop\\4GHW";
    XSSFWorkbook wb;
    XSSFWorkbook changesWrb;

    Exporter(String weekName) {
        wb = prepareWorkbooks(orginalFileName);
        updatedFileName = prepareDashboardName(weekName);
    }

    Exporter() {
        wb = prepareWorkbooks(updatedFileName);
    }

    void exportBcfList(ArrayList<Cabinet> bcfList) throws IOException {
        ZipSecureFile.setMinInflateRatio(0);
        int numOfColumns = 23;
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
            cells.get(6).setCellValue(value.getOnAir() == 1 && value.getNumberOfOnAirCells() > 0);
            cells.get(7).setCellValue(bcf.getNumberOfTRXs());
            cells.get(8).setCellValue(bcf.getNumberOfCells());
            cells.get(9).setCellValue(bcf.getNumberOfOnAirCells());
            cells.get(10).setCellValue(bcf.getNumberOfDcsCells());
            cells.get(11).setCellValue(bcf.getNumberOfGsmCells());
            cells.get(12).setCellValue(bcf.getTxMode());
            cells.get(13).setCellValue(bcf.getNumberOfE1s());
            cells.get(14).setCellValue(bcf.getNumberOfGTRXs());
            cells.get(15).setCellValue(Integer.valueOf(bcf.getLac()));
            cells.get(16).setCellValue(Integer.valueOf(bcf.getRac()));
            cells.get(17).setCellValue(bcf.getVersion());
            cells.get(18).setCellValue(bcf.getCtrlIp());
            cells.get(19).setCellValue(bcf.getManIp());
            try {
                cells.get(20).setCellValue(Integer.valueOf(bcf.getUsedETP()));
            } catch (NumberFormatException e) {
                cells.get(20).setCellValue(bcf.getUsedETP());
            }
            try {
                cells.get(21).setCellValue(Integer.valueOf(bcf.getgConf()));
            } catch (Exception e) {
                cells.get(21).setCellValue(bcf.getgConf());
            }
            try {
                cells.get(22).setCellValue(Integer.valueOf(bcf.getdConf()));
            } catch (Exception e) {
                cells.get(22).setCellValue(bcf.getdConf());
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
        int numOfColumns = 21;
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
            Hardware gHardware = bcf.getHardware();

            cells.get(5).setCellValue(gHardware.getRfString());
            cells.get(6).setCellValue(gHardware.getSmString());
            cells.get(7).setCellValue(gHardware.getTxString());
            cells.get(8).setCellValue(gHardware.getWeek());

            cells.get(9).setCellValue(gHardware.getModuleValue("ESMB"));
            cells.get(10).setCellValue(gHardware.getModuleValue("ESMC"));
            cells.get(11).setCellValue(gHardware.getModuleValue("FIQA"));
            cells.get(12).setCellValue(gHardware.getModuleValue("FIQB"));
            cells.get(13).setCellValue(gHardware.getModuleValue("FSMF"));
            cells.get(14).setCellValue(gHardware.getModuleValue("FTIF"));
            cells.get(15).setCellValue(gHardware.getModuleValue("FXDA"));
            cells.get(16).setCellValue(gHardware.getModuleValue("FXDB"));
            cells.get(17).setCellValue(gHardware.getModuleValue("FXEA"));
            cells.get(18).setCellValue(gHardware.getModuleValue("FXEB"));
            cells.get(19).setCellValue(gHardware.getModuleValue("FXEF"));
            cells.get(20).setCellValue(gHardware.getModuleValue("FXED"));
//            cells.get(9).setCellValue(gHardware.ESMB);
//            cells.get(10).setCellValue(gHardware.ESMC);
//            cells.get(11).setCellValue(gHardware.FIQA);
//            cells.get(12).setCellValue(gHardware.FIQB);
//            cells.get(13).setCellValue(gHardware.FSMF);
//            cells.get(14).setCellValue(gHardware.FTIF);
//            cells.get(15).setCellValue(gHardware.FXDA);
//            cells.get(16).setCellValue(gHardware.FXDB);
//            cells.get(17).setCellValue(gHardware.FXEA);
//            cells.get(18).setCellValue(gHardware.FXEB);
//            cells.get(19).setCellValue(gHardware.FXX);
//            cells.get(20).setCellValue(gHardware.FXED);

            r[0]++;
        });

        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }


    void exportNodeBList(ArrayList<Cabinet> nodeBList) throws IOException {
        int numOfColumns = 36;
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
//            cells.get(0).setCellValue(nodeB.getRegion());
//            try {
//                cells.get(1).setCellValue(Integer.valueOf(nodeB.getRncId()));
//                cells.get(4).setCellValue(Integer.valueOf(nodeB.getWbtsId()));
//            } catch (Exception e) {
//                cells.get(1).setCellValue("");
//                cells.get(4).setCellValue("");
//            }
            // Todo: add key in all
            NodeB nodeB = (NodeB) cabinet;
            cells.get(0).setCellValue(nodeB.getCode());
            cells.get(1).setCellValue(nodeB.getName());
            cells.get(2).setCellValue(nodeB.getRegion());
            cells.get(3).setCellValue(Integer.valueOf(nodeB.getRncId()));
            cells.get(4).setCellValue(Integer.valueOf(nodeB.getWbtsId()));
            cells.get(5).setCellValue(nodeB.getKey());
            cells.get(6).setCellValue(nodeB.getNumberOfCells());
            cells.get(7).setCellValue(nodeB.getNumberOfOnAirCells());
            cells.get(8).setCellValue(nodeB.getNumberOfFirstCarriersCells());
            cells.get(9).setCellValue(nodeB.getNumberOfOnAirFirstCarriersCells());
            cells.get(10).setCellValue(nodeB.getNumberOfSecondCarriersCells());
            cells.get(11).setCellValue(nodeB.getNumberOfOnAirSecondCarriersCells());
            cells.get(12).setCellValue(nodeB.getNumberOfThirdCarriersCells());
            cells.get(13).setCellValue(nodeB.getNumberOfOnAirThirdCarriersCells());
            cells.get(14).setCellValue(nodeB.getNumberOfFirstU900Cells());
            cells.get(15).setCellValue(nodeB.getNumberOfOnAirFirstU900Cells());
            cells.get(16).setCellValue(nodeB.getNumberOfSecondU900Cells());
            cells.get(17).setCellValue(nodeB.getNumberOfOnAirSecondU900Cells());
            cells.get(18).setCellValue(nodeB.getVersion());
            cells.get(19).setCellValue(nodeB.getTxMode());
            cells.get(20).setCellValue(nodeB.getNumberOfHSDPASet1());
            cells.get(21).setCellValue(nodeB.getNumberOfHSDPASet2());
            cells.get(22).setCellValue(nodeB.getNumberOfHSDPASet3());
            cells.get(23).setCellValue(nodeB.getNumberOfHSUPASet1());
            cells.get(24).setCellValue(nodeB.getNumberOfChannelElements());
            cells.get(25).setCellValue(nodeB.getNumberOfE1s());
            cells.get(26).setCellValue(nodeB.getSectors());
            cells.get(27).setCellValue(nodeB.getNumberOfCarriers());
            cells.get(28).setCellValue(nodeB.isStandAloneU900());
            cells.get(29).setCellValue(nodeB.isRfSharing());
            cells.get(30).setCellValue(nodeB.getPower());
            cells.get(31).setCellValue(nodeB.getU900Power());
            cells.get(32).setCellValue(Integer.valueOf(nodeB.getLac()));
            cells.get(33).setCellValue(Integer.valueOf(nodeB.getRac()));
            cells.get(34).setCellValue(nodeB.getNodeBIP());
            try {
                cells.get(35).setCellValue(Integer.valueOf(nodeB.getSfp()));
            } catch (NumberFormatException e) {
                cells.get(35).setCellValue(nodeB.getSfp());
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
        int numOfColumns = 29;
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
            cells.get(5).setCellValue(nHardware.getWeek());
            cells.get(6).setCellValue(nHardware.getRfString());
            cells.get(7).setCellValue(nHardware.getSmString());
            cells.get(8).setCellValue(nHardware.getTxString());
            // todo: resume module values for all techs
            cells.get(9).setCellValue(nHardware.getModuleValue("FBBA"));
            cells.get(10).setCellValue(nHardware.getModuleValue("FRGC"));
            cells.get(11).setCellValue(nHardware.getModuleValue("FRGD"));
            cells.get(12).setCellValue(nHardware.getModuleValue("FRGF"));
            cells.get(13).setCellValue(nHardware.getModuleValue("FRGL"));
            cells.get(14).setCellValue(nHardware.getModuleValue("FRGM"));
            cells.get(15).setCellValue(nHardware.getModuleValue("FRGP"));
            cells.get(16).setCellValue(nHardware.getModuleValue("FRGT"));
            cells.get(17).setCellValue(nHardware.getModuleValue("FRGU"));
            cells.get(18).setCellValue(nHardware.getModuleValue("FRGX"));
            cells.get(19).setCellValue(nHardware.getModuleValue("FSMB"));
            cells.get(20).setCellValue(nHardware.getModuleValue("FSMD"));
            cells.get(21).setCellValue(nHardware.getModuleValue("FSME"));
            cells.get(22).setCellValue(nHardware.getModuleValue("FSMF"));
            cells.get(23).setCellValue(nHardware.getModuleValue("FTIA"));
            cells.get(24).setCellValue(nHardware.getModuleValue("FTIB"));
            cells.get(25).setCellValue(nHardware.getModuleValue("FTIF"));
            cells.get(26).setCellValue(nHardware.getModuleValue("FTPB"));
            cells.get(27).setCellValue(nHardware.getModuleValue("FXDA"));
            cells.get(28).setCellValue(nHardware.getModuleValue("FXDB"));
//            cells.get(9).setCellValue(nHardware.FBBA);
//            cells.get(10).setCellValue(nHardware.FRGC);
//            cells.get(11).setCellValue(nHardware.FRGD);
//            cells.get(12).setCellValue(nHardware.FRGF);
//            cells.get(13).setCellValue(nHardware.FRGL);
//            cells.get(14).setCellValue(nHardware.FRGM);
//            cells.get(15).setCellValue(nHardware.FRGP);
//            cells.get(16).setCellValue(nHardware.FRGT);
//            cells.get(17).setCellValue(nHardware.FRGU);
//            cells.get(18).setCellValue(nHardware.FRGX);
//            cells.get(19).setCellValue(nHardware.FSMB);
//            cells.get(20).setCellValue(nHardware.FSMD);
//            cells.get(21).setCellValue(nHardware.FSME);
//            cells.get(22).setCellValue(nHardware.FSMF);
//            cells.get(23).setCellValue(nHardware.FTIA);
//            cells.get(24).setCellValue(nHardware.FTIB);
//            cells.get(25).setCellValue(nHardware.FTIF);
//            cells.get(26).setCellValue(nHardware.FTPB);
//            cells.get(27).setCellValue(nHardware.FXDA);
//            cells.get(28).setCellValue(nHardware.FXDB);
            r[0]++;
        });
        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

//    void export2GSitesList(ArrayList<GSite> sitesList, String sites) throws IOException {
//        ZipSecureFile.setMinInflateRatio(0);
//        int numOfColumns = 16;
//        XSSFSheet sheet = wb.getSheet(sites);
//        int r = 1;
//        for (GSite site : sitesList) {
//            ArrayList<XSSFCell> cells = new ArrayList<>();
//            XSSFRow row = sheet.createRow(r);
//            //iterating c number of columns
//            for (int i = 0; i < numOfColumns; i++) {
//                XSSFCell cell = row.createCell(i);
//                cells.add(i, cell);
//            }
//            cells.get(0).setCellValue(site.getRegion());
//            cells.get(1).setCellValue(site.getSiteBSCName());
//            cells.get(2).setCellValue(site.getSiteName());
//            cells.get(3).setCellValue(site.getSiteCode());
//            cells.get(4).setCellValue(site.getSiteNumberOfBCFs());
//            cells.get(5).setCellValue(site.getSiteNumberOfTRXs());
//            cells.get(6).setCellValue(site.getSiteNumberOfCells());
//            cells.get(7).setCellValue(site.getSiteNumberOfOnAirCells());
//            cells.get(8).setCellValue(site.getSiteNumberOfDcsCells());
//            cells.get(9).setCellValue(site.getSiteNumberOfGsmCells());
//            cells.get(10).setCellValue(site.getSiteTxMode());
//            cells.get(11).setCellValue(site.getSiteNumberOfE1s());
//            cells.get(12).setCellValue(site.getSiteNumberOfGTRXs());
//            cells.get(13).setCellValue(site.getLac());
//            cells.get(14).setCellValue(site.getRac());
//            cells.get(15).setCellValue(site.isOnAir());
//            r++;
//        }
//        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
//        //write this workbook to an Outputstream.
//        wb.write(fileOut);
//        fileOut.flush();
//        fileOut.close();
////        wb.close();
//    }

    void export3GSitesList(ArrayList<USite> sitesList, String sites) throws IOException {

        int numOfColumns = 32;
        XSSFSheet sheet = wb.getSheet(sites);
        int r = 1;
        for (USite site : sitesList) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            cells.get(0).setCellValue(site.getSiteRegion());
            try {
                cells.get(1).setCellValue(Integer.valueOf(site.getSiteRncId()));
                cells.get(4).setCellValue(Integer.valueOf(site.getSiteWbtsId()));
            } catch (Exception e) {
                cells.get(1).setCellValue("");
                cells.get(4).setCellValue("");
            }
            cells.get(2).setCellValue(site.getSiteName());
            cells.get(3).setCellValue(site.getSiteCode());
            cells.get(5).setCellValue(site.getSiteNumberOfCells());
            cells.get(6).setCellValue(site.getSiteNumberOfOnAirCells());
            cells.get(7).setCellValue(site.getSiteNumberOfFirstCarriersCells());
            cells.get(8).setCellValue(site.getSiteNumberOfOnAirFirstCarriersCells());
            cells.get(9).setCellValue(site.getSiteNumberOfSecondCarriersCells());
            cells.get(10).setCellValue(site.getSiteNumberOfOnAirSecondCarriersCells());
            cells.get(11).setCellValue(site.getSiteNumberOfThirdCarriersCells());
            cells.get(12).setCellValue(site.getSiteNumberOfOnAirThirdCarriersCells());
            cells.get(13).setCellValue(site.getSiteNumberOfU900CarriersCells());
            cells.get(14).setCellValue(site.getSiteNumberOfOnAirU900CarriersCells());
            cells.get(15).setCellValue(site.getSiteVersion());
            cells.get(16).setCellValue(site.getSiteTxMode());
            cells.get(17).setCellValue(site.getSiteNumberOfHSDPASet1());
            cells.get(18).setCellValue(site.getSiteNumberOfHSDPASet2());
            cells.get(19).setCellValue(site.getSiteNumberOfHSDPASet3());
            cells.get(20).setCellValue(site.getSiteNumberOfHSUPASet1());
            cells.get(21).setCellValue(site.getSiteNumberOfChannelElements());
            cells.get(22).setCellValue(site.getSiteNumberOfE1s());
            cells.get(23).setCellValue(site.getSiteNumberOfNodeBs());
            cells.get(24).setCellValue(site.getSiteNumberOfSectors());
            cells.get(25).setCellValue(site.getSiteNumberOfCarriers());
            cells.get(26).setCellValue(site.isStandAloneU900());
            cells.get(27).setCellValue(site.isRfSharing());
            cells.get(28).setCellValue(site.getSitePower());
            cells.get(29).setCellValue(site.getSiteU900Power());
            cells.get(30).setCellValue(site.getLac());
            cells.get(31).setCellValue(site.getRac());
            r++;
        }
        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
//        wb.close();
        System.out.println("3G Site list done..");
    }

    void export4GSitesList(ArrayList<EnodeB> lSitesList) throws IOException {

        int numOfColumns = 14;
        XSSFSheet sheet = wb.getSheet("LTE");
        int r = 1;
        for (EnodeB site : lSitesList) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            cells.get(0).setCellValue(site.getENodeBCode());
            cells.get(1).setCellValue(site.getENodeBName());
            cells.get(2).setCellValue(Integer.valueOf(site.getENodeBId()));
            cells.get(3).setCellValue(site.getENodeBRegion());
            cells.get(4).setCellValue(site.getENodeBNumberOfCells());
            cells.get(5).setCellValue(site.getENodeBNumberOfOnAirCells());
            cells.get(6).setCellValue(site.getENodeBVersion());
            cells.get(7).setCellValue(site.getENodeBBW());
            cells.get(8).setCellValue(site.getENodeBMimo());
            cells.get(10).setCellValue(site.getManIp());
            cells.get(11).setCellValue(site.getS1Ip());
            cells.get(12).setCellValue(site.getSecIp());
            cells.get(13).setCellValue(site.getSecGw());
            try {
                cells.get(9).setCellValue(Integer.valueOf(site.getTac()));
            } catch (NumberFormatException e) {
                cells.get(9).setCellValue(site.getTac());
            }

            r++;
        }
        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("4G Site list done..");
//        wb.close();
    }


//    void export2GHardWare(ArrayList<GSite> sitesList, String sheetName) throws IOException {
//        int numOfColumns = 17;
//        XSSFSheet sheet = wb.getSheet(sheetName);
//        int r = 1;
//
//        for (GSite site : sitesList) {
//            ArrayList<XSSFCell> cells = new ArrayList<>();
//            XSSFRow row = sheet.createRow(r);
//            //iterating c number of columns
//            for (int i = 0; i < numOfColumns; i++) {
//                XSSFCell cell = row.createCell(i);
//                cells.add(i, cell);
//            }
//            GSite.GHardware gHardware = site.getGHardware();
//            cells.get(0).setCellValue(site.getRegion());
//            cells.get(1).setCellValue(site.getSiteName());
//            cells.get(2).setCellValue(site.getSiteCode());
//            cells.get(3).setCellValue(gHardware.ESMB);
//            cells.get(4).setCellValue(gHardware.ESMC);
//            cells.get(5).setCellValue(gHardware.FIQA);
//            cells.get(6).setCellValue(gHardware.FIQB);
//            cells.get(7).setCellValue(gHardware.FSMF);
//            cells.get(8).setCellValue(gHardware.FTIF);
//            cells.get(9).setCellValue(gHardware.FXDA);
//            cells.get(10).setCellValue(gHardware.FXDB);
//            cells.get(11).setCellValue(gHardware.FXEA);
//            cells.get(12).setCellValue(gHardware.FXEB);
//            cells.get(13).setCellValue(gHardware.FXX);
//            cells.get(14).setCellValue(gHardware.FXED);
//            cells.get(15).setCellValue(gHardware.rfString);
//            cells.get(16).setCellValue(gHardware.smString);
//            r++;
//        }
//
//        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
//        //write this workbook to an Outputstream.
//        wb.write(fileOut);
//        fileOut.flush();
//        fileOut.close();
//    }

    void export3GHardWare(ArrayList<USite> sitesList, String sheetName) throws IOException {
        int numOfColumns = 25;
        XSSFSheet sheet = wb.getSheet(sheetName);
        int r = 1;

        for (USite site : sitesList) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            UHardware uHardware = site.getUHardware();
            cells.get(0).setCellValue(site.getSiteRegion());
            cells.get(1).setCellValue(site.getSiteName());
            cells.get(2).setCellValue(site.getSiteCode());
            cells.get(3).setCellValue(uHardware.FBBA);
            cells.get(4).setCellValue(uHardware.FRGC);
            cells.get(5).setCellValue(uHardware.FRGD);
            cells.get(6).setCellValue(uHardware.FRGF);
            cells.get(7).setCellValue(uHardware.FRGL);
            cells.get(8).setCellValue(uHardware.FRGM);
            cells.get(9).setCellValue(uHardware.FRGP);
            cells.get(10).setCellValue(uHardware.FRGT);
            cells.get(11).setCellValue(uHardware.FRGU);
            cells.get(12).setCellValue(uHardware.FRGX);
            cells.get(13).setCellValue(uHardware.FSMB);
            cells.get(14).setCellValue(uHardware.FSMD);
            cells.get(15).setCellValue(uHardware.FSME);
            cells.get(16).setCellValue(uHardware.FSMF);
            cells.get(17).setCellValue(uHardware.FTIA);
            cells.get(18).setCellValue(uHardware.FTIB);
            cells.get(19).setCellValue(uHardware.FTIF);
            cells.get(20).setCellValue(uHardware.FTPB);
            cells.get(21).setCellValue(uHardware.FXDA);
            cells.get(22).setCellValue(uHardware.FXDB);
            cells.get(23).setCellValue(uHardware.rfString);
            cells.get(24).setCellValue(uHardware.smString);
            r++;
        }

        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    void exportCarrierList(String dump3R1Path, String dump3R2Path, String sheetName) throws IOException {
        int numOfColumns = carrierHeader.length;
        XSSFSheet sheet = wb.getSheet(sheetName);
        DatabaseConnector databaseConnector1 = new DatabaseConnector(dump3R1Path);
        DatabaseConnector databaseConnector2 = new DatabaseConnector(dump3R2Path);
        ArrayList<USite> carrierList;
        try {
            if (sheetName.equals("3rd Carrier")) {
                carrierList = databaseConnector1.getThirdCarrierSites(new ArrayList<>());
                carrierList = databaseConnector2.getThirdCarrierSites(carrierList);
            } else {
                carrierList = databaseConnector1.getU900List(new ArrayList<>());
                carrierList = databaseConnector2.getU900List(carrierList);
            }

            int r = 1;
            for (USite site : carrierList) {
                ArrayList<XSSFCell> cells = new ArrayList<>(4);
                XSSFRow row = sheet.createRow(r);
                //iterating c number of columns
                for (int i = 0; i < numOfColumns; i++) {
                    XSSFCell cell = row.createCell(i);
                    cells.add(i, cell);
                }
                cells.get(0).setCellValue(site.getSiteCode());
                cells.get(1).setCellValue(site.getSiteName());
                cells.get(2).setCellValue(Integer.valueOf(site.getSiteRncId()));
                r++;
            }

            FileOutputStream fileOut = new FileOutputStream(updatedFileName);
            //write this workbook to an Outputstream.
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
            System.out.println(sheetName + " list done..");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void export4GHardWare(ArrayList<EnodeB> sitesList) throws IOException {
        int numOfColumns = 15;
        XSSFSheet sheet = wb.getSheet("4G HW");
        int r = 1;

        for (EnodeB site : sitesList) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            LHardware lHardware = site.getLHardware();
            cells.get(0).setCellValue(site.getENodeBCode());
            cells.get(1).setCellValue(site.getENodeBName());
            cells.get(2).setCellValue(Integer.valueOf(site.getENodeBId()));
            cells.get(3).setCellValue(site.getENodeBRegion());


            Hardware hardware = site.getHardware();
            cells.get(4).setCellValue(hardware.getRfString());
            cells.get(5).setCellValue(hardware.getSmString());
            //todo:tx string
            cells.get(6).setCellValue(hardware.getWeek());


            cells.get(7).setCellValue(hardware.getModuleValue("FBBA"));
//            cells.get(8).setCellValue(lHardware.FBBC);
//            cells.get(9).setCellValue(lHardware.FRGT);
//            cells.get(10).setCellValue(lHardware.FSMF);
//            cells.get(11).setCellValue(lHardware.FSPD);
//            cells.get(12).setCellValue(lHardware.FTIF);
//            cells.get(13).setCellValue(lHardware.FXEB);
            cells.get(14).setCellValue(hardware.getModuleValue("FXED"));


//            cells.get(4).setCellValue(lHardware.rfString);
//            cells.get(5).setCellValue(lHardware.smString);
//            cells.get(6).setCellValue(lHardware.getWeek());
//            cells.get(7).setCellValue(lHardware.FBBA);
//            cells.get(8).setCellValue(lHardware.FBBC);
//            cells.get(9).setCellValue(lHardware.FRGT);
//            cells.get(10).setCellValue(lHardware.FSMF);
//            cells.get(11).setCellValue(lHardware.FSPD);
//            cells.get(12).setCellValue(lHardware.FTIF);
//            cells.get(13).setCellValue(lHardware.FXEB);
//            cells.get(14).setCellValue(lHardware.FXED);
            r++;
        }
        FileOutputStream fileOut = new FileOutputStream(updatedFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        wb.close();
    }

    XSSFWorkbook prepareWorkbooks(String path) {
        File file = new File(path);
//        prepareDashboardName(weekName);
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

    private String prepareDashboardName(String weekName) {
        return "C:\\Users\\Ater\\Desktop\\Dashboard " + weekName + ".xlsx";
    }

    private void prepareTrxSheetNames(String weekName) {
        TRX1FileName = "C:\\Users\\Ater\\Desktop\\TRXSheet1" + weekName + ".xlsx";
        TRX2FileName = "C:\\Users\\Ater\\Desktop\\TRXSheet2" + weekName + ".xlsx";

    }

    private void prepare3GcellsSheetNames(String weekName) {
        uCells1FileName = "C:\\Users\\Ater\\Desktop\\3G Cells1" + weekName + ".xlsx";
        uCells2FileName = "C:\\Users\\Ater\\Desktop\\3G Cells2" + weekName + ".xlsx";
    }

    private XSSFWorkbook get2GHWWorkbook() throws IOException {
        File file = new File("D:\\RAN Tool\\2GHWList.xlsx");
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

    getWorkbook lambda = (templatePath) -> {
        File file = new File(templatePath);
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
    };

    private interface getWorkbook {
        XSSFWorkbook prepareWB(String templatePath) throws IOException;

    }

    private XSSFWorkbook get3GHWWorkbook() throws IOException {
        File file = new File("D:\\RAN Tool\\3GHWList.xlsx");
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

    private XSSFWorkbook get4GHWWorkbook() throws IOException {
        File file = new File("D:\\RAN Tool\\4GHWList.xlsx");
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

    private XSSFWorkbook getTRXWorkbook() throws IOException {
        File file = new File("D:\\RAN Tool\\Dashboard_TRX1.xlsx");
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
        File file = new File("D:\\RAN Tool\\3G Cells.xlsx");
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
        DatabaseConnector databaseConnector1 = new DatabaseConnector(dump2R1Path);
        DatabaseConnector databaseConnector2 = new DatabaseConnector(dump2R2Path);
        ResultSet trxResultSet1 = databaseConnector1.getTRXSheet();
        ResultSet trxResultSet2 = databaseConnector2.getTRXSheet();
        prepareTrxSheetNames(weekName);
        exportTrx(trxResultSet1, TRX1FileName);
        exportTrx(trxResultSet2, TRX2FileName);


    }

    private void exportTrx(ResultSet resultSet, String trxFileName) throws IOException, SQLException {
        XSSFSheet sheet1;
        XSSFWorkbook trxWb;
//        trxWb = getTRXWorkbook();
        trxWb = lambda.prepareWB("D:\\RAN Tool\\Dashboard_TRX1.xlsx");
        sheet1 = trxWb.getSheet("TRX");
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
            String siteName = resultSet.getString(10);
            cells.get(0).setCellValue(Integer.valueOf(resultSet.getString(11)));
            cells.get(1).setCellValue(siteName);
            cells.get(2).setCellValue(Utils.extractSiteCode(siteName));
            cells.get(3).setCellValue(Integer.valueOf(resultSet.getString(1)));
            cells.get(4).setCellValue(Integer.valueOf(resultSet.getString(2)));
            cells.get(5).setCellValue(Integer.valueOf(resultSet.getString(3)));
            cells.get(6).setCellValue(Integer.valueOf(resultSet.getString(4)));
            cells.get(7).setCellValue(Integer.valueOf(resultSet.getString(5)));
            cells.get(8).setCellValue(Integer.valueOf(resultSet.getString(6)));
            cells.get(9).setCellValue(resultSet.getString(7));
            cells.get(10).setCellValue(Integer.valueOf(resultSet.getString(8)));
            cells.get(11).setCellValue(Integer.valueOf(resultSet.getString(9)));
            if (resultSet.getInt(12) == 0)
                cells.get(12).setCellValue("GSM");
            else cells.get(12).setCellValue("DCS");
            r++;
        }
        FileOutputStream fileOut;
        fileOut = new FileOutputStream(trxFileName);
        trxWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        trxWb.close();
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

    private static void export3GCells(ResultSet resultSet, String uCellsFileName) throws IOException, SQLException {
        XSSFSheet sheet1;
        XSSFWorkbook uCellsWb;
        uCellsWb = getUcellsWorkbook();
        sheet1 = uCellsWb.getSheet("Cells");
        int numOfColumns = 11;
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
            cells.get(4).setCellValue(Integer.valueOf(resultSet.getString(5)));
            cells.get(5).setCellValue(Integer.valueOf(resultSet.getString(6)));
            cells.get(6).setCellValue(Integer.valueOf(resultSet.getString(7)));
            cells.get(7).setCellValue(Integer.valueOf(resultSet.getString(8)));
            cells.get(8).setCellValue(Integer.valueOf(resultSet.getString(9)));
            cells.get(9).setCellValue(Integer.valueOf(resultSet.getString(10)));
            cells.get(10).setCellValue(resultSet.getString(11));

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
    void export2GHWfromXML(ArrayList<BCF> bcfs, String weekName) throws IOException {
        XSSFWorkbook gHardwareWb;
//        gHardwareWb = get2GHWWorkbook();
        gHardwareWb = lambda.prepareWB("D:\\RAN Tool\\2GHWList.xlsx");
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
                cells.get(2).setCellValue(Integer.valueOf(bcf.getBscId()));
                cells.get(3).setCellValue(Integer.valueOf(bcf.getBcfId()));
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

    // 3G HW list
    void export3GHWfromXML(ArrayList<NodeB> nodeBList, String weekName) throws IOException {

        XSSFWorkbook uHardwareWb;
//        uHardwareWb = get3GHWWorkbook();
        uHardwareWb = lambda.prepareWB("D:\\RAN Tool\\3GHWList.xlsx");
        int numOfColumns = 8;
        XSSFSheet sheet = uHardwareWb.getSheet("Sheet1");
        int r = 1;
        for (NodeB nodeB : nodeBList) {
            for (HwItem hwItem : nodeB.getHardware().getHwItems()) {
                ArrayList<XSSFCell> cells = new ArrayList<>();
                XSSFRow row = sheet.createRow(r);
                //iterating c number of columns
                for (int i = 0; i < numOfColumns; i++) {
                    XSSFCell cell = row.createCell(i);
                    cells.add(i, cell);
                }
                cells.get(0).setCellValue(nodeB.getCode());
                cells.get(1).setCellValue(nodeB.getName());
                cells.get(2).setCellValue(Integer.valueOf(nodeB.getRncId()));
                cells.get(3).setCellValue(Integer.valueOf(nodeB.getWbtsId()));
                cells.get(4).setCellValue(hwItem.getUserLabel());
                cells.get(5).setCellValue(hwItem.getSerialNumber());
                cells.get(6).setCellValue(hwItem.getIdentificationCode());
                cells.get(7).setCellValue(nodeB.getHardware().getWeek());

                r++;
//                }
            }
        }
        uHardwareFileName = uHardwareFileName + weekName + ".xlsx";
        FileOutputStream fileOut = new FileOutputStream(uHardwareFileName);
        //write this workbook to an Outputstream.
        uHardwareWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("3G hardwarefile is done");
    }

    // 4G HW list
    void export4GHWfromXML(ArrayList<EnodeB> lSitesList, String weekName) throws IOException {

        XSSFWorkbook lHardwareWb;
//        lHardwareWb = get4GHWWorkbook();
        lHardwareWb = lambda.prepareWB("D:\\RAN Tool\\4GHWList.xlsx");
        int numOfColumns = 7;
        XSSFSheet sheet = lHardwareWb.getSheet("Sheet1");
        int r = 1;
        for (EnodeB enodeB : lSitesList) {
            for (HwItem hwItem : enodeB.getLHardware().hwItems) {
                ArrayList<XSSFCell> cells = new ArrayList<>();
                XSSFRow row = sheet.createRow(r);
                //iterating c number of columns
                for (int i = 0; i < numOfColumns; i++) {
                    XSSFCell cell = row.createCell(i);
                    cells.add(i, cell);
                }
                cells.get(0).setCellValue(enodeB.getENodeBCode());
                cells.get(1).setCellValue(enodeB.getENodeBName());
                cells.get(2).setCellValue(Integer.valueOf(enodeB.getENodeBId()));
                cells.get(3).setCellValue(hwItem.getUserLabel());
                cells.get(4).setCellValue(hwItem.getSerialNumber());
                cells.get(5).setCellValue(hwItem.getIdentificationCode());
                cells.get(6).setCellValue(enodeB.getLHardware().getWeek());
                r++;
            }
        }
        lHardwareFileName = lHardwareFileName + weekName + ".xlsx";
        FileOutputStream fileOut = new FileOutputStream(lHardwareFileName);
        //write this workbook to an Outputstream.
        lHardwareWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("4G hardwarefile is done");
    }


}



