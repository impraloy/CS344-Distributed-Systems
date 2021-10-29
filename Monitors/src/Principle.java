package com.company;

import java.util.Random;
import java.util.Vector;

import static com.company.schoolCoordinator.arrivedStudents;
import static com.company.schoolCoordinator.time;

public class Principle extends Thread {
    public static Vector<Student> studentsWaitingToBeTested = new Vector<Student>();
    public static Vector<Student> studentsInClassRooms = new Vector<Student>();
    private static boolean principleIsWithStudent=false;
    private static int positionOfStudent=0;
    public static Object sessionNotification=new Object();
    public static boolean hasBeenNotifiedToStartClass=false;
    public static int studentsAttendingForCurrentSession=0;


    public Principle() {
        setName("principle");
    }

    @Override
    public void run() {
        super.run();

        msg("students are starting to be called");
        while (positionOfStudent<engine.numberOfStudents)
        {
          while (!principleIsWithStudent && positionOfStudent<arrivedStudents.size())
          {
               callStudent();
               positionOfStudent++;
          }

        }
       notify(schoolCoordinator.waitForSchoolDayToStart);
        while (schoolCoordinator.sessionsAttended<engine.NumberOfSessionsRequired())
        {

            wait(sessionNotification);

          schoolCoordinator.sessionInProgress=true;
          sleepSeconds(5);
          msg((schoolCoordinator.sessionsAttended+1)+" has been concluded");
           notifyAll(schoolCoordinator.sessionLockPE);
           sleepSeconds(1);
            notify(InstructorMaths.principleNotification);
            sleepSeconds(1);
            notify(InstructorEla.principleNotification);
            studentsAttendingForCurrentSession=0;
          msg("Everyone is taking a break");

          sleepSeconds(5);
            schoolCoordinator.sessionsAttended++;
            msg("Break concluded session "+(schoolCoordinator.sessionsAttended+1)+ " to begin");
            hasBeenNotifiedToStartClass=false;
            notify(schoolCoordinator.sessionLock);
            schoolCoordinator.sessionInProgress=false;


        }
    msg("school is goooooooone");

    }
    public void notifyAll(Object o) {
        synchronized (o) {
            o.notifyAll();
        }
    }
    public void wait(Object o) {

        try {

            synchronized (o) {
                o.wait();
            }
        }catch(InterruptedException e) {

            e.printStackTrace();
        }
    }
    public synchronized void callStudent() {
        principleIsWithStudent=true;
        Student student= arrivedStudents.get(positionOfStudent);
        msg(student.getName() +" has been called by the principle");
        if (student.questionnaire(engine.numberOfStudents)<(.15*engine.numberOfStudents))
        {
            student.stop();
            msg(student.getName()+" has been terminated because of incomplete questionnaire");
            principleIsWithStudent=false;
            return;
        }
        if (student.getArrivalTime()>engine.lateTime)
        {
            student.stop();
            msg(student.getName()+" has been terminated because of arriving late");
            principleIsWithStudent=false;
            return;
        }
        if (willTheStudentBeTested())
        {

          studentsWaitingToBeTested.add(student);
          notify(student);
          msg(student.getName()+" sent to the nurse office for testing");

        }
        else {
            studentsInClassRooms.add(student);
            notify(student);
            msg(student.getName()+" sent to class");
        }
        principleIsWithStudent=false;

    }
  private boolean willTheStudentBeTested()
  {
      return new Random().nextInt(engine.randomNumberToBeTested)==1;
  }
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

    public void notify(Object o) {
        synchronized(o) {
            o.notify();
        }
    }
    public void sleepSeconds(int sec) {

        try {
            Thread.sleep(sec * 1000);
        }catch(InterruptedException e) {

        }
    }
}
