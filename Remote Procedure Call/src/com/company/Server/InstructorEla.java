package com.company.Server;

import com.company.engine;

public class InstructorEla extends Thread{
    public static int currentClassCapacity=0;
    public static Object principleNotification = new Object();

    public InstructorEla()
    {
        setName("Ela instructor ");
    }
    private static long time = System.currentTimeMillis();

    @Override
    public void run() {
        super.run();
        msg("started...");
        while (SchoolCoordinator.sessionsAttended < engine.NumberOfSessionsRequired()) {
            if (SchoolCoordinator.sessionInProgress) {
                msg("session "+(SchoolCoordinator.sessionsAttended+1)+" has been started");
                msg(" has started to teach");
                wait(principleNotification);
                notifyAll(SchoolCoordinator.sessionLockELA);
                msg("Students attending Ela class notified of ending of class");
                msg("Session "+(SchoolCoordinator.sessionsAttended+1)+" has been concluded");
                currentClassCapacity = 0;
            }
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

    private void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

    private void notifyAll(Object o) {
        synchronized (o) {
            o.notifyAll();
        }
    }
}
