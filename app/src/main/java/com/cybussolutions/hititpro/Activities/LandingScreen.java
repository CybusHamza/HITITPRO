package com.cybussolutions.hititpro.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.cybussolutions.hititpro.Fragments.ClientsFragment;
import com.cybussolutions.hititpro.Fragments.ProfileFragment;
import com.cybussolutions.hititpro.Fragments.ReviewInspectionFragment;
import com.cybussolutions.hititpro.Fragments.TemplatesFragment;
import com.cybussolutions.hititpro.Fragments.TemplatesListFragment;
import com.cybussolutions.hititpro.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class LandingScreen extends AppCompatActivity {


    Drawer drawer;
    Toolbar toolbar;
    String image;
    Bitmap[] bitmap1;
    String userName,userEmail;
    // drawer items name icons
    String[] drawerNames = new String[]{"Profile", "Clients", "Inspection List","Create Inspection" ,"Review Inspection","Logout"};
    int[] drawerImages = new int[]{R.drawable.profile, R.drawable.clients, R.drawable.template,R.drawable.review, R.drawable.startinspection
           ,R.drawable.logout};

    String activityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
         userName = pref.getString("user_name", null);
         userEmail = pref.getString("email", null);
        image = pref.getString("img", null);


        Intent i=getIntent();
        activityName=i.getStringExtra("activityName");
        String url = null;
        bitmap1 = new Bitmap[1];

        url =  "http://xfer.cybusservices.com/hititpro/uploads/inspection/" + image;

        Picasso.with(LandingScreen.this)
                .load(url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        bitmap1[0] =bitmap;
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                        Log.e("here","onBitmapFailed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.e("here","onPrepareLoad");
                    }
                });



        setupDrawer();

        if(activityName.equals("addTemplateClass")){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new TemplatesFragment()).commit();
        }else if(activityName.equals("addClientClass")){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ClientsFragment()).commit();
        }
        else
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();




    }

    private void setupDrawer() {
        PrimaryDrawerItem drawerItem1 = new PrimaryDrawerItem().withIdentifier(1).withName(drawerNames[0]).withIcon(drawerImages[0]);
        PrimaryDrawerItem drawerItem2 = new PrimaryDrawerItem().withIdentifier(2).withName(drawerNames[1]).withIcon(drawerImages[1]);
        PrimaryDrawerItem drawerItem3 = new PrimaryDrawerItem().withIdentifier(3).withName(drawerNames[2]).withIcon(drawerImages[2]);
        PrimaryDrawerItem drawerItem4 = new PrimaryDrawerItem().withIdentifier(3).withName(drawerNames[3]).withIcon(drawerImages[3]);
        PrimaryDrawerItem drawerItem5 = new PrimaryDrawerItem().withIdentifier(4).withName(drawerNames[4]).withIcon(drawerImages[4]);
        PrimaryDrawerItem drawerItem8 = new PrimaryDrawerItem().withIdentifier(5).withName(drawerNames[5]).withIcon(drawerImages[5]);




    //create the drawer and remember the `Drawer` drawer object
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(setupHeader(userName, userEmail))
                .addDrawerItems(
                        drawerItem1,
                        new DividerDrawerItem(),
                        drawerItem2,
                        new DividerDrawerItem(),
                        drawerItem3,
                        new DividerDrawerItem(),
                        drawerItem4,
                        new DividerDrawerItem(),
                        drawerItem5,
                        new DividerDrawerItem(),
                        drawerItem8
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        Fragment fragment = null;

                        switch (position) {
                            case 1: {
                                fragment = new ProfileFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

                                break;
                            }
                            case 3: {
                                fragment = new ClientsFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                break;
                            }
                            case 5: {
                                fragment = new TemplatesListFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                break;
                            }
                            case 7: {
                                ///////////inspection///////////
                                fragment = new TemplatesFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                break;
                            }
                            case 9: {
                                fragment = new ReviewInspectionFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                break;
                            }
                            case 11: {
                                SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
                                Intent intent = new Intent(LandingScreen.this, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                break;
                            }
                            default:
                                break;
                        }
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                        drawer.closeDrawer();
                        return true;
                    }
                })
                .build();

    }

    private AccountHeader setupHeader(String name, String email) {

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.house_bkg)
                .addProfiles(
                        new ProfileDrawerItem().withName(name).withEmail(email).withIcon(bitmap1[0])
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        return headerResult;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        }
        else if(getSupportFragmentManager().getBackStackEntryCount()==1)
        {

            new AlertDialog.Builder(LandingScreen.this)
                    .setTitle("Close HitItPro")
                    .setMessage("Are you sure you want to Close HitItPro? ")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            getSupportFragmentManager().popBackStack();

                            finish();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        }
        else {
            finish();

            super.onBackPressed();
        }
    }


}
