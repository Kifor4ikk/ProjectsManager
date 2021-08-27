package ru.kifor4ik.ProjectsManager.Controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SpringController {

    @GetMapping("/")
    public ResponseEntity test(){

        try{
            return ResponseEntity.ok(" Good :| ");
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Error :| ");
        }
    }

}
