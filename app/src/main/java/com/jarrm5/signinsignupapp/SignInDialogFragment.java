package com.jarrm5.signinsignupapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SignInDialogFragment extends DialogFragment {

    public EditText username;
    public EditText password;

    //Use this instance of the interface too deliver action events
    SignInDialogListener signInDialogListener;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment back to the calling activity if it needs to query it.
     * In this situation, pass the SignInDialogFragment back to MainActivity only if it passes validation in this activity */
    public interface SignInDialogListener {
        void onDialogSignIn(DialogFragment dialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SignInDialogListener) {
            signInDialogListener = (SignInDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //Inflate and set the layout for the dialog
        //Pass null as the parent view because its going in the dialog layout
        View signinDialogView = inflater.inflate(R.layout.dialog_signin, null);

        username = signinDialogView.findViewById(R.id.username);
        password = signinDialogView.findViewById(R.id.password);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(signinDialogView)
                // Add action buttons
                .setPositiveButton(R.string.sign_in, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Send back to main activity for signin
                        // Authentication
                        // sign in the user ...
                        signInDialogListener.onDialogSignIn(SignInDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SignInDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}