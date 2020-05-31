/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roundrobin.algorithm;

import roundrobin.model.Process;
import roundrobin.model.Interruption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author lts
 */
public class RoundRobin {

    private int quantum;
    //ready queue
    private Queue<Process> readyQueue = new ArrayDeque<>();
    // arrivale time
    private List<Integer> arrivalTime = new ArrayList<Integer>();
    // response time
    private List<Interruption> responseTimeList = new ArrayList();
    // rurring process
    private List<Process> runningProcess = new ArrayList<>();
    // interruption
    private List<Interruption> interruptionList = new ArrayList();

    private double avgTurnAround;
    private double avgResponse;
    private double avgWait;

    public RoundRobin(List<Process> processes, int quatum) {
        this.quantum = quatum;
        copyProcesses(processes);
        int trunArround = 0;
        int waitTime = 0;
        int totalProcess = processes.size();
        // start with which process
        int runningTime = arrivalMin(processes);
        // put prosess on ready queue
        scheduling(processes, runningTime);
        //
        while (!readyQueue.isEmpty()) {
            Process p = readyQueue.poll();

            Interruption interruption = new Interruption();
            interruption.setProcess(p);
            interruption.setTime(runningTime);
            // 0 
            int responseToProcess = responseToProcess(interruption);
            if (responseToProcess >= 0) {
                // record response time 0
                p.setResponseTime(responseToProcess);
            }
            // 5 > 2
            if (p.getRemainingDuration() > quantum) {
                // 5 -2 
                p.setRemainingDuration(p.getRemainingDuration() - quantum);
                // +2 
                runningTime += quantum;
                // 2
                interruption.setTime(runningTime);
                intrruptProcessor(interruption);

                //
                scheduling(processes, runningTime);
                // add to ready queue
                readyQueue.add(p);

            } // 1 < 2
            else {
                runningTime += p.getRemainingDuration();
            }

            if (!readyQueue.contains(p)) {
                interruption.setTime(runningTime);
                intrruptProcessor(interruption);
                updateProcess(p, runningTime);
                trunArround += (runningTime - p.getArrivalTime());
                waitTime += (trunArround - p.getBrustTime());

            }
        }

        setAvgTurnAround((float) trunArround / totalProcess);
        setAvgResponse((float) responseTimeTotal() / totalProcess);
        setAvgWait((float) waitTime / totalProcess);
    }

    /**
     * check if this process is already on readyQueue or not else add to ready
     * queue
     *
     * @param processes
     * @param runningTime
     */
    private void scheduling(List<Process> processes, int runningTime) {
        int min = 0;

        for (Process p1 : processes) {
            if (!arrivalTime.contains(p1.getArrivalTime()) && p1.getArrivalTime() <= runningTime) {
                if (!readyQueue.contains(p1)) {
                    min = p1.getArrivalTime();
                    arrivalTime.add(min);
                    for (Process p2 : processes) {
                        if (p2.getArrivalTime() == min) {
                            readyQueue.add(p2);
                        }
                    }
                }
            }
        }
    }

    /**
     * calc sum responseTime for all process
     *
     * @return
     */
    private int responseTimeTotal() {
        int sumResposta = 0;
        for (Interruption interruption : responseTimeList) {
            sumResposta += interruption.getTime();
        }
        return sumResposta;
    }

    /**
     * update complated process data , complationTime , turnarroundTime
     * ,waitingTime
     *
     * @param p
     * @param complationTime
     */
    private void updateProcess(Process p, int complationTime) {

        int turnarroundTime = complationTime - p.getArrivalTime();
        int waitingTime = turnarroundTime - p.getBrustTime();

        p.setTurnarroundTime(turnarroundTime);
        p.setWaitingTime(waitingTime);
        p.setCompletionTime(complationTime);

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

    /**
     * return interruptionList
     *
     * @return
     */
    public List<Interruption> getInterruptionList() {
        return interruptionList;
    }

    /**
     * store on interruptionList
     *
     * @param interruption
     */
    private void intrruptProcessor(Interruption interruption) {
        interruptionList.add(interruption);
    }

    /**
     * calc response time for every process and record response time on
     * responseTimeList
     *
     * @param interruption
     * @return min time for next running process
     */
    private int responseToProcess(Interruption interruption) {

        int responseingTime = -1;

        if (!responseTimeList.contains(interruption)) {
            Process process = interruption.getProcess();
            responseingTime = interruption.getTime() - process.getArrivalTime();
            System.out.println("responseingTime" + responseingTime);
            responseTimeList.add(interruption);
        }
        return responseingTime;
    }

    /**
     * get minimum arrival time
     *
     * @param process
     * @return
     */
    public int arrivalMin(List<Process> process) {
        int min = process.get(0).getArrivalTime();
        for (Process p : process) {
            if (p.getArrivalTime() < min) {
                min = p.getArrivalTime();
            }
        }
        return min;
    }

    // pring data
    public void print() {
        System.out.println(String.format("%.1f %.1f %.1f", getAvgTurnAround(),
                getAvgResponse(), getAvgWait()));
    }

    public List<Process> getRunningProcess() {
        return runningProcess;
    }

    /**
     * copy processes to runningProcess
     *
     * @param processes
     */
    private void copyProcesses(List<Process> processes) {
        processes.forEach(process -> runningProcess.add(process));
    }

    public double getAvgTurnAround() {
        return avgTurnAround;
    }

    public void setAvgTurnAround(double avgTurnAround) {
        this.avgTurnAround = avgTurnAround;
    }

    public double getAvgResponse() {
        return avgResponse;
    }

    public void setAvgResponse(double avgResponse) {
        this.avgResponse = avgResponse;
    }

    public double getAvgWait() {
        return avgWait;
    }

    public void setAvgWait(double avgWait) {
        this.avgWait = avgWait;
    }

}
