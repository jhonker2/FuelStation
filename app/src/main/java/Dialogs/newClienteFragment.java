package Dialogs;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.genesis.genesisfuelstation.R;

/**
 * Created by GENESIS on 09/03/2017.
 */

public class newClienteFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.activity_cliente, container, false);
        getDialog().setTitle("Nuevo Cliente");

        return rootView;

    }


}
