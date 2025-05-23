package modelo;

import java.io.File;
import java.util.ArrayList;

import aeds3.*;
import entidades.Atores;

public class ArquivoAtor extends Arquivo<Atores> {

    Arquivo<Atores> arqAtores;
    ArvoreBMais<ParNomeId> indiceNome;
    ArvoreBMais<ParIdId> indiceAtorSerie;

    public ArquivoAtor() throws Exception {
        super("atores", Atores.class.getConstructor());
        File diretorio = new File("./dados/atores");
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
        indiceNome = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "./dados/atores/indiceNome.db");
        indiceAtorSerie = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "./dados/atores/indiceAtorSerie.db");
    }

    @Override
    public int create(Atores a) throws Exception {
        int id = super.create(a);
        indiceNome.create(new ParNomeId(a.getName(), id));

        if (a.getIdSerie() != null && !a.getIdSerie().isEmpty()) {
            String[] idsSerie = a.getIdSerie().split(",");
            for (String idSerieStr : idsSerie) {
                idSerieStr = idSerieStr.trim();
                if (!idSerieStr.isEmpty()) {
                    try {
                        int idSerie = Integer.parseInt(idSerieStr);
                        indiceAtorSerie.create(new ParIdId(idSerie, id));
                    } catch (NumberFormatException e) {
                        System.out.println("ID de série inválido: " + idSerieStr);
                    }
                }
            }
        }

        return id;
    }

    public Atores[] readNome(String nome) throws Exception {
        if (nome.length() == 0)
            return null;
        
        ArrayList<ParNomeId> pnis = indiceNome.read(new ParNomeId(nome, -1));
        if (pnis.size() > 0) {
            Atores[] atores = new Atores[pnis.size()];
            int i = 0;
            for (ParNomeId pni : pnis) {
                atores[i++] = read(pni.getId());
            }
            return atores;
        } else
            return null;
    }

    public Atores[] readAll() throws Exception {
        ArrayList<Atores> atores = new ArrayList<>();

        int ultimoID = super.ultimoID();

        for (int id = 1; id <= ultimoID; id++) {
            Atores a = super.read(id);
            if (a != null) {
                atores.add(a);
            }
        }

        return atores.toArray(new Atores[0]);
    }

    public Atores[] readIdSerie(int idSerie) throws Exception {
        ArrayList<ParIdId> par = indiceAtorSerie.read(new ParIdId(idSerie, -1));
    
        if (par.isEmpty()) {
            return null;
        }
    
        Atores[] atores = new Atores[par.size()];
        int i = 0;
    
        for (ParIdId parId : par) {
            Atores ator = read(parId.getId2());
            if (ator != null) {
                atores[i++] = ator;
            }
        }
        
    
        return atores;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Atores a = read(id);
        if (a != null) {
            if (super.delete(id)) {
                boolean okNome = indiceNome.delete(new ParNomeId(a.getName(), id));

                boolean okSerie = true;
                if (a.getIdSerie() != null && !a.getIdSerie().isEmpty()) {
                    String[] idsSerie = a.getIdSerie().split(",");
                    for (String idSerieStr : idsSerie) {
                        idSerieStr = idSerieStr.trim();
                        if (!idSerieStr.isEmpty()) {
                            try {
                                int idSerie = Integer.parseInt(idSerieStr);
                                okSerie &= indiceAtorSerie.delete(new ParIdId(idSerie, id));
                            } catch (NumberFormatException e) {
                                System.out.println("ID de série inválido: " + idSerieStr);
                            }
                        }
                    }
                }

                return okNome && okSerie;
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
    public boolean update(Atores novoAtor) throws Exception {
        Atores antigoAtor = read(novoAtor.getId());
        if (antigoAtor != null) {
            if (super.update(novoAtor)) {
                // Atualiza índice de nome se necessário
                if (!antigoAtor.getName().equals(novoAtor.getName())) {
                    indiceNome.delete(new ParNomeId(antigoAtor.getName(), antigoAtor.getId()));
                    indiceNome.create(new ParNomeId(novoAtor.getName(), novoAtor.getId()));
                }

                // Atualiza índice de séries
                // Remove os antigos
                String[] idsAntigos = antigoAtor.getIdSerie().split(",");
                for (String idStr : idsAntigos) {
                    if (!idStr.trim().isEmpty()) {
                        indiceAtorSerie.delete(new ParIdId(Integer.parseInt(idStr.trim()), antigoAtor.getId()));
                    }
                }

                // Adiciona os novos
                String[] idsNovos = novoAtor.getIdSerie().split(",");
                for (String idStr : idsNovos) {
                    if (!idStr.trim().isEmpty()) {
                        indiceAtorSerie.create(new ParIdId(Integer.parseInt(idStr.trim()), novoAtor.getId()));
                    }
                }

                return true;
            }
        }
        return false;
    }

public void removerIndiceSerie(int idSerie, int idAtor) throws Exception {
    indiceAtorSerie.delete(new ParIdId(idSerie, idAtor));
}

}