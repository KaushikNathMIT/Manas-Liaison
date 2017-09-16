package in.projectmanas.manasliaison.BackendlessClasses;

/**
 * Created by knnat on 9/16/2017.
 */


import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class Sheet {
    private String interviewPending1;
    private String interviewSelected2;
    private String interviewScheduled1;
    private java.util.Date created;
    private String tpRejected;
    private String interviewSelected1;
    private String spreadsheetId;
    private String interviewRejected1;
    private String interviewResultPending1;
    private String objectId;
    private java.util.Date updated;
    private String interviewScheduled2;
    private String interviewResultPending2;
    private String interviewStatus1;
    private String interviewRejected2;
    private String ownerId;
    private String tpPending;
    private String interviewPending2;
    private String interviewStatus2;
    private String tpStatus;
    private String tpSelected;
    private String emailID;
    private String mobileNumber;

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

    public String getInterviewPending1() {
        return interviewPending1;
    }

    public void setInterviewPending1(String interviewPending1) {
        this.interviewPending1 = interviewPending1;
    }

    public String getInterviewSelected2() {
        return interviewSelected2;
    }

    public void setInterviewSelected2(String interviewSelected2) {
        this.interviewSelected2 = interviewSelected2;
    }

    public String getInterviewScheduled1() {
        return interviewScheduled1;
    }

    public void setInterviewScheduled1(String interviewScheduled1) {
        this.interviewScheduled1 = interviewScheduled1;
    }

    public java.util.Date getCreated() {
        return created;
    }

    public String getTpRejected() {
        return tpRejected;
    }

    public void setTpRejected(String tpRejected) {
        this.tpRejected = tpRejected;
    }

    public String getInterviewSelected1() {
        return interviewSelected1;
    }

    public void setInterviewSelected1(String interviewSelected1) {
        this.interviewSelected1 = interviewSelected1;
    }

    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public void setSpreadsheetId(String spreadsheetId) {
        this.spreadsheetId = spreadsheetId;
    }

    public String getInterviewRejected1() {
        return interviewRejected1;
    }

    public void setInterviewRejected1(String interviewRejected1) {
        this.interviewRejected1 = interviewRejected1;
    }

    public String getInterviewResultPending1() {
        return interviewResultPending1;
    }

    public void setInterviewResultPending1(String interviewResultPending1) {
        this.interviewResultPending1 = interviewResultPending1;
    }

    public String getObjectId() {
        return objectId;
    }

    public java.util.Date getUpdated() {
        return updated;
    }

    public String getInterviewScheduled2() {
        return interviewScheduled2;
    }

    public void setInterviewScheduled2(String interviewScheduled2) {
        this.interviewScheduled2 = interviewScheduled2;
    }

    public String getInterviewResultPending2() {
        return interviewResultPending2;
    }

    public void setInterviewResultPending2(String interviewResultPending2) {
        this.interviewResultPending2 = interviewResultPending2;
    }

    public String getInterviewStatus1() {
        return interviewStatus1;
    }

    public void setInterviewStatus1(String interviewStatus1) {
        this.interviewStatus1 = interviewStatus1;
    }

    public String getInterviewRejected2() {
        return interviewRejected2;
    }

    public void setInterviewRejected2(String interviewRejected2) {
        this.interviewRejected2 = interviewRejected2;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getTpPending() {
        return tpPending;
    }

    public void setTpPending(String tpPending) {
        this.tpPending = tpPending;
    }

    public String getInterviewPending2() {
        return interviewPending2;
    }

    public void setInterviewPending2(String interviewPending2) {
        this.interviewPending2 = interviewPending2;
    }

    public String getInterviewStatus2() {
        return interviewStatus2;
    }

    public void setInterviewStatus2(String interviewStatus2) {
        this.interviewStatus2 = interviewStatus2;
    }

    public String getTpSelected() {
        return tpSelected;
    }

    public void setTpSelected(String tpSelected) {
        this.tpSelected = tpSelected;
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
}
