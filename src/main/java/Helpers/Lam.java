package Helpers;

import sample.EnodeB;
import sample.Hardware;

import java.sql.*;
import java.util.ArrayList;
import java.util.function.BiFunction;

public class Lam {
    Connection connection;

    private Connection getConnection(String path) {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + path, "r", "s");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void getMissing4gHW(EnodeB enodeB, int weekNumber) {
        ResultSet resultSet;
        connection = getConnection("D:/RAN Tool/NokiaDumpToolHistory.db");
        try {
            Statement statement = connection.createStatement();
            while (weekNumber > 0) {

                String existingTable = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='W" + weekNumber + "'";
                resultSet = statement.executeQuery(existingTable);
                if (resultSet.getInt(1) > 0) {
                    Hardware lHardware = h.apply(enodeB.getUniqueName(), weekNumber);

                    if (lHardware != null) {
                        enodeB.setHardware(lHardware);
                        break;
                    }
                }
                weekNumber--;
            }
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    BiFunction<String, Integer, Hardware> h = (x, y) -> {
        Hardware lHardware = new Hardware(new ArrayList<>());
        ResultSet resultSet;
        try {
            Statement statement = connection.createStatement();
            String hwQuery = "Select lRfModuleIdentifier,lSystemModuleIdentifier,gDate from W" + y + " where id='" + x + "'";
            resultSet = statement.executeQuery(hwQuery);
            while (resultSet.next()) {
                if (!resultSet.getString(1).equals("")) {
                    lHardware.setRfIdentifier(resultSet.getString(1));
                    lHardware.setSmIdentifier(resultSet.getString(2));
                    lHardware.setWeek(resultSet.getString(3));
                    connection.close();
                    return lHardware;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lHardware;
    };


}
