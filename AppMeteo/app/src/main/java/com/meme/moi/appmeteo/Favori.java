package com.meme.moi.appmeteo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Skyro on 18/03/2015.
 */
public class Favori {
    private String ville;
    private String longitude;
    private String latitude;
    private String stringMeteo;

    public Favori(String meteo) {
        this.stringMeteo = meteo;

        String[] infoSemaine = meteo.split("!");
        String[] infoBase = infoSemaine[0].split(";");
        ville = infoBase[0];
        longitude = infoBase[1];
        latitude = infoBase[2];

    }

    public Favori(String ville, String longitude, String latitude) {
        this.ville = ville;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStringMeteo() {
        return stringMeteo;
    }

    public void setStringMeteo(String stringMeteo) {
        this.stringMeteo = stringMeteo;
    }

    public String getTemperatureToday(){
        return stringMeteo.split("!")[1].split(";")[1];
    }

    @Override
    public String toString() {
        return "Favori{" +
                "ville='" + ville + '\'' +
                '}';
    }

    public String getSkyToday(){
        if (Boolean.getBoolean(stringMeteo.split("!")[1].split(";")[2])){
            return "snow";
        } else if (Boolean.getBoolean(stringMeteo.split("!")[1].split(";")[3])){
            return "rain";
        } else if (Boolean.getBoolean(stringMeteo.split("!")[1].split(";")[4])){
            return  "cloud";
        } else {
            return "sun";
        }
    }
}


