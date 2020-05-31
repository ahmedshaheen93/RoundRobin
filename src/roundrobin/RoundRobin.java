/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roundrobin;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author lts
 */
public class RoundRobin extends Scheduler {

    private int quantum;

    private Queue<Process> readyQueue = new ArrayDeque<>();
    private List<Integer> arrivalTime = new ArrayList<Integer>();
    private List<Interruption> responseTimeList = new ArrayList();

    private List<Process> runningProcess = new ArrayList<>();

    private List<Interruption> interruptionList = new ArrayList();

    private void prepareList(List<Process> process, int returnAux) {
        int min = 0;

        for (Process p1 : process) {
            if (!arrivalTime.contains(p1.getArrivalTime()) && p1.getArrivalTime() <= returnAux) {
                if (!readyQueue.contains(p1)) {
                    min = p1.getArrivalTime();
                    arrivalTime.add(min);
                    for (Process p2 : process) {
                        if (p2.getArrivalTime() == min) {
                            readyQueue.add(p2);
                        }
                    }
                }
            }
        }
    }

    private int responseTimeTotal() {
        int sumResposta = 0;
        for (Interruption interruption : responseTimeList) {
            sumResposta += interruption.getTime();
        }
        return sumResposta;
    }

    public RoundRobin(List<Process> processes, int quatum) {
        this.quantum = quatum;
        copyProcesses(processes);
        int returnTime = 0;
        int waitTime = 0;
        int totalProcess = super.getAmountOfProcess(processes);
        // arrivalProcess is min arrival time
        // considring the idel time befor arriving
        int runningTime = arrivalMin(processes);

        prepareList(processes, runningTime);

        while (!readyQueue.isEmpty()) {
            Process p = readyQueue.poll();
            Interruption interruption = new Interruption();
            interruption.setProcess(p);
            interruption.setTime(runningTime);
            int responseToProcess = responseToProcess(interruption);
            if (responseToProcess >= 0) {
                p.setResponseTime(responseToProcess);
            }

            if (p.getRemainingDuration() > quantum) {
                p.setRemainingDuration(p.getRemainingDuration() - quantum);
                runningTime += quantum;
                interruption.setTime(runningTime);
                intrruptProcessor(interruption);
                prepareList(processes, runningTime);
                readyQueue.add(p);

            } else {
                runningTime += p.getRemainingDuration();
            }

            if (!readyQueue.contains(p)) {
                interruption.setTime(runningTime);
                intrruptProcessor(interruption);
                updateProcess(p, runningTime);
                returnTime += (runningTime - p.getArrivalTime());
                waitTime += (runningTime - p.getArrivalTime() - p.getBrustTime());

            }
        }

        super.setAvgReturn((float) returnTime / totalProcess);
        super.setAvgResponse((float) responseTimeTotal() / totalProcess);
        super.setAvgWait((float) waitTime / totalProcess);
    }

    public void print() {
        super.print("RR");
    }

    public List<Process> getRunningProcess() {
        return runningProcess;
    }

    private void copyProcesses(List<Process> processes) {
        processes.forEach(process -> runningProcess.add(process));
    }

    private void updateProcess(Process p, int runningTime) {

        p.setTurnarroundTime(runningTime - p.getArrivalTime());
        p.setWaitingTime(runningTime - p.getArrivalTime() - p.getBrustTime());
        p.setCompletionTime(runningTime);

        int indexOfUpdatedProcess = -1;
        for (int i = 0; i < runningProcess.size(); i++) {
            if (runningProcess.get(i).getId() == p.getId()) {
                indexOfUpdatedProcess = i;
            }
        }
        if (indexOfUpdatedProcess >= 0) {
            runningProcess.set(indexOfUpdatedProcess, p);
        }
    }

    public List<Interruption> getInterruptionList() {
        return interruptionList;
    }

    private void intrruptProcessor(Interruption interruption) {
        System.out.println(interruption);
        interruptionList.add(interruption);
    }

    private int responseToProcess(Interruption interruption) {
        // calc responseTime 
        // if !existing add on map
        int responseingTime = -1;

        System.out.println(interruption.getProcess().getId());
        System.out.println(interruption.hashCode());
        if (!responseTimeList.contains(interruption)) {
            Process process = interruption.getProcess();
            responseingTime = interruption.getTime() - process.getArrivalTime();
            System.out.println("responseingTime" + responseingTime);
            responseTimeList.add(interruption);
        }
        return responseingTime;
    }

}
