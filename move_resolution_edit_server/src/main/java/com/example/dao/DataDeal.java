package com.example.dao;

import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;

import static java.lang.System.exit;
@Repository
public class DataDeal {
    static private LinkedList<Student> stu_list;
    // 每次创建这个类的时候，我们初始化的读取文件。
    static {
        stu_list = new LinkedList<>();
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
        //显示一下加载到内存的数据
        for(Student item : stu_list)
        {
            System.out.println(item);
        }

    }


    static Student string_to_student(String text_line){
        String[] srl_arr = text_line.split(" ");
        return new Student(srl_arr[0],srl_arr[1],srl_arr[2],srl_arr[3],srl_arr[4],srl_arr[5]);
    }
    static String student_to_string(Student s){
        return s.getSno()+" "+s.getName()+" "+s.getGender()+" "+s.getBorn_data()+" "+s.getBorn_place()+ " "+ s.getAcademy();
    }
    public  DataDeal(){
    }
    //持久化学生的信息到数据文件中
    static private void  storeStudentDisk(){
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
    //增加一个学生的信息
    public static  boolean addstudent(Student s){
        // 判断添加的学号是否已经存在
        String add_stu_no = s.getSno();
        for(Student item: stu_list){
            if (item.getSno().equals(add_stu_no)){
                return false;
            }
        }
        ///如果不存在的话我们添加学生的信息
        stu_list.add(s);
        storeStudentDisk();
        return true;
    }
    //删除一个学生的信息
    public static boolean delete_student(Student s){
        // 判断待删除的学号是否已经存在
        String add_stu_no = s.getSno();
        for(Student item: stu_list){
            if (item.getSno().equals(add_stu_no)){
                stu_list.remove(item);
                storeStudentDisk();
                return true;
            }
        }
        // 如果没有待删除学号存在的话，返回false
        return false;
    }
    //修改学生的信息
    public static boolean edit_student(Student s){
        // 判断待修改的学号是否已经存在
        String add_stu_no = s.getSno();
        for(Student item: stu_list){
            if (item.getSno().equals(add_stu_no)){
                item.setName(s.getName());
                item.setAcademy(s.getAcademy());
                item.setBorn_data(s.getBorn_data());
                item.setBorn_place(s.getBorn_place());
                item.setGender(s.getGender());
                storeStudentDisk();
                return true;
            }
        }
        // 如果没有待删除学号存在的话，返回false
        return false;
    }
    //查询学生的信息
    public static Student get_student(Student s){
        // 判断待查询的学号是否已经存在
        String add_stu_no = s.getSno();
        for(Student item: stu_list){
            if (item.getSno().equals(add_stu_no)){

               return item;
            }
        }
        // 如果没有待删除学号存在的话，返回false
        return null;
    }

}
