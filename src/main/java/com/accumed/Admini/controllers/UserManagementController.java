package com.accumed.Admini.controllers;


import com.accumed.Admini.Model.User;
import com.accumed.Admini.dto.request.UserRegisterRequest;
import com.accumed.Admini.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user_management")
public class UserManagementController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;




    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/addUser")
    ResponseEntity<Object> addUser(@RequestBody UserRegisterRequest userRegisterRequest) throws Exception {
        try {
            userDetailsService.addUse(userRegisterRequest);
            return new ResponseEntity<>("{ \"message\": \"user added  successfully \" }", HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);


        }
    }
    @PreAuthorize("hasAuthority('DELETE:delete')")
    @DeleteMapping("/deleteUser/{userId}")
    ResponseEntity<Object> deleteUser(@PathVariable("userId") Long userId) throws Exception {
        try {
            userDetailsService.deleteUser(userId);
            return new ResponseEntity<>("{ \"message\": \"user delete  successfully \" }", HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);


        }
    }
 @PreAuthorize("hasAuthority('update_user')")
    @PutMapping("/updateUser")
    ResponseEntity<Object> updateUser(@RequestBody UserRegisterRequest userRegisterRequest) throws Exception {
        try {
            userDetailsService.updateUser(userRegisterRequest);
            return new ResponseEntity<>("{ \"message\": \"user updated  successfully \" }", HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);


        }
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/getAllUsers")
    ResponseEntity<Object> getAllUsers() throws Exception {
        try {
            List<User> allUsers = userDetailsService.getAllUsers();

            return new ResponseEntity<>(allUsers,HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FAILED_DEPENDENCY);


        }
    }












    @PostMapping("/user_register")
    ResponseEntity<Object> userRegister(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest  request) throws Exception {
        try {
            userDetailsService.registerUser(userRegisterRequest,getSiteURL(request));
            return new ResponseEntity<>("{ \"message\": \"we sent you verification code to your email pleas verify to log in successfully \" }", HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);


        }
    }
    @PostMapping("/verify")
    ResponseEntity<Object> verify(@RequestParam("code") String code) throws Exception {
        try {
            userDetailsService.verify(code);
            return new ResponseEntity<>("{ \"message\": \"verified successfully \" }", HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);


        }
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }


}