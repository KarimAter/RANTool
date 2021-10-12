package Helpers;

import sample.Hardware;
import sample.HwItem;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SerialDatabaseSaver {
    private Connection connection;
    private static final String TABLE_NAME = "Serials";
    private static final String ID = "ID";
    private static final String CODE = "Code";
    private static final String NAME = "Name";
    private static final String USER_LABEL = "UserLabel";
    private static final String SERIAL = "SerialNumber";
    private static final String ID_CODE = "IdentificationCode";
    private static final String WEEK = "Week";

    public SerialDatabaseSaver(String path) {
        this.connection = getConnection(path);
        try {
            Statement statement = connection.createStatement();
            String tableCreator = "create table if not exists " + TABLE_NAME
                    + " (" + ID + " text, " + CODE + " text, " + NAME + " text, " + USER_LABEL + " text, "
                    + SERIAL + " text, " + ID_CODE + " text, unique (" + ID + "," + SERIAL + ") )";
//            String x="create table if not exists Serials( ID text, Serial text  ,status text, unique (ID, SERIAL) )";

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


    public boolean insertAndRemove(HashMap<String, Hardware> xmlHwMap, String weekName) {
        List<String> deletedList;
        // creating a map with all serials in serials database
        Map<String, String> databaseSerials = new HashMap<>();
        String loadSerials = "Select " + SERIAL + ", " + ID + " from " + TABLE_NAME;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(loadSerials);
            while (resultSet.next()) {
                databaseSerials.put(resultSet.getString(1) + "__" + resultSet.getString(2),
                        resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        DatabaseHelper databaseHelper = new DatabaseHelper("C:/Ater/Development/RAN Tool/NokiaDumpToolHistory.db", weekName);
        HashMap<String, Hardware> dumpHwMap = databaseHelper.loadSbtsNodeRelations();

        // Deleting converted SRAN sites from all technologies HW files

        // getting SBTS Ids
        List<String> sbtsList = dumpHwMap.entrySet().stream()
                .filter(stringHardwareEntry -> !stringHardwareEntry.getValue().getSBTSId().equals("null"))
                .filter(stringHardwareEntry -> !stringHardwareEntry.getKey().contains("SBTS"))
                .map(Map.Entry::getKey).collect(Collectors.toList());

        List<String> serialDbIds = databaseSerials.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        List<String> dumpRemovedNodes = serialDbIds.stream().filter(n -> !dumpHwMap.containsKey(n)).collect(Collectors.toList());

        System.out.println("Deleting " + sbtsList.size() + " Old SRAN 2G-3G-4G Nodes");
        System.out.println("Deleting " + dumpRemovedNodes.size() + " have been removed from dump");

        deletedList = new ArrayList<>(sbtsList);
        deletedList.addAll(dumpRemovedNodes);

        System.out.println("Deleting total " + deletedList.size() + " nodes");

        String deleteUnusedNodes = "Delete from " + TABLE_NAME + " where " + ID + " = ?";
        try {
            PreparedStatement deletionStatement = connection.prepareStatement(deleteUnusedNodes);
            final int[] cnt = {0};
            deletedList.forEach(nodeId -> {
                try {
                    deletionStatement.setString(1, nodeId);
                    deletionStatement.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                cnt[0]++;
                if (cnt[0] % 1000 == 0) {
                    try {
                        deletionStatement.executeBatch();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            try {
                deletionStatement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


//        // creating a map with all serials in XML files
//        HashMap<String, Hardware> xmlSerialListMap = xmlHwMap.entrySet().stream().collect(
//                HashMap::new, (m, e) -> e.getValue().getHwItems().forEach(v -> m.put(v.getSerialNumber(), e.getValue())), Map::putAll);
//        System.out.println("HwItems from XMLs: " + xmlSerialListMap.size());

        // creating a map with all serials in XML files
        HashMap<String, Hardware> xmlSerialListMap2 = xmlHwMap.entrySet().stream().collect(
                HashMap::new, (m, e) -> e.getValue().getHwItems().
                        forEach(v -> m.put(v.getSerialNumber() + "__" + e.getValue().getUniqueName(), e.getValue())), Map::putAll);
        System.out.println("HwItems from XMLs: " + xmlSerialListMap2.size());


        //Insertion
        // filtering serials in xmls files not found in database ,i.e new serials
        // and removing redundant lte XML if site is SRAN
        Map<String, Hardware> newSerialsMap = xmlSerialListMap2.entrySet().parallelStream().
                filter(stringHardwareEntry -> !sbtsList.contains(stringHardwareEntry.getValue().getUniqueName()))
                .filter(stringHardwareEntry -> !databaseSerials.containsKey(stringHardwareEntry.getKey())
                        || !databaseSerials.containsValue(stringHardwareEntry.getValue().getUniqueName())
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("New HwItems from XMLs: " + newSerialsMap.size());

//

        // Inserting in serials databases
        String dumpHardwareInsertion = "INSERT INTO " + TABLE_NAME + " (" + ID + "," + CODE + "," + NAME + ","
                + USER_LABEL + "," + SERIAL + "," + ID_CODE + ")"
                + "VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement insertionStatement = connection.prepareStatement(dumpHardwareInsertion);
            final int[] cnt = {0};
            newSerialsMap.forEach((serial, hardware) -> {
                try {

                    String[] split = serial.split("__");
                    String serialNumber = split[0];
                    String uniqueName = hardware.getUniqueName();
                    Hardware dumpHardware = dumpHwMap.get(uniqueName);
                    insertionStatement.setString(1, uniqueName);
                    if (dumpHardware != null) {
                        insertionStatement.setString(2, dumpHwMap.get(uniqueName).getCode());
                        insertionStatement.setString(3, dumpHwMap.get(uniqueName).getName());
                    } else {
                        insertionStatement.setString(2, "");
                        insertionStatement.setString(3, "");
                    }
                    List<HwItem> hwItem = hardware.getHwItems().stream().filter(item ->
                            item.getSerialNumber().equals(serialNumber)).collect(Collectors.toList());
                    insertionStatement.setString(4, hwItem.get(0).getUserLabel());
                    insertionStatement.setString(5, hwItem.get(0).getSerialNumber());
                    insertionStatement.setString(6, hwItem.get(0).getIdentificationCode());
                    insertionStatement.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                cnt[0]++;
                if (cnt[0] % 1000 == 0) {
                    try {
                        insertionStatement.executeBatch();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            try {
                insertionStatement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Inserting in Serials database is done.. " + Utils.getTime());

        //Deletion
        // filtering database serials not found in xml files,and deleting them if site is found in xml file,
        // i.e:deleting replaced items
//        Map<String, String> deletedSerialsMap = databaseSerials.entrySet().parallelStream()
//                .filter(databaseSerial -> !xmlSerialListMap2.containsKey(databaseSerial.getKey()))
////                .filter(stringStringEntry -> xmlHwMap.containsKey(stringStringEntry.getValue()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        Map<String, String> deletedSerialsMap = databaseSerials.entrySet().parallelStream()
                .filter(databaseSerial -> (!xmlSerialListMap2.containsKey(databaseSerial.getKey()))
                        && (xmlHwMap.containsKey(databaseSerial.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println("deleted items from database: " + deletedSerialsMap.size());
        // deleting replace items from database
        String deleteExtraHw = "Delete from " + TABLE_NAME + " where " + SERIAL + " =? and " + ID + " = ?";
        try {
            PreparedStatement deletionStatement = connection.prepareStatement(deleteExtraHw);
            final int[] cnt = {0};
            deletedSerialsMap.forEach((serial, hardware) -> {
                try {
                    String[] parts = serial.split("__");
                    deletionStatement.setString(1, parts[0]);
                    deletionStatement.setString(2, parts[1]);
                    deletionStatement.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                cnt[0]++;
                if (cnt[0] % 1000 == 0) {
                    try {
                        deletionStatement.executeBatch();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            try {
                deletionStatement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("deletion done.. " + Utils.getTime());

        return false;
    }

    public void updateSiteNames(String weekName) {
        DatabaseHelper databaseHelper = new DatabaseHelper("C:/Ater/Development/RAN Tool/NokiaDumpToolHistory.db", weekName);
        HashMap<String, Hardware> stringHardwareHashMap = databaseHelper.loadDumpHwMap();
        ResultSet resultSet;
        Statement statement;
        ArrayList<String> ids = new ArrayList<>();
        try {
            statement = connection.createStatement();
            String selectBlanks = "Select " + ID + " from " + TABLE_NAME + " where " + CODE + " is NULL or Code =''";
            resultSet = statement.executeQuery(selectBlanks);
            while (resultSet.next()) {
                ids.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (String id : ids) {
            try {
                String code;
                String name;
                try {
                    code = stringHardwareHashMap.get(id).getCode();
                    name = stringHardwareHashMap.get(id).getName();
                } catch (Exception e) {
                    code = "";
                    name = "";
                }
                System.out.println(id);
                System.out.println(code + " " + name);
                statement = connection.createStatement();
                String update = "UPDATE Serials SET Code = '"
                        + code + "', Name = '" +
                        name + "' where ID = '" + id + "'";
                statement.execute(update);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet load(String tech) {

        ResultSet resultSet = null;
        Statement statement;
        try {
            statement = connection.createStatement();
            String bcfQuery = "Select * from " + TABLE_NAME + " where " + ID + " like '%" + tech + "%' ORDER by Code ASC";
            resultSet = statement.executeQuery(bcfQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public Hardware getMissinHw(String uniqueName, String weekName) {
        ArrayList<HwItem> hwItems = new ArrayList<>();
        ResultSet resultSet;
        Statement statement;
        Hardware hardware;
        try {
            statement = connection.createStatement();
            String bcfQuery = "Select " + USER_LABEL + " from " + TABLE_NAME + " where " + ID + " = '" + uniqueName + "'";

            resultSet = statement.executeQuery(bcfQuery);

            while (resultSet.next()) {
                hwItems.add(new HwItem(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        hardware = new Hardware(hwItems);
        DatabaseHelper databaseHelper = new DatabaseHelper("C:/Ater/Development/RAN Tool/NokiaDumpToolHistory.db");
        hardware.setWeek(databaseHelper.getMissingHWWeek(uniqueName, weekName));
        return hardware;
    }

    public Map<String, Long> getHardwareCount() {
        Map<String, Long> modules = new LinkedHashMap<>();
        ResultSet resultSet = null;
        Statement statement;
        try {
            statement = connection.createStatement();
            String bcfQuery = "Select UserLabel,count(UserLabel) from (" +
                    "Select * from Serials group by SerialNumber ) group by UserLabel";

            resultSet = statement.executeQuery(bcfQuery);
            while (resultSet.next()) {
                modules.put(resultSet.getString(1), resultSet.getLong(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<String> userLabels = new ArrayList<>(Constants.rfMap.values());
        userLabels.addAll(new ArrayList<>(Constants.smMap.values()));
        userLabels.addAll(new ArrayList<>(Constants.txMap.values()));
        return modules.entrySet().stream()
                .filter(stringLongEntry -> userLabels.contains(stringLongEntry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }
}
