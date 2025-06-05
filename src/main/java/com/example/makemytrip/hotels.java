package com.example.makemytrip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hotels {
    @GetMapping("/hotels")
    public String getData() {return  "Please reserve your hotels now from mmt new user interface kindly book ticket for New Delhi to anywhere at 5% discount" ; }
}
