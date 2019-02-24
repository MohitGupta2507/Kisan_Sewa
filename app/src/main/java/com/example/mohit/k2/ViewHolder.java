package com.example.mohit.k2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;
    Context c;
    public ViewHolder(View itemView) {
        super(itemView);
        mView=itemView;
        c=mView.getContext();
    }

    public void setProfile(String ProfileImage)
    {
        de.hdodenhof.circleimageview.CircleImageView ProImage=(de.hdodenhof.circleimageview.CircleImageView)mView.findViewById(R.id.ProfilePicture);
        Picasso.with(c).load(ProfileImage).into(ProImage);
    }
    public void setName(String Name)
    {
        TextView name=(TextView)mView.findViewById(R.id.UsernameText);
        name.setText(Name);
    }
    public void setProductImage(String PImage)
    {
        c=mView.getContext();
        ImageView img=(ImageView)mView.findViewById(R.id.CropImage);
        //Toast.makeText(c,PImage,Toast.LENGTH_LONG).show();
        Picasso.with(c).load(PImage).into(img);
    }
    public void setMax(String max)
    {
        TextView name=(TextView)mView.findViewById(R.id.Price);
        name.setText(max);

    }
    public void setQuantity(String q)
    {
        TextView name=(TextView)mView.findViewById(R.id.Quantity);
        name.setText(q);
    }
    public void setQuantityUnit(String qunit)
    {
        TextView name=(TextView)mView.findViewById(R.id.QuantityUnit);
        name.setText(qunit);
    }
    public void setDate(String x)
    {
        TextView name=(TextView)mView.findViewById(R.id.date);
        name.setText(x);
    }
}
