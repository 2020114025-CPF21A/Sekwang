package Sekwang.config;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public Hibernate6Module hibernate5Module() {
        Hibernate6Module m = new Hibernate6Module();
        // 필요 시 lazy 강제 로딩 (주의: N+1 가능)
        // m.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        return m;
    }
}