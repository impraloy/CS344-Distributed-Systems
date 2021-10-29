package com.company;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import static com.company.schoolCoordinator.nurseNotification;

public class Student extends Thread{
    public static long time = System.currentTimeMillis();
    private int studentId;
    private long arrivalTime;
    private boolean attendedELA=false;
    private boolean attendedMATH=false;
    private boolean isAlreadyAttending=false;

    private HashSet<String> report = new HashSet<String>();


    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getStudentId() {
        return studentId;
    }
    public Student(int id) {

        setStudentId(id);
        setName("Student "+id);

    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public synchronized void arriveAtSchool(){
        msg("has left home");
        sleepRandomTime();
        msg("Has arrived at the school yard and waiting to be let in");
        schoolCoordinator.studentsLetIn++;
        if (schoolCoordinator.studentsLetIn<18)
            wait(schoolCoordinator.waitingToBeLetIn);

            setArrivalTime(System.currentTimeMillis()-time);
            schoolCoordinator.arrivedStudents.add(this);
        msg("Has been let into the school");
     }

    @Override
    public void run() {
        super.run();
            arriveAtSchool();
          wait(this);
          if (Principle.studentsWaitingToBeTested.contains(this))
          {
              wait(this);
          }
        wait(nurseNotification);
           while (schoolCoordinator.sessionsAttended<engine.NumberOfSessionsRequired())
          {
if (!schoolCoordinator.sessionInProgress && !isAlreadyAttending)
{
    Principle.studentsAttendingForCurrentSession+=1;
msg(" is attempting to attend a class in session "+(schoolCoordinator.sessionsAttended+1)+" as number "+Principle.studentsAttendingForCurrentSession);
    if (Principle.studentsAttendingForCurrentSession==Principle.studentsInClassRooms.size() && !Principle.hasBeenNotifiedToStartClass)
    {
        notify(Principle.sessionNotification);
        msg("Principle notified by "+Principle.studentsAttendingForCurrentSession);
        Principle.hasBeenNotifiedToStartClass=true;
    }
    if (!attendedELA && InstructorEla.currentClassCapacity < engine.studentsCap)
    {
        attemptToAttendELA();


    }
    else
    if (!attendedMATH && InstructorMaths.currentClassCapacity < engine.studentsCap)
    {

        attemptToAttendMATH();

    }
    else
    {
        attendPE();

    }


}


          }

          printReportAndGoHome();

    }

    private synchronized void attendPE() {
        isAlreadyAttending=true;
        msg("started attending Pe class");

        wait(schoolCoordinator.sessionLockPE);

        report.add(getName()+" Attended PE in session - " + (schoolCoordinator.sessionsAttended+1));
        msg("finished attending PE");
        isAlreadyAttending=false;
    }


    private synchronized void  attemptToAttendMATH() {
        isAlreadyAttending=true;
        InstructorMaths.currentClassCapacity+=1;
            wait(schoolCoordinator.classMATHLOCK);


        msg("started attending math class");

        wait(schoolCoordinator.sessionLockMATHS);


            attendedMATH=true;

            report.add(getName()+" Attended math class in session - " + (schoolCoordinator.sessionsAttended+1));

            msg("finished attending math class");
        isAlreadyAttending=false;
    }

    private synchronized void attemptToAttendELA() {
        isAlreadyAttending=true;
        InstructorEla.currentClassCapacity+=1;
        wait(schoolCoordinator.classELALock);

        msg("started attending Ela class");

        wait(schoolCoordinator.sessionLockELA);

        attendedELA=true;

        report.add(getName()+" Attended Ela class in session - " + (schoolCoordinator.sessionsAttended+1));

        msg("finished attending Ela class");
        isAlreadyAttending=false;

    }

    private void printReportAndGoHome() {


        for (String s : report) {
            System.out.println(s);
        }
        msg("school is ended for the day");
    }


    public int questionnaire(int totalStudents)
{
    Random r = new Random();
    return r.nextInt(totalStudents);
}
    public void sleepRandomTime() {

        Random r = new Random();
        int time = r.nextInt(1000) + 1;

        try {
            Thread.sleep(time);

        }catch(InterruptedException e) {
            e.printStackTrace();
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
    public void sleepSeconds(int sec) {

        try {
            Thread.sleep(sec * 1000);
        }catch(InterruptedException e) {

        }
    }
    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }
    public void notify(Object o) {
        synchronized(o) {
            o.notify();
        }
    }
}
