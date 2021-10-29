package com.company;

public class engine {
    static schoolCoordinator c;
    static int numberOfStudents=20;
    static long lateTime=10000;
    static int randomNumberToBeTested=3;
    static int oddsOfBeingTested=1;
    static int maxSick=3;
    static int studentsCap=4;
    public static void main(String[] args) {
        Principle principle=new Principle();
        c=new schoolCoordinator(principle);
        c.start();
        for (int i=0;i<20;i++)
        {
            Student student=new Student(i);
            student.start();
        }
    }
    public static int NumberOfSessionsRequired()
    {
        int students=Principle.studentsInClassRooms.size();
        int sessions= (int) Math.ceil(students/8);
        return 3;

    }
}
