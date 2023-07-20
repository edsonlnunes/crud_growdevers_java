package br.com.growdev.growdevers.controllers;

import br.com.growdev.growdevers.database.Database;
import br.com.growdev.growdevers.dtos.AddSkills;
import br.com.growdev.growdevers.dtos.ErrorData;
import br.com.growdev.growdevers.models.GrowdeverSkill;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/growdevers")
public class GrowdeverSkillController {
    @PostMapping("/{idGrowdever}/skills")
    public ResponseEntity addSkills(@PathVariable UUID idGrowdever, @Valid @RequestBody AddSkills newSkills){
//        var growdever = Database.getGrowdeverById(idGrowdever);
//
//        if(growdever == null){
//            return ResponseEntity.badRequest().body(new ErrorData("Growdever não localizado"));
//        }
//
//        for (String skill : newSkills.skills()) {
//            var findSkill = growdever.getSkills().stream().filter(gs -> gs.getName().equalsIgnoreCase(skill)).findAny();
//
//            if(findSkill.isEmpty()) {
//                growdever.getSkills().add(new GrowdeverSkill(skill));
//            }
//        }

        return ResponseEntity.noContent().build();
    }
    /*
        skills ja cadastradas: ["Flutter", "Dart", "React", "Java", "Angular", "PHP", "NodeJS"]
        novas skills: ["PHP", "Java", "NodeJS"]

        - percorrer as novas skills
        - para cada nova skill, verificar se a skill em questao existe
          nas habilidades do growdever
        - Se existir, ignorar e seguir e pular para próxima skill
        - Se não existir, adicionar essa skill a lista de habilidades do growdever
     */

    @DeleteMapping("/{idGrowdever}/skills/{skill}")
    public ResponseEntity deleteSkill (@PathVariable UUID idGrowdever , @PathVariable String skill ) {
        System.out.println(skill);

//        var growdever = Database.getGrowdeverById(idGrowdever);
//
//        if(growdever == null){
//            return ResponseEntity.badRequest().body(new ErrorData("Growdever não localizado"));
//        }
//
//       var skillOptional = growdever.getSkills().stream()
//                .filter(growdeverSkill -> growdeverSkill
//                        .getName().equalsIgnoreCase(skill)).findAny();
//
//        // skillOptional.isPresent() => retorna verdadeiro SE a skill existir
//        // skillOptional.isEmpty() => retornar verdadeiro SE  s skill NÃO existir
//        if (skillOptional.isEmpty()){
//            return ResponseEntity.badRequest().body(new ErrorData("Skill não existe"));
//        }
//
//        growdever.getSkills().remove(skillOptional.get());

        return ResponseEntity.noContent().build();
    }
}
