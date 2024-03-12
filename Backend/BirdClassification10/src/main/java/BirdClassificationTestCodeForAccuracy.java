import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.BaseImageLoader;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.*;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
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
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BirdClassificationTestCodeForAccuracy {
    private static final String[] allowedExtensions = BaseImageLoader.ALLOWED_FORMATS;
    private static final long seed = 12345;
    private static final Random randNumGen = new Random(seed);

    private static final int height = 50;
    private static final int width = 50;
    private static final int channels = 3;

    public static String dataLocalPath;

    public static void main(String[] args) throws Exception {
        dataLocalPath = "D:\\xmenz\\Desktop\\birdsnap\\download\\images10";

        File parentDir = new File(dataLocalPath, "ImagePipeline/");

        FileSplit filesInDir = new FileSplit(parentDir, allowedExtensions, randNumGen);

        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();

        BalancedPathFilter pathFilter = new BalancedPathFilter(randNumGen, allowedExtensions, labelMaker);

        InputSplit[] filesInDirSplit = filesInDir.sample(pathFilter, 80, 30);
        InputSplit trainData = filesInDirSplit[0];
        InputSplit testData = filesInDirSplit[1];

        ImageRecordReader recordReader = new ImageRecordReader(height, width, channels, labelMaker);

        ImageTransform transform = new MultiImageTransform(randNumGen, new ShowImageTransform("Display - before"));

        recordReader.initialize(trainData, transform);
        int outputNum = recordReader.numLabels();

        int batchSize = 10;
        int labelIndex = 1;

        DataSetIterator dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, labelIndex, outputNum);
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);

        List<DataSet> dataSets = new ArrayList<>();
        while (dataIter.hasNext()) {
            DataSet ds = dataIter.next();
            INDArray features = ds.getFeatures();
            long[] shape = features.shape();
            INDArray reshapedFeatures = features.reshape(shape[0], shape[1] * shape[2] * shape[3]);
            ds.setFeatures(reshapedFeatures);
            dataSets.add(ds);
        }

        DataSetIterator flattenedDataIter = new ListDataSetIterator<>(dataSets, dataSets.size());

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(Nesterovs.builder().learningRate(0.01).momentum(0.9).build())
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(height * width * channels)
                        .nOut(100)
                        .activation(Activation.RELU)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(100)
                        .nOut(outputNum)
                        .activation(Activation.SOFTMAX)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(1), new TimeIterationListener(1));

        int numEpochs = 100;  // Start with 1 epoch
        double lastAccuracy = 0.0; // Initialize the last accuracy

        while (true) {
            System.out.println("Training Neural Net for " + numEpochs + " epochs");
            for (int i = 0; i < numEpochs; i++) {
                model.fit(flattenedDataIter);
            }

            // Evaluate the model
            System.out.println("Evaluating Neural Net");
            Evaluation evaluation = new Evaluation(outputNum);

            recordReader.initialize(testData, transform);  // Use the same transform as during training

            DataSetIterator testIter = new RecordReaderDataSetIterator(recordReader, batchSize, labelIndex, outputNum);
            testIter.setPreProcessor(scaler);

            List<DataSet> evalDataSets = new ArrayList<>();

            while (testIter.hasNext()) {
                DataSet next = testIter.next();
                INDArray features = next.getFeatures();
                long[] shape = features.shape();
                INDArray reshapedFeatures = features.reshape(shape[0], shape[1] * shape[2] * shape[3]);
                next.setFeatures(reshapedFeatures);
                evalDataSets.add(next);
            }

            DataSetIterator flattenedEvalDataIter = new ListDataSetIterator<>(evalDataSets, evalDataSets.size());

            while (flattenedEvalDataIter.hasNext()) {
                DataSet next = flattenedEvalDataIter.next();
                INDArray output = model.output(next.getFeatures());
                evaluation.eval(next.getLabels(), output);
            }

            double accuracy = evaluation.accuracy();
            System.out.println("Accuracy after " + numEpochs + " epochs: " + accuracy);

            if (accuracy >= lastAccuracy -.05) {
                lastAccuracy = accuracy;
                numEpochs++; // Increase the number of epochs
            } else {
                break; // Exit the loop if accuracy starts decreasing
            }
        }

        System.out.println("Training stopped because accuracy started to decrease.");

        String modelSavePath = "BirdClassifier10.zip"; // Replace with your desired file path
        ModelSerializer.writeModel(model, modelSavePath, true);
        System.out.println("Model saved at: " + modelSavePath);
    }
}
