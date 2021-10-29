package com.company.Server;

import com.company.Server.InstructorEla;
import com.company.Server.InstructorMaths;
import com.company.Server.Principle;
import com.company.Server.SchoolCoordinator;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket clientSock;
    private int studentId;
    private SchoolCoordinator schoolCoordinator;

    public ServerThread (Socket clientSock, SchoolCoordinator schoolCoordinator) {
        this.clientSock = clientSock;
        this.schoolCoordinator = schoolCoordinator;
    }

    private int boolToint(boolean b) {
        if(b) return 1; else return 0;
    }

    public void run() {
        System.out.println("Got a connection: " + clientSock.getInetAddress() + " Port: " + clientSock.getPort() + "\n");

        try {
            BufferedReader in = null;
            BufferedWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSock.getOutputStream()));

                String word;
                do {
                    word = in.readLine();
                    String[] commands = new String[0];
                    int com = -1;
                    if(word != null) {
                        commands = word.split(" ");
                        com = Integer.valueOf(commands[0]);
                        ///System.out.println(studentId+" "+word);
                    }

                    ///System.out.println(studentId+" "+com);
                    switch (com) {
                        case 0:
                            studentId = Integer.valueOf(commands[1]);
                            SchoolCoordinator.studentsLetIn++;
                            out.write(schoolCoordinator.studentsLetIn + "\n");
                            break;
                        case 1:
                            out.write(boolToint(Principle.studentsWaitingToBeTestedId.contains(studentId)) + "\n");
                            break;
                        case 2:
                            SchoolCoordinator.arrivedStudentsId.add(studentId);
                            SchoolCoordinator.arrivalTimes.put(studentId, Long.valueOf(commands[1]));
                            out.write(0 + "\n");
                            break;
                        case 3:
                            out.write(SchoolCoordinator.sessionsAttended + "\n");
                            break;
                        case 4:
                            out.write(boolToint(SchoolCoordinator.sessionInProgress) + "\n");
                            break;
                        case 5:
                            Principle.studentsAttendingForCurrentSession++;
                            out.write(Principle.studentsAttendingForCurrentSession + "\n");
                            break;
                        case 6://currentClassCapacity - all
                            out.write(InstructorEla.currentClassCapacity + " " + InstructorMaths.currentClassCapacity + "\n");
                            break;
                        case 7://attendingClassId
                            if(!Principle.attendingClassId.contains(studentId)) Principle.attendingClassId.add(studentId);
                            ///System.out.println("777 "+InstructorEla.currentClassCapacity);
                            if(Integer.valueOf(commands[1]) == 1) InstructorEla.currentClassCapacity++;
                            if(Integer.valueOf(commands[1]) == 2) InstructorMaths.currentClassCapacity++;
                            if(InstructorEla.currentClassCapacity > 4) {
                                InstructorEla.currentClassCapacity = 0;
                            }
                            if(InstructorMaths.currentClassCapacity > 4) {
                                InstructorMaths.currentClassCapacity = 0;
                                SchoolCoordinator.sessionsAttended++;
                            }
                            out.write(Principle.studentsAttendingForCurrentSession + " " + Principle.studentsInClassRoomsId.size() + " "
                                    + boolToint(Principle.hasBeenNotifiedToStartClass) + "\n");
                            break;
                        case 8:
                            Principle.hasBeenNotifiedToStartClass = true;
                            out.write(Principle.studentsAttendingForCurrentSession + "\n");
                            break;
                    }
                    if(com >= 0) out.flush();

                } while(word == null || !word.equals("exit"));

            } finally {
                System.out.println("Connection  closed. Port: " + clientSock.getPort() + "\n");
                clientSock.close();

                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
