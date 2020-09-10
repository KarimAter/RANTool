package Helpers;

import sample.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

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
                    ("jdbc:ucanaccess://" + dumpPath + ";keepMirror=true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
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
                bcf.setNewCellCount(resultSet.getInt(6));
                bcf.setNewOnAirCount(resultSet.getInt(7));
                bcf.setTxMode(resultSet.getString(8));
                bcf.setNumberOfE1s(resultSet.getInt(9));
                bcf.setNumberOfGTRXs(resultSet.getInt(10));
                bcf.setNumberOfDCSCells(resultSet.getInt(11));
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
        uQuery = "Select  BTSAdditionalInfo,RncId,WBTSId,C,O,BTSAdditionalInfo,fC,onFC,sC,onSC,tC,onTC,uC,onUC,u2C,onU2C," +
                "I,V,HD1,HD2,HD3,HU1,R99,P,S,Name,maxFPower,maxUPower," +
                "vamF,vamU,LAC,RAC,IP,sfp,lcg,noOfChains from (" +
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

        ResultSet nResultSet = statement.executeQuery(uQuery);
        ArrayList<Cabinet> nodeBList = new ArrayList<>();
        while (nResultSet.next()) {
            NodeB nodeB = new NodeB();
            String key = nResultSet.getString(2) + "_" + nResultSet.getString(3);
            nodeB.setCode(nResultSet.getString(1));
            nodeB.setRncId(nResultSet.getString(2));
            nodeB.setWbtsId(nResultSet.getString(3));
            nodeB.setNumberOfCells(nResultSet.getInt(4));
            nodeB.setNumberOfOnAirCells(nResultSet.getInt(5));
            int[] cellCountIndices = IntStream.range(7, 17).map(i -> {
                try {
                    return nResultSet.getInt(i);
                } catch (SQLException e) {
                    return 0;
                }
            }).toArray();
            nodeB.createCellsCountMap(NodeB.cellsCountNames, cellCountIndices);
            nodeB.setTxMode(nResultSet.getString(17));
            nodeB.setVersion(nResultSet.getString(18));
//            nodeB.setNumberOfHSDPASet1(nResultSet.getInt(19));
//            nodeB.setNumberOfHSDPASet2(nResultSet.getInt(20));
//            nodeB.setNumberOfHSDPASet3(nResultSet.getInt(21));
//            nodeB.setNumberOfHSUPASet1(nResultSet.getInt(22));
//            nodeB.setNumberOfChannelElements(nResultSet.getInt(23));
            int[] r99ParamsIndices = IntStream.range(19, 24).map(i -> {
                try {
                    return nResultSet.getInt(i);
                } catch (SQLException e) {
                    return 0;
                }
            }).toArray();
            nodeB.createR99CountMap(NodeB.r99ParametersNames, r99ParamsIndices);
            nodeB.setNumberOfE1s((int) Math.ceil(nResultSet.getInt(24) / 4490.0));
            nodeB.setRfSharing(nResultSet.getInt(25));
            nodeB.setName(nResultSet.getString(26));
            nodeB.setPower(nResultSet.getInt(27), nResultSet.getInt(29));
            nodeB.setU900Power(nResultSet.getInt(28), nResultSet.getInt(30));
            nodeB.setLac(nResultSet.getString(31));
            nodeB.setRac(nResultSet.getString(32));
            nodeB.setNodeBIP(nResultSet.getString(33));
            nodeB.setSfp(nResultSet.getString(34));
            nodeB.setNumberOfLCGs(nResultSet.getInt(35));
            nodeB.setNumberOfChains(nResultSet.getInt(36));
            nodeB.setConfiguration(uSectorsConfiguration.get(key));
            nodeB.analyzeConfiguration();
            nodeB.finishProperties();
            nodeBList.add(nodeB);
        }
        System.out.println("Number of NodeBs: " + nodeBList.size());
        return nodeBList;
    }

    public ArrayList<Cabinet> getEnodeBs() throws SQLException {
        String lQuery;
        Statement statement = connection.createStatement();
        lQuery = "Select mrbtsId,sum(C),sum(O),first(V),first(N),first(BW),first(M),sum(S),sum(SO),first(TAC),first(manIP),first(s1Ip),first(secIp),first(secGw),first(cAgg) from " +
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
                "(Select mrbtsId,first(mPlaneIpAddress) as manIP,first(uPlaneIpAddress) as s1Ip from A_LTE_MRBTS_LNBTS_FTM_IPNO group by mrbtsId) as sixthSet " +
                "on (firstSet.mrbtsId=sixthSet.mrbtsId) " +
                "left join " +
                "(Select mrbtsId, first(localTunnelEndpoint) as secIp,first(remoteTunnelEndpoint) as secGw from A_LTE_IPSECC_SECURITYPOLICIES" +
                " where ipSecStatus ='0' group by mrbtsId) as seventhSet " +
                "on (firstSet.mrbtsId=seventhSet.mrbtsId) " +
                "left join " +
                "(Select mrbtsId,first(actDLCAggr) as cAgg from A_LTE_LNBTS_CARRAGG group by mrbtsId) as eighthSet " +
                "on (firstSet.mrbtsId=eighthSet.mrbtsId) " +
                "group by " +
                "mrbtsId";
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
            eNodeB.setCarrierAggregation(lResultSet.getInt(15));
            eNodeB.finishProperties();
            enodeBS.add(eNodeB);
        }
        System.out.println("Number of eNodeBs: " + enodeBS.size());
        return enodeBS;
    }

    public ArrayList<Cabinet> getSBTSs() throws SQLException {
        String sbtsQuery;
        Statement statement = connection.createStatement();
        sbtsQuery = "Select mrbtsId,name,version from " +
                "(Select mrbtsId from A_MNL where productVariantPlanned ='10' ) as firstSet " +
                " left join " +
                " (Select mrbtsId,name,version from A_MRBTS) as secondSet " +
                "on (firstSet.mrbtsId=secondSet.mrbtsId)";
        ArrayList<Cabinet> sBtsS = new ArrayList<>();
        ResultSet lResultSet = statement.executeQuery(sbtsQuery);
        while (lResultSet.next()) {
            SBTS sbts = new SBTS();
            sbts.setControllerId(lResultSet.getString(1));
//            eNodeB.setNumberOfCells(lResultSet.getInt(2) + lResultSet.getInt(8));
//            eNodeB.setNumberOfOnAirCells(lResultSet.getInt(3), lResultSet.getInt(9));

            sbts.setName(lResultSet.getString(2));
            sbts.setVersion(lResultSet.getString(3));
//            eNodeB.setBw(lResultSet.getInt(6));
//            eNodeB.setMimo(lResultSet.getInt(7));
//            eNodeB.setTac(lResultSet.getString(10));
//            eNodeB.setManIp(lResultSet.getString(11));
//            eNodeB.setS1Ip(lResultSet.getString(12));
//            eNodeB.setSecIp(lResultSet.getString(13));
//            eNodeB.setSecGw(lResultSet.getString(14));
//            eNodeB.setCarrierAggregation(lResultSet.getInt(15));
//            eNodeB.finishProperties();
            sbts.finishProperties();
            sBtsS.add(sbts);
        }
        System.out.println("Number of Sbts: " + sBtsS.size());
        return sBtsS;

    }

    private HashMap<String, NodeConfiguration> getUSectorsConfiguration() throws SQLException {
        String uSecConfQuery;
        Statement statement = connection.createStatement();
        uSecConfQuery = "Select RncId,WBTSId,lCelwId,group_concat(antId),group_concat(rModId)," +
                "group_concat(prodCode),group_concat(sModId),group_concat(positionInChain),group_concat(linkId),first(defaultCarrier),first(SectorID),first(name)," +
                "first(vamEnabled),first(MaxDLPowerCapability) " +
                "FROM " + " ( " +
                "(SELECT RncId,WBTSId,lCelwId,antlId FROM A_WBTSF_LCELW_RESOURCELIST) as lCelSet " +
                "LEFT JOIN" +
                "(SELECT RncId,WBTSId,antId,antlId,rModId FROM A_WBTSF_RNC_WBTS_MRBTS_ANTL) as antSet " +
                "on (lCelSet.RncId=antSet.RncId and lCelSet.WBTSId=antSet.WBTSId and lCelSet.antlId=antSet.antlId) " +
                "left join " +
                "(SELECT RncId,WBTSId,lcrId,name,MaxDLPowerCapability FROM A_WCEL) as cellNameSet " +
                "on (lCelSet.RncId=cellNameSet.RncId and lCelSet.WBTSId=cellNameSet.WBTSId and lCelSet.lCelwId=cellNameSet.lcrId) " +
                "left join " +
                "(SELECT RncId,WBTSId,rModId,linkId,positionInChain,sModId FROM A_WBTSF_RMOD_CONNECTIONLIST) as connectionSet " +
                "on (antSet.RncId=connectionSet.RncId and antSet.WBTSId=connectionSet.WBTSId and antSet.rModId=connectionSet.rModId) " +
                "left join " +
                "(SELECT RncId,WBTSId,rModId,prodCode,serNum FROM A_WBTSF_RNC_WBTS_MRBTS_RMOD) as rModSet " +
                "on (antSet.RncId=rModSet.RncId and antSet.WBTSId=rModSet.WBTSId and antSet.rModId=rModSet.rModId)" +
                "left join " +
                "(SELECT RncId,WBTSId,lCelwId,defaultCarrier,vamEnabled FROM A_WBTSF_WBTS_MRBTS_BTSSCW_LCELW) as defaultCarrierSet " +
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
            if (key.equalsIgnoreCase("14_316")) {
                int x = 1;
            }
            SectorConfiguration sectorConfiguration = new SectorConfiguration();
            sectorConfiguration.setlCellId(nResultSet.getString(3));
            sectorConfiguration.setAntId(nResultSet.getString(4));
            sectorConfiguration.setrModId(nResultSet.getString(5));
            sectorConfiguration.setProductCode(nResultSet.getString(6));
            sectorConfiguration.setsModId(nResultSet.getString(7));
            sectorConfiguration.setPosInChain(nResultSet.getString(8));
            sectorConfiguration.setLinkId(nResultSet.getString(9));
            sectorConfiguration.setDefaultCarrier(nResultSet.getString(10));
            sectorConfiguration.setCellName(nResultSet.getString(12));
            sectorConfiguration.setSectorId(nResultSet.getString(11));
            sectorConfiguration.setPower(nResultSet.getInt(14), nResultSet.getInt(13));

            try {
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
            } catch (NullPointerException e) {
                System.out.println("Sectors misconfigured " + key);
            }

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
        String query = "Select BSCId,BCFId,BTSId,adminState,cellId,frequencyBandInUse,locationAreaIdLAC,rac,segmentId,name,BSCName,totalTRX,totalPower from " +
                "(Select BSCId,BCFId,BTSId,adminState,cellId,frequencyBandInUse,locationAreaIdLAC,rac,segmentId from A_BTS ) as firstSet "
                + "left join "
                + "(Select BSCId,BCFId,name from A_BCF group by BSCId,BCFId )as secondSet "
                + "on (firstSet.BSCId=secondSet.BSCId and firstSet.BCFId=secondSet.BCFId) "
                + "left join "
                + "(Select BSCId,name as BSCName from A_BSC) as thirdSet " +
                "on (firstSet.BSCId=thirdSet.BSCId) " +
                " left join " +
                "(Select BSCId,BCFId,first(BTSId) as fBTSId,segmentId,count(frequencyBandInUse) as totalTRX ,sum(trxRfPower) as totalPower " +
                "from " +
                "(Select BSCId,BTSId,BCFId,initialFrequency,trxRfPower from A_TRX ) as xDSet " +
                "left join " +
                "(Select BSCId,BCFId,BTSId,segmentId,frequencyBandInUse from A_BTS ) as bDSet " +
                "on (xDSet.BSCId=bDSet.BSCId and xDSet.BTSId=bDSet.BTSId ) " +
                "GROUP BY BSCId,BCFId,segmentId,frequencyBandInUse ) as eighthSet " +
                "on (firstSet.BSCId=eighthSet.BSCId and firstSet.BCFId=eighthSet.BCFId and firstSet.segmentId=eighthSet.segmentId and firstSet.BTSId=eighthSet.fBTSId)";

        return statement.executeQuery(query);
    }

    public ResultSet getUcellsSheet() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "Select BTSAdditionalInfo,name,RncId,WBTSId,AdminCellState,CId,LAC,MaxDLPowerCapability,RAC," +
                "UARFCN,cName,vamEnabled from " +
                "(Select RncId,WBTSId,LcrId,AdminCellState,CId,LAC,MaxDLPowerCapability,RAC,UARFCN,name as cName from A_WCEL ) as firstSet "
                + "left join "
                + "(Select RncId,WBTSId,BTSAdditionalInfo,name from A_WBTS )as secondSet "
                + "on (firstSet.RncId=secondSet.RncId and firstSet.WBTSId=secondSet.WBTSId)"
                + "left join "
                + "(SELECT RncId,WBTSId,lCelwId,vamEnabled FROM A_WBTSF_WBTS_MRBTS_BTSSCW_LCELW )as thirdSet "
                + "on (firstSet.RncId=thirdSet.RncId and firstSet.WBTSId=thirdSet.WBTSId and firstSet.LcrId=thirdSet.lcelwId)";
        return statement.executeQuery(query);
    }
//    public ResultSet getUcellsSheet() throws SQLException {
//        Statement statement = connection.createStatement();
//        String query = "Select BTSAdditionalInfo,name,RncId,WBTSId,AdminCellState,CId,LAC,MaxDLPowerCapability,RAC," +
//                "UARFCN,cName,vamEnabled,group_concat(antId),group_concat(rModId)," + "group_concat(prodCode),group_concat(sModId),group_concat(positionInChain)," +
//                "group_concat(linkId),first(defaultCarrier),first(SectorID),first(name),"
//                + "first(vamEnabled),first(MaxDLPowerCapability)  from " +
//                "(Select RncId,WBTSId,LcrId,AdminCellState,CId,LAC,MaxDLPowerCapability,RAC,UARFCN,name as cName from A_WCEL ) as firstSet "
//                + "left join "
//                + "(Select RncId,WBTSId,BTSAdditionalInfo,name from A_WBTS )as secondSet "
//                + "on (firstSet.RncId=secondSet.RncId and firstSet.WBTSId=secondSet.WBTSId)"
//                + "left join "
//                + "(SELECT RncId,WBTSId,lCelwId,vamEnabled FROM A_WBTSF_WBTS_MRBTS_BTSSCW_LCELW )as thirdSet "
//                + "on (firstSet.RncId=thirdSet.RncId and firstSet.WBTSId=thirdSet.WBTSId and firstSet.LcrId=thirdSet.lcelwId)" +
//
//                "(SELECT RncId,WBTSId,lCelwId,antlId FROM A_WBTSF_LCELW_RESOURCELIST) as lCelSet " +
//                "LEFT JOIN" +
//                "(SELECT RncId,WBTSId,antId,antlId,rModId FROM A_WBTSF_RNC_WBTS_MRBTS_ANTL) as antSet " +
//                "on (lCelSet.RncId=antSet.RncId and lCelSet.WBTSId=antSet.WBTSId and lCelSet.antlId=antSet.antlId) " +
//                "left join " +
//                "(SELECT RncId,WBTSId,rModId,linkId,positionInChain,sModId FROM A_WBTSF_RMOD_CONNECTIONLIST) as connectionSet " +
//                "on (antSet.RncId=connectionSet.RncId and antSet.WBTSId=connectionSet.WBTSId and antSet.rModId=connectionSet.rModId) " +
//                "left join " +
//                "(SELECT RncId,WBTSId,rModId,prodCode,serNum FROM A_WBTSF_RNC_WBTS_MRBTS_RMOD) as rModSet " +
//                "on (antSet.RncId=rModSet.RncId and antSet.WBTSId=rModSet.WBTSId and antSet.rModId=rModSet.rModId)" +
//                "left join " +
//                "(SELECT RncId,WBTSId,LcrId,SectorID FROM A_WCEL_AC) as sectorIdSet " +
//                "on (lCelSet.RncId=sectorIdSet.RncId and lCelSet.WBTSId=sectorIdSet.WBTSId and lCelSet.lCelwId=sectorIdSet.LcrId) " +
//                ") ";
//        return statement.executeQuery(query);
//    }

    public ResultSet getLcellsSheet() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "Select name,mrbtsId,administrativeState,eutraCelId,tac,dlChBw,dlMimoMode,pMax,cellName from " +
                "(Select mrbtsId,administrativeState,cellName,eutraCelId,tac,lnCelId from A_LTE_MRBTS_LNBTS_LNCEL ) as firstSet " +
                "left join " +
                "(Select mrbtsId,name from A_LTE_MRBTS_LNBTS) as secondSet " +
                "on (firstSet.mrbtsId=secondSet.mrbtsId) " +
                "left join " +
                "(Select mrbtsId,dlChBw,dlMimoMode,lnCelId from A_LTE_MRBTS_LNBTS_LNCEL_LNCEL_FDD) as thirdSet " +
                "on (firstSet.mrbtsId=thirdSet.mrbtsId and firstSet.lnCelId=thirdSet.lnCelId) "
                + "left join " +
                "(Select mrbtsId,pMax,lnCelId from A_LTE_LNCEL_PC) as fourthSet " +
                "on (firstSet.mrbtsId=fourthSet.mrbtsId and firstSet.lnCelId=fourthSet.lnCelId) ";
        return statement.executeQuery(query);
    }


}