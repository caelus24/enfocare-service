import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
    private String secretKey = System.getenv("JWT_SECRET_KEY"); // Read from environment variable for security

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/enfocare/chat/ws")
                .setAllowedOrigins("https://enfocare-service-production.up.railway.app")
                .withSockJS()
                .setHandshakeHandler(new CustomHandshakeHandler())
                .addInterceptors(new JwtHandshakeInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/enfocare/chat/ws").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable();
    }

    public class JwtHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
        @Override
        public boolean beforeHandshake(
            org.springframework.web.socket.server.HandshakeRequest request,
            org.springframework.web.socket.server.HandshakeResponse response,
            org.springframework.web.socket.WebSocketHandler wsHandler) throws Exception {

            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                if (isValidToken(token)) {
                    return true;
                }
            }
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }

        private boolean isValidToken(String token) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                        .getBody();

                if (claims.getExpiration().before(new java.util.Date())) {
                    log.error("Token has expired");
                    return false;
                }
                return true;
            } catch (SignatureException | ExpiredJwtException e) {
                log.error("Invalid JWT token: {}", e.getMessage());
                return false;
            } catch (Exception e) {
                log.error("Unexpected error during token validation: {}", e.getMessage());
                return false;
            }
        }
    }

    public class CustomHandshakeHandler extends org.springframework.web.socket.server.support.DefaultHandshakeHandler {
        @Override
        public boolean doHandshake(org.springframework.web.socket.server.HandshakeRequest request,
                                    org.springframework.web.socket.server.HandshakeResponse response,
                                    org.springframework.web.socket.WebSocketHandler handler) throws Exception {
            return super.doHandshake(request, response, handler);
        }
    }
}
