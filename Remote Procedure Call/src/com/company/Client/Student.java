package com.company.Client;

import com.company.engine;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

public class Student extends Thread {
    private static final int MAXSTUD = 18;
    private static long time = System.currentTimeMillis();
    private int studentId;
    private long arrivalTime;
    private boolean attendedELA=false;
    private boolean attendedMATH=false;
    private boolean isAlreadyAttending=false;
    private static HashSet<String> report = new HashSet<>();

    public long getArrivalTime1() {
        return arrivalTime;
    }

    private void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    private void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    private StudentClient studentClient;

    public Student(int id) {
        setStudentId(id);
        setName("Student "+id);
        studentClient = new StudentClient(id);
    }

    private void waitserver() {
        while(studentClient.sender) {
            try {
                Thread.sleep(2300);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void arriveAtSchool(){
        msg("has left home");
        sleepRandomTime();
        msg("Has arrived at the school yard and waiting to be let in");

        studentClient.sendCommand(0, studentId);
        if(studentClient.studentsLetIn < MAXSTUD) {
            waitserver();
        }

        setArrivalTime(System.currentTimeMillis() - time);
        studentClient.sendCommand(2, arrivalTime);
        waitserver();

        if(studentClient.studentsLetIn >= MAXSTUD) {
            msg("Has been let into the school");
        }

//        SchoolCoordinator.studentsLetIn++;// todo
//        if (SchoolCoordinator.studentsLetIn < MAXSTUD)// todo
//            wait(SchoolCoordinator.waitingToBeLetIn);
//
//        setArrivalTime(System.currentTimeMillis() - time);
//        SchoolCoordinator.arrivedStudents.add(this);// todo
//        if(SchoolCoordinator.arrivedStudents.size() >= MAXSTUD) {// todo
//            msg("Has been let into the school");
//        }
    }

    @Override
    public void run() {
        super.run();
        studentClient.start();

        arriveAtSchool();
        studentClient.sendCommand(1, 0);

        if (studentClient.studentsWaitingToBeTestedId_contains) {// todo
            wait(this);
        }
        studentClient.sendCommand(3, 0);//sessionsAttended
        waitserver();

        ///wait(SchoolCoordinator.nurseNotification);// todo
        while (StudentClient.sessionsAttended < engine.NumberOfSessionsRequired()) {
            studentClient.sendCommand(4, 0);//sessionInProgress
            waitserver();

            if (!studentClient.sessionInProgress && !isAlreadyAttending) {
                studentClient.sendCommand(5, 0);//studentsAttendingForCurrentSession
                waitserver();

                msg(" is attempting to attend a class in session "+(studentClient.sessionsAttended+1)+" as number "+
                        studentClient.studentsAttendingForCurrentSession);// todo

                studentClient.sendCommand(6, 0);//currentClassCapacity - all
                waitserver();

                if (!attendedELA && studentClient.Ela_currentClassCapacity < engine.studentsCap) {// todo
                    attemptToAttendELA();
                }
                else if (!attendedMATH && studentClient.Math_currentClassCapacity < engine.studentsCap) {// todo
                    attemptToAttendMATH();
                }
                else {
                    attendPE();
                }
            }
            studentClient.sendCommand(3, 0);//sessionsAttended
            waitserver();
        }


//        wait(this);
//        if (Principle.studentsWaitingToBeTested.contains(this)) {// todo
//            wait(this);
//        }
//        wait(SchoolCoordinator.nurseNotification);// todo
//        while (SchoolCoordinator.sessionsAttended < engine.NumberOfSessionsRequired()) {// todo
//            if (!SchoolCoordinator.sessionInProgress && !isAlreadyAttending) {// todo
//                Principle.studentsAttendingForCurrentSession+=1;// todo
//                msg(" is attempting to attend a class in session "+(SchoolCoordinator.sessionsAttended+1)+" as number "+
//                        Principle.studentsAttendingForCurrentSession);// todo
//
//                if (!attendedELA && InstructorEla.currentClassCapacity < engine.studentsCap) {// todo
//                    attemptToAttendELA();
//                }
//                else if (!attendedMATH && InstructorMaths.currentClassCapacity < engine.studentsCap) {// todo
//                    attemptToAttendMATH();
//                }
//                else {
//                    if (!attendedMATH && InstructorMaths.currentClassCapacity < engine.studentsCap) {// todo
//                        attemptToAttendMATH();
//                    }
//                    else
//                        attendPE();
//                }
//            }
//        }

        try {
            printReportAndGoHome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void attendPE() {
        isAlreadyAttending = true;
        msg("started attending Pe class");

        studentClient.sendCommand(7, 0);
        waitserver();
        if (studentClient.attendingClassId.size()==studentClient.studentsInClassRoomsIdsize && !studentClient.hasBeenNotifiedToStartClass)
        {
            ///notify(Principle.sessionNotification);// todo
            studentClient.sendCommand(8, 0);
            waitserver();
            msg("Principle notified by "+studentClient.studentsAttendingForCurrentSession);// todo
            studentClient.hasBeenNotifiedToStartClass = true;// todo
        }
        ///wait(SchoolCoordinator.sessionLockPE);// todo

//        Principle.attendingClass.add(this);// todo
//        if (Principle.attendingClass.size()==Principle.studentsInClassRooms.size() && !Principle.hasBeenNotifiedToStartClass)// todo
//        {
//            notify(Principle.sessionNotification);// todo
//            msg("Principle notified by "+Principle.studentsAttendingForCurrentSession);// todo
//            Principle.hasBeenNotifiedToStartClass = true;// todo
//        }
//        wait(SchoolCoordinator.sessionLockPE);// todo

        report.add(getName()+" Attended PE in session - " + (studentClient.sessionsAttended + 1));
//        report.add(getName()+" Attended PE in session - " + (SchoolCoordinator.sessionsAttended+1));// todo
        msg("finished attending PE");
        isAlreadyAttending = false;
    }


    private synchronized void  attemptToAttendMATH() {
        isAlreadyAttending = true;

        studentClient.sendCommand(7, 2);
        waitserver();
        if (studentClient.attendingClassId.size()==studentClient.studentsInClassRoomsIdsize && !studentClient.hasBeenNotifiedToStartClass)
        {
            ///notify(Principle.sessionNotification);// todo
            studentClient.sendCommand(8, 0);
            waitserver();
            msg("Principle notified by "+studentClient.studentsAttendingForCurrentSession);// todo
            studentClient.hasBeenNotifiedToStartClass = true;// todo
        }

//        wait(SchoolCoordinator.classMATHLOCK);// todo
        msg("started attending math class");
//        wait(SchoolCoordinator.sessionLockMATHS);// todo

//        InstructorMaths.currentClassCapacity++;// todo
//        Principle.attendingClass.add(this);// todo
//        if (Principle.attendingClass.size()==Principle.studentsInClassRooms.size() && !Principle.hasBeenNotifiedToStartClass)// todo
//        {
//            notify(Principle.sessionNotification);// todo
//            msg("Principle notified by "+Principle.studentsAttendingForCurrentSession);// todo
//            Principle.hasBeenNotifiedToStartClass=true;// todo
//        }
//
//        wait(SchoolCoordinator.classMATHLOCK);// todo
//        msg("started attending math class");
//        wait(SchoolCoordinator.sessionLockMATHS);// todo


        attendedMATH = true;
//        report.add(getName()+" Attended math class in session - " + (SchoolCoordinator.sessionsAttended+1));// todo
        report.add(getName()+" Attended math class in session - " + (StudentClient.sessionsAttended+1));// todo
        msg("finished attending math class");

        isAlreadyAttending=false;
    }

    private synchronized void attemptToAttendELA() {// todo
        isAlreadyAttending = true;

        studentClient.sendCommand(7, 1);
        waitserver();
        if (studentClient.attendingClassId.size()==studentClient.studentsInClassRoomsIdsize && !studentClient.hasBeenNotifiedToStartClass)
        {
            ///notify(Principle.sessionNotification);// todo
            studentClient.sendCommand(8, 0);
            waitserver();
            msg("Principle notified by "+studentClient.studentsAttendingForCurrentSession);// todo
            studentClient.hasBeenNotifiedToStartClass = true;// todo
        }

//        wait(SchoolCoordinator.classELALock);// todo
        msg("started attending Ela class");
//        wait(SchoolCoordinator.sessionLockELA);// todo

//        InstructorEla.currentClassCapacity++;// todo
//        Principle.attendingClass.add(this);// todo
//        if (Principle.attendingClass.size()==Principle.studentsInClassRooms.size() && !Principle.hasBeenNotifiedToStartClass)// todo
//        {
//            notify(Principle.sessionNotification);// todo
//            msg("Principle notified by "+Principle.studentsAttendingForCurrentSession);// todo
//            Principle.hasBeenNotifiedToStartClass=true;// todo
//        }
//        wait(SchoolCoordinator.classELALock);// todo
//
//        msg("started attending Ela class");
//        wait(SchoolCoordinator.sessionLockELA);// todo


        attendedELA=true;
//        report.add(getName()+" Attended Ela class in session - " + (SchoolCoordinator.sessionsAttended+1));// todo
        report.add(getName()+" Attended Ela class in session - " + (StudentClient.sessionsAttended+1));// todo

        msg("finished attending Ela class");
        isAlreadyAttending=false;

    }

    private void printReportAndGoHome() throws IOException {
        FileWriter outputFile = new FileWriter("output.txt");
        for (String s : report) {
            outputFile.write(s + "\n");
        }
        outputFile.close();
//        for (String s : report) {
//            System.out.println(s);
//        }
        msg("school is ended for the day");
    }

    private void sleepRandomTime() {
        Random r = new Random();
        int time = r.nextInt(1000) + 1;
        try {
            Thread.sleep(time);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void wait(Object o) {
        try {
            synchronized (o) {
                o.wait();
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

    private void notify(Object o) {
        synchronized(o) {
            o.notify();
        }
    }

    public static void main (String [] args) {
        for (int i = 0; i < engine.numberOfStudents; i++) {
            Student student = new Student(i);
            student.start();
        }
    }
}
