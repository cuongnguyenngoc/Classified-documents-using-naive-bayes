
package classifiedDocuments;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static main.Testing.sw;

/**
 *
 * @author Cuong Nguyen Ngoc
 */
public class NaiveBayes {
    
    public String predictDoc(HashMap<String,HashMap<String,Integer>> DatasTraining,int T,ArrayList<String> wordsTest){
        
        ArrayList<String> cats = FileProcessing.getCategories();
        for(String cat : cats){
            System.out.println(cat);
        }
        double max = 1.0;
        String c_star = "";
        int k = 0;
     
       
        for(String cat : cats){
            double volumn = 0.0;
            double PCi = this.getProbabilityCi(cat);
            System.out.println(cat);
            HashMap<String,Integer> words = DatasTraining.get(cat);
            
            HashMap<String,Integer> fTm = sw.getAllWordsAndFrequencyInCategory(DatasTraining, cat);
            int sumFTm=this.sumFTm(fTm);
            for(String word : wordsTest){
                int fTj = 0;
                if(words.containsKey(word)){
                    fTj = words.get(word); // số lần xuất hiện của từ khóa word trong phân lớp cat.
                }else{
                    fTj = 0;
                }
                double Ptjci = (1.0 * fTj + 1) / (sumFTm + T);
               // System.out.println("Ptjci:" + Ptjci);

                volumn += Math.log(Ptjci);
                //System.out.println("volume: " + volumn);
            }
            System.out.println("volumn: "+volumn);
            double P = Math.log(PCi) + volumn;
            if(max > 0 || max < P){
                max = P;
                c_star = cat;
            }
            
        }
     
        return c_star;
    }
    // tổng số lần xuất hiện của tất cả các từ khóa trong tập test có trong phân lớp ci;
    private int sumFTm(HashMap<String,Integer> fTm){
        int sum = 0;
        for(Map.Entry<String, Integer> entry : fTm.entrySet()){
            sum += entry.getValue();
        }
        return sum;
    }
    
    private double getProbabilityCi(String category) {
        HashMap<String, ArrayList<File>> filesInFolders = new HashMap<>();

        filesInFolders = FileProcessing.getDirsAndFiles(filesInFolders, SeperatedWord.file);

        ArrayList<File> Ci = filesInFolders.get(category);

        int countFilesInD = 0;
        for (Map.Entry<String, ArrayList<File>> entry : filesInFolders.entrySet()) {

            countFilesInD += entry.getValue().size();
        }
        return (1.0 * Ci.size()) / (countFilesInD);
    }
}
