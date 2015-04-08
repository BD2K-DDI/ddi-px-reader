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
public class GeneratePxEbeFiles {


    public static void main(String[] args){

        String outputFolder;
        if(args != null && args[0] != null)
               outputFolder = args[0];
        else{
            System.exit(-1);

        try {

            String pxURL = ReadProperties.getInstance().getProperty("pxURL");

            Integer startPoint = Integer.valueOf(ReadProperties.getInstance().getProperty("pxStart"));

            Integer endPoint   = Integer.valueOf(ReadProperties.getInstance().getProperty("pxEnd"));

            Integer loopGap = Integer.valueOf(ReadProperties.getInstance().getProperty("loopGap"));

            for(int i = startPoint; i < endPoint && loopGap > 0; i ++){
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void readRss(){

    }




}
