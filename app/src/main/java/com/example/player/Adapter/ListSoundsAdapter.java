package com.example.player.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.player.Model.Result;
import com.example.player.PlayMusicActivity;
import com.example.player.R;
import com.squareup.picasso.Picasso;

public class ListSoundsAdapter extends RecyclerView.Adapter<ListSoundsAdapter.MyViewHolder> {
    private Context mContext;
    private Result mResult;

    public ListSoundsAdapter(Context context, Result result) {
        this.mContext = context;
        this.mResult = result;
    }

    @NonNull
    @Override
    public ListSoundsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_list_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSoundsAdapter.MyViewHolder holder, final int position) {
        holder.textViewNameBand.setText(new StringBuilder(mResult.getResults().get(position).getArtistName()));
        holder.textViewNameSound.setText(String.valueOf(mResult.getResults().get(position).getTrackName()));
        Picasso.get().load(mResult.getResults().get(position).getArtworkUrl60()).into(holder.imageViewImageAlbum);

        holder.linearLayoutListSounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageURL, artistURL;
                imageURL = mResult.getResults().get(position).getArtworkUrl100();
                artistURL = mResult.getResults().get(position).getPreviewUrl();
                Intent intent = new Intent(mContext, PlayMusicActivity.class);
                intent.putExtra("imageURL", imageURL);
                intent.putExtra("artistURL", artistURL);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResult.getResults().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewImageAlbum;
        TextView textViewNameSound;
        TextView textViewNameBand;
        LinearLayout linearLayoutListSounds;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewImageAlbum = (ImageView) itemView.findViewById(R.id.imageViewImageAlbum);
            textViewNameBand = (TextView) itemView.findViewById(R.id.textViewNameBand);
            textViewNameSound = (TextView) itemView.findViewById(R.id.textViewNameSound);
            linearLayoutListSounds = (LinearLayout) itemView.findViewById(R.id.linearLayoutListSounds);
        }
    }
}
