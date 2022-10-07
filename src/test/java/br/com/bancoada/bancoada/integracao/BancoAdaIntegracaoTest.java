package br.com.bancoada.bancoada.integracao;

import br.com.bancoada.bancoada.BancoAdaApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.bancoada.bancoada.entity.ContaCorrente;
import br.com.bancoada.bancoada.repository.ContaCorrenteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = BancoAdaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class BancoAdaIntegracaoTest {

    // PROJETO REALIZADO PELAS ALUNAS: ANA PAULA SILVA ANTONIO / BEATRIZ VASCONCELOS / JULIANA DAL OLIO /
    //                                 KEILA ESQUEDINO / MARIANA PIVARO / MARIANE KELCZESKI

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ContaCorrenteRepository repository;

    @Test
    void testarGetSaldo() throws Exception {
        // criar conta corrente
        ContaCorrente jorgeContaCorrente = new ContaCorrente();
        jorgeContaCorrente.setTitular("jorge");
        jorgeContaCorrente.setSaldo(new BigDecimal("22"));

        ContaCorrente contaDoJorge = repository.save(jorgeContaCorrente);


        //http://localhost:8080/conta-corrente/saldo?id=1

        // performa uma chamada para o endpoint
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/conta-corrente/saldo").param("id", contaDoJorge.getId() + ""))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String body = result.getResponse().getContentAsString();

        BigDecimal saldoRetornado = new BigDecimal(body);

//        Assertions.assertEquals(new BigDecimal(22), saldoRetornado);
        Assertions.assertTrue(saldoRetornado.compareTo(new BigDecimal(22)) == 0);
    }

    @Test
    void depositar() throws Exception {

        // criando conta para teste
        ContaCorrente cc = new ContaCorrente();
        cc.setSaldo(new BigDecimal("22.39"));
        cc.setTitular("maria");

        //salvando e retornando conta de teste atualizada
        ContaCorrente contaCorrenteSalva = repository.save(cc);

        // testando chamada do endpoint depositar
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/conta-corrente/{id}/depositar/11", contaCorrenteSalva.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        // buscando conta a qual foi depositada
        ContaCorrente cc1 = repository.findById(contaCorrenteSalva.getId())
                .orElseThrow();

        // testando se saldo foi atualizado
        Assertions.assertEquals(new BigDecimal("33.39"), cc1.getSaldo());
    }

    @Test
    public void testarNovaConta() throws Exception {

        // crie conta para teste
        ContaCorrente novaConta = new ContaCorrente(); // instanciei o objeto
        novaConta.setTitular("Juliana");
        novaConta.setSaldo(new BigDecimal(40));

                mockMvc.perform(MockMvcRequestBuilders.post("/conta-corrente/nova-conta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaConta)))
                .andExpect(status().isCreated())
                .andReturn();

            }
}
