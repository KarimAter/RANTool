package sample;

import Helpers.Utils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.sql.*;
import java.text.DecimalFormat;
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
    static String TRX1FileName = "C:\\Users\\Ater\\Desktop\\Dashboard_TRX1.xlsx";
    static String TRX2FileName = "C:\\Users\\Ater\\Desktop\\Dashboard_TRX2.xlsx";
    static String changesFileName = "C:\\Users\\Ater\\Desktop\\NetworkChanges.xlsx";
    static XSSFWorkbook wb;
    static XSSFWorkbook changesWrb;
    static DecimalFormat df = new DecimalFormat("##.#");

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

    static void getWorkbook(String excelFileName) throws IOException {
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

    private static XSSFWorkbook getTRXWorkbook(String trxFileName, int ran) throws IOException {
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
//        if (ran == 1)
        return workbook;
//        else
//            trxWb2 = workbook;
    }


    static void exportTRXSheet(ResultSet resultSet, int ran) throws SQLException, IOException {
        XSSFSheet sheet1;
        XSSFWorkbook trxWb;
        if (ran == 1) {
            trxWb = getTRXWorkbook(TRX1FileName, 1);
            sheet1 = trxWb.getSheet("TRX");
        } else {
            trxWb = getTRXWorkbook(TRX2FileName, 2);
            sheet1 = trxWb.getSheet("TRX");
        }
        int numOfColumns = 11;
//        XSSFSheet sheet1 = trxWb.getSheet("RAN1");
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
            cells.get(0).setCellValue(siteName);
            cells.get(1).setCellValue(Utils.extractSiteCode(siteName));
            cells.get(2).setCellValue(Integer.valueOf(resultSet.getString(1)));
            cells.get(3).setCellValue(Integer.valueOf(resultSet.getString(2)));
            cells.get(4).setCellValue(Integer.valueOf(resultSet.getString(3)));
            cells.get(5).setCellValue(Integer.valueOf(resultSet.getString(4)));
            cells.get(6).setCellValue(Integer.valueOf(resultSet.getString(5)));
            cells.get(7).setCellValue(Integer.valueOf(resultSet.getString(6)));
            cells.get(8).setCellValue(resultSet.getString(7));
            cells.get(9).setCellValue(Integer.valueOf(resultSet.getString(8)));
            cells.get(10).setCellValue(Integer.valueOf(resultSet.getString(9)));
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
        XSSFSheet trxSheet, gTrxSheet, psSheet, ceSheet, paSheet, bwSheet;
        changesWrb = getChangesWorkbook(changesFileName);
        int trxColumns = 8;
        int gtrxColumns = 5;
        int powerColumns = 11;
        int tokenColumns = 15;
        int ceColumns = 8;
        int bwColumns = 5;
        int trxCursor = 1, gTrxCursor = 1, psCursor = 1, ceCursor = 1, paCursor = 1, bwCursor = 1;
        while (resultSet.next()) {
            String siteName = resultSet.getString(2);
            String siteCode = resultSet.getString(3);

            switch (resultSet.getInt(4)) {
                case 2: {
                    String trxComment = ChangesDetector.getTrxComment(resultSet);
                    String gTrxComment = ChangesDetector.getGtrxComment(resultSet);
                    int oldTrx = resultSet.getInt(24);
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
                        cells.get(4).setCellValue(resultSet.getInt(5));
                        String currentSm = resultSet.getString(13);
                        String oldSm = resultSet.getString(32);
                        cells.get(5).setCellValue(oldSm);
                        cells.get(6).setCellValue(currentSm);
                        if (oldSm.equals("") || currentSm.equals(""))
                            cells.get(7).setCellValue("");
                        else if (oldSm.equals(currentSm))
                            cells.get(7).setCellValue("Soft");
                        else
                            cells.get(7).setCellValue("Hard");
                        trxCursor++;
                    }

                    int oldGtrx = resultSet.getInt(25);
                    if (gTrxComment.contains("grade") && oldGtrx != 0) {
                        gTrxSheet = changesWrb.getSheet("GTRX");
                        ArrayList<XSSFCell> cells = new ArrayList<>();
                        XSSFRow row = gTrxSheet.createRow(gTrxCursor);
                        //iterating c number of columns
                        for (int i = 0; i < gtrxColumns; i++) {
                            XSSFCell cell = row.createCell(i);
                            cells.add(i, cell);
                        }
                        cells.get(0).setCellValue(siteName);
                        cells.get(1).setCellValue(siteCode);
                        cells.get(2).setCellValue(gTrxComment);
                        cells.get(3).setCellValue(oldGtrx);
                        cells.get(4).setCellValue(resultSet.getInt(6));
                        gTrxCursor++;
                    }
                }
                break;
                case 3: {
                    String psComment = ChangesDetector.getTokenChanges(resultSet).getComment();
                    String ceComment = ChangesDetector.getChannelElementsComment(resultSet);
                    String paComment = ChangesDetector.getPowerChanges(resultSet).getComment();
                    if (psComment.contains("grade")) {
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
                        cells.get(2).setCellValue(psComment);
                        ChangesDetector.ChangeValues tokenChanges = ChangesDetector.getTokenChanges(resultSet);
                        cells.get(3).setCellValue(tokenChanges.getActionType());
                        cells.get(4).setCellValue((int) tokenChanges.getOld()[0]);
                        cells.get(5).setCellValue((int) tokenChanges.getCurrent()[0]);
                        cells.get(6).setCellValue((int) tokenChanges.getOld()[1]);
                        cells.get(7).setCellValue((int) tokenChanges.getCurrent()[1]);
                        cells.get(8).setCellValue((int) tokenChanges.getOld()[2]);
                        cells.get(9).setCellValue((int) tokenChanges.getCurrent()[2]);
                        cells.get(10).setCellValue((int) tokenChanges.getOld()[3]);
                        cells.get(11).setCellValue((int) tokenChanges.getCurrent()[3]);
                        String currentSm = resultSet.getString(16);
                        String oldSm = resultSet.getString(35);
                        cells.get(12).setCellValue(oldSm);
                        cells.get(13).setCellValue(currentSm);
                        if (oldSm.equals("") || currentSm.equals(""))
                            cells.get(14).setCellValue("");
                        else if (oldSm.equals(currentSm))
                            cells.get(14).setCellValue("Soft");
                        else
                            cells.get(14).setCellValue("Hard");
                        psCursor++;
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
                        cells.get(4).setCellValue(resultSet.getInt(8));
                        String currentSm = resultSet.getString(16);
                        String oldSm = resultSet.getString(35);
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
                    if (paComment.contains("grade")) {
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
                        cells.get(2).setCellValue(paComment);
                        ChangesDetector.ChangeValues powerChanges = ChangesDetector.getPowerChanges(resultSet);
                        cells.get(3).setCellValue(powerChanges.getActionType());
                        cells.get(4).setCellValue(Math.round(powerChanges.getOld()[0]));
                        cells.get(5).setCellValue(Math.round(powerChanges.getCurrent()[0]));
                        cells.get(6).setCellValue(Math.round(powerChanges.getOld()[1] * 10) / 10.0);
                        cells.get(7).setCellValue(Math.round(powerChanges.getCurrent()[1] * 10) / 10.0);
                        String currentRF = resultSet.getString(15);
                        String oldRF = resultSet.getString(34);
                        cells.get(8).setCellValue(oldRF);
                        cells.get(9).setCellValue(currentRF);
                        if (oldRF.equals(currentRF))
                            cells.get(10).setCellValue("Soft");
                        else
                            cells.get(10).setCellValue("Hard");
                        paCursor++;
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
                        cells.get(3).setCellValue(resultSet.getInt(30));
                        cells.get(4).setCellValue(resultSet.getInt(11));
                        bwCursor++;
                    }
                }
                break;

            }

        }

        FileOutputStream fileOut;
        fileOut = new FileOutputStream(changesFileName);
        changesWrb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("Changes sheet is done..");
    }

    private static void getTrxUpgrades(ResultSet resultSet) throws SQLException {
        XSSFSheet trxSheet = changesWrb.getSheet("TRX");
        int numOfColumns = 5;
        int r = 1;
        while (resultSet.next()) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = trxSheet.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            String trxComment = ChangesDetector.getTrxComment(resultSet);
            if (trxComment.contains("grade")) {
                cells.get(0).setCellValue(resultSet.getString(2));
                cells.get(1).setCellValue(resultSet.getString(3));
                cells.get(2).setCellValue(trxComment);
                cells.get(3).setCellValue(resultSet.getInt(26));
                cells.get(4).setCellValue(resultSet.getInt(5));
                r++;
            }

        }
    }

    private static void getGtrxUpgrades(ResultSet resultSet) throws SQLException {
        XSSFSheet gTrxSheet = changesWrb.getSheet("GTRX");
        int numOfColumns = 5;
        int r = 1;
        while (resultSet.next()) {
            ArrayList<XSSFCell> cells = new ArrayList<>();
            XSSFRow row = gTrxSheet.createRow(r);
            //iterating c number of columns
            for (int i = 0; i < numOfColumns; i++) {
                XSSFCell cell = row.createCell(i);
                cells.add(i, cell);
            }
            String gtrxComment = ChangesDetector.getGtrxComment(resultSet);
            if (gtrxComment.contains("grade")) {
                cells.get(0).setCellValue(resultSet.getString(2));
                cells.get(1).setCellValue(resultSet.getString(3));
                cells.get(2).setCellValue(gtrxComment);
                cells.get(3).setCellValue(resultSet.getInt(27));
                cells.get(4).setCellValue(resultSet.getInt(6));
                r++;
            }

        }
    }


//        XSSFSheet sheet1;
//        XSSFWorkbook changesWrb;
//        changesWrb = getChangesWorkbook(changesFileName);
//        sheet1 = changesWrb.getSheet("Changes");
//        int numOfColumns = 10;
////        XSSFSheet sheet1 = trxWb.getSheet("RAN1");
//        int r = 1;
//        while (resultSet.next()) {
//            ArrayList<XSSFCell> cells = new ArrayList<>();
//            XSSFRow row = sheet1.createRow(r);
//            //iterating c number of columns
//            for (int i = 0; i < numOfColumns; i++) {
//                XSSFCell cell = row.createCell(i);
//                cells.add(i, cell);
//            }
//            cells.get(0).setCellValue(resultSet.getString(2));
//            cells.get(1).setCellValue(resultSet.getString(3));
//            cells.get(2).setCellValue(resultSet.getInt(4));
//            cells.get(3).setCellValue(ChangesDetector.getTrxComment(resultSet));
//            cells.get(4).setCellValue(ChangesDetector.getGtrxComment(resultSet));
//            cells.get(5).setCellValue(ChangesDetector.getProcessingSetsComment(resultSet));
//            cells.get(6).setCellValue(ChangesDetector.getChannelElementsComment(resultSet));
//            cells.get(7).setCellValue(ChangesDetector.getNewCarriersComment(resultSet));
//            cells.get(8).setCellValue(ChangesDetector.getPowerChanges(resultSet));
//            cells.get(9).setCellValue(ChangesDetector.getBWComment(resultSet));
//            r++;
//        }
//        FileOutputStream fileOut;
//        fileOut = new FileOutputStream(changesFileName);
//        changesWrb.write(fileOut);
//        fileOut.flush();
//        fileOut.close();
//        System.out.println("Changes sheet is done..");

}


