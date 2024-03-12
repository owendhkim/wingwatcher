import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.BaseImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.*;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.optimize.listeners.TimeIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Brian Xicon
 */
public class BirdClassification {
    private static final String[] allowedExtensions = BaseImageLoader.ALLOWED_FORMATS;
    private static final long seed = 12345;
    private static final Random randNumGen = new Random(seed);
    private static final int height = 224;
    private static final int width = 224;
    private static final int channels = 3;
    public static String dataLocalPath;

    public static void main(String[] args) throws Exception {
        dataLocalPath = "D:\\xmenz\\Desktop\\birdsnap\\download\\images10";
        File parentDir = new File(dataLocalPath, "ImagePipeline/");
        FileSplit filesInDir = new FileSplit(parentDir, allowedExtensions, randNumGen);
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
        BalancedPathFilter pathFilter = new BalancedPathFilter(randNumGen, allowedExtensions, labelMaker);
        InputSplit[] filesInDirSplit = filesInDir.sample(pathFilter, 70, 30);
        InputSplit trainData = filesInDirSplit[0];
        InputSplit testData = filesInDirSplit[1];

        ImageRecordReader recordReader = new ImageRecordReader(height, width, channels, labelMaker);

        ImageTransform transform = new MultiImageTransform(randNumGen,
                new CropImageTransform(5),               // Crop a 5-pixel border from all sides
                new FlipImageTransform(randNumGen),      // Randomly flip images horizontally
                new ScaleImageTransform(5),              // Randomly scale images by up to 5%
                new RotateImageTransform(randNumGen, 15),// Rotate images up to +/- 15 degrees
                new WarpImageTransform(10)         // Warp the image randomly
        );

        // Initialize ImageRecordReader with the training data and transformations
        recordReader.initialize(trainData, transform);

        // Get the number of labels from the record reader
        int outputNum = recordReader.numLabels();

        // Define batch size and label index for mini-batch training
        int batchSize = 10;
        int labelIndex = 1;

        // Create a DataSetIterator to feed the preprocessed data to the network
        DataSetIterator trainIter = new RecordReaderDataSetIterator(recordReader, batchSize, labelIndex, outputNum);

        // Data normalization scaler
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        scaler.fit(trainIter);
        trainIter.setPreProcessor(scaler);

        // Network configuration
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Adam(.0005))
                .list()
                .layer(0, new Convolution2D.Builder()
                        .nIn(channels)
                        .nOut(32)
                        .activation(Activation.RELU)
                        .weightInit(WeightInit.XAVIER)
                        .kernelSize(3, 3)
                        .stride(1, 1)
                        .build())
                .layer(1, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(2, new Convolution2D.Builder()
                        .nOut(64)
                        .activation(Activation.RELU)
                        .weightInit(WeightInit.XAVIER)
                        .kernelSize(3, 3)
                        .stride(1, 1)
                        .build())
                .layer(3, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(4, new DenseLayer.Builder()
                        .nOut(128)
                        .activation(Activation.RELU)
                        .build())
                //.layer(5, new DropoutLayer(0.5))
                .layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(outputNum)
                        .activation(Activation.SOFTMAX)
                        .build())
                .setInputType(InputType.convolutionalFlat(height, width, channels))
                .build();

        // Initialize the network with the configuration
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        // Set listeners
        model.setListeners(new ScoreIterationListener(10), new TimeIterationListener(1));

        System.out.println("Training Neural Net");
        // Train the network on the training data
        for (int i = 0; i < 200; i++) { // Epochs
            trainIter.reset();
            model.fit(trainIter);

            // Evaluate on Training Data
            recordReader.reset();
            recordReader.initialize(trainData);
            trainIter = new RecordReaderDataSetIterator(recordReader, batchSize, labelIndex, outputNum);
            trainIter.setPreProcessor(scaler);
            Evaluation trainEval = model.evaluate(trainIter);
            System.out.println("Epoch " + (i + 1) + " Training stats: ");
            System.out.println(trainEval.stats());

            // Evaluate on Test Data
            recordReader.reset();
            recordReader.initialize(testData);
            DataSetIterator testIter = new RecordReaderDataSetIterator(recordReader, batchSize, labelIndex, outputNum);
            testIter.setPreProcessor(scaler); // Apply the same preprocessor as training
            Evaluation testEval = model.evaluate(testIter);
            System.out.println("Epoch " + (i + 1) + " Testing stats: ");
            System.out.println(testEval.stats());
        }

        System.out.println("Saving the Model");
//        // Evaluate the network on the test set
//        recordReader.reset();
//        recordReader.initialize(testData);
//        DataSetIterator testIter = new RecordReaderDataSetIterator(recordReader, batchSize, labelIndex, outputNum);
//        scaler.fit(testIter);
//        testIter.setPreProcessor(scaler);
//        Evaluation eval = model.evaluate(testIter);
//
//        // Print evaluation statistics
//        System.out.println(eval.stats());

        // Save the model
        File locationToSave = new File("BirdClassifier10.zip");
        boolean saveUpdater = true; // Preserves the updater state
        ModelSerializer.writeModel(model, locationToSave, saveUpdater);
    }
}
