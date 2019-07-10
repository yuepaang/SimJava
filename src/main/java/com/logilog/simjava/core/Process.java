package com.logilog.simjava.core;

import com.logilog.simjava.action.ActionImpl;

import java.util.Iterator;


public class Process extends Event{

    public Event target;
    public Iterator<Event> generator;


    public Process(Environment environment, Iterable<Event> generator, int priority){
        super(environment);
        this.generator = generator.iterator();
        this.isOK = true;
        this.target = new Initialize(environment, this, priority);
    }

    public void Interrupt(Object cause, int priority) {
        if (isTriggered) throw new ArithmeticException("The process has terminated and cannot be interrupted.");
        if (environment.activeProcess == this) throw new ArithmeticException("A process is not allowed to interrupt itself.");

        Event interruptEvent = new Event(environment);
        interruptEvent.AddCallback(new ActionImpl<>(e -> Resume(e)));
        interruptEvent.Fail(cause, priority);

        if (this.target != null) this.target.RemoveCallback(new ActionImpl<>(e -> Resume(e)));
    }

    protected void Resume(Event event) {
        environment.activeProcess = this;
        while (true) {
            if (event.isOK) {
                if (generator.hasNext()) {
                    if (this.isTriggered) {
                        // the generator called e.g. Environment.ActiveProcess.Fail
                        environment.activeProcess = null;
                        return;
                    }
                    if (!ProceedToEvent()) {
                        event = target;
                        continue;
                    } else break;
                } else if (!this.isTriggered) {
                    Succeed(event.value, 0);
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
                isOK = false;
                value = event.value;

                if (generator.hasNext()) {
                    if (isTriggered) {
                        // the generator called e.g. Environment.ActiveProcess.Fail
                        environment.activeProcess = null;
                        return;
                    }
                    // if we move next, but IsOk is still false
                    if (!isOK) throw new ArithmeticException("The process did not react to being faulted.");
                    // otherwise HandleFault was called and the fault was handled
                    if (ProceedToEvent()) break;
                } else if (!isTriggered) {
                    if (!isOK) Fail(event.value, 0);
            else Succeed(event.value, 0);
                    break;
                } else break;
            }
        }
        environment.activeProcess = null;
    }

    protected boolean ProceedToEvent() {
        target = generator.next();
        value = target.value;
        if (target.isProcessed) return false;
        target.AddCallback(new ActionImpl<>(e -> Resume(e)));
        return true;
    }

    public boolean HandleFault() {
        if (isOK) return false;
        isOK = true;
        return true;
    }

    private class Initialize extends Event {
        public Initialize(Environment environment, Process process, int priority) {
            super(environment);
            super.AddCallback(new ActionImpl<>(e -> process.Resume(e)));
            isOK = true;
            isTriggered = true;
            environment.Schedule(this, priority);
        }
    }
}