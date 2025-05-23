package aeds3;
public interface EntidadeArquivo {
    public void setId(int id);
    public int getId();
    public byte[] toByteArray() throws Exception;
    public void fromByteArray(byte[] vb) throws Exception;
}