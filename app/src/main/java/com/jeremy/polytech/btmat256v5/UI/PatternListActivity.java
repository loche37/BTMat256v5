package com.jeremy.polytech.btmat256v5.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
 * Activité qui gère la selection d'un motif sauvegardé
 * s'affiche sous forme de boîte de dialogue
 */
public class PatternListActivity extends AppCompatActivity {


    AppCompatTextView hintTextView;
    File data;
    ListView PatternListView;
    ArrayAdapter<String> PatternArrayAdapter;
    String noPattern="Aucun motif sauvegardé";
    TreeMap<String,String> mDataMap;
    final public static String EXTRA_PIXEL_ART_LOADED_STRING="pixel_art_loaded_string";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_list);
        setTitle("Ouvrir un motif");



        hintTextView=(AppCompatTextView)findViewById(R.id.hint_PatternListTextView);

        PatternArrayAdapter=new ArrayAdapter<String>(this,R.layout.pattern_name);

        PatternListView=(ListView) findViewById(R.id.PatternListView);

        PatternListView.setAdapter(PatternArrayAdapter);

        PatternListView.setOnItemClickListener(mPatternClickListener);

        PatternListView.setLongClickable(true);

        registerForContextMenu(PatternListView);
        /*
        PatternListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String name = ((TextView) view).getText().toString();

                if (!name.equals(noPattern)) {
                    try {
                        mDataMap.remove(name);
                        fillArrayAdapter();
                        writeData(data);
                        Toast.makeText(getBaseContext(), String.format("Motif %s supprimé.", name), Toast.LENGTH_SHORT).show();
                        return true;
                    } catch (IOException e) {
                        Toast.makeText(getBaseContext(),"Echec de la suppression.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                return false;
            }
        });
        */

        data=new File(this.getFilesDir().getPath()+"/"+"data.txt"); // Ouvre le fichier dans le dossier de l'application.

        mDataMap=new TreeMap<>();

        try {
            importData(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        fillArrayAdapter();
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        getMenuInflater().inflate(R.menu.context_menu_pattern_list, menu);
        
        AdapterView.AdapterContextMenuInfo menuInfo1 = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle(((TextView) menuInfo1.targetView).getText().toString());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_item_delete : {

                //On récupère la vue correspondant a l'élément de la liste sur lequel on a cliqué

                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

                String name = ((TextView) menuInfo.targetView).getText().toString();

                deleteAndUpdate(name);
            }
        }

        return super.onContextItemSelected(item);
    }

    /**
     * Listener de la listView, gère les actions a la selection des motifs dans la listView
     */
    private AdapterView.OnItemClickListener mPatternClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            String name = ((TextView) v).getText().toString();

            if(mDataMap.get(name)!=null){
                // Crée l'Intent de résultat et ajoute l'adresse MAC en extra
                Intent returndata = new Intent();
                returndata.putExtra(EXTRA_PIXEL_ART_LOADED_STRING, mDataMap.get(name)); //On recupere la String du motif sur lequel on a cliqué dans le tableau des motifs et on le stock dans l'intent

                // Set le resultat et finit l'activité
                setResult(RESULT_OK, returndata);
            }
            else{  //Si on a cliqué sur Aucun motif sauvegardé alors c'est qu'on a en réalité rien selectionné.
                setResult(RESULT_CANCELED);
            }

            //Termine l'activité a la suite d'un clic sur un element de la liste.
            finish();

        }
    };


    void deleteAndUpdate(String name){
        if (!name.equals(noPattern)) {
            try {
                mDataMap.remove(name);
                fillArrayAdapter();
                writeData(data);
                Toast.makeText(getBaseContext(), String.format("Motif %s supprimé.", name), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Echec de la suppression.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    /**
     * Remplit l'ArrayAdapter de la ListView
     * Le vide s'il contenaît déjà des éléments
     */
    void fillArrayAdapter(){

        //Vide l'ArrayAdapter
        PatternArrayAdapter.clear();

        //Ajoute une valeur spéciale si il n'y a aucun motifs
        if(mDataMap.size()==0){
            PatternArrayAdapter.add(noPattern);
        }else{
            for(String name : mDataMap.keySet()){
                PatternArrayAdapter.add(name);
            }
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
     * writeData(BufferedWriter sortie) ecrit dans sortie tout les motifs contenus par mDataMap ligne par ligne
     * @param sortie
     * @throws IOException
     */
    void writeData(BufferedWriter sortie) throws IOException {

        for(String name : mDataMap.keySet()){
            String motif= "<" + name +">[" + mDataMap.get(name)+ "]";
            sortie.write(motif);
            sortie.newLine();
        }
    }

    /**
     * writeData(File F) ecrit dans F tout les motifs contenus par mDataMap ligne par ligne
     * @param F
     * @throws IOException
     */
    void writeData(File F) throws IOException {

        //FileWriter(File f, false) ecrit dans le fichier f en écrasant les anciennes données
        BufferedWriter sortie = new BufferedWriter(new FileWriter(F,false)); //Ouvre F en mode ecriture et ecrit en écrasant.

        writeData(sortie);

        sortie.close();
    }
}

