package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.Order;

public class GetOrderByIdRequest extends Request {
    private int id;
    private Order order;

    public GetOrderByIdRequest(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            order = databaseUtility.getOrderById(id);
        } catch (Exception ex) {
            setErrorMessage("Could not get order from database\n" + ex.getMessage());
            return;
        }

        setValid(true);
    }
}
