package com.jeremy.polytech.btmat256v5.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.jeremy.polytech.btmat256v5.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Activité qui sert à sauvegarder le motif déssiné dans l'activité Create_Pattern_Activity
 * s'affiche en tant que boite de dialogue
 * permet de saisir un nom à donner au motif.
 * sauvegarde dans un fichier texte
 */
public class PatternSaveActivity extends AppCompatActivity {

    File data;
    AppCompatEditText inputTextView;
    AppCompatButton buttonSave;
    AppCompatTextView hintPatternSave;
    String StringToSave;
    TreeMap<String,String> mDataMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_save);
        setTitle("Sauvegarder le motif");

        //On recupere l'intent donné par Create_Pattern_Activity qui contient la String du pixelArt a sauver
        Intent dataIntent=getIntent();


        //Recuperation de la chaine de caractère.
        StringToSave=dataIntent.getStringExtra(Create_Pattern_Activity.EXTRA_PIXELART_STRING);



        inputTextView=(AppCompatEditText)findViewById(R.id.PatternNameEditText);

        buttonSave=(AppCompatButton)findViewById(R.id.buttonSavePattern);

        hintPatternSave=(AppCompatTextView)findViewById(R.id.hint_SavePatternTextView);

        data=new File(this.getFilesDir().getPath()+"/"+"data.txt"); // Ouvre le fichier dans le dossier de l'application.

        mDataMap=new TreeMap<>();

        try {
            importData(data);
        } catch (FileNotFoundException e) {
            //Toast.makeText(getBaseContext(),"Pas de fichier de sauvegarde créée pour l'instant. Il sera créé lors de la première sauvegarde.",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });


    }


    /**
     * Lance la sauvegarde du motif suite a l'appui sur le bouton sauvegarder.
     * Verifie que le nom entré dans inputTextView n'est pas vide ou ne contient pas les caracteres de délimitation des String de nom et motif.
     * Affiche des Toast si les conditions ne sont pas respectés.
     * Si tout va bien, Enregistre dans le fichier data.txt le motif et son nom puis quitte l'activité.
     */
    private void doSave(){
        String name = inputTextView.getText().toString();


        if(name.length()>0){
            if(mDataMap.get(name)==null){
                if(!(name.contains("<")||name.contains(">")||name.contains("[")||name.contains("]"))){
                    try {
                        writeData(name,StringToSave);
                        Toast.makeText(getBaseContext(),"Sauvegarde réussie",Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } catch (IOException e) {
                        hintPatternSave.setText("Echec Sauvegarde, réessayez");
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getBaseContext(),"Le nom ne peut pas contenir les caractères <, >, [, ]",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getBaseContext(),"Le nom est déjà utilisé, choisir un autre nom de motif.",Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getBaseContext(),"Le nom ne peut pas être vide.",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * importData(File f) lit le fichier ligne par ligne et rempli
     * mDataMap avec Key=chaine entre "<" et ">" et Value=chaine entre "[" et "]"
     * @param f
     * @throws FileNotFoundException
     */
    void importData(File f) throws FileNotFoundException {
        Scanner sc= new Scanner(f);

        importData(sc);

        sc.close();
    }

    /**
     * importData(Scanner sc) lit le Scanner ligne par ligne et rempli
     * mDataMap avec
     * Key=chaine entre "<" et ">"
     * Value=chaine entre "[" et "]"
     * @param sc
     */
    void importData(Scanner sc){

        while(sc.hasNextLine()){
            String motif=sc.nextLine();
            String name=motif.substring(motif.indexOf("<")+1,motif.indexOf(">"));
            String value=motif.substring(motif.indexOf("[")+1,motif.indexOf("]"));
            mDataMap.put(name,value);
        }

    }

    /**
     * writeData(BufferedWriter sortie,String name,String PatternString) ecrit dans sortie tout les motifs contenus par mDataMap ligne par ligne
     * @param sortie
     * @throws IOException
     */
    void writeData(BufferedWriter sortie,String name,String PatternString) throws IOException {

        String motif= "<" + name +">[" + PatternString + "]";
        sortie.write(motif);
        sortie.newLine();
    }

    /**
     * writeData() ecrit dans le fichier data global tout les motifs contenus par mDataMap ligne par ligne
     *
     * @throws IOException
     */
    void writeData(String name,String PatternString) throws IOException {

        //FileWriter(File F,true) ecrit dans le fichier F à la suite des données qui y sont déjà.
        BufferedWriter sortie = new BufferedWriter(new FileWriter(data,true)); // Ouvre le fichier en ecriture non ecrasante

        writeData(sortie,name,PatternString);

        sortie.close();
    }

}
