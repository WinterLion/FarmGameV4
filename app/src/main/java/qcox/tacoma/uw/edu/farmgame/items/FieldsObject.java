package qcox.tacoma.uw.edu.farmgame.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cox Family on 5/31/2016.
 */
public class FieldsObject {

    List<FarmField> mFields;
    String mUsername;
    long mSystemtime;

    public FieldsObject(String theUsername, long theSystemTime){
        mFields  = new ArrayList<>();
        mUsername = theUsername;
        mSystemtime = theSystemTime;
    }

    public List<FarmField> getFields() {
        return mFields;
    }

    public void setFields(List<FarmField> mFields) {
        this.mFields = mFields;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public long getSystemtime() {
        return mSystemtime;
    }

    public void addField(int thePosition, String theCrop){
        FarmField theField = new FarmField(thePosition, theCrop);
        this.mFields.add(theField);
    }

    public void setSystemtime(int mSystemtime) {
        this.mSystemtime = mSystemtime;
    }

    public int getPosition(int index) {
        return this.mFields.get(index).mPosition;
    }

    public void setPosition(int index, int mPosition) {
        this.mFields.get(index).mPosition = mPosition;
    }

    public String getCrop(int index) {
        return this.mFields.get(index).mCrop;
    }

    public void setCrop(int index, String mCrop) {
        this.mFields.get(index).mCrop = mCrop;
    }

    private class FarmField{
        int mPosition;
        String mCrop;
        FarmField(int thePosition, String theCrop){
            mPosition = thePosition;
            mCrop = theCrop;
        }
    }
}
