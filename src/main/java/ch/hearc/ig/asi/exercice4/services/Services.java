/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.ig.asi.exercice4.services;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/**
 *
 * @author dimitri.mella classe de Services qui contacte les ressources REST
 */
public class Services {

    /**
     * Méthode permettant de convertir des données WGS84 en MN95
     * @param est latitude au format WGS84
     * @param nord longitude au format WGS84
     * @param high altitude au format WGS84
     * @return Une map avec les données au format MN95
     */
    public static Map<String, String> convertWGS84ToMN95(String est, String nord, String high) {
        try {
            HashMap<String, String> mN95Output = new HashMap();
            DefaultHttpClient httpClient = new DefaultHttpClient();
            StringBuilder sb = new StringBuilder("http://geodesy.geo.admin.ch/reframe/wgs84tolv95?");
            sb.append("easting=").append(est);
            sb.append("&northing=").append(nord);
            sb.append("&altitude=").append(high);
            sb.append("&format=json"); // Création de l'URL afin de contacter le service REST
            
            HttpGet getRequest = new HttpGet(
                    sb.toString());
            getRequest.addHeader("accept", "application/json");
            
            HttpResponse response = httpClient.execute(getRequest); // Envoie de la demande au service REST
            
            // Controle de la réponse du serveur.
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            
            // Instanciation du buffer
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
            
            JSONObject jsonObject = new JSONObject(br.readLine());
            mN95Output.put("altitude", jsonObject.get("altitude").toString());
            mN95Output.put("easting", jsonObject.get("easting").toString());
            mN95Output.put("northing", jsonObject.get("northing").toString());
            
            httpClient.getConnectionManager().shutdown(); // Fermeture de la connexion
            
            return mN95Output;
            
        } catch (IOException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Méthode permettant d'ouvrir le navigateur et d'afficher la destination sur Google Maps
     * @param est latitude au format WGS84
     * @param nord lontitude au format WGS84
     * @param high altitude au format WGS84
     */
    public static void openGoogleMap(String est, String nord, String high){
        try {
            StringBuilder sb = new StringBuilder("https://www.google.ch/maps/dir//");
            sb.append(nord).append(",");
            sb.append(est).append("/@");
            sb.append(nord).append(",");
            sb.append(est).append(",");
            sb.append(high).append("m/data=!3m1!1e3?hl=fr");
            URI uri = new URI(sb.toString());
            Desktop d = Desktop.getDesktop();
            
            d.browse(uri); // Ouverture du navigateur
            
            
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
