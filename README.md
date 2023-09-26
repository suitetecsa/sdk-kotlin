# SuitEtecsa SDK Kotlin

`SuitEtecsa SDK` es una herramienta diseñada para interactuar con los servicios de [ETECSA](https://www.etecsa.cu/). La librería utiliza técnicas de scrapping para acceder a los portales de [acceso a internet ](https://secure.etecsa.net:8443/)y de [usuario](https://www.portal.nauta.cu/) de Nauta. Implementa funciones para todas las operaciones disponibles en ambos portales, y ofrece soporte para Nauta Hogar.

Al ser un proyecto open-source, se valoran y se reciben contribuciones de la comunidad de desarrolladores/as.

## Funciones implementadas

- [x] [Secure Etecsa](https://secure.etecsa.net:8443/)

    - [x] Iniciar sesión.
    - [x] Cerrar sesión.
    - [x] Obtener el tiempo disponible en la cuenta.
    - [x] Obtener la información de la cuenta.

- [x] [Portal de Usuario](https://www.portal.nauta.cu/)

    - [x] Iniciar sesión.
    - [x] Obtener información de la cuenta.
    - [x] Recargar la cuenta.
    - [x] Transferir saldo a otra cuenta nauta.
    - [x] Transferir saldo para pago de cuota (`solo para cuentas Nauta Hogar`).
    - [x] Cambiar la contraseña de la cuenta de acceso.
    - [x] Cambiar la contraseña de la cuenta de correo asociada.
    - [x] Obtener las conexiones realizadas en el periódo `año-mes` especificado.
    - [x] Obtener las recargas realizadas en el periódo `año-mes` especificado.
    - [x] Obtener las transferencias realizadas en el periódo `año-mes` especificado.
    - [x] Obtener los pagos de cuotas realizados en el periódo `año-mes` especificado (`solo para cuentas Nauta Hogar`).

# Uso

Importa `SuitEtecsa SDK` en tu proyecto

[![](https://jitpack.io/v/suitetecsa/sdk-kotlin.svg)](https://jitpack.io/#suitetecsa/sdk-kotlin)

**Groovy**
```groovy
implementation 'com.github.suitetecsa:sdk-kotlin:{last-version}'
```

**Kotlin DSL**
```kotlin
implementation("com.github.suitetecsa:sdk-kotlin:{last-version}")
```

Conectate a internet desde la wifi o Nauta Hogar

**Kotlin**
```kotlin
import cu.suitetecsa.sdk.nauta.ConnectApi

val connectApi = ConnectApi.Builder().build()
connectApi.setCredentials("user.name@nauta.com.cu", "userPassword.123")
connectApi.connect()
  .onSuccess { result ->
      if (result == "Connected") {
          // Todo bien
      }
  }. onFailure { exception: Throwable ->
      throw exception
  }
```

**Java**
```java 
import cu.suitetecsa.sdk.nauta.rxjava.ConnectApi;
ConnectApi connectApi = new ConnectApi.Builder().build();
connectApi.setCredentials("user.name@nauta.com.cu", "userPassword.123");
connectApi.connect().observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
            if (result == "Connected") {
                // Todo bien
            }
        }, error -> { throw error });
```

## ConnectApi

`ConnectApi` es la clase encargada de comunicarse con el portal de acceso a internet de [ETECSA](https://www.etecsa.cu). Por razones de compatibilidad, si usas Java como lenguaje para programar, debes importar la clase ubicada en el paquete `cu.suitetecsa.sdk.nauta.rxjava` (tanto para `ConnectApi`, como para las demás).

Funciones y variables de `ConnectApi`:
- username (Nullable): nombre de usuario especificado con el cual se iniciará sesión en el portal.
- password (Nullable): contraseña especificada con la cual se iniciará sesión en el portal.
- isConnected: comprueba si se está bajo el portal cautivo de Nauta, en caso positivo devuelve `false`.
- dataSession (Nullable): un objeto que, una vez iniciada la sesión, devuelve la información necesaria para restablecer la misma y poder cerrarla posteriormente.
- remainingTime: devuelve el tiempo restante de la cuenta que ha iniciado sesión.
- connectInformation: devuelve información relevante de la cuenta especificada con `setCredentials`.
- setCredentials: se usa para especificar las credenciales con las que se iniciará sesión.
- connect: conecta la cuenta especificada.
- disconnect: desconecta la cuenta conectada.

__Nota__: Tanto la clase del paquete `cu.suitetecsa.sdk.nauta` como la de `cu.suitetecsa.sdk.nauta.rxjava` se instancian de la misma manera y tienen las mismas funciones y variables.

## UserPortalAuthApi

`UserPortalAuthApi` es la clase encargada de manejar la sesión en el [portal de usuario de Nauta](https://www.portal.nauta.cu).

Funciones y variables de `UserPortalAuthApi`:
- isNautaHome: indica si la cuenta que inició sesión es una cuenta asociada al servicio de Nauta Hogar (devuelve `false` por defecto, solo debe tenerse en cuenta una vez que se haya iniciado sesión con una cuenta).
- setCredentials: se usa para especificar las credenciales con las que se iniciará sesión.
- captchaImage: devuelve la imagen captcha proporcionada por el portal como un arreglo de bytes.
- userInformation: devuelve la información del usuario que ha iniciado sesión.
- login: recibe el código captcha contenido en la imagen e inicia la sesión en el portal.

## UserPortalBalanceHandler

`UserPortalBalanceHandler` es la clase encargada de manejar el balance de la cuenta que ha iniciado sesión.

Funciones y variables de `UserPortalBalanceHandler`:
- topUpBalance: recarga el saldo de la cuenta que inició sesión usando un código de 12 o 16 dígitos.
- transferFunds: si se establece un `destinationAccount`, transfiere saldo a la cuenta especificada; de lo contrario, intenta pagar una cuota del servicio Nauta Hogar (solo en cuentas asociadas a este servicio).

## UserPortalPasswordManager

`UserPortalPasswordManager` es la clase encargada de manejar la seguridad de la cuenta que ha iniciado sesión en el portal.

Funciones y variables de `UserPortalPasswordManager`:
- changePassword: cambia la contraseña de acceso a internet de la cuenta que ha iniciado sesión.
- changeEmailPassword: cambia la contraseña del correo asociado a la cuenta que inició sesión.

## UserPortalActionsProvider

`UserPortalActionsProvider` es la clase encargada de recuperar la información de las acciones (conexiones, recargas, transferencias, fondos de cuota) llevadas a cabo por la cuenta que ha iniciado sesión.

Funciones y variables de `UserPortalActionsProvider`:
- getConnectionsSummary: devuelve el resumen de conexiones llevadas a cabo en un mes de un año especificado.
- getConnections: devuelve las conexiones llevadas a cabo en un mes de un año especificado.
- getRechargesSummary: devuelve el resumen de recargas llevadas a cabo en un mes de un año especificado.
- getRecharges: devuelve las recargas llevadas a cabo en un mes de un año especificado.
- getTransfersSummary: devuelve el resumen de transferencias llevadas a cabo en un mes de un año especificado.
- getTransfers: devuelve las transferencias llevadas a cabo en un mes de un año especificado.
- getQuotesPaidSummary: devuelve el resumen de fondos de cuotas en un mes de un año especificado.
- getConnections: devuelve los fondos de cuotas en un mes de un año especificado.

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
