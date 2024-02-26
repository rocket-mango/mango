package com.demogroup.demoweb.controller;

import com.demogroup.demoweb.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class AppController {
    private final AppService appService;

    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity<String> test(){
        return ResponseEntity.ok().body("{\"이름\":\"유진\"}");
    }

    @GetMapping(value = {"/","/home"})
    public String homePage(Model model){
        try {
            double temperature = appService.getWeather();
            model.addAttribute("temperature", temperature);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "home";
    }
}
