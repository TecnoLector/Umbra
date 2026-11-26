package umbra.core;

/**
 * Constantes globales del juego.
 * Si algo se siente lento, rápido o raro, normalmente se ajusta aquí.
 */
public final class Config {

    private Config() { }

    public static final String TITLE = "Umbra";
    /** Cámbiala en cada versión publicada: v0.3 (30%), v0.7 (70%), v1.0 (entrega en la materia)... */
    public static final String VERSION = "v0.9.7";

    /** true: el juego empieza con la intro y luego el menú. Ponlo en false para entrar directo al nivel mientras pruebas. */
    public static final boolean SHOW_INTRO = true;

    // ---- Ventana ----
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    // ---- Game loop ----
    /** Actualizaciones de lógica por segundo (paso de tiempo fijo). */
    public static final int UPS = 60;

    // ---- Física del jugador (píxeles y segundos) ----
    public static final double GRAVITY = 1800;        // px/s^2
    public static final double MOVE_SPEED = 260;      // px/s
    public static final double JUMP_SPEED = 620;      // px/s
    public static final double MAX_FALL_SPEED = 900;  // px/s
    /** Velocidad al flotar dentro de una sombra (W sube, S baja). */
    public static final double SWIM_SPEED = 220;      // px/s

    // ---- Vida de Candil [v1.0] ----
    /** Segundos que aguanta fuera de la sombra, con la vida llena, antes de desmoronarse. */
    public static final double LIFE_DRAIN_TIME = 6.0;
    /** Segundos que tarda en recuperar toda la vida dentro de una sombra. */
    public static final double LIFE_REGEN_TIME = 2.0;
    /** Si el centro de Candil queda a esta distancia de la luz (px) o menos, se desmorona. */
    public static final double CRUMBLE_RADIUS = 70;
    /** Segundos que pasan entre que se desmorona y reaparece. */
    public static final double RESPAWN_DELAY = 0.8;

    // ---- Música [v1.0] ----
    /** Archivo de música (dentro de src). Para cambiar la música, reemplaza ese archivo. */
    public static final String MUSIC_FILE = "/umbra/res/ambiente.wav";
    /** Volumen de la música, de 0 (silencio) a 1 (volumen original del archivo). */
    public static final double MUSIC_VOLUME = 0.6;
    /** Segundos que tarda en subir el volumen al empezar (entrada suave). */
    public static final double MUSIC_FADE_TIME = 3.0;

    // ---- Luz ----
    /** Qué tan rápido la luz alcanza al ratón (mayor = más rápido). */
    public static final double LIGHT_SMOOTHING = 8.0;
    /** Radio del halo de luz en pantalla. */
    public static final double LIGHT_RADIUS = 520;

    // ---- Avances ----
    /**
     * Puente provisional del nivel de prueba, para probar el movimiento en el avance 30%.
     * Desde el avance 70% está en false: las sombras ya son sólidas.
     */
    public static final boolean TEMP_BRIDGE = false;
}
