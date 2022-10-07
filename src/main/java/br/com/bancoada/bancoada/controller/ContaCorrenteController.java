package br.com.bancoada.bancoada.controller;

import br.com.bancoada.bancoada.entity.ContaCorrente;
import br.com.bancoada.bancoada.service.ContaCorrenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/conta-corrente")
@RequiredArgsConstructor
public class ContaCorrenteController {

    private final ContaCorrenteService service;

    @PostMapping("/nova-conta")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ContaCorrente criarNovaConta(@RequestBody ContaCorrente contaCorrente) {
        // chamada para uma nova conta
        // estou pegando a informação do service, pois lá criei o método "salvar conta".
         return service.criarNovaConta(contaCorrente) ;
    }

    // www.bancoada.com/conta-corrente/saldo?id=1
    @GetMapping("/saldo")
    public BigDecimal consultarSaldo(@RequestParam("id") int idConta) {
        BigDecimal saldo = service.consultarSaldo(idConta);
        return saldo;
    }

    // www.bancoada.com/conta-corrente/1/sacar/20
    @PostMapping("/{id_conta}/sacar/{valor}")
    public ResponseEntity<BigDecimal> sacar(@PathVariable("id_conta") int idconta, @PathVariable BigDecimal valor) {
        BigDecimal novoSaldo = service.sacar(idconta, valor);
        return ResponseEntity.ok(novoSaldo);
    }

    // www.bancoada.com/conta-corrente/1/transferir/2/20
    @PostMapping("/{id_conta_origem}/transferir/{id_conta_destino}/{valor}")
    public ResponseEntity<BigDecimal> transferir(@PathVariable("id_conta_origem") int idContaOrigem,
                                                 @PathVariable("id_conta_destino") int idContaDestino,
                                                 @PathVariable BigDecimal valor) {

        BigDecimal novoSaldoOrigem = service.transferir(idContaOrigem, idContaDestino, valor);
        return ResponseEntity.ok(novoSaldoOrigem);
    }

    @PutMapping("/{idConta}/depositar/{valor}")
    public ResponseEntity<Void> depositar(@PathVariable int idConta, @PathVariable BigDecimal valor) {
        service.depositar(idConta, valor);
        return ResponseEntity.ok().build();
    }

    /**
     *TRABALHO FINAL
     *
     * Implementar o teste unitário do criar conta;
          *
     * Implementar o método criar conta no service;
     *
     *
     * - requisitos
     * 2. não é possível criar uma conta com zero ou menos reais, deve começar com um valor
     *
     */
}
