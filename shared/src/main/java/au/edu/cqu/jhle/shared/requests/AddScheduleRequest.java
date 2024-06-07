package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.DeliverySchedule;

public class AddScheduleRequest extends Request {
    private DeliverySchedule schedule;

    public AddScheduleRequest(DeliverySchedule schedule) {
        this.schedule = schedule;
    }

    public DeliverySchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(DeliverySchedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            databaseUtility.upsertDeliverySchedule(schedule);
        } catch (Exception ex) {
            setErrorMessage("Failed to upsert schedule!\n" + ex.getMessage());
            return;
        }

        setValid(true);
    }
}
