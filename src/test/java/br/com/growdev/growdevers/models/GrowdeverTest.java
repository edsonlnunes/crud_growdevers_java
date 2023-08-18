package br.com.growdev.growdevers.models;

import br.com.growdev.growdevers.builders.dtos.UpdateGrowdeverBuilder;
import br.com.growdev.growdevers.builders.models.GrowdeverBuilder;
import br.com.growdev.growdevers.dtos.UpdateGrowdever;
import br.com.growdev.growdevers.enums.EStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


class GrowdeverTest {

    @Test
    @DisplayName("Deve atualizar name, email, phoneNumber e status")
    void updateInfoCase1() {
        // given (dado)
        var growdever = GrowdeverBuilder.init().builder();

        var dataToUpdate = UpdateGrowdeverBuilder.init().builder();

        // when (quando)
        growdever.updateInfo(dataToUpdate);

        // then (entao)
        assertThat(growdever.getName()).isEqualTo(dataToUpdate.name());
        assertThat(growdever.getNumberPhone()).isEqualTo(dataToUpdate.phone());
        assertThat(growdever.getEmail()).isEqualTo(dataToUpdate.email());
        assertThat(growdever.getStatus()).isEqualTo(dataToUpdate.status());
    }

    @Test
    @DisplayName("Deve atualizar name e numberPhone e não deve atualizar email e status")
    void updateInfoCase2() {
        // given (dado)
        var growdever = GrowdeverBuilder.init().builder();

        var dataToUpdate = UpdateGrowdeverBuilder.init()
                .withEmail(null).withStatus(null).builder();

        // when (quando)
        growdever.updateInfo(dataToUpdate);

        // then (entao)
        assertThat(growdever.getName()).isEqualTo(dataToUpdate.name());
        assertThat(growdever.getNumberPhone()).isEqualTo(dataToUpdate.phone());

        assertThat(growdever.getEmail()).isEqualTo("any@email.com");
        assertThat(growdever.getStatus()).isEqualTo(EStatus.ESTUDING);
    }

    @Test
    @DisplayName("Deve atualizar email e status e não deve atualizar name e phoneNumber")
    void updateInfoCase3() {
        // given (dado)
        var growdever = GrowdeverBuilder.init().builder();

        var dataToUpdate = UpdateGrowdeverBuilder.init()
                .withName(null).withPhone(null).builder();

        // when (quando)
        growdever.updateInfo(dataToUpdate);

        // then (entao)
        assertThat(growdever.getName()).isEqualTo("any_name");
        assertThat(growdever.getNumberPhone()).isEqualTo("51922223333");

        assertThat(growdever.getEmail()).isEqualTo(dataToUpdate.email());
        assertThat(growdever.getStatus()).isEqualTo(dataToUpdate.status());
    }
}