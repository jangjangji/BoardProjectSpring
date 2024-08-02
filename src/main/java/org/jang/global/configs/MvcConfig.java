package org.jang.global.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaAuditing
public class MvcConfig implements WebMvcConfigurer {
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        /**
         * <input type="hidden" name="_method" value="PATCH"
         */
        return new HiddenHttpMethodFilter();
    }

}
