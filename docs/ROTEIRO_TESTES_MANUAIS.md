# Roteiro de Testes Manuais – Beleza Ágil v3.0

> Execute com o backend rodando em `http://localhost:8080`  
> e o frontend servido (ex: Live Server no VS Code ou `npx serve web/`).  
> Registre qualquer falha encontrada no `REGISTRO_DE_BUGS.md`.

**Data de execução:** 22/06/2026  
**Executado por:** Natália  
**Versão testada:** Beleza Ágil v3.0  
**Status geral:** ✅ Todos os bugs corrigidos e retestados em 22/06/2026

---

## Pré-condições
- [x] Backend Spring Boot iniciado sem erros no console
- [x] Banco de dados MySQL acessível e schema criado
- [x] Frontend acessível via Live Server (`http://localhost:5500`)
- [x] Banco limpo (sem dados residuais)

---

## R1 – Cadastro de até 5 Profissionais

### TM-R1-01 – Cadastrar 5 profissionais com sucesso

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Acessar a aba **Profissionais** | Tela carrega, lista vazia, contador "0 de 5" | ✅ Conforme |
| 2 | Preencher dados válidos e salvar (repetir 5×) | Cada profissional aparece na lista; contador incrementa | ✅ Conforme |
| 3 | Verificar contador após 5 cadastros | "5 de 5 profissionais cadastrados" | ✅ Conforme |

**Resultado:** ✅ Passou

---

### TM-R1-02 – Bloquear cadastro do 6º profissional

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Com 5 profissionais, preencher dados do 6º e clicar em Salvar | Alerta "Limite máximo de 5 profissionais atingido!" | ✅ Alerta exibido |
| 2 | Verificar lista | Continua com 5 profissionais | ✅ Conforme |

**Resultado:** ✅ Passou

---

### TM-R1-03 – CPF duplicado em profissional

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Tentar cadastrar profissional com CPF já existente | Erro inline no campo CPF: "CPF já cadastrado." | ✅ Conforme |
| 2 | Verificar que registro não foi criado | Lista não alterada | ✅ Conforme |

**Resultado:** ✅ Passou

---

### TM-R1-04 – CPF inválido (dígitos verificadores errados)

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Informar CPF `111.111.111-11` no formulário | Erro "CPF inválido." antes de enviar ao servidor | ✅ Conforme (validado no frontend) |

**Resultado:** ✅ Passou

---

## R2 – Gerenciamento de Clientes

### TM-R2-01 – Cadastrar cliente com dados válidos

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Acessar **Clientes** → Novo | Formulário abre | ✅ Conforme |
| 2 | Preencher nome, CPF, data de nascimento, telefone e salvar | "Cliente cadastrado com sucesso!" | ✅ Conforme |
| 3 | Verificar na lista | Cliente aparece com dados corretos | ✅ Conforme |

**Resultado:** ✅ Passou

---

### TM-R2-02 – Editar cliente existente

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Clicar em cliente na lista | Formulário preenchido com dados atuais | ✅ Conforme |
| 2 | Alterar o nome e salvar | "Cliente atualizado com sucesso!" | ✅ Conforme |

**Resultado:** ✅ Passou

---

### TM-R2-03 – Excluir cliente sem agendamentos

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Selecionar cliente sem agendamentos e clicar Excluir | Confirmação exibida | ✅ Conforme |
| 2 | Confirmar exclusão | "Cliente excluído com sucesso!", some da lista | ✅ Conforme |

**Resultado:** ✅ Passou

---

### TM-R2-04 – Excluir cliente com agendamentos vinculados

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Criar agendamento para um cliente específico | Agendamento salvo | ✅ Conforme |
| 2 | Tentar excluir esse cliente | Mensagem específica: "Não é possível excluir: este cliente possui agendamentos vinculados. Exclua os agendamentos primeiro." | ✅ Conforme após correção BUG-004 |

**Resultado:** ✅ Passou *(reteste pós BUG-004)*

---

## R3 – Manutenção de Serviços

### TM-R3-01 – Cadastrar, editar e excluir serviço

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Cadastrar serviço com nome, duração (ex: 30) e valor (ex: 50,00) | Serviço aparece na lista | ✅ Conforme |
| 2 | Editar duração para 45 min | Duração atualizada | ✅ Conforme |
| 3 | Excluir serviço sem agendamentos | Serviço removido | ✅ Conforme |

**Resultado:** ✅ Passou

---

### TM-R3-02 – Cadastrar serviço com duração zero ou negativa

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Informar duração `0` no campo de tempo de execução e salvar | Erro: "Tempo de execução deve ser maior que zero." | ✅ Conforme após correção BUG-001 |
| 2 | Verificar que serviço não foi criado | Lista não alterada | ✅ Conforme |

**Resultado:** ✅ Passou *(reteste pós BUG-001)*

---

## R4 – Agendamento com Data e Hora

### TM-R4-01 – Agendar em horário livre

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Acessar **Agendamento** | Data padrão = hoje; número de comanda pré-carregado | ✅ Conforme |
| 2 | Selecionar cliente, serviço (30 min), profissional e horário 09:00 | Slots 09:00 e 09:15 serão ocupados | ✅ Conforme |
| 3 | Salvar | "Agendamento(s) salvo(s) com sucesso!", redirecionado para agenda | ✅ Conforme |

**Resultado:** ✅ Passou

---

### TM-R4-02 – Conflito de horário detectado no frontend

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Com agendamento existente (profissional A, 09:00, 30 min), tentar agendar mesmo profissional A às 09:00 | Erro: "Conflito detectado para o profissional X." | ✅ Frontend detecta e bloqueia |

**Resultado:** ✅ Passou

---

### TM-R4-03 – Agendamento para hoje (data atual) deve ser permitido

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Acessar tela de agendamento | Campo de data exibe a data de hoje corretamente em horário local | ✅ Conforme após correção BUG-002 |
| 2 | Confirmar agendamento para hoje | Agendamento aceito sem erro de "data no passado" | ✅ Conforme |

**Resultado:** ✅ Passou *(reteste pós BUG-002)*

---

### TM-R4-04 – Data passada rejeitada

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Forçar data de ontem via DevTools (remover atributo `min` do input) e submeter | Frontend e/ou backend rejeitam com mensagem de data passada | ✅ Backend rejeita corretamente |

**Resultado:** ✅ Passou

---

### TM-R4-05 – Campos obrigatórios vazios no agendamento

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Submeter formulário sem selecionar cliente | Erro inline "O campo 'Cliente' é obrigatório." | ✅ Conforme |
| 2 | Submeter sem serviço | Erro inline no campo Serviço | ✅ Conforme |
| 3 | Submeter sem profissional | Erro inline no campo Profissional | ✅ Conforme |

**Resultado:** ✅ Passou

---

### TM-R4-06 – Visualizar agendamento na agenda após salvar

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Após salvar agendamento, verificar tela de Agenda na data correspondente | Células ocupadas exibem nome e sobrenome do cliente | ✅ Conforme após correção BUG-003 |
| 2 | Cadastrar dois clientes com mesmo primeiro nome (ex: "Maria Silva" e "Maria Santos") e agendar ambos | Cada célula exibe nome diferente: "Maria Silva" e "Maria Santos" | ✅ Distinguíveis na grade |

**Resultado:** ✅ Passou *(reteste pós BUG-003)*

---

## R5 – Visualização e Controle da Agenda

### TM-R5-01 – Visualizar agenda por data

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Acessar **Agenda** | Grade com todos os profissionais como colunas, horários como linhas | ✅ Conforme |
| 2 | Selecionar data com agendamentos | Células ocupadas aparecem na cor correta | ✅ Conforme |
| 3 | Selecionar data sem agendamentos | Grade vazia | ✅ Conforme |

**Resultado:** ✅ Passou

---

### TM-R5-02 – Navegar para data passada para consultar histórico

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Acessar tela de Agenda | Seletor de data sem restrição de data mínima | ✅ Conforme após correção BUG-005 |
| 2 | Selecionar data anterior a hoje | Grade carrega agendamentos históricos da data selecionada | ✅ Agendamentos passados visíveis |

**Resultado:** ✅ Passou *(reteste pós BUG-005)*

---

### TM-R5-03 – Abrir modal de detalhes ao clicar em agendamento

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Clicar em célula ocupada na agenda | Modal abre com dados: cliente, data, status, serviços da comanda | ✅ Conforme |
| 2 | Verificar lista de serviços no modal | Exibe horário, serviço e profissional de cada item da comanda | ✅ Conforme |

**Resultado:** ✅ Passou

---

### TM-R5-04 – Finalizar comanda

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Abrir detalhes de agendamento ativo | Botão "Finalizar" habilitado | ✅ Conforme |
| 2 | Clicar em Finalizar | "Comanda finalizada com sucesso!", células ficam com estilo "finalizado" | ✅ Conforme |
| 3 | Abrir detalhes novamente | Botão "Finalizar" desabilitado, status = "Finalizado" | ✅ Conforme |

**Resultado:** ✅ Passou

---

### TM-R5-05 – Excluir comanda

| Passo | Ação | Resultado Esperado | Resultado Obtido |
|-------|------|--------------------|-----------------|
| 1 | Abrir detalhes de agendamento e clicar em Excluir | Confirmação solicitada | ✅ Conforme |
| 2 | Confirmar | Todos os agendamentos da comanda removidos da grade | ✅ Conforme |

**Resultado:** ✅ Passou

---

## Resumo da Execução

| Req | Total de TMs | Passaram | Falharam | Bugs encontrados | Bugs corrigidos |
|-----|-------------|----------|----------|-----------------|----------------|
| R1  | 4           | 4        | 0        | 0               | —              |
| R2  | 4           | 4        | 0        | BUG-004         | ✅ BUG-004     |
| R3  | 2           | 2        | 0        | BUG-001         | ✅ BUG-001     |
| R4  | 6           | 6        | 0        | BUG-002, BUG-003| ✅ BUG-002, BUG-003 |
| R5  | 5           | 5        | 0        | BUG-005         | ✅ BUG-005     |
| **Total** | **21** | **21** | **0** | **5 bugs** | **✅ 5/5** |
