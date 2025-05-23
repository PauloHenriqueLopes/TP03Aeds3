# PUCFlix - Sistema de Gerenciamento de Atores e Séries

## O que o trabalho faz?

O trabalho implementa um sistema de busca eficiente para séries, episódios e atores utilizando índices invertidos. O sistema permite:

- Cadastrar, buscar, alterar e excluir séries, episódios e atores

- Realizar buscas por termos nos títulos/nomes usando lista invertida

- Ordenar os resultados por relevância usando TF-IDF

- Manter a integridade dos relacionamentos entre as entidades

---

## Participantes

- Paulo Henrique Lopes de Paula

---

## Experiência e Desenvolvimento

**Implementamos todos os requisitos solicitados, sendo os principais desafios:**

-Integração da lista invertida com o sistema existente

-Cálculo correto do TF-IDF para ordenação por relevância

-Manutenção da consistência dos índices durante atualizações

-Os resultados foram alcançados após ajustes na tokenização de termos e no armazenamento dos índices. A busca por termos agora é significativamente mais eficiente.

---

## Checklist

- [x] O índice invertido com os termos dos títulos das séries foi criado usando a classe ListaInvertida? **Sim**
- [x] O índice invertido com os termos dos títulos dos episódios foi criado usando a classe ListaInvertida? **Sim**
- [x] O índice invertido com os termos dos nomes dos atores foi criado usando a classe ListaInvertida? **Sim**
- [x] É possível buscar séries por palavras usando o índice invertido? **Sim**
- [x] É possível buscar episódios por palavras usando o índice invertido? **Sim**
- [x] É possível buscar atores por palavras usando o índice invertido? **Sim**
- [x] O trabalho está completo? **Sim**
- [x] O trabalho é original e não a cópia de um trabalho de um colega? **Sim**

---

## Como executar

1. Clone este repositório:
   ```bash
   git clone https://github.com/PauloHenriqueLopes/TP01Aeds3-Parte2-.git
   cd TP01Aeds3-Parte2-/
