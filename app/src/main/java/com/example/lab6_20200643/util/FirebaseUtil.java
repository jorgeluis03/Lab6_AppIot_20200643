package com.example.lab6_20200643.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {
    public static String currentUserId(){
        //devuelvo el id de autenticacion del usuario logeado
        return FirebaseAuth.getInstance().getUid();
    }

    //metodo para abrir la actividad principal si ya esta logeado
    public static  boolean isLoggedIn(){
        if (currentUserId()!=null){
            return true;
        }
        return false;
    }
    public static DocumentReference currentUserDetails(){
        //obtengo el documento del usuario logeado
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }
}
