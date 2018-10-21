package ExcelManipulater;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

public class Gate {

    public static void readFromExcel() {
        try {

            InputStream ExcelFileToRead = new FileInputStream("D:\\test.xlsx");
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

            XSSFWorkbook test = new XSSFWorkbook();

            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;

            Iterator rows = sheet.rowIterator();

            while (rows.hasNext()) {
                row = (XSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                while (cells.hasNext()) {
                    cell = (XSSFCell) cells.next();

                    if (cell.getCellType() == CellType.STRING) {
                        System.out.print(cell.getStringCellValue() + " ");
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        System.out.print(cell.getNumericCellValue() + " ");
                    } else {
                        //U Can Handel Boolean, Formula, Errors
                    }
                }
                System.out.println();
            }
        } catch (Exception e) {
        }

    }
}
