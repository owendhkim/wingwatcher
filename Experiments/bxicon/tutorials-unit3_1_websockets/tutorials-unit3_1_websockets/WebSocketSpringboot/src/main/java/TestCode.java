import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Base64;

/**
 * @author Brian Xicon
 */
public class TestCode {
    public static void main(String[] args) {
        // Define the path to your image file
        File imageFile = new File("D:\\xmenz\\Desktop\\birdsnap\\download\\images\\American_Crow\\185773.jpg");

        try {
            // Convert the image file to a ByteBuffer
            ByteBuffer imageBuffer = fileToByteBuffer(imageFile);

            // Convert the ByteBuffer to a base64 string
            String base64Image = byteBufferToBase64(imageBuffer);

            // Print the base64 string
            System.out.println(base64Image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Convert a file to a ByteBuffer
    public static ByteBuffer fileToByteBuffer(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             FileChannel channel = fis.getChannel()) {
            int fileSize = (int) channel.size();
            ByteBuffer buffer = ByteBuffer.allocate(fileSize);
            channel.read(buffer);
            buffer.flip(); // Set position to the beginning of the buffer
            return buffer;
        }
    }

    // Convert a ByteBuffer to a base64 string
    public static String byteBufferToBase64(ByteBuffer buffer) {
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        return Base64.getEncoder().encodeToString(data);
    }
}
