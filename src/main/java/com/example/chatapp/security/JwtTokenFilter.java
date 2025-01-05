//package com.example.chatapp.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//public class JwtTokenFilter extends OncePerRequestFilter {
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final UserDetailsServiceImpl userDetailsService;
//
//    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService) {
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws IOException, jakarta.servlet.ServletException {
//
//        String token = getTokenFromCookie(request);
//
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            String username = jwtTokenProvider.getUsername(token);
//
//            // Load user details to set authentication
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        chain.doFilter(request, response);
//    }
//
//    private String getTokenFromCookie(HttpServletRequest request) {
//        if (request.getCookies() != null) {
//            for (Cookie cookie : request.getCookies()) {
//                if ("jwt".equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
//}
