package Tables.ClassificationWS;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.Map;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.awt.image.BufferedImage;

@ServerEndpoint("/classification/{username}")
@Component
public class ClassificationServer {
    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    // server side logger
    private final Logger logger = LoggerFactory.getLogger(ClassificationServer.class);

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session  represents the WebSocket session for the connected user.
     * @param username username specified in path parameter.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        // server side log
        logger.info("[onOpen] " + username);

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        } else {
            // map current session with username
            sessionUsernameMap.put(session, username);

            // map current username with session
            usernameSessionMap.put(username, session);
        }
    }

    /**
     * Handles incoming WebSocket messages from a client.
     *
     * @param session The WebSocket session representing the client's connection.
     * @param messageBuffer The image received from the client.
     */
    @OnMessage
    public void onImage(Session session, ByteBuffer messageBuffer) throws IOException {
        // get the username by session
        int predictedClass = -1;
        String username = sessionUsernameMap.get(session);
        byte[] imageBytes = new byte[messageBuffer.remaining()];
        messageBuffer.get(imageBytes);


        if (imageBytes == null || imageBytes.length == 0) {
            // Handle the case where no image bytes were sent by the user.
            logger.warn("[onMessage] " + username + ": No image data sent.");
            sendMessageToParticularUser(username, "Error: No image data sent.");
            return; // Exit the method.
        }

        // server side log
        logger.info("[onMessage] " + username + ": " + imageBytes.length);

        try{
            //Creates Neural Network Model
            String modelLoadPath = "BirdClassifier10.zip";
            MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(modelLoadPath);

            //Converts Bytes To Image
            File destinationFile = new File("output.jpg");
            BufferedImage image = convertBytesToImage(imageBytes);
            ImageIO.write(image, "jpg", destinationFile);

            //Feeds image to neural network
            NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
            INDArray imageInput = loader.asMatrix(destinationFile);

            ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0,1);
            scaler.transform(imageInput);

            //Gets predicted class
            INDArray output = model.output(imageInput);
            predictedClass = output.argMax(1).getInt(0);

            if(predictedClass == 0)
                predictedClass = 10;
            else if(predictedClass == 1)
                predictedClass = 13;
            else if(predictedClass == 2)
                predictedClass = 18;
            else if(predictedClass == 3)
                predictedClass = 30;
            else if(predictedClass == 4)
                predictedClass = 71;
            else if(predictedClass == 5)
                predictedClass = 106;
            else if(predictedClass == 6)
                predictedClass = 211;
            else if(predictedClass == 7)
                predictedClass = 298;
            else if(predictedClass == 8)
                predictedClass = 363;
            else
                predictedClass = 406;

            String predictedClassString = predictedClass + "";

            sendMessageToParticularUser(username, predictedClassString);

        } catch (IOException e){
            logger.error("[onMessage] " + username + ": An error occurred while processing the image.", e);
            sendMessageToParticularUser(username, "71");
        }
    }

    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // server side log
        logger.info("[onClose] " + username);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // do error handling here
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }

    /**
     * Sends a message to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.
     */
    private void sendMessageToParticularUser(String username, String message) {
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("[DM Exception] " + e.getMessage());
        }
    }

    public BufferedImage convertBytesToImage(byte[] imageBytes) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
            return ImageIO.read(bis);
        }
    }
}