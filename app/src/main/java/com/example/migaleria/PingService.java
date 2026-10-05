package com.example.migaleria;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.net.InetAddress;

public class PingService extends Service {
    private boolean ejecutando = false;
    private PingListener listener;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int numIntentos = intent.getIntExtra("numIntentos", 1);
        ejecutarPings(numIntentos);
        return START_NOT_STICKY;
    }

    private void ejecutarPings(int numIntentos) {
        ejecutando = true;
        Thread hilo = new Thread(() -> {
            int exitos = 0;
            int fallos = 0;
            int intento = 1;

            while (intento <= numIntentos && ejecutando) {
                boolean resultado = hacerPing();
                if (resultado) {
                    exitos++;
                } else {
                    fallos++;
                }

                if (listener != null) {
                    listener.resultadoPing(intento, resultado);
                }

                intento++;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            ejecutando = false;

            if (listener != null) {
                listener.pingsFinalizados(exitos, fallos);
            }

            stopSelf();
        });
        hilo.start();
    }

    private boolean hacerPing() {
        try {
            InetAddress direccion = InetAddress.getByName("google.com");
            return direccion.isReachable(5000);
        } catch (Exception e) {
            return false;
        }
    }

    public void pararPing() {
        ejecutando = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface PingListener {
        void resultadoPing(int intento, boolean exito);
        void pingsFinalizados(int exitos, int fallos);
    }

    public void setListener(PingListener listener) {
        this.listener = listener;
    }
}