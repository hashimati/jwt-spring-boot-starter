package com.joshlong.jwt;

import com.nimbusds.jose.JWSSigner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.security.Principal;
import java.util.Optional;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
@AutoConfigureAfter(JwtTokenAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class ServletTokenEndpointAutoConfiguration {

	@Bean
	RouterFunction<ServerResponse> jwtTokenServletEndpoint(JwtProperties properties, JWSSigner signer) {
		return route()//
				.POST(properties.getTokenUrl(), serverRequest -> {
					Optional<Principal> pp = serverRequest.principal();
					Assert.isTrue(pp.isPresent(), "the principal must be non-null!");
					String token = TokenUtils.buildTokenFor(properties, signer, pp.get());
					return ServerResponse.ok().body(token);
				})//
				.build();
	}

}