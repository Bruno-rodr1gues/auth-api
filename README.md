# Auth API

API REST de autenticação e autorização de usuários, construída com Java 21 e Spring Boot. A aplicação usa Spring Security com tokens JWT, PostgreSQL para persistência e Flyway para versionar o esquema do banco.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Security
- Spring Data JPA e Hibernate
- PostgreSQL
- Flyway
- Springdoc OpenAPI / Swagger UI
- Maven
- Docker e Docker Compose

## Requisitos

Para executar localmente: Java 21, Docker com Docker Compose e acesso ao Maven (o Maven Wrapper está incluído). Para iniciar a aplicação inteira em containers, basta o Docker com Compose.

## Executar com Docker Compose

Na raiz do projeto, execute:

```bash
docker compose up --build
```

O Compose inicia o PostgreSQL e, depois que o banco passar no health check, constrói e inicia a API. As portas publicadas são `8080` para a API e `5432` para o PostgreSQL. Os dados do banco são mantidos no volume `postgres_data`.

Para iniciar somente o banco e executar a aplicação pela IDE:

```bash
docker compose up -d db
```

Depois, inicie `AuthApiApplication` pela IDE. A configuração local em `application.properties` conecta ao PostgreSQL em `localhost:5432`. Mantenha usuário, senha e nome do banco compatíveis com as configurações do serviço `db` em `compose.yaml`.

Para interromper os serviços:

```bash
docker compose down
```

Esse comando preserva o volume do banco. Para remover também os dados persistidos, use `docker compose down -v`.

## Endereços

Com a aplicação em execução:

- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- Especificação OpenAPI: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- API: `http://localhost:8080`
- PostgreSQL: `localhost:5432`

## Endpoints

As rotas de registro e login são públicas. As demais rotas de teste exigem um token JWT no cabeçalho `Authorization: Bearer <token>`. As rotas de cliente e administrador exigem também a autoridade correspondente.

### Registrar usuário

`POST /users/register`

Cria um usuário e armazena a senha codificada com BCrypt. O campo `role` aceita `ROLE_USER` ou `ROLE_ADMIN`.

**Corpo da requisição:**

```json
{
  "email": "usuario@exemplo.com",
  "password": "senha",
  "role": "ROLE_USER"
}
```

**Resposta de sucesso:** `201 Created`, sem corpo.

> **Espaço para imagem do Swagger:** adicione a captura desta operação abaixo.
>
> <!-- Salve a imagem em docs/images/swagger/register.png e descomente a linha seguinte. -->
> <!-- ![POST /users/register no Swagger](docs/images/swagger/register.png) -->

### Autenticar usuário

`POST /users/login`

Autentica pelo e-mail e senha e retorna um JWT.

**Corpo da requisição:**

```json
{
  "email": "usuario@exemplo.com",
  "password": "senha"
}
```

**Resposta de sucesso:** `200 OK`.

```json
{
  "token": "<jwt>"
}
```

> **Espaço para imagem do Swagger:** adicione a captura desta operação abaixo.
>
> <!-- Salve a imagem em docs/images/swagger/login.png e descomente a linha seguinte. -->
> <!-- ![POST /users/login no Swagger](docs/images/swagger/login.png) -->

### Testar autenticação

`GET /users/test`

Disponível para qualquer usuário autenticado.

**Resposta de sucesso:** `200 OK` — `Autenticado com sucesso`.

> **Espaço para imagem do Swagger:** adicione a captura desta operação abaixo.
>
> <!-- Salve a imagem em docs/images/swagger/test-authenticated.png e descomente a linha seguinte. -->
> <!-- ![GET /users/test no Swagger](docs/images/swagger/test-authenticated.png) -->

### Testar acesso de usuário

`GET /users/test/customer`

Exige a autoridade `ROLE_USER`.

**Resposta de sucesso:** `200 OK` — `Cliente autenticado com sucesso`.

> **Espaço para imagem do Swagger:** adicione a captura desta operação abaixo.
>
> <!-- Salve a imagem em docs/images/swagger/test-customer.png e descomente a linha seguinte. -->
> <!-- ![GET /users/test/customer no Swagger](docs/images/swagger/test-customer.png) -->

### Testar acesso de administrador

`GET /users/test/administrator`

Exige a autoridade `ROLE_ADMIN`.

**Resposta de sucesso:** `200 OK` — `Administrador autenticado com sucesso`.

> **Espaço para imagem do Swagger:** adicione a captura desta operação abaixo.
>
> <!-- Salve a imagem em docs/images/swagger/test-administrator.png e descomente a linha seguinte. -->
> <!-- ![GET /users/test/administrator no Swagger](docs/images/swagger/test-administrator.png) -->

## Fluxo de autenticação

1. Registre um usuário em `POST /users/register`, informando seu papel (`ROLE_USER` ou `ROLE_ADMIN`).
2. Faça login em `POST /users/login` com as mesmas credenciais.
3. Copie o valor de `token` da resposta.
4. Para acessar as rotas protegidas, envie o cabeçalho `Authorization: Bearer <token>`.
5. O token é assinado com HMAC-SHA256 e expira após uma hora.

Exemplo com `curl`:

```bash
curl -X POST http://localhost:8080/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"usuario@exemplo.com","password":"senha"}'
```

Use o token retornado em uma chamada protegida:

```bash
curl http://localhost:8080/users/test \
  -H "Authorization: Bearer <jwt>"
```

## Banco de dados e migrações

As migrações Flyway ficam em `src/main/resources/db/migration` e são executadas na inicialização da aplicação. A migração `V1__create_users_and_roles.sql` cria:

- `users`: identificador, e-mail único e senha codificada.
- `roles`: identificador e nome do papel.
- `users_roles`: associação entre usuários e papéis, com chaves estrangeiras.

O Hibernate está configurado para validar o esquema (`spring.jpa.hibernate.ddl-auto=validate`), não para criá-lo. Novas alterações no esquema devem ser adicionadas em novas migrações Flyway versionadas.

## Estrutura do projeto

```text
src/main/java/com/example/authapi/
├── config/       # Segurança, filtro JWT e configuração OpenAPI
├── controller/   # Endpoints HTTP
├── details/      # Adaptação da entidade para o Spring Security
├── dto/          # Objetos de entrada e saída da API
├── entity/       # Entidades JPA User e Role
├── enums/        # Papéis suportados
├── repository/   # Acesso aos dados
└── service/      # Autenticação, usuários e geração/verificação de JWT
```

## Build e testes

No Windows:

```powershell
.\mvnw.cmd test
```

No Linux ou macOS:

```bash
./mvnw test
```

Para gerar o pacote sem executar os testes, use `.\mvnw.cmd -DskipTests package` no Windows ou `./mvnw -DskipTests package` no Linux/macOS.

## Configuração e segurança

A configuração de conexão local fica em `src/main/resources/application.properties`; a configuração para containers fica em `compose.yaml`. O Compose aceita a variável `POSTGRES_PASSWORD` e usa `authpassword` como valor padrão para desenvolvimento. Defina uma senha própria fora do ambiente local e mantenha-a consistente entre o banco e a aplicação.

Não use segredos de desenvolvimento em produção. A chave de assinatura do JWT deve ser mantida em configuração segura fora do código-fonte e protegida como credencial. O Swagger UI e a especificação OpenAPI estão liberados pela configuração de segurança atual; avalie restringi-los ou desativá-los em produção.
