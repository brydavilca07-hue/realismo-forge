# Realismo (mod para Minecraft 1.20.1 - Forge)

Hace el juego mas realista con mecanicas de supervivencia:

- **Heridas:** con 3 corazones o menos cojeas; con 1.5 corazones o menos tambien te cuesta minar.
- **Peso:** llevar el inventario muy lleno te vuelve lento.
- **Clima:** el frio extremo, el calor del desierto y la lluvia te desgastan mas rapido. Una pechera de cuero te protege.
- **Esfuerzo:** correr consume mas hambre.

No afecta a jugadores en modo creativo o espectador.

## Instalacion en TLauncher

1. En TLauncher elige la version **Forge 1.20.1** y ejecuta el juego una vez.
2. Pon `realismo-1.0.0.jar` en la carpeta `mods` (Windows: `%appdata%\.minecraft\mods`).
3. Abre el juego con el perfil Forge 1.20.1. No necesitas Fabric API.

## Como obtener el .jar

Sube esta carpeta a GitHub: la pestana Actions compila el mod y lo descargas desde Artifacts.
O en tu PC (Java 17 + Gradle 8.x): ejecuta `gradle build`; el mod queda en `build/libs/realismo-1.0.0.jar`.

## Ajustes

Los valores (umbrales de vida, peso, cansancio) estan en
`src/main/java/com/realismo/RealismoMod.java`.
