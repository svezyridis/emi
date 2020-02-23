package lawfirm.project.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lawfirm.project.RestResponse;
import lawfirm.project.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
@RestController
public class AuthController {


    Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Value("${privateKey}")
    private String privateKeyStr;

    @Value("${publicKey}")
    private String publicKeyStr;

    @Autowired
    @Qualifier("jdbcUserRepository")
    private UserRepository userRepository;

    @Autowired
    Validator validator;

    @PostMapping(value = "/register")
    public RestResponse register(@RequestBody User newUser, @CookieValue(value = "jwt", defaultValue = "token") String token){
        if(!validator.simpleValidateToken(token))
            return new RestResponse("error", null, "invalid token");
        if (!verifyNewUser(newUser))
            return new RestResponse("error", null, "missing user attributes");
        newUser.setPassword(BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt()));
        Integer result=userRepository.createUser(newUser);
        if (result == 1)
            return new RestResponse("success", newUser, "user " + newUser.getUsername() + " successfully created");
        else if (result == -1)
            return new RestResponse("error", null, "user already exists");
        else
            return new RestResponse("error", null, "user creation failed");
    }
    @RequestMapping("/login")
    public RestResponse token(@RequestParam(value = "username", defaultValue = "") String username, @RequestParam(value = "password",defaultValue = "") String password, HttpServletResponse response) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            return new RestResponse("error", null, "user not found");
        } else if (!BCrypt.checkpw(password, user.getPassword())) {
            return new RestResponse("error", null, "wrong password");
        } else {
            String token = null;
            try {
                ECPrivateKey privateKey = sphy.ECDSA.reconstructPrivateKey(privateKeyStr);
                Algorithm algorithm = Algorithm.ECDSA256(null, privateKey);

                // Creating Object of ObjectMapper define in Jakson Api
                ObjectMapper obj = new ObjectMapper();
                //do not include null values in JSON
                obj.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                // get User object as a json string
                String jsonStr = obj.writeValueAsString(user);
                // Displaying JSON String
                token = JWT.create()
                        .withClaim("metadata", jsonStr)
                        .withClaim("role", user.getRole())
                        .sign(algorithm);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
                return new RestResponse("error", null, "internal error");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return new RestResponse("error", null, "internal error");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return new RestResponse("error", null, "could not fetch user metadata");
            }

            Cookie cookie = new Cookie("jwt", token);
            cookie.setMaxAge(7 * 24 * 60 * 60);
            cookie.setSecure(false);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return new RestResponse("success", token, "");
        }
    }

    @RequestMapping(value = "/roles")
    public RestResponse getRoles(@CookieValue(value = "jwt", defaultValue = "token") String token) {
        if (!(validator.simpleValidateToken(token)))
            return new RestResponse("error", null, "invalid token");
        List<Role> roles = userRepository.getRoles();
        if (roles == null)
            return new RestResponse("error", null, "roles could not be fetched");
        return new RestResponse("success", roles, null);
    }

    @PostMapping(value= "/password")
    public RestResponse changePassword(@CookieValue(value = "jwt", defaultValue = "token") String token,@RequestBody String newPassword){
        if (!(validator.simpleValidateToken(token)))
            return new RestResponse("error", null, "invalid token");
        Integer userID = validator.getUserID(token);
        if (userID == null)
            return new RestResponse("error", null, "id not found in token");
        Integer result=userRepository.updatePassword(userID,newPassword);
        if(result==-1)
            return new RestResponse("error", null, "password could not be changed");
        return new RestResponse("success", null, "password changed successfully");
    }

    private boolean verifyNewUser(User user) {
        return user.getPassword() != null && user.getRoleID() != null && user.getFirstName() != null && user.getLastName() != null
                 && user.getUsername() != null && user.getPassword() != "" && user.getFirstName() != "" && user.getLastName() != "" && user.getUsername() != "";
    }

}