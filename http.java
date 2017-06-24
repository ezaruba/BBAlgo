import au.com.bytecode.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Stack;

public class http {

    public static String getHTML(String ticker, String beginYear) throws Exception {
        String strURL = String.format("http://www.google.com/finance/historical?q=NASDAQ:%s&startdate=%s&output=csv",ticker,beginYear);
        Stack<String> st = new Stack();
        StringBuilder result = new StringBuilder();
        URL url = new URL(strURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            if(line.contains("Date"))
                System.out.println("ignore");
            else {
                //System.out.println(line);
                //String[] items = line.split(",");

                st.push(line);
            }
        }
        rd.close();
        String csv = String.format("%s.csv",ticker);
        CSVWriter writer = new CSVWriter(new FileWriter(csv), ',', CSVWriter.NO_QUOTE_CHARACTER);        while(!st.empty()) {
            String StackOut = st.pop();
            //System.out.println(StackOut);
            writer.writeNext(StackOut);

        }
        writer.close();
        return result.toString();
    }


}