package visao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import entidades.Episodio;
import entidades.Serie;
import modelo.ArquivoEpisodio;
import modelo.ArquivoSerie;
import modelo.ArquivoAtor;

public class MenuEpisodio {
    ArquivoEpisodio arqEpisodio;
    ArquivoSerie arqSerie;
    ArquivoAtor arqAtor;
    private static Scanner scanner = new Scanner(System.in);

    public MenuEpisodio() throws Exception {
        arqEpisodio = new ArquivoEpisodio();
        arqSerie = new ArquivoSerie();
    }

    public void menu() {
        int opition;
        do {
            System.out.println("\n\nPUCFlix 1.0");
            System.out.println("-----------");
            System.out.println("> Início > Episódios\n");
            System.out.println("1) Adicionar");
            System.out.println("2) Buscar");
            System.out.println("3) Alterar");
            System.out.println("4) Excluir");
            System.out.println("5) Listar Episódios por Série");
            System.out.println("0) Retornar");

            System.out.print("\nOpção: ");
            try {
                opition = Integer.valueOf(scanner.nextLine());
            } catch (Exception e) {
                opition = -1;
            }

            switch (opition) {
                case 1:
                    addEpisodio();
                    break;
                case 2:
                    buscarEpisodio();
                    break;
                case 3:
                    alterarEpisodio();
                    break;
                case 4:
                    excluirEpisodio();
                    break;
                case 5:
                    listarPorSerie();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opition != 0);
    }

    public void addEpisodio() {
        System.out.println("\nAdicionar Episodio");
        int idSerie = 0;
        String nome = "";
        int temporada = 0;
        LocalDate lancamento = null;
        int duracao = 0;
        String sinopse = "";
        boolean dadosCorretos = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Serie[] s = null;

        ArrayList<Serie> seriesDisponiveis = null;

        try {
            seriesDisponiveis = arqSerie.readAll();
        } catch (Exception e) {
            System.out.println("Erro ao carregar as séries.");
            e.printStackTrace();
            return;
        }

        if (seriesDisponiveis == null || seriesDisponiveis.isEmpty()) {
            System.out.println("Nenhuma série cadastrada.");
            return;
        }

        System.out.println("Selecione a série para qual deseja adicionar o episódio:");
        for (int i = 0; i < seriesDisponiveis.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + seriesDisponiveis.get(i).getName());
        }

        int escolha = -1;
        do {
            System.out.print("Digite o número correspondente à série: ");
            try {
                escolha = Integer.parseInt(scanner.nextLine());
                if (escolha < 1 || escolha > seriesDisponiveis.size()) {
                    System.out.println("Escolha inválida. Tente novamente.");
                    escolha = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        } while (escolha == -1);

        Serie serieSelecionada = seriesDisponiveis.get(escolha - 1);
        idSerie = serieSelecionada.getId();
        System.out.println("Série selecionada: " + serieSelecionada.getName());

        do {
            System.out.print("Nome do Episódio(Mínimo 1 letra): ");
            nome = scanner.nextLine();
            if (nome.length() >= 1)
                dadosCorretos = true;
            else
                System.err.println("O nome do episódio deve ter no mínimo 1 caractere.");
        } while (!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Temporada: ");
            temporada = Integer.parseInt(scanner.nextLine());
            if (temporada > 0)
                dadosCorretos = true;
            else
                System.err.println("A temporada deve ser constituida por um número inteiro e positivo.");
        } while (!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Data de lançamento do episódio (DD/MM/YYYY): ");
            String data = scanner.nextLine();
            try {
                lancamento = LocalDate.parse(data, formatter);
                if (lancamento.isBefore(serieSelecionada.getLancamento())) {
                    System.err.println("A data do episódio não pode ser anterior à data de lançamento da série: "
                        + serieSelecionada.getLancamento().format(formatter));
                } else {
                    dadosCorretos = true;
                }
            } catch (Exception e) {
                System.err.println("Data inválida! Use o formato (DD/MM/YYYY): ");
            }
        } while (!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Duração em minutos: ");
            duracao = Integer.parseInt(scanner.nextLine());
            if (duracao > 0)
                dadosCorretos = true;
            else
                System.err.println("A Duração deve ser constituida por um número inteiro e positivo.");
        } while (!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Sinópse: ");
            sinopse = scanner.nextLine();
            if (sinopse.length() > 20 && sinopse.length() < 500)
                dadosCorretos = true;
            else
                System.err
                        .println("Escreva a sinópse dentro dos padrões definidos (mínimo 20, máximo 500 caracteres).");
        } while (!dadosCorretos);

        System.out.println("\nConfirma a inclusão do episódio? (S/N): ");
        char resp = scanner.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                Episodio episodio = new Episodio(idSerie, nome, temporada, lancamento, duracao, sinopse);
                arqEpisodio.create(episodio);
                System.out.println("Episódio incluido com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro de sistema. Não foi possível incluir o episódio");
            }
        }
    }

    public void buscarEpisodio() {
        System.out.println("\nBuscar Episódio:");
        System.out.print("Nome do episódio: ");
        String nome = scanner.nextLine().trim();
    
        try {
            Episodio[] episodios = arqEpisodio.readNome(nome);
    
            if (episodios == null || episodios.length == 0) {
                System.out.println("Nenhum episódio encontrado com esse nome.");
                return;
            }
    
            int op = 0;
            if (episodios.length > 1) {
                int n = 1;
                for (Episodio e : episodios) {
                    System.out.printf("%d) %s (Temporada %d)\n", n++, e.getName(), e.getTemporada());
                }
                System.out.print("Escolha um episódio: ");
                do {
                    try {
                        op = Integer.parseInt(scanner.nextLine());
                    } catch (Exception e) {
                        op = -1;
                    }
                    if (op <= 0 || op > episodios.length)
                        System.out.println("Escolha um número entre 1 e " + episodios.length);
                } while (op <= 0 || op > episodios.length);
    
                mostrarEpisodio(episodios[op - 1]);
    
            } else {
                mostrarEpisodio(episodios[0]);
            }
    
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar o episódio!");
            e.printStackTrace();
        }
    }

    public void mostrarEpisodio(Episodio episodio) {
        if (episodio != null) {
            System.out.println("----------------------");
            try {
                Serie s = arqSerie.read(episodio.getSerieId());
                System.out.printf("Série........: %s\n", (s != null ? s.getName() : "Série não encontrada"));
            } catch (Exception e) {
                System.out.println("Erro: nao foi possível buscar a série do episodio.");
            }
            System.out.printf("Nome.........: %s\n", episodio.getName());
            System.out.printf("Temporada....: %d\n", episodio.getTemporada());
            System.out.printf("lancamento...: %s\n",
                    episodio.getLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.printf("Duracao......: %d\n", episodio.getDuracao());
            System.out.printf("Sinopse......: %s\n", episodio.getSinopse());
            System.out.println("----------------------");
        }
    }

    public void alterarEpisodio() {
        System.out.println("Alteração de Episódio");
        String nome;
        boolean dadosCorretos;
    
        dadosCorretos = false;
        do {
            System.out.print("Nome do episódio (mínimo 1 caractere): ");
            nome = scanner.nextLine();
            if (nome.isEmpty()) return;
    
            if (nome.length() >= 1)
                dadosCorretos = true;
            else
                System.out.println("Nome inválido. O nome do episódio deve conter pelo menos 1 caractere.");
        } while (!dadosCorretos);
    
        try {
            Episodio[] episodios = arqEpisodio.readNome(nome);
            if (episodios.length > 0) {
                int op = 0;
                if (episodios.length > 1) {
                    int n = 1;
                    for (Episodio ep : episodios)
                        System.out.println((n++) + ") " + ep.getName());
    
                    do {
                        System.out.print("Escolha um episódio: ");
                        try {
                            op = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            op = -1;
                        }
                        if (op <= 0 || op > n - 1)
                            System.out.println("Escolha um número entre 1 e " + (n - 1));
                    } while (op <= 0 || op > n - 1);
                } else {
                    op = 1;
                }
    
                Episodio episodio = episodios[op - 1];
                Serie serie = arqSerie.read(episodio.getSerieId()); // Pegando a série do episódio
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
                // Nome
                System.out.print("\nNovo nome do episódio (deixe em branco para manter o anterior): ");
                String novoNome = scanner.nextLine();
                if (!novoNome.isEmpty() && novoNome.length() >= 1)
                    episodio.setName(novoNome);
    
                // Temporada
                System.out.print("\nNova temporada da série (deixe em branco para manter o anterior): ");
                String novaTemporadaStr = scanner.nextLine();
                if (!novaTemporadaStr.isEmpty()) {
                    try {
                        int novaTemporada = Integer.parseInt(novaTemporadaStr);
                        if (novaTemporada > 0)
                            episodio.setTemporada(novaTemporada);
                        else
                            System.out.println("A temporada deve ser um número inteiro e positivo.");
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido. Temporada mantida.");
                    }
                }
    
                // Data de lançamento
                dadosCorretos = false;
                do {
                    System.out.print("\nNova data de lançamento do episódio (DD/MM/YYYY) (deixe em branco para manter): ");
                    String novaData = scanner.nextLine();
                    if (!novaData.isEmpty()) {
                        try {
                            LocalDate novaDataLancamento = LocalDate.parse(novaData, formatter);
                            if (novaDataLancamento.isBefore(serie.getLancamento())) {
                                System.err.println("A data do episódio não pode ser anterior à data da série: " +
                                    serie.getLancamento().format(formatter));
                            } else {
                                episodio.setLancamento(novaDataLancamento);
                                dadosCorretos = true;
                            }
                        } catch (Exception e) {
                            System.err.println("Data inválida! Use o formato (DD/MM/YYYY).");
                        }
                    } else {
                        dadosCorretos = true; // Mantém a data original
                    }
                } while (!dadosCorretos);
    
                // Duração
                System.out.print("\nNova duração do episódio (deixe em branco para manter o anterior): ");
                String novaDuracaoStr = scanner.nextLine();
                if (!novaDuracaoStr.isEmpty()) {
                    try {
                        int novaDuracao = Integer.parseInt(novaDuracaoStr);
                        if (novaDuracao > 0)
                            episodio.setDuracao(novaDuracao);
                        else
                            System.out.println("A duração deve ser um número inteiro e positivo.");
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido. Duração mantida.");
                    }
                }
    
                // Sinopse
                dadosCorretos = false;
                do {
                    System.out.print("Nova sinópse do episódio (deixe em branco para manter o anterior): ");
                    String novaSinopse = scanner.nextLine();
                    if (!novaSinopse.isEmpty()) {
                        if (novaSinopse.length() > 20 && novaSinopse.length() < 500) {
                            episodio.setSinopse(novaSinopse);
                            dadosCorretos = true;
                        } else {
                            System.err.println("Escreva a sinópse dentro dos padrões (mín. 20, máx. 500 caracteres).");
                        }
                    } else {
                        dadosCorretos = true; // Mantém
                    }
                } while (!dadosCorretos);
    
                // Confirmação
                System.out.print("\nDeseja confirmar as alterações? (S/N): ");
                char resp = scanner.next().charAt(0);
                scanner.nextLine(); // Consome o enter
    
                if (resp == 'S' || resp == 's') {
                    boolean alterado = arqEpisodio.update(episodio);
                    if (alterado)
                        System.out.println("Episódio alterado com sucesso.");
                    else
                        System.out.println("Erro ao alterar o episódio.");
                } else {
                    System.out.println("Alterações canceladas.");
                }
    
            } else {
                System.out.println("Nenhum episódio encontrado com esse nome.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar os episódios.");
            e.printStackTrace();
        }
    }
    

    public void excluirEpisodio() {
        System.out.println("Exclusão de Episódio");

        String nome;
        Boolean dadosCorretos;

        dadosCorretos = false;
        do {
            System.out.println("Nome do episódio (Mínimo 1 caractere): ");
            nome = scanner.nextLine();
            if (nome.isEmpty())
                return;
            if (nome.length() >= 1)
                dadosCorretos = true;
            else
                System.out.println("Nome inválido!");
        } while (!dadosCorretos);

        try {
            Episodio[] episodio = arqEpisodio.readNome(nome);
            int op = 0;

            if (episodio == null || episodio.length == 0) {
                System.out.println("Episódio não encontrado.");
                return;
            }

            if (episodio.length > 1) {
                int n = 1;

                for (Episodio p : episodio) {
                    System.out.println((n++) + ") " + p.getName());
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
            } else if (episodio.length == 1) {
                op = 1;
            }
            mostrarEpisodio(episodio[op - 1]);

            System.out.print("\nConfirma a exclusão do episódio? (S/N) ");
            char resp = scanner.nextLine().charAt(0);
            if (resp == 'S' || resp == 's') {
                boolean excluido = arqEpisodio.delete(nome);
                if (excluido) {
                    System.out.println("Episódio excluída com sucesso.");
                } else {
                    System.out.println("Erro ao excluir o episódio.");
                }
            } else {
                System.out.println("Episódio não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir o episódio");
            e.printStackTrace();
        }
    }

    public void listarPorSerie() {
        System.out.println("\nListar Episódios por Série:");
    
        try {
            ArrayList<Serie> series = arqSerie.readAll();
    
            if (series == null || series.size() == 0) {
                System.out.println("Nenhuma série cadastrada.");
                return;
            }
    
            // Listar séries
            int n = 1;
            for (Serie s : series) {
                System.out.printf("[%d] %s\n", n++, s.getName());
            }
    
            // Escolha do usuário
            System.out.print("Escolha uma série: ");
            int escolha = 0;
            do {
                try {
                    escolha = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    escolha = -1;
                }
    
                if (escolha <= 0 || escolha > series.size()) {
                    System.out.println("Escolha um número entre 1 e " + series.size());
                }
    
            } while (escolha <= 0 || escolha > series.size());
    
            Serie serieEscolhida = series.get(escolha - 1);
            System.out.printf("\nEpisódios da série \"%s\":\n", serieEscolhida.getName());
    
            // Buscar episódios dessa série
            Episodio[] episodios = arqEpisodio.readAll();
            boolean encontrou = false;
            for (Episodio e : episodios) {
                if (e.getSerieId() == serieEscolhida.getId()) {
                    mostrarEpisodio(e);
                    encontrou = true;
                }
            }
    
            if (!encontrou) {
                System.out.println("Nenhum episódio encontrado para essa série.");
            }
    
        } catch (Exception e) {
            System.out.println("Erro ao listar episódios por série.");
            e.printStackTrace();
        }
    }
    
}
