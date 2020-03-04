package lawfirm.project.auth;

import java.util.List;

public interface UserRepository {
    Integer createUser(User newUser);
    List<Role> getRoles();

    User findByUsername(String username);

    Integer updatePassword(Integer userID, String newPassword);

    List<User> getAllUsers();

    Integer deleteUser(Integer userID);
}
