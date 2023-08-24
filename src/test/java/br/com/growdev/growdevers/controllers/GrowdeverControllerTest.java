package br.com.growdev.growdevers.controllers;

import java.util.Arrays;
import java.util.UUID;

import br.com.growdev.growdevers.builders.dtos.CreateGrowdeverBuilder;
import br.com.growdev.growdevers.builders.models.GrowdeverBuilder;
import br.com.growdev.growdevers.dtos.ErrorData;
import br.com.growdev.growdevers.dtos.GrowdeverDetail;
import br.com.growdev.growdevers.dtos.GrowdeverList;
import br.com.growdev.growdevers.enums.EStatus;
import br.com.growdev.growdevers.repositories.GrowdeverRepository;
import br.com.growdev.growdevers.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class GrowdeverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private GrowdeverRepository growdeverRepository;

    public String makeAuthToken(){
        var growdever = GrowdeverBuilder.init().withEmail("logged@email.com").withCpf("99999999999").builder();
        growdeverRepository.save(growdever);
        var token = new TokenService().getToken(growdever);
        return token;
    }

    @AfterEach
    public void afterEach() {
        growdeverRepository.deleteAll();
    }

/*
    ========== CREATE GROWDEVER ==========
*/

    @Test
    @DisplayName("Deve retornar 400 devido aos dados estarem inválidos")
    @WithMockUser
    public void createGrowdeverCase1() throws Exception {
        // given (dado)
        var dataJson = mapper.writeValueAsString(
                CreateGrowdeverBuilder.init().
                        withEmail("invallid_email")
                        .withCpf("11122233344").builder()
        );

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/growdevers")
//                        .header("Authorization", "Bearer ")
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
    @WithMockUser
    public void createGrowdeverCase2() throws Exception {
        // given (dado)
        var dataJson = mapper.writeValueAsString(CreateGrowdeverBuilder.init().builder());

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
    @WithMockUser
    public void createGrowdeverCase3() throws Exception {
        // given (dado)
        var dataJson = mapper.writeValueAsString(CreateGrowdeverBuilder.init().builder());

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
    @DisplayName("Deve retornar 204")
    public void createGrowdeverCase4() throws Exception {
        // given (dado)
        var dataJson = mapper.writeValueAsString(CreateGrowdeverBuilder.init().builder());
        var token = makeAuthToken();

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/growdevers")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

/*
    ========== GET GROWDEVER ==========
*/
    @Test
    @DisplayName("Deve retornar 404 quando nao encontrar o growdever")
    @WithMockUser
    public  void getGrowdeverCase1() throws Exception {
        // given
        var uid = UUID.randomUUID();
        var token = makeAuthToken();

        // when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.get("/growdevers/" + uid)
                        .header("Authorization", "Bearer " + token)
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deve retornar 204 quando encontrar o growdever")
    @WithMockUser
    public  void getGrowdeverCase2() throws Exception {
        // given
         var growdever = GrowdeverBuilder.init().builder();
         growdeverRepository.save(growdever);

        // when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.get("/growdevers/" + growdever.getId())
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var dataResponse = mapper.readValue(response.getContentAsString(), GrowdeverDetail.class);

        assertThat(dataResponse.email()).isEqualTo(growdever.getEmail());
        assertThat(dataResponse.name()).isEqualTo(growdever.getName());
    }

/*
    ========== DELETE GROWDEVER ==========
*/

    @Test
    @DisplayName("Deve retornar 404 quando acessar o id do growdever")
    @WithMockUser
    public void deleteGrowdeverCase1() throws Exception {
        //given
        var id = UUID.randomUUID();

        // when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/growdevers" +id)
        ).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deve retornar 204 ao deletar o growdever")
    @WithMockUser
    public void deleteGrowdeverCase2() throws Exception {
        //given
        var growdever = GrowdeverBuilder.init().builder();
        growdeverRepository.save(growdever);

        //when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/growdevers/" +growdever.getId())
        ).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

        var optionalGrowdever = growdeverRepository.findById(growdever.getId());

        assertThat(optionalGrowdever.isEmpty()).isTrue();
    }


/*
    ========== LIST GROWDEVERs ==========
*/

    // lista os growdevers sem filtro (retornando uma lista vazia)

    @Test
    @DisplayName("Deve retornar 200 com uma lista vazia")
    @WithMockUser
    public void listGrowdeversCase1() throws Exception {
        // given (dado)

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.get("/growdevers")
        ).andReturn().getResponse();

        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    // lista os growdevers sem filtro (retornando uma lista preenchida)
    @Test
    @DisplayName("Deve retornar 200 com uma lista com tres elementos")
    @WithMockUser
    public void listGrowdeversCase2() throws Exception {
        // given (dado)

        var g1 = GrowdeverBuilder.init().builder();
        var g2 = GrowdeverBuilder.init().withCpf("22233344455").withEmail("any2@email.com").builder();

        growdeverRepository.save(g1);
        growdeverRepository.save(g2);

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.get("/growdevers")
        ).andReturn().getResponse();


        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var contentData = Arrays.asList(
                mapper.readValue(response.getContentAsString(), GrowdeverList[].class)
        );

        assertThat(contentData.size()).isEqualTo(2);
    }

    // lista os growdevers filtrado por nome
    @Test
    @DisplayName("Deve retornar 200 com uma lista com 2 elementos filtrados por nome")
    @WithMockUser
    public void listGrowdeversCase3() throws Exception {
        // given (dado)
        var g1 = GrowdeverBuilder.init().withName("Ana Clara").builder();
        var g2 = GrowdeverBuilder.init().withName("Ana Luiza").withCpf("22233344455").withEmail("any2@email.com").builder();
        var g3 = GrowdeverBuilder.init().withName("Lucas").withCpf("33344455566").withEmail("any3@email.com").builder();

        growdeverRepository.save(g1);
        growdeverRepository.save(g2);
        growdeverRepository.save(g3);

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.get("/growdevers")
                        .queryParam("name", "ana")
        ).andReturn().getResponse();


        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var contentData = Arrays.asList(
                mapper.readValue(response.getContentAsString(), GrowdeverList[].class)
        );

        assertThat(contentData.size()).isEqualTo(2);

        assertThat(contentData.stream().anyMatch(
                growdever -> growdever.name().equalsIgnoreCase("ana luiza")
        )).isTrue();

        assertThat(contentData.stream().anyMatch(
                growdever -> growdever.name().equalsIgnoreCase("ana clara")
        )).isTrue();

        assertThat(contentData.stream().anyMatch(
                growdever -> growdever.name().equalsIgnoreCase("lucas")
        )).isFalse();
    }

    // lista os growdevers filtrado por status
    @Test
    @DisplayName("Deve retornar 200 com uma lista com 1 elemento filtrado por status")
    @WithMockUser
    public void listGrowdeversCase4() throws Exception {
        // given (dado)
        var g1 = GrowdeverBuilder.init().withName("Ana Clara").builder();
        var g2 = GrowdeverBuilder.init().withName("Ana Luiza").withStatus(EStatus.GRADUED).withCpf("22233344455").withEmail("any2@email.com").builder();
        var g3 = GrowdeverBuilder.init().withName("Lucas").withStatus(EStatus.CANCELLED).withCpf("33344455566").withEmail("any3@email.com").builder();

        growdeverRepository.save(g1);
        growdeverRepository.save(g2);
        growdeverRepository.save(g3);

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.get("/growdevers")
                        .queryParam("status", "CANCELLED")
        ).andReturn().getResponse();


        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var contentData = Arrays.asList(
                mapper.readValue(response.getContentAsString(), GrowdeverList[].class)
        );

        assertThat(contentData.size()).isEqualTo(1);

        assertThat(contentData.stream().anyMatch(
                growdever -> growdever.name().equalsIgnoreCase("ana luiza")
        )).isFalse();

        assertThat(contentData.stream().anyMatch(
                growdever -> growdever.name().equalsIgnoreCase("ana clara")
        )).isFalse();

        assertThat(contentData.stream().anyMatch(
                growdever -> growdever.name().equalsIgnoreCase("lucas")
        )).isTrue();
    }

    // lista os growdevers filtrado por nome e status
    @Test
    @DisplayName("Deve retornar 200 com uma lista com 1 elemento filtrado por name e status")
    @WithMockUser
    public void listGrowdeversCase5() throws Exception {
        // given (dado)
        var g1 = GrowdeverBuilder.init().withName("Ana Clara").builder();
        var g2 = GrowdeverBuilder.init().withName("Ana Luiza").withStatus(EStatus.GRADUED).withCpf("22233344455").withEmail("any2@email.com").builder();
        var g3 = GrowdeverBuilder.init().withName("Lucas").withStatus(EStatus.CANCELLED).withCpf("33344455566").withEmail("any3@email.com").builder();

        growdeverRepository.save(g1);
        growdeverRepository.save(g2);
        growdeverRepository.save(g3);

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.get("/growdevers")
                        .queryParam("status", "GRADUED")
                        .queryParam("name", "ana")
        ).andReturn().getResponse();


        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var contentData = Arrays.asList(
                mapper.readValue(response.getContentAsString(), GrowdeverList[].class)
        );

        assertThat(contentData.size()).isEqualTo(1);

        assertThat(contentData.stream().anyMatch(
                growdever -> growdever.name().equalsIgnoreCase("ana luiza")
        )).isTrue();

        assertThat(contentData.stream().anyMatch(
                growdever -> growdever.name().equalsIgnoreCase("ana clara")
        )).isFalse();

        assertThat(contentData.stream().anyMatch(
                growdever -> growdever.name().equalsIgnoreCase("lucas")
        )).isFalse();
    }
}
