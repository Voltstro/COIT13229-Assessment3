package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.Order;
import java.util.List;

public class GetOrdersRequest extends Request {
    public GetOrdersRequest() {}
    
    public GetOrdersRequest(List<Order> orderList) {
        this.orderList = orderList;
    }
    
    public List<Order> getOrderList() {
        return orderList;
    }
    
    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
    
    private List<Order> orderList;
    
    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            orderList = databaseUtility.getOrders();
        } catch (Exception ex) {
            setErrorMessage("Could not get orders from database!\n" + ex.getMessage());
            return;
        }
        
        setValid(true);
    }
}
