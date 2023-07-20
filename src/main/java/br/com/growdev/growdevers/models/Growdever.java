package br.com.growdev.growdevers.models;


import br.com.growdev.growdevers.dtos.UpdateGrowdever;
import br.com.growdev.growdevers.enums.EStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// ORM = hibernete

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "growdevers", schema = "growdevers")
public class Growdever {
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

    // UM growdever tem MUITAS skills
    // One growdever has MANY skills
    // OneToMany
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "growdever_id")
    private List<GrowdeverSkill> skills;

    // List => É a interface que define os comportamentos (ou seja, é a abstracao)
    // ArrayList => É a implementacao da interface List (define como vai acontecer os comportamentos definidos em List)

    public Growdever(String name, String email, String cpf, String numberPhone, EStatus status) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.numberPhone = numberPhone;
        this.status = status;
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
}
