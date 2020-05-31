package roundrobin.model;

public class Process implements Comparable<Process> {

    private int id;
    private final int arrivalTime;
    private int brustTime;
    private int remainingDuration;
    private int completionTime;
    private int turnarroundTime;
    private int waitingTime;
    private int responseTime;
    
    public Process(int id, int arrivalTime, int brustTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.brustTime = brustTime;
        this.remainingDuration = brustTime;
    }

    public int compareTo(Process process) {
        if ((this.arrivalTime < process.getArrivalTime() || this.arrivalTime == process.getArrivalTime())
                && (this.getBrustTime()< process.getBrustTime())) {
            return -1;
        } else if ((this.arrivalTime > process.getArrivalTime() || (this.arrivalTime == process.getArrivalTime())
                && this.getBrustTime() > process.getBrustTime())) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

//    public int getDuration() {
//        return brustTime;
//    }

    public int getRemainingDuration() {
        return remainingDuration;
    }

    public void setRemainingDuration(int remainingDuration) {
        this.remainingDuration = remainingDuration;
    }

    public int getBrustTime() {
        return brustTime;
    }

    public void setBrustTime(int brustTime) {
        this.brustTime = brustTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getTurnarroundTime() {
        return turnarroundTime;
    }

    public void setTurnarroundTime(int turnarroundTime) {
        this.turnarroundTime = turnarroundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }
    
}
