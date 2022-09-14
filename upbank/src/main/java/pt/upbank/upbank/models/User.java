// package pt.upbank.upbank.models;

// import java.io.Serializable;

// import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.Id;

// import lombok.Data;

// @Data
// @Entity
// public class User implements Serializable {
//     private @Id @GeneratedValue Long id;
    
// }

package pt.upbank.upbank.models;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails ;
import javax.persistence.*;
import java.io.Serializable ;
import java.util.Collection;
import java.util.UUID ;
@Entity
@Table ( name = "TB_USER" )
public class User implements UserDetails , Serializable {
    private static final long serialVersionUID = 1L ;
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private UUID userId ;
    @Column ( nullable = false , unique = true )
    private String username;
    @Column ( nullable = false )
    private String password ;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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

    public void setUserId(UUID userId ) {
        this.userId = userId ;
    }

    public void setUsername ( String username ) {
        this.username = username ;
    }

    public void setPassword ( String password ) {
        this.password = password ;
    }
}
