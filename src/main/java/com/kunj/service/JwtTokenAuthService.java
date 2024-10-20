package com.kunj.service;

import com.kunj.config.PropertyConfig;
import com.kunj.exception.custome.InvalidAuthHeaderException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The type Jwt token auth service.
 */
@Slf4j
@Service
public class JwtTokenAuthService {


  private final PropertyConfig propertiesConfig;


  /**
   * Instantiates a new Jwt token auth service.
   *
   * @param propertiesConfig the properties config
   */
  public JwtTokenAuthService(PropertyConfig propertiesConfig) {
    log.info("JwtTokenAuthService intialize service : {} ",propertiesConfig);
    this.propertiesConfig = propertiesConfig;
  }

  /**
   * Generates a JSON Web Token (JWT) for the given user details and claims.
   *
   * <p>This method uses the provided {@link UserDetails} to set the subject of the token
   * and includes additional claims specified in the {@code claimToken} map. The token is
   * signed using the provided secret key and the HS256 algorithm. The token's expiration
   * time is set based on the configured expiration period.</p>
   *
   * @param userDetails the details of the user for whom the token is being generated,
   *                    including the username to be set as the subject of the token.
   * @param claimToken a map of claims to be included in the JWT. Claims represent
   *                   additional information to be encoded in the token.
   * @return a {@link String} representing the generated JWT.
   *
   * @throws IllegalArgumentException if the provided {@code userDetails} or {@code claimToken}
   *                                  are {@code null}.
   * @throws JwtException if there is an issue with generating or signing the JWT.
   */
//    public String genrateToken(UserDetails userDetails, Map<String, Object> claimToken) {
//        return Jwts.builder()
//                .setClaims(claimToken)
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + propertiesConfig.getJwtExpiration()))
//                .signWith(getSignInKey(propertiesConfig.getSecretKey()), SignatureAlgorithm.HS256)
//                .compact();
//    }

  /**
   * Generates a {@link SecretKey} for signing JWTs using the provided base64-encoded secret key.
   *
   * <p>This method decodes the base64-encoded secret key into a byte array and then
   * creates a {@link SecretKey} object suitable for HMAC signing algorithms. This key can be used
   * to sign JWTs securely using the HS256 algorithm.</p>
   *
   * @param mobileNumber    a base64-encoded string representing the secret key used for signing
   *                        JWTs. The key should be kept confidential and secure.
   * @param tokenExipreDate the token exipre date
   * @return a {@link SecretKey} object created from the decoded byte array.
   * @throws IllegalArgumentException if the provided {@code secretKey} is {@code null} or cannot be
   *                                  decoded.
   */
  public String genrateToken(String mobileNumber, Date tokenExipreDate) {
    Map<String, Object> claims = new HashMap<>();
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(mobileNumber)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(tokenExipreDate)
        .signWith(getSignInKey(propertiesConfig.getJwtSecretKey()), SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * Claims mobile number from token string.
   *
   * @param token the token
   * @return the string
   */
  public String claimsMobileNumberFromToken(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(getSignInKey(propertiesConfig.getJwtSecretKey()))
          .setAllowedClockSkewSeconds(6000000) // Allow a clock skew of 60 seconds
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getSubject();

    } catch (ExpiredJwtException e) {
      e.printStackTrace();
      throw new InvalidAuthHeaderException("Token expired: ", "8001004");
    } catch (SignatureException e) {
      throw new InvalidAuthHeaderException("Invalid signature:", "8001004");
    }

  }

  private SecretKey getSignInKey(String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}


