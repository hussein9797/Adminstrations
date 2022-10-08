package com.accumed.Admini.service;


import com.accumed.Admini.Authentication.MyUserDetails;
import com.accumed.Admini.Model.User;
import com.accumed.Admini.dto.request.UserRegisterRequest;
import com.accumed.Admini.repository.UserRepository;
import com.accumed.Admini.enums.UserRole;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService,UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;


    public static  Long userId;

    public UserDetailsServiceImpl() {
    }

 
    @Override
    public UserDetails loadUserByUsername(String user_name)
            throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(user_name);

        userId=1L;

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new MyUserDetails(user);
    }


    @Override
    public void registerUser(UserRegisterRequest userRegisterRequest,String siteUrl) throws Exception {
        try {
            User user = new User();
            if (checkIfUserExist(userRegisterRequest.getUser_name()))
                throw new Exception("User with " + userRegisterRequest.getUser_name() + " is already exist");
            user.setUsername(userRegisterRequest.getUser_name());
            user.setEmail(userRegisterRequest.getEmail());
            user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
            user.setEnabled(false);
            String randomCode = RandomString.make(64);
            user.setVerificationCode(randomCode);
            user.setUserRole(UserRole.NORMAL_USER);//defualtRule
            userRepository.save(user);
            this.sendVerificationEmail(user,siteUrl);

        } catch (Exception e) {

            throw e;
        }
    }




    @Override
    public boolean checkIfUserExist(String user_name) {
        return userRepository.findByUsername(user_name) != null;
    }

    @Override
    public void deleteUser(Long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            user.ifPresent(value -> userRepository.delete(value));



        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean verify(String verificatioCOde) {
try {
    User user = userRepository.findByVerificationCode(verificatioCOde);


    if (user == null || user.isEnabled()) {
        return false;
    } else {
        user.setVerificationCode(null);
        user.setEnabled(true);
        userRepository.save(user);

        return true;
    }

}catch (Exception e){
    e.printStackTrace();
    throw e;
}
    }

    @Override
    public List<User> getAllUsers() {
        try {

            return userRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
            throw e ;
        }
    }

    @Override
    public void sendVerificationEmail(User user, String siteURL) throws UnsupportedEncodingException, MessagingException {
        try {

            String toAddress = user.getEmail();
            String fromAddress = "majd.mhd.work@gmail.com";
            String senderName = "majd.mhd.work@gmail.com";
            String subject = "Please verify your registration";
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to verify your registration:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY Code</a>\"[[verifycode]]\"</h3>"
                    + "Thank you,<br>"
                    + "Your company name.";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            content = content.replace("[[name]]", user.getUsername());
            content = content.replace("[[verifycode]]", user.getVerificationCode());
            String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

            content = content.replace("[[URL]]", verifyURL);

            helper.setText(content, true);

            mailSender.send(message);

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addUse(UserRegisterRequest userRegisterRequest) throws Exception {
        try{
            User user = new User();
            if (checkIfUserExist(userRegisterRequest.getUser_name()))
                throw new Exception("User with " + userRegisterRequest.getUser_name() + " is already exist");
            user.setUsername(userRegisterRequest.getUser_name());
            user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()
            ));

            user.setEmail(userRegisterRequest.getEmail());
            user.isEnabled();
            user.setEnabled(true);
            user.setUserRole(UserRole.NORMAL_USER);
            userRepository.save(user);

        }catch (Exception e){

            e.printStackTrace();

            throw e;
        }
    }

    @Override
    public void updateUser(UserRegisterRequest userRegisterRequest) throws Exception {
        try{
            Optional<User> user = userRepository.findById(userRegisterRequest.getUserId());
          if (user.isPresent()){
              if (checkIfUserExist(userRegisterRequest.getUser_name()))
                  throw new Exception("User with " + userRegisterRequest.getUser_name() + " is already exist");
              User user1 = user.get();
              user1.setUsername(userRegisterRequest.getUser_name());
              user1.setEmail(userRegisterRequest.getEmail());
              user1.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()
              ));

              userRepository.save(user1);
          }


        }catch (Exception e){e.printStackTrace();
        throw e;}
    }

}