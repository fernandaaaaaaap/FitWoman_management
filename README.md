# Documentación API - Entrenamientos

API para gestionar entrenamientos y fotos.

## Servidor
El servidor corre en: `http://localhost:3000`

---

## ENTRENAMIENTOS

### Obtener todos los entrenamientos
**GET** `http://localhost:3000/api/entrenamientos`

---

### Crear entrenamiento
**POST** `http://localhost:3000/api/entrenamientos`

JSON:
```json
{
  "nombre": "Rutina de Piernas",
  "descripcion": "Entrenamiento completo de piernas",
  "categoria": "Fuerza",
  "duracionMinutos": 45,
  "grupoMuscular": "Piernas"
}
```

---

### Actualizar entrenamiento
**PUT** `http://localhost:3000/api/entrenamientos/595162df`

JSON:
```json
{
  "nombre": "Rutina de Pecho Avanzada",
  "descripcion": "Entrenamiento modificado",
  "categoria": "Fuerza",
  "duracionMinutos": 75,
  "grupoMuscular": "Pecho"
}
```

---

### Eliminar entrenamiento
**DELETE** `http://localhost:3000/api/entrenamientos/595162df`

---

## FOTOS

### Crear foto
**POST** `http://localhost:3000/api/fotos`

JSON:
```json
{
  "url": "https://ejemplo.com/foto.jpg",
  "descripcion": "Foto de entrenamiento",
  "entrenamientoId": "595162df"
}
```

---

## Notas
- Cambiar `595162df` por el ID real del entrenamiento
- Para POST y PUT usar Body → raw → JSON en Postman
