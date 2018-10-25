package sample;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.usermodel.*;
import java.io.*;
import java.util.ArrayList;

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
    static XSSFWorkbook wb;

    static void export2GSitesList(ArrayList<GSite> sitesList, String sites) throws IOException {
        ZipSecureFile.setMinInflateRatio(0);
        int numOfColumns = gSiteHeader.length;
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
            r++;
        }
        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    static void export3GSitesList(ArrayList<USite> sitesList, String sites) throws IOException {

        int numOfColumns = 30;
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
            r++;
        }
        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("3G Site list done..");
    }

    static void export4GSitesList(ArrayList<LSite> lSitesList, String sheetName) throws IOException {

        int numOfColumns = lSiteHeader.length;
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
            r++;
        }
        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("4G Site list done..");
    }

    static void export2GHardWare(ArrayList<GSite> sitesList, String sheetName) throws IOException {
        int numOfColumns = 16;
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
            cells.get(14).setCellValue(gHardware.rfString);
            cells.get(15).setCellValue(gHardware.smString);
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
            USite.UHardware gHardware = site.getUHardware();
            cells.get(0).setCellValue(site.getSiteRegion());
            cells.get(1).setCellValue(site.getSiteName());
            cells.get(2).setCellValue(site.getSiteCode());
            cells.get(3).setCellValue(gHardware.FBBA);
            cells.get(4).setCellValue(gHardware.FRGC);
            cells.get(5).setCellValue(gHardware.FRGD);
            cells.get(6).setCellValue(gHardware.FRGF);
            cells.get(7).setCellValue(gHardware.FRGL);
            cells.get(8).setCellValue(gHardware.FRGM);
            cells.get(9).setCellValue(gHardware.FRGP);
            cells.get(10).setCellValue(gHardware.FRGT);
            cells.get(11).setCellValue(gHardware.FRGU);
            cells.get(12).setCellValue(gHardware.FRGX);
            cells.get(13).setCellValue(gHardware.FSMB);
            cells.get(14).setCellValue(gHardware.FSMD);
            cells.get(15).setCellValue(gHardware.FSME);
            cells.get(16).setCellValue(gHardware.FSMF);
            cells.get(17).setCellValue(gHardware.FTIA);
            cells.get(18).setCellValue(gHardware.FTIB);
            cells.get(19).setCellValue(gHardware.FTIF);
            cells.get(20).setCellValue(gHardware.FTPB);
            cells.get(21).setCellValue(gHardware.FXDA);
            cells.get(22).setCellValue(gHardware.FXDB);
            cells.get(23).setCellValue(gHardware.rfString);
            cells.get(24).setCellValue(gHardware.smString);
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

        int numOfColumns = header.length;
        File file = new File(excelFileName);
//        excelFileName = file.getPath();
        InputStream ExcelFileToRead;
        XSSFWorkbook wb;
        if (file.exists()) {
            ExcelFileToRead = new FileInputStream(file);
            wb = new XSSFWorkbook(ExcelFileToRead);
        } else {
            wb = new XSSFWorkbook();
        }

//        XSSFWorkbook wb = new XSSFWorkbook();
//        XSSFSheet sheet = wb.createSheet(sheetName);
        XSSFSheet sheet = wb.getSheet("NodeBs");
        XSSFCellStyle style = wb.createCellStyle();
        XSSFCellStyle headerStyle = wb.createCellStyle();
//        headerStyle.setFillBackgroundColor(Color);
//        style.setWrapText(false);

        ArrayList<XSSFCell> cells = new ArrayList<>(4);
        XSSFRow firstRow = sheet.createRow(0);
        //iterating c number of columns
        for (int i = 0; i < numOfColumns; i++) {
            XSSFCell cell = firstRow.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(header[i]);
            cells.add(i, cell);
        }
        int r = 1;
        for (NodeB aNodeB : nodeBList) {
            XSSFRow row = sheet.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                cells.add(i, cell);
            }
//            cells.get(0).setCellValue(aNodeB.getSiteName());
//            cells.get(1).setCellValue(aNodeB.getSiteCode());
            //RncId,WBTSId,allCells,onAirCells,First,onFirst,Second,onSecond,Third,onThird,U9,onU9,WBTSName
            cells.get(0).setCellValue(Integer.valueOf(aNodeB.getNodeBRncId()));
            cells.get(1).setCellValue(Integer.valueOf(aNodeB.getNodeBWbtsId()));
            cells.get(2).setCellValue(aNodeB.getNodeBNumberOfCells());
            cells.get(3).setCellValue(aNodeB.getNodeBNumberOfOnAirCells());
            cells.get(4).setCellValue(aNodeB.getNodeBNumberOfFirstCarriersCells());
            cells.get(5).setCellValue(aNodeB.getNodeBNumberOfOnAirFirstCarriersCells());
            cells.get(6).setCellValue(aNodeB.getNodeBNumberOfSecondCarriersCells());
            cells.get(7).setCellValue(aNodeB.getNodeBNumberOfOnAirSecondCarriersCells());
            cells.get(8).setCellValue(aNodeB.getNodeBNumberOfThirdCarriersCells());
            cells.get(9).setCellValue(aNodeB.getNodeBNumberOfOnAirThirdCarriersCells());
            cells.get(10).setCellValue(aNodeB.getNodeBNumberOfU900CarriersCells());
            cells.get(11).setCellValue(aNodeB.getNodeBNumberOfOnAirU900CarriersCells());
            cells.get(12).setCellValue(aNodeB.getNodeBName());
            cells.get(13).setCellValue(aNodeB.getNodeBTxMode());
            cells.get(14).setCellValue(aNodeB.getNodeBVersion());
            cells.get(15).setCellValue(aNodeB.getNumberOfHSDPASet1());
            cells.get(16).setCellValue(aNodeB.getNumberOfHSDPASet2());
            cells.get(17).setCellValue(aNodeB.getNumberOfHSDPASet3());
            cells.get(18).setCellValue(aNodeB.getNumberOfHSUPASet1());
            cells.get(19).setCellValue(aNodeB.getNumberOfChannelElements());
            cells.get(20).setCellValue(aNodeB.getNodeBNumberOfE1s());
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
    }

    static void getWorkbook() throws IOException {
        File file = new File(excelFileName);
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
}


