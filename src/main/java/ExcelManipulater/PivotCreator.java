package ExcelManipulater;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class PivotCreator {

    public static void createPivot(XSSFSheet sheet) {
        AreaReference source = new AreaReference("NodeBs!$A$1:$B$1117",SpreadsheetVersion.EXCEL2007);

        CellReference position = new CellReference("H1");
        // Create a pivot table on this sheet, with H5 as the top-left cell..
        // The pivot table's data source is on the same sheet in A1:D4
        XSSFPivotTable pivotTable = sheet.createPivotTable(source, position);
        //Configure the pivot table
        //Use first column as row label
        pivotTable.addRowLabel(0);
        //Sum up the second column
        pivotTable.addColumnLabel(DataConsolidateFunction.COUNT, 1);
        //Set the third column as filter
//        pivotTable.addColumnLabel(DataConsolidateFunction.AVERAGE, 1);
        //Add filter on forth column
//        pivotTable.addReportFilter(3);



    }
}
