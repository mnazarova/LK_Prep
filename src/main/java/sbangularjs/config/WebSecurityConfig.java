package sbangularjs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sbangularjs.repository.UserRepository;
import sbangularjs.service.UserService;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
//@EnableOAuth2Sso
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

//    @Autowired
//    private AuthProvider authProvider;
    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
//            .httpBasic().disable()
//            .formLogin().disable()
//            .antMatcher("/**")
            .authorizeRequests()
            .antMatchers(/*"/", */"/home", "/login**").permitAll()
            .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login")//.disable()
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .permitAll()
//            .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .logout()
                .logoutSuccessUrl("/home"); // можно изменить на login, тогда не будет переходить на начальную страницу, сразу на sso

            http.addFilterBefore(ssoFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public AuthorizationCodeResourceDetails google() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("security.oauth2.resource")
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }

    private Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oAuth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        CustomUserInfoTokenServices tokenServices = new CustomUserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
        tokenServices.setRestTemplate(googleTemplate);
        googleFilter.setTokenServices(tokenServices);
        tokenServices.setUserRepo(userRepository);
//        tokenServices.setPasswordEncoder(passwordEncoder);
        return googleFilter;
    }

    @Bean
    public FilterRegistrationBean oAuth2ClientFilterRegistration(OAuth2ClientContextFilter oAuth2ClientContextFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(oAuth2ClientContextFilter);
        registration.setOrder(-100);
        return registration;
    }

    /*@Bean
    public PrincipalExtractor principalExtractor(UserRepository userRepository) {
        return map -> {
            String username = (String) map.get("email");
            User user = userRepository.findByUsername(username);
            if (user == null) {
                user = new User();
                user.setUsername(username);
                user.setPassword(username);
                user.setActive(true);
                user.setRoles(Collections.singleton(Role.TEACHER));
            }

            user.setLastVisit(new Date());
            return userRepository.save(user);
        };
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authProvider);
        auth.userDetailsService(userService)
            .passwordEncoder(NoOpPasswordEncoder.getInstance());

//        auth.jdbcAuthentication()
//            .dataSource(dataSource)
//            .passwordEncoder(NoOpPasswordEncoder.getInstance())
//            .usersByUsernameQuery("select username, password, active from usr where username=?")
//            .authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.username=?");
    }
}