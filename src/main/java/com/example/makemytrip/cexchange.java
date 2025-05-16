package com.example.makemytrip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class cexchange {
    @GetMapping("/cexchange")
    public String getData() {return  "Please have a currency exchanger";}
}
