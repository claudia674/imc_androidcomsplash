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

public class Simulador_de_Mergulho_Submarino extends AppCompatActivity {

    private SwitchMaterial switchEscotilha;
    private ToggleButton toggleSuporteVida;
    private RadioGroup rgModoNavegacao;
    private EditText edtProfundidadeAlvo;
    private Button btnIniciarMissao, btnSubmergir, btnEmergir, btnFinalizarMissao;
    private EditText txtStatus;

    // Controle de estado
    private boolean missaoIniciada = false;
    private int profundidadeAtual = 0;
    private int profundidadeAlvo = 0;
    private final int PASSO_PROFUNDIDADE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_simulador_de_mergulho_submarino);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Mapeamento dos componentes da tela
        switchEscotilha = findViewById(R.id.switchEscotilha);
        toggleSuporteVida = findViewById(R.id.toggleSuporteVida);
        rgModoNavegacao = findViewById(R.id.rgModoNavegacao);
        edtProfundidadeAlvo = findViewById(R.id.edtProfundidadeAlvo);

        btnIniciarMissao = findViewById(R.id.btnIniciarMissao);
        btnSubmergir = findViewById(R.id.btnSubmergir);
        btnEmergir = findViewById(R.id.btnEmergir);
        btnFinalizarMissao = findViewById(R.id.btnFinalizarMissao);

        txtStatus = findViewById(R.id.txtStatus);
        txtStatus.setMovementMethod(new ScrollingMovementMethod());

        adicionarStatus("Sistemas em espera. Aguardando preparação...");

        // ==========================================
        // REGRA CRÍTICA DE SEGURANÇA
        // ==========================================
        switchEscotilha.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked && profundidadeAtual > 0) {
                // Impede abrir a escotilha se estiver submerso
                switchEscotilha.setChecked(true);
                Toast.makeText(this, "PERIGO: Não abra a escotilha submerso!", Toast.LENGTH_LONG).show();
                adicionarStatus("ALERTA DE SEGURANÇA: Tentativa de abrir a escotilha submerso foi bloqueada!");
            }
        });

        toggleSuporteVida.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked && profundidadeAtual > 0) {
                // Impede desligar o suporte à vida se estiver submerso
                toggleSuporteVida.setChecked(true);
                Toast.makeText(this, "PERIGO: Não desligue o suporte à vida submerso!", Toast.LENGTH_LONG).show();
                adicionarStatus("ALERTA DE SEGURANÇA: Tentativa de desligar suporte à vida submerso foi bloqueada!");
            }
        });

        // ==========================================
        // INICIAR MISSÃO
        // ==========================================
        btnIniciarMissao.setOnClickListener(v -> {
            if (validarPreparacao()) {
                missaoIniciada = true;
                profundidadeAlvo = Integer.parseInt(edtProfundidadeAlvo.getText().toString());

                // Travar campos de configuração
                travarControlesPreparacao(false);

                // Ativar o botão de submergir
                btnSubmergir.setEnabled(true);
                btnIniciarMissao.setEnabled(false);

                adicionarStatus("Preparação 100% concluída.");
                adicionarStatus("Missão Iniciada. Alvo definido: " + profundidadeAlvo + "m.");
                Toast.makeText(this, "Missão Iniciada com Sucesso!", Toast.LENGTH_SHORT).show();
            }
        });

        // ==========================================
        // SUBMERGIR (+50m)
        // ==========================================
        btnSubmergir.setOnClickListener(v -> {
            if (!missaoIniciada) return;

            if (profundidadeAtual < profundidadeAlvo) {
                profundidadeAtual += PASSO_PROFUNDIDADE;

                if (profundidadeAtual > profundidadeAlvo) {
                    profundidadeAtual = profundidadeAlvo;
                }

                adicionarStatus("Submergindo... Profundidade atual: " + profundidadeAtual + "m");

                if (profundidadeAtual == profundidadeAlvo) {
                    adicionarStatus("Profundidade alvo atingida: " + profundidadeAlvo + "m.");
                    Toast.makeText(this, "Profundidade alvo atingida!", Toast.LENGTH_SHORT).show();
                }

                atualizarBotoesNavegacao();
            } else {
                Toast.makeText(this, "Já está na profundidade máxima configurada.", Toast.LENGTH_SHORT).show();
            }
        });

        // ==========================================
        // EMERGIR (-50m)
        // ==========================================
        btnEmergir.setOnClickListener(v -> {
            if (!missaoIniciada) return;

            if (profundidadeAtual > 0) {
                profundidadeAtual -= PASSO_PROFUNDIDADE;

                if (profundidadeAtual < 0) {
                    profundidadeAtual = 0;
                }

                adicionarStatus("Emergindo... Profundidade atual: " + profundidadeAtual + "m");

                if (profundidadeAtual == 0) {
                    adicionarStatus("Submarino chegou à superfície (0m).");
                    Toast.makeText(this, "Superfície atingida!", Toast.LENGTH_SHORT).show();
                }

                atualizarBotoesNavegacao();
            }
        });

        // ==========================================
        // FINALIZAR MISSÃO
        // ==========================================
        btnFinalizarMissao.setOnClickListener(v -> {
            if (profundidadeAtual == 0) {
                missaoIniciada = false;

                // Destravar os controles para uma nova preparação
                travarControlesPreparacao(true);
                btnIniciarMissao.setEnabled(true);
                btnSubmergir.setEnabled(false);
                btnEmergir.setEnabled(false);
                btnFinalizarMissao.setEnabled(false);

                adicionarStatus("Missão finalizada com sucesso. Tripulação autorizada a desembarcar.");
                Toast.makeText(this, "Missão Encerrada!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Erro: O submarino precisa estar na superfície (0m) para finalizar.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ==========================================
    // VALIDAÇÕES E MÉTODOS AUXILIARES
    // ==========================================

    private boolean validarPreparacao() {
        if (!switchEscotilha.isChecked()) {
            Toast.makeText(this, "A escotilha precisa estar TRANCADA!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!toggleSuporteVida.isChecked()) {
            Toast.makeText(this, "O Suporte à Vida precisa estar LIGADO!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (rgModoNavegacao.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecione um Modo de Navegação!", Toast.LENGTH_SHORT).show();
            return false;
        }

        String textoProfundidade = edtProfundidadeAlvo.getText().toString().trim();
        if (textoProfundidade.isEmpty()) {
            Toast.makeText(this, "Informe a profundidade alvo!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int valorProfundidade = Integer.parseInt(textoProfundidade);
        if (valorProfundidade <= 0) {
            Toast.makeText(this, "A profundidade deve ser maior que 0m!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void travarControlesPreparacao(boolean habilitado) {
        switchEscotilha.setEnabled(habilitado);
        toggleSuporteVida.setEnabled(habilitado);
        edtProfundidadeAlvo.setEnabled(habilitado);

        for (int i = 0; i < rgModoNavegacao.getChildCount(); i++) {
            rgModoNavegacao.getChildAt(i).setEnabled(habilitado);
        }
    }

    private void atualizarBotoesNavegacao() {
        btnSubmergir.setEnabled(profundidadeAtual < profundidadeAlvo);
        btnEmergir.setEnabled(profundidadeAtual > 0);
        btnFinalizarMissao.setEnabled(profundidadeAtual == 0 && missaoIniciada);
    }

    private void adicionarStatus(String mensagem) {
        if (txtStatus.getText().toString().isEmpty()) {
            txtStatus.setText("• " + mensagem);
        } else {
            txtStatus.append("\n\n• " + mensagem);
        }

        // Garante a rolagem automática até o final da mensagem
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