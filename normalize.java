import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * Created by dim on 6/21/2017.
 */

/*
public class normalize {
     double lowLimit;
     double highLimit;
     double baseMin = 99999;
     double baseMax ;

    public normalize(double lowLimitIn, double highLimitIn) {
        this.lowLimit= lowLimitIn;
        this.highLimit = highLimitIn;


    }
    public void normalize(DataSet dataSet) {
        findMaxAndMin(dataSet);

        for (DataSetRow row : dataSet.getRows()) {
            double[] normalizedInput = scale(row.getInput());
            row.setInput(normalizedInput);

            if (dataSet.isSupervised()) {
                double[] normalizedOutput = scale(row.getDesiredOutput());
                row.setDesiredOutput(normalizedOutput);
            }

        }

    }


    public  double[] scale(double[] valueIn) {
        double[] normalizedValue = new double[valueIn.length];
        for (int i = 0; i < valueIn.length; i++) {

            normalizedValue[i] = ((highLimit - lowLimit) * (valueIn[i] - baseMin) / (baseMax - baseMin)) + lowLimit;
        }
            return normalizedValue;
    }


    public double scaleUp(double valIn){
        System.out.println("Basemin " + baseMin);
        System.out.println("basemax " + baseMax);
        System.out.println("fuck " + ((baseMax - baseMin) * (valIn - baseMin) / (baseMax - baseMin)) + baseMin);
        return  ((baseMax - baseMin) * (valIn - baseMin) / (baseMax - baseMin)) + baseMin;
    }


    private void findMaxAndMin(DataSet dataSet) {
        int inputSize = dataSet.getInputSize();
        int outputSize = dataSet.getOutputSize();



        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double[] input = dataSetRow.getInput();
            for (int i = 0; i < input.length; i++) {
                if (input[i] > baseMax) {
                    baseMax = input[i];

                }
                if (input[i]<baseMin) {
                    baseMin = input[i];
                }
            }

            double[] output = dataSetRow.getDesiredOutput();
            for (int i = 0; i < output.length; i++) {
                if (output[i] > baseMax) {
                    baseMax = output[i];
                }
                if (output[i] < baseMin) {
                    baseMin = output[i];
                }
            }



        }
        System.out.println("min " + baseMin);
        System.out.println("max " +baseMax);

    }
}

*/
public class normalize {
    private double[] maxIn, maxOut; // contains max values for all columns
    private  double[] scaleFactorIn, scaleFactorOut; // holds scaling values for all columns

    public void normalize(DataSet dataSet) {
        findMaxVectors(dataSet);
        findScaleVectors();

        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double[] normalizedInput = normalizeScale(dataSetRow.getInput(), scaleFactorIn);
            dataSetRow.setInput(normalizedInput);

            if (dataSet.isSupervised()) {
                double[] normalizedOutput = normalizeScale(dataSetRow.getDesiredOutput(), scaleFactorOut);
                dataSetRow.setDesiredOutput(normalizedOutput);
            }
        }
    }

    private void findMaxVectors(DataSet dataSet) {
        int inputSize = dataSet.getInputSize();
        int outputSize = dataSet.getOutputSize();

        maxIn = new double[inputSize];
        for (int i = 0; i < inputSize; i++) {
            maxIn[i] = Double.MIN_VALUE;
        }

        maxOut = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            maxOut[i] = Double.MIN_VALUE;
        }

        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double[] input = dataSetRow.getInput();
            for (int i = 0; i < inputSize; i++) {
                if (input[i] > maxIn[i]) {
                    maxIn[i] = input[i];
                }
            }

            double[] output = dataSetRow.getDesiredOutput();
            for (int i = 0; i < outputSize; i++) {
                if (output[i] > maxOut[i]) {
                    maxOut[i] = output[i];
                }
            }

        }
    }

    public void findScaleVectors() {
        scaleFactorIn = new double[maxIn.length];
        for (int i = 0; i < scaleFactorIn.length; i++) {
            scaleFactorIn[i] = 1;
        }

        for (int i = 0; i < maxIn.length; i++) {
            while (maxIn[i] > 1) {
                maxIn[i] = maxIn[i] / 10.0;
                scaleFactorIn[i] = scaleFactorIn[i] * 10;
            }
        }

        scaleFactorOut = new double[maxOut.length];
        for (int i = 0; i < scaleFactorOut.length; i++) {
            scaleFactorOut[i] = 1;
        }

        for (int i = 0; i < maxOut.length; i++) {
            while (maxOut[i] > 1) {
                maxOut[i] = maxOut[i] / 10.0;
                scaleFactorOut[i] = scaleFactorOut[i] * 10;
            }
        }


    }

    private double[] normalizeScale(double[] vector, double[] scaleFactor) {
        double[] normalizedVector = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = vector[i] / scaleFactor[i];
        }
        return normalizedVector;
    }

    public double scaleUp(double varIn){
        double scaledUpVector;

        scaledUpVector = varIn * scaleFactorOut[0];
        return scaledUpVector;
    }
}