## Como testar

### Rode a aplicação pelo Shell
 ``` shell
 $ ./mvnw spring-boot:run
 ```
Ela vai estar disponível em localhost:8080

### Postman

Uma vez que a aplicação está rodando, você pode importar a coleção da API no Postman:
[`./doc/projeto-integrador.postman_collection.json`](./doc/projeto-integrador.postman_collection.json)

### Swagger

Com a aplicação rodando, acesse `localhost:8080/swagger-ui/index.html` para consultar a documentação da API

### US06 - Heatmap warehouse

Feature desenvolvida para consulta de saturação de um ou vários setores de um armazém, gerando métricas para
acompanhamento da eficácia na utilização do armazém e identificação de oportunidades de melhorias, visando ao aumentando
da eficiência.

**Métricas geradas**

- volume cm³ área ocupada
- volume cm³ área vazia
- volume utilizado em litros
- quantidade unitária de produto (sku)
- quantidade total de produto
- porcentagem de área vazia
- porcentagem de área ocupada

Os relatórios podem ser consultados por armazém, resumido por setores ou uma busca por um setor específico.

**Documentação** 

- [User Story e utilização.](./doc/requisito-06/US06-heatmap-warehouse.pdf)
- [Coleção da API no Postman.](./doc/requisito-06/projeto-integrador-us06.postman_collection.json)