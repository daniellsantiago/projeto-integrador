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

### US06 - Controle de informações dos lotes

O objetivo desse requisito é manter as informações seguras de edições avulsas e fazer um bom controle dos lotes que estão nos armazéns.

Para isso foram criados 3 endpoints:
* Um patch para alterar apenas os valores de quantidade do produto, volume e preço. Foi pensado para que um operador pudesse fazer as alterações, sendo diferente do endpoint de PUT, desenvolvido no requisito 1, que atualiza todas as informações e foi pensado para que um supervisor/líder o utilizasse.
* Um get para receber todos os lotes que estão com menos de 21 dias para vencer. Dessa forma há um bom controle dos lotes que estão armazenados nas seções.
* Um get que retorna todos os lotes que foram alterados em um período determinado, entre duas datas informadas. Assim, um operador ou supervisor poderá verificar as alterações feitas durante determinado momento para assegurar que as informações dos lotes estejam corretas.
