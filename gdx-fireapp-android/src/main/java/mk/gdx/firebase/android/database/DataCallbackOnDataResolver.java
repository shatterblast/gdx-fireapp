/*
 * Copyright 2017 mk
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

package mk.gdx.firebase.android.database;

import com.google.firebase.database.DataSnapshot;

import mk.gdx.firebase.callbacks.DataCallback;
import mk.gdx.firebase.database.pojos.OrderByClause;

/**
 * Resolves data callback with ordering preserved.
 */
public class DataCallbackOnDataResolver
{

    // TODO - docs
    @SuppressWarnings("unchecked")
    public static <T, E extends T> void resolve(Class<T> dataType, OrderByClause orderByClause, DataSnapshot dataSnapshot, DataCallback<E> dataCallback)
    {
        if (DataSnapshotOrderByResolver.shouldResolveOrderBy(orderByClause, dataType, dataSnapshot)) {
            dataCallback.onData((E) DataSnapshotOrderByResolver.resolve(dataSnapshot));
        } else {
            dataCallback.onData((E) dataSnapshot.getValue());
        }
    }
}
