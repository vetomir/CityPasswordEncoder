package pl.gregorymartin.passwordencoder.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public
class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(final UserRepository userRepository, final RoleRepository roleRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> showAllUsers(){
        return userRepository.findAll();
    }

    public List<Role> showAllRoles(){
        return roleRepository.findAll();
    }

    public User addUser(User toAdd, String role){
        logger.info("add new User");

        toAdd.setPassword(passwordEncoder.encode(toAdd.getPassword()));
        List<Role> roleArray = new ArrayList<>();
        Optional<Role> role1 = roleRepository.findByName("USER");
        roleArray.add(role1.get());

        if(!role.isBlank()){
            Optional<Role> role2 = roleRepository.findByName(role.toUpperCase());

            role2.ifPresent(roleArray::add);
        }

        toAdd.setRoles(roleArray);
        logger.info("User " + toAdd.getName() + ". Created!");
        return userRepository.save(toAdd);
    }

    public User updateUser(Long id, User toUpdate){
        Optional<User> byId = userRepository.findById(id);

        byId.ifPresent(user -> {
            user.toUpdate(toUpdate);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        });

        logger.info("User " + byId.get().getName() + ". Updated!");
        return userRepository.findById(id).get();
    }
    public Role createRole(Role newRole){
        if(!roleRepository.existsByName(newRole.getName().toUpperCase())){
            newRole.setName(newRole.getName().toUpperCase());
            return roleRepository.save(newRole);
        }
        else throw new IllegalArgumentException("Role is already exists");
    }
    public boolean deleteRole(String role){
        role = "ROLE_" + role.toUpperCase();
        final AtomicBoolean result = new AtomicBoolean(false);
        Optional<Role> byName = roleRepository.findByName(role);
        byName.ifPresent(x -> {
            roleRepository.deleteById(x.getId());
            result.set(true);
        });
        return result.get();
    }

    public boolean deleteApp(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            logger.info("User with id: " + id +". Deleted!");
        }
        return !userRepository.existsById(id);
    }
}
