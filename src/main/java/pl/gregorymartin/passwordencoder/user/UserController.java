package pl.gregorymartin.passwordencoder.user;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
class UserController {

    static Long uberId;

    private UserService service;
    private UserRepository repository;
    private RoleRepository roleRepository;


    public UserController(final UserService service, final UserRepository repository, final RoleRepository roleRepository) {
        this.service = service;
        this.repository = repository;
        this.roleRepository = roleRepository;
    }

    @RequestMapping("/login")
    public String login(
            Authentication authentication
    ){
        User user = (User) authentication.getPrincipal();
        System.out.println(user.getName());
        repository.findById(user.getId()).ifPresent(x -> {
            x.setCount(x.getCount() + 1);
            repository.save(x);
        });
        return "login";
    }


    @GetMapping("/all")
    String allUsersAndRoles(
            Model model,
            Authentication authentication
    ){
        User user = (User) authentication.getPrincipal();
        final List<String> userRoles = new ArrayList<>();
        user.getRoles().forEach(x -> userRoles.add(x.getName()));


        model.addAttribute("welcomeMsg", "Hello "
                + userRoles.get(userRoles.size()-1)
                + " " + user.getName());
        model.addAttribute("allUsers", service.showAllUsers());
        model.addAttribute("allRoles", service.showAllRoles());
        return "users";

    }

    @GetMapping("/signup")
    String signup(
            Model model
    ) {

        model.addAttribute("userWriteModel", new UserWriteModel());
        model.addAttribute("allRoles", service.showAllRoles());
        return "register";
    }
    @PostMapping("/register")
    String createUser(
            Model model,
            UserWriteModel userWriteModel
    ) {
        if(userWriteModel.getPassword().equals(userWriteModel.getPassword2())){

            User user = new User(
                    userWriteModel.getName(),
                    userWriteModel.getUsername(),
                    userWriteModel.getPassword(),
                    userWriteModel.getEmail()
            );

            service.addUser(
                    user,
                    userWriteModel.getRole()
            );

            model.addAttribute("user", new User());
            model.addAttribute("userWriteModel", new UserWriteModel());
            return "redirect:/login";
        }
        else {
            model.addAttribute("message", "Passwords are NOT THE SAME");
            return "redirect:/signup";
        }


    }
/*
    @GetMapping("/moderate/{id}")
    String editUser(
            Model model,
            @PathVariable Long id
    ) {
        Optional<User> byId = repository.findById(id);


        model.addAttribute("foundedUser", byId.get());
        model.addAttribute("user", );

        return "edit";
    }*/
    @GetMapping("/moderate/{id}")
    String showNoteToEdit(
            Model model,
            @PathVariable Long id
            ) {
        uberId = id;

        User user = repository.findById(uberId).get();
        model.addAttribute("id", uberId);
        model.addAttribute("userById", user);

        return "edit";
    }


    @PostMapping("/edit")
    String editNote(
            @ModelAttribute("userById") User current,
            BindingResult bindingResult,
            Model model
    ) {
        if(bindingResult.hasErrors()){
            return "edit";
        }
        User user = repository.findById(uberId).get();
        user.toUpdate(current);
        service.updateUser(uberId , user);
        model.addAttribute("id", uberId);
        return "redirect:";
    }
/*
    @PostMapping("/edit")
    String updateUser(
            Model model,
            User foundedUser
    ) {

        service.editUser(foundedUser.getId(), foundedUser);

        model.addAttribute("user", new User());
        model.addAttribute("foundedUser", new User());
        return "redirect:";
    }*/
    @GetMapping("/new-role")
    String newRole(
            Model model
    ) {
        model.addAttribute("newRole", new Role());

        return "register-role";
    }

    @PostMapping("/register-role")
    String createRole(
            Model model,
            @ModelAttribute(value = "newRole") Role newRole
    ) {
        service.createRole(newRole);
        model.addAttribute("newRole", new Role());
        return "redirect:";
    }
}
