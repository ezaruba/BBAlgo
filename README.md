# BBAlgo
This is a general purpose lightweight backtest trading engine using an artificial neural network for stocks, written in modern Java 8.

This artificial neural network uses a multi layer perceptron in a format of  5 input neurons 2 hidden layers with 21 neurons each, and 1 output layer. The learning method is backpropagation. A visual of the Neural Network can be found here: 

![NNVisual](https://github.com/magicaltoaster/BBAlgo/blob/master/NN/NN%20visualized.PNG) 

The engine uses the HTTP get class to download a CSV file, feed the values into an array, and then normalize the data into a decimal scale. From there an activation function used to transform the activation level of neuron (weighted sum of inputs) to an 
output signal: 
![Activation](https://github.com/magicaltoaster/BBAlgo/blob/master/NN/Activation.PNG)

Then the trade engine determines if the output should trigger a trade. When finished iterating through the time series it takes the prediction values and time series and maps it to a chart

![chart](https://github.com/magicaltoaster/BBAlgo/blob/master/NN/NFLXANN.PNG)

The Neural Network can be quite profitable however the difficulty is getting the network to predict discrepancies in future market stucture.

This is a side project and I'm not planning to extend this further.

## License

MIT
