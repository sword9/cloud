package com.hochan.sqlite.tools;


import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Administrator on 2016/5/12.
 */
public class DataHandler {

    public static String[][] j2string(String js){ //传入一个json,返回对应的二维数组
        JSONArray jsonarray = new JSONArray();
        JSONArray jst = new JSONArray();
        try {
            jsonarray = new JSONArray(js);
            int rownum = jsonarray.length();
            int colnum= 0;
            colnum = (jsonarray.getJSONArray(0)).length();
            String a[][] = new String[rownum][colnum];
            for (int i = 0; i < rownum; i++){
                jst = jsonarray.getJSONArray(i);
                for (int j=0;j<colnum;j++){
                    a[i][j]=jst.getString(j);
                }
            }
            return(a);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[][] jsonArray2StringArray(JSONArray jsonArray){
        try {
            int rowNum = jsonArray.length();
            int colNum = jsonArray.getJSONArray(0).length();
            String result[][] = new String[rowNum][colNum];
            for(int i = 0; i < rowNum; i++){
                JSONArray js = (JSONArray) jsonArray.get(i);
                for(int j = 0; j < colNum; j++)
                    result[i][j] = js.getString(j);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
