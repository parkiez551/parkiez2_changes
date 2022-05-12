package com.parkiezmobility.parkiez.utility;

import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.parkiezmobility.parkiez.Entities.ParkingEntities;

import java.util.ArrayList;

public class JSONReader {

    private static JSONReader reader=null;

    private JSONReader(){

    }

    public static JSONReader getInstance(){
        if(reader == null){
            reader = new JSONReader();
        }
        return reader;
    }

    public ArrayList<ParkingEntities> parseParkingEntitiesResponse(String inputjson) {
        ArrayList<ParkingEntities> arrayList = new ArrayList<>();
        try
        {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(inputjson, JsonObject.class);
            JsonElement element = jsonObject.get("parkings");
            JsonArray jsonArray = element.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObj = jsonArray.get(i).getAsJsonObject();
                ParkingEntities product = gson.fromJson(jsonObj.toString(), ParkingEntities.class);
                if (!product.getLongitude().equals("null") && !product.getLatitude().equals("null")
                                        && !product.getAddress().equals("null"))
                    arrayList.add(product);
            }
        } catch(Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arrayList;
    }
}
