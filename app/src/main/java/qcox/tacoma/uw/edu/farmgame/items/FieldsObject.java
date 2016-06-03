package qcox.tacoma.uw.edu.farmgame.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cox Family on 5/31/2016.
 */
public class FieldsObject {

    List<FarmField> mFields;
    String mUsername;

    public FieldsObject(String theUsername){
        mFields  = new ArrayList<>();
        mUsername = theUsername;
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


    public void addField(int thePosition, String theCrop, long theSystemTime){
        FarmField theField = new FarmField(thePosition, theCrop, theSystemTime);
        this.mFields.add(theField);
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
    public long getSystemTime(int index) {
        return this.mFields.get(index).mSystemTime;
    }

    public void setCrop(int index, String mCrop) {
        this.mFields.get(index).mCrop = mCrop;
    }
    public void setSystemTime(int index, long systemTime) {
        this.mFields.get(index).mSystemTime = systemTime;
    }

    private class FarmField{
        int mPosition;
        String mCrop;
        long mSystemTime;
        FarmField(int thePosition, String theCrop, long theSystemTime){
            mPosition = thePosition;
            mCrop = theCrop;
            mSystemTime = theSystemTime;
        }
    }
}
