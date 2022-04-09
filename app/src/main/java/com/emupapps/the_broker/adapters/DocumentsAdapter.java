package com.emupapps.the_broker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.models.info_user.Documents;
import com.emupapps.the_broker.utils.interfaces.DeleteClickHandler;
import com.emupapps.the_broker.utils.interfaces.DownloadClickHandler;

import java.util.ArrayList;

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.AdapterViewHolder> {

    private static final String TAG = DocumentsAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<Documents> mListDocuments;
    private DeleteClickHandler mDeleteHandler;
    private DownloadClickHandler mDownloadHandler;

    public DocumentsAdapter(Context context,
                            ArrayList<Documents> listDocuments,
                            DeleteClickHandler deleteHandler,
                            DownloadClickHandler downloadHandler) {
        mContext = context;
        mListDocuments = listDocuments;
        mDeleteHandler = deleteHandler;
        mDownloadHandler = downloadHandler;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_document, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        holder.mTitle.setText(mListDocuments.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return mListDocuments.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView mDownload;
        ImageView mDelete;
        TextView mTitle;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mDelete.setOnClickListener(v -> mDeleteHandler.onClick(getAdapterPosition()));
            mDownload.setOnClickListener(v -> mDownloadHandler.onClick(getAdapterPosition()));
        }
    }

    public void swapData(ArrayList<Documents> documents){
        mListDocuments = documents;
        notifyDataSetChanged();
    }
}
