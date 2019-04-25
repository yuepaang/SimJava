package com.simjava.core;

import java.util.ArrayList;
import java.util.Iterator;

public class Process extends Event{

    private Event target;
    private Iterator<Event> generator;

    public Process(Environment environment, Iterable<Event> generator, int priority){
        this.generator = generator.iterator();
        setOK(true);
        target = new Initialize(environment, this, priority);
    }

    private class Initialize extends Event {
      public Initialize(Environment environment, Process process, int priority) {
            setOK(true);
            setTrigger(true);
            environment.Schedule(this, priority, 0);
        }
    }

    public void Interrupt(String cause, int priority) {
        if (isTrigger()) throw new ArithmeticException("The process has terminated and cannot be interrupted.");
        if (this.getEnvironment().getActiveProcess() == this) throw new ArithmeticException("A process is not allowed to interrupt itself.");

        var interruptEvent = new Event(this.getEnvironment());
        interruptEvent.Fail(cause, priority);
    }

    protected void Resume(Event event) {
        this.getEnvironment().setActiveProcess(this);
        while (true) {
            if (event.isOK()) {
                if (generator.hasNext()) {
                    if (this.isTrigger()) {
                        // the generator called e.g. Environment.ActiveProcess.Fail
                        this.getEnvironment().setActiveProcess(null);
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
        this.getEnvironment().setActiveProcess(null);
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