package br.com.growdev.growdevers.controllers;

import br.com.growdev.growdevers.database.Database;
import br.com.growdev.growdevers.dtos.*;
import br.com.growdev.growdevers.enums.EStatus;
import br.com.growdev.growdevers.models.Growdever;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("growdevers")
public class GrowdeverController {

    // URL => dominio.com.br/growdevers/id?name=Edson
    // Query Params

    @GetMapping
    public ResponseEntity<List<GrowdeverList>> listGrowdevers(@RequestParam(required = false) String name, @RequestParam(required = false) EStatus status) {
//        var data = Database.getGrowdevers().stream().map(
//                (growdever) -> new GrowdeverList(growdever)
//        ).toList();

        var data = Database.getGrowdevers().stream().filter(growdever-> {

            // nome e status ao tempo
            if(name != null && status != null){
                return growdever.getName().toLowerCase().contains(name.toLowerCase())
                        && growdever.getStatus().equals(status);
            }

            // somente nome ou somente status
            if(name != null || status != null){
                var filterByName = false;
                var filterByStatus = false;

                if(name != null){
                    filterByName = growdever.getName().toLowerCase().contains(name.toLowerCase());
                }

                if(status != null){
                    filterByStatus = growdever.getStatus().equals(status);
                }

                return filterByName || filterByStatus;
            }

            return true;
        }).map(
                (growdever) -> new GrowdeverList(growdever)
        ).toList();


        return ResponseEntity.ok(data); // 200
    }

    @GetMapping("/{id}")
    public  ResponseEntity getGrowdever(@PathVariable UUID id){
        var growdever = Database.getGrowdeverById(id);

        if(growdever == null){
            return ResponseEntity.badRequest().body(new ErrorData("Growdever não localizado"));
        }

        var growDetail = new GrowdeverDetail(growdever);

        return  ResponseEntity.ok(growDetail);
    }

    @PostMapping
    public ResponseEntity createGrowdever(@RequestBody @Valid CreateGrowdever data) {
        if(Database.growdeverExitsByCPF(data.cpf())){
            return ResponseEntity.badRequest().body(new ErrorData("CPF já cadastrado"));
        }

        if(Database.growdeverExitsByEmail(data.email())){
            return ResponseEntity.badRequest().body(new ErrorData("E-mail já cadastrado"));
        }

        var growdever = new Growdever(
                data.name(),
                data.email(),
                data.cpf(),
                data.phone(),
                data.status()
        );

        Database.addGrowdever(growdever);

        return ResponseEntity.noContent().build(); // 201
    }

    @DeleteMapping("/{id}") // parametro de rota
    public ResponseEntity deleteGrowdever(@PathVariable UUID id) {
        var growdever = Database.getGrowdeverById(id);

        if(growdever == null){
            return ResponseEntity.badRequest().body(new ErrorData("Growdever não localizado"));
        }

        Database.removeGrowdever(growdever);

        return ResponseEntity.noContent().build(); // 201
    }

    @PutMapping("/{id}")
    public ResponseEntity updateGrowdever(@RequestBody @Valid UpdateGrowdever data, @PathVariable UUID id ){

        var growdever = Database.getGrowdeverById(id);

        if(growdever == null){
            return ResponseEntity.badRequest().body(new ErrorData("Growdever não localizado"));
        }

        if(data.email() != null && Database.growdeverExitsByEmail(data.email())){
            return ResponseEntity.badRequest().body(new ErrorData("Já existe um growdever com este e-mail"));
        }

        growdever.updateInfo(data);

        Database.updateGrowdever(growdever);

        return ResponseEntity.noContent().build();
    }
}
