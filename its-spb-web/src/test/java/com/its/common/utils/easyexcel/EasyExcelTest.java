package com.its.common.utils.easyexcel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;

/**
 * 
 * description: alibaba百万级大数据量Excel导出
 * company: tzz
 * @author: 01115486
 * date: 2019/10/30 17:04
 */
public class EasyExcelTest {

    /** 数据量少的(20W以内吧)：一个SHEET一次查询导出*/
    @Test
    public void writeExcelOneSheetOnceWrite() throws IOException {
 
        // 生成EXCEL并指定输出路径
        OutputStream out = new FileOutputStream("E:\\temp\\withoutHead1.xlsx");
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
 
        // 设置SHEET
        Sheet sheet = new Sheet(1, 0);
        sheet.setSheetName("sheet1");
 
        // 设置标题
        Table table = new Table(1);
        List<List<String>> titles = new ArrayList<List<String>>();
        titles.add(Arrays.asList("用户ID"));
        titles.add(Arrays.asList("名称"));
        titles.add(Arrays.asList("年龄"));
        titles.add(Arrays.asList("生日"));
        table.setHead(titles);
        Integer writeCount = 100;
        // 查询数据导出即可 比如说一次性总共查询出100条数据
        List<List<String>> userList = new ArrayList<>();
        for (int i = 0; i < writeCount; i++) {
            userList.add(Arrays.asList("ID_" + i, "Test" + i, String.valueOf(i), new Date().toString()));
        }
 
        writer.write0(userList, sheet, table);
        writer.finish();

    }
    
    /** 数据量适中（100W以内）： 一个SHEET分批查询导出 */
    @Test
    public void writeExcelOneSheetMoreWrite() {
        long beginTm = System.currentTimeMillis();
        List<List<String>> titles = new ArrayList<List<String>>();
        titles.add(Arrays.asList("ID"));
        titles.add(Arrays.asList("名称"));
        titles.add(Arrays.asList("年龄"));
        titles.add(Arrays.asList("生日"));
        // try-with-resource
        try (OutputStream out = new FileOutputStream("D:\\withoutHead2.xlsx")) {
            ExcelWriter writer = new ExcelWriterBuilder().file(out).excelType(ExcelTypeEnum.XLSX).build();
            // 设置SHEET
            WriteSheet sheet = new WriteSheet();
            sheet.setSheetNo(1);
            sheet.setSheetName("sheet1");
            // 设置标题
            WriteTable table = new WriteTable();
            table.setTableNo(1);
            table.setHead(titles);

            // 模拟分批查询：总记录数80W条，每次查询2000条， 分三次查询 最后一次查询记录数是10
            Integer totalRowCount = 800000;
            Integer pageSize = 2000;
            Integer writeCount =
                totalRowCount % pageSize == 0 ? (totalRowCount / pageSize) : (totalRowCount / pageSize + 1);

            for (int i = 0; i < writeCount; i++) {
                List<List<String>> userList = new ArrayList<>();
                for (int j = 0; j < pageSize; j++) {
                    userList.add(Arrays.asList("ID_" + Math.random(), "Test", String.valueOf(Math.random()),
                        new Date().toString()));
                }
                writer.write(userList, sheet, table);
            }
            writer.finish();
            System.out.println(System.currentTimeMillis() - beginTm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**数据量很大（几百万都行）： 多个SHEET分批查询导出 */
    @Test
    public void writeExcelMoreSheetMoreWrite() throws IOException {
        long beginTm = System.currentTimeMillis();

        // 生成EXCEL并指定输出路径
        OutputStream out = new FileOutputStream("E:\\temp\\withoutHead3.xlsx");
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);

        // 设置标题
        Table table = new Table(1);
        List<List<String>> titles = new ArrayList<List<String>>();
        titles.add(Arrays.asList("用户ID"));
        titles.add(Arrays.asList("名称"));
        titles.add(Arrays.asList("年龄"));
        titles.add(Arrays.asList("生日"));
        table.setHead(titles);

        // 模拟分批查询：总记录数50条，每次查询20条， 分三次查询 最后一次查询记录数是10
        Integer totalRowCount = 2700000;
        Integer perSheetRowCount = 900000;
        Integer pageSize = 2000;
        Integer sheetCount = totalRowCount % perSheetRowCount == 0 ? (totalRowCount / perSheetRowCount)
            : (totalRowCount / perSheetRowCount + 1);
        Integer writeCount =
            totalRowCount % pageSize == 0 ? (totalRowCount / pageSize) : (totalRowCount / pageSize + 1);

        for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
            // 设置SHEET
            Sheet sheet = new Sheet(sheetIndex + 1, 0);
            sheet.setSheetName("sheet" + sheetIndex);

            // 注： 此处仅仅为了模拟数据，实用环境不需要将最后一次分开，合成一个即可， 参数为： currentPage = i+1; pageSize = pageSize
            for (int i = 0; i < writeCount; i++) {
                List<List<String>> userList = new ArrayList<>();
                for (int j = 0; j < pageSize; j++) {
                    userList.add(Arrays.asList("ID_" + Math.random(), "Test", String.valueOf(Math.random()),
                        new Date().toString()));
                }
                writer.write0(userList, sheet, table);
            }

        }

        writer.finish();
        System.out.println(System.currentTimeMillis() - beginTm);
    }
  
}
