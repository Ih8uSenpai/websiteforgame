package com.example.websiteforgame.service;

import com.example.websiteforgame.models.SiteUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class FirebaseService {

    private final FirebaseAuth firebaseAuth;
    private final DatabaseReference siteUsersRef;
    private SiteUser user;

    public FirebaseService(FirebaseAuth firebaseAuth, FirebaseDatabase firebaseDatabase) {
        this.firebaseAuth = firebaseAuth;
        this.siteUsersRef = firebaseDatabase.getReference("SiteUsers");
    }

    public FirebaseToken verifyToken(String idToken) {
        try {
            return firebaseAuth.verifyIdToken(idToken);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getUserEmail(String uid) {
        try {
            UserRecord userRecord = firebaseAuth.getUser(uid);
            return userRecord.getEmail();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user details for UID: " + uid);
        }
    }

    public void addUser(SiteUser user) {
        siteUsersRef.child(user.getUid()).setValueAsync(user);
    }


    public CompletableFuture<SiteUser> getUser(String uid, DatabaseReference userRef) {
        //DatabaseReference userRef = siteUsersRef.child(uid);
        CompletableFuture<SiteUser> completableFuture = new CompletableFuture<>();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SiteUser siteUser = dataSnapshot.getValue(SiteUser.class);
                completableFuture.complete(siteUser); // завершаем CompletableFuture с полученным значением
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Ошибка чтения данных. Здесь вы можете обработать ошибку, например, записать в лог.
                completableFuture.completeExceptionally(new RuntimeException("Failed to get user for UID: " + uid));
            }
        });

        return completableFuture; // возвращаем CompletableFuture
    }

    public void setUser(SiteUser user) {
        this.user = user;
    }

    public SiteUser getUser() {
        return user;
    }

    public void changeUsername(String uid, String username, DatabaseReference userRef) {
        try {
            //DatabaseReference userRef = siteUsersRef.child(uid);
            userRef.child("username").setValueAsync(username);
        } catch (Exception e) {
            throw new RuntimeException("Failed to change username for UID: " + uid);
        }
    }

    public DatabaseReference getSiteUsersRef() {
        return siteUsersRef;
    }
}

