package br.com.growdev.growdevers.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "skills", schema = "growdevers")
public class GrowdeverSkill {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    private String name;

    // MUITAS skills pertencem a UM growdever
    // MANY skills has ONE growdever
    // ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "growdever_id")
    private Growdever growdever;
}

// Um pra um?
// Um pra muitos?
// Muitos para muitos?
