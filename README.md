# PUCFlix - Sistema de Gerenciamento de Atores e Séries

## O que o trabalho faz?

O trabalho implementa um sistema completo para o gerenciamento de **atores** e suas **séries**, simulando um serviço de streaming. O sistema permite:

- Cadastrar, buscar, alterar **atores** e excluir os que não estão vinculados a nenhuma série.
- Cadastrar, buscar, alterar **séries**, vinculando-os aos atores.
- Visualizar **atores por série** e **séries por ator**.
- Garantir a integridade referencial entre atores e séries, utilizando **ID de ator e ID de série** para criar vínculos.
- Utilizar estruturas eficientes de **armazenamento e indexação** com:
  - CRUD genérico com arquivos.
  - **Árvore B+** como índice secundário para o relacionamento N:M entre atores e séries.
  - **Hash Extensível** como índice direto para acesso rápido por ID.


O sistema foi desenvolvido com o objetivo de simular a gestão de atores e suas respectivas séries em um serviço de streaming, utilizando conceitos de **programação orientada a objetos** e **estruturas de dados avançadas**.

---

## Participantes

- Paulo Henrique Lopes de Paula

---

## Experiência e Desenvolvimento

Este trabalho foi um grande aprendizado sobre o gerenciamento de dados e a utilização de estruturas de dados avançadas, como **Árvore B+** e **Hash Extensível**. 

- **Requisitos implementados**: Sim, todos os requisitos foram implementados com sucesso.
- **Maior desafio**: O maior desafio foi integrar corretamente a Árvore B+ com o CRUD genérico e garantir que o relacionamento entre atores e séries fosse consistente.
- **Resultado**: O sistema funciona como esperado, permitindo adicionar, buscar, editar e excluir atores e séries, mantendo a consistência nos vínculos.
- **Aprendizado**: Reforçamos os conceitos de **integridade referencial**, **programação orientada a objetos**, e o uso de **estruturas de dados eficientes** para manipulação de grandes volumes de dados.
- **Depuração e Resolução de Erros**: Ao longo do desenvolvimento, trabalhamos ativamente no diagnóstico e correção de exceções como `NullPointerException`, resultando em um sistema mais confiável e fácil de manter.

---

## Checklist

- [x] As operações de inclusão, busca, alteração e exclusão de atores estão implementadas e funcionando corretamente? **Sim**
- [x] O relacionamento entre séries e atores foi implementado com árvores B+ e funciona corretamente, assegurando a consistência entre as duas entidades? **Sim**
- [x] É possível consultar quais são os atores de uma série? **Sim**
- [x] É possível consultar quais são as séries de um ator? **Sim**
- [x] A remoção de séries remove os seus vínculos de atores? **Sim**
- [x] A inclusão de um ator em uma série em um episódio se limita aos atores existentes? **Sim**
- [x] A remoção de um ator checa se há alguma série vinculada a ele? **Sim**
- [x] O trabalho está funcionando corretamente? **Sim**
- [x] O trabalho está completo? **Sim**
- [x] O trabalho é original e não a cópia de um trabalho de outro grupo? **Sim**

---

## Como executar

1. Clone este repositório:
   ```bash
   git clone https://github.com/PauloHenriqueLopes/TP01Aeds3-Parte2-.git
   cd TP01Aeds3-Parte2-/
