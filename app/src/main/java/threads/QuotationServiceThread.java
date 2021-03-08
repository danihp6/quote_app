package threads;
import java.lang.ref.WeakReference;

import com.example.quote.QuotationActivity;
import databases.RoomDB;
import databases.RoomDao;
import databases.SQLiteOpenHelper;
import models.Quotation;
import services.QuotationService;

public class QuotationServiceThread extends  Thread {

    private WeakReference<QuotationActivity> reference;
    boolean useRoom;

    public QuotationServiceThread(QuotationActivity acticity, boolean useRoom) {
        super();
        this.reference = new WeakReference<QuotationActivity>(acticity);
        this.useRoom = useRoom;
    }

    @Override
    public void run() {
        super.run();
        QuotationService quotationService = new QuotationService();
        SQLiteOpenHelper helperDb;
        RoomDao roomDb;
        Quotation quotation;

        if(reference != null){
            quotation = quotationService.getQuotation(reference.get());

            boolean exists;
            if(useRoom) {
                roomDb = RoomDB.getInstance(reference.get()).room();
                exists = roomDb.searchQuotation(quotation.getQuote()) != null;
            }
            else{
                helperDb = SQLiteOpenHelper.getInstance(reference.get());
                exists = helperDb.exists(quotation.getQuote());
            }

            reference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reference.get().setQuotation(quotation);
                    reference.get().setAddVisible(!exists);
                }
            });

        }
    }
}
