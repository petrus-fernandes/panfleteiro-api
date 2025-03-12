package br.com.promo.panfleteiro.repository;

import br.com.promo.panfleteiro.entity.User;
import br.com.promo.panfleteiro.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByLogin(String login);

    UserDetails findByRole(UserRole role);

    User findUserByLogin(String login);
}
