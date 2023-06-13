package br.com.growdev.growdevers.dtos;

import br.com.growdev.growdevers.enums.EStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

// DTO = Data Transfer object
public record CreateGrowdever(
        @NotBlank
        @Length(min = 3, max = 30)
        String name,
        String phone,
        @NotBlank
        @CPF
        String cpf,
        @NotBlank
        @Email
        String email,
        @NotNull
        EStatus status
) { }

// @NotNull -> utilizado para todos os tipos de dados -> a informação não pode ser nula, mas pode ser vazia
// @NotEmpty -> utilizado para strings e arrays -> a informação não pode ser nula e nem vazia
// @NotBlank -> utilizado para strings -> a informação não pode ser nula e nem vazia
