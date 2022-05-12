package com.parkiezmobility.parkiez.managers;

import android.content.Context;

public class ApplicationManager {
    private static ApplicationManager manager;
    private Context context;

    public ApplicationManager() {
    }

    public static ApplicationManager getInstance(){
        if(manager==null){
           manager = new ApplicationManager();
        }
        return manager;
    }
    public void Init(Context c){
        this.context = c;
    }

    public Context getContext(){ return this.context;}
}
