package egs.task.services.user;

import egs.task.models.entities.User;
import egs.task.repositories.RoleRepository;
import egs.task.repositories.UserRepository;
import egs.task.services.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("userService")
@Transactional
public class UserServiceImpl extends AbstractService<User, UserRepository> implements UserService, UserDetailsService {
    @Lazy
    private final UserRepository userRepository;
    @Lazy
    private final RoleRepository roleRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByEmailAndHiddenFalse(username);
        Optional<User> optionalUserByPhone = userRepository.findByPhoneNumberAndHiddenFalse(username);
        if (optionalUser.isEmpty() && optionalUserByPhone.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Email or phoneNumber %s doesn't exist", username));
        }
        User user = optionalUser.orElseGet(optionalUserByPhone::get);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        if (username.contains("@") || (username.length() != 12 && !username.contains("+"))) { // condition after || for authentication apis, where guest's email is deviceId (not phone)
            return userRepository.findByEmailAndHiddenFalse(username);
        } else {
            return userRepository.findByPhoneNumberAndHiddenFalse(username);
        }
    }

    @Override
    public void deleteByUsername(String deviceId) {
        userRepository.deleteByEmail(deviceId);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
