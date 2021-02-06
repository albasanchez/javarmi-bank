# Práctica RPC/RMI (Bank)

## Componentes

- [Bank](https://github.com/albasanchez/javarmi-bank)
- [ATM](https://github.com/albasanchez/javarmi-atm)

## Instrucciones

### Inicializar la base de datos

> Este proyecto utiliza PostgreSQL

- [Descargar e instalar el Driver de PostgreSQL](https://codigoxules.org/conectar-postgresql-utilizando-driver-jdbc-java-postgresql-jdbc/)
- Agregar el Driver a las Referenced Libraries del Java Project
- Crear una BD en PostgreSQL
- Ejecutar los scripts de creación que se encuentran en ./db/create.sql
- Crear el archivo ./config.properties con las credenciales de la BD (utilizando de referencia el archivo ./config-template.properties)

### Compilación

1. Ejecutar javac \*.java

### Ejecución

1. Inicializar rmiregistry y esperar a que esté activo
   - En Windows: start rmiregistry
   - En Linux: rmiregistry &
2. Iniciar el servidor
   > En caso de estar utilizando VSCode, se debe hacer click al botón de Run o realizar F5 desde el archivo ./Server.java. Esto para que el Driver sea registrado satisfactoriamente en la compilación del código
   - java Server
3. [Iniciar el cliente ATM](https://github.com/albasanchez/javarmi-atm)

**NOTA**: Cada vez que el servidor se detiene es necesario reiniciar el rmiregistry
