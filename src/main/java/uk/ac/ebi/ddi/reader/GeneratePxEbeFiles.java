package uk.ac.ebi.ddi.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.reader.utils.ReadProperties;
import uk.ac.ebi.ddi.reader.utils.ReaderPxXML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * This class read an RSS an retrieve the PX Id of all the available datasets.
 *
 * @author Yasset Perez-Riverol
 */
public class GeneratePxEbeFiles {


    private static HashMap<String, String> pageBuffer = new HashMap<String, String>();

    private static final Logger logger = LoggerFactory.getLogger(GeneratePxEbeFiles.class);

    private static String PRIDE_PATTERN = "hostingRepository=\"PRIDE\"";

    private static final String PXSUBMISSION_PATTERN = "id=\"PXD%s\"";
    /**
     * This program take an output folder as a parameter an create different EBE eyes files for
     * all the project in ProteomeXchange. It loop all the project in ProteomeCentral and print them to the give output
     *
     * @param args
     */
    public static void main(String[] args){

        String outputFolder = null;

        if(args != null && args.length > 0 && args[0] != null)
               outputFolder = args[0];
        else{
            System.exit(-1);
        }

        try {

            String pxURL = ReadProperties.getInstance().getProperty("pxURL");

            String pxPrefix = ReadProperties.getInstance().getProperty("pxPrefix");

            Integer startPoint = Integer.valueOf(ReadProperties.getInstance().getProperty("pxStart"));

            Integer endPoint   = Integer.valueOf(ReadProperties.getInstance().getProperty("pxEnd"));

            Integer loopGap = Integer.valueOf(ReadProperties.getInstance().getProperty("loopGap"));

            for(int i = startPoint; i < endPoint && loopGap > 0; i ++){

                String pxID = (pxPrefix + String.valueOf(i));

                pxID = pxID.substring( pxID.length() - 6, pxID.length());

                String pxURLProject = String.format(pxURL, pxID);

                String page = getPage(pxURLProject);

                if(isDataset(page, pxID) && !isPRIDEDataset(page)){
                    System.out.print(page);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Gets the page from the given address. Returns the
     * retrieved page as a string.
     *
     * @param urlString The address of the resource to retrieve.
     * @return The page as a String
     * @throws Exception Thrown on any problem.
     */
    private static String getPage(String urlString) throws Exception {
        // check if the page is cached
        if (pageBuffer.containsKey(urlString))
            return pageBuffer.get(urlString);

        // create the url
        URL url = new URL(urlString);

        // send the request
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.connect();

        // get the page
        BufferedReader in = null;
        StringBuilder page = new StringBuilder();
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                page.append(line);
                page.append("\n");
            }
        } catch (IOException ioe) {
            logger.warn("Failed to read web page");
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return page.toString();
    }

    private static boolean isPRIDEDataset(String pxSubmission){
         return pxSubmission.contains(PRIDE_PATTERN);
    }

    private static boolean isDataset(String pxSubmission, String pxID){
        String pattern = String.format(PXSUBMISSION_PATTERN, pxID);
        return pxSubmission.contains(pattern);
    }






}
