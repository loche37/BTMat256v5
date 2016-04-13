package com.jeremy.polytech.btmat256v5.BT;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremy.polytech.btmat256v5.R;

import java.util.Set;

/**
 * Activité servant a sélectionner le périphérique bluetooth avec lequel on souhaite communiquer. S'affiche en tant que boite de dialogue
 * Renvoie un Intent avec RESULT_OK contenant l'adresse MAC du périphérique
 * ou renvoie Intent avec RESULT_CANCELED
 */
public class DeviceListActivity extends AppCompatActivity {


    /**
     * Return Intent extra
     */
    public static String EXTRA_DEVICE_INFOS = "device_address";

    /**
     * Member fields
     */
    private BluetoothAdapter mBtAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_device_list);
        setTitle(R.string.title_paired_devices);

        ArrayAdapter<String> mPairedDevicesArrayAdapter=new ArrayAdapter<String>(this,R.layout.device_name);
        ListView mPairedDevicesListView=(ListView)findViewById(R.id.pairedDevicesListView);
        mPairedDevicesListView.setAdapter(mPairedDevicesArrayAdapter);
        mPairedDevicesListView.setOnItemClickListener(mDeviceClickListener);

        mBtAdapter=BluetoothAdapter.getDefaultAdapter();

        // Récuperer les appareils appairés dans un set
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // S'il y a des appareils appairés on les rajoute au ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }



    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            // Récupere la chaine de caractere du motif sur lequel on a cliqué
            String info = ((TextView) v).getText().toString();

            if(!(info.equals(getResources().getText(R.string.none_paired).toString()))) //On verifie qu'on a bien une adresse de cliqué car sinon on ne renvoie pas une adresse
            {
                String deviceInfos = info;

                // Crée l'Intent de résultat et ajoute l'adresse MAC en extra
                Intent data = new Intent();
                data.putExtra(EXTRA_DEVICE_INFOS, deviceInfos);

                // Set le resultat et finit l'activité
                setResult(RESULT_OK, data);
            }

            finish();
        }
    };

    /**
     * Permet de supprimer un des éléments de la liste
     */
    private AdapterView.OnLongClickListener mDeviceLongClickListener = new AdapterView.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {


            return false;
        }
    };


}
