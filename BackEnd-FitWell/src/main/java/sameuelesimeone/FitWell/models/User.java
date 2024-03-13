package sameuelesimeone.FitWell.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sameuelesimeone.FitWell.models.Diet.Diet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties({
        "password"
        , "workouts"
        , "diets"
        , "credentialsNonExpired"
        , "accountNonExpired"
        , "authorities"
        , "username"
        , "accountNonLocked"
        , "enabled"})
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String surname;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private List<Role> role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CardWorkout> workouts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Diet> diets;
//    private NoteBookD noteBookD;
//    private NoteBookW noteBookW;


    public User(String username, String email, String password, String name, String surname, Gender gender, String avatar) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.avatar = avatar;
        List<Role> rolesList = new ArrayList<>();
        rolesList.add(Role.USER);
        this.role = rolesList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = this.role.stream().map(element -> new SimpleGrantedAuthority(element.name())).toList();
        return list;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
