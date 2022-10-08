package com.accumed.Admini.Security;


import com.accumed.Admini.jwt.JwtTokenVerifier;
import com.accumed.Admini.jwt.JwtUserNameAndPasswordAuthenticationFilter;
import com.accumed.Admini.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders={"x-auth-token", "x-requested-with", "x-xsrf-token"})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

   private  final PasswordEncoder passwordEncoder;
    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .addFilter(new UsernamePasswordAuthenticationFilter(authenticationManager()))
//                .authorizeRequests()
//                .antMatchers("/","index","/css/*","/js/*").permitAll()
//                .and().authorizeRequests().antMatchers("/user_management/**").permitAll()  .and().authorizeRequests().antMatchers("/h2-console/**").permitAll().and().authorizeRequests().antMatchers("/login/**").permitAll()
//                .anyRequest()
//                .authenticated();
//        http.headers().frameOptions().disable();
//
//
//
//    }
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
            .csrf().disable().cors().and()
//       .csrf()
//      .          .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                        .addFilter(new JwtUserNameAndPasswordAuthenticationFilter(authenticationManager()))
            .addFilterAfter(new JwtTokenVerifier(),JwtUserNameAndPasswordAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers("/","index","/css/*","/js/*")
            .permitAll().and().authorizeRequests().antMatchers("/console/**").permitAll()
            .and().authorizeRequests().antMatchers("/user_management/user_register").permitAll() .and().authorizeRequests().antMatchers("/login/**").permitAll().and().authorizeRequests().antMatchers("/user_management/verify/**").permitAll()
//               .antMatchers("/Estates/**").hasAnyRole(VISITOR.name(),ADMIN.name())
//               .antMatchers(HttpMethod.POST,"/admin/Estates/**").hasAuthority(ESTATES_WRITE.getPermission())
//               .antMatchers("/admin/Estates/**").hasRole(ADMIN.name())
            .anyRequest()
            .authenticated()
            .and()

            .httpBasic();
    http.headers().frameOptions().disable();
    //.loginPage("/login").permitAll()
//              .defaultSuccessUrl("/Estates",true)
//              .and()
//              .rememberMe()
//              .tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(21))
//              .key("secured")
//              .and()
//              .logout()
//              .logoutUrl("/logout")
//              .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
//              .clearAuthentication(true)
//      .invalidateHttpSession(true).deleteCookies("JSESSIONID","remember-me").logoutSuccessUrl("/login");





}
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails majdUser = User.builder()
//                .username("Majd")
//                .password( passwordEncoder.encode("Majd123"))
//                //.roles(VISITOR.name())
//                .authorities(VISITOR.getGrantedAuthorities())
//                .build();
////        UserDetails HusseinUser = User.builder()
////                .username("Hussein")
////                .password(passwordEncoder.encode("Hussein1234"))
////               // .roles(ADMIN.name())
////                .authorities(ADMIN.getGrantedAuthorities())
////                .build();
//        return  new InMemoryUserDetailsManager(
//                majdUser
////                HusseinUser
//        );
//    }
}
