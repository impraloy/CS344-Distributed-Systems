package com.company;

public class InstructorEla extends Thread{
    public static int currentClassCapacity=0;
    public static Object principleNotification = new Object();

    public InstructorEla()
    {
        setName("Ela instructor ");
    }
    public static long time = System.currentTimeMillis();

    @Override
    public void run() {
        super.run();
        msg("started...");
        while (schoolCoordinator.sessionsAttended<engine.NumberOfSessionsRequired())
        {
            if (schoolCoordinator.sessionInProgress){
                msg("session "+(schoolCoordinator.sessionsAttended+1)+" has been started");
                msg(" has started to teach");
          wait(principleNotification);
          notifyAll(schoolCoordinator.sessionLockELA);
                msg("Students attending Ela class notified of ending of class");
          msg("Session "+(schoolCoordinator.sessionsAttended+1)+" has been concluded");
          currentClassCapacity=0;
            }
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

    public void notify(Object o) {

        synchronized(o) {

            o.notify();
        }
    }

    public void msg(String m) {

        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

    public void sleepSeconds(int sec) {

        try {
            Thread.sleep(sec * 1000);
        }catch(InterruptedException e) {

        }
    }
    public void notifyAll(Object o) {
        synchronized (o) {
            o.notifyAll();
        }
    }
}
