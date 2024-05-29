package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseManager;

import java.io.Serializable;

public interface IRequest extends Serializable {
    void doRequest(DatabaseManager databaseManager);
}
