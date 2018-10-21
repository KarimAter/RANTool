package sample;

import com.healthmarketscience.jackcess.Cursor;
import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NodeBProperties {
    public static int getNumberOfSectors(Database database, NodeB nodeB) {
        int noOfSectors = 0;
        Table table = null;
        Map<String, Integer> criteria = new HashMap<>();

        try {
            table = database.getTable("A_WCEL");

            Cursor cursor = CursorBuilder.createCursor(table);
            criteria.put("RncId", Integer.valueOf(nodeB.getNodeBRncId()));
            criteria.put("WBTSId", Integer.valueOf(nodeB.getNodeBWbtsId()));
            criteria.put("UARFCN", 10612);
            criteria.put("AdminCellState", 1);

            while (cursor.findNextRow(criteria)) {
                noOfSectors++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return noOfSectors;
    }
}
