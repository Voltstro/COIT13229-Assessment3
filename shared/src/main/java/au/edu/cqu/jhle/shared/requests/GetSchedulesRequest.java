package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.DeliverySchedule;

import java.util.ArrayList;

public class GetSchedulesRequest extends Request {
    private ArrayList<DeliverySchedule> deliverySchedulesList;

    public GetSchedulesRequest() {
    }

    public ArrayList<DeliverySchedule> getDeliverySchedulesList() {
        return deliverySchedulesList;
    }

    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            //Fetch schedules list from DB
            deliverySchedulesList = databaseUtility.getDeliverySchedules();
        } catch (Exception ex) {
            setErrorMessage("Could not get schedules from database!\n" + ex.getMessage());
            return;
        }

        setValid(true);
    }
}
