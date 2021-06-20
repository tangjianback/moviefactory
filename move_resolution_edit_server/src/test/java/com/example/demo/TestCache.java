package com.example.demo;

import com.example.dao.Student;
import com.example.service.WebService;
import org.springframework.cache.annotation.Cacheable;

public class TestCache {
    @Cacheable
    public String  test_list()
    {
        return "";
    }
}
