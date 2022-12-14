package com.accumed.Admini.repository;



import com.accumed.Admini.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Long> {
      @Query("SELECT u FROM User u WHERE u.username = :username")
      public User getUserByUsername(@Param("username") String username);

      User findByUsername(String user_name);

      @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
      public User findByVerificationCode(String code);

      @Query("SELECT u FROM User u WHERE u.id = :id")
      User find(@Param("id") Long id);

}
