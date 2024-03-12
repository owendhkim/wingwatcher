import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.io.File;
import java.io.IOException;

public class ClassificationTesterNew {

    public static void main(String[] args) throws IOException {
        // Path to the saved model and the image to classify
        String modelFilename = "BirdClassifier10.zip";
        String imageToClassify = "D:\\xmenz\\Desktop\\birdsnap\\download\\images\\Bald_Eagle\\25672.jpg";

        // Load the saved model
        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(new File(modelFilename));

        // Use the same height, width, and channels as used in the training phase
        int height = 224;
        int width = 224;
        int channels = 3;

        // Load and preprocess the image
        NativeImageLoader loader = new NativeImageLoader(height, width, channels);
        INDArray image = loader.asMatrix(new File(imageToClassify));

        // Normalize the image
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);
        scaler.transform(image);

        // Pass the preprocessed image to the model
        INDArray output = model.output(image);

        // Get the index of the predicted class
        int predictedClassIndex = output.argMax(1).getInt(0);
        System.out.println("The image was predicted to be in class index: " + predictedClassIndex);
    }
}
