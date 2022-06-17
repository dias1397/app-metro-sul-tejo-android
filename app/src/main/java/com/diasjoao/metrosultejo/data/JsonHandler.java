package com.diasjoao.metrosultejo.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JsonHandler {

    public static String loadJSONFromAsset(InputStream is) {
        String json = null;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static int getStationId(JSONObject jsonFile, String stationName) throws JSONException {
        JSONArray stationsList = jsonFile.getJSONArray("stations");

        for (int i = 0; i < stationsList.length(); i++) {
            if (stationsList.getJSONObject(i).getString("name").equals(stationName)) {
                return Integer.parseInt(stationsList.getJSONObject(i).getString("id"));
            }
        }

        return -1;
    }

    public static int getStationNumber(JSONObject jsonFile, int lineId, int stationId, Boolean inverse) throws JSONException {
        JSONObject line = jsonFile.getJSONArray("lines").getJSONObject(lineId);
        JSONArray stationOrder = line.getJSONArray("stations");

        for (int i = 0; i < stationOrder.length(); i++) {
            if (Integer.parseInt(stationOrder.getString(i)) == stationId) {
                if (inverse == false)
                    return i;
                else
                    return (stationOrder.length() - 1) - i;
            }
        }

        return -1;
    }

    public static int getStationOffset(JSONObject jsonFile, int lineId, int stationNumber, Boolean inverse) throws JSONException {
        JSONObject line = jsonFile.getJSONArray("lines").getJSONObject(lineId);
        JSONObject lineWay = line.getJSONArray("directions").getJSONObject(inverse ? 1 : 0);
        JSONArray offsets = lineWay.getJSONArray("offsets");

        int cont = 0;

        for (int i = 0; i < stationNumber; i++) {
            cont += Integer.parseInt(offsets.getString(i));
        }

        return cont;
    }

}
