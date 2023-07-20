package br.com.growdev.growdevers.repositories;

import br.com.growdev.growdevers.enums.EStatus;
import br.com.growdev.growdevers.models.Growdever;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

// repository = padrão de projeto = design pattern
public interface GrowdeverRepository extends JpaRepository<Growdever, UUID>, JpaSpecificationExecutor<Growdever> {

    Boolean existsByCpf(String cpf);

    Boolean existsByEmail(String email);

    //List<Growdever> findAllByLikeNameIgnoreCaseAndStatus(String name, EStatus status);

    void deleteByName(String name);

//    @Query("""
//            select
//                count(g.id)
//            from
//                Growdever g
//            where
//                g.email = :email
//            or
//                g.name = :name
//            """)
    @Query(value = "select count(g.id) from growdevers g where g.email = :email or g.name = :name", nativeQuery = true)
    int procuraPeloEmailOuName(String email, String name);

}


