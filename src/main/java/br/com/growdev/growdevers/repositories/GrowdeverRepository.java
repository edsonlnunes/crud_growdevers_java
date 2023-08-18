package br.com.growdev.growdevers.repositories;

import br.com.growdev.growdevers.enums.EStatus;
import br.com.growdev.growdevers.models.Growdever;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

// repository = padrão de projeto = design pattern
public interface GrowdeverRepository extends JpaRepository<Growdever, UUID>, JpaSpecificationExecutor<Growdever> {
    Boolean existsByCpf(String cpf);

    Boolean existsByEmail(String email);

    UserDetails findByEmail(String email);
}


