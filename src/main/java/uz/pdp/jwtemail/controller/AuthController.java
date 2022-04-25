package uz.pdp.jwtemail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.jwtemail.payload.ApiResponse;
import uz.pdp.jwtemail.payload.LoginDto;
import uz.pdp.jwtemail.payload.RegisterDto;
import uz.pdp.jwtemail.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register/staff")
    public HttpEntity<?> registerStaff(@RequestBody RegisterDto dto){
        ApiResponse apiResponse = authService.registerStaff(dto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201:409).body(apiResponse);
    }

    @PostMapping("/register/hrManager")
    public HttpEntity<?> registerHrManager(@RequestBody RegisterDto dto){
        ApiResponse apiResponse = authService.registerHrManager(dto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201:409).body(apiResponse);
    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?>verifyEmail(@RequestParam String emailCode,@RequestParam String email){
        ApiResponse apiResponse = authService.verifyEmail(emailCode,email);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PostMapping("/login")
    public HttpEntity<?>LOGIN(@RequestBody LoginDto dto){
        ApiResponse apiResponse = authService.login(dto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }
}
