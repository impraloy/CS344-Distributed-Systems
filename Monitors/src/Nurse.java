package com.company;

import javax.swing.text.html.HTMLDocument;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class Nurse extends Thread {
    public static long time = System.currentTimeMillis();
    public static Vector<Student> covidPositiveStudents = new Vector<Student>();
    public static Object nurseOverallNotification =new Object();
    private static int studentPosition=0;
    private static boolean nurseIsOccupied=false;

    public Nurse()
    {
        setName("nurse");
    }
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

    @Override
    public void run() {
        super.run();

        msg("students are starting to be let into the clinic in pairs with a line of"+Principle.studentsWaitingToBeTested.size());
        while (studentPosition<Principle.studentsWaitingToBeTested.size())
        {
           callStudents();

        }
     if (covidPositiveStudents.size()>engine.maxSick)
     {
         msg("all threads terminated and everyone goes home");
     }
     msg("The nurse has gone home "+Principle.studentsInClassRooms.size());
     sleepSeconds(1);
     notifyAll(schoolCoordinator.nurseNotification);


    }

    private synchronized void callStudents() {
        nurseIsOccupied=true;
        int []pairs=selectedPair();
        Student student2 = null;
        if (pairs[1]!=-1)
        {
             student2 = Principle.studentsWaitingToBeTested.get(pairs[1]);

        }

        Student student1 = (Student) Principle.studentsWaitingToBeTested.get(pairs[0]);

        msg("student "+student1.getName()+(student2==null?" ":" and student "+student2.getName())+" called by nurse for testing");

        if(isCovidPositive())
        {
            student1.stop();
            covidPositiveStudents.add(student1);
            msg("The student has been sent home due to positive covid19 test");
        }
        else {
            msg("The student has been sent to class");
            notify(student1);
            Principle.studentsInClassRooms.add(student1);
        }

        if (student2!=null)
        {
            if(isCovidPositive())
            {
                student2.stop();
                covidPositiveStudents.add(student2);
                msg("The student has been sent home due to positive covid19 test");
            }
            else {
                msg("The student has been sent to class");
                notify(student2);
                Principle.studentsInClassRooms.add(student2);
            }

         }
       studentPosition+=2;
        nurseIsOccupied=false;

    }

    private static boolean isCovidPositive()
    {
        //double 3 perecent of 20
        double percentage=0.6;
        Random r = new Random();
        double randomValue = 0 + (engine.numberOfStudents) * r.nextDouble();
        return randomValue <= percentage;

    }
    private int[]selectedPair()
    {
        int[]selectedPairs=new int[2];
        int newPair=studentPosition+1;
        selectedPairs[0]=studentPosition;
        if (newPair>=Principle.studentsWaitingToBeTested.size())
        selectedPairs[1]=-1;
        else selectedPairs[1]=newPair;
        return selectedPairs;
    }
    public void sleepSeconds(int sec) {

        try {
            Thread.sleep(sec * 1000);
        }catch(InterruptedException e) {

        }
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
