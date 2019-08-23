package com.mariapps.qdmswiki.login.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.mariapps.qdmswiki.login.model.LoginResponse;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface LoginDao {

    /**
     * Get the user from the table. Since for simplicity we only have one user in the database,
     * this query gets all users from the table, but limits the result to just the 1st user.
     *
     * @return the user from the table
     */
    @Query("SELECT * FROM login")
    Maybe<List<Login>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Login> loginData);

   @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insetLoginResponse(LoginResponse loginResponse);

    @Query("SELECT * FROM loginResponse WHERE uId=1")
    LoginResponse getLoginResponse();

    @Query("DELETE FROM login")
    void delete();

    @Query("DELETE FROM loginResponse")
    void deleteLoginResponse();

    @Update
    void updateLogin(List<Login> login);










    /*@Query("SELECT * FROM login WHERE uid IN (:userIds)")
    Flowable<List<Login>> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM login WHERE first_name LIKE :first AND "
            + "last_name LIKE :last LIMIT 1")
    Login findByName(String first, String last);

    @Query("SELECT * FROM login where uid = :id")
    Maybe<Login> findById(int id);




    @Delete
    void delete(Login user);

    @Update
    public void updateLogin(Login... users);*/
}
