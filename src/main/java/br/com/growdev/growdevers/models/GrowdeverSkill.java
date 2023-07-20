package br.com.growdev.growdevers.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "growdevers_skills")
public class GrowdeverSkill {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    private String name;

    @Column(name = "growdever_id")
    private UUID growdeverId;

    public GrowdeverSkill(String name, UUID growdeverId){
        this.name = name;
        this.growdeverId = growdeverId;
    }
}

// Um pra um?
// Um pra muitos?
// Muitos para muitos?
