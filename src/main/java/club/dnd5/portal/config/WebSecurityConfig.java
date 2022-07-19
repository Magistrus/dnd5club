package club.dnd5.portal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import club.dnd5.portal.security.JwtAuthenticationEntryPoint;
import club.dnd5.portal.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
	@Autowired
	private UserDetailsService userDetailsService;
    
	@Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return  new JwtAuthenticationFilter();
    }
    
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		 * http.authorizeRequests().antMatchers("/profile/**").hasRole("USER");
		 * http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");
		 * http.authorizeRequests().antMatchers("/webjars/**").permitAll();
		 * 
		 * http.authorizeRequests().antMatchers("/robots.txt").permitAll();
		 * 
		 * http.authorizeRequests().and().logout().logoutSuccessUrl("/").permitAll();
		 * 
		 * http.authorizeRequests().and().exceptionHandling().accessDeniedPage(
		 * "/error.html");
		 * 
		 * http.cors().and().csrf().disable();
		 */

		http.httpBasic();
        http.cors().and()
        .csrf().disable()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/api/v1/**").permitAll()
        .antMatchers("/api/v1/auth/**").permitAll()
        .antMatchers("/swagger-ui/**").permitAll()
        .antMatchers("/swagger-resources/**").permitAll()
        .antMatchers("/swagger-ui.html").permitAll()
        .antMatchers(HttpMethod.GET, "/**").permitAll()
        .anyRequest()
        .authenticated();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		 registry.addMapping("/**") .allowedOrigins(
			"http://localhost:8080",
			"http://localhost:30000",
			"http://localhost:8081",
			"https://svifty7.stoplight.io/",
			"https://dev.dnd5.club/",
			"https://dnd5.club/"
			) .allowedMethods("*");
	}

	@Override
	public final void configure(final WebSecurity web) throws Exception {
		super.configure(web);
//		web.ignoring().antMatchers("/resources/**");
		web.httpFirewall(new AnnotatingHttpFirewall());
		return;
	}
}