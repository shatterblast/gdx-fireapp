/*
 * Copyright 2018 mk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.mk5.gdx.fireapp.android.database;

import com.badlogic.gdx.utils.GdxNativesLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import pl.mk5.gdx.fireapp.android.AndroidContextTest;
import pl.mk5.gdx.fireapp.database.FirebaseMapConverter;
import pl.mk5.gdx.fireapp.promises.ConverterPromise;

import static org.mockito.Mockito.spy;

@PrepareForTest({GdxNativesLoader.class, FirebaseDatabase.class})
public class QueryReadValueTest extends AndroidContextTest {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public void setup() throws Exception {
        super.setup();
        PowerMockito.mockStatic(FirebaseDatabase.class);
        firebaseDatabase = PowerMockito.mock(FirebaseDatabase.class);
        Mockito.when(FirebaseDatabase.getInstance()).thenReturn(firebaseDatabase);
        databaseReference = Mockito.mock(DatabaseReference.class);
        Mockito.when(firebaseDatabase.getReference(Mockito.anyString())).thenReturn(databaseReference);
    }

    @Test
    public void run_ok() {
        // Given
        Database database = spy(Database.class);
        final DataSnapshot dataSnapshot = Mockito.mock(DataSnapshot.class);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                ((ValueEventListener) invocation.getArgument(0)).onDataChange(dataSnapshot);
                return null;
            }
        }).when(databaseReference).addListenerForSingleValueEvent(Mockito.any(ValueEventListener.class));
        database.inReference("test");
        QueryReadValue query = new QueryReadValue(database, "/test");
        ConverterPromise promise = spy(ConverterPromise.class);
        promise.with(Mockito.mock(FirebaseMapConverter.class), String.class);

        // When
        query.with(promise).withArgs(String.class).execute();

        // Then
    }

    @Test
    public void run_fail() {
        // Given
        Database database = spy(Database.class);
        final DatabaseError databaseError = Mockito.mock(DatabaseError.class);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                ((ValueEventListener) invocation.getArgument(0)).onCancelled(databaseError);
                return null;
            }
        }).when(databaseReference).addListenerForSingleValueEvent(Mockito.any(ValueEventListener.class));
        database.inReference("test");
        QueryReadValue query = new QueryReadValue(database, "/test");
        ConverterPromise promise = spy(ConverterPromise.class);
        promise.silentFail();

        // When
        query.with(promise).withArgs(String.class).execute();

        // Then
        Mockito.verify(promise, VerificationModeFactory.times(1)).doFail(Mockito.nullable(Exception.class));
    }
}