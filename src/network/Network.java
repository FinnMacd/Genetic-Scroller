package network;

import java.util.Arrays;
import java.util.Random;

import math.Matrix;
import math.MatrixMath;
import math.Sigmoid;

public class Network {
	
	public static Random random = new Random();
	
	private int inputSize, hiddenSize, outputSize;
	
	private Matrix inputWeights, inputBiases, hiddenWeights, hiddenBiases;
	
	private int score = 0, id = 0;
	
	private double startLimit = 1;
	
	public Network(int inputNeurons, int hiddenNeurons, int outputNeurons){
		
		inputWeights = new Matrix(inputNeurons,hiddenNeurons);
		inputBiases = new Matrix(1, hiddenNeurons);
		
		hiddenWeights = new Matrix(hiddenNeurons,outputNeurons);
		hiddenBiases = new Matrix(1, outputNeurons);
		
		inputWeights.randomize(-startLimit, startLimit);
		inputBiases.randomize(-startLimit, startLimit);
		
		hiddenWeights.randomize(-startLimit, startLimit);
		hiddenBiases.randomize(-startLimit, startLimit);
		
		inputSize = inputNeurons;
		hiddenSize = hiddenNeurons;
		outputSize = outputNeurons;
		
	}
	
	public Network(String s){
		
		String[] chars = s.split(" ");
		
		inputSize = Integer.parseInt(chars[0]);
		hiddenSize = Integer.parseInt(chars[1]);
		outputSize = Integer.parseInt(chars[2]);
		
		int i = 3;
		
		double[][] inputWeightA = new double[inputSize][hiddenSize], inputBiasA = new double[1][hiddenSize], 
				   hiddenWeightA = new double[hiddenSize][outputSize], hiddenBiasA = new double[1][outputSize];
		
		int temp = i;
		
		for(;i < temp + inputSize * hiddenSize; i++){
			
			int x = (i - temp)/inputSize, y = (i-temp)%inputSize;
			
			inputWeightA[y][x] = Double.parseDouble(chars[i]);
			
		}
		
		temp = i;
		
		for( ;i < temp + outputSize * hiddenSize; i++){
			
			int x = (i - temp)/hiddenSize, y = (i-temp)%hiddenSize;
			
			hiddenWeightA[y][x] = Double.parseDouble(chars[i]);
			
		}
		
		temp = i;
		
		for( ;i < temp + hiddenSize; i++){
			
			int x = i - temp, y = 0;
			
			inputBiasA[y][x] = Double.parseDouble(chars[i]);
			
		}
		
		temp = i;
		
		for( ;i < temp + outputSize; i++){
			
			int x = i - temp, y = 0;
			
			hiddenBiasA[y][x] = Double.parseDouble(chars[i]);
			
		}
		
		inputWeights = new Matrix(inputWeightA);
		inputBiases = new Matrix(inputBiasA);
		
		hiddenWeights = new Matrix(hiddenWeightA);
		hiddenBiases = new Matrix(hiddenBiasA);
		
	}
	
	public Network(int inputNeurons, int hiddenNeurons, int outputNeurons, double[] data) {
		
		inputWeights = new Matrix(inputNeurons,hiddenNeurons, Arrays.copyOfRange(data, 0, inputNeurons*hiddenNeurons));
		inputBiases = new Matrix(1, hiddenNeurons, Arrays.copyOfRange(data, inputNeurons*hiddenNeurons, hiddenNeurons*(inputNeurons+1)));
		
		hiddenWeights = new Matrix(hiddenNeurons,outputNeurons, Arrays.copyOfRange(data, hiddenNeurons*(inputNeurons+1), hiddenNeurons*(inputNeurons+1) + hiddenNeurons*outputNeurons));
		hiddenBiases = new Matrix(1, outputNeurons, Arrays.copyOfRange(data, hiddenNeurons*(inputNeurons+1) + hiddenNeurons*outputNeurons, hiddenNeurons*(inputNeurons+1) + hiddenNeurons*(outputNeurons+1)));
		
		inputSize = inputNeurons;
		hiddenSize = hiddenNeurons;
		outputSize = outputNeurons;
		
	}
	
	public Matrix[] test(Matrix inputs){
		
		Matrix delta1 = MatrixMath.add(MatrixMath.multiply(inputs, inputWeights), inputBiases);
		
		Matrix delta2 = MatrixMath.add(MatrixMath.multiply(Sigmoid.sigmoid(delta1), hiddenWeights), hiddenBiases);
		
		return new Matrix[]{delta1, delta2,(Sigmoid.sigmoid(delta2))};
		
	}
	
	public Matrix simpleTest(Matrix inputs){
		
		Matrix delta1 = MatrixMath.add(MatrixMath.multiply(inputs, inputWeights), inputBiases);
		
		Matrix delta2 = MatrixMath.add(MatrixMath.multiply(Sigmoid.sigmoid(delta1), hiddenWeights), hiddenBiases);
		
		return (Sigmoid.sigmoid(delta2));
		
	}
	
	private double sigs(double input){
		
		int pow = (int)Math.log10(input);
		
		input = (input/Math.pow(10, pow))*1000;
		
		input = (int)input;
		
		input = (input/1000)*Math.pow(10, pow);
		
		return input;
		
	}
	
	public String toString(){
		
		String output = inputSize + " " + hiddenSize + " " + outputSize + " ";
		
		
		for(double d:inputWeights.getSingleArray()){
			output += sigs(d) + " ";
		}
		
		for(double d:hiddenWeights.getSingleArray()){
			output += sigs(d) + " ";
		}
		
		for(double d:inputBiases.getSingleArray()){
			output += sigs(d) + " ";
		}
		
		for(double d:hiddenBiases.getSingleArray()){
			output += sigs(d) + " ";
		}
		
		return output;
		
	}
	
	public double[] toDoubleArry() {
		
		double[] ret = new double[hiddenSize*(inputSize + 1) + outputSize*(hiddenSize + 1)];
		
		double[] inputW = inputWeights.getSingleArray();
		int i = 0;
		for(; i < inputW.length; i++) {
			ret[i] = inputW[i];
		}
		double[] inputB = inputBiases.getSingleArray();
		int temp = i;
		for(; i < temp + inputB.length; i++) {
			ret[i] = inputB[i-temp];
		}
		double[] hiddenW = hiddenWeights.getSingleArray();
		temp = i;
		for(; i < temp + hiddenW.length; i++) {
			ret[i] = hiddenW[i-temp];
		}
		double[] hiddenB = hiddenBiases.getSingleArray();
		temp = i;
		for(; i < temp + hiddenB.length; i++) {
			ret[i] = hiddenB[i-temp];
		}
		
		return ret;
		
	}
	
	public Network mutate() {
		
		double[] data = toDoubleArry();
		
		for(int i = 0; i < data.length * 0.05 * random.nextDouble() + 1; i++) {
			int t = (int)(random.nextDouble()*data.length);
			data[t] = data[t] * (random.nextDouble()-0.5) * 4;
		}
		
		return new Network(inputSize,hiddenSize,outputSize,data);
	}
	
	public void setScore(int i) {
		score = i;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
}
