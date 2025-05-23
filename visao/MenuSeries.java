package visao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import aeds3.ParIdId;
import entidades.Atores;
import entidades.Episodio;
import entidades.Serie;
import modelo.ArquivoEpisodio;
import modelo.ArquivoSerie;
import modelo.ArquivoAtor;

public class MenuSeries {
     ArquivoSerie arqSerie;
     ArquivoEpisodio arqEpisodio;
     ArquivoAtor arqAtor;
     private static Scanner scanner = new Scanner(System.in);
     private static final String[] STREAMINGS = {
               "Netflix",
               "Amazon Prime",
               "HBO Max",
               "Disney+",
               "Globoplay",
               "Apple TV+",
               "Paramount+"
     };

     public MenuSeries() throws Exception {
          arqSerie = new ArquivoSerie();
          arqEpisodio = new ArquivoEpisodio();
          arqAtor = new ArquivoAtor();
     }

     public void menu() {
          int opition;
          do {
               System.out.println("\n\nPUCFlix 1.0");
               System.out.println("-----------");
               System.out.println("> Início > Séries\n");
               System.out.println("1) Adicionar");
               System.out.println("2) Buscar");
               System.out.println("3) Alterar");
               System.out.println("4) Excluir");
               System.out.println("5) Listar Séries");
               System.out.println("6) Episódios por Temporada");
               System.out.println("7) Mostrar atores participantes da série");
               System.out.println("0) Retornar");
               System.out.print("\nOpção: ");
               try {
                    opition = Integer.valueOf(scanner.nextLine());
               } catch (Exception e) {
                    opition = -1;
               }
               switch (opition) {
                    case 1:
                         addSerie();
                         break;
                    case 2:
                         buscarSerie();
                         break;
                    case 3:
                         alterarSerie();
                         break;
                    case 4:
                         excluirSerie();
                         break;
                    case 5:
                         listarSeries();
                         break;
                    case 6:
                         listarEpisodioPorTemporada();
                         break;
                    case 7:
                         listarAtores();
                    case 0:
                         break;
                    default:
                         System.out.println("Opção inválida!");
                         break;
               }
          } while (opition != 0);
     }

     public void addSerie() {
          System.out.println("\nAdicionar Série");
          String nome = "";
          LocalDate lancamento = null;
          String sinopse = "";
          String streaming = "";
          boolean dadosCorretos = false;
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

          dadosCorretos = false;
          do {
               System.out.print("Nome da série (Mínimo 3 letras): ");
               nome = scanner.nextLine();
               if (nome.length() >= 3)
                    dadosCorretos = true;
               else
                    System.err.println("O nome da série deve ter no mínimo 3 caracteres.");
          } while (!dadosCorretos);

          dadosCorretos = false;
          do {
               System.out.print("Data de lançamento (DD/MM/YYYY): ");
               String data = scanner.nextLine();
               try {
                    lancamento = LocalDate.parse(data, formatter);
                    dadosCorretos = true;
               } catch (Exception e) {
                    System.err.println("Data inválida! Use o formato (DD/MM/YYYY): ");
               }
          } while (!dadosCorretos);

          dadosCorretos = false;
          do {
               System.out.print("Sinópse: ");
               sinopse = scanner.nextLine();
               if (sinopse.length() > 20 && sinopse.length() < 500)
                    dadosCorretos = true;
               else
                    System.err.println(
                              "Escreva a sinópse dentro dos padrões definidos (mínimo 20, máximo 500 caracteres).");
          } while (!dadosCorretos);

          dadosCorretos = false;
          System.out.println("\nEscolha a plataforma de streaming:");
          for (int i = 0; i < STREAMINGS.length; i++) {
               System.out.printf("[%d] %s%n", i + 1, STREAMINGS[i]);
          }
          int escolha = -1;
          do {
               System.out.print("Opção: ");
               try {
                    escolha = Integer.parseInt(scanner.nextLine());
                    if (escolha >= 1 && escolha <= STREAMINGS.length) {
                         streaming = STREAMINGS[escolha - 1];
                    } else {
                         System.err.println("Escolha inválida.");
                         escolha = -1;
                    }
               } catch (NumberFormatException e) {
                    System.err.println("Digite um número válido.");
               }
          } while (escolha == -1);

          System.out.print("\nConfirma a inclusão da série? (S/N): ");
          char resp = scanner.nextLine().charAt(0);
          if (resp == 'S' || resp == 's') {
               try {
                    Serie s = new Serie(nome, lancamento, sinopse, streaming);
                    arqSerie.create(s);
                    System.out.println("Série incluida com sucesso!");

               } catch (Exception e) {
                    System.out.println("Erro de sistema. Não foi possível incluir a série");
                    e.printStackTrace();
               }
          }
     }

     public void buscarSerie() {
          System.out.println("\nBuscar Série");
          System.out.println("\n\nNome da Série: ");
          String nome = scanner.nextLine();

          if (nome.isEmpty())
               return;

          try {
               Serie[] series = arqSerie.readNome(nome);
               if (series.length > 0) {
                    int n = 1;
                    for (Serie s : series) {
                         System.out.println("[" + (n++) + "] " + s.getName());
                    }
                    System.out.println("Escolha uma Série: ");
                    int op;
                    do {
                         try {
                              op = Integer.valueOf(scanner.nextLine());
                         } catch (NumberFormatException e) {
                              op = -1;
                         }
                         if (op <= 0 || op > n - 1)
                              System.out.println("Escolha um número entre 1 e " + (n - 1));
                    } while (op <= 0 || op > n - 1);
                    mostraSerie(series[op - 1]);
               } else {
                    System.out.println("Nenhuma série encontrada.");
               }
          } catch (Exception e) {
               System.out.println("Erro do sistema. Não foi possível buscar a série!");
               e.printStackTrace();
          }
     }

     public void mostraSerie(Serie serie) {
          if (serie != null) {
               System.out.println("----------------------");
               System.out.printf("Nome.........: %s%n", serie.getName());
               System.out.printf("Sinopse......: %s%n", serie.getSinopse());
               System.out.printf("Streaming....: %s%n", serie.getStreaming());
               System.out.printf("Lançamento...: %s%n",
                         serie.getLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
               System.out.println("----------------------");
          }
     }

     public void alterarSerie() {
          System.out.println("\nAlterção de Série");
          String nome;
          Boolean dadosCorretos;

          dadosCorretos = false;
          do {
               System.out.print("Nome da série (mínimo 3 caractéres): ");
               nome = scanner.nextLine();
               if (nome.isEmpty())
                    return;
               if (nome.length() >= 3)
                    dadosCorretos = true;
               else
                    System.out.println("Nome inválido. O nome da série deve conter pelomenos 3 caractéres.");
          } while (!dadosCorretos);

          try {
               Serie[] serie = arqSerie.readNome(nome);
               if (serie.length > 0) {
                    int op = 0;
                    if (serie.length > 1) {
                         int n = 1;

                         for (Serie s : serie) {
                              System.out.println("[" + (n++) + "] " + s.getName());
                         }

                         System.out.println("Escolha uma Série: ");
                         do {
                              try {
                                   op = Integer.valueOf(scanner.nextLine());
                              } catch (NumberFormatException e) {
                                   op = -1;
                              }
                              if (op <= 0 || op > n - 1)
                                   System.out.println("Escolha um número entre 1 e " + (n - 1));
                         } while (op <= 0 || op > n - 1);
                    } else if (serie.length == 1) {
                         op = 1;
                    }

                    // Alteração Nome
                    String novoNome;
                    dadosCorretos = false;
                    do {
                         System.out.print("\nNovo nome da série (deixe em branco para manter o anterior): ");
                         novoNome = scanner.nextLine();
                         if (!novoNome.isEmpty()) {
                              if (nome.length() >= 3) {
                                   serie[op - 1].setName(novoNome);
                                   dadosCorretos = true;
                              } else
                                   System.err.println("O nome da série deve ter no mínimo 3 caracteres.");
                         } else
                              dadosCorretos = true;
                    } while (!dadosCorretos);
                    // Alteração Data de Lançamento
                    String novaData;
                    dadosCorretos = false;
                    do {
                         System.out.print(
                                   "\nNova data de lançamento da série (DD/MM/YYYY) (deixe em branco para manter o anterior): ");
                         novaData = scanner.nextLine();
                         if (!novaData.isEmpty()) {
                              try {
                                   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                   serie[op - 1].setLancamento(LocalDate.parse(novaData, formatter));
                              } catch (Exception e) {
                                   System.err.println("Data inválida. Valor mantido.");
                              }
                         } else
                              dadosCorretos = true;
                    } while (!dadosCorretos);
                    // Alteração Sinopse
                    String novaSinopse;
                    dadosCorretos = false;
                    do {
                         System.out.print("Nova sinópse da série (deixe em branco para manter o anterior): ");
                         novaSinopse = scanner.nextLine();
                         if (!novaSinopse.isEmpty()) {
                              if (novaSinopse.length() > 20 && novaSinopse.length() < 500) {
                                   serie[op - 1].setSinopse(novaSinopse);
                                   dadosCorretos = true;
                              } else
                                   System.err.println(
                                             "Escreva a sinópse dentro dos padrões definidos (mínimo 20, máximo 500 caracteres).");
                         } else
                              dadosCorretos = true;
                    } while (!dadosCorretos);
                    // Alteração Streaming
                    System.out.println("\nEscolha o novo streaming (ou 0 para manter o atual):");
                    for (int i = 0; i < STREAMINGS.length; i++) {
                         System.out.printf("[%d] %s%n", i + 1, STREAMINGS[i]);
                    }
                    int escolha = -1;
                    do {
                         System.out.print("Opção: ");
                         try {
                              escolha = Integer.parseInt(scanner.nextLine());
                              if (escolha == 0)
                                   break;
                              if (escolha >= 1 && escolha <= STREAMINGS.length) {
                                   serie[op - 1].setStreaming(STREAMINGS[escolha - 1]);
                                   break;
                              } else {
                                   System.err.println("Escolha inválida.");
                              }
                         } catch (NumberFormatException e) {
                              System.err.println("Digite um número válido.");
                         }
                    } while (true);

                    // confirmação
                    System.out.print("\nDeseja confirmar as alterações? (S/N): ");
                    char resp = scanner.next().charAt(0);
                    if (resp == 'S' || resp == 's') {
                         boolean alterado = arqSerie.update(serie[op - 1]);
                         if (alterado)
                              System.out.println("Série alterado com sucesso.");
                         else
                              System.out.println("Erro ao alterar a série.");
                    } else {
                         System.out.println("Alterações canceladas.");
                    }
                    scanner.nextLine();
               } else {
                    System.out.println("Série não encontrada.");
               }
          } catch (Exception e) {
               System.out.println("Erro do sistema. Não foi possuvel buscar as séries!");
               e.printStackTrace();
          }
     }

     public void excluirSerie() {
          System.out.println("\n Exclusão de Série");

          String nome;
          Boolean dadosCorretos;

          dadosCorretos = false;
          do {
               System.out.print("Nome da série (mínimo 3 caractéres): ");
               nome = scanner.nextLine();
               if (nome.isEmpty())
                    return;
               if (nome.length() >= 3)
                    dadosCorretos = true;
               else
                    System.out.println("Nome inválido!");
          } while (!dadosCorretos);

          try {
               Serie[] serie = arqSerie.readNome(nome);
               if (serie.length > 0) {
                    int op = 0;
                    if (serie.length > 1) {
                         int n = 1;

                         for (Serie s : serie) {
                              System.out.println("[" + (n++) + "] " + s.getName());
                         }

                         System.out.println("Escolha uma Série: ");
                         do {
                              try {
                                   op = Integer.valueOf(scanner.nextLine());
                              } catch (NumberFormatException e) {
                                   op = -1;
                              }
                              if (op <= 0 || op > n - 1)
                                   System.out.println("Escolha um número entre 1 e " + (n - 1));
                         } while (op <= 0 || op > n - 1);
                    } else if (serie.length == 1) {
                         op = 1;
                    }
                    Serie serieSelecionada = serie[op - 1];
                    mostraSerie(serie[op - 1]);

                    Episodio[] episodiosVinculados = arqEpisodio.readIdSerie(serieSelecionada.getId());
                    if (episodiosVinculados != null && episodiosVinculados.length > 0) {
                         System.out.println("Esta série possui episódios vinculados e não pode ser excluída.");
                         return;
                    }

                    System.out.print("\nConfirma a exclusão da série? (S/N) ");
                    char resp = scanner.next().charAt(0);
                    scanner.nextLine();
                    if (resp == 'S' || resp == 's') {
                         boolean excluido = arqSerie.delete(nome);
                         if (excluido) {
                              removerAtores(serieSelecionada.getId());
                              System.out.println("Série excluída com sucesso.");
                         } else {
                              System.out.println("Erro ao excluir a Série.");
                         }
                    } else {
                         System.out.println("Exclusão cancelada.");
                    }
               } else {
                    System.out.println("Série não encontrada.");
               }
          } catch (Exception e) {
               System.out.println("Erro do sistema. Não foi possível excluir a série");
               e.printStackTrace();
          }
     }

     public void removerAtores(int idSerieRemovido) {
          try {
               Atores[] atores = arqAtor.readIdSerie(idSerieRemovido);
               if (atores == null || atores.length == 0)
                    return;

               for (Atores ator : atores) {
                    String[] idsSeries = ator.getIdSerie().split(",");
                    StringBuilder novaIdSerie = new StringBuilder();

                    for (String idStr : idsSeries) {
                         if (!idStr.trim().equals(String.valueOf(idSerieRemovido))) {
                              if (novaIdSerie.length() > 0)
                                   novaIdSerie.append(",");
                              novaIdSerie.append(idStr.trim());
                         }
                    }

                    // Atualiza o idSerie do ator
                    ator.setIdSerie(novaIdSerie.toString());

                    // Atualiza o ator no arquivo
                    arqAtor.update(ator);

                    // Remove a relação do índice entre esse ator e a série removida
                    arqAtor.removerIndiceSerie(idSerieRemovido, ator.getId());
               }

          } catch (Exception e) {
               System.out.println("Erro ao remover referência da série nos atores.");
               e.printStackTrace();
          }
     }

     public void listarSeries() {
          System.out.println("\nListar todas as Séries:");
          try {
               ArrayList<Serie> series = arqSerie.readAll();

               if (series.isEmpty()) {
                    System.out.println("Nenhuma série foi encontrada!");
                    return;
               }

               for (Serie serie : series) {
                    mostraSerie(serie);
               }
          } catch (Exception e) {
               System.out.println("Erro ao listar todas as séries.");
               e.printStackTrace();
          }
     }

     private void listarEpisodioPorTemporada() {
          System.out.println("\nListar Episódios por Temporada");

          try {
               // 1. Listar todas as séries
               ArrayList<Serie> series = arqSerie.readAll();

               if (series == null || series.size() == 0) {
                    System.out.println("Nenhuma série cadastrada.");
                    return;
               }

               System.out.println("Séries disponíveis:");
               for (int i = 0; i < series.size(); i++) {
                    System.out.println("[" + (i + 1) + "] " + series.get(i).getName());
               }

               // 2. Escolher uma série
               int index = -1;
               do {
                    System.out.print("Escolha uma série pelo número: ");
                    try {
                         index = Integer.parseInt(scanner.nextLine()) - 1;
                    } catch (NumberFormatException e) {
                         index = -1;
                    }
               } while (index < 0 || index >= series.size());

               Serie serieEscolhida = series.get(index);

               // 3. Buscar episódios da série
               ArquivoEpisodio arqEpisodio = new ArquivoEpisodio();
               ArrayList<Episodio> episodiosDaSerie = arqEpisodio.readPorSerie(serieEscolhida.getId());

               if (episodiosDaSerie.isEmpty()) {
                    System.out.println("Nenhum episódio cadastrado para esta série.");
                    return;
               }

               // 4. Descobrir maior número de temporada
               int maiorTemporada = 0;
               for (Episodio ep : episodiosDaSerie) {
                    if (ep.getTemporada() > maiorTemporada) {
                         maiorTemporada = ep.getTemporada();
                    }
               }

               // 5. Mostrar opções de temporada
               System.out.println("\nTemporadas disponíveis:");
               for (int i = 1; i <= maiorTemporada; i++) {
                    System.out.println("[" + i + "] Temporada " + i);
               }

               // 6. Escolher a temporada
               int temporadaEscolhida = -1;
               do {
                    System.out.print("Escolha a temporada: ");
                    try {
                         temporadaEscolhida = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                         temporadaEscolhida = -1;
                    }
               } while (temporadaEscolhida < 1 || temporadaEscolhida > maiorTemporada);

               // 7. Buscar episódios da temporada
               ArrayList<Episodio> epsTemporada = arqEpisodio.readPorSerieETemporada(serieEscolhida.getId(),
                         temporadaEscolhida);

               if (epsTemporada.isEmpty()) {
                    System.out.println("Nenhum episódio cadastrado para a temporada " + temporadaEscolhida + ".");
               } else {
                    System.out.println("\nEpisódios da temporada " + temporadaEscolhida + ":\n");
                    for (Episodio ep : epsTemporada) {
                         System.out.printf("Nome.........: %s\n", ep.getName());
                         System.out.printf("Temporada....: %d\n", ep.getTemporada());
                         System.out.printf("Lançamento...: %s\n",
                                   ep.getLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                         System.out.printf("Duração......: %d minutos\n", ep.getDuracao());
                         System.out.printf("Sinopse......: %s\n", ep.getSinopse());
                         System.out.println("----------------------");
                    }
               }

          } catch (Exception e) {
               System.err.println("Erro ao listar episódios por temporada.");
               e.printStackTrace();
          }

     }

     public void listarAtores() {
          System.out.println("Listar atores de uma série");
          System.out.print("Nome da série: ");
          String nome = scanner.nextLine().trim();

          try {
               Serie[] seriesEncontradas = arqSerie.readNome(nome);

               if (seriesEncontradas == null || seriesEncontradas.length == 0) {
                    System.out.println("Nenhuma série encontrada com esse nome.");
                    return;
               }

               int i = 0;
               System.out.println("\nSéries encontradas:");
               for (Serie s : seriesEncontradas) {
                    System.out.println("[" + (++i) + "] " + s.getName());
               }

               int escolha = -1;
               boolean entradaValida = false;
               do {
                    System.out.print("Escolha a série pelo número: ");
                    try {
                         escolha = Integer.parseInt(scanner.nextLine().trim());
                         if (escolha >= 1 && escolha <= seriesEncontradas.length) {
                              entradaValida = true;
                         } else {
                              System.out.println("Número fora do intervalo.");
                         }
                    } catch (NumberFormatException e) {
                         System.out.println("Digite um número válido.");
                    }
               } while (!entradaValida);

               int idSerieEscolhida = seriesEncontradas[escolha - 1].getId();

               Atores[] todosAtores = arqAtor.readAll();
               ArrayList<Atores> atoresRelacionados = new ArrayList<>();

               if (todosAtores == null) {
                    System.out.println("todosAtores é null.");
                    return;
               }
               System.out.println("Total de atores carregados: " + todosAtores.length);

               for (Atores ator : todosAtores) {
                    if (ator == null) {
                         System.out.println("Ator nulo encontrado, ignorando.");
                         continue;
                    }

                    System.out.println("Processando ator ID " + ator.getId() + " com idSerie = " + ator.getIdSerie());

                    String idSeriesStr = ator.getIdSerie();
                    if (idSeriesStr == null || idSeriesStr.trim().isEmpty())
                         continue;

                    try {
                         String[] ids = idSeriesStr.split(",");
                         for (String idStr : ids) {
                              if (idStr.trim().equals(String.valueOf(idSerieEscolhida))) {
                                   Atores atorCompleto = arqAtor.read(ator.getId());
                                   if (atorCompleto != null) {
                                        atoresRelacionados.add(atorCompleto);
                                   }
                                   break;
                              }
                         }
                    } catch (Exception e) {
                         System.out.println("Erro ao processar ator ID " + ator.getId());
                         e.printStackTrace();
                    }
               }

               if (atoresRelacionados.isEmpty()) {
                    System.out.println("Nenhum ator associado a essa série.");
                    return;
               }

               System.out.println("\nAtores associados à série '" + seriesEncontradas[escolha - 1].getName() + "':");
               for (Atores ator : atoresRelacionados) {
                    System.out.println("----------------------");
                    System.out.printf("Nome.....: %s\n", ator.getName());
                    System.out.printf("Idade....: %d\n", ator.getIdade());
                    System.out.println("Sexo.....: " + (ator.getSexo() == 'F' ? "Feminino" : "Masculino"));
                    System.out.println("----------------------");
               }

          } catch (Exception e) {
               System.out.println("Erro ao listar atores da série.");
               e.printStackTrace();
          }
     }
}
