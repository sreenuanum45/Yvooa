package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExcelUtility provides methods to read from and write to Excel files.
 * It supports both .xlsx and .xls formats and handles various cell types.
 */
public class ExcelUtility {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtility.class);

    /**
     * Reads a string value from a specific cell in an Excel file.
     *
     * @param filePath  The path to the Excel file.
     * @param sheetName The name of the sheet.
     * @param rowNum    The zero-based row number.
     * @param colNum    The zero-based column number.
     * @return The string value of the cell, or null if not found or not a string.
     */
    public String getStringData(String filePath, String sheetName, int rowNum, int colNum) {
        Cell cell = getCell(filePath, sheetName, rowNum, colNum);
        if (cell != null) {
            try {
                return getCellValueAsString(cell);
            } catch (Exception e) {
                logger.error("Error reading string data from cell [{}, {}] in sheet '{}': {}", rowNum, colNum, sheetName, e.getMessage());
            }
        }
        return null;
    }

    /**
     * Reads a numeric value from a specific cell in an Excel file.
     *
     * @param filePath  The path to the Excel file.
     * @param sheetName The name of the sheet.
     * @param rowNum    The zero-based row number.
     * @param colNum    The zero-based column number.
     * @return The numeric value of the cell, or 0.0 if not found or not numeric.
     */
    public double getNumericData(String filePath, String sheetName, int rowNum, int colNum) {
        Cell cell = getCell(filePath, sheetName, rowNum, colNum);
        if (cell != null) {
            try {
                return getCellValueAsNumeric(cell);
            } catch (Exception e) {
                logger.error("Error reading numeric data from cell [{}, {}] in sheet '{}': {}", rowNum, colNum, sheetName, e.getMessage());
            }
        }
        return 0.0;
    }

    /**
     * Reads a boolean value from a specific cell in an Excel file.
     *
     * @param filePath  The path to the Excel file.
     * @param sheetName The name of the sheet.
     * @param rowNum    The zero-based row number.
     * @param colNum    The zero-based column number.
     * @return The boolean value of the cell, or false if not found or not boolean.
     */
    public boolean getBooleanData(String filePath, String sheetName, int rowNum, int colNum) {
        Cell cell = getCell(filePath, sheetName, rowNum, colNum);
        if (cell != null) {
            try {
                return getCellValueAsBoolean(cell);
            } catch (Exception e) {
                logger.error("Error reading boolean data from cell [{}, {}] in sheet '{}': {}", rowNum, colNum, sheetName, e.getMessage());
            }
        }
        return false;
    }

    /**
     * Reads a date value from a specific cell in an Excel file.
     *
     * @param filePath  The path to the Excel file.
     * @param sheetName The name of the sheet.
     * @param rowNum    The zero-based row number.
     * @param colNum    The zero-based column number.
     * @return The date value of the cell, or null if not found or not a date.
     */
    public Date getDateData(String filePath, String sheetName, int rowNum, int colNum) {
        Cell cell = getCell(filePath, sheetName, rowNum, colNum);
        if (cell != null) {
            try {
                return getCellValueAsDate(cell);
            } catch (Exception e) {
                logger.error("Error reading date data from cell [{}, {}] in sheet '{}': {}", rowNum, colNum, sheetName, e.getMessage());
            }
        }
        return null;
    }

    /**
     * Writes a value to a specific cell in an Excel file.
     * Supports String, double, boolean, and Date types.
     *
     * @param filePath  The path to the Excel file.
     * @param sheetName The name of the sheet.
     * @param rowNum    The zero-based row number.
     * @param colNum    The zero-based column number.
     * @param value     The value to write (String, Double, Boolean, Date).
     */
    public void setData(String filePath, String sheetName, int rowNum, int colNum, Object value) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = getWorkbook(fis, filePath)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
                logger.info("Sheet '{}' created as it did not exist.", sheetName);
            }

            Row row = sheet.getRow(rowNum);
            if (row == null) {
                row = sheet.createRow(rowNum);
                logger.info("Row '{}' created as it did not exist.", rowNum);
            }

            Cell cell = row.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            setCellValue(cell, value);
            logger.info("Set cell [{}, {}] in sheet '{}' with value '{}'.", rowNum, colNum, sheetName, value);

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                logger.info("Workbook '{}' written successfully.", filePath);
            }

        } catch (IOException | InvalidFormatException e) {
            logger.error("Error setting data in Excel file '{}': {}", filePath, e.getMessage());
        }
    }

    /**
     * Reads all data from a specific sheet and returns it as a List of Maps.
     * Assumes the first row contains headers.
     *
     * @param filePath  The path to the Excel file.
     * @param sheetName The name of the sheet.
     * @return A List of Maps where each Map represents a row with column header as key.
     */
    public List<Map<String, Object>> getAllData(String filePath, String sheetName) {
        List<Map<String, Object>> sheetData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = getWorkbook(fis, filePath)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet '{}' does not exist in '{}'.", sheetName, filePath);
                return sheetData;
            }

            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) {
                logger.warn("Sheet '{}' is empty in '{}'.", sheetName, filePath);
                return sheetData;
            }

            // Read header row
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }

            // Read data rows
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, Object> rowData = new LinkedHashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.put(headers.get(i), getCellValue(cell));
                }
                sheetData.add(rowData);
            }

            logger.info("Read {} rows from sheet '{}' in '{}'.", sheetData.size(), sheetName, filePath);
        } catch (IOException | InvalidFormatException e) {
            logger.error("Error reading all data from Excel file '{}': {}", filePath, e.getMessage());
        }

        return sheetData;
    }

    /**
     * Retrieves the total number of rows in a specific sheet.
     *
     * @param filePath  The path to the Excel file.
     * @param sheetName The name of the sheet.
     * @return The total number of rows, or 0 if sheet does not exist.
     */
    public int getRowCount(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = getWorkbook(fis, filePath)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                int rowCount = sheet.getPhysicalNumberOfRows();
                logger.info("Sheet '{}' in '{}' has {} rows.", sheetName, filePath, rowCount);
                return rowCount;
            } else {
                logger.warn("Sheet '{}' does not exist in '{}'.", sheetName, filePath);
                return 0;
            }

        } catch (IOException | InvalidFormatException e) {
            logger.error("Error getting row count from Excel file '{}': {}", filePath, e.getMessage());
            return 0;
        }
    }

    /**
     * Retrieves the total number of columns in a specific sheet.
     * Assumes the first row contains headers.
     *
     * @param filePath  The path to the Excel file.
     * @param sheetName The name of the sheet.
     * @return The total number of columns, or 0 if sheet does not exist.
     */
    public int getColumnCount(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = getWorkbook(fis, filePath)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet != null && sheet.getPhysicalNumberOfRows() > 0) {
                Row headerRow = sheet.getRow(0);
                int colCount = headerRow.getPhysicalNumberOfCells();
                logger.info("Sheet '{}' in '{}' has {} columns.", sheetName, filePath, colCount);
                return colCount;
            } else {
                logger.warn("Sheet '{}' does not exist or is empty in '{}'.", sheetName, filePath);
                return 0;
            }

        } catch (IOException | InvalidFormatException e) {
            logger.error("Error getting column count from Excel file '{}': {}", filePath, e.getMessage());
            return 0;
        }
    }

    /**
     * Retrieves all data from a specific row as a Map.
     * Assumes the first row contains headers.
     *
     * @param filePath  The path to the Excel file.
     * @param sheetName The name of the sheet.
     * @param rowNum    The zero-based row number.
     * @return A Map where the key is the column header and the value is the cell data.
     */
    public Map<String, Object> getRowData(String filePath, String sheetName, int rowNum) {
        Map<String, Object> rowData = new LinkedHashMap<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = getWorkbook(fis, filePath)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet '{}' does not exist in '{}'.", sheetName, filePath);
                return rowData;
            }

            if (sheet.getPhysicalNumberOfRows() <= rowNum) {
                logger.warn("Row '{}' does not exist in sheet '{}' of '{}'.", rowNum, sheetName, filePath);
                return rowData;
            }

            Row headerRow = sheet.getRow(0);
            Row targetRow = sheet.getRow(rowNum);

            if (headerRow == null || targetRow == null) {
                logger.warn("Header row or target row is null in sheet '{}' of '{}'.", sheetName, filePath);
                return rowData;
            }

            int colCount = headerRow.getPhysicalNumberOfCells();
            for (int i = 0; i < colCount; i++) {
                Cell headerCell = headerRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell dataCell = targetRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String header = getCellValueAsString(headerCell);
                Object data = getCellValue(dataCell);
                rowData.put(header, data);
            }

            logger.info("Retrieved data for row '{}' in sheet '{}' of '{}'.", rowNum, sheetName, filePath);
        } catch (IOException | InvalidFormatException e) {
            logger.error("Error getting row data from Excel file '{}': {}", filePath, e.getMessage());
        }

        return rowData;
    }

    /**
     * Retrieves a specific cell and returns its value as an Object.
     *
     * @param filePath  The path to the Excel file.
     * @param sheetName The name of the sheet.
     * @param rowNum    The zero-based row number.
     * @param colNum    The zero-based column number.
     * @return The cell value as an Object, or null if not found.
     */
    public Object readCell(String filePath, String sheetName, int rowNum, int colNum) {
        Cell cell = getCell(filePath, sheetName, rowNum, colNum);
        if (cell != null) {
            return getCellValue(cell);
        }
        return null;
    }

    /**
     * Reads all data from a specific sheet and returns it as a List of Lists.
     *
     * @param filePath  The path to the Excel file.
     * @param sheetName The name of the sheet.
     * @return A List of Lists where each inner List represents a row with cell data.
     */
    public List<List<Object>> getAllDataAsList(String filePath, String sheetName) {
        List<List<Object>> sheetData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = getWorkbook(fis, filePath)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet '{}' does not exist in '{}'.", sheetName, filePath);
                return sheetData;
            }

            for (Row row : sheet) {
                List<Object> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(getCellValue(cell));
                }
                sheetData.add(rowData);
            }

            logger.info("Read {} rows from sheet '{}' in '{}'.", sheetData.size(), sheetName, filePath);
        } catch (IOException | InvalidFormatException e) {
            logger.error("Error reading all data as list from Excel file '{}': {}", filePath, e.getMessage());
        }

        return sheetData;
    }

    /**
     * Retrieves a specific sheet from the workbook.
     *
     * @param workbook  The Workbook instance.
     * @param sheetName The name of the sheet.
     * @return The Sheet object, or null if not found.
     */
    private Sheet getSheet(Workbook workbook, String sheetName) {
        return workbook.getSheet(sheetName);
    }

    /**
     * Retrieves a specific cell from the Excel file.
     *
     * @param filePath  The path to the Excel file.
     * @param sheetName The name of the sheet.
     * @param rowNum    The zero-based row number.
     * @param colNum    The zero-based column number.
     * @return The Cell object, or null if not found.
     */
    private Cell getCell(String filePath, String sheetName, int rowNum, int colNum) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = getWorkbook(fis, filePath)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet '{}' does not exist in '{}'.", sheetName, filePath);
                return null;
            }

            Row row = sheet.getRow(rowNum);
            if (row == null) {
                logger.warn("Row '{}' does not exist in sheet '{}' of '{}'.", rowNum, sheetName, filePath);
                return null;
            }

            Cell cell = row.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            return cell;
        } catch (IOException | InvalidFormatException e) {
            logger.error("Error retrieving cell [{}, {}] from sheet '{}' in '{}': {}", rowNum, colNum, sheetName, filePath, e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves the cell value based on its type.
     *
     * @param cell The Cell object.
     * @return The cell value as an Object.
     */
    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return getCellValueAsString(cell);

            case BOOLEAN:
                return getCellValueAsBoolean(cell);

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return getCellValueAsDate(cell);
                } else {
                    return getCellValueAsNumeric(cell);
                }

            case FORMULA:
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case STRING:
                        return cellValue.getStringValue();
                    case BOOLEAN:
                        return cellValue.getBooleanValue();
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            return cell.getDateCellValue();
                        }
                        return cellValue.getNumberValue();
                    default:
                        return "";
                }

            case BLANK:
            case _NONE:
            case ERROR:
            default:
                return "";
        }
    }

    /**
     * Retrieves the cell value as a String, handling different cell types.
     *
     * @param cell The Cell object.
     * @return The cell value as a String.
     */
    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    cell.setCellType(CellType.STRING);
                    return cell.getStringCellValue();
                }
            case FORMULA:
                // Evaluate the formula and return as String
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case STRING:
                        return cellValue.getStringValue();
                    case BOOLEAN:
                        return Boolean.toString(cellValue.getBooleanValue());
                    case NUMERIC:
                        return Double.toString(cellValue.getNumberValue());
                    default:
                        return "";
                }
            case BLANK:
            case _NONE:
            case ERROR:
            default:
                return "";
        }
    }

    /**
     * Retrieves the cell value as a double, handling different cell types.
     *
     * @param cell The Cell object.
     * @return The cell value as a double.
     */
    private double getCellValueAsNumeric(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    logger.error("Cannot parse numeric value from string '{}': {}", cell.getStringCellValue(), e.getMessage());
                    return 0.0;
                }
            case BOOLEAN:
                return cell.getBooleanCellValue() ? 1.0 : 0.0;
            case FORMULA:
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case NUMERIC:
                        return cellValue.getNumberValue();
                    case STRING:
                        try {
                            return Double.parseDouble(cellValue.getStringValue());
                        } catch (NumberFormatException e) {
                            logger.error("Cannot parse numeric value from formula string '{}': {}", cellValue.getStringValue(), e.getMessage());
                            return 0.0;
                        }
                    case BOOLEAN:
                        return cellValue.getBooleanValue() ? 1.0 : 0.0;
                    default:
                        return 0.0;
                }
            case BLANK:
            case _NONE:
            case ERROR:
            default:
                return 0.0;
        }
    }

    /**
     * Retrieves the cell value as a boolean, handling different cell types.
     *
     * @param cell The Cell object.
     * @return The cell value as a boolean.
     */
    private boolean getCellValueAsBoolean(Cell cell) {
        switch (cell.getCellType()) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                String val = cell.getStringCellValue().toLowerCase();
                return val.equals("true") || val.equals("yes") || val.equals("1");
            case NUMERIC:
                return cell.getNumericCellValue() != 0;
            case FORMULA:
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case BOOLEAN:
                        return cellValue.getBooleanValue();
                    case STRING:
                        String formulaVal = cellValue.getStringValue().toLowerCase();
                        return formulaVal.equals("true") || formulaVal.equals("yes") || formulaVal.equals("1");
                    case NUMERIC:
                        return cellValue.getNumberValue() != 0;
                    default:
                        return false;
                }
            case BLANK:
            case _NONE:
            case ERROR:
            default:
                return false;
        }
    }

    /**
     * Retrieves the cell value as a Date, handling different cell types.
     *
     * @param cell The Cell object.
     * @return The cell value as a Date, or null if not a date.
     */
    private Date getCellValueAsDate(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        }
        logger.warn("Cell [{}] is not formatted as a date.", cell.getAddress());
        return null;
    }

    /**
     * Sets the value of a cell based on the type of the provided value.
     *
     * @param cell  The Cell object to set the value for.
     * @param value The value to set (String, Double, Boolean, Date).
     */
    private void setCellValue(Cell cell, Object value) {
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
            CreationHelper creationHelper = cell.getSheet().getWorkbook().getCreationHelper();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("m/d/yy h:mm"));
            cell.setCellStyle(cellStyle);
            cell.setCellValue((Date) value);
        } else {
            logger.warn("Unsupported data type '{}'. Setting cell to blank.", value.getClass().getSimpleName());
            cell.setBlank();
        }
    }

    /**
     * Retrieves the appropriate Workbook instance based on the file extension.
     *
     * @param fis      The FileInputStream of the Excel file.
     * @param filePath The path to the Excel file.
     * @return The Workbook instance.
     * @throws IOException            If an I/O error occurs.
     * @throws InvalidFormatException If the file format is invalid.
     */
    private Workbook getWorkbook(FileInputStream fis, String filePath) throws IOException, InvalidFormatException {
        if (filePath.toLowerCase().endsWith("xlsx")) {
            return new XSSFWorkbook(fis);
        } else if (filePath.toLowerCase().endsWith("xls")) {
            return new HSSFWorkbook(fis);
        } else {
            logger.error("Unsupported file format for file '{}'. Only .xlsx and .xls are supported.", filePath);
            throw new InvalidFormatException("Unsupported file format: " + filePath);
        }
    }

    /**
     * Closes the Workbook, FileInputStream, and FileOutputStream if they are not null.
     *
     * @param workbook The Workbook instance to close.
     * @param fis      The FileInputStream to close.
     * @param fos      The FileOutputStream to close.
     */
    private void closeWorkbook(Workbook workbook, FileInputStream fis, FileOutputStream fos) {
        try {
            if (workbook != null) {
                workbook.close();
                logger.debug("Workbook closed successfully.");
            }
            if (fis != null) {
                fis.close();
                logger.debug("FileInputStream closed successfully.");
            }
            if (fos != null) {
                fos.close();
                logger.debug("FileOutputStream closed successfully.");
            }
        } catch (IOException e) {
            logger.error("Error closing resources: {}", e.getMessage());
        }
    }
    public static List<Map<String, String>> getData(String filePath, String sheetName) throws IOException {
        List<Map<String, String>> dataList = new ArrayList<>();
        FileInputStream fis = null;
        Workbook workbook = null;
        try {
            fis = new FileInputStream(new File(filePath));
            File file = new File(filePath);
            if (!file.exists()) {
                throw new IllegalArgumentException("File not found: " + filePath);
            }
            if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                throw new IllegalArgumentException("Unsupported file format: " + filePath);
            }

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet not found: " + sheetName);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return dataList; // Empty sheet
            }

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row dataRow = sheet.getRow(i);
                if (dataRow != null && !isRowEmpty(dataRow)) {
                    Map<String, String> rowData = new HashMap<>();
                    for (int j = 0; j < headers.size(); j++) {
                        Cell cell = dataRow.getCell(j);
                        String cellValue = "";
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case STRING:
                                    cellValue = cell.getStringCellValue();
                                    break;
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        cellValue = sdf.format(cell.getDateCellValue());
                                    } else {
                                        cellValue = String.valueOf(cell.getNumericCellValue()); // Keep decimals
                                    }
                                    break;
                                case BOOLEAN:
                                    cellValue = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                case BLANK:
                                    cellValue = "";
                                    break;
                                default:
                                    cellValue = "";
                            }
                        }
                        rowData.put(headers.get(j), cellValue);
                    }
                    dataList.add(rowData);
                }
            }
        } finally {
            if (workbook != null) {
                workbook.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return dataList;
    }
    // Helper method
    private static boolean isRowEmpty(Row row) {
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
    public static List<Object[]> getExcelData(String excelPath, String sheetName) {
        List<Object[]> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            // Iterate through all rows
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Object[] rowData = new Object[13]; // 13 columns
                for (int i = 0; i < 13; i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData[i] = cell.toString();
                }
                data.add(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
