package ma.ensa.apms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AgileProjectManagementSystemApplication {
    /* 
     * 
     */
    public static void main(String[] args) {
        SpringApplication.run(AgileProjectManagementSystemApplication.class, args);
    }

}