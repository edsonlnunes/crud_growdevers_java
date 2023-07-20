package br.com.growdev.growdevers.repositories;

import br.com.growdev.growdevers.models.GrowdeverSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GrowdeverSkillRepository extends JpaRepository<GrowdeverSkill, UUID> {
}
