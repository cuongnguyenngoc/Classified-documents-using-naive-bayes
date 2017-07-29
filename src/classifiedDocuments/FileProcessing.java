package classifiedDocuments;

import creatingDataTrainingRss.CollectionData;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileProcessing {

    public static String absolutePath = new File("").getAbsolutePath();
    public static File file = new File(absolutePath + CollectionData.FOLDER_TRAINING_DATA);

    public static HashMap<String, Integer> readFileToMap(String filePath) {

        BufferedReader br = null;
        try {
            String line = "";
            String path = new File("").getAbsolutePath();
            br = new BufferedReader(new FileReader(path + filePath));

            HashMap<String, Integer> datas = new HashMap<>();
            while ((line = br.readLine()) != null) {
                datas.put(line, 1);
            }
            return datas;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileProcessing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileProcessing.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(FileProcessing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
     public static String ReadFileToList(File file) {
        String result = "";
        BufferedReader br = null;
        try {
            String line = "";
            br = new BufferedReader(new FileReader(file));

            while ((line = br.readLine()) != null) {
                result += line;
                result += '\n';
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileProcessing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileProcessing.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(FileProcessing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result.toLowerCase();
    }

    public static void writeToFile(BufferedWriter bw, String p) {
        try {
            bw.write(p);
            bw.write("\r\n");
        } catch (IOException ex) {
            Logger.getLogger(CollectionData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void writeToFile_1(BufferedWriter bw, String p) {
        try {
            bw.write(p);
            bw.write("\n");
        } catch (IOException ex) {
            Logger.getLogger(CollectionData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static ArrayList<String> getCategories() {

        HashMap<String, ArrayList<File>> filesInFolders = new HashMap<>();

        filesInFolders = getDirsAndFiles(filesInFolders, file);

        ArrayList<String> cats = new ArrayList<>();
        for (String key : filesInFolders.keySet()) {
            cats.add(key);
        }
        return cats;
    }

    public static HashMap<String, ArrayList<File>> getDirsAndFiles(HashMap<String, ArrayList<File>> filesInFolders, File file) {
        if (file.isDirectory()) {
            for (File fuzzyFile : file.listFiles()) {
                if (fuzzyFile.isDirectory()) {
                    getDirsAndFiles(filesInFolders, fuzzyFile);
                }
                if (fuzzyFile.isFile()) {
                    ArrayList<File> files = new ArrayList<File>(Arrays.asList(fuzzyFile.getParentFile().listFiles()));
                    filesInFolders.put(fuzzyFile.getParentFile().getName(), files);
                }
            }
            return filesInFolders;
        }
        return null;
    }

    //hoan
    public static HashMap<String, ArrayList<File>> getAllDataFromFolder(String pathFolder) {
        File files = new File(pathFolder);
        HashMap<String, ArrayList<File>> listFile = new HashMap<>();
        int i = 0;
        if (files.isDirectory()) {
            for (File file : files.listFiles()) {
                i++;
                //  listFile.put(files.getName(), file.getPath());
                System.out.println(i);
            }
        }
        return listFile;
    }

    //hoan
    public static void insertToDatabase(HashMap<String, HashMap<String, Integer>> DatasInCategory) {
        for (Map.Entry<String, HashMap<String, Integer>> data : DatasInCategory.entrySet()) {
            BufferedWriter bw = CollectionData.getBufferedWriter( data.getKey());
            if (bw != null) {
                for (Map.Entry<String, Integer> word : data.getValue().entrySet()) {
                    String p = word.getKey() + ";" + word.getValue();
                    writeToFile_1(bw, p);

                }

            }
        }

    }

    public static HashMap<String, HashMap<String, Integer>> readDataTrainFromDatabase(File folderTrain) {
        ArrayList<String> cats=new ArrayList<>();
            for(File file:folderTrain.listFiles()){
                cats.add(file.getName());
            
        }
       
        HashMap<String, HashMap<String, Integer>> DatasInCategory = new HashMap<>();
        BufferedReader br = null;
        for (String cat:cats) {
            HashMap<String,Integer> datas=new HashMap<>();
            try {
                String line = "";
                File file=new File(new File("").getAbsoluteFile()+
                        CollectionData.FOLDER_DATA+"/"+cat);
                br = new BufferedReader(new FileReader(file));
                
                while ((line = br.readLine()) != null) {
                   String[] split=line.split(";");
                   if(split.length>1){
                   String key=split[0];
                   
                   int value=Integer.valueOf(split[1]);
                   
                   datas.put(key,value);
                   }else{
                      
                   }
                }
                DatasInCategory.put(cat, datas);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileProcessing.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FileProcessing.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileProcessing.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return DatasInCategory;
    }

   
}
