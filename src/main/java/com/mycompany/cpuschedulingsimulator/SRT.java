package com.mycompany.cpuschedulingsimulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SRT {
    private ArrayList<PCB> processes;
    private int contextSwitchTime;

    public SRT(ArrayList<PCB> processes, int contextSwitchTime) {
        this.processes = new ArrayList<>(processes);
        this.contextSwitchTime = contextSwitchTime;
    }

    private void sortProcessesByArrival() {
        processes.sort(Comparator.comparingInt(PCB::getArrivalTime));
    }

    public void execute() {
        sortProcessesByArrival();
        PriorityQueue<PCB> readyQueue = new PriorityQueue<>(Comparator.comparingInt(PCB::getRemainingTime));
        int currentTime = 0;
        int processIndex = 0;
        PCB currentProcess = null;
        int totalIdleTime = 0;
        int totalContextSwitchTime = 0;

        System.out.println("Gantt Chart:");
        System.out.print(currentTime);
        while (!readyQueue.isEmpty() || processIndex < processes.size() || currentProcess != null) {
            while (processIndex < processes.size() && processes.get(processIndex).getArrivalTime() <= currentTime) {
                readyQueue.offer(processes.get(processIndex));
                processIndex++;
            }
            if (currentProcess == null || (!readyQueue.isEmpty() && readyQueue.peek().getRemainingTime() < currentProcess.getRemainingTime())) {
                if (currentProcess != null && currentProcess.getRemainingTime() > 0) {
                    readyQueue.offer(currentProcess);
                }
                if (currentProcess != null) {
                    System.out.print(currentTime + ")");
                    if (!readyQueue.isEmpty()) {
                        System.out.print(" -> CS (" + currentTime + " - ");
                        currentTime += contextSwitchTime;
                        totalContextSwitchTime += contextSwitchTime;
                        System.out.print(currentTime + ")");
                    }
                }
                if (!readyQueue.isEmpty()) {
                    currentProcess = readyQueue.poll();
                    System.out.print(" -> P" + currentProcess.getProcessID() + " (" + currentTime + " - ");
                } else {
                    currentProcess = null;
                }
            }

            if (currentProcess != null) {
                currentTime++;
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                if (currentProcess.getRemainingTime() == 0) {
                    System.out.print(currentTime + ")");
                    currentProcess.setCompletionTime(currentTime);
                    currentProcess = null;
                    if (!readyQueue.isEmpty() && processIndex < processes.size()) {
                        System.out.print(" -> CS (" + currentTime + " - ");
                        currentTime += contextSwitchTime;
                        totalContextSwitchTime += contextSwitchTime;
                        System.out.print(currentTime + ")");
                    }
                }
            } else {
                if (processIndex < processes.size()) {
                    int idleStartTime = currentTime;
                    currentTime = processes.get(processIndex).getArrivalTime();
                    totalIdleTime += currentTime - idleStartTime;
                    System.out.print(" -> Idle (" + idleStartTime + " - " + currentTime + ")");
                }
            }
        }

        System.out.println();
        printMetrics(currentTime, totalIdleTime, totalContextSwitchTime);
    }

    private void printMetrics(int lastEndTime, int totalIdleTime, int totalContextSwitchTime) {
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;
        for (PCB process : processes) {
            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
            process.setWaitingTime(process.getTurnaroundTime() - process.getCpuBurst());
            totalTurnaroundTime += process.getTurnaroundTime();
            totalWaitingTime += process.getWaitingTime();
        }

        System.out.println("ID | Arrival Time | Burst Time | Completion Time | Turnaround Time | Waiting Time");
        for (PCB process : processes) {
            System.out.format("%2d | %12d | %10d | %15d | %15d | %12d\n",
            process.getProcessID(), process.getArrivalTime(), process.getCpuBurst(),
            process.getCompletionTime(), process.getTurnaroundTime(), process.getWaitingTime());
        }

        double avgTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        double avgWaitingTime = (double) totalWaitingTime / processes.size();
        double throughput = (double) processes.size() / lastEndTime;
        double cpuUtilization = (double) (lastEndTime - totalIdleTime - totalContextSwitchTime) / lastEndTime * 100;

        System.out.println("Average turnaround time: " + avgTurnaroundTime);
        System.out.println("Average waiting time: " + avgWaitingTime);
        System.out.println("Throughput: " + throughput);
        System.out.println("CPU Utilization: " + cpuUtilization + "%");
    }
}
