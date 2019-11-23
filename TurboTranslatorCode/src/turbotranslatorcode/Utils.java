/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turbotranslatorcode;

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
    }
    
    public String exportFileNameCreator(String path, String originExtension){
        //this will create the name of file that'll be exported. It includes FOLDER_ORIGINFILENAME_ORIGINEXTENSION_LANG.EXTENSION
        return getFileDirectory(path) + "_" + getFileName(path) + "_" + originExtension + "_" 
                + settings.getStringValue(SETTINGS_KEY.LANG_OUTPUT) + OUTPUT_FILE_EXTENSION;
    }
    
    public String getImportFileExtension(String fileName){
        //estract extension of origin file
        fileName = fileName.substring(0, fileName.lastIndexOf("_"));
        return fileName.substring(fileName.lastIndexOf("_")+1, fileName.length());
    }
    
    private String getFileDirectory(String path){
        path = path.substring(0, path.lastIndexOf("\\"));                   //remove file name
        return path.substring(path.lastIndexOf("\\")+1, path.length());     //get and return directory
    }
    
    private String getFileName(String path){
        return path.substring(path.lastIndexOf("\\")+1, path.lastIndexOf("."));
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
    
}
