package kg.demirbank.services.config;

import java.util.EnumSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionTrackingMode;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author almasn DispatcherServlet initializer
 */
public class Initializer extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true"); // 404
																					// handling
		registration.setAsyncSupported(true); // async support

	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { WebAppConfig.class };
	}

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		super.onStartup(servletContext);
		servletContext.setSessionTrackingModes(EnumSet
				.of(SessionTrackingMode.COOKIE));// store session only in the cookie
		servletContext.getSessionCookieConfig().setName("sessionId");//rename default session cookie
		servletContext.getSessionCookieConfig().setHttpOnly(true);//prevent accessing by JS
		servletContext.setInitParameter("defaultHtmlEscape", "true");//prevent XSS
	    if(System.getProperty("spring.profiles.active") == null)
	    	System.setProperty("spring.profiles.active", "test");//set default spring profile if not set
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
