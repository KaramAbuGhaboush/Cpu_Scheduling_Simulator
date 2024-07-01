/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.cpuschedulingsimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author macbook
 */
public class CPUSchedulingSimulator {

    public static void main(String[] args) {
        ArrayList<PCB> processes = new ArrayList<>();
        ArrayList<Integer> arrivalTimes = new ArrayList<>();
        ArrayList<Integer> cpuBursts = new ArrayList<>();
        Integer contextSwitchTime = 0;
        Integer quantum = 0;
        String filePath = "Input.txt";
        displayHeader();
        
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.startsWith("Arrival times:")) {
                    String[] arrivalTimesStr = line.split(":")[1].trim().split(" ");
                    for (String arrivalTimeStr : arrivalTimesStr) {
                        arrivalTimes.add(Integer.parseInt(arrivalTimeStr));
                    }
            } else if (line.startsWith("CPU bursts:")) {
                    String[] cpuBurstsStr = line.split(":")[1].trim().split(" ");
                    for (String cpuBurstStr : cpuBurstsStr) {
                    cpuBursts.add(Integer.parseInt(cpuBurstStr));
                    }
            } else if (line.startsWith("Context Switch Time:")) {
                    contextSwitchTime = Integer.parseInt(line.split(":")[1].trim());
            } else if (line.startsWith("Quantum:")) {
                    quantum = Integer.parseInt(line.split(":")[1].trim());
            }
            
        }
        scanner.close();
    } catch (FileNotFoundException e) {
        System.out.println("File not found!");
        e.printStackTrace();
    } catch (Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }

    for (int i = 0; i < arrivalTimes.size(); i++) {
        processes.add(new PCB(i + 1, arrivalTimes.get(i), cpuBursts.get(i)));
    }

    //Made By: Karam Abu Ghaboush 21126.

    Scanner scanner = new Scanner(System.in);
    int choice = 0;
    do {
        System.out.println("Enter your choice: ");
        System.out.println("1. FCFS");
        System.out.println("2. SRT");
        System.out.println("3. RR");
        System.out.println("4. Exit");
        System.out.println("Choice: ");

        choice = scanner.nextInt();
        switch (choice) {
            case 1:
                FCFS fcfs = new FCFS(processes, contextSwitchTime);
                fcfs.execute();
                break;
            case 2:
                SRT srt = new SRT(processes, contextSwitchTime);
                srt.execute();
                break;
            case 3:
                RR rr = new RR(processes, contextSwitchTime, quantum);
                rr.execute();
                break;
            case 4:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    } while (choice != 4);
    scanner.close();
}

    // Made By: Karam Abu Ghaboush 21126

    public static void displayHeader() {
        System.out.println("| ---------------------------------------- |");
        System.out.println("| ><><><> CPU Scheduling Simulator <><><>< |");
        System.out.println("| ><><><>   By:Karam Abu Ghaboush  <><><>< |");
        System.out.println("| ---------------------------------------- |");
    }

}