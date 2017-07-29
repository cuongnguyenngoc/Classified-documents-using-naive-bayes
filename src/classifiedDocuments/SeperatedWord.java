package classifiedDocuments;

import creatingDataTrainingRss.CollectionData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.hus.nlp.tokenizer.VietTokenizer;

/**
 *
 * @author Cuong Nguyen Ngoc
 */
public class SeperatedWord {

    
    public VietTokenizer tokenizer;
    public HashMap<String, Integer> vi_words;
    public static File file;
    public String absolutePath;
    public Map<String, Integer> WordSet;

    public SeperatedWord() {
        tokenizer = new VietTokenizer();
        vi_words = FileProcessing.readFileToMap("/src/Rss/words_vi.txt");
        absolutePath = new File("").getAbsolutePath();
        file = new File(absolutePath + CollectionData.FOLDER_TRAINING_DATA);
        WordSet = new HashMap<String, Integer>();

    }

    public HashMap<String, HashMap<String, Integer>> readDataTraining() {
        // nhan-tu-so tu xuat hien trong nhan
        File folderTrain = new File(new File("").getAbsoluteFile()
                + CollectionData.FOLDER_DATA);
        HashMap<String, HashMap<String, Integer>> DatasInCategory = new HashMap<>();
        if (folderTrain.exists()) {
            DatasInCategory = FileProcessing.readDataTrainFromDatabase(folderTrain);
        } else {
            HashMap<String, ArrayList<File>> filesInFolders = new HashMap<>();

            filesInFolders = FileProcessing.getDirsAndFiles(filesInFolders, file);

            for (Map.Entry<String, ArrayList<File>> entry : filesInFolders.entrySet()) {

                HashMap<String, Integer> collectionWords = new HashMap<>();
                for (File fileData : entry.getValue()) {

                    String line = FileProcessing.ReadFileToList(fileData);

                    //collectionWords = getCollectionWordToMap(line, vi_words, tokenizer, collectionWords);
                    String[] tokens = tokenizer.tokenize(line);
                    String[] words = tokens[0].split(" ");
                    for (String word : words) {
                        if (vi_words.containsKey(word)) {  // Kiểm tra xem từ có thuộc bộ từ điển tiếng việt không
                            if (collectionWords.containsKey(word)) {
                                collectionWords.replace(word, collectionWords.get(word) + 1);
                            } else {
                                collectionWords.put(word, 1);
                            }
                           
                        }
                    }

                }
                DatasInCategory.put(entry.getKey(), collectionWords);
            }
        }
        FileProcessing.insertToDatabase(DatasInCategory);
        return DatasInCategory;
    }
     public HashMap<String, HashMap<String, Integer>> readDataTraining(HashMap<String,ArrayList<File>> listFileDataTrain) {
// nhan-tu-so tu xuat hien trong nhan
             HashMap<String, HashMap<String, Integer>> DatasInCategory = new HashMap<>();
            for (Map.Entry<String, ArrayList<File>> entry : listFileDataTrain.entrySet()) {

                HashMap<String, Integer> collectionWords = new HashMap<>();
                for (File fileData : entry.getValue()) {

                    String line = FileProcessing.ReadFileToList(fileData);

                    //collectionWords = getCollectionWordToMap(line, vi_words, tokenizer, collectionWords);
                    if(line !=null){
                    String[] tokens = tokenizer.tokenize(line);
                    String[] words = tokens[0].split(" ");
                    for (String word : words) {
                        if (vi_words.containsKey(word)) {  // Kiểm tra xem từ có thuộc bộ từ điển tiếng việt không
                            if (collectionWords.containsKey(word)) {
                                collectionWords.replace(word, collectionWords.get(word) + 1);
                            } else {
                                collectionWords.put(word, 1);
                            }
                           
                        }
                    }
                DatasInCategory.put(entry.getKey(), collectionWords);
                }
            }
            }
       
        return DatasInCategory;
    }
    
    

    public HashMap<String, Integer> getAllWordsAndFrequencyInCategory(
            HashMap<String, HashMap<String, Integer>> DatasInCategory, String cat) {

        HashMap<String, Integer> wordSet = new HashMap<>();
        for (Map.Entry<String, HashMap<String, Integer>> entry : DatasInCategory.entrySet()) {

            Map<String, Integer> Words = entry.getValue();

            for (Map.Entry<String, Integer> entry2 : Words.entrySet()) {
                if(DatasInCategory.get(cat)!=null){
                   
                
                if (!DatasInCategory.get(cat).containsKey(entry2.getKey())) {
                    wordSet.put(entry2.getKey(), 0);
                } else {
                    wordSet.put(entry2.getKey(), DatasInCategory.get(cat).get(entry2.getKey()));
                }
                }
            }
        }
        return wordSet;
    }
    public int sumKeyWordInDataTrain( HashMap<String,HashMap<String,Integer>> DataTrain){
        int T=0;// tính số lượng từ khóa trong tập D_train
        for(Map.Entry<String,HashMap<String,Integer>> entry : DataTrain.entrySet()){
            for(Map.Entry<String,Integer> itemEntry: entry.getValue().entrySet()){
                T++;
            }
            
        }
        return T;
    }
    public ArrayList<String> readDataTestFromFile(String filePath) {

        ArrayList<String> collectionWordsTest = new ArrayList<>();

//        File fileData = new File(absolutePath + filePath);
        File fileData = new File(filePath);
        String line = FileProcessing.ReadFileToList(fileData);

//        collectionWordsTest = getCollectionWordList(line, vi_words, tokenizer, collectionWordsTest);
        String[] results = tokenizer.tokenize(line);
        String[] words = results[0].split(" ");
        for (String word : words) {
            if (vi_words.containsKey(word)) {  // Kiểm tra xem từ có thuộc bộ từ điển tiếng việt không

                collectionWordsTest.add(word);

            }
        }
        return collectionWordsTest;
    }
    
//    //hoan
     public ArrayList<String> readDataTestFromTextArea(String data) {

        ArrayList<String> collectionWordsTest = new ArrayList<>();
        String[] results = tokenizer.tokenize(data);
        String[] words = results[0].split(" ");
        for (String word : words) {
            if (vi_words.containsKey(word)) {  // Kiểm tra xem từ có thuộc bộ từ điển tiếng việt không

                collectionWordsTest.add(word);

            }
        }
        return collectionWordsTest;
    }
}
