package br.com.growdev.growdevers.repositories.specifications;

import br.com.growdev.growdevers.enums.EStatus;
import br.com.growdev.growdevers.models.Growdever;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class GrowdeverSpecification {
    public static Specification<Growdever> filterByNameAndStatus(String name, EStatus status, String email){
        return (root, queryBuilder, criteriaBuilder) -> {
            // root => Faz referencia para a entidade (Growdever)
            // criteriaBuilder => É um construtor de condicoes (igual, like, maior que, maior igual que)
            // queryBuilder => É um constutor de condicoes so que utilizando a linguagem SQL

            var conditions = new ArrayList<Predicate>();

            if(name != null  && !name.isEmpty()){
                conditions.add(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%")
                );
            }

            if(status != null){
                conditions.add(
                        criteriaBuilder.equal(root.get("status"), status)
                );
            }

            if(email != null && !email.isEmpty()){
                // ...
            }

            // int != string
            // ArrayList != Array

            // ArrayList => O tamanho da lista é flexivel

            // Array => O tamanho da lista é fixa

            // converte de array list para array
            var conditionsInArray = conditions.toArray(new Predicate[0]);

            return criteriaBuilder.and(conditionsInArray);
        };
    }
}


