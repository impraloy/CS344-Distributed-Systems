package com.company.Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class StudentClient extends Thread {

    public static int studentsLetIn = 0;
    public static int sessionsAttended = 0;
    public static boolean studentsWaitingToBeTestedId_contains = false;
    public static boolean sessionInProgress = false;
    public static int studentsAttendingForCurrentSession = 0;
    public static int Ela_currentClassCapacity;
    public static int Math_currentClassCapacity;
    public static ArrayList<Integer> attendingClassId = new ArrayList<>();
    public static int studentsInClassRoomsIdsize = 0;
    public static boolean hasBeenNotifiedToStartClass = false;

    private static final int numStudents = 3;
    private static final int portNumber = 5000;
    private static final String address = "localhost";

    private Socket clientSocket;
    private int id;
    private  BufferedReader in;
    private  BufferedWriter out;

    private int command = 0;
    long param = 0;
    public boolean sender = false;
    public void sendCommand(int command, long param) {
        this.command = command;
        this.param = param;
        sender = true;
    }

    public StudentClient(int number) {
        this.id = number;
    }

    public void run ()
    {
        try {
            try {
                clientSocket = new Socket(address, portNumber);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                while(true) {
                    Thread.sleep(900);
                    if(sender) {
                        out.write(command + " " + param + "\n");
                        out.flush();

                        String serverWord = in.readLine();
                        switch (command) {
                            case 0://arrived
                                studentsLetIn = Integer.valueOf(serverWord);
                                break;
                            case 1://studentsWaitingToBeTested
                                studentsWaitingToBeTestedId_contains = serverWord.equals("1");
                                break;
                            case 2://arrivalTime
                                break;
                            case 3:
                                sessionsAttended = Integer.valueOf(serverWord);
                                break;
                            case 4:
                                sessionInProgress = Integer.valueOf(serverWord) == 1;
                                ///System.out.println(id+" "+sessionInProgress);
                                break;
                            case 5://studentsAttendingForCurrentSession
                                studentsAttendingForCurrentSession = Integer.valueOf(serverWord);
                                break;
                            case 6://currentClassCapacity - all
                                String[] cap = serverWord.split(" ");
                                Ela_currentClassCapacity = Integer.valueOf(cap[0]);
                                Math_currentClassCapacity = Integer.valueOf(cap[1]);
                                break;
                            case 7://attendingClassId
                                if(!attendingClassId.contains(id)) attendingClassId.add(id);
                                String[] s7 = serverWord.split(" ");
                                studentsAttendingForCurrentSession = Integer.valueOf(s7[0]);
                                studentsInClassRoomsIdsize = Integer.valueOf(s7[1]);
                                hasBeenNotifiedToStartClass = s7[2].equals("1");
                                break;
                            case 8://5?
                                studentsAttendingForCurrentSession = Integer.valueOf(serverWord);
                                break;
                        }
                        sender = false;
                    }

//                    Thread.sleep(1000);
//
//                    Date date = new Date();
//                    String word = date.toString();
//
//                    out.write(word + "\n");
//                    out.flush();
//                    String serverWord = in.readLine();
//
//                    System.out.println("Client: " + id + " " + serverWord);
//                    System.out.println(serverWord);
//                    if(word.equals("exit")) break;
                }

            } finally {
                System.out.println("Client " + id + " closed...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
        }
    }

    private void wait(Object o) {
        try {
            synchronized (o) {
                o.wait();
            }
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main (String [] args) {
        for (int i = 0; i < numStudents; i++)
            new StudentClient(i).start();
    }
}
