##                    SOUNDMONOAPP 👩‍💻
Descripción
SOUNDMONOAPP es una aplicación móvil diseñada para monitorear el audio capturado por el micrófono de tus auriculares y reproducirlo en tiempo real a través de los mismos auriculares. Esta funcionalidad permite escuchar directamente lo que el micrófono está captando, útil para diversas aplicaciones como pruebas de sonido, monitoreo ambiental, y más.

Características
Monitoreo en tiempo real: Captura y reproduce el audio de inmediato.

Interfaz de usuario sencilla: Usa Jetpack Compose para una UI moderna y fácil de usar.

Gestión de permisos: Solicita permisos de audio automáticamente para una experiencia sin problemas.

## ⚡️ Requisitos
Android Studio instalado.
Conocimiento básico de Kotlin y Jetpack Compose.
Dispositivo Android con Android 6.0 (Marshmallow) o superior.

## 🛠 Skills
KOTLIN COMPOSE

## Instalación
Clona el repositorio:
git clone https://github.com/tu_usuario/SOUNDMONOAPP.git
Abre el proyecto en Android Studio.

> [!NOTE]
>Asegúrate de tener las siguientes dependencias en tu archivo build.gradle:
>implementation "androidx.compose.ui:ui:1.3.0"
>implementation "androidx.compose.ui:ui-tooling-preview:1.3.0"
>implementation "androidx.activity:activity-compose:1.5.0"


> [!NOTE]
>Añade los permisos necesarios en tu archivo AndroidManifest.xml:
><uses-permission android:name="android.permission.RECORD_AUDIO"/>
><uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>

# Uso
Abre la aplicación en tu dispositivo Android.
Concede los permisos de audio cuando se te soliciten.
Haz clic en el botón "Start Monitoring" para comenzar a capturar y reproducir el audio.
Para detener el monitoreo, haz clic en el botón "Stop Monitoring".



## 👯‍♀️ Contribuciones
¡Las contribuciones son bienvenidas! Si deseas colaborar, por favor abre un issue o haz un pull request en el repositorio de GitHub.

[!IMPORTANT]
Licencia
Este proyecto está bajo la Licencia MIT. Consulta el archivo LICENSE para más detalles.

¡Eso es todo! Este README debería ser un buen punto de partida. Puedes ajustarlo según las necesidades específicas de tu proyecto. ¡Buena suerte!
