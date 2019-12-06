package com.its.common.utils.poi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * POIUtil
 * @author tzz
 */
public class PoiUtil {
    private static final Log log = LogFactory.getLog(PoiUtil.class);

    /** Excel导入 */
    public static Map<String, List<String>> read(String path) {
        Map<String, List<String>> maps = new HashMap<String, List<String>>(16);
        try {
            Sheet sheet = getSheet(path, 0);
            int rowsNum = sheet.getPhysicalNumberOfRows();
            log.info("excel总行数：" + rowsNum);
            List<String> list = null;
            for (Row row : sheet) {
                list = new ArrayList<String>();
                for (Cell cell : row) {
                    list.add(getCellValue(cell));
                }
                maps.put(row.getRowNum() + "", list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }

    /**
     * 获取Sheet
     * 
     * @param path
     * @param sheetIndex
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Sheet getSheet(String path, int sheetIndex) throws FileNotFoundException, IOException {
        Workbook wb = null;
        InputStream inputStream = new FileInputStream(path);
        String suffix = path.substring(path.lastIndexOf(".") + 1, path.length());
        String xls = "xls";
        String xlsx = "xlsx";
        if (xls.equals(suffix)) {
            wb = new HSSFWorkbook(inputStream);
        } else if (xlsx.equals(suffix)) {
            wb = new XSSFWorkbook(inputStream);
        } else {
            log.info("您输入的excel格式不正确");
        }
        Sheet sheet = wb.getSheetAt(sheetIndex);
        return sheet;
    }

    /** 获取单元格数据 */
    public static String getCellValue(Cell cell) {
        String value = "";
        switch (cell.getCellType()) {
        case STRING :
            value = cell.getStringCellValue();
            break;
        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                value = sdf.format(cell.getDateCellValue());
            } else {
                value = String.valueOf(cell.getNumericCellValue());
            }
            break;
        case BOOLEAN:
            value = String.valueOf(cell.getBooleanCellValue());
            break;
        case BLANK:
            value = "";
            break;
        default:
            break;
        }
        return value;
    }

    /** Excel导出 */
    public static Workbook writer(String fileType, Map<String, List<String>> maps) {
        // 创建工作文档对象
        Workbook wb = null;
        try {
            String xls = "xls";
            String xlsx = "xlsx";
            if (xls.equals(fileType)) {
                wb = new HSSFWorkbook();
            } else if (xlsx.equals(fileType)) {
                wb = new XSSFWorkbook();
            } else {
                log.info("您输入的excel格式不正确");
            }
            // 创建sheet对象
            Sheet sheet1 = (Sheet) wb.createSheet("sheet1");
            // 循环写入行数据
            for (Map.Entry<String, List<String>> map : maps.entrySet()) {
                //行号
                int rowNum = Integer.parseInt(map.getKey());
                Row row = (Row) sheet1.createRow(rowNum);
                List<String> list = map.getValue();
                // 循环写入列数据
                for (int i = 0; i < list.size(); i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(list.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }
}
