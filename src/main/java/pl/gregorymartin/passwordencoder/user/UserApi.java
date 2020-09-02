package pl.gregorymartin.passwordencoder.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserApi {
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserApi.class);

    public UserApi(final UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    ResponseEntity<List<User>> readAllUsers() {
        logger.warn("Exposing all the people!");
        return ResponseEntity.ok(userService.showAllUsers());
    }

    @GetMapping("/role")
    ResponseEntity<List<Role>> readAllRoles() {
        logger.warn("Exposing all the people!");
        return ResponseEntity.ok(userService.showAllRoles());
    }

    @PostMapping
    ResponseEntity<User> createAppUser(@RequestBody @Valid User toCreate, @RequestHeader(defaultValue = "") String role) {
        User result = userService.addUser(toCreate, role);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }


    @PostMapping("/role")
    ResponseEntity<?> createRole(@RequestBody Role role) {
        userService.createRole(role);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    ResponseEntity<?> updateUser(@RequestParam long id, @RequestBody User toUpdate) {
        userService.updateUser(id,toUpdate);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping
    ResponseEntity<?> deleteUser(@RequestParam long id) {
        boolean result = userService.deleteApp(id);
        if(result){
            return ResponseEntity.noContent().build();
        }
        else
            return ResponseEntity.notFound().build();
    }


}
