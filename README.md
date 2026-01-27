# 🛒 Supermercado API REST

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-green?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=flat-square)
![JWT](https://img.shields.io/badge/Security-JWT-red?style=flat-square)

## 📖 Descripción

Este proyecto es el Backend de una aplicación de **E-commerce para un Supermercado**, desarrollado como parte de un desafío técnico / proyecto personal. 

La API permite gestionar el flujo completo de compras: desde la administración de productos y sucursales, hasta la gestión del carrito de compras del usuario y la generación de órdenes de venta. Cuenta con un sistema de seguridad robusto basado en **Roles (Admin/User)** y autenticación vía **Tokens JWT**.

## 🚀 Tecnologías Utilizadas

* **Lenguaje:** Java 17
* **Framework Principal:** Spring Boot 3
* **Base de Datos:** MySQL (Ejecutada en contenedor Docker)
* **Seguridad:** Spring Security 6 + JWT (Json Web Token)
* **ORM:** Hibernate / JPA
* **Documentación:** OpenAPI (Swagger UI)
* **Herramientas:** Docker Compose, Maven, Lombok, ModelMapper.

## ✨ Funcionalidades Principales

### 🔐 Seguridad y Autenticación
* Registro e Inicio de Sesión (Devuelve Token JWT).
* Manejo de Roles (**ADMIN** y **USUARIO**).
* Protección de rutas mediante `Authorization: Bearer Token`.

### 📦 Gestión de Catálogo (Rol ADMIN)
* CRUD de **Sucursales**.
* CRUD de **Productos** (con control de Stock y Precios).
* Paginación y ordenamiento de listados para optimizar el rendimiento.

### 🛒 Experiencia de Compra (Rol USUARIO)
* **Carrito de Compras Inteligente:** Persistente en base de datos.
* Agregar/Quitar productos y cálculo automático de totales.
* **Checkout:** Generación de la orden de venta y vaciado automático del carrito.
* Historial de compras personal.

### 📊 Ventas (Rol ADMIN)
* Visualización de todas las ventas realizadas.
* Filtros y detalles de facturación.

## 🛠️ Instalación y Ejecución

### Prerrequisitos
* Tener instalado [Docker Desktop](https://www.docker.com/products/docker-desktop/).
* (Opcional) Java 17 y Maven si quieres correrlo sin Docker.

### Pasos
1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/lucianoZG/PruebaTecnicaSupermercado
    cd PruebaTecnicaSupermercado
    ```

2.  **Levantar la Base de Datos (y la App):**
    Asegúrate de que Docker Desktop esté corriendo y ejecuta:
    ```bash
    docker-compose up -d
    ```
    *Esto levantará MySQL en el puerto `3307` y la aplicación en el `8080`.*

3.  **¡Listo!**
    La API estará disponible en: `http://localhost:8080`

## 📑 Documentación de la API

El proyecto cuenta con documentación interactiva generada automáticamente con Swagger.

👉 **Ver Documentación Swagger:** http://localhost:8080/swagger-ui/index.html

### Endpoints de Ejemplo

| Método | Endpoint | Descripción | Rol Requerido |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Obtener Token JWT | Público |
| `GET` | `/api/productos` | Listar catálogo (Paginado) | Público |
| `POST` | `/api/productos` | Crear nuevo producto | **ADMIN** |
| `GET` | `/api/carrito/mi-carrito` | Ver mi carrito actual | **USUARIO** |
| `POST` | `/api/carrito/{id}/checkout/{sucursal}` | Finalizar compra | **USUARIO** |

## 🧪 Testing (Próximamente)

* [ ] Tests Unitarios con JUnit 5 y Mockito.
* [ ] Tests de Integración.

## 🗄️ Modelo de Datos (DER)

./assets/diagrama-der.png`

## 👤 Autor

**Luciano Zanni Giuliano**
* **LinkedIn:** [Tu Perfil](https://linkedin.com/in/tu-perfil)
* **Portfolio:** [Link a tu web si tienes]
* **Email:** lucianozannig@gmail.com
