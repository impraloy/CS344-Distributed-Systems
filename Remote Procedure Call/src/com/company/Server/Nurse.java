package com.company.Server;

import com.company.engine;

import java.util.ArrayList;
import java.util.Random;

public class Nurse extends Thread {
    private static long time = System.currentTimeMillis();
    ///private static Vector<Student> covidPositiveStudents = new Vector<Student>();
    private static ArrayList<Integer> covidPositiveStudentsId = new ArrayList<>();

    private static Object nurseOverallNotification =new Object();
    private static int studentPosition=0;
    private static boolean nurseIsOccupied=false;

    public Nurse()
    {
        setName("nurse");
    }
    private void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

    @Override
    public void run() {
        super.run();

        msg("students are starting to be let into the clinic in pairs with a line of"+ Principle.studentsWaitingToBeTestedId.size());
        while (studentPosition<Principle.studentsWaitingToBeTestedId.size()) {
            callStudents();
        }
        if (covidPositiveStudentsId.size()> engine.maxSick) {
            msg("all threads terminated and everyone goes home");
        }
        msg("The nurse has gone home "+Principle.studentsInClassRoomsId.size());

//        msg("students are starting to be let into the clinic in pairs with a line of"+Principle.studentsWaitingToBeTested.size());
//        while (studentPosition<Principle.studentsWaitingToBeTested.size()) {
//            callStudents();
//        }
//        if (covidPositiveStudents.size()> engine.maxSick) {
//            msg("all threads terminated and everyone goes home");
//        }
//        msg("The nurse has gone home "+Principle.studentsInClassRooms.size());
        sleepSeconds(1);
        notifyAll(SchoolCoordinator.nurseNotification);
    }

    private synchronized void callStudents() {
        nurseIsOccupied=true;
        int[] pairs = selectedPair();

        int student2 = -1;
        if (pairs[1]!=-1) {
            student2 = Principle.studentsWaitingToBeTestedId.get(pairs[1]);
        }
        int student1 = Principle.studentsWaitingToBeTestedId.get(pairs[0]);
        msg("student "+"Student " + student1 +(student2 == -1?" ":" and student "+"Student " + student2)+" called by nurse for testing");
        if(isCovidPositive()) {
            ///student1.stop();
            covidPositiveStudentsId.add(student1);
            msg("The student has been sent home due to positive covid19 test");
        }
        else {
            msg("The student has been sent to class");
            ///notify(student1);//todo
            Principle.studentsInClassRoomsId.add(student1);
        }
        if (student2 != -1) {
            if(isCovidPositive()) {
                ///student2.stop();
                covidPositiveStudentsId.add(student2);
                msg("The student has been sent home due to positive covid19 test");
            }
            else {
                msg("The student has been sent to class");
                ///notify(student2);//todo
                Principle.studentsInClassRoomsId.add(student2);
            }
        }


//        Student student2 = null;
//        if (pairs[1]!=-1) {
//            student2 = Principle.studentsWaitingToBeTested.get(pairs[1]);
//        }
//        Student student1 = (Student) Principle.studentsWaitingToBeTested.get(pairs[0]);
//        msg("student "+student1.getName()+(student2==null?" ":" and student "+student2.getName())+" called by nurse for testing");
//        if(isCovidPositive()) {
//            student1.stop();
//            covidPositiveStudents.add(student1);
//            msg("The student has been sent home due to positive covid19 test");
//        }
//        else {
//            msg("The student has been sent to class");
//            notify(student1);
//            Principle.studentsInClassRooms.add(student1);
//        }
//        if (student2 != null) {
//            if(isCovidPositive()) {
//                student2.stop();
//                covidPositiveStudents.add(student2);
//                msg("The student has been sent home due to positive covid19 test");
//            }
//            else {
//                msg("The student has been sent to class");
//                notify(student2);
//                Principle.studentsInClassRooms.add(student2);
//            }
//        }

        studentPosition+=2;
        nurseIsOccupied=false;
    }

    private static boolean isCovidPositive()
    {
        double percentage=0.6;//double 3 perecent of 20
        Random r = new Random();
        double randomValue = 0 + (engine.numberOfStudents) * r.nextDouble();
        return randomValue <= percentage;
    }
    private int[]selectedPair()
    {
        int[]selectedPairs=new int[2];
        int newPair=studentPosition+1;
        selectedPairs[0]=studentPosition;
//        if (newPair>=Principle.studentsWaitingToBeTested.size())
        if (newPair>=Principle.studentsWaitingToBeTestedId.size())
        selectedPairs[1]=-1;
        else selectedPairs[1]=newPair;
        return selectedPairs;
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
