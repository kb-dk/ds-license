package dk.kb.license.storage;

import dk.kb.license.model.v1.ChangeTypeEnumDto;
import dk.kb.license.model.v1.ObjectTypeEnumDto;

/**
 * Internal DTO used in the AUDITLOG table when adding a new audit for an object change
 * Every time a user changes any license or configuration definitions there will be generated an entry in the auditlog table.
 * The entry will have the objectId of the modified objects so all changes on that object can be extracted.
 * 
 *  The service generated AuditEntryOutputDto object is used when extracting information from the AuditLog
 * 
 */
public class AuditLogEntry {   
    
    private long objectId;
    private String userName;   
    private ChangeTypeEnumDto changeType;
    private ObjectTypeEnumDto changeName;
    private String changeComment;
    private String textBefore; 
    private String textAfter;
       
    /**
     * Create a AuditLogEntry object. The modified time will be set automatic in the storage method.
     * The userName will later be removed from this constructor when license-module GUI also has been written
     * in a new frontend and use OAuth. The userName will then be automatic set just as the modifiedTime.
     * 
     * @param objectId The id of the business object that has been modified.
     * @param userName FullName of the user. Taken from the Oath Token or session. Will be removed later.
     * @param changeType The type of change. UPDATE, CREATE, DELETE
     * @param changeName Name of the business object that has been changed
     * @param changeComment Optional custom comment that is tied to the ChangeName. The restricted ID method could log the id in this field.
     * @param textBefore The generated text from the business object before change.
     * @param textAfter  The generated text from the business object after change.
     * 
     * 
     */
    public AuditLogEntry(long objectId, String userName, ChangeTypeEnumDto changeType, ObjectTypeEnumDto changeName,
                    String changeComment, String textBefore, String textAfter) {        
        this.objectId = objectId;
        this.userName = userName;
        this.changeType = changeType;
        this.changeName = changeName;
        this.changeComment = changeComment;
        this.textBefore = textBefore;
        this.textAfter = textAfter;
    }

    public long getObjectId() {      
        return objectId;
    }

    public String getUserName() {
        return userName;
    }

    public ChangeTypeEnumDto getChangeType() {
        return changeType;
    }

    public ObjectTypeEnumDto getChangeName() {
        return changeName;
    }

    public String getChangeComment() {
        return changeComment;
    }

    public String getTextBefore() {
        return textBefore;
    }

    public String getTextAfter() {
        return textAfter;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setChangeType(ChangeTypeEnumDto changeType) {
        this.changeType = changeType;
    }

    public void setChangeName(ObjectTypeEnumDto changeName) {
        this.changeName = changeName;
    }

    public void setChangeComment(String changeComment) {
        this.changeComment = changeComment;
    }

    public void setTextBefore(String textBefore) {
        this.textBefore = textBefore;
    }

    public void setTextAfter(String textAfter) {
        this.textAfter = textAfter;
    }

    @Override
    public String toString() {
        return "AuditLogEntry [objectId=" + objectId + ", userName=" + userName + ", changeType=" + changeType + ", changeName=" + changeName + ", changeComment="
                + changeComment + ", textBefore=" + textBefore + ", textAfter=" + textAfter + "]";
    }
        
}
    
