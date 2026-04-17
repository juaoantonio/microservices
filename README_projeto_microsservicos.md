# Plataforma de Pedidos Distribuída (Java)

Projeto de estudo com arquitetura baseada em microsserviços e
comunicação totalmente assíncrona via eventos.

------------------------------------------------------------------------

## Objetivo

Implementar um sistema distribuído onde o fluxo de pedidos é conduzido
exclusivamente por eventos.

------------------------------------------------------------------------

## Arquitetura

-   Comunicação 100% assíncrona (event-driven)
-   Nenhuma chamada HTTP entre serviços
-   Serviços desacoplados via broker (RabbitMQ ou Kafka)

------------------------------------------------------------------------

## Serviços

-   Order Service
-   Payment Service
-   Inventory Service
-   Notification Service

------------------------------------------------------------------------

## Ciclo de Vida do Pedido

-   **CREATED**: pedido criado e editável\
-   **SUBMITTED**: pedido enviado para pagamento e não editável\
-   **COMPLETED**: pagamento e estoque confirmados\
-   **CANCELLED**: falha no pagamento ou estoque

------------------------------------------------------------------------

## Fluxo Principal

1.  `order.created`
2.  `order.submitted`
3.  `payment.approved` ou `payment.rejected`
4.  `inventory.confirmed` ou `inventory.rejected`
5.  `order.completed` ou `order.cancelled`

------------------------------------------------------------------------

## Regras de Negócio

### Pedido

-   Só pode ser alterado no estado `CREATED`
-   Ao entrar em `SUBMITTED`, os dados são congelados
-   Deve possuir ao menos 1 item válido
-   Valor total deve ser maior que zero

### Pagamento

-   Iniciado após `order.submitted`
-   Estados:
    -   `PENDING`
    -   `APPROVED`
    -   `REJECTED`
-   Valor deve corresponder ao total do pedido

### Inventário

-   Processado apenas após `payment.approved`
-   Deve validar disponibilidade de todos os itens
-   Falha em qualquer item rejeita o pedido

### Notificação

-   Consome eventos finais
-   Não interfere no fluxo principal

------------------------------------------------------------------------

## Eventos

    order.created
    order.updated
    order.submitted
    payment.approved
    payment.rejected
    inventory.confirmed
    inventory.rejected
    order.completed
    order.cancelled

------------------------------------------------------------------------

## Persistência

-   Banco por serviço (preferencial)
-   PostgreSQL
-   JPA/Hibernate

------------------------------------------------------------------------

## Consistência

-   Consistência eventual
-   Estratégias recomendadas:
    -   idempotência
    -   retry
    -   DLQ
    -   Outbox Pattern
    -   Saga (coreografia)

------------------------------------------------------------------------

## Estrutura sugerida

    /project-root
      /services
        /order-service
        /payment-service
        /inventory-service
        /notification-service
      /shared
      /docs
      docker-compose.yml
      README.md

------------------------------------------------------------------------

## Endpoints (externos)

    POST /orders
    GET /orders/{id}
    GET /actuator/health

------------------------------------------------------------------------

## Cenários

### Fluxo completo

-   pedido criado
-   pagamento aprovado
-   estoque confirmado
-   pedido finalizado

### Pagamento recusado

-   pedido criado
-   pagamento rejeitado
-   pedido cancelado

### Falha no estoque

-   pagamento aprovado
-   estoque rejeitado
-   pedido cancelado

### Evento duplicado

-   sistema deve ser idempotente

------------------------------------------------------------------------

## Boas práticas

-   baixo acoplamento
-   contratos de eventos bem definidos
-   logs com `orderId`
-   separação de responsabilidades
-   tratamento de falhas

------------------------------------------------------------------------

## Documentação

A pasta `/docs` pode conter:

-   diagramas de arquitetura
-   fluxos de eventos
-   contratos de mensagens
-   decisões técnicas
