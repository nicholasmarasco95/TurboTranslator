/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turbotranslatorcode;

import java.io.File;

/**
 *
 * @author nmarasco
 */
public class Utils {
    
    private Settings settings;
    public static final String[] SUPPORTED_FORMAT_LIST = {SUPPORTED_FORMAT.JSON, SUPPORTED_FORMAT.XML, SUPPORTED_FORMAT.JS};
    public static final String PREFERNCES_PATH_KEY = "totranslatefile";
    public static final String PATH_SPLITTER = "--";
    public static final String OUTPUT_FILE_EXTENSION = ".csv";
    public static final String FILE_SEPARATOR = "\\";
    public static final String LINE_ERROR_MESSAGE = "***********LINE ERROR MESSAGE***********";
    public static final String ESCAPE_COMMA_CHARACTER = "|";
    public static final String INFO_LINE = "---------INFO - DO NOT TRANSLATE---------";
    public static final String KEY_COLUMN_STRING = "KEYCODE";
    public static final String SHEET_INFO_NAME = "filesInfo";
    public static final String EXPORT_FILE_NAME = "turbo_translation_export.xlsx";
    
    public static String IMPORT_FILE_PATH;

    public Utils() {
        this.settings = new Settings();
    }
    
    public static class SUPPORTED_FORMAT {
        //supported files. If you want to add a new file extension, remember to add it to SUPPORTED_FORMAT_LIST
        public static final String XML = "xml";
        public static final String JSON = "json";
        public static final String JS = "js";
    }
    
    public static class SETTINGS_KEY {
        public static final String OUTPUT_FOLDER = "uploadpath";
        public static final String LANG_OUTPUT = "lanoutput";
        public static final String LANG_INPUT = "laninput";
        public static final String DEFAULT_EXPORT_PATH = "defexpath";
        public static final String YANDEX_KEY = "yandexkey";
    }
    
    public static String getYandexKey(){
        Settings settings = new Settings();
        return settings.getStringValue(SETTINGS_KEY.YANDEX_KEY);
    }
    
    public String getSheetName(int fileNumber, String fileName){
        return fileNumber + "_" + fileName;
    }
    
    public String getImportFileExtension(String fileName){
        //estract extension of origin file
        fileName = fileName.substring(0, fileName.lastIndexOf("_"));
        return fileName.substring(fileName.lastIndexOf("_")+1, fileName.length());
    }
    
    public String getFileName(String path){
        return path.substring(path.lastIndexOf("\\")+1, path.length());
    }
    
    public String getTranslatedFileName(String path, String importFileExtension){
        String fileName = path.substring(path.lastIndexOf("\\"), path.lastIndexOf("."));
        return fileName.substring(fileName.lastIndexOf("_")+1, fileName.length()) + "." + importFileExtension;
    }
    
    public String getFilePath(String path){
        //remove file name from path
        return path.substring(0, path.lastIndexOf("\\"));
    }
    
    public static boolean isFileSupported(String extension){
        for(int i=0; i<SUPPORTED_FORMAT_LIST.length; i++){
            if(extension.equals(SUPPORTED_FORMAT_LIST[i])) return true;
        }
        return false;
    }
    
    public static String getFileExtension(String path){
        return path.substring(path.lastIndexOf(".")+1, path.length());
    }
    
    public static boolean pathExists(String path){
        return new File(path).exists();
    }
    
    public static String getExportFileName(){
        Settings settings = new Settings();
        return settings.getStringValue(Utils.SETTINGS_KEY.OUTPUT_FOLDER) + FILE_SEPARATOR + EXPORT_FILE_NAME;
    }
    
    public static String androidPathBuilder(String originalPath){ 
        if(originalPath.contains("strings.xml")){
            originalPath = originalPath.substring(0, originalPath.lastIndexOf(FILE_SEPARATOR)+1);
        }
        if(originalPath.contains("values")){
            originalPath = originalPath.substring(0, originalPath.lastIndexOf(FILE_SEPARATOR)+1);
        }
        Settings settings = new Settings();
        String folderName = "values-" + settings.getStringValue(Utils.SETTINGS_KEY.LANG_OUTPUT);
        String destPath = originalPath + folderName;
        new File(destPath).mkdir();     //generates destination folder
        return destPath;
    }
    
}
