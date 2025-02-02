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

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private String secretKey = "2D9A4F1C0E238A653BC1170F3B32A6916E72597D63596A120AC9B76943AAB9E5";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the WebSocket endpoint with JWT authentication via HandshakeInterceptor
        registry.addEndpoint("/enfocare/chat/ws")
                .setAllowedOrigins("https://enfocare-service-production.up.railway.app") // Allow only Railway frontend
                .withSockJS()
                .setHandshakeHandler(new CustomHandshakeHandler()) // Handle authentication during handshake
                .addInterceptors(new JwtHandshakeInterceptor()); // Intercept handshake for JWT validation
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // Enable message broker for topic and queue destinations
        registry.setApplicationDestinationPrefixes("/app"); // Prefix for app-level destinations
    }

    // Optionally, if you need to configure security to disable CSRF for WebSockets and allow other resources
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/enfocare/chat/ws").permitAll() // Permit access to WebSocket endpoint
            .anyRequest().authenticated()
            .and()
            .csrf().disable(); // Disable CSRF for WebSocket
    }

    // Custom HandshakeInterceptor to extract and validate JWT
    public class JwtHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
        @Override
        public boolean beforeHandshake(
            org.springframework.web.socket.server.HandshakeRequest request,
            org.springframework.web.socket.server.HandshakeResponse response,
            org.springframework.web.socket.WebSocketHandler wsHandler) throws Exception {

            // Extract token from WebSocket handshake (headers or query parameter)
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove "Bearer " prefix

                // Validate the token (use your JWT validation logic)
                if (isValidToken(token)) {
                    return true; // Proceed with handshake
                }
            }
            response.setStatusCode(HttpStatus.FORBIDDEN); // Reject connection if invalid token
            return false;
        }

        // JWT validation logic
        private boolean isValidToken(String token) {
            try {
                // Parse the token and validate signature, expiration, etc.
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey) // Set the secret key to validate the signature
                        .parseClaimsJws(token)    // Parse the token
                        .getBody();               // Extract the claims from the token

                // Check if the token has expired
                if (claims.getExpiration().before(new java.util.Date())) {
                    return false; // Token is expired
                }

                return true; // Token is valid
            } catch (SignatureException | ExpiredJwtException e) {
                // Invalid signature or expired token
                return false;
            }
        }
    }

    // Custom HandshakeHandler to handle authentication logic during WebSocket handshake
    public class CustomHandshakeHandler extends org.springframework.web.socket.server.support.DefaultHandshakeHandler {
        @Override
        public boolean doHandshake(org.springframework.web.socket.server.HandshakeRequest request,
                                    org.springframework.web.socket.server.HandshakeResponse response,
                                    org.springframework.web.socket.WebSocketHandler handler) throws Exception {
            // Optionally, you can add logic for extracting or validating JWT here as well
            return super.doHandshake(request, response, handler);
        }
    }
}
