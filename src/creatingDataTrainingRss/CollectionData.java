
package creatingDataTrainingRss;

import classifiedDocuments.FileProcessing;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CollectionData {
    
    public final static String FILE_RSS_PATH = "/src/Rss/rss.txt";
    public final static String FOLDER_TRAINING_DATA = "/src/data";
    public final static String FOLDER_DATA="/src/train";
  
    public static ArrayList<ArrayList<String>> collectDataFromRss(ArrayList<String> RSSs){
        Document docRSS;
	try {
            // need http protocola
            for(int i = 0; i< RSSs.size();i++){
                docRSS = Jsoup.connect(RSSs.get(i)).get();
                String subFolder = CollectionData.getCategory(RSSs.get(i));
                Vector<String> links = new Vector<String>();
                // get all links
                Elements items = docRSS.select("item");
                for (Element item : items) {
                    links.add(item.getElementsByTag("link").text());
                }
                
                ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
                for(String link : links){
                    System.out.println(link);
                    int id = getArticleId(link);
                    ArrayList<String> parameters = new ArrayList<String>();
                    Document docContent = Jsoup.connect(link).get();
                    Element divContent = docContent.getElementById("ArticleContent");
                    
                    BufferedWriter bw = getBufferedWriter(subFolder,id);
                    for(Element p : divContent.getElementsByTag("p")){
                        if(!p.html().contains("<strong>") && p.hasText() &&
                                !p.html().contains("<em>")){

                            parameters.add(p.text());
                            FileProcessing.writeToFile(bw, p.text());
                        }
                    }
                    bw.close();
                    content.add(parameters);
                    return content;
                }
            }
	} catch (IOException e) {
		e.printStackTrace();
	}
        return null;
    }
    private static int getArticleId(String link){
        String[] res = link.split("/");
        String id = res[5];
        return Integer.parseInt(id);
    }
    
    private static BufferedWriter getBufferedWriter(String subFolder, int id){
        String AbsolutePath = new File("").getAbsolutePath();
        File file;
        file = new File(AbsolutePath+FOLDER_TRAINING_DATA+"/"+subFolder);
        if(!file.exists())
            file.mkdir();
        file = new File(AbsolutePath+FOLDER_TRAINING_DATA+"/"+subFolder+"/"+ id);
        // if file doesnt exists, then create it
        FileWriter fw = null;
        BufferedWriter bw = null;
        try{
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile(), false);
            bw = new BufferedWriter(fw);
            return bw;
        } catch (IOException ex) {
            Logger.getLogger(CollectionData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
     public static BufferedWriter getBufferedWriter(String path){
        String AbsolutePath = new File("").getAbsolutePath();
        File file;
        file = new File(AbsolutePath+FOLDER_DATA);
        if(!file.exists())
            file.mkdir();
        file = new File(AbsolutePath+FOLDER_DATA+"/"+ path);
        // if file doesnt exists, then create it
        FileWriter fw = null;
        BufferedWriter bw = null;
        try{
            if (!file.exists()) {
                file.createNewFile();
            fw = new FileWriter(file.getAbsoluteFile(), false);
            bw = new BufferedWriter(fw);
            return bw;
            }
        } catch (IOException ex) {
            Logger.getLogger(CollectionData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public static String getCategory(String link){
        String[] res = link.split("/");
        String lastRes = res[res.length-1];
        int lastPos = lastRes.indexOf(".rss");
        return lastRes.substring(0,lastPos);
    }
    
    
}
