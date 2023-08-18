package br.com.growdev.growdevers.builders.dtos;

import br.com.growdev.growdevers.dtos.UpdateGrowdever;
import br.com.growdev.growdevers.enums.EStatus;

public class UpdateGrowdeverBuilder {
    private String name = "any_name_updated";
    private String phone = "51944445555";
    private String email = "any_updated@email.com";
    private EStatus status = EStatus.GRADUED;

    public static UpdateGrowdeverBuilder init(){
        return new UpdateGrowdeverBuilder();
    }

    public UpdateGrowdeverBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public UpdateGrowdeverBuilder withStatus(EStatus status){
        this.status = status;
        return this;
    }

    public UpdateGrowdeverBuilder withName(String name){
        this.name = name;
        return this;
    }

    public UpdateGrowdeverBuilder withPhone(String phone){
        this.phone = phone;
        return this;
    }

    public UpdateGrowdever builder(){
        return new UpdateGrowdever(
                name,
                phone,
                email,
                status
        );
    }
}
