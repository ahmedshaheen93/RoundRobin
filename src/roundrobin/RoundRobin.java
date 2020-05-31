/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roundrobin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lts
 */
public class RoundRobin extends Scheduler {

    private static int quantum;
    private static List<Process> listReady = new ArrayList<Process>();
    private static List<Integer> arrivalTime = new ArrayList<Integer>();
    private static Map<Integer, Integer> responseTime = new HashMap<Integer, Integer>();

    private List<Process> runningProcess = new ArrayList<>();

    private List<Map<Process, Integer>> running = new ArrayList();

    private void prepareList(List<Process> process, int returnAux) {
        int min = 0;

        for (Process p1 : process) {
            if (!arrivalTime.contains(p1.getArrivalTime()) && p1.getArrivalTime() <= returnAux) {
                if (!listReady.contains(p1)) {
                    min = p1.getArrivalTime();
                    arrivalTime.add(min);
                    for (Process p2 : process) {
                        if (p2.getArrivalTime() == min) {
                            listReady.add(p2);
                        }
                    }
                }
            }
        }
    }

    private int responseTimeTotal() {
        int sumResposta = 0;
        for (int key : responseTime.keySet()) {
            sumResposta += responseTime.get(key);
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
        int arrivalProcess = arrivalMin(processes);

        prepareList(processes, arrivalProcess);

        // enquanto houver processos na lista de prontos
        while (!listReady.isEmpty()) {
            Process p = listReady.remove(0);

            // calc responseTime 
            // if !existing add on map
            if (!responseTime.containsKey(p.getId())) {
                int responseingTime = arrivalProcess - p.getArrivalTime();
                p.setResponseTime(responseingTime);

//                Map<Process, Integer> runningpr = new HashMap<>();
//                runningpr.put(p, responseingTime);
//                running.add(runningpr);
                responseTime.put(p.getId(), responseingTime);
            }

            if (p.getRemainingDuration() > quantum) {
                p.setRemainingDuration(p.getRemainingDuration() - quantum);
                arrivalProcess += quantum;
                prepareList(processes, arrivalProcess);
                Map<Process, Integer> runningpr = new HashMap<>();
                runningpr.put(p, arrivalProcess);
                running.add(runningpr);
                listReady.add(p);

            } else {
                arrivalProcess += p.getRemainingDuration();
//                Map<Process, Integer> runningpr = new HashMap<>();
//                runningpr.put(p, arrivalProcess);
//                running.add(runningpr);

            }

            if (!listReady.contains(p)) {

                p.setTurnarroundTime(arrivalProcess - p.getArrivalTime());
                p.setWaitingTime(arrivalProcess - p.getArrivalTime() - p.getBrustTime());
                p.setCompletionTime(arrivalProcess);
                updateProcess(p);
                Map<Process, Integer> runningpr = new HashMap<>();
                runningpr.put(p, arrivalProcess);
                running.add(runningpr);
                returnTime += (arrivalProcess - p.getArrivalTime());
                waitTime += (arrivalProcess - p.getArrivalTime() - p.getBrustTime());

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

    private void updateProcess(Process p) {
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

    public List<Map<Process, Integer>> getRunning() {
        return running;
    }

}
