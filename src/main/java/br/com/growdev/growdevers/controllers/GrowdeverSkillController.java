package br.com.growdev.growdevers.controllers;

import br.com.growdev.growdevers.dtos.AddSkills;
import br.com.growdev.growdevers.dtos.ErrorData;
import br.com.growdev.growdevers.models.GrowdeverSkill;
import br.com.growdev.growdevers.repositories.GrowdeverRepository;
import br.com.growdev.growdevers.repositories.GrowdeverSkillRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/growdevers")
public class GrowdeverSkillController {

    @Autowired
    private GrowdeverRepository growdeverRepository;

    @Autowired
    private GrowdeverSkillRepository growdeverSkillRepository;

    @PostMapping("/{idGrowdever}/skills")
    @Transactional
    public ResponseEntity addSkills(@PathVariable UUID idGrowdever, @Valid @RequestBody AddSkills newSkills) {
        var optionalGrowdever = growdeverRepository.findById(idGrowdever);

        if (optionalGrowdever.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("","Growdever não localizado"));
        }

        var growdever = optionalGrowdever.get();

        for (String skill : newSkills.skills()) {
            var findSkill = growdever.getSkills().stream().filter(gs -> gs.getName().equalsIgnoreCase(skill)).findAny();

            if (findSkill.isEmpty()) {
                var newSkill = new GrowdeverSkill(skill, growdever.getId());
                growdeverSkillRepository.save(newSkill);
            }
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idGrowdever}/skills/{skill}")
    public ResponseEntity deleteSkill(@PathVariable UUID idGrowdever, @PathVariable String skill) {

        var optionalGrowdever = growdeverRepository.findById(idGrowdever);

        if (optionalGrowdever.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("","Growdever não localizado"));
        }

        var growdever = optionalGrowdever.get();

        var skillOptional = growdever.getSkills().stream()
                .filter(growdeverSkill -> growdeverSkill
                        .getName().equalsIgnoreCase(skill)).findAny();

        // skillOptional.isPresent() => retorna verdadeiro SE a skill existir
        // skillOptional.isEmpty() => retornar verdadeiro SE  a skill NÃO existir
        if (skillOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("","Skill não existe"));
        }

        growdeverSkillRepository.delete(skillOptional.get());

        return ResponseEntity.noContent().build();
    }
}
