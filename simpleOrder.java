import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class simpleOrder  {

    trade trd;
    private static ArrayList<trade> orders = new ArrayList<trade>();
    public int index=0;

    public void addOrder(String instrumentIn, LocalDate openDate, double openPriceIn, int amountIn, boolean openIn, boolean isLongIn) {
        trd = new trade(index,instrumentIn,openDate,openPriceIn,amountIn,openIn,isLongIn);
        orders.add(trd);
        index++;
    }

    public boolean tradeActive(){
        if(!orders.isEmpty()) {
            if (orders.get(orders.size()-1).getOpen() == true) {
                return true;
            }
            else
                return false;
        }
        return false;
    }
    public void closeOrder(LocalDate closeDateIn, double closePriceIn){
        orders.get(orders.size()-1).closeOrder(closeDateIn,closePriceIn);
    }

    public void printOrderLast(){

            System.out.println(orders.get(orders.size()-1));

    }
     public ArrayList<trade> retList(){
        return orders;
     }


}
