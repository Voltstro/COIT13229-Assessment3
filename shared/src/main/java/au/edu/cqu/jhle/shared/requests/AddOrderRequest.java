package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.Order;

public class AddOrderRequest extends Request {
    public AddOrderRequest(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    
    private Order order;
    
    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            databaseUtility.upsertOrder(order);
        } catch (Exception ex) {
            setErrorMessage("Failed to upsert order\n" + ex.getMessage());
            return;
        }
        
        setValid(true);
    }
}