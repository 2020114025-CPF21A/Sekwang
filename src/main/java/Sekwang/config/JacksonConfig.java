package Sekwang.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public Hibernate5Module hibernate5Module() {
        Hibernate5Module m = new Hibernate5Module();
        // 필요 시 lazy 강제 로딩 (주의: N+1 가능)
        // m.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        return m;
    }
}