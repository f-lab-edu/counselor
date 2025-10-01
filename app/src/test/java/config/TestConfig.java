package config;

import com.hyejin.counselor.core.repository.CounselRepository;
import com.hyejin.counselor.core.service.CounselService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public CounselService counselService(){
        return Mockito.mock(CounselService.class);
    }

}
