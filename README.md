# SuitEtecsa SDK Kotlin

`SuitEtecsa SDK` es una herramienta diseñada para interactuar con los servicios de [ETECSA](https://www.etecsa.cu/). La librería utiliza técnicas de scrapping para acceder al portal de [acceso a internet ](https://secure.etecsa.net:8443/) de Nauta.

Al ser un proyecto open-source, se valoran y se reciben contribuciones de la comunidad de desarrolladores/as.

## Funciones implementadas

- [x] [Secure Etecsa](https://secure.etecsa.net:8443/)

    - [x] Iniciar sesión.
    - [x] Cerrar sesión.
    - [x] Obtener el tiempo disponible en la cuenta.
    - [x] Obtener la información de la cuenta.

- [x] [Portal de Usuario Nauta](https://www.nauta.cu/)

    - [x] Iniciar sesión.
    - [x] Obtener información de las cuentas a nombre del usuario (correo, navegación, telefonía móvil y telefonía fija).
    - [ ] Recargar las cuentas de navegación.
    - [ ] Transferir saldo a otra cuenta nauta.
    - [ ] Transferir saldo para pago de cuota (`solo para cuentas Nauta Hogar`).
    - [ ] Cambiar la contraseña de la cuenta de acceso.
    - [ ] Cambiar la contraseña de las cuentas de correo o navegación asociadas.
    - [ ] Obtener las conexiones realizadas en el mes especificado por las cuantas de navegación asociadas.
    - [ ] Obtener las recargas realizadas en el mes especificado a las cuentas de navegación asociadas.
    - [ ] Obtener las transferencias realizadas en el mes especificado por las cuantas de navegación asociadas.
    - [ ] Obtener los pagos de cuotas realizados en el mes especificado por las cuantas de navegación asociadas (`solo para cuentas Nauta Hogar`).

# Uso

Importa `SuitEtecsa SDK` en tu proyecto

[![](https://img.shields.io/maven-central/v/io.github.suitetecsa.sdk/kotlin.svg)](https://img.shields.io/maven-central/v/io.github.suitetecsa.sdk/kotlin.svg)


```kotlin
implementation("io.github.suitetecsa.sdk:kotlin:{last-version}")
```

Conectate a internet desde la wifi o Nauta Hogar

**Kotlin**

```kotlin
import io.github.suitetecsa.sdk.access.AccessClient

val client = AccessClient.Builder().build()
val dataSession = client.connect("user.name@nauta.com.cu", "userPassword.123")
```

**Java**
```java 
import io.github.suitetecsa.sdk.access.AccessClientRx;

AccessClientRx clientRx = new AccessClientRx.Builder().build();
clientRx.connect("user.name@nauta.com.cu", "userPassword.123")
.observeOn(AndroidSchedulers.mainThread()).subscribe(dataSession -> {
            // use dataSession here
        }, error -> { throw error });
```

## AccessClient/AccessClientRx

`AccessClient` es la clase encargada de comunicarse con el portal de acceso a internet de [ETECSA](https://www.etecsa.cu).

Funciones y variables de `AccessClient`:
- isConnected: comprueba si se está bajo el portal cautivo de Nauta, en caso positivo devuelve `false`.
- getUserInformation: devuelve información relevante de la cuenta especificada.
- connect: conecta la cuenta especificada. Devuelve un objeto DataSession que contiene la información necesaria para restablecer la sesión y poder cerrarla posteriormente.
- getRemainingTime: devuelve el tiempo restante de la cuenta que ha iniciado sesión. Requiere del objeto DataSession devuelto por `connect`.
- disconnect: desconecta la cuenta conectada. Requiere del objeto DataSession devuelto por `connect`.

## NautaApi

`NautaApi` es un objeto que proporciona instancias Retrofit configuradas listas para crear servicios API con o sin soporte RxJava. Esta están diseñadas para interactuar con el [portal Nauta](https://www.nauta.cu).

# Contribución

¡Gracias por tu interés en colaborar con nuestro proyecto! Nos encanta recibir contribuciones de la comunidad y valoramos mucho tu tiempo y esfuerzo.

## Cómo contribuir

Si estás interesado en contribuir, por favor sigue los siguientes pasos:

1. Revisa las issues abiertas para ver si hay alguna tarea en la que puedas ayudar.
2. Si no encuentras ninguna issue que te interese, por favor abre una nueva issue explicando el problema o la funcionalidad que te gustaría implementar. Asegúrate de incluir toda la información necesaria para que otros puedan entender el problema o la funcionalidad que estás proponiendo.
3. Si ya tienes una issue asignada o si has decidido trabajar en una tarea existente, por favor crea un fork del repositorio y trabaja en una nueva rama (`git checkout -b nombre-de-mi-rama`).
4. Cuando hayas terminado de trabajar en la tarea, crea un pull request explicando los cambios que has realizado y asegurándote de que el código cumple con nuestras directrices de estilo y calidad.
5. Espera a que uno de nuestros colaboradores revise el pull request y lo apruebe o sugiera cambios adicionales.

## Directrices de contribución

Por favor, asegúrate de seguir nuestras directrices de contribución para que podamos revisar y aprobar tus cambios de manera efectiva:

- Sigue los estándares de codificación y estilo de nuestro proyecto.
- Asegúrate de que el código nuevo esté cubierto por pruebas unitarias.
- Documenta cualquier cambio que hagas en la documentación del proyecto.

¡Gracias de nuevo por tu interés en contribuir! Si tienes alguna pregunta o necesitas ayuda, no dudes en ponerte en contacto con nosotros en la sección de issues o enviándonos un mensaje directo.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Esto significa que tienes permiso para utilizar, copiar, modificar, fusionar, publicar, distribuir, sublicenciar y/o vender copias del software, y para permitir que las personas a las que se les proporcione el software lo hagan, con sujeción a las siguientes condiciones:

- Se debe incluir una copia de la licencia en todas las copias o partes sustanciales del software.
- El software se proporciona "tal cual", sin garantía de ningún tipo, expresa o implícita, incluyendo pero no limitado a garantías de comerciabilidad, aptitud para un propósito particular y no infracción. En ningún caso los autores o titulares de la licencia serán responsables de cualquier reclamo, daño u otra responsabilidad, ya sea en una acción de contrato, agravio o de otra manera, que surja de, fuera de o en conexión con el software o el uso u otros tratos en el software.

Puedes encontrar una copia completa de la Licencia MIT en el archivo LICENSE que se incluye en este repositorio.

## Contacto

Si tienes alguna pregunta o comentario sobre el proyecto, no dudes en ponerte en contacto conmigo a través de los siguientes medios:

- Correo electrónico: [lesclaz95@gmail.com](mailto:lesclaz95@gmail.com)
- Twitter: [@lesclaz](https://twitter.com/lesclaz)
- Telegram: [@lesclaz](https://t.me/lesclaz)

Estaré encantado de escuchar tus comentarios y responder tus preguntas. ¡Gracias por tu interés en mi proyecto!
