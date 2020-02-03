/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turbotranslatorcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author nmarasco
 */
public class OnlineTranslator {
    
    public String translate(String textToTranslate, String originLan, String translateLan){
        URL url;
        StringBuilder sb = new StringBuilder();
        try {
            String text = URLEncoder.encode(textToTranslate, "UTF-8");
            url = new URL("https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + Utils.getYandexKey() + "&text=" + text 
                    + "&lang=" + originLan + "-" + translateLan);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST");          // PUT is another valid option
            http.setDoOutput(true);
            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
              sb.append(output);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(OnlineTranslator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OnlineTranslator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return textExtractor(sb.toString());
    }
    
    private String textExtractor(String strToFilter){
        if(!strToFilter.contains("text")) return "";
        return strToFilter.substring(strToFilter.indexOf("[\"")+2, strToFilter.indexOf("]")-1);
    }
}
