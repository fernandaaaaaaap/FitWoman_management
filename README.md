Documentación API - Entrenamientos API para gestionar entrenamientos y fotos.
usuario del cliente: fitwoman
contraseña:1234 ( es para la prueba de la app que se ocupa eso para el incio de sesion) 

Servidor El servidor corre en: http://localhost:3000 y la url del API en la nube conn render: https://fitwoman-api.onrender.com

1)Create entrenamiento metodo POST URL:http://localhost:3000/api/entrenamientos JSON: { "nombre": "Leg Day", "categoria": "Strength", "descripcion": "Complete leg workout", "duracionMinutos": 45, "grupoMuscular": "Legs", "notas": "Focus on glutes" }

Read all Metodo GET URL: http://localhost:3000/api/entrenamientos ( no tiene body su respuesta esperada es success)

Read one por medio de un ID Metodo: GET URL: http://localhost:3000/api/entrenamientos/{ID} body: niguno

UPDATE metodo: PUT URL:http://localhost:3000/api/entrenamientos/{ID} Body: { "nombre": "Leg Day - UPDATED", "duracionMinutos": 60 }
DELETE URL: http://localhost:3000/api/entrenamientos/{ID} body ninguno.

FOTOS PROGRESO:

1)CREATE Metodo: POST URL: http://localhost:3000/api/fotos body: { "rutaImagen": "data:image/jpeg;base64,/9j/4AAQSkZJRg...", "mes": "January 2025", "peso": 65.5, "categoria": "Front View", "notas": "First progress photo" }

READ ALL Metodo: GET URL: http://localhost:3000/api/fotos body: ninguno

READ ONE
Método: GET URL: http://localhost:3000/api/fotos/{ID} Body: Ninguno

UPDATE Metodo: PUT URL: http://localhost:3000/api/fotos/{ID} body:

{ "mes": "February 2025", "peso": 64.0 }

DELETE URL: http://localhost:3000/api/fotos/{ID}

adjunto evidencia de una prueba: WhatsApp Image 2025-12-07 at 6 09 06 PM

 
 <img width="890" height="855" alt="image" src="https://github.com/user-attachments/assets/fcbfac37-c621-4ee3-ae61-e80f00b0b439" />
  <img width="552" height="370" alt="image" src="https://github.com/user-attachments/assets/c151ba20-3fb6-4296-a85a-e27f6fbd82b8" />


