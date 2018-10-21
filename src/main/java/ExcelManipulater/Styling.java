package ExcelManipulater;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Styling {

    public static void autoSizeColumns(XSSFWorkbook workbook, int noOfColumns) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            XSSFSheet sheet = workbook.getSheetAt(i);
            for (int j = 0; j < noOfColumns; j++) {
                sheet.autoSizeColumn(j);
            }
        }
    }
}
