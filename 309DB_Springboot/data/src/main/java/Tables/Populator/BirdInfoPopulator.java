package Tables.Populator;
import Tables.BirdInfo.BirdInfo;
import Tables.BirdInfo.BirdInfoRepository;
import org.json.JSONObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Brian Xicon
 */
@Component
public class BirdInfoPopulator {
    //public BirdInfo( String scientificName, String name, String shortDesc, String image, String rangeMap, String callSound)

    @Autowired
    private BirdInfoRepository birdInfoRepository;

    public static List<BirdInfo> readSpeciesList(String filename){
        List<BirdInfo> birdInfoList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    String commonName = parts[2];
                    String scientificName = parts[1];

                    birdInfoList.add(new BirdInfo(commonName, scientificName, "", "", "", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return birdInfoList;
    }

    public static String toWikiAddress(String nameOfBird){
        if(nameOfBird.equals("LeConte's Sparrow"))
            return "leConte%27s_sparrow";
        if(nameOfBird.equals("Cape May Warbler"))
            return "Cape_May_warbler";

        String wikiAddress = nameOfBird.replaceAll(" ", "_");
        wikiAddress = wikiAddress.replaceAll("'", "%27");
        wikiAddress = wikiAddress.toLowerCase();

        return wikiAddress;
    }

    public static void populateBirdInfoFromWikipedia(List<BirdInfo> birdInfoList) {
        for (BirdInfo birdInfo : birdInfoList) {
            String birdName = birdInfo.getName();
            String wikiPage = toWikiAddress(birdName);


            String propParam = "extracts%7Cpageimages%7Cinfo";
            String wikipediaURL = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=" + propParam +
                    "&exintro=true&titles=" + wikiPage;

            HttpResponse<String> response = Unirest.get(wikipediaURL)
                    .header("User-Agent", "Your-App-Name")
                    .asString();

            // Parse and process the JSON response
            String jsonResponse = response.getBody();

            try {
                JSONObject result = new JSONObject(jsonResponse);
                JSONObject pages = result.getJSONObject("query").getJSONObject("pages");
                String pageId = pages.keys().next();
                JSONObject page = pages.getJSONObject(pageId);

                // Extract the description
                String description = page.optString("extract");

                // Extract the image URL
                String imageURL = page.getJSONObject("thumbnail").optString("source");
                String highResImageURL = imageURL.replace("/50px-", "/400px-");
                // Output the description and image URL
                birdInfo.setShortDesc(description.replaceAll("<[^>]+>", "").replaceAll("\\s+", " ").replaceAll("\\n{2,}", ""));
                birdInfo.setImage(highResImageURL);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            } finally {
                // Close the connection
                Unirest.shutDown();
            }
        }
    }

    public static void printWikiInfo(String birdName){

        String birdTitle = toWikiAddress(birdName);

        String propParam = "extracts%7Cpageimages%7Cinfo";
        String wikipediaURL = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=" + propParam +
                "&exintro=true&titles=" + birdTitle;

        HttpResponse<String> response = Unirest.get(wikipediaURL)
                .header("User-Agent", "Your-App-Name")
                .asString();

        // Parse and process the JSON response
        String jsonResponse = response.getBody();

        try {
            JSONObject result = new JSONObject(jsonResponse);
            JSONObject pages = result.getJSONObject("query").getJSONObject("pages");
            String pageId = pages.keys().next();
            JSONObject page = pages.getJSONObject(pageId);

            // Extract the description
            String description = page.optString("extract");

            // Extract the image URL
            String imageURL = page.getJSONObject("thumbnail").optString("source");
            String highResImageURL = imageURL.replace("/50px-", "/400px-");
            // Output the description and image URL
            System.out.println("Description: " + description.replaceAll("<[^>]+>", ""));
            System.out.println("Image URL: " + highResImageURL);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Close the connection
            Unirest.shutDown();
        }
    }

    public static void createSpeciesList() {
        String directoryPath = "D:\\xmenz\\Desktop\\birdsnap\\download\\images";
        String outputFileName = "speciesList.txt";

        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] folders = directory.listFiles(File::isDirectory);

            if (folders != null) {
                try (FileWriter writer = new FileWriter(outputFileName)) {
                    for (File folder : folders) {
                        writer.write(folder.getName() + "\n");
                    }
                    System.out.println("Folder names written to " + outputFileName);
                } catch (IOException e) {
                    System.err.println("Error writing to the file: " + e.getMessage());
                }
            }
        }
    }
}