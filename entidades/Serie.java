package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;

import aeds3.EntidadeArquivo;

public class Serie implements EntidadeArquivo {
    private int id;
    private String name;
    private LocalDate lancamento;
    private String sinopse;
    private String streaming;

    // Construtores
    public Serie() throws Exception {
        this(-1, "", LocalDate.now(), "", "");
    }

    public Serie(String name, LocalDate lancamento, String sinopse, String streaming) throws Exception {
        this(-1, name, lancamento, sinopse, streaming);
    }

    public Serie(int id, String name, LocalDate lancamento, String sinopse, String streaming) throws Exception {
        this.id = id;
        this.name = name;
        this.lancamento = lancamento;
        this.sinopse = sinopse;
        this.streaming = streaming;
    }

    // sets
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLancamento(LocalDate lancamento) {
        this.lancamento = lancamento;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public void setStreaming(String streaming) {
        this.streaming = streaming;
    }

    // geters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getLancamento() {
        return lancamento;
    }

    public String getSinopse() {
        return sinopse;
    }

    public String getStreaming() {
        return streaming;
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeUTF(name);
        dos.writeInt((int) lancamento.toEpochDay());
        dos.writeUTF(sinopse);
        dos.writeUTF(streaming);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] byteArr) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArr);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readInt();
        name = dis.readUTF();
        lancamento = LocalDate.ofEpochDay(dis.readInt());
        sinopse = dis.readUTF();
        streaming = dis.readUTF();
    }

}
