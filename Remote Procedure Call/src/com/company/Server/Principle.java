package com.company.Server;

import com.company.engine;

import java.util.ArrayList;
import java.util.Random;

public class Principle extends Thread {
    ///public static Vector<Student> studentsWaitingToBeTested = new Vector<>();
    public static ArrayList<Integer> studentsWaitingToBeTestedId = new ArrayList<>();

    ///public static Vector<Student> studentsInClassRooms = new Vector<Student>();
    public static ArrayList<Integer> studentsInClassRoomsId = new ArrayList<>();

    private static boolean principleIsWithStudent=false;
    private static int positionOfStudent=0;
    public static Object sessionNotification=new Object();
    public static boolean hasBeenNotifiedToStartClass=false;
    public static int studentsAttendingForCurrentSession=0;
    ///public static Vector<Student> attendingClass = new Vector<>();
    public static ArrayList<Integer> attendingClassId = new ArrayList<>();

    public Principle() {
        setName("principle");
    }

    @Override
    public void run() {
        super.run();

        msg("students are starting to be called");
        while (positionOfStudent < engine.numberOfStudents)
        {
            while (!principleIsWithStudent && positionOfStudent < SchoolCoordinator.arrivedStudentsId.size())
//            while (!principleIsWithStudent && positionOfStudent<SchoolCoordinator.arrivedStudents.size())
          {
               callStudent();
               positionOfStudent++;
          }

        }
       ///notify(SchoolCoordinator.waitForSchoolDayToStart);
        while (SchoolCoordinator.sessionsAttended < engine.NumberOfSessionsRequired()) {
            ///wait(sessionNotification);

            SchoolCoordinator.sessionInProgress=true;
            sleepSeconds(10);
            msg((SchoolCoordinator.sessionsAttended + 1) + " has been concluded");
            SchoolCoordinator.sessionInProgress=false;
            ///notifyAll(SchoolCoordinator.sessionLockPE);
            sleepSeconds(1);
            notify(InstructorMaths.principleNotification);
            sleepSeconds(1);
            ///notify(InstructorEla.principleNotification);
            studentsAttendingForCurrentSession=0;
            ///attendingClass.clear();//todo
            attendingClassId.clear();
            msg("Everyone is taking a break");

            sleepSeconds(5);
            SchoolCoordinator.sessionsAttended++;
            if (SchoolCoordinator.sessionsAttended!=3){
                msg("Break concluded session "+(SchoolCoordinator.sessionsAttended + 1)+ " to begin");
                hasBeenNotifiedToStartClass=false;
                notify(SchoolCoordinator.sessionLock);
            }
        }
    msg("school is finished");

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
    private int questionnaire(int totalStudents) {
        Random r = new Random();
        return r.nextInt(totalStudents);
    }
    private String studentName(int id) {
        return "Student " + id;
    }
    private synchronized void callStudent() {
        principleIsWithStudent=true;

        int studentId = SchoolCoordinator.arrivedStudentsId.get(positionOfStudent);
        msg("Student " + studentId +" has been called by the principle");
        if (questionnaire(engine.numberOfStudents) < (0.15*engine.numberOfStudents)) {
            msg(studentName(studentId) +" has been terminated because of incomplete questionnaire");
            principleIsWithStudent=false;
            return;
        }
        if (SchoolCoordinator.arrivalTimes.get(studentId) > engine.lateTime) {
            msg(studentName(studentId) +" has been terminated because of arriving late");
            principleIsWithStudent = false;
            return;
        }
        if (willTheStudentBeTested()) {
            studentsWaitingToBeTestedId.add(studentId);
            ///notify(student); todo
            msg(studentName(studentId)+" sent to the nurse office for testing");
        }
        else {
            studentsInClassRoomsId.add(studentId);
            ///notify(student); todo
            msg(studentName(studentId)+" sent to class");
        }

//        Student student = SchoolCoordinator.arrivedStudents.get(positionOfStudent);
//        msg(student.getName() +" has been called by the principle");
//        if (questionnaire(engine.numberOfStudents) < (.15*engine.numberOfStudents))
//        {
//            student.stop();
//            msg(student.getName()+" has been terminated because of incomplete questionnaire");
//            principleIsWithStudent=false;
//            return;
//        }
//        if (student.getArrivalTime() > engine.lateTime)
//        {
//            student.stop();
//            msg(student.getName()+" has been terminated because of arriving late");
//            principleIsWithStudent=false;
//            return;
//        }
//        if (willTheStudentBeTested())
//        {
//          studentsWaitingToBeTested.add(student);
//          notify(student);
//          msg(student.getName()+" sent to the nurse office for testing");
//        }
//        else {
//            studentsInClassRooms.add(student);
//            notify(student);
//            msg(student.getName()+" sent to class");
//        }
        principleIsWithStudent=false;
    }
    private boolean willTheStudentBeTested() {
        return new Random().nextInt(engine.randomNumberToBeTested)==1;
    }

    private void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-SchoolCoordinator.time)+"] "+getName()+": "+m);
    }
    private void sleepSeconds(int sec) {
        try {
            Thread.sleep(sec * 1000);
        }catch(InterruptedException e) {

        }
    }

    private void notify(Object o) {
        synchronized(o) {
            o.notify();
        }
    }
    private void notifyAll(Object o) {
        synchronized (o) {
            o.notifyAll();
        }
    }

}
