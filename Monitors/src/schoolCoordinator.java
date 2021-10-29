package com.company;

import java.util.*;

public class schoolCoordinator extends Thread{
    public static long time = System.currentTimeMillis();
    public static Object waitingToBeLetIn=new Object();
    public static Object waitForSchoolDayToStart = new Object();
    public static Vector<Student> arrivedStudents = new Vector<>();
    public static Vector<Student> waitingToAttendELA = new Vector<>();
    public static Vector<Student> waitingToAttendMATH = new Vector<>();
    public static Object classELALock = new Object();
    public static Object classMATHLOCK = new Object();
    public static Object sessionLock = new Object();
    public static Object sessionLockELA = new Object();
    public static Object sessionLockMATHS = new Object();
    public static Object sessionLockPE = new Object();
    public static Object nurseNotification = new Object();
    public static int studentsLetIn=0;
    public static int sessionsAttended=0;

    public static boolean sessionInProgress=false;
    public static boolean shouldPrincipleCalled=false;

    Principle principle;

    @Override
    public void run() {
        super.run();
       msg("the school coordinator started...");

       while (!shouldPrincipleCalled)
       {
           msg("waiting for students...");
           if (studentsLetIn>=18)
           {
               msg("18 students are being allowed in");
               notifyAll(waitingToBeLetIn);

               msg("starting principle...");
               startPrinciple();
               msg("principle started...");
               shouldPrincipleCalled=true;
           }

       }
        msg("waiting for notification from principle...");
        wait(waitForSchoolDayToStart);
        msg("Notification from principle received");
        msg(Principle.studentsWaitingToBeTested.size()+" students waiting for nurse to arrive");
        sleepSeconds(10);
        Nurse nurse=new Nurse();
        nurse.start();
        msg("the nurse has arrived");

        msg("Starting maths instructor...");
        InstructorMaths math=new InstructorMaths();
        math.start();
        msg("Maths instructor instructor...");

        msg("Starting maths instructor...");
        InstructorEla ela=new InstructorEla();
        ela.start();
        msg("Maths instructor instructor...");
        msg("number of sessions required "+engine.NumberOfSessionsRequired());
        wait(nurseNotification);
        msg("Nurse notification received");
        while (sessionsAttended<engine.NumberOfSessionsRequired())
        {

            if (!sessionInProgress)
            {
                notifyAll(classELALock);
                notifyAll(classMATHLOCK);
               if (sessionsAttended==2)
               {
                   sleepSeconds(2);
                   msg("priciple notified for last session");
               }
            }

        }
        msg("finished");

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
    public schoolCoordinator(Principle principle)
    {
        this.principle=principle;
        setName("School coordinator");
    }
    public void sleepSeconds(int sec) {

        try {
            Thread.sleep(sec * 1000);
        }catch(InterruptedException e) {

        }
    }
    private void startPrinciple() {
      principle.start();
    }
    public void msg(String m) {

        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }
    public void notify(Object o) {

        synchronized(o) {

            o.notify();
        }
    }

    public void notifyAll(Object o) {
        synchronized (o) {
            o.notifyAll();
        }
    }
}
