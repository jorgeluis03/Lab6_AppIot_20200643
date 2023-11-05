package com.example.lab6_20200643.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {
    public static String actualUserId(){
        //devuelvo el id de autenticacion del usuario logeado
        return FirebaseAuth.getInstance().getUid();
    }

    //metodo para abrir la actividad principal si ya esta logeado
    public static  boolean userEstaLogeado(){
        if (actualUserId()!=null){
            return true;
        }
        return false;
    }
    public static DocumentReference currentUserDetails(){
        //obtengo el documento del usuario logeado y si no lo creo
        return FirebaseFirestore.getInstance().collection("users").document(actualUserId());
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }
}
