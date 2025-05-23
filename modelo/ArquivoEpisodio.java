package modelo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.ElementoLista;
import aeds3.ListaInvertida;
import aeds3.ParIdId;
import entidades.Episodio;

public class ArquivoEpisodio extends Arquivo<Episodio> {

    Arquivo<Episodio> arqEpisodio;
    ArvoreBMais<ParNomeId> indiceNome;
    ArvoreBMais<ParIdId> indiceSerieEpisodio;
    ListaInvertida listaInvertida;

    public ArquivoEpisodio() throws Exception {
        super("episodio", Episodio.class.getConstructor());
        File diretorio = new File("./dados/episodio");
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
        indiceNome = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "./dados/episodio" + "/indiceNome.db");

        indiceSerieEpisodio = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5,
                "./dados/episodio" + "/indiceSerieEpisodio.db");
        listaInvertida = new ListaInvertida(4, "./dados/dicionario.episodios.db", "./dados/blocos.episodios.db");

    }

    @Override
    public int create(Episodio e) throws Exception {
        int id = super.create(e);
        indiceNome.create(new ParNomeId(e.getName(), id));
        indiceSerieEpisodio.create(new ParIdId(e.getSerieId(), id));

        // Indexar palavras do título na lista invertida
        List<String> tokens = TextProcessor.tokenize(e.getName());
        for (String termo : tokens) {
            float tf = TextProcessor.calculateTf(termo, e.getName());
            listaInvertida.create(termo, new ElementoLista(id, tf));
        }

        return id;
    }

    public Episodio[] buscarPorTermo(String termo) throws Exception {
        // Busca na lista invertida
        ElementoLista[] elementos = listaInvertida.read(termo.toLowerCase());
        if (elementos == null || elementos.length == 0) {
            return new Episodio[0];
        }

        // Ordenar por TF-IDF (já está ordenado na classe ElementoLista)
        Episodio[] episodios = new Episodio[elementos.length];
        for (int i = 0; i < elementos.length; i++) {
            episodios[i] = read(elementos[i].getId());
        }

        return episodios;
    }

    public Episodio[] readNome(String nome) throws Exception {
        if (nome.length() == 0)
            return null;
        ArrayList<ParNomeId> pnis = indiceNome.read(new ParNomeId(nome, -1));

        if (pnis.size() > 0) {
            Episodio[] episodios = new Episodio[pnis.size()];
            int i = 0;
            for (ParNomeId pni : pnis) {
                episodios[i++] = read(pni.getId());
            }
            return episodios;
        } else
            return null;
    }

    public Episodio[] readIdSerie(int idSerie) throws Exception {
        ArrayList<ParIdId> piis = indiceSerieEpisodio.read(new ParIdId(idSerie, -1));
        if (piis.isEmpty()) {
            return null;
        }

        Episodio[] episodios = new Episodio[piis.size()];
        int i = 0;
        for (ParIdId pii : piis) {
            episodios[i++] = read(pii.getId2());
        }
        return episodios;
    }

    public Episodio[] readAll() throws Exception {
        ArrayList<Episodio> episodios = new ArrayList<>();

        int ultimoID = super.ultimoID();

        for (int id = 1; id <= ultimoID; id++) {
            Episodio e = super.read(id);
            if (e != null) {
                episodios.add(e);
            }
        }

        return episodios.toArray(new Episodio[0]);
    }

    @Override
    public boolean delete(int id) throws Exception {
        Episodio e = read(id);
        if (e != null) {
            if (super.delete(id)) {
                indiceNome.delete(new ParNomeId(e.getName(), id));
                indiceSerieEpisodio.delete(new ParIdId(e.getSerieId(), id));

                // Remover da lista invertida
                List<String> tokens = TextProcessor.tokenize(e.getName());
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
    public boolean update(Episodio novoEpisodio) throws Exception {
        Episodio episodioAntigo = read(novoEpisodio.getId());
        if (episodioAntigo != null) {
            if (super.update(novoEpisodio)) {
                // Atualizar índices se necessário
                if (!episodioAntigo.getName().equals(novoEpisodio.getName())) {
                    indiceNome.delete(new ParNomeId(episodioAntigo.getName(), novoEpisodio.getId()));
                    indiceNome.create(new ParNomeId(novoEpisodio.getName(), novoEpisodio.getId()));

                    // Remover termos antigos da lista invertida
                    List<String> tokensAntigos = TextProcessor.tokenize(episodioAntigo.getName());
                    for (String termo : tokensAntigos) {
                        listaInvertida.delete(termo, novoEpisodio.getId());
                    }

                    // Adicionar novos termos na lista invertida
                    List<String> tokensNovos = TextProcessor.tokenize(novoEpisodio.getName());
                    for (String termo : tokensNovos) {
                        float tf = TextProcessor.calculateTf(termo, novoEpisodio.getName());
                        listaInvertida.create(termo, new ElementoLista(novoEpisodio.getId(), tf));
                    }
                }

                // Atualizar índice de série se necessário
                if (episodioAntigo.getSerieId() != novoEpisodio.getSerieId()) {
                    indiceSerieEpisodio.delete(new ParIdId(episodioAntigo.getSerieId(), novoEpisodio.getId()));
                    indiceSerieEpisodio.create(new ParIdId(novoEpisodio.getSerieId(), novoEpisodio.getId()));
                }

                return true;
            }
        }
        return false;
    }

    public ArrayList<Episodio> readPorSerie(int id) throws Exception {
        ArrayList<Episodio> lista = new ArrayList<>();

        // Lê todos os pares da árvore que correspondem à série
        ArrayList<ParIdId> listaPares = indiceSerieEpisodio.read(new ParIdId(id, -1));

        for (ParIdId par : listaPares) {
            Episodio ep = super.read(par.getId2());
            if (ep != null) {
                lista.add(ep);
            }
        }

        return lista;
    }

    public ArrayList<Episodio> readPorSerieETemporada(int id, int temporadaEscolhida) throws Exception {
        ArrayList<Episodio> lista = new ArrayList<>();

        // Lê todos os pares da árvore que correspondem à série
        ArrayList<ParIdId> listaPares = indiceSerieEpisodio.read(new ParIdId(id, -1));

        for (ParIdId par : listaPares) {
            Episodio ep = super.read(par.getId2());
            if (ep != null && ep.getTemporada() == temporadaEscolhida) {
                lista.add(ep);
            }
        }

        return lista;
    }
}
