package lawfirm.project;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;

@Service
public class Validator {
    @Value("${publicKey}")
    private final  String publicKeyStr;

    @Autowired
    public Validator(@Value("${publicKey}") String publicKeyStr){
        this.publicKeyStr=publicKeyStr;
    }
    /**
     * @param token
     * @return true if token is valid, false otherwise
     */
    public boolean simpleValidateToken(String token) {
        try {
            ECPublicKey publicKey = sphy.ECDSA.reconstructPublicKey(publicKeyStr);
            Algorithm algorithm = Algorithm.ECDSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public DecodedJWT decode(String token){
        String[] arrOfStr = token.split(" ");
        if(arrOfStr.length>1)
            token=arrOfStr[1].replace("\"", "");
        return JWT.decode(token);
    }
    public  Integer getUserID(String token){
        DecodedJWT decrypted=JWT.decode(token);
        String metadata=decrypted.getClaim("metadata").asString();
        ObjectMapper mapper = new ObjectMapper();
        Integer id= null;
        try {
            id = mapper.readTree(metadata).get("id").asInt();
        } catch (JsonProcessingException e) {
            return id;
        }
        return id;
    }

    public String getUserRole(String token){
        DecodedJWT decrypted=JWT.decode(token);
        return decrypted.getClaim("role").asString();
    }
}
