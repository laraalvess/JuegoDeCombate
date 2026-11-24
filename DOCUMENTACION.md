# JuegoDeCombate

## Memoria del proyecto

<hr>

<div class="meta">

**Asignatura:** Diseño de Software

**Equipo:** JuegoDeCombate

**Integrantes:** Iñigo Catalina · Lara Alves · Alejandro Sastre

**Fecha:** 10 de mayo de 2026

</div>

</div>

> Estado del código a fecha 2026-05-10

---

## Cómo ejecutar

```bash
./run.sh
```

El script `run.sh` en la raíz del proyecto compila todos los `.java` de `src/` hacia `bin/` con `javac` y arranca `juego.Main` automáticamente. Requiere Java 8 o superior y entorno gráfico (el juego usa Swing).

---

## Estructura de paquetes

```
src/
├── acciones/       → acciones de combate + patrón Decorator (Ap. 2)
├── estado/         → patrón State — estados del personaje (Ap. 3)
├── estrategias/    → patrón Strategy — IA del enemigo (Ap. 1)
├── fabricas/       → patrón Abstract Factory — generación de enemigos (Ap. 4)
├── juego/          → lógica de juego e interfaz gráfica
├── personajes/     → jerarquía de personajes
└── testing/        → demo standalone del patrón Strategy
```

---

## Paquete `personajes`

### `Personaje.java` — Clase abstracta base

Clase raíz de la que heredan `Jugador` y `Enemigo`. Contiene todos los atributos que afectan al combate y las operaciones comunes. Integra el **patrón State** (Ap. 3): mantiene un `EstadoPersonaje` que se actualiza automáticamente tras cada golpe o curación.

**Atributos:**

| Campo          | Tipo             | Descripción                                    |
|----------------|------------------|------------------------------------------------|
| `nombre`       | String           | Nombre del personaje                           |
| `vida`         | int              | Vida actual                                    |
| `vidaMaxima`   | int              | Vida máxima (tope de curación)                 |
| `fuerza`       | int              | Aumenta el daño causado                        |
| `resistencia`  | int              | Reduce el daño recibido                        |
| `agilidad`     | int              | Determina quién ataca primero                  |
| `estadoActual` | EstadoPersonaje  | Estado actual (Activo / Herido / Paralizado)   |

**Métodos:**

- `estaVivo()` → `vida > 0`
- `recibirDaño(cantidad)` → resta vida (mín. 0) y llama a `actualizarEstado()`
- `curar(cantidad)` → suma vida (máx. `vidaMaxima`) y llama a `actualizarEstado()`
- `porcentajeVida()` → devuelve `vida/vidaMaxima` como double (0.0–1.0)
- `getEstado()` → devuelve el `EstadoPersonaje` actual
- `puedeActuar()` → delega en el estado actual

**`actualizarEstado()` (privado):** Transición automática de estados según porcentaje de vida:

| % de vida  | Estado aplicado   |
|------------|-------------------|
| ≥ 60%      | `EstadoActivo`    |
| 40%–59%    | `EstadoHerido`    |
| < 40%      | `EstadoParalizado`|

---

### `Jugador.java`

Extiende `Personaje`. No añade lógica propia — sus acciones las decide el usuario a través de la interfaz. Creado en `Main` con stats fijos: vida=100, fuerza=15, resistencia=10, agilidad=12.

---

### `Enemigo.java`

Extiende `Personaje`. Implementa el **patrón Strategy** (Ap. 1) y el **patrón Template Method** (Ap. 6).

- Mantiene una referencia a una `Estrategia` que puede sustituirse en tiempo de ejecución con `setEstrategia()`.
- `decidirAccion(Personaje oponente)` es el **método plantilla**: define el algoritmo en pasos fijos y no debe sobreescribirse.

**Pasos del Template Method en `decidirAccion`:**

| Paso | Método                          | Tipo        | Descripción                                        |
|------|---------------------------------|-------------|----------------------------------------------------|
| 1    | `accionParalizado()`            | hook        | Devuelve `NoHacerNada` si el enemigo está paralizado |
| 2    | `elegirAccionSegunEstrategia()` | hook        | Delega en la estrategia actual (sobreescribible)   |
| 3    | `registrarDecision(accion)`     | paso común  | Registro interno silencioso (no rompe la UI)       |

---

## Paquete `acciones` — Patrón Decorator (Ap. 2)

### `Accion.java` — Interfaz

Contrato que deben cumplir todas las acciones:

- `getDescripcion()` → texto para el log de combate
- `getDañoBase()` → daño bruto antes de modificadores
- `ejecutar(ejecutor, objetivo)` → aplica el efecto de la acción

---

### `AccionDecorador.java` — Clase base del Decorator

Implementa `Accion` y envuelve otra `Accion` (`accionDecorada`). Las subclases añaden comportamiento antes/después sin modificar la acción original.

---

### `AtacarConEspada.java`

Ataque cuerpo a cuerpo base. `getDañoBase()` = 10.

### `Defenderse.java`

Acción defensiva. `getDañoBase()` = 0. El efecto real (daño recibido × 0.5) lo gestiona `Combate` mediante el flag `jugadorDefendiendo` / `enemigoDefendiendo`.

### `NoHacerNada.java`

Pasa el turno. `getDañoBase()` = 0.

### `DecoradorAtaquePoderoso.java` — Decorator

Envuelve cualquier `Accion`. Si la fuerza del ejecutor alcanza el umbral, añade el bonus al daño.

| Constante      | Valor | Descripción                                          |
|----------------|-------|------------------------------------------------------|
| `UMBRAL_FUERZA`| 15    | Fuerza mínima para activar el bonus                 |
| `BONUS`        | 5     | Daño adicional si se supera el umbral               |

### `DecoradorAtaqueVenenoso.java` — Decorator

Envuelve cualquier `Accion`. Al ejecutar, activa un contador de veneno que `Combate` descuenta cada turno.

| Constante    | Valor | Descripción                                   |
|--------------|-------|-----------------------------------------------|
| `TURNOS`     | 3     | Turnos que dura el veneno                     |
| `DAÑO_VENENO`| 3     | Daño por turno de veneno                      |

---

## Paquete `estado` — Patrón State (Ap. 3)

### `EstadoPersonaje.java` — Interfaz

Contrato del patrón State. Define los modificadores que cada estado aplica en combate:

- `getNombre()` → nombre legible para la UI
- `modificarDaño(int d)` → transforma el daño que inflige el personaje en este estado
- `modificarDefensa(int d)` → transforma la resistencia efectiva en este estado
- `puedeActuar()` → si el personaje puede realizar una acción en su turno

### `EstadoActivo.java`

Estado por defecto (vida ≥ 60%). No modifica ningún valor; `puedeActuar()` = `true`.

### `EstadoHerido.java`

Estado intermedio (40%–59%). Reduce daño y defensa a la mitad (`d / 2`); `puedeActuar()` = `true`.

### `EstadoParalizado.java`

Estado crítico (< 40%). El personaje no causa daño (`modificarDaño` = 0), no tiene defensa (`modificarDefensa` = 0) y no puede actuar (`puedeActuar()` = `false`).

---

## Paquete `estrategias` — Patrón Strategy (Ap. 1)

### `Estrategia.java` — Interfaz

- `decidirAccion(Enemigo enemigo, Personaje oponente)` → devuelve la `Accion` elegida
- `getNombre()` → nombre legible para la UI

### `EstrategiaAgresiva.java`

Siempre devuelve `AtacarConEspada`. Máximo daño sin importar el estado propio.

### `EstrategiaDefensiva.java`

Si `enemigo.porcentajeVida() < 0.20` → `Defenderse`; en caso contrario → `AtacarConEspada`.

### `EstrategiaEquilibrada.java`

Decide aleatoriamente (50/50) entre `Defenderse` y `AtacarConEspada` con `Math.random() < 0.5`.

---

## Paquete `fabricas` — Patrón Abstract Factory (Ap. 4)

### `FabricaEnemigos.java` — Interfaz (Abstract Factory)

Define la familia de enemigos que un bioma puede producir. Cada implementación concreta encapsula los nombres, stats y estrategia inicial de los enemigos del bioma.

- `crearGuerrero(int ronda)` → enemigo tipo guerrero escalado por ronda
- `crearMago(int ronda)` → enemigo tipo mago escalado por ronda
- `crearBestia(int ronda)` → enemigo tipo bestia escalado por ronda
- `getNombreBioma()` → nombre del bioma para la UI

### `FabricaEnemigosBosque.java` — Bioma inicial (rondas 1–3)

Enemigos rápidos pero poco resistentes. Todos tienen stats moderados y estrategias ágiles.

| Tipo     | Nombre            | Estrategia         |
|----------|-------------------|--------------------|
| Guerrero | Bandido del bosque| `EstrategiaAgresiva`  |
| Mago     | Druida            | `EstrategiaEquilibrada` |
| Bestia   | Lobo salvaje      | `EstrategiaAgresiva`  |

### `FabricaEnemigosMazmorra.java` — Bioma intermedio (rondas 4–6)

Enemigos más resistentes con tendencia defensiva.

| Tipo     | Nombre          | Estrategia           |
|----------|-----------------|----------------------|
| Guerrero | Caballero oscuro| `EstrategiaDefensiva`  |
| Mago     | Nigromante      | `EstrategiaEquilibrada`|
| Bestia   | Gárgola         | `EstrategiaDefensiva`  |

### `FabricaEnemigosVolcan.java` — Bioma final (ronda 7+)

Enemigos con mucha fuerza, siempre agresivos.

| Tipo     | Nombre           | Estrategia          |
|----------|------------------|---------------------|
| Guerrero | Berserker de fuego| `EstrategiaAgresiva` |
| Mago     | Pirómago         | `EstrategiaAgresiva` |
| Bestia   | Dragon de magma  | `EstrategiaAgresiva` |

---

## Paquete `juego`

### `Main.java` — Punto de entrada

Arranca el juego creando la ventana Swing en el EDT y delegando todo lo demás a `GameController` (**patrón Facade**, Ap. 7).

**Flujo:**

1. Crea `ConsolaVentana` en el EDT con `SwingUtilities.invokeAndWait`
2. Crea un `GameController` pasándole la consola
3. Llama a `controller.iniciarJuego()` — pide nombre y crea el jugador
4. Llama a `controller.ejecutarBucleJuego()` — gestiona todas las rondas hasta el fin

---

### `GameController.java` — Facade (Ap. 7)

Actúa como **única fachada** del subsistema de juego. Oculta a `Main` la lógica de progresión, selección de fábricas, gestión de puntos y curación entre rondas.

**API pública:**

- `iniciarJuego()` — pide nombre al jugador y crea el `Jugador` con stats fijos (vida=100, fuerza=15, resistencia=10, agilidad=12)
- `ejecutarBucleJuego()` — bucle principal: rondas hasta derrota o cierre
- `getPuntos()` — devuelve los puntos acumulados

**Subsistema privado oculto:**

- `ejecutarRonda()` — selecciona fábrica, genera enemigo, lanza `Combate`, gestiona resultado
- `recuperarVidaEntreRondas()` — cura 30 de vida al jugador tras cada victoria
- `mostrarPantallaFinal()` — muestra puntuación final
- `seleccionarFabrica(ronda)` — progresión de biomas:

| Rondas | Fábrica                   |
|--------|---------------------------|
| 1–3    | `FabricaEnemigosBosque`   |
| 4–6    | `FabricaEnemigosMazmorra` |
| 7+     | `FabricaEnemigosVolcan`   |

- `generarEnemigo(fabrica, ronda)` — rota el tipo según `(ronda-1) % 3`: Guerrero → Mago → Bestia

---

### `CalculadorCombate.java` — Singleton (Ap. 5)

Implementa el **patrón Singleton** con inicialización estática (eager, thread-safe). Una sola instancia compartida por todo el sistema se obtiene con `CalculadorCombate.getInstancia()`.

**Método principal:**

`calcularDaño(base, fuerza, resistencia, objetivoDefendiendo, estadoAtacante, estadoDefensor)`

```
resEfectiva = estadoDefensor.modificarDefensa(resistencia)
daño        = base + fuerza × 0.5 − resEfectiva × 0.3
daño        = estadoAtacante.modificarDaño(daño)
si objetivoDefendiendo: daño × 0.5
resultado   = max(1, round(daño))
```

`Combate` lo obtiene una vez con `CalculadorCombate.getInstancia()` y lo reutiliza en cada ataque del jugador y del enemigo.

---

### `Combate.java` — Lógica del combate

Gestiona un combate completo entre jugador y enemigo.

**Constantes:**

| Constante   | Valor | Descripción                                        |
|-------------|-------|----------------------------------------------------|
| `MAX_LOG`   | 6     | Líneas visibles del historial en pantalla          |
| `BOX_INNER` | 70    | Ancho interior del marco de la UI en caracteres    |
| `ROJO`      | ANSI 31 | Color rojo — nombre del enemigo                |
| `BLANCO`    | ANSI 37 | Color blanco — nombre del jugador              |
| `RESET`     | ANSI 0  | Reset al color por defecto                     |

**Flags y contadores:**

| Campo                | Tipo | Descripción                                          |
|----------------------|------|------------------------------------------------------|
| `jugadorDefendiendo` | bool | Activo cuando el jugador eligió defenderse           |
| `enemigoDefendiendo` | bool | Activo cuando el enemigo eligió defenderse           |
| `venenoJugadorTurnos`| int  | Turnos de veneno restantes sobre el jugador          |
| `venenoEnemigoTurnos`| int  | Turnos de veneno restantes sobre el enemigo          |

**`iniciar()`:** Determina el turno inicial comparando `agilidad`. Alterna turnos hasta que uno de los dos muere. Devuelve `true` si gana el jugador.

**`dibujarUI(mostrarMenu)`:** Llama a `consola.limpiar()` y redibuja toda la pantalla desde cero cada turno:

```
╔════════════════════════════════════════════════════════════════════════╗
║ JUEGO DE COMBATE
║ Enemigo: [nombre en rojo]  [estrategia actual]
╠════════════════════════════════════════════════════════════════════════╣
║ [jugador en blanco]  [████████████] vida/vidaMax · Estado · ☠ veneno
║ [enemigo en rojo]    [████████░░░░] vida/vidaMax · Estado · ☠ veneno
╠════════════════════════════════════════════════════════════════════════╣
║ línea log 1
║ línea log 2   (hasta MAX_LOG = 6 líneas)
╠════════════════════════════════════════════════════════════════════════╣
║ Tu turno. Elige una accion:
║   1. Atacar con espada
║   2. Atacar con espada poderosa  (+5 si fuerza ≥ 15)
║   3. Atacar con espada venenosa  (veneno 3 turnos · 3 daño/turno)
║   4. Defenderse
║   5. No hacer nada
╚════════════════════════════════════════════════════════════════════════╝
```

**`turnoJugador()`:**
1. Aplica veneno si `venenoJugadorTurnos > 0` (puede matar; en ese caso el turno termina)
2. Comprueba `jugador.puedeActuar()` — si está paralizado, pierde el turno
3. Lee opción 1–5, construye la acción (con decoradores si corresponde)
4. Calcula daño con `calcularDaño()` y registra en el log
5. Activa `venenoEnemigoTurnos = 3` si se usó ataque venenoso

**`turnoEnemigo()`:**
1. Aplica veneno si `venenoEnemigoTurnos > 0`
2. Actualiza la estrategia del enemigo según su % de vida:

| % de vida del enemigo | Estrategia aplicada     |
|-----------------------|-------------------------|
| 40%–60%               | `EstrategiaEquilibrada` |
| < 40%                 | `EstrategiaDefensiva`   |
| > 60%                 | sin cambio (la inicial) |

3. Comprueba `enemigo.puedeActuar()` — si está paralizado, pierde el turno

**`fila(contenido)`:** Formatea una línea del marco calculando el padding sobre la longitud visible (elimina los códigos ANSI con regex).

**`barraVida(...)`:** Construye la cadena de barra de vida con nombre coloreado, barra de bloques, estado actual e indicador de veneno si `venenoTurnos > 0`.

---

### `ConsolaVentana.java` — Interfaz gráfica

Ventana Swing independiente del IDE que actúa como terminal del juego. Extiende `JFrame`.

**Componentes:**

- `JTextPane areaTexto` — área de texto no editable, fondo negro, texto verde monoespaciado. Usa `StyledDocument` para aplicar colores distintos a tramos de texto.
- `JTextField campoEntrada` — campo de entrada en la parte inferior con el mismo estilo visual.

**Colores definidos:**

| Constante       | Valor RGB      | Uso                    |
|-----------------|----------------|------------------------|
| `COLOR_DEFECTO` | (0, 200, 0)    | Verde — texto general  |
| `COLOR_ROJO`    | (220, 60, 60)  | Nombre del enemigo     |
| `COLOR_BLANCO`  | (255, 255, 255)| Nombre del jugador     |

**Modelo de hilos:**

```
Hilo principal (main)          EDT (Event Dispatch Thread)
─────────────────────          ───────────────────────────
println() ─────────────────► invokeLater → appendTexto()
print()   ─────────────────► invokeLater → appendTexto()
limpiar() ─────────────────► invokeLater → setText("")
readLine() → queue.take() ◄── ActionListener → queue.offer()
  (bloquea)                    (desbloquea al pulsar Enter)
```

**`appendTexto(texto)`:** Parsea el texto buscando códigos ANSI. Código 31 → rojo, 37 → blanco, 0 → verde por defecto.

---

## Paquete `testing`

### `DemoStrategy.java`

Programa independiente con su propio `main` para verificar el patrón Strategy sin arrancar el juego. Crea un jugador y un enemigo y muestra qué acción decide el enemigo con cada estrategia. No depende del paquete `juego`.

---

## Apartados implementados

| Ap. | Patrón           | Estado       | Paquete/Clase principal                        |
|-----|------------------|--------------|------------------------------------------------|
| 1   | Strategy         | Implementado | `estrategias/`, `personajes/Enemigo`           |
| 2   | Decorator        | Implementado | `acciones/AccionDecorador` y subclases         |
| 3   | State            | Implementado | `estado/`, `personajes/Personaje`              |
| 4   | Abstract Factory | Implementado | `fabricas/FabricaEnemigos` y subclases         |
| 5   | Singleton        | Implementado | `juego/CalculadorCombate`                      |
| 6   | Template Method  | Implementado | `personajes/Enemigo.decidirAccion()`           |
| 7   | Facade           | Implementado | `juego/GameController`                         |

---

## Diagrama de dependencias

```
Main ──► GameController (Facade) [Ap. 7] ──► ConsolaVentana
              │                                     ▲
              ├──► Jugador (extends Personaje)       │
              │                                     │
              ├──► FabricaEnemigos (interface) [Ap. 4]
              │       ├── FabricaEnemigosBosque      │
              │       ├── FabricaEnemigosMazmorra    │
              │       └── FabricaEnemigosVolcan      │
              │                                     │
              └──► Combate ────────────────────────┘
                     │
                     ├──► CalculadorCombate (Singleton) [Ap. 5]
                     │
                     ├──► Jugador / Enemigo (extends Personaje)
                     │              │
                     │              ├──► Estrategia (interface)        [Ap. 1]
                     │              │       ├── EstrategiaAgresiva
                     │              │       ├── EstrategiaDefensiva
                     │              │       └── EstrategiaEquilibrada
                     │              │
                     │              ├──► decidirAccion() Template Method [Ap. 6]
                     │              │       ├── accionParalizado()       (hook)
                     │              │       ├── elegirAccionSegunEstrategia() (hook)
                     │              │       └── registrarDecision()      (común)
                     │              │
                     │              └──► EstadoPersonaje (interface)   [Ap. 3]
                     │                      ├── EstadoActivo
                     │                      ├── EstadoHerido
                     │                      └── EstadoParalizado
                     │
                     └──► Accion (interface)                           [Ap. 2]
                               ├── AtacarConEspada
                               ├── Defenderse
                               ├── NoHacerNada
                               └── AccionDecorador
                                       ├── DecoradorAtaquePoderoso
                                       └── DecoradorAtaqueVenenoso
```
