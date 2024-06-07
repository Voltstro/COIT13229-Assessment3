package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.OrderLine;
import java.util.List;

public class GetOrderLinesForOrderRequest extends Request {
    public GetOrderLinesForOrderRequest(int orderId) {
        this.orderId = orderId;
    }
    
    public List<OrderLine> getOrderLinesList() {
        return orderLinesList;
    }
    
    public void setOrderLinesList(List<OrderLine> orderLinesList) {
        this.orderLinesList = orderLinesList;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    private List<OrderLine> orderLinesList;
    
    private int orderId;
    
    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            //Fetch order lines from db
            orderLinesList = databaseUtility.getOrderLinesForOrder(orderId);
        } catch (Exception ex) {
            setErrorMessage("Could not get order lines from database\n" + ex.getMessage());
            return;
        }
        
        setValid(true);
    }
}
