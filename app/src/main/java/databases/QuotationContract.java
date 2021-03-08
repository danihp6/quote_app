package databases;

import android.provider.BaseColumns;

public class QuotationContract {

    private QuotationContract(){}

    static class MyTableEntry implements BaseColumns {

        static final String QUOTE_TABLE = "quotations";
        static final String _ID = "id";
        static final String QUOTE = "quote";
        static final String AUTHOR = "author";
    }
}
