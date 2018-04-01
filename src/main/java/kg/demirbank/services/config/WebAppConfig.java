package kg.demirbank.services.config;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * @author almasn Web MVC configuration
 */
@Configuration
@EnableWebMvc
@ComponentScan("kg.demirbank.services.*")
public class WebAppConfig extends WebMvcConfigurerAdapter {
	
	@Configuration
    @Profile("localhost")
    @PropertySource(value ={"classpath:config.properties","classpath:config-localhost.properties"}, ignoreResourceNotFound = true)
    static class Localhost
    { }

    @Configuration
    @Profile("test")
    @PropertySource(value ={"classpath:config.properties","classpath:config-test.properties"}, ignoreResourceNotFound = true)
    static class Test
    { }
    
    @Configuration
    @Profile("prod")
    @PropertySource(value ={"classpath:config.properties","classpath:config-prod.properties"}, ignoreResourceNotFound = true)
    static class Prod
    { }

	@Resource
	private Environment env;

	private static final String PROPERTY_NAME_REQUEST_ASYNC_TIMEOUT = "req.async.timeout";

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer
				.setDefaultTimeout(Integer.parseInt(env
						.getRequiredProperty(PROPERTY_NAME_REQUEST_ASYNC_TIMEOUT)) * 1000L);// timeout
																							// for
																							// async
																							// requests
																							// in
																							// ms
	}

	@Bean
	public ViewResolver pageViewResolver() {
		UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public PathMatcher pathMatcher() {
		return new CaseInsensitivePathMatcher();
	}

	// http msg converters
	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
		converters.add(stringHttpMessageConverter());
		converters.add(formHttpMessageConverter());
		converters.add(jsonHttpMessageConverter());
	}

	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter();
		converter.setSupportedMediaTypes(Arrays.asList(new MediaType("text",
				"plain", Charset.forName("UTF-8")), new MediaType(
				"application", "json", Charset.forName("UTF-8"))));
		return converter;
	}

	@Bean
	public MappingJackson2HttpMessageConverter jsonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Arrays.asList(new MediaType("text",
				"plain", Charset.forName("UTF-8")), new MediaType(
				"application", "json", Charset.forName("UTF-8"))));
		return converter;
	}

	@Bean
	public FormHttpMessageConverter formHttpMessageConverter() {
		FormHttpMessageConverter converter = new FormHttpMessageConverter();
		converter.setSupportedMediaTypes(Arrays.asList(new MediaType("text",
				"plain", Charset.forName("UTF-8"))));
		return converter;
	}
}
