package utfpr.edu.br.trabalhofinal;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class PrincipalActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, SensorEventListener {

    private ImageView ivImagem;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvId;
    private Button btnIniciar;
    private Button btnParar;
    private TextView tvTempo;
    private TextView tvAceleracao;
    private TextView tvAceleracaoMedia;

    private Timer timer;
    private Integer tempo;

    private GoogleApiClient googleApiClient;
    private GoogleSignInAccount account;

    private Sensor accelerometer;
    private SensorManager sensorManager;
    private Float aceleracao;
    private List<Float> aceleracoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        ivImagem = (ImageView) findViewById(R.id.ivFoto);
        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvId = (TextView) findViewById(R.id.tvId);
        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnParar = (Button) findViewById(R.id.btnParar);
        tvTempo = (TextView) findViewById(R.id.tvTempo);
        tvAceleracao = (TextView) findViewById(R.id.tvAceleracao);
        tvAceleracaoMedia = (TextView) findViewById(R.id.tvAceleracaoMedia);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            this.account = result.getSignInAccount();

            tvName.setText(account.getDisplayName());
            tvEmail.setText(account.getEmail());
            tvId.setText(account.getId());

            Glide.with(this).load(account.getPhotoUrl()).into(ivImagem);

        } else {
            goLogInScreen();
        }
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void btSair(View view) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.not_closse_sessao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        this.aceleracao = event.values[0] + event.values[1] + event.values[2];
        this.aceleracoes.add(this.aceleracao);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void iniciarMonitoramento(View view) {
        this.btnIniciar.setVisibility(View.GONE);
        this.btnParar.setVisibility(View.VISIBLE);
        this.tempo = 0;
        this.aceleracoes = new ArrayList<>();

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tempo += 1;
                Date date = new Date(tempo * 1000L);
                String format = new SimpleDateFormat("HH:mm:ss").format(date);
                tvTempo.setText("Tempo: " + format);

                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);

                tvAceleracao.setText("Acelerômetro atual: " + df.format(aceleracao));
                tvAceleracaoMedia.setText("Acelerômetro média: " + df.format(getMedia()));
            }
        }, 0, 1000);
    }

    private synchronized Float getMedia() {
        Float soma = 0F;
        for (Float aceleracao : aceleracoes) {
            soma += aceleracao;
        }
        return soma / (aceleracoes.size() == 0 ? 1 : aceleracoes.size());
    }

    public void pararMonitoramento(View view) {
        this.btnIniciar.setVisibility(View.VISIBLE);
        this.btnParar.setVisibility(View.GONE);

        final Monitoramento monitoramento = new Monitoramento(account.getDisplayName(), getMedia(), tempo);

        this.timer.cancel();
        this.tempo = 0;
        this.tvTempo.setText("");
        this.tvAceleracao.setText("");
        this.tvAceleracaoMedia.setText("");

        new Runnable() {
            @Override
            public void run() {
                new MonitoramentoService().enviar(monitoramento);
            }
        }.run();
    }

    public void ranking(View view) {
        Intent intent = new Intent(this, RankingActivity.class);
        startActivity(intent);
    }

    public void grupos(View view) {

        Intent intent = new Intent(this, GruposActivity.class);
        startActivity(intent);

    }

    public void criarGrupos(View view) {

        Intent intent = new Intent(this, ListarGrupos.class);
        startActivity(intent);
    }
}
