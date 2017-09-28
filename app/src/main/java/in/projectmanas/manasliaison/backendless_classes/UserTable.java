package in.projectmanas.manasliaison.backendless_classes;

/**
 * Created by knnat on 9/16/2017.
 */

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class UserTable {
    private String pref1Confirm;
    private String pref2Confirm;
    private String CV;
    private java.util.Date updated;
    private String registrationNumber;
    private String objectId;
    private java.util.Date created;
    private String ownerId;
    private String deviceToken;
    private String div1;
    private String div2;
    private String githubID;
    private String hackerRankID;


    public UserTable() {
    }

    public static UserTable findById(String id) {
        return Backendless.Data.of(UserTable.class).findById(id);
    }

    public static void findByIdAsync(String id, AsyncCallback<UserTable> callback) {
        Backendless.Data.of(UserTable.class).findById(id, callback);
    }

    public static UserTable findFirst() {
        return Backendless.Data.of(UserTable.class).findFirst();
    }

    public static void findFirstAsync(AsyncCallback<UserTable> callback) {
        Backendless.Data.of(UserTable.class).findFirst(callback);
    }

    public static UserTable findLast() {
        return Backendless.Data.of(UserTable.class).findLast();
    }

    public static void findLastAsync(AsyncCallback<UserTable> callback) {
        Backendless.Data.of(UserTable.class).findLast(callback);
    }

    public static List<UserTable> find(DataQueryBuilder queryBuilder) {
        return Backendless.Data.of(UserTable.class).find(queryBuilder);
    }

    public static void findAsync(DataQueryBuilder queryBuilder, AsyncCallback<List<UserTable>> callback) {
        Backendless.Data.of(UserTable.class).find(queryBuilder, callback);
    }

    public String getCV() {
        return CV;
    }

    public void setCV(String CV) {
        this.CV = CV;
    }

    public java.util.Date getUpdated() {
        return updated;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getObjectId() {
        return objectId;
    }

    public java.util.Date getCreated() {
        return created;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public UserTable save() {
        return Backendless.Data.of(UserTable.class).save(this);
    }

    public void saveAsync(AsyncCallback<UserTable> callback) {
        Backendless.Data.of(UserTable.class).save(this, callback);
    }

    public Long remove() {
        return Backendless.Data.of(UserTable.class).remove(this);
    }

    public void removeAsync(AsyncCallback<Long> callback) {
        Backendless.Data.of(UserTable.class).remove(this, callback);
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }


    public String getDiv2() {
        return div2;
    }

    public void setDiv2(String div2) {
        this.div2 = div2;
    }

    public String getDiv1() {
        return div1;
    }

    public void setDiv1(String div1) {
        this.div1 = div1;
    }

    public String getGithubID() {
        return githubID;
    }

    public void setGithubID(String githubID) {
        this.githubID = githubID;
    }

    public String getHackerRankID() {
        return hackerRankID;
    }

    public void setHackerRankID(String hackerRankID) {
        this.hackerRankID = hackerRankID;
    }

    public String getPref1Confirm() {
        return pref1Confirm;
    }

    public void setPref1Confirm(String pref1Confirm) {
        this.pref1Confirm = pref1Confirm;
    }

    public String getPref2Confirm() {
        return pref2Confirm;
    }

    public void setPref2Confirm(String pref2Confirm) {
        this.pref2Confirm = pref2Confirm;
    }
}