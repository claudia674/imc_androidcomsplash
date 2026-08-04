package com.example.imc_androidcomsplash;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class IMC_Android extends AppCompatActivity {
    EditText edtNome, edtIdade, edtPeso, edtAltura;
    Button btnCalcular;
    TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_imc_android);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Liga os componentes do XML ao Java
        edtNome = findViewById(R.id.edtNome);
        edtIdade = findViewById(R.id.edtIdade);
        edtPeso = findViewById(R.id.edtPeso);
        edtAltura = findViewById(R.id.edtAltura);

        btnCalcular = findViewById(R.id.btnCalcular);
        txtResultado = findViewById(R.id.txtResultado);

        // Evento do botão
        btnCalcular.setOnClickListener(v -> {

            String nome = edtNome.getText().toString();
            int idade = Integer.parseInt(edtIdade.getText().toString());

            double peso = Double.parseDouble(edtPeso.getText().toString());
            double altura = Double.parseDouble(edtAltura.getText().toString());

            double imc = peso / (altura * altura);

            String classificacao;

            if (imc < 18.5) {
                classificacao = "Abaixo do peso";
            } else if (imc < 25) {
                classificacao = "Peso Normal";
            } else if (imc < 30) {
                classificacao = "Sobrepeso";
            } else if (imc < 35) {
                classificacao = "Obesidade Grau I";
            } else if (imc < 40) {
                classificacao = "Obesidade Grau II";
            } else if (imc < 50) {
                classificacao = "Obesidade Grau III";
            } else if (imc < 60) {
                classificacao = "Obesidade Grau IV";
            } else {
                classificacao = "Obesidade Grau V";
            }

            String mensagem =
                    nome + ", você tem " + idade + " anos.\n\n" +
                            "O cálculo do seu IMC é " +
                            String.format("%.2f", imc) +
                            ".\n\nSua classificação é:\n" +
                            classificacao;

            txtResultado.setText(mensagem);
        });
    }
}

