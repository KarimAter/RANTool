package Helpers;


import sample.GSite;
import sample.LSite;
import sample.USite;

import java.sql.*;
import java.util.ArrayList;

public class DbSaver {


    private Connection connection;

    public DbSaver(String path) {
        this.connection = getConnection(path);

        Statement st;
        try {
            st = connection.createStatement();
            String GSites = "create table if not exists GSMSites (id text,siteName text ,siteCode text,"
                    + " region text, siteBSCName text,siteBCFId int,siteBSCId int,siteNumberOfBCFs integer,"
                    + " siteNumberOfTRXs integer, siteNumberOfSectors integer, siteNumberOfCells integer, "
                    + " siteNumberOfDcsCells integer, siteNumberOfGsmCells integer,siteNumberOfE1s integer,"
                    + " siteNumberOfOnAirCells integer, siteNumberOfGTRXs integer, lac integer, rac integer,gSiteTxMode text,"
                    + " rFModuleIdentifier text, systemModuleIdentifier text,txModuleIdentifier text,"
                    + " trxIdentifier integer,gTrxIdentifier integer,txModeIdentifier integer)";
//38
            String USites = "create table if not exists UMTSSites (id text,siteName text, siteCode text, "
                    + "siteRegion text, siteRncId text, siteVersion text, siteNumberOfNodeBs integer,"
                    + " siteNumberOfSectors integer, siteNumberOfCells integer,siteNumberOfCarriers integer,"
                    + " siteNumberOfE1s integer, siteNumberOfOnAirCells integer, siteNumberOfFirstCarriersCells integer, "
                    + "siteNumberOfOnAirFirstCarriersCells integer,siteNumberOfSecondCarriersCells integer, "
                    + "siteNumberOfOnAirSecondCarriersCells integer, siteNumberOfThirdCarriersCells integer,"
                    + " siteNumberOfOnAirThirdCarriersCells integer,siteNumberOfU900CarriersCells integer, "
                    + "siteNumberOfOnAirU900CarriersCells integer, "
                    + "siteNumberOfHSDPASet1 integer, siteNumberOfHSDPASet2 integer, siteNumberOfHSDPASet3 integer,"
                    + " siteNumberOfHSUPASet1 integer, siteNumberOfChannelElements integer, sitePower double,"
                    + " siteU900Power double, lac integer, rac integer,processingSetsIdentifier text,"
                    + " channelElementsIdentifier int, carriersIdentifier text, powerIdentifier text, rfSharing boolean,"
                    + "standAloneU900 boolean, rFModuleIdentifier text,sModuleIdentifier text,txModuleIdentifier text)";

            String LSites = "create table if not exists LTESites (id text, eNodeBName text, eNodeBCode text,"
                    + " eNodeBRegion text, eNodeBId text, eNodeBVersion text, eNodeBNumberOfSectors integer,"
                    + " eNodeBNumberOfCells integer, eNodeBNumberOfOnAirCells integer, eNodeBBW integer,"
                    + " eNodeBMimo integer, tac integer,rFModuleIdentifier text,sModuleIdentifier text)";

            st.execute(GSites);
            st.execute(LSites);
            st.execute(USites);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(String path) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection
                    ("jdbc:sqlite:" + path, "r", "s");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return conn;
    }


    private void insertGSM(ArrayList<GSite> gSitesList) {
        try {
            String sq = "INSERT INTO GSMSites (id,siteName  ,siteCode ,"
                    + "region , siteBSCName ,siteBCFId ,siteBSCId ,siteNumberOfBCFs ,"
                    + " siteNumberOfTRXs , siteNumberOfSectors , siteNumberOfCells , "
                    + "siteNumberOfDcsCells , siteNumberOfGsmCells ,siteNumberOfE1s ,"
                    + " siteNumberOfOnAirCells , siteNumberOfGTRXs, lac , rac ,gSiteTxMode , "
                    + "rFModuleIdentifier , systemModuleIdentifier ,txModuleIdentifier ,"
                    + " trxIdentifier , gTrxIdentifier , txModeIdentifier )" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement pr = connection.prepareStatement(sq)) {
                for (int i = 0; i < gSitesList.size(); i++) {

                    GSite gSite = gSitesList.get(i);
                    pr.setString(1, gSite.getUniqueName());
                    pr.setString(2, gSite.getSiteName());
                    pr.setString(3, gSite.getSiteCode());
                    pr.setString(4, gSite.getRegion());
                    pr.setString(5, gSite.getSiteBSCName());
                    pr.setString(6, gSite.getSiteBCFId());
                    pr.setString(7, gSite.getSiteBSCId());
                    pr.setInt(8, gSite.getSiteNumberOfBCFs());
                    pr.setInt(9, gSite.getSiteNumberOfTRXs());
                    pr.setInt(10, gSite.getSiteNumberOfSectors());
                    pr.setInt(11, gSite.getSiteNumberOfCells());
                    pr.setInt(12, gSite.getSiteNumberOfDcsCells());
                    pr.setInt(13, gSite.getSiteNumberOfGsmCells());
                    pr.setInt(14, gSite.getSiteNumberOfE1s());
                    pr.setInt(15, gSite.getSiteNumberOfOnAirCells());
                    pr.setInt(16, gSite.getSiteNumberOfGTRXs());
                    pr.setInt(17, gSite.getLac());
                    pr.setInt(18, gSite.getRac());
                    pr.setString(19, gSite.getSiteTxMode());
                    pr.setString(20, gSite.getGHardware().getRfModuleIdentifier());
                    pr.setString(21, gSite.getGHardware().getSystemModuleIdentifier());
                    pr.setString(22, gSite.getGHardware().getTxModuleIdentifier());
                    pr.setInt(23, gSite.getTrxIdentifier());
                    pr.setInt(24, gSite.getgTrxIdentifier());
                    pr.setInt(25, gSite.getTxModeIdentifier());
                    pr.executeUpdate();
                }
                pr.executeBatch();
                System.out.println("GSM Table is done..");
                //connection.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void insertUMTS(ArrayList<USite> uSitesList) {
        try {

            String sq = "INSERT INTO UMTSSites (id ,siteName , siteCode , "
                    + "siteRegion , siteRncId , siteVersion , siteNumberOfNodeBs ,"
                    + " siteNumberOfSectors , siteNumberOfCells ,siteNumberOfCarriers ,"
                    + " siteNumberOfE1s , siteNumberOfOnAirCells , siteNumberOfFirstCarriersCells , "
                    + "siteNumberOfOnAirFirstCarriersCells,siteNumberOfSecondCarriersCells , "
                    + "siteNumberOfOnAirSecondCarriersCells , siteNumberOfThirdCarriersCells ,"
                    + " siteNumberOfOnAirThirdCarriersCells ,siteNumberOfU900CarriersCells , "
                    + "siteNumberOfOnAirU900CarriersCells ,"
                    + "siteNumberOfHSDPASet1 , siteNumberOfHSDPASet2 , siteNumberOfHSDPASet3 ,"
                    + " siteNumberOfHSUPASet1 , siteNumberOfChannelElements , sitePower ,"
                    + " siteU900Power,lac,rac,processingSetsIdentifier , channelElementsIdentifier , carriersIdentifier ," +
                    " powerIdentifier , rfSharing ,standAloneU900 , rFModuleIdentifier ,sModuleIdentifier ,txModuleIdentifier )" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement pr = connection.prepareStatement(sq)) {
                for (int i = 0; i < uSitesList.size(); i++) {
                    USite uSite = uSitesList.get(i);
                    pr.setString(1, uSite.getUniqueName());
                    pr.setString(2, uSite.getSiteName());
                    pr.setString(3, uSite.getSiteCode());
                    pr.setString(4, uSite.getSiteRegion());
                    pr.setString(5, uSite.getSiteRncId());
                    pr.setString(6, uSite.getSiteVersion());
                    pr.setInt(7, uSite.getSiteNumberOfNodeBs());
                    pr.setInt(8, uSite.getSiteNumberOfSectors());
                    pr.setInt(9, uSite.getSiteNumberOfCells());
                    pr.setInt(10, uSite.getSiteNumberOfCarriers());
                    pr.setInt(11, uSite.getSiteNumberOfE1s());
                    pr.setInt(12, uSite.getSiteNumberOfOnAirCells());
                    pr.setInt(13, uSite.getSiteNumberOfFirstCarriersCells());
                    pr.setInt(14, uSite.getSiteNumberOfOnAirFirstCarriersCells());
                    pr.setInt(15, uSite.getSiteNumberOfSecondCarriersCells());
                    pr.setInt(16, uSite.getSiteNumberOfOnAirSecondCarriersCells());
                    pr.setInt(17, uSite.getSiteNumberOfThirdCarriersCells());
                    pr.setInt(18, uSite.getSiteNumberOfOnAirThirdCarriersCells());
                    pr.setInt(19, uSite.getSiteNumberOfU900CarriersCells());
                    pr.setInt(20, uSite.getSiteNumberOfOnAirU900CarriersCells());
                    pr.setInt(21, uSite.getSiteNumberOfHSDPASet1());
                    pr.setInt(22, uSite.getSiteNumberOfHSDPASet2());
                    pr.setInt(23, uSite.getSiteNumberOfHSDPASet3());
                    pr.setInt(24, uSite.getSiteNumberOfHSUPASet1());
                    pr.setInt(25, uSite.getSiteNumberOfChannelElements());
                    pr.setDouble(26, uSite.getSitePower());
                    pr.setDouble(27, uSite.getSiteU900Power());
                    pr.setInt(28, uSite.getLac());
                    pr.setInt(29, uSite.getRac());
                    pr.setString(30, uSite.getProcessingSetsIdentifier());
                    pr.setInt(31, uSite.getChannelElementsIdentifier());
                    pr.setString(32, uSite.getCarriersIdentifier());
                    pr.setString(33, uSite.getPowerIdentifier());
                    pr.setBoolean(34, uSite.isRfSharing());
                    pr.setBoolean(35, uSite.isStandAloneU900());
                    USite.UHardware uHardware = uSite.getUHardware();
                    pr.setString(36, uHardware.getrFModuleIdentifier());
                    pr.setString(37, uHardware.getsModuleIdentifier());
                    pr.setString(38, uHardware.getTransmissionModuleIdentifier());
                    pr.executeUpdate();
                }
                pr.executeBatch();
                System.out.println("GSM Table is done..");
                // connection.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void insertLTE(ArrayList<LSite> lSitesList) {
        try {

            String sq = "INSERT INTO LTESites (id, eNodeBName , eNodeBCode , eNodeBRegion , eNodeBId , "
                    + "eNodeBVersion , eNodeBNumberOfSectors , eNodeBNumberOfCells , eNodeBNumberOfOnAirCells ,"
                    + " eNodeBBW , eNodeBMimo,tac,rFModuleIdentifier,sModuleIdentifier )VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement pr = connection.prepareStatement(sq)) {
                for (int i = 0; i < lSitesList.size(); i++) {
                    LSite lSite = lSitesList.get(i);
                    pr.setString(1, lSite.getUniqueName());
                    pr.setString(2, lSite.getENodeBName());
                    pr.setString(3, lSite.getENodeBCode());
                    pr.setString(4, lSite.getENodeBRegion());
                    pr.setString(5, lSite.getENodeBId());
                    pr.setString(6, lSite.getENodeBVersion());
                    pr.setInt(7, lSite.getENodeBNumberOfSectors());
                    pr.setInt(8, lSite.getENodeBNumberOfCells());
                    pr.setInt(9, lSite.getENodeBNumberOfOnAirCells());
                    pr.setInt(10, lSite.getENodeBBW());
                    pr.setInt(11, lSite.getENodeBMimo());
                    pr.setInt(12, lSite.getTac());
                    LSite.LHardware lHardware = lSite.getLHardware();
                    pr.setString(13, lHardware.getrFModuleIdentifier());
                    pr.setString(14, lHardware.getsModuleIdentifier());
                    pr.executeUpdate();
                }
                pr.executeBatch();
                System.out.println("LTE Table is done..");
                //connection.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void store(ArrayList<GSite> gSitesList, ArrayList<USite> uSitesList, ArrayList<LSite> lSitesList) {
        insertGSM(gSitesList);
        insertUMTS(uSitesList);
        insertLTE(lSitesList);
    }

    public ResultSet loadGIdentifiers() {
        ResultSet resultSet = null;
        try {

            Statement statement = connection.createStatement();
            String query = " Select id,siteName,siteCode,siteNumberOfOnAirCells,trxIdentifier,txModeIdentifier" +
                    ",rFModuleIdentifier , systemModuleIdentifier ,txModuleIdentifier from GSMSites";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet loadUIdentifiers() {

        ResultSet resultSet = null;
        try {

            Statement statement = connection.createStatement();
            String query = " Select id,siteName,siteCode,siteNumberOfOnAirCells,processingSetsIdentifier,channelElementsIdentifier," +
                    "carriersIdentifier,powerIdentifier,rFModuleIdentifier , sModuleIdentifier ,txModuleIdentifier from UMTSSites";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet loadLIdentifiers() {

        ResultSet resultSet = null;
        try {

            Statement statement = connection.createStatement();
            String query = " Select id,eNodeBName,eNodeBCode,eNodeBNumberOfOnAirCells,eNodeBBW" +
                    ",rFModuleIdentifier , sModuleIdentifier  from LTESites";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

}
