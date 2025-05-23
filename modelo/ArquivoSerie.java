package modelo;

import aeds3.*;

import entidades.Serie;

import java.io.File;
import java.util.ArrayList;

public class ArquivoSerie extends Arquivo<Serie> {

  Arquivo<Serie> arqSeries;
  ArvoreBMais<ParNomeId> indiceNome;

  public ArquivoSerie() throws Exception {
    super("series", Serie.class.getConstructor());
    File diretorio = new File("./dados");
    if (!diretorio.exists()) {
      diretorio.mkdirs();
    }
    indiceNome = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "./dados" + "/indiceNome.db");

  }

  @Override
  public int create(Serie s) throws Exception {
    int id = super.create(s);
    indiceNome.create(new ParNomeId(s.getName(), id));
    return id;
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
        return indiceNome.delete(new ParNomeId(s.getName(), id));
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
    Serie serie = read(novaSerie.getId());

    if (serie != null) {
      if (super.update(novaSerie)) {
        if (!serie.getName().equals(novaSerie.getName())) {
          indiceNome.delete(new ParNomeId(serie.getName(), serie.getId()));
          indiceNome.create(new ParNomeId(novaSerie.getName(), novaSerie.getId()));
        }
        return true;
      }
    }
    return false;
  }

}
