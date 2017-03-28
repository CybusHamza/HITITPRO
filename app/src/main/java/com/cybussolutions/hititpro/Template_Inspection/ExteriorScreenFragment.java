package com.cybussolutions.hititpro.Template_Inspection;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybussolutions.hititpro.Activities.Detailed_Activity_All_Screens;
import com.cybussolutions.hititpro.Activities.StructureScreensActivity;
import com.cybussolutions.hititpro.Fragments.BaseFragment;
import com.cybussolutions.hititpro.Network.End_Points;
import com.cybussolutions.hititpro.R;
import com.cybussolutions.hititpro.Sql_LocalDataBase.Database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ExteriorScreenFragment extends BaseFragment {

    View root;
    Button next, back;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Database database;

    private static final String EXTERIROR_TABLE = "exterior";

    Button  exteriorWallCoveringButton, exteriorEavesButton, exteriorDoorsButton, exteriorWindowButton, exteriorDrivewaysButton, exteriorWalksButton,
            exteriorPorchButton, exteriorOverheadButton, exteriorSurfaceButton, exteriorTrainingButton, exteriorFencingButton,
            exteriorObservationButton, roWallsButton, roEavesButton, roDoorsButton, roGarageButton, roPorchesButton,
            roDrivewaysButton, roStepsButton, roDeckButton, roDrainangeButton, roLandscapButton, roRetainingButton,
            roFencingButton;

    String[] exteriorWallCoveringButtonValues, exteriorEavesButtonValues, exteriorDoorsButtonValues, exteriorWindowButtonValues,
            exteriorDrivewaysButtonValues, exteriorWalksButtonValues, exteriorPorchButtonValues, exteriorOverheadButtonValues,
            exteriorSurfaceButtonValues, exteriorTrainingButtonValues, exteriorFencingButtonvalues, exteriorObservationButtonValues,
            roWallsButtonValues, roEavesButtonValues, roDoorsButtonValues, roGarageButtonValues, roPorchesButtonValues,
            roDrivewaysButtonValues, roStepsButtonValues, roDeckButtonValues, roDrainangeButtonValues, roLandscapButtonValues,
            roRetainingButtonValues, roFencingButtonValues;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_exterior_screen, container, false);

        next = (Button) root.findViewById(R.id.next);
        back = (Button) root.findViewById(R.id.back);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExteriorSync();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new InteriorScreenFragment()).addToBackStack("interior").commit();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new RoofingScreenFragment()).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        database= new Database(getActivity());

        try {
            database = database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        exteriorWallCoveringButton = (Button) root.findViewById(R.id.exterior_wall_covering_Button);
        exteriorEavesButton = (Button) root.findViewById(R.id.exterior_eaves_Button);
        exteriorDoorsButton = (Button) root.findViewById(R.id.exterior_doors_Button);
        exteriorWindowButton = (Button) root.findViewById(R.id.exterior_windows_Button);
        exteriorDrivewaysButton = (Button) root.findViewById(R.id.exterior_driveways_Button);
        exteriorWalksButton = (Button) root.findViewById(R.id.exterior_walks_Button);
        exteriorPorchButton = (Button) root.findViewById(R.id.exterior_porch_Button);
        exteriorOverheadButton = (Button) root.findViewById(R.id.exterior_overhead_Button);
        exteriorSurfaceButton = (Button) root.findViewById(R.id.exterior_surface_Button);
        exteriorTrainingButton = (Button) root.findViewById(R.id.exterior_training_Button);
        exteriorFencingButton = (Button) root.findViewById(R.id.exterior_fencing_Button);
        exteriorObservationButton = (Button) root.findViewById(R.id.exterior_observations_Button);
        roWallsButton = (Button) root.findViewById(R.id.ro_exterior_walls_Button);
        roEavesButton = (Button) root.findViewById(R.id.ro_eaves_Button);
        roDoorsButton = (Button) root.findViewById(R.id.ro_doors_Button);
        roGarageButton = (Button) root.findViewById(R.id.ro_garage_Button);
        roPorchesButton = (Button) root.findViewById(R.id.ro_porches_Button);
        roDrivewaysButton = (Button) root.findViewById(R.id.ro_driveways_Button);
        roStepsButton = (Button) root.findViewById(R.id.ro_steps_Button);
        roDeckButton = (Button) root.findViewById(R.id.ro_deck_Button);
        roDrainangeButton = (Button) root.findViewById(R.id.ro_drainage_Button);
        roLandscapButton = (Button) root.findViewById(R.id.ro_landscaping_Button);
        roRetainingButton = (Button) root.findViewById(R.id.ro_retaining_Button);
        roFencingButton = (Button) root.findViewById(R.id.ro_fencing_Button);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Exterior Screen");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);


        exteriorWallCoveringButtonValues = new String[]{"Brick Veneer%0", "Cement Siding%0", "Concrete Block%0", "Wood Composite%0", "Vinyl Siding%0",
                "Metal Siding%0", "Stucco%0", "Wood Shakes%0", "Asbestos Cement%0", "T1-11 Plywood%0"};
        exteriorEavesButtonValues = new String[]{"Wood%0", "Vinyl%0", "Aluminum%0", "Metal%0", "Open Rafters%0"};
        exteriorDoorsButtonValues = new String[]{"Metal%0", "Wood%0", "Sliding Glass%0", "French Doors%0"};
        exteriorWindowButtonValues = new String[]{"Wood%0", "Vinyl%0", "Metal%0", "Other%0"};
        exteriorDrivewaysButtonValues = new String[]{"Concrete%0", "Asphalt%0", "Gravel%0", "None%0", "Other%0", "Not Visible /Snow%0"};
        exteriorWalksButtonValues = new String[]{"Concrete%0", "Asphalt%0", "Pavers%0", "Brick%0", "Stone%0", "Wood%0", "Not Visible /Snow%0"};
        exteriorPorchButtonValues = new String[]{"Wood%0", "Concrete%0", "Brick%0", "Stone%0", "TREX (Synthetic Wood)%0", "Other%0", "Not Visible/Snow%0"};
        exteriorOverheadButtonValues = new String[]{"Steel%0", "Wood%0", "Vinyl%0", "Steel with Wood Trim%0", "Automatic Openers Installed%0", "None%0"};
        exteriorSurfaceButtonValues = new String[]{"Level Grade%0", "Graded Away From House%0", "Graded Towards House%0", "Gently Sloping Grade%0",
                "Ravine Lot%0", "Not Visible/Snow%0"};
        exteriorTrainingButtonValues = new String[]{"Wood%0", "Brick%0", "Concrete%0", "Block%0", "Stone%0", "Pre-Fab Masonry%0"};
        exteriorFencingButtonvalues = new String[]{"Wood%0", "Chain Link%0", "Vinyl Coated Steel%0", "Steel/Iron%0", "None%0"};
        exteriorObservationButtonValues = new String[]{"GOOD%0", "AVERAGE%0", "BELOW AVERAGE%0"};

        roWallsButtonValues = new String[]{"Cracks Typical%0", "Seal Openings%0", "Soil/Wood Siding Contact%0", "Loose Siding%0", "Damaged Trim%0",
                "Wavy Siding%0", "Siding Butt Joints%0", "End Cracks Cement Siding%0", "Pressure Wash Siding%0",
                "Brick Damage%0", "General Paint Peeling%0", "LP/Masonite Siding Good Condition%0",
                "LP/Masonite Siding Poor Condition%0", "Wood Shingles Dried Out%0", "Stucco Damage Minor%0",
                "Stucco Damage Severe%0", "Asbestos Cement%0", "T1-11 Plywood%0"};
        roEavesButtonValues = new String[]{"Soffit/Fascia Painting%0", "Fascia Localized Rot%0", "Fascia Significant Rot%0", "Close Construction Gap%0"};
        roDoorsButtonValues = new String[]{"Window Caulking%0", "Window Frames Paint%0", "Mild Window Trim Rot%0", "Shutters Rotting%0", "Bay Window Rot%0", "Door Jamb Rot%0"};
        roGarageButtonValues = new String[]{"Overhead Door Physical Damage%0", "Overhead Door Minor Rot%0", "Overhead Door Extensive Rot%0",
                "Floor Sensors Inoperative%0", "Hold Button to Close%0", "Opener Inoperative%0", "Opener Auto Reverse Defective%0", "Floor Drainage Issue%0",
                "Floor Cracks Typical%0", "Floor Cracks Pronounced%0", "Gas Proofing Insufficient%0", "Man Door Hollow Core%0"};
        roPorchesButtonValues = new String[]{"Front Porch Landing Settled%0", "Porch Steps Separated From Landing%0", "Porch Railing Loose%0", "Porch Railing Needed%0", "Porch Railing Openings%0", "Seal Porch Columns%0", "Porch Dilapidated%0"};
        roDrivewaysButtonValues = new String[]{"Drainage Insufficient%0", "Drain Needed%0", "Surface Cracks%0", "Driveway Heaving%0"};
        roStepsButtonValues = new String[]{"Re-Point Patio Bricks/Stone%0", "Re-Point Brick/Stone Steps%0", "Steps Rotting%0", "Cracks in Patios/Walkways%0", "Walkway Trip Hazard%0", "Stair Treads Deteriorated%0", "Stair Treads Loose%0"};
        roDeckButtonValues = new String[]{"Need Paint or Stain%0", "Deck Rot%0", "Ledger Band Not Bolted%0", "Deck Flashing Missing%0", "Floor Joists Toe Nailed%0", "Deck Built Grade Level%0", "Deck Lattice Contact Ground%0", "4X4 Supports Poor%0", "Deck Steps Rotting%0", "Railing Loose%0", "Railing Needed%0", "Railing Height Improper%0", "Railing Pickets Too Far Apart%0", "Deck Dilapidated%0"};
        roDrainangeButtonValues = new String[]{"Low Lot Relative to Neighbor%0", "Drainage Easement%0", "Grading Improvement Needed%0", "Improve Swale b/w Houses%0", "Backyard Hill Slopes to House%0", "Driveway Slopes to House%0", "Ravine Erosion Concern%0", "Ponding%0"};
        roLandscapButtonValues = new String[]{"Trim Branches%0", "Irrigation System%0", "Tree Roots%0", "Vines Growing on House%0"};
        roRetainingButtonValues = new String[]{"Wood Wall OK%0", "Wall Slight Movement%0", "Movement/Minor Rot%0", "Movement/Major Rot%0", "Rebuild Wall%0"};
        roFencingButtonValues = new String[]{"Poor Condition%0", "Fair Condition%0", "Posts Damaged%0", "Paint/Staining Needed%0", "Sections Damaged%0", "Gate Latch Broken%0"};


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Exterior Screen");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("HititPro", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String populate = pref.getString("isExterior_populated","");


        if(!(populate.equals("true")))
        {
            database.prePopulateData("wallcovering", exteriorWallCoveringButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("eaves_soffits_fascia", exteriorEavesButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("exteriordoors", exteriorDoorsButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("windows_doorframes_trim", exteriorWindowButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("entry_driveways", exteriorDrivewaysButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("entrywalk_patios", exteriorWalksButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("porch_decks_steps_railings", exteriorPorchButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("overheadgaragedoors", exteriorOverheadButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("surfacedrainage", exteriorSurfaceButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("retainingwalls", exteriorTrainingButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("fencing", exteriorFencingButtonvalues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("observations", exteriorObservationButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rexteriorwalls", roWallsButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("reaves", roEavesButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rexteriordoors_windows", roDoorsButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rgrage", roGarageButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rdriveway", roDrivewaysButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rexteriorsteps_walkways", roStepsButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rporches", roPorchesButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rdeck", roDeckButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rlotdrainage", roDrainangeButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rlandscaping", roLandscapButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("retainwall", roRetainingButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rfencing", roFencingButtonValues, EXTERIROR_TABLE, StructureScreensActivity.inspectionID);

            // Saving string
            editor.putString("isExterior_populated", "true");
            editor.apply();
        }

        exteriorWallCoveringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",exteriorWallCoveringButtonValues);
                intent.putExtra("heading",exteriorWallCoveringButton.getText().toString());
                intent.putExtra("column","wallcovering");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorEavesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",exteriorEavesButtonValues);
                intent.putExtra("heading",exteriorEavesButton.getText().toString());
                intent.putExtra("column","eaves_soffits_fascia");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorDoorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",exteriorDoorsButtonValues);
                intent.putExtra("heading",exteriorDoorsButton.getText().toString());
                intent.putExtra("column","exteriordoors");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",exteriorWindowButtonValues);
                intent.putExtra("heading",exteriorWindowButton.getText().toString());
                intent.putExtra("column","windows_doorframes_trim");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorDrivewaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",exteriorDrivewaysButtonValues);
                intent.putExtra("heading",exteriorDrivewaysButton.getText().toString());
                intent.putExtra("column","entry_driveways");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorWalksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",exteriorWalksButtonValues);
                intent.putExtra("heading",exteriorWalksButton.getText().toString());
                intent.putExtra("column","entrywalk_patios");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorPorchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",exteriorPorchButtonValues);
                intent.putExtra("heading",exteriorPorchButton.getText().toString());
                intent.putExtra("column","porch_decks_steps_railings");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorOverheadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",exteriorOverheadButtonValues);
                intent.putExtra("heading",exteriorOverheadButton.getText().toString());
                intent.putExtra("column","overheadgaragedoors");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorSurfaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",exteriorSurfaceButtonValues);
                intent.putExtra("heading",exteriorSurfaceButton.getText().toString());
                intent.putExtra("column","surfacedrainage");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",exteriorTrainingButtonValues);
                intent.putExtra("heading",exteriorTrainingButton.getText().toString());
                intent.putExtra("column","retainingwalls");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorFencingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",exteriorFencingButtonvalues);
                intent.putExtra("heading",exteriorFencingButton.getText().toString());
                intent.putExtra("column","fencing");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        exteriorObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",exteriorObservationButtonValues);
                intent.putExtra("heading",exteriorObservationButton.getText().toString());
                intent.putExtra("column","observations");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });

        roWallsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roWallsButtonValues);
                intent.putExtra("heading",roWallsButton.getText().toString());
                intent.putExtra("column","rexteriorwalls");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roEavesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roEavesButtonValues);
                intent.putExtra("heading",roEavesButton.getText().toString());
                intent.putExtra("column","reaves");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roDoorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roDoorsButtonValues);
                intent.putExtra("heading",roDoorsButton.getText().toString());
                intent.putExtra("column","rexteriordoors_windows");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roGarageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roGarageButtonValues);
                intent.putExtra("heading",roGarageButton.getText().toString());
                intent.putExtra("column","rgrage");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roPorchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roPorchesButtonValues);
                intent.putExtra("heading",roPorchesButton.getText().toString());
                intent.putExtra("column","rporches");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roDrivewaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roDrivewaysButtonValues);
                intent.putExtra("heading",roDrivewaysButton.getText().toString());
                intent.putExtra("column","rdriveway");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roStepsButtonValues);
                intent.putExtra("heading",roStepsButton.getText().toString());
                intent.putExtra("column","rexteriorsteps_walkways");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roDeckButtonValues);
                intent.putExtra("heading",roDeckButton.getText().toString());
                intent.putExtra("column","rdeck");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roDrainangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roDrainangeButtonValues);
                intent.putExtra("heading",roDrainangeButton.getText().toString());
                intent.putExtra("column","rlotdrainage");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roLandscapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roLandscapButtonValues);
                intent.putExtra("heading",roLandscapButton.getText().toString());
                intent.putExtra("column","rlandscaping");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roRetainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roRetainingButtonValues);
                intent.putExtra("heading",roRetainingButton.getText().toString());
                intent.putExtra("column","retainwall");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });
        roFencingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), Detailed_Activity_All_Screens.class);
                intent.putExtra("items",roFencingButtonValues);
                intent.putExtra("heading",roFencingButton.getText().toString());
                intent.putExtra("column","rfencing");
                intent.putExtra("dbTable",EXTERIROR_TABLE);
                intent.putExtra("inspectionID", StructureScreensActivity.inspectionID);
                startActivity(intent);
            }
        });



        return root;



    }


    public void ExteriorSync() {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Sync Exterior ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNC_EXTERIOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("No Internet Connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else if (error instanceof TimeoutError) {

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Cursor cursor = database.getTable(EXTERIROR_TABLE,StructureScreensActivity.inspectionID);
                cursor.moveToFirst();



                Map<String, String> params = new HashMap<>();
                params.put("template_id", "");
                params.put("inspection_id", StructureScreensActivity.inspectionID);
                params.put("client_id", "2");
                params.put("is_applicable", "1");
                params.put("empty_fields", "0");
                if(cursor != null) {
                    params.put("wallcovering", cursor.getString(6));
                    params.put("eaves_soffits_fascia", cursor.getString(7));
                    params.put("exteriordoors", cursor.getString(8));
                    params.put("windows_doorframes_trim", cursor.getString(9));
                    params.put("entry_driveways", cursor.getString(10));
                    params.put("entrywalk_patios", cursor.getString(11));
                    params.put("porch_decks_steps_railings", cursor.getString(12));
                    params.put("overheadgaragedoors", cursor.getString(13));
                    params.put("surfacedrainage", cursor.getString(14));
                    params.put("retainingwalls", cursor.getString(15));
                    params.put("fencing", cursor.getString(16));
                    params.put("observations", cursor.getString(17));
                    params.put("rexteriorwalls", cursor.getString(18));
                    params.put("reaves", cursor.getString(19));
                    params.put("rexteriordoors_windows", cursor.getString(20));
                    params.put("rgrage", cursor.getString(21));
                    params.put("rporches", cursor.getString(22));
                    params.put("rdriveway", cursor.getString(23));
                    params.put("rexteriorsteps_walkways", cursor.getString(24));
                    params.put("rdeck", cursor.getString(25));
                    params.put("rlotdrainage", cursor.getString(26));
                    params.put("rlandscaping", cursor.getString(27));
                    params.put("retainwall", cursor.getString(28));
                    params.put("rfencing", cursor.getString(29));
                }

                return params;

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }
   





}
