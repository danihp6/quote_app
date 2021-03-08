package databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import models.Quotation;

@Database(entities = Quotation.class, version = 1)
abstract public class RoomDB extends RoomDatabase {

    static private RoomDB instance;

    public synchronized static RoomDB getInstance(Context context){
        if(instance == null) instance =  Room
                .databaseBuilder(context, RoomDB.class, "quotation_database")
                .build();
        ;
        return instance;
    }

    public abstract RoomDao room();

    public void destroyInstance(){
        if(instance != null) {
            instance.close();
            instance = null;
        }
    }
}
