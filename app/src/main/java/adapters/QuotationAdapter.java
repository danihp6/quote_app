package adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quote.R;

import java.util.ArrayList;
import java.util.List;

import models.Quotation;

public class QuotationAdapter extends RecyclerView.Adapter<QuotationAdapter.ViewHolder> {
    private List<Quotation> quotations = new ArrayList<>();
    static private OnClickListener onClickListener;

    public QuotationAdapter( OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public QuotationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotation_list_row, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuotationAdapter.ViewHolder holder, int position) {
        Quotation quotation = quotations.get(position);
        String quoteText = quotation.getQuote();
        String quoteAuthor = quotation.getAuthor();
        holder.tv_quoteText.setText(quoteText);
        if(quoteAuthor.isEmpty()) holder.tv_quoteAuthor.setText(R.string.favourite_empty_author);
        else holder.tv_quoteAuthor.setText(quoteAuthor);
    }

    @Override
    public int getItemCount() {
        return quotations.size();
    }

    public Quotation getQuotationAt(int position){
        return quotations.get(position);
    }

    public void removeQuotationAt(int position){
        quotations.remove(position);
        notifyItemRemoved(position);
    }

    public void clearAll(){
        quotations.clear();
        notifyDataSetChanged();
    }

    public void setQuotations(List<Quotation> quotations){
        this.quotations = quotations;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        public TextView tv_quoteText;
        public TextView tv_quoteAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_quoteText = itemView.findViewById(R.id.tv_quoteText);
            this.tv_quoteAuthor = itemView.findViewById(R.id.tv_quoteAuthor);
            itemView.setOnClickListener(this); itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            onClickListener.onLongClick(getAdapterPosition());
            return true;
        }
    }

}

