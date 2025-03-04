# CodeHub

| Usuario de GitHub       | Nombre                            | Correo electrónico                           |
|-------------------------|-----------------------------------|----------------------------------------------|
| [Sonaca](https://github.com/Sonaca)      | Diego Ismael Cantador Trapero | di.cantador.2022@alumnos.urjc.es |
| [CazaMopis43](https://github.com/CazaMopis43) | Marc Burgos Ucendo           | m.burgos.2022@alumnos.urjc.es |
| [ASastre03](https://github.com/ASastre03)    | Alberto Sastre Zorrilla      | a.sastrez.2022@alumnos.urjc.es |




# Entidades

Username-> Es la entidad del usuario

Topic-> Entidad que referencia los Temas

Post-> Entidad que referencia a las Publicaciones

Comment-> Son los comentarios de una Publicaciónç

# Herramientas
Para la realización de esta práctica hemos utilizado la extensión de Visual Studio de Live Share, la cual nos ha permitido trabajar simultáneamente sin necesidad de estar haciendo diversos commits para tener el codigo actualizado

# Imágenes
El ususario tiene un atributo llamado profilePicture que es la foto de perfil

# Diagrama de clases y Templates
![Description](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-2/blob/main/READMEDATA/DDC.svg)

# Diagrama de Entidades

![Description](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-2/blob/dac41fde31372ecd60a01eebe83601655cca6538/READMEDATA/Screen/EntityDiagram.jpg)

# Diagrama de la base de Datos

![Description](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-2/blob/main/READMEDATA/SQLDiagram.jpg)
# 🧭 NAVIGATION

## **LOGIN**


![Log In](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-2/blob/a7083cc08cae2846530de436f0a0792c0db97e4b/READMEDATA/Screen/LogIn.png)

Esta es la pantalla que encontramos nada más iniciar la web. Desde ella podemos inciar sesión o bien crear un usuario. 

## **SIGN UP**


![Sign Up](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-2/blob/a7083cc08cae2846530de436f0a0792c0db97e4b/READMEDATA/Screen/Sign-up.png)

Esta pantalla nos permite crear un usuario introduciendo usuario,email y constraseña. Nos llevará a Log In.

## **POST**


![Post](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-2/blob/a7083cc08cae2846530de436f0a0792c0db97e4b/READMEDATA/Screen/Posts.png)

Esta es la Pantalla post, en ella podemos ver todos los posts que se han creado. Desde aquí podemos volver a la pantalla Inicial, ver el post en detalle o crear un post si así lo deseamos.

## **ADD POST**


![Add Post](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-2/blob/a7083cc08cae2846530de436f0a0792c0db97e4b/READMEDATA/Screen/CrearPost.png)

Aquí podemos Crear un post en el que estableceremos un Titulo, el cuerpo del Post y el topic al que está  asociado.

## **SHOW MORE POSTS**


![Show more Post](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-2/blob/a7083cc08cae2846530de436f0a0792c0db97e4b/READMEDATA/Screen/Show%20more%20post.png)

Aquí podemos ver en detalle el post, viendo así su Creador, su título, su contenido y sus comentarios asociados si es que los tuviera.

## **TOPIC**


![Topic](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-2/blob/a7083cc08cae2846530de436f0a0792c0db97e4b/READMEDATA/Screen/Topic.png)

En el apartado Topic podemos ver todos los topics que existen. Desde aquí podemos crear un Topic nuevo, volver o expandir el topic para ver los posts que este tiene asociados.

## **ADD TOPIC**


![Add Topic](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-2/blob/a7083cc08cae2846530de436f0a0792c0db97e4b/READMEDATA/Screen/addTopic.png)

Esta es la pantalla para crear un topic, en ella se solicita el nombre que se quiere poner al nuevo topic.

## **POST BY TOPIC**


![Post by Topic](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-2/blob/a7083cc08cae2846530de436f0a0792c0db97e4b/READMEDATA/Screen/PostByTopic.png)

Esta es la pantalla que expande un topic. En ella podemos ver todos los posts asociados así como el poder borrar un post o expandirlo, o eliminar el topic.

## ** CREATE COMMENT**


![CreateComent](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-2/blob/a7083cc08cae2846530de436f0a0792c0db97e4b/READMEDATA/Screen/CreateComent.png)

Para crear un comment necesitarmeos estar dentro de un post y se nos solicitará el contenido de nuestro comment.

## **PROFILE**


![profile](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-2/blob/a7083cc08cae2846530de436f0a0792c0db97e4b/READMEDATA/Screen/Profile.png)

Este es el apartado de perfil, en el podremos ver tanto nuestro user como el mail, así como la foto que hemos puesto de perfil y podremos mostrar nuestra constaseña si así lo queremos

# Instrucciones de ejecución

1.Descargar el repositorio y descomprimirlo en una carpeta.

2.Descargar MySQL Configurator y MySQL Worckbench.

3.En MySQL Configurator poner Usuario: root, Password: grupo15SQL y el puerto predeterminado (3306).

4.En MySQL Worckbench añadir una conexión con el usuario y password previamente introducidos.

5.Crear un esquema llamado bookshop en la conexión anterior.

6.Ejecutar la aplicación.

7.Ir a http://localhost:8080/




