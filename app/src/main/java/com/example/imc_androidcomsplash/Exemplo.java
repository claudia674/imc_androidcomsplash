package com.example.imc_androidcomsplash;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Exemplo extends AppCompatActivity {

    ToggleButton situacao2;
    Switch situacao;
    RadioButton aberto, fechado;
    EditText resultado;

    String situacao3 = "Nada marcado";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exemplo);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Liga os componentes
        situacao = findViewById(R.id.situacao);
        situacao2 = findViewById(R.id.situacao2);
        aberto = findViewById(R.id.aberto);
        fechado = findViewById(R.id.fechado);
        resultado = findViewById(R.id.resultado);

        resultado.setEnabled(false);

        atualizar_resultado();

        // ToggleButton
        situacao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                atualizar_resultado();

            }
        });

        // Switch
        situacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (situacao.isChecked()) {
                    situacao.setText("LIGADO");
                } else {
                    situacao.setText("DESLIGADO");
                }

                atualizar_resultado();

            }
        });

        // RadioButton Aberto
        aberto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                situacao3 = aberto.getText().toString();
                atualizar_resultado();

            }
        });

        // RadioButton Fechado
        fechado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                situacao3 = fechado.getText().toString();
                atualizar_resultado();

            }
        });

    }

    public void atualizar_resultado() {

        resultado.setText(
                "Situação 1 = " + situacao.getText().toString() +
                        "\nSituação 2 = " + situacao2.getText().toString() +
                        "\nSituação 3 = " + situacao3
        );

    }

}