package com.company.Server;

import com.company.engine;

public class InstructorMaths extends Thread{
    private static long time = System.currentTimeMillis();
    public static int currentClassCapacity=0;
    public static Object principleNotification = new Object();

    public InstructorMaths()
    {
        setName("Maths instructor ");
    }

    @Override
    public void run() {
        msg("started...");
        super.run();
        while (SchoolCoordinator.sessionsAttended< engine.NumberOfSessionsRequired())
        {
          if (SchoolCoordinator.sessionInProgress) {
              msg("session "+(SchoolCoordinator.sessionsAttended+1)+" has been started");
              msg(" has started to teach");
              wait(principleNotification);
              msg("principle end class notification received");
              notifyAll(SchoolCoordinator.sessionLockMATHS);
              msg("Students attending maths class notified of ending of class");
              msg("Session "+(SchoolCoordinator.sessionsAttended+1)+" has been concluded");
              currentClassCapacity=0;
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
