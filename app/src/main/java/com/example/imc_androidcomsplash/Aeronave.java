package com.example.imc_androidcomsplash;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Aeronave extends AppCompatActivity {

    private Button btnLigar;
    private Button btnCheckList;
    private Button btnAutorizacaoDecolar;
    private Button btnSubir;
    private Button btnAutorizacaoDescer;
    private Button btnDescer;
    private Button btnDesligar;

    private EditText txtStatus;

    // Estados da aeronave
    private boolean ligada = false;
    private boolean checklist = false;
    private boolean autorizacaoDecolar = false;
    private boolean autorizacaoPouso = false;

    // Altitude
    private int altitude = 0;

    private final int PASSO = 10000;
    private final int ALTITUDE_MAXIMA = 40000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeronave);

        btnLigar = findViewById(R.id.btnLigar);
        btnCheckList = findViewById(R.id.btnCheckList);
        btnAutorizacaoDecolar = findViewById(R.id.btnAutorizacaoDecolar);
        btnSubir = findViewById(R.id.btnSubir);
        btnAutorizacaoDescer = findViewById(R.id.btnAutorizacaoDescer);
        btnDescer = findViewById(R.id.btnDescer);
        btnDesligar = findViewById(R.id.btnDesligar);

        txtStatus = findViewById(R.id.txtStatus);
        txtStatus.setMovementMethod(new ScrollingMovementMethod());

        adicionarStatus("Sistema iniciado.");

        // ==========================
        // LIGAR
        // ==========================
        btnLigar.setOnClickListener(v -> {
            if (!ligada) {
                ligada = true;
                adicionarStatus("Aeronave ligada.");
                Toast.makeText(this, "Aeronave ligada.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "A aeronave já está ligada.", Toast.LENGTH_SHORT).show();
            }
        });

        // ==========================
        // CHECK LIST
        // ==========================
        btnCheckList.setOnClickListener(v -> {
            if (!ligada) {
                Toast.makeText(this, "Ligue a aeronave primeiro.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checklist) {
                checklist = true;
                adicionarStatus("Check List realizado.");
                Toast.makeText(this, "Check List concluído.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Check List já realizado.", Toast.LENGTH_SHORT).show();
            }
        });

        // ==========================
        // AUTORIZAÇÃO DE DECOLAGEM
        // ==========================
        btnAutorizacaoDecolar.setOnClickListener(v -> {
            if (!ligada) {
                Toast.makeText(this, "Ligue a aeronave primeiro.", Toast.LENGTH_SHORT).show();
                return;
            }

            autorizacaoDecolar = true;
            adicionarStatus("Torre autorizou a decolagem.");
            Toast.makeText(this, "Autorização concedida.", Toast.LENGTH_SHORT).show();
        });

        // ==========================
        // SUBIR
        // ==========================
        btnSubir.setOnClickListener(v -> {
            if (!ligada) {
                Toast.makeText(this, "Ligue a aeronave primeiro.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checklist) {
                Toast.makeText(this, "Realize o Check List.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!autorizacaoDecolar) {
                Toast.makeText(this, "Solicite autorização para decolar.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (altitude >= ALTITUDE_MAXIMA) {
                Toast.makeText(this, "A aeronave já está na altitude máxima.", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                while (altitude < ALTITUDE_MAXIMA) {
                    altitude += 1000;

                    final int altitudeAtual = altitude;

                    runOnUiThread(() -> {
                        adicionarStatus("Subindo... Altitude atual: " + altitudeAtual + " pés");
                    });

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                runOnUiThread(() -> {
                    adicionarStatus("Altitude máxima atingida: 40.000 pés.");
                    Toast.makeText(Aeronave.this,
                            "Aeronave chegou a 40.000 pés.",
                            Toast.LENGTH_SHORT).show();
                });
            }).start();
        });

        // ==========================
        // AUTORIZAÇÃO PARA POUSAR
        // ==========================
        btnAutorizacaoDescer.setOnClickListener(v -> {
            if (altitude == ALTITUDE_MAXIMA) {
                autorizacaoPouso = true;
                adicionarStatus("Torre autorizou o pouso.");
                Toast.makeText(this, "Autorização para pousar concedida.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Primeiro atinja 40.000 pés.", Toast.LENGTH_SHORT).show();
            }
        });

        // ==========================
        // DESCER
        // ==========================
        btnDescer.setOnClickListener(v -> {
            if (!autorizacaoPouso) {
                Toast.makeText(this, "Solicite autorização para pousar.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (altitude > 0) {
                altitude -= PASSO;
                adicionarStatus("Descendo... Altitude atual: " + altitude + " pés.");
                Toast.makeText(this, "Altitude: " + altitude + " pés.", Toast.LENGTH_SHORT).show();

                if (altitude == 0) {
                    adicionarStatus("A aeronave pousou.");
                    adicionarStatus("Aeronave em solo.");
                    Toast.makeText(this, "Pouso realizado com sucesso.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "A aeronave já está em solo.", Toast.LENGTH_SHORT).show();
            }
        });

        // ==========================
        // DESLIGAR
        // ==========================
        btnDesligar.setOnClickListener(v -> {
            if (!ligada) {
                Toast.makeText(this, "A aeronave já está desligada.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (altitude > 0) {
                Toast.makeText(this, "Não é possível desligar durante o voo.", Toast.LENGTH_SHORT).show();
                return;
            }

            ligada = false;
            checklist = false;
            autorizacaoDecolar = false;
            autorizacaoPouso = false;

            adicionarStatus("Aeronave desligada.");
            Toast.makeText(this, "Sistema encerrado.", Toast.LENGTH_SHORT).show();
        });
    }

    // ==========================
    // MÉTODO PARA EXIBIR O STATUS
    // ==========================
    private void adicionarStatus(String mensagem) {
        if (txtStatus.getText().toString().isEmpty()) {
            txtStatus.setText("• " + mensagem);
        } else {
            txtStatus.append("\n\n• " + mensagem);
        }

        // Rola automaticamente o EditText até a última linha
        txtStatus.post(() -> {
            if (txtStatus.getLayout() != null) {
                int scrollAmount = txtStatus.getLayout().getLineTop(txtStatus.getLineCount()) - txtStatus.getHeight();
                if (scrollAmount > 0) {
                    txtStatus.scrollTo(0, scrollAmount);
                } else {
                    txtStatus.scrollTo(0, 0);
                }
            }
        });
    }
}