package threads;

import com.example.quote.FavouriteActivity;

import java.lang.ref.WeakReference;
import java.util.List;

import databases.RoomDB;
import databases.RoomDao;
import databases.SQLiteOpenHelper;
import models.Quotation;

public class DatabaseThread extends  Thread {

    private WeakReference<FavouriteActivity> reference;
    boolean useRoom;

    public DatabaseThread(FavouriteActivity acticity, boolean useRoom) {
        super();
        this.reference = new WeakReference<FavouriteActivity>(acticity);
        this.useRoom = useRoom;
    }

    @Override
    public void run() {
        super.run();
        SQLiteOpenHelper helperDb;
        RoomDao roomDB;
        List<Quotation> quotations;

        if(reference != null){
            if( useRoom) {
                roomDB = RoomDB.getInstance(reference.get()).room();
                quotations = roomDB.getQuotations();
            }
            else{
                helperDb = SQLiteOpenHelper.getInstance(reference.get());
                quotations = helperDb.getQuotations();
            }
            reference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reference.get().updateAdapter(quotations);
                }
            });
        }
    }
}
