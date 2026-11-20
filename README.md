# 🩺 Sistema de Gestión de Exámenes Clínicos — Laboratorio Médico

[![Java](https://img.shields.io/badge/Java-11%20%7C%2021-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-13-blue?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.9.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.10.0-25A162?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![SonarQube](https://img.shields.io/badge/SonarQube-Passed-4B9CD3?style=for-the-badge&logo=sonarqube&logoColor=white)](http://localhost:9000)

> **Proyecto Académico de Refactorización y Calidad de Software**  
> **Institución:** Politécnico Grancolombiano  
> **Estudiante / Desarrollador:** Jostin Santiago Perez Agudelo  
> **Repositorio Fork:** [https://github.com/Goldiman6/ParadigmasExamen2](https://github.com/Goldiman6/ParadigmasExamen2)

---

## 📌 1. Descripción del Proyecto

El presente proyecto corresponde a la refactorización arquitectónica integral y aseguramiento de la calidad de un sistema de laboratorio clínico para el registro, cotización y persistencia de exámenes médicos (inicialmente enfocado en exámenes de sangre).

El proyecto original presentaba un diseño monolítico, alto acoplamiento hacia interfaces gráficas (JavaFX) y mecanismos de persistencia rígidos. A través de este trabajo se transformó en una **Arquitectura Hexagonal (Ports & Adapters)** desacoplada, complementada con tres **Patrones de Diseño GoF** (uno Creacional, uno Estructural y uno Comportamental), una suite exhaustiva de pruebas unitarias automatizadas con **JUnit 5**, y un análisis de calidad estático con **SonarQube** con resultado **Passed (0 bugs, 0 vulnerabilidades, 0 code smells y 0.0% duplicación)**.

---

## 🏛️ 2. Arquitectura de Software: Hexagonal (Ports & Adapters)

El sistema aísla por completo las reglas de negocio del mundo exterior (UI, persistencia, frameworks).

```
co.edu.poli.Examen2/
├── domain/                               # NÚCLEO DE DOMINIO (Independiente de frameworks)
│   ├── model/                            # Entidades y objetos de valor (Examen, ExamenSangre, etc.)
│   ├── exceptions/                       # Excepciones de negocio (DomainValidationException)
│   ├── builder/                          # Patrón Creacional: Builder
│   ├── decorator/                        # Patrón Estructural: Decorator
│   └── strategy/                         # Patrón Comportamental: Strategy
├── ports/                                # CONTRATOS / INTERFACES DEL HEXÁGONO
│   ├── in/                               # Puertos de Entrada (Casos de Uso: Registrar, Listar, Guardar, Cargar)
│   └── out/                              # Puertos de Salida (ExamenRepositoryPort)
├── application/service/                  # CAPA DE APLICACIÓN
│   └── ExamenApplicationService.java     # Orquestación de casos de uso y lógica de aplicación
├── infrastructure/adapters/out/          # ADAPTADORES DE SALIDA (Persistencia)
│   ├── persistence/FileSerializationAdapter.java  # Serialización en disco
│   └── persistence/InMemoryExamenAdapter.java     # Persistencia en memoria (Testing)
├── controlador/                          # ADAPTADORES DE ENTRADA (JavaFX Controller)
│   └── PrimaryController.java
└── vista/                                # VISTA / INTERFAZ DE USUARIO
    └── App.java
```

---

## 🎨 3. Patrones de Diseño GoF Implementados

Se incorporó un patrón de cada categoría principal del catálogo GoF para resolver problemas reales de diseño y extensibilidad:

| Categoría | Patrón GoF | Clases Clave | Problema que Resuelve |
|---|---|---|---|
| **Creacional** | **Builder** | `ExamenBuilder`<br>`ExamenSangreBuilder`<br>`ExamenDirector` | Construcción paso a paso de exámenes clínicos complejos. Valida de forma estricta las invariantes (código no nulo, costo > 0, paciente válido) impidiendo la existencia de objetos en estados inconsistentes. |
| **Estructural** | **Decorator** | `ExamenDecorator`<br>`UrgenciaDecorator`<br>`DomicilioDecorator` | Adición dinámica de servicios y recargos clínicos (urgencia +25%, toma a domicilio +$25.00) en tiempo de ejecución de forma transparente y acumulativa, sin sufrir explosión combinatoria de subclases. |
| **Comportamental** | **Strategy** | `TarifaStrategy`<br>`TarifaConDescuentoStrategy`<br>`TarifaParticularStrategy`<br>`TarifaTerceraEdadStrategy`<br>`TarifaConvenioEpsStrategy` | Encapsulamiento de políticas de tarificación y descuento intercambiables en tiempo de ejecución (Particular 100%, Tercera Edad 15% desc., Convenio EPS 25% desc.), respetando el principio Open/Closed (OCP). |

---

## 🧪 4. Calidad de Software, Pruebas y SonarQube

### Pruebas Unitarias (JUnit 5)
El sistema cuenta con **39 pruebas unitarias automatizadas** que cubren la totalidad de los patrones y la capa de aplicación:
* `ExamenBuilderTest`: Validación de construcción, campos requeridos, excepciones y Director (9 pruebas).
* `ExamenDecoratorTest`: Pruebas de recargos porcentuales, tarifas fijas, composición en cadena y transparencia (8 pruebas).
* `TarifaStrategyTest`: Pruebas parametrizadas sobre cálculos matemáticos de descuento e intercambiabilidad polimórfica (14 evaluaciones).
* `ExamenApplicationServiceTest`: Casos de uso de registro, unicidad de códigos, inmutabilidad y persistencia (8 pruebas).

### Métricas de Calidad SonarQube (Quality Gate: PASSED)
* **Security:** `A` (0 Vulnerabilities, 0 Secrets expuestos).
* **Reliability:** `A` (0 Bugs).
* **Maintainability:** `A` (0 Code Smells / Deuda técnica).
* **Hotspots Reviewed:** `A` (100% seguros).
* **Duplications:** `0.0%` (duplicación eliminada mediante abstracción en Strategy).
* **Coverage:** `65.0%` (concentrado en Dominio y Aplicación).

---

## 🚀 5. Instrucciones de Instalación y Ejecución

### Requisitos Previos
* **Java Development Kit (JDK):** Versión 11 o superior (Recomendado Java 21).
* **Apache Maven:** Versión 3.8+ o superior.
* **IDE (Opcional):** Eclipse IDE for Java Developers.

### Compilación y Ejecución de Pruebas Unitarias
Desde la raíz del subproyecto `Examen2/`:
```bash
mvn clean test
```

### Ejecución de la Aplicación Gráfica (JavaFX)
```bash
mvn clean javafx:run
```
*O en Eclipse: Clic derecho sobre `src/main/java/co/edu/poli/Examen2/vista/App.java` -> **Run As** -> **Java Application**.*

### Ejecución del Análisis Estático de SonarQube
Con el servidor SonarQube activo localmente (`http://localhost:9000`):
```bash
mvn clean verify sonar:sonar -Dsonar.token=<TU_SONAR_TOKEN>
```

---

## 📑 6. Estructura de Entregables y Enlaces a la Wiki

La documentación detallada de cada fase se encuentra publicada en la **GitHub Wiki** del repositorio:

* **Actividad 1:** Análisis arquitectónico del proyecto original, identificación de acoplamiento, fallas de persistencia y diagnóstico (`actividad1.pdf`).
* **Actividad 2:** Definición de la Arquitectura Hexagonal, especificación de puertos y adaptadores (`dominio2.png`, `arquitectura2.png`, `actividad2.pdf`).
* **Actividad 3:** Especificación formal e implementación de los 3 Patrones de Diseño GoF (`dominio3.png`, `actividad3.pdf`).
* **Actividad 4:** Aseguramiento de calidad, documentación JavaDoc completa, suite de pruebas unitarias y reporte de SonarQube.
* **Actividad 5:** Video de sustentación técnica del proyecto (`video5`).

---

## 🤖 7. Declaración y Justificación del Uso de Inteligencia Artificial

En cumplimiento con los lineamientos del trabajo se dan los usos de la IA en el proyecto

* **Rol de la IA:** Se utilizó Inteligencia Artificial (Gemini) como herramienta de **consultoría técnica, reducción sistemática de errores y aceleración en la generación de código**.
* **Diseño y Control del Autor:** La arquitectura, los diagramas de dominio y los modelos de interacción fueron concebidos, diseñados y definidos por mi desde Draw.io. La IA actuó asistiendo en la traducción de dichos diagramas hacia código fuente en Java, la configuración de perfiles en Maven (`pom.xml`), la erradicación de falsos positivos y duplicaciones detectadas por SonarQube, y la estructuración de la suite de pruebas unitarias en JUnit 5.
* **Validación Humana:** Todo el código, compilación, ejecución de pruebas unitarias y análisis estático fueron revisados, probados y verificados manualmente dentro de Eclipse IDE y entornos locales por parte del estudiante.
