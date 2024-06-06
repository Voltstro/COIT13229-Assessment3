package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;

import java.io.Serializable;

public abstract class Request implements Serializable {
    private boolean isValid;

    private String errorMessage;

    public abstract void doRequest(DatabaseUtility databaseUtility);

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
