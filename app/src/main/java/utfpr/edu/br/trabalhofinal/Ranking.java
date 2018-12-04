package utfpr.edu.br.trabalhofinal;

import java.io.Serializable;

public class Ranking implements Serializable {

    private String usuario;

    private Float valor;

    private Integer tempo;

    public Ranking(String usuario, Float valor, Integer tempo) {
        this.usuario = usuario;
        this.valor = valor;
        this.tempo = tempo;
    }

    public Ranking() {
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
}
