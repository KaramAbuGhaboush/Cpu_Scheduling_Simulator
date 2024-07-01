package com.mycompany.cpuschedulingsimulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RR {
    private ArrayList<PCB> processes;
    private int contextSwitchTime;
    private int quantum;

    public RR(ArrayList<PCB> processes, int contextSwitchTime, int quantum) {
        this.processes = new ArrayList<>(processes);
        this.contextSwitchTime = contextSwitchTime;
        this.quantum = quantum;
    }

    public void execute() {
        Queue<PCB> queue = new LinkedList<>();
        int currentTime = 0;
        int processIndex = 0;
        int totalIdleTime = 0;
        int totalContextSwitchTime = 0;

        processes.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));

        System.out.println("Gantt Chart:");
        System.out.print(currentTime);

        while (!queue.isEmpty() || processIndex < processes.size()) {
            while (processIndex < processes.size() && processes.get(processIndex).getArrivalTime() <= currentTime) {
                queue.add(processes.get(processIndex));
                processIndex++;
            }

            if (!queue.isEmpty()) {
                PCB process = queue.poll();

                int execTime = Math.min(process.getRemainingTime(), quantum);
                process.setRemainingTime(process.getRemainingTime() - execTime);

                System.out.print(" -> P" + process.getProcessID() + " (" + currentTime + " - " + (currentTime + execTime) + ")");
                currentTime += execTime;

                if (process.getRemainingTime() > 0) {
                    queue.add(process);

                    if (!queue.isEmpty() || processIndex < processes.size()) {
                        totalContextSwitchTime += contextSwitchTime;
                        currentTime += contextSwitchTime;  
                        System.out.print(" -> CS (" + (currentTime - contextSwitchTime) + " - " + currentTime + ")");
                    }
                } else {
                    process.setCompletionTime(currentTime);
                }
            } else {
                System.out.print(" -> Idle (" + currentTime + " - ");
                int idleStartTime = currentTime;
                currentTime = processes.get(processIndex).getArrivalTime();
                totalIdleTime += currentTime - idleStartTime;
                System.out.print(currentTime + ")");
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
