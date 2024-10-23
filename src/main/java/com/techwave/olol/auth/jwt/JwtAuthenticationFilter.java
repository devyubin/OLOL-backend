package com.techwave.olol.auth.jwt;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.techwave.olol.auth.exception.AuthErrorCode;
import com.techwave.olol.auth.exception.AuthException;
import com.techwave.olol.auth.util.RequestUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final List<String> whiteList;
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			if (checkAuthRequired(request)) {
				String token = RequestUtil.getAccessToken(request);
				if (token != null) {
					Authentication authentication = jwtProvider.getAuthentication(token);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}

			filterChain.doFilter(request, response);
		} catch (Exception e) {
			log.error("[JwtAuthenticationFilter] message: {}", e.getMessage());
			throw new AuthException(AuthErrorCode.INVALID_TOKEN);
		}
	}

	private boolean checkAuthRequired(HttpServletRequest request) {
		RequestMatcher rm = new NegatedRequestMatcher(new OrRequestMatcher(
			whiteList.stream()
				.map(AntPathRequestMatcher::new)
				.collect(Collectors.toList())));
		return rm.matcher(request).isMatch();
	}

	//
}
