package lawfirm.project.auth;

import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class JdbcUserRepository implements UserRepository {
    @Override
    public Integer createUser(User newUser) {
        return null;
    }

    @Override
    public List<Role> getRoles() {
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public Integer updatePassword(Integer userID, String newPassword) {
        return null;
    }
}
