package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.OrderLine;

public class AddOrderLineRequest extends Request {
    private OrderLine orderLine;

    public AddOrderLineRequest(OrderLine orderLine) {
        this.orderLine = orderLine;
    }

    public OrderLine getOrderLine() {
        return this.orderLine;
    }

    public void setOrderLine(OrderLine orderLine) {
        this.orderLine = orderLine;
    }

    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            databaseUtility.upsertOrderLine(orderLine);
        } catch (Exception ex) {
            setErrorMessage("Failed to upsert order line\n" + ex.getMessage());
            return;
        }

        setValid(true);
    }
}
