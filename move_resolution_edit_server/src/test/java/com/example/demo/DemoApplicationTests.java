package com.example.demo;

import com.example.dao.DataDeal;
import com.example.dao.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.jms.*;
import javax.naming.InitialContext;
import java.io.*;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import static com.example.batch.Excel.*;
import static java.lang.System.exit;

import java.sql.*;
import java.util.Scanner;

@SpringBootTest
class DemoApplicationTests {
    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
//    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//    static final String DB_URL = "jdbc:mysql://localhost:3306/jiangege";

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/jiangege?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";


    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "tangjian";
    static final String PASS = "197325tj";
    @Test
    public  void sql() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM t_user";
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                String id = rs.getString("key_id");
                String name = rs.getString("keywords");
                String url = rs.getString("description");
                String name1 = rs.getString("str_content");
                String url1 = rs.getString("entity_content");


                // 输出数据
                System.out.print("ID: " + id);
                System.out.print(", 站点名称: " + name);
                System.out.print(", 站点 URL: " + url);
                System.out.print("ID: " + id);
                System.out.print(", 站点名称: " + name1);
                System.out.print(", 站点 URL: " + url1);
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }
    @Test
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

    @Test
    /**
     * 连接池连接方式
     */
    public void demo2(){
        // 获取连接池配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(30);
        // 设置最大的空闲连接数
        config.setMaxIdle(10);

        // 获得连接池: JedisPool jedisPool = new JedisPool(poolConfig,host,port);
        JedisPool jedisPool = new JedisPool(config,"localhost",6379);

        // 获得核心对象：jedis
        Jedis jedis = null;
        try{
            // 通过连接池来获得连接
            jedis = jedisPool.getResource();
            // 设置数据
            jedis.set("name","张三");
            // 获取数据
            String value = jedis.get("name");
            System.out.println(value);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            // 释放资源
            if(jedis != null){
                jedis.close();
            }
            // 释放连接池
            if(jedisPool != null){
                jedisPool.close();
            }
        }

    }


    @Test
    public void redis_test()
    {
        // 1. 设置IP地址和端口
        Jedis jedis = new Jedis("localhost",6379);
        // 2. 保存数据
        //jedis.set("name",);
        // 3. 获取数据
        String value = jedis.get("name");
        System.out.println(value);
        // 4.释放资源
        jedis.close();
    }


//    private RedisTemplate redisTemplate;
//
//
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    public void setRedisTemplate(RedisTemplate redisTemplate) {
//        System.out.printf("注入");
//        this.redisTemplate = redisTemplate;
//    }
//    @Autowired
//    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
//        System.out.println("注入");
//        this.stringRedisTemplate = stringRedisTemplate;
//    }





    @Test
    void daotest(){
        System.out.println("wait for process....");
        Scanner input=new Scanner(System.in);
        try {
            Thread.sleep(20000);    //延时20秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("get task and deal with it ");
        System.out.println("transer finished....");
        input.next();
    }
    @Test
    void messageget()
    {
        try{
            //1) Create and start connection
            InitialContext ctx=new InitialContext();
            QueueConnectionFactory f=(QueueConnectionFactory)ctx.lookup("myQueueConnectionFactory");
            QueueConnection con=f.createQueueConnection();
            con.start();
            //2) create Queue session
            QueueSession ses=con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            //3) get the Queue object
            Queue t=(Queue)ctx.lookup("myQueue");
            //4)create QueueReceiver
            QueueReceiver receiver=ses.createReceiver(t);

            //5) create listener object
            MyListener listener=new MyListener();

            //6) register the listener object with receiver
            receiver.setMessageListener(listener);

            System.out.println("wait for process");
            System.out.println("press Ctrl+c to shutdown...");
            while(true){
                Thread.sleep(1000);
            }
        }catch(Exception e){System.out.println(e);}
    }

}
