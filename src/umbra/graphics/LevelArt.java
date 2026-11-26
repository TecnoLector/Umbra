package umbra.graphics;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import umbra.core.Config;
import umbra.entities.Block;
import umbra.entities.LightSource;
import umbra.entities.Player;
import umbra.math.Mat3;
import umbra.math.Vec2;
import umbra.world.Level;

/**
 * [v1.0] El aspecto del nivel: repisas de roca con musgo, plantas que se mecen, hongos que
 * brillan, bloques como losas de piedra con una runa que se enciende cuando Logi se
 * acerca, y columnas rotas.
 *
 * Lo que no se mueve (roca, musgo, losas, columnas) se dibuja una sola vez al cargar el
 * nivel y se guarda en imágenes (pre-renderizado). Las plantas y los hongos se dibujan en
 * cada cuadro porque se mecen. Las colisiones no cambian: siguen siendo los mismos
 * rectángulos. Regla visual: lo que tiene borde claro arriba se puede pisar; las columnas
 * son oscuras y sin borde porque son solo decoración.
 *
 * [v0.9.5] En una zona con sala del trono (solo la Cúpula) también guarda, en otra imagen, el
 * fondo de la sala (ThroneHallArt), y dibuja el piso de la sala como losas de piedra labrada en
 * vez de roca. Además, solo en esa zona: una repisa cuya cara de arriba queda toda tapada por
 * otras (la cima de los escalones del domo, debajo del piso de la sala) no lleva borde claro ni
 * musgo, y ningún hongo, mata ni columna rota nace dentro de la roca de otra repisa. Las demás
 * zonas se dibujan igual que antes.
 */
public class LevelArt {

    // ---- Paleta (fría, como el fondo) ----
    private static final Color ROCK_TOP = new Color(46, 54, 76);
    private static final Color ROCK_BOTTOM = new Color(22, 26, 40);
    private static final Color RIM = new Color(125, 145, 190, 170);
    private static final Color STRATA = new Color(70, 82, 110, 45);
    private static final Color PEBBLE = new Color(54, 62, 86, 120);
    private static final Color PEBBLE_LINE = new Color(12, 14, 24, 120);
    private static final Color CRACK = new Color(8, 10, 18, 170);
    private static final Color MOSS_DARK = new Color(34, 66, 70);
    private static final Color MOSS_LIGHT = new Color(78, 140, 135);
    private static final Color PROP = new Color(24, 30, 46);
    private static final Color STONE_TOP = new Color(92, 98, 124);
    private static final Color STONE_BOTTOM = new Color(62, 66, 88);
    private static final Color STONE_LIGHT = new Color(150, 158, 190, 200);
    private static final Color STONE_DARK = new Color(28, 30, 44, 220);
    private static final Color STONE_EDGE = new Color(160, 165, 200, 160);
    private static final Color CARVED = new Color(30, 32, 46, 200);
    private static final Color STEM = new Color(150, 170, 190);
    private static final Color CAP_TEAL = new Color(80, 215, 205);
    private static final Color CAP_BLUE = new Color(130, 190, 255);
    private static final Color CAP_SHINE = new Color(235, 255, 255, 190);
    private static final Color RUNE = new Color(255, 215, 140);

    private static final int SIDE = 14;       // margen a los lados de cada repisa (musgo que cuelga)
    private static final int TOP = 26;        // margen arriba (musgo sobre la orilla)
    private static final double RUNE_SIZE = 9;
    private static final double RUNE_NEAR = 80, RUNE_FAR = 420;   // distancias a Logi (px)

    // ---- Resorte de las plantas: aceleración angular = K * (meta - ángulo) - C * velocidad ----
    private static final double STIFFNESS = 70;   // K: qué tan fuerte regresa a su posición
    private static final double DAMPING = 7;      // C: qué tan rápido se calma el bamboleo
    private static final Stroke BLADE_STROKE = new BasicStroke(1.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    /** Una imagen ya dibujada y su posición en el mundo. */
    private static class Sprite {
        final BufferedImage img;
        final double x, y;
        Sprite(BufferedImage img, double x, double y) { this.img = img; this.x = x; this.y = y; }
    }

    /** Algo que se mece: su base (x, y) está fija sobre la orilla de una repisa. */
    private static class Plant {
        double x, y;          // base, en coordenadas del mundo
        double height;        // qué tan alta es (para saber si Candil la toca)
        double maxBend;       // cuánto se dobla como máximo (radianes)
        double angle, speed;  // ángulo actual (0 = derecha hacia arriba; > 0 = hacia la derecha) y su velocidad
        double phase;         // para el vaivén del aire
    }

    /** Hongo: tallo y sombrero que giran juntos alrededor de la base. Su brillo pulsa. */
    private static class Mushroom extends Plant {
        double capW;
        Color cap;
        BufferedImage glow;
        double energy;   // 0 = reposo, 1 = excitado por la cercanía de Logi o el roce de Candil
    }

    /** Mata de briznas: cada brizna se dobla más en la punta que en la base. */
    private static class Tuft extends Plant {
        double[] dx, heights, offsets;
        Color color;
    }

    /** Brillo de una runa. */
    private static class Rune {
        final double x, y, phase;
        Rune(double x, double y, double phase) { this.x = x; this.y = y; this.phase = phase; }
    }

    private final List<Sprite> props = new ArrayList<>();
    private final List<Sprite> platforms = new ArrayList<>();
    private final List<Sprite> blocks = new ArrayList<>();
    private final List<Mushroom> mushrooms = new ArrayList<>();
    private final List<Tuft> tufts = new ArrayList<>();
    private final List<Rune> runes = new ArrayList<>();
    private final BufferedImage glowTeal = renderGlow(CAP_TEAL, 22);
    private final BufferedImage glowBlue = renderGlow(CAP_BLUE, 22);
    private final BufferedImage runeGlow = renderRuneGlow();
    private double time;

    private final Level level;
    private final Rectangle2D hall;          // [v0.9.5] la sala del trono (null si la zona no tiene)
    private final BufferedImage hallImage;   // [v0.9.5] su fondo quieto, dibujado una sola vez

    public LevelArt(Level level, long seed) {
        this.level = level;
        Random rnd = new Random(seed);
        int i = 0;
        hall = level.getThroneHall();
        for (Rectangle2D r : level.getPlatforms()) {
            if (r.getWidth() >= 400) props.add(renderColumn(r, rnd));
            // El piso de la sala se dibuja como losas labradas; su roca se calcula igual (y se tira) para
            // que el azar del resto del decorado de la zona no cambie
            Sprite rock = renderPlatform(r, rnd, i % 2 == 0, hall != null && buried(level, r));
            platforms.add(isHallFloor(r) ? renderHallFloor(r) : rock);
            i++;
        }
        for (Block b : level.getBlocks()) {
            Rectangle2D r = b.getBounds();
            blocks.add(renderBlock(r, rnd));
            runes.add(new Rune(r.getCenterX(), r.getCenterY(), rnd.nextDouble() * Math.PI * 2));
        }
        if (hall != null) {
            // [v0.9.5] En la zona de la sala nada nace dentro de la roca (la cima de los escalones del
            // domo quedó bajo el piso de la sala) ni en el piso labrado de la sala: se quitan al final,
            // para no cambiar el resto del decorado
            for (int k = mushrooms.size() - 1; k >= 0; k--) if (noPlant(mushrooms.get(k))) mushrooms.remove(k);
            for (int k = tufts.size() - 1; k >= 0; k--) if (noPlant(tufts.get(k))) tufts.remove(k);
            for (int k = props.size() - 1; k >= 0; k--) {
                Sprite c = props.get(k);
                if (inRock(c.x + c.img.getWidth() / 2.0, c.y + c.img.getHeight() - 8 - 3)) props.remove(k);   // la base de la columna
            }
        }
        hallImage = hall == null ? null : renderHall(hall);
    }

    /** [v0.9.5] ¿Esta planta no debe estar? Su base cae dentro de la roca o sobre el piso de la sala. */
    private boolean noPlant(Plant p) {
        boolean onHallFloor = Math.abs(p.y - hall.getMaxY()) < 0.5 && p.x >= hall.getMinX() && p.x <= hall.getMaxX();
        return onHallFloor || inRock(p.x, p.y - 1.5);
    }

    /** ¿El punto cae dentro de alguna repisa? */
    private boolean inRock(double x, double y) {
        for (Rectangle2D p : level.getPlatforms()) if (p.contains(x, y)) return true;
        return false;
    }

    /** [v0.9.5] ¿Toda la cara de arriba de 'r' queda tapada por otras repisas? (cada 2 px) */
    private static boolean buried(Level level, Rectangle2D r) {
        for (double x = r.getMinX() + 1; x < r.getMaxX(); x += 2) {
            boolean covered = false;
            for (Rectangle2D p : level.getPlatforms()) {
                if (p != r && p.contains(x, r.getMinY() - 0.5)) { covered = true; break; }
            }
            if (!covered) return false;
        }
        return true;
    }

    /** [v0.9.5] ¿'r' es parte del piso de la sala del trono (su cara de arriba es el borde de abajo de la sala)? */
    private boolean isHallFloor(Rectangle2D r) {
        return hall != null && Math.abs(r.getMinY() - hall.getMaxY()) < 0.5
                && r.getMaxX() > hall.getMinX() && r.getMinX() < hall.getMaxX();
    }

    /**
     * [v0.9.5] El fondo de la sala del trono y sus luces (los pétalos del rosetón y los braseros,
     * según la zona). Va detrás de todo lo demás del mundo, incluso del halo de Logi.
     */
    public void drawHall(Graphics2D g, double camX, double camY) {
        if (hallImage == null) return;
        if (hall.getMaxX() < camX || hall.getMinX() > camX + Config.WIDTH
                || hall.getMaxY() < camY || hall.getMinY() > camY + Config.HEIGHT) return;   // fuera de la pantalla
        g.drawImage(hallImage, (int) Math.round(hall.getX()), (int) Math.round(hall.getY()), null);
        ThroneHallArt.drawLights(g, hall, level.getHallPetals(), level.getHallBraziers(), level.getTime());
    }

    /** [v0.9.5] Lo quieto de la sala, una sola vez: imagen opaca, que se copia muy rápido. */
    private static BufferedImage renderHall(Rectangle2D hall) {
        BufferedImage img = new BufferedImage((int) Math.ceil(hall.getWidth()), (int) Math.ceil(hall.getHeight()), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = Background.smooth(img);
        g.translate(-hall.getX(), -hall.getY());
        ThroneHallArt.drawStatic(g, hall, false);
        g.dispose();
        return img;
    }

    /**
     * Avanza el tiempo y mece las plantas: Candil las empuja al pasar y luego regresan solas.
     * Además, los hongos se excitan si Logi está a menos de 110 px o Candil los roza (suben su
     * 'energy'), las briznas sueltan esporas cuando Candil corre entre ellas y las runas sueltan
     * chispas si Logi está cerca.
     */
    public void update(double dt, Player player, LightSource light, ParticleSystem particles) {
        time += dt;
        for (Mushroom m : mushrooms) {
            sway(m, player, dt);
            boolean touched = touches(player, m);
            double distLight = (light != null) ? Math.hypot(light.getX() - m.x, light.getY() - (m.y - m.height)) : 999;
            double targetEnergy = (touched || distLight < 110) ? 1.0 : 0.0;
            // Interpolación exponencial: energy se acerca a su meta una fracción por cuadro (dt·5), sin saltos
            m.energy += (targetEnergy - m.energy) * Math.min(1.0, dt * 5.0);

            if (particles != null && m.energy > 0.35 && Math.random() < dt * (touched ? 14.0 : 4.0)) {
                particles.emitBioSpore(m.x, m.y - m.height, m.cap);
            }
        }
        for (Tuft t : tufts) {
            sway(t, player, dt);
            if (particles != null && touches(player, t) && Math.abs(player.getVx()) > 80 && Math.random() < dt * 8.0) {
                particles.emitBioSpore(t.x, t.y - 4, MOSS_LIGHT);
            }
        }
        if (particles != null && light != null) {
            for (Rune r : runes) {
                double d = Math.hypot(light.getX() - r.x, light.getY() - r.y);
                if (d < RUNE_NEAR * 1.4 && Math.random() < dt * 4.5) {
                    particles.emitSpark(r.x, r.y);
                }
            }
        }
    }

    /** Versión sin luz ni partículas (solo mece las plantas). */
    public void update(double dt, Player player) {
        update(dt, player, null, null);
    }

    /**
     * Resorte amortiguado (oscilador armónico) integrado con Euler semi-implícito:
     *   velocidad += (K * (meta - ángulo) - C * velocidad) * dt
     *   ángulo    += velocidad * dt
     * Sin Candil, la meta es un vaivén suave de "aire". Si Candil la toca, la meta es doblarse
     * hacia donde él camina (o hacia afuera, si está quieto encima). Cuando se va, el resorte
     * la regresa con un pequeño bamboleo.
     */
    private void sway(Plant p, Player player, double dt) {
        double target = 0.05 * Math.sin(time * 1.4 + p.phase) + 0.03 * Math.sin(time * 0.6 + p.phase * 1.7);
        if (touches(player, p)) {
            double vx = player.getVx();
            target = vx != 0
                    ? Math.max(-1, Math.min(1, vx / Config.MOVE_SPEED)) * p.maxBend
                    : Math.signum(p.x - player.getCenterX()) * p.maxBend * 0.6;
        }
        p.speed += (STIFFNESS * (target - p.angle) - DAMPING * p.speed) * dt;
        p.angle += p.speed * dt;
    }

    /** ¿El cuerpo de Candil (con 6 px de holgura a los lados) toca la planta? */
    private static boolean touches(Player player, Plant p) {
        if (player == null || player.isDead()) return false;
        Rectangle2D b = player.getBounds();
        return p.x > b.getMinX() - 6 && p.x < b.getMaxX() + 6
            && b.getMaxY() > p.y - p.height - 4 && b.getMinY() < p.y + 2;
    }

    /** Decoración que va detrás de las sombras: columnas rotas. */
    public void drawBehind(Graphics2D g, double camX, double camY) {
        drawSprites(g, props, camX, camY);
    }

    /** Repisas, plantas, hongos, losas y runas. Logi enciende las runas cercanas. */
    public void drawFront(Graphics2D g, double camX, double camY, LightSource light) {
        drawSprites(g, platforms, camX, camY);

        Stroke oldStroke = g.getStroke();
        g.setStroke(BLADE_STROKE);
        for (Tuft t : tufts) {
            if (visible(t.x, t.y, camX, camY)) drawTuft(g, t);
        }
        g.setStroke(oldStroke);

        Composite old = g.getComposite();
        for (Mushroom m : mushrooms) {
            if (!visible(m.x, m.y, camX, camY)) continue;
            Vec2 cap = drawMushroom(g, m);
            // Pulso lento; si el hongo está excitado, brilla más y su halo crece (matriz T · S sobre la imagen)
            float basePulse = (float) (0.50 + 0.25 * Math.sin(time * 1.3 + m.phase));
            float a = Math.min(1.0f, (float) (basePulse + 0.45 * m.energy));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
            if (m.energy > 0.02) {
                AffineTransform prev = g.getTransform();
                double scale = 1.0 + 0.45 * m.energy;
                g.translate(cap.x, cap.y);
                g.scale(scale, scale);
                drawCentered(g, m.glow, 0, 0);
                g.setTransform(prev);
            } else {
                drawCentered(g, m.glow, cap.x, cap.y);
            }
            g.setComposite(old);
        }

        drawSprites(g, blocks, camX, camY);
        for (Rune r : runes) {
            // Entre más cerca está Logi, más brilla: 1 a RUNE_NEAR px o menos, 0 a RUNE_FAR o más
            double d = Math.hypot(light.getX() - r.x, light.getY() - r.y);
            double near = Math.max(0, Math.min(1, (RUNE_FAR - d) / (RUNE_FAR - RUNE_NEAR)));
            if (near < 0.01) continue;
            float a = (float) (near * (0.8 + 0.2 * Math.sin(time * 2.4 + r.phase)));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
            drawCentered(g, runeGlow, r.x, r.y);
        }
        g.setComposite(old);

        // Cambiar intensidad de luz
        drawRimLighting(g, camX, camY, light);
    
    }

    /** ¿La base (x, y) de una planta queda en la pantalla, con 40 px de holgura? [B1] También en Y. */
    private static boolean visible(double x, double y, double camX, double camY) {
        return x > camX - 40 && x < camX + Config.WIDTH + 40
            && y > camY - 40 && y < camY + Config.HEIGHT + 40;
    }

    private static void drawCentered(Graphics2D g, BufferedImage img, double x, double y) {
        g.drawImage(img, (int) Math.round(x - img.getWidth() / 2.0), (int) Math.round(y - img.getHeight() / 2.0), null);
    }

    private static void drawSprites(Graphics2D g, List<Sprite> sprites, double camX, double camY) {
        for (Sprite s : sprites) {
            if (s.x + s.img.getWidth() < camX || s.x > camX + Config.WIDTH) continue;   // fuera de la pantalla
            if (s.y + s.img.getHeight() < camY || s.y > camY + Config.HEIGHT) continue;  // [B1] arriba o abajo
            g.drawImage(s.img, (int) Math.round(s.x), (int) Math.round(s.y), null);
        }
    }

    // =========================================================
    //  PLANTAS (se dibujan en cada cuadro)
    // =========================================================

    /**
     * Hongo girado alrededor de la base de su tallo: T(base) * R(ángulo) * T(-base).
     * Regresa dónde quedó el sombrero, para dibujar ahí su brillo.
     */
    private static Vec2 drawMushroom(Graphics2D g, Mushroom m) {
        Mat3 rot = Mat3.translation(m.x, m.y).multiply(Mat3.rotation(m.angle)).multiply(Mat3.translation(-m.x, -m.y));
        AffineTransform old = g.getTransform();
        g.transform(rot.toAffineTransform());
        double stem = m.height, cw = m.capW;
        g.setColor(STEM);
        g.fill(new RoundRectangle2D.Double(m.x - 1.2, m.y - stem, 2.4, stem + 3, 2, 2));
        g.setColor(m.cap);
        g.fill(new Arc2D.Double(m.x - cw / 2, m.y - stem - cw * 0.35, cw, cw * 0.7, 0, 180, Arc2D.CHORD));
        g.setColor(CAP_SHINE);
        g.fill(new Ellipse2D.Double(m.x - cw * 0.15, m.y - stem - cw * 0.28, cw * 0.3, cw * 0.14));
        g.setTransform(old);
        return rot.apply(new Vec2(m.x, m.y - stem));
    }

    /** Cada brizna es una curva: la punta gira el ángulo completo y el punto de control solo 40%. */
    private static void drawTuft(Graphics2D g, Tuft t) {
        g.setColor(t.color);
        for (int i = 0; i < t.heights.length; i++) {
            double a = t.angle + t.offsets[i], h = t.heights[i];
            double bx = t.x + t.dx[i], by = t.y + 1;
            double cx = bx + Math.sin(a * 0.4) * h * 0.55, cy = by - Math.cos(a * 0.4) * h * 0.55;
            g.draw(new QuadCurve2D.Double(bx, by, cx, cy, bx + Math.sin(a) * h, by - Math.cos(a) * h));
        }
    }

    // =========================================================
    //  REPISAS DE ROCA (imágenes que se dibujan una sola vez)
    // =========================================================

    private Sprite renderPlatform(Rectangle2D r, Random rnd, boolean teal, boolean buried) {
        int w = (int) Math.ceil(r.getWidth()), h = (int) Math.ceil(r.getHeight());
        BufferedImage img = Background.newImage(w + 2 * SIDE, h + TOP);
        Graphics2D g = Background.smooth(img);
        g.translate(SIDE, TOP);   // desde aquí, (0, 0) es la esquina de arriba a la izquierda de la repisa

        double[] left = insets(rnd), right = insets(rnd);
        Path2D body = rockBody(w, h, left, right);
        g.setPaint(new GradientPaint(0, 0, ROCK_TOP, 0, 180, ROCK_BOTTOM));
        g.fill(body);

        // Textura, recortada a la forma de la roca
        Shape oldClip = g.getClip();
        g.clip(body);
        g.setStroke(new BasicStroke(1.2f));
        g.setColor(STRATA);
        for (double y = 16 + rnd.nextDouble() * 10; y < h; y += 22 + rnd.nextDouble() * 12) {
            Path2D vein = new Path2D.Double();
            double phase = rnd.nextDouble() * 10;
            vein.moveTo(0, y);
            for (double x = 0; x <= w + 24; x += 24) vein.lineTo(x, y + Math.sin(x * 0.03 + phase) * 2.5);
            g.draw(vein);
        }
        for (int k = 0; k < w * Math.min(h, 220) / 4500; k++) {
            double pw = 6 + rnd.nextDouble() * 12, ph = 4 + rnd.nextDouble() * 6;
            Ellipse2D pebble = new Ellipse2D.Double(rnd.nextDouble() * w, 8 + rnd.nextDouble() * Math.min(h, 220), pw, ph);
            g.setColor(PEBBLE);
            g.fill(pebble);
            g.setColor(PEBBLE_LINE);
            g.draw(pebble);
        }
        g.setColor(CRACK);
        g.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int k = 0; k < 2 + w / 150; k++) {
            boolean fromTop = rnd.nextBoolean();
            double x = fromTop ? 10 + rnd.nextDouble() * (w - 20) : (rnd.nextBoolean() ? 2 : w - 2);
            double y = fromTop ? 0 : 10 + rnd.nextDouble() * Math.min(h - 10, 120);
            drawCrack(g, rnd, x, y, 30 + rnd.nextDouble() * 80, true);
        }
        g.setClip(oldClip);

        // Borde claro arriba: aquí se puede pisar. [v0.9.5] Si otras repisas tapan toda la cara de
        // arriba ('buried'), ni borde ni musgo: el musgo se calcula igual, sin pintarlo (para no
        // cambiar el resto del decorado), y sus matas y hongos se quitan al final (inRock).
        if (!buried) {
            g.setColor(RIM);
            g.setStroke(new BasicStroke(2f));
            g.draw(new Line2D.Double(left[0] + 4, 1, w - right[0] - 4, 1));
        }

        drawMoss(g, rnd, r, left[0], right[0], !buried);
        for (double x = 26 + rnd.nextDouble() * 60; x < w - 20; x += 110 + rnd.nextDouble() * 120) {
            addMushrooms(rnd, r, x, teal);
        }
        g.dispose();
        return new Sprite(img, r.getX() - SIDE, r.getY() - TOP);
    }

    /**
     * [v0.9.5] Una losa del piso de la sala del trono: piedra labrada como las losas, no roca. Su
     * cara de enfrente (los 10 px de grueso) tiene el borde claro arriba (aquí se pisa), juntas
     * entre losas y una moldura, con una sombra suave debajo; el piso que se aleja hacia la pared
     * lo dibuja el fondo de la sala (ThroneHallArt). Sin musgo ni hongos.
     */
    private static Sprite renderHallFloor(Rectangle2D r) {
        int w = (int) Math.ceil(r.getWidth()), h = (int) Math.ceil(r.getHeight()), below = 8;
        BufferedImage img = Background.newImage(w, h + below);
        Graphics2D g = Background.smooth(img);
        g.setPaint(new GradientPaint(0, 0, STONE_TOP, 0, h, STONE_BOTTOM));
        g.fill(new Rectangle2D.Double(0, 0, w, h));
        g.setPaint(new GradientPaint(0, h, new Color(0, 0, 0, 120), 0, h + below, new Color(0, 0, 0, 0)));
        g.fill(new Rectangle2D.Double(0, h, w, below));
        g.setStroke(new BasicStroke(1f));
        g.setColor(CARVED);
        g.draw(new Line2D.Double(0, h * 0.62, w, h * 0.62));   // la moldura
        g.setColor(STONE_DARK);
        g.draw(new Line2D.Double(0, h - 0.5, w, h - 0.5));
        for (double x = 50 - (r.getX() % 50); x < w; x += 50) {   // juntas cada 50 px del mundo
            g.setColor(CARVED);
            g.draw(new Line2D.Double(x, 2, x, h));
            g.setColor(Background.alpha(STONE_LIGHT, 70));
            g.draw(new Line2D.Double(x + 1, 3, x + 1, h * 0.6));
        }
        g.setColor(STONE_EDGE);   // las puntas
        g.draw(new Line2D.Double(0.5, 0, 0.5, h));
        g.draw(new Line2D.Double(w - 0.5, 0, w - 0.5, h));
        g.setColor(RIM);   // aquí se puede pisar
        g.setStroke(new BasicStroke(2f));
        g.draw(new Line2D.Double(0, 1, w, 1));
        g.dispose();
        return new Sprite(img, r.getX(), r.getY());
    }

    /** Cuánto se mete cada lado de la roca (0 a 7 px), con el mismo terreno fractal del fondo. */
    private static double[] insets(Random rnd) {
        double[] d = Background.midpointDisplacement(rnd, 16, 1, 0.6);
        for (int i = 0; i < d.length; i++) d[i] = Math.max(0, Math.min(7, 3.5 + 3 * d[i]));
        return d;
    }

    /** La forma de la roca: arriba es recta (donde se pisa), los lados son irregulares. */
    private static Path2D rockBody(double w, double h, double[] left, double[] right) {
        int n = left.length - 1;
        Path2D p = new Path2D.Double();
        p.moveTo(left[0] + 5, 0);
        p.lineTo(w - right[0] - 5, 0);
        p.quadTo(w - right[0], 0, w - right[0], 5);   // esquina redondeada
        for (int i = 1; i <= n; i++) p.lineTo(w - right[i], h * i / n);
        for (int i = n; i >= 1; i--) p.lineTo(left[i], h * i / n);
        p.lineTo(left[0], 5);
        p.quadTo(left[0], 0, left[0] + 5, 0);
        p.closePath();
        return p;
    }

    /** Grieta: una caminata al azar hacia abajo; a veces se parte en dos. */
    private static void drawCrack(Graphics2D g, Random rnd, double x, double y, double len, boolean canBranch) {
        Path2D p = new Path2D.Double();
        p.moveTo(x, y);
        double angle = Math.PI / 2 + (rnd.nextDouble() - 0.5) * 0.8;
        for (double walked = 0; walked < len; ) {
            double seg = 6 + rnd.nextDouble() * 10;
            angle = Math.max(0.35, Math.min(Math.PI - 0.35, angle + (rnd.nextDouble() - 0.5) * 0.9));
            x += Math.cos(angle) * seg;
            y += Math.sin(angle) * seg;
            p.lineTo(x, y);
            walked += seg;
            if (canBranch && rnd.nextDouble() < 0.15) drawCrack(g, rnd, x, y, len * 0.4, false);
        }
        g.draw(p);
    }

    /**
     * Musgo en manchas sobre la orilla (con huecos para que se vea el borde), matas de briznas
     * que se mecen sobre las manchas, y hebras que cuelgan por los lados. [v0.9.5] Con 'paint' en
     * false no pinta nada, pero saca los mismos números al azar (y agrega las mismas matas).
     */
    private void drawMoss(Graphics2D g, Random rnd, Rectangle2D r, double left0, double right0, boolean paint) {
        double w = r.getWidth();
        for (double x = left0 + 2; x < w - right0 - 6; ) {
            double patch = 20 + rnd.nextDouble() * 60;
            double end = Math.min(x + patch, w - right0 - 2);
            for (double px = x; px < end; px += 3 + rnd.nextDouble() * 4) {
                double mw = 5 + rnd.nextDouble() * 8, mh = 2 + rnd.nextDouble() * 4;
                Color moss = mix(MOSS_DARK, MOSS_LIGHT, rnd.nextDouble() * 0.5);
                if (paint) {
                    g.setColor(moss);
                    g.fill(new Ellipse2D.Double(px - mw / 2, -mh, mw, mh + 5));
                }
            }
            for (double px = x + 4 + rnd.nextDouble() * 8; px < end - 2; px += 14 + rnd.nextDouble() * 16) {
                addTuft(rnd, r.getX() + px, r.getY());
            }
            x += patch + 10 + rnd.nextDouble() * 50;
        }
        // Hebras que cuelgan por los dos lados, desde la orilla de arriba
        Stroke old = g.getStroke();
        g.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int side = 0; side < 2; side++) {
            double baseX = side == 0 ? left0 : w - right0;
            for (int k = 0; k < 2 + rnd.nextInt(3); k++) {
                double x = baseX + (side == 0 ? -1 : 1) * rnd.nextDouble() * 3;
                double len = 14 + rnd.nextDouble() * 50, sway = 2 + rnd.nextDouble() * 4;
                Path2D strand = new Path2D.Double();
                strand.moveTo(x, 1);
                strand.curveTo(x + sway, len * 0.33, x - sway, len * 0.66, x + sway * 0.5, len);
                Color moss = rnd.nextBoolean() ? MOSS_DARK : mix(MOSS_DARK, MOSS_LIGHT, 0.4);
                if (paint) {
                    g.setColor(moss);
                    g.draw(strand);
                }
            }
        }
        g.setStroke(old);
    }

    private void addTuft(Random rnd, double x, double y) {
        Tuft t = new Tuft();
        int n = 3 + rnd.nextInt(3);
        t.x = x;
        t.y = y;
        t.dx = new double[n];
        t.heights = new double[n];
        t.offsets = new double[n];
        for (int i = 0; i < n; i++) {
            t.dx[i] = (rnd.nextDouble() - 0.5) * 6;
            t.heights[i] = 4 + rnd.nextDouble() * 7;
            t.offsets[i] = (rnd.nextDouble() - 0.5) * 0.7;
            t.height = Math.max(t.height, t.heights[i]);
        }
        t.maxBend = 0.8;
        t.phase = rnd.nextDouble() * Math.PI * 2;
        t.color = mix(MOSS_DARK, MOSS_LIGHT, 0.55 + rnd.nextDouble() * 0.45);
        tufts.add(t);
    }

    /** Un grupo de 1 a 3 hongos con sombrero luminoso. */
    private void addMushrooms(Random rnd, Rectangle2D r, double cx, boolean teal) {
        int n = 1 + rnd.nextInt(3);
        for (int k = 0; k < n; k++) {
            Mushroom m = new Mushroom();
            m.x = r.getX() + cx + (k - (n - 1) / 2.0) * (7 + rnd.nextDouble() * 4);
            m.y = r.getY();
            m.height = 5 + rnd.nextDouble() * 11;
            m.capW = 6 + rnd.nextDouble() * 7;
            m.maxBend = 0.45;
            m.phase = rnd.nextDouble() * Math.PI * 2;
            m.cap = teal ? CAP_TEAL : CAP_BLUE;
            m.glow = teal ? glowTeal : glowBlue;
            mushrooms.add(m);
        }
    }

    // =========================================================
    //  LOSAS, COLUMNAS Y BRILLOS
    // =========================================================

    /** Losa de piedra tallada: bisel, marco grabado, una grieta y la runa del Gran Farol. */
    private static Sprite renderBlock(Rectangle2D r, Random rnd) {
        int w = (int) Math.round(r.getWidth()), h = (int) Math.round(r.getHeight()), pad = 2;
        BufferedImage img = Background.newImage(w + 2 * pad, h + 2 * pad);
        Graphics2D g = Background.smooth(img);
        g.translate(pad, pad);

        RoundRectangle2D body = new RoundRectangle2D.Double(0, 0, w, h, 6, 6);
        g.setPaint(new GradientPaint(0, 0, STONE_TOP, 0, h, STONE_BOTTOM));
        g.fill(body);
        g.setStroke(new BasicStroke(2f));
        g.setColor(STONE_LIGHT);                       // luz arriba y a la izquierda
        g.draw(new Line2D.Double(3, 1.5, w - 3, 1.5));
        g.draw(new Line2D.Double(1.5, 3, 1.5, h - 3));
        g.setColor(STONE_DARK);                        // sombra abajo y a la derecha
        g.draw(new Line2D.Double(3, h - 1.5, w - 3, h - 1.5));
        g.draw(new Line2D.Double(w - 1.5, 3, w - 1.5, h - 3));

        g.setStroke(new BasicStroke(1f));
        g.setColor(CARVED);
        g.draw(new RoundRectangle2D.Double(5, 5, w - 10, h - 10, 4, 4));

        Shape oldClip = g.getClip();
        g.clip(body);
        g.setColor(CRACK);
        g.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawCrack(g, rnd, rnd.nextBoolean() ? w * 0.2 : w * 0.8, 0, h * 0.7, false);
        g.setClip(oldClip);

        // Runa grabada: una línea clara debajo de la oscura da efecto de tallado
        Path2D rune = rune(w / 2.0, h / 2.0, RUNE_SIZE);
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(Background.alpha(STONE_LIGHT, 90));
        g.translate(0, 1);
        g.draw(rune);
        g.translate(0, -1);
        g.setColor(CARVED);
        g.draw(rune);

        g.setStroke(new BasicStroke(1f));
        g.setColor(STONE_EDGE);
        g.draw(body);
        g.dispose();
        return new Sprite(img, r.getX() - pad, r.getY() - pad);
    }

    /** La runa: el rombo del Gran Farol, atravesado por una línea. */
    private static Path2D rune(double cx, double cy, double s) {
        Path2D p = new Path2D.Double();
        p.moveTo(cx, cy - s);
        p.lineTo(cx + s * 0.6, cy);
        p.lineTo(cx, cy + s);
        p.lineTo(cx - s * 0.6, cy);
        p.closePath();
        p.moveTo(cx, cy - s - 4);
        p.lineTo(cx, cy + s + 4);
        return p;
    }

    /** Columna rota oscura, de pie sobre una repisa ancha. Es solo decoración. */
    private static Sprite renderColumn(Rectangle2D r, Random rnd) {
        double colW = 26 + rnd.nextDouble() * 10, colH = 70 + rnd.nextDouble() * 70;
        double cx = r.getX() + 60 + rnd.nextDouble() * (r.getWidth() - 140);
        int iw = (int) Math.ceil(colW + 24), ih = (int) Math.ceil(colH + 10);
        BufferedImage img = Background.newImage(iw, ih);
        Graphics2D g = Background.smooth(img);
        g.setColor(PROP);
        g.fill(Background.column(iw / 2.0, ih - 2, colW, colH, rnd, true));
        g.dispose();
        return new Sprite(img, cx - iw / 2.0, r.getY() + 8 - ih);
    }

    /** Brillo suave de un color, en una imagen que se reutiliza. */
    private static BufferedImage renderGlow(Color c, int radius) {
        BufferedImage img = Background.newImage(radius * 2, radius * 2);
        Graphics2D g = Background.smooth(img);
        g.setPaint(new RadialGradientPaint(new Point2D.Double(radius, radius), radius,
                new float[] { 0f, 1f }, new Color[] { Background.alpha(c, 110), Background.alpha(c, 0) }));
        g.fillRect(0, 0, radius * 2, radius * 2);
        g.dispose();
        return img;
    }

    /** La runa encendida: un halo cálido con el trazo de la runa en color de Logi. */
    private static BufferedImage renderRuneGlow() {
        int s = 48;
        BufferedImage img = Background.newImage(s, s);
        Graphics2D g = Background.smooth(img);
        g.setPaint(new RadialGradientPaint(new Point2D.Double(s / 2.0, s / 2.0), s / 2f,
                new float[] { 0f, 1f }, new Color[] { Background.alpha(RUNE, 90), Background.alpha(RUNE, 0) }));
        g.fillRect(0, 0, s, s);
        g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(RUNE);
        g.draw(rune(s / 2.0, s / 2.0, RUNE_SIZE));
        g.dispose();
        return img;
    }

    /** Interpolación lineal entre dos colores. */
    private static Color mix(Color a, Color b, double t) {
        return new Color(
            (int) Math.round(a.getRed() + (b.getRed() - a.getRed()) * t),
            (int) Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * t),
            (int) Math.round(a.getBlue() + (b.getBlue() - a.getBlue()) * t));
    }
    // =========================================================
    //  ILUMINACIÓN DE BORDE (RIM LIGHTING) DINÁMICA
    // =========================================================

    private void drawRimLighting(Graphics2D g, double camX, double camY, LightSource light) {
        if (light == null) return;
        double lx = light.getX();
        double ly = light.getY();
        double maxDist = 240.0;

        Composite oldComp = g.getComposite();
        Stroke oldStroke = g.getStroke();

        double dark = light.getDarkness();
        Color rimGlow = mix(new Color(255, 195, 110), new Color(220, 45, 45), dark);
        Color rimCore = mix(new Color(255, 240, 195), new Color(255, 130, 110), dark);

        if (light.isPinned()) {
            rimGlow = mix(rimGlow, new Color(130, 195, 255), 0.65);
            rimCore = mix(rimCore, new Color(215, 240, 255), 0.65);
        }

        Stroke glowStroke = new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        Stroke coreStroke = new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        // 1. Repisas pisables: solo la cara superior recibe luz de borde (evita rayas en muros laterales)
        for (Rectangle2D r : level.getPlatforms()) {
            if (hall != null && buried(level, r)) continue;
            paintEdgeRim(g, r, lx, ly, camX, camY, maxDist, rimGlow, rimCore, glowStroke, coreStroke, true);
        }

        // 2. Bloques / losas flotantes: iluminan todas sus caras orientadas hacia Logi
        for (Block b : level.getBlocks()) {
            paintEdgeRim(g, b.getBounds(), lx, ly, camX, camY, maxDist, rimGlow, rimCore, glowStroke, coreStroke, false);
        }

        g.setStroke(oldStroke);
        g.setComposite(oldComp);
    }

    private static void paintEdgeRim(Graphics2D g, Rectangle2D r, double lx, double ly,
                                     double camX, double camY, double maxDist,
                                     Color rimGlow, Color rimCore,
                                     Stroke glowStroke, Stroke coreStroke, boolean topOnly) {
        if (r.getMaxX() < camX || r.getMinX() > camX + Config.WIDTH
                || r.getMaxY() < camY || r.getMinY() > camY + Config.HEIGHT) return;

        double closestX = Math.max(r.getMinX(), Math.min(lx, r.getMaxX()));
        double closestY = Math.max(r.getMinY(), Math.min(ly, r.getMaxY()));
        double dist = Math.hypot(lx - closestX, ly - closestY);

        if (dist >= maxDist || dist < 1.0) return;

        double t = 1.0 - (dist / maxDist);
        float intensity = (float) (t * t);

        double span = Math.sqrt(Math.max(0, maxDist * maxDist - dist * dist));
        double x1 = Math.max(r.getMinX() + 3.0, lx - span);
        double x2 = Math.min(r.getMaxX() - 3.0, lx + span);
        double y1 = Math.max(r.getMinY() + 1.0, ly - span);
        double y2 = Math.min(r.getMaxY() - 1.0, ly + span);

        boolean topVisible    = ly < r.getMinY() && x2 > x1;
        boolean bottomVisible = !topOnly && ly > r.getMaxY() && x2 > x1;
        boolean leftVisible   = !topOnly && lx < r.getMinX() && y2 > y1;
        boolean rightVisible  = !topOnly && lx > r.getMaxX() && y2 > y1;

        if (!topVisible && !bottomVisible && !leftVisible && !rightVisible) return;

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, intensity * 0.35f)));
        g.setColor(rimGlow);
        g.setStroke(glowStroke);
        if (topVisible)    g.draw(new Line2D.Double(x1, r.getMinY() + 1.0, x2, r.getMinY() + 1.0));
        if (bottomVisible) g.draw(new Line2D.Double(x1, r.getMaxY() - 1.0, x2, r.getMaxY() - 1.0));
        if (leftVisible)   g.draw(new Line2D.Double(r.getMinX() + 1.0, y1, r.getMinX() + 1.0, y2));
        if (rightVisible)  g.draw(new Line2D.Double(r.getMaxX() - 1.0, y1, r.getMaxX() - 1.0, y2));

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, intensity * 0.75f)));
        g.setColor(rimCore);
        g.setStroke(coreStroke);
        if (topVisible)    g.draw(new Line2D.Double(x1, r.getMinY() + 1.0, x2, r.getMinY() + 1.0));
        if (bottomVisible) g.draw(new Line2D.Double(x1, r.getMaxY() - 1.0, x2, r.getMaxY() - 1.0));
        if (leftVisible)   g.draw(new Line2D.Double(r.getMinX() + 1.0, y1, r.getMinX() + 1.0, y2));
        if (rightVisible)  g.draw(new Line2D.Double(r.getMaxX() - 1.0, y1, r.getMaxX() - 1.0, y2));
    }
}
