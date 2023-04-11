package pog.pgp_alpha_v1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@MapperScan("pog.pgp_alpha_v1.mapper")//扫描Mapper注入到项目中
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })// Security development
public class PgpAlphaV1Application {

    public static void main(String[] args) {
        SpringApplication.run(PgpAlphaV1Application.class, args);
    }

}
