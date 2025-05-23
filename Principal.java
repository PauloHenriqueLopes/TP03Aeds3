import java.util.Scanner;

import visao.MenuAtores;
import visao.MenuEpisodio;
import visao.MenuSeries;

public class Principal {
    public static void main(String[] args) {
        Scanner scanner;
        try {
            scanner = new Scanner(System.in);
            int opition;
            do {
                System.out.println("\n\nPUCFlix 1.0");
                System.out.println("-----------");
                System.out.println("> Início\n");
                System.out.println("1) Séries");
                System.out.println("2) Episódios");
                System.out.println("3) Atores");
                System.out.println("0) Sair");
                System.out.print("\nOpção: ");
                try {
                    opition = Integer.valueOf(scanner.nextLine());
                } catch (NumberFormatException e) {
                    opition = -1;
                }

                switch (opition) {
                    case 1:
                        (new MenuSeries()).menu();
                        break;
                    case 2:
                        (new MenuEpisodio()).menu();
                        break;
                    case 3:
                        (new MenuAtores()).menu();
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            } while (opition != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
