package com.jeremy.polytech.btmat256v5.UI;

import android.graphics.Canvas;

/**
 * Created by Jeremy on 04/02/2016.
 */
public class SurfaceViewLoopThread extends Thread {

    private static int FRAMES_PER_SECOND=60;
    private static int SKIP_TICKS=1000/FRAMES_PER_SECOND;  //periode de rafraichissement en ms

    private final DrawSurfaceView view;

    private boolean running=false;


    public SurfaceViewLoopThread(DrawSurfaceView view){
        this.view=view;
    }

    public boolean getRunning(){
        return running;
    }

    public void setRunning(boolean run){
        running=run;
    }

    @Override
    public void run(){
        long startTime;
        long sleepTime;

        while(running){

            startTime=System.currentTimeMillis();

            synchronized (view.getHolder()){view.update();}

            Canvas can=null;
            try{
                can=view.getHolder().lockCanvas();
                synchronized (view.getHolder()){
                    view.doDraw(can);
                }
            }
            finally {
                if(can!=null){
                    view.getHolder().unlockCanvasAndPost(can);
                }
            }

            sleepTime= SKIP_TICKS-(System.currentTimeMillis()-startTime);

            try{
                if(sleepTime>=0)
                    Thread.sleep(sleepTime);
            }catch(Exception e){}


        }
    }


}
