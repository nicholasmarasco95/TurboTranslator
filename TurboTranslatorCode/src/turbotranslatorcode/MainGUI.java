/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turbotranslatorcode;

import java.awt.Color;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author nicho
 */
public class MainGUI extends javax.swing.JFrame {

    /**
     * Creates new form MainGUI
     */
    
    private Settings settings;
    private boolean error;
    
    public MainGUI() {
        initComponents();
        this.settings = new Settings();
        this.error = false;
        
        refreshGui();
    }
    
    private void refreshGui(){
        //FILES TO TRANSLATE
        List<String> filePath = settings.getPathList();
        this.txtAreaFilesPath.setText("");
        this.error = false;
        if(filePath==null){
            this.error = true;
            this.txtFileNumber.setForeground(Color.red);
            this.txtFileNumber.setText("0");
            this.btnAddFiles.setForeground(Color.red);
        }else{
            this.btnAddFiles.setForeground(Color.black);
            Iterator<String> filePathIt = filePath.iterator();
            while(filePathIt.hasNext()){
                this.txtAreaFilesPath.append(filePathIt.next() + "\n");
            }
            this.txtFileNumber.setForeground(Color.blue);
            this.txtFileNumber.setText(String.valueOf(filePath.size()));
        }
        //EXPORT FOLDER
        String outputFolder = settings.getStringValue(Utils.SETTINGS_KEY.OUTPUT_FOLDER);
        if(outputFolder!=null && outputFolder.length()>2){
            this.btnSetExport.setForeground(Color.black);
            this.txtExportPath.setForeground(Color.black);
            this.txtExportPath.setText(outputFolder);
        }else{
            this.error = true;
            this.btnSetExport.setForeground(Color.red);
            this.txtExportPath.setForeground(Color.red);
            this.txtExportPath.setText("EMPTY");
        }
        //LANGUAGES MANAGER
        String inputLanSet = settings.getStringValue(Utils.SETTINGS_KEY.LANG_INPUT);
        this.textInputStr.setForeground(Color.black);
        String outputLanSet = settings.getStringValue(Utils.SETTINGS_KEY.LANG_OUTPUT);
        this.textOutputStr.setForeground(Color.black);
        if(inputLanSet!=null && inputLanSet.length()>1){
            this.fieldInput.setText(inputLanSet);
        }else{
            this.error = true;
            this.textInputStr.setForeground(Color.red);
        }
        if(outputLanSet!=null && outputLanSet.length()>1){
            this.filedOutput.setText(outputLanSet);
        }else{
            this.error = true;
            this.textOutputStr.setForeground(Color.red);
        }
        
        //Action Buttons Manager
        boolean enabled = !error;
        btnTranslateAdd.setEnabled(enabled);
        btnTranslateExport.setEnabled(enabled);
        btnExportFiles.setEnabled(enabled);
        
        if(error){
            this.textStatus.setText("ERROR");
            this.textStatus.setForeground(Color.red);
        }else{
            this.textStatus.setText("READY");
            this.textStatus.setForeground(Color.blue);
        }
        
        //YANDEX KEY
        String yandexKey = Utils.getYandexKey();
        if(yandexKey == null || yandexKey.length()<=2){
            this.btnYandexKey.setForeground(Color.red);
            btnTranslateAdd.setEnabled(false);
            btnTranslateExport.setEnabled(false);
        }
        else this.btnYandexKey.setForeground(Color.black);
    }
    
    private boolean checkFilePath(String path, boolean isCsv){
        if(!new File(path).exists()){
            JOptionPane.showMessageDialog(this, "File doesn't exists", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(isCsv){
            if(Utils.getFileExtension(path).equals(".csv")){
                JOptionPane.showMessageDialog(this, "Only CSV files are supported", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        else if(!Utils.isFileSupported(Utils.getFileExtension(path))){
            JOptionPane.showMessageDialog(this, "File not supported", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private boolean checkDirectory(String path){
        File file = new File(path);
        if(!file.exists()){
            JOptionPane.showMessageDialog(this, "Directory doesn't exists", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }else if(file.isDirectory()){
            JOptionPane.showMessageDialog(this, "Only directory are allowed", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
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
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaFilesPath = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        txtExportPath = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnAddFiles = new javax.swing.JButton();
        btnRemoveFiles = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnSetExport = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        textWordsTranslated = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        textFilesDone = new javax.swing.JLabel();
        txtFileNumber = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        textStatus = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        btnYandexKey = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textAreaLogs = new javax.swing.JTextArea();
        btnTranslateAdd = new javax.swing.JButton();
        btnTranslateExport = new javax.swing.JButton();
        btnExportFiles = new javax.swing.JButton();
        btnImportFile = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        textInputStr = new javax.swing.JLabel();
        fieldInput = new javax.swing.JTextField();
        textOutputStr = new javax.swing.JLabel();
        filedOutput = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        btnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Turbo Translator");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Turbo Translator");

        jLabel3.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        jLabel3.setText("Files to Translate");

        txtAreaFilesPath.setEditable(false);
        txtAreaFilesPath.setColumns(20);
        txtAreaFilesPath.setRows(5);
        jScrollPane1.setViewportView(txtAreaFilesPath);

        jLabel4.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        jLabel4.setText("Export Folder Path =");

        txtExportPath.setFont(new java.awt.Font("Microsoft Tai Le", 1, 10)); // NOI18N
        txtExportPath.setText("C:\\\\SOMEWHERE");

        jLabel6.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        jLabel6.setText("Files to Translate Actions");

        btnAddFiles.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        btnAddFiles.setText("Add Files");
        btnAddFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFilesActionPerformed(evt);
            }
        });

        btnRemoveFiles.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        btnRemoveFiles.setText("Remove All");
        btnRemoveFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveFilesActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        jLabel7.setText("Export Path");

        btnSetExport.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        btnSetExport.setText("Set Folder");
        btnSetExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetExportActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Microsoft Tai Le", 1, 18)); // NOI18N
        jLabel8.setText("Info");

        jLabel11.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        jLabel11.setText("Words Translated");

        textWordsTranslated.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        textWordsTranslated.setText("0000");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel13.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        jLabel13.setText("Files Done");

        textFilesDone.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        textFilesDone.setText("0000");

        txtFileNumber.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        txtFileNumber.setText("0000");

        jLabel16.setFont(new java.awt.Font("Microsoft Tai Le", 1, 18)); // NOI18N
        jLabel16.setText("Status");

        textStatus.setFont(new java.awt.Font("Microsoft Tai Le", 1, 18)); // NOI18N
        textStatus.setText("READY");

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton4.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        jButton4.setText("REFRESH");

        jLabel18.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        jLabel18.setText("Yandex Key");

        btnYandexKey.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        btnYandexKey.setText("Edit Key");
        btnYandexKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYandexKeyActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        jLabel19.setText("Logs");

        textAreaLogs.setColumns(20);
        textAreaLogs.setRows(5);
        jScrollPane2.setViewportView(textAreaLogs);

        btnTranslateAdd.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        btnTranslateAdd.setForeground(new java.awt.Color(153, 255, 0));
        btnTranslateAdd.setText("Translate & Add to Project");
        btnTranslateAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTranslateAddActionPerformed(evt);
            }
        });

        btnTranslateExport.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        btnTranslateExport.setText("Translate & Export");
        btnTranslateExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTranslateExportActionPerformed(evt);
            }
        });

        btnExportFiles.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        btnExportFiles.setText("Export Files");
        btnExportFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportFilesActionPerformed(evt);
            }
        });

        btnImportFile.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        btnImportFile.setText("Import File");
        btnImportFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportFileActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        jLabel5.setText("Language Settings (i18n)");

        textInputStr.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        textInputStr.setText("Input Language: ");

        fieldInput.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                fieldInputInputMethodTextChanged(evt);
            }
        });
        fieldInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldInputActionPerformed(evt);
            }
        });
        fieldInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldInputKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fieldInputKeyTyped(evt);
            }
        });

        textOutputStr.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        textOutputStr.setText("Output Language: ");

        btnSave.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtFileNumber))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(btnSetExport))
                                .addGap(26, 26, 26)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(btnYandexKey))
                                .addGap(37, 37, 37))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnAddFiles)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnRemoveFiles)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtExportPath))
                            .addComponent(jLabel19)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnTranslateAdd)
                                    .addComponent(btnTranslateExport))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnImportFile)
                                    .addComponent(btnExportFiles))))
                        .addGap(0, 16, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(textWordsTranslated))
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textFilesDone)
                                    .addComponent(jLabel13))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(199, 199, 199))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textStatus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton4)
                                .addGap(42, 42, 42))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(textInputStr)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fieldInput, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textOutputStr)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filedOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSave)
                        .addGap(47, 47, 47))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(txtFileNumber))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddFiles)
                            .addComponent(btnRemoveFiles))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSetExport))
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnYandexKey)))
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(44, 44, 44)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtExportPath))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textInputStr)
                    .addComponent(filedOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textOutputStr)
                    .addComponent(btnSave))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textWordsTranslated)
                            .addComponent(textFilesDone)))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textStatus)
                            .addComponent(jButton4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTranslateAdd)
                    .addComponent(btnExportFiles))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTranslateExport)
                    .addComponent(btnImportFile))
                .addGap(19, 19, 19))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFilesActionPerformed
        String inputPath= JOptionPane.showInputDialog("Please enter File Path");
        if(!checkFilePath(inputPath, false)) return;
        settings.addPath(inputPath);
        JOptionPane.showMessageDialog(this, "File added", "Info", JOptionPane.INFORMATION_MESSAGE);
        refreshGui();
    }//GEN-LAST:event_btnAddFilesActionPerformed

    private void btnRemoveFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveFilesActionPerformed
        settings.cleanPath();
        JOptionPane.showMessageDialog(this, "Files path deleted", "Info", JOptionPane.INFORMATION_MESSAGE);
        refreshGui();
    }//GEN-LAST:event_btnRemoveFilesActionPerformed

    private void btnTranslateAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTranslateAddActionPerformed
        new Thread(new TurboReader(true, false, false, this.textAreaLogs, textWordsTranslated, textFilesDone)).start();
    }//GEN-LAST:event_btnTranslateAddActionPerformed

    private void btnImportFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportFileActionPerformed
        String inputPath= JOptionPane.showInputDialog("Please enter File Path");
        if(!checkFilePath(inputPath, true)) return;
        Utils.IMPORT_FILE_PATH = inputPath;
        JOptionPane.showMessageDialog(this, "File will be saved into Export Folder", "Info", JOptionPane.INFORMATION_MESSAGE);
        new Thread(new TurboReader(false, false, true, this.textAreaLogs, textWordsTranslated, textFilesDone)).start();
        JOptionPane.showMessageDialog(this, "Import Started", "Info", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnImportFileActionPerformed

    private void btnTranslateExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTranslateExportActionPerformed
        new Thread(new TurboReader(true, true, false, this.textAreaLogs, textWordsTranslated, textFilesDone)).start();
    }//GEN-LAST:event_btnTranslateExportActionPerformed

    private void btnExportFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportFilesActionPerformed
        new Thread(new TurboReader(false, true, false, this.textAreaLogs, textWordsTranslated, textFilesDone)).start();
    }//GEN-LAST:event_btnExportFilesActionPerformed

    private void fieldInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldInputKeyTyped
        System.out.println("KEY TYPED: " + this.fieldInput.getText());
    }//GEN-LAST:event_fieldInputKeyTyped

    private void fieldInputInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_fieldInputInputMethodTextChanged
        System.out.println("TEXT CHANGED: " + this.fieldInput.getText());
    }//GEN-LAST:event_fieldInputInputMethodTextChanged

    private void fieldInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldInputActionPerformed
        System.out.println("ACTION: " + this.fieldInput.getText());
    }//GEN-LAST:event_fieldInputActionPerformed

    private void fieldInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldInputKeyPressed
        System.out.println("KEY PRESSED: " + this.fieldInput.getText());
    }//GEN-LAST:event_fieldInputKeyPressed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        settings.saveSetting("string", Utils.SETTINGS_KEY.LANG_INPUT, fieldInput.getText());
        settings.saveSetting("string", Utils.SETTINGS_KEY.LANG_OUTPUT, filedOutput.getText());
        refreshGui();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSetExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetExportActionPerformed
        String inputPath= JOptionPane.showInputDialog("Please enter File Path");
        if(!checkFilePath(inputPath, true)) return;
        checkDirectory(inputPath);
        settings.saveSetting("string", Utils.SETTINGS_KEY.OUTPUT_FOLDER, inputPath);
        JOptionPane.showMessageDialog(this, "Export Path Saved", "Info", JOptionPane.INFORMATION_MESSAGE);
        refreshGui();
    }//GEN-LAST:event_btnSetExportActionPerformed

    private void btnYandexKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYandexKeyActionPerformed
        String yandexKeyInput= JOptionPane.showInputDialog("Please enter Yandex Key");
        settings.saveSetting("string", Utils.SETTINGS_KEY.YANDEX_KEY, yandexKeyInput);
        JOptionPane.showMessageDialog(this, "Yandex Key Saved", "Info", JOptionPane.INFORMATION_MESSAGE);
        refreshGui();
    }//GEN-LAST:event_btnYandexKeyActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddFiles;
    private javax.swing.JButton btnExportFiles;
    private javax.swing.JButton btnImportFile;
    private javax.swing.JButton btnRemoveFiles;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSetExport;
    private javax.swing.JButton btnTranslateAdd;
    private javax.swing.JButton btnTranslateExport;
    private javax.swing.JButton btnYandexKey;
    private javax.swing.JTextField fieldInput;
    private javax.swing.JTextField filedOutput;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTextArea textAreaLogs;
    private javax.swing.JLabel textFilesDone;
    private javax.swing.JLabel textInputStr;
    private javax.swing.JLabel textOutputStr;
    private javax.swing.JLabel textStatus;
    private javax.swing.JLabel textWordsTranslated;
    private javax.swing.JTextArea txtAreaFilesPath;
    private javax.swing.JLabel txtExportPath;
    private javax.swing.JLabel txtFileNumber;
    // End of variables declaration//GEN-END:variables
}
