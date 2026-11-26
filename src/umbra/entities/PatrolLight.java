package umbra.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Ellipse2D;

import umbra.core.Config;

/**
 * Centinela de Llama Negra: una luz autónoma que patrulla entre dos puntos (x1, y1) y (x2, y2).
 * Proyecta sombras dinámicas mediante ShadowCaster y desmorona a Candil si entra en su radio.
 */
public class PatrolLight {

    private final double x1, y1, x2, y2;
    private final double speed;       // ciclos por segundo (frecuencia angular)
    private double phase;
    private double time;
    private double x, y;

    public PatrolLight(double x1, double y1, double x2, double y2, double speed, double startPhase) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.speed = speed;
        this.phase = startPhase;
        updatePosition();
    }

    public void update(double dt) {
        time += dt;
        phase += speed * dt;
        updatePosition();
    }

    private void updatePosition() {
        double t = 0.5 - 0.5 * Math.cos(phase); // oscilación suave 0 -> 1 -> 0
        this.x = x1 + (x2 - x1) * t;
        this.y = y1 + (y2 - y1) * t;
    }

    /** Dibuja el halo atmosférico oscuro-carmesí y el núcleo de Llama Negra. */
    public void draw(Graphics2D g) {
        float radius = (float) (Config.CRUMBLE_RADIUS * 1.9);
        float[] dist = { 0.0f, 0.55f, 1.0f };
        Color[] colors = {
            new Color(180, 40, 80, 85),
            new Color(90, 20, 60, 35),
            new Color(20, 10, 30, 0)
        };
        g.setPaint(new RadialGradientPaint((float) x, (float) y, radius, dist, colors));
        g.fill(new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2));

        // Núcleo de llama negra (darkness = 1.0)
        LightSource.drawDark(g, x, y, 1.0, 0.95, time);
    }

    /** Crea un LightSource virtual en su posición actual para calcular sus sombras con ShadowCaster. */
    public LightSource asLightSource() {
        return new LightSource(x, y);
    }

    public double getX() { return x; }
    public double getY() { return y; }
}