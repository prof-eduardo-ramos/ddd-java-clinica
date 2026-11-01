package br.com.fiap.clinica.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Teste de ponta a ponta da API: sobe o Spring e faz requisições HTTP simuladas. */
@SpringBootTest
@AutoConfigureMockMvc
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    private static String corpo(String cpf, String crm, String dataHora) {
        return """
                {
                  "paciente": {"nome": "Ana Souza", "cpf": "%s", "email": "ana@email.com"},
                  "medico": {"nome": "Carlos Lima", "crm": "%s", "uf": "SP", "especialidade": "CARDIOLOGIA"},
                  "dataHora": "%s"
                }
                """.formatted(cpf, crm, dataHora);
    }

    private static String proxima(DayOfWeek dia) {
        return LocalDate.now().plusWeeks(1).with(TemporalAdjusters.next(dia)).atTime(10, 0).toString();
    }

    @Test
    @DisplayName("Agenda consulta (201) e recusa o mesmo médico no mesmo horário (422)")
    void agendaERecusaConflito() throws Exception {
        String dataHora = proxima(DayOfWeek.TUESDAY);

        mvc.perform(post("/consultas").contentType(MediaType.APPLICATION_JSON)
                        .content(corpo("529.982.247-25", "111111", dataHora)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("AGENDADA"))
                .andExpect(jsonPath("$.paciente").value("Ana Souza"));

        mvc.perform(post("/consultas").contentType(MediaType.APPLICATION_JSON)
                        .content(corpo("153.509.460-56", "111111", dataHora)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.detail").value("Médico já possui consulta neste horário"));
    }

    @Test
    @DisplayName("CPF inválido devolve 422 com a mensagem do domínio")
    void cpfInvalidoDevolve422() throws Exception {
        mvc.perform(post("/consultas").contentType(MediaType.APPLICATION_JSON)
                        .content(corpo("111.111.111-11", "222222", proxima(DayOfWeek.WEDNESDAY))))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.detail").value("CPF inválido: 11111111111"));
    }

    @Test
    @DisplayName("Consulta no domingo devolve 422")
    void domingoDevolve422() throws Exception {
        mvc.perform(post("/consultas").contentType(MediaType.APPLICATION_JSON)
                        .content(corpo("529.982.247-25", "333333", proxima(DayOfWeek.SUNDAY))))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.detail").value("A clínica não funciona aos domingos"));
    }

    @Test
    @DisplayName("Requisição sem médico devolve 400")
    void semMedicoDevolve400() throws Exception {
        mvc.perform(post("/consultas").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"paciente": {"nome": "Ana Souza", "cpf": "529.982.247-25", "email": "ana@email.com"},
                                 "dataHora": "2026-12-07T10:00:00"}
                                """))
                .andExpect(status().isBadRequest());
    }
}
