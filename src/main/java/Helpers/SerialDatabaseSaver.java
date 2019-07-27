package Helpers;

import sample.Hardware;
import sample.HwItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        DatabaseHelper databaseHelper = new DatabaseHelper("D:/RAN Tool/NokiaDumpToolHistory.db", weekName);
        HashMap<String, Hardware> dumpHwMap = databaseHelper.loadDumpHwMap();

        // creating a map with all serials in serials database
        Map<String, String> databaseSerials = new HashMap<>();
        String loadSerials = "Select " + SERIAL + ", " + ID + " from " + TABLE_NAME;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(loadSerials);
            while (resultSet.next()) {
                databaseSerials.put(resultSet.getString(1) + "__" + resultSet.getString(2), resultSet.getString(2));
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
                HashMap::new, (m, e) -> e.getValue().getHwItems().forEach(v -> m.put(v.getSerialNumber() + "__" + e.getValue().getUniqueName(), e.getValue())), Map::putAll);
        System.out.println("HwItems from XMLs: " + xmlSerialListMap2.size());


        //Insertion
        // filtering serials in xmls files not found in database,i.e new serials
        Map<String, Hardware> newSerialsMap = xmlSerialListMap2.entrySet().parallelStream().
                filter(stringHardwareEntry -> !databaseSerials.containsKey(stringHardwareEntry.getKey())
                        || !databaseSerials.containsValue(stringHardwareEntry.getValue().getUniqueName())
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("New HwItems from XMLs: " + newSerialsMap.size());


        newSerialsMap.forEach((s, h) -> System.out.println(s + h.getUniqueName()));

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
                    List<HwItem> hwItem = hardware.getHwItems().stream().filter(item -> item.getSerialNumber().equals(serialNumber)).collect(Collectors.toList());
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
        // filtering database serials not found in xml files,and deleting them if site is found in xml file,i.e:deleting replaced items
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
                    System.out.println(serial);
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
//
//        // if it is the initial insertion, break and insert from Main
//        if (databaseHelper.isDatabaseEmpty())
//            return true;
//        else {
//            // to form the main list of dump nodes
//            Map<String, Hardware> collect = dumpHwMap.entrySet()
//                    .stream().collect(Collectors.toMap(Map.Entry::getKey, (entry) -> {
//                        String key = entry.getKey();
//                        Hardware value = entry.getValue();
//
//                        Hardware h = xmlHwMap.get(key);
//                        if (h != null) {
//                            h.setCode(value.getCode());
//                            h.setName(value.getName());
//                            xmlHwMap.remove(key);
//                            return h;
//                        }
//                        return (Hardware) value;
//                    }));
//

//            String deleteExtraHw = "Delete from " + TABLE_NAME + " where " + ID + " =? ";
//
//            System.out.println("Deleting "+xmlHwMap.size()+" nodes not found in dump "+Utils.getTime());
//            try (PreparedStatement pr1 = connection.prepareStatement(deleteExtraHw)) {
//                xmlHwMap.forEach((key, value) -> {
//                    // set the corresponding param
//                    try {
//                        pr1.setString(1, key);
//                        pr1.executeUpdate();
//                        pr1.executeBatch();
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//
//                });
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }


//            System.out.println("Inserting " + collect.size() + " hardware  in Serials database " + Utils.getTime());
//            String s = "Update " + TABLE_NAME + " set " + WEEK + " = ? where " + ID + " = ?";
//            String d = "Delete from " + TABLE_NAME + "  where " + ID + " = ? and " + WEEK + " != ?";
//
//
//            try {
//                PreparedStatement pr2 = connection.prepareStatement(dumpHardwareInsertion);
//                PreparedStatement pr3 = connection.prepareStatement(s);
//                final int[] counter = {0};
//                collect.forEach((key, value) -> {
//                    try {
//                        ArrayList<HwItem> hwItems = value.getHwItems();
//                        hwItems.forEach(hwItem -> {
//                            counter[0]++;
//                            String code = value.getCode();
//                            String name = value.getName();
//                            try {
//                                pr2.setString(1, key);
//                                pr2.setString(2, code);
//                                pr2.setString(3, name);
//                                pr2.setString(4, hwItem.getUserLabel());
//                                pr2.setString(5, hwItem.getSerialNumber());
//                                pr2.setString(6, hwItem.getIdentificationCode());
//                                pr2.setString(7, weekName);
////                            pr2.setString(3, "onair");
////                                pr2.executeUpdate();
//
//                                pr2.addBatch();
////                            } catch (SQLException ignored) {
////
////                            }
//
////                        });
////                        if (counter[0] % 1000 == 0) {
////                            System.out.println("executing batch");
////                            pr2.executeBatch();
////                            pr3.executeBatch();
////                        }
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//
//                                try {
//                                    pr3.setString(1, weekName);
//                                    pr3.setString(2, key);
//                                    pr3.addBatch();
//
//                                } catch (SQLException ex) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        });
//                        if (counter[0] % 1000 == 0) {
//                            pr2.executeBatch();
//                            pr3.executeBatch();
//                        }
//
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                });
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                PreparedStatement pr4 = connection.prepareStatement(d);
//                collect.forEach((key, value) -> {
//                    try {
//                        pr4.setString(1, key);
//                        pr4.setString(2, weekName);
//                        pr4.addBatch();
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                });
//                System.out.println("deleting");
//                pr4.executeBatch();
//                System.out.println("done");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//
//            try {
//                this.connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        return false;
    }


    public ResultSet load(int tech) {

        ResultSet resultSet = null;
        Statement statement;
        try {
            statement = connection.createStatement();
            String bcfQuery = "Select * from " + TABLE_NAME + " where " + ID + " like '%" + tech + "G%'";

            resultSet = statement.executeQuery(bcfQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public Hardware getMissinHw(String uniqueName) {
        ArrayList<HwItem> hwItems = new ArrayList<>();
        ResultSet resultSet;
        Statement statement;
        try {
            statement = connection.createStatement();
            String bcfQuery = "Select " + USER_LABEL + " from " + TABLE_NAME + " where " + ID + " = '" + uniqueName + "'";

            resultSet = statement.executeQuery(bcfQuery);

            while (resultSet.next()) {
                hwItems.add(new HwItem(resultSet.getString(1)));
                System.out.println(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Hardware(hwItems);
    }
}
