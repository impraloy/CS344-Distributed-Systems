package com.company;

import com.company.Client.Student;
import com.company.Server.Principle;
import com.company.Server.SchoolCoordinator;

public class engine {
    private static SchoolCoordinator schoolCoordinator;
    public static final int numberOfStudents = 20;
    public static final long lateTime=10000000;
    public static final int randomNumberToBeTested=3;
    private static final int oddsOfBeingTested=1;
    public static final int maxSick=3;
    public static final int studentsCap=4;
    public static void main(String[] args) {
        Principle principle = new Principle();
        schoolCoordinator = new SchoolCoordinator(principle);
        schoolCoordinator.start();
        for (int i = 0; i < numberOfStudents; i++) {
            Student student = new Student(i);
            student.start();
        }
    }

    public static int NumberOfSessionsRequired() {
//        int students = Principle.studentsInClassRooms.size();
//        int sessions = (int) Math.ceil(students/8);
        return 3;
    }
}
