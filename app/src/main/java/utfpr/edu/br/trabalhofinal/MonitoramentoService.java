package utfpr.edu.br.trabalhofinal;

import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class MonitoramentoService {

    private static final String url = "http://ec2-54-158-55-11.compute-1.amazonaws.com:8080";

    private RestTemplate restTemplate;

    public MonitoramentoService() {
        this.restTemplate = new RestTemplate();
    }

    public void enviar(Monitoramento monitoramento) {
        this.restTemplate.postForEntity(url + "/monitoramento", monitoramento, Void.class);
    }

    public List<Ranking> findRanking() {
        Ranking[] rankings = this.restTemplate.getForObject(url + "/ranking", Ranking[].class);
        return Arrays.asList(rankings);
    }
}
