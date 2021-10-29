package com.company;

public class studentData {
    long arrivalTime;
    int questionnaire;
    int id;

    public studentData(long arrivalTime, int questionnaire, int id) {
        this.arrivalTime = arrivalTime;
        this.questionnaire = questionnaire;
        this.id = id;
    }

    public studentData(long arrivalTime, int questionnaire) {

        this.arrivalTime = arrivalTime;
        this.questionnaire = questionnaire;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public int getQuestionnaire() {
        return questionnaire;
    }

    public int getId() {
        return id;
    }
}
