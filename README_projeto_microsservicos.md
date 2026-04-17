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

## Resiliência e Falhas de Comunicação

-   Nenhum serviço deve assumir que outro serviço está disponível no
    momento do processamento.
-   Falhas ao publicar ou consumir eventos devem ser tratadas com retry
    controlado, backoff e limite máximo de tentativas.
-   Mensagens que continuarem falhando após o limite de retries devem ser
    enviadas para uma DLQ para análise e reprocessamento manual ou
    automatizado.
-   A indisponibilidade temporária de `Payment Service`, `Inventory
    Service` ou `Notification Service` não deve bloquear a criação do
    pedido; o fluxo deve continuar quando os eventos pendentes forem
    processados.
-   Timeouts, erros de broker e falhas de serialização devem gerar logs
    com `orderId`, nome do evento e causa da falha.

------------------------------------------------------------------------

## Idempotência

-   Todos os consumidores de eventos devem ser idempotentes.
-   Cada evento deve possuir um identificador único (`eventId`) e dados de
    correlação (`orderId`, `correlationId`).
-   Eventos já processados não devem alterar novamente o estado do
    agregado nem gerar novos eventos duplicados.
-   Transições de estado devem validar o estado atual antes de aplicar a
    mudança. Exemplo: `payment.approved` recebido duas vezes mantém o
    pagamento aprovado sem repetir a confirmação de estoque.
-   Reprocessamentos vindos da DLQ ou de retries devem produzir o mesmo
    resultado de uma primeira execução bem-sucedida.

------------------------------------------------------------------------

## ACK, NACK e Reentrega

-   Consumidores devem usar ACK manual quando a biblioteca/broker permitir.
-   O ACK só deve ser enviado após a persistência local e a publicação de
    eventos derivados terem sido concluídas com sucesso.
-   Em erro transitório, o consumidor deve usar NACK/requeue ou mecanismo
    equivalente para permitir nova tentativa.
-   Em erro não recuperável, como payload inválido ou contrato
    incompatível, a mensagem deve ser rejeitada sem requeue e direcionada
    para DLQ.
-   O sistema deve tolerar mensagens redelivered pelo broker, usando
    idempotência para evitar efeitos colaterais duplicados.

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

### Falha temporária no consumidor

-   evento não confirmado com ACK
-   broker reentrega a mensagem
-   consumidor processa novamente sem duplicar efeitos

### Falha permanente no payload

-   consumidor rejeita a mensagem
-   evento vai para DLQ
-   erro fica rastreável por `eventId` e `orderId`

------------------------------------------------------------------------

## Boas práticas

-   baixo acoplamento
-   contratos de eventos bem definidos
-   logs com `orderId`
-   logs com `eventId` e `correlationId`
-   separação de responsabilidades
-   tratamento de falhas
-   consumidores idempotentes
-   ACK apenas após processamento completo

------------------------------------------------------------------------

## Documentação

A pasta `/docs` pode conter:

-   diagramas de arquitetura
-   fluxos de eventos
-   contratos de mensagens
-   decisões técnicas
