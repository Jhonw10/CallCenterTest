package co.com.almundo.model;

public class Director extends Employee {

    @Override
    public void run() {
        this.setMessage("I am the Director, i will take your call.");
        this.setTimeOnCall();
        this.setBusy(false);
        System.out.println(this.getMessage());
    }
}
