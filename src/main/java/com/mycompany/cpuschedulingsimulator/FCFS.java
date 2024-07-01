package com.mycompany.cpuschedulingsimulator;

import java.util.ArrayList;

public class FCFS {
    private ArrayList<PCB> processes;
    private int contextSwitchTime;

    public FCFS(ArrayList<PCB> processes, int contextSwitchTime) {
        this.processes = new ArrayList<>(processes);
        this.contextSwitchTime = contextSwitchTime;
    }

    private void sortProcesses() {
        processes.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
    }

    public void execute() {
        sortProcesses();
        printGanttChart();
        printMetrics();
    }

    private void printGanttChart() {
        int currentTime = 0;
        System.out.println("Gantt Chart:");
        System.out.print(currentTime);

        for (int i = 0; i < processes.size(); i++) {
            PCB process = processes.get(i);
            if (currentTime < process.getArrivalTime()) {
                System.out.print(" -> Idle (" + currentTime + " - " + process.getArrivalTime() + ")");
                currentTime = process.getArrivalTime();
            }

            System.out.print(" -> P" + process.getProcessID() + " (" + currentTime + " - ");
            currentTime += process.getCpuBurst();
            System.out.print(currentTime + ")");

            if (contextSwitchTime > 0 && i < processes.size() - 1) {
                PCB nextProcess = processes.get(i + 1);
                if (currentTime < nextProcess.getArrivalTime()) {
                    // No context switch needed if we are going to idle
                    continue;
                }
                System.out.print(" -> CS (" + currentTime + " - ");
                currentTime += contextSwitchTime;
                System.out.print(currentTime + ")");
            }
        }
        System.out.println();
    }

    private void printMetrics() {
        int currentTime = 0;
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;
        int totalIdleTime = 0;
        int lastEndTime = 0;
        int totalContextSwitchTime = 0;
    
        for (int i = 0; i < processes.size(); i++) {
            PCB process = processes.get(i);
            if (currentTime < process.getArrivalTime()) {
                totalIdleTime += process.getArrivalTime() - currentTime;
                currentTime = process.getArrivalTime();
            }
            currentTime += process.getCpuBurst();
            process.setCompletionTime(currentTime);
            process.setTurnaroundTime(currentTime - process.getArrivalTime());
            process.setWaitingTime(process.getTurnaroundTime() - process.getCpuBurst());
            totalTurnaroundTime += process.getTurnaroundTime();
            totalWaitingTime += process.getWaitingTime();
            lastEndTime = currentTime;
    
            if (contextSwitchTime > 0 && i < processes.size() - 1) {
                PCB nextProcess = processes.get(i + 1);
                if (currentTime >= nextProcess.getArrivalTime()) {
                    totalContextSwitchTime += contextSwitchTime;
                    currentTime += contextSwitchTime;
                }
            }
        }
    
        double cpuUtilization = ((double) (lastEndTime - totalIdleTime - totalContextSwitchTime) / lastEndTime) * 100;
        double avgTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        double avgWaitingTime = (double) totalWaitingTime / processes.size();
        double throughput = (double) processes.size() / lastEndTime;
        System.out.println("ID | Arrival Time | Burst Time | Completion Time | Turnaround Time | Waiting Time");
    
        processes.forEach(p -> System.out.format("%2d | %12d | %10d | %15d | %15d | %12d\n",
        p.getProcessID(), p.getArrivalTime(), p.getCpuBurst(),
        p.getCompletionTime(), p.getTurnaroundTime(), p.getWaitingTime()));
    
        System.out.println("Average turnaround time: " + avgTurnaroundTime);
        System.out.println("Average waiting time: " + avgWaitingTime);
        System.out.printf("Throughput: %.2f processes/unit time\n", throughput);
        System.out.printf("CPU Utilization: %.2f%%\n", cpuUtilization);
    }
    
}
