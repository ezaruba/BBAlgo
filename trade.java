import java.time.LocalDate;
import java.util.Date;

/**
 * Created by dim on 6/14/2017.
 */
public class trade {
    int id;
    String instrument;
    LocalDate openDate;
    double openPrice;
    LocalDate closeDate;
    double closePrice;
    int amount;
    boolean open;
    boolean isLong;
    public trade(int idIn, String instrumentIn, LocalDate openDateIn, double openPriceIn, int amountIn, boolean openIn, boolean isLongIn){
        this.id = idIn;
        this.instrument = instrumentIn;
        this.openDate = openDateIn;
        this.openPrice = openPriceIn;
        this.closePrice = 0;
        this.amount = amountIn;
        this.open = openIn;
        this.isLong = isLongIn;
    }

    public void closeOrder(LocalDate closeDateIn ,double closePriceIn){
        this.closeDate = closeDateIn;
        this.closePrice = closePriceIn;
       setOpen();
    }
    private void setClosePrice(double closePriceIn){
        closePrice = closePriceIn;
        open = false;
    }

    public LocalDate getOpenDate(){
        return this.openDate;
    }
    public LocalDate getCloseDate(){
        return this.closeDate;
    }

    public boolean getOpen(){
        return open;
    }
    public void setOpen(){
        open = false;
    }
    public String toString(){
        return "//TRADE/// ID " + this.id +" Open Date "+ openDate +" Open Price " + this.openPrice + " Close Date " + closeDate + " close Price " + this.closePrice ;
    }
}
