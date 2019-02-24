package com.example.mohit.k2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder2 extends RecyclerView.ViewHolder {
    View mView;
    Context c;
    String qq;
    public ViewHolder2(View itemView) {
        super(itemView);
        mView=itemView;
        c=mView.getContext();
    }

    public void setProductImage(String PImage)
    {
        c=mView.getContext();
        ImageView img=(ImageView)mView.findViewById(R.id.PostImage);
        //Toast.makeText(c,PImage,Toast.LENGTH_LONG).show();
        Picasso.with(c).load(PImage).into(img);
    }

    public void setProduct(String Product)
    {
        TextView t=(TextView)mView.findViewById(R.id.PostName);
        t.setText(Product);
    }
    public void setMax(String max)
    {
        TextView name=(TextView)mView.findViewById(R.id.PostPrice);
        name.setText("\u20B9"+max);

    }
    public void setQuantity(String q)
    {
        TextView name=(TextView)mView.findViewById(R.id.PostQuantity);
        q=q+" "+qq;
        name.setText(q);
    }
    public void setQuantityUnit(String qunit)
    {
 //       TextView name=(TextView)mView.findViewById(R.id.QuantityUnit);
        qq=qunit;
    }




}
