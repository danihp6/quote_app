package databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import models.Quotation;

@Dao
public interface RoomDao {

    @Insert
    void addQuotation(Quotation quotation);

    @Delete
    void deleteQuotation(Quotation quotation);

    @Query("SELECT * FROM " + QuotationContract.MyTableEntry.QUOTE_TABLE)
    List<Quotation> getQuotations();

    @Query("SELECT * FROM " + QuotationContract.MyTableEntry.QUOTE_TABLE +" WHERE " + QuotationContract.MyTableEntry.QUOTE + " = :quote")
    Quotation searchQuotation(String quote);

    @Query("DELETE FROM " + QuotationContract.MyTableEntry.QUOTE_TABLE)
    void clear();
}
