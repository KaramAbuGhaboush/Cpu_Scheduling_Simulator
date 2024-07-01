package com.mycompany.cpuschedulingsimulator;

public class PCB {
    private int processID;
    private int arrivalTime;
    private int cpuBurst;
    private int remainingTime;
    private int completionTime;
    private int waitingTime;
    private int turnaroundTime;

    public PCB(int processID, int arrivalTime, int cpuBurst) {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.cpuBurst = cpuBurst;
        this.remainingTime = cpuBurst;
        this.completionTime = 0;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
    }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getCpuBurst() {
        return cpuBurst;
    }

    public void setCpuBurst(int cpuBurst) {
        this.cpuBurst = cpuBurst;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    @Override
    public String toString() {
        return "Process{" + "processID=" + processID + ", arrivalTime=" + arrivalTime + ", cpuBurst=" + cpuBurst + ", remainingTime=" + remainingTime + ", completionTime=" + completionTime + ", waitingTime=" + waitingTime + ", turnaroundTime=" + turnaroundTime + '}';
    }
}
