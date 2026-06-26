# Beleza Ágil — API REST Spring Boot

API REST que conecta o front-end web ao banco MySQL `esmalteria`.

## Pré-requisitos

- Java 17+
- Maven 3.8+
- MySQL 8 com o banco `esmalteria` criado (script em `../src/banco/esmalteria.sql`)

## Configuração

Edite `src/main/resources/application.yml` se necessário:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/esmalteria
    username: root
    password: root
```

## Executar

```bash
cd api
mvn spring-boot:run
```

A API sobe em **http://localhost:8080** e também serve o front-end em **http://localhost:8080/index.html**.

## Endpoints

| Recurso | Base URL |
|---------|----------|
| Clientes | `/api/clientes` |
| Profissionais | `/api/profissionais` |
| Serviços | `/api/servicos` |
| Comandas | `/api/comandas` |
| Agendamentos | `/api/agendamentos` |

## Front-end

O módulo `web/js/storage.js` consome a API em `http://localhost:8080/api`.  
Certifique-se de que o backend está em execução antes de usar o sistema web.
