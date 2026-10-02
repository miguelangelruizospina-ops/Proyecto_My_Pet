# 🐾 Mi Pet

Aplicación web para la **gestión integral de mascotas**, desarrollada para organizar y administrar información relacionada con usuarios, mascotas, historial médico, documentos, agenda, recordatorios, emergencias y servicios.

El proyecto está dividido en tres componentes principales:

* **Frontend:** interfaz con la que interactúa el usuario.
* **Backend:** lógica de negocio y API REST.
* **Base de datos:** almacenamiento y organización de la información.

---

## 🗄️ Base de datos

La base de datos de **Mi Pet** permite almacenar y gestionar la información necesaria para el funcionamiento de la aplicación.

### Gestor de base de datos

* **MySQL / MariaDB**
* **Base de datos:** `mypet`
* **Entorno:** XAMPP

### Tablas principales

* `usuario`
* `perfil_usuario`
* `mascota`
* `perfil_mascota`
* `historial_medico`
* `evento`
* `recordatorio`
* `documento`
* `administrador`
* `servicio`
* `publicacion_foro`
* `emergencia`
* `notificacion`

Las tablas se encuentran relacionadas mediante **claves primarias y claves foráneas**, permitiendo mantener la integridad y conexión entre la información de usuarios, mascotas y funcionalidades de la aplicación.

---

## ⚙️ Backend

El Backend se encarga de procesar las solicitudes del Frontend, ejecutar la lógica de negocio y gestionar la comunicación con la base de datos.

### Tecnologías

* **Java 17**
* **Spring Boot 3.5.14**
* **Gradle**
* **JPA / Hibernate**
* **MySQL / MariaDB**
* **Postman**

### Arquitectura

El Backend se encuentra organizado en diferentes capas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
JPA / Hibernate
    ↓
Base de datos
```

* **Controller:** recibe las solicitudes HTTP.
* **Service:** contiene la lógica de negocio.
* **Repository:** gestiona el acceso a los datos.
* **Model / Entity:** representa las entidades de la aplicación.
* **Security:** gestiona autenticación y autorización.
* **Configuration:** contiene las configuraciones necesarias del sistema.

---

## 🎨 Frontend

El Frontend corresponde a la interfaz visual de **Mi Pet** y permite al usuario interactuar con las diferentes funcionalidades del sistema.

### Tecnologías

* **HTML5**
* **CSS3**
* **JavaScript**
* **Bootstrap 5**
* **Fetch API**

### Funcionalidades

* 🔐 Inicio de sesión
* 👤 Gestión de usuarios
* 🐕 Gestión de mascotas
* 📋 Perfil de mascota
* 🩺 Historial médico
* 📄 Gestión de documentos
* 📅 Agenda
* 🔔 Recordatorios y notificaciones
* 🚨 Emergencias
* 💬 Comunidad y foro
* 🤖 Chatbot
* 🏥 Servicios
* ⚙️ Configuración
* 🔑 Recuperación de contraseña
* 👨‍💼 Administración

---

## 🔗 Comunicación del sistema

Los componentes de **Mi Pet** se comunican de la siguiente manera:

```text
┌─────────────────────┐
│      FRONTEND       │
│ HTML / CSS / JS     │
│ Bootstrap           │
└──────────┬──────────┘
           │
           │ API REST
           ▼
┌─────────────────────┐
│       BACKEND       │
│ Java / Spring Boot  │
│ Controller          │
│ Service             │
│ Repository          │
└──────────┬──────────┘
           │
           │ JPA / Hibernate
           ▼
┌─────────────────────┐
│    BASE DE DATOS    │
│   MySQL / MariaDB   │
│       `mypet`       │
└─────────────────────┘
```

De esta forma, el **Frontend** se comunica con el Backend mediante una **API REST**, mientras que el Backend utiliza **JPA/Hibernate** para realizar las operaciones sobre la base de datos.

---

## 🛠️ Herramientas utilizadas

| Herramienta     | Uso                                      |
| --------------- | ---------------------------------------- |
| Java            | Lenguaje principal del Backend           |
| Spring Boot     | Desarrollo del Backend y API REST        |
| Gradle          | Gestión y construcción del proyecto      |
| JPA / Hibernate | Persistencia y comunicación con la BD    |
| MySQL / MariaDB | Gestión de la base de datos              |
| XAMPP           | Entorno local para la base de datos      |
| HTML / CSS      | Estructura y estilos del Frontend        |
| JavaScript      | Interactividad y comunicación con la API |
| Bootstrap       | Diseño de interfaces                     |
| Postman         | Pruebas de la API                        |

---

## 📁 Estructura general

```text
Mi-Pet/
│
├── Backend/
│   ├── src/
│   ├── build.gradle
│   └── ...
│
├── Frontend/
│   ├── html/
│   ├── css/
│   ├── js/
│   └── ...
│
├── Base de datos/
│   └── mypet.sql
│
└── README.md
```

---

## 🎯 Objetivo

El objetivo de **Mi Pet** es proporcionar una plataforma que permita centralizar y organizar la información de las mascotas, facilitando la gestión de sus datos, historial médico, documentos, agenda, emergencias y otros servicios desde una misma aplicación.
