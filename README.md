<p align="center">
  <img src="https://img.shields.io/static/v1?label=Spring Essential - Dev Superior&message=Estudo Spring Batch&color=8257E5&labelColor=000000" alt="estudo spring batch" />
</p>


# Objetivo

Parte 2 dos estudos de spring batch.

# Componentes Spring Batch

## Objetivo

Visão geral dos componentes que compõem o Spring Batch.

Compreender o ciclo de vida do Job e seus Steps.

## Spring Batch

Imagine um cenário:

Um sistema de uma grande empresa (amazon), que processa dezenas de milhares de pedidos todos os dias. Este processamento
deve ser rápido e confiável e deve funcionar sem interrupções.

## Componentes do Spring Batch

![img.png](img.png)

-Visão geral dos componentes do Spring Batch-

Referência: https://spring.io/batch

### Entendendo cada um dos componentes

#### Job

![img_1.png](img_1.png)

É o componente principal! Ele irá definir uma tarefa e como ela irá se comportar.

Um Job é uma aplicação que processa uma quantidade **finita** de dados sem **interação** ou **interrupção**.

Um Job, pode ser composto de vários Steps.

Pode também, ser compreendido como uma máquina de estados com sequência de steps (etapas) que irão possuir uma lógica
própria.

![img_2.png](img_2.png)

#### Step

![img_3.png](img_3.png)

Step representa uma etapa ou passo no qual uma lógica é executada.

Geralmente, eles são encadeados com o intuito de fornecer um resultado ao final de um processamento. Ou seja, sempre
esperamos que no final do step, ele nos dê algum resultado.

Temos dois tipos de Steps: tasklet e chunks.

##### Tasklet

Os baseados em tasklet são mais simples e geralmente não tem uma lógica complexa. 

Um bom exemplo pode ser impressão de mensagem ou algum log.

##### Chunks

Já os chunks são mais complexos e normalmente são quebrados em 3 etapas:

1. Leitura (ItemReader) - Leitura de um arquivo CSV, por exemplo;
2. Processamento (ItemProcessor) - Poderia remover dados, duplicatas desse CSV;
3. Escrita (ItemWriter) - Gravar dados em outra base dados, alimentar sistema.

![img_4.png](img_4.png)

Esse steps podem ser acompanhados de outros steps.

#### JobRepository

![img_5.png](img_5.png)

JobRepository irá manter o estado do Job (duração da execução, status, erros, etc.)

É também responsável por manter os metadados e são utilizados pelos componentes do framework para controlar o fluxo
de operação do Job.

#### JobLaucher

![img_6.png](img_6.png)

É responsável por executar o job! (geralmente por meio de agendamentos).

É possível definir parâmetros e propriedades da execução (variáveis de ambiente, por exemplo).

# Configurando MySQL com Docker compose

## Script Docker compose

[Link github](https://gist.github.com/oliveiralex/fe234f71d7e594ea83566cfb2625c180)

Para subir a estrutura

> docker-compose up -d

Parar a estrutura

> docker-compose down

Ou simplesmente coloque o arquivo na pasta do projeto, executa com o docker aberto e monte os containers.

Abra o ``localhost:5050`` e insira os dados abaixo:

### Dados de conexão (phpMyAdmin)

Server: mysql-docker
Usuário: root / user
Senha: 1234567

### Dados de conexão para o MySQL Workbench

Host: 127.0.0.1
Porta: 3307
Usuário: root / user

### Crie um banco de dados

Após logar, crie um banco de dados. Novo > e coloque o nome do banco (usaremos spring_batch).

![img_7.png](img_7.png)

Nesse banco de dados, é onde criaremos os nossos metadados.

# Estudo de caso - acessando metadados Spring Batch

Agora que criamos o banco de dados, iremos adaptar a nossa aplicação para se conectar a ele. Assim que iniciarmos o 
projeto, iremos visualizar esses metadados.

❗Inserir o MySQL driver como dependência.

Em ``application.properties``, colocar a nossa conexão com o banco de dados MySQL que criamos.

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/spring_batch
spring.datasource.username=user/root
spring.datasource.password=1234567

# Criar as tabelas de metadados
spring.batch.jdbc.initialize-schema=always
```

Ao rodar a aplicação e voltar para o phpMyAdmin, podemos verificar as tabelas de metadados criadas!

![img_8.png](img_8.png)

## Tabelas metadados

Existem duas tabelas que são mais importantes:

1. BATCH_JOB_INSTANCE

![img_9.png](img_9.png)

Essa tabela nos mostrará a quantidade de execuções lógicas feitas desse Job. Teremos seu nome, ID e seu hash.

2. BATCH_JOB_EXECUTION

![img_10.png](img_10.png)

Teremos o ID de execução do Job, a versão, data de criação, seus status e mais.

Caso executássemos de novo a aplicação, se criaria outra instância desse Job (de ID 2).

❗Existem também as tabelas dos Steps com suas informações. Em steps complexos, ele pode acabar falhando em alguma etapa,
então podemos saber o possível ponto que esse Step falhou.

# Apresentação Projeto Leitura paginada

Construiremos um Job responsável por fazer uma requisição para uma API remota, buscar esses dados, processá-los e 
gravá-los em um banco de dados (no nosso caso, um banco MySQL).

Esse cenário, é um cenário clássico de integração de dados! (busca, processamento e input dos mesmos).

[Github introdutório](https://github.com/devsuperior/user-request-spring-batch)

[Projeto base](https://github.com/devsuperior/user-request-spring-batch/tree/main/clients/src/main)

❗Lembrar de inserir o docker-compose no escopo do projeto para criação de container.

O projeto acima já tem o repository/service/controller.

Implementaremos o Job para fazer a leitura dos dados (das consultas), para inserir eles no banco de dados.

## Configurando Job

Criar pacote job e a nossa classe JobConfig.

Terá dois parâmetros! JobRepository e o nosso Step.

No tocante ao Step, precisamos pensar: queremos um step que fará 03 etapas muito bem definidas.

A etapa de leitura, ele terá que consultar uma API remota e obter esses dados.

A etapa de processamento, iremos executar um processamento para selecionar campos específicos.

E a etapa de escrita, armazenar no banco de dados.

Seu nome será ``fetchUserDataAndStoreDbStep``! ❗É bom colocar nomes significativos ao criar Steps.

Dentro do método, teremos o JobBuilder com o sempre start (colocando o Step), incrementer e build.

```java
@Configuration
public class JobConfig {
    @Bean
    public Job job(JobRepository jobRepository, Step fetchUserDataAndStoreDBStep) {
        return new JobBuilder("job", jobRepository)
                .start(fetchUserDataAndStoreDBStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
```

## Configurando Step

No último exemplo, o nosso Step foi feito em tasklet (mais simples), dessa vez será em chunks.

Já sabemos que o chunk possui algumas etapas (leitura, processamento e escrita), utilizando o ItemReader, Processor e
Writer.

Eles são injetados no construtor. Só tem um problema, essas interfaces elas possuem um tipo, qual usar?

No caso em pauta, estamos fazendo uma requisição de uma API remota de clients. Se verificarmos o ClientDTO, podemos
observar os seus atributos:

![img_7.png](img_7.png)

Vamos criar uma classe espelho dessa ClientDTO! Ela não precisa ter o mesmo nome, mas precisa ter os mesmos atributos.

Assim, quando formos fazer a leitura, podemos pegar todos os campos dessa API remota a ser consultada.

Só criar um pacote dto e uma classe UserDTO.

Entenda, esse UserDTO será uma cópia fiel dos dados que iremos buscar na API remota.

```java
public class UserDTO {

    private Long id;
    private String login;
    private String name;
    private String location;
    private String avatarUrl;
    private Integer followers;
    private Integer following;
    private Instant createdAt;
    private String reposUrl;
    private String company;
    
    //construtor vazio + com argumentos
    //++ getters and setters e toString
}
```

Agora sim! Voltando para o Step, o ItemReader terá seu tipo de UserDTO.

Para definir o tamanho do chunk, só colocar no application.properties e criar uma variável dentro da classe Step.

Quando formos buscar os dados na API remota, será utilizando o valor do chunkSize (de 10 em 10, por exemplo).

❗O valor do chunk precisa ser em função do tamanho da página a ser pesquisada.

Exemplo: se o Size da URI for 10 e o chunksize for 5, teremos dois chunks.

## Configurando ItemReader (conectando na API remota)

Criaremos uma classe ``FetchUserDataReaderConfig`` no pacote reader. Ela implementará ``ItemReader<UserDTO>``, visto que
já sabemos que o UserDTO será uma cópia dos dados consultados.

Criaremos um método para conectar na API remota.

### RestTemplate

Faz parte do Spring Web, é um cliente http que iremos utilizar e conseguir conectar na API remota que  está na nossa 
máquina (mas poderia estar em qualquer outro servidor da web).

O RestTemplate nos permite usar o ``.exchange()``, dentro dele iremos passar:

1. A Uri (com format, passando o número de página);
2. Método HTTP (neste caso, GET);
3. null;
4. O quarto argumento é o tipo que queremos buscar. No nosso caso, sabemos que a requisição do Postman retorna uma lista
de content (itens estão encapsulados), veja:

![img_11.png](img_11.png)

Faremos uma classe para obter o parâmetro desse content. Ela se irá se chamar ResponseUser (pacote domain).

```java
public class ResponseUser {
    
    //content, pois a lista do Postman tem esse nome
    private List<UserDTO> content;
    
    public List<UserDTO> getContent() {
        return content;
    }
}
```

Voltando para a UserDataReader, passaremos a classe criada no quarto parâmetro da seguinte forma 
``new ParameterizedTypeReference<ResponseUser>() {});``. Após isso, o método fica pronto. Esse RestTemplate com exchange,
retornará um ResponseEntity<ResponseUser>, então alocaremos em uma variável.

Depois, é só criar uma List<UserDTO> e dar um ``response.getBody().getContent();``

![img_12.png](img_12.png)

### Função read

Um chunk terá vários registros, para cada registro (usuário) a função read do ItemReader será chamada.

![img_13.png](img_13.png)

Precisamos criar uma lógica para que enquanto tivermos registro na nossa lista de Users, a gente retorne um objeto User.

Primeiro, vamos criar a lista para armazenar esses usuários.

Depois, criaremos um índice para percorrer a lista. Afinal, quando os dados acabarem (for null), encerraremos.

Resumo:

A função read irá ler os dados da lista de Users (um por um), caso a posição da lista for maior que o userIndex (que
está sendo incrementado), instanciaremos um User.

Quando a posição do index for maior que a lista, retornaremos null.

### Lidando com eventos Spring Batch - Carregando a lista Users

Assim que buscamos os dados na API e sabemos o tamanho do chunk, queremos carregar a nossa lista de users. 

Para isso, iremos trabalhar com eventos que pode nos auxiliar. Quem define o evento, é o **batch!**

Ou seja, assim que chamarmos nosso batch, podemos carregar a nossa lista de Users.

Teremos um evento antes de chamar o chunk (BeforeChunk).

Assim como o chunkSize está definido no properties, coloque também o ``pageSize=10``.

Esse evento ficará no finalzinho da classe, veja:

![img_14.png](img_14.png)

Voltando para a função da API, colocaremos agora o pageSize nela:

![img_15.png](img_15.png)

Agora, teremos outro evento! O AfterChunk, afinal a page precisa ser incrementada a cada chamada.

Criar um método para incrementar a page:

![img_16.png](img_16.png)

AfterChunk

![img_17.png](img_17.png)

