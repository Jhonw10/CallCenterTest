package co.com.almundo.model;

public class Operator extends Employee {

    @Override
    public void run() {
        this.setMessage("I am the Operator, i will take your call.");
        this.setTimeOnCall();
        this.setBusy(false);
        System.out.println(this.getMessage());
    }
}
