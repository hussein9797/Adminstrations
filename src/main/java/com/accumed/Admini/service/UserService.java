package com.accumed.Admini.service;


import com.accumed.Admini.Model.User;
import com.accumed.Admini.dto.request.UserRegisterRequest;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {

    void registerUser(UserRegisterRequest userRegisterRequest,String siteUrl) throws Exception;

     boolean checkIfUserExist(String email);
     void deleteUser(Long userId);
 boolean verify(String  verificatioCOde);
     List<User> getAllUsers();

 void sendVerificationEmail(User user, String siteURL) throws UnsupportedEncodingException, MessagingException;

 void addUse(UserRegisterRequest
                  userRegisterRequest) throws Exception;
     void updateUser(UserRegisterRequest userRegisterRequest) throws Exception;


}
