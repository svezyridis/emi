package lawfirm.project;

import lawfirm.project.storage.StorageProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import javax.servlet.Filter;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class WebConfig {

    @Bean
    public Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
}