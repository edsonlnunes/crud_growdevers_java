package br.com.growdev.growdevers.dtos;

import br.com.growdev.growdevers.enums.EStatus;
import br.com.growdev.growdevers.models.Growdever;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public record GrowdeverDetail(
        UUID id,
        String name,
        String cpf,
        String email,
        String phone,
        EStatus status,
        List<String> skills
) {

    public GrowdeverDetail (Growdever g){
        this(
                g.getId(),
                g.getName(),
                g.getCpf(),
                g.getEmail(),
                g.getNumberPhone(),
                g.getStatus(),
                g.getSkills().stream().map(gs -> gs.getName()).toList()
        );
    }
}
