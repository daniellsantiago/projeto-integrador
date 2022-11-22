## Como testar

### Rode a aplicação pelo Shell
 ``` shell
 $ ./mvnw spring-boot:run
 ```
Ela vai estar disponível em localhost:8080

## Postman

Uma vez que a aplicação está rodando, você pode importar a coleção da API no Postman:
[`./doc/projeto-integrador.postman_collection.json`](./doc/projeto-integrador.postman_collection.json)

## Swagger

Com a aplicação rodando, acesse `localhost:8080/swagger-ui/index.html` para consultar a documentação da API


## Requisito 06 - Buyer CRUD

O objetivo deste requisito é efetuar o gerenciamento do cliente, possibilitando sua criação, atualização, deleção e consulta individual.

Cada operação pode ser feita através de 4 endpoints diferentes, sendo eles:

#### GET
Obtém os dados do cliente cujo id foi inserido.
Retorna 404 caso nenhum cliente com o id provido seja encontrado.

#### POST
Insere um novo cliente com base nos dados enviados.
Retorna 422 caso algum dado enviado seja inválido.

#### PATCH
Altera os dados de um cliente cujo id foi inserido.
Retorna 404 caso nenhum cliente com o id provido seja encontrado.
Retorna 422 caso algum dado enviado seja inválido.


#### DELETE
Deleta os dados de um cliente cujo id foi inserido.
Retorna 404 caso nenhum cliente com o id provido seja encontrado.


## Validações
Ao inserir novo cliente, é validado:
* se o CEP corresponde ao endereço cadastrado
* se o CPF é válido
* se o CPF é único
