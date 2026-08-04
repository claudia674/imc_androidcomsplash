package com.example.imc_androidcomsplash;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Logica_carro extends AppCompatActivity {

    EditText edtCombustivel;
    Button btnAbastecer, btnLigar, btnAumentar, btnReduzir, btnDesligar;
    TextView txtVelocidade, txtStatus;

    int combustivel = 0;
    int velocidade = 0;
    boolean ligado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logica_carro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtCombustivel = findViewById(R.id.edtCombustivel);

        btnAbastecer = findViewById(R.id.btnAbastecer);
        btnLigar = findViewById(R.id.btnLigar);
        btnAumentar = findViewById(R.id.btnAumentar);
        btnReduzir = findViewById(R.id.btnReduzir);
        btnDesligar = findViewById(R.id.btnDesligar);

        txtVelocidade = findViewById(R.id.txtVelocidade);
        txtStatus = findViewById(R.id.txtStatus);

        txtVelocidade.setText("Velocidade: 0 km/h");
        txtStatus.setText("Status: Desligado");

        // Abastecer
        btnAbastecer.setOnClickListener(v -> {

            String valor = edtCombustivel.getText().toString().trim();

            if (valor.isEmpty()) {
                Toast.makeText(Logica_carro.this,
                        "Informe a quantidade de combustível",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            combustivel += Integer.parseInt(valor);

            txtStatus.setText("Status: Carro abastecido");
            edtCombustivel.setText("");

        });

        // Ligar
        btnLigar.setOnClickListener(v -> {

            if (ligado) {
                txtStatus.setText("Status: Carro já está ligado");
            } else if (combustivel > 0) {
                ligado = true;
                txtStatus.setText("Status: Carro ligado");
            } else {
                txtStatus.setText("Status: Sem combustível");
            }

        });
           // Aumentar velocidade
        btnAumentar.setOnClickListener(v -> {

            if (!ligado) {

                txtStatus.setText("Status: Ligue o carro");

            } else if (combustivel <= 0) {

                txtStatus.setText("Status: Sem combustível");

            } else if (velocidade >= 150) {

                txtStatus.setText("Status: Velocidade máxima atingida");

            } else {

                velocidade += 10;
                combustivel--;

                txtVelocidade.setText("Velocidade: " + velocidade + " km/h");

                if (combustivel > 0) {

                    txtStatus.setText("Status: Carro em movimento");

                } else {

                    txtStatus.setText("Status: Combustível acabou");

                }

            }

        });


        // Reduzir velocidade
        btnReduzir.setOnClickListener(v -> {

            if (velocidade > 0) {

                velocidade -= 10;

                txtVelocidade.setText("Velocidade: " + velocidade + " km/h");

                if (velocidade == 0) {
                    txtStatus.setText("Status: Carro parado");
                }

            } else {

                txtStatus.setText("Status: Carro já está parado");

            }

        });

        // Desligar
        btnDesligar.setOnClickListener(v -> {

            if (!ligado) {

                txtStatus.setText("Status: Carro já está desligado");

            } else if (velocidade > 0) {

                txtStatus.setText("Status: Reduza a velocidade para 0 km/h");

            } else {

                ligado = false;
                txtStatus.setText("Status: Carro desligado");

            }

        });

    }
}