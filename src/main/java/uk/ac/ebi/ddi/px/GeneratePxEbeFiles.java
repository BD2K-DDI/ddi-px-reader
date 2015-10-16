package uk.ac.ebi.ddi.px;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.px.model.Project;
import uk.ac.ebi.ddi.px.utils.ReadProperties;
import uk.ac.ebi.ddi.px.utils.ReaderPxXML;
import uk.ac.ebi.ddi.px.utils.WriterEBeyeXML;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * This program takes a ProteomeXchange URL and generate for all the experiments the
 *
 * @author Yasset Perez-Riverol
 */
public class GeneratePxEbeFiles {


    private static HashMap<String, String> pageBuffer = new HashMap<String, String>();

    private static final Logger logger = LoggerFactory.getLogger(GeneratePxEbeFiles.class);

    private static String PRIDE_PATTERN = "hostingRepository=\"PRIDE\"";

    private static final String PXSUBMISSION_PATTERN = "<ProteomeXchangeDataset";

    private static List<String> databases = Arrays.asList("PRIDE", "MassIVE", "PeptideAtlas");

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

            Integer endPoint   = Integer.valueOf(ReadProperties.getInstance().getProperty("pxEnd"));

            Integer loopGap = Integer.valueOf(ReadProperties.getInstance().getProperty("loopGap"));

            searchFilesWeb(loopGap, endPoint, pxPrefix, pxURL, outputFolder, databases);

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }

    }

    public static void searchFilesWeb(int loopGap, int endPoint, String pxPrefix, String pxURL, String outputFolder, List<String> databases) throws Exception {

        int initialGap = loopGap;

        for(int i = 0; i < endPoint && loopGap > 0; i ++){

            String pxID = (pxPrefix + String.valueOf(i));

            pxID = pxID.substring( pxID.length() - 6, pxID.length());

            String pxURLProject = String.format(pxURL, pxID);

            String page = getPage(pxURLProject);

            if (page != null && isDataset(page)){


                Project proj = ReaderPxXML.readProject(page);

                if(proj != null && databases.contains(proj.getRepositoryName())){
                    //Sometimes PeptideAtlas change the original identifier for that reason we need to override this value
                    proj.setAccession("PXD" + pxID);

                    WriterEBeyeXML writer = new WriterEBeyeXML(proj,new File(outputFolder),null);

                    writer.generate();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    logger.info(loopGap + "|" + proj.getAccession() + "|" + proj.getRepositoryName() + "|" + dateFormat.format(proj.getPublicationDate()) + "|" + getType(proj) + "|" + getFileType(proj) + "|" + getNumberFiles(proj) + "|" + getNumberPeakFile(proj));
                }

                logger.debug(proj.getAccession()  + "|PX PROJECT FOUND IT|");


                loopGap = initialGap;

            }else{
                loopGap--;
                logger.debug(loopGap + "| LOGGER GAP CHANGE|");
            }
        }
        logger.info("Search for Files has been FINISHED!!");
    }

    private static String getNumberPeakFile(Project proj) {
        if(proj != null && proj.getDataFiles()!=null && !proj.getDataFiles().isEmpty()){
            Integer count = 0;
            for(String file: proj.getDataFiles()){
                file = file.toLowerCase();
                if(file.endsWith("mgf") || file.endsWith("pkl") || file.endsWith("mzml") || file.endsWith("ms2") || file.endsWith("dta") || file.endsWith("apl")
                        || file.endsWith("mgf.gz") || file.endsWith("pkl.gz") || file.endsWith("mzml.gz") || file.endsWith("ms2.gz") || file.endsWith("dta.gz") || file.endsWith("apl.gz"))
                    count++;
            }
            return count.toString();
        }
        return null;
    }

    private static String getNumberFiles(Project proj) {
        if(proj != null && proj.getDataFiles()!=null && !proj.getDataFiles().isEmpty()){
            Integer count = 0;
            for(String file: proj.getDataFiles()){
                file = file.toLowerCase();
                if(file.endsWith("xml") || file.endsWith("xml.gz") && file.contains("pride")){
                    count++;
                }else if(file.endsWith("mzid") || file.endsWith("mzid.gz")){
                    count++;
                }
            }
            return count.toString();
        }
        return null;
    }

    private static String getFileType(Project proj) {
        String type = "PARTIAL";
        if(proj != null && proj.getDataFiles()!=null && !proj.getDataFiles().isEmpty()){
            for(String file: proj.getDataFiles()){
                file = file.toLowerCase();
                if(file.endsWith("xml") || file.endsWith("xml.gz") && file.contains("pride")){
                    type = "PRIDE XML";
                }else if(file.endsWith("mzid") || file.endsWith("mzid.gz")){
                    type = "MZIDENTML";
                }
            }
        }
        return type;
    }

    private static String getType(Project proj) {
        String type = "PARTIAL";
        if(proj != null && proj.getDataFiles()!=null && !proj.getDataFiles().isEmpty()){
            for(String file: proj.getDataFiles()) {
                file = file.toLowerCase();
                if (file.endsWith("xml") || file.endsWith("xml.gz") && file.contains("pride")) {
                    type = "COMPLETE";
                } else if (file.endsWith("mzid") || file.endsWith("mzid.gz")) {
                    type = "COMPLETE";
                }
            }
        }
        return type;
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

        try{
            if (pageBuffer.containsKey(urlString))
                return pageBuffer.get(urlString);

            // create the url
            URL url = new URL(urlString);

            // send the request
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(10000); //set timeout to 10 seconds

            connection.setReadTimeout(300000); // set timeout to 10 seconds

            connection.connect();

            // get the page
            BufferedReader in = null;
            StringBuilder page = new StringBuilder();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                page.append(line);
                page.append("\n");
            }

            return page.toString();
        }catch (Exception ioe) {
            logger.warn("Failed to read web page");
        }
        logger.debug(urlString);
        return null;
    }

    private static boolean isPRIDEDataset(String pxSubmission){
         return pxSubmission.contains(PRIDE_PATTERN);
    }

    private static boolean isDataset(String pxSubmission){
            return pxSubmission.contains(PXSUBMISSION_PATTERN);
    }
}
