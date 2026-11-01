package br.com.fiap.clinica.web;

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

    @Test
    @DisplayName("POST /pacientes com CPF inválido devolve 422 com a mensagem do domínio")
    void cpfInvalidoDevolve422() throws Exception {
        mvc.perform(post("/pacientes").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome": "Ana Souza", "cpf": "111.111.111-11", "email": "ana@email.com"}
                                """))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.detail").value("CPF inválido: 11111111111"));
    }

    @Test
    @DisplayName("POST /pacientes sem nome devolve 400")
    void semNomeDevolve400() throws Exception {
        mvc.perform(post("/pacientes").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cpf": "529.982.247-25", "email": "ana@email.com"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Fluxo completo: cadastra paciente e médico, agenda e recusa conflito")
    void fluxoCompleto() throws Exception {
        String pacienteId = extrairId(mvc.perform(post("/pacientes").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome": "Bruno Alves", "cpf": "153.509.460-56", "email": "bruno@email.com"}
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        String medicoId = extrairId(mvc.perform(post("/medicos").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome": "Carla Dias", "crm": "654321", "uf": "SP", "especialidade": "PEDIATRIA"}
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        String dataHora = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0).toString();
        String consulta = """
                {"pacienteId": "%s", "medicoId": "%s", "dataHora": "%s"}
                """.formatted(pacienteId, medicoId, dataHora);

        mvc.perform(post("/consultas").contentType(MediaType.APPLICATION_JSON).content(consulta))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("AGENDADA"));

        mvc.perform(post("/consultas").contentType(MediaType.APPLICATION_JSON).content(consulta))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.detail").value("Médico já possui consulta neste horário"));
    }

    private static String extrairId(String json) {
        return json.replaceAll(".*\"id\"\\s*:\\s*\"([^\"]+)\".*", "$1");
    }
}
