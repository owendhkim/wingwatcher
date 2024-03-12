import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.io.File;
import java.io.IOException;

public class ImageToInput {
    private NativeImageLoader loader;
    private DataNormalization scaler;
    private int height;
    private int width;
    private int channels;

    public ImageToInput(int height, int width, int channels) {
        this.height = height;
        this.width = width;
        this.channels = channels;
        loader = new NativeImageLoader(height, width, channels);
        scaler = new ImagePreProcessingScaler(0, 1);
    }

    public INDArray prepare(File imageFile) throws IOException {
        INDArray image = loader.asMatrix(imageFile);

        // Normalize the image data
        scaler.transform(image);

        // Flatten the image data to a 1D vector
        image = image.reshape(1, height * width * channels);

        return image;
    }
}
