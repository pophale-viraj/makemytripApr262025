package com.example.makemytrip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class trains {
    @GetMapping("/trains")
    public String getData() {return  "Please book your train from GOI trains' new UI kindly book ticket for New Delhi to anywhere at 5% discount" ; }
}