# Registro de Bugs – Beleza Ágil v3.0

> Arquivo de rastreamento de falhas encontradas durante a execução do plano de testes.  
> Ciclo de testes realizado em: **22/06/2026**  
> Responsável: **Natália**

---

## Tabela de Bugs

| ID      | Data       | Tipo      | Req | Teste que falhou                       | Descrição da Falha                                                                                                                   | Severidade | Status    | Commit de Correção                     | Data Correção |
|---------|------------|-----------|-----|----------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|------------|-----------|----------------------------------------|---------------|
| BUG-001 | 22/06/2026 | Manual/UI | R3  | TM-R3-02 – duração zero ou negativa   | `ServicoService.salvar()` não validava se `tempoExecucao` era maior que zero. Serviço era salvo com duração 0, gerando lista vazia de slots e tornando o agendamento invisível na grade. | Alta       | ✅ Resolvido | `fix: [BUG-001] validar duracao servico > 0` | 22/06/2026 |
| BUG-002 | 22/06/2026 | Manual/UI | R4  | TM-R4-03 – agendar para hoje          | `Utils.getTodayISO()` usava `toISOString()` (UTC), causando desvio de data no fuso UTC-3 do Brasil. Entre 00:00 e 03:00 o campo exibia ontem como mínimo, e após 21:00 UTC exibia amanhã. | Média      | ✅ Resolvido | `fix: [BUG-002] getTodayISO usar horario local` | 22/06/2026 |
| BUG-003 | 22/06/2026 | Manual/UI | R4  | TM-R4-06 – visualizar na agenda       | Célula da grade exibia apenas o primeiro nome do cliente (`split(' ')[0]`). Clientes com mesmo primeiro nome (ex: "Maria Silva" e "Maria Santos") ficavam indistinguíveis na grade sem abrir o modal. | Baixa      | ✅ Resolvido | `fix: [BUG-003] exibir nome e sobrenome na grade` | 22/06/2026 |
| BUG-004 | 22/06/2026 | Manual/UI | R2  | TM-R2-04 – excluir cliente vinculado  | `GlobalExceptionHandler` retornava mensagem genérica "registro possui vínculos com outros dados" ao tentar excluir qualquer entidade com agendamentos vinculados, sem indicar o que desvincular. | Baixa      | ✅ Resolvido | `fix: [BUG-004] mensagem especifica por entidade vinculada` | 22/06/2026 |
| BUG-005 | 22/06/2026 | Manual/UI | R5  | TM-R5-02 – navegar para data passada  | `agenda.js` definia `dateInput.min = Utils.getTodayISO()` no `init()`, bloqueando a seleção de datas passadas no seletor. Agendamentos históricos existentes no banco eram inacessíveis pela tela. | Média      | ✅ Resolvido | `fix: [BUG-005] remover min do seletor de data da agenda` | 22/06/2026 |

---

## Detalhamento dos Bugs e Correções

### BUG-001 – Serviço com duração zero aceito pelo sistema

**Arquivo:** `api/src/main/java/com/belezaagil/service/ServicoService.java`

**Código original (com problema):**
```java
public ServicoDto salvar(ServicoDto dto) {
    Servico entity = dto.id() != null ? ... : new Servico();
    entity.setTempoExecucaoMinutes(dto.tempoExecucao()); // sem validação
    ...
}
```

**Código corrigido:**
```java
public ServicoDto salvar(ServicoDto dto) {
    // BUG-001 fix: rejeitar duração zero ou negativa
    if (dto.tempoExecucao() == null || dto.tempoExecucao() <= 0) {
        throw new BusinessException("Tempo de execução deve ser maior que zero.");
    }
    ...
}
```

**Reteste (TM-R3-02):** Ao informar duração `0`, o sistema exibe "Tempo de execução deve ser maior que zero." e não salva o serviço. ✅

---

### BUG-002 – Fuso horário incorreto em getTodayISO()

**Arquivo:** `web/js/utils.js`

**Código original (com problema):**
```javascript
getTodayISO() {
    const today = new Date();
    return today.toISOString().split('T')[0]; // retorna data em UTC, não no fuso local
}
```

**Código corrigido:**
```javascript
getTodayISO() {
    // BUG-002 fix: usa getFullYear/Month/Date para evitar desvio de fuso UTC vs local
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}
```

**Reteste (TM-R4-03):** Campo de data exibe a data correta em horário local. Agendamento para hoje aceito sem erro. ✅

---

### BUG-003 – Célula da agenda exibe só o primeiro nome

**Arquivo:** `web/js/agenda.js`

**Código original (com problema):**
```javascript
html += `<td ...>${slot.level === 0 ? slot.cliente.split(' ')[0] : ''}</td>`;
```

**Código corrigido:**
```javascript
// BUG-003 fix: exibe os dois primeiros nomes para distinguir clientes homônimos
const partesNome = slot.cliente.split(' ');
const nomeExibido = partesNome.length > 1 ? `${partesNome[0]} ${partesNome[1]}` : partesNome[0];
html += `<td ...>${slot.level === 0 ? nomeExibido : ''}</td>`;
```

**Reteste (TM-R4-06):** Grade exibe "Maria Silva" e "Maria Santos" corretamente nas células. ✅

---

### BUG-004 – Mensagem genérica ao excluir registro com vínculos

**Arquivo:** `api/src/main/java/com/belezaagil/exception/GlobalExceptionHandler.java`

**Código original (com problema):**
```java
@ExceptionHandler(DataIntegrityViolationException.class)
public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
    return ResponseEntity.badRequest()
            .body(new ErrorResponse("Não é possível excluir: registro possui vínculos com outros dados."));
}
```

**Código corrigido:**
```java
@ExceptionHandler(DataIntegrityViolationException.class)
public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
    // BUG-004 fix: mensagem orientativa indica que vínculos devem ser removidos antes
    String msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
    String resposta;
    if (msg.contains("profissional_id")) {
        resposta = "Não é possível excluir: este profissional possui agendamentos vinculados. Exclua os agendamentos primeiro.";
    } else if (msg.contains("cliente_id")) {
        resposta = "Não é possível excluir: este cliente possui agendamentos vinculados. Exclua os agendamentos primeiro.";
    } else if (msg.contains("servico_id")) {
        resposta = "Não é possível excluir: este serviço possui agendamentos vinculados. Exclua os agendamentos primeiro.";
    } else {
        resposta = "Não é possível excluir: registro possui vínculos com outros dados.";
    }
    return ResponseEntity.badRequest().body(new ErrorResponse(resposta));
}
```

**Reteste (TM-R2-04):** Ao tentar excluir cliente com agendamentos, mensagem informa especificamente "este cliente possui agendamentos vinculados". ✅

---

### BUG-005 – Agenda bloqueia visualização de datas passadas

**Arquivo:** `web/js/agenda.js`

**Código original (com problema):**
```javascript
// init()
this.dateInput.value = this.selectedDate;
this.dateInput.min = Utils.getTodayISO(); // bloqueava datas anteriores
```

**Código corrigido:**
```javascript
// init()
this.dateInput.value = this.selectedDate;
// BUG-005 fix: removido min para permitir consulta ao histórico de datas passadas
```

**Reteste (TM-R5-02):** Seletor de data permite navegar para datas passadas. Agendamentos históricos carregados corretamente. ✅

---

## Histórico de Correções

| ID      | O que foi corrigido                                        | Arquivo(s) alterado(s)                                          | Retestado? |
|---------|------------------------------------------------------------|-----------------------------------------------------------------|------------|
| BUG-001 | Validação de `tempoExecucao > 0` no `ServicoService`       | `service/ServicoService.java`                                   | ✅ Sim     |
| BUG-002 | `getTodayISO()` corrigido para usar horário local          | `web/js/utils.js`                                               | ✅ Sim     |
| BUG-003 | Grade exibe dois primeiros nomes do cliente                | `web/js/agenda.js`                                              | ✅ Sim     |
| BUG-004 | Mensagem de erro de vínculo específica por entidade        | `exception/GlobalExceptionHandler.java`                         | ✅ Sim     |
| BUG-005 | Removida restrição `min` do seletor de data da agenda      | `web/js/agenda.js`                                              | ✅ Sim     |

---

## Métricas do Ciclo de Testes

| Métrica                         | Valor |
|---------------------------------|-------|
| Total de casos de teste manuais | 21    |
| Testes que passaram (1º ciclo)  | 17    |
| Testes que falharam (1º ciclo)  | 4     |
| Bugs registrados                | 5     |
| Bugs resolvidos                 | 5     |
| Bugs em aberto                  | 0     |
| Testes que passaram (reteste)   | 21    |
| Taxa de aprovação final         | 100%  |
