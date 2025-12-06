package deepfocus.services;

import deepfocus.modelos.Pomodoro;
import deepfocus.utils.TimerUtil;
import java.time.LocalDateTime;

public class PomodoroService implements TimerUtil.TimerFinishListener {

    private static final String STATUS_FOCO = "FOCO";
    private static final String STATUS_DESCANSO = "DESCANSO";

    private final Pomodoro configuracao;
    private final TimerUtil timerUtil;
    private final HistoricoService historicoService;

    private String statusAtual;
    private int ciclosConcluidos = 0;

    // Interface para notificar a UI sobre mudanças de estado nos ciclos
    public interface CicloChangeListener {
        void onCicloChange(String novoStatus, int proximaDuracaoSegundos);
    }

    private CicloChangeListener changeListener;

    // Construtor parametrizado
    public PomodoroService(Pomodoro config, TimerUtil util, HistoricoService historicoService) {
        this.configuracao = config;
        this.timerUtil = util;
        this.historicoService = historicoService;
        this.timerUtil.setFinishListener(this);
        this.statusAtual = STATUS_FOCO;
    }

    // Inicia o ciclo no foco
    public void start() {
        if (timerUtil.isRunning() || timerUtil.isPaused()) {
            return;
        }

        configuracao.setInicio(LocalDateTime.now());
        statusAtual = STATUS_FOCO;

        int duracaoInicial = configuracao.getDuracaoFoco();
        timerUtil.iniciarContagem(duracaoInicial);

        if (changeListener != null) {
            changeListener.onCicloChange(STATUS_FOCO, duracaoInicial);
        }
    }

    // Função chamada pelo TimerUtil quando a contegem chega a zero
    @Override
    public void onFinish() {
        if (statusAtual.equals(STATUS_FOCO)) {

            // Fim do Foco -> Inicia o Descanso
            ciclosConcluidos++;
            statusAtual = STATUS_DESCANSO;

            int duracaoDescanso = configuracao.getDuracaoDescanso();
            timerUtil.iniciarContagem(duracaoDescanso);

            if (changeListener != null) {
                changeListener.onCicloChange(STATUS_DESCANSO, duracaoDescanso);
            }

        } else if (statusAtual.equals(STATUS_DESCANSO)) {

            // Fim do Descanso -> Inicia o próximo Foco
            statusAtual = STATUS_FOCO;

            int duracaoFoco = configuracao.getDuracaoFoco();
            timerUtil.iniciarContagem(duracaoFoco);

            if (changeListener != null) {
                changeListener.onCicloChange(STATUS_FOCO, duracaoFoco);
            }
        }
    }

    // Controle de pausas no cronometro
    public void pause() {
        timerUtil.pause();
    }

    public void resume() {
        timerUtil.resume();
    }

    public void stop() {
        // Persistência
        System.out.println("🛑 STOP chamado!");
        System.out.println("Início configurado: " + configuracao.getInicio());

        if (configuracao.getInicio() != null) {
            try {
                // Define o término
                configuracao.setTermino(LocalDateTime.now());
                System.out.println("📝 Salvando no histórico...");
                System.out.println("   Início: " + configuracao.getInicio());
                System.out.println("   Término: " + configuracao.getTermino());
                System.out.println("   Foco: " + configuracao.getDuracaoFoco() + "s");
                System.out.println("   Descanso: " + configuracao.getDuracaoDescanso() + "s");

                // Salva o Pomodoro atual no histórico
                historicoService.adicionarPomodoro(configuracao);
                System.out.println("✅ Salvo com sucesso no histórico!");
            } catch (Exception e) {
                System.err.println("❌ Erro ao salvar Pomodoro no histórico: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠ Pomodoro não iniciado, não há nada para salvar.");
        }

        timerUtil.stop();
        statusAtual = STATUS_FOCO;
        ciclosConcluidos = 0;
        configuracao.setInicio(null);
        configuracao.setTermino(null);
    }

    // Getters
    public String getStatusAtual() {
        return statusAtual;
    }

    public int getCiclosConcluidos() {
        return ciclosConcluidos;
    }

    public boolean isRunning() {
        return timerUtil.isRunning();
    }

    public boolean isPaused() {
        return timerUtil.isPaused();
    }

    // Setters
    public void setCicloChangeListener(CicloChangeListener listener) {
        this.changeListener = listener;
    }

    public void setTickListener(TimerUtil.TimerTickListener listener) {
        timerUtil.setTickListener(listener);
    }
}
