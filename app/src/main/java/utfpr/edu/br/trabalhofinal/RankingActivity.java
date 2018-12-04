package utfpr.edu.br.trabalhofinal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

public class RankingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        ListView listView = (ListView) findViewById(R.id.listView);

        List<Ranking> ranking = new MonitoramentoService().findRanking();

        RankingAdapter adapter = new RankingAdapter(ranking, this);
        listView.setAdapter(adapter);
    }
}
