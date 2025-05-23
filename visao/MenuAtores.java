package visao;

import java.time.format.DateTimeFormatter;
import java.util.*;
import entidades.Serie;
import entidades.Atores;
import entidades.Episodio;
import modelo.ArquivoAtor;
import modelo.ArquivoSerie;

public class MenuAtores {
    ArquivoAtor arqAtor;
    ArquivoSerie arqSerie;
    private Scanner scanner = new Scanner(System.in);

    public MenuAtores() throws Exception {
        arqAtor = new ArquivoAtor();
        arqSerie = new ArquivoSerie();
    }

    public void menu() {
        int opition;
        do {
            System.out.println("\n\nPUCFlix 1.0");
            System.out.println("-----------");
            System.out.println("> Início > Atores\n");
            System.out.println("1) Adicionar");
            System.out.println("2) Buscar");
            System.out.println("3) Alterar");
            System.out.println("4) Excluir");
            System.out.println("5) Mostrar séries que o ator participa");
            System.out.println("0) Retornar");

            System.out.print("\nOpção: ");
            try {
                opition = Integer.valueOf(scanner.nextLine());
            } catch (Exception e) {
                opition = -1;
            }

            switch (opition) {
                case 1:
                    addAtor();
                    break;
                case 2:
                    buscarAtor();
                    break;
                case 3:
                    alterarAtor();
                    break;
                case 4:
                    excluirAtor();
                    break;
                case 5:
                    mostrarSeries();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opition != 0);
    }

    public void addAtor() {
        System.out.println("\nAdicionar Ator");
        String nome = "";
        int idade = 0;
        char sexo = 'x';
        String idSeries = "";
        boolean dadosCorretos = false;
        ArrayList<Serie> seriesDisponiveis = null;

        // Nome do ator
        do {
            System.out.print("Nome do Ator (mínimo 4 letras): ");
            nome = scanner.nextLine();
            if (nome.length() >= 4)
                dadosCorretos = true;
            else
                System.err.println("O nome do ator deve ter no mínimo 4 caracteres.");
        } while (!dadosCorretos);

        // Idade do ator
        dadosCorretos = false;
        do {
            System.out.print("Idade do Ator: ");
            try {
                idade = Integer.parseInt(scanner.nextLine());
                if (idade >= 6 && idade <= 90) {
                    dadosCorretos = true;
                } else {
                    System.err.println("A idade deve ser entre 6 e 90 anos.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Digite um número válido.");
            }
        } while (!dadosCorretos);

        //sexo
        dadosCorretos = false;
        do {
            System.out.print("Sexo do ator (F/M): ");
            try {
                String sexoStr = scanner.next().toUpperCase();
                if (sexoStr.length() == 1 && (sexoStr.charAt(0) == 'F' || sexoStr.charAt(0) == 'M')) {
                    sexo = sexoStr.charAt(0);
                    dadosCorretos = true;
                    scanner.nextLine();
                } else {
                    System.out.println("Sexo inválido. Digite 'F' ou 'M'.");
                }
            } catch (Exception e) {
                System.out.println("Erro na entrada. Tente novamente.");
                scanner.nextLine();
            }
        } while (!dadosCorretos);


        // Carregar séries
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

        // Exibir séries disponíveis
        System.out.println("Selecione as séries que o ator participa (digite números separados por espaço):");
        for (int i = 0; i < seriesDisponiveis.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + seriesDisponiveis.get(i).getName());
        }

        dadosCorretos = false;
        do {
            System.out.print("Escolha as séries (ex: 1 3 5), ou deixe em branco para não vincular nenhuma série: ");
            String linhaEscolha = scanner.nextLine().trim();
            idSeries = "";

            if (!linhaEscolha.isEmpty()) {
                String[] escolhas = linhaEscolha.split("\\s+");
                boolean peloMenosUmaValida = false;

                for (String escolhaStr : escolhas) {
                    try {
                        int escolha = Integer.parseInt(escolhaStr.trim());
                        if (escolha >= 1 && escolha <= seriesDisponiveis.size()) {
                            int idSerie = seriesDisponiveis.get(escolha - 1).getId();
                            idSeries += idSerie + ",";
                            peloMenosUmaValida = true;
                        } else {
                            System.out.println("Número fora do intervalo: " + escolhaStr);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Número inválido: " + escolhaStr);
                    }
                }

                if (peloMenosUmaValida) {
                    // Remover vírgula final
                    if (idSeries.endsWith(",")) {
                        idSeries = idSeries.substring(0, idSeries.length() - 1);
                    }
                    dadosCorretos = true;
                } else {
                    System.out.println("Nenhuma entrada válida. Tente novamente.");
                }
            } else {
                // Se a entrada estiver em branco, não vincula nenhuma série
                idSeries = "";
                dadosCorretos = true; // Não há erro aqui, já que o ator não será vinculado a nenhuma série.
                System.out.println("Nenhuma série foi vinculada ao ator.");
            }
        } while (!dadosCorretos);

        System.out.println("Séries selecionadas: " + (idSeries.isEmpty() ? "Nenhuma" : idSeries));



        // Confirmação
        System.out.println("\nConfirma a inclusão do ator? (S/N): ");
        char resp = scanner.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                Atores ator = new Atores(idSeries, nome, idade, sexo);
                arqAtor.create(ator);
                System.out.println("Ator incluído com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro de sistema. Não foi possível incluir o ator.");
                e.printStackTrace();
            }
        }
    }

    public void buscarAtor() {
        System.out.println("\nBuscar Ator");
        System.out.print("Nome do ator: ");
        String nome = scanner.nextLine().trim();
    
        try {
            Atores[] ator = arqAtor.readNome(nome);
    
            if (ator == null || ator.length == 0) {
                System.out.println("Nenhum ator encontrado com esse nome.");
                return;
            }
    
            if (ator.length > 1) {
                int n = 1;
                for (Atores a : ator) {
                    System.out.printf("[%d] %s\n", n++, a.getName());
                }
    
                int op = -1;
                do {
                    System.out.print("Escolha um ator: ");
                    try {
                        op = Integer.parseInt(scanner.nextLine());
                        if (op < 1 || op > ator.length) {
                            System.out.println("Número inválido. Escolha entre 1 e " + ator.length);
                            op = -1;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Digite um número.");
                    }
                } while (op == -1);
    
                mostrarAtor(ator[op - 1]);
            } else {
                mostrarAtor(ator[0]);
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar o ator!");
            e.printStackTrace();
        }
    }
    

    public void mostrarAtor(Atores ator) {
        if (ator != null) {
            System.out.println("----------------------");
            System.out.printf("Nome.....: %s\n", ator.getName());
            System.out.printf("Idade....: %d\n", ator.getIdade());
            System.out.println("Sexo.....: " + (ator.getSexo() == 'F' ? "Feminino" : "Masculino"));
            System.out.println("----------------------");
        }
    }
    
    public void alterarAtor() {
        System.out.println("Alterar Ator");
        String nome;
        boolean dadosCorretos = false;
        Atores[] atores = null;

        do {
            System.out.print("Nome do ator que deseja alterar: ");
            nome = scanner.nextLine();
            if(nome.isEmpty()) return;

            if(nome.length() >= 1) {
                dadosCorretos = true;
            } else {
                System.out.println("Nome inválido. O nome do ator deve conter pelo menos 4 caracteres.");
            }
        } while (!dadosCorretos);

        try {
            atores = arqAtor.readNome(nome);
            
            if(atores.length > 0) {
                int op = 0;
                if(atores.length > 1) {
                    int n = 1;
                    for(Atores a : atores) {
                        System.out.println((n++) + ") " + a.getName());
                    }

                    do {
                        System.out.print("Escolha um ator: ");
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
                Atores ator = atores[op-1];

                //Nome
                System.out.print("\nNovo nome do ator (deixe em branco para manter o anterior): ");
                String novoNome = scanner.nextLine();
                if (novoNome.length() >= 4) {
                    ator.setName(novoNome);
                }

                //Idade
                System.out.print("\nNova idade do ator (deixe em branco para manter o anterior): ");
                String idadeStr = scanner.nextLine().trim();
                if (!idadeStr.isEmpty()) {
                    try {
                        int novaIdade = Integer.parseInt(idadeStr);
                        if (novaIdade >= 6 && novaIdade <= 90) {
                            ator.setIdade(novaIdade);
                        } else {
                            System.out.println("Idade fora do intervalo permitido (6 a 90).");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Idade inválida, valor mantido.");
                    }
                }

                //Sexo
                System.out.print("\nNovo sexo do ator (F/M): ");
                String sexoStr = scanner.nextLine().toUpperCase();
                if(!sexoStr.isEmpty()) {
                    try {
                        if (sexoStr.charAt(0) == 'F'|| sexoStr.charAt(0) == 'M') {
                            ator.setSexo(sexoStr.charAt(0));
                        }
                    } catch (Exception e) {
                        System.out.println("Genero inválido, valor mantido.");
                    }
                }

                // Series
                try {
                    ArrayList<Serie> seriesDisponiveis = arqSerie.readAll();

                    if (seriesDisponiveis == null || seriesDisponiveis.isEmpty()) {
                        System.out.println("Nenhuma série cadastrada.");
                    } else {
                        System.out.println("\nSelecione as novas séries para o ator (digite os números separados por espaço):");
                        System.out.println("[0] Nenhuma (desvincular todas)");

                        for (int i = 0; i < seriesDisponiveis.size(); i++) {
                            System.out.println("[" + (i + 1) + "] " + seriesDisponiveis.get(i).getName());
                        }

                        System.out.print("Entrada: ");
                        String entrada = scanner.nextLine().trim();

                        if (!entrada.isEmpty()) {
                            String[] escolhas = entrada.split("\\s+");
                            StringBuilder novaIdSerie = new StringBuilder();
                            boolean desvincularTodas = false;

                            for (String escolha : escolhas) {
                                try {
                                    int indice = Integer.parseInt(escolha);
                                    if (indice == 0) {
                                        desvincularTodas = true;
                                        break; // Ignorar outras seleções
                                    } else if (indice >= 1 && indice <= seriesDisponiveis.size()) {
                                        int idSerie = seriesDisponiveis.get(indice - 1).getId();
                                        novaIdSerie.append(idSerie).append(",");
                                    } else {
                                        System.out.println("Número fora do intervalo: " + indice);
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Valor inválido: " + escolha);
                                }
                            }

                            if (desvincularTodas) {
                                ator.setIdSerie("");
                                System.out.println("Nenhuma série vinculada ao ator.");
                            } else {
                                // Remover vírgula final, se houver
                                if (novaIdSerie.length() > 0 && novaIdSerie.charAt(novaIdSerie.length() - 1) == ',') {
                                    novaIdSerie.setLength(novaIdSerie.length() - 1);
                                }

                                ator.setIdSerie(novaIdSerie.toString());
                            }
                        } else {
                            System.out.println("Entrada em branco. Nenhuma alteração feita nas séries.");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao carregar as séries.");
                    e.printStackTrace();
                }

                //Confirmação
                System.out.print("\nDeseja confirmar as alterações? (S/N): ");
                char resp = scanner.next().charAt(0);
                scanner.nextLine();

                if (resp == 'S' || resp == 's') {
                    boolean alterado = arqAtor.update(ator);
                    if (alterado)
                        System.out.println("Ator alterado com sucesso.");
                    else
                        System.out.println("Erro ao alterar o ator.");
                } else {
                    System.out.println("Alterações canceladas.");
                }

            } else {
                System.out.println("Nenhum ator encontrado com esse nome.");
            }


        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir o ator");
            e.printStackTrace();
        }
    }

    public void excluirAtor() {
        System.out.println("Excluir Ator");
    
        String nome;
        boolean dadosCorretos = false;
    
        do {
            System.out.print("Digite o nome do ator (Mínimo 4 caracteres): ");
            nome = scanner.nextLine();
            if (nome.isEmpty()) return;
            if (nome.length() >= 4) dadosCorretos = true;
            else System.out.println("Nome inválido!");
        } while (!dadosCorretos);
    
        try {
            Atores[] atores = arqAtor.readNome(nome);
            int op = 0;
    
            if (atores == null || atores.length == 0) {
                System.out.println("Ator não encontrado.");
                return;
            }
    
            if (atores.length > 1) {
                int n = 1;
    
                for (Atores a : atores) {
                    System.out.println((n++) + ") " + a.getName());
                }
    
                System.out.println("Escolha o Ator: ");
                do {
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
    
            Atores atorSelecionado = atores[op - 1];
    
            // Verifica se o ator está vinculado a alguma série
            if (atorSelecionado.getIdSerie() != null && !atorSelecionado.getIdSerie().trim().isEmpty()) {
                System.out.println("Este ator está vinculado a uma ou mais séries e não pode ser excluído.");
                return;
            }
    
            mostrarAtor(atorSelecionado);
    
            System.out.print("\nConfirma a exclusão do ator? (S/N) ");
            char resp = scanner.nextLine().charAt(0);
            if (resp == 'S' || resp == 's') {
                boolean excluido = arqAtor.delete(nome);
                if (excluido) {
                    System.out.println("Ator excluído com sucesso.");
                } else {
                    System.out.println("Erro ao excluir o ator.");
                }
            } else {
                System.out.println("Exclusão cancelada.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir o ator.");
            e.printStackTrace();
        }
    }
    

    public void mostrarSeries() {
        System.out.println("Lista de Séries que o ator participa: ");
        String nome;
        boolean dadosCorretos = false;
        
        do {
            System.out.print("Nome do ator: ");
            nome = scanner.nextLine();
            if (nome.isEmpty()) return;
    
            if (nome.length() >= 1) {
                dadosCorretos = true;
            } else {
                System.out.println("Nome inválido. O nome do ator deve conter pelo menos 4 caracteres.");
            }
        } while (!dadosCorretos);
    
        try {
            Atores[] atores = arqAtor.readNome(nome);
    
            if (atores.length > 0) {
                int op = 0;
                if (atores.length > 1) {
                    int n = 1;
                    for (Atores a : atores) {
                        System.out.println((n++) + ") " + a.getName());
                    }
    
                    do {
                        System.out.print("Escolha um ator: ");
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
    
                Atores ator = atores[op - 1];
    
                if (ator.getIdSerie() == null || ator.getIdSerie().trim().isEmpty()) {
                    System.out.println("Este ator não está vinculado a nenhuma série.");
                    return;
                }
    
                String[] ids = ator.getIdSerie().split(",");
                ArrayList<Serie> series = arqSerie.readAll();
    
                ArrayList<String> seriesEncontradas = new ArrayList<>();
                for (String idStr : ids) {
                    try {
                        int id = Integer.parseInt(idStr.trim());
                        for (Serie s : series) {
                            if (s.getId() == id) {
                                seriesEncontradas.add(s.getName());
                                break;
                            }
                        }
                    } catch (NumberFormatException e) {
                    }
                }
    
                if (seriesEncontradas.isEmpty()) {
                    System.out.println("Este ator não está vinculado a nenhuma série existente.");
                } else {
                    System.out.println("\nSéries associadas ao ator:");
                    for (String nomeSerie : seriesEncontradas) {
                        System.out.println("- " + nomeSerie);
                    }
                }
            } else {
                System.out.println("Ator não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar séries do ator!");
            e.printStackTrace();        
        }
    }
       
}
