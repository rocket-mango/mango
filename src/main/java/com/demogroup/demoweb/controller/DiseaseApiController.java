package com.demogroup.demoweb.controller;

import com.demogroup.demoweb.domain.User;
import com.demogroup.demoweb.domain.dto.MangoDTO;
import com.demogroup.demoweb.domain.dto.UserDTO;
import com.demogroup.demoweb.service.DiseaseService;
import com.demogroup.demoweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/disease")
@RequiredArgsConstructor
public class DiseaseApiController {
    private final DiseaseService diseaseService;
    private final UserService userService;

    @PostMapping("/diagnosis")
    public ResponseEntity mangoDiagnosis(@RequestParam("mangoImage")MultipartFile mangoImage
                                                 /*@RequestParam("location") String location*/){
        String location="임시";
        System.out.println("DiseaseApiController.mangoDiagnosis");
        String s3Url = diseaseService.saveToS3(mangoImage);
        List<String> resultList = diseaseService.diagnosis(s3Url);
        User user = userService.findByUsername("임시2");
        boolean is_disease=true;
        String diseaseName="";

        if(resultList.get(0).equals("Healthy")){
            is_disease=false;
        }
        else {
            diseaseName=resultList.get(0);
        }
        //mango에 저장
        MangoDTO dto =new MangoDTO(user,is_disease,diseaseName, s3Url, location);
        diseaseService.saveMango(dto);
        System.out.println(resultList);

        return ResponseEntity.ok().body("{\"result\":"+resultList+"}");
    }
    @GetMapping("/lists/delete/{mid}")
    public ResponseEntity<String> listDelete(@PathVariable("mid") Long mid){
        System.out.println("DiseaseApiController.listDelete");
        try{
            diseaseService.deleteMango(mid);
            return ResponseEntity.ok("성공");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
        }
    }

}
