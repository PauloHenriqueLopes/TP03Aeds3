package modelo;

import aeds3.*;
import entidades.Serie;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArquivoSerie extends Arquivo<Serie> {

  Arquivo<Serie> arqSeries;
  ArvoreBMais<ParNomeId> indiceNome;
  ListaInvertida listaInvertida;

  public ArquivoSerie() throws Exception {
    super("series", Serie.class.getConstructor());
    File diretorio = new File("./dados");
    if (!diretorio.exists()) {
      diretorio.mkdirs();
    }
    indiceNome = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "./dados" + "/indiceNome.db");
    listaInvertida = new ListaInvertida(4, "./dados/dicionario.series.db", "./dados/blocos.series.db");
  }

  @Override
  public int create(Serie s) throws Exception {
    int id = super.create(s);
    indiceNome.create(new ParNomeId(s.getName(), id));

    List<String> tokens = TextProcessor.tokenize(s.getName());

    for (String termo : tokens) {
      float tf = TextProcessor.calculateTf(termo, s.getName());
      System.out.println("Adicionando termo '" + termo + "' para série ID " + id + " com TF=" + tf);
      listaInvertida.create(termo, new ElementoLista(id, tf));
      listaInvertida.updateTfidf(termo);
    }

    return id;
  }

  public Serie[] buscarPorTermo(String termo) throws Exception {
    List<String> tokens = TextProcessor.tokenize(termo.toLowerCase());

    if (tokens.isEmpty()) {
      return new Serie[0];
    }

    // Busca apenas pelo primeiro termo para simplificar
    String primeiroTermo = tokens.get(0);

    ElementoLista[] elementos = listaInvertida.read(primeiroTermo);

    if (elementos == null || elementos.length == 0) {
      return new Serie[0];
    }

    Serie[] series = new Serie[elementos.length];
    for (int i = 0; i < elementos.length; i++) {
      series[i] = read(elementos[i].getId());
    }

    return series;
  }

  public Serie[] readNome(String nome) throws Exception {
    if (nome.length() == 0)
      return new Serie[0];

    ArrayList<ParNomeId> pnis = indiceNome.read(new ParNomeId(nome, -1));

    Serie[] series = new Serie[pnis.size()];
    int i = 0;
    for (ParNomeId pni : pnis) {
      series[i++] = read(pni.getId());
    }

    return series;
  }

  public ArrayList<Serie> readAll() throws Exception {
    ArrayList<Serie> lista = new ArrayList<>();

    int id = 0;
    int falhasSeguidas = 0;
    final int LIMITE_FALHAS = 100;

    while (falhasSeguidas < LIMITE_FALHAS) {
      try {
        Serie s = this.read(id);
        if (s != null) {
          lista.add(s);
          falhasSeguidas = 0;
        } else {
          falhasSeguidas++;
        }
      } catch (Exception e) {
        falhasSeguidas++;
      }
      id++;
    }

    return lista;
  }

  @Override
  public boolean delete(int id) throws Exception {
    Serie s = read(id);
    if (s != null) {
      if (super.delete(id)) {
        indiceNome.delete(new ParNomeId(s.getName(), id));

        // Remover da lista invertida
        List<String> tokens = TextProcessor.tokenize(s.getName());
        for (String termo : tokens) {
          listaInvertida.delete(termo, id);
        }
        return true;
      }
    }
    return false;
  }

  public boolean delete(String nome) throws Exception {
    ArrayList<ParNomeId> pnis = indiceNome.read(new ParNomeId(nome, -1));
    if (pnis != null && pnis.size() > 0) {
      return delete(pnis.get(0).getId());
    }
    return false;
  }

  @Override
  public boolean update(Serie novaSerie) throws Exception {
    Serie serieAntiga = read(novaSerie.getId());

    if (serieAntiga != null) {
      if (super.update(novaSerie)) {
        if (!serieAntiga.getName().equals(novaSerie.getName())) {
          indiceNome.delete(new ParNomeId(serieAntiga.getName(), serieAntiga.getId()));
          indiceNome.create(new ParNomeId(novaSerie.getName(), novaSerie.getId()));

          // Remover termos antigos da lista invertida
          List<String> tokensAntigos = TextProcessor.tokenize(serieAntiga.getName());
          for (String termo : tokensAntigos) {
            listaInvertida.delete(termo, novaSerie.getId());
          }

          // Adicionar novos termos na lista invertida
          List<String> tokensNovos = TextProcessor.tokenize(novaSerie.getName());
          for (String termo : tokensNovos) {
            float tf = TextProcessor.calculateTf(termo, novaSerie.getName());
            listaInvertida.create(termo, new ElementoLista(novaSerie.getId(), tf));
          }
        }
        return true;
      }
    }
    return false;
  }

}
