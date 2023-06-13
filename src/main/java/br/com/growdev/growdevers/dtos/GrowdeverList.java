package br.com.growdev.growdevers.dtos;

import br.com.growdev.growdevers.enums.EStatus;
import br.com.growdev.growdevers.models.Growdever;

import java.util.UUID;

// DTO = DATA TRANSFER OBJECT
public record GrowdeverList(UUID id, String name, String email, EStatus status) {
    public GrowdeverList(Growdever growdever){
        this(growdever.getId(), growdever.getName(), growdever.getEmail(), growdever.getStatus());
    }
}
