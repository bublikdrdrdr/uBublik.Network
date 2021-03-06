package ubublik.network.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class Token {

    private static final SignatureAlgorithm defaultAlgorithm = SignatureAlgorithm.HS512;
    private static final long defaultExpiration = 7 * 24 * 60 * 60 * 1000;
    private static final String secretKey = "tYg5gzmHNww923C8bq3gKtnCb3jqdHNsHeZu83pVAVx7BZFN6PZFSFREdCvsJqwJre95Pte";
    private static final String defaultIssuer = "network.ubublik.esy.es";
    private static final String defaultAudience = "client";

    private String username;
    private SignatureAlgorithm algorithm;
    private Date expirationDate;
    private String audience;
    private String issuer;

    public Token(String username){
        this(username, defaultAlgorithm, defaultExpiration);
    }

    public Token(String username, SignatureAlgorithm algorithm, Date expirationDate, String issuer, String audience){
        this.username = username;
        this.algorithm = algorithm;
        this.expirationDate = expirationDate;
        this.issuer = issuer;
        this.audience = audience;
    }

    public Token(String username, SignatureAlgorithm algorithm, long expirationTimeMillis){
        this(username, algorithm, new Date(System.currentTimeMillis()+expirationTimeMillis));
    }

    public Token(String username, SignatureAlgorithm algorithm, Date expirationDate){
        this(username, algorithm, expirationDate, defaultIssuer, defaultAudience);
    }

    @Override
    public String toString() {
        return Jwts.builder()
                .setSubject(username)
                .setAudience(audience)
                .setIssuer(issuer)
                .setExpiration(expirationDate)
                .signWith(algorithm, secretKey)
                .compact();
    }

    public static Token parse(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token).getBody();
        return new Token(claims.getSubject(), defaultAlgorithm, claims.getExpiration(), claims.getIssuer(), claims.getAudience());
    }

    public String getUsername() {
        return username;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAudience() {
        return audience;
    }

    public SignatureAlgorithm getAlgorithm() {
        return algorithm;
    }
}
