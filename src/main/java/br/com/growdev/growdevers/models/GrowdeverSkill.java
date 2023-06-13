package br.com.growdev.growdevers.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class GrowdeverSkill {
    private UUID id;
    private String name;

    public GrowdeverSkill(String name) {
        this.name = name;
        id = UUID.randomUUID();
    }
}
