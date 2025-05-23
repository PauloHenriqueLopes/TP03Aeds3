package aeds3;

public class ElementoLista implements Comparable<ElementoLista>, Cloneable {
    
    private int id;
    private float tfidf; // Alterando o nome de 'frequencia' para 'tfidf' para melhor semântica

    public ElementoLista(int i, float f) {
        this.id = i;
        this.tfidf = f;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTfidf() {
        return tfidf;
    }

    public void setTfidf(float tfidf) {
        this.tfidf = tfidf;
    }

    @Override
    public String toString() {
        return "("+this.id+";"+this.tfidf+")";
    }

    @Override
    public ElementoLista clone() {
        try {
            return (ElementoLista) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int compareTo(ElementoLista outro) {
        // Ordena por TF-IDF decrescente, depois por ID crescente
        int cmp = Float.compare(outro.tfidf, this.tfidf);
        if (cmp == 0) {
            return Integer.compare(this.id, outro.id);
        }
        return cmp;
    }
}