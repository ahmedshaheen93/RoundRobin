/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roundrobin.model;

import java.util.Objects;

/**
 *
 * @author lts
 */
public class Interruption {

    private Process process;
    private int time;

    public Interruption() {
    }

    public Interruption(Process process, int time) {
        this.process = process;
        this.time = time;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.process);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Interruption other = (Interruption) obj;
        if (!Objects.equals(this.process, other.process)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Interruption{" + "process=" + process.getId() + ", time=" + time + '}';
    }
    
}
