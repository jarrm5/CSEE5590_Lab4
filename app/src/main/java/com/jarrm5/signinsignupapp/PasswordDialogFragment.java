package com.jarrm5.signinsignupapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordDialogFragment extends DialogFragment {

    FirebaseAuth firebaseAuth;
    EditText emailEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //Inflate and set the layout for the dialog
        //Pass null as the parent view because its going in the dialog layout
        final View passwordDialogView = inflater.inflate(R.layout.dialog_password, null);

        firebaseAuth = FirebaseAuth.getInstance();
        emailEditText = passwordDialogView.findViewById(R.id.enter_email);

        builder.setView(passwordDialogView)
                .setPositiveButton(R.string.send_email, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        firebaseAuth.sendPasswordResetEmail(emailEditText.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(passwordDialogView.getContext(), "Password email reset succesfully sent to " + emailEditText.getText().toString(),
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                        else{
                                            Toast.makeText(passwordDialogView.getContext(), emailEditText.getText().toString() + " seems to be an invalid address.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PasswordDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


}
