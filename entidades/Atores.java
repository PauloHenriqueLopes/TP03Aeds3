package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import aeds3.EntidadeArquivo;

public class Atores implements EntidadeArquivo{
    private int id;
    private String idSerie;
    private String name;
    private int idade;
    private char sexo;

    public Atores() throws Exception{
        this(-1, "", "", -1, ' ');
    }
    
    public Atores(String idSerie, String name, int idade, char sexo) throws Exception{
        this(-1, idSerie, name, idade, sexo);
    }

    public Atores(int id, String idSerie, String name, int idade, char sexo) throws Exception{
        this.id = id;
        this.idSerie = idSerie;
        this.name = name;
        this.idade = idade;
        this.sexo = sexo;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setIdSerie(String idSerie) {
        this.idSerie = idSerie;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setIdade(int idade) {
        this.idade = idade;
    }
    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    //getters
    public int getId() {
        return id;
    }
    public String getIdSerie() {
        return idSerie;
    }
    public String getName() {
        return name;
    }
    public int getIdade() {
        return idade;
    }
    public char getSexo() {
        return sexo;
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeUTF(idSerie);
        dos.writeUTF(name);
        dos.writeInt(idade);
        dos.writeChar(sexo);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] byteArr) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArr);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readInt();
        idSerie = dis.readUTF();
        name = dis.readUTF();
        idade = dis.readInt();
        sexo = dis.readChar();
    }
}
