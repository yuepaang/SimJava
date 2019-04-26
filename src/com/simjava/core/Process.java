package com.simjava.core;

import com.simjava.action.ActionImpl;
import com.simjava.yield.*;


public class Process extends Event{

    private Event target;
    private ClosableIterator<Event> generator;

    public ClosableIterator<Event> getGenerator() {
        return generator;
    }

    public void setGenerator(ClosableIterator<Event> generator) {
        this.generator = generator;
    }

    public Event getTarget() {
        return target;
    }

    protected void setTarget(Event target) {
        this.target = target;
    }

    public Process(Environment environment, Yielderable<Event> generator, int priority){
        super(environment);
        this.generator = generator.iterator();
        setOK(true);
        this.target = new Initialize(environment, this, priority);
    }

    public void Interrupt(String cause, int priority) {
        if (isTrigger()) throw new ArithmeticException("The process has terminated and cannot be interrupted.");
        if (super.getEnvironment().getActiveProcess() == this) throw new ArithmeticException("A process is not allowed to interrupt itself.");

        Event interruptEvent = new Event(super.getEnvironment());
        interruptEvent.AddCallback(new ActionImpl<Event>(e -> Resume(e)));
        interruptEvent.Fail(cause, priority);

        if (this.target != null) this.target.RemoveCallback(new ActionImpl<Event>(e -> Resume(e)));
    }

    protected void Resume(Event event) {
        super.getEnvironment().setActiveProcess(this);
        while (true) {
            if (event.isOK()) {
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

    private class Initialize extends Event {
        public Initialize(Environment environment, Process process, int priority) {
            super(environment);
            super.AddCallback(new ActionImpl<Event>(e -> process.Resume(e)));
            setOK(true);
            setTrigger(true);
            environment.Schedule(this, priority);
        }
    }
}