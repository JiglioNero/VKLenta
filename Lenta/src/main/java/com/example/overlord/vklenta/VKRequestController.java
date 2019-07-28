package com.example.overlord.vklenta;

import com.vk.sdk.api.VKRequest;

import java.util.ArrayList;
/**
 * Created by OverLord on 26.04.2018.
 */

public class VKRequestController {
    private static VKRequestController instance;

    private ArrayList<VKRequest> requestQueue = new ArrayList<>();
    private boolean isRunning = false;

    private VKRequestController(){
    }

    public static VKRequestController getInstance(){
        if (instance == null){
            instance = new VKRequestController();
        }
        return instance;
    }

    public void addRequest(VKRequest request){
        requestQueue.add(0,request);
        run();
    }

    private void run(){
        if (!isRunning && requestQueue.size()>0){
            isRunning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (requestQueue.size()>0) {
                        VKRequest request = requestQueue.get(requestQueue.size()-1);
                        requestQueue.remove(requestQueue.size()-1);
                        request.executeWithListener(request.requestListener);
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    isRunning = false;
                }
            }).start();
        }
    }
}
