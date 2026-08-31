# Changelog

Todos los cambios notables en este proyecto serán documentados en este archivo.
## [0.2.4] - 2026-08-30
### Añadido
- **Menu de inicio:** se creo un menu de inicio con opcion de empezar a jugar y salir del juego
### Correcciones
- **Destruccion proyectiles:** Ahora los proyectiles se destruyen al momento de colisionar un enemigo.

## [0.2.3] - 2026-08-30
### Correcciones
- **Hitbox enemiga:** se solucionaron problemas de colisiones entre proyectiles y enemigos.

## [0.2.2] - 2026-08-28
### Añadido
- **Barra de vida enemigo:** Se implementa un indicador de vida a los enemigos.
- **Colisiones de enemigos:** Se implementa el primer metodo de verificacion de colisiones con los proyectiles.

## [0.2.1] - 2026-08-26

### Añadido
- **Creacion de clase mundo:** se creo una clase Mundo que es la encargada de toda las comprobaciones de colisiones del entorno.
### Modificado
- **Correccion dispose() enemigos:** se corrigio el metodo dispose() de los enemigos.

## [0.2.0] - 2026-08-09

### Añadido
- **Generacion de enemigos:** Se implemento un metodo en la clase MapManager para leer los arhivos .tmx y asi generar los enemigos en los lugares correspondientes.
- **Clase padre Enemigo:** Se creo la clase abstracta padre "Enemigo" la cual utilizaran todos los enemigos como plantilla.

### Modificado
- **Archivos .tmx:** se modificaron los archivos .tmx para crearle una capa de objetos llamada "spawnEnemigo" la cual le indica al juego donde spawnear los enemigos.

## [0.1.3] - 2026-08-06

### Añadido
- **Disparo del jugador y proyectiles:** Se aplico la mecanica de disparo al jugador, creando la clase padre proyectiles para poder crear distintos tipos de proyectiles

## [0.1.2] - 2026-07-21

### Añadido
- **Generacion de mapas:** Se aplico la generacion de mapas aleatoria mediante la union de salas prefabricadas en Tiled. 

## [0.1.1] - 2026-07-21

### Añadido
- **Creacion del jugador:** Se creo la clase del jugador junto a su movimiento basico y un sistema simple de colisiones
- **Implementacion de mapas de Tiled:** Se importo un primer mapa de pruebas para empezar a aplicar todas las funciones de Tiled.
- **Creacion de clases de control:** Se creo la clase que controla como dibujar el mapa, la camara y al jugador.

## [0.1.0] - 2026-07-17

### Añadido
- **Estructura Base del Proyecto:** Configuración inicial del entorno multiproyecto utilizando Gradle, dividiendo el espacio de trabajo en los módulos independientes `core` (lógica compartida) y `desktop` (lanzador nativo para PC).
- **Dependencias del Framework:** Integración de la biblioteca de desarrollo de videojuegos **LibGDX (v1.12.x)** en el archivo de configuración `build.gradle`.
- **Gestión de Recursos:** Creación del directorio unificado `assets` estructurado internamente para el almacenamiento ordenado de mapas en formato `.tmx` (procedentes de Tiled Map Editor), fuentes vectoriales y esquemas de datos.
- **Documentación Inicial:** Redacción e incorporación en la raíz del repositorio de los archivos organizacionales obligatorios `README.md` (con la descripción formal de la propuesta del videojuego) y este archivo `CHANGELOG.md`.

