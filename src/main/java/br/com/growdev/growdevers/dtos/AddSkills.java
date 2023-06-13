package br.com.growdev.growdevers.dtos;

import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;

public record AddSkills(
        @NotEmpty
        ArrayList<String> skills
) {

}
