package utfpr.edu.br.trabalhofinal;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RankingAdapter extends BaseAdapter {

    private final List<Ranking> list;
    private final Activity act;

    public RankingAdapter(List<Ranking> list, Activity act) {
        this.list = list;
        this.act = act;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.item_list, parent, false);

        Ranking ranking = list.get(i);

        TextView usuario = (TextView) view.findViewById(R.id.usuario);
        TextView acelerometro = (TextView) view.findViewById(R.id.acelerometro);
        TextView tempo = (TextView) view.findViewById(R.id.tempo);

        usuario.setText(ranking.getUsuario());

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        acelerometro.setText(df.format(ranking.getValor()));

        Date date = new Date(ranking.getTempo() * 1000L);
        String format = new SimpleDateFormat("HH:mm:ss").format(date);
        tempo.setText(format);

        return view;
    }

}
