import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.util.ModelSerializer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.io.File;

public class ClassificationTester {
    public static void main(String[] args) throws Exception {
        String modelLoadPath = "BirdClassifier10.zip";
        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(modelLoadPath);

        // Load and prepare the image you want to classify
        File imageFile = new File("D:\\xmenz\\Desktop\\birdsnap\\download\\images\\Bald_Eagle\\25672.jpg"); // Replace with the path to your image

        ImageToInput imagetoInput = new ImageToInput(224, 224, 3);
        INDArray image = imagetoInput.prepare(imageFile);

        // Feed the image to the model for classification
        INDArray output = model.output(image);

        // Get the predicted class label
        int predictedClass = output.argMax(1).getInt(0);
        System.out.println("Predicted Class Index: " + predictedClass);


        // String[] classNames = { "Class1", "Class2", ... }; // Define your class labels
        // String predictedLabel = classNames[predictedClass];
        // System.out.println("Predicted Class: " + predictedLabel);
    }
}
