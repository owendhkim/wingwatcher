package Tables.websocket3.chat;

import java.io.IOException;
import java.util.*;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import Tables.BirdTrackingInfo.BirdTrackingInfo;
import Tables.BirdTrackingInfo.BirdTrackingInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/map")  // this is Websocket url
public class MapWebsocket {

  // cannot autowire static directly (instead we do it by the below
  // method
	private static BirdTrackingInfoRepository trackingRepo;


	@Autowired
	public void setTrackingRepository(BirdTrackingInfoRepository r) { trackingRepo = r;}

	// Store all socket session and their corresponding username.
	private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
	private static Map<String, Session> usernameSessionMap = new Hashtable<>();

	private final Logger logger = LoggerFactory.getLogger(MapWebsocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username)
      throws IOException {

		logger.info("Entered into Open");

    // store connecting user information
		sessionUsernameMap.put(session, username);
		usernameSessionMap.put(username, session);

		//When opened get all of the current tracking infos on repository
		getAllTrackingInfo();

	}


	@OnMessage
	public void onMessage(Session session, String location) throws IOException {

		// Handle new messages
		logger.info("Entered into Message: Spotted new bird:");
		String username = sessionUsernameMap.get(session);
		// onMessage create new TrackingInfo object with longitude and latitude
		// and add it to BirdTrackingRepo
		Scanner s = new Scanner(location);
		String latitude = s.next();
		String longitude = s.next();
		String date = s.next();
		String time = s.next();
		String birdName = s.next();

		trackingRepo.save(new BirdTrackingInfo(Double.parseDouble(latitude), Double.parseDouble(longitude), date, time, birdName));
	}


	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Entered into Close");

    // remove the user connection information
		String username = sessionUsernameMap.get(session);
		sessionUsernameMap.remove(session);
		usernameSessionMap.remove(username);
	}


	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}

	private ArrayList<BirdTrackingInfo> getAllTrackingInfo() {
		List<BirdTrackingInfo> allTrackings = trackingRepo.findAll();
        ArrayList<BirdTrackingInfo> allTrackingArraylist = new ArrayList<>(allTrackings);
		return allTrackingArraylist;
	}

} // end of Class
