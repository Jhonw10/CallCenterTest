package co.com.almundo.dispatcher;

import co.com.almundo.model.Director;
import co.com.almundo.model.Employee;
import co.com.almundo.model.Operator;
import co.com.almundo.model.Supervisor;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Clae que contiene la lógica para manejar las llamadas. Para temas de concurrencia debidos a la cantidad de hilos que
 * pueden llegar de manera concurrente, se usa la utilidad Semaforo, que nos permite evitar asignar a empleados ocupados
 * dos tareas o mas al  mismo tiempo, esto aplica para los trea tipos de empleado. Se especifica que solo se procesarán
 * de a 10 threads, por eso se define el poolThread.
 *
 * La implementación de cada empleado tiene el timeout (No se especifica pero es posible que cada uno tenga un tiempo
 * en línea diferente).
 *
 * Se establece la variable Busy, esto para identificar que empleado anda ocupado, y así no asignarle mas tareas.
 */
public class Dispatcher {

    ExecutorService executor = Executors.newFixedThreadPool(10);
    private List<Operator> operators;
    private List<Supervisor> supervisors;
    private List<Director> directors;
    private final Semaphore permitOperators = new Semaphore(1);
    private final Semaphore permitSupervisor = new Semaphore(1);
    private final Semaphore permitDirectors = new Semaphore(1);

    public Dispatcher(List<Operator> operators, List<Supervisor> supervisors, List<Director> directors) {
        this.operators = operators;
        this.supervisors = supervisors;
        this.directors = directors;
    }

    public boolean dispatchCall(){
        Employee employee = getOperatorToAnswerCall();
        if(employee == null) {
            employee = getSupervisorToAnswerCall();
            if (employee == null) {
                employee = getDirectorToAnswerCall();
            }
        }
        if (employee == null) {
            System.out.println("Su llamada no puede ser atendida en estos momentos.");
            return false;
        }else{
            executor.execute(employee);
            return true;
        }
    }

    public Operator getOperatorToAnswerCall(){
        Operator operator = null;
        if (permitOperators.tryAcquire()) {
            if (operators != null && operators.size() > 0) {
                operator = operators.stream().filter(emp -> !emp.getBusy()).findFirst().orElse(null);
            }
            if (operator != null) {
                operator.setBusy(true);
            }
            permitOperators.release();
        }
        return operator;
    }

    public Supervisor getSupervisorToAnswerCall(){
        Supervisor supervisor = null;
        if (permitSupervisor.tryAcquire()) {
            if (supervisors != null && supervisors.size() > 0) {
                supervisor = supervisors.stream().filter(emp -> !emp.getBusy()).findFirst().orElse(null);
            }
            if (supervisor != null) {
                supervisor.setBusy(true);
            }
            permitSupervisor.release();
        }
        return supervisor;
    }

    public Director getDirectorToAnswerCall(){
        Director director = null;
        if (permitDirectors.tryAcquire()) {
            if (directors != null && directors.size() > 0) {
                director = directors.stream().filter(emp -> !emp.getBusy()).findFirst().orElse(null);
            }
            if (director != null){
                director.setBusy(true);
            }
            permitDirectors.release();
        }
        return director;
    }
}
