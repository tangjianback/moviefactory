package com.example.batch;

import com.example.dao.DataDeal;
import com.example.dao.Student;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static com.example.batch.Excel.readRowsAndColums;
import static java.lang.System.exit;

public class BatchProcess {
    public  void batch() {
        try {
            String str = "files/1.xls";
            //通过类名.class.getClassLoader()，获取到类加载器
            ClassLoader classLoader = DataDeal.class.getClassLoader();
            //通过classLoader.getResource()获取到输入流后，通过getFile()获取文件
            File file = new File(classLoader.getResource(str).getFile());


            //读取行列的值
            List<String> student_list = readRowsAndColums(file);
//            for(String item: student_list)
//            {
//                System.out.println(item);
//            }
            //先添加已存在的
            List<Student> Total_student = new LinkedList<>();
            Total_student.addAll(read_file());
            //转换
            List<Student> excel_student =  excel_student(student_list);
            Total_student.addAll(excel_student);
            storeStudentDisk(Total_student);



        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    private void  storeStudentDisk(List<Student> stu_list){
//        for(Student item : stu_list)
//        {
//            System.out.println(item);
//        }
        String str = "files/data.txt";
//		//通过类名.class.getClassLoader()，获取到类加载器
        ClassLoader classLoader = DataDeal.class.getClassLoader();
//		//通过classLoader.getResource()获取到输入流后，通过getFile()获取文件
        File file = new File(classLoader.getResource(str).getFile());
//		//判断该文件是否存在
        if(file == null || !file.exists())
        {
            System.out.println("没有检测到数据文件,严重的错误");
            exit(0);
        }
        //持久化
        BufferedWriter bfw = null;
        try {
            bfw = new BufferedWriter(new FileWriter(file));
            for(Student item: stu_list){
                //System.out.println("store "+item.toString());
                bfw.write(student_to_string(item));
                bfw.newLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bfw.flush();
                bfw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private String student_to_string(Student s){
        return s.getSno()+" "+s.getName()+" "+s.getGender()+" "+s.getBorn_data()+" "+s.getBorn_place()+ " "+ s.getAcademy();
    }
    private List<Student> excel_student(List<String> str_list){
        List<Student> temp_student_list = new LinkedList<>();
        for(String item:str_list){
            String[] srl_arr = item.split(" ");
            if (srl_arr.length == 5){


                temp_student_list.add(new Student(srl_arr[0],srl_arr[1],"未知","未知","未知",srl_arr[2]+srl_arr[3]));
            }
        }




        return temp_student_list;
    }
    private List<Student> read_file(){
        List<Student> stu_list = new LinkedList<>();
        String str = "files/data.txt";
//		//通过类名.class.getClassLoader()，获取到类加载器
        ClassLoader classLoader = DataDeal.class.getClassLoader();
//		//通过classLoader.getResource()获取到输入流后，通过getFile()获取文件
        File file = new File(classLoader.getResource(str).getFile());
//		//判断该文件是否存在
        if(file == null || !file.exists())
        {
            System.out.println("没有检测到数据文件,严重的错误");
            exit(0);
        }
        //装载文件到内存中
        BufferedReader bfr = null;
        try {
            bfr = new BufferedReader(new FileReader(file));
            String string_line = null;
            while ((string_line = bfr.readLine()) != null){
                if(string_line.trim() == "")
                    break;
                stu_list.add(string_to_student(string_line));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bfr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stu_list;
    }

    private Student string_to_student(String text_line){
        String[] srl_arr = text_line.split(" ");
        return new Student(srl_arr[0],srl_arr[1],srl_arr[2],srl_arr[3],srl_arr[4],srl_arr[5]);
    }

}
