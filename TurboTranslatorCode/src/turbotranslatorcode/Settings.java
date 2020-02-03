/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turbotranslatorcode;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.*;

/**
 *
 * @author nicho
 */
public class Settings {
    
    Preferences setting = Preferences.userRoot();
    
    public void saveSetting(String type, String key, String value){
        if(type.toLowerCase().equals("string")){
            setting.put(key, value);
        }
        if(type.toLowerCase().equals("bool") || type.toLowerCase().equals("boolean")){
            boolean bool = Boolean.parseBoolean(value.toLowerCase());
            setting.putBoolean(key, bool);
        }
        if(type.toLowerCase().equals("int") || type.toLowerCase().equals("integer")){
            if(value!=null){
                Integer intValue= Integer.parseInt(value);
                setting.putInt(key, intValue);
            }
        }
        if(type.toLowerCase().equals("long")){
            if(value!=null){
                Long longValue= Long.parseLong(value);
                setting.putLong(key, longValue);
            }
        }
    }
    
    public void addPath(String pathToAdd){
        String pathPref = getStringValue(Utils.PREFERNCES_PATH_KEY);
        if(pathPref!=null && pathPref.length()>1){
            pathPref += Utils.PATH_SPLITTER + pathToAdd;      //there are other path saved, add new path with splitter
        }else{
            pathPref = pathToAdd;
        }
        saveSetting("string", Utils.PREFERNCES_PATH_KEY, pathPref);
    }
    
    public void cleanPath(){
        saveSetting("string", Utils.PREFERNCES_PATH_KEY, "");
    }
    
    public List<String> getPathList(){
        String pathValue = getStringValue(Utils.PREFERNCES_PATH_KEY);
        if(pathValue!=null && pathValue.length()>2){
            return Arrays.asList(pathValue.split(Utils.PATH_SPLITTER));
        }
        return null;
    }
    
    public Class getClassType(){
        return setting.getClass();
    }
    
    public boolean getBoolValue(String key){
        return setting.getBoolean(key, false);
    }
    
    public String getStringValue(String key){
        return setting.get(key, "");
    }
    
    public Integer getIntValue(String key){
        return setting.getInt(key, -1);
    }
    
    public long getLongValue(String key){
        return setting.getLong(key, -1);
    }
    
}
