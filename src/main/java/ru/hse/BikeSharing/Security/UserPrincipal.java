package ru.hse.BikeSharing.Security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.hse.BikeSharing.domain.User;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserPrincipal implements UserDetails {

    private Long id;
  //  private String googleID;
  //  private String facebookID;
    private String email;


    //private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String email/*,  String facebookID, String googleID Collection<? extends GrantedAuthority> authorities*/) {
        this.id = id;
        //this.googleID = googleID;
        this.email = email;
        //this.facebookID = facebookID;
      //  this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
//        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
//                new SimpleGrantedAuthority(role.getName().name())
//        ).collect(Collectors.toList());

       // List<GrantedAuthority> authorities = new ArrayList<>();
        //authorities.add(new SimpleGrantedAuthority("USER"));

        return new UserPrincipal(
                user.getId(),
                user.getEmail()
               // user.getFacebookID(),
               // user.getGoogleID()
              //  authorities
        );
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
//        if (facebookID != null) {
//            return facebookID;
//        } else {
//            return googleID;
//        }

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      //  return authorities;

        return Arrays.asList(new SimpleGrantedAuthority("USER"));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
