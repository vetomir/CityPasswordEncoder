package pl.gregorymartin.passwordencoder.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Entity
@Getter @Setter @NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private Collection<User> users;

    public Role(String name) {
        this.name = name;
    }
}
