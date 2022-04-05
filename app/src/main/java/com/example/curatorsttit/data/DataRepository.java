package com.example.curatorsttit.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.curatorsttit.data.local.AppDatabase;
import com.example.curatorsttit.data.local.entity.*;

import java.util.List;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private MediatorLiveData<List<UserEntity>> mObservableUsers;

    //FixMe в mObservableUsers нет данных от REMOTE
    private DataRepository(final AppDatabase database) {
        mDatabase = database;
        mObservableUsers = new MediatorLiveData<>();

        mObservableUsers.addSource(mDatabase.userDao().loadAllUsers(),
                userEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableUsers.postValue(userEntities);
                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of users from the database and get notified when the data changes.
     */
    public LiveData<List<UserEntity>> getUsers() {
        return mObservableUsers;
    }

    public LiveData<UserEntity> loadUser(final int userId) {
        return mDatabase.userDao().loadUser(userId);
    }

    /*public LiveData<List<CommentEntity>> loadComments(final int productId) {
        return mDatabase.commentDao().loadComments(productId);
    }

    public LiveData<List<ProductEntity>> searchProducts(String query) {
        return mDatabase.productDao().searchAllProducts(query);
    }*/
}
