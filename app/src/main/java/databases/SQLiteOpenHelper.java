package databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import models.Quotation;

public class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    static private SQLiteOpenHelper instance;

    public synchronized static SQLiteOpenHelper getInstance(Context context){
        if(instance == null) instance = new SQLiteOpenHelper(context);
        return instance;
    }
    public SQLiteOpenHelper(@Nullable Context context) {
        super(context, "quotation_database", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " +
                    QuotationContract.MyTableEntry.QUOTE_TABLE +
                    " (" +
                    QuotationContract.MyTableEntry._ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    QuotationContract.MyTableEntry.QUOTE+
                    " TEXT NOT NULL, "+
                    QuotationContract.MyTableEntry.AUTHOR+
                    " TEXT);"
            );
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Quotation> getQuotations() {
        List<Quotation> quotations = new ArrayList<Quotation>();
        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.query(QuotationContract.MyTableEntry.QUOTE_TABLE,new String[]{QuotationContract.MyTableEntry._ID,QuotationContract.MyTableEntry.QUOTE,QuotationContract.MyTableEntry.AUTHOR},null,null,null,null,null);
        while (cursor.moveToNext())
            quotations.add(new Quotation(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2)));

        cursor.close();
        db.close();
        return quotations;
    }

    public boolean exists(String quote) {
        Cursor cursor = instance.getReadableDatabase().query(QuotationContract.MyTableEntry.QUOTE_TABLE,null,QuotationContract.MyTableEntry.QUOTE + "=?",new String[]{quote},null,null,null);
        boolean quotationExists = cursor.getCount() > 0;
        cursor.close();
        instance.close();
        return quotationExists;
    }

    public void addQuotation(Quotation quotation) {
        ContentValues values = new ContentValues();
        values.put(QuotationContract.MyTableEntry.QUOTE,quotation.getQuote());
        values.put(QuotationContract.MyTableEntry.AUTHOR,quotation.getAuthor());
        SQLiteDatabase db = instance.getWritableDatabase();
        db.insert(QuotationContract.MyTableEntry.QUOTE_TABLE,null,values);
        db.close();
    }

    public void deleteQuotation(Quotation quotation) {
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete(QuotationContract.MyTableEntry.QUOTE_TABLE, QuotationContract.MyTableEntry.QUOTE + "=?", new
                String[]{quotation.getQuote()});
        db.close();
    }

    public void clear() {
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete(QuotationContract.MyTableEntry.QUOTE_TABLE, null, null);
        db.close();
    }
}
