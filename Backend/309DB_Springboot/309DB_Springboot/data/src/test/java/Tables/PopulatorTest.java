package Tables;

import Tables.Populator.BirdInfoPopulator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import Tables.BirdInfo.BirdInfo;
import java.util.List;

public class PopulatorTest {

    @Test
    public void testReadSpeciesList() {
        String testFilename = getClass().getClassLoader().getResource("test_species_list.txt").getFile();
        List<BirdInfo> birdInfoList = BirdInfoPopulator.readSpeciesList(testFilename);

        assertFalse(birdInfoList.isEmpty());
        BirdInfo testBird = birdInfoList.get(0);
        assertNotNull(testBird.getName());
        assertNotNull(testBird.getScientificName());
    }

    @Test
    public void testToWikiAddress() {
        assertEquals("leConte%27s_sparrow", BirdInfoPopulator.toWikiAddress("LeConte's Sparrow"));
        assertEquals("Cape_May_warbler", BirdInfoPopulator.toWikiAddress("Cape May Warbler"));
        assertEquals("northern_cardinal", BirdInfoPopulator.toWikiAddress("Northern Cardinal"));
    }

    @Test
    public void testPopulateBirdInfoFromWikipedia() {
        List<BirdInfo> birdInfoList = List.of(new BirdInfo("Test Bird", "Testus birdus", "", "", "testRangeMap.jpg", "testCallSound.mp3"));
        BirdInfoPopulator.populateBirdInfoFromWikipedia(birdInfoList);

        BirdInfo testBird = birdInfoList.get(0);
        assertNotNull(testBird.getShortDesc());
        assertNotNull(testBird.getImage());
    }

    @Test
    public void testPrintWikiInfo() {
        assertDoesNotThrow(() -> BirdInfoPopulator.printWikiInfo("Northern Cardinal"));
    }
}
