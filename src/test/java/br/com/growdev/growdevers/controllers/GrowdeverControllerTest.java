package br.com.growdev.growdevers.controllers;
import java.util.Arrays;

import br.com.growdev.growdevers.builders.models.GrowdeverBuilder;
import br.com.growdev.growdevers.dtos.ErrorData;
import br.com.growdev.growdevers.dtos.CreateGrowdever;
import br.com.growdev.growdevers.dtos.GrowdeverDetail;
import br.com.growdev.growdevers.enums.EStatus;
import br.com.growdev.growdevers.repositories.GrowdeverRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class GrowdeverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private GrowdeverRepository growdeverRepository;

    @AfterEach
    public void afterEach(){
        growdeverRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar um status 400 devido aos dados estarem inválidos")
    public void createGrowdeverCase1() throws Exception  {
        // given (dado)
        var dataJson = mapper.writeValueAsString(
                new CreateGrowdever(
                        "any_name",
                        "51922223333",
                        "11122233344",
                        "invallid_email",
                        EStatus.ESTUDING,
                        "123456"
                )
        );

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/growdevers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        var data = Arrays.asList(mapper.readValue(response.getContentAsString(), ErrorData[].class));

        assertThat(data.size()).isEqualTo(2);

        assertThat(
                data.stream().anyMatch(error -> error.field().equalsIgnoreCase("cpf"))
        ).isTrue();

        assertThat(
                data.stream().anyMatch(error -> error.field().equalsIgnoreCase("email"))
        ).isTrue();
    }

    @Test
    @DisplayName("Deve retornar 400 quando já existir um usuário com o cpf informado cadastrado no banco de dados")
    public void createGrowdeverCase2() throws Exception  {
        // given (dado)
        var dataJson = mapper.writeValueAsString(
                new CreateGrowdever(
                        "any_name",
                        "51922223333",
                        "17179579009",
                        "any@email.com",
                        EStatus.ESTUDING,
                        "123456"
                )
        );

        var growdever = GrowdeverBuilder.init().withCpf("17179579009").builder();
        growdeverRepository.save(growdever);

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/growdevers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        var data = mapper.readValue(response.getContentAsString(), ErrorData.class);

        assertThat(data.field()).isEqualTo("cpf");
        assertThat(data.message()).isEqualTo("CPF ja cadastrado");
    }

    @Test
    @DisplayName("Deve retornar 400 quando já existir um usuário com o email informado cadastrado no banco de dados")
    public void createGrowdeverCase3() throws Exception  {
        // given (dado)
        var dataJson = mapper.writeValueAsString(
                new CreateGrowdever(
                        "any_name",
                        "51922223333",
                        "17179579009",
                        "any@email.com",
                        EStatus.ESTUDING,
                        "123456"
                )
        );

        var growdever = GrowdeverBuilder.init().builder();
        growdeverRepository.save(growdever);

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/growdevers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        var data = mapper.readValue(response.getContentAsString(), ErrorData.class);

        assertThat(data.field()).isEqualTo("email");
        assertThat(data.message()).isEqualTo("E-mail ja cadastrado");
    }

    @Test
    @DisplayName("Deve retornar 204 com os dados do growdever")
    public void createGrowdeverCase4() throws Exception  {
        // given (dado)
        var dataJson = mapper.writeValueAsString(
                new CreateGrowdever(
                        "any_name",
                        "51922223333",
                        "17179579009",
                        "any@email.com",
                        EStatus.ESTUDING,
                        "123456"
                )
        );

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/growdevers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
