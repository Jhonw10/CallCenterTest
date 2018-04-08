# CallCenterTest


Clases que contiene la lógica para manejar las llamadas. Para temas de concurrencia debido a la cantidad de hilos que
pueden llegar de manera concurrente, se usa la utilidad Semaforo, que nos permite evitar asignar a empleados ocupados
dos tareas o mas al  mismo tiempo, esto aplica para todos los tipos de empleado. Se especifica que solo se procesarán
de a 10 threads, por eso se define el poolThread.

La implementación de cada empleado tiene el timeout (No se especifica pero es posible que cada uno tenga un tiempo
en línea diferente).

Se establece la variable Busy, esto para identificar que empleado anda ocupado, y así no asignarle mas tareas.
