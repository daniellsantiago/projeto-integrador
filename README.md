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