# PVJ

![Logo](src/main/resources/imagenes/logo.png)

Software de Punto de Venta simple para tiendas de abarrotes, desarrollada para JDK 11 y MariaDB.

## Nombre
PVJ son siglas para Punto de Venta Jemmaengz. El nombre es una marca no registrada y es la designación al producto de software entregable.

## ¿Por qué?
En su momento, uno de mis primeros trabajos pagados, fue crear un Sistema de Punto de Venta para una tienda de abarrotes de mi localidad, a medida y según los requerimientos establecidos. Al desarrollarla (una versión modificada de éste tipo proyecto) y al contactar con más clientes con ésta necesidad, me di cuenta de que algunos clientes no requieren de herramientas complejas y sofisticadas para realizar la gestión completa de su negocio, sino más bien una herramienta de apoyo *sencilla*, **simple** que pueda ser usada por prácticamente cualquier edad/conocimiento y que vaya al grano. He decidido compartir una de las versiones con pocos cambios a la medida, a manera de boilerplate para mí y para cualquier persona que se vea en la necesidad de desarrollar una herramienta de éste tipo, ya sea que use un stack parecido al mío, o que tenga necesidades similares. Además, así tengo un repo en Java para cuando toque volver a juguetear con él y/o si modifico el propio boilerplate para necesidades futuras.

## Licencia
El código, las imágenes y estilos son licenciados bajo [BSD 2-Clause Simplified License](https://github.com/jemmaengz1202/pvj/blob/master/LICENSE), que en resumen te permite hacer casi lo que quieras; incluído usar este proyecto, modificarlo, venderlo, cerrar el código para tu propio uso o distribuirlo. No se ofrece garantía y cualquier pormenor causado por el proyecto es tu propia responsabilidad.

## Requisitos de uso
* [MariaDB](https://github.com/MariaDB/server)
* [JRE/OpenJDK JRE 11](https://adoptopenjdk.net/)
* Para tickets: Impresora térmica de tickets genérica

## Requisitos para build
* [Maven 3.6.1+](https://maven.apache.org/download.cgi)
* [JDK/OpenJDK 11](https://adoptopenjdk.net/)

## Configuración
1. Proveer las variables de entorno `DB_URL`, `DB_USER`, `DB_PASSWORD` (correspondientes a las credenciales de la BD MariaDB) y `PVJ_ENV` (`"dev" ó "prod"` para modo de desarrollo o producción, en caso de ser necesario) antes de la ejecución; o bien, proveer un archivo `.env` dentro del directorio `src/main/resources` con el formato correspondiente (ver ejemplo en [src/main/resources/.env.example](https://github.com/jemmaengz1202/pvj/blob/master/src/main/resources/.env.example)) antes del proceso de empaquetado.
2. Proveer en la base de datos, los disparadores correspondientes a la automatización interna de actualización de registros, o bien, utilizar el código en [triggers_and_views.sql](github.com/jemmaengz1202/pvj/blob/master/triggers_and_views.sql).

## Uso
### Empaquetar
Estando en el directorio raiz y desde la terminal:
```
  mvn package
```
generará un uber-jar portable y ejecutable en el directorio `target` y con la extensión `shaded.jar`. Copiarlo al directorio deseado y usar como corresponde.

### Ejecutar
Estando en el directorio raiz y desde la terminal:
```
  mvn exec:java
```
ejecutará el proyecto.

## Tecnologias utilizadas
* Java 11
* JavaFX
* JFoenix
* MariaDB
* Hibernate
* Java-Dotenv
* JasperReports
* Maven

Entre otras.

## Características
* Interfaz limpia y muy fácil de usar, pensada en la practicidad
* Roles de usuario (Administrador, Jefe y Empleado)
* Sistema de compras sencillo
* Ventas
* Impresión de tickets
* Historial de compras, ventas y sesiones

Entre otras.

## Imágenes del programa
![Imgur](https://i.imgur.com/YtERCHo.png)
![Imgur](https://i.imgur.com/kwMPdbs.png)
![Imgur](https://i.imgur.com/J1D4Ogv.png)
![Imgur](https://i.imgur.com/ADcMb4V.png)
![Imgur](https://i.imgur.com/cjY5QBj.png)
![Imgur](https://i.imgur.com/MNJOtl7.png)
