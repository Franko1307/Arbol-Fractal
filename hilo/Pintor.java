package hilo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
/**
 * Constructor de la clase Pintor
 * Este clase crea un pintor el que se encarga de hacer una línea de un punto inicial a un punto final
 * luego llama a más pintores dependiendo del número de ramificaciones y los añade con cierto ángulo para 
 * que se vea bonito el árbol
 * @author Fran
 */
public class Pintor extends Thread {
    private final Point pInicial;
    private final Point pFinal;
    private final int ramificaciones;
    private final int grosor;
    private final int iteraciones;
    private final float proporcion;
    private final float velocidad;
    private final Graphics2D brocha;
    private final ArrayList<Point> puntosFinales;
    private final int angulo;
    private final ArrayList<Integer> angulos;
    private final int rojo;
    private final int verde;
    private final int azul;
    private final int aumentorojo;
    private final int aumentoverde;
    private final int aumentoazul;
    
    public Pintor(int angulo, Point pInicial, Point pFinal,
        int ramificaciones, int grosor, int iteraciones,
        float proporcion, float velocidad, Graphics2D brocha,
        int rojo, int aumentorojo, int verde, int aumentoverde, int azul, int aumentoazul) {
        this.angulos = new ArrayList<>();
        this.pInicial = pInicial;
        this.pFinal = pFinal;
        this.ramificaciones = ramificaciones;
        this.grosor = grosor; 
        this.iteraciones = iteraciones;       
        this.proporcion = proporcion;
        this.velocidad = velocidad;
        this.brocha = brocha;
        puntosFinales = new ArrayList<>();
        this.angulo = angulo;
        this.rojo = rojo;
        this.azul = azul;
        this.verde = verde;
        this.aumentorojo = aumentorojo;
        this.aumentoverde = aumentoverde;
        this.aumentoazul = aumentoazul;
    }
    /**
     * Este es el método run del hilo.
     */
    @Override
    public synchronized void run(){
        if (rojo <= 255 && verde <= 255 && azul <= 255 && rojo >= 0 && verde >= 0 && azul >= 0)
            brocha.setColor(new Color(rojo,verde,azul));
        
        if (iteraciones > 0){            
            try {
                    wait((long) (1000/velocidad));
            } catch (InterruptedException ex) {
                System.err.println("No me pude dormir, favor de llamar a un tecnico");            
            }
            brocha.drawLine(pInicial.x, pInicial.y, pFinal.x, pFinal.y);
            calcularPuntosFinales();
            for (int i = 0; i < ramificaciones; ++i)
                new Pintor((int)(angulos.get(i)), pFinal, puntosFinales.get(i),
                        ramificaciones, grosor, iteraciones - 1, proporcion/2 + proporcion/4,
                        velocidad, brocha, rojo + aumentorojo, aumentorojo, verde + aumentoverde,
                        aumentoverde, azul + aumentoazul, aumentoazul
                ).start();                                     
            try {
                finalize();
            } catch (Throwable ex) {
                System.err.println("No me pude finalizar, favor de llamar a un técnico");
            }
        }else{
            try {
                finalize();
            } catch (Throwable ex) {
                System.err.println("No me pude finalizar, favor de llamar a un técnico");
            }
        }
    }    
    private void calcularPuntosFinales() {
        int anguloAniadido = 180/(ramificaciones+1);
        int anguloAux = (int)(90 - anguloAniadido);
        for (int i = 0; i < ramificaciones; ++i){            
            puntosFinales.add(new Point(
                    pFinal.x + (int)(Math.cos(Math.toRadians(angulo+anguloAux)) * 75 * proporcion),
                    pFinal.y - (int)(Math.sin(Math.toRadians(angulo+anguloAux)) * 75 * proporcion)
            ));
            angulos.add((Integer)(angulo+anguloAux));
            anguloAux -= anguloAniadido;
        }
    }
}
