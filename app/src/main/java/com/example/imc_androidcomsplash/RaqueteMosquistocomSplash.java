package com.example.imc_androidcomsplash;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RaqueteMosquistocomSplash extends AppCompatActivity {

    Button btnCarregar, btnLigar, btnUsar;
    TextView txtCarga, txtStatus;

    int carga = 0;
    boolean ligada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_raquete_mosquistocom_splash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCarregar = findViewById(R.id.btnCarregar);
        btnLigar = findViewById(R.id.btnLigar);
        btnUsar = findViewById(R.id.btnUsar);

        txtCarga = findViewById(R.id.txtCarga);
        txtStatus = findViewById(R.id.txtStatus);

        txtCarga.setText("Carga: 0%");
        txtStatus.setText("Status: Desligada");

        // Carregar
        btnCarregar.setOnClickListener(v -> {
            carga = 100;
            txtCarga.setText("Carga: " + carga + "%");
            txtStatus.setText("Status: Carregada");
        });

        // Ligar
        btnLigar.setOnClickListener(v -> {

            if (carga > 0) {
                ligada = true;
                txtStatus.setText("Status: Ligada");
            } else {
                txtStatus.setText("Status: Carregue a raquete");
            }

        });

        // Usar
        btnUsar.setOnClickListener(v -> {

            if (!ligada) {
                txtStatus.setText("Status: Ligue a raquete");
                return;
            }

            if (carga <= 0) {
                ligada = false;
                txtStatus.setText("Status: Sem carga");
                return;
            }

            carga -= 10;

            txtCarga.setText("Carga: " + carga + "%");

            if (carga == 0) {
                ligada = false;
                txtStatus.setText("Status: Bateria acabou - Raquete desligada");
            } else {
                txtStatus.setText("Status: Raquete em uso");
            }

        });

    }
}