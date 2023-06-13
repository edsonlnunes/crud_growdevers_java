package br.com.growdev.growdevers.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/helloworld")
public class HelloWorldController {

    @GetMapping
    public String helloWorld(){
        return "E aíi ";
    }
}
