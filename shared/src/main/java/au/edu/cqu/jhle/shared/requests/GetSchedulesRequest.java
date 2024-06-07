package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.DeliverySchedule;
import java.util.LinkedList;

public class GetSchedulesRequest extends Request {
    public GetSchedulesRequest() {}
    
    private LinkedList<DeliverySchedule> deliverySchedulesList; 
    
    public LinkedList<DeliverySchedule> getDeliverySchedulesList() {
        return deliverySchedulesList;
    }
    
    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            //Fetch schedules list from DB
            deliverySchedulesList = databaseUtility.getDeliverySchedules();
            setValid(true);
        } catch (Exception ex) {
            //TODO
        }
    }
}
