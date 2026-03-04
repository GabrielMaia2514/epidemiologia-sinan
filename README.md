# 📊 Epidemiologia SINAN - API Analítica

API REST desenvolvida em Java com Spring Boot para análise epidemiológica
com base em dados públicos do SINAN (Sistema de Informação de Agravos de Notificação).

O projeto realiza a importação de dados de doenças como Dengue e Chikungunya,
processa as informações e disponibiliza análises estatísticas por estado.

---

## 🚀 Objetivo do Projeto

Construir uma API capaz de:

- Importar automaticamente dados epidemiológicos (CSV)
- Processar e consolidar informações por estado e ano
- Calcular métricas analíticas relevantes
- Disponibilizar os dados via REST API
- Servir como base para um futuro dashboard web (frontend)

---

## 🧠 Funcionalidades Implementadas

### 📥 Importação de Dados
- Leitura automática de arquivos CSV
- Processamento e normalização dos dados
- Upsert no banco de dados
- Cálculo de taxa por 100 mil habitantes

---

### 📊 Análise por Estado

Endpoint: GET /api/analises//estado-anual/{uf}/{doenca}


Retorna:

- Série temporal de casos por ano
- Pior ano (maior número de casos)
- Crescimento percentual anual
- Maior taxa por 100 mil habitantes

---

## 📦 Exemplo de Resposta JSON

```json
{
  "estado": "São Paulo",
  "doenca": "Dengue",
  "piorAno": 2022,
  "totalPiorAno": 15300,
  "maiorTaxaPor100k": 245.73,
  "serieTemporal": [
    { "ano": 2020, "totalCasos": 4300 },
    { "ano": 2021, "totalCasos": 8100 },
    { "ano": 2022, "totalCasos": 15300 }
  ],
  "crescimentoAnual": [
    {
      "ano": 2021,
      "totalCasos": 8100,
      "percentualCrescimento": 88.37
    },
    {
      "ano": 2022,
      "totalCasos": 15300,
      "percentualCrescimento": 88.89
    }
  ]
}
```

## 🏗️ Arquitetura

O projeto segue arquitetura em camadas:
Controller → Service → Repository → Banco de Dados
Controller → expõe endpoints RES
Service → regra de negócio e cálculos estatísticos
Repository → consultas ao banco
DTOs → modelagem da resposta da API

### 🛠️ Tecnologias Utilizadas

Java 17+
Spring Boot
Spring Data JPA
Hibernate
Lombok
Maven
Banco de dados relacional (PostgreSQL ou similar)

### 📈 Métricas Calculadas

Total de casos por ano
Pior ano epidemiológico
Crescimento percentual anual
Maior taxa por 100 mil habitantes

### 🔒 Tratamento de Robustez

A API trata adequadamente:
Estados inexistentes
Doenças inexistentes
Estados sem dados
Divisão por zero
Valores nulos
Listas vazias
Garantindo sempre respostas consistentes e seguras.

### ✅ Testes
O projeto inclui testes unitários com JUnit 5 e Mockito, cobrindo:
- Cenários sem dados (listas vazias).
- Cenários com dados (cálculo correto dos indicadores).
- Casos de erro (estado ou doença não encontrados).
- Limites (apenas um ano de dados).


### 🧪 Como Executar o Projeto
Pré-requisitos

Java 17 ou superior
Maven
Banco de dados configurado

## Passos

git clone https://github.com/seu-usuario/epidemiologia-sinan.git
cd epidemiologia-sinan
mvn clean install
mvn spring-boot:run

### 🔮 Próximos Passos

Construção de frontend (Dashboard interativo)
Implementação de Swagger/OpenAPI
Adição de testes automatizados
Deploy em ambiente cloud

## 📌 Status do Projeto

🟢 Backend analítico funcional
🟡 Frontend em desenvolvimento
🔵 Projeto em evolução contínua

### 👨‍💻 Autor

Gabriel Maia

Projeto desenvolvido com foco em estudo avançado de backend,
análise de dados e arquitetura de APIs REST.
