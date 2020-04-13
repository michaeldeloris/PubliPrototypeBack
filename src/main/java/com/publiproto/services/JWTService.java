package com.publiproto.services;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class JWTService {
	
	private RSAPrivateKey privateKey;
	private RSAPublicKey publicKey;
	
	private long expirationTime = 1800000;
	
	@PostConstruct
	private void initKeys() throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA"); //the chosen algorithm
		
		generator.initialize(2048); //other possible value : 1024
		
		KeyPair keyPair = generator.generateKeyPair(); //generate the 2 keys public & private
		
		privateKey = (RSAPrivateKey)keyPair.getPrivate();
		publicKey = (RSAPublicKey)keyPair.getPublic();
	}
	
	public String generateToken(String name, String role) {
		return JWT.create()
				.withClaim("user", name) // we claim all the data we want to put on the payload
				.withClaim("role", role)
				.withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))	// set expiration time
				.sign(Algorithm.RSA256(publicKey, privateKey)); // that's what is generating the token
	}
	
	public String validateToken(String token) throws JWTVerificationException {		 //Spring does automatically use this method
		String encodedPayload = JWT.require(Algorithm.RSA256(publicKey, privateKey)) //get algorithm
									.build()										 //build JWTVerifier
									.verify(token)									 //check if the token is correct
									.getPayload();									 //return base64 encoded payload
		
		return new String(Base64.getDecoder().decode(encodedPayload));
	}

}
