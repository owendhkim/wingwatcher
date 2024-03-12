import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.optimize.listeners.TimeIterationListener;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Xicon
 */
public class BirdClassification {
    public static void main(String[] args) {

        // Set image dimensions (Might not have to)
        int height = 64;
        int width = 64;
        // Use 3 channels for colored images
        int channels = 3;

        // Define the neural network architecture
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(123)//Set random seed for reproducibility
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)//Uses stochastic gradient descent as the optimization algorithm
                .list() //Create a list to define the layers of the neural network
                .layer(0, new ConvolutionLayer.Builder()
                        .nIn(channels) //Number of input channels (3 for color)
                        .nOut(32) //Number of output features for this convolutional layer
                        .weightInit(WeightInit.XAVIER) //Initialize weights using Xavier initialization
                        .activation(Activation.RELU) //Uses ReLU as the activation function
                        .build())
                .layer(1, new DenseLayer.Builder()
                        .nIn(32) // Set nIn to match the number of output features of the previous layer
                        .nOut(128) // Adjust the number of output features
                        .weightInit(WeightInit.XAVIER) //Initialize weights using Xavier
                        .activation(Activation.RELU) //Uses ReLU as the activation function
                        .build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(128) // Set nIn to match the number of output features of the previous layer
                        .nOut(500) // Specify the number of bird classes
                        .weightInit(WeightInit.XAVIER) //Initialize weights using Xavier
                        .activation(Activation.SOFTMAX) //Uses SoftMax as the activation function (since it is the final layer)
                        .build())
                .build();



        // Create the neural network model
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(1000), new TimeIterationListener(1000));

        System.out.println("Now loading in data...");
        // Load training and testing data
        List<INDArray> trainingData = loadAndPreprocessImagesFromFile("D:\\xmenz\\Desktop\\MS317\\ms_317\\Backend\\Bird_Classification\\train_paths.txt", height, width, channels);
        List<INDArray> testingData = loadAndPreprocessImagesFromFile("D:\\xmenz\\Desktop\\MS317\\ms_317\\Backend\\Bird_Classification\\test_paths.txt", height, width, channels);

        System.out.println("Now defining labels...");
        // Define the labels for training and testing data
        int[] trainingLabels = loadLabelsFromFile("D:\\xmenz\\Desktop\\MS317\\ms_317\\Backend\\Bird_Classification\\train_labels.txt");
        int[] testingLabels = loadLabelsFromFile("D:\\xmenz\\Desktop\\MS317\\ms_317\\Backend\\Bird_Classification\\test_labels.txt");

        System.out.println("Now training neural network...");
        // Train the model
        trainModel(model, trainingData, trainingLabels);

        // Evaluate the model
        evaluateModel(model, testingData, testingLabels);

        // Make predictions
        INDArray sampleInput = preprocess("D:\\xmenz\\Desktop\\birdsnap\\download\\temp\\American_Crow\\185954.jpg", height, width, channels);
        int predictedClass = classifySample(model, sampleInput);
        System.out.println("Predicted class: " + predictedClass);
    }

    // Load labels from a text file
    private static int[] loadLabelsFromFile(String filename) {
        List<Integer> labels = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = reader.readLine()) != null) {
                int label = Integer.parseInt(line);
                labels.add(label);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO
        }

        int[] labelArray = new int[labels.size()];
        for (int i = 0; i < labels.size(); i++) {
            labelArray[i] = labels.get(i);
        }

        return labelArray;
    }

    // Load and preprocess images from a text file
    private static List<INDArray> loadAndPreprocessImagesFromFile(String filePath, int height, int width, int channels) {
        List<INDArray> preprocessedImages = new ArrayList();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                INDArray preprocessedImage = preprocess(line, height, width, channels);

                if (preprocessedImage != null) {
                    preprocessedImages.add(preprocessedImage);
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO
        }

        return preprocessedImages;
    }

    // Train the model
    private static void trainModel(MultiLayerNetwork model, List<INDArray> trainingData, int[] trainingLabels) {
        List<DataSet> dataSetList = new ArrayList<>();
        for (int i = 0; i < trainingData.size(); i++) {
            INDArray features = trainingData.get(i);
            INDArray labels = Nd4j.create(1, 500); // Create label array with rank 2
            labels.putScalar(trainingLabels[i], 1.0); // Set the correct class to 1
            dataSetList.add(new DataSet(features, labels));
        }

        ListDataSetIterator trainData = new ListDataSetIterator(dataSetList, 16);
        model.fit(trainData);
    }

    // Evaluate the model
    private static void evaluateModel(MultiLayerNetwork model, List<INDArray> testingData, int[] testingLabels) {
        List<DataSet> dataSetList = new ArrayList<>();
        for (int i = 0; i < testingData.size(); i++) {
            INDArray features = testingData.get(i);
            INDArray labels = Nd4j.create(testingLabels[i]);
            dataSetList.add(new DataSet(features, labels));
        }

        ListDataSetIterator testData = new ListDataSetIterator(dataSetList, 16);

        Evaluation evaluation = model.evaluate(testData);
        System.out.println("Evaluation metrics: " + evaluation.stats());
    }

    // Classify a sample input with the model
    private static int classifySample(MultiLayerNetwork model, INDArray sampleInput) {
        INDArray output = model.output(sampleInput);

        // Find the index with the highest probability
        int predictedClass = Nd4j.argMax(output, 1).getInt(0);

        return predictedClass;
    }


    // Preprocess the specified image
    private static INDArray preprocess(String filename, int height, int width, int channels) {
        try {
            NativeImageLoader loader = new NativeImageLoader(height, width, channels);
            INDArray image = loader.asMatrix(new File(filename));

            if(image == null || image.isEmpty())
            {
                return null;
            }

            ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);
            scaler.transform(image);

            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // TODO
        }
    }
}
