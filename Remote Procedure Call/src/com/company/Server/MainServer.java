package com.company.Server;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class MainServer {
    private static SchoolCoordinator schoolCoordinator;
    private static int numberOfStudents = 20;
    public MainServer() {
        Principle principle = new Principle();
        schoolCoordinator = new SchoolCoordinator(principle);
        schoolCoordinator.start();

//        for (int i = 0; i < numberOfStudents; i++) {
//            Student student = new Student(i);
//            student.start();
//        }

    }

    public void run() {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int n = 0;
        for (;;) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                n++;
            } catch (IOException e) {
                e.printStackTrace();
            }

            new Thread(new ServerThread(clientSocket, schoolCoordinator)).start();
        }
    }

    public static void main (String [] args) {
        MainServer server = new MainServer();
        server.run();
    }
}
