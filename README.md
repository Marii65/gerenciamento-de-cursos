# Gerenciamento de Cursos

Sistema web para gerenciamento de **alunos, cursos e matrículas**, desenvolvido com **Java + Spring Boot** no backend e **HTML, CSS e JavaScript** no frontend.

A aplicação disponibiliza uma API REST para gerenciamento dos dados, autenticação e regras de negócio relacionadas às matrículas.

---

## Tecnologias

### Backend

* Java 21
* Spring Boot 4.1.1
* Spring Web MVC
* Spring Data JPA
* Hibernate
* MySQL
* Lombok
* Bean Validation
* Spring Security
* JWT
* Maven
* Springdoc OpenAPI / Swagger

### Frontend

* HTML5
* CSS3
* JavaScript
* Fetch API

---

## Funcionalidades

### Alunos

* Cadastro, consulta, atualização e exclusão de alunos
* Busca por ID
* Validação dos dados
* E-mail único

### Cursos

* Cadastro, consulta, atualização e exclusão de cursos
* Busca por ID

### Matrículas

* Matrícula de alunos em cursos
* Consulta de matrículas
* Consulta por aluno
* Consulta por curso
* Filtro por status
* Alteração do status da matrícula
* Prevenção de matrículas ativas duplicadas
* Permissão de nova matrícula após cancelamento

### Segurança

* Autenticação utilizando Spring Security
* Autorização baseada em autenticação
* Tokens JWT

### Validações e tratamento de erros

A API utiliza Bean Validation e tratamento global de exceções, com respostas HTTP adequadas para:

* `400 Bad Request` — dados inválidos
* `404 Not Found` — recurso não encontrado
* `409 Conflict` — conflito com regra de negócio

---

## Arquitetura

O backend utiliza arquitetura em camadas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
MySQL
```

### Componentes

* **Controller:** gerenciamento das requisições HTTP.
* **Service:** implementação das regras de negócio.
* **Repository:** acesso e persistência dos dados.
* **DTO:** representação dos dados de entrada e saída da API.
* **Exception Handler:** tratamento centralizado das exceções.
* **Security:** autenticação e autorização da aplicação.

---

## Estrutura do projeto

```text
GerenciamentoDeCursos/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/gerenciamentocursos/
│   │   │   │       ├── controller/
│   │   │   │       ├── dto/
│   │   │   │       ├── entity/
│   │   │   │       ├── exception/
│   │   │   │       ├── repository/
│   │   │   │       ├── security/
│   │   │   │       └── service/
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   ├── test/
│   │   ├── pom.xml
│   │   └── mvnw.cmd
│   │
├── frontend/
│   ├── index.html
│   ├── css/
│   │   └── style.css
│   └── js/
│
└── README.md
```

---

## Pré-requisitos

Para executar o projeto, é necessário ter instalado:

* **Java 21**
* **MySQL 8 ou superior**
* **Git**

O projeto possui Maven Wrapper, portanto não é necessário instalar o Maven separadamente.

O frontend não utiliza Node.js, npm, Vite ou frameworks JavaScript.

---

## Configuração

Crie o banco de dados no MySQL:

```sql
CREATE DATABASE gerenciamento_cursos;
```

No arquivo:

```text
backend/src/main/resources/application.properties
```

configure as credenciais do banco:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gerenciamento_cursos
spring.datasource.username=root
spring.datasource.password=SUA_SENHA

spring.jpa.hibernate.ddl-auto=update
```

---

## Execução

### Ordem recomendada para execução

Para executar o projeto corretamente:

### 1. Inicie o MySQL

Verifique se o servidor MySQL está funcionando.

### 2. Crie o banco

```sql
CREATE DATABASE gerenciamento_cursos;
```

### 3. Configure o `application.properties`

Informe o usuário e a senha do MySQL.

### 4. Inicie o backend

```bash
cd backend
mvnw.cmd spring-boot:run
```

### 5. Inicie o frontend

Abra o `index.html` diretamente ou utilize um servidor local, como o Live Server.

### 6. Acesse o sistema

**Frontend:**

```text
http://127.0.0.1:5500
```

**API:**

```text
http://localhost:8080
```

> A porta do frontend pode variar conforme o servidor local utilizado.

---

## Documentação da API

### Swagger UI

A documentação interativa da API está disponível em:

```text
http://localhost:8080/swagger-ui/index.html
```

Especificação OpenAPI:

```text
http://localhost:8080/v3/api-docs
```

O Swagger permite visualizar os endpoints disponíveis, seus parâmetros, requisições e respostas.

### Javadoc

Para gerar a documentação Javadoc do backend:

```bash
cd backend
mvn javadoc:javadoc
```

Os arquivos gerados ficam em:

```text
backend/target/site/apidocs/
```

O arquivo inicial da documentação é:

```text
backend/target/site/apidocs/index.html
```

---

## Endpoints

### Alunos

| Método | Endpoint           | Descrição          |
| ------ | ------------------ | ------------------ |
| GET    | `/api/alunos`      | Lista alunos       |
| GET    | `/api/alunos/{id}` | Busca aluno por ID |
| POST   | `/api/alunos`      | Cadastra aluno     |
| PUT    | `/api/alunos/{id}` | Atualiza aluno     |
| DELETE | `/api/alunos/{id}` | Exclui aluno       |

### Cursos

| Método | Endpoint           | Descrição          |
| ------ | ------------------ | ------------------ |
| GET    | `/api/cursos`      | Lista cursos       |
| GET    | `/api/cursos/{id}` | Busca curso por ID |
| POST   | `/api/cursos`      | Cadastra curso     |
| PUT    | `/api/cursos/{id}` | Atualiza curso     |
| DELETE | `/api/cursos/{id}` | Exclui curso       |

### Matrículas

| Método | Endpoint          | Descrição         |
| ------ | ----------------- | ----------------- |
| GET    | `/api/matriculas` | Lista matrículas  |
| POST   | `/api/matriculas` | Realiza matrícula |

Também estão disponíveis operações de consulta de matrículas por **aluno, curso e status**.

---

## Regras de negócio

* Um aluno não pode possuir duas matrículas **ATIVAS** para o mesmo curso.
* Uma matrícula **CANCELADA** não impede uma nova matrícula do mesmo aluno no mesmo curso.
* O aluno e o curso devem existir para que uma matrícula seja realizada.
* Os dados recebidos pela API são validados antes da persistência.

---

## Comunicação Frontend / Backend

O frontend utiliza a **Fetch API** para consumir os endpoints REST do Spring Boot.

```text
HTML + CSS + JavaScript
          ↓
      Fetch API
          ↓
   Spring Boot REST API
          ↓
        MySQL
```

---

## Autora

**Maria Luiza**

Desenvolvedora em formação, com foco em desenvolvimento de sistemas e backend Java.

* GitHub: **@Marii65**
* Projeto: **Gerenciamento de Cursos**
