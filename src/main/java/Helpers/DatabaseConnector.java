package Helpers;

import sample.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseConnector {
    private Connection connection;
    private static final String driver = "net.ucanaccess.jdbc.UcanaccessDriver";

    public DatabaseConnector(String dumpPath) {
        System.out.println("Starting connection" + Utils.getTime());
        this.connection = conn(dumpPath);
        System.out.println("got connection" + Utils.getTime());
    }

    private Connection conn(String dumpPath) {

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection
                    ("jdbc:ucanaccess://" + dumpPath +
                            ";keepMirror=true");
//            calendar = Calendar.getInstance();
//            System.out.println(calendar.getTime().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
//
//    public ArrayList<GSite> get2GSites(ArrayList<GSite> gSitesList) throws SQLException {
//
//        Statement statement = connection.createStatement();
//
//
//        String gQuery = "Select name,count(name),sum(T),first(BSCName),sum(C),sum(O),max(TX),sum(E),sum(G),sum(F),first(LAC),first(RAC),first(BSCId),first(status) from" +
//                "(Select BSCId,BCFId,count(TRXId) as T,sum(gprsEnabledTrx) as G from A_TRX group by BSCId,BCFId) as firstSet " +
//                "left join " +
//                "(Select BSCId,BCFId,name,adminState as status from A_BCF group by BSCId,BCFId) as secondSet " +
//                "on (firstSet.BSCId=secondSet.BSCId and firstSet.BCFId=secondSet.BCFId)" +
//                "left join " +
//                "left join " +
//                "(Select BSCId,name as BSCName from A_BSC) as thirdSet " +
//                "on (firstSet.BSCId=thirdSet.BSCId)" +
//                "left join " +
//                "(Select BSCId,BCFId,count(BTSId) as C,sum(adminState) as O,sum(frequencyBandInUse) as F ,first(locationAreaIdLAC) as LAC," +
//                " first(rac) as RAC from A_BTS group by BSCId,BCFId) as fourthSet " +
//                "on (firstSet.BSCId=fourthSet.BSCId and firstSet.BCFId=fourthSet.BCFId) " +
//                "left join " +
//                "(Select  BSCId,BCFId,max(channel0Pcm) as TX from A_TRX " +
//                "group by BSCId,BCFId) as fifthSet " +
//                "on (firstSet.BSCId=fifthSet.BSCId and firstSet.BCFId=fifthSet.BCFId)" +
//                "left join " +
//                "(Select BSCId,BCFId,count(channel0Pcm) as E from ( Select BSCId,BCFId,channel0Pcm from A_TRX " +
//                "where channel0Pcm not like '65535' group by BSCId,BCFId,channel0Pcm ) group by BSCId,BCFId ) as sixthSet " +
//                "on (firstSet.BSCId=sixthSet.BSCId and firstSet.BCFId=sixthSet.BCFId) " +
//                " group by name";
//        String gHardwareQuery = "Select name,first(ESMB),first(ESMC),first(FIQA),first(FIQB),first(FSMF),first(FTIF),first(FXDA),first(FXDB),first(FXEA),first(FXEB),first(FXX),first(FXED) " +
//                "from (" +
//                "(Select name from (Select BSCId,BCFId from A_TRX group by BSCId,BCFId) as A left join (Select BSCId,BCFId,name from A_BCF group by BSCId,BCFId) as B  " +
//                "on (A.BSCId=B.BSCId and A.BCFId=B.BCFId) group by name) as SiteSet  " +
//                "left join " +
//                "(Select siteName,count(unitTypeActual) as ESMB from 2GHW where unitTypeActual = 'ESMB' group by siteName) as  ESMBSet on (SiteSet.name=ESMBSet.siteName) " +
//                "left join " +
//                "(Select siteName,count(unitTypeActual) as ESMC from 2GHW where unitTypeActual = 'ESMC' group by siteName) as  ESMCSet on (SiteSet.name=ESMCSet.siteName) " +
//                "left join " +
//                "(Select siteName,count(unitTypeActual) as FIQA from 2GHW where unitTypeActual = 'FIQA' group by siteName) as  FIQASet on (SiteSet.name=FIQASet.siteName) " +
//                "left join " +
//                "(Select siteName,count(unitTypeActual) as FIQB from 2GHW where unitTypeActual = 'FIQB' group by siteName) as  FIQBSet on (SiteSet.name=FIQBSet.siteName) " +
//                "left join " +
//                "(Select siteName,count(unitTypeActual) as FSMF from 2GHW where unitTypeActual = 'FSMF' group by siteName) as  FSMFSet on (SiteSet.name=FSMFSet.siteName) " +
//                "left join " +
//                "(Select siteName,count(unitTypeActual) as FTIF from 2GHW where unitTypeActual = 'FTIF' group by siteName) as  FTIFSet on (SiteSet.name=FTIFSet.siteName) " +
//                "left join " +
//                "(Select siteName,count(unitTypeActual) as FXDA from 2GHW where unitTypeActual = 'FXDA' group by siteName) as  FXDASet on (SiteSet.name=FXDASet.siteName) " +
//                "left join " +
//                "(Select siteName,count(unitTypeActual) as FXDB from 2GHW where unitTypeActual = 'FXDB' group by siteName) as  FXDBSet on (SiteSet.name=FXDBSet.siteName) " +
//                "left join " +
//                "(Select siteName,count(unitTypeActual) as FXEA from 2GHW where unitTypeActual = 'FXEA' group by siteName) as  FXEASet on (SiteSet.name=FXEASet.siteName) " +
//                "left join " +
//                "(Select siteName,count(unitTypeActual) as FXEB from 2GHW where unitTypeActual = 'FXEB' group by siteName) as  FXEBSet on (SiteSet.name=FXEBSet.siteName) " +
//                "left join " +
//                "(Select siteName,count(unitTypeActual) as FXX from 2GHW where unitTypeActual = 'FXX' group by siteName) as  FXXSet on (SiteSet.name=FXXSet.siteName) " +
//                "left join " +
//                "(Select siteName,count(unitTypeActual) as FXED from 2GHW where unitTypeActual = 'FXED' group by siteName) as  FXEDSet on (SiteSet.name=FXEDSet.siteName) " +
//                ") group by name";
//        ResultSet gResultSet = statement.executeQuery(gQuery);
////        ResultSet hwResultSet = statement.executeQuery(gHardwareQuery);
//
//
//        while (gResultSet.next()) {
//            GSite site = new GSite();
//            site.setSiteName(gResultSet.getString(1));
//            site.setSiteNumberOfBCFs(gResultSet.getInt(2));
//            site.setSiteNumberOfTRXs(gResultSet.getInt(3));
//            site.setSiteBSCName(gResultSet.getString(4));
//            site.setSiteNumberOfCells(gResultSet.getInt(5));
//            site.setSiteNumberOfOnAirCells(gResultSet.getInt(6));
//            site.setSiteTxMode(gResultSet.getString(7));
//            site.setSiteNumberOfE1s(gResultSet.getInt(8));
//            site.setSiteNumberOfGTRXs(gResultSet.getInt(9));
//            site.setSiteNumberOfDcsCells(gResultSet.getInt(10));
//            site.setLac(gResultSet.getInt(11));
//            site.setRac(gResultSet.getInt(12));
//            site.setSiteBSCId(gResultSet.getString(13));
//            site.setOnAir(gResultSet.getInt(14));
//            site.finalizeProperties();
//            if (site.getSiteName() != null)
//                gSitesList.add(site);
//        }
//        return gSitesList;
//    }

    public ArrayList<USite> get3GSites(int ran, ArrayList<USite> uSitesList) throws SQLException {
        Statement statement = connection.createStatement();
        String uQuery, hwQuery;
        if (ran == 1) {
            uQuery = "Select  BTSAdditionalInfo,max(RncId),max(WBTSId),sum(C),sum(O),count(BTSAdditionalInfo),sum(fC),sum(onFC),sum(sC),sum(onSC),sum(tC),sum(onTC)\n" +
                    ",sum(uC),sum(onUC),max(I),first(V),sum(HD1),sum(HD2),sum(HD3),sum(HU1),sum(R99),count(HD1Count),count(HD2Count),count(HD3Count)," +
                    "count(HU1Count),sum(P),sum(S),first(Name),max(maxFPower),max(maxUPower),sum(vamF),sum(vamU),first(LAC),first(RAC) from \n" +
                    "(Select RncId,WBTSId,COCOId,BTSAdditionalInfo ,IubTransportMedia as I,NESWVersion as V,WBTSName as Name from A_WBTS ) as firstSet \n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,count(WBTSId) as C,sum(AdminCellState) as O, first(LAC) as LAC ,first(RAC) as RAC from A_WCEL group by RncId,WBTSId) as secondSet \n" +
                    "on (firstSet.RncId=secondSet.RncId and firstSet.WBTSId=secondSet.WBTSId) \n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,count(WBTSId) as fC,sum(AdminCellState) onFC from A_WCEL where UARFCN ='10612' group by RncId,WBTSId ) as thirdSet \n" +
                    "on (firstSet.RncId=thirdSet.RncId and firstSet.WBTSId=thirdSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,count(WBTSId) as sC,sum(AdminCellState) onSC from A_WCEL where UARFCN ='10637' group by RncId,WBTSId ) as fourthSet \n" +
                    "on (firstSet.RncId=fourthSet.RncId and firstSet.WBTSId=fourthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,count(WBTSId) as tC,sum(AdminCellState) onTC from A_WCEL where UARFCN ='10662' group by RncId,WBTSId ) fifthSet \n" +
                    "on (firstSet.RncId=fifthSet.RncId and firstSet.WBTSId=fifthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,count(WBTSId) as uC,sum(AdminCellState) onUC from A_WCEL where UARFCN ='2988' or UARFCN='3009' group by RncId,WBTSId ) as sixthSet\n" +
                    "on (firstSet.RncId=sixthSet.RncId and firstSet.WBTSId=sixthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                    "as R99,rfSharingEnabled as S  from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW ) as seventhSet \n" +
                    "on (firstSet.RncId=seventhSet.RncId and firstSet.WBTSId=seventhSet.WBTSId)\n" +
                    "left  join \n" +
                    "(Select RncId,WBTSId,numberOfHSDPASet1 as HD1Count \n" +
                    "from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW where numberOfHSDPASet1 ='-1') as eighthSet\n" +
                    "on (firstSet.RncId=eighthSet.RncId and firstSet.WBTSId=eighthSet.WBTSId)\n" +
                    "left  join \n" +
                    "(Select RncId,WBTSId,numberOfHSDPASet2 as HD2Count \n" +
                    "from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW where numberOfHSDPASet2 ='-1') as ninthSet\n" +
                    "on (firstSet.RncId=ninthSet.RncId and firstSet.WBTSId=ninthSet.WBTSId)\n" +
                    "left  join \n" +
                    "(Select RncId,WBTSId,numberOfHSDPASet3 as HD3Count \n" +
                    "from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW where numberOfHSDPASet3 ='-1') as tenthSet\n" +
                    "on (firstSet.RncId=tenthSet.RncId and firstSet.WBTSId=tenthSet.WBTSId)\n" +
                    "left  join \n" +
                    "(Select RncId,WBTSId,numberOfHSUPASet1 as HU1Count \n" +
                    "from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW where numberOfHSUPASet1 ='-1') as eleventhSet\n" +
                    "on (firstSet.RncId=eleventhSet.RncId and firstSet.WBTSId=eleventhSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId ,WBTSId,COCOId from A_WBTS ) as twelvthSet\n" +
                    "on (firstSet.RncId=twelvthSet.RncId and firstSet.WBTSId=twelvthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId ,COCOId,P from\n" +
                    "(Select RncId ,COCOId,max(cast (AAL2UPPCR01Egr as int)) as P from A_COCO_AAL2TP  group by RncId,COCOId)) as thirteenthSet\n" +
                    "on (firstSet.RncId=thirteenthSet.RncId and firstSet.COCOId=thirteenthSet.COCOId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,max(MaxDLPowerCapability) as maxFPower from A_WCEL where ( UARFCN ='10612') and ( MaxDLPowerCapability not like '65535' )" +
                    "group by RncId,WBTSId ) as fourteenthSet \n" +
                    "on (firstSet.RncId=fourteenthSet.RncId and firstSet.WBTSId=fourteenthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,max(MaxDLPowerCapability) as maxUPower from A_WCEL where ( UARFCN ='2988' or UARFCN ='3009') " +
                    "and ( MaxDLPowerCapability not like '65535')  group by RncId,WBTSId ) as fifteenthSet \n" +
                    "on (firstSet.RncId=fifteenthSet.RncId and firstSet.WBTSId=fifteenthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,sum(vamEnabled) as vamF from A_WBTSF_WBTS_MRBTS_BTSSCW_LCELW where defaultCarrier ='10612' group by RncId,WBTSId ) as sixteenthSet \n" +
                    "on (firstSet.RncId=sixteenthSet.RncId and firstSet.WBTSId=sixteenthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,sum(vamEnabled) as vamU from A_WBTSF_WBTS_MRBTS_BTSSCW_LCELW  where defaultCarrier ='2988' or defaultCarrier= '3009' group by RncId,WBTSId ) as seventeenthSet \n" +
                    "on (firstSet.RncId=seventeenthSet.RncId and firstSet.WBTSId=seventeenthSet.WBTSId)\n" +
                    " group by BTSAdditionalInfo";
        } else {
            uQuery = "Select  BTSAdditionalInfo,max(RncId),max(WBTSId),sum(C),sum(O),count(BTSAdditionalInfo),sum(fC),sum(onFC),sum(sC),sum(onSC),sum(tC),sum(onTC)\n" +
                    ",sum(uC),sum(onUC),max(I),first(V),sum(HD1),sum(HD2),sum(HD3),sum(HU1),sum(R99),count(HD1Count),count(HD2Count),count(HD3Count)," +
                    "count(HU1Count),sum(P),sum(S),first(Name),max(maxFPower),max(maxUPower),sum(vamF),sum(vamU),first(LAC),first(RAC) from \n" +
                    "(Select RncId,WBTSId,COCOId,BTSAdditionalInfo ,IubTransportMedia as I,NESWVersion as V,WBTSName as Name from A_WBTS ) as firstSet \n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,count(WBTSId) as C,sum(AdminCellState) as O, first(LAC) as LAC ,first(RAC) as RAC from A_WCEL group by RncId,WBTSId) as secondSet \n" +
                    "on (firstSet.RncId=secondSet.RncId and firstSet.WBTSId=secondSet.WBTSId) \n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,count(WBTSId) as fC,sum(AdminCellState) onFC from A_WCEL where UARFCN ='10612' group by RncId,WBTSId ) as thirdSet \n" +
                    "on (firstSet.RncId=thirdSet.RncId and firstSet.WBTSId=thirdSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,count(WBTSId) as sC,sum(AdminCellState) onSC from A_WCEL where UARFCN ='10637' group by RncId,WBTSId ) as fourthSet \n" +
                    "on (firstSet.RncId=fourthSet.RncId and firstSet.WBTSId=fourthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,count(WBTSId) as tC,sum(AdminCellState) onTC from A_WCEL where UARFCN ='10662' group by RncId,WBTSId ) fifthSet \n" +
                    "on (firstSet.RncId=fifthSet.RncId and firstSet.WBTSId=fifthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,count(WBTSId) as uC,sum(AdminCellState) onUC from A_WCEL where UARFCN ='2988' or UARFCN='3009' group by RncId,WBTSId ) as sixthSet\n" +
                    "on (firstSet.RncId=sixthSet.RncId and firstSet.WBTSId=sixthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                    "as R99,rfSharingEnabled as S  from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW ) as seventhSet \n" +
                    "on (firstSet.RncId=seventhSet.RncId and firstSet.WBTSId=seventhSet.WBTSId)\n" +
                    "left  join \n" +
                    "(Select RncId,WBTSId,numberOfHSDPASet1 as HD1Count \n" +
                    "from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW where numberOfHSDPASet1 ='-1') as eighthSet\n" +
                    "on (firstSet.RncId=eighthSet.RncId and firstSet.WBTSId=eighthSet.WBTSId)\n" +
                    "left  join \n" +
                    "(Select RncId,WBTSId,numberOfHSDPASet2 as HD2Count \n" +
                    "from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW where numberOfHSDPASet2 ='-1') as ninthSet\n" +
                    "on (firstSet.RncId=ninthSet.RncId and firstSet.WBTSId=ninthSet.WBTSId)\n" +
                    "left  join \n" +
                    "(Select RncId,WBTSId,numberOfHSDPASet3 as HD3Count \n" +
                    "from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW where numberOfHSDPASet3 ='-1') as tenthSet\n" +
                    "on (firstSet.RncId=tenthSet.RncId and firstSet.WBTSId=tenthSet.WBTSId)\n" +
                    "left  join \n" +
                    "(Select RncId,WBTSId,numberOfHSUPASet1 as HU1Count \n" +
                    "from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW where numberOfHSUPASet1 ='-1') as eleventhSet\n" +
                    "on (firstSet.RncId=eleventhSet.RncId and firstSet.WBTSId=eleventhSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId ,WBTSId,COCOId from A_WBTS ) as twelvthSet\n" +
                    "on (firstSet.RncId=twelvthSet.RncId and firstSet.WBTSId=twelvthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId ,COCOId,P from\n" +
                    "(Select RncId ,COCOId,max(cast (AAL2UPPCR01Egr as int)) as P from A_COCO_AAL2TP  group by RncId,COCOId)) as thirteenthSet\n" +
                    "on (firstSet.RncId=thirteenthSet.RncId and firstSet.COCOId=thirteenthSet.COCOId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,max(MaxDLPowerCapability) as maxFPower from A_WCEL where UARFCN ='10612' and MaxDLPowerCapability not like '65535' " +
                    "group by RncId,WBTSId ) as fourteenthSet \n" +
                    "on (firstSet.RncId=fourteenthSet.RncId and firstSet.WBTSId=fourteenthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,max(MaxDLPowerCapability) as maxUPower from A_WCEL where ( UARFCN ='2988' or UARFCN ='3009') " +
                    "and MaxDLPowerCapability not like '65535' group by RncId,WBTSId ) as fifteenthSet \n" +
                    "on (firstSet.RncId=fifteenthSet.RncId and firstSet.WBTSId=fifteenthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,sum(vamEnabled) as vamF from A_WBTSF_WBTS_MRBTS_BTSSCW_LCELW  where defaultCarrier ='10612' group by RncId,WBTSId ) as sixteenthSet \n" +
                    "on (firstSet.RncId=sixteenthSet.RncId and firstSet.WBTSId=sixteenthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,sum(vamEnabled) as vamU from A_WBTSF_WBTS_MRBTS_BTSSCW_LCELW  where defaultCarrier ='2988' or defaultCarrier= '3009' group by RncId,WBTSId ) as seventeenthSet \n" +
                    "on (firstSet.RncId=seventeenthSet.RncId and firstSet.WBTSId=seventeenthSet.WBTSId)\n" +
                    " group by BTSAdditionalInfo";
        }
        hwQuery = "Select Code,first(FBBA),first(FRGC),first(FRGD),first(FRGF),first(FRGL),first(FRGM),first(FRGP),first(FRGT),first(FRGU),first(FRGX),first(FSMB),first(FSMD)," +
                "first(FSME),first(FSMF),first(FTIA),first(FTIB),first(FTIF),first(FTPB),first(FXDA),first(FXDB) " +
                " from (" +
                "(Select BTSAdditionalInfo as Code from A_WBTS ) as SiteSet " +
                "left join\n" +
                "(Select SiteCode,count(HWType) as FBBA from 3GHW where HWType = 'FBBA' group by SiteCode) as  FBBASet on (SiteSet.Code=FBBASet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGC from 3GHW where HWType = 'FRGC' group by SiteCode) as  FRGCSet  on (SiteSet.Code=FRGCSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGD from 3GHW where HWType = 'FRGD' group by SiteCode) as  FRGDSet  on (SiteSet.Code=FRGDSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGF from 3GHW where HWType = 'FRGF' group by SiteCode) as  FRGFSet  on (SiteSet.Code=FRGFSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGL from 3GHW where HWType = 'FRGL' group by SiteCode) as  FRGLSet  on (SiteSet.Code=FRGLSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGM from 3GHW where HWType = 'FRGM' group by SiteCode) as  FRGMSet  on (SiteSet.Code=FRGMSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGP from 3GHW where HWType = 'FRGP' group by SiteCode) as  FRGPSet  on (SiteSet.Code=FRGPSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGT from 3GHW where HWType = 'FRGT' group by SiteCode) as  FRGTSet  on (SiteSet.Code=FRGTSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGU from 3GHW where HWType = 'FRGU' group by SiteCode) as  FRGUSet  on (SiteSet.Code=FRGUSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGX from 3GHW where HWType = 'FRGX' group by SiteCode) as  FRGXSet  on (SiteSet.Code=FRGXSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FSMB from 3GHW where HWType = 'FSMB' group by SiteCode) as  FSMBSet  on (SiteSet.Code=FSMBSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FSMD from 3GHW where HWType = 'FSMD' group by SiteCode) as  FSMDSet  on (SiteSet.Code=FSMDSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FSME from 3GHW where HWType = 'FSME' group by SiteCode) as  FSMESet  on (SiteSet.Code=FSMESet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FSMF from 3GHW where HWType = 'FSMF' group by SiteCode) as  FSMFSet  on (SiteSet.Code=FSMFSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FTIA from 3GHW where HWType = 'FTIA' group by SiteCode) as  FTIASet  on (SiteSet.Code=FTIASet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FTIB from 3GHW where HWType = 'FTIB' group by SiteCode) as  FTIBSet  on (SiteSet.Code=FTIBSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FTIF from 3GHW where HWType = 'FTIF' group by SiteCode) as  FTIFSet  on (SiteSet.Code=FTIFSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FTPB from 3GHW where HWType = 'FTPB' group by SiteCode) as  FTPBSet  on (SiteSet.Code=FTPBSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FXDA from 3GHW where HWType = 'FXDA' group by SiteCode) as  FXDASet  on (SiteSet.Code=FXDASet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FXDB from 3GHW where HWType = 'FXDB' group by SiteCode) as  FXDBSet  on (SiteSet.Code=FXDBSet.SiteCode) " +
                ") group by Code";
        ResultSet uResultSet = statement.executeQuery(uQuery);
//        ResultSet hwResultSet = statement.executeQuery(hwQuery);

        while (uResultSet.next()) {
            USite site = new USite();
            site.setSiteCode(uResultSet.getString(1));
            site.setSiteRncId(uResultSet.getString(2));
            site.setSiteWbtsId(uResultSet.getString(3));
            site.setSiteNumberOfCells(uResultSet.getInt(4));
            site.setSiteNumberOfOnAirCells(uResultSet.getInt(5));
            site.setSiteNumberOfNodeBs(uResultSet.getInt(6));
            site.setSiteNumberOfFirstCarriersCells(uResultSet.getInt(7));
            site.setSiteNumberOfOnAirFirstCarriersCells(uResultSet.getInt(8));
            site.setSiteNumberOfSecondCarriersCells(uResultSet.getInt(9));
            site.setSiteNumberOfOnAirSecondCarriersCells(uResultSet.getInt(10));
            site.setSiteNumberOfThirdCarriersCells(uResultSet.getInt(11));
            site.setSiteNumberOfOnAirThirdCarriersCells(uResultSet.getInt(12));
            site.setSiteNumberOfU900CarriersCells(uResultSet.getInt(13));
            site.setSiteNumberOfOnAirU900CarriersCells(uResultSet.getInt(14));
            site.setSiteTxMode(uResultSet.getString(15));
            site.setSiteVersion(uResultSet.getString(16));
            site.setSiteNumberOfHSDPASet1(uResultSet.getInt(17) + uResultSet.getInt(22));
            site.setSiteNumberOfHSDPASet2(uResultSet.getInt(18) + uResultSet.getInt(23));
            site.setSiteNumberOfHSDPASet3(uResultSet.getInt(19) + uResultSet.getInt(24));
            site.setSiteNumberOfHSUPASet1(uResultSet.getInt(20) + uResultSet.getInt(25));
            site.setSiteNumberOfChannelElements(uResultSet.getInt(21));
            site.setSiteNumberOfE1s((int) Math.ceil(uResultSet.getInt(26) / 4490.0));
            site.setRfSharing(uResultSet.getInt(27));
            site.setSiteName(uResultSet.getString(28));
            site.setSitePower(uResultSet.getInt(29), uResultSet.getInt(31));
            site.setSiteU900Power(uResultSet.getInt(30), uResultSet.getInt(32));
            site.setLac(uResultSet.getInt(33));
            site.setRac(uResultSet.getInt(34));
//            USite.UHardware uHardware = new USite.UHardware(hwResultSet.getInt(2), hwResultSet.getInt(3), hwResultSet.getInt(4),
//                    hwResultSet.getInt(5), hwResultSet.getInt(6), hwResultSet.getInt(7), hwResultSet.getInt(8),
//                    hwResultSet.getInt(9), hwResultSet.getInt(10), hwResultSet.getInt(11),
//                    hwResultSet.getInt(12), hwResultSet.getInt(13), hwResultSet.getInt(14),
//                    hwResultSet.getInt(15), hwResultSet.getInt(16), hwResultSet.getInt(17),
//                    hwResultSet.getInt(18), hwResultSet.getInt(19), hwResultSet.getInt(20), hwResultSet.getInt(21));
//            site.setUHardware(uHardware);
            site.finalizeProperties();
            uSitesList.add(site);
        }
        System.out.println(uSitesList.size());
        return uSitesList;
    }


    public ArrayList<USite> getThirdCarrierSites() throws SQLException {
        ArrayList<USite> thirdCarrierList = new ArrayList<>();
        Statement statement = connection.createStatement();
        String thirdCarrierQuery = "Select  BTSAdditionalInfo,min(RncId),first(WBTSName) from " +
                "(Select RncId,WBTSId from A_WCEL where UARFCN='10662' and AdminCellState ='1' )" +
                "left join A_WBTS using (RncId,WBTSId) group by BTSAdditionalInfo";
        ResultSet thirdCarrierResultSet = statement.executeQuery(thirdCarrierQuery);

        while (thirdCarrierResultSet.next()) {
            USite site = new USite();
            site.setSiteCode(thirdCarrierResultSet.getString(1));
            site.setSiteRncId(thirdCarrierResultSet.getString(2));
            site.setSiteName(thirdCarrierResultSet.getString(3));
            thirdCarrierList.add(site);
        }
        return thirdCarrierList;
    }

    public ArrayList<USite> getU900List() throws SQLException {
        ArrayList<USite> u900List = new ArrayList<>();
        Statement statement = connection.createStatement();
        String u900Query = "Select  BTSAdditionalInfo,min(RncId),first(WBTSName) from " +
                "(Select RncId,WBTSId from A_WCEL where UARFCN='2988' and AdminCellState ='1' )" +
                "left join A_WBTS using (RncId,WBTSId) group by BTSAdditionalInfo";
        ResultSet u900ResultSet = statement.executeQuery(u900Query);

        while (u900ResultSet.next()) {
            USite site = new USite();
            site.setSiteCode(u900ResultSet.getString(1));
            site.setSiteRncId(u900ResultSet.getString(2));
            site.setSiteName(u900ResultSet.getString(3));
            u900List.add(site);
        }
        return u900List;
    }

    public ArrayList<Cabinet> get2GBCFs() {
        ArrayList<Cabinet> bcfs = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "Select BSCId,BCFId,first(name),first(T),first(BSCName),first(newCellCount),first(newOnAirCount),first(TX),first(E),first(G),first(newDCount),first(LAC),first(RAC)," +
                    "first(status), first(version) , first(ctrlIP) , first(manIp) , first(etp) , first(gConf) , first(dConf) " +
                    " from" +
                    "(Select BSCId,BCFId,count(TRXId) as T,sum(gprsEnabledTrx) as G from A_TRX group by BSCId,BCFId) as firstSet " +
                    "left join " +
                    "(Select BSCId,BCFId,name,adminState as status,bcfPackVers as version,btsCuPlaneIpAddress as ctrlIP,btsMPlaneIpAddress as manIp, usedEtpId as etp " +
                    "from A_BCF group by BSCId,BCFId) as secondSet " +
                    "on (firstSet.BSCId=secondSet.BSCId and firstSet.BCFId=secondSet.BCFId)" +
                    "left join " +
                    "(Select BSCId,name as BSCName from A_BSC) as thirdSet " +
                    "on (firstSet.BSCId=thirdSet.BSCId)" +
                    "left join " +
                    "(Select BSCId,BCFId,count(BTSId) as C,sum(adminState) as O,sum(frequencyBandInUse) as F ,first(locationAreaIdLAC) as LAC," +
                    " first(rac) as RAC from A_BTS group by BSCId,BCFId) as fourthSet " +
                    "on (firstSet.BSCId=fourthSet.BSCId and firstSet.BCFId=fourthSet.BCFId) " +
                    "left join " +
                    "(Select  BSCId,BCFId,max(channel0Pcm) as TX from A_TRX " +
                    "group by BSCId,BCFId) as fifthSet " +
                    "on (firstSet.BSCId=fifthSet.BSCId and firstSet.BCFId=fifthSet.BCFId)" +
                    "left join " +
                    "(Select BSCId,BCFId,count(channel0Pcm) as E from ( Select BSCId,BCFId,channel0Pcm from A_TRX " +
                    "where channel0Pcm not like '65535' group by BSCId,BCFId,channel0Pcm ) group by BSCId,BCFId ) as sixthSet " +
                    "on (firstSet.BSCId=sixthSet.BSCId and firstSet.BCFId=sixthSet.BCFId) " +
                    " left join " +
                    "(SELECT BSCId,BCFId,GROUP_CONCAT(gT SEPARATOR'/ ') as gConf " +
                    "FROM " +
                    "(Select BSCId,BCFId,segmentId,count(initialFrequency) as gT " +
                    "from " +
                    "(Select BSCId,BCFId,BTSId,initialFrequency from A_TRX where initialFrequency < '600' ) as xGSet " +
                    "left join " +
                    "(Select BSCId,BCFId,BTSId,segmentId from A_BTS ) as bGSet " +
                    "on (xGSet.BSCId=bGSet.BSCId and xGSet.BTSId=bGSet.BTSId ) " +
                    "GROUP BY BSCId,BCFId,segmentId ) " +
                    "GROUP BY BSCId,BCFId) " +
                    " as seventhSet " +
                    "on (firstSet.BSCId=seventhSet.BSCId and firstSet.BCFId=seventhSet.BCFId) " +
                    " left join " +
                    "(SELECT BSCId,BCFId,GROUP_CONCAT(dT SEPARATOR'/ ') as dConf " +
                    "FROM " +
                    "(Select BSCId,BCFId,segmentId,count(initialFrequency) as dT " +
                    "from " +
                    "(Select BSCId,BCFId,BTSId,initialFrequency from A_TRX where initialFrequency > '600' ) as xDSet " +
                    "left join " +
                    "(Select BSCId,BCFId,BTSId,segmentId from A_BTS ) as bDSet " +
                    "on (xDSet.BSCId=bDSet.BSCId and xDSet.BTSId=bDSet.BTSId ) " +
                    "GROUP BY BSCId,BCFId,segmentId ) " +
                    "GROUP BY BSCId,BCFId) " +
                    " as eighthSet " +
                    "on (firstSet.BSCId=eighthSet.BSCId and firstSet.BCFId=eighthSet.BCFId) " +
                    "left join " +
                    "(select BSCId,BCFId, count(distinct CONCAT(segmentId, '_', frequencyBandInUse)) as newCellCount from A_BTS GROUP by BSCId,BCFId) as ninthSet " +
                    "on (firstSet.BSCId=ninthSet.BSCId and firstSet.BCFId=ninthSet.BCFId)" +
                    "left join " +
                    "(select BSCId,BCFId, count(distinct CONCAT(segmentId, '_', frequencyBandInUse)) as newOnAirCount from A_BTS where adminState = '1' GROUP by BSCId,BCFId)" +
                    " as tenthSet " +
                    "on (firstSet.BSCId=tenthSet.BSCId and firstSet.BCFId=tenthSet.BCFId)" +
                    "left join " +
                    "(select BSCId,BCFId, count(distinct CONCAT(segmentId, '_', frequencyBandInUse)) as newDCount from A_BTS where " +
                    "frequencyBandInUse = '1' GROUP by BSCId,BCFId)" +
                    " as eleventhSet " +
                    "on (firstSet.BSCId=eleventhSet.BSCId and firstSet.BCFId=eleventhSet.BCFId)" +
                    " group by BSCId,BCFId";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                BCF bcf = new BCF();
                bcf.setKey(resultSet.getString(1), resultSet.getString(2));
                bcf.setName(resultSet.getString(3));
                bcf.setNumberOfTRXs(resultSet.getInt(4));
                bcf.setBSCName(resultSet.getString(5));
//                bcf.setNumberOfCells(resultSet.getInt(6));
//                bcf.setNumberOfOnAirCells(resultSet.getInt(7));
                bcf.setNewCellCount(resultSet.getInt(6));
                bcf.setNewOnAirCount(resultSet.getInt(7));
                bcf.setTxMode(resultSet.getString(8));
                bcf.setNumberOfE1s(resultSet.getInt(9));
                bcf.setNumberOfGTRXs(resultSet.getInt(10));
//                bcf.setNumberOfDcsCells(resultSet.getInt(11));
                bcf.setNewDCount(resultSet.getInt(11));
                bcf.setLac(resultSet.getString(12));
                bcf.setRac(resultSet.getString(13));
                bcf.setOnAir(resultSet.getInt(14));
                bcf.setVersion(resultSet.getString(15));
                bcf.setCtrlIp(resultSet.getString(16));
                bcf.setManIp(resultSet.getString(17));
                bcf.setUsedETP(resultSet.getString(18));
                bcf.setgConf(resultSet.getString(19));
                bcf.setdConf(resultSet.getString(20));


                bcf.finishProperties();
                bcfs.add(bcf);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Number of BCFs: " + bcfs.size());
        return bcfs;
    }

    public ArrayList<Cabinet> getNodeBs() throws Exception {

        HashMap<String, NodeConfiguration> uSectorsConfiguration = getUSectorsConfiguration();

        Statement statement = connection.createStatement();
        String uQuery;
        uQuery = "Select  BTSAdditionalInfo,RncId,WBTSId,C,O,BTSAdditionalInfo,fC,onFC,sC,onSC,tC,onTC\n" +
                ",uC,onUC,I,V,HD1,HD2,HD3,HU1,R99,P,S,Name,maxFPower,maxUPower," +
                "vamF,vamU,LAC,RAC,IP,u2C,onU2C,sfp,lcg,noOfChains from (" +
                "(Select RncId,WBTSId,COCOId,BTSAdditionalInfo ,IubTransportMedia as I,NESWVersion as V,WBTSName as Name,BTSIPAddress as IP from A_WBTS ) as firstSet \n" +
                "left join\n" +
                "(Select RncId,WBTSId,count(WBTSId) as C,sum(AdminCellState) as O, first(LAC) as LAC ,first(RAC) as RAC from A_WCEL group by RncId,WBTSId) as secondSet \n" +
                "on (firstSet.RncId=secondSet.RncId and firstSet.WBTSId=secondSet.WBTSId) \n" +
                "left join\n" +
                "(Select RncId,WBTSId,count(WBTSId) as fC,sum(AdminCellState) onFC from A_WCEL where UARFCN ='10612' group by RncId,WBTSId ) as thirdSet \n" +
                "on (firstSet.RncId=thirdSet.RncId and firstSet.WBTSId=thirdSet.WBTSId)\n" +
                "left join\n" +
                "(Select RncId,WBTSId,count(WBTSId) as sC,sum(AdminCellState) onSC from A_WCEL where UARFCN ='10637' group by RncId,WBTSId ) as fourthSet \n" +
                "on (firstSet.RncId=fourthSet.RncId and firstSet.WBTSId=fourthSet.WBTSId)\n" +
                "left join\n" +
                "(Select RncId,WBTSId,count(WBTSId) as tC,sum(AdminCellState) onTC from A_WCEL where UARFCN ='10662' group by RncId,WBTSId ) fifthSet \n" +
                "on (firstSet.RncId=fifthSet.RncId and firstSet.WBTSId=fifthSet.WBTSId)\n" +
                "left join\n" +
                "(Select RncId,WBTSId,count(WBTSId) as uC,sum(AdminCellState) onUC from A_WCEL where UARFCN ='2988' group by RncId,WBTSId ) as sixthSet\n" +
                "on (firstSet.RncId=sixthSet.RncId and firstSet.WBTSId=sixthSet.WBTSId)\n" +
                "left join\n" +
                "(Select RncId,WBTSId,count(WBTSId) as u2C,sum(AdminCellState) onU2C from A_WCEL where UARFCN='3009' group by RncId,WBTSId ) as eighteenthSet\n" +
                "on (firstSet.RncId=eighteenthSet.RncId and firstSet.WBTSId=eighteenthSet.WBTSId)\n" +
                "left join\n" +
                "(Select RncId,WBTSId,GROUP_CONCAT(linkSpeed SEPARATOR'/ ') as sfp from A_WBTSF_BTSSCW_CABLINGLIST group by RncId,WBTSId ) as nineteenthSet \n" +
                "on (firstSet.RncId=nineteenthSet.RncId and firstSet.WBTSId=nineteenthSet.WBTSId)\n" +
                "left join\n" +
                "(Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                "as R99,rfSharingEnabled as S  from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW  ) as seventhSet \n" +
                "on (firstSet.RncId=seventhSet.RncId and firstSet.WBTSId=seventhSet.WBTSId)\n" +
                "left join\n" +
                "(Select RncId,WBTSId,count(WBTSId) as lcg from A_WBTSF_WBTS_MRBTS_BTSSCW_LCELGW group by RncId,WBTSId ) lcgSet " +
                "on (firstSet.RncId=lcgSet.RncId and firstSet.WBTSId=lcgSet.WBTSId) " +
                "left join\n" +
                "(Select RncId,WBTSId,count(positionInChain) as noOfChains from A_WBTSF_RMOD_CONNECTIONLIST " +
                "where positionInChain = '2' group by RncId,WBTSId ) chainSet " +
                "on (firstSet.RncId=chainSet.RncId and firstSet.WBTSId=chainSet.WBTSId) " +
                "left join\n" +
                "(Select RncId ,WBTSId,COCOId from A_WBTS ) as twelvthSet\n" +
                "on (firstSet.RncId=twelvthSet.RncId and firstSet.WBTSId=twelvthSet.WBTSId)\n" +
                "left join\n" +
                "(Select RncId ,COCOId,P from\n" +
                "(Select RncId ,COCOId,max(cast (AAL2UPPCR01Egr as int)) as P from A_COCO_AAL2TP  group by RncId,COCOId)) as thirteenthSet\n" +
                "on (firstSet.RncId=thirteenthSet.RncId and firstSet.COCOId=thirteenthSet.COCOId)\n" +
                "left join\n" +
                "(Select RncId,WBTSId,max(MaxDLPowerCapability) as maxFPower from A_WCEL where ( UARFCN ='10612') and ( MaxDLPowerCapability not like '65535' )" +
                "group by RncId,WBTSId ) as fourteenthSet \n" +
                "on (firstSet.RncId=fourteenthSet.RncId and firstSet.WBTSId=fourteenthSet.WBTSId)\n" +
                "left join\n" +
                "(Select RncId,WBTSId,max(MaxDLPowerCapability) as maxUPower from A_WCEL where ( UARFCN ='2988' or UARFCN ='3009') " +
                "and ( MaxDLPowerCapability not like '65535')  group by RncId,WBTSId ) as fifteenthSet \n" +
                "on (firstSet.RncId=fifteenthSet.RncId and firstSet.WBTSId=fifteenthSet.WBTSId)\n" +
                "left join\n" +
                "(Select RncId,WBTSId,sum(vamEnabled) as vamF from A_WBTSF_WBTS_MRBTS_BTSSCW_LCELW   where defaultCarrier ='10612' group by RncId,WBTSId ) as sixteenthSet \n" +
                "on (firstSet.RncId=sixteenthSet.RncId and firstSet.WBTSId=sixteenthSet.WBTSId)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,sum(vamEnabled) as vamU from A_WBTSF_WBTS_MRBTS_BTSSCW_LCELW where defaultCarrier ='2988' or defaultCarrier= '3009' group by RncId,WBTSId ) as seventeenthSet \n" +
                "on (firstSet.RncId=seventeenthSet.RncId and firstSet.WBTSId=seventeenthSet.WBTSId) ) where C  != '0' ";
//        }

        ResultSet nResultSet = statement.executeQuery(uQuery);
        ArrayList<Cabinet> nodeBList = new ArrayList<>();
        while (nResultSet.next()) {
            NodeB nodeB = new NodeB();
            nodeB.setName(nResultSet.getString(24));
            nodeB.setCode(nResultSet.getString(1));
            nodeB.setRncId(nResultSet.getString(2));
            nodeB.setWbtsId(nResultSet.getString(3));
            nodeB.setNumberOfCells(nResultSet.getInt(4));
            nodeB.setNumberOfOnAirCells(nResultSet.getInt(5));
//            nodeB.setNodeBNumberOfNodeBs(uResultSet.getInt(6));
            nodeB.setNumberOfFirstCarriersCells(nResultSet.getInt(7));
            nodeB.setNumberOfOnAirFirstCarriersCells(nResultSet.getInt(8));
            nodeB.setNumberOfSecondCarriersCells(nResultSet.getInt(9));
            nodeB.setNumberOfOnAirSecondCarriersCells(nResultSet.getInt(10));
            nodeB.setNumberOfThirdCarriersCells(nResultSet.getInt(11));
            nodeB.setNumberOfOnAirThirdCarriersCells(nResultSet.getInt(12));
            nodeB.setNumberOfFirstU900Cells(nResultSet.getInt(13));
            nodeB.setNumberOfOnAirFirstU900Cells(nResultSet.getInt(14));
            nodeB.setTxMode(nResultSet.getString(15));
            nodeB.setVersion(nResultSet.getString(16));
            nodeB.setNumberOfHSDPASet1(nResultSet.getInt(17));
            nodeB.setNumberOfHSDPASet2(nResultSet.getInt(18));
            nodeB.setNumberOfHSDPASet3(nResultSet.getInt(19));
            nodeB.setNumberOfHSUPASet1(nResultSet.getInt(20));
            nodeB.setNumberOfChannelElements(nResultSet.getInt(21));
            nodeB.setNumberOfE1s((int) Math.ceil(nResultSet.getInt(22) / 4490.0));
            nodeB.setRfSharing(nResultSet.getInt(23));
            nodeB.setPower(nResultSet.getInt(25), nResultSet.getInt(27));
            nodeB.setU900Power(nResultSet.getInt(26), nResultSet.getInt(28));
            nodeB.setLac(nResultSet.getString(29));
            nodeB.setRac(nResultSet.getString(30));
            nodeB.setNodeBIP(nResultSet.getString(31));
            nodeB.setNumberOfSecondU900Cells(nResultSet.getInt(32));
            nodeB.setNumberOfOnAirSecondU900Cells(nResultSet.getInt(33));
            nodeB.setSfp(nResultSet.getString(34));
            nodeB.setNumberOfLCGs(nResultSet.getInt(35));
            nodeB.setNumberOfChains(nResultSet.getInt(36));
            nodeB.finishProperties();

            nodeB.setConfiguration(uSectorsConfiguration.get(nodeB.getKey()));
            nodeBList.add(nodeB);
        }
        System.out.println("Number of NodeBs: " + nodeBList.size());
        return nodeBList;
    }

    public ArrayList<Cabinet> getEnodeBs() throws SQLException {

        String lQuery;
        Statement statement = connection.createStatement();
//        if (ran == 2) {

        lQuery = "Select mrbtsId,sum(C),sum(O),first(V),first(N),first(BW),first(M),sum(S),sum(SO),first(TAC),first(manIP),first(s1Ip),first(secIp),first(secGw) from " +
                "(Select mrbtsId,count(mrbtsId) as C,sum(administrativeState) as O, first(tac) as TAC from A_LTE_MRBTS_LNBTS_LNCEL group by mrbtsId) as firstSet " +
                "left join " +
                "(Select mrbtsId,first(name) as N from A_LTE_MRBTS_LNBTS group by mrbtsId) as secondSet " +
                "on (firstSet.mrbtsId=secondSet.mrbtsId) " +
                "left join " +
                "(Select mrbtsId,sum(actSuperCell) as SO from ( " +
                "(Select mrbtsId,lnCelId,administrativeState from A_LTE_MRBTS_LNBTS_LNCEL  ) as A " +
                "left join \n" +
                "(Select mrbtsId,lnCelId,actSuperCell from A_LTE_MRBTS_LNBTS_LNCEL_LNCEL_FDD ) as B " +
                "on(A.mrbtsId=B.mrbtsId and A.lnCelId=B.lnCelId) ) where administrativeState = '1' group by mrbtsId) as thirdSet " +
                "on (firstSet.mrbtsId=thirdSet.mrbtsId) " +
                "left join " +
                "(Select mrbtsId,max(dlChBw) as BW,max(dlMimoMode) as M,sum(actSuperCell) as S from A_LTE_MRBTS_LNBTS_LNCEL_LNCEL_FDD group by mrbtsId) as fourthSet " +
                "on (firstSet.mrbtsId=fourthSet.mrbtsId)  " +
                "left join " +
                "(Select mrbtsId,first(softwareReleaseVersion) as V from A_LTE_MRBTS_LNBTS_FTM group by mrbtsId) as fifthSet " +
                "on (firstSet.mrbtsId=fifthSet.mrbtsId) " +
                "left join " +
                "(Select mrbtsId,first(localIpAddress) as manIP from A_LTE_IPSECC_SECURITYPOLICIES where ipSecStatus ='2' group by mrbtsId) as sixthSet " +
                "on (firstSet.mrbtsId=sixthSet.mrbtsId) " +
                "left join " +
                "(Select mrbtsId,first(localIpAddress) as s1Ip,first(localTunnelEndpoint) as secIp,first(remoteTunnelEndpoint) as secGw from A_LTE_IPSECC_SECURITYPOLICIES" +
                " where ipSecStatus ='0' group by mrbtsId) as seventhSet " +
                "on (firstSet.mrbtsId=seventhSet.mrbtsId) " +
                "group by " +
                "mrbtsId";
//        }
//        else {
//            lQuery = "Select mrbtsId,sum(C),sum(O),first(V),first(N),first(BW),first(M),sum(S),sum(SO),first(TAC) from " +
//                    "(Select mrbtsId,count(mrbtsId) as C,sum(administrativeState) as O,sum(actSuperCell) as S from A_LTE_LNCEL group by mrbtsId ) as firstSet " +
//                    "left join " +
//                    "(Select mrbtsId,first(name) as N from A_LTE_LNBTS group by mrbtsId) as secondSet " +
//                    "on (firstSet.mrbtsId=secondSet.mrbtsId) " +
//                    "left join " +
//                    "(Select mrbtsId, sum(actSuperCell) as SO from A_LTE_LNCEL where administrativeState = '1'  group by mrbtsId) as thirdSet " +
//                    "on (firstSet.mrbtsId=thirdSet.mrbtsId) " +
//                    "left join " +
//                    "(Select mrbtsId,max(dlChBw) as BW,max(dlMimoMode) as M from A_LTE_LNCEL_PS group by mrbtsId) as fourthSet " +
//                    "on (firstSet.mrbtsId=fourthSet.mrbtsId) " +
//                    "left join " +
//                    "(Select mrbtsId,first(tac) as TAC from A_LTE_LNCEL_SIB group by mrbtsId) as fifthSet " +
//                    "on (firstSet.mrbtsId=fifthSet.mrbtsId) " +
//                    "left join " +
//                    "(Select mrbtsId,first(version) as V from A_LTE_MRBTS group by mrbtsId) as sixthSet " +
//                    "on (firstSet.mrbtsId=sixthSet.mrbtsId) " +
//                    "group by " +
//                    "mrbtsId";
        ArrayList<Cabinet> enodeBS = new ArrayList<>();
        ResultSet lResultSet = statement.executeQuery(lQuery);
        while (lResultSet.next()) {
            EnodeB eNodeB = new EnodeB();
            eNodeB.setENodeBId(lResultSet.getString(1));
            eNodeB.setNumberOfCells(lResultSet.getInt(2) + lResultSet.getInt(8));
            eNodeB.setNumberOfOnAirCells(lResultSet.getInt(3), lResultSet.getInt(9));
            eNodeB.setVersion(lResultSet.getString(4));
            eNodeB.setName(lResultSet.getString(5));
            eNodeB.setBw(lResultSet.getInt(6));
            eNodeB.setMimo(lResultSet.getInt(7));
            eNodeB.setTac(lResultSet.getString(10));
            eNodeB.setManIp(lResultSet.getString(11));
            eNodeB.setS1Ip(lResultSet.getString(12));
            eNodeB.setSecIp(lResultSet.getString(13));
            eNodeB.setSecGw(lResultSet.getString(14));
            eNodeB.finishProperties();
            enodeBS.add(eNodeB);
        }
        System.out.println("Number of eNodeBs: " + enodeBS.size());


        return enodeBS;
    }


    private HashMap<String, NodeConfiguration> getUSectorsConfiguration() throws SQLException {
        String uSecConfQuery;
        Statement statement = connection.createStatement();
        uSecConfQuery = "Select RncId,WBTSId,lCelwId,group_concat(antId),group_concat(rModId)," +
                "group_concat(prodCode),group_concat(sModId),group_concat(positionInChain),group_concat(linkId),first(defaultCarrier),first(SectorID) " +
                "FROM " + " ( " +
                "(SELECT RncId,WBTSId,lCelwId,antlId FROM A_WBTSF_LCELW_RESOURCELIST) as lCelSet " +
                "LEFT JOIN" +
                "(SELECT RncId,WBTSId,antId,antlId,rModId FROM A_WBTSF_RNC_WBTS_MRBTS_ANTL) as antSet " +
                "on (lCelSet.RncId=antSet.RncId and lCelSet.WBTSId=antSet.WBTSId and lCelSet.antlId=antSet.antlId) " +
                "left join " +
                "(SELECT RncId,WBTSId,rModId,linkId,positionInChain,sModId FROM A_WBTSF_RMOD_CONNECTIONLIST) as connectionSet " +
                "on (antSet.RncId=connectionSet.RncId and antSet.WBTSId=connectionSet.WBTSId and antSet.rModId=connectionSet.rModId) " +
                "left join " +
                "(SELECT RncId,WBTSId,rModId,prodCode,serNum FROM A_WBTSF_RNC_WBTS_MRBTS_RMOD) as rModSet " +
                "on (antSet.RncId=rModSet.RncId and antSet.WBTSId=rModSet.WBTSId and antSet.rModId=rModSet.rModId)" +
                "left join " +
                "(SELECT RncId,WBTSId,lCelwId,defaultCarrier FROM A_WBTSF_WBTS_MRBTS_BTSSCW_LCELW) as defaultCarrierSet " +
                "on (lCelSet.RncId=defaultCarrierSet.RncId and lCelSet.WBTSId=defaultCarrierSet.WBTSId and lCelSet.lCelwId=defaultCarrierSet.lCelwId)" +
                "left join " +
                "(SELECT RncId,WBTSId,LcrId,SectorID FROM A_WCEL_AC) as sectorIdSet " +
                "on (lCelSet.RncId=sectorIdSet.RncId and lCelSet.WBTSId=sectorIdSet.WBTSId and lCelSet.lCelwId=sectorIdSet.LcrId) " +
                ") " +
                " group by RncId,WBTSId,lCelwId";


        ResultSet nResultSet = statement.executeQuery(uSecConfQuery);
        HashMap<String, NodeConfiguration> uSectorConfs = new HashMap<>();
        String previousKey = "";
        NodeConfiguration recurringNodeConfiguration = null;
        while (nResultSet.next()) {
            String rncId = nResultSet.getString(1);
            String wbtsId = nResultSet.getString(2);
            String key = rncId + "_" + wbtsId;
            SectorConfiguration sectorConfiguration = new SectorConfiguration();
            sectorConfiguration.setlCellId(nResultSet.getString(3));
            sectorConfiguration.setAntId(nResultSet.getString(4));
            sectorConfiguration.setrModId(nResultSet.getString(5));
            sectorConfiguration.setProductCode(nResultSet.getString(6));
            sectorConfiguration.setsModId(nResultSet.getString(7));
            sectorConfiguration.setPosInChain(nResultSet.getString(8));
            sectorConfiguration.setLinkId(nResultSet.getString(9));
            sectorConfiguration.setDefaultCarrier(nResultSet.getString(10));
            sectorConfiguration.setSectorId(nResultSet.getString(11));
            sectorConfiguration.analyzeSector();

            if (!previousKey.equals(key)) {
                recurringNodeConfiguration = new NodeConfiguration(key);
                recurringNodeConfiguration.setKey(key);
                recurringNodeConfiguration.addSectorConfiguration(sectorConfiguration);
                uSectorConfs.put(key, recurringNodeConfiguration);
            } else {
                if (recurringNodeConfiguration != null) {
                    recurringNodeConfiguration.addSectorConfiguration(sectorConfiguration);
                }
            }
            previousKey = rncId + "_" + wbtsId;

        }
        System.out.println("Sec Conf " + uSectorConfs.size());
        return uSectorConfs;
    }


    public ResultSet getTRXSheet() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "Select BSCId,BCFId,BTSId,TRXId,channel0Pcm,channel0Tsl,lapdLinkName,tsc,trxRfPower,name,cellId,frequencyBandInUse,segmentId from " +
                "(Select BSCId,BCFId,BTSId,TRXId,channel0Pcm,channel0Tsl,lapdLinkName,tsc,trxRfPower from A_TRX ) as firstSet "
                + "left join "
                + "(Select BSCId,BCFId,name from A_BCF group by BSCId,BCFId )as secondSet "
                + "on (firstSet.BSCId=secondSet.BSCId and firstSet.BCFId=secondSet.BCFId) "
                + "left join "
                + "(Select BSCId,BTSId,cellId,frequencyBandInUse,segmentId from A_BTS )as thirdSet "
                + "on (firstSet.BSCId=thirdSet.BSCId and firstSet.BTSId=thirdSet.BTSId)";

        return statement.executeQuery(query);

    }

    public ResultSet getGcellsSheet() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "Select BSCId,BCFId,BTSId,adminState,cellId,frequencyBandInUse,locationAreaIdLAC,rac,segmentId,name,BSCName from " +
                "(Select BSCId,BCFId,BTSId,adminState,cellId,frequencyBandInUse,locationAreaIdLAC,rac,segmentId from A_BTS ) as firstSet "
                + "left join "
                + "(Select BSCId,BCFId,name from A_BCF group by BSCId,BCFId )as secondSet "
                + "on (firstSet.BSCId=secondSet.BSCId and firstSet.BCFId=secondSet.BCFId) "
                + "left join "
                + "(Select BSCId,name as BSCName from A_BSC) as thirdSet " +
                "on (firstSet.BSCId=thirdSet.BSCId)";

        return statement.executeQuery(query);
    }

    public ResultSet getUcellsSheet() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "Select BTSAdditionalInfo,name,RncId,WBTSId,AdminCellState,CId,LAC,MaxDLPowerCapability,RAC," +
                "UARFCN,cName from " +
                "(Select RncId,WBTSId,AdminCellState,CId,LAC,MaxDLPowerCapability,RAC,UARFCN,name as cName from A_WCEL ) as firstSet "
                + "left join "
                + "(Select RncId,WBTSId,BTSAdditionalInfo,name from A_WBTS )as secondSet "
                + "on (firstSet.RncId=secondSet.RncId and firstSet.WBTSId=secondSet.WBTSId)";
        return statement.executeQuery(query);
    }

    public ResultSet getLcellsSheet() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "Select name,mrbtsId,administrativeState,eutraCelId,tac,dlChBw,dlMimoMode,cellName from " +
                "(Select mrbtsId,administrativeState,cellName,eutraCelId,tac,lnCelId from A_LTE_MRBTS_LNBTS_LNCEL ) as firstSet " +
                "left join " +
                "(Select mrbtsId,name from A_LTE_MRBTS_LNBTS) as secondSet " +
                "on (firstSet.mrbtsId=secondSet.mrbtsId) " +
                "left join " +
                "(Select mrbtsId,dlChBw,dlMimoMode,lnCelId from A_LTE_MRBTS_LNBTS_LNCEL_LNCEL_FDD) as thirdSet " +
                "on (firstSet.mrbtsId=thirdSet.mrbtsId and firstSet.lnCelId=thirdSet.lnCelId) ";
        return statement.executeQuery(query);
    }
}