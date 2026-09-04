package com.oraclejava.springrest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", "/main"})
public class frontMainController {

    @GetMapping
    public String welcomePage(){
        return "index";
    }

}
