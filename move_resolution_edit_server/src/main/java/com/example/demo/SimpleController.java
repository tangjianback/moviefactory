package com.example.demo;

import com.example.dao.Student;
import com.example.service.WebService;
import com.example.service.WebServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class SimpleController {

    @Value("${spring.application.name}")
    String appName;

    static private WebServiceInterface webService;
    static {
        webService = new WebService();
    }
    @RequestMapping(value = "test", method = RequestMethod.DELETE)
    public String test(Model model) {

        return "home";
    }

    @GetMapping("/")
    public String indexPage(Model model) {
        //model.addAttribute("appName", appName);
        //Student s = new Student("mf2033004","df","sfsd","sdfs","sfdsd","fdsf");
        //model.addAttribute("student",s);
        return "index";
    }

    // 新建一个学生
    @PostMapping("/User")
    public String addstudent(@RequestParam Map parameters,Model model) {
        String returnmessage ;
        System.out.println(parameters);
        //封装为student对象
        Student s = new Student((String)parameters.get("Sno"),(String)parameters.get("name"),(String)parameters.get("gender"),(String)parameters.get("born_data"),(String)parameters.get("born_place"),(String)parameters.get("academy"));
        //输出打印一下
        System.out.println(s);
        //调用并且添加
        if(webService.addstudent(s)){
            returnmessage = "添加成功";
        }
        else{
            returnmessage = "添加失败";
        }
        model.addAttribute("returnmessage",returnmessage);
        return "index";
    }

    // 删除一个学生
    @DeleteMapping("/User")
    public String deletestudent(@RequestParam Map parameters,Model model) {
        String returnmessage ;
        System.out.println(parameters);
        //封装为student对象
        Student s = new Student((String)parameters.get("Sno"),"","","","","");
        // 打印一下
        System.out.println(s);
        //调用并且添加
        if(webService.deletestuddent(s)){
            returnmessage = "删除成功";
        }
        else{
            returnmessage = "删除失败";
        }
        model.addAttribute("returnmessage",returnmessage);
        return "index";
    }

    // 更新一个用户
    @PutMapping("/User")
    public String editstudent(@RequestParam Map parameters,Model model) {
        String returnmessage ;
        System.out.println(parameters);
        //封装为student对象
        Student s = new Student((String)parameters.get("Sno"),(String)parameters.get("name"),(String)parameters.get("gender"),(String)parameters.get("born_data"),(String)parameters.get("born_place"),(String)parameters.get("academy"));
        //输出打印一下
        System.out.println(s);
        //调用并且添加
        if(webService.editstudent(s)){
            returnmessage = "修改成功";
        }
        else{
            returnmessage = "修改失败";
        }
        model.addAttribute("returnmessage",returnmessage);
        return "index";
    }

    @GetMapping("/User")
    public String getstudent(@RequestParam Map parameters,Model model) {
        String returnmessage;
        System.out.println(parameters);
        //封装为student对象
        Student s = new Student((String)parameters.get("Sno"),"","","","","");
        // 打印一下
        System.out.println(s);
        //调用并且查询
        Student returnstudent = webService.get_student(s);
        if(returnstudent != null)
        {
            model.addAttribute("student",returnstudent);
            returnmessage = "查询成功";
        }
        else{
            returnmessage = "查询失败";
        }
        model.addAttribute("returnmessage",returnmessage);
        return "index";
    }
}