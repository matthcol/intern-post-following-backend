package canard.intern.post.following.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    @GetMapping
    public String hello(){
        return "I'm learning Spring boot";
    }
}
