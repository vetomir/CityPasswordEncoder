package pl.gregorymartin.passwordencoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.gregorymartin.passwordencoder.user.UserRepository;


@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private UserRepository repo;

    public UserDetailsServiceImpl(final UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(final String s) throws UsernameNotFoundException {
        return repo.findAllByUsername(s);
    }
}
