package com.cybussolutions.hititpro.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.cybussolutions.hititpro.R;
import com.cybussolutions.hititpro.Sql_LocalDataBase.Database;
import com.cybussolutions.hititpro.Template_Inspection.StructureScreenFragment;

import java.sql.SQLException;


public class StructureScreensActivity extends AppCompatActivity {

    public static String inspectionID;
    public static String inspection_type;
    public static String client_id;
    public static String template_id;
    public static boolean is_saved;
    public static String is_notemplate;

    public static String[] foundationSpinnerValues, columnsSpinnerValues, floorStructureSpinnerValues, wallStructureSpinnerValues, ceilingStructureSpinnerValues, roofStructureSpinnerValues,
            structureObservationsSpinnerValues, roFoundationSpinnerValues, roCrawlSpacesSpinnerValues, roFloorsValues, roExteriorWallsValues, roRoofValues;
    public static String[] roofCoveringButtonValues, roofFlashingButtonValues, chimneysButtonValues, roofDrainageButtonValues, skyLightsButtonValues, methodInspectionButtonValues,
            roofingObservationsButtonValues, roSloppedButtonValues, roFlatButtonValues, roFlashingButtonValues, roChimneyButtonValues, roGutterDownspoutsButtonValues;
    public static  String[] exteriorWallCoveringButtonValues, exteriorEavesButtonValues, exteriorDoorsButtonValues, exteriorWindowButtonValues,
            exteriorDrivewaysButtonValues, exteriorWalksButtonValues, exteriorPorchButtonValues, exteriorOverheadButtonValues,
            exteriorSurfaceButtonValues, exteriorTrainingButtonValues, exteriorFencingButtonvalues, exteriorObservationButtonValues,
            roWallsButtonValues, roEavesButtonValues, roDoorsButtonValues, roGarageButtonValues, roPorchesButtonValues,
            roDrivewaysButtonValues, roStepsButtonValues, roDeckButtonValues, roDrainangeButtonValues, roLandscapButtonValues,
            roRetainingButtonValues, roFencingButtonValues;
    public static String[] wall_ceilingValues,floors_interoirValues,windowsValues,doorsValues,interior_observationsValues,
            walls_ceilingsValues,floors_roValues,windows_roValues,doors_roValues,counters_cabinetsValues,skylights_roValues,
            stairways_roValues,basement_roValues,environmental_issuesValues,walls_ceilings_roValues,floors_observerValues,windows_observeValues,
            doors_observationValues,Counters_Cabinets_observationValues,skylights_obsValues,stairways_observationValues,basement_observationValues,environmental_issues_roValues;
    public static String[] heatingEnergyButtonValues, heatingSystemButtonValues, heatingChimneysButtonValues, heatingDistributionValues, heatingComponentsButtonValues,
            heatingObservationsButtonValues, roFuranceButtonValues,
            roDuctWorkButtonValues, roBoilerButtonValues, roCombustionButtonValues, roChimneysButtonValues,
            roThermostatsButtonValues;
    public static String[] coolingEnergyButtonValues, coolingSystemButtonValues, coolingEquipmentButtonValues, coolingComponentsButtonValues,
            coolingObservationsButtonValues, roCentralButtonValues,
            roPumpButtonValues, roEvaporatorButtonValues, roFansButtonValues;
    public static String[] electricalSizeButtonValues, electricalDropButtonValues, electricalConductorsButtonValues, electricalDisconnectButtonValues,
            electricalGroundingButtonValues, electricalPanelButtonValues,
            electricalSubpanelButtonValues, electricalWiringButtonValues, electricalMethodButtonValues, electricalSwitchesButtonValues,
            electricalgfciButtonValues, electricalSmokeButtonValues, electricalObservationButtonValues, roEntranceButtonValues,
            roMainpanelButtonValues, roSubpanelButtonValues, roDistributionButtonValues, roOutletButtonValues,
            roSwitchesButtonValues, roCeilingButtonValues, roDetectorButtonValues;
    public static String[] ATTIC_INSULATION_Values,EXTERIORWALLINSULATION_Values,BASEMENTWALLINSULATION_Values,
            CRAWLSPACEINSULATION_Values,VAPORRETARDERS_Values,ROOFVENTILATION_Values,CRAWLSPACEVENTILATION_Values,
            EXHAUSTFANS_VENTS_Values,Insulation_Ventilation_Observations_Values,Attic_Roof_Values,Basement_Values
            ,CrawlSpace_Values;
    public static String[] water_supply_sourceValues,service_pipe_to_houseValues,main_water_valve_locationValues,interior_supply_pipingValues,
            waste_systemValues,water_heaterValues,dwv_pipingValues,fuel_storage_distributionValues,fuel_shut_off_valvesValues,other_components_plumbingValues,
            plumbing_observationsValues,water_heater_plimbingValues,gas_pipingValues,supply_pipingValues,dwv_piping_observationValues,fixturesValues,sump_pumpValues
            ,waste_ejector_pumpValues,water_heater_plumbingValues,gas_piping_plumbingValues,supply_plumbingValues,dwv_plumbingValues,fixtures_observationValues,sump_pump_plumbingValues,waste_ejector_pump_roValues;
    public static String[]  appliances_testedValues,laundry_facilityValues,other_components_testedValues,appliance_observationsValues,electric_rangeValues,
            gas_rangeValues,built_in_electric_ovenValues,electric_cooktopValues,gas_cooktopValues,microwave_ovenValues,dishwasherValues,waste_disposerValues,refrigeratorValues,
            wine_coolerValues,trash_compactorValues,clothes_washerValues,clothes_dryerValues,cooktop_exhaust_fanValues,central_vacuumValues,door_bellValues;
    public static String []  fireplaces_wood_stovesValues,wood_coal_stovesValues,vents_flues_chimneyValues,fireplaces_wood_stoves_observationValues,
            fireplaceValues,wood_stoveValues,fireplace_roValues,wood_stove_roValues;

    Toolbar toolbar;
    Database database;


    private static final String PORTFOLIO_TABLE = "portfolio";
    private static final String ROOFING_TABLE = "roofing";
    private static final String EXTERIROR_TABLE = "exterior";
    private static final String INTERIOR_TABLE = "interior";
    private static final String HEATING_TABLE = "heating";
    private static final String COOLING_TABLE = "cooling";
    private static final String ELECTRICAL_TABLE = "electrical";
    private static final String INSULATION_TABLE = "insulation";
    private static final String PLUMBING_TABLE = "plumbing";
    private static final String APPLIANCE_TABLE = "appliance";
    private static final String FIREPLACE_TABLE = "fireplaces";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_structure_screens);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Structure");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();

        database= new Database(StructureScreensActivity.this);

        try {
            database = database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        inspectionID = intent.getStringExtra("inspectionId");
        inspection_type = intent.getStringExtra("inspection_type");
        is_saved = false;
        is_notemplate = intent.getStringExtra("is_notemplate");
        client_id = intent.getStringExtra("client_id");
        template_id = intent.getStringExtra("template_id");



        prePopulatelocal();


        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new StructureScreenFragment()).addToBackStack("structure").commit();
    }

    @Override
    public void onBackPressed() {


        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        }
        else if(getSupportFragmentManager().getBackStackEntryCount()==1)
        {

            new AlertDialog.Builder(StructureScreensActivity.this)
                    .setTitle("Close Inspection")
                    .setMessage("Are you sure you want to Close this Inspection")
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

    public void prePopulatelocal()
    {


        if(StructureScreensActivity.inspection_type.equals("new"))
        {

            //Structure Screen
           database.clearDb();

            foundationSpinnerValues = new String[]{"Poured Concrete%0", "Slab On Grade%0", "Concrete Block%0", "Masonry Block%0", "Piers%0", "Basement Configuration%0", "Crawl Space%0", "Basement/Crawl Space Configuration%0"};
            columnsSpinnerValues = new String[]{"Steel%0", "Wood%0", "Concrete Block%0", "Wood%0"};
            floorStructureSpinnerValues = new String[]{"Wood Joist%0", "Trusses%0", "Concrete%0"};
            wallStructureSpinnerValues = new String[]{"Wood Frame%0", "Wood Frame, Brick Veneer%0", "Masonry%0", "Other%0"};
            ceilingStructureSpinnerValues = new String[]{"Joist%0", "Truss%0", "Rafters%0", "Other%0"};
            roofStructureSpinnerValues = new String[]{"Rafters%0", "Truss%0", "Joists%0", "Other%0"};
            structureObservationsSpinnerValues = new String[]{"GOOD%0", "AVERAGE%0", "MINOR SETTLEMENT%0", "SIGNIFICANT ISSUES%0"};
            roFoundationSpinnerValues = new String[]{"Cracks Minor%0", "Cracks Moderate%0", "Cracks Major%0", "Water Intrusion%0", "Floor Jacks%0", "Active Plumbing Leaks%0", "Termite Warning%0", "Basement Floor Cracks%0"};
            roCrawlSpacesSpinnerValues = new String[]{"Wood Debris/Trash%0", "Wood/Soil Contact%0", "Water Intrusion%0", "Efflorescence%0", "Floor Jacks%0", "Seal Openings%0", "Mildew/Mold on Joists%0", "Reroute Dryer Vent%0", "Vermin Activity%0", "Active Plumbing Leaks%0", "Standing Water%0"};
            roFloorsValues = new String[]{"Minor Framing Flaws%0", "Sills Near Grade%0", "Sills Below Grade%0",
                    "Joist Cracking%0", "Joist Notching%0", "Rot Visible%0"};
            roExteriorWallsValues = new String[]{"Foundation Cracks%0", "Chimney Movement%0", "Parge Exterior Walls%0"};
            roRoofValues = new String[]{"Ridge Sag%0", "Rafter Sag%0", "Collar Ties Insufficient%0", "Trusses Cut%0",
                    "Decking Delaminating%0", "Major Roof Leaks%0"};

            database.prePopulateData("foundation", foundationSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("columns", columnsSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("floor_structure", floorStructureSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("wall_structure", wallStructureSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("celling_struture", ceilingStructureSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("roof_structure", roofStructureSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("observation", structureObservationsSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("recomnd_foundation", roFoundationSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("crawl_space", roCrawlSpacesSpinnerValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("recomnd_floor", roFloorsValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("exterior_wall", roExteriorWallsValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("roof", roRoofValues, PORTFOLIO_TABLE, StructureScreensActivity.inspectionID);

            //roofing Screen
            roofCoveringButtonValues = new String[]{"Asphalt Shingle%0", "Metal%0", "Roll Roofing%0"};
            roofFlashingButtonValues = new String[]{"Metal%0", "Asphalt%0", "Not Visible%0"};
            chimneysButtonValues = new String[]{"Masonry%0", "Metal Below Siding%0", "Metal%0"};
            roofDrainageButtonValues = new String[]{"Aluminum%0", "Galvanized Steel%0", "Plastic%0", "None%0", "Downspouts Discharge Above Grade%0", "Downspouts Discharge Below Grade%0", "Downspouts Discharge Above and Below Grade%0"};
            skyLightsButtonValues = new String[]{"Curb-Type%0", "Bubble-Type%0", "None%0"};
            methodInspectionButtonValues = new String[]{"Walked on Roof%0", "Viewed From Ladder at Eave%0", "Viewed with Binoculars%0", "Other%0"};
            roofingObservationsButtonValues = new String[]{"NEWER%0", "ABOVE AVERAGE%0", "AVERAGE%0", "BELOW AVERAGE%0"};
            roSloppedButtonValues = new String[]{"Minor Repairs Only%0", "Replacement%0", "Near End of Life Cycle%0", "Moss%0", "Algae%0", "Prior Repairs%0", "Nail Heads Exposed%0", "Roof Openings%0", "Remove Debris%0", "Shingles Tear Easily%0", "Granular Shortage%0", "Short Plumbing Vents%0", "Atlas Chalet Shingles%0", "Trim Branches From Roof%0"};
            roFlatButtonValues = new String[]{"Replacement Needed%0", "Near End of Life Cycle%0", "Remove Debris%0"};
            roFlashingButtonValues = new String[]{"Old-Replacement Needed%0", "Masonry/Roof Intersection Needs Flashing%0", "Rusting%0", "Damaged%0"};
            roChimneyButtonValues = new String[]{"Masonry Re-Pointing%0", "Masonry Spalling%0", "Masonry Out of Plumb%0", "Rain Cap/Vermin Screen Needed%0", "Rusted Metal Chimney%0", "Metal Chimney Damage%0"};
            roGutterDownspoutsButtonValues = new String[]{"Cleaning Needed%0", "Cover Gutters%0", "Gutter Damage%0", "Gutter Rusting%0", "Downspout Discharges Near House%0", "Downspout Damaged/Loose%0", "Gutters and Downspouts Needed%0"};

            database.prePopulateData("roofcovering", roofCoveringButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("roofflashing", roofFlashingButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("chimneys", chimneysButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("roofdrainage", roofDrainageButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("skylights", skyLightsButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("methodofinspection", methodInspectionButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("observations", roofingObservationsButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("recommendslopedroofing", roSloppedButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("flatroofing", roFlatButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("flashing", roFlashingButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("recommendchimneys", roChimneyButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("guttersdownspouts", roGutterDownspoutsButtonValues, ROOFING_TABLE, StructureScreensActivity.inspectionID);

            //Exterior Screen
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

            //Interior Sccreen
            wall_ceilingValues = new String[]{"Drywall%0","Wood%0","Plaster%0","Paneling%0","Masonry%0","Suspended Tile%0","Tile%0"};
            floors_interoirValues = new String[]{"Carpet%0","Tile%0","Wood%0","Vinyl Resilient%0","Concrete%0","Stone%0","Slate%0"};
            windowsValues = new String[]{"Double/Single Hung%0","Sliders%0","Casement%0","Fixed Pane%0","Jalousie%0","Awning%0","Single Pane%0","Double Pane%0"};
            doorsValues = new  String []{"Wood Solid Core%0","Wood Hollow Core%0","Metal%0","Sliding Glass%0","French%0","Other%0"};
            interior_observationsValues = new  String []{"GOOD%0","AVERAGE%0","BELOW AVERAGE%0"};
            walls_ceilingsValues = new  String []{"Ceiling Water Stain (Infra-Red%0)","Ceiling Water Stain (Other)%0","Active Ceiling Leak (Infra-Red)%0","Active Ceiling Leak (Moisture Meter)%0"
                    ,"Drywall Blemishes%0","Patching%0","Damage Noted%0","Minor Ceiling /Wall Cracks%0","Drywall Tape Visible%0","Seal Trim/Miter Joints%0","Mold Like Substance%0"};
            floors_roValues = new  String []{"Loose Subflooring%0","Floor Creaking%0","Floor Popping%0","H’wood Floor Gaps%0"
                    ,"Re-Stain Floor%0","H’wood Warped%0","Tile Cracked%0","Poor Tile Install%0","Vinyl Seams Poor%0","Vinyl Floor Damage%0",
                    "Carpet Stains%0","Carpet Bunching%0","Base Molding Gap%0","Trim Incomplete%0","Trim Loose%0"};
            windows_roValues = new  String []{"Old Windows%0","Water Damage Below Sill%0","Windows Stuck%0","Sash Stuck Open%0",
                    "Window Drafting%0","Window Locked Shut%0","Window Pane Cracked%0","Window Sprung%0","Window H’ware Missing%0",
                    "Window H’ware Damaged%0","Some Seals Broken%0","Spacer Seal Showing%0","Speckled Windows%0","Window Screens Damaged%0","Window Screens Missing%0"};
            doors_roValues = new  String []{"Trim/Adjust%0","Door Stops%0","Top Latch Missing%0","Need Weather Stripping%0","Bi-Fold Door Off Track%0","Door H’ware Damaged%0"
                    ,"Door Damaged%0","Double Keyed Deadbolt%0","Daylight at Door%0","Sliding Glass Door Old%0","Sliding Door Leak%0","Garage Man Door Rating%0"};
            counters_cabinetsValues = new  String []{"Damaged Countertops%0","Missing Grout%0","Damaged Tile%0","Damaged Cabinets%0"
                    ,"Damaged Door Hinges%0","Missing/Damaged Cabinet Handles%0","Broken Drawers%0","Paint Cabinets%0"};
            skylights_roValues = new  String []{"Water Damage%0"};
            stairways_roValues = new  String []{"Loose Handrail%0","Need Handrail%0","Openings in Handrail%0","Railing Height%0",
                    "Stairway Clearance%0","Stairway Treads%0","Door Opens Into Stairway%0"};
            basement_roValues = new  String []{"No Leaks Visible%0","Leaking Repair%0","Leaking Monitor%0","Leaking With Sump Pump Present%0"};
            environmental_issuesValues = new  String []{"Possible Asbestos%0","3 Hour Radon Screening%0"};
            walls_ceilings_roValues = new  String []{"Ceiling Water Stain (Infra-Red)%0","Ceiling Water Stain (Other)%0"
                    ,"Active Ceiling Leak (Infra-Red)%0","Active Ceiling Leak (Moisture Meter)%0","Drywall Blemishes%0",
                    "Patching%0","Damage Noted%0","Minor Ceiling / Wall Cracks%0","Drywall Tape%0","Seal Trim Miter Joints%0","Mold-Like Substance%0"};
            floors_observerValues = new  String []{"Loose Subflooring%0","Floor Creaking%0","Floor Popping%0","H’wood Floor Gaps%0"
                    ,"Re-Stain Floor%0","H’wood Warped%0","Tile Cracked%0","Poor Tile Install%0","Vinyl Seams Poor%0","Vinyl Floor Damage%0",
                    "Carpet Stains%0","Carpet Bunching%0","Base Molding Gap%0","Trim Incomplete%0","Trim Loose%0"};
            windows_observeValues = new  String []{"Old Windows%0","Water Damage Below Sill%0","Windows Stuck%0","Sash Stuck Open%0",
                    "Window Drafting%0","Window Locked Shut%0","Window Pane Cracked%0","Window Sprung%0","Window H’ware Missing%0",
                    "Window H’ware Damaged%0","Some Seals Broken%0","Spacer Seal Showing%0","Speckled Windows%0","Window Screens Damaged%0","Window Screens Missing%0"};
            doors_observationValues = new  String []{"Trim/Adjust%0","Door Stops%0","Top Latch Missing%0","Need Weather Stripping%0","Bi-Fold Door Off Track%0","Door H’ware Damaged%0"
                    ,"Door Damaged%0","Double Keyed Deadbolt%0","Daylight at Door%0","Sliding Glass Door Old%0","Sliding Door Leak%0","Garage Man Door Rating%0"};
            Counters_Cabinets_observationValues = new  String []{"Damaged Countertops%0","Missing Grout%0","Damaged Tile%0","Damaged Cabinets%0"
                    ,"Damaged Door Hinges%0","Missing/Damaged Cabinet Handles%0","Broken Drawers%0","Paint Cabinets%0"};
            skylights_obsValues = new  String []{"Water Damage%0"};
            stairways_observationValues = new  String []{"Loose Handrail%0","Need Handrail%0","Openings in Handrail%0","Railing Height%0",
                    "Stairway Clearance%0","Stairway Treads%0","Door Opens Into Stairway%0"};
            basement_observationValues = new String[]{"No Leaks Visible%0","Leaking Repair%0","Leaking Monitor%0","Leaking With Sump Pump Present%0"};
            environmental_issues_roValues = new  String []{"Possible Asbestos%0","3 Hour Radon Screening%0"};

            database.prePopulateData("wall_cieling", wall_ceilingValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("floors", floors_interoirValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("windows", windowsValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("doors", doorsValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("observation", interior_observationsValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rwalls_ceiling", walls_ceilingsValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rfloors", floors_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rwindows", windows_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rdoors", doors_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rcounters_cabinets", counters_cabinetsValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rskylights", skylights_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rstairways", stairways_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rbasement", basement_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("renvironmentalissues", environmental_issuesValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("walls_ceilings_ro", walls_ceilings_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("floors_observer", floors_observerValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("windows_observe", windows_observeValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("doors_observation", doors_observationValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("Counters_Cabinets_observation", Counters_Cabinets_observationValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("skylights_obs", skylights_obsValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("stairways_observation", stairways_observationValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("basement_observation", basement_observationValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("environmental_issues_ro", environmental_issues_roValues, INTERIOR_TABLE, StructureScreensActivity.inspectionID);

            //Heating Sccreen

            heatingEnergyButtonValues = new String[]{"Gas%0", "Electricity%0", "Oil%0","Wood%0","Other%0"};
            heatingSystemButtonValues = new String[]{"Forced Air Furnace%0", "Air Handler for Heat Pump%0", "Hot Water Boiler%0","Steam Boiler%0","Manufacturer%0","Age%0"};
            heatingChimneysButtonValues = new String[]{"Metal Single Wal%0l", "Metal Multi-Wall%0", "Plastic%0","Masonry%0","Not Visible%0"};
            heatingDistributionValues = new String[]{"Ductwork%0", "Radiators%0", "Baseboard Heaters%0", "Other%0"};
            heatingComponentsButtonValues = new String[]{"Humidifier%0", "Condensate Pump%0", "Electronic Air Cleaner%0", "Other%0"};
            heatingObservationsButtonValues = new String[]{"SINGLE FURNACE GENERALLY OK DESCRIPTION%0", "MULTIPLE FURNACES GENERALLY OK DESCRIPTION%0",
                    "NOT OPERATIVE SINGLE FURNACE%0","NOT OPERATIVE IN MULTI FURNACE CONFIGURATION%0"};
            roFuranceButtonValues = new String[]{"Older Single Unit Needs Service%0","Older Multiple Units Need Service%0",
                    "Replacement Rusted Burner%0 ","Near End of Life Cycle%0", "Replacement Imminent%0","Seal Openings%0",
                    "Flexible Gas Line%0","Sediment Trap Missing/Improper%0","Air Filter Dirty%0","Humidifier Needs Maintenance%0",
                    "Condensate Line Dirty%0","Condensate Pump Inoperative%0","Carbon Monoxide Detected%0","Natural Gas Leak Detected%0"};
            roDuctWorkButtonValues = new String[]{"No Ductwork in Basement%0", "No Heat/Cooling Supply%0", "Low Flow at Register%0"
                    , "Duct Flow Restricted%0", "Supply Vent Disconnected%0", "Proximity of Furnace Poor%0", "Seal Ductwork%0",
                    "Electronic Dampers Suspect%0","Asbestos Tape on Ductwork%0"};
            roBoilerButtonValues = new String[]{"Near End of Life %0", "Older Boiler Unit%0", "Older Boiler Unit%0", "Asbestos Covering%0", "Servicing Needed%0"};
            roCombustionButtonValues = new String[]{"Combustion Air Lacking%0", "Burners Dirty%0", "Burners Rusting%0","Flashback%0", "Back Drafting%0",
                    "Poor Flue Connections%0","Flue Slope%0 ", "Flue Slope%0"};
            roChimneysButtonValues = new String[]{"Liner Needed%0", "Obstructed Chimney%0"};
            roThermostatsButtonValues = new String[]{"Inoperative%0", "Loose%0", "Old/Replace%0"};

            database.prePopulateData("energy_source", heatingEnergyButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("heatingsystemtype", heatingSystemButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("vent_flues_chimneys", heatingChimneysButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("heatdistributionmethods", heatingDistributionValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("othercomponents", heatingComponentsButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("observation", heatingObservationsButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rfurnace", roFuranceButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rsupplyairductwork", roDuctWorkButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("boiler", roBoilerButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("combustion_exhaust", roCombustionButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("furnace_chimneys", roChimneysButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("thermostats", roThermostatsButtonValues, HEATING_TABLE, StructureScreensActivity.inspectionID);

            //Cooling Sccreen
            coolingEnergyButtonValues = new String[]{"Gas%0", "Electricity%0", "Other%0"};
            coolingSystemButtonValues = new String[]{"Air Cooled Central Air%0", "Air Source Heat Pump System%0", "Water Source Heat Pump System%0",
                    "Ground Source Heat Pump System%0", "Evaporator Cooler%0", "Capacity in Tons ??? (1 Ton Serves Approx 500-600 sf)%0",
                    "Manufacturer / Age ???%0", "Serial Number ???%0"};
            coolingEquipmentButtonValues = new String[]{"Present At ???%0"};
            coolingComponentsButtonValues = new String[]{"Whole House Fan%0"};
            coolingObservationsButtonValues = new String[]{"SINGLE A/C UNIT GENERALLY OK DESCRIPTION%0", "MULTIPLE A/C UNITS GENERALLY OK DESCRIPTION%0",
                    "NOT COOLING PROPERLY SINGLE UNIT%0", "NOT COOLING PROPERLY MULTI A/C CONFIGURATION%0"};

            roCentralButtonValues = new String[]{"Inoperative System%0", "Old System%0", "Very Old System%0", "Undersized%0", "Not Cooling Adequately%0",
                    "Temperature Drop Excessive%0", "Clean Outdoor Unit%0", "Insulation Damage to Refrigerant Lines%0",
                    "Units Out of Level%0", "Outdoor Unit Fin Damage%0", "Outdoor Unit Noisy", "Cut Back Vegetation%0",
                    "Re-route Condensate Line%0", "Negative Slope on Condensate Line%0"};
            roPumpButtonValues = new String[]{"Inoperative System%0", "Old System%0", "Very Old System%0", "Undersized%0",
                    "Not Cooling Adequately%0", "Temperature Drop Excessive%0", "Clean Outdoor Unit%0",
                    "Insulation Damage to Refrigerant Lines%0", "Units Out of Level%0", "Outdoor Unit Fin Damage%0",
                    "Outdoor Unit Noisy%0", "Cut Back Vegetation%0", "Re-route Condensate Line%0",
                    "Negative Slope on Condensate Line%0"};
            roEvaporatorButtonValues = new String[]{"Inoperative%0", "Older System%0", "Lacking Maintenance%0",
                    "Noisy%0", "Spray Nozzle Restricted%0", "Float Valve Suspect%0", "Pump Suspect%0",
                    "Excess Humidity in House%0"};
            roFansButtonValues = new String[]{"Inoperative%0", "Removal Recommended%0"};

            database.prePopulateData("energysource", coolingEnergyButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("centralsystemtype", coolingSystemButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("throughwallequipment", coolingEquipmentButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("othercomponents", coolingComponentsButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("heatobservation", coolingObservationsButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("recomndcentralaircondition", roCentralButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("heatpumps", roPumpButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("evaporator", roEvaporatorButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("housefans", roFansButtonValues, COOLING_TABLE, StructureScreensActivity.inspectionID);

            //Electrical Sccreen

            electricalSizeButtonValues = new String[]{"100 Amps  120/240v Main Service%0", "150 Amps  120/240v Main Service%0", "200 Amps  120/240v Main Service%0", "(2) 150 Amps  120/240v Main Service%0"};
            electricalDropButtonValues = new String[]{"Overhead%0", "Underground%0", "Not Visible%0"};
            electricalConductorsButtonValues = new String[]{"Aluminum%0", "Copper%0", "Not Visible%0"};
            electricalDisconnectButtonValues = new String[]{"Main Service Rating 100 Amps%0", "Main Service Rating 150 Amps%0", "Main Service Rating 200 Amps%0", "Main Service Rating (2) 150 Amps%0"};
            electricalGroundingButtonValues = new String[]{"Copper%0", "Aluminum%0", "Ground Rod Connection%0", "UFER Ground%0", "Connection Not Visible%0"};
            electricalPanelButtonValues = new String[]{"Panel Rating 100 Amps%0", "Panel Rating 150 Amps%0", "Panel Rating 200 Amps%0", "Fuses%0", "Breakers%0", "Location ???%0"};
            electricalSubpanelButtonValues = new String[]{"Panel Rating  60 Amps%0", "Panel Rating 100 Amps%0", "Panel Rating 150 Amps%0", "Breakers%0", "Fuses%0", "Location???%0"};
            electricalWiringButtonValues = new String[]{"Copper%0", "Aluminum Solid Conductor%0", "Aluminum Multi-Strand%0"};
            electricalMethodButtonValues = new String[]{"Non- Metalic  Cable Romex%0", "Tinned Copper%0", "Armored Cable BX%0", "Fabric Covered%0", "Knob and Tube Copper%0"};
            electricalSwitchesButtonValues = new String[]{"Grounded%0", "Ungrounded%0", "Grounded and Ungrounded%0"};
            electricalgfciButtonValues = new String[]{"Bathroom(s)%0", "Kitchen%0", "Exterior%0", "Garage%0", "Jetted Tub%0"};
            electricalSmokeButtonValues = new String[]{"Present%0", "Smoke Detectors Present%0", "None Observed%0"};
            electricalObservationButtonValues = new String[]{"MINOR IMPROVEMENTS NEEDED%0", "NO IMPROVEMENTS%0", "NUMEROUS REPAIRS NEEDED%0", "OBSOLETE SYSTEM%0"};

            roEntranceButtonValues = new String[]{"Undersized%0", "Tap on Main Service%0", "Drip Loop Insufficient%0", "Clearance Inadequate%0", "Poorly Attached%0", "Service Conductors Need Conduit%0", "Rusted Service Box%0", "Damaged Service Box%0", "Ground Rod Ineffective%0"};
            roMainpanelButtonValues = new String[]{"Old%0", "Federal Pacific Panel%0", "Zinsko Panel%0", "Panel Location Poor%0", "Panel Crowded%0", "Close Panel Openings%0",
                    "Panel Front Missing%0", "Panel Poorly Labeled%0", "Wiring Clamps Missing%0", "Blunt Panel Screws Needed%0", "Overheated Wiring%0", "Double Taps Present%0",
                    "Oversized AC Breaker%0"};
            roSubpanelButtonValues = new String[]{"Old%0", "Panel Location Poor%0", "Panel Crowded%0", "Close Panel Openings%0", "Panel Poorly Labeled%0",
                    "Wiring Clamps Missing%0", "Blunt Panel Screws Needed%0", "Overheated Wiring%0", "Double Taps Present%0"};
            roDistributionButtonValues = new String[]{"Abandoned Wiring%0", "Loose Wiring%0", "Overheated%0", "Exposed Wiring%0", "Extension Cords%0", "Open Junction Box%0",
                    "Loose Junction Box%0", "Poor Wiring Installation%0", "Knob and Tube%0"};
            roOutletButtonValues = new String[]{"Inoperative%0", "Damaged%0", "Missing Covers%0", "Loose Outlet%0", "Missing Cover Plate%0", "Ungrounded 3-Prong Outlet%0",
                    "Grounding Pins Broken%0", "Outlet Layout Inadequate%0", "Reversed Polarity%0", "Most Outlets Ungrounded%0", "Overheated Outlet%0",
                    "GFCI Inoperative%0", "GFCI Did Not Trip%0", "GFCI Recommended%0", "3-Prong Dryer Outlet%0", "Dryer Outlet Inoperative%0", "Add ARC Fault Breakers%0", "Add Surge Protector%0"};
            roSwitchesButtonValues = new String[]{"Inoperative%0", "Damaged%0", "Loose%0", "Non-Functioning 3-Way%0", "Overheated%0", "Inoperative Dimmer Switch%0"};
            roCeilingButtonValues = new String[]{"Non-Working Lights%0", "Damaged Fixture%0", "Loose Fixture%0", "Missing Globe%0", "Recessed Light Rating%0", "Ceiling Fan Inoperative%0",
                    "Ceiling Fan Wobbles%0", "Ceiling Fan Loose%0", "Fan Not Operable at Low Speeds%0"};
            roDetectorButtonValues = new String[]{"No Detectors Present%0", "Need CO Detector%0", "Smoke Detector Non-Responsive%0", "Update Smoke Detectors%0"};


            database.prePopulateData("sizeofservice", electricalSizeButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("servicedrop", electricalDropButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("entranceconductors", electricalConductorsButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("maindisconnect", electricalDisconnectButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("grounding", electricalGroundingButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("servicepanel", electricalPanelButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("sub_panel", electricalSubpanelButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("wiring", electricalWiringButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("wiring_method", electricalMethodButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("switches_receptacles", electricalSwitchesButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("gfci", electricalgfciButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("smoke_codetector", electricalSmokeButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("observation", electricalObservationButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("serviceentrance", roEntranceButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("mainpanel", roMainpanelButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("subpanel", roSubpanelButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("distribution", roDistributionButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("outlets", roOutletButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("switches", roSwitchesButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("lights_ceiling_fans", roCeilingButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("smoke_co_detectors", roDetectorButtonValues, ELECTRICAL_TABLE, StructureScreensActivity.inspectionID);

            //Insulation Sccreen

            ATTIC_INSULATION_Values = new String[]{"R 30 Fiberglass%0","R 37 Fiberglass%0","R 25-30 Fiberglass%0","R 30 Cellulose%0",
                    "Attic Inaccessible%0","None Visible%0","None%0"};
            EXTERIORWALLINSULATION_Values = new String[]{"R 13%0","R 19%0","Not Visible%0","None%0"};
            BASEMENTWALLINSULATION_Values = new String[]{"R 13 On Visible Portions%0","R 19 On Visible Portions%0","Not Visible%0","None%0"};
            CRAWLSPACEINSULATION_Values = new String[]{"R 19 In Floor Above Crawl Space%0","R 13 In Floor Above Crawl Space%0","None Visible%0",
                    "None%0","Crawl Space Inaccessible%0"};
            VAPORRETARDERS_Values = new String[]{"Plastic%0","Kraft Paper%0","None Visible%0"};
            ROOFVENTILATION_Values = new String[]{"Roof Vents%0","Ridge Vents%0","Gable Vents%0","Soffit Vents%0","Power Ventilator%0",
                    "None Visible%0"};
            CRAWLSPACEVENTILATION_Values = new String[]{"Exterior Wall Vents%0","Vents to Interior%0","No Ventilation Visible%0"};
            EXHAUSTFANS_VENTS_Values = new String[]{"Bathrooms%0","Kitchen Cooktop Hood Vent%0","Dryer%0","Kitchen Cooktop Downdraft%0",
                    "Whole Kitchen Vent%0"};
            Insulation_Ventilation_Observations_Values = new String[]{"WELL INSULATED HOME%0","OK INSULATED HOME%0","POORLY INSULATED HOME%0"};
            Attic_Roof_Values = new String[]{"Even Out Insulation%0","Vermin Activity%0","Compressed Insulation%0","Add Bags of Insulation / Stairs%0"
                    ,"Improve Insulation and Stairs%0","Too Many Platforms%0","Vermin Activity %0","Ridge Vent Recommended%0","Improper Ridge Vent Installation%0"
                    ,"Soffit Vents Congested%0","Condensation / Mildew%0","Power Ventilator Inoperative%0","Ducts in Attic Need Insulation%0",
                    "Vent Exhaust Fans Outside%0","Trail Runs%0"};
            Basement_Values = new String[]{"Insulation Improvement%0","Vermin Activity%0"};
            CrawlSpace_Values = new String[]{"Improper Dryer Venting%0","Crawl Space Mold / Mildew%0","Floor Insulation Needed%0","Loose Floor Insulation%0",
                    "Obstructed Wall Vents%0","Ventilation Needed%0","Ductwork Needs Insulation%0","Moisture Barrier Needed%0","Moisture Barrier Adjustment%0","Vermin Activity%0"};

            database.prePopulateData("atticinsulation", ATTIC_INSULATION_Values, INSULATION_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("exteriorwallinsulation", EXTERIORWALLINSULATION_Values, INSULATION_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("basementwallinsulation", BASEMENTWALLINSULATION_Values, INSULATION_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("csv", CRAWLSPACEINSULATION_Values, INSULATION_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("efv", EXHAUSTFANS_VENTS_Values, INSULATION_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("crawlspaceinsulation", ROOFVENTILATION_Values, INSULATION_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("vaporretarders", VAPORRETARDERS_Values, INSULATION_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("roofventilation", ROOFVENTILATION_Values, INSULATION_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("observations", Insulation_Ventilation_Observations_Values, INSULATION_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("atticandroof", Attic_Roof_Values, INSULATION_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("basement", Basement_Values, INSULATION_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("crawlspace", CrawlSpace_Values, INSULATION_TABLE, StructureScreensActivity.inspectionID);

            //Plumbing Sccreen
            water_supply_sourceValues =  new String[]{"Public%0","Private%0","Unknown%0"};
            service_pipe_to_houseValues =  new String[]{"Copper%0","Plastic%0","Steel%0","Lead%0","Not Visible%0"};
            main_water_valve_locationValues =  new String[]{"Front Wall Basement%0","Rear Wall of Basement%0","Crawl Space%0","Beside Water Heater%0","Location ???%0","Not Found%0"};
            interior_supply_pipingValues =  new String[]{"Copper%0","Plastic%0","Steel%0","Lead%0"};
            waste_systemValues =  new String[]{"Public Sewer%0","Private Sewer%0","Unknown%0"};
            dwv_pipingValues =  new String[]{"Plastic%0","Cast Iron%0","Steel%0","Copper%0","Lead%0"};
            water_heaterValues =  new String[]{"Gas%0","Electric%0","Tank Capacity%0","Age%0","Manufacturer%0","Tankless Gas%0",
                    "Tankless Electric%0"};
            fuel_storage_distributionValues =  new String[]{"Natural Gas%0","Propane%0","Liquid Petrolium%0", "None; All Electric%0"};
            fuel_shut_off_valvesValues =  new String[]{"Natural Gas Main Side of Home%0","Natural Gas Main %0",
                    "Propane Shut-Off %0","LP Shut-Off%0","Heating Oil Tank %0"};
            other_components_plumbingValues =  new String[]{"Pressure Regulator on Main Line%0","Sump Pump%0",
                    "Sewer Ejector Pump%0","Hot Water Circulator%0","Backflow Devices on Hose Bibs%0","Sprinkler  System%0"};
            plumbing_observationsValues =  new String[]{"OVERALL GOOD CONDITION%0","OVERALL GOOD WITH OLDER PLUMBING FIXTURES%0","NUMEROUS REPAIRS NEEDED%0"};
            water_heater_plimbingValues =  new String[]{"Very Old Unit%0","Old Unit%0","Leakage Evident%0", "Thermal Expansion Device Needed%0",
                    "Sediment Trap%0","Capacity Questionable%0","Burner Dirty/Rust%0","No Cold Water Shut-Off%0", "No TPR Valve%0"
                    ,"Leaking TPR Valve%0","Wiring Connection Poor%0","Exhaust Spillage %0","Missing Burner Cover%0",
                    "Lack of Combustion Air%0","Improve Draft Diverter%0"};
            gas_pipingValues =  new String[]{"Gas Leak%0","Cap Gas Lines %0","Main Shut-Off  Not Found%0","Secure Gas Line%0","Gas Line Corrosion%0",
                    "Drip Leg Needed%0","Shut-Off  Valve Needed%0","Dryer Gas Line Cap Needed%0","Support Lacking%0"};
            supply_pipingValues =  new String[]{"Pressure Regulator Needed%0","Label Water Shut-Offs%0","Leak%0"
                    ,"Polybutelyne%0","Freezing Potential%0","Support Lacking%0","Supply Handle Missing%0","Pipe Hammer%0"};
            dwv_piping_observationValues =  new String[]{"Leaks%0","Flexible Piping%0","Overflow Pan for Washer%0",
                    "Overflow Pan for WH%0","Trap Leak%0","S Trap%0","Insufficient Slope%0","Older Piping %0","Support Lacking%0",
                    "No Main Clean-Out Found%0","Septic System Disclosure%0","Septic System Disclaimer%0","Odor%0","Vent Stack Height Inadequate%0"};
            fixturesValues =  new String[]{"Older%0","Insufficient Water Flow%0","Hot/Cold Reversed%0","Faucet Leaky%0",
                    "Previous Leaks Under Sink%0","Under Sink Openings%0","Sink Drains Slow%0","Sink Drain Damaged%0",
                    "Sink Drain Missing%0","Loose Toilet Only%0","Loose Toilet Wax Ring%0","Toilet Lid Cracked%0","Toilet Slow Flush%0",
                    "Toilet Slow Flush%0","Toilet Old%0","Shower Head Leak%0","Hot Shower/Cold Shower%0", "Seal Shower Fixtures%0",
                    "Backsplash Caulking Top%0","Backslash Caulking Bottom%0","Shower Stall Tile/Grout%0","Shower Stall Tile Damage%0",
                    "Shower Stall Drains Slow%0","Bathtub Tile/Grout%0","Bathtub Enclosure Caulk%0","Bathtub Floor Damage%0",
                    "Bathtub No Drain Plug%0","Bathtub Drains Slow%0","Jetted Tub Inoperative%0","Jetted Tub Noisy%0","Jetted Tub No Motor Access%0"
                    ,"Jetted Tub Lacks GFCI%0","Laundry Tub Loose%0","Exhaust Fan Needed%0","Exhaust Fan Inoperative%0","Hose Bib Anti-Siphon Needed%0"};
            sump_pumpValues =  new String[]{"Inoperative%0","Old%0 ","Discharge Line Suspect%0","Cover Needed%0"};
            waste_ejector_pumpValues =  new String[]{"Inoperative%0","Seal Openings%0","No Vent Visible%0"};
            water_heater_plumbingValues =  new String[]{"Very Old Unit%0","Older Unit%0","Leakage Evident%0","Thermal Expansion Device Needed%0",
                    "Sediment Trap%0","Capacity Questionable%0","Burner Dirty/Rust%0","No Cold Water Shut-Off%0","No TPR Valve%0",
                    "Leaking TPR Valve%0","TPR Discharge Improvement%0","Wiring Connection Poor%0","Missing Burner Cover%0"
                    ,"Exhaust Spillage%0","Lack of Combustion Air%0","Improve Draft Diverter%0","Vent Pipe Clearance%0",
                    "Protect WH from Damage%0","Older WH Garage Floor Clearance Needed%0"};
            gas_piping_plumbingValues =  new String[]{"Gas Leak%0","Cap Gas Lines%0","Main Shut-Off Not Found%0","Secure Gas Line%0",
                    "Gas Line Corrosion%0", "Drip Leg Needed%0","Shut-Off Valve Needed%0","Dryer Gas Line Cap Needed%0","Support Lacking%0",
                    "Gas Meter Locked%0"};
            supply_plumbingValues =  new String[]{"Pressure Regulator Needed%0","Label Water Shut-Offs%0","Leak%0",
                    "Polybutylene%0","Freezing Potential%0","Support Lacking%0","Supply Handle Missing%0","Pipe Hammer%0"};
            dwv_plumbingValues =  new String[]{"Leaks%0","Flexible Piping%0","Overflow Pan for Washing Machine%0",
                    "Overflow Pan for WH%0","Trap Leak%0","S-Trap%0", "Insufficient Slope%0","Support Lacking%0","Older Piping%0",
                    "No Main Clean-Out Found%0","Septic System Warning/Disclosure%0", "Septic System Disclaimer%0",
                    "Odor%0","Vent Stack Height Insufficient%0"};
            fixtures_observationValues =  new String[]{"Older%0","Insufficient Water Flow%0","Hot/Cold Reversed%0","Faucet Leaking%0","Previous Leaks Under Sinks%0"
                    ,"Under Sink Openings%0","Sink Drains Slow%0", "Sink Drain Damaged%0","Sink Drain Missing%0","Loose Toilet Only%0"
                    ,"Loose Toilet Wax Ring%0","Toilet Lid Cracked%0","Toilet Slow Flush%0","Toilet Runs%0",
                    "Toilet Old%0", "Shower Head Leak%0","Hot Shower/Cold Shower%0","Seal Shower Fixtures%0","Backsplash Top Caulking%0"
                    ,"Backsplash Bottom Caulking%0","Shower Stall Tile Grout/Caulk%0","Shower Stall Tile Damage%0","Shower Stall Drains Slow%0",
                    "Bathtub Tile Grout/Caulk%0","Bathtub Floor Damage %0","Bathtub Enclosure Caulk%0","Bathtub Drains Slow%0",
                    "Bathtub No Drain Plug%0","Jetted Tub Inoperative%0","Jetted Tub Noisy%0","Jetted Tub No Motor Access%0",
                    "Jetted Tub Lacks GFCI%0","Waste Ejector Pump%0","Laundry Tub Loose%0","Exhaust Fan Needed%0","Exhaust Fan Inoperative%0","Hose Bib Anti-Siphon Needed%0 "
            };
            sump_pump_plumbingValues =  new String[]{"Inoperative%0","Old%0","Discharge Line Suspect%0","Cover Needed%0"};
            waste_ejector_pump_roValues =  new String[]{"Inoperative%0","Unsealed Openings%0","No Vent%0"};

            database.prePopulateData("watersupplysource", water_supply_sourceValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("servicepipe", service_pipe_to_houseValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("mainwatervalvelocation", main_water_valve_locationValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("interiorsupply", interior_supply_pipingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("wastesystem", waste_systemValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("waterheater", water_heaterValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("dwvpiping", dwv_pipingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("fuelshortage_distribution", fuel_storage_distributionValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("fuelshutoffvalves", fuel_shut_off_valvesValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("othercomponents", other_components_plumbingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("observation", plumbing_observationsValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rwaterheater", water_heater_plimbingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("gaspiping", gas_pipingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rsupplypiping", supply_pipingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("dwvpiping", dwv_piping_observationValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("fixtures", fixturesValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("sumppump", sump_pumpValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("wasteejectorpump", waste_ejector_pumpValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("gas_piping_plumbing", gas_piping_plumbingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("supply_plumbing", supply_plumbingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("dwv_plumbing", dwv_plumbingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("fixtures_observation", fixtures_observationValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("sump_pump_plumbing", sump_pump_plumbingValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("waste_ejector_pump_ro", waste_ejector_pump_roValues, PLUMBING_TABLE, StructureScreensActivity.inspectionID);

            //Applliences Screen
            appliances_testedValues = new String[]{"Electric Range%0","Gas Range%0","Built-In Electric Oven %0","Electric Cooktop%0",
                    "Gas Cooktop%0","Microwave Oven%0","Dishwasher%0","Waste Disposer%0","Refrigerator%0","Wine Cooler%0","Trash Compactor%0"
                    ,"Clothes Washer%0","Clothes Dryer%0"};
            laundry_facilityValues = new String[]{"240V Circuit for Dryer%0","Gas Piping for Dryer%0","Vents to Building Exterior%0",
                    "120V Circuit for Washer%0","Waste Standpipe for Washer%0"};
            other_components_testedValues = new String[]{"Kitchen Exhaust Hood%0","Cooktop Exhaust Fan%0","Central Vacuum%0"
                    ,"Door Bell%0","Instant Hot Water Dispenser%0","Water Conditioning Equipment%0"};
            appliance_observationsValues = new String[]{"ALL TESTED APPLIANCES OK%0","MOST APPLIANCES NEWER%0","MOST TESTED APPLIANCES OK%0"
                    , "AGING APPLIANCES%0","VERY OLD APPLIANCES%0"};
            electric_rangeValues = new String[] {"Need Anti-Tip Bracket%0","Burner Inoperative %0","Oven Light Inoperative%0"};
            gas_rangeValues = new String[]{"Need Anti-Tip Bracket%0","Burner Inoperative %0","Oven Light Inoperative%0"};
            built_in_electric_ovenValues = new String[]{"Old%0","Inoperative %0","Oven Light Inoperative%0"};
            electric_cooktopValues = new String[]{"Old%0","Burner Inoperative%0","Inoperative %0"};
            gas_cooktopValues = new String[]{"Old%0","Burner Inoperative%0","Inoperative %0"};
            microwave_ovenValues = new String[]{"Inoperative %0","Under Mount Light Inoperative%0"};
            dishwasherValues= new String[]{"Old%0","Inoperative%0","Leak%0","Loose %0"};
            waste_disposerValues = new String[]{"Inoperative%0","Needs Wire Clamp%0","Seized%0","Loose%0","Wiring Loose%0"};
            refrigeratorValues = new String[]{"Old %0","Inoperative%0"};
            wine_coolerValues = new String[]{"Old %0","Inoperative%0"};
            trash_compactorValues = new String[]{"Old %0","Inoperative%0"};
            clothes_washerValues = new String[]{"Old%0","Inoperative%0"};
            clothes_dryerValues = new String[]{"Old %0","Inoperative%0"};
            cooktop_exhaust_fanValues = new String[]{"Inoperative%0"};
            central_vacuumValues = new String[]{"Inoperative%0"};
            door_bellValues = new String[]{"Inoperative%0","None%0"};

            database.prePopulateData("appliancestested", appliances_testedValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("laundryfacility", laundry_facilityValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("othercomponentstested", other_components_testedValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("observations", appliance_observationsValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("relectricrange", electric_rangeValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rgasrange", gas_rangeValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rbuiltinelectricoven", built_in_electric_ovenValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("relectriccooktop", electric_cooktopValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rgascooktop", gas_cooktopValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rmicrowaveoven", microwave_ovenValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rdishwasher", dishwasherValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rwastedisposer", waste_disposerValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rrefrigerator", refrigeratorValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rwinecooler", wine_coolerValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rtrashcompactor", trash_compactorValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rclotheswasher", clothes_washerValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rclothesdryer", clothes_dryerValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rcooktopexhaustfan", cooktop_exhaust_fanValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rcentralvacuum", central_vacuumValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("rdoorbell", door_bellValues, APPLIANCE_TABLE, StructureScreensActivity.inspectionID);

            //fireplace Screen
            fireplaces_wood_stovesValues = new String[]{"Masonry%0","Factory Insert%0","Steel Firebox%0","Direct Vent%0",
                    "Vent Free%0","Gas%0"};
            wood_coal_stovesValues = new String[]{"Wood Stove%0"};
            vents_flues_chimneyValues = new String[]{"Outside Combustion Air%0","Masonry%0","Metal%0"};
            fireplaces_wood_stoves_observationValues = new String[]{"ABOVE AVERAGE%0","BELOW AVERAGE%0"};
            fireplaceValues = new String[]{"Inspection Needed%0","Firebox Repair Needed%0","Firebox Repair Needed%0","Seal Gas Line Opening%0",
                    "Fireplace Key Hole Jammed%0","Damper Repairs%0","Hearth Insufficient%0"};
            wood_stoveValues = new String[]{"Old%0","Clearance From Combustibles%0","Inadequate Hearth%0","Improper Flue Pipe Configuration%0"};
            fireplace_roValues = new String[]{"Inspection Needed%0","Firebox Repair Needed%0","Firebox Repair Needed%0"
                    ,"Seal Gas Line Opening%0","Fireplace Key Hole Jammed%0","Damper Repairs Needed%0","Hearth Insufficient%0"};
            wood_stove_roValues = new String[]{"Old%0","Clearance From Combustibles%0","Inadequate Hearth%0","Improper Flue Configuration%0"};

            database.prePopulateData("fireplaceswoodstoves", fireplaces_wood_stovesValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("woodcoalstoves", wood_coal_stovesValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("ventsflueschimney", vents_flues_chimneyValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("observations", fireplaces_wood_stoves_observationValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("recommendationsfireplace", fireplaceValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("recommendationswood", wood_stoveValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("fireplace_ro", fireplace_roValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);
            database.prePopulateData("wood_stove_ro", wood_stove_roValues, FIREPLACE_TABLE, StructureScreensActivity.inspectionID);

        }


    }

}
