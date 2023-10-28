package com.example.websiteforgame.service;

import com.example.websiteforgame.models.SiteUser;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FirebaseServiceTest {

    @Mock
    private FirebaseAuth firebaseAuth;

    @Mock
    private FirebaseDatabase firebaseDatabase;

    @Mock
    private DatabaseReference userRef;

    @Mock
    private DatabaseReference siteUsersRef;
    @Mock
    private DataSnapshot dataSnapshot;


    @InjectMocks
    private FirebaseService firebaseService;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/androidgachaapp-firebase-adminsdk-m3sur-23211218e4.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://androidgachaapp-default-rtdb.firebaseio.com/")
                .setStorageBucket("androidgachaapp.appspot.com")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        // Убедитесь, что вы возвращаете правильный тип объекта:
        when(firebaseDatabase.getReference(anyString())).thenReturn(siteUsersRef);

        when(siteUsersRef.child(anyString())).thenReturn(userRef);

        firebaseService = new FirebaseService(firebaseAuth, firebaseDatabase);
    }




    @Test
    void verifyToken() throws Exception {
        String idToken = "...";
        FirebaseToken firebaseToken = mock(FirebaseToken.class);
        when(firebaseAuth.verifyIdToken(idToken)).thenReturn(firebaseToken);

        FirebaseToken result = firebaseService.verifyToken(idToken);

        assertEquals(firebaseToken, result);
    }

    @Test
    void getUserEmail() throws Exception {
        String uid = "...";
        UserRecord userRecord = mock(UserRecord.class);
        when(firebaseAuth.getUser(uid)).thenReturn(userRecord);
        when(userRecord.getEmail()).thenReturn("m1@m.ru");

        String result = firebaseService.getUserEmail(uid);

        assertEquals("m1@m.ru", result);
    }

    @Test
    public void testAddUser() {
        SiteUser siteUser = new SiteUser();
        siteUser.setUid("testUid");
        siteUser.setEmail("fsdf@mail.ru");
        firebaseService.addUser(siteUser);

        verify(userRef).setValueAsync(siteUser);
    }


    @Test
    void getUser() throws ExecutionException, InterruptedException {
        SiteUser expectedUser = new SiteUser();
        expectedUser.setUsername("testUsername");
        expectedUser.setUid("testUID");
        expectedUser.setEmail("test@mail.ru");

        firebaseService.setUser(expectedUser);
        SiteUser resultUser = firebaseService.getUser();

        assertEquals(expectedUser, resultUser);
    }



    @Test
    void setUserAndGetUser() {
        SiteUser user = new SiteUser();
        firebaseService.setUser(user);

        assertEquals(user, firebaseService.getUser());
    }

    @Test
    void changeUsernameTest() {
        String uid = "EkriKNzSqdeyS73xfNiLA6yNl8o1";
        String newUsername = "newUsername";
        DatabaseReference userRefMock = mock(DatabaseReference.class);
        DatabaseReference usernameRefMock = mock(DatabaseReference.class);
        when(userRefMock.child("username")).thenReturn(usernameRefMock);

        firebaseService.changeUsername(uid, newUsername, userRefMock);

        verify(usernameRefMock).setValueAsync(newUsername);
    }

}
