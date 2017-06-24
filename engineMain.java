/**
 * Created by dim on 4/17/2017.
 */
import eu.verdelhan.ta4j.TimeSeries;

public class engineMain {
    public static void main(String[] args) throws Exception{

        String ticker ="NFLX";
        String beginYear= "Jan+01%2C+2015";

        //Creates a timeseries
        http.getHTML(ticker,beginYear);
        TimeSeries series = CsvTicksLoader.loadSeries(ticker);
        //initializes and runs the Neural Network Strategy
        ANNStrategy test = new ANNStrategy(series);


    }
}
