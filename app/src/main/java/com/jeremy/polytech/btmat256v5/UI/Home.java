package com.jeremy.polytech.btmat256v5.UI;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.jeremy.polytech.btmat256v5.BT.BtInterface;
import com.jeremy.polytech.btmat256v5.BT.DeviceListActivity;
import com.jeremy.polytech.btmat256v5.R;

/**
 * Cette activité est la page d'accueil de l'application
 */

public class Home extends AppCompatActivity {


    /**
     * Graphic objects déclaration
     */
    AppCompatButton btnCreateNewPattern;
    AppCompatButton btnConnectBTDevice;
    AppCompatTextView mAdressTextView;
    AppCompatTextView mPixelArtStringTextView;
    AppCompatTextView mDeviceNameTextView;
    AppCompatTextView mPatternReadyTextView;
    AppCompatButton btnSend;
    AppCompatSeekBar mIntensitySeekBar;
    AppCompatTextView IntensityValueTextView;
    AppCompatSeekBar mBlinkSpeedSeekBar;
    AppCompatTextView BlinkSpeedValueTextView;
    RadioGroup mEffectRadioGroup;

    /**
     * Boolean d'autorisations
     */
    boolean pattern_readyFlag=false;
    boolean connectedFlag=false;
    boolean can_connectFlag =false;

    /**
     * Codes pour le Handler
     */
    public static final int CONNECTED_CODE=1;
    public static final int DISCONNECTED_CODE=2;
    public static final int CONNECTION_FAILED_CODE=3;
    public static final int DISCONNECTION_FAILED_CODE=4;

    /**
     * Global String déclaration
     */
    String mDeviceInfos="Aucun";
    String mDeviceName;
    String mAdressMAC;
    String mPixelArtString;

    /**
     * Global REQUEST_Codes pour les Intents
     */
    public static final int REQUEST_CODE_DEVICELISTACTIVITY=100;
    public static final int REQUEST_CODE_CREATEPATTERNACTIVITY=200;
    public static final int REQUEST_ENABLE_BT=300;

    /**
     * Bluetooth Communication
     */
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mBTDevice;



    /**
     *  Service Bluetooth.
     */
    private BtInterface mBtInterface=null;



    /**
     * Programme principal: s'éxecute au démarrage de l'application.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter==null) {
            noBTQuit();
        }


        mAdressTextView=(AppCompatTextView)findViewById(R.id.ADRESS_TEXT_VIEW);

        btnCreateNewPattern=(AppCompatButton)findViewById(R.id.BTN_CREATE_NEW_MOTIF);

        mPixelArtStringTextView=(AppCompatTextView)findViewById(R.id.PIXEL_ART_TEXT_VIEW);

        mDeviceNameTextView =(AppCompatTextView) findViewById(R.id.DEVICE_NAME_TEXT_VIEW);

        mPatternReadyTextView =(AppCompatTextView)findViewById(R.id.AVAILABLE_PATTERN_TEXT_VIEW);

        btnConnectBTDevice=(AppCompatButton)findViewById(R.id.BTN_CONNECT_DEVICE);

        IntensityValueTextView=(AppCompatTextView)findViewById(R.id.intensity_value_TEXT_VIEW);

        BlinkSpeedValueTextView=(AppCompatTextView)findViewById(R.id.blinkSpeed_value_TEXT_VIEW);

        mIntensitySeekBar=(AppCompatSeekBar)findViewById(R.id.seekbar_intensity);

        mBlinkSpeedSeekBar =(AppCompatSeekBar)findViewById(R.id.seekbar_blinkSpeed);

        btnSend=(AppCompatButton)findViewById(R.id.BUTTON_SEND);

        mEffectRadioGroup=(RadioGroup)findViewById(R.id.radioGroup_effect);

        mEffectRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(connectedFlag){
                    String code;
                    switch (checkedId){
                        case R.id.radioButton_blinkEffect:
                            code="C";
                            break;
                        case R.id.radioButton_breathEffect:
                            code="F";
                            break;
                        case R.id.radioButton_staticEffect:
                            code="N";
                            break;
                        case R.id.radioButton_scrollDownEffect:
                            code="DD";
                            break;
                        case R.id.radioButton_scrollLeftEffect:
                            code="DL";
                            break;
                        case R.id.radioButton_scrollRightEffect:
                            code="DR";
                            break;
                        case R.id.radioButton_scrollUpEffect:
                            code="DU";
                            break;
                        default: code="N";
                            break;
                    }
                    mBtInterface.sendData(code);
                }

            }
        });
        

        btnCreateNewPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCreateNewPattern();
            }
        });

        btnConnectBTDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtConnectButtonPressed();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendButtonPressed();
            }
        });

        mIntensitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                IntensityValueTextView.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if(connectedFlag){
                    char code;
                    switch (seekBar.getProgress()){
                        case 0:code='0';
                            break;
                        case 1:code='1';
                            break;
                        case 2:code='2';
                            break;
                        case 3:code='3';
                            break;
                        case 4:code='4';
                            break;
                        case 5:code='5';
                            break;
                        case 6:code='6';
                            break;
                        case 7:code='7';
                            break;
                        case 8:code='8';
                            break;
                        case 9:code='9';
                            break;
                        case 10:code='A';
                            break;
                        case 11:code='B';
                            break;
                        case 12:code='C';
                            break;
                        case 13:code='D';
                            break;
                        case 14:code='E';
                            break;
                        case 15:code='F';
                            break;
                        default:code='F';
                            break;
                    }
                    mBtInterface.sendData("I" + code);
                }

            }
        });

        mBlinkSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BlinkSpeedValueTextView.setText(progress+1+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (connectedFlag) {
                    mBtInterface.sendData("V" + seekBar.getProgress());
                }
            }
        });

        updateButtonsAndTextViews();
    }

    /**
     * Au demarrage de l'application, on vérifie que l'appareil est compatible bluetooth et on l'active si c'est le cas. sinon on quitte l'appli.
     */
    @Override
    protected void onStart() {
        super.onStart();

        //Activation du bluetooth si ce n'est deja fait.
        if(!mBluetoothAdapter.isEnabled()){ //A t on  le Bluetooth d'activé ?
                Intent enableBT=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBtInterface != null) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //On eteind le service Bluetooth à l'arret de l'application
        if (mBtInterface != null) {
            mBtInterface.close();
        }
    }

    /**
     * Instancie le menu et la barre d'action depuis le xml pour les rendre visible dans l'application.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Gere les actions du menu et la barre d'action de l'activité d'acceuil
     * @param item: item du menu.
     * @return boolean: true si évènement geré.
     */
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.home_menu_open_bt_connexion:
                navigateToBtDeviceSelectionDialog();
                return true;

            case R.id.home_menu_open_settings:
                navigateToSettings();
                return true;
            case R.id.home_menu_help:
                navigateToHelp();
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateToHelp(){
        Intent goToHelp=new Intent(Home.this, HelpActivity.class);
        startActivity(goToHelp);
    }

    /**
     * lance la fenetre de paramètres.
     */
    private void navigateToSettings() {
    }

    /**
     * lance la fenetre de connexion bluetooth
     */
    private void navigateToBtDeviceSelectionDialog() {
        Intent goToBT=new Intent(Home.this, DeviceListActivity.class);
        startActivityForResult(goToBT, REQUEST_CODE_DEVICELISTACTIVITY);

    }

    /**
     * Cette méthode lance la fenetre Creer un nouveau motif
     */
    private void navigateToCreateNewPattern() {
        Intent goToPattern=new Intent(Home.this, Create_Pattern_Activity.class);
        startActivityForResult(goToPattern, REQUEST_CODE_CREATEPATTERNACTIVITY);
    }


    /**
     * Listener pour les Intent demandés.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_CODE_DEVICELISTACTIVITY:   //Si une reponse arrive de l'activité DeviceListActivity
                onDeviceListActivityReturn(resultCode,data);
                break;
            case REQUEST_CODE_CREATEPATTERNACTIVITY:    //Si une reponse arrive de l'activité CreatePatternActivity
                onCreatePatternActivityReturn(resultCode,data);
                break;
            case REQUEST_ENABLE_BT:
                onBtRequestActivityReturn(resultCode,data);
                break;
        }
    }

    /**
     * Fermeture de l'application lorsque pas de bluetooth
     */
    private void noBTQuit(){
        Toast.makeText(this,R.string.not_bt_device,Toast.LENGTH_LONG).show();   //On indique a l'utilisateur qu'il ne peut pas utiliser l'appli sans BT.
        finish();
    }


    /**
     * Action au retour de l'activité DeviceListActivity
     * @param resultCode
     * @param data
     */
    private void onDeviceListActivityReturn(int resultCode, Intent data){
        if (resultCode == RESULT_OK) {

            mDeviceInfos = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_INFOS);

            //Recuperation du nom de l'appareil à partir des devicesInfos
            mDeviceName= mDeviceInfos.substring(0, mDeviceInfos.length() - 17 - 2);  //mdeviceInfos.length()-17 , enleve l'adresse MAC , -2 enleve \n retour a la ligne.
            //Recuperation de l'adresse MAC à partir de deviceInfos
            mAdressMAC = mDeviceInfos.substring(mDeviceInfos.length() - 17);

            setmBTDeviceByMAC(mAdressMAC);

            //Si un service bluetooth existe déjà on l'eteint et on le supprime.
            if(mBtInterface!=null){
                mBtInterface.close();
                mBtInterface=null;
            }

            //si un service de bluetooth n'existe pas on le crée avec le nouveau BTdevice.
            if(mBtInterface==null){
                mBtInterface=new BtInterface(mBTDevice,handlerStatus);

                //Quand le l'interface de bluetooth est créée, on active le bouton de connexion.
                can_connectFlag=true;
                btnConnectBTDevice.setEnabled(can_connectFlag);
            }

            updateButtonsAndTextViews();    //On rafraichit l'etat des boutons

        }

        if (resultCode == RESULT_CANCELED) {
            mAdressTextView.setText("Activité annulée");
        }

    }

    /**
     * Action au retour de l'activité CreatePatternActivity
     * @param resultCode
     * @param data
     */
    private void onCreatePatternActivityReturn(int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            mPixelArtString=data.getStringExtra(Create_Pattern_Activity.EXTRA_PIXELART_STRING);
            mPixelArtStringTextView.setText(mPixelArtString);

            mPatternReadyTextView.setText("Oui");
            pattern_readyFlag=true;
            if(connectedFlag){
                mBtInterface.sendPattern(mPixelArtString);
            }

        }
        updateButtonsAndTextViews();
    }

    /**
     * Action au retour de l'activité BtRequestActivity
     * @param resultCode
     * @param data
     */
    private void onBtRequestActivityReturn(int resultCode,Intent data){
        if(resultCode == RESULT_CANCELED){
            noBTQuit();
        }
    }

    /**
     * declare mBTdevice a l'aide de son adresse MAC
     * @param AdresseMAC
     */
    private void setmBTDeviceByMAC(String AdresseMAC){
        mBTDevice=mBluetoothAdapter.getRemoteDevice(AdresseMAC);
    }

    /**
     * Action lorsque le bouton connexion à l'appareil bluetooth est préssé.
     */
    private void onBtConnectButtonPressed(){

        if(mBtInterface!=null){
            Toast.makeText(getBaseContext(), "Tentative de connection à l'appareil.", Toast.LENGTH_SHORT).show();
            mBtInterface.connect();

        }

    }

    /**
     * Action lors de l'appuis sur le bouton envoyer.
     */
    private void onSendButtonPressed(){
        mBtInterface.sendData(mPixelArtString);
    }


    /**
     * Reçois les informations de BTinterface et agit en consequences de ces infos.
     */
    private final Handler handlerStatus = new Handler() {
        public void handleMessage(Message msg) {

            int message = msg.arg1;

            switch(message) {
                case CONNECTED_CODE:{
                    btnConnectBTDevice.setText("Connecté");
                    btnConnectBTDevice.setEnabled(false);
                    connectedFlag=true;
                    Toast.makeText(getBaseContext(),"Connecté",Toast.LENGTH_SHORT).show();
                    updateButtonsAndTextViews();
                }
                    break;
                case DISCONNECTED_CODE:
                    btnConnectBTDevice.setText("Déconnecté");
                    break;
                case CONNECTION_FAILED_CODE:{
                    btnConnectBTDevice.setText("échec réessayer");
                    Toast.makeText(getBaseContext(),"Echec de la connexion réessayer.",Toast.LENGTH_SHORT).show();
                    updateButtonsAndTextViews();}
                    break;
                case DISCONNECTION_FAILED_CODE:
                    break;
            }
        }
    };




    private void updateButtonsAndTextViews(){

        btnSend.setEnabled(connectedFlag && pattern_readyFlag);

        mAdressTextView.setText(mDeviceInfos);
        
        mDeviceNameTextView.setText(mDeviceInfos);

    }

}
