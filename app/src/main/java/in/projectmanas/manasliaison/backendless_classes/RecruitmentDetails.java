package in.projectmanas.manasliaison.backendless_classes;

/**
 * Created by knnat on 9/16/2017.
 */

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.Date;
import java.util.List;

public class RecruitmentDetails {
    private Integer phase;
    private Date onlineChallengeDate;
    private String reScheduleCall;
    private java.util.Date updated;
    private String objectId;
    private String ownerId;
    private java.util.Date created;

    public static RecruitmentDetails findById(String id) {
        return Backendless.Data.of(RecruitmentDetails.class).findById(id);
    }

    public static void findByIdAsync(String id, AsyncCallback<RecruitmentDetails> callback) {
        Backendless.Data.of(RecruitmentDetails.class).findById(id, callback);
    }

    public static RecruitmentDetails findFirst() {
        return Backendless.Data.of(RecruitmentDetails.class).findFirst();
    }

    public static void findFirstAsync(AsyncCallback<RecruitmentDetails> callback) {
        Backendless.Data.of(RecruitmentDetails.class).findFirst(callback);
    }

    public static RecruitmentDetails findLast() {
        return Backendless.Data.of(RecruitmentDetails.class).findLast();
    }

    public static void findLastAsync(AsyncCallback<RecruitmentDetails> callback) {
        Backendless.Data.of(RecruitmentDetails.class).findLast(callback);
    }

    public static List<RecruitmentDetails> find(DataQueryBuilder queryBuilder) {
        return Backendless.Data.of(RecruitmentDetails.class).find(queryBuilder);
    }

    public static void findAsync(DataQueryBuilder queryBuilder, AsyncCallback<List<RecruitmentDetails>> callback) {
        Backendless.Data.of(RecruitmentDetails.class).find(queryBuilder, callback);
    }

    public Integer getPhase() {
        return phase;
    }

    public void setPhase(Integer phase) {
        this.phase = phase;
    }

    public java.util.Date getUpdated() {
        return updated;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public java.util.Date getCreated() {
        return created;
    }

    public RecruitmentDetails save() {
        return Backendless.Data.of(RecruitmentDetails.class).save(this);
    }

    public void saveAsync(AsyncCallback<RecruitmentDetails> callback) {
        Backendless.Data.of(RecruitmentDetails.class).save(this, callback);
    }

    public Long remove() {
        return Backendless.Data.of(RecruitmentDetails.class).remove(this);
    }

    public void removeAsync(AsyncCallback<Long> callback) {
        Backendless.Data.of(RecruitmentDetails.class).remove(this, callback);
    }

    public Date getOnlineChallengeDate() {
        return onlineChallengeDate;
    }

    public void setOnlineChallengeDate(Date onlineChallengeDate) {
        this.onlineChallengeDate = onlineChallengeDate;
    }

    public String getReScheduleCall() {
        return reScheduleCall;
    }

    public void setReScheduleCall(String reScheduleCall) {
        this.reScheduleCall = reScheduleCall;
    }
}
