package com.example.service;

import com.example.dao.DataDeal;
import com.example.dao.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.sql.SQLOutput;
import java.util.List;

@Service
public class WebService implements WebServiceInterface{
    static private DataDeal mydatadeal ;
    static private JedisPool jedisPool;
    static private Jedis jedis;
    static {
        mydatadeal = new DataDeal();
        //jedis = new Jedis("localhost",6379);
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(30);
        // 设置最大的空闲连接数
        config.setMaxIdle(10);

        // 获得连接池: JedisPool jedisPool = new JedisPool(poolConfig,host,port);
        jedisPool = new JedisPool(config,"localhost",6379);

    }

//    private RedisTemplate redisTemplate;
//    private StringRedisTemplate stringRedisTemplate;

//    @Autowired
//    public void setRedisTemplate(RedisTemplate redisTemplate) {
//        System.out.println("注入");
//        this.redisTemplate = redisTemplate;
//    }
//    @Autowired
//    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
//        System.out.println("注入");
//        this.stringRedisTemplate = stringRedisTemplate;
//    }
//    @Autowired
//    public void setMydatadeal(DataDeal mydatadeal) {
//        this.mydatadeal = mydatadeal;
//    }
// 增加一个学生

    public   boolean addstudent(Student s){
        return mydatadeal.addstudent(s);
    }
    // 删除一个学生
    public    boolean deletestuddent(Student s){
        return mydatadeal.delete_student(s);
    }
    // 修改一个学生的信息
    public    boolean editstudent(Student s){
        return mydatadeal.edit_student(s);
    }
    // 查询一个学生的信息
    public Student get_student(Student s){return mydatadeal.get_student(s);}
    // 查询一个学生的信息(redis版本)
    @Cacheable(value = "employees")
    public Student get_student_s(Student s){
        //return mydatadeal.get_student(s);

        // 获得核心对象：jedis
        Jedis jedis = null;
        Student retuen_s = null;
        try{
            // 通过连接池来获得连接
            jedis = jedisPool.getResource();


            if (jedis.get("tangjian")==null || jedis.get("tangjian").equals("")){
                retuen_s = mydatadeal.get_student(s);

                jedis.set("tangjian",retuen_s.getSno() +" "+retuen_s.getName()+" "+retuen_s.getGender() + " "+retuen_s.getBorn_data()+" "+retuen_s.getBorn_place()+" "+retuen_s.getAcademy());
                System.out.println("no huancun");
            }
            else{
                String temp = jedis.get("tangjian");
                String[] string_list = temp.split(" ");
                retuen_s = new Student(string_list[0],string_list[1],string_list[2],string_list[3],string_list[4],string_list[5]);
                System.out.println("huancun");
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            // 释放资源
            if(jedis != null){
                jedis.close();
            }
            // 释放连接池

        }
        return  retuen_s;
    }
    @Override
    @Cacheable(value = "employees1")
    public  String my_test()
    {

        return "wocao";
    }

}
