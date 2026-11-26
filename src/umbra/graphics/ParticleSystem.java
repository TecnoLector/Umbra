package umbra.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import umbra.entities.LightSource;

/**
 * [v1.0] Sistema de partículas: muchas partículas pequeñas, cada una con posición, velocidad
 * y tiempo de vida. En cada cuadro se mueven (con su propia gravedad), pierden vida y se
 * desvanecen: su transparencia depende de life / maxLife. Las que llegan a 0 se quitan.
 *
 * [v1.1] Además de círculos (Ellipse2D), hay destellos de cuatro puntas (Path2D) que giran:
 * se dibujan con una matriz de transformación T(x, y) · R(ángulo) · S(tamaño) sobre una forma
 * unitaria que se crea una sola vez. Cada partícula puede llevar fricción (drag: la velocidad
 * se multiplica por drag^(60·dt), así no depende de los cuadros por segundo) y una oscilación
 * horizontal con coseno (turbulencia).
 *
 * Efectos de Candil:
 *   emit         chispas al aterrizar
 *   emitVapor    estela de vapor al moverse (violeta dentro de la sombra)
 *   emitDust     polvo de luz que suelta mientras pierde vida fuera de la sombra
 *   emitCrumble  niebla y ceniza cuando se desmorona
 *
 * Efectos de Logi y Ambiente:
 *   emitSpark       chispa que sale de la llama y flota hacia arriba
 *   emitAsh         ceniza y brasas que va dejando atrás cuando se mueve
 *   emitPinPulse    anillo radial de destellos al fijar/soltar a Logi
 *   emitShadowMote  motas que flotan en la sombra, con oscilación armónica
 *   emitBioSpore    esporas que sueltan los hongos y las plantas cuando Logi o Candil los tocan
 */
public class ParticleSystem {

    /** Tope para que nunca se acumulen demasiadas. */
    private static final int MAX_PARTICLES = 900;

    /** Geometría de la partícula. */
    private enum ShapeType { CIRCLE, SPARKLE }

    /** Geometría precalculada de un destello de 4 puntas (radio unitario = 1.0). */
    private static final Path2D SPARKLE_SHAPE = createSparkleShape();

    /** Destello de cuatro puntas: ocho vértices que alternan entre la punta (radio 1) y un punto interior (0.25). */
    private static Path2D createSparkleShape() {
        Path2D p = new Path2D.Double();
        double inner = 0.25;
        p.moveTo(0, -1);
        p.lineTo(inner, -inner);
        p.lineTo(1, 0);
        p.lineTo(inner, inner);
        p.lineTo(0, 1);
        p.lineTo(-inner, inner);
        p.lineTo(-1, 0);
        p.lineTo(-inner, -inner);
        p.closePath();
        return p;
    }

    /** Una partícula: posición, velocidad y vida restante (en segundos). */
    private static class Particle {
        double x, y;
        double vx, vy;
        double life, maxLife;
        double size, gravity;
        double drag = 1.0;          // factor de amortiguamiento (1.0 = sin fricción)
        double angle, angularVel;   // rotación en radianes para primitivas poligonales
        double waveAmp, waveFreq;   // turbulencia sinusoidal horizontal
        double age;
        Color color;
        boolean halo;   // con un halo suave alrededor
        ShapeType shape = ShapeType.CIRCLE;
    }

    private final List<Particle> particles = new ArrayList<>();
    private final Random rnd = new Random();

    /** Chispas al aterrizar en el punto (x, y): saltan hacia arriba y a los lados. */
    public void emit(double x, double y, int count) {
        for (int i = 0; i < count; i++) {
            add(x + rand(-6, 6), y - 1, rand(-120, 120), rand(-160, -40), rand(0.3, 0.6),
                    rand(1.5, 2.5), 600, new Color(200, 235, 255), true);
        }
    }

    /**
     * Una bocanada de la estela de vapor de Candil: niebla translúcida (el alfa del color es su
     * opacidad máxima) que sube despacio y se deshace. Dentro de la sombra es violeta, como
     * "sumergida".
     */
    public void emitVapor(double x, double y, boolean inShadow) {
        Color c = inShadow ? new Color(170, 150, 235, 120) : new Color(185, 200, 228, 130);
        add(x + rand(-5, 5), y + rand(-6, 6), rand(-14, 14), rand(-26, -6), rand(0.45, 0.8),
                rand(2.5, 4), -10, c, true);
    }

    /** Polvo de luz que se desprende de Candil y sube mientras pierde vida. */
    public void emitDust(double x, double y) {
        add(x + rand(-9, 9), y + rand(-12, 12), rand(-15, 15), rand(-45, -20), rand(0.5, 0.9),
                rand(1.2, 2.2), -20, new Color(190, 220, 245), false);
    }

    /**
     * Candil se desmorona en 40 partículas: se deshace en niebla (bocanadas translúcidas que
     * suben y se dispersan) y ceniza (copos grises que caen despacio). Sus dos ojos se quedan
     * quietos donde estaban y son lo último en apagarse.
     */
    public void emitCrumble(Rectangle2D body) {
        double eyeX = body.getCenterX(), eyeY = body.getY() + 9;
        Color eye = new Color(235, 245, 255);
        add(eyeX - 2.5, eyeY, 0, 0, 1.2, 1.6, 0, eye, true);
        add(eyeX + 2.5, eyeY, 0, 0, 1.2, 1.6, 0, eye, true);
        for (int i = 0; i < 38; i++) {
            double px = body.getX() + rnd.nextDouble() * body.getWidth();
            double py = body.getY() + rnd.nextDouble() * body.getHeight();
            if (i % 3 == 0) {   // ceniza
                add(px, py, rand(-60, 60), rand(-90, -20), rand(0.7, 1.15), rand(1, 1.8), 220,
                        new Color(122, 118, 120), false);
            } else {            // niebla
                add(px, py, rand(-70, 70), rand(-80, 10), rand(0.6, 1.1), rand(2.5, 4.5), -40,
                        new Color(170, 186, 214, 150), true);
            }
        }
    }

    /**
     * Anillo de 16 destellos que salen en todas direcciones al fijar (dorados) o soltar (azules)
     * a Logi. Cada uno sale en el ángulo theta = 2π·i/16 (con un poco de azar), a velocidad
     * (cos θ, sin θ)·speed, y se frena solo con la fricción (drag) mientras gira.
     */
    public void emitPinPulse(double x, double y, boolean pinned) {
        int count = 16;
        Color c = pinned ? new Color(255, 225, 140) : new Color(170, 215, 255);
        for (int i = 0; i < count; i++) {
            if (particles.size() >= MAX_PARTICLES) return;
            double theta = (2 * Math.PI * i) / count + rand(-0.1, 0.1);
            double speed = rand(70, 130);
            Particle p = new Particle();
            p.x = x + Math.cos(theta) * 6;
            p.y = y + Math.sin(theta) * 6;
            p.vx = Math.cos(theta) * speed;
            p.vy = Math.sin(theta) * speed;
            p.life = rand(0.35, 0.55);
            p.maxLife = p.life;
            p.size = rand(3.5, 5.5);
            p.gravity = 0;
            p.drag = 0.90;   // desaceleración exponencial suave
            p.angle = rand(0, Math.PI);
            p.angularVel = rand(-6, 6);
            p.color = c;
            p.halo = true;
            p.shape = ShapeType.SPARKLE;
            particles.add(p);
        }
    }

    /** Mota que flota en la sombra: sube despacio y se bambolea de lado con una onda (waveAmp, waveFreq). */
    public void emitShadowMote(double x, double y) {
        if (particles.size() >= MAX_PARTICLES) return;
        Particle p = new Particle();
        p.x = x;
        p.y = y;
        p.vx = rand(-6, 6);
        p.vy = rand(-18, -8);
        p.life = rand(1.8, 3.0);
        p.maxLife = p.life;
        p.size = rand(1.4, 2.4);
        p.gravity = 0;
        p.waveAmp = rand(12, 24);
        p.waveFreq = rand(2.5, 4.5);
        p.color = new Color(150, 140, 220, 140);
        p.halo = true;
        p.shape = ShapeType.CIRCLE;
        particles.add(p);
    }

    /** Espora bioluminiscente que sueltan los hongos y las plantas al interactuar con ellos (gravedad negativa: sube). */
    public void emitBioSpore(double x, double y, Color color) {
        if (particles.size() >= MAX_PARTICLES) return;
        Particle p = new Particle();
        p.x = x + rand(-4, 4);
        p.y = y + rand(-3, 2);
        p.vx = rand(-14, 14);
        p.vy = rand(-32, -14);
        p.life = rand(0.9, 1.6);
        p.maxLife = p.life;
        p.size = rand(1.3, 2.3);
        p.gravity = -6;
        p.waveAmp = rand(8, 16);
        p.waveFreq = rand(3.0, 5.0);
        p.color = color;
        p.halo = true;
        p.shape = ShapeType.CIRCLE;
        particles.add(p);
    }

    public void update(double dt) {
        for (Particle p : particles) {
            p.age += dt;
            p.vy += p.gravity * dt;
            if (p.drag < 1.0) {
                double factor = Math.pow(p.drag, dt * 60.0);
                p.vx *= factor;
                p.vy *= factor;
            }
            double sway = (p.waveAmp > 0) ? Math.cos(p.age * p.waveFreq) * p.waveAmp : 0;
            p.x += (p.vx + sway) * dt;
            p.y += p.vy * dt;
            p.angle += p.angularVel * dt;
            p.life -= dt;
        }
        particles.removeIf(p -> p.life <= 0);
    }

    public void draw(Graphics2D g) {
        draw(g, null);
    }

    /**
     * [v1.2] Dibuja las partículas con dispersión atmosférica (efecto Tyndall):
     * las motas y esporas que entran en el radio de la luz ganan un halo cálido/frío
     * y aumentan ligeramente su brillo según la distancia inversa al foco.
     */
    public void draw(Graphics2D g, LightSource light) {
        double lx = (light != null) ? light.getX() : 0;
        double ly = (light != null) ? light.getY() : 0;
        double maxDist = 230.0;

        Color tintColor = null;
        if (light != null) {
            double dark = light.getDarkness();
            tintColor = mixColor(new Color(255, 210, 125), new Color(235, 65, 65), dark);
            if (light.isPinned()) {
                tintColor = mixColor(tintColor, new Color(145, 205, 255), 0.65);
            }
        }

        for (Particle p : particles) {
            double t = p.life / p.maxLife;   // 1 al nacer, 0 al morir
            double envelope = (p.waveAmp > 0) ? Math.sin(Math.PI * t) : (t * t);
            int alpha = (int) (255 * envelope);
            alpha = alpha * p.color.getAlpha() / 255;

            // Factor de iluminación por proximidad a Logi (0.0 lejos, hasta 1.0 muy cerca)
            double lightBoost = 0.0;
            if (light != null) {
                double dist = Math.hypot(p.x - lx, p.y - ly);
                if (dist < maxDist) {
                    double k = 1.0 - (dist / maxDist);
                    lightBoost = k * k;
                }
            }

            Color renderColor = (lightBoost > 0.01 && tintColor != null)
                    ? mixColor(p.color, tintColor, lightBoost * 0.55)
                    : p.color;

            if (p.halo || lightBoost > 0.15) {
                double haloScale = 2.4 + lightBoost * 1.4;
                double h = p.size * haloScale;
                double lifeRatio = alpha / 255.0;
                int haloAlpha = (int) Math.min(255, (alpha / 4.0) + (lightBoost * 75.0 * lifeRatio));
                g.setColor(withAlpha(renderColor, haloAlpha));
                g.fill(new Ellipse2D.Double(p.x - h, p.y - h, h * 2, h * 2));
            }

            double s = p.size * (0.5 + 0.5 * t) * (1.0 + lightBoost * 0.25);
            int coreAlpha = (int) Math.min(255, alpha + lightBoost * 90);
            g.setColor(withAlpha(renderColor, coreAlpha));

            if (p.shape == ShapeType.SPARKLE) {
                AffineTransform prev = g.getTransform();
                g.translate(p.x, p.y);
                g.rotate(p.angle);
                g.scale(s, s);
                g.fill(SPARKLE_SHAPE);
                g.setTransform(prev);
            } else {
                g.fill(new Ellipse2D.Double(p.x - s, p.y - s, s * 2, s * 2));
            }
        }
    }

    private static Color mixColor(Color a, Color b, double t) {
        t = Math.max(0, Math.min(1, t));
        int r = (int) (a.getRed() + (b.getRed() - a.getRed()) * t);
        int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t);
        return new Color(r, g, bl);
    }

    /** Cuántas partículas hay vivas (para el modo debug). */
    public int size() {
        return particles.size();
    }

    /** Quita todas (al empezar el nivel de nuevo). */
    public void clear() {
        particles.clear();
    }

    /** [B4] Una chispa dorada que se queda atrás del fragmento mientras vuela hacia Candil. */
    public void emitFragmentTrail(double x, double y) {
        add(x + rand(-4, 4), y + rand(-4, 4), rand(-25, 25), rand(-25, 25), rand(0.25, 0.45), rand(1.4, 2.4),
                0, new Color(255, 214, 110), true);
    }

    /** [B4] La chispa breve que queda sobre Candil cuando el fragmento llega. */
    public void emitFragmentArrive(double x, double y) {
        for (int i = 0; i < 8; i++) {
            double a = rand(0, Math.PI * 2), speed = rand(20, 70);
            add(x, y, Math.cos(a) * speed, Math.sin(a) * speed - 15, rand(0.25, 0.5), rand(1.6, 2.6),
                    -20, new Color(255, 226, 150), true);
        }
    }

    /**
     * [v1.0] Una chispa de Logi: sale de la llama y flota hacia arriba (la gravedad negativa la
     * acelera un poco hacia arriba) mientras se desvanece y se apaga.
     */
    public void emitSpark(double x, double y) {
        add(x + rand(-6, 6), y + rand(-10, 2), rand(-18, 18), rand(-45, -20), rand(0.9, 1.5),
                rand(1.1, 1.9), -30, new Color(255, 215, 120), true);
    }

    /**
     * [v1.0] Ceniza de la cola de Logi. (vx, vy) es la velocidad de Logi: la ceniza sale detrás de
     * ella y se va quedando atrás, despacio. Con d = v / |v| (hacia donde va) y n = (-d.y, d.x)
     * (perpendicular a d), sale en
     *     p = (x, y) - d·(4 a 24) + n·(-5 a 5)     con velocidad     -d·(30 a 90) + n·(-30 a 30).
     * Al azar, una de cada tres es una brasa naranja que brilla y flota; las demás, ceniza gris que
     * cae despacio.
     */
    public void emitAsh(double x, double y, double vx, double vy) {
        double speed = Math.hypot(vx, vy);
        double dx = speed > 0 ? vx / speed : 0, dy = speed > 0 ? vy / speed : 0;
        double nx = -dy, ny = dx;
        double back = rand(4, 24), side = rand(-5, 5);
        double drift = rand(30, 90), spread = rand(-30, 30);
        boolean ember = rnd.nextInt(3) == 0;
        add(x - dx * back + nx * side, y - dy * back + ny * side,
                -dx * drift + nx * spread, -dy * drift + ny * spread, rand(0.5, 0.9),
                ember ? rand(1.2, 2) : rand(1.4, 2.4), ember ? -15 : 50,
                ember ? new Color(255, 150, 60) : new Color(125, 110, 100), ember);
    }

    private void add(double x, double y, double vx, double vy, double life, double size,
                     double gravity, Color color, boolean halo) {
        if (particles.size() >= MAX_PARTICLES) return;
        Particle p = new Particle();
        p.x = x;
        p.y = y;
        p.vx = vx;
        p.vy = vy;
        p.life = life;
        p.maxLife = life;
        p.size = size;
        p.gravity = gravity;
        p.color = color;
        p.halo = halo;
        particles.add(p);
    }

    private double rand(double min, double max) {
        return min + rnd.nextDouble() * (max - min);
    }

    private static Color withAlpha(Color c, int a) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.max(0, Math.min(255, a)));
    }
}
