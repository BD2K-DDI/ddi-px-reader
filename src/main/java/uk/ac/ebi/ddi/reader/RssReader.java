package uk.ac.ebi.ddi.reader;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import uk.ebi.ac.ddi.utils.ReadProperties;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * This class read an RSS an retrieve the PX Id of all the available datasets.
 *
 * @author Yasset Perez-Riverol
 */
public class RssReader {

    static SyndFeedInput input = new SyndFeedInput();

    public static void readRss(){

        try {
            String rssURL = ReadProperties.getInstance().getProperty("pxRss");

            SyndFeed feed = input.build(new XmlReader(new URL(rssURL)));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        }


    }




}
