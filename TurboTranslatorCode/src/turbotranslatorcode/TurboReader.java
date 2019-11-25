/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turbotranslatorcode;

import com.google.gson.Gson;
import com.gtranslate.Language;
import com.gtranslate.Translator;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author nmarasco
 */
public class TurboReader implements Runnable{
    
    private ArrayList<String> listTranslated;
    private Settings settings;
    private Utils utils;
    private OnlineTranslator onlineTranslator;
    private boolean autoTranslate;
    private boolean translateExport;
    private boolean fileImport;
    private String toTranslateLan;
    private String originLan;
    private JTextArea textAreaLogs;
    private JLabel textWordsTranslated;
    private JLabel textFilesDone;
    private List<String> currentPathList;
    private int wordsTranslated;
    private int filesDone;
    private String outputFolder;
    private HashMap<String, String> jsonKeysValuesList;
    private ArrayList<Object[]> fileInfoList;
    

    public TurboReader(boolean autoTranslate, boolean translateExport, boolean fileImport, JTextArea textAreaLogs, JLabel textWordsTranslated, JLabel textFilesDone) {
        settings = new Settings();
        utils = new Utils();
        toTranslateLan = settings.getStringValue(Utils.SETTINGS_KEY.LANG_OUTPUT);
        originLan = settings.getStringValue(Utils.SETTINGS_KEY.LANG_INPUT);
        this.autoTranslate = autoTranslate;
        this.translateExport = translateExport;
        this.textAreaLogs = textAreaLogs;
        this.textWordsTranslated = textWordsTranslated;
        this.fileImport = fileImport;
        this.textFilesDone = textFilesDone;
        this.textWordsTranslated.setForeground(Color.blue);
        this.textFilesDone.setForeground(Color.blue);
        this.wordsTranslated = 0;
        this.filesDone = 0;
        this.textWordsTranslated.setText(String.valueOf(wordsTranslated));
        this.textFilesDone.setText(String.valueOf(filesDone));
        this.textAreaLogs.setText("");
        onlineTranslator = new OnlineTranslator();
        currentPathList = settings.getPathList();
        this.outputFolder = settings.getStringValue(Utils.SETTINGS_KEY.OUTPUT_FOLDER);
        this.fileInfoList = new ArrayList<Object[]>();
        
        //***********DEBUG PURPOSE ONLY***********
//        currentPathList = new ArrayList();
//        currentPathList.add("C:\\Users\\nmarasco\\Documents\\Progetti\\bview-3\\app_api\\i18n\\en.js");                             
//        currentPathList.add("C:\\Users\\nmarasco\\Documents\\Progetti\\bview-3\\public\\js\\i18n\\en.js");                             
//        currentPathList.add("C:\\Users\\nmarasco\\Documents\\Progetti\\bview-3\\public\\js\\i18n\\pixie\\en.js");                             
//        currentPathList.add("C:\\Users\\nmarasco\\Documents\\Progetti\\bview_chrome_extension_2\\i18n\\en.js");                             
//        currentPathList.add("C:\\Users\\nmarasco\\Documents\\Progetti\\bview_android\\app\\src\\main\\res\\values\\strings.xml");   
//        settings.saveSetting("string", Utils.SETTINGS_KEY.OUTPUT_FOLDER, "C:\\Users\\nmarasco\\Desktop\\Traduzioni Spagnolo");
//        settings.saveSetting("string", Utils.SETTINGS_KEY.LANG_OUTPUT, "it");
//        settings.saveSetting("string", Utils.SETTINGS_KEY.LANG_INPUT, "en");
        //****************************************
        
        //***********DEBUG PURPOSE ONLY***********
//        importFileBuilder("C:\\Users\\nmarasco\\Desktop\\i18n_en_js_ITA.csv", "C:\\Users\\nmarasco\\Desktop");
//        importFileBuilder("C:\\Users\\nmarasco\\Desktop\\values_strings_xml_ITA.csv", "C:\\Users\\nmarasco\\Desktop");
        //****************************************
    }
    
    @Override
    public void run() {
        
        //***********DEBUG PURPOSE ONLY***********
//        writeExcel(null, "C:\\Users\\nmarasco\\Desktop\\excelTest.xlsx", null);
        //****************************************
        
        this.textAreaLogs.append("Task started\n");
        if(fileImport){
            this.textAreaLogs.append("importing\n");
            importFileBuilder(Utils.IMPORT_FILE_PATH, outputFolder);
            this.textAreaLogs.append("File Saved on: " + outputFolder + "\n");
        }
        else{
            Iterator<String> currentPathIt = currentPathList.iterator();
            String tmpFilePath;
            while(currentPathIt.hasNext()){
                tmpFilePath = currentPathIt.next();
                this.textAreaLogs.append("file: " + tmpFilePath + "\n");
                fileSplitter(tmpFilePath);
                updateFileDone();
            }
            writeExcel("C:\\Users\\nmarasco\\Desktop\\excelTest.xlsx");
        }
        this.textAreaLogs.append("**********DONE!**********\n");
    }
    
    private void updateWordsTranslated(){
        wordsTranslated++;
        textWordsTranslated.setText(String.valueOf(wordsTranslated));
    }
    
    private void updateFileDone(){
        filesDone++;
        textFilesDone.setText(String.valueOf(filesDone));
    }
    
    private void fileSplitter(String path){
        //check extension and filter file creating 3 columns (KEY, ENGLISH, LAN_TO_TRANSLATE)
        File file = new File(path);
        String tmpLine = "", toTranslateStr = "", translatedStr = "", autoTranslatedFileName = "", firstLine = "", 
                translateComplete = "", tmpKey = "", jsonFileStr = "", sheetName = "";
        String split[];
        Object fileInfoObj[] = null;
        ArrayList<Object[]> listToWrite = new ArrayList<Object[]>();
        String fileExtension = Utils.getFileExtension(file.getPath());
        Iterator<String> keyList;
        JSONObject tmpJsonObject;
        sheetName = utils.getSheetName(filesDone, utils.getFileName(path));
        listToWrite.add(rowBuilder(Utils.KEY_COLUMN_STRING, originLan, toTranslateLan));    //initizalize first line of file/list
        if(!file.exists()){
            //*****************THROW ERROR*****************
            return;
        }
        try{
            FileReader frComList = new FileReader(path);
            BufferedReader brComList = new BufferedReader(frComList);
            switch(fileExtension){
                case Utils.SUPPORTED_FORMAT.JS:{
                    while((tmpLine = brComList.readLine())!=null){
                        if((tmpLine.contains("var") || tmpLine.contains("module")) && tmpLine.contains("=") && tmpLine.contains("{")) firstLine = tmpLine;
                        if(tmpLine.contains(":")){
                            tmpLine = "{" + tmpLine + "}";                          //add curly braces to make it a JSON object
                            tmpJsonObject = new JSONObject(tmpLine);
                            keyList = tmpJsonObject.keys();
                            while(keyList.hasNext()){
                                tmpKey = keyList.next();                            //even if a I know that is a single key obj, I must iterate it
                            }
                            tmpLine = tmpLine.substring(1, tmpLine.length()-1);     //remove curly braces
                            if(autoTranslate){
                                translatedStr = onlineTranslator.translate(tmpJsonObject.getString(tmpKey), originLan, toTranslateLan);
                                listToWrite.add(rowBuilder(tmpKey, tmpJsonObject.getString(tmpKey), translatedStr));
                                updateWordsTranslated();
                            }else{
                                listToWrite.add(rowBuilder(tmpKey, tmpJsonObject.getString(tmpKey), ""));
                            }
                        }
                        else if(!tmpLine.contains("var")){                      //if contains var means that is the first line of file
                            if(isComment(tmpLine)) 
                                listToWrite.add(rowBuilder(tmpLine, "", ""));   //if is a comment add line to list
                            else listToWrite.add(rowBuilder("", "", ""));
                        }
                    }
                    if(!fileImport && firstLine.length()>1){
                        fileInfoObj = infoRowBuilder(sheetName, firstLine, "};");
                    }
                    break;
                }
                case Utils.SUPPORTED_FORMAT.XML:{
                    firstLine = "<resources>";
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setValidating(true);
                    factory.setIgnoringElementContentWhitespace(true);
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(file);
                    Node node = document.getDocumentElement();
                    NodeList nodeList = node.getChildNodes();
                    Node tmpNode;
                    for(int row=0; row<nodeList.getLength(); row++){
                        tmpNode = nodeList.item(row);
                        NamedNodeMap nodeMap = nodeList.item(row).getAttributes();
                        if(nodeMap!=null){
                            if(nodeMap.getNamedItem("translatable")==null){
                                //if line is not translatable, it has this attribute
                                if(autoTranslate){
                                    translatedStr = onlineTranslator.translate(tmpNode.getTextContent(), originLan, toTranslateLan);
                                    listToWrite.add(rowBuilder(nodeMap.getNamedItem("name").getTextContent(), tmpNode.getTextContent(), translatedStr));
                                    updateWordsTranslated();
                                }else{
                                    listToWrite.add(rowBuilder(nodeMap.getNamedItem("name").getTextContent(), tmpNode.getTextContent(), ""));
                                }
                            }
                        }
                    }
                    fileInfoObj = infoRowBuilder(sheetName, firstLine, "</resources>");
                    break;
                }
                case Utils.SUPPORTED_FORMAT.JSON:{
                    firstLine = "{";
                    while((tmpLine = brComList.readLine())!=null){
                        //write all file in a string to convert it to a json
                        jsonFileStr += tmpLine;
                    }
                    tmpJsonObject = new JSONObject(jsonFileStr);
                    this.jsonKeysValuesList = new HashMap<String, String>();
                    jsonParser(tmpJsonObject);
                    Iterator keyIt = jsonKeysValuesList.entrySet().iterator();
                    Map.Entry tmpEntry;
                    while(keyIt.hasNext()){
                        System.out.println(tmpKey);
                        tmpEntry = (Map.Entry) keyIt.next();
                        if(autoTranslate){
                            translatedStr = onlineTranslator.translate(tmpEntry.getValue().toString(), originLan, toTranslateLan);
                            listToWrite.add(rowBuilder(tmpEntry.getKey().toString(), tmpEntry.getValue().toString(), translatedStr));
                            updateWordsTranslated();                            
                        }else{
                            listToWrite.add(rowBuilder(tmpEntry.getKey().toString(), tmpEntry.getValue().toString(), ""));
                        }
                    }
                    fileInfoObj = infoRowBuilder(sheetName, firstLine, "}");
                }
                break;
            }
            brComList.close();
            frComList.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TurboReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TurboReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TurboReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(TurboReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        fileMapToWrite.put(sheetName, listToWrite);
        this.fileInfoList.add(fileInfoObj);
    }
    
    private HashMap<String, ArrayList<Object[]>> fileMapToWrite = new HashMap<String, ArrayList<Object[]>>();
    
    
    private void jsonParser(JSONObject jsonObj){
        Iterator<String> keyListIt = jsonObj.keys();
        String tmpKey;
        Object tmpObj;
        while(keyListIt.hasNext()){
            tmpKey = keyListIt.next();
            tmpObj = jsonObj.get(tmpKey);
            if(tmpObj instanceof String) jsonKeysValuesList.put(tmpKey, (String) tmpObj);
            else if(tmpObj instanceof JSONObject) jsonParser((JSONObject) tmpObj);
        }
    }
    
    private boolean isComment(String str){
        //check if line is a comment
        if(str.contains("//") || str.contains("<!--")) return true;
        return false;
    }
    
    private void writeFile(ArrayList<String> list, String fileName, String destPath){
        try {
            FileWriter fileWriter = new FileWriter(destPath + Utils.FILE_SEPARATOR + fileName, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            Iterator<String> lineListIt = list.iterator();
            while(lineListIt.hasNext()){
                printWriter.println(lineListIt.next());
            }
            printWriter.close();
            bufferedWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(TurboReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Object[] rowBuilder(String key, String originLan, String translatedLan){
        //build a row to add to Excel file
        Object[] row = {key, originLan, translatedLan};
        return row;
    }
    
    private Object[] infoRowBuilder(String sheetName, String startLine, String lastLine){
        //build row for start and end line of file
        Object[] row = {sheetName, startLine, lastLine};
        return row;
    }
    
    private void sheetInfoManager(XSSFWorkbook workbook, ArrayList<Object[]> fileInfo){
        //manages sheet info files
        XSSFSheet infoSheet = workbook.getSheet(Utils.SHEET_INFO_NAME);
        Cell tmpCell = null;
        Row tmpRow = null;
        Iterator<Object[]> rowIterator = fileInfo.iterator();
        Object[] tmpRowObj;
        int rowCounter = 0;
        int colCounter = 0;
        if(infoSheet==null){
            infoSheet = workbook.createSheet(Utils.SHEET_INFO_NAME);       //if info sheet doen't exists, it'll create it
        }
        while(rowIterator.hasNext()){
            tmpRowObj = rowIterator.next();
            tmpRow = infoSheet.createRow(rowCounter++);
            colCounter = 0;
            for(Object field : tmpRowObj){
                tmpCell = (Cell) tmpRow.createCell(colCounter++);
                tmpCell.setCellValue((String) field);
            }
        }
    }
    
    private void writeExcel(String fileName){
        XSSFWorkbook workbook = new XSSFWorkbook();
        Iterator sheetIt = fileMapToWrite.entrySet().iterator();
        Map.Entry tmpEntry;
        while(sheetIt.hasNext()){
            tmpEntry = (Map.Entry) sheetIt.next();
            XSSFSheet sheet = workbook.createSheet(tmpEntry.getKey().toString());
            ArrayList<Object[]> tmpOBj = (ArrayList<Object[]>) tmpEntry.getValue();
            Iterator<Object[]> rowIterator = tmpOBj.iterator();
            Object[] tmpRowObj;
            int rowCounter = 0;
            int colCounter = 0;
            Row tmpRow;
            Cell tmpCell;
            while(rowIterator.hasNext()){
                tmpRowObj = rowIterator.next();
                tmpRow = sheet.createRow(rowCounter++);
                colCounter = 0;
                for(Object field : tmpRowObj){
                    tmpCell = (Cell) tmpRow.createCell(colCounter++);
                    if(field instanceof String){
                        tmpCell.setCellValue((String) field);
                    }else if(field instanceof Integer){
                        tmpCell.setCellValue((Integer) field);
                    }
                }
            }
            if(this.fileInfoList!=null) sheetInfoManager(workbook, this.fileInfoList);
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void importFileBuilder(String translatedPath, String destinationFilePath){
        //get file translated and build file back
        File fileToTranslate = new File(translatedPath);
        boolean isInfoSection = false;
        String tmpReadLine, tmpWriteLine;
        String split[];
        String importFileExtension = utils.getImportFileExtension(translatedPath);
        ArrayList<String> listTranslated = new ArrayList();
        if(!fileToTranslate.exists()){
            JOptionPane.showMessageDialog(null, "FATAL: File not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try{
            FileReader frComList = new FileReader(translatedPath);
            BufferedReader brComList = new BufferedReader(frComList);
            switch(importFileExtension){
                case Utils.SUPPORTED_FORMAT.JS:{
                    while((tmpReadLine = brComList.readLine())!=null){
                        if(isInfoSection){
                            listTranslated.add(0, tmpReadLine);
                        }
                        else if(tmpReadLine.contains(",")){
                            split = tmpReadLine.split(",");
                            if(split.length == 3){
                                split[2] = split[2].replace(Utils.ESCAPE_COMMA_CHARACTER, ",");
                                tmpWriteLine = "\t" + "\"" + split[0] + "\": " + "\"" + split[2] + "\",";
                                if(!split[0].contains(Utils.KEY_COLUMN_STRING)) listTranslated.add(tmpWriteLine);
                            }else{
                                listTranslated.add(Utils.LINE_ERROR_MESSAGE);
                            }
                        }
                        else if(tmpReadLine.contains(Utils.INFO_LINE)) isInfoSection = true;
                        else{
                            if(isComment(tmpReadLine)) listTranslated.add(tmpReadLine);    //if is a comment add line to list
                            else listTranslated.add("");                                   //if is not a comment add empty line
                        }
                    }
                    listTranslated.add("};");
                    break;
                }
                case Utils.SUPPORTED_FORMAT.XML:{
                    listTranslated.add("<resources>");
                    while((tmpReadLine = brComList.readLine())!=null){
                        if(tmpReadLine.contains(",")){
                            split = tmpReadLine.split(",");
                            if(split.length == 3){
                                split[2] = split[2].replace(Utils.ESCAPE_COMMA_CHARACTER, ",");
                                tmpWriteLine = "\t" + "<string name=\"" + split[0] + "\">" + split[2] + "</string>";
                                if(!split[0].contains(Utils.KEY_COLUMN_STRING)) listTranslated.add(tmpWriteLine);
                            }else{
                                if(isComment(tmpReadLine)) listTranslated.add(tmpReadLine.replaceAll(",", ""));   //if is a comment, remove commas
                                else listTranslated.add("");
                            }
                        }else{
                            if(isComment(tmpReadLine)) listTranslated.add(tmpReadLine);    //if is a comment add line to list
                            else listTranslated.add("");                                   //if is not a comment add empty line
                        }
                    }
                    listTranslated.add("</resources>");
                    break;
                }
            }
            writeFile(listTranslated, utils.getTranslatedFileName(translatedPath, importFileExtension), destinationFilePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TurboReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TurboReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
