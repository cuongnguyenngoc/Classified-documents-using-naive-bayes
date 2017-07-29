/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import classifiedDocuments.NaiveBayes;
import classifiedDocuments.SeperatedWord;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author hoan
 */
public class Testing extends javax.swing.JPanel {

    /**
     * Creates new form Testing
     */
     private HashMap<String, HashMap<String, Integer>> DatasTraining;
    public static SeperatedWord  sw = new SeperatedWord();
    public Testing() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaContentDoccument = new javax.swing.JTextArea();
        jButtonTest = new javax.swing.JButton();
        jLabelResult = new javax.swing.JLabel();

        setBackground(new java.awt.Color(178, 195, 221));
        setPreferredSize(new java.awt.Dimension(678, 440));

        jLabel1.setFont(new java.awt.Font("WenQuanYi Micro Hei", 1, 15)); // NOI18N
        jLabel1.setText("PHÂN LOẠI VĂN BẢN");

        jLabel2.setText("Nhập văn bản cần test");

        jTextAreaContentDoccument.setColumns(20);
        jTextAreaContentDoccument.setRows(5);
        jScrollPane1.setViewportView(jTextAreaContentDoccument);

        jButtonTest.setText("Kiểm tra");
        jButtonTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTestActionPerformed(evt);
            }
        });

        jLabelResult.setText("Chưa thực hiện");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(315, 315, 315)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelResult)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                            .addComponent(jButtonTest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(217, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonTest)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelResult)
                .addContainerGap(29, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTestActionPerformed
        // TODO add your handling code here:

        WorkingReadDataTraining train = new WorkingReadDataTraining();
        train.execute();

        //test
        WorkingReadDataTest test = new WorkingReadDataTest();
        test.execute();
    }//GEN-LAST:event_jButtonTestActionPerformed
    class WorkingReadDataTraining extends SwingWorker<String, Void> {

        @Override
        protected String doInBackground() throws Exception {
            String resultReadTrain = null;

            try {
                publish();

                DatasTraining = sw.readDataTraining();
                resultReadTrain = "Đã đọc xong dữ liệu học";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultReadTrain;
        }

        @Override
        protected void done() {
            super.done(); //To change body of generated methods, choose Tools | Templates.   
            try {
                String resultReadTrain = get();
                jLabelResult.setText(resultReadTrain);

            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        protected void process(List<Void> chunks) {
//To change body of generated methods, choose Tools | Templates.
            jLabelResult.setText("Đang đọc dữ liệu học...");
        }

    }

    class WorkingReadDataTest extends SwingWorker<String, Void> {

        @Override
        protected String doInBackground() throws Exception {
            String resultTest = null;

            try {
                publish();
                NaiveBayes nb = new NaiveBayes();

                String dataTest = jTextAreaContentDoccument.getText().toString();
                ArrayList<String> wordTest = sw.readDataTestFromTextArea(dataTest);
                int T = sw.sumKeyWordInDataTrain(DatasTraining);
                resultTest = nb.predictDoc(DatasTraining, T, wordTest);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultTest;
        }

        @Override
        protected void done() {
            super.done(); //To change body of generated methods, choose Tools | Templates.   
            try {
                String resultTest = get();

                jLabelResult.setText("Đã phân loại xong");
                if (resultTest == null) {
                    JOptionPane.showMessageDialog(null, "Văn bản không phân loại được");
                } else {
                    JOptionPane.showMessageDialog(null, "Văn bản thuộc lớp:" + resultTest);
                }

            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        protected void process(List<Void> chunks) {
//To change body of generated methods, choose Tools | Templates.
            jLabelResult.setText("Đang kiểm tra...");
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonTest;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelResult;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaContentDoccument;
    // End of variables declaration//GEN-END:variables
}
