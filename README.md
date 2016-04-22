# iTracking
Desarrollo de una app para Android

La aplicación dibuja y almacena la ruta que sigue el usuario. 
Al abrir la aplicación se muestra el menú principal en el que aparecen varias opciones. Por una parte tres botones de actividad (ANDAR, CORRER, BICI) y por otra el HISTORIAL DE ACTIVIDADES.

* Botones de actividad --> Al pulsar sobre cualquiera de las tres opciones se inicia la actividad con el mapa. En la parte inferior se muestran, por una parte los marcadores de tiempo, distancia y velocidad y por otra los botones de iniciar, cambiar el tipo de mapa y el de FINALIZAR. Nada más cargar la actividad el botón de finalizar está deshabilitado. Si cuando se pulsa iniciar no hay localización disponible la aplicación impide comenzar. Cuando se inicia la actividad el mapa se centra y hace zoom en la localización, el cronómetro empieza a contar y se dibuja el movimiento de usuario en el mapa. Cada ver que se pulsa el botón para cambiar de mapa éste se cambia entre NORMAL, SATÉLITE e HÍBRIDO.
      * Finalizar --> Tras finalizar la actividad se muestra un resumen y se da la opción de GUARDAR o CANCELAR la actividad realizada. Si se guarda, se almacena en el historial de actividades.
      
* Historial de actividades --> Aquí se muestra la lista con todas las actividades realizadas. En la vista de lista, por cada una de ellas aparece un icono que indica el tipo de actividad (ANDAR, CORRER, BICI), la fecha, la duración y la distancia recorrida. Si pinchamos en una de ellas se muestra además el mapa con la ruta realizada y permite eliminarla.
