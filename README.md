# Payment API

API REST para recebimento e gerenciamento de pagamentos de débitos de pessoas físicas e jurídicas.

## Tecnologias

- Java 17
- Spring Boot 3.5
- H2 (banco em memória)
- Maven

## Como rodar
```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

O console do H2 está disponível em `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:paymentdb`
- Usuário: `sa`
- Senha: (vazio)

## Endpoints

| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | /pagamentos | Criar pagamento |
| PATCH | /pagamentos/{id}/status | Atualizar status |
| GET | /pagamentos | Listar e filtrar pagamentos |
| DELETE | /pagamentos/{id} | Exclusão lógica |

### Filtros disponíveis no GET

- `?codigoDebito=123`
- `?cpfCnpj=12345678901`
- `?status=PENDENTE`

Os filtros são opcionais e combináveis.

## Regras de negócio

- Pagamentos são criados com status `PENDENTE`
- Cartão de crédito e débito exigem número do cartão
- `PENDENTE` → `PROCESSADO_SUCESSO` ou `PROCESSADO_FALHA`
- `PROCESSADO_SUCESSO` → nenhuma alteração permitida
- `PROCESSADO_FALHA` → apenas `PENDENTE`
- Exclusão lógica permitida apenas para pagamentos `PENDENTE`