package Helpers;

import sample.Cabinet;
import sample.Hardware;
import sample.LHardware;
import sample.UHardware;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper {
    private Connection connection;
    private String tableName;
    private static final String ID = "ID";
    private static final String CODE = "code";
    private static final String NAME = "name";
    private static final String REGION = "region";
    private static final String WEEK = "week";
    private static final String TECHNOLOGY = "technology";
    private static final String CONTROLLER_ID = "controllerId";
    private static final String NODE_ID = "nodeId";
    private static final String PROPERTIES = "properties";
    private static final String RF_IDENTIFIER = "rfIdentifier";
    private static final String SM_IDENTIFIER = "smIdentifier";
    private static final String TX_IDENTIFIER = "txIdentifier";


    public DatabaseHelper(String path) {
        this.connection = getConnection(path);
    }


    public DatabaseHelper(String path, String tableName) {
        this.connection = getConnection(path);
        this.tableName = tableName;
        try {
            Statement statement = connection.createStatement();
            String tableCreator = "create table if not exists " + tableName + " ( " + ID + " text , " + CODE + " text, "
                    + NAME + " text, " + REGION + " text, " + WEEK + " text NOT NULL , " + TECHNOLOGY + " text, " + CONTROLLER_ID + " text, "
                    + NODE_ID + " text, " + PROPERTIES + " text, " + RF_IDENTIFIER + " text NOT NULL , " + SM_IDENTIFIER + " text NOT NULL , " + TX_IDENTIFIER + " text NOT NULL  "
                    + ")";

//            String Identifiers = "create table if not exists " + tableName + " (" + ID + " text ," + CODE + " text, siteName text ,region text,  "
//                    + "technology integer,bsc text,bscId text,bcfId text,trx integer,gCell text , gTxMode text,gE1s integer,gtrx integer,gLac text,gRac text," +
//                    "gProp text, gRfModuleIdentifier text, gSystemModuleIdentifier text,gTransmissionModuleIdentifier text," +
//
//                    "rncId text,wbtsId text,uCell text, uVersion text,uTxMode text,uE1s integer,r99 text," +
//                    "u9 text,uPower text,uLac text,uRac text,nodeBIp text,uProp text, uRfModuleIdentifier text," +
//                    " uSystemModuleIdentifier text,uTransmissionModuleIdentifier text," +
//
//                    "mrbtsId text,lcell text,lVersion text, bw integer,mimo text,tac text,ip text, lRfModuleIdentifier text, lSystemModuleIdentifier text," +
//
//                    "gDate text) ";

            statement.execute(tableCreator);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection(String path) {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + path, "r", "s");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    //, ArrayList<USite> uSitesList, ArrayList<EnodeB> lSitesList
    public void insertCabinets(ArrayList<Cabinet> cabinets) {

        String gsm = "INSERT INTO " + tableName + " (" + ID + "," + CODE + " ," + NAME + "," + REGION + "," + WEEK + "," + TECHNOLOGY + ","
                + CONTROLLER_ID + "," + NODE_ID + "," + PROPERTIES + "," + RF_IDENTIFIER + "," + SM_IDENTIFIER + "," + TX_IDENTIFIER + " )"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

//        String gsm = "INSERT INTO " + tableName + " (id, code , siteName ,region,  technology ,bsc, bscId," +
//                "bcfId,trx,gCell,gTxMode,gE1s,gtrx,gLac,gRac,gProp, gRfModuleIdentifier ,gSystemModuleIdentifier ,"
//                + "gTransmissionModuleIdentifier,gDate )"
//                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement pr2 = connection.prepareStatement(gsm)) {

            cabinets.forEach(cabinet -> {
                try {
                    Hardware hardware = cabinet.getHardware();
                    pr2.setString(1, cabinet.getUniqueName());
                    pr2.setString(2, cabinet.getCode());
                    pr2.setString(3, cabinet.getName());
                    pr2.setString(4, cabinet.getRegion());
                    pr2.setString(5, hardware.getWeek());
                    pr2.setInt(6, cabinet.getTechnology());
                    pr2.setString(7, cabinet.getControllerId());
                    pr2.setString(8, cabinet.getNodeId());
                    pr2.setString(9, cabinet.getProperties());
                    pr2.setString(10, hardware.getRfIdentifier());
                    pr2.setString(11, hardware.getSmIdentifier());
                    pr2.setString(12, hardware.getTxIdentifier());
                    pr2.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            pr2.executeBatch();
            System.out.println(cabinets.get(0).getTechnology() + "G Identifiers Table is done..");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
//        String lte = "INSERT INTO " + tableName + " (ID, siteName , siteCode , Technology_Identifier,onAirCells, bwIdentifier ,"
//                + "lRfModuleIdentifier , lSystemModuleIdentifier )"
//                + "VALUES(?,?,?,?,?,?,?,?)";
//        EnodeB lSite = null;
//        try (PreparedStatement pr4 = connection.prepareStatement(lte)) {
//            for (int i = 0; i < lSitesList.size(); i++) {
//                lSite = lSitesList.get(i);
//                pr4.setString(1, lSite.getUniqueName());
//                pr4.setString(2, lSite.getENodeBName());
//                pr4.setString(3, lSite.getENodeBCode());
//                pr4.setInt(4, 4);
//                pr4.setDouble(5, lSite.getENodeBNumberOfOnAirCells());
//                pr4.setDouble(6, lSite.getBwIdentifier());
//                pr4.setString(7, lSite.getLHardware().rfString);
//                pr4.setString(8, lSite.getLHardware().smString);
//                pr4.executeUpdate();
//            }
//
//
//            pr4.executeBatch();
//            System.out.println("4G Identifiers Table is done..");
//            System.out.println("Identifiers Table is done..");
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            lSite.getENodeBName();
//        }


//    public void insertNodeBs(ArrayList<NodeB> nodeBs) {
//        String umts = "INSERT INTO " + tableName + " (id, code , siteName ,region, technology ,rncId,wbtsId,uCell,uVersion,uTxMode,uE1s,r99,u9,uPower," +
//                "uLac,uRac,nodeBIp,uProp, uRfModuleIdentifier ,uSystemModuleIdentifier ,uTransmissionModuleIdentifier,gDate)"
//                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//
//        String umts = "INSERT INTO " + tableName + " (" + ID + "," + CODE + " ," + NAME + "," + REGION + "," + WEEK + "," + TECHNOLOGY + ","
//                + CONTROLLER_ID + "," + NODE_ID + "," + PROPERTIES + "," + RF_IDENTIFIER + "," + SM_IDENTIFIER + "," + TX_IDENTIFIER + " )"
//                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
//        try (PreparedStatement pr3 = connection.prepareStatement(umts)) {
//            for (NodeB nodeB : nodeBs) {
//                pr3.setString(1, nodeB.getUniqueName());
//                pr3.setString(2, nodeB.getNodeBCode());
//                pr3.setString(3, nodeB.getNodeBName());
//                pr3.setString(4, nodeB.getRegion());
//                pr3.setInt(5, 3);
//                pr3.setString(6, nodeB.getRncId());
//                pr3.setString(7, nodeB.getWbtsId());
//                pr3.setString(8, nodeB.getCellIdentifier());
//                pr3.setString(9, nodeB.getNodeBVersion());
//                pr3.setString(10, nodeB.getNodeBTxMode());
//                pr3.setInt(11, nodeB.getNumberOfE1s());
//                pr3.setString(12, nodeB.getR99Identifier());
//                pr3.setString(13, nodeB.getU9Identifier());
//                pr3.setString(14, nodeB.getPowerIdentifier());
//                pr3.setString(15, nodeB.getLac());
//                pr3.setString(16, nodeB.getRac());
//                pr3.setString(17, nodeB.getNodeBIP());
//                pr3.setString(18, nodeB.getProperties());
//                Hardware uHardware = nodeB.getHardware();
//                pr3.setString(19, uHardware.getRfIdentifier());
//                pr3.setString(20, uHardware.getSmIdentifier());
//                pr3.setString(21, uHardware.getTxIdentifier());
//                pr3.setString(22, uHardware.getWeek());
//                pr3.executeUpdate();
//
//            }
//            System.out.println("3G Identifiers Table is done..");
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }


//    public void insertEnodeBs(ArrayList<EnodeB> eNodeBs) {
//        String lte = "INSERT INTO " + tableName + " (id, code , siteName, region , technology," +
//                "mrbtsId ,lcell ,lVersion , bw ,mimo ,tac ,ip, lRfModuleIdentifier , lSystemModuleIdentifier ,gDate )"
//                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//        try (PreparedStatement pr4 = connection.prepareStatement(lte)) {
//            for (EnodeB enodeB : eNodeBs) {
//                pr4.setString(1, enodeB.getUniqueName());
//                pr4.setString(2, enodeB.getCode());
//                pr4.setString(3, enodeB.getName());
//                pr4.setString(4, enodeB.getRegion());
//                pr4.setInt(5, 4);
//                pr4.setString(6, enodeB.getNodeId());
//                pr4.setString(7, enodeB.getCellIdentifier());
//                pr4.setString(8, enodeB.getVersion());
//                pr4.setInt(9, enodeB.getBw());
//                pr4.setInt(10, enodeB.getMimo());
//                pr4.setString(11, enodeB.getTac());
//                pr4.setString(12, enodeB.getIpIdentifier());
//                Hardware lHardware = enodeB.getHardware();
//                pr4.setString(13, lHardware.getRfIdentifier());
//                pr4.setString(14, lHardware.getSmIdentifier());
//                pr4.setString(15, lHardware.getWeek());
//                pr4.executeUpdate();
//            }
//            pr4.executeBatch();
//            System.out.println("4G Identifiers Table is done..");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    public void insertAndRemove(ResultSet gResultSet, ResultSet uResultSet, ResultSet lResultSet) {
//
//        String gsm = "INSERT INTO " + tableName + " (ID, siteName , siteCode , Technology_Identifier,onAirCells, trxIdentifier,"
//                + "pAbisIdentifier ,gRfModuleIdentifier ,gSystemModuleIdentifier ,"
//                + "gTransmissionModuleIdentifier )"
//                + "VALUES(?,?,?,?,?,?,?,?,?,?)";
//        try (PreparedStatement pr2 = connection.prepareStatement(gsm)) {
//
//            while (gResultSet.next()) {
//                pr2.setString(1, gResultSet.getString(1));
//                pr2.setString(2, gResultSet.getString(2));
//                pr2.setString(3, gResultSet.getString(3));
//                pr2.setInt(4, 2);
//                pr2.setInt(5, gResultSet.getInt(4));
//                pr2.setInt(6, gResultSet.getInt(5));
//                pr2.setInt(7, gResultSet.getInt(6));
//                pr2.setString(8, gResultSet.getString(7));
//                pr2.setString(9, gResultSet.getString(8));
//                pr2.setString(10, gResultSet.getString(9));
//                pr2.executeUpdate();
//            }
//            pr2.executeBatch();
//            System.out.println("2G Identifiers Table is done..");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        String umts = "INSERT INTO " + tableName + " (ID, siteName , siteCode , Technology_Identifier ,onAirCells, processingSetsIdentifier "
//                + ", channelElementsIdentifier , "
//                + "carriersIdentifier ,powerIdentifier ,uRfModuleIdentifier ,uSystemModuleIdentifier ,uTransmissionModuleIdentifier)"
//                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
//        try (PreparedStatement pr3 = connection.prepareStatement(umts)) {
//            while (uResultSet.next()) {
//                pr3.setString(1, uResultSet.getString(1));
//                pr3.setString(2, uResultSet.getString(2));
//                pr3.setString(3, uResultSet.getString(3));
//                pr3.setInt(4, 2);
//                pr3.setInt(5, uResultSet.getInt(4));
//                pr3.setInt(6, uResultSet.getInt(5));
//                pr3.setInt(7, uResultSet.getInt(6));
//                pr3.setString(8, uResultSet.getString(7));
//                pr3.setString(9, uResultSet.getString(8));
//                pr3.setString(10, uResultSet.getString(9));
//                pr3.setString(11, uResultSet.getString(10));
//                pr3.setString(12, uResultSet.getString(11));
//                pr3.executeUpdate();
//            }
//            pr3.executeBatch();
//            System.out.println("3G Identifiers Table is done..");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        String lte = "INSERT INTO " + tableName + " (ID, siteName , siteCode , Technology_Identifier,onAirCells, bwIdentifier ,"
//                + "lRfModuleIdentifier , lSystemModuleIdentifier )"
//                + "VALUES(?,?,?,?,?,?,?,?)";
//
//        try (PreparedStatement pr4 = connection.prepareStatement(lte)) {
//            while (lResultSet.next()) {
//                pr4.setString(1, lResultSet.getString(1));
//                pr4.setString(2, lResultSet.getString(2));
//                pr4.setString(3, lResultSet.getString(3));
//                pr4.setInt(4, 2);
//                pr4.setInt(5, lResultSet.getInt(4));
//                pr4.setInt(6, lResultSet.getInt(5));
//                pr4.setInt(7, lResultSet.getInt(6));
//                pr4.setString(8, lResultSet.getString(7));
//
//                pr4.executeUpdate();
//            }
//            pr4.executeBatch();
//            System.out.println("4G Identifiers Table is done..");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public ArrayList<String> getTableNames() {
        ArrayList<String> tableNames = new ArrayList<>();
        DatabaseMetaData md = null;
        try {
            md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableNames;

    }

    public ResultSet compareDumps(String currentTable, String oldTable) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            String query = " Select * from (Select * from " + currentTable +
                    ") as firstSet left join (Select * from " + oldTable + ") as secondSet" +
                    " on(firstSet.ID = secondSet.ID)";
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public Hardware getMissingHW(String uniqueName, int weekNumber) {
        ResultSet resultSet;
        Hardware hardware;
        try {
            Statement statement = connection.createStatement();
            while (weekNumber > 0) {

                String existingTable = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='W" + weekNumber + "'";
                resultSet = statement.executeQuery(existingTable);
                if (resultSet.getInt(1) > 0) {
                    hardware = getHardwareFromWeek(uniqueName, weekNumber);
                    if (hardware != null) {
                        return hardware;
                    }
                }
                weekNumber--;
            }

            this.connection.close();
            //Todo:check close conn here

        } catch (SQLException e) {
//            e.printStackTrace();
        }
        return new Hardware("W" + weekNumber);
    }

    private Hardware getHardwareFromWeek(String uniqueName, int weekNumber) throws SQLException {

        Hardware hardware;
        ResultSet resultSet;
        Statement statement = connection.createStatement();
        String hwQuery = "Select " + RF_IDENTIFIER + "," + SM_IDENTIFIER + "," + TX_IDENTIFIER + "," + WEEK + " from W" +
                weekNumber + " where ID='" + uniqueName + "'";
        resultSet = statement.executeQuery(hwQuery);
        while (resultSet.next()) {
//            if (!resultSet.getString(1).equals("")) {
            if (resultSet.getString(1) != null) {
                hardware = new Hardware(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
                hardware.setWeek(resultSet.getString(4));
                connection.close();
                return hardware;
            }
        }
        this.connection.close();
        return null;
    }


//    public void get2GMissingHW(BCF value, int weekNumber) {
//        ResultSet resultSet;
//        try {
//            Statement statement = connection.createStatement();
//            while (weekNumber > 0) {
//
//                String existingTable = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='W" + weekNumber + "'";
//                resultSet = statement.executeQuery(existingTable);
//                if (resultSet.getInt(1) > 0) {
//                    GSite.GHardware gHardware = get2gHardware(value, weekNumber);
//                    if (gHardware != null) {
//                        value.setGHardware(gHardware);
//                        break;
//                    }
//                }
//                weekNumber--;
//            }
//            this.connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

//
//    private GSite.GHardware get2gHardware(BCF value, int weekNumber) throws SQLException {
//        GSite.GHardware gHardware = new GSite.GHardware();
//        ResultSet resultSet;
//        Statement statement = connection.createStatement();
//        String hwQuery = "Select gRfModuleIdentifier,gSystemModuleIdentifier,gTransmissionModuleIdentifier,gDate from W" +
//                weekNumber + " where ID='" + value.getUniqueName() + "'";
//        resultSet = statement.executeQuery(hwQuery);
//        while (resultSet.next()) {
//            if (!resultSet.getString(1).equals("")) {
//                gHardware.setRfModuleIdentifier(resultSet.getString(1));
//                gHardware.setSmIdentifier(resultSet.getString(2));
//                gHardware.setTxModuleIdentifier(resultSet.getString(3));
//                gHardware.setWeek(resultSet.getString(4));
//                connection.close();
//                return gHardware;
//            }
//        }
//        return null;
//    }

//    public void getMissing3gHW(NodeB nodeB, int weekNumber) {
//        ResultSet resultSet;
//        try {
//            Statement statement = connection.createStatement();
//            while (weekNumber > 0) {
//
//                String existingTable = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='W" + weekNumber + "'";
//                resultSet = statement.executeQuery(existingTable);
//                if (resultSet.getInt(1) > 0) {
//                    UHardware uHardware = get3gHardware(nodeB.getUniqueName(), weekNumber);
//                    if (uHardware != null) {
//                        nodeB.setUhardware(uHardware);
//                        break;
//                    }
//                }
//                weekNumber--;
//            }
//            this.connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    private UHardware get3gHardware(String uniqueName, int weekNumber) throws SQLException {
        UHardware uHardware = new UHardware();
        ResultSet resultSet;
        Statement statement = connection.createStatement();
        String hwQuery = "Select uRfModuleIdentifier,uSystemModuleIdentifier,uTransmissionModuleIdentifier,gDate from W"
                + weekNumber + " where ID='" + uniqueName + "'";
        resultSet = statement.executeQuery(hwQuery);
        while (resultSet.next()) {
            if (!resultSet.getString(1).equals("")) {
                uHardware.setRfModIdentifier(resultSet.getString(1));
                uHardware.setSysModIdentifier(resultSet.getString(2));
                uHardware.setTxModIdentifier(resultSet.getString(3));
                uHardware.setWeek(resultSet.getString(4));
                connection.close();
                return uHardware;
            }
        }
        return null;
    }


    public boolean isDatabaseEmpty() {
        ResultSet resultSet;
        boolean isEmpty = false;
        try {
            Statement statement = connection.createStatement();
            String existingTable = "SELECT count(*) FROM sqlite_master WHERE type='table'";
            resultSet = statement.executeQuery(existingTable);
            isEmpty = !(resultSet.getInt(1) > 0);

            this.connection.close();
        } catch (SQLException e) {
            System.out.println("Problem in isDatabaseEmpty method ");
        }
        return isEmpty;
    }

    private Hardware get4gHardware(String uniqueName, int weekNumber) throws SQLException {
        LHardware lHardware = new LHardware();
        ResultSet resultSet;
        Statement statement = connection.createStatement();
        String hwQuery = "Select lRfModuleIdentifier,lSystemModuleIdentifier,gDate from W" + weekNumber + " where ID='" + uniqueName + "'";
        resultSet = statement.executeQuery(hwQuery);
        while (resultSet.next()) {
            if (!resultSet.getString(1).equals("")) {
                Hardware hardware = new Hardware("0.1.0.1.0.1.0.1.0.1.0.1.0.1.0.",
                        "0.1.0.1.0.1.0.1.", "0.0.0.0.0.0.");
                hardware.setWeek(resultSet.getString("gDate"));
            }
        }


        return null;
    }

    public ArrayList<Cabinet> loadCabinets(int technology) throws SQLException {
        ArrayList<Cabinet> cabinets = new ArrayList<>();
        ResultSet resultSet;
        Statement statement = connection.createStatement();
        String bcfQuery = "Select * from " + tableName + " where +" + TECHNOLOGY + " = '" + technology + "'";
        resultSet = statement.executeQuery(bcfQuery);
        while (resultSet.next()) {
            Cabinet cabinet = Cabinet.nodeProvider(technology);
            cabinet.setUniqueName(resultSet.getString(ID));
            cabinet.setCode(resultSet.getString(CODE));
            cabinet.setName(resultSet.getString(NAME));
            cabinet.setRegion(resultSet.getString(REGION));
            cabinet.setControllerId(resultSet.getString(CONTROLLER_ID));
            cabinet.setNodeId(resultSet.getString(NODE_ID));
            cabinet.setProperties(resultSet.getString(PROPERTIES));
            Hardware hardware = new Hardware(resultSet.getString(RF_IDENTIFIER),
                    resultSet.getString(SM_IDENTIFIER), resultSet.getString(TX_IDENTIFIER));
            hardware.setWeek(resultSet.getString(WEEK));
            hardware.setTech(resultSet.getInt(TECHNOLOGY));
            cabinet.setHardware(hardware);
            cabinet.finishProperties();
            cabinets.add(cabinet);
        }
        System.out.println("Number of " + technology + "G Cabinets: " + cabinets.size());
        return cabinets;
    }


    public HashMap<String, Hardware> loadDumpHwMap() {
        ResultSet resultSet;
        Statement statement;
        HashMap<String, Hardware> dumpHwMap = new HashMap<>();
        try {
            statement = connection.createStatement();
            String nodeBQuery = "Select " + ID + "," + CODE + "," + NAME + "," + WEEK + " from " + tableName;
            resultSet = statement.executeQuery(nodeBQuery);


            while (resultSet.next()) {
                String key = resultSet.getString(ID);
                Hardware hardware = new Hardware(resultSet.getString(WEEK));
                hardware.setCode(resultSet.getString(CODE));
                hardware.setName(resultSet.getString(NAME));
                dumpHwMap.put(key, hardware);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dumpHwMap;
    }

    public boolean isWeekInserted(int tech) throws SQLException {
        ResultSet resultSet;
        Statement statement = connection.createStatement();
        String query = "Select technology from " + tableName + " where technology = " + tech;
        resultSet = statement.executeQuery(query);
        return resultSet.next();
    }


}