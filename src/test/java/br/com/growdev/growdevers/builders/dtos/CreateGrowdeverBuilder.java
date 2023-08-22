package br.com.growdev.growdevers.builders.dtos;

import br.com.growdev.growdevers.dtos.CreateGrowdever;
import br.com.growdev.growdevers.enums.EStatus;

public class CreateGrowdeverBuilder {
    private String name = "any_name";
    private String phone = "51922223333";
    private String cpf = "17179579009";
    private String email = "any@email.com";
    private EStatus status = EStatus.ESTUDING;
    private String password = "123456";

    public static CreateGrowdeverBuilder init(){
        return new CreateGrowdeverBuilder();
    }

    public CreateGrowdeverBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public CreateGrowdeverBuilder withCpf(String cpf){
        this.cpf = cpf;
        return this;
    }

    public CreateGrowdever builder(){
        return new CreateGrowdever(
                name,
                phone,
                cpf,
                email,
                status,
                password
        );
    }
}
