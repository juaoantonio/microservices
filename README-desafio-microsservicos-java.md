# Desafio Back-end — Plataforma de Pedidos Distribuída (Java)

Primeiramente, obrigado pelo seu interesse em desenvolver este desafio.  
Abaixo você encontrará as informações necessárias para iniciar o projeto.

## Avisos antes de começar

- Leia este documento com atenção e tente seguir ao máximo as instruções;
- Crie um repositório no seu GitHub com um nome neutro, sem citar empresas reais;
- Faça commits frequentes e com mensagens claras;
- Documente as decisões técnicas que considerar importantes;
- Você pode consultar documentação, artigos, Stack Overflow e materiais de apoio;
- O foco principal é o raciocínio arquitetural, a modelagem da solução e a clareza na implementação;
- Tente equilibrar simplicidade com boas práticas;
- Fique à vontade para deixar observações sobre o que você implementou, o que deixaria para uma próxima etapa e como evoluiria a solução.

## Sobre o ambiente da aplicação

Este desafio foi pensado para ser implementado com **Java** e ferramentas do ecossistema JVM.

### Stack sugerida

Você pode usar alternativas equivalentes, mas a stack recomendada é:

- **Java 17** ou superior;
- **Spring Boot** para construção dos serviços;
- **Spring Web** para APIs REST;
- **Spring Data JPA** para persistência relacional;
- **PostgreSQL** como banco de dados principal;
- **RabbitMQ** ou **Apache Kafka** para mensageria;
- **Docker** e **Docker Compose** para execução local;
- **Maven** ou **Gradle** para build;
- **JUnit 5** e **Testcontainers** para testes;
- **Spring Actuator** para health checks e observabilidade básica;
- **OpenAPI/Swagger** para documentação dos endpoints.

### Observações

- Você pode usar **Spring Cloud**, mas não é obrigatório;
- Caso use muitos starters ou abstrações prontas, explique as decisões e os trade-offs;
- Queremos entender seu raciocínio arquitetural e sua capacidade de estruturar a solução;
- Valorizamos uma boa estrutura de containers criada por você;
- O desafio deve rodar localmente com o menor atrito possível.

## Para o dia da apresentação técnica

Tenha sua aplicação rodando localmente, com instruções claras para execução.  
Durante a conversa, você deverá ser capaz de:

- explicar a arquitetura proposta;
- demonstrar o fluxo principal funcionando;
- justificar o uso de comunicação síncrona e assíncrona;
- explicar decisões de modelagem, consistência e tratamento de falhas;
- comentar pontos de melhoria e evolução da solução.

---

# Objetivo: Plataforma de Pedidos Distribuída

Você deverá construir uma plataforma simplificada de pedidos, com arquitetura baseada em microsserviços.

O sistema deve simular o fluxo de compra de um pedido em um ambiente distribuído, incluindo:

- criação de pedido;
- processamento de pagamento;
- atualização de estoque;
- envio de notificação;
- comunicação síncrona entre alguns serviços;
- comunicação assíncrona orientada a eventos entre outros serviços.

A proposta do desafio é avaliar sua capacidade de modelar um sistema distribuído simples, porém realista, usando **Java** e ferramentas comuns do ecossistema.

---

# Contexto de negócio

Quando um cliente realiza um pedido, o sistema precisa:

1. receber os dados do pedido;
2. validar se os itens existem e se o pedido pode ser iniciado;
3. processar o pagamento;
4. após pagamento aprovado, atualizar o estoque;
5. notificar o cliente sobre o resultado;
6. manter rastreabilidade do fluxo entre os serviços.

A arquitetura deve conter, no mínimo, os seguintes contextos:

- **Order Service**
- **Payment Service**
- **Inventory Service**
- **Notification Service**

Você pode incluir outros componentes caso ache necessário, como API Gateway, broker, service discovery, configuração centralizada, observabilidade etc.

---

# Requisitos funcionais

## Regras principais

- O sistema deve permitir a criação de um pedido com um ou mais itens;
- Cada pedido deve possuir, no mínimo:
  - identificador;
  - cliente;
  - lista de itens;
  - valor total;
  - status;
  - data de criação;
- O pedido deve iniciar com status compatível com o fluxo escolhido, por exemplo `PENDING`;
- O processamento de pagamento deve acontecer por um serviço separado;
- O pagamento deve retornar sucesso ou falha;
- Em caso de pagamento aprovado, o sistema deve publicar um evento para continuidade do fluxo;
- O serviço de estoque deve consumir o evento e reservar ou debitar os itens;
- O serviço de notificação deve ser acionado de forma assíncrona para informar o cliente sobre mudanças relevantes;
- O pedido deve refletir a evolução do fluxo com mudanças de status, por exemplo:
  - `PENDING`
  - `PAYMENT_APPROVED`
  - `PAYMENT_FAILED`
  - `INVENTORY_CONFIRMED`
  - `INVENTORY_FAILED`
  - `COMPLETED`
  - `CANCELLED`

## Comunicação obrigatória

Sua solução deve obrigatoriamente usar os dois estilos abaixo:

### Comunicação síncrona

Use comunicação síncrona para uma etapa crítica do fluxo.

Exemplo esperado:
- `Order Service` chamando `Payment Service` via HTTP usando **Spring WebClient**, **RestClient** ou **OpenFeign**.

### Comunicação assíncrona

Use mensageria ou eventos para uma etapa posterior do fluxo.

Exemplo esperado:
- após pagamento aprovado, publicação de evento `payment.approved`;
- `Inventory Service` consumindo esse evento via **Spring AMQP** ou **Spring for Apache Kafka**;
- `Notification Service` consumindo eventos de domínio como `order.completed`, `payment.failed` ou `inventory.failed`.

---

# Requisitos técnicos

## Arquitetura

A solução deve:

- ser organizada em múltiplos serviços independentes;
- deixar claro o papel de cada serviço;
- preferencialmente isolar persistência por serviço;
- evidenciar separação de responsabilidades;
- permitir execução local via containers.

## Persistência

- Cada serviço pode ter seu próprio banco de dados ou schema dedicado;
- A solução recomendada é **PostgreSQL** com isolamento por banco ou schema por serviço;
- Caso escolha compartilhar banco, explique a decisão e os trade-offs;
- Espera-se uso de modelagem com **JPA/Hibernate** ou alternativa equivalente do ecossistema Java.

## Mensageria

- Use algum mecanismo de mensageria para a comunicação assíncrona;
- Recomendamos **RabbitMQ** ou **Apache Kafka**;
- Caso não implemente um broker real, explique claramente como faria em produção e entregue uma simulação mínima consistente;
- Os contratos de mensagens devem ser claros e versionáveis.

## Tratamento de falhas

Seu sistema deve demonstrar preocupação com cenários de falha, como:

- pagamento recusado;
- indisponibilidade temporária de outro serviço;
- evento processado mais de uma vez;
- falha ao atualizar estoque após pagamento aprovado;
- falha no envio de notificação.

Não é necessário resolver tudo perfeitamente, mas é importante mostrar como você pensou nesses cenários.

## Consistência

Como se trata de um sistema distribuído:

- não é necessário consistência forte entre todos os serviços;
- esperamos que você reconheça e trate o conceito de **consistência eventual**;
- caso use estratégia compensatória, documente-a;
- caso implemente **Outbox Pattern**, **Saga**, retry ou DLQ, isso será considerado diferencial.

---

# Fluxo mínimo esperado

## 1. Criar pedido

O sistema recebe uma solicitação para criar um pedido.

Exemplo:

```http
POST /orders
Content-Type: application/json

{
  "customerId": "c123",
  "items": [
    {
      "productId": "p1",
      "quantity": 2,
      "unitPrice": 50.0
    },
    {
      "productId": "p2",
      "quantity": 1,
      "unitPrice": 30.0
    }
  ],
  "payment": {
    "method": "credit_card"
  }
}
```

## 2. Processar pagamento

Após criar o pedido, o `Order Service` deve chamar o `Payment Service` de forma síncrona.

Exemplo de contrato sugerido:

```http
POST /payments
Content-Type: application/json

{
  "orderId": "o123",
  "customerId": "c123",
  "amount": 130.0,
  "method": "credit_card"
}
```

Resposta esperada:

```json
{
  "paymentId": "pay_123",
  "orderId": "o123",
  "status": "APPROVED"
}
```

ou

```json
{
  "paymentId": "pay_123",
  "orderId": "o123",
  "status": "FAILED"
}
```

## 3. Publicar evento

Após a resposta do pagamento:

- se aprovado, publique um evento para continuidade do fluxo;
- se falhar, atualize o pedido e publique um evento compatível com falha.

Exemplo de evento:

```json
{
  "eventType": "payment.approved",
  "orderId": "o123",
  "customerId": "c123",
  "amount": 130.0,
  "items": [
    {
      "productId": "p1",
      "quantity": 2
    },
    {
      "productId": "p2",
      "quantity": 1
    }
  ],
  "occurredAt": "2026-04-14T10:00:00Z"
}
```

## 4. Atualizar estoque

O `Inventory Service` deve consumir o evento e tentar reservar ou debitar estoque.

Em caso de sucesso:
- publicar algo como `inventory.confirmed`.

Em caso de falha:
- publicar algo como `inventory.failed`.

## 5. Finalizar pedido e notificar

Com base nos eventos:

- o pedido deve ser atualizado para estado final coerente;
- o `Notification Service` deve consumir eventos relevantes e registrar o envio da notificação.

---

# O que esperamos da modelagem

## Serviços mínimos sugeridos

### Order Service
Responsável por:

- criar pedido;
- persistir status do pedido;
- iniciar o fluxo;
- reagir a eventos de pagamento e estoque;
- expor endpoint de consulta do pedido.

### Payment Service
Responsável por:

- receber solicitação de pagamento;
- simular aprovação ou recusa;
- registrar transação;
- responder de forma síncrona ao `Order Service`.

### Inventory Service
Responsável por:

- consumir evento de pagamento aprovado;
- verificar/reservar/debitar estoque;
- publicar sucesso ou falha.

### Notification Service
Responsável por:

- consumir eventos relevantes;
- registrar tentativa de notificação;
- simular sucesso ou falha sem interromper o fluxo principal.

---

# Requisitos mínimos obrigatórios

- API REST para iniciar o fluxo;
- pelo menos uma chamada entre serviços via HTTP;
- pelo menos um fluxo orientado a eventos;
- persistência de dados;
- containers para subir o ambiente localmente;
- README com instruções de execução;
- documentação da arquitetura;
- tratamento básico de erros;
- logs suficientes para acompanhar o fluxo.

---

# Diretrizes específicas para Java

## Organização recomendada

- Separação clara entre camadas, por exemplo:
  - `controller`
  - `service`
  - `domain` ou `model`
  - `repository`
  - `client`
  - `messaging`
  - `config`
- Uso coerente de DTOs para entrada e saída;
- Uso de exceptions tratadas com `@ControllerAdvice` ou abordagem equivalente;
- Mapeamento de entidades com cuidado para evitar acoplamento excessivo entre API e persistência.

## Spring Boot

Esperamos familiaridade com recursos comuns do ecossistema, como:

- `spring-boot-starter-web`
- `spring-boot-starter-validation`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-actuator`
- `spring-boot-starter-test`
- `springdoc-openapi` ou equivalente
- `spring-rabbit` ou `spring-kafka`

## Build e qualidade

Você pode usar **Maven** ou **Gradle**.  
Também será bem-vindo o uso de ferramentas como:

- **Checkstyle**;
- **SpotBugs**;
- **PMD**;
- **JaCoCo**;
- **SonarLint/SonarQube**.

Não é obrigatório usar todas, mas é interessante demonstrar preocupação com qualidade e manutenção.

---

# Diferenciais

## Serão considerados diferenciais

- uso de Docker Compose bem organizado;
- banco isolado por serviço;
- uso real de broker de mensagens;
- documentação de eventos e contratos;
- retries e/ou dead-letter queue;
- idempotência no consumo de mensagens;
- correlação de logs por `orderId`;
- testes unitários com **JUnit 5** e **Mockito**;
- testes de integração com **Spring Boot Test** e **Testcontainers**;
- testes de contrato;
- health checks com **Actuator**;
- observabilidade básica com métricas e tracing;
- proposta de Saga ou compensação;
- uso de API Gateway com **Spring Cloud Gateway**;
- uso de configuração externa e profiles;
- pipeline de CI.

---

# O que não será avaliado

- interface frontend;
- autenticação completa;
- autorização complexa;
- cadastro completo de usuários;
- integrações externas reais;
- regras fiscais, antifraude ou meios de pagamento reais;
- escalabilidade real em nuvem.

---

# O que será avaliado

## Habilidades básicas

- organização do projeto;
- clareza na estrutura dos serviços;
- legibilidade do código;
- domínio dos conceitos de API REST;
- uso de Git;
- documentação de execução.

## Habilidades intermediárias

- separação de responsabilidades;
- modelagem de domínio;
- tratamento de erros;
- uso adequado de comunicação síncrona e assíncrona;
- noções de consistência eventual;
- capacidade de explicar trade-offs;
- testes;
- containerização.

## Habilidades avançadas

- visão arquitetural;
- resiliência;
- idempotência;
- desacoplamento;
- observabilidade;
- desenho de fluxos distribuídos;
- clareza sobre limites dos microsserviços;
- proposta de evolução da solução.

---

# Boas práticas esperadas

- código limpo e organizado;
- nomes claros;
- baixo acoplamento;
- responsabilidade bem definida por componente;
- validação de entrada com **Bean Validation**;
- respostas HTTP coerentes;
- documentação dos eventos;
- README objetivo e completo;
- explicação de limitações conhecidas.

---

# Sugestão de endpoints

Você pode propor seus próprios contratos, mas um conjunto mínimo poderia ser:

```http
POST /orders
GET /orders/{id}
POST /payments
GET /payments/{orderId}
GET /inventory/{productId}
GET /actuator/health
```

Você também pode expor endpoints auxiliares para facilitar demonstração, como:

```http
POST /inventory
POST /seed/products
GET /notifications
```

---

# Sugestão de estrutura de repositório

```text
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
```

A pasta `docs` pode conter, por exemplo:

- diagrama de arquitetura;
- fluxos de sequência;
- contratos de eventos;
- decisões arquiteturais;
- melhorias futuras.

---

# Cenários que sua solução deve contemplar

Pelo menos estes cenários devem estar claros na demonstração:

## Cenário 1 — Fluxo feliz
- pedido criado;
- pagamento aprovado;
- estoque reservado;
- pedido finalizado;
- notificação registrada.

## Cenário 2 — Pagamento recusado
- pedido criado;
- pagamento recusado;
- pedido cancelado ou marcado como falho;
- notificação registrada.

## Cenário 3 — Falha no estoque
- pagamento aprovado;
- estoque indisponível;
- pedido não pode ser concluído;
- solução deve indicar claramente o que acontece com o pedido.

Aqui é muito valioso explicar:
- se haverá compensação;
- se o pedido ficará pendente de análise;
- se haverá cancelamento;
- como você trataria estorno em uma evolução real.

## Cenário 4 — Reprocessamento de evento
- um mesmo evento chega duas vezes;
- o sistema não deve gerar inconsistência.

Mesmo que a implementação seja simples, vale documentar a estratégia.

---

# Entregáveis

Sua entrega deve conter:

- código-fonte completo;
- instruções para subir o ambiente;
- README com visão geral;
- explicação da arquitetura;
- explicação do fluxo síncrono;
- explicação do fluxo assíncrono;
- instruções para testar os cenários principais;
- observações sobre limitações e melhorias futuras.

---

# Materiais úteis

- Documentação oficial do Spring Boot
- Documentação do Spring Data JPA
- Documentação do Spring AMQP ou Spring for Apache Kafka
- Documentação do Spring Actuator
- Documentação do Testcontainers
- Martin Fowler — Microservices
- Refactoring Guru
- OpenAPI Specification
- Materiais sobre:
  - idempotência;
  - consistência eventual;
  - Saga Pattern;
  - comunicação orientada a eventos;
  - observabilidade em sistemas distribuídos.

---

# Observações finais

Tente ser o mais aderente possível ao que foi pedido, mas não se preocupe caso não consiga implementar tudo.  
O mais importante é que sua solução demonstre:

- entendimento dos conceitos;
- clareza na separação entre responsabilidades;
- uso consciente de síncrono e assíncrono;
- capacidade de justificar decisões técnicas;
- visão de evolução arquitetural.

Durante a apresentação, queremos entender não só o que foi feito, mas também como você pensou.

Boa sorte.
