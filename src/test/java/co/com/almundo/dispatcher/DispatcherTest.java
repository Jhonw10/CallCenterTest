package co.com.almundo.dispatcher;

import co.com.almundo.model.Director;
import co.com.almundo.model.Operator;
import co.com.almundo.model.Supervisor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


/**
 * Se hace necesario realizar el test de toda la clase Dispatcher, pues a consideración, ningún código deberías ser
 * liberado si no cuenta minimamente con una prueba unitaria que pase por cada parte del código.
 */
public class DispatcherTest
    extends TestCase
{

    public DispatcherTest(String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( DispatcherTest.class );
    }
    
    /**
     * Método de prueba que permite ejecutar 10 hilos de manera concurrente, y para todos ellos verificar la respuesta
     * y validar que efectivametne fue procesada de manera exitosa, de forma asíncrona
     */
    public void testMustAnswerAllCallsBecauseBeExecuteTenThreadsAndThereAreTenEmployees() throws InterruptedException {
        CountDownLatch lock = new CountDownLatch(10);
        Dispatcher disp = new Dispatcher(initOperators(), initSupervisors(), initDirectors());
        ExecutorService es = Executors.newFixedThreadPool(10);
        for(int i=0;i<10;i++){
            Future<Boolean> result = es.submit(() -> {
                Boolean res = disp.dispatchCall();
                if(res)
                    lock.countDown();
                return res;
            });
        }
        //Tiempo de espera máximo para que terminen todos los hilos.
        TimeUnit.SECONDS.sleep(20);
        es.shutdown();
        //Se verificar que el contador que se inicializó en 10, ahora sea 0 dado que fue exitosamente ejecutado.
        assertTrue( lock.getCount() == 0);
    }

    /**
     * Método de prueba que permite validar la respuesta cundo no hay disponibilidad de empleados.
     */
    public void testMustReturnFalseBecauseThereAreNotEmployeesToAnswerCall() throws InterruptedException {
        CountDownLatch lock = new CountDownLatch(1);
        Dispatcher disp = new Dispatcher(null,null, null);
        ExecutorService es = Executors.newFixedThreadPool(2);
        for(int i=0;i<2;i++){
            Future<Boolean> result = es.submit(() -> {
                Boolean res = disp.dispatchCall();
                if(res)
                    lock.countDown();
                return res;
            });
        }
        //Tiempo de espera máximo para que terminen todos los hilos.
        TimeUnit.SECONDS.sleep(10);
        es.shutdown();
        //Se verificar que el contador que se inicializó en 0, ahora sea 0 dado que no se respondió ninguna llamada.
        assertTrue( lock.getCount() == 1);
    }

    /**
     * Método de prueba que permite validar que se procesen llamadas cuando solo existan directores disponibles.
     */
    public void testMustReturnResponseByDirectorWhenThereAreNotOperatorsAndSupervisors() throws InterruptedException {
        CountDownLatch lock2 = new CountDownLatch(1);
        Dispatcher disp = new Dispatcher(null,null, initDirectors());
        ExecutorService es = Executors.newSingleThreadExecutor();
        for(int i=0;i<1;i++){
            Future<Boolean> result = es.submit(() -> {
                Boolean res = disp.dispatchCall();
                if(res)
                    lock2.countDown();
                return res;
            });
        }
        //Tiempo de espera máximo para que terminen todos los hilos.
        TimeUnit.SECONDS.sleep(12);
        es.shutdown();
        //Se verificar que el contador que se inicializó en 2, ahora sea 0 dado que habían solo directores disponibles.
        assertTrue( lock2.getCount() == 0);
    }

    /**
     * Método de prueba que permite validar que se procesen llamadas cuando solo existan directores Operadores.
     */
    public void testMustReturnResponseByOperatorWhenThereAreNotDirectorsAndSupervisors() throws InterruptedException {
        CountDownLatch lock = new CountDownLatch(2);
        Dispatcher disp = new Dispatcher(initOperators(),null,null  );
        ExecutorService es = Executors.newFixedThreadPool(2);
        for(int i=0;i<2;i++){
            Future<Boolean> result = es.submit(() -> {
                Boolean res = disp.dispatchCall();
                if(res)
                    lock.countDown();
                return res;
            });
        }
        //Tiempo de espera máximo para que terminen todos los hilos.
        TimeUnit.SECONDS.sleep(20);
        es.shutdown();
        //Se verificar que el contador que se inicializó en 2, ahora sea 0 dado que habían solo operadores disponibles.
        assertTrue( lock.getCount() == 0);
    }

    public void testMustReturnNullWhenEmployeeOperatorsAreNull(){
        Operator res = new Dispatcher(null, null, null ).getOperatorToAnswerCall();
        assertNull(res);
    }

    public void testMustReturnNullWhenEmployeeDirectorsAreNull(){
        Director res = new Dispatcher(null, null, null ).getDirectorToAnswerCall();
        assertNull(res);
    }

    public void testMustReturnNullWhenEmployeeSupervisorsAreNull(){
        Supervisor res = new Dispatcher(null, null, null ).getSupervisorToAnswerCall();
        assertNull(res);
    }

    public void testMustReturnNullWhenAllEmployeesOperatorAreBusy(){
        List<Operator> operators = new ArrayList<>();
        Operator operator1 = new Operator();
        operator1.setBusy(true);
        operators.add(operator1);
        Operator res = new Dispatcher(operators, null, null).getOperatorToAnswerCall();
        assertNull(res);
    }

    public void testMustReturnNullWhenAllEmployeesDirectorsAreBusy(){
        List<Director> directors = new ArrayList<>();
        Director director1 = new Director();
        director1.setBusy(true);
        directors.add(director1);
        Director res = new Dispatcher(null, null, directors).getDirectorToAnswerCall();
        assertNull(res);
    }

    public void testMustReturnNullWhenAllEmployeesSupervisorAreBusy(){
        List<Supervisor> supervisors = new ArrayList<>();
        Supervisor supervisor1 = new Supervisor();
        supervisor1.setBusy(true);
        supervisors.add(supervisor1);
        Supervisor res = new Dispatcher(null, supervisors, null).getSupervisorToAnswerCall();
        assertNull(res);
    }

    private List<Operator> initOperators(){
        List<Operator> operators = new ArrayList<>();
        Operator operator1 = new Operator();
        Operator operator2 = new Operator();
        Operator operator3 = new Operator();
        Operator operator4 = new Operator();
        Operator operator5 = new Operator();
        operators.add(operator1);
        operators.add(operator2);
        operators.add(operator3);
        operators.add(operator4);
        operators.add(operator5);
        return operators;
    }

    private List<Supervisor> initSupervisors(){
        List<Supervisor> supervisors = new ArrayList<>();
        Supervisor supervisor1 = new Supervisor();
        Supervisor supervisor2 = new Supervisor();
        Supervisor supervisor3 = new Supervisor();
        Supervisor supervisor4 = new Supervisor();
        Supervisor supervisor5 = new Supervisor();
        supervisors.add(supervisor1);
        supervisors.add(supervisor2);
        supervisors.add(supervisor3);
        supervisors.add(supervisor4);
        supervisors.add(supervisor5);
        return supervisors;
    }

    private List<Director> initDirectors(){
        List<Director> directors = new ArrayList<>();
        Director director1 = new Director();
        Director director2 = new Director();
        Director director3 = new Director();
        directors.add(director1);
        directors.add(director2);
        directors.add(director3);
        return directors;
    }
}
