package com.example.makemytrip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class s400 {
    @GetMapping("/s400")
    public String getData() {return  "Have some fire crackers" ; }
}