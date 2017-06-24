import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import org.jfree.ui.RefineryUtilities;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.SupervisedHebbianNetwork;
import org.neuroph.nnet.learning.*;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.data.norm.DecimalScaleNormalizer;
import org.neuroph.util.data.norm.MaxMinNormalizer;
import org.neuroph.util.data.norm.RangeNormalizer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by dim on 5/22/2017.
 */
public class ANNStrategy implements LearningEventListener {
    NeuralNetwork<?> neuralNet;
    DataSet trainingSet = new DataSet(5, 1);

    simpleOrder history = new simpleOrder();

    public  ANNStrategy(TimeSeries series) throws ParseException {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }
        ClosePriceIndicator close = new ClosePriceIndicator(series);
        normalize dec = new normalize();

        for (int i = 0; i < series.getEnd(); i++) {
            //System.out.println("Tick " + i+ " "+series.getTick(i));
            double[] inputStockPrice = new double[5];
            double[] desiredOutputPrice = new double[1];
            if(i<0){
                System.out.println("Skip");
            }else {
                double outP = 0;

                double doubleValue = new Double(String.valueOf(close.getValue(i - 5)));
                double doubleValue2 = new Double(String.valueOf(close.getValue(i - 4)));
                double doubleValue3 = new Double(String.valueOf(close.getValue(i - 3)));
                double doubleValue4 = new Double(String.valueOf(close.getValue(i - 2)));
                double doubleValue5 = new Double(String.valueOf(close.getValue(i - 1)));

                double desired = new Double(String.valueOf(close.getValue(i)));

                inputStockPrice[0] = doubleValue;
                inputStockPrice[1] = doubleValue2;
                inputStockPrice[2] = doubleValue3;
                inputStockPrice[3] = doubleValue4;
                inputStockPrice[4] = doubleValue5;

                desiredOutputPrice[0] = desired;

                trainingSet.addRow(inputStockPrice, desiredOutputPrice);
                //System.out.println("data " + trainingSet.get(i));
            }
        }

        dec.normalize(trainingSet);

        train();
        ArrayList<Double> outputlst = new ArrayList<Double>();
        for (int i = 0; i < series.getEnd(); i++) {
            DataSetRow trainingElement = trainingSet.getRowAt(i);
            neuralNet.setInput(trainingElement.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();
            double outRef =  dec.scaleUp(networkOutput[0]);
            outputlst.add(outRef);
            double[] desiredOutVal = trainingElement.getDesiredOutput();
            double difference = outRef - close.getValue(i).toDouble()  ;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            LocalDate localDate = LocalDate.parse(series.getTick(i).getSimpleDateName(), formatter);
            System.out.println("//SERIES//" + series.getTick(i)+  " predicted out  " +outRef + " Difference " + difference);
            try {
                if (outRef > outputlst.get(outputlst.size() - 2) && history.tradeActive() != true) {
                    history.addOrder(series.getName(),localDate, close.getValue(i).toDouble(),100, true, true);
                   // history.printOrderLast();

                } else {
                    history.closeOrder(localDate,close.getValue(i).toDouble());
                    //history.printOrderLast();

                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
        }
        backtestResults results = new backtestResults();
        results.run(history.retList());
        createGUI(series,outputlst,history.retList());
    }
    public void train() {

        // create MultiLayerPerceptron neural network
        neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 5,21, 21, 1);

        BackPropagation learningRule = (BackPropagation)neuralNet.getLearningRule();
        ((BackPropagation) neuralNet.getLearningRule()).setMaxError(0.00001);
        ((BackPropagation) neuralNet.getLearningRule()).setLearningRate(0.01);

        ((BackPropagation) neuralNet.getLearningRule()).setMaxIterations(2000);
        learningRule.addListener(this);


        // train the network with training set
        neuralNet.learn(trainingSet);
        System.out.println("Done training.");
    }
    public void createGUI(TimeSeries series,ArrayList<Double> ANNoutIn,ArrayList<trade> orders){
        backtestGUI chart = new backtestGUI("Stock ANN" , "StockANN",series,ANNoutIn,orders);

        chart.pack( );
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
    }

    public void handleLearningEvent(LearningEvent event) {
        SupervisedLearning rule = (SupervisedLearning)event.getSource();
        rule.setMaxError(0.000001);
        System.out.println( "Training, Network Epoch " + rule.getCurrentIteration() + ", Error:" + rule.getTotalNetworkError());
    }


}
