package deepfocus.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TimerUtil {

    // Interfaces para notificação de eventos
    public interface TimerFinishListener {
        void onFinish();
    }

    public interface TimerTickListener {
        void onTick(int remainingSeconds);
    }

    private TimerTickListener tickListener;
    private TimerFinishListener finishListener;

    // Gerenciam a contagem
    private Timeline timeline;
    private volatile int segundosRestando; 
    
    // Executor: Pool de threads estático para gerenciar tarefas de background (como waitMs)
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    
    // Inicia a contagem
    public void iniciarContagem(int segundos) {
        stop(); // Garante que o timer anterior seja parado

        this.segundosRestando = segundos;
        
        // Define o passar do tempo (mudança no relógio)
        KeyFrame keyFrame = new KeyFrame(
            Duration.seconds(1), 
            actionEvent -> { 
                
                // Diminui e notifica o tempo restante
            	if (tickListener != null) {
            	    tickListener.onTick(segundosRestando);
            	}
            	segundosRestando--;


                // Verifica se o timer chegou a zero
            	if (segundosRestando <= 0) {
            	    stop();
            	    if (finishListener != null) finishListener.onFinish();
            	}
            }
        );

        // Cria a Timeline
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }

    // Pausa o timer instantaneamente
    public void pause() {
        if (timeline != null) {
            timeline.pause();
        }
    }

    // Retorna à contagem depois do pause
    public void resume() {
        if (timeline != null) {
            timeline.play();
        }
    }

    // Para o timer
    public void stop() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }


	// Threading pra evitar travamentos na inteface gráfica
    public static void waitMs(long ms, Runnable afterWait) {
        // Envia a tarefa para o executor
        executor.submit(() -> {
            try {
                // Suspende a thread por X milissegundos
                TimeUnit.MILLISECONDS.sleep(ms);
            } catch (InterruptedException ignored) {
                // Interrupção: thread parada
            }
            // Executa o Runnable após a espera
            if (afterWait != null) afterWait.run();
        });
    }

    // Converte o tempo para string
    public static String formatSeconds(int totalSeconds) {
        if (totalSeconds < 0) totalSeconds = 0;
        int min = totalSeconds / 60;
        int sec = totalSeconds % 60;
        return String.format("%02d:%02d", min, sec);
    }
    
    // Setters
    public void setTickListener(TimerTickListener listener) {
        this.tickListener = listener;
    }

    public void setFinishListener(TimerFinishListener listener) {
        this.finishListener = listener;
    }

    // Boolean que verifica se a contagem está sendo feita
    public boolean isRunning() {
        return timeline != null && timeline.getStatus() == Timeline.Status.RUNNING;
    }
    
    // Boolean que verifica se a contagem foi pausada
    public boolean isPaused() {
        return timeline != null && timeline.getStatus() == Timeline.Status.PAUSED;
    }
    
    // Getter, retorna o tempo restante no timer
    public int getRemainingSeconds() {
        return segundosRestando;
    }
}
