package in.projectmanas.manasliaison.backendless_classes;

/**
 * Created by knnat on 9/16/2017.
 */


import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class Sheet {
    private String pref1Schedule;
    private String pref2Schedule;

    private java.util.Date created;

    private String spreadsheetId;
    private String objectId;
    private java.util.Date updated;
    private String interviewStatus1;
    private String ownerId;
    private String interviewStatus2;
    private String tpStatus;
    private String emailID;
    private String mobileNumber;
    private String name;
    private String regNumber;
    private String div;
    private String prefDiv1;
    private String prefDiv2;

    public static Sheet findById(String id) {
        return Backendless.Data.of(Sheet.class).findById(id);
    }

    public static void findByIdAsync(String id, AsyncCallback<Sheet> callback) {
        Backendless.Data.of(Sheet.class).findById(id, callback);
    }

    public static Sheet findFirst() {
        return Backendless.Data.of(Sheet.class).findFirst();
    }

    public static void findFirstAsync(AsyncCallback<Sheet> callback) {
        Backendless.Data.of(Sheet.class).findFirst(callback);
    }

    public static Sheet findLast() {
        return Backendless.Data.of(Sheet.class).findLast();
    }

    public static void findLastAsync(AsyncCallback<Sheet> callback) {
        Backendless.Data.of(Sheet.class).findLast(callback);
    }

    public static List<Sheet> find(DataQueryBuilder queryBuilder) {
        return Backendless.Data.of(Sheet.class).find(queryBuilder);
    }

    public static void findAsync(DataQueryBuilder queryBuilder, AsyncCallback<List<Sheet>> callback) {
        Backendless.Data.of(Sheet.class).find(queryBuilder, callback);
    }


    public java.util.Date getCreated() {
        return created;
    }


    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public void setSpreadsheetId(String spreadsheetId) {
        this.spreadsheetId = spreadsheetId;
    }


    public String getObjectId() {
        return objectId;
    }

    public java.util.Date getUpdated() {
        return updated;
    }


    public String getInterviewStatus1() {
        return interviewStatus1;
    }

    public void setInterviewStatus1(String interviewStatus1) {
        this.interviewStatus1 = interviewStatus1;
    }


    public String getOwnerId() {
        return ownerId;
    }


    public String getInterviewStatus2() {
        return interviewStatus2;
    }

    public void setInterviewStatus2(String interviewStatus2) {
        this.interviewStatus2 = interviewStatus2;
    }

    public Sheet save() {
        return Backendless.Data.of(Sheet.class).save(this);
    }

    public void saveAsync(AsyncCallback<Sheet> callback) {
        Backendless.Data.of(Sheet.class).save(this, callback);
    }

    public Long remove() {
        return Backendless.Data.of(Sheet.class).remove(this);
    }

    public void removeAsync(AsyncCallback<Long> callback) {
        Backendless.Data.of(Sheet.class).remove(this, callback);
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTpStatus() {
        return tpStatus;
    }

    public void setTpStatus(String tpStatus) {
        this.tpStatus = tpStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public String getPrefDiv1() {
        return prefDiv1;
    }

    public void setPrefDiv1(String prefDiv1) {
        this.prefDiv1 = prefDiv1;
    }

    public String getPrefDiv2() {
        return prefDiv2;
    }

    public void setPrefDiv2(String prefDiv2) {
        this.prefDiv2 = prefDiv2;
    }

    public String getPref1Schedule() {
        return pref1Schedule;
    }

    public void setPref1Schedule(String pref1Schedule) {
        this.pref1Schedule = pref1Schedule;
    }

    public String getPref2Schedule() {
        return pref2Schedule;
    }

    public void setPref2Schedule(String pref2Schedule) {
        this.pref2Schedule = pref2Schedule;
    }
}
