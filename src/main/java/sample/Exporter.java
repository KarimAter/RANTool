package sample;

import Helpers.Utils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

class Exporter {

    static String[] header = {"RNC", "WBTS", "cells", "onAir", "first", "onFirst", "second", "onSecond", "third", "onThird", "u900", "onU900", "name",
            "Tx Mode", "Version", "HD1", "HD2", "HD3", "HU1", "R99", "E1s"};
    static String[] uSiteHeader = {"Region", "RNC", "name", "SiteCode", "WBTS", "Cells", "OnAir", "first", "onFirst", "second", "onSecond", "third", "onThird", "u900", "onU900",
            "Version", "Tx Mode", "HD1", "HD2", "HD3", "HU1", "R99", "E1s", "NodeBs", "Sectors", "Carriers", "U900StandAlone"
            , "RfSharing"};
    static String[] gSiteHeader = {"Region", "BSC", "Name", "Code", "BCF Count", "TRX Count", "Cells Count", "OnAir Cells", "DCS Cells", "GSM Cells",
            "TX Mode", "E1s", "GTRXs"};
    static String[] lSiteHeader = {"Region", "Name", "Code", "Id", "Cells", "OnAir", "Version", "BW", "Mimo"};
    static String[] carrierHeader = {"Code", "Name", "Rnc"};
    static String excelFileName = "C:\\Users\\Ater\\Desktop\\Dashboard.xlsx";
    static String TRX1FileName = "C:\\Users\\Ater\\Desktop\\Dashboard_TRX1.xlsx";
    static String TRX2FileName = "C:\\Users\\Ater\\Desktop\\Dashboard_TRX2.xlsx";
    static String uCells1FileName = "C:\\Users\\Ater\\Desktop\\3G Cells1.xlsx";
    static String uCells2FileName = "C:\\Users\\Ater\\Desktop\\3G Cells2.xlsx";
    static String changesFileName = "C:\\Users\\Ater\\Desktop\\NetworkChanges.xlsx";
    static String uHardwareFileName = "C:\\Users\\Ater\\Desktop\\3GHWList.xlsx";
    static String gHardwareFileName = "C:\\Users\\Ater\\Desktop\\2GHWList.xlsx";
    static String lHardwareFileName = "C:\\Users\\Ater\\Desktop\\4GHWList.xlsx";
    static XSSFWorkbook wb;
    static XSSFWorkbook changesWrb;
    static DecimalFormat df = new DecimalFormat("##.#");


//    static void export3GHWfromXML(ArrayList<NodeBHW> hwList) throws IOException {
//
//        XSSFWorkbook uHardwareWb;
//        uHardwareWb = get3GHWWorkbook();
//        int numOfColumns = 8;
//        XSSFSheet sheet = uHardwareWb.getSheet("Sheet1");
//        int r = 1;
//        for (NodeBHW nodeBHW : hwList) {
//
//            for (HwItem hwItem : nodeBHW.getHwItems()) {
//                ArrayList<XSSFCell> cells = new ArrayList<>();
//                XSSFRow row = sheet.createRow(r);
//                //iterating c number of columns
//                for (int i = 0; i < numOfColumns; i++) {
//                    XSSFCell cell = row.createCell(i);
//                    cells.add(i, cell);
//                }
//                cells.get(0).setCellValue(nodeBHW.getNodeBCode());
//                cells.get(1).setCellValue(nodeBHW.getNodeBName());
//                cells.get(2).setCellValue(Integer.valueOf(nodeBHW.getRncID()));
//                cells.get(3).setCellValue(Integer.valueOf(nodeBHW.getWBTSId()));
//                cells.get(4).setCellValue(hwItem.getUserLabel());
//                cells.get(5).setCellValue(hwItem.getSerialNumber());
//                cells.get(6).setCellValue(hwItem.getIdentificationCode());
//                cells.get(7).setCellValue(hwItem.getState());
//                r++;
//            }
//        }
//        FileOutputStream fileOut = new FileOutputStream(uHardwareFileName);
//        //write this workbook to an Outputstream.
//        uHardwareWb.write(fileOut);
//        fileOut.flush();
//        fileOut.close();
//    }


    static void export2GSitesList(ArrayList<GSite> sitesList, String sites) throws IOException {
        ZipSecureFile.setMinInflateRatio(0);
        int numOfColumns = 15;
        XSSFSheet sheet = wb.getSheet(sites);
        int r = 1;
        for (GSite site : sitesList) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            cells.get(0).setCellValue(site.getRegion());
            cells.get(1).setCellValue(site.getSiteBSCName());
            cells.get(2).setCellValue(site.getSiteName());
            cells.get(3).setCellValue(site.getSiteCode());
            cells.get(4).setCellValue(site.getSiteNumberOfBCFs());
            cells.get(5).setCellValue(site.getSiteNumberOfTRXs());
            cells.get(6).setCellValue(site.getSiteNumberOfCells());
            cells.get(7).setCellValue(site.getSiteNumberOfOnAirCells());
            cells.get(8).setCellValue(site.getSiteNumberOfDcsCells());
            cells.get(9).setCellValue(site.getSiteNumberOfGsmCells());
            cells.get(10).setCellValue(site.getSiteTxMode());
            cells.get(11).setCellValue(site.getSiteNumberOfE1s());
            cells.get(12).setCellValue(site.getSiteNumberOfGTRXs());
            cells.get(13).setCellValue(site.getLac());
            cells.get(14).setCellValue(site.getRac());
            r++;
        }
        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
//        wb.close();
    }

    static void export3GSitesList(ArrayList<USite> sitesList, String sites) throws IOException {

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
        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
//        wb.close();
        System.out.println("3G Site list done..");
    }

    static void export4GSitesList(ArrayList<LSite> lSitesList, String sheetName) throws IOException {

        int numOfColumns = 10;
        XSSFSheet sheet = wb.getSheet(sheetName);
        int r = 1;
        for (LSite site : lSitesList) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            cells.get(0).setCellValue(site.getENodeBRegion());
            cells.get(1).setCellValue(site.getENodeBName());
            cells.get(2).setCellValue(site.getENodeBCode());
            cells.get(3).setCellValue(Integer.valueOf(site.getENodeBId()));
            cells.get(4).setCellValue(site.getENodeBNumberOfCells());
            cells.get(5).setCellValue(site.getENodeBNumberOfOnAirCells());
            cells.get(6).setCellValue(site.getENodeBVersion());
            cells.get(7).setCellValue(site.getENodeBBW());
            cells.get(8).setCellValue(site.getENodeBMimo());
            cells.get(9).setCellValue(site.getTac());
            r++;
        }
        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("4G Site list done..");
//        wb.close();
    }

    static void export2GHardWare(ArrayList<GSite> sitesList, String sheetName) throws IOException {
        int numOfColumns = 17;
        XSSFSheet sheet = wb.getSheet(sheetName);
        int r = 1;

        for (GSite site : sitesList) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            GSite.GHardware gHardware = site.getGHardware();
            cells.get(0).setCellValue(site.getRegion());
            cells.get(1).setCellValue(site.getSiteName());
            cells.get(2).setCellValue(site.getSiteCode());
            cells.get(3).setCellValue(gHardware.ESMB);
            cells.get(4).setCellValue(gHardware.ESMC);
            cells.get(5).setCellValue(gHardware.FIQA);
            cells.get(6).setCellValue(gHardware.FIQB);
            cells.get(7).setCellValue(gHardware.FSMF);
            cells.get(8).setCellValue(gHardware.FTIF);
            cells.get(9).setCellValue(gHardware.FXDA);
            cells.get(10).setCellValue(gHardware.FXDB);
            cells.get(11).setCellValue(gHardware.FXEA);
            cells.get(12).setCellValue(gHardware.FXEB);
            cells.get(13).setCellValue(gHardware.FXX);
            cells.get(14).setCellValue(gHardware.FXED);
            cells.get(15).setCellValue(gHardware.rfString);
            cells.get(16).setCellValue(gHardware.smString);
            r++;
        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    static void export3GHardWare(ArrayList<USite> sitesList, String sheetName) throws IOException {
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
            USite.UHardware uHardware = site.getUHardware();
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

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    static void exportCarrierList(ArrayList<USite> thirdCarrierList, String sheetName) throws IOException {
        int numOfColumns = carrierHeader.length;
        XSSFSheet sheet = wb.getSheet(sheetName);
        int r = 1;
        for (USite site : thirdCarrierList) {
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
        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("Carrier list done..");
    }

    static void exportNodeBList(ArrayList<NodeB> nodeBList, String sheetName) throws IOException {
        int numOfColumns = 32;
        XSSFSheet sheet = wb.getSheet(sheetName);
        int r = 1;
        for (NodeB nodeB : nodeBList) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            cells.get(0).setCellValue(nodeB.getRegion());
            try {
                cells.get(1).setCellValue(Integer.valueOf(nodeB.getNodeBRncId()));
                cells.get(4).setCellValue(Integer.valueOf(nodeB.getNodeBWbtsId()));
            } catch (Exception e) {
                cells.get(1).setCellValue("");
                cells.get(4).setCellValue("");
            }
            cells.get(2).setCellValue(nodeB.getNodeBName());
            cells.get(3).setCellValue(nodeB.getNodeBCode());
            cells.get(5).setCellValue(nodeB.getNodeBNumberOfCells());
            cells.get(6).setCellValue(nodeB.getNodeBNumberOfOnAirCells());
            cells.get(7).setCellValue(nodeB.getNodeBNumberOfFirstCarriersCells());
            cells.get(8).setCellValue(nodeB.getNodeBNumberOfOnAirFirstCarriersCells());
            cells.get(9).setCellValue(nodeB.getNodeBNumberOfSecondCarriersCells());
            cells.get(10).setCellValue(nodeB.getNodeBNumberOfOnAirSecondCarriersCells());
            cells.get(11).setCellValue(nodeB.getNodeBNumberOfThirdCarriersCells());
            cells.get(12).setCellValue(nodeB.getNodeBNumberOfOnAirThirdCarriersCells());
            cells.get(13).setCellValue(nodeB.getNodeBNumberOfU900CarriersCells());
            cells.get(14).setCellValue(nodeB.getNodeBNumberOfOnAirU900CarriersCells());
            cells.get(15).setCellValue(nodeB.getNodeBVersion());
            cells.get(16).setCellValue(nodeB.getNodeBTxMode());
            cells.get(17).setCellValue(nodeB.getNumberOfHSDPASet1());
            cells.get(18).setCellValue(nodeB.getNumberOfHSDPASet2());
            cells.get(19).setCellValue(nodeB.getNumberOfHSDPASet3());
            cells.get(20).setCellValue(nodeB.getNumberOfHSUPASet1());
            cells.get(21).setCellValue(nodeB.getNumberOfChannelElements());
            cells.get(22).setCellValue(nodeB.getNodeBNumberOfE1s());
//            cells.get(23).setCellValue(nodeB.getNodeBNumberOfNodeBs());
            cells.get(23).setCellValue(nodeB.getNodeBNumberOfSectors());
            cells.get(24).setCellValue(nodeB.getNodeBNumberOfCarriers());
            cells.get(25).setCellValue(nodeB.isStandAloneU900());
            cells.get(26).setCellValue(nodeB.isRfSharing());
            cells.get(27).setCellValue(nodeB.getPower());
            cells.get(28).setCellValue(nodeB.getU900Power());
            cells.get(29).setCellValue(nodeB.getLac());
            cells.get(30).setCellValue(nodeB.getRac());
            cells.get(31).setCellValue(nodeB.getNodeBIP());
            r++;
        }
        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("NodeB list done..");
    }

    static void exportNodeBHardWare(ArrayList<NodeB> nodeBList, String sheetName) throws IOException {
        int numOfColumns = 27;
        XSSFSheet sheet = wb.getSheet(sheetName);
        int r = 1;

        for (NodeB nodeB : nodeBList) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            USite.UHardware nHardware = nodeB.getUHardware();
            cells.get(0).setCellValue(nodeB.getRegion());
            cells.get(1).setCellValue(nodeB.getNodeBName());
            cells.get(2).setCellValue(nodeB.getNodeBCode());
            cells.get(3).setCellValue(Integer.valueOf(nodeB.getNodeBRncId()));
            cells.get(4).setCellValue(Integer.valueOf(nodeB.getNodeBWbtsId()));
            cells.get(5).setCellValue(nHardware.FBBA);
            cells.get(6).setCellValue(nHardware.FRGC);
            cells.get(7).setCellValue(nHardware.FRGD);
            cells.get(8).setCellValue(nHardware.FRGF);
            cells.get(9).setCellValue(nHardware.FRGL);
            cells.get(10).setCellValue(nHardware.FRGM);
            cells.get(11).setCellValue(nHardware.FRGP);
            cells.get(12).setCellValue(nHardware.FRGT);
            cells.get(13).setCellValue(nHardware.FRGU);
            cells.get(14).setCellValue(nHardware.FRGX);
            cells.get(15).setCellValue(nHardware.FSMB);
            cells.get(16).setCellValue(nHardware.FSMD);
            cells.get(17).setCellValue(nHardware.FSME);
            cells.get(18).setCellValue(nHardware.FSMF);
            cells.get(19).setCellValue(nHardware.FTIA);
            cells.get(20).setCellValue(nHardware.FTIB);
            cells.get(21).setCellValue(nHardware.FTIF);
            cells.get(22).setCellValue(nHardware.FTPB);
            cells.get(23).setCellValue(nHardware.FXDA);
            cells.get(24).setCellValue(nHardware.FXDB);
            cells.get(25).setCellValue(nHardware.rfString);
            cells.get(26).setCellValue(nHardware.smString);
            r++;
        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    static void export4GHardWare(ArrayList<LSite> sitesList, String sheetName) throws IOException {
        int numOfColumns = 14;
        XSSFSheet sheet = wb.getSheet(sheetName);
        int r = 1;

        for (LSite site : sitesList) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = sheet.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            LSite.LHardware lHardware = site.getLHardware();
            cells.get(0).setCellValue(site.getENodeBRegion());
            cells.get(1).setCellValue(site.getENodeBName());
            cells.get(2).setCellValue(site.getENodeBCode());
            cells.get(3).setCellValue(Integer.valueOf(site.getENodeBId()));
            cells.get(4).setCellValue(lHardware.FBBA);
            cells.get(5).setCellValue(lHardware.FBBC);
            cells.get(6).setCellValue(lHardware.FRGT);
            cells.get(7).setCellValue(lHardware.FSMF);
            cells.get(8).setCellValue(lHardware.FSPD);
            cells.get(9).setCellValue(lHardware.FTIF);
            cells.get(10).setCellValue(lHardware.FXEB);
            cells.get(11).setCellValue(lHardware.FXED);
            cells.get(12).setCellValue(lHardware.rfString);
            cells.get(13).setCellValue(lHardware.smString);
            r++;
        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        wb.close();
    }

    static void getWorkbook() throws IOException {
        File file = new File("D:\\RAN Tool\\Dashboard.xlsx");
        InputStream ExcelFileToRead;
        XSSFWorkbook workbook;
        if (file.exists()) {
            ZipSecureFile.setMinInflateRatio(0);
            ExcelFileToRead = new FileInputStream(file);
            workbook = new XSSFWorkbook(ExcelFileToRead);
        } else {
            workbook = new XSSFWorkbook();
        }
        wb = workbook;
    }

    private static XSSFWorkbook get2GHWWorkbook() throws IOException {
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

    private static XSSFWorkbook get3GHWWorkbook() throws IOException {
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

    private static XSSFWorkbook get4GHWWorkbook() throws IOException {
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

    private static XSSFWorkbook getTRXWorkbook() throws IOException {
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

    static void exportTRXSheet(ResultSet resultSet, int ran) throws SQLException, IOException {
        XSSFSheet sheet1;
        XSSFWorkbook trxWb;
        trxWb = getTRXWorkbook();
        sheet1 = trxWb.getSheet("TRX");
        int numOfColumns = 12;
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
            r++;
        }
        FileOutputStream fileOut;
        if (ran == 1) {
            fileOut = new FileOutputStream(TRX1FileName);
            trxWb.write(fileOut);
        } else {
            fileOut = new FileOutputStream(TRX2FileName);
            trxWb.write(fileOut);
        }
        fileOut.flush();
        fileOut.close();
        trxWb.close();

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

    static void exportUcellsSheet(ResultSet resultSet, int ran) throws SQLException, IOException {
        XSSFSheet sheet1;
        XSSFWorkbook trxWb;
        trxWb = getUcellsWorkbook();
        sheet1 = trxWb.getSheet("Cells");
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
        if (ran == 1) {
            fileOut = new FileOutputStream(uCells1FileName);
            trxWb.write(fileOut);
        } else {
            fileOut = new FileOutputStream(uCells2FileName);
            trxWb.write(fileOut);
        }
        fileOut.flush();
        fileOut.close();
        trxWb.close();

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

    static void exportChangesSheet(ResultSet resultSet) throws SQLException, IOException {
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
    static void export2GHWfromXML(HashMap<String, BCF> bcfs) throws IOException {
        XSSFWorkbook gHardwareWb;
        gHardwareWb = get2GHWWorkbook();
        int numOfColumns = 7;
        XSSFSheet sheet = gHardwareWb.getSheet("Sheet1");
        final int[] r = {1};
        bcfs.forEach((key, value) ->
        {
            ArrayList<HwItem> hwItems = value.getHwItems();
            for (HwItem hwItem : hwItems) {
                ArrayList<XSSFCell> cells = new ArrayList<>();
                XSSFRow row = sheet.createRow(r[0]);
                //iterating c number of columns
                for (int i = 0; i < numOfColumns; i++) {
                    XSSFCell cell = row.createCell(i);
                    cells.add(i, cell);
                }
                cells.get(0).setCellValue(value.getBcfCode());
                cells.get(1).setCellValue(value.getBcfName());
                cells.get(2).setCellValue(Integer.valueOf(value.getBscId()));
                cells.get(3).setCellValue(Integer.valueOf(value.getBcfId()));
                cells.get(4).setCellValue(hwItem.getUserLabel());
                cells.get(5).setCellValue(hwItem.getSerialNumber());
                cells.get(6).setCellValue(hwItem.getIdentificationCode());
                r[0]++;
            }
        });
        FileOutputStream fileOut = new FileOutputStream(gHardwareFileName);
        //write this workbook to an Outputstream.
        gHardwareWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("2G hardwarefile is done");
    }

    // 3G HW list
    static void export3GHWfromXML(ArrayList<NodeB> nodeBList, HashMap<String, NodeBHW> hwList) throws IOException {

        XSSFWorkbook uHardwareWb;
        uHardwareWb = get3GHWWorkbook();
        int numOfColumns = 7;
        XSSFSheet sheet = uHardwareWb.getSheet("Sheet1");
        int r = 1;
        for (NodeB nodeB : nodeBList) {
            String key = nodeB.getNodeBRncId() + "_" + nodeB.getNodeBWbtsId();
            NodeBHW nodeBHW = hwList.get(key);
            if (nodeBHW != null) {
                for (HwItem hwItem : nodeBHW.getHwItems()) {
                    ArrayList<XSSFCell> cells = new ArrayList<>();
                    XSSFRow row = sheet.createRow(r);
                    //iterating c number of columns
                    for (int i = 0; i < numOfColumns; i++) {
                        XSSFCell cell = row.createCell(i);
                        cells.add(i, cell);
                    }
                    cells.get(0).setCellValue(nodeB.getNodeBCode());
                    cells.get(1).setCellValue(nodeB.getNodeBName());
                    cells.get(2).setCellValue(Integer.valueOf(nodeB.getNodeBRncId()));
                    cells.get(3).setCellValue(Integer.valueOf(nodeB.getNodeBWbtsId()));
                    cells.get(4).setCellValue(hwItem.getUserLabel());
                    cells.get(5).setCellValue(hwItem.getSerialNumber());
                    cells.get(6).setCellValue(hwItem.getIdentificationCode());

                    r++;
                }
            }
        }
        FileOutputStream fileOut = new FileOutputStream(uHardwareFileName);
        //write this workbook to an Outputstream.
        uHardwareWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("3G hardwarefile is done");
    }

    // 4G HW list
    static void export4GHWfromXML(ArrayList<LSite> lSitesList, HashMap<String, ENodeBHW> eNodeBHWHashMap) throws IOException {

        XSSFWorkbook lHardwareWb;
        lHardwareWb = get4GHWWorkbook();
        int numOfColumns = 6;
        XSSFSheet sheet = lHardwareWb.getSheet("Sheet1");
        int r = 1;
        for (LSite lSite : lSitesList) {
            String key = lSite.getENodeBId();
            ENodeBHW eNodeBHW = eNodeBHWHashMap.get(key);
            if (eNodeBHW != null) {
                for (HwItem hwItem : eNodeBHW.getHwItems()) {
                    ArrayList<XSSFCell> cells = new ArrayList<>();
                    XSSFRow row = sheet.createRow(r);
                    //iterating c number of columns
                    for (int i = 0; i < numOfColumns; i++) {
                        XSSFCell cell = row.createCell(i);
                        cells.add(i, cell);
                    }
                    cells.get(0).setCellValue(lSite.getENodeBCode());
                    cells.get(1).setCellValue(lSite.getENodeBName());
                    cells.get(2).setCellValue(Integer.valueOf(lSite.getENodeBId()));
                    cells.get(3).setCellValue(hwItem.getUserLabel());
                    cells.get(4).setCellValue(hwItem.getSerialNumber());
                    cells.get(5).setCellValue(hwItem.getIdentificationCode());
                    r++;
                }
            }
        }
        FileOutputStream fileOut = new FileOutputStream(lHardwareFileName);
        //write this workbook to an Outputstream.
        lHardwareWb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("4G hardwarefile is done");
    }
}



