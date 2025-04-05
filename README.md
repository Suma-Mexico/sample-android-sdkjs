# Sample SDK JS - Android

Este proyecto de ejemplo integra un componente WebView con soporte para JavaScript que permite la ejecución de sistema de autocaptura de documentos. Se puede encontrar más información del componente aqui: https://github.com/Suma-Mexico/demo-integration-js/tree/main/id-autocapture#integraci%C3%B3n-y-pruebas-de-autocaptureminjs

## 🧠 Descripción

El objetivo de este ejemplo es demostrar cómo ejecutar un componente web (HTML + JS) dentro de una app Android, utilizando `WebView` y solicitando permisos de cámara para la captura continua de imágenes.

- El componente principal es un archivo `HTML` local ubicado en `assets/www/veridoc.html`.
- Este HTML ya contiene el archivo JavaScript necesario para la autocaptura.
- El `JavaScript` no debe ser modificado directamente, pero sí se puede cambiar libremente el HTML y el CSS del archivo.
- Las imágenes capturadas se retornan vía `console.log` (consoleMessage en el WebView).

## ⚙️ Funcionalidad destacada

- El botón de captura (`btn_capture`) se muestra/oculta dependiendo de los mensajes recibidos desde el WebView.
- La función JavaScript `window['continueDetection']()` que retorna el componente de autocaptura se utiliza para continuar con la detección desde el botón Android.
- La comunicación entre el WebView y el código Kotlin se maneja mediante `console.log`. Esta comunicación se puede modificar de acuerdo a sus necesidades.

## 🚀 Ventajas de la estrategia utilizada

- Se evita cargar el JavaScript directamente desde Kotlin.
- La estructura es modular: HTML, CSS y JS separados.
- Mayor seguridad al mantener el JavaScript sin modificaciones.
- La comunicación bidireccional WebView ↔️ Kotlin es sencilla mediante `console.log`.

## 🛡️ Permisos

El flujo solicita el permiso de cámara usando la nueva API (`ActivityResultContracts.RequestPermission`).

> **Nota**: Si el usuario niega el permiso, se muestra un `Toast`, pero se pueden extender este comportamiento para volver a solicitar el permiso si lo desean.

## 🧩 Personalización

- Puedes modificar libremente el contenido de `veridoc.html` y su estilo visual.
- El JavaScript debe permanecer sin cambios para asegurar compatibilidad y evitar problemas.


## 📝 Notas adicionales

- La propiedad `allowUniversalAccessFromFileURLs` está marcada como deprecated, pero se mantiene para permitir que el archivo HTML local realice solicitudes HTTPS si es necesario.
- Los mensajes JSON recibidos desde `console.log` son parseados para extraer el estado de error, las imágenes capturadas y controlar el flujo de botones.


