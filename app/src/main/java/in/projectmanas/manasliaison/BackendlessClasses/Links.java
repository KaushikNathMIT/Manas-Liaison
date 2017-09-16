package in.projectmanas.manasliaison.BackendlessClasses;

/**
 * Created by knnat on 9/16/2017.
 */


import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class Links {
    private String registrationForm;
    private java.util.Date created;
    private String objectId;
    private String ownerId;
    private String onlineChallenge;
    private String posterURI;
    private java.util.Date updated;

    public static Links findById(String id) {
        return Backendless.Data.of(Links.class).findById(id);
    }

    public static void findByIdAsync(String id, AsyncCallback<Links> callback) {
        Backendless.Data.of(Links.class).findById(id, callback);
    }

    public static Links findFirst() {
        return Backendless.Data.of(Links.class).findFirst();
    }

    public static void findFirstAsync(AsyncCallback<Links> callback) {
        Backendless.Data.of(Links.class).findFirst(callback);
    }

    public static Links findLast() {
        return Backendless.Data.of(Links.class).findLast();
    }

    public static void findLastAsync(AsyncCallback<Links> callback) {
        Backendless.Data.of(Links.class).findLast(callback);
    }

    public static List<Links> find(DataQueryBuilder queryBuilder) {
        return Backendless.Data.of(Links.class).find(queryBuilder);
    }

    public static void findAsync(DataQueryBuilder queryBuilder, AsyncCallback<List<Links>> callback) {
        Backendless.Data.of(Links.class).find(queryBuilder, callback);
    }

    public String getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationForm(String registrationForm) {
        this.registrationForm = registrationForm;
    }

    public java.util.Date getCreated() {
        return created;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getOnlineChallenge() {
        return onlineChallenge;
    }

    public void setOnlineChallenge(String onlineChallenge) {
        this.onlineChallenge = onlineChallenge;
    }

    public String getPosterURI() {
        return posterURI;
    }

    public void setPosterURI(String posterURI) {
        this.posterURI = posterURI;
    }

    public java.util.Date getUpdated() {
        return updated;
    }

    public Links save() {
        return Backendless.Data.of(Links.class).save(this);
    }

    public void saveAsync(AsyncCallback<Links> callback) {
        Backendless.Data.of(Links.class).save(this, callback);
    }

    public Long remove() {
        return Backendless.Data.of(Links.class).remove(this);
    }

    public void removeAsync(AsyncCallback<Long> callback) {
        Backendless.Data.of(Links.class).remove(this, callback);
    }
}