package pl.gregorymartin.passwordencoder.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter @Getter @NoArgsConstructor
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    @Email
    private String email;

    private Integer count = 0;

    private boolean isEnabled;

    @ManyToMany(fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    Collection<Role> roles;



    public User(final String name, final String username, final String password, final String email) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        roles = new ArrayList<>();
    }
    public void newRole(Role newRole){
        this.roles.add(newRole);
    }

    public void toUpdate(User toUpdate){
        this.name = toUpdate.name;
        this.username = toUpdate.username;
        this.email = toUpdate.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        roles.forEach(x -> authorities.add(new SimpleGrantedAuthority("ROLE_" + x.getName())));
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

/*    public void toggleEnable() {
        isEnabled = !isEnabled;
    }*/
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


}
