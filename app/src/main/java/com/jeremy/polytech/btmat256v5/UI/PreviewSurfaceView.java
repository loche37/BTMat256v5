package com.jeremy.polytech.btmat256v5.UI;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class PreviewSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder mSurfaceHolder;
    SurfaceViewLoopThread mThread;
    PixelArt mpixelart;
    int width;
    int height;


    public PreviewSurfaceView(Context context){
        super(context);
        init();
    }

    public PreviewSurfaceView(Context context, AttributeSet attrs){
        super(context,attrs);

        init();
    }


    /**
     * Definit les tailles de la SurfaceView,
     * on veut 1 carré qui peut loger exactement 16 carrés dans sa largeur et sa hauteur.
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    /*
     @Override
     protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){



         int screenWidth=width-width%mpixelart.getMimageArrayWidth(); //La largeur de l'ecran n'est pas forcément un multiple de 16 donc on enlève les pixels restants.
         int screenHeight=height-height%mpixelart.getMimageArrayHeight();  //idem on enlève les pixels inutiles

         int retour=Math.min(screenHeight,screenWidth);

         setMeasuredDimension(retour, retour);
     }
    */

    /**
     * lance la methode de dessin de mimage dans le canvas de la SurfaceView.
     * @param canvas
     */
    public void doDraw(Canvas canvas){
        if(canvas==null){
            return;
        }

        mpixelart.draw(canvas);
    }

    public void update(){
        mpixelart.saveImage();
    }

    /**
     * Si la surface est modifié, on envoie les nouvelles valeurs de la taille de la surfaceView a l'image. Et on recharge
     * @param holder
     * @param format
     * @param width Largeur de l'ecran
     * @param heigth Hauteur de l'écran
     */
    public void surfaceChanged(SurfaceHolder holder,int format,int width,int heigth){
        mpixelart.resize(width,heigth);

        Canvas can=mSurfaceHolder.lockCanvas();
        doDraw(can);
        mSurfaceHolder.unlockCanvasAndPost(can);
    }

    /**
     * Action effectués a la crétion de la SurfaceView
     * on crée le thread de mise a jour de la surfaceView et on le lance si ce n'est pas déjà fait.
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder){


        Canvas can=mSurfaceHolder.lockCanvas();
        doDraw(can);
        mSurfaceHolder.unlockCanvasAndPost(can);


    }


    /**
     * Actions lorsque la surfaceView est détruite
     * on en profite pour éteindre le Thread de raffraichissement du Canvas.
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

    }


    /**
     * OnTouchEvent met à jour le pixel en fonction de là ou on
     * touche l'écran.
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){


        /*
        mpixelart.setPixel(event.getX(),event.getY());
        */

        return true;
    }


    /**
     * Appelé par les constructeurs définit les variables globales
     * mPixelArt: objet PixelArt
     * mSurfaceHolder: Holder de la surface
     */
    public void init(){
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        mpixelart= new PixelArt(16,16);
        mpixelart.setMimageArray(mpixelart.genImage());
    }

}
