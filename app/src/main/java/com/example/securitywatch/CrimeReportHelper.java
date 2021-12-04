package com.example.securitywatch;

public class CrimeReportHelper {
    private String crimeLocation;
    private String crimeTitle;
    private String crimeDescription;
    private String reporter;
    private String respondent;
    private String respondentLocation;
    private String respondentStatus;
    private String stat;

    public CrimeReportHelper(){

    }

//    public CrimeReportHelper(String location, String title, String description, String reporter, String respondent, String respondentLocation, String respondentStatus, String s){
//        this.crimeLocation =  location;
//        this.crimeTitle = title;
//        this.crimeDescription = description;
//        this.reporter = reporter;
//        this.respondent = respondent;
//        this.respondentLocation = respondentLocation;
//        this.respondentStatus = respondentStatus;
//        this.stat = s;
//    }

    public CrimeReportHelper(String location, String title, String description, String reported_by, String respondent, String respondentLocation, String respondentStatus) {
        this.crimeLocation =  location;
        this.crimeTitle = title;
        this.crimeDescription = description;
        this.reporter = reported_by;
        this.respondent = respondent;
        this.respondentLocation = respondentLocation;
        this.respondentStatus = respondentStatus;
    }

    public String getCrimeDescription() {
        return crimeDescription;
    }

    public String getCrimeTitle() {
        return crimeTitle;
    }

    public String getCrimeLocation() {
        return crimeLocation;
    }

    public String getReporter() {
        return reporter;
    }

    public String getRespondent() {
        return respondent;
    }

    public String getRespondentLocation() {
        return respondentLocation;
    }

    public String getRespondentStatus() {
        return respondentStatus;
    }

    public String getStat() {
        return stat;
    }

    public void setRespondent(String respondent) {
        this.respondent = respondent;
    }

    public void setRespondentLocation(String respondentLocation) {
        this.respondentLocation = respondentLocation;
    }

    public void setRespondentStatus(String respondentStatus) {
        this.respondentStatus = respondentStatus;
    }

    public void setCrimeDescription(String crimeDescription) {
        this.crimeDescription = crimeDescription;
    }

    public void setCrimeLocation(String crimeLocation) {
        this.crimeLocation = crimeLocation;
    }

    public void setCrimeTitle(String crimeTitle) {
        this.crimeTitle = crimeTitle;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
