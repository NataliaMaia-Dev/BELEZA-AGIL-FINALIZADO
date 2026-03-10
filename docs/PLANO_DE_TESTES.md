# Plano de Testes – Beleza Ágil (Esmalteria)

## 1. Objetivo

Este plano descreve a estratégia de testes para o sistema, priorizando **testes unitários** das funcionalidades que **não acessam banco de dados**. Testes que dependem de BD podem ser tratados em etapa posterior (testes de integração ou manuais com ambiente configurado).

---

## 2. Requisitos Principais (já implementados)

Com base no README e na estrutura do projeto, os requisitos considerados são:

| ID | Requisito |
|----|-----------|
| R1 | Cadastro de até 5 profissionais |
| R2 | Cadastro e gerenciamento de clientes |
| R3 | Cadastro e manutenção de serviços |
| R4 | Agendamento de serviços com data e hora |
| R5 | Visualização e controle da agenda de atendimentos |

---

## 3. Estratégia por Tipo de Teste

### 3.1 Testes unitários (JUnit) – implementados

Foco em **lógica de negócio e validações** sem acesso a banco ou UI pesada.

| Classe / funcionalidade | O que é testado | Arquivo de teste |
|-------------------------|-----------------|------------------|
| **Validação de agendamento** | Dados obrigatórios (cliente, data, serviço, profissional); mensagens de exceção | `test/validator/AgendamentoValidatorTest.java` |
| **Cálculo de horários** | Geração de horários ocupados por duração e intervalo; formato HH:mm; bordas (um slot, vários slots, arredondamento) | `test/utils/AgendamentoUtilsTest.java` |
| **Regras do serviço de agendamento** | Comanda nula rejeitada; lista nula/vazia não chama repositório; associação comanda–agendamento com repositórios mockados | `test/service/AgendamentoServiceTest.java` |

**Como executar**

- **NetBeans:** Botão direito no projeto → **Test** (ou atalho **Ctrl+F6**).
- É necessário ter os JARs do JUnit na pasta `lib/` (ver `lib/README-JUNIT.txt`).

Funcionalidades que **dependem de banco** (DAOs, listagens, persistência) foram **dispensadas** dos testes unitários, conforme solicitado.

---

### 3.2 Requisitos × Cobertura de teste

| Requisito | Teste unitário | Teste com banco | Teste manual / UI |
|-----------|----------------|-----------------|-------------------|
| **R1** Cadastro de até 5 profissionais | — | Opcional (integração) | Verificar limite de 5 e persistência na tela/BD |
| **R2** Gerenciamento de clientes | — | Opcional | Cadastrar, editar, listar, excluir clientes |
| **R3** Manutenção de serviços | — | Opcional | Cadastrar, editar, listar serviços |
| **R4** Agendamento com data e hora | Validator + Utils + Service (regras e horários) | Opcional | Abrir agenda, preencher dados, salvar e ver na agenda |
| **R5** Visualização e controle da agenda | — | Opcional | Visualizar grade, conflitos, finalizar/comanda |

Legenda:

- **Teste unitário:** coberto pelos testes JUnit atuais (validação, horários, regras de comanda).
- **Teste com banco:** deixado de fora do escopo atual; pode ser planejado depois (integração ou ambiente de teste).
- **Teste manual/UI:** indicado para validar fluxo completo e limite de 5 profissionais.

---

## 4. Casos de teste sugeridos (resumo)

### 4.1 Já cobertos por JUnit

- **AgendamentoValidator:** cliente/data/serviço/profissional nulos → exceção com mensagem correta; todos preenchidos → sem exceção.
- **AgendamentoUtils.gerarHorariosOcupados:** 1 slot, 2 slots, duração não exata (arredondamento), intervalo 15 min, formato inválido → exceção.
- **AgendamentoService.salvarAgendamentosComComanda:** comanda nula → exceção; lista nula ou vazia → não chama repositório; lista com itens → associa comanda e chama salvar (mock).

### 4.2 Sugestões para testes manuais (requisitos)

- **R1:** Cadastrar 5 profissionais; tentar cadastrar o 6º e verificar bloqueio/mensagem.
- **R2:** Incluir, alterar e listar clientes; buscar por nome/telefone se houver.
- **R3:** Incluir, alterar e listar serviços; duração e preço (se aplicável).
- **R4:** Agendar em horário livre; tentar agendar em horário já ocupado (conflito); validar data e hora na agenda.
- **R5:** Abrir agenda por data; visualizar profissionais e horários; finalizar comanda e conferir atualização.

### 4.3 Testes com banco (futuro, opcional)

- Conexão e criação de comanda.
- Persistência e listagem de agendamentos por data.
- Integração com cadastro de clientes, profissionais e serviços (CRUD).

---

## 5. Resumo

- **Implementado:** testes unitários JUnit para **validação de agendamento**, **cálculo de horários ocupados** e **regras de salvar agendamentos com comanda** (sem uso de banco).
- **Dispensado neste plano:** testes unitários que dependem diretamente do banco de dados.
- **Planejado:** testes manuais para os 5 requisitos principais e, se desejado depois, testes de integração com BD para persistência e fluxos completos.
