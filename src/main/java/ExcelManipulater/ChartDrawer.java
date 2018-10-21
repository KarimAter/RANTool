package ExcelManipulater;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ChartDrawer {

    public static void creatChart(XSSFWorkbook wb) {

//
//            XSSFSheet sheet = wb.createSheet("Line");
//            final int NUM_OF_ROWS = 3;
//            final int NUM_OF_COLUMNS = 10;
//
//            // Create a row and put some cells in it. Rows are 0 based.
//            XSSFRow row;
//            XSSFCell cell;
//            for (int rowIndex = 0; rowIndex < NUM_OF_ROWS; rowIndex++) {
//                row = sheet.createRow((short) rowIndex);
//                for (int colIndex = 0; colIndex < NUM_OF_COLUMNS; colIndex++) {
//                    cell = row.createCell((short) colIndex);
//                    cell.setCellValue(colIndex * (rowIndex + 1));
//                }
//            }
//
//        XSSFDrawing drawing = sheet.createDrawingPatriarch();
//        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 5, 10, 15);
//
//        XSSFChart chart = drawing.createChart(anchor);
//        XDDFChartLegend legend = chart.getOrAddLegend();
//        legend.setPosition(LegendPosition.TOP_RIGHT);
//
//        // Use a category axis for the bottom axis.
//        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
//        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
//        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
//
//        XDDFDataSource<Double> xs = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
//                new CellRangeAddress(0, 0, 0, NUM_OF_COLUMNS - 1));
//        XDDFNumericalDataSource<Double> ys1 = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
//                new CellRangeAddress(1, 1, 0, NUM_OF_COLUMNS - 1));
//        XDDFNumericalDataSource<Double> ys2 = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
//                new CellRangeAddress(2, 2, 0, NUM_OF_COLUMNS - 1));
//
//        XDDFChartData data = chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
//        data.addSeries(xs, ys1);
//        data.addSeries(xs, ys2);
//        chart.plot(data);
    }
}
