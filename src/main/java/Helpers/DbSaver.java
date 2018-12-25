package Helpers;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.management.remote.JMXConnectorFactory.connect;

import sample.GSite;
import sample.LSite;
import sample.USite;

/**
 * @author user
 */
public class DbSaver extends Thread {


    Connection connection;

    public Connection getConnection(String path) {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection
                    ("jdbc:sqlite:" + path, "r", "s");
            System.out.println("A new database has been created.");

            Statement st = conn.createStatement();
//        Statement L = conn.createStatement();
//        Statement U = conn.createStatement();

            String GSites = "create table if not exists GSMSites (id integer PRIMARY KEY,siteName text ,siteCode text,"
                    + "region text, siteBSCName text,siteBCFId text,siteVersion text,siteNumberOfBCFs integer,"
                    + " siteNumberOfTRXs integer, siteNumberOfSectors integer, siteNumberOfCells integer, "
                    + "siteNumberOfDcsCells integer, siteNumberOfGsmCells integer,siteNumberOfE1s integer,"
                    + " siteNumberOfOnAirCells integer, siteNumberOfGTRXs integer)";

            String LSites = "create table if not exists LTESites (id integer PRIMARY KEY, eNodeBName text, eNodeBCode text,"
                    + " eNodeBRegion text, eNodeBId text, eNodeBVersion text, eNodeBNumberOfSectors integer,"
                    + " eNodeBNumberOfCells integer, eNodeBNumberOfOnAirCells integer, eNodeBBW integer,"
                    + " eNodeBMimo integer)";

            String USites = "create table if not exists UMTSSites (id integer PRIMARY KEY,siteName text, siteCode text, "
                    + "siteRegion text, siteRncId text, siteWbtsId text, siteVersion text, siteNumberOfNodeBs integer,"
                    + " siteNumberOfSectors integer, siteNumberOfCells integer,siteNumberOfCarriers integer,"
                    + " siteNumberOfE1s integer, siteNumberOfOnAirCells integer, siteNumberOfFirstCarriersCells integer, "
                    + "siteNumberOfOnAirFirstCarriersCells integer,siteNumberOfSecondCarriersCells integer, "
                    + "siteNumberOfOnAirSecondCarriersCells integer, siteNumberOfThirdCarriersCells integer,"
                    + " siteNumberOfOnAirThirdCarriersCells integer,siteNumberOfU900CarriersCells integer, "
                    + "siteNumberOfOnAirU900CarriersCells integer, siteNumberOfOffAirCells integer,"
                    + "siteNumberOfHSDPASet1 integer, siteNumberOfHSDPASet2 integer, siteNumberOfHSDPASet3 integer,"
                    + " siteNumberOfHSUPASet1 integer, siteNumberOfChannelElements integer, sitePower double,"
                    + " siteU900Power double,processingSetsIdentifier double, channelElementsIdentifier double, "
                    + "carriersIdentifier double, powerIdentifier double,firstCarrier integer,u900 integer,"
                    + "rfSharing integer,standAloneU900 integer)";





            st.execute(GSites);
            st.execute(LSites);
            st.execute(USites);


        } catch (SQLException ex) {
            Logger.getLogger(DbSaver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conn;
    }

    public DbSaver(String path) {
        this.connection = getConnection(path);
    }


    public void insertUMTS(ArrayList<USite> uSitesList) {
        try {

            String sq = "INSERT INTO UMTSSites (id ,siteName , siteCode , "
                    + "siteRegion , siteRncId , siteWbtsId , siteVersion , siteNumberOfNodeBs ,"
                    + " siteNumberOfSectors , siteNumberOfCells ,siteNumberOfCarriers ,"
                    + " siteNumberOfE1s , siteNumberOfOnAirCells , siteNumberOfFirstCarriersCells , "
                    + "siteNumberOfOnAirFirstCarriersCells,siteNumberOfSecondCarriersCells , "
                    + "siteNumberOfOnAirSecondCarriersCells , siteNumberOfThirdCarriersCells ,"
                    + " siteNumberOfOnAirThirdCarriersCells ,siteNumberOfU900CarriersCells , "
                    + "siteNumberOfOnAirU900CarriersCells ,"
                    + "siteNumberOfHSDPASet1 , siteNumberOfHSDPASet2 , siteNumberOfHSDPASet3 ,"
                    + " siteNumberOfHSUPASet1 , siteNumberOfChannelElements , sitePower ,"
                    + " siteU900Power )VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                    + "?,?,?,?)";
            try (PreparedStatement pr = connection.prepareStatement(sq)) {
                for (int i = 0; i < uSitesList.size(); i++) {
                    pr.setInt(1, i + 1);
                    pr.setString(2, uSitesList.get(i).getSiteName());
                    pr.setString(3, uSitesList.get(i).getSiteCode());
                    pr.setString(4, uSitesList.get(i).getSiteRegion());
                    pr.setString(5, uSitesList.get(i).getSiteRncId());
                    pr.setString(6, uSitesList.get(i).getSiteWbtsId());
                    pr.setString(7, uSitesList.get(i).getSiteVersion());
                    pr.setInt(8, uSitesList.get(i).getSiteNumberOfNodeBs());
                    pr.setInt(9, uSitesList.get(i).getSiteNumberOfSectors());
                    pr.setInt(10, uSitesList.get(i).getSiteNumberOfCells());
                    pr.setInt(11, uSitesList.get(i).getSiteNumberOfCarriers());
                    pr.setInt(12, uSitesList.get(i).getSiteNumberOfE1s());
                    pr.setInt(13, uSitesList.get(i).getSiteNumberOfOnAirCells());
                    pr.setInt(14, uSitesList.get(i).getSiteNumberOfFirstCarriersCells());
                    pr.setInt(15, uSitesList.get(i).getSiteNumberOfOnAirFirstCarriersCells());
                    pr.setInt(16, uSitesList.get(i).getSiteNumberOfSecondCarriersCells());
                    pr.setInt(17, uSitesList.get(i).getSiteNumberOfOnAirSecondCarriersCells());
                    pr.setInt(18, uSitesList.get(i).getSiteNumberOfThirdCarriersCells());
                    pr.setInt(19, uSitesList.get(i).getSiteNumberOfOnAirThirdCarriersCells());
                    pr.setInt(20, uSitesList.get(i).getSiteNumberOfU900CarriersCells());
                    pr.setInt(21, uSitesList.get(i).getSiteNumberOfOnAirU900CarriersCells());
                    pr.setInt(22, uSitesList.get(i).getSiteNumberOfHSDPASet1());
                    pr.setInt(23, uSitesList.get(i).getSiteNumberOfHSDPASet2());
                    pr.setInt(24, uSitesList.get(i).getSiteNumberOfHSDPASet3());
                    pr.setInt(25, uSitesList.get(i).getSiteNumberOfHSUPASet1());
                    pr.setInt(26, uSitesList.get(i).getSiteNumberOfChannelElements());
                    pr.setDouble(27, uSitesList.get(i).getSitePower());
                    pr.setDouble(28, uSitesList.get(i).getSiteU900Power());
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

    public void insertGSM(ArrayList<GSite> gSitesList) {
        try {

            String sq = "INSERT INTO GSMSites (id,siteName  ,siteCode ,"
                    + "region , siteBSCName ,siteBCFId ,siteVersion ,siteNumberOfBCFs ,"
                    + " siteNumberOfTRXs , siteNumberOfSectors , siteNumberOfCells , "
                    + "siteNumberOfDcsCells , siteNumberOfGsmCells ,siteNumberOfE1s ,"
                    + " siteNumberOfOnAirCells , siteNumberOfGTRXs )VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement pr = connection.prepareStatement(sq)) {
                for (int i = 0; i < gSitesList.size(); i++) {
                    pr.setInt(1, i + 1);
                    pr.setString(2, gSitesList.get(i).getSiteName());
                    pr.setString(3, gSitesList.get(i).getSiteCode());
                    pr.setString(4, gSitesList.get(i).getRegion());
                    pr.setString(5, gSitesList.get(i).getSiteBSCName());
                    pr.setString(6, gSitesList.get(i).getSiteBCFId());
                    pr.setString(7, gSitesList.get(i).getSiteVersion());
                    pr.setInt(8, gSitesList.get(i).getSiteNumberOfBCFs());
                    pr.setInt(9, gSitesList.get(i).getSiteNumberOfTRXs());
                    pr.setInt(10, gSitesList.get(i).getSiteNumberOfSectors());
                    pr.setInt(11, gSitesList.get(i).getSiteNumberOfCells());
                    pr.setInt(12, gSitesList.get(i).getSiteNumberOfDcsCells());
                    pr.setInt(13, gSitesList.get(i).getSiteNumberOfGsmCells());
                    pr.setInt(14, gSitesList.get(i).getSiteNumberOfE1s());
                    pr.setInt(15, gSitesList.get(i).getSiteNumberOfOnAirCells());
                    pr.setInt(16, gSitesList.get(i).getSiteNumberOfGTRXs());
                    pr.executeUpdate();
                }
                pr.executeBatch();
                System.out.println("UMTS Table is done..");
                //connection.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insertLTE(ArrayList<LSite> lSitesList) {
        try {

            String sq = "INSERT INTO LTESites (id, eNodeBName , eNodeBCode , eNodeBRegion , eNodeBId , "
                    + "eNodeBVersion , eNodeBNumberOfSectors , eNodeBNumberOfCells , eNodeBNumberOfOnAirCells ,"
                    + " eNodeBBW , eNodeBMimo )VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement pr = connection.prepareStatement(sq)) {
                for (int i = 0; i < lSitesList.size(); i++) {
                    pr.setInt(1, i + 1);
                    pr.setString(2, lSitesList.get(i).getENodeBName());
                    pr.setString(3, lSitesList.get(i).getENodeBCode());
                    pr.setString(4, lSitesList.get(i).getENodeBRegion());
                    pr.setString(5, lSitesList.get(i).getENodeBId());
                    pr.setString(6, lSitesList.get(i).getENodeBVersion());
                    pr.setInt(7, lSitesList.get(i).getENodeBNumberOfSectors());
                    pr.setInt(8, lSitesList.get(i).getENodeBNumberOfCells());
                    pr.setInt(9, lSitesList.get(i).getENodeBNumberOfOnAirCells());
                    pr.setInt(10, lSitesList.get(i).getENodeBBW());
                    pr.setInt(11, lSitesList.get(i).getENodeBMimo());
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


}
