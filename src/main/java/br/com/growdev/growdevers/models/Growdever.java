package br.com.growdev.growdevers.models;


import br.com.growdev.growdevers.dtos.UpdateGrowdever;
import br.com.growdev.growdevers.enums.EStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

// ORM = hibernete

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "growdevers")
@EqualsAndHashCode(of = "id")
public class Growdever implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String email;
    private String cpf;
    @Column(name = "phone")
    private String numberPhone;
    @Enumerated(EnumType.STRING)
    private EStatus status;
    private String password;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "growdever_id")
    private List<GrowdeverSkill> skills;

    public Growdever(String name, String email, String cpf, String numberPhone, EStatus status, String password) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.numberPhone = numberPhone;
        this.status = status;
        this.password = password;
        skills = new ArrayList<>();
    }

    public void updateInfo(UpdateGrowdever data){
        if(data.name() != null){
            name = data.name();
        }

        if(data.email() != null){
            email = data.email();
        }

        if(data.phone() != null){
            numberPhone = data.phone();
        }

        if(data.status() != null){
            status = data.status();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
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
