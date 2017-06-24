import org.apache.commons.math3.stat.StatUtils;

import java.util.ArrayList;

/**
 * Created by dim on 6/14/2017.
 */
public class backtestResults {
    double pnl;
ArrayList<Double> pnlArr = new ArrayList<Double>();
double sum;

    public void run(ArrayList<trade> orders){
        if(orders.get(orders.size()-1).getCloseDate()==(null)){
            orders.remove(orders.size()-1);
        }
        int winTrades = 0;
        int loseTrades = 0;
        for(int i =0;i<orders.size();i++){
            pnl = (orders.get(i).closePrice - orders.get(i).openPrice) * orders.get(i).amount;
            System.out.println(orders.get(i).toString()+" "+ pnl);
            pnlArr.add(pnl);
            if(pnl>0){
                winTrades++;
            }
            else{
                loseTrades++;
            }
        }
        for (double i : pnlArr)
            sum += i;
        double[] arr = pnlArr.stream().mapToDouble(i -> i).toArray();

        double sharpe = StatUtils.mean(arr) / Math.sqrt(StatUtils.variance(arr)) * Math.sqrt(250);

        System.out.println("total PNL " + sum + "\nWinning Trades " + winTrades + "\nLose Trades " + loseTrades + "\nSharpe " +sharpe);
    }


}
