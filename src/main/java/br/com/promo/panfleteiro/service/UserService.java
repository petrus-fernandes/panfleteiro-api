package br.com.promo.panfleteiro.service;

import br.com.promo.panfleteiro.entity.User;
import br.com.promo.panfleteiro.entity.UserRole;
import br.com.promo.panfleteiro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }

    public void createUserAdminIfNotExists() {
        if (userRepository.findByRole(UserRole.ADMIN) == null) {
            String encryptedPassword = new BCryptPasswordEncoder().encode("admin");
            User user = new User("admin", encryptedPassword, UserRole.ADMIN);
            userRepository.save(user);
        }
    }
}
