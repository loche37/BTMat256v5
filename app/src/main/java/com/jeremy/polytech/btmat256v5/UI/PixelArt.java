package com.jeremy.polytech.btmat256v5.UI;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Jeremy on 08/02/2016.
 */
public class PixelArt {


    private int[][] mimageArray; //Tableau d'entier de l'image 0 ou 1 contient la description des pixels, allumé ou eteint.
    private int width; //Largeur du PixelArt en pixels
    private int height; //Hauteur du PixelArt en pixels
    private int pixHeight; //Hauteur d'un Pixel de PixelArt en pixels
    private int pixWidth; //Largeur d'un Pixel de PixelArt en pixels
    private int mimageArrayWidth; //Largeur du  tableau mimageArray
    private int mimageArrayHeight; //Hauteur du tableau mimageArray
    private Paint paintPixelInsideLight; //Paint pour dessiner les pixels allumés sur un Canvas
    private Paint paintPixelInsideUnlight; //Paint pour les pixes eteints sur le Canvas
    private Paint paintPixelOutside; //Paint pour les contours de Pixels (délimitations dans la V2)


    /**
     * Constructeur initialise les vairables.
     */
    public PixelArt(int imageArrayWidth,int imageArrayHeight){
        mimageArrayHeight =imageArrayHeight;
        mimageArrayWidth =imageArrayWidth;
        mimageArray =new int[mimageArrayHeight][mimageArrayWidth];
        //genImage(mimageArray);
        paintPixelInsideLight =new Paint();
        paintPixelInsideUnlight =new Paint();
        paintPixelOutside =new Paint();
        paintPixelInsideLight.setColor(Color.CYAN);
        paintPixelInsideUnlight.setColor(Color.LTGRAY);
        paintPixelOutside.setColor(Color.DKGRAY);
    }


    /**
     * Rempli le tableau int[][] image de 0 et 1 aléatoirement.
     *
     */
    public int[][] genImage(){
        int[][] image=new int[16][16];
        for(int y=0;y<image.length;y++){
            for(int x=0;x<image[y].length;x++){
                //image[y][x]= 0;
                image[y][x]= Math.random()<=0.5 ? 1:0 ; //Randomize Image
            }
        }
        return image;
    }

    /**
     * Dessine un rectangle à l'aide de l'image de couleur blanc si mimageArray[j][i]=1 noir sinon.
     * @param canvas
     */
    public void draw(Canvas canvas){
        Rect rOutside=new Rect();
        Rect rInside=new Rect();
        for(int y=0;y< mimageArray.length;y++){
            for(int x=0;x< mimageArray[y].length;x++){
                rOutside.set(x* pixWidth,y* pixHeight,(x+1)* pixWidth,(y+1)* pixHeight);
                rInside.set(x* pixWidth + pixWidth /20,y* pixHeight + pixHeight /20,(x+1)* pixWidth - pixWidth /20,(y+1)* pixHeight - pixWidth /20);
                if(mimageArray[y][x]==1){
                    canvas.drawRect(rOutside, paintPixelOutside);
                    canvas.drawRect(rInside, paintPixelInsideLight);
                }
                else{
                    canvas.drawRect(rOutside, paintPixelOutside);
                    canvas.drawRect(rInside, paintPixelInsideUnlight);
                }
            }
        }
    }

    /**
     * Met a jours le pixel du tableau mimageArray en fonction des coordonnées du toucher et de l'etat du PixelArt (dessiner ou effacer)
     * @param xtouch    float: coordonnée x du toucher
     * @param ytouch    float: coordonnée y du toucher
     */
    public void setPixel(float xtouch, float ytouch){

        int x=(int)xtouch/ pixWidth;    //On voit a quel pixel index on se trouve en x
        int y=(int)ytouch/ pixHeight;   //idem en y
        if(x< mimageArray[0].length && x>=0 && y< mimageArray.length && y>=0) //on verifie qu'on ne depasse pas la taille du tableau mmimage
            if(PixelArtState.STATE==PixelArtState.PENCIL)   //On regarde si on dessine ou efface
                mimageArray[y][x]=1;
            else
                mimageArray[y][x]=0;
    }


    /**
     * redimmensionne la taille du pixel art width et height et la taille des pixels pixWidth et pixHeight (rectangles)
     * en fonction des tailles de l'ecran placés en paramètres pour former un carré.
     * @param screenWidth   Largeur de l'ecran en pixels
     * @param screenHeigth  Hauteut de l'ecran en pixelsS
     */
    public void resize(int screenWidth,int screenHeigth){
        if(screenHeigth>screenWidth){
            width=screenWidth;
            height=screenWidth;
        }
        else{
            width=screenHeigth;
            height =screenHeigth;
        }


        pixWidth = width/ mimageArrayWidth;
        pixHeight = height/ mimageArrayHeight;
    }

    /**
     * efface l'image, place des 0 dans tout le tableau mimageArray.
     */
    public void eraseImage(){
        for(int y=0;y< mimageArray.length;y++) {
            for (int x = 0; x < mimageArray[y].length; x++) {
                mimageArray[y][x] = 0;
            }
        }
    }

    /**
     * Stock le tableau mimageArray dans un tableau de même taille PixelArtState.image
     */
    public void saveImage(){
        PixelArtState.image=new int[mimageArrayHeight][mimageArrayWidth];
        PixelArtState.image= mimageArray;
    }

    /**
     * Récupère le tableau PixelArtState.image et le stocke dans mimageArray. Si PixelArtState.image est vide,on lance saveImage.
     */
    public void importImage(){
        if(PixelArtState.image==null){
            saveImage();
        }

        mimageArray =PixelArtState.image;
    }

    public int[][] getMimageArray() {
        return mimageArray;
    }

    public void setMimageArray(int[][] mimageArray) {
        this.mimageArray = mimageArray;
    }

    public int getMimageArrayHeight() {
        return mimageArrayHeight;
    }

    public void setMimageArrayHeight(int mimageArrayHeight) {
        this.mimageArrayHeight = mimageArrayHeight;
    }

    public int getMimageArrayWidth() {
        return mimageArrayWidth;
    }

    public void setMimageArrayWidth(int mimageArrayWidth) {
        this.mimageArrayWidth = mimageArrayWidth;
    }

    public Paint getPaintPixelInsideLight() {
        return paintPixelInsideLight;
    }

    public void setPaintPixelInsideLight(Paint paintPixelInsideLight) {
        this.paintPixelInsideLight = paintPixelInsideLight;
    }

    public Paint getPaintPixelInsideUnlight() {
        return paintPixelInsideUnlight;
    }

    public void setPaintPixelInsideUnlight(Paint paintPixelInsideUnlight) {
        this.paintPixelInsideUnlight = paintPixelInsideUnlight;
    }

    public Paint getPaintPixelOutside() {
        return paintPixelOutside;
    }

    public void setPaintPixelOutside(Paint paintPixelOutside) {
        this.paintPixelOutside = paintPixelOutside;
    }

    /**
     * Converti un tableau a 2 dimension en String qui contient chaque valeur de du tableau de [0][0] à [n][n]
     * @param image int[][] tableau a convertir
     * @return  stringBuilder.toString()= tableau converti en chaine de caractere.
     */
    private String PixelArtToString(int[][] image){
        StringBuilder stringBuilder=new StringBuilder();
        for(int y=0;y<image.length;y++){
            for(int x=0;x<image[y].length;x++){
                stringBuilder.append(image[y][x]);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Converti la chaine "010101011...000" en tableau int[][] correspondant a l'image en pixel decrite et la place dans mimageArray
     * @param s
     */
    public void setMimageArrayFromString(String s){
        int[][] tab=mimageArray;

        for(int i=0;i<s.length();i++){
            if(s.charAt(i)=='0'){
                tab[i/mimageArray.length][i%mimageArray[0].length]=0;
            }
            else{
                tab[i/mimageArray.length][i%mimageArray[0].length]=1;
            }
        }
    }
    /**
     * Renvoie la conversion de mimageArray en String.
     * @return
     */
    public String toString(){
        return PixelArtToString(mimageArray);
    }
}



