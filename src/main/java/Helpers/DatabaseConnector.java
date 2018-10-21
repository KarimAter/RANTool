package Helpers;

import sample.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseConnector {
    Connection connection;
    private static final String driver = "net.ucanaccess.jdbc.UcanaccessDriver";

    public DatabaseConnector(String dumpPath) {
        this.connection = conn(dumpPath);
    }

    private Connection conn(String dumpPath) {
//        connection = null;
        try {
            Class.forName(driver);
            Calendar calendar = Calendar.getInstance();
            System.out.println(calendar.getTime().toString());
//            connection = DriverManager.getConnection
//                    ("jdbc:ucanaccess://D:\\Planning\\NOkIA\\DumPs\\3GDump\\2018\\Dump_3G_16_09_2018\\Dump_3G_24_09_2018\\Dump_3G_24_09_2018.mdb;" +
//                            "keepMirror=true");
            connection = DriverManager.getConnection
                    ("jdbc:ucanaccess://" + dumpPath +
                            ";keepMirror=true");
            calendar = Calendar.getInstance();
            System.out.println(calendar.getTime().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public ArrayList<GSite> get2GSites(ArrayList<GSite> gSitesList) throws SQLException {

        Statement statement = connection.createStatement();

        String gQuery = "Select name,count(name),sum(T),first(BSCName),sum(C),sum(O),max(TX),sum(E),sum(G),sum(F)from" +
                "(Select BSCId,BCFId,count(TRXId) as T,sum(gprsEnabledTrx) as G from A_TRX group by BSCId,BCFId) as firstSet " +
                "left join " +
                "(Select BSCId,BCFId,name from A_BCF group by BSCId,BCFId) as secondSet " +
                "on (firstSet.BSCId=secondSet.BSCId and firstSet.BCFId=secondSet.BCFId)" +
                "left join " +
                "(Select BSCId,name as BSCName from A_BSC) as thirdSet " +
                "on (firstSet.BSCId=thirdSet.BSCId)" +
                "left join " +
                "(Select BSCId,BCFId,count(BTSId) as C,sum(adminState) as O,sum(frequencyBandInUse) as F from A_BTS group by BSCId,BCFId) as fourthSet " +
                "on (firstSet.BSCId=fourthSet.BSCId and firstSet.BCFId=fourthSet.BCFId) " +
                "left join " +
                "(Select  BSCId,BCFId,max(channel0Pcm) as TX from A_TRX " +
                "group by BSCId,BCFId) as fifthSet " +
                "on (firstSet.BSCId=fifthSet.BSCId and firstSet.BCFId=fifthSet.BCFId)" +
                "left join " +
                "(Select BSCId,BCFId,count(channel0Pcm) as E from ( Select BSCId,BCFId,channel0Pcm from A_TRX " +
                "where channel0Pcm not like '65535' group by BSCId,BCFId,channel0Pcm ) group by BSCId,BCFId ) as sixthSet " +
                "on (firstSet.BSCId=sixthSet.BSCId and firstSet.BCFId=sixthSet.BCFId) " +
                " group by name";
//        String bcfCount = "Select name,count(name) from\n" +
//                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId ) \n" +
//                "left join\n" +
//                "(Select BSCId,BCFId,name from A_BCF  )\n" +
//                "using(BSCId,BCFId)\n" +
//                " group by name";
//        String trxCount = "Select name,sum(T) from" +
//                "(Select BSCId,BCFId,count(TRXId) as T from A_TRX group by BSCId,BCFId) " +
//                "left join" +
//                "(Select BSCId,BCFId,name from A_BCF)" +
//                "using(BSCId,BCFId)" +
//                "group by name";
//        String bscName = "Select name,first(N) from\n" +
//                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId ) \n" +
//                "left join\n" +
//                "(Select BSCId,BCFId,name from A_BCF  )\n" +
//                "using(BSCId,BCFId)\n" +
//                "left join \n" +
//                "(Select BSCId,name as N from A_BSC)\n" +
//                "using (BSCId)\n" +
//                "group by name";
//        String cellCount = "Select name,sum(C) from\n" +
//                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId )\n" +
//                "left join\n" +
//                "(Select BSCId,BCFId,count(BTSId) as C from A_BTS group by BSCId,BCFId) \n" +
//                "using(BSCId,BCFId)\n" +
//                "left join\n" +
//                "(Select BSCId,BCFId,name from A_BCF)\n" +
//                "using(BSCId,BCFId)\n" +
//                "group by name";
//        String onAirCellCount = "Select name,sum(O) from\n" +
//                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId )\n" +
//                "left join\n" +
//                "(Select BSCId,BCFId,sum(adminState) as O from A_BTS group by BSCId,BCFId) \n" +
//                "using(BSCId,BCFId)\n" +
//                "left join\n" +
//                "(Select BSCId,BCFId,name from A_BCF)\n" +
//                "using(BSCId,BCFId)\n" +
//                "group by name";
//        String txMode = "Select name,first(channel0Pcm) from\n" +
//                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId )\n" +
//                "left join\n" +
//                "(Select BSCId,BCFId,name from A_BCF) \n" +
//                "using(BSCId,BCFId)\n" +
//                "left join(\n" +
//                "Select distinct BSCId,BCFId,channel0Pcm from A_TRX  \n" +
//                "group by BSCId,BCFId,channel0Pcm)\n" +
//                "using(BSCId,BCFId)\n" +
//                "group by name";
//        String e1sCount = "Select name,count(T) from\n" +
//                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId )\n" +
//                "left join\n" +
//                "(Select BSCId,BCFId,name from A_BCF) \n" +
//                "using(BSCId,BCFId)\n" +
//                "left join(\n" +
//                "Select distinct BSCId,BCFId,channel0Pcm as T from A_TRX  \n" +
//                "where channel0Pcm not like '65535' group by BSCId,BCFId,channel0Pcm)\n" +
//                "using(BSCId,BCFId)\n" +
//                "group by name";
//        String gTrxCount = "Select name,sum(G) from\n" +
//                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId )\n" +
//                "left join\n" +
//                "(Select BSCId,BCFId,name from A_BCF) \n" +
//                "using(BSCId,BCFId)\n" +
//                "left join(\n" +
//                "Select  BSCId,BCFId,sum(gprsEnabledTrx) as G from A_TRX  \n" +
//                "group by BSCId,BCFId)\n" +
//                "using(BSCId,BCFId)\n" +
//                "group by name";
//        String dcsCellsCount = "Select name,sum(F) from\n" +
//                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId )\n" +
//                "left join\n" +
//                "(Select BSCId,BCFId,name from A_BCF) \n" +
//                "using(BSCId,BCFId)\n" +
//                "left join(\n" +
//                "Select  BSCId,BCFId,sum(frequencyBandInUse) as F from A_BTS  \n" +
//                "group by BSCId,BCFId)\n" +
//                "using(BSCId,BCFId)\n" +
//                "group by name";
//        ResultSet bcfCountResultSet = statement.executeQuery(bcfCount);
//        ResultSet trxCountResultSet = statement.executeQuery(trxCount);
//        ResultSet bscNameResultSet = statement.executeQuery(bscName);
//        ResultSet cellCountResultSet = statement.executeQuery(cellCount);
//        ResultSet onAirCellCountResultSet = statement.executeQuery(onAirCellCount);
//        ResultSet txModeResultSet = statement.executeQuery(txMode);
//        ResultSet e1sCountResultSet = statement.executeQuery(e1sCount);
//        ResultSet gTRXCountResultSet = statement.executeQuery(gTrxCount);
//        ResultSet dcsCellsCountResultSet = statement.executeQuery(dcsCellsCount);
        ResultSet gResulSet = statement.executeQuery(gQuery);

        while (gResulSet.next()) {
            GSite site = new GSite();
            site.setSiteName(gResulSet.getString(1));
            site.setSiteNumberOfBCFs(gResulSet.getInt(2));
            site.setSiteNumberOfTRXs(gResulSet.getInt(3));
            site.setSiteBSCName(gResulSet.getString(4));
            site.setSiteNumberOfCells(gResulSet.getInt(5));
            site.setSiteNumberOfOnAirCells(gResulSet.getInt(6));
            site.setSiteTxMode(gResulSet.getString(7));
            site.setSiteNumberOfE1s(gResulSet.getInt(8));
            site.setSiteNumberOfGTRXs(gResulSet.getInt(9));
            site.setSiteNumberOfDcsCells(gResulSet.getInt(10));
            site.finalizeProperties();
            gSitesList.add(site);
        }
        return gSitesList;
    }

    public ArrayList<USite> get3GSites(int ran, ArrayList<USite> uSitesList) throws SQLException {
        Statement statement = connection.createStatement();
        String uQuery, hwQuery;
        if (ran == 1) {
            uQuery = "Select  BTSAdditionalInfo,max(RncId),max(WBTSId),sum(C),sum(O),count(BTSAdditionalInfo),sum(fC),sum(onFC),sum(sC),sum(onSC),sum(tC),sum(onTC)\n" +
                    ",sum(uC),sum(onUC),max(I),first(V),sum(HD1),sum(HD2),sum(HD3),sum(HU1),sum(R99),count(HD1Count),count(HD2Count),count(HD3Count)," +
                    "count(HU1Count),sum(P),sum(S),first(Name) from \n" +
                    "(Select RncId,WBTSId,COCOId,BTSAdditionalInfo ,IubTransportMedia as I,NESWVersion as V,WBTSName as Name from A_WBTS ) as firstSet \n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,count(WBTSId) as C,sum(AdminCellState) as O from A_WCEL group by RncId,WBTSId) as secondSet \n" +
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
                    "(Select RncId,WBTSId,count(WBTSId) as uC,sum(AdminCellState) onUC from A_WCEL where UARFCN ='2988' or UARFCN='2986' group by RncId,WBTSId ) as sixthSet\n" +
                    "on (firstSet.RncId=sixthSet.RncId and firstSet.WBTSId=sixthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                    "as R99,rfSharingEnabled as S  from A_WBTSF_BTSSCW ) as seventhSet \n" +
                    "on (firstSet.RncId=seventhSet.RncId and firstSet.WBTSId=seventhSet.WBTSId)\n" +
                    "left  join \n" +
                    "(Select RncId,WBTSId,numberOfHSDPASet1 as HD1Count \n" +
                    "from A_WBTSF_BTSSCW where numberOfHSDPASet1 ='-1') as eighthSet\n" +
                    "on (firstSet.RncId=eighthSet.RncId and firstSet.WBTSId=eighthSet.WBTSId)\n" +
                    "left  join \n" +
                    "(Select RncId,WBTSId,numberOfHSDPASet2 as HD2Count \n" +
                    "from A_WBTSF_BTSSCW where numberOfHSDPASet2 ='-1') as ninthSet\n" +
                    "on (firstSet.RncId=ninthSet.RncId and firstSet.WBTSId=ninthSet.WBTSId)\n" +
                    "left  join \n" +
                    "(Select RncId,WBTSId,numberOfHSDPASet3 as HD3Count \n" +
                    "from A_WBTSF_BTSSCW where numberOfHSDPASet3 ='-1') as tenthSet\n" +
                    "on (firstSet.RncId=tenthSet.RncId and firstSet.WBTSId=tenthSet.WBTSId)\n" +
                    "left  join \n" +
                    "(Select RncId,WBTSId,numberOfHSUPASet1 as HU1Count \n" +
                    "from A_WBTSF_BTSSCW where numberOfHSUPASet1 ='-1') as eleventhSet\n" +
                    "on (firstSet.RncId=eleventhSet.RncId and firstSet.WBTSId=eleventhSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId ,WBTSId,COCOId from A_WBTS ) as twelvthSet\n" +
                    "on (firstSet.RncId=twelvthSet.RncId and firstSet.WBTSId=twelvthSet.WBTSId)\n" +
                    "left join\n" +
                    "(Select RncId ,COCOId,P from\n" +
                    "(Select RncId ,COCOId,max(cast (AAL2UPPCR01Egr as int)) as P from A_COCO_AAL2TP  group by RncId,COCOId)) as thirteenthSet\n" +
                    "on (firstSet.RncId=thirteenthSet.RncId and firstSet.COCOId=thirteenthSet.COCOId)\n" +
                    " group by BTSAdditionalInfo";
        } else {
            uQuery = "Select  BTSAdditionalInfo,max(RncId),max(WBTSId),sum(C),sum(O),count(BTSAdditionalInfo),sum(fC),sum(onFC),sum(sC),sum(onSC),sum(tC),sum(onTC)\n" +
                    ",sum(uC),sum(onUC),max(I),first(V),sum(HD1),sum(HD2),sum(HD3),sum(HU1),sum(R99),count(HD1Count),count(HD2Count),count(HD3Count)," +
                    "count(HU1Count),sum(P),sum(S),first(Name) from \n" +
                    "(Select RncId,WBTSId,COCOId,BTSAdditionalInfo ,IubTransportMedia as I,NESWVersion as V,WBTSName as Name from A_WBTS ) as firstSet \n" +
                    "left join\n" +
                    "(Select RncId,WBTSId,count(WBTSId) as C,sum(AdminCellState) as O from A_WCEL group by RncId,WBTSId) as secondSet \n" +
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
                    "(Select RncId,WBTSId,count(WBTSId) as uC,sum(AdminCellState) onUC from A_WCEL where UARFCN ='2988' or UARFCN='2986' group by RncId,WBTSId ) as sixthSet\n" +
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
                    " group by BTSAdditionalInfo";
        }
        hwQuery = "Select Code,first(FBBA),first(FRGC),first(FRGD),first(FRGF),first(FRGL),first(FRGM),first(FRGP),first(FRGT),first(FRGU),first(FRGX),first(FSMB),first(FSMD)," +
                "first(FSME),first(FSMF),first(FTIA),first(FTIB),first(FTIF),first(FTPB),first(FXDA),first(FXDB) " +
                " from (" +
                "(Select BTSAdditionalInfo as Code from A_WBTS ) as SiteSet " +
                "left join\n" +
                "(Select SiteCode,count(HWType) as FBBA from HW where HWType = 'FBBA' group by SiteCode) as  FBBASet on (SiteSet.Code=FBBASet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGC from HW where HWType = 'FRGC' group by SiteCode) as  FRGCSet  on (SiteSet.Code=FRGCSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGD from HW where HWType = 'FRGD' group by SiteCode) as  FRGDSet  on (SiteSet.Code=FRGDSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGF from HW where HWType = 'FRGF' group by SiteCode) as  FRGFSet  on (SiteSet.Code=FRGFSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGL from HW where HWType = 'FRGL' group by SiteCode) as  FRGLSet  on (SiteSet.Code=FRGLSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGM from HW where HWType = 'FRGM' group by SiteCode) as  FRGMSet  on (SiteSet.Code=FRGMSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGP from HW where HWType = 'FRGP' group by SiteCode) as  FRGPSet  on (SiteSet.Code=FRGPSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGT from HW where HWType = 'FRGT' group by SiteCode) as  FRGTSet  on (SiteSet.Code=FRGTSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGU from HW where HWType = 'FRGU' group by SiteCode) as  FRGUSet  on (SiteSet.Code=FRGUSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FRGX from HW where HWType = 'FRGX' group by SiteCode) as  FRGXSet  on (SiteSet.Code=FRGXSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FSMB from HW where HWType = 'FSMB' group by SiteCode) as  FSMBSet  on (SiteSet.Code=FSMBSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FSMD from HW where HWType = 'FSMD' group by SiteCode) as  FSMDSet  on (SiteSet.Code=FSMDSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FSME from HW where HWType = 'FSME' group by SiteCode) as  FSMESet  on (SiteSet.Code=FSMESet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FSMF from HW where HWType = 'FSMF' group by SiteCode) as  FSMFSet  on (SiteSet.Code=FSMFSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FTIA from HW where HWType = 'FTIA' group by SiteCode) as  FTIASet  on (SiteSet.Code=FTIASet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FTIB from HW where HWType = 'FTIB' group by SiteCode) as  FTIBSet  on (SiteSet.Code=FTIBSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FTIF from HW where HWType = 'FTIF' group by SiteCode) as  FTIFSet  on (SiteSet.Code=FTIFSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FTPB from HW where HWType = 'FTPB' group by SiteCode) as  FTPBSet  on (SiteSet.Code=FTPBSet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FXDA from HW where HWType = 'FXDA' group by SiteCode) as  FXDASet  on (SiteSet.Code=FXDASet.SiteCode) " +
                "left join \n" +
                "(Select SiteCode,count(HWType) as FXDB from HW where HWType = 'FXDB' group by SiteCode) as  FXDBSet  on (SiteSet.Code=FXDBSet.SiteCode) " +
                ") group by Code";
        ResultSet uResultSet = statement.executeQuery(uQuery);
        ResultSet hwResultSet = statement.executeQuery(hwQuery);

        while (uResultSet.next() && hwResultSet.next()) {
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
            UHardware uHardware = new UHardware(hwResultSet.getInt(2), hwResultSet.getInt(3), hwResultSet.getInt(4),
                    hwResultSet.getInt(5), hwResultSet.getInt(6), hwResultSet.getInt(7), hwResultSet.getInt(8),
                    hwResultSet.getInt(9), hwResultSet.getInt(10), hwResultSet.getInt(11),
                    hwResultSet.getInt(12), hwResultSet.getInt(13), hwResultSet.getInt(14),
                    hwResultSet.getInt(15), hwResultSet.getInt(16), hwResultSet.getInt(17),
                    hwResultSet.getInt(18),hwResultSet.getInt(19),hwResultSet.getInt(20),hwResultSet.getInt(21));
            site.setUHardware(uHardware);
            site.finalizeProperties();
            uSitesList.add(site);
        }
        System.out.println(uSitesList.size());
        return uSitesList;
    }

    public ArrayList<LSite> get4GSites(int ran, ArrayList<LSite> lSites) throws SQLException {

        String cellCount;
        String lteFeatures;
        String lQuery;
        Statement statement = connection.createStatement();
        if (ran == 2) {
            cellCount = "Select mrbtsId,count(C),sum(C),first(V),first(N) from\n" +
                    "(Select mrbtsId,softwareReleaseVersion as V,name as N from A_LTE_MRBTS_LNBTS_FTM)" +
                    "left join\n" +
                    "(Select mrbtsId,administrativeState as C from A_LTE_MRBTS_LNBTS_LNCEL) " +
                    "using (mrbtsId) \n" +
                    "group by \n" +
                    "mrbtsId";
            lteFeatures = "Select mrbtsId,first(BW),first(M),sum(S) from\n" +
                    "(Select mrbtsId from A_LTE_MRBTS_LNBTS) " +
                    "left join\n" +
                    "(Select mrbtsId,dlChBw as BW,dlMimoMode as M,actSuperCell as S from A_LTE_MRBTS_LNBTS_LNCEL_LNCEL_FDD) " +
                    "using (mrbtsId) \n" +
                    "group by \n" +
                    "mrbtsId";
            lQuery = "Select mrbtsId,sum(C),sum(O),first(V),first(N),first(BW),first(M),sum(S),sum(SO) from (" +
                    "(Select mrbtsId,count(mrbtsId) as C,sum(administrativeState) as O from A_LTE_MRBTS_LNBTS_LNCEL group by mrbtsId) as firstSet " +
                    "left join " +
                    "(Select mrbtsId,first(softwareReleaseVersion) as V,first(name) as N from A_LTE_MRBTS_LNBTS_FTM group by mrbtsId) as secondSet " +
                    "on (firstSet.mrbtsId=secondSet.mrbtsId) " +
                    "left join " +

                    "(Select mrbtsId,sum(actSuperCell) as SO from (\n" +
                    "(Select mrbtsId,lnCelId,administrativeState from A_LTE_MRBTS_LNBTS_LNCEL  ) as A \n" +
                    "left join \n" +
                    "(Select mrbtsId,lnCelId,actSuperCell from A_LTE_MRBTS_LNBTS_LNCEL_LNCEL_FDD ) as B \n" +
                    "on(A.mrbtsId=B.mrbtsId and A.lnCelId=B.lnCelId) ) where administrativeState = '1' group by mrbtsId) as thirdSet " +
                    "on (firstSet.mrbtsId=thirdSet.mrbtsId) " +

                    "left join " +
                    "(Select mrbtsId,max(dlChBw) as BW,max(dlMimoMode) as M,sum(actSuperCell) as S from A_LTE_MRBTS_LNBTS_LNCEL_LNCEL_FDD group by mrbtsId) as fourthSet " +
                    "on (firstSet.mrbtsId=fourthSet.mrbtsId) ) " +
                    "group by " +
                    "mrbtsId";
        } else {

            cellCount = "Select mrbtsId,count(C),sum(C),first(V),first(N),sum(S) from " +
                    "(Select mrbtsId,activeSWReleaseVersion as V,btsName as N from A_LTE_BTSSCL) " +
                    "left join \n" +
                    "(Select mrbtsId,administrativeState as C,actSuperCell as S from A_LTE_LNCEL) " +
                    "using (mrbtsId) \n" +
                    "group by \n" +
                    "mrbtsId";
            lteFeatures = "Select mrbtsId,first(BW),first(M) from\n" +
                    "(Select mrbtsId from A_LTE_BTSSCL) " +
                    "left join \n" +
                    "(Select mrbtsId,dlChBw as BW,dlMimoMode as M from A_LTE_LNCEL_PS) " +
                    "using (mrbtsId) \n" +
                    "group by \n" +
                    "mrbtsId";
            lQuery = "Select mrbtsId,sum(C),sum(O),first(V),first(N),first(BW),first(M),sum(S),sum(SO) from " +
                    "(Select mrbtsId,count(mrbtsId) as C,sum(administrativeState) as O,sum(actSuperCell) as S from A_LTE_LNCEL group by mrbtsId ) as firstSet " +
                    "left join " +
                    "(Select mrbtsId,first(activeSWReleaseVersion) as V,first(btsName) as N from A_LTE_BTSSCL group by mrbtsId) as secondSet " +
                    "on (firstSet.mrbtsId=secondSet.mrbtsId) " +
                    "left join " +
                    "(Select mrbtsId, sum(actSuperCell) as SO from A_LTE_LNCEL where administrativeState = '1'  group by mrbtsId) as thirdSet " +
                    "on (firstSet.mrbtsId=thirdSet.mrbtsId) " +
                    "left join " +
                    "(Select mrbtsId,max(dlChBw) as BW,max(dlMimoMode) as M from A_LTE_LNCEL_PS group by mrbtsId) as fourthSet " +
                    "on (firstSet.mrbtsId=fourthSet.mrbtsId) " +
                    "group by " +
                    "mrbtsId";
        }

//        ResultSet cellCountResultSet = statement.executeQuery(cellCount);
//        ResultSet lteFeaturesResultSet = statement.executeQuery(lteFeatures);
        ResultSet lResultSet = statement.executeQuery(lQuery);
        while (lResultSet.next()) {
            LSite site = new LSite();
            site.setENodeBId(lResultSet.getString(1));
            site.setENodeBNumberOfCells(lResultSet.getInt(2) + lResultSet.getInt(8));
            site.setENodeBNumberOfOnAirCells(lResultSet.getInt(3), lResultSet.getInt(9));
            site.setENodeBVersion(lResultSet.getString(4));
            site.setENodeBName(lResultSet.getString(5));
            site.setENodeBBW(lResultSet.getInt(6));
            site.setENodeBMimo(lResultSet.getInt(7));
            site.finalizeProperties();
            lSites.add(site);
        }

        return lSites;
    }

    public ResultSet get2GHW() throws SQLException {
        Statement statement = connection.createStatement();
        String hwQuery = "Select BSCName,siteName ,BCF_ID,unitTypeActual,serialNumber from HW ";
        return statement.executeQuery(hwQuery);
    }

    public ResultSet get3GHW() throws SQLException {
        Statement statement = connection.createStatement();
        String hwQuery = "Select RNCId,SiteCode,SiteName,WBTSId,HWType,SerialNumber from HW ";
        return statement.executeQuery(hwQuery);
    }

    public ResultSet get4GHW() throws SQLException {
        Statement statement = connection.createStatement();
        String hwQuery = "Select MrBTSId,siteName ,unitTypeActual,serialNumber from HW ";
        return statement.executeQuery(hwQuery);
    }

    public ArrayList<USite> getThirdCarrierSites(ArrayList<USite> thirdCarrierList) throws SQLException {

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

    public ArrayList<USite> getU900List(ArrayList<USite> u900List) throws SQLException {

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

    public ArrayList<GSite> get2GSitesOld(int ran, ArrayList<GSite> gSitesList) throws SQLException {

        Statement statement = connection.createStatement();
        String bcfCount = "Select name,count(name) from\n" +
                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId ) \n" +
                "left join\n" +
                "(Select BSCId,BCFId,name from A_BCF  )\n" +
                "using(BSCId,BCFId)\n" +
                " group by name";
        String trxCount = "Select name,sum(T) from" +
                "(Select BSCId,BCFId,count(TRXId) as T from A_TRX group by BSCId,BCFId) " +
                "left join" +
                "(Select BSCId,BCFId,name from A_BCF)" +
                "using(BSCId,BCFId)" +
                "group by name";
        String bscName = "Select name,first(N) from\n" +
                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId ) \n" +
                "left join\n" +
                "(Select BSCId,BCFId,name from A_BCF  )\n" +
                "using(BSCId,BCFId)\n" +
                "left join \n" +
                "(Select BSCId,name as N from A_BSC)\n" +
                "using (BSCId)\n" +
                "group by name";
        String cellCount = "Select name,sum(C) from\n" +
                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId )\n" +
                "left join\n" +
                "(Select BSCId,BCFId,count(BTSId) as C from A_BTS group by BSCId,BCFId) \n" +
                "using(BSCId,BCFId)\n" +
                "left join\n" +
                "(Select BSCId,BCFId,name from A_BCF)\n" +
                "using(BSCId,BCFId)\n" +
                "group by name";
        String onAirCellCount = "Select name,sum(O) from\n" +
                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId )\n" +
                "left join\n" +
                "(Select BSCId,BCFId,sum(adminState) as O from A_BTS group by BSCId,BCFId) \n" +
                "using(BSCId,BCFId)\n" +
                "left join\n" +
                "(Select BSCId,BCFId,name from A_BCF)\n" +
                "using(BSCId,BCFId)\n" +
                "group by name";
        String txMode = "Select name,first(channel0Pcm) from\n" +
                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId )\n" +
                "left join\n" +
                "(Select BSCId,BCFId,name from A_BCF) \n" +
                "using(BSCId,BCFId)\n" +
                "left join(\n" +
                "Select distinct BSCId,BCFId,channel0Pcm from A_TRX  \n" +
                "group by BSCId,BCFId,channel0Pcm)\n" +
                "using(BSCId,BCFId)\n" +
                "group by name";
        String e1sCount = "Select name,count(T) from\n" +
                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId )\n" +
                "left join\n" +
                "(Select BSCId,BCFId,name from A_BCF) \n" +
                "using(BSCId,BCFId)\n" +
                "left join(\n" +
                "Select distinct BSCId,BCFId,channel0Pcm as T from A_TRX  \n" +
                "where channel0Pcm not like '65535' group by BSCId,BCFId,channel0Pcm)\n" +
                "using(BSCId,BCFId)\n" +
                "group by name";
        String gTrxCount = "Select name,sum(G) from\n" +
                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId )\n" +
                "left join\n" +
                "(Select BSCId,BCFId,name from A_BCF) \n" +
                "using(BSCId,BCFId)\n" +
                "left join(\n" +
                "Select  BSCId,BCFId,sum(gprsEnabledTrx) as G from A_TRX  \n" +
                "group by BSCId,BCFId)\n" +
                "using(BSCId,BCFId)\n" +
                "group by name";
        String dcsCellsCount = "Select name,sum(F) from\n" +
                "(Select BSCId,BCFId from A_TRX group by BSCId,BCFId )\n" +
                "left join\n" +
                "(Select BSCId,BCFId,name from A_BCF) \n" +
                "using(BSCId,BCFId)\n" +
                "left join(\n" +
                "Select  BSCId,BCFId,sum(frequencyBandInUse) as F from A_BTS  \n" +
                "group by BSCId,BCFId)\n" +
                "using(BSCId,BCFId)\n" +
                "group by name";
        ResultSet bcfCountResultSet = statement.executeQuery(bcfCount);
        ResultSet trxCountResultSet = statement.executeQuery(trxCount);
        ResultSet bscNameResultSet = statement.executeQuery(bscName);
        ResultSet cellCountResultSet = statement.executeQuery(cellCount);
        ResultSet onAirCellCountResultSet = statement.executeQuery(onAirCellCount);
        ResultSet txModeResultSet = statement.executeQuery(txMode);
        ResultSet e1sCountResultSet = statement.executeQuery(e1sCount);
        ResultSet gTRXCountResultSet = statement.executeQuery(gTrxCount);
        ResultSet dcsCellsCountResultSet = statement.executeQuery(dcsCellsCount);

        while (bcfCountResultSet.next() && trxCountResultSet.next() && bscNameResultSet.next() && cellCountResultSet.next()
                && onAirCellCountResultSet.next() && txModeResultSet.next() && e1sCountResultSet.next()
                && gTRXCountResultSet.next() && dcsCellsCountResultSet.next()) {
            GSite site = new GSite();
            site.setSiteName(bcfCountResultSet.getString(1));
            site.setSiteNumberOfBCFs(bcfCountResultSet.getInt(2));
            site.setSiteNumberOfTRXs(trxCountResultSet.getInt(2));
            site.setSiteBSCName(bscNameResultSet.getString(2));
            site.setSiteNumberOfCells(cellCountResultSet.getInt(2));
            site.setSiteNumberOfOnAirCells(onAirCellCountResultSet.getInt(2));
            site.setSiteTxMode(txModeResultSet.getString(2));
            site.setSiteNumberOfE1s(e1sCountResultSet.getInt(2));
            site.setSiteNumberOfGTRXs(gTRXCountResultSet.getInt(2));
            site.setSiteNumberOfDcsCells(dcsCellsCountResultSet.getInt(2));
            site.finalizeProperties();
            gSitesList.add(site);
        }
        return gSitesList;
    }

    public ArrayList<USite> get3GSitesOld(int ran, ArrayList<USite> uSitesList) throws SQLException {
//        ArrayList<USite> sitesList = new ArrayList<>();
        Statement statement = connection.createStatement();
        String r99Parameters, hd1Count, hd2Count, hd3Count, hu1Count;
        String siteCells = "Select  BTSAdditionalInfo,sum(C),sum(O) from\n" +
                "(Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS) \n" +
                "left join \n" +
                "(Select RncId,WBTSId,count(WBTSId) as C,sum(AdminCellState) as O from A_WCEL group by RncId,WBTSId)\n" +
                "using (RncId,WBTSId) \n" +
                "group by BTSAdditionalInfo  ";

        String numberOfNodeBs = "Select  BTSAdditionalInfo,count(BTSAdditionalInfo) from A_WBTS group by BTSAdditionalInfo";

        String firstCarrier = "Select BTSAdditionalInfo,sum(fC),sum(onFC) from \n" +
                "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,fC,onFC from \n" +
                "(Select RncId,WBTSId from A_WCEL group by RncId,WBTSId)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,count(WBTSId) as fC,sum(AdminCellState) onFC from A_WCEL where UARFCN ='10612' group by RncId,WBTSId )\n" +
                " using (RncId,WBTSId))\n" +
                "using (RncId,WBTSId)\n" +
                ") group by BTSAdditionalInfo";

        String secondCarrier = "Select BTSAdditionalInfo,sum(fC),sum(onFC) from \n" +
                "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,fC,onFC from \n" +
                "(Select RncId,WBTSId from A_WCEL group by RncId,WBTSId)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,count(WBTSId) as fC,sum(AdminCellState) onFC from A_WCEL where UARFCN ='10637' group by RncId,WBTSId )\n" +
                " using (RncId,WBTSId))\n" +
                "using (RncId,WBTSId)\n" +
                ") group by BTSAdditionalInfo";

        String thirdCarrier = "Select BTSAdditionalInfo,sum(fC),sum(onFC) from \n" +
                "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,fC,onFC from \n" +
                "(Select RncId,WBTSId from A_WCEL group by RncId,WBTSId)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,count(WBTSId) as fC,sum(AdminCellState) onFC from A_WCEL where UARFCN ='10662' group by RncId,WBTSId )\n" +
                " using (RncId,WBTSId))\n" +
                "using (RncId,WBTSId)\n" +
                ") group by BTSAdditionalInfo";
        String u900 = "Select BTSAdditionalInfo,sum(fC),sum(onFC) from \n" +
                "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,fC,onFC from \n" +
                "(Select RncId,WBTSId from A_WCEL group by RncId,WBTSId)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,count(WBTSId) as fC,sum(AdminCellState) onFC from A_WCEL where UARFCN ='2988' or UARFCN='2986' group by RncId,WBTSId )\n" +
                " using (RncId,WBTSId))\n" +
                "using (RncId,WBTSId)\n" +
                ") group by BTSAdditionalInfo";

        String generalInfo = "Select BTSAdditionalInfo,first(WBTSName),max(IubTransportMedia),first(NESWVersion),first(R),first(W) from \n" +
                "((Select RncId,WBTSId,BTSAdditionalInfo,WBTSName,IubTransportMedia,NESWVersion from A_WBTS)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,first(RncId) as R,first(WBTSId) as W from A_WCEL group by RncId,WBTSId)\n" +
                "using (RncId,WBTSId)\n" +
                ") group by BTSAdditionalInfo";

        if (ran == 2) {
            r99Parameters = "Select BTSAdditionalInfo,sum(HD1),sum(HD2),sum(HD3),sum(HU1),sum(R99),sum(S)" +
                    "  from \n" +
                    "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                    "left join (\n" +
                    " Select RncId,WBTSId,HD1,HD2,HD3,HU1,R99,S from \n" +
                    " (Select RncId,WBTSId from A_WCEL  group by RncId,WBTSId)\n" +
                    "left  join (\n" +
                    "Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,\n" +
                    "numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                    "as R99,rfSharingEnabled as S  from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW  ) using (RncId , WBTSId)\n" +
                    " )\n" +
                    "using (RncId,WBTSId)\n" +
                    ") group by BTSAdditionalInfo";

            hd1Count = "Select BTSAdditionalInfo,count(HD1)\n" +
                    " from \n" +
                    "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                    "left join (\n" +
                    " Select RncId,WBTSId,HD1,HD2,HD3,HU1,R99 from \n" +
                    " (Select RncId,WBTSId from A_WCEL where AdminCellState='1' group by RncId,WBTSId)\n" +
                    "left  join (\n" +
                    "Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,\n" +
                    "numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                    "as R99 from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW where numberOfHSDPASet1 ='-1') using (RncId , WBTSId)\n" +
                    " )\n" +
                    "using (RncId,WBTSId)\n" +
                    ") group by BTSAdditionalInfo";
            hd2Count = "Select BTSAdditionalInfo,count(HD2)\n" +
                    " from \n" +
                    "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                    "left join (\n" +
                    " Select RncId,WBTSId,HD1,HD2,HD3,HU1,R99 from \n" +
                    " (Select RncId,WBTSId from A_WCEL where AdminCellState='1' group by RncId,WBTSId)\n" +
                    "left  join (\n" +
                    "Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,\n" +
                    "numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                    "as R99 from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW where numberOfHSDPASet2 ='-1') using (RncId , WBTSId)\n" +
                    " )\n" +
                    "using (RncId,WBTSId)\n" +
                    ") group by BTSAdditionalInfo";
            hd3Count = "Select BTSAdditionalInfo,count(HD3)\n" +
                    " from \n" +
                    "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                    "left join (\n" +
                    " Select RncId,WBTSId,HD1,HD2,HD3,HU1,R99 from \n" +
                    " (Select RncId,WBTSId from A_WCEL where AdminCellState='1' group by RncId,WBTSId)\n" +
                    "left  join (\n" +
                    "Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,\n" +
                    "numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                    "as R99 from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW where numberOfHSDPASet3 ='-1') using (RncId , WBTSId)\n" +
                    " )\n" +
                    "using (RncId,WBTSId)\n" +
                    ") group by BTSAdditionalInfo";
            hu1Count = "Select BTSAdditionalInfo,count(HU1)\n" +
                    " from \n" +
                    "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                    "left join (\n" +
                    " Select RncId,WBTSId,HD1,HD2,HD3,HU1,R99 from \n" +
                    " (Select RncId,WBTSId from A_WCEL where AdminCellState='1' group by RncId,WBTSId)\n" +
                    "left  join (\n" +
                    "Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,\n" +
                    "numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                    "as R99 from A_WBTSF_RNC_WBTS_MRBTS_BTSSCW where numberOfHSUPASet1 ='-1') using (RncId , WBTSId)\n" +
                    " )\n" +
                    "using (RncId,WBTSId)\n" +
                    ") group by BTSAdditionalInfo";
        } else {
            r99Parameters = "Select BTSAdditionalInfo,sum(HD1),sum(HD2),sum(HD3),sum(HU1),sum(R99),sum(S)" +
                    "  from \n" +
                    "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                    "left join (\n" +
                    " Select RncId,WBTSId,HD1,HD2,HD3,HU1,R99,S from \n" +
                    " (Select RncId,WBTSId from A_WCEL  group by RncId,WBTSId)\n" +
                    "left  join (\n" +
                    "Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,\n" +
                    "numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                    "as R99,rfSharingEnabled as S  from A_WBTSF_BTSSCW  ) using (RncId , WBTSId)\n" +
                    " )\n" +
                    "using (RncId,WBTSId)\n" +
                    ") group by BTSAdditionalInfo";

            hd1Count = "Select BTSAdditionalInfo,count(HD1)\n" +
                    " from \n" +
                    "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                    "left join (\n" +
                    " Select RncId,WBTSId,HD1,HD2,HD3,HU1,R99 from \n" +
                    " (Select RncId,WBTSId from A_WCEL where AdminCellState='1' group by RncId,WBTSId)\n" +
                    "left  join (\n" +
                    "Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,\n" +
                    "numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                    "as R99 from A_WBTSF_BTSSCW where numberOfHSDPASet1 ='-1') using (RncId , WBTSId)\n" +
                    " )\n" +
                    "using (RncId,WBTSId)\n" +
                    ") group by BTSAdditionalInfo";
            hd2Count = "Select BTSAdditionalInfo,count(HD2)\n" +
                    " from \n" +
                    "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                    "left join (\n" +
                    " Select RncId,WBTSId,HD1,HD2,HD3,HU1,R99 from \n" +
                    " (Select RncId,WBTSId from A_WCEL where AdminCellState='1' group by RncId,WBTSId)\n" +
                    "left  join (\n" +
                    "Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,\n" +
                    "numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                    "as R99 from A_WBTSF_BTSSCW where numberOfHSDPASet2 ='-1') using (RncId , WBTSId)\n" +
                    " )\n" +
                    "using (RncId,WBTSId)\n" +
                    ") group by BTSAdditionalInfo";
            hd3Count = "Select BTSAdditionalInfo,count(HD3)\n" +
                    " from \n" +
                    "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                    "left join (\n" +
                    " Select RncId,WBTSId,HD1,HD2,HD3,HU1,R99 from \n" +
                    " (Select RncId,WBTSId from A_WCEL where AdminCellState='1' group by RncId,WBTSId)\n" +
                    "left  join (\n" +
                    "Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,\n" +
                    "numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                    "as R99 from A_WBTSF_BTSSCW where numberOfHSDPASet3 ='-1') using (RncId , WBTSId)\n" +
                    " )\n" +
                    "using (RncId,WBTSId)\n" +
                    ") group by BTSAdditionalInfo";
            hu1Count = "Select BTSAdditionalInfo,count(HU1)\n" +
                    " from \n" +
                    "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                    "left join (\n" +
                    " Select RncId,WBTSId,HD1,HD2,HD3,HU1,R99 from \n" +
                    " (Select RncId,WBTSId from A_WCEL where AdminCellState='1' group by RncId,WBTSId)\n" +
                    "left  join (\n" +
                    "Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,\n" +
                    "numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                    "as R99 from A_WBTSF_BTSSCW where numberOfHSUPASet1 ='-1') using (RncId , WBTSId)\n" +
                    " )\n" +
                    "using (RncId,WBTSId)\n" +
                    ") group by BTSAdditionalInfo";

        }
        String siteE1s = "Select BTSAdditionalInfo,sum(P)" +
                " from \n" +
                "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                "\n" +
                "left  join (\n" +
                "Select RncId,WBTSId,COCOId,P from (Select RncId ,WBTSId  from A_WCEL where AdminCellState='1' group by RncId,WBTSId) \n" +
                "left join\n" +
                "(Select RncId ,WBTSId,COCOId from A_WBTS ) using (RncId,WBTSId)\n" +
                "left  join \n" +
                "(Select RncId ,COCOId,P from (\n" +
                "Select RncId ,COCOId,max(cast (AAL2UPPCR01Egr as int)) as P from A_COCO_AAL2TP  group by RncId,COCOId))\n" +
                "using (RncId,COCOId))\n" +
                "using (RncId,WBTSId)\n" +
                ") group by BTSAdditionalInfo";

        String siteFRGTs = "Select BTSAdditionalInfo,sum(P) from \n" +
                "((Select RncId,WBTSId,BTSAdditionalInfo from A_WBTS)\n" +
                "left  join (\n" +
                "Select RncId,WBTSId,P from (Select RncId ,WBTSId  from A_WCEL where AdminCellState='1' group by RncId,WBTSId) \n" +
                "left join\n" +
                "(Select RncId ,WBTSId from A_WBTS ) using (RncId,WBTSId)\n" +
                "left  join \n" +
                "(Select RncId ,WBTSId,P from (\n" +
                "Select RncId ,WBTSId,count(HWType) as P from HW where HWType like '%FRGT%' group by RncId,WBTSId))\n" +
                "using (RncId,WBTSId))\n" +
                "using (RncId,WBTSId)\n" +
                ") group by BTSAdditionalInfo";

        ResultSet siteCellsResultSet = statement.executeQuery(siteCells);
        ResultSet nodeBsResultSet = statement.executeQuery(numberOfNodeBs);
        ResultSet firstCarrierResultSet = statement.executeQuery(firstCarrier);
        ResultSet secondCarrierResultSet = statement.executeQuery(secondCarrier);
        ResultSet thirdCarrierResultSet = statement.executeQuery(thirdCarrier);
        ResultSet u900ResultSet = statement.executeQuery(u900);
        ResultSet generalInfoResultSet = statement.executeQuery(generalInfo);
        ResultSet r99ResultSet = statement.executeQuery(r99Parameters);
        ResultSet hd1CountResultSet = statement.executeQuery(hd1Count);
        ResultSet hd2CountResultSet = statement.executeQuery(hd2Count);
        ResultSet hd3CountResultSet = statement.executeQuery(hd3Count);
        ResultSet hu1CountResultSet = statement.executeQuery(hu1Count);
        ResultSet e1sResultSet = statement.executeQuery(siteE1s);
//        ResultSet fRGTsResultSet = statement.executeQuery(siteFRGTs);
//                int f = 0;
//        while (fRGTsResultSet.next()){
//            f++;
//        }
//        System.out.println(f);

//        int a = 0;
//        while (siteCellsResultSet.next()){
//            a++;
//        }
//        System.out.println(a);
//        int b = 0;
//        while (nodeBsResultSet.next()){
//            b++;
//        }
//        System.out.println(b);
//        int c = 0;
//        while (firstCarrierResultSet.next()){
//            c++;
//        }
//        System.out.println(c);
//        int d = 0;
//        while (secondCarrierResultSet.next()){
//            d++;
//        }
//        System.out.println(d);
//        int e = 0;
//        while (thirdCarrierResultSet.next()){
//            e++;
//        }
//        System.out.println(e);
//        int lo = 0;
//        while (generalInfoResultSet.next()){
//            lo++;
//        }
//        System.out.println(lo);

        int count = 0;
        while (siteCellsResultSet.next() && nodeBsResultSet.next() && firstCarrierResultSet.next() && secondCarrierResultSet.next()
                && thirdCarrierResultSet.next() && u900ResultSet.next() && generalInfoResultSet.next() &&
                r99ResultSet.next() && hd1CountResultSet.next() && hd2CountResultSet.next() && hd3CountResultSet.next()
                && hu1CountResultSet.next() && e1sResultSet.next()) {
            count++;
            USite site = new USite();
            site.setSiteCode(siteCellsResultSet.getString(1));
            site.setSiteNumberOfNodeBs(nodeBsResultSet.getInt(2));
            site.setSiteNumberOfCells(siteCellsResultSet.getInt(2));
            site.setSiteNumberOfOnAirCells(siteCellsResultSet.getInt(3));
            site.setSiteNumberOfFirstCarriersCells(firstCarrierResultSet.getInt(2));
            site.setSiteNumberOfOnAirFirstCarriersCells(firstCarrierResultSet.getInt(3));
            site.setSiteNumberOfSecondCarriersCells(secondCarrierResultSet.getInt(2));
            site.setSiteNumberOfOnAirSecondCarriersCells(secondCarrierResultSet.getInt(3));
            site.setSiteNumberOfThirdCarriersCells(thirdCarrierResultSet.getInt(2));
            site.setSiteNumberOfOnAirThirdCarriersCells(thirdCarrierResultSet.getInt(3));
            site.setSiteNumberOfU900CarriersCells(u900ResultSet.getInt(2));
            site.setSiteNumberOfOnAirU900CarriersCells(u900ResultSet.getInt(3));
            site.setSiteName(generalInfoResultSet.getString(2));
            site.setSiteTxMode(generalInfoResultSet.getString(3));
            site.setSiteVersion(generalInfoResultSet.getString(4));
            site.setSiteRncId(generalInfoResultSet.getString(5));
            site.setSiteWbtsId(generalInfoResultSet.getString(6));
            site.setSiteNumberOfHSDPASet1(r99ResultSet.getInt(2) + hd1CountResultSet.getInt(2));
            site.setSiteNumberOfHSDPASet2(r99ResultSet.getInt(3) + hd2CountResultSet.getInt(2));
            site.setSiteNumberOfHSDPASet3(r99ResultSet.getInt(4) + hd3CountResultSet.getInt(2));
            site.setSiteNumberOfHSUPASet1(r99ResultSet.getInt(5) + hu1CountResultSet.getInt(2));
            site.setSiteNumberOfChannelElements(r99ResultSet.getInt(6));
            site.setRfSharing(r99ResultSet.getInt(7));
            site.setSiteNumberOfE1s((int) Math.ceil(e1sResultSet.getInt(2) / 4490.0));
            site.finalizeProperties();
            uSitesList.add(site);

        }
        System.out.println(count);
        System.out.println(uSitesList.size());
        return uSitesList;
    }

    public ArrayList<LSite> get4GSitesOld(int ran, ArrayList<LSite> lSites) throws SQLException {

        String cellCount;
        String lteFeatures;
        Statement statement = connection.createStatement();
        if (ran == 2) {
            cellCount = "Select mrbtsId,count(C),sum(C),first(V),first(N) from\n" +
                    "(Select mrbtsId,softwareReleaseVersion as V,name as N from A_LTE_MRBTS_LNBTS_FTM)" +
                    "left join\n" +
                    "(Select mrbtsId,administrativeState as C from A_LTE_MRBTS_LNBTS_LNCEL) " +
                    "using (mrbtsId) \n" +
                    "group by \n" +
                    "mrbtsId";
            lteFeatures = "Select mrbtsId,first(BW),first(M),sum(S) from\n" +
                    "(Select mrbtsId from A_LTE_MRBTS_LNBTS) " +
                    "left join\n" +
                    "(Select mrbtsId,dlChBw as BW,dlMimoMode as M,actSuperCell as S from A_LTE_MRBTS_LNBTS_LNCEL_LNCEL_FDD) " +
                    "using (mrbtsId) \n" +
                    "group by \n" +
                    "mrbtsId";
        } else {

            cellCount = "Select mrbtsId,count(C),sum(C),first(V),first(N),sum(S) from\n" +
                    "(Select mrbtsId,activeSWReleaseVersion as V,btsName as N from A_LTE_BTSSCL) " +
                    "left join \n" +
                    "(Select mrbtsId,administrativeState as C,actSuperCell as S from A_LTE_LNCEL) " +
                    "using (mrbtsId) \n" +
                    "group by \n" +
                    "mrbtsId";
            lteFeatures = "Select mrbtsId,first(BW),first(M) from\n" +
                    "(Select mrbtsId from A_LTE_BTSSCL) " +
                    "left join \n" +
                    "(Select mrbtsId,dlChBw as BW,dlMimoMode as M from A_LTE_LNCEL_PS) " +
                    "using (mrbtsId) \n" +
                    "group by \n" +
                    "mrbtsId";
        }

        ResultSet cellCountResultSet = statement.executeQuery(cellCount);
        ResultSet lteFeaturesResultSet = statement.executeQuery(lteFeatures);
        while (cellCountResultSet.next() && lteFeaturesResultSet.next()) {
            LSite site = new LSite();
            if (ran == 2)
                site.setENodeBNumberOfCells(cellCountResultSet.getInt(2) + lteFeaturesResultSet.getInt(4));
            else
                site.setENodeBNumberOfCells(cellCountResultSet.getInt(2) + cellCountResultSet.getInt(6));
//            site.setENodeBNumberOfOnAirCells(cellCountResultSet.getInt(3));
            site.setENodeBId(cellCountResultSet.getString(1));
            site.setENodeBName(cellCountResultSet.getString(5));
            site.setENodeBVersion(cellCountResultSet.getString(4));
            site.setENodeBBW(lteFeaturesResultSet.getInt(2));
            site.setENodeBMimo(lteFeaturesResultSet.getInt(3));
            site.finalizeProperties();
            lSites.add(site);
        }

        return lSites;
    }

    public ArrayList<NodeB> getNodeBs() throws Exception {
        Statement statement = connection.createStatement();

        String nodeBCells = "Select RncId,WBTSId,count(WBTSId),sum(AdminCellState) from A_WCEL group by RncId,WBTSId";
        String firstCarrier = "Select RncId,WBTSId,fC,onFC from \n" +
                "(Select RncId,WBTSId from A_WCEL group by RncId,WBTSId)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,count(WBTSId) as fC,sum(AdminCellState) onFC from A_WCEL where UARFCN ='10612' group by RncId,WBTSId )\n" +
                " using (RncId,WBTSId)";
        String secondCarrier = "Select RncId,WBTSId,sC,onSC from \n" +
                "(Select RncId,WBTSId from A_WCEL group by RncId,WBTSId)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,count(WBTSId) as sC,sum(AdminCellState) onSC from A_WCEL where UARFCN ='10637' group by RncId,WBTSId )\n" +
                " using (RncId,WBTSId)";
        String thirdCarrier = "Select RncId,WBTSId,tC,onTC from \n" +
                "(Select RncId,WBTSId from A_WCEL group by RncId,WBTSId)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,count(WBTSId) as tC,sum(AdminCellState) onTC from A_WCEL where UARFCN ='10662' group by RncId,WBTSId )\n" +
                " using (RncId,WBTSId)";
        String u900 = "Select RncId,WBTSId,uC,onUC from \n" +
                "(Select RncId,WBTSId from A_WCEL group by RncId,WBTSId)\n" +
                "left join \n" +
                "(Select RncId,WBTSId,count(WBTSId) as uC,sum(AdminCellState) onUC from A_WCEL where UARFCN ='2988' or UARFCN='2986'\n" +
                " group by RncId,WBTSId )\n" +
                " using (RncId,WBTSId)";
        // Name,Version,etc..
        String generalInfo = "Select RncId,WBTSId,Name,TxMode,Version from (Select RncId,WBTSId from A_WCEL group by RncId,WBTSId)\n" +
                " left join(Select RncId,WBTSId,WBTSName as Name,IubTransportMedia as TxMode,NESWVersion as Version from A_WBTS )\n" +
                " using (RncId , WBTSId)";
        // Processing Sets
        String r99Parameters = "Select RncId,WBTSId,HD1,HD2,HD3,HU1,R99 from \n" +
                "(Select RncId,WBTSId from A_WCEL group by RncId,WBTSId)\n" +
                "left outer join (\n" +
                "Select RncId,WBTSId,numberOfHSDPASet1 as HD1,numberOfHSDPASet2 as HD2,\n" +
                "numberOfHSDPASet3 as HD3,numberOfHSUPASet1 as HU1,numberOfR99ChannelElements \n" +
                "as R99 from A_WBTSF_BTSSCW  ) using (RncId , WBTSId)";

        // COCO
        String coco = "Select RncId,WBTSId,COCOId,P from (Select RncId ,WBTSId  from A_WCEL group by RncId,WBTSId) \n" +
                "left join\n" +
                "(Select RncId ,WBTSId,COCOId from A_WBTS ) using (RncId,WBTSId)\n" +
                "left  join  \n" +
                "(Select RncId ,COCOId,P from (\n" +
                "Select RncId ,COCOId,max(cast (AAL2UPPCR01Egr as int)) as P from A_COCO_AAL2TP  group by RncId,COCOId))\n" +
                " using (RncId,COCOId)";


        ResultSet cellsResult = statement.executeQuery(nodeBCells);
        ResultSet firstCarrierResult = statement.executeQuery(firstCarrier);
        ResultSet secondCarrierResult = statement.executeQuery(secondCarrier);
        ResultSet thirdCarrierResult = statement.executeQuery(thirdCarrier);
        ResultSet u900Result = statement.executeQuery(u900);
        ResultSet generalInfoResultSet = statement.executeQuery(generalInfo);
        ResultSet r99ParametersResultSet = statement.executeQuery(r99Parameters);
        ResultSet cocoResultSet = statement.executeQuery(coco);

        ArrayList<NodeB> nodeBList = new ArrayList<>();
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        while (cellsResult.next() && firstCarrierResult.next() && secondCarrierResult.next() && thirdCarrierResult.next() && u900Result.next()
                && generalInfoResultSet.next() && r99ParametersResultSet.next() && cocoResultSet.next()) {
            NodeB nodeB = new NodeB();
            nodeB.setNodeBRncId(cellsResult.getString(1));
            nodeB.setNodeBWbtsId(cellsResult.getString(2));
            nodeB.setNodeBNumberOfCells(cellsResult.getInt(3));
            nodeB.setNodeBNumberOfOnAirCells(cellsResult.getInt(4));
            nodeB.setNodeBNumberOfFirstCarriersCells(firstCarrierResult.getInt(3));
            nodeB.setNodeBNumberOfOnAirFirstCarriersCells(firstCarrierResult.getInt(4));
            nodeB.setNodeBNumberOfSecondCarriersCells(secondCarrierResult.getInt(3));
            nodeB.setNodeBNumberOfOnAirSecondCarriersCells(secondCarrierResult.getInt(4));
            nodeB.setNodeBNumberOfThirdCarriersCells(thirdCarrierResult.getInt(3));
            nodeB.setNodeBNumberOfOnAirThirdCarriersCells(thirdCarrierResult.getInt(4));
            nodeB.setNodeBNumberOfU900CarriersCells(u900Result.getInt(3));
            nodeB.setNodeBNumberOfOnAirU900CarriersCells(u900Result.getInt(4));
            nodeB.setNodeBName(generalInfoResultSet.getString(3));
            nodeB.setNodeBTxMode(generalInfoResultSet.getString(4));
            nodeB.setNodeBVersion(generalInfoResultSet.getString(5));
            nodeB.setNumberOfHSDPASet1(r99ParametersResultSet.getInt(3));
            nodeB.setNumberOfHSDPASet2(r99ParametersResultSet.getInt(4));
            nodeB.setNumberOfHSDPASet3(r99ParametersResultSet.getInt(5));
            nodeB.setNumberOfHSUPASet1(r99ParametersResultSet.getInt(6));
            nodeB.setNumberOfChannelElements(r99ParametersResultSet.getInt(7));
            nodeB.setNodeBNumberOfE1s(((int) Math.ceil(cocoResultSet.getInt(4) / 4490.0)));
            nodeBList.add(nodeB);
            i++;
            j++;
            k++;
            l++;
        }
        System.out.println(i + " " + j + " " + k + " " + l);
        return nodeBList;
    }


}


