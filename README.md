# Umbra

Juego de plataformas 2D en Java donde **la sombra es el único refugio**. Con el ratón mueves a **Logi**, una pequeña llama; las sombras que proyecta son el camino.

## La historia

Bajo la tierra existió un mundo de cavernas iluminado por el **Gran Farol**. Una noche el Farol se apagó y su luz se rompió en mil pedazos. La oscuridad llenó todo y se llamó **Umbra**.

De la última chispa nacieron dos: **Candil**, un pequeño fantasma que puede andar pero apenas brilla, y **la Llama(Logi)**, una luz que alumbra lejos pero no puede andar. Solos no llegan a ningún lado. Juntos tienen una oportunidad.

Pero Candil es frágil. El vacío lo apaga poco a poco, y la luz directa lo deshace: si la Llama(Logi) se le acerca demasiado, se desmorona. Solo la sombra lo abriga. Cuando la Llama(Logi) ilumina las cosas nacen sombras, y dentro de ellas Candil flota a salvo.

Así avanzan juntos, caverna por caverna: la Llama(Logi) abre caminos de sombra y Candil los recorre, buscando el Gran Farol para volver a encenderlo. En las losas antiguas todavía quedan runas del Farol, y despiertan cuando la Llama(Logi) pasa cerca.

## Capturas

**La historia (la intro)**

![La sala dorada, con el Gran Farol encendido](capturas/intro-1-sala-dorada.jpg)
Escena 1: el Gran Farol, encendido en su sala dorada.

![El Gran Farol se rompe y los pedazos salen volando](capturas/intro-2-se-rompe.png)
Escena 2: el Farol se apaga y se rompe; sus pedazos salen volando.

![El Farol apagado y la cueva a oscuras, con Candil y Logi](capturas/intro-3-apagado.png)
Escena 6: el Farol apagado cuelga en la oscuridad; Candil y Logi empiezan el camino.

**El menú**

![El menú con las tres partidas](capturas/menu.png)
Tres espacios de partida: cada uno dice la zona, los fragmentos y el tiempo.

**El mundo**

![Las Raíces: Candil flotando en una sombra, con Logi fijo](capturas/mundo-1-raices.png)
Las Raíces: Logi, fijo con un clic, proyecta la sombra de un bloque y Candil flota dentro de ella.

![Las Galerías](capturas/mundo-2-galerias.png)
Las Galerías: la sombra de un bloque es el puente que Candil cruza.

![Los Pozos](capturas/mundo-3-pozos.png)
Los Pozos: columnas y tiros profundos, con la sombra de Logi como único camino.

![El mapa grande](capturas/mundo-4-mapa.png)
El mapa grande (Tab): las zonas recorridas se iluminan, lo demás sigue en neblina y los fragmentos tomados quedan marcados.

![El mapa grande, con toda la cueva](capturas/mundo-4b-mapa-completo.png)
Toda la cueva: las cuatro zonas reveladas, la Cúpula incluida, y los 6 fragmentos tomados.

![Un fragmento vuela hacia Candil](capturas/mundo-5-fragmento.png)
Al tomar un fragmento, vuela hacia Candil y aparece una línea de la historia; el contador sube.

**Logi se resiste**

![Logi con 3 fragmentos y con 6, lado a lado](capturas/logi-1-tres-y-seis.png)
Con 3 fragmentos (izquierda) Logi se pone rojo; con 6 (derecha), de un rojo muy oscuro y con los ojos claros.

![Un arrebato: Logi tira hacia Candil](capturas/logi-2-arrebato.png)
En pleno arrebato: Logi, rojo, ha tirado hasta quedar junto a Candil. Ya no hay sombra que lo cubra, y la barra de vida baja con la luz.

**La sala del trono**

![El Gran Farol con sus copas encendidas](capturas/sala-trono.png)
La Cúpula: el Gran Farol, con sus copas encendidas, sobre el estrado.

<details>
<summary>El final (contiene spoilers)</summary>

![La espera con el destello del Farol](capturas/final-1-espera.png)
Al tocar el Farol hay una espera: el corazón de vidrio y las copas dan un destello.

![La llama negra encerrada en el corazón del Farol](capturas/final-2-corazon.png)
Con los 6 fragmentos, la llama negra queda encerrada en el corazón de vidrio.

![La llama negra escapa por un brazo sin fragmento](capturas/final-3-escapa.png)
Si faltan fragmentos, la llama negra escapa por un brazo del Farol que quedó apagado.

![La pantalla final](capturas/final-4-pantalla.png)
La pantalla final: el cierre de la historia, con el tiempo y las veces que Candil se desmoronó.

</details>

---

Proyecto final de **Graficación**.

- **Autores:** Maria Magdalena Rivera Ramirez y Raul Omar Jurado Camargo

---

## Cómo se juega

- **Dentro de una sombra** Candil está a salvo: flota sin gravedad (W o Espacio para subir, S para bajar) y recupera vida.
- **Fuera de la sombra** camina y salta, pero pierde vida (la barra de arriba a la derecha, debajo del mapa circular) y se va deshaciendo en polvo de luz.
- **Si la Llama(Logi) se acerca demasiado**, Candil se desmorona.
- Mueve la Llama(Logi) con el ratón y **haz clic para fijar a Logi**: así no se mueve mientras avanzas. Otro clic lo suelta.
- Si sales de una sombra nadando hacia arriba, **sales de un salto**: sirve para alcanzar repisas altas.
- Si te desmoronas o caes al vacío, reapareces en la última repisa firme que pisaste.

**La cueva.** El juego es una cueva de cuatro zonas conectadas hacia los lados, hacia arriba y hacia abajo: **Las Raíces**, **Las Galerías**, **Los Pozos** y **la Cúpula**. Al llegar a la orilla de una zona pasas a la siguiente, y también puedes regresar. Algunas zonas son más altas que la pantalla: la cámara te sigue al subir y al caer. Arriba de todo, en la Cúpula, cuelga el **Gran Farol** apagado; tocarlo es el final.

**Los fragmentos del Gran Farol.** En la cueva hay 6 fragmentos, dos por zona: uno en el camino y otro escondido. Al tomar uno, vuela hacia Candil y aparece una línea de la historia, y un contador en pantalla muestra cuántos llevas. Cada fragmento llena un hueco del Gran Farol y lo hace brillar más. El final sale aunque falten, y su texto cambia según cuántos juntaste.

**El mapa.** Arriba a la derecha hay un mapa circular que siempre muestra dónde estás. Con **Tab** se abre el mapa grande, con toda la cueva. Lo que no has recorrido está cubierto de neblina, lo que ya recorriste se ilumina con la luz de Logi, y ahí se ven los fragmentos que ya tomaste.

**Guardar la partida.** El menú tiene 3 espacios de partida. Elige uno con **W/S** o con el ratón y pulsa **Enter**: si está vacío empieza una partida nueva, y si no, sigue donde la dejaste. **Supr** borra una partida, siempre preguntando antes. El juego guarda solo al cruzar a otra zona, al volver al menú con **Esc** y al cerrar la ventana. Al continuar, Candil aparece en la última repisa firme que pisó. Después del final, la partida queda frente a la Cúpula. Los archivos van en `%APPDATA%\Umbra\`.

**Logi se resiste.** Con cada fragmento que juntas, Logi te sigue más despacio y se oscurece: de naranja a rojo y, con los 6, a un rojo muy oscuro; el negro total de la llama queda para el final. Si dejas el ratón quieto un momento, se acerca solo a Candil. Y a ratos tiene un arrebato: la llama parpadea en rojo para avisar, se queda quieto un instante (el clic no lo fija) y luego tira hacia Candil: te deja sin sombra y su luz te quita vida poco a poco. Si Candil flotaba en una sombra sobre un hueco, puede caer. Logi se detiene a unos pasos, sin llegar a tocarlo: lo que daña es la luz, no el tirón. Con más fragmentos los arrebatos son más seguidos y más largos. Fijar a Logi con un clic te protege: mientras esté fijo no hay arrebato, y al soltarlo tienes un momento antes del siguiente. Sin fragmentos, Logi se comporta como siempre.

**Candil y Logi cambian de forma.** Candil es un fantasma encapuchado y tiene una forma para cada acción: descansa, corre, salta, cae y flota. Logi también cambia: se estira como cometa cuando lo mueves rápido, se enrosca en espiral al subir y se calma cuando está quieto.

## Cómo ejecutarlo

**Sin Java (Windows, lo más fácil):** en [Releases](https://github.com/TecnoLector/Umbra/releases), descarga `Umbra-vX-Windows.zip` de la versión más reciente. Descomprímelo («Extraer todo») y abre `Umbra.exe` con doble clic dentro de la carpeta `Umbra`. Ya trae su propio Java, así que no hay que instalar nada. Si Windows muestra «Windows protegió su PC», elige **Más información** y luego **Ejecutar de todas formas**: aparece porque el programa no está firmado.

**Con Java:** se necesita **Java 8 o superior** (recomendado 17 o 21). No usa librerías externas, solo Java2D (Swing) y `javax.sound` para el audio. Descarga `Umbra-vX.jar` de la versión más reciente en [Releases](https://github.com/TecnoLector/Umbra/releases) y ábrelo con doble clic, o con `java -jar Umbra-vX.jar`. Esta opción también sirve en Linux y macOS.

**Desde un IDE**
- **IntelliJ IDEA:** `File > Open` y elige la carpeta del proyecto. Si no detecta el código, clic derecho en `src` > `Mark Directory as > Sources Root`. Ejecuta `umbra.Main`.
- **NetBeans:** `File > New Project > Java with Ant > Java Project with Existing Sources`, agrega la carpeta `src`. Ejecuta `umbra.Main`.
- **VS Code** (con *Extension Pack for Java*): abre la carpeta del proyecto y ejecuta `Main.java`.

**Desde la terminal**
- Windows: doble clic en `run.bat`
- Linux / macOS: `sh run.sh`

## Controles

| Tecla | Acción |
|---|---|
| A / D o flechas | Moverse |
| W, Espacio o flecha arriba | Saltar; dentro de la sombra, subir |
| S o flecha abajo | Bajar dentro de la sombra |
| Ratón | Mover a Logi |
| Clic | Fijar o soltar a Logi |
| R | Reiniciar la zona |
| Tab | Abrir o cerrar el mapa |
| Esc | Volver al menú (guarda la partida) |
| M | Silenciar o encender la música y la voz |
| F3 | Mostrar u ocultar los datos de depuración |
| Enter, Esc, Espacio o clic | Saltar la intro |
| W / S o ratón (en el menú) | Elegir partida |
| Enter o clic (en el menú) | Continuar la partida o empezar una nueva |
| Supr (en el menú) | Borrar la partida elegida |
| I (en el menú) | Ver la intro otra vez |
| Esc (en el menú) | Salir del juego |

## Cómo pasar el Nivel 1: Las Raíces

Ningún hueco se puede cruzar saltando: hay que usar las sombras. En cada tramo pon la luz donde se indica, haz clic para fijarla, métete a la sombra y avanza flotando con **D**. Al llegar arriba, sigue subiendo y Candil sale de un salto.

Ojo con **W**: en los tramos 1 y 2 la sombra queda lejos de Logi y puedes mantener **D + W**. En los tramos 3 y 4 la sombra pasa cerca de Logi: si subes con W, Candil se acerca a Logi y se desmorona. Ahí avanza solo con D.

| Tramo | Obstáculo | Dónde va la luz | Cómo avanzar |
|---|---|---|---|
| 1. El puente | Hueco de 450 px | A la izquierda del bloque, a la altura de su cara de arriba: la sombra se estira como un puente | D + W |
| 2. La rampa | Pared de 220 px | Abajo a la izquierda del bloque: la sombra sube hasta lo alto de la pared | D + W |
| 3. Hacia atrás | Hueco con el bloque del otro lado | A la derecha del bloque: la sombra viene hacia ti; salta desde la pared para entrar en ella | D; solo un instante D + W para entrar |
| 4. La carpa | Foso de 700 px con un bloque flotando | Justo encima del bloque: la sombra baja por los dos lados como una montaña; crúzala por dentro | Solo D |

## Cómo pasar el Nivel 2: Las Galerías

Al cruzar de nivel, Logi aparece arriba de Candil y espera a que muevas el ratón.

| Tramo | Obstáculo | Dónde va la luz | Cómo avanzar |
|---|---|---|---|
| 1. El refugio | Ninguno: es donde descansas | Arriba de la losa de la entrada: su sombra cae como una carpa y cubre a Candil, que no pierde vida | Quédate abajo y acomoda a Logi para el tramo siguiente |
| 2. Dos sombras, un puente | Hueco de 1000 px: una sola sombra no alcanza | A la izquierda de la primera losa, a la altura de su cara de arriba: las dos sombras salen hacia la derecha y se enciman | D + W hasta pasar la segunda losa; después solo D |
| 3. La chimenea | Subida de 300 px por un pozo | Arriba a la izquierda de las dos losas: sus sombras caen una sobre otra dentro del pozo | D + W: sube nadando y, al salir por arriba, sale de un salto |
| 4. El pasillo bajo | Túnel muy bajo con un foso de 200 px | Lejos, atrás del túnel y un poco más arriba que su entrada: la sombra entra casi horizontal y lo llena. Dentro del túnel Logi no sirve: Candil se desmoronaría | Solo D, flotando por el túnel |
| 5. La salida | Nada: el camino sigue | Déjala donde quedó | Sube a la repisa alta y camina con D hasta la orilla derecha: lleva a Los Pozos |

## Cómo pasar el Nivel 3: Los Pozos

Una zona de dos tiros: uno baja por la izquierda, el fondo cruza la cueva y otro sube por la derecha hasta la Cúpula. Abajo hay vacío.

| Tramo | Obstáculo | Dónde va la luz | Cómo avanzar |
|---|---|---|---|
| 1. El freno | Tiro muy hondo; el camino sigue por una abertura a media bajada | Fija arriba de las losas del tiro: sus sombras forman una columna | Baja flotando con S, detente frente a la abertura y sal por ella |
| 2. La luz desde abajo | Hueco sobre el vacío con losas cerca del techo | Abajo, en el aire sobre el vacío: las sombras suben y se abren en abanico | Cruza por arriba de las losas, de una sombra a otra |
| 3. El columpio | Hueco ancho con una sola losa en medio | Suelta: muévela despacio por encima de la losa, de un lado al otro | La sombra se columpia y lleva a Candil flotando hasta la otra orilla |
| 4. La chimenea al revés | Subida por el tiro derecho | Abajo, en el fondo: las sombras de las losas escalonadas suben | Sube nadando con W de una sombra a otra |
| 5. El hueco del techo | Agujero angosto en el techo, muy alto para saltar | Bien alineada abajo de la losa chica: la sombra sube recta por el agujero | Entra a la sombra con un salto desde un lado y sube nadando a la Cúpula |

## Cómo pasar la Cúpula

Cuatro pantallas de alto, con paredes que se cierran como un domo. Arriba de todo está la sala del trono, y en ella cuelga el Gran Farol, un candelabro de seis brazos.

| Tramo | Obstáculo | Dónde va la luz | Cómo avanzar |
|---|---|---|---|
| 1. La subida por relevos | Chimenea de losas de unos 1400 px | Por etapas: súbela al siguiente tramo mientras Candil espera | Entre etapas, Candil descansa en nichos con una losa encima, que quedan en sombra |
| 2. Las repisas del domo | Repisas cada vez más cortas | Junto a las losas pequeñas, para que sus sombras unan las repisas | Sube en zigzag |
| 3. Las losas colgantes | El último paso hasta la repisa del Farol | Que las sombras de las dos losas que cuelgan del techo lleguen a la repisa | Cruza por sus sombras |
| 4. La sala del trono | Nada: es el final | — | Sube al estrado, bajo el candelabro, y tócalo |

**El final.** Al tocar el candelabro corre una animación con voz y subtítulos: los brazos se encienden, la luz de Logi se vuelve una llama negra y se abalanza sobre Candil, que se refugia en su sombra. Con los 6 fragmentos, los brazos se cierran sobre la llama y la encierran en el corazón de vidrio; con menos, queda un hueco y la llama escapa por lo alto del domo. Después sale la pantalla final. Se puede saltar con **Enter** (también Esc, Espacio o clic, medio segundo después de empezar).

Las partidas guardadas con la v0.9 salen todas como «dañadas», en cualquier zona: la Cúpula creció hacia arriba y el mapa explorado ya no coincide. No se convierten ni se borran solas: bórralas desde el menú con **Supr** (Enter confirma) y empieza una nueva.

## Estructura del proyecto

```
src/umbra/
├── Main.java                 Crea la ventana y arranca el juego
├── audio/
│   ├── Music.java            Música de ambiente en bucle (entrada suave, tecla M; baja mientras habla la narración)
│   ├── VoiceOver.java        Las voces de la intro, de los fragmentos y del final: un clip a la vez, cargados de antemano, con reloj real
│   ├── DangerAudio.java      Sonido del peligro y del desmoronarse, sintetizado por código
│   ├── FragmentAudio.java    Tono de campana al tomar un fragmento, sintetizado por código
│   └── FinaleAudio.java      El rugido y los golpes de la llama negra en el final, sintetizados
├── core/
│   ├── Config.java           Constantes: física, vida, música, versión
│   ├── GamePanel.java        Game loop, update y render
│   ├── Input.java            Estado del teclado y del ratón
│   ├── Resources.java        Abre la música y los textos (dentro del .jar o en src)
│   └── SaveStore.java        Los 3 espacios de partida en disco, con escritura segura
├── entities/
│   ├── Player.java           Candil: física, vida, flotar en la sombra y animación
│   ├── CandilArt.java        El dibujo de Candil en cada una de sus formas
│   ├── LightSource.java      Logi: sigue al ratón con interpolación; se fija con un clic
│   ├── LogiArt.java          El dibujo de Logi (cometa, espiral, en calma)
│   ├── LogiTemper.java       El carácter de Logi: arrebatos al azar, lentitud y oscuridad según los fragmentos
│   └── Block.java            Objetos que proyectan sombra
├── graphics/
│   ├── Background.java       Cueva por capas (parallax), niebla, esporas y viñeta
│   ├── Camera.java           Cámara que sigue a Candil en los dos ejes (matriz de vista)
│   ├── CaveShapes.java       Formas de la cueva que comparten el juego y la intro (estalactitas, crestas)
│   ├── LevelArt.java         Roca, musgo, plantas que se mecen, hongos, losas con runas
│   ├── CandelabraArt.java    El Gran Farol: el candelabro de seis brazos, con corazón de vidrio, para el juego, la intro y el final
│   ├── CandelabraState.java  Qué brazos están encendidos y cerrados, el brillo y la sacudida del candelabro
│   ├── ThroneHallArt.java    La sala del trono de la Cúpula: estrado, columnas y braseros
│   ├── FragmentArt.java      Un fragmento del Gran Farol: vidrio dorado con facetas que brilla
│   ├── FragmentFlights.java  El vuelo del fragmento hacia Candil al tomarlo
│   ├── ParticleSystem.java   Rastro de luz, chispas, polvo, niebla y ceniza
│   └── ShadowCaster.java     Proyección de sombras (envolvente convexa)
├── math/
│   ├── Vec2.java             Vector 2D
│   └── Mat3.java             Matriz 3x3 (traslación, rotación, escala)
├── res/
│   ├── ambiente.wav          Música
│   ├── voz_1.wav … voz_6.wav Narración de la intro, una por escena; voz_7 … voz_12: la voz de cada línea de fragmentos.txt; voz_13 … voz_20: la voz del final
│   ├── intro.txt             Texto de respaldo de la intro: sale solo si la voz no puede sonar
│   ├── fragmentos.txt        Las líneas de historia que salen al tomar cada fragmento
│   ├── final_animacion.txt   Los subtítulos de la animación final, con la voz de cada línea
│   └── final.txt             Texto de la pantalla final (algunas líneas dependen de los fragmentos)
├── ui/
│   ├── Intro.java            La intro: línea de tiempo de las seis escenas, fundidos y narración
│   ├── TextSprite.java       Texto ya dibujado en una imagen, para que la intro vaya a 60 FPS
│   ├── intro/                Escenas de la intro: el Gran Farol, la caverna dorada, las grietas,
│   │                         los pedazos de luz, la neblina, el nacimiento de Candil, la llegada de Logi,
│   │                         los planos con parallax y los subtítulos
│   ├── Menu.java             Menú de inicio: el título y los 3 espacios de partida
│   ├── LevelTitle.java       Título de la zona al empezar o al cruzar
│   ├── StoryLine.java        La línea de historia al tomar un fragmento
│   ├── Finale.java           La animación final: el guion por pasos con la voz, la llama negra y el candelabro que se cierra
│   ├── Victory.java          Pantalla final
│   ├── Text.java             Ayudantes para dibujar texto
│   ├── MapView.java          Mapa grande (Tab): toda la cueva y lo recorrido
│   ├── MiniMap.java          Mapa circular de la esquina, centrado en Candil
│   ├── MapPainter.java       El dibujo de una zona que comparten los dos mapas
│   └── MapFog.java           Neblina que cubre lo no recorrido (la usan los dos mapas)
└── world/
    ├── Level.java            Las zonas: Las Raíces, Las Galerías y la zona de prueba
    ├── Cave.java             La cueva completa: arma Los Pozos y la Cúpula y conecta las zonas
    ├── Passage.java          Un paso en una orilla de una zona hacia otra
    ├── World.java            Las zonas conectadas, la actual, las visitadas y los fragmentos tomados
    ├── Fragment.java         Un fragmento del Gran Farol dentro de una zona
    ├── SaveData.java         La foto de una partida: zona, mapa, fragmentos, tiempo y reaparición
    └── Exploration.java      Qué partes de cada zona ya recorrió Candil
```

## Avances

### v0.3 — Avance 30%: base funcional
- [x] Ventana y game loop con paso de tiempo fijo (60 actualizaciones/s)
- [x] Jugador con gravedad, salto y colisiones AABB
- [x] Fuente de luz que sigue al ratón con interpolación suave
- [x] Halo de luz con degradado radial
- [x] Salida animada con transformaciones de matrices 3x3 (traslación, rotación, escala)
- [x] Nivel de prueba con puente temporal

### v0.7 — Avance 70%: la mecánica central
- [x] Implementar `ShadowCaster.computeShadows` (proyección de sombras)
- [x] Sombras sólidas en `Player.update` (paso 5)
- [x] Quitar el puente temporal (`Config.TEMP_BRIDGE = false`)
- [x] Un nivel completo que solo se pueda cruzar usando las sombras
- [x] Cámara con desplazamiento horizontal: el nivel mide tres pantallas
- [x] Luz que se fija con un clic, escalón para subir bordes bajos y reaparición en la última repisa

### v0.8.5 — Mundo conectado, mapa y personajes
- [x] Mecánica de la sombra: Candil flota dentro, pierde vida fuera y se desmorona cerca de la luz
- [x] Ambiente de cueva: fondo por capas con parallax, niebla, esporas y viñeta
- [x] Decoraciones: repisas de roca con musgo, hongos que brillan, losas con runas y columnas rotas
- [x] Candil encapuchado, con una forma para cada acción (descansa, corre, salta, cae, flota)
- [x] Logi cambia de forma según se mueve: cometa, espiral al subir, en calma
- [x] Partículas: rastro de luz, chispas, polvo al perder vida, niebla y ceniza al desmoronarse
- [x] Plantas que se mecen al pasar Candil
- [x] Música de ambiente
- [x] 60 FPS en Windows
- [x] Menú con la historia, título del nivel y pantalla final
- [x] Mundo conectado: se puede pasar de un nivel al siguiente y regresar
- [x] Nivel 2, "Las Galerías": cuatro tramos con ideas nuevas y un portal al final
- [x] Mapa grande con Tab, con neblina sobre lo no recorrido
- [x] Mapa circular arriba a la derecha, siempre a la vista

### v0.8.6 — La intro y las sombras negras
- [x] Intro de unos 45 s dibujada por el propio juego: el Gran Farol, cómo se quebró, la oscuridad, el nacimiento de Candil y la llegada de Logi
- [x] Candil y Logi de la intro son los mismos del juego; cámara con curvas suaves, grietas por desplazamiento del punto medio y pedazos de luz con tiro parabólico
- [x] El menú ya no cuenta la historia letra por letra: la cuenta la intro
- [x] Sombras negras con una orilla tenue
- [x] Logi ya no cae sobre Candil al reaparecer

### v0.8.7 — Una intro más solemne, con voz
- [x] Narración con voz en las seis escenas y subtítulos sincronizados con el audio; la música baja mientras se narra
- [x] Profundidad: las cavernas se separan en planos con parallax, con claroscuro y un hilo de luz en las orillas
- [x] El Gran Farol, alto y esbelto: pedestal tallado, cadenas hasta el techo y anillos que giran alrededor de la llama; ya no tiene ojos
- [x] La rotura: la luz se contrae, un cuadro de impacto, una onda expansiva y pedazos que rebotan al caer
- [x] La oscuridad baja también del techo; Candil se condensa de arriba abajo y Logi lanza un pulso de luz al fijarse
- [x] El final sube hasta el Gran Farol apagado en lo alto de la chimenea, junto al título

### v0.9 — El mundo-cueva, los fragmentos y guardar la partida
- [x] Zonas más altas que la pantalla, con cámara que sigue a Candil al subir y al caer
- [x] Pasos en las cuatro orillas: la cueva se conecta hacia los lados, arriba y abajo, y se puede regresar
- [x] Nivel 3, **Los Pozos**: cinco tramos verticales (el freno, la luz desde abajo, el columpio, la chimenea al revés y el hueco del techo)
- [x] **La Cúpula**: la subida por relevos hasta el Gran Farol, que cuelga en lo alto y es el final
- [x] Mapa grande con toda la cueva y los pasos entre zonas
- [x] 6 fragmentos del Gran Farol, dos por zona: uno en el camino y otro escondido
- [x] Al tomar un fragmento: vuela hacia Candil, suena una campana y sale una línea de la historia
- [x] El Gran Farol llena un hueco y brilla más con cada fragmento; el texto final cambia según cuántos juntaste
- [x] 3 espacios de partida en el menú: continuar, empezar y borrar; se guarda solo al cruzar de zona, al salir con Esc y al cerrar
- [x] Sonido del peligro y del desmoronarse sintetizado por código (aporte de Maria Magdalena)

### v0.9.5 — La sala del trono y el final
- [x] La Cúpula crece hacia arriba y termina en la sala del trono, con el Gran Farol como candelabro de seis brazos
- [x] La animación final con voz: la llama negra de Logi, que el candelabro encierra (o que escapa si faltan fragmentos); se salta con Enter
- [x] La intro usa el mismo candelabro
- [x] Las partidas de la v0.9 salen todas dañadas (se borran desde el menú con Supr)

### v0.9.6 — Logi se resiste
- [x] Antes del final, una espera de 2 s con un destello del candelabro
- [x] Con cada fragmento, Logi sigue al ratón más lento y se oscurece (naranja → rojo → casi negro); el negro total queda para el final
- [x] Arrebatos con aviso: Logi se queda quieto y luego tira hacia Candil, lo deja sin sombra y puede hacerlo caer; fijarlo lo protege
- [x] Capturas de pantalla en este README

### v0.9.7 — Iluminación atmosférica y depuración visual
- [x] Iluminación de borde dinámica (*Rim Lighting*) en `LevelArt`: resalta las repisas y bloques flotantes orientados hacia Logi dentro de un radio de 240 px con atenuación cuadrática inversa y cambio de temperatura según el estado de la llama
- [x] Dispersión atmosférica (*efecto Tyndall*) en `ParticleSystem`: las motas de sombra y esporas que entran en el haz de luz de Logi amplifican su halo y adoptan el tinte de la llama
- [x] Visualizador técnico con tecla `F3` en `GamePanel`: muestra en tiempo real los rayos de proyección hacia los bloques oclusores, los contornos de los polígonos de sombra (`ShadowCaster`), los radios de luz/desmoronamiento, la hitbox reactiva de Candil y telemetría de rendimiento

### Después
Las versiones siguen: el juego todavía puede crecer.
- [ ] La persecución: la llama negra, luces que se mueven solas y obstáculos nuevos

## Técnicas de graficación

| Técnica | Dónde |
|---|---|
| Game loop con paso de tiempo fijo | `GamePanel.tick()` |
| Interpolación lineal y suavizado exponencial | `LightSource.update()`, `Camera.follow()` |
| Degradado radial (iluminación) | `LightSource.drawGlow()` |
| Transformaciones 2D con matrices homogéneas | `Mat3`, `Level.drawPortal()` |
| Escala y rotación alrededor de un punto: `T · R · S · T⁻¹` | `Player.draw()`, `LevelArt.drawMushroom()` |
| Cámara: matriz de vista y su inversa (mundo ↔ pantalla) | `Camera` |
| Parallax: cada capa con `T(−camX · profundidad, 0)` | `Background.drawLayer()` |
| Terreno fractal por desplazamiento del punto medio | `Background.midpointDisplacement()` |
| Perspectiva atmosférica y viñeta | `Background` |
| Composición alfa (`DST_IN`) y pre-renderizado en imágenes | `Background`, `LevelArt` |
| Curvas de Bézier (la silueta de Candil) | `CandilArt.cloakShape()` |
| Detección y respuesta de colisiones AABB | `Player.update()` |
| Proyección de sombras desde una luz puntual | `ShadowCaster.computeShadows()` |
| Envolvente convexa (monotone chain de Andrew) | `ShadowCaster.convexHull()` |
| Punto dentro de polígono: ¿Candil está en la sombra? | `Player.inShadow()` |
| Resorte amortiguado (plantas que se mecen) | `LevelArt.sway()` |
| Sistema de partículas | `ParticleSystem` |
| Mapa: escala y traslación fijas, y su recorte circular | `MapView`, `MiniMap` |
| Neblina con máscara bilineal (orillas suaves) | `MapFog` |
| Parallax por planos: la vista de cada plano según su profundidad | `IntroCamera.getViewMatrix(factor)`, `GoldenCavern`, `DarkCavern` |
| Interpolación bilineal al escalar imágenes (con la tarjeta de video) | `Scene.drawImage()`, `GreatLantern.draw()` |
| Proyección 2.5D de un círculo: `T · R · S(1, 0.28)` | `GreatLantern.drawArcaneRings()` |
| Tiro parabólico con rebote amortiguado | `LightShards` |
| Máscara de degradado con composición alfa (`DST_IN`) | `DarkStage.drawCandil()` |
| Sincronía de subtítulos con el reloj del audio | `VoiceOver`, `SubtitleTrack` |
| Cámara en dos ejes: matriz de vista `T(−x, −y)` con seguimiento suavizado | `Camera.follow()`, `Camera.getViewMatrix()` |
| Mundo en un plano: cada zona con su traslación, y pasos entre orillas | `Cave`, `Passage`, `MapView` |
| Interpolación lineal hacia un blanco que se mueve, con escala que se encoge | `FragmentFlights` |
| Degradado radial y facetas con bisel de luz (iluminación falsa) | `FragmentArt`, `CandelabraArt` |
| Síntesis de audio por código (ondas senoidales con envolvente) | `FragmentAudio`, `DangerAudio`, `FinaleAudio` |
| Guion por pasos sincronizado con la voz: cada paso avanza cuando acaba su audio | `Finale` |
| Interpolación por brazo al cerrarse: cada brazo gira de su posición abierta a la cerrada con su propio avance | `CandelabraArt`, `CandelabraState` |
| Sacudida con amortiguación (golpes que se apagan) | `Finale`, `CandelabraState` |
| Cambio de color de la llama negra por interpolación (naranja, rojo, negro) | `LogiArt`, `Finale` |
| Máquina de estados con azar (calma, aviso, quieto, tirón): esperas uniformes al azar según los fragmentos | `LogiTemper` |
| Suavizado exponencial con factor variable (Logi más lento con cada fragmento) | `LightSource.update()` |

## Música

La música está en `src/umbra/res/ambiente.wav` y se repite sin fin (**M** la silencia). Para cambiarla, reemplaza ese archivo por otro **WAV** con el mismo nombre: de preferencia mono, de 22 050 o 44 100 Hz y de 3 minutos o menos. Java no lee MP3, así que hay que convertirla antes.

La narración de la intro está en `src/umbra/res/voz_1.wav` … `voz_6.wav`, una por escena (WAV PCM de 16 bits, 44 100 Hz, estéreo). Mientras habla, la música baja un poco; **M** silencia las dos. Si una voz falta o no hay tarjeta de sonido, la intro muestra el texto de `intro.txt` en su lugar.

`voz_7.wav` a `voz_12.wav` son las voces de las líneas de los fragmentos, una por línea de `fragmentos.txt` en orden, con el mismo formato que las de la intro: la línea n suena al tomar el fragmento número n. Si falta una, solo sale el texto.

`voz_13.wav` a `voz_20.wav` son las voces de la animación final, en el mismo formato; sus subtítulos están en `final_animacion.txt`:

| Archivo | Quién | Línea |
|---|---|---|
| `voz_13` | Narrador | «Por primera vez en mucho tiempo, la Cúpula se llenó de luz.» |
| `voz_14` | Narrador | «Pero Logi no buscaba encender el Farol. Buscaba volver a él.» |
| `voz_15` | Narrador | «Y el trono que una vez fue suyo se volvió su prisión.» |
| `voz_16` | Logi | «Me encerraste en mi propio trono, Candil. Pero la luz siempre vuelve a su dueño.» |
| `voz_17` | Narrador | «Desde entonces, en la cueva hay luz… y también sombra donde descansar.» |
| `voz_18` | Narrador | «El Gran Farol volvió a arder… pero no del todo.» |
| `voz_19` | Logi | «Te faltó un pedazo, pequeño. La oscuridad es mía otra vez.» |
| `voz_20` | Narrador | «Los pedazos que faltan siguen perdidos en algún lugar de la cueva.» |

`voz_13` a `voz_17` son el final con los 6 fragmentos; el que falla usa `voz_18`, `voz_14`, `voz_19` y `voz_20`. Si una falta, solo salen los subtítulos.
