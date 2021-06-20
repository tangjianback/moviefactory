package com.example.service;

import com.example.dao.Student;
import org.springframework.stereotype.Service;

public interface WebServiceInterface {
    public   boolean addstudent(Student s);
    public    boolean deletestuddent(Student s);
    public    boolean editstudent(Student s);
    public    Student get_student(Student s);
    public  String my_test();
}
