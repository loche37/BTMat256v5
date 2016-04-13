package com.jeremy.polytech.btmat256v5.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.jeremy.polytech.btmat256v5.R;


/**
 * Cette classe correspond a l'activité permettant de désinner un motif.
 * Elle est ratachée au Layout :
 * activity_create_pattern
 */
public class Create_Pattern_Activity extends AppCompatActivity {


    /**
     * mDrawSurfaceView est la surface de dessin contenant le cadrillage
     */
    DrawSurfaceView mDrawSurfaceView;
    /**
     * mDrawSwitch est le Switch permettant de dessiner ou non.
     */
    SwitchCompat mDrawSwitch;
    /**
     * EraseAllButton est le bouton servant à effacer tout le dessin de mDrawSurfaceView
     */
    AppCompatButton EraseAllButton;
    /**
     * Code désignant permettant d'identifier la chaine du pixelArt dessiné dans
     * mDrawSurfaceView pour la passer en extra des Intents
     */
    public static String EXTRA_PIXELART_STRING="pixelart_string";
    /**
     * Code désignant l'activité PatternSaveActivity lors de demandes d'Intent
     */
    public static final int REQUEST_CODE_PATTERNSAVEACTIVITY =400;
    /**
     * Code désignant l'activité PatternListActivity lors de demandes d'Intent
     */
    public static final int REQUEST_CODE_PATTERNLISTACTIVITY=500;


    /**
     * onCreate() est éxécuté au lancement de l'activité, on initialise tout les éléments graphiques ici.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Surface_LayoutView=new DrawSurfaceView(this);
        setContentView(R.layout.activity_create_pattern);
        setTitle(R.string.activity_create_pattern_title);


        mDrawSurfaceView= (DrawSurfaceView)findViewById(R.id.PixelArtSurfaceView);

        EraseAllButton=(AppCompatButton)findViewById(R.id.btn_erase_pixelart);

        mDrawSwitch =(SwitchCompat)findViewById(R.id.DrawSwitch);

        //On met l'etat du switch a l'etat de PixelArtState
        mDrawSwitch.setChecked(((PixelArtState.STATE == PixelArtState.PENCIL) ? true : false));
        mDrawSwitch.setText((PixelArtState.STATE == PixelArtState.PENCIL) ? R.string.switch_drawing_text_enable : R.string.switch_drawing_text_disable);


        mDrawSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PixelArtState.STATE = PixelArtState.PENCIL;
                    mDrawSwitch.setText(R.string.switch_drawing_text_enable);
                } else if (!mDrawSwitch.isChecked()) {
                    PixelArtState.STATE = PixelArtState.ERASER;
                    mDrawSwitch.setText(R.string.switch_drawing_text_disable);
                }

            }
        });


        EraseAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawSurfaceView.mpixelart.eraseImage();
            }
        });

    }

    /**
     * onDestroy() est executé lorsque l'activité est fermée
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * onCreateOptionsMenu() est executé lors l'ouverture de l'activité et construit le menu et boutons d'actions
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.create_pattern_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Gere les actions du menu et la barre d'action de l'activité d'acceuil
     * @param item: item du menu.
     * @return boolean: true si évènement geré.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.menu_item_return_pattern:
                onMenuReturnPattern();
                return true;
            case R.id.menu_item_open_pattern:
                onMenuOpenPattern();
                return true;
            case R.id.menu_item_save_pattern:
                onMenuSavePattern();
                return true;
            case R.id.menu_item_close_pattern:
                onMenuClosePattern();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Ferme l'activité en signalant qu'on la annulé avec le resultat RESULT_CANCELED
     */
    private void onMenuClosePattern(){
        setResult(RESULT_CANCELED);
        finish();
    }
    /**
     * Ferme l'activité en renvoyant la string du pixel Art
     */
    private void onMenuReturnPattern(){
        Intent data=new Intent();
        data.putExtra(EXTRA_PIXELART_STRING, mDrawSurfaceView.mpixelart.toString());
        setResult(RESULT_OK,data);
        finish();
    }

    /**
     * Ouvre l'activité de selection du motif.
     */
    private  void onMenuOpenPattern(){
        Intent goToOpenPattern=new Intent(Create_Pattern_Activity.this,PatternListActivity.class);
        startActivityForResult(goToOpenPattern, REQUEST_CODE_PATTERNLISTACTIVITY);
    }

    /**
     * Ouvre l'activité de sauvegarde du motif.
     */
    private void onMenuSavePattern(){
        Intent goToSavePattern=new Intent(Create_Pattern_Activity.this,PatternSaveActivity.class);
        goToSavePattern.putExtra(EXTRA_PIXELART_STRING,mDrawSurfaceView.mpixelart.toString());
        startActivityForResult(goToSavePattern, REQUEST_CODE_PATTERNSAVEACTIVITY);
    }

    /**
     * onActivityResult() est appelée pour gérer l'Intent de retour envoyé par PatternListActivity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_PATTERNLISTACTIVITY:
                onPatternListActivityReturn(resultCode,data);
                break;
        }
    }

    /**
     * onPatternListActivityReturn() est appelée par onActivityResult si le code correspond à celui de PatternListActivity
     * @param resultCode
     * @param data
     */
    private void onPatternListActivityReturn(int resultCode,Intent data){
        if(resultCode==RESULT_OK){
            String pixelartstring=data.getStringExtra(PatternListActivity.EXTRA_PIXEL_ART_LOADED_STRING);
            mDrawSurfaceView.mpixelart.setMimageArrayFromString(pixelartstring);
        }
    }
}
