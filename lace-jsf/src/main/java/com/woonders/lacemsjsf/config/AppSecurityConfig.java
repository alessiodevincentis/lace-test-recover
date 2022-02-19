package com.woonders.lacemsjsf.config;

import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemsjsf.config.security.CustomAuthenticationFailureHandler;
import com.woonders.lacemsjsf.config.security.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by Emanuele on 14/09/2016.
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(operatorService);
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(operatorService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // http://stackoverflow.com/questions/33205236/spring-security-added-prefix-role-to-all-roles-name
        // http://stackoverflow.com/questions/28647136/how-to-disable-x-frame-options-response-header-in-spring-security
        // file download
        http.headers().frameOptions().sameOrigin().and().authorizeRequests()
                .antMatchers("/*", "/app/*", "/resources/**").permitAll()
                .antMatchers("/app/secure/*").fullyAuthenticated()
                .antMatchers("/laceadminmonitor/**").hasIpAddress("93.46.10.50")
                .antMatchers("/app/secure/clienti/delete/**").hasAnyAuthority(Role.RoleName.getClientiDeleteRolesString())
                .antMatchers("/app/secure/clienti/**").hasAnyAuthority(Role.RoleName.getClientiReadRolesString())
                .antMatchers("/app/secure/nominativi/**").hasAnyAuthority(Role.RoleName.getNominativiReadRolesString())
                .antMatchers("/app/secure/marketing/**").hasAnyAuthority(Role.RoleName.getCampagneMarketingWriteRolesString())
                .antMatchers("/app/secure/antiriciclaggio/**").hasAnyAuthority(Role.RoleName.getAntiriciclaggioReadRolesString())
//                .antMatchers("/app/secure/ricarica/**").hasAnyAuthority(Role.RoleName.getRicaricaWriteRolesString())
                .antMatchers("/app/secure/ricarica/**").denyAll()
                .antMatchers("/app/secure/backup/**").hasAuthority(Role.RoleName.BACKUP_DOWNLOAD.getValue())
                .antMatchers("/app/secure/accesslog/**").hasAnyAuthority(Role.RoleName.getAccessLogReadRolesString())
                .antMatchers("/app/secure/simulator/createnewtable.xhtml").hasAnyAuthority(Role.RoleName.getSimulatoriWriteRolesString())
                .antMatchers("/app/secure/simulator/**").hasAnyAuthority(Role.RoleName.getSimulatoriReadRolesString())
                .antMatchers("/app/secure/analytics/analyticsclienti.xhtml").hasAnyAuthority(Role.RoleName.getClientiReadRolesString())
                .antMatchers("/app/secure/analytics/analyticsnominativi.xhtml").hasAnyAuthority(Role.RoleName.getNominativiReadRolesString())
                .and().formLogin().loginPage("/app/login.xhtml")
                .usernameParameter("username").passwordParameter("password")
                .successHandler(customAuthenticationSuccessHandler).failureHandler(customAuthenticationFailureHandler)
                .and().logout().logoutSuccessUrl("/app/login.xhtml").and().exceptionHandling()
                .accessDeniedPage("/access.xhtml").and().sessionManagement().maximumSessions(1)
                .expiredUrl("/app/login.xhtml").and()
                .invalidSessionStrategy((httpServletRequest, httpServletResponse) -> {
                    // http://javaevangelist.blogspot.it/2013/01/jsf-2x-tip-of-day-ajax-redirection-from.html
                    // quanto te vojo bene! pero ammazza che schifo de codice
                    final String redirectURL = "/session-expired.xhtml";
                    removeAllTheCookies(httpServletRequest, httpServletResponse);

                    if (isAJAXRequest(httpServletRequest)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><partial-response><redirect url=\"")
                                .append(redirectURL).append("\"></redirect></partial-response>");
                        httpServletResponse.setHeader("Cache-Control", "no-cache");
                        httpServletResponse.setCharacterEncoding("UTF-8");
                        httpServletResponse.setContentType("text/xml");
                        final PrintWriter pw = httpServletResponse.getWriter();
                        pw.println(sb.toString());
                        pw.flush();
                    } else {
                        httpServletResponse.sendRedirect(redirectURL);
                    }

                });
        // http://stackoverflow.com/questions/7722159/csrf-xss-and-sql-injection-attack-prevention-in-jsf
        // in teoria è già protetta, ma per non sbagliare, attiviamo anche la
        // protezione csrf di spring
        // http://stackoverflow.com/questions/26886121/how-to-enable-csrf-protection-in-jsf-spring-integrated-application
    }

    private void removeAllTheCookies(final HttpServletRequest httpServletRequest,
                                     final HttpServletResponse httpServletResponse) {
        if (httpServletRequest.getCookies() != null) {
            for (final Cookie requestCookie : httpServletRequest.getCookies()) {
                final Cookie cookie = new Cookie(requestCookie.getName(), null);
                if ("JSESSIONID".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    cookie.setSecure(Constants.HTTPS_ENABLED);
                    httpServletResponse.addCookie(cookie);
                    break;
                }
            }
        }
    }

    private boolean isAJAXRequest(final HttpServletRequest request) {
        final String facesRequest = request.getHeader("Faces-Request");
        return (facesRequest != null && facesRequest.equals("partial/ajax"));
    }
}
