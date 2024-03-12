import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ImageDataPreparation {
    public static void main(String[] args) {
        // Directory to where all the images are located.
        String datasetRootDir = "D:\\xmenz\\Desktop\\birdsnap\\download\\images";
        // The ratio of images that will be part of training (.8) and the images that will be part of testing (.2)
        double trainTestSplitRatio = 0.8;

        // Creates a list of strings which is used to save all of the image paths
        List<String> imagePaths = new ArrayList<>();
        // Creates a list of bird folder names
        List<String> birdFolderNames = new ArrayList<>();
        // Creates a file from the File class which is the root of our dataset
        File datasetRoot = new File(datasetRootDir);

        // Checks to see if the dataset root actually exists and is a directory. If not, exit the code.
        if (datasetRoot.exists() && datasetRoot.isDirectory()) {
            // Goes through the different bird folders in the dataset root
            for (File birdClassDir : datasetRoot.listFiles()) {
                // Checks to see if the current folder is a directory (to see if it is a bird class or not)
                if (birdClassDir.isDirectory()) {
                    // Add the name of the bird folder to the list
                    birdFolderNames.add(birdClassDir.getName());
                    // Goes through the image files in the bird folder
                    for (File imageFile : birdClassDir.listFiles()) {
                        // Checks to make sure that the image file is an actual valid image
                        if (imageFile.isFile() && imageFile.getName().toLowerCase().endsWith(".jpg")) {
                            // Adds the image directory to the imagePaths list
                            imagePaths.add(imageFile.getAbsolutePath());
                        }
                    }
                }
            }
        } else {
            System.out.println("Dataset root not found.");
            return;
        }

        // Shuffles the list of image paths for randomization effect in data.
        Collections.shuffle(imagePaths, new Random(123));
        // Calculates where to split the list of image paths for training and testing
        int splitIndex = (int) (imagePaths.size() * trainTestSplitRatio);

        // Creates a list of training and testing image paths
        List<String> trainingImagePaths = imagePaths.subList(0, splitIndex);
        List<String> testingImagePaths = imagePaths.subList(splitIndex, imagePaths.size());

        // Create corresponding label lists for training and testing
        List<Integer> trainingLabels = new ArrayList<>();
        List<Integer> testingLabels = new ArrayList<>();

        // Assign labels to training and testing data
        for (int i = 0; i < imagePaths.size(); i++) {
            if (i < splitIndex) {
                trainingLabels.add(birdFolderNames.indexOf(getParentDirectoryName(imagePaths.get(i))));
            } else {
                testingLabels.add(birdFolderNames.indexOf(getParentDirectoryName(imagePaths.get(i))));
            }
        }

        // Save the lists of training and testing image paths and labels to files
        saveImagePathsToFile(trainingImagePaths, "train_paths.txt");
        saveImagePathsToFile(testingImagePaths, "test_paths.txt");
        saveLabelsToFile(trainingLabels, "train_labels.txt");
        saveLabelsToFile(testingLabels, "test_labels.txt");
        saveBirdNamesToFile(birdFolderNames, "bird_names.txt");

        // Print out the number of bird folders
        System.out.println("Number of bird folders: " + birdFolderNames.size());
        System.out.println("Training image paths and labels saved to train_paths.txt and train_labels.txt");
        System.out.println("Testing image paths and labels saved to test_paths.txt and test_labels.txt");
    }

    // Save list of image file paths to a text file
    private static void saveImagePathsToFile(List<String> imagePaths, String filename) {
        try {
            // Creates PrintWriter class to write to the file
            PrintWriter writer = new PrintWriter(filename);
            // Loops through the imagePaths list
            for (String path : imagePaths) {
                // Prints the image path to the file
                writer.println(path);
            }
            // Closes the writer
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Save list of labels to a text file
    private static void saveLabelsToFile(List<Integer> labels, String filename) {
        try {
            // Creates PrintWriter class to write to the file
            PrintWriter writer = new PrintWriter(filename);
            // Loops through the labels list
            for (int label : labels) {
                // Prints the label to the file
                writer.println(label);
            }
            // Closes the writer
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Save list of bird folder names to a text file along with label numbers
    private static void saveBirdNamesToFile(List<String> birdFolderNames, String filename) {
        try {
            // Creates PrintWriter class to write to the file
            PrintWriter writer = new PrintWriter(filename);
            // Loops through the birdFolderNames list
            for (int i = 0; i < birdFolderNames.size(); i++) {
                // Prints the label number and bird name to the file
                writer.println(i + ": " + birdFolderNames.get(i));
            }
            // Closes the writer
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get the parent directory name from a file path
    private static String getParentDirectoryName(String path) {
        File file = new File(path);
        return file.getParentFile().getName();
    }
}
