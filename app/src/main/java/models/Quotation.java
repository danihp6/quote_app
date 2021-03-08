package models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "quotations")
public class Quotation {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "quote")
    @SerializedName("quoteText")
    private String quote;

    @ColumnInfo(name = "author")
    @SerializedName("quoteAuthor")
    private String author;

    public Quotation() {}

    public Quotation(String quoteText,String quoteAuthor) {
        this.quote = quoteText;
        this.author = quoteAuthor;
    }

    public Quotation(int id,String quoteText,String quoteAuthor) {
        this.id = id;
        this.quote = quoteText;
        this.author = quoteAuthor;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getQuote(){
        return quote;
    }

    public void setQuote(String quote){
        this.quote = quote;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author = author;
    }
}
