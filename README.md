# Gerenciador de Finanças

O Gerenciador de Finanças é uma aplicação simples desenvolvida com Spring Boot para facilitar o controle de receitas e despesas. Ela permite o cadastro de usuários, gerenciamento de transações e geração de relatórios financeiros. A aplicação é hospedada na Azure utilizando Azure SQL Database para armazenamento de dados e Azure App Service para deployment, proporcionando uma solução escalável e acessível via web.

Para testar a aplicação na nuvem [CLIQUE AQUI](https://gerenciador-de-financas-hgejf8h3cefve8et.brazilsouth-01.azurewebsites.net/swagger-ui/index.html)

## 📋 Funcionalidades
  
1. **Cadastro de Usuários**:  
   - Permite que novos usuários se cadastrem e façam login na aplicação.

2. **Gerenciamento de Transações**:  
   - Adição, visualização, edição e exclusão de transações financeiras (despesas e receitas).

3. **Relatórios**:  
   - Geração de relatórios simples com:
     - Saldo atual.
     - Total de despesas e receitas em períodos específicos.
</br>

<details>
  <summary>🚀 Como Configurar e Executar</summary>

### Pré-requisitos

- **Java 17+**
- **Maven 3.8+**
- **Docker** (opcional para execução via container)
- Conta no **Azure** (caso deseje implantar na nuvem)

### Configuração

1. Clone este repositório:
   ```bash
   git clone https://github.com/seu-usuario/gerenciador-de-financas.git
   cd gerenciador-de-financas

2. **Configure o banco de dados**:
   - O programa atualmente está utilizando o banco de dados que configurei, caso não queira mudar o banco, é só pular esse passo
   - Certifique-se de que o **Azure SQL Database** está configurado corretamente.
   - Atualize as credenciais no arquivo `application.properties` localizado no diretório `src/main/resources/` com os seguintes valores:

     ```properties
     # Configurações do banco de dados
     spring.datasource.url=jdbc:sqlserver://<seu-endereco>.database.windows.net:1433;database=<nome-do-banco>
     spring.datasource.username=<seu-usuario>
     spring.datasource.password=<sua-senha>

     # Configuração de JPA
     spring.jpa.hibernate.ddl-auto=update
     spring.jpa.show-sql=true
     spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
     ```

   - Substitua os placeholders `<seu-endereco>`, `<nome-do-banco>`, `<seu-usuario>` e `<sua-senha>` pelas informações reais de sua configuração no Azure.

   - **Exemplo**:
     ```properties
     spring.datasource.url=jdbc:sqlserver://meuservidor.database.windows.net:1433;database=gerenciador_financas
     spring.datasource.username=adminuser
     spring.datasource.password=minhasenha123
     ```

4. **Compile e execute a aplicação**:  
   ```bash
   mvn spring-boot:run

5. **Acesse a aplicação**:  
   - A aplicação estará disponível localmente em:  
     `http://localhost:8080`
  </br>

## Utilizando DockerHub e containers

  - Eu criei um container dessa aplicação e disponibilizei no [DockerHub](https://hub.docker.com/r/jpedroduarte123/gerenciado-de-financas), para executar a imagem é necessário ter o docker configurado em sua máquina
  - Após isso, basta rodar o seguinte comando para baixar e iniciar o container:
  ```bash
    docker run -d -p 8080:8080 jpedroduarte123/gerenciado-de-financas
  ```
  - A aplicação estará acessível em `http://localhost:8080`.
</br>

## Endpoints Disponíveis

- **Transações**
  - Retornar todas as transações: `GET /api/transacoes`
  - Registrar uma nova transação: `POST /api/transacoes`
  - Retornar uma transação específica: `GET /api/transacoes/{id}`
  - Alterar uma transação: `PUT /api/transacoes/{id}`
  - Alterar parcialmente uma transação: `PATCH /api/transacoes/{id}`
  - Excluir uma transação: `DELETE /api/transacoes/{id}`
  - Gerar relatório de transações: `GET /api/transacoes/relatorio`

- **Usuários**
  - Cadastrar um novo usuário: `POST /api/usuarios`

- **Autenticação**
  - Login: `POST /api/auth/login`
</br>
</details>

<details>
<summary>🔍 Instruções para Utilização dos Endpoints</summary>

### Considerações Gerais
- Todos os endpoints que manipulam transações exigem autenticação. Inclua o cabeçalho `Authorization` com um token válido.
- Para conseguir o token realize um login, ele será retornado como resposta caso login efetuado com sucesso
- As transações só podem ser do tipo:
  - **RECEITA**: Representa um ganho.
  - **DESPESA**: Representa um gasto.
- É obrigatório informar o campo `data` em todos os endpoints que criam ou alteram transações.

---

### **Endpoints de Transações**

### 1. Retornar Todas as Transações
**`GET /api/transacoes`**

- **Cabeçalho Requerido**:
  - `Authorization`: Token de autenticação.
    
- **Respostas**
  - `201` Created: Retorna a transação cadastrada
  - `400` Bad Request: Erro na chamada do endpoint
  - `401` Unauthorized: Token inválido
  - `404` Not Found: Transação não encontrada
</br>


### 2. Registrar uma Nova Transação
**`POST /api/transacoes`**

- **Cabeçalho Requerido**:
  - `Authorization`: Token de autenticação.
- **Corpo da Requisição**:
```json
{
  "tipo": "RECEITA",
  "valor": 100.00,
  "descricao": "Salário",
  "data": "2025-01-01T00:00:00"
}
```
- **Respostas**
  - `201` Created: Retorna a transação cadastrada
  - `400` Bad Request: Erro na chamada do endpoint
  - `401` Unauthorized: Token inválido
  - `404` Not Found: Transação não encontrada

- **Observações**
  - O campo `tipo` aceita apenas os valores `RECEITA` ou `DESPESA`.
  - O valor mínimo permitido é `0.01`.
</br>

### 3. Retornar uma Transação Específica
**`GET /api/transacoes/{id}`**

- **Parâmetros de Rota**:
  - `id`: ID da transação.
- **Cabeçalho Requerido**:
  - `Authorization`: Token de autenticação.
    
- **Respostas**
  - `200` OK: Retorna a transação solicitada
  - `400` Bad Request: Erro na chamada do endpoint
  - `401` Unauthorized: Token inválido
  - `404` Not Found: Transação não encontrada
</br>

### 4. Alterar uma Transação
**`PUT /api/transacoes/{id}`**

- **Parâmetros de Rota**:
  - `id`: ID da transação.
- **Cabeçalho Requerido**:
  - `Authorization`: Token de autenticação.
- **Corpo da Requisição**:
```json
{
  "tipo": "DESPESA",
  "valor": 50.00,
  "descricao": "Compra de material",
  "data": "2025-01-10T12:00:00"
}
```
- **Respostas**
  - `200` OK: Retorna a transação atualizada
  - `400` Bad Request: Erro na chamada do endpoint
  - `401` Unauthorized: Token inválido
  - `404` Not Found: Transação não encontrada
</br>

### 5. Alterar Parcialmente uma Transação
**`PATCH /api/transacoes/{id}`**

- **Parâmetros de Rota**:
  - `id`: ID da transação.
    
- **Cabeçalho Requerido**:
  - `Authorization`: Token de autenticação.

- **Corpo da Requisição (Exemplo):**
```json
{
  "valor": 120.00
}
```
- **Respostas**
  - `200` OK: Retorna a transação atualizada
  - `400` Bad Request: Erro na chamada do endpoint
  - `401` Unauthorized: Token inválido
  - `404` Not Found: Transação não encontrada
</br>

### 6. Excluir uma Transação
**`DELETE /api/transacoes/{id}`**

- **Parâmetros de Rota**:
  - `id`: ID da transação.
- **Cabeçalho Requerido**:
  - `Authorization`: Token de autenticação.

- **Respostas**
  - `204` No Content: Não retorna nada
  - `400` Bad Request: Erro na chamada do endpoint
  - `401` Unauthorized: Token inválido
  - `404` Not Found: Transação não encontrada
</br>

### 7. Gerar relatório
**`GET /api/transacoes/relatorio`**

- **Parâmetros de Query**:
  - `dataInicio`: Data inicial no formato `yyyy-MM-dd`.
  - `dataFim`: Data final no formato `yyyy-MM-dd`
  - Exemplo: `/api/transacoes/relatorio?dataInicio=2024-01-01&dataFim=2026-01-12`
    
- **Cabeçalho Requerido**:
  - `Authorization`: Token de autenticação.

- **Respostas**
  - `200` OK: Retorna o relatório das faturas do período especificado
  - `400` Bad Request: Erro na chamada do endpoint
  - `401` Unauthorized: Token inválido
  - `404` Not Found: Transação não encontrada
</br>
 
---

### **Endpoints de Usuários**

### 1. Cadastrar usuario
**`POST /api/usuarios`**

- **Corpo da Requisição**:
```json
{
  "nome": "Mathues",
  "cpf": "12312312312",
  "email": "matheus@gmail.com",
  "senha": "senha123"
}
```
- **Respostas**
  - `201` Created: Retorna o usuário cadastrado
  - `400` Bad Request: Erro na chamada do endpoint
  - `409` Conflict: E-mail ou CPF já cadastrado no sistema
  
- **Observações**
  - O CPF não pode estar na formatação com `.` e `-` e precisa ser um CPF válido
  - A senha tem que ter entre 8 e 20 caracteres
    
</br>

---

### **Endpoints de Autorização**

### 1. Login
**`POST /api/auth/login`**

- **Corpo da Requisição**:
```json
{
  "email": "matheus@gmail.com",
  "senha": "senha123"
}
```
    
- **Respostas**
  - `200` OK: Retorna o token de autorização
  - `400` Bad Request: Erro na chamada do endpoint
  - `401` Unauthorized: E-mail ou senha inválidos
  - `404` Not Found: Usuário não encontrado
</br>
</details>

<details>
  <summary>:globe_with_meridians: Implantação no Azure App Service</summary>
  
### Criar o Azure App Service:
- Acesse o Azure Portal e vá até App Services.
- Clique em Adicionar e preencha os dados para criar o App Service, como nome, plano de hospedagem e região.
- Após a criação, obtenha o URL do aplicativo, que será utilizado para o deploy.
- Deploy via Azure App Service:
- Para implantar sua aplicação diretamente do repositório GitHub ou de um Docker container, você pode conectar o Azure App Service ao seu repositório GitHub ou configurar a implantação via Docker.

### Implantação via GitHub:
- No portal Azure, dentro do App Service, vá para a seção Deployment Center.
- Escolha GitHub como fonte de código e conecte sua conta do GitHub.
- Escolha o repositório e a branch que deseja implantar.

### Implantação via Docker:
- No App Service, selecione Container Settings e configure para usar uma imagem do Docker disponível no DockerHub
  </br>
</details>

