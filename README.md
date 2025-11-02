# OndeTáMoto? API – FIAP Challenger (Java)

**OndeTáMoto?** é uma solução IoT desenvolvida para a empresa **Mottu**, especializada em motofrete, com o objetivo de otimizar o controle de entrada, saída e localização de motos dentro da garagem da empresa.

## 🔍 Sobre o Projeto

A dinâmica do sistema é simples, porém poderosa: cada moto da frota é equipada com uma tag inteligente, que funciona como um identificador exclusivo. Assim, toda movimentação é registrada instantaneamente, sem necessidade de intervenção manual.

Esses dados são enviados para um aplicativo mobile, que centraliza todas as informações em uma interface amigável. A equipe da Mottu pode, com poucos toques na tela, visualizar o status de cada moto, saber onde ela está estacionada, identificar quais estão dentro ou fora da garagem e até categorizá-las conforme sua finalidade ou situação atual.
## 📱 Funcionalidades

- Monitoramento em tempo real das motos da garagem
- Visualização via aplicativo mobile
- Identificação das motos com tags inteligentes
- Categorização por status ou função

## 🎥 Link do Vídeo
[Link do Video de Java](https://www.youtube.com/watch?v=nHo1kcqVIB0)

## 🔗 Rotas Pricipais

A API do projeto pode ser acessada via Swagger na rota, ou pela páginas html, e o banco tambem:

- [http://localhost:8081/register](http://localhost:8081/register)
- [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- [http://localhost:8081/h2-console](http://localhost:8081/h2-console)
`JDBC URL:	jdbc:h2:mem:testdb, User Name: sa, Password: deixar em branco`


## ⚠️ Atenção Importante

Crie um **Estabelecimento** antes de criar um **Setor** e crie um **Setor** antes de adicionar uma **Moto**. O ID gerado em um passo é usado no próximo.

## 🔗 Rotas principais:

### 🏍️ Motos
- `GET /api/motos` – Lista todas as motos  
- `POST /api/motos` – Cadastra uma nova moto  
- `GET /api/motos/{id}` – Detalhes de uma moto  
- `DELETE /api/motos/{id}` – Remove uma moto
- `PUT /api/motos/{id}` – Altera uma moto  

---

### 👤 Usuários
- `GET /api/usuarios` – Lista de usuários  
- `POST /api/auth/register` – Cadastro de usuário  
- `GET /api/usuarios/{id}` – Detalhes de um usuário  
- `DELETE /api/usuarios/{id}` – Remove um usuário
- `PUT /api/usuarios/{id}` – Altera um usuário 

---

### 🏢 Estabelecimentos
- `GET /api/estabelecimentos` – Lista estabelecimentos  
- `POST /api/estabelecimentos` – Cadastro de estabelecimento  
- `GET /api/estabelecimentos/{id}` – Detalhes de um estabelecimento  
- `DELETE /api/estabelecimentos/{id}` – Remove um estabelecimento
- `PUT /api/estabelecimentos/{id}` – Altera um estabelecimento 

---

### 🗺️ Setores
- `GET /api/setores` – Lista todos os setores
- `POST /api/setores` – Cadastra um novo setor
- `GET /api/setores/{id}` – Detalhes de um setor
- `DELETE /api/setores/{id}` – Remove um setor
- `PUT /api/setores/{id}` – Altera um setor

---

## Rotas recomendadas para o Teste:
#### Exemplo 1: (Registrar Usuário)

```bash
{
    "email": "henriquechaco@gmail.com",
    "senha": "SenhaForte123",
    "role": "ADMIN"
}
```
#### Exemplo 1.5: (Logar Usuário)

```bash
{
    "email": "henriquechaco@gmail.com",
    "senha": "SenhaForte123"
}
```

#### Exemplo 2: (Criar Estabelecimento)

```bash
{
    "endereco": "Avenida Ale de Vasconcelos 362",
    "usuarioId": 1
}
```

#### Exemplo 3: (Criar Setor)

```bash
{
    "nome": "Ala de Reparos Rápidos",
    "tipo": "MANUTENCAO",
    "tamanho": "Grande",
    "idEstabelecimento": 1
}
```

#### Exemplo 4: (Adicionar Moto)


```bash
{
    "marca": "Honda",
    "placa": "XYZ1234",
    "tag": "MT-01",
    "idSetores": 1
}
```
---

## 🛠️ Tecnologias Utilizadas

- ☕ Java 17
- 🌱 Spring Boot
- 🍃 Thymeleaf
- 🟦 Spring Data JPA
- 🟩 Bean Validation
- 📦 Spring Cache
- 📄 Swagger/OpenAPI
- 🪰 Flyway
- 🛢️ Banco de Dados H2

## 🚀 Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/GuiRomanholi/ondetamoto.git
   cd ondetamoto

## 🧑‍💻 Integrantes do Grupo

- **Guilherme Romanholi Santos - RM557462**
- **Murilo Capristo - RM556794**
- **Nicolas Guinante Cavalcanti - RM557844**

---
