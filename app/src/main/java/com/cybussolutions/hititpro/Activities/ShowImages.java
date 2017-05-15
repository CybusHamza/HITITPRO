package com.cybussolutions.hititpro.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cybussolutions.hititpro.R;
import com.squareup.picasso.Picasso;

public class ShowImages extends AppCompatActivity {
    String ImageName,data,table_name;
    String[] imageNames;
    ImageView imageView,addNew;
    ImageButton image1,image2,image3,image4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("View Images");
        setSupportActionBar(toolbar);

        imageView= (ImageView) findViewById(R.id.selectedImage);

        image1= (ImageButton) findViewById(R.id.image1);
        image2= (ImageButton) findViewById(R.id.image2);
        image3= (ImageButton) findViewById(R.id.image3);
//        image4= (ImageButton) findViewById(R.id.image4);

        addNew= (ImageView) findViewById(R.id.add_pic);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageNames.length<3) {
                    Intent intent=new Intent(ShowImages.this,MainActivity.class);
                    intent.putExtra("dbTable",table_name);
                    intent.putExtra("data",data);
                    intent.putExtra("showImages","true");
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(ShowImages.this,Add_Comments.class);
                    intent.putExtra("dbTable",table_name);
                    intent.putExtra("data",data);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "you cannot upload more photos", Toast.LENGTH_LONG).show();
                }
            }
        });

        Intent intent=getIntent();

        imageNames=intent.getStringArrayExtra("imageNames");
        data=intent.getStringExtra("data");
        table_name=intent.getStringExtra("dbTable");

        if(imageNames.length>0) {
            String url = "http://xfer.cybusservices.com/hititpro/uploads/inspection/" + imageNames[0];
            Picasso.with(getApplicationContext())
                    .load(url)
                    .into(image1);
        }

        if(imageNames.length>1) {
            String url1 = "http://xfer.cybusservices.com/hititpro/uploads/inspection/" + imageNames[1];
            Picasso.with(getApplicationContext())
                    .load(url1)
                    .into(image2);
        }

        if(imageNames.length>2) {
            String url2 = "http://xfer.cybusservices.com/hititpro/uploads/inspection/" + imageNames[2];
            Picasso.with(getApplicationContext())
                    .load(url2)
                    .into(image3);
        }
        imageView.setImageDrawable(image1.getDrawable());
        /*if(imageNames.length>3) {
            String url3 = "http://xfer.cybusservices.com/hititpro/uploads/inspection/" + imageNames[3];
            Picasso.with(getApplicationContext())
                    .load(url3)
                    .into(image4);
        }*/

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(image1.getDrawable());
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(image2.getDrawable());
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(image3.getDrawable());
            }
        });
       /* image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(image4.getDrawable());
            }
        });*/
    }
}
