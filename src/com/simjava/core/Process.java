package com.simjava.core;

import com.simjava.action.ActionImpl;
import com.simjava.yield.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class Process extends Event{

    private Environment environment;
    private List<Function> callBackList;
    private Object value;

    private Event target;
    private ClosableIterator<Event> generator;

    public Event getTarget() {
        return target;
    }

    public Object getValue() {
        return value;
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    public boolean IsAlive(){
        return this.value.equals(PENGDING);
    }

    public Process(Environment environment, Yielderable<Event> generator){
        this.environment = environment;
        this.callBackList = new ArrayList<>();
        this.value = PENGDING;

        this.generator = generator.iterator();
        this.target = new Initialize(environment, this);
    }

    public void Interrupt(String cause) {
        new Interruption(this, cause);
    }

    protected void Resume(Event event) {
        this.environment.setActiveProcess(this);
        while (true) {
            if (event.OK()) {
                if (generator.hasNext()) {
                    if (this.isTrigger()) {
                        // the generator called e.g. Environment.ActiveProcess.Fail
                        super.getEnvironment().setActiveProcess(null);
                        return;
                    }
                    if (!ProceedToEvent()) {
                        event = target;
                        continue;
                    } else break;
                } else if (!this.isTrigger()) {
                    Succeed(event.getValue(), 0);
                    break;
                } else break;
            } else {
                /* Fault handling differs from SimPy as in .NET it is not possible to inject an
                 * exception into an enumerator and it is impossible to put a yield return inside
                 * a try-catch block. In SimSharp the Process will set IsOk and will then move to
                 * the next yield in the generator. However, if after this move IsOk is still false
                 * we know that the error was not handled. It is assumed the error is handled if
                 * HandleFault() is called on the environment's ActiveProcess which will reset the
                 * flag. */
                setOK(false);
                setValue(event.getValue());

                if (generator.hasNext()) {
                    if (isTrigger()) {
                        // the generator called e.g. Environment.ActiveProcess.Fail
                        this.getEnvironment().setActiveProcess(null);
                        return;
                    }
                    // if we move next, but IsOk is still false
                    if (!isOK()) throw new ArithmeticException("The process did not react to being faulted.");
                    // otherwise HandleFault was called and the fault was handled
                    if (ProceedToEvent()) break;
                } else if (!isTrigger()) {
                    if (!isOK()) Fail(event.getValue(), 0);
            else Succeed(event.getValue(), 0);
                    break;
                } else break;
            }
        }
        super.getEnvironment().setActiveProcess(null);
    }

    protected boolean ProceedToEvent() {
        target = generator.next();
        this.setValue(target.getValue());
        if (target.isProcessed()) return false;
        return true;
    }

    public boolean HandleFault() {
        if (isOK()) return false;
        setOK(true);
        return true;
    }


}