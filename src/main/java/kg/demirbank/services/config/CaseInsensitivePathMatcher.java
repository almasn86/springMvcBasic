package kg.demirbank.services.config;

import java.util.Map;

import org.springframework.util.AntPathMatcher;

/**
 * @author almasn 
 * URL matcher - ignorecase
 */
public class CaseInsensitivePathMatcher extends AntPathMatcher {
	
    @Override
    protected boolean doMatch(String pattern, String path, boolean fullMatch, Map<String, String> uriTemplateVariables) {
        return super.doMatch(pattern.toLowerCase(), path.toLowerCase(), fullMatch, uriTemplateVariables);
    }
}