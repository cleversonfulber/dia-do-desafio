package utfpr.edu.br.trabalhofinal;

import java.io.Serializable;
import java.util.Date;

public class Monitoramento implements Serializable {

    private String usuario;

    private Float valor;

    private Integer tempo;

    private Date data;

    public Monitoramento(String usuario, Float valor, Integer tempo) {
        this.usuario = usuario;
        this.valor = valor;
        this.tempo = tempo;
        this.data = new Date();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Integer getTempo() {
        return tempo;
    }

    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
