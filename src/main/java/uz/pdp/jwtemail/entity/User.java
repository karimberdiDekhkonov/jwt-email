package uz.pdp.jwtemail.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.*;

@Data
@Entity(name="users")
@NoArgsConstructor
@AllArgsConstructor

public class User implements UserDetails {
    @Id@GeneratedValue
    private UUID id;//Unique id

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @ManyToOne
    private Role role;

    @UpdateTimestamp
    private Timestamp updateAt;

    private String emailCode;

    private boolean accountNotExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = false;

    //----USER DETAILS METHODLARI----

    //BU USER HUQUQLARI RO'YXATI
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    //USERNING USERNAMENI QAYTARUVCHI METHOD
    @Override
    public String getUsername() {
        return this.email;
    }

    //ACCOUNTNIG AMAL QILISH MUDDATINI QAYTARADI
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNotExpired;
    }

    //ACCOUNT BLOKLANGA HOLATINI QAYTARADI
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    //ACCOUNTNI YAROQLILIK MUDDATI TUGAGAN TUGAMAGANLIGINI BILDIRADI
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    //ACCOUNTNI O'CHIQ YOKI YONIQLIGINI QAYTARADI
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
