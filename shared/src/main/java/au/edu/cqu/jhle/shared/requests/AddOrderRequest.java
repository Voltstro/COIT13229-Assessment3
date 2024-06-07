package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.Order;

public class AddOrderRequest extends Request {
    private Order order;
    private int id;

    public AddOrderRequest(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            id = databaseUtility.upsertOrder(order);
        } catch (Exception ex) {
            setErrorMessage("Failed to upsert order\n" + ex.getMessage());
            return;
        }

        setValid(true);
    }
}
