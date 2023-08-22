package br.com.growdev.growdevers.builders.models;

import br.com.growdev.growdevers.enums.EStatus;
import br.com.growdev.growdevers.models.Growdever;

import java.util.UUID;

public class GrowdeverBuilder {
    private UUID id;
    private String name = "any_name";
    private String email = "any@email.com";
    private String cpf = "11122233344";
    private String numberPhone = "51922223333";
    private EStatus status = EStatus.ESTUDING;
    private String password = "123456";

    public static GrowdeverBuilder init(){
        return new GrowdeverBuilder();
    }

    public GrowdeverBuilder withName(String name){
        this.name = name;
        return this;
    }

    public GrowdeverBuilder withStatus(EStatus status){
        this.status = status;
        return this;
    }

    public GrowdeverBuilder withCpf(String cpf){
        this.cpf = cpf;
        return this;
    }

    public GrowdeverBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public Growdever builder(){
        return new Growdever(
                name,
                email,
                cpf,
                numberPhone,
                status,
                password
        );
    }
}
