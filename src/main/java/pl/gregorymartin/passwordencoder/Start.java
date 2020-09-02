package pl.gregorymartin.passwordencoder;

import org.springframework.stereotype.Component;
import pl.gregorymartin.passwordencoder.user.Role;
import pl.gregorymartin.passwordencoder.user.RoleRepository;
import pl.gregorymartin.passwordencoder.user.User;
import pl.gregorymartin.passwordencoder.user.UserService;


@Component
public class Start {

    private UserService service;
    private RoleRepository roleRepository;

    public Start(final UserService service, final RoleRepository roleRepository) {
        this.service = service;
        this.roleRepository = roleRepository;

        // ROLES

        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));
        roleRepository.save(new Role("MOD"));

        //TEST USER

        User appUser = new User("Administrator", "admin", "admin123","vetomir@gmail.com");
        User appUser1 = new User("Administrator1", "admin1", "admin123","vetomir@gmail.com");
        User appUser2 = new User("Administrator2", "admin2", "admin123","vetomir@gmail.com");
        appUser.setEnabled(true);
        service.addUser(appUser, "ADMIN");
        service.addUser(appUser1, "USERsaf");
        service.addUser(appUser2, "mod");

    }

}
