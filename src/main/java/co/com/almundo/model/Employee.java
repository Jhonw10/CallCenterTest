package co.com.almundo.model;

import java.util.Random;

public class Employee implements Runnable{

    private int idEmployee;
    private boolean busy;
    private String message;

    public Boolean getBusy() {
        return busy;
    }

    public void setBusy(Boolean busy) {
        this.busy = busy;
    }

    public void setTimeOnCall(){
        try {
            Thread.sleep((long)(new Random().nextInt(6) + 5)*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void run() {

    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }
}
