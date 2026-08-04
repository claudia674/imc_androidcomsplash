package com.example.imc_androidcomsplash;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class Caldeira_Industrial extends AppCompatActivity {

    private SwitchMaterial switchEnergia;
    private ToggleButton toggleTrava;
    private RadioGroup rgCombustivel;
    private EditText edtPressaoMaxima;
    private Button btnIniciarAquecimento, btnPressurizar, btnDespressurizar;
    private EditText txtStatus;

    // Estado do Sistema
    private boolean aquecimentoIniciado = false;
    private int pressaoAtual = 0;
    private int pressaoMaxima = 0;
    private final int PASSO_PRESSAO = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_caldeira_industrial);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Mapeamento de componentes
        switchEnergia = findViewById(R.id.switchEnergia);
        toggleTrava = findViewById(R.id.toggleTrava);
        rgCombustivel = findViewById(R.id.rgCombustivel);
        edtPressaoMaxima = findViewById(R.id.edtPressaoMaxima);

        btnIniciarAquecimento = findViewById(R.id.btnIniciarAquecimento);
        btnPressurizar = findViewById(R.id.btnPressurizar);
        btnDespressurizar = findViewById(R.id.btnDespressurizar);

        txtStatus = findViewById(R.id.txtStatus);
        txtStatus.setMovementMethod(new ScrollingMovementMethod());

        adicionarStatus("Sistema em espera. Ligue a energia para iniciar.");

        // =======================================================
        // REGRA DE SEGURANÇA: ENERGIA E TRAVA
        // =======================================================
        switchEnergia.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked && pressaoAtual > 0) {
                // Impede desligar a energia se houver pressão no sistema
                switchEnergia.setChecked(true);
                Toast.makeText(this, "PERIGO: Despressurize a caldeira a 0 PSI antes de desligar!", Toast.LENGTH_LONG).show();
                adicionarStatus("ALERTA DE SEGURANÇA: Tentativa de desligar a energia com o sistema pressurizado!");
            } else if (!isChecked && aquecimentoIniciado) {
                // Se a pressão estiver em 0 PSI, permite desligar e encerrar a operação
                aquecimentoIniciado = false;
                travarControlesPreparacao(true);
                btnIniciarAquecimento.setEnabled(true);
                btnPressurizar.setEnabled(false);
                btnDespressurizar.setEnabled(false);

                adicionarStatus("Energia principal desligada. Sistema encerrado com segurança.");
                Toast.makeText(this, "Caldeira desligada com segurança.", Toast.LENGTH_SHORT).show();
            } else if (isChecked) {
                adicionarStatus("Energia principal LIGADA.");
            }
        });

        toggleTrava.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked && pressaoAtual > 0) {
                // Impede travar o sistema com pressão interna acumulada
                toggleTrava.setChecked(true);
                Toast.makeText(this, "PERIGO: Não altere a trava enquanto houver pressão!", Toast.LENGTH_SHORT).show();
                adicionarStatus("ALERTA DE SEGURANÇA: A trava não pode ser acionada sob pressão!");
            }
        });

        // =======================================================
        // INICIAR AQUECIMENTO
        // =======================================================
        btnIniciarAquecimento.setOnClickListener(v -> {
            if (validarInicializacao()) {
                aquecimentoIniciado = true;
                pressaoMaxima = Integer.parseInt(edtPressaoMaxima.getText().toString());

                // Trava as opções de configuração iniciais
                travarControlesPreparacao(false);

                btnIniciarAquecimento.setEnabled(false);
                btnPressurizar.setEnabled(true);

                adicionarStatus("Validação concluída com sucesso.");
                adicionarStatus("Aquecimento iniciado. Pressão Alvo: " + pressaoMaxima + " PSI.");
                Toast.makeText(this, "Aquecimento iniciado!", Toast.LENGTH_SHORT).show();
            }
        });

        // =======================================================
        // PRESSURIZAR (+10 PSI)
        // =======================================================
        btnPressurizar.setOnClickListener(v -> {
            if (!aquecimentoIniciado) return;

            if (pressaoAtual < pressaoMaxima) {
                pressaoAtual += PASSO_PRESSAO;

                if (pressaoAtual > pressaoMaxima) {
                    pressaoAtual = pressaoMaxima;
                }

                adicionarStatus("Pressurizando... Pressão atual: " + pressaoAtual + " PSI");

                if (pressaoAtual == pressaoMaxima) {
                    adicionarStatus("Pressão máxima de trabalho atingida: " + pressaoMaxima + " PSI.");
                    Toast.makeText(this, "Limite de pressão atingido!", Toast.LENGTH_SHORT).show();
                }

                atualizarBotoesPressao();
            } else {
                Toast.makeText(this, "A caldeira já está na pressão máxima permitida.", Toast.LENGTH_SHORT).show();
            }
        });

        // =======================================================
        // DESPRESSURIZAR (-10 PSI)
        // =======================================================
        btnDespressurizar.setOnClickListener(v -> {
            if (!aquecimentoIniciado) return;

            if (pressaoAtual > 0) {
                pressaoAtual -= PASSO_PRESSAO;

                if (pressaoAtual < 0) {
                    pressaoAtual = 0;
                }

                adicionarStatus("Abrindo válvula de alívio... Pressão atual: " + pressaoAtual + " PSI");

                if (pressaoAtual == 0) {
                    adicionarStatus("Caldeira completamente despressurizada (0 PSI).");
                    adicionarStatus("É seguro desligar a energia principal.");
                    Toast.makeText(this, "Equipamento seguro para desligar.", Toast.LENGTH_LONG).show();
                }

                atualizarBotoesPressao();
            }
        });
    }

    // =======================================================
    // MÉTODOS AUXILIARES E VALIDAÇÕES
    // =======================================================

    private boolean validarInicializacao() {
        if (!switchEnergia.isChecked()) {
            Toast.makeText(this, "A Energia Principal deve estar LIGADA!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!toggleTrava.isChecked()) {
            Toast.makeText(this, "A Trava de Segurança precisa estar DESTRAVADA!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (rgCombustivel.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecione o tipo de combustível (Gás ou Biomassa)!", Toast.LENGTH_SHORT).show();
            return false;
        }

        String textoPressao = edtPressaoMaxima.getText().toString().trim();
        if (textoPressao.isEmpty()) {
            Toast.makeText(this, "Informe a pressão máxima de trabalho!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int valorPressao = Integer.parseInt(textoPressao);
        if (valorPressao <= 0) {
            Toast.makeText(this, "A pressão máxima deve ser maior que 0 PSI!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void travarControlesPreparacao(boolean habilitado) {
        toggleTrava.setEnabled(habilitado);
        edtPressaoMaxima.setEnabled(habilitado);

        for (int i = 0; i < rgCombustivel.getChildCount(); i++) {
            rgCombustivel.getChildAt(i).setEnabled(habilitado);
        }
    }

    private void atualizarBotoesPressao() {
        btnPressurizar.setEnabled(pressaoAtual < pressaoMaxima);
        btnDespressurizar.setEnabled(pressaoAtual > 0);
    }

    private void adicionarStatus(String mensagem) {
        if (txtStatus.getText().toString().isEmpty()) {
            txtStatus.setText("• " + mensagem);
        } else {
            txtStatus.append("\n\n• " + mensagem);
        }

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