# Epidemiologia SINAN - Backend

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

## Descrição

Backend para coleta, processamento e armazenamento de dados epidemiológicos do SINAN.  
Atualmente suporta as doenças **Dengue** e **Chikungunya**, permitindo:  

- Download automático dos arquivos CSV disponibilizados pelo Ministério da Saúde.
- Processamento e agregação dos casos e óbitos por estado e mês.
- Cálculo de taxa de incidência por 100 mil habitantes.
- Armazenamento em banco de dados relacional com suporte a upsert.

Desenvolvido em **Java** com **Spring Boot**, seguindo boas práticas e pronto para integração com frontend interativo.

---

## Tecnologias

- Java 17+
- Spring Boot
- Spring Data JPA
- PostgreSQL
- OpenCSV
- HTTP Client padrão do Java
- Git & GitHub

---

## Estrutura do Projeto

src/
└─ main/
├─ java/com/seuprojeto/epidemiologia/
│ ├─ entity/ # Entidades JPA
│ ├─ repository/ # Repositórios Spring Data
│ └─ service/ # Lógica de importação e processamento
└─ resources/
└─ application.yml # Configurações do Spring Boot


---

## Funcionalidades Atuais

- `DoencasImportService` — Faz o download, parse e processamento dos CSVs de Dengue e Chikungunya.
- Upsert de indicadores agregados por estado/mês.
- Logging de progresso da importação.
- Controle de exceções para informar problemas ao usuário.

---

## Como Executar

1. Configure o banco de dados PostgreSQL no `application.yml`.
2. Crie as tabelas e entidades necessárias.
3. Execute a aplicação Spring Boot:

```bash
mvn spring-boot:run
POST /import/{doenca}/{ano}
Ex.: /import/dengue/2026

