package Library.Management.System.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {
    @Value("${library.admin.email:admin@library.com}")
    private String adminEmail;
    @Value("${library.admin.password:ChangeMe123!}")
    private String adminPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername(adminEmail).password(encoder.encode(adminPassword)).roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository csrfRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfRepository.setHeaderName("X-XSRF-TOKEN");
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/login.html", "/csrf", "/error", "/favicon.ico", "/css/**",
                                "/js/**", "/images/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login.html").loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true).failureUrl("/login.html?error=true").permitAll())
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login.html?logout=true")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll())
                .csrf(csrf -> csrf.csrfTokenRepository(csrfRepository)
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()));
        return http.build();
    }
}
