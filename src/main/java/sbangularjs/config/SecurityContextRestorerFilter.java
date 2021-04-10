/*
package sbangularjs.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class SecurityContextRestorerFilter extends OncePerRequestFilter {

    private final UserInfoRestTemplateFactory userInfoRestTemplateFactory;
    private final ResourceServerTokenServices userInfoTokenServices;

    public SecurityContextRestorerFilter(UserInfoRestTemplateFactory userInfoRestTemplateFactory, ResourceServerTokenServices userInfoTokenServices) {
        this.userInfoRestTemplateFactory = userInfoRestTemplateFactory;
        this.userInfoTokenServices = userInfoTokenServices;
    }

    @Override
    public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        try {
            final OAuth2AccessToken authentication = userInfoRestTemplateFactory.getUserInfoRestTemplate().getOAuth2ClientContext().getAccessToken();
            if (authentication != null && authentication.getExpiresIn() > 0) {
                OAuth2Authentication oAuth2Authentication = userInfoTokenServices.loadAuthentication(authentication.getValue());
                SecurityContextHolder.getContext().setAuthentication(oAuth2Authentication);
//                log.debug("Added token authentication to security context");
            } else {
                ;
//                log.debug("Authentication not found.");
            }
            chain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }


/*@Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException {
        Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);
        if (map.containsKey("error")) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("userinfo returned error: " + map.get("error"));
            }
            throw new InvalidTokenException(accessToken);
        }
        return extractAuthentication(map);
    }*/

/*}*/
