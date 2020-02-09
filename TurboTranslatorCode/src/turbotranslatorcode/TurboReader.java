/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turbotranslatorcode;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
    
    private Settings settings;
    private Utils utils;
    private OnlineTranslator onlineTranslator;
    private boolean autoTranslate;
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
    private HashMap<String, ArrayList<Object[]>> fileMapToWrite = new HashMap<String, ArrayList<Object[]>>();
    private ArrayList<Sheet> sheetList;
    private ArrayList<Object[]> sheetInfoRows;
    

    public TurboReader(boolean autoTranslate, boolean fileImport, JTextArea textAreaLogs, JLabel textWordsTranslated, JLabel textFilesDone) {
        settings = new Settings();
        utils = new Utils();
        toTranslateLan = settings.getStringValue(Utils.SETTINGS_KEY.LANG_OUTPUT);
        originLan = settings.getStringValue(Utils.SETTINGS_KEY.LANG_INPUT);
        this.autoTranslate = autoTranslate;
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
    }
    
    @Override
    public void run() {
        this.textAreaLogs.append("Task started\n");
        if(fileImport){
            this.textAreaLogs.append("importing\n");
            readSheetExcelFile(Utils.IMPORT_FILE_PATH);
            Iterator<Sheet> itSheet = sheetList.iterator();
            while(itSheet.hasNext()){
                Sheet tmpSheet = itSheet.next();
                if(!tmpSheet.equals(Utils.SHEET_INFO_NAME)){
                    buildFileImport(tmpSheet);
                }
            }
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
            writeExcel(Utils.getExportFileName());
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
        //check extension and filter file creating 3 columns (KEY, LAN_FROM, LAN_TO_TRANSLATE)
        File file = new File(path);
        String tmpLine = "", translatedStr = "", firstLine = "", tmpKey = "", jsonFileStr = "", sheetName = "";
        Object fileInfoObj[] = null;
        ArrayList<Object[]> listToWrite = new ArrayList<Object[]>();
        String fileExtension = Utils.getFileExtension(file.getPath());
        Iterator<String> keyList;
        JSONObject tmpJsonObject;
        sheetName = utils.getSheetName(filesDone, utils.getFileName(path));
        listToWrite.add(rowBuilder(Utils.KEY_COLUMN_STRING, originLan, toTranslateLan));    //initizalize first line of file/list
        if(!file.exists()){
            JOptionPane.showMessageDialog(null, "Path not found, skipping file: " + path, "Error", JOptionPane.ERROR);
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
                        fileInfoObj = infoRowBuilder(sheetName, firstLine, "};", path);
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
                    fileInfoObj = infoRowBuilder(sheetName, firstLine, "</resources>", path);
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
                    fileInfoObj = infoRowBuilder(sheetName, firstLine, "}", path);
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
    
    private Object[] infoRowBuilder(String sheetName, String startLine, String lastLine, String originalFilePath){
        //build row for start and end line of file
        Object[] row = {sheetName, startLine, lastLine, originalFilePath};
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
    
    private void writeExcel(String filePath){
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
            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            System.err.println("Fatal, FileNotFoundException: " + e);
        } catch (IOException e) {
            System.err.println("Fatal, IOException: " + e);
        }
    }
    
    private void readSheetInfo(Sheet sheetInfo){
        //read sheetInfo and populate sheet Object List
        Iterator<Row> rowIt = sheetInfo.iterator();
        sheetInfoRows = new ArrayList();
        Object[] rowObj;
        Cell tmpCell;
        int cellCounter;
        while(rowIt.hasNext()){
            Row row = rowIt.next();
            Iterator<Cell> cellIt = row.iterator();
            rowObj = new Object[4];
            cellCounter = 0;
            while(cellIt.hasNext()){
                //iterate cells and put the value into a Object[]
                tmpCell = cellIt.next();
                rowObj[cellCounter] = tmpCell.getStringCellValue();
                cellCounter++;
            }
            sheetInfoRows.add(rowObj);  //add cell object into row ArrayList
        }
    }
    
    private void readSheetExcelFile(String filePath){
        //read file's sheets and place them into an ArrayList
        FileInputStream excelFile = null;
        try {
            sheetList = new ArrayList();
            Sheet tmpSheet;
            excelFile = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(excelFile);
            int sheetCount = workbook.getNumberOfSheets();
            for(int sheetCounter = 0; sheetCounter<sheetCount; sheetCounter++){
                //iterate file sheets and fill sheetList
                tmpSheet = workbook.getSheetAt(sheetCounter);
                sheetList.add(tmpSheet);        //add sheet to list
                if(tmpSheet.getSheetName().equals(Utils.SHEET_INFO_NAME)) readSheetInfo(tmpSheet);  //if is sheet info, call method to read and populate sheet row array
                
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TurboReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TurboReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                excelFile.close();
            } catch (IOException ex) {
                Logger.getLogger(TurboReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void buildFileImport(Sheet sheet){
        //build file that can be imported into project
        String fileBegin = "", fileEnd = "", lineToWrite = "", originalDestPath = "", fileName = "";
        ArrayList<String> listRowToWrite = new ArrayList<String>();
        Iterator<Object[]> rowListIt = sheetInfoRows.iterator();
        Object[] infoRowObj;
        Row tmpRow;
        Cell tmpCell;
        Iterator<Row> rowIt = sheet.iterator();
        while(rowListIt.hasNext()){
            infoRowObj = rowListIt.next();
            if(infoRowObj[0].equals(sheet.getSheetName())){
                fileBegin = infoRowObj[1].toString();           //get file start from col[1]
                fileEnd = infoRowObj[2].toString();             //get file end from col[2]
                originalDestPath = utils.getFilePath(infoRowObj[3].toString());    //get file end from col[3]
                break;                                      //if row that has col [0] == to file name, break (row found)
            }
        }
        listRowToWrite.add(fileBegin);
        switch(Utils.getFileExtension(sheet.getSheetName())){
            case(Utils.SUPPORTED_FORMAT.JS):{
                fileName = settings.getStringValue(Utils.SETTINGS_KEY.LANG_OUTPUT) + ".js";
                while(rowIt.hasNext()){
                    tmpRow = rowIt.next();
                    if((tmpRow.getCell(0).getCellTypeEnum() == CellType.STRING && tmpRow.getCell(0).getStringCellValue().length()<2) 
                            || isComment(tmpRow.getCell(0).getStringCellValue())) listRowToWrite.add(tmpRow.getCell(0).getStringCellValue());      //if is a comment, add first cell as a line
                    else{
                        lineToWrite = "\t\"" + tmpRow.getCell(0).getStringCellValue() + "\": \"";
                        if(tmpRow.getPhysicalNumberOfCells()>2 && tmpRow.getCell(2)!=null){
                            tmpCell = tmpRow.getCell(2);
                            if(tmpCell.getCellTypeEnum() == CellType.STRING) lineToWrite += tmpCell.getStringCellValue();
                            else if(tmpCell.getCellTypeEnum() == CellType.NUMERIC) lineToWrite += tmpCell.getNumericCellValue();
                        }else{
                            tmpCell = tmpRow.getCell(1);                        //somehow the line wasn't translated
                            if(tmpCell.getCellTypeEnum() == CellType.STRING) lineToWrite += tmpCell.getStringCellValue();
                            else if(tmpCell.getCellTypeEnum() == CellType.NUMERIC) lineToWrite += tmpCell.getNumericCellValue();
                        }
                        lineToWrite += "\",";
                        listRowToWrite.add(lineToWrite);
                    }
                }
                break;
            }
            case(Utils.SUPPORTED_FORMAT.XML):{
                fileName = "strings.xml";
                originalDestPath = Utils.androidPathBuilder(originalDestPath);      //with Android folder destination must be changed since it's based on folder and not on files
                while(rowIt.hasNext()){
                    tmpRow = rowIt.next();
                    if((tmpRow.getCell(0).getCellTypeEnum() == CellType.STRING && tmpRow.getCell(0).getStringCellValue().length()<2) 
                            || isComment(tmpRow.getCell(0).getStringCellValue())) listRowToWrite.add(tmpRow.getCell(0).getStringCellValue());      //if is a comment, add first cell as a line
                    else{
                        lineToWrite = "\t<string name=\"";
                        lineToWrite += tmpRow.getCell(0).getStringCellValue() + "\">";
                        if(tmpRow.getPhysicalNumberOfCells()>2 && tmpRow.getCell(2)!=null){
                            tmpCell = tmpRow.getCell(2);
                            if(tmpCell.getCellTypeEnum() == CellType.STRING) lineToWrite += tmpCell.getStringCellValue();
                            else if(tmpCell.getCellTypeEnum() == CellType.NUMERIC) lineToWrite += tmpCell.getNumericCellValue();  
                        }else{
                            tmpCell = tmpRow.getCell(1);                        //somehow the line wasn't translated
                            if(tmpCell.getCellTypeEnum() == CellType.STRING) lineToWrite += tmpCell.getStringCellValue();
                            else if(tmpCell.getCellTypeEnum() == CellType.NUMERIC) lineToWrite += tmpCell.getNumericCellValue();
                        }
                        lineToWrite += "</string>";
                        listRowToWrite.add(lineToWrite);
                    }
                }
                break;
            }
            case(Utils.SUPPORTED_FORMAT.JSON):{
                JOptionPane.showMessageDialog(null, "JSON import is not supported!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        listRowToWrite.add(fileEnd);
        if(new File(originalDestPath).exists()){
            writeFile(listRowToWrite, fileName, originalDestPath);
            this.textAreaLogs.append("File Saved on: " + originalDestPath + "\n");
        }else if(originalDestPath.length()>2){
            //if file path got from filesInfo sheet doesn't exists, export to OUTPUT_FOLDER
            String defaultExportPath = settings.getStringValue(Utils.SETTINGS_KEY.OUTPUT_FOLDER);
            JOptionPane.showMessageDialog(null, "Original File Path doen's exists, exporting to: " + defaultExportPath, "Warning", JOptionPane.WARNING_MESSAGE);
            if(!Utils.pathExists(defaultExportPath)){
                JOptionPane.showMessageDialog(null, "Path not found, aborting", "Error", JOptionPane.ERROR);
                return;
            }
            writeFile(listRowToWrite, fileName, defaultExportPath);
            this.textAreaLogs.append("File Saved on: " + defaultExportPath + "\n");
        }
    }
    
}
