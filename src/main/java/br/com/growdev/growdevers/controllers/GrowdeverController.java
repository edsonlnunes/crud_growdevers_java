package br.com.growdev.growdevers.controllers;

import br.com.growdev.growdevers.dtos.*;
import br.com.growdev.growdevers.enums.EStatus;
import br.com.growdev.growdevers.models.Growdever;
import br.com.growdev.growdevers.repositories.GrowdeverRepository;
import br.com.growdev.growdevers.repositories.specifications.GrowdeverSpecification;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/growdevers")
public class GrowdeverController {
    @Autowired
    private GrowdeverRepository growdeverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<GrowdeverList>> listGrowdevers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) EStatus status
    ) {
        var specification = GrowdeverSpecification.filterByNameAndStatus(name, status);

        var data = growdeverRepository.findAll(specification).stream().map(
                (growdever) -> new GrowdeverList(growdever)
        ).toList();

        return ResponseEntity.ok(data); // 200
    }

    @GetMapping("/{id}")
    public ResponseEntity getGrowdever(@PathVariable UUID id) {
        var optional = growdeverRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var growDetail = new GrowdeverDetail(optional.get());

        return ResponseEntity.ok(growDetail);
    }

    @PostMapping
    @Transactional
    public ResponseEntity createGrowdever(@RequestBody @Valid CreateGrowdever data) {
        if (growdeverRepository.existsByCpf(data.cpf())) {
            return ResponseEntity.badRequest().body(new ErrorData("cpf", "CPF ja cadastrado"));
        }

        if (growdeverRepository.existsByEmail(data.email())) {
            return ResponseEntity.badRequest().body(new ErrorData("email","E-mail ja cadastrado"));
        }

        var growdever = new Growdever(
                data.name(),
                data.email(),
                data.cpf(),
                data.phone(),
                data.status(),
                passwordEncoder.encode(data.password())
        );

        growdeverRepository.save(growdever);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}") // parametro de rota
    public ResponseEntity deleteGrowdever(@PathVariable UUID id) {
        // exclusão lógica (soft delete) => Que nao remove o registro do banco de dados, somente altera uma coluna
        //                    para indicar que aquele registro está removido (ou desabilitado)

        // exclusão fisíca (hard delete) => Remove o registro do banco de dados.


        if (!growdeverRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        growdeverRepository.deleteById(id);

        return ResponseEntity.noContent().build(); // 201
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity updateGrowdever(@PathVariable UUID id, @RequestBody UpdateGrowdever data) {

        if (!growdeverRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if(data.email() != null && growdeverRepository.existsByEmail(data.email())) {
            return ResponseEntity.badRequest().body(new ErrorData("email", "Já existe um growdever com este e-mail. "));
        }

        var growdever = growdeverRepository.getReferenceById(id);
        growdever.updateInfo(data);
        //growdeverRepository.save(growdever);

        return ResponseEntity.noContent().build();
    }
}
