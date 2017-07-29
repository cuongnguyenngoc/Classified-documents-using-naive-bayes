/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifiedDocuments;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author hoan
 */
public class Measure {

    public HashMap<String, ArrayList<File>> hashmapfileTest = new HashMap<>();
    public HashMap<String, ArrayList<File>> hashmapfileTrain = new HashMap<>();
    public ResultTesting resultTesting = new ResultTesting();

    public void divData(HashMap<String, ArrayList<File>> Data, int k, int scale) {
        
        int sumFileTest = 0, sumFileTrain = 0;
        for (Map.Entry<String, ArrayList<File>> entry : Data.entrySet()) {
            int size = entry.getValue().size();
            int count = size / k;// so file cho moi phan
            int posStart = count * (scale - 1);// vi tri bat dau lay lam file test
            int posEnd = count * (scale);// vi tri ket thuc lay vao file test;
            ArrayList<File> filesTest = new ArrayList<>();
            ArrayList<File> filesTrain = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (i < posEnd && i >= posStart) {
                    filesTest.add(entry.getValue().get(i));

                    sumFileTest++;

                } else {
                    filesTrain.add(entry.getValue().get(i));
                    sumFileTrain++;
                }
            }
            hashmapfileTest.put(entry.getKey(), filesTest);
            hashmapfileTrain.put(entry.getKey(), filesTrain);
            resultTesting.setSumFileTest(sumFileTest);
            resultTesting.setSumFileTrain(sumFileTrain);

        }
    }
     
    
    public void divData_2(HashMap<String, ArrayList<File>> Data, int scale1, int scale2) {
        
        int sumFileTest = 0, sumFileTrain = 0;
        for (Map.Entry<String, ArrayList<File>> entry : Data.entrySet()) {
            int size = entry.getValue().size() - 1;
            int count = (scale1 * size) / scale2;// so file can lay lam D_train
            int posStart = 0;// vi tri bat dau lay lam file test
            int posEnd = posStart + count;// vi tri ket thuc lay vao file test;

            ArrayList<File> filesTest = new ArrayList<>();
            ArrayList<File> filesTrain = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (i < posEnd && i >= posStart) {

                    filesTrain.add(entry.getValue().get(i));
                    sumFileTrain++;

                } else {

                    filesTest.add(entry.getValue().get(i));

                    sumFileTest++;
                }
            }
            hashmapfileTest.put(entry.getKey(), filesTest);
            hashmapfileTrain.put(entry.getKey(), filesTrain);
            resultTesting.setSumFileTest(sumFileTest);
            resultTesting.setSumFileTrain(sumFileTrain);

        }
    }
    
    public ResultTesting test(int k, String scale,int plan) {
        HashMap<String, ArrayList<File>> Datas = new HashMap<>();
        Datas = FileProcessing.getDirsAndFiles(Datas, FileProcessing.file);
        int sumTruePos = 0, sumFalesPos = 0;
        //chia du lieu
        if(plan==1){
            String s[] = scale.split(" ");
            String s1 = s[0];
            int s2 = Integer.valueOf(s[1]);
           
            divData(Datas, k, s2);
        }else if(plan==2){
            String s[] = scale.split("/");
            int s1 = Integer.valueOf(s[0]);
            int s2 = Integer.valueOf(s[1]);
            divData_2(Datas,s1,s2);
        }
        
        
        SeperatedWord sw = new SeperatedWord();
        HashMap<String,HashMap<String,Integer>> DataTrain = sw.readDataTraining(hashmapfileTrain);
       
        NaiveBayes Nb = new NaiveBayes();
        
        int T = sw.sumKeyWordInDataTrain(DataTrain);
        for (Map.Entry<String, ArrayList<File>> entry : hashmapfileTest.entrySet()) {
            for (File pathFile : entry.getValue()) {

                ArrayList<String> wordsTest = sw.readDataTestFromFile(pathFile.getPath());
                String result = Nb.predictDoc(DataTrain,T, wordsTest);

                if(result.equals(entry.getKey())){
                    sumTruePos++;
                }else
                    sumFalesPos++;

            }
        }
        System.out.println("Da xong");
        resultTesting.setSumFalesPos(sumFalesPos);
        resultTesting.setSumTruePos(sumTruePos);
        double acc = sumTruePos*((float)1/(sumFalesPos+sumTruePos));
        resultTesting.setAccuracy(acc);
        return resultTesting;

    }

    public static void main(String args[]) {

        Measure measure = new Measure();
        ResultTesting result = measure.test(5,"phần 1",1);

        System.out.println("Tong so file Test:" + result.getSumFileTest());
        System.out.println("Tong so file train:" + result.getSumFileTrain());
    }
}
