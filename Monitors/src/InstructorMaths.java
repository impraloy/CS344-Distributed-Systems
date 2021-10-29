package com.company;

public class InstructorMaths extends Thread{
    public static long time = System.currentTimeMillis();
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
        while (schoolCoordinator.sessionsAttended<engine.NumberOfSessionsRequired())
        {
          if (schoolCoordinator.sessionInProgress)
          {
              msg("session "+(schoolCoordinator.sessionsAttended+1)+" has been started");
              msg(" has started to teach");
              wait(principleNotification);
              msg("principle end class notification received");
              notifyAll(schoolCoordinator.sessionLockMATHS);
              msg("Students attending maths class notified of ending of class");
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
