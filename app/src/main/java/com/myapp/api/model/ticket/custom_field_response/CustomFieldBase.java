package com.myapp.api.model.ticket.custom_field_response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomFieldBase {
    int id;
    String name;
    String description;
    String mandatory;
    String type;
    String customFieldType;
    @SerializedName("options")
    ArrayList<ListOptions> listOptions;
    HashMap<String,Long> mapOptions;

    public CustomFieldBase(int id, String name, String description, String mandatory, String type, String customFieldType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mandatory = mandatory;
        this.type = type;
        this.customFieldType = customFieldType;
    }

    public CustomFieldBase(int id, String name, String description, String mandatory, String type, String customFieldType, ArrayList<ListOptions> listoptions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mandatory = mandatory;
        this.type = type;
        this.customFieldType = customFieldType;
        this.listOptions = listoptions;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMandatory() {
        return mandatory;
    }

    public String getType() {
        return type;
    }

    public String getCustomFieldType() {
        return customFieldType;
    }

    public ArrayList<ListOptions> getListOptions() {
        return listOptions;
    }
    public void setMapList(){
        mapOptions = new HashMap<>();
        for(int i = 0; i < listOptions.size();i++){
            mapOptions.put(listOptions.get(i).name,listOptions.get(i).id);
        }
    }
    public long getOptionId(String key){
        if (mapOptions ==null)
            return -1L;
        return mapOptions.get(key);
    }
}
