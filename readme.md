# PersonAPI

Uma API RESTful desenvolvida para o gerenciamento eficiente de cadastros de pessoas, contendo informações detalhadas como endereços, telefones e dados pessoais.

## 🚀 Tecnologias Utilizadas

Este projeto foi construído utilizando as seguintes tecnologias e ferramentas:

* **Java 17**
* **Spring Boot**
* **Spring Data JPA**
* **Hibernate**
* **H2 Database**
* **Maven**
* **Swagger / OpenAPI**

## 📋 Funcionalidades

A API permite realizar operações completas de CRUD (Create, Read, Update, Delete):

* **Cadastrar uma nova pessoa**: Adiciona um registro contendo nome, sobrenome, CPF, data de nascimento, além de múltiplos telefones e endereços.
* **Listar todas as pessoas cadastradas**: Retorna uma lista com todas as pessoas registradas no sistema.
* **Buscar pessoa por ID**: Consulta os detalhes de uma pessoa específica através do seu identificador único.
* **Atualizar dados de uma pessoa**: Modifica informações cadastrais existentes.
* **Deletar uma pessoa**: Remove um registro do sistema.

## ⚙️ Como Executar o Projeto

Siga os passos abaixo para rodar o projeto localmente em sua máquina:

### Pré-requisitos
* **Java Development Kit (JDK)** instalado versão 17.
* **Maven** instalado e configurado nas variáveis de ambiente.
* Uma IDE de sua preferência (IntelliJ IDEA, Eclipse, etc.).

### Passo a passo

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/fxxggah/personapi.git
   ```

2. **Acesse o diretório do projeto:**
   ```bash
   cd personapi
   ```

3. **Execute o projeto utilizando o Maven:**
   ```bash
   mvn spring-boot:run
   ```

4. O servidor iniciará por padrão na porta `8080`. Você pode acessar a aplicação em `http://localhost:8080`.

## 📚 Documentação da API

A documentação interativa da API pode ser acessada via Swagger UI (caso configurada no projeto) através da rota:
* `http://localhost:8080/swagger-ui.html`

## 🛠️ Contribuindo

Contribuições são sempre bem-vindas! Se você encontrou algum bug ou tem uma sugestão de melhoria, siga os passos abaixo:

1. Faça um Fork do projeto.
2. Crie uma Branch para a sua Feature (`git checkout -b feature/MinhaFeature`).
3. Faça o Commit das suas alterações (`git commit -m 'Adicionando nova feature'`).
4. Faça o Push para a Branch (`git push origin feature/MinhaFeature`).
5. Abra um Pull Request.

## 📄 Licença

Este projeto está sob a licença [MIT](LICENSE).