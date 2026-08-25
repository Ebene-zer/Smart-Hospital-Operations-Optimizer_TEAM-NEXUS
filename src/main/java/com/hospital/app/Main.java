package com.hospital.app;

import com.hospital.benchmark.PerformanceLab;
import com.hospital.db.HospitalBootstrap;
import com.hospital.graph.TraceGenerator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Korle-Bu Smart Hospital Operations Optimizer");
        System.out.println(TeamParameters.summary());
        try {
            HospitalBootstrap.ensureReady();
            ConsoleMenu menu = new ConsoleMenu(new Scanner(System.in));
            if (hasFlag(args, "--demo")) {
                menu.reloadFromDatabase();
                menu.demoAll();
                return;
            }
            if (hasFlag(args, "--experiments")) {
                menu.reloadFromDatabase();
                System.out.println(PerformanceLab.runAll(true));
                return;
            }
            if (hasFlag(args, "--traces")) {
                TraceGenerator.main(new String[] {
                        Double.toString(TeamParameters.DIJKSTRA_PENALTY), "L046"
                });
                return;
            }
            menu.reloadFromDatabase();
            menu.loop();
        } catch (Exception ex) {
            System.out.println("Startup failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static boolean hasFlag(String[] args, String flag) {
        if (args == null) {
            return false;
        }
        for (String arg : args) {
            if (flag.equals(arg)) {
                return true;
            }
        }
        return false;
    }
}
