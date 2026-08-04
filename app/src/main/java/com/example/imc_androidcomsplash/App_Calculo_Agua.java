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

public class App_Calculo_Agua extends AppCompatActivity {

    EditText edtNome, edtIdade, edtPeso, edtTemperatura;
    Button btnCalcular;
    TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app_calculo_agua);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtNome = findViewById(R.id.edtNome);
        edtIdade = findViewById(R.id.edtIdade);
        edtPeso = findViewById(R.id.edtPeso);
        edtTemperatura = findViewById(R.id.edtTemperatura);

        btnCalcular = findViewById(R.id.btnCalcular);
        txtResultado = findViewById(R.id.txtResultado);

        btnCalcular.setOnClickListener(v -> {

            String nome = edtNome.getText().toString();
            String idade = edtIdade.getText().toString();

            double peso = Double.parseDouble(edtPeso.getText().toString());
            double temperatura = Double.parseDouble(edtTemperatura.getText().toString());

            double mlPorKg;
            String risco;

            if (temperatura <= 32) {
                mlPorKg = 35;
                risco = "Risco baixo de desidratação.";
            } else if (temperatura <= 37) {
                mlPorKg = 45;
                risco = "Risco alto de desidratação.";
            } else {
                mlPorKg = 65;
                risco = "Risco muito alto de desidratação.";
            }

            double aguaMl = peso * mlPorKg;
            double aguaLitros = aguaMl / 1000.0;

            String mensagem =
                    nome + ", você tem " + idade + " anos.\n\n" +
                            "Peso: " + peso + " kg\n" +
                            "Temperatura: " + temperatura + " °C\n\n" +
                            "Você deve beber aproximadamente " +
                            String.format("%.2f", aguaLitros) +
                            " litros de água por dia.\n\n" +
                            risco;

            txtResultado.setText(mensagem);
        });
    }
}