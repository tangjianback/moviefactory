package com.example.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class Excel {

//    public static void main(String[] args) {
//        try {
//
//            //获取指定列的值
//            readSpecifyColumns(new File("files/1.xlsx"));
//
//            //获取指定行的值
//            readSpecifyRows(new File("files/1.xlsx"));
//
//            //读取行列的值
//            readRowsAndColums(new File("files/1.xlsx"));
//
//            //将获取到的值写入到TXT或者xls中
//            copy_excel(new File("files/1.xlsx"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     *  	读取指定列
     * @param file
     * @throws Exception
     */

    public static void readSpecifyColumns(File file)throws Exception{
        ArrayList<String> columnList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();
        Workbook readwb = null;
        InputStream io = new FileInputStream(file.getAbsoluteFile());
        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setEncoding("GBK"); //关键代码，解决中文乱码
        readwb = Workbook.getWorkbook(io,workbookSettings);
        Sheet readsheet = readwb.getSheet(0);
        int rsColumns = readsheet.getColumns();  //获取表格列数
        int rsRows = readsheet.getRows();  //获取表格行数
        for (int i = 1; i < rsRows; i++) {
            Cell cell_name = readsheet.getCell(0, i);  //第一列的值
            columnList.add(cell_name.getContents());
            Cell cell_value = readsheet.getCell(2, i);  //第三列的值，此处需要手动更改，获取不同列的值
            valueList.add(cell_value.getContents());
        }
        System.out.println(columnList);
        System.out.println(valueList);

        String[] name_String = new String[columnList.size()];
        String[] value_String = new String[columnList.size()];
        for (int i = 0; i < columnList.size(); i++) {
            name_String[i] = columnList.get(i);
            value_String[i] = valueList.get(i);
			System.out.println("<string name=" + "\"" + name_String[i] + "\">" + value_String[i] +  "</string>");
        }
    }

    /**
     *   	读取指定行
     * @param file
     * @throws Exception
     */
    public static void readSpecifyRows(File file)throws Exception{
        ArrayList<String> columnList = new ArrayList<String>();
        Workbook readwb = null;
        InputStream io = new FileInputStream(file.getAbsoluteFile());
        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setEncoding("GBK"); //关键代码，解决中文乱码
        readwb = Workbook.getWorkbook(io,workbookSettings);
        Sheet readsheet = readwb.getSheet(0);
        int rsColumns = readsheet.getColumns();  //获取表格列数
        int rsRows = readsheet.getRows();  //获取表格行数
        for (int i = 1; i < rsColumns; i++) {
            Cell cell_name = readsheet.getCell(i, 1);  //在这里指定行，此处需要手动更改，获取不同行的值
            columnList.add(cell_name.getContents());
        }
        System.out.println(columnList);
    }


    public static List<String> readRowsAndColums(File file) throws BiffException, IOException {
        //1:创建workbook
        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setEncoding("GBK"); //关键代码，解决中文乱码
        Workbook workbook=Workbook.getWorkbook(file,workbookSettings);
        //2:获取第一个工作表sheet
        Sheet sheet=workbook.getSheet(0);
        //3:获取数据
        System.out.println("行："+sheet.getRows());
        System.out.println("列："+sheet.getColumns());
        //4:行数据的保存
        List<String> student_list = new LinkedList<String>();

        for(int i=0;i<sheet.getRows();i++){
            String temp_student = "";
            for(int j=0;j<sheet.getColumns();j++){
                Cell cell=sheet.getCell(j,i);
                //System.out.print(cell.getContents()+" ");
                temp_student += (cell.getContents()+" ");
            }
            temp_student = temp_student.trim();
            student_list.add(temp_student);
        }

        //最后一步：关闭资源
        workbook.close();
        return student_list;
    }

    /**
     * 	将获取到的值写入到TXT或者xls中
     * @param file
     * @throws Exception
     */
    public static void copy_excel(File file) throws Exception {
        FileWriter fWriter = null;
        PrintWriter out = null;
        String fliename = file.getName().replace(".xls", "");
        fWriter = new FileWriter(file.getParent()+ "/agetwo.xls");//输出格式为.xls
        fWriter = new FileWriter(file.getParent() + "/" + fliename + ".txt");//输出格式为.txt
        out = new PrintWriter(fWriter);
        InputStream is = new FileInputStream(file.getAbsoluteFile());
        Workbook wb = null;
        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setEncoding("GBK"); //关键代码，解决中文乱码
        wb = Workbook.getWorkbook(is,workbookSettings);
        int sheet_size = wb.getNumberOfSheets();
        Sheet sheet = wb.getSheet(0);
        for (int j = 1; j < sheet.getRows(); j++) {
            String cellinfo = sheet.getCell(0, j).getContents();//读取的是第二列数据，没有标题，标题起始位置在for循环中定义
            out.println(cellinfo);
        }
        out.close();//关闭流
        fWriter.close();
        out.flush();//刷新缓存
        System.out.println("输出完成！");
    }

}