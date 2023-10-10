package pog.pgp_alpha_v1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080",
                        "http://192.168.0.106:8080",
                        "http://10.157.73.88:8080",
                        "http://192.168.1.104:8080",
                        "http://192.168.3.116:8080",
                        "http://192.168.3.210:8080",
                        "http://10.157.46.249:8080",
                        "http://10.157.73.42:8080")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

}
