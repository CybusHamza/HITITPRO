package com.cybussolutions.hititpro.Sql_LocalDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cybussolutions.hititpro.Activities.StructureScreensActivity;

import java.sql.SQLException;


public class Database {

    static final String DATABASE_NAME = "HitItPro.db";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_PORTFOLIO = "create table " + "portfolio" + "( "
            + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "template_id  INTEGER, inspection_id INTEGER , client_id INTEGER , is_applicable INTEGER" +
            ",empty_fields INTEGER ,foundation varchar ,columns varchar ,floor_structure varchar , wall_structure varchar " +
            ",celling_struture varchar ,roof_structure varchar ,observation varchar ,recomnd_foundation varchar " +
            ",crawl_space varchar ,recomnd_floor varchar,exterior_wall varchar,roof varchar" +
            ",columns_comments varchar,floor_structure_comments varchar,wall_structure_comments varchar,celling_struture_comments varchar" +
            ",roof_structure_comments varchar,observation_comments varchar,recomnd_foundation_comments varchar," +
            " crawl_space_comments varchar,recomnd_floor_comments varchar,exterior_wall_comments varchar," +
            " roof_comments varchar ,addedby  INTEGER ,addedon  INTEGER );); ";
    static final String DATABASE_ROOFING = "create table " + "roofing" + "( "
            + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "template_id  INTEGER, inspection_id INTEGER , client_id INTEGER , is_applicable INTEGER" +
            ",empty_fields INTEGER ,roofcovering varchar ,roofflashing varchar ,chimneys varchar , roofdrainage varchar " +
            ",skylights varchar ,methodofinspection varchar ,observations varchar ,recommendslopedroofing varchar " +
            ",flatroofing varchar ,flashing varchar,recommendchimneys varchar,guttersdownspouts varchar,roofcovering_comments varchar" +
            ",roofflashing_comments varchar,chimneys_comments varchar,roofdrainage_comments varchar,skylights_comments varchar" +
            ",methodofinspection_comments varchar,observations_comments varchar,recommendslopedroofing_comments varchar," +
            " flatroofing_comments varchar,flashing_comments varchar,recommendchimneys_comments varchar," +
            " guttersdownspouts_comments varchar ,addedby  INTEGER ,addedon  INTEGER );); ";
    static final String DATABASE_PLUMBING = "create table " + "plumbing" + "( "
            + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "template_id  INTEGER, inspection_id INTEGER , client_id INTEGER , is_applicable INTEGER" +
            ",empty_fields INTEGER ,watersupplysource varchar ,servicepipe varchar ,mainwatervalvelocation varchar , interiorsupply varchar " +
            ",wastesystem varchar ,strudwvpiping varchar ,waterheater varchar ,fuelshortage_distribution varchar " +
            ",fuelshutoffvalves varchar ,othercomponents varchar,observation varchar,rwaterheater varchar,gaspiping varchar" +
            ",rsupplypiping varchar,dwvpiping varchar,fixtures varchar,sumppump varchar" +
            ",wasteejectorpump varchar, water_heater_plumbing varchar, gas_piping_plumbing varchar, supply_plumbing varchar," +
            "  dwv_plumbing varchar,fixtures_observation varchar, sump_pump_plumbing varchar, waste_ejector_pump_ro varchar," +
            "watersupplysource_comments varchar,servicepipe_comments varchar," +
            " mainwatervalvelocation_comments varchar,interiorsupply_comments varchar,wastesystem_comments varchar," +
            " strudwvpiping_comments varchar ,waterheater_comments varchar ," +
            " fuelshortage_distribution_comments varchar ,fuelshutoffvalves_comments varchar ,othercomponents_comments varchar " +
            ",observation_comments   varchar ,rwaterheater_comments,gaspiping_comments varchar,rsupplypiping_comments varchar,dwvpiping_comments varchar" +
            ",fixtures_comments varchar,sumppump_comments varchar,wasteejectorpump_comments varchar" +
            ",addedby  INTEGER ,addedon  INTEGER );); ";
    static final String DATABASE_INTERIOR = "create table " + "interior" + "( "
            + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "template_id  INTEGER, inspection_id INTEGER , client_id INTEGER , is_applicable INTEGER" +
            ",empty_fields INTEGER ,wall_cieling varchar ,floors varchar ,windows varchar , doors varchar " +
            ",observation varchar ,rwalls_ceiling varchar ,rfloors varchar ,rwindows varchar " +
            ",rdoors varchar ,rcounters_cabinets varchar,rskylights varchar,rstairways varchar,rbasement varchar" +
            ",renvironmentalissues varchar , walls_ceilings_ro varchar ,floors_observer varchar ,windows_observe varchar " +
            ",doors_observation varchar ,Counters_Cabinets_observation varchar ,skylights_obs varchar,stairways_observation varchar ,basement_observation varchar , environmental_issues_ro varchar  " +
            ",wall_cieling_comments varchar,floors_comments varchar,windows_comments varchar" +
            ",doors_comments varchar,observation_comments varchar,rwalls_ceiling_comments varchar" +
            ", rfloors_comments varchar,rwindows_comments varchar,rdoors_comments varchar" +
            ",rcounters_cabinets_comments varchar ,rskylights_comments varchar " +
            ",rstairways_comments varchar ,rbasement_comments varchar ,renvironmentalissues_comments varchar " +
            ",addedby  INTEGER ,addedon  INTEGER );); ";


    static final String DATABASE_INSULATION = "create table " + "insulation" + "( "
            + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "template_id  INTEGER, inspection_id INTEGER , client_id INTEGER , is_applicable INTEGER" +
            ",empty_fields INTEGER ,atticinsulation varchar ,exteriorwallinsulation varchar ,basementwallinsulation varchar,csv varchar ,efv varchar " +
            ",crawlspaceinsulation varchar ,vaporretarders varchar ,roofventilation varchar " +
            ",observations varchar ,atticandroof varchar,basement varchar,crawlspace varchar,atticinsulation_comments varchar" +
            ",exteriorwallinsulation_comments varchar,basementwallinsulation_comments varchar" +
            ",crawlspaceinsulation_comments varchar,vaporretarders_comments varchar,roofventilation_comments varchar" +
            ",observations_comments varchar,atticandroof_comments varchar,basement_comments varchar" +
            ",crawlspace_comments varchar " +
            ",addedby  INTEGER ,addedon  INTEGER );); ";
    static final String DATABASE_HEATING = "create table " + "heating" + "( "
            + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "template_id  INTEGER, inspection_id INTEGER , client_id INTEGER , is_applicable INTEGER" +
            ",empty_fields INTEGER ,energy_source varchar ,heatingsystemtype varchar ,vent_flues_chimneys varchar , heatdistributionmethods varchar " +
            ",othercomponents varchar ,observation varchar ,rfurnace varchar ,rsupplyairductwork varchar " +
            ",boiler varchar ,combustion_exhaust varchar,furnace_chimneys varchar,thermostats varchar,energy_source_comments varchar" +
            ",heatingsystemtype_comments varchar,vent_flues_chimneys_comments varchar,heatdistributionmethods_comments varchar,othercomponents_comments varchar" +
            ",observation_comments varchar,rfurnace_comments varchar,rsupplyairductwork_comments varchar," +
            " boiler_comments varchar,combustion_exhaust_comments varchar,furnace_chimneys_comments varchar," +
            " thermostats_comments varchar " +
            ",addedby  INTEGER ,addedon  INTEGER );); ";
    static final String DATABASE_FIREPLACE = "create table " + "fireplaces" + "( "
            + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "template_id  INTEGER, inspection_id INTEGER , client_id INTEGER , is_applicable INTEGER" +
            ",empty_fields INTEGER ,fireplaceswoodstoves varchar ,woodcoalstoves varchar ,ventsflueschimney varchar , observations varchar " +
            ",recommendationsfireplace varchar ,recommendationswood varchar ,fireplace_ro varchar , wood_stove_ro varchar ,fireplaceswoodstoves_comments varchar ,woodcoalstoves_comments varchar " +
            ",ventsflueschimney_comments varchar ,observations_comments varchar,recommendationsfireplace_comments varchar,recommendationswood_comments varchar" +
            ",addedby  INTEGER ,addedon  INTEGER );); ";
    static final String DATABASE_EXTERIROR = "create table " + "exterior" + "( "
            + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "template_id  INTEGER, inspection_id INTEGER , client_id INTEGER , is_applicable INTEGER" +
            ",empty_fields INTEGER ,wallcovering varchar ,eaves_soffits_fascia varchar ,exteriordoors varchar , windows_doorframes_trim varchar " +
            ",entry_driveways varchar ,entrywalk_patios varchar ,porch_decks_steps_railings varchar ,overheadgaragedoors varchar " +
            ",surfacedrainage varchar ,retainingwalls varchar,fencing varchar,observations varchar,rexteriorwalls varchar" +
            ",reaves varchar,rexteriordoors_windows varchar,rgrage varchar" +
            ",rporches varchar,rdriveway varchar,rexteriorsteps_walkways varchar,rdeck varchar," +
            " rlotdrainage varchar,rlandscaping varchar,retainwall varchar," +
            " rfencing varchar , wallcovering_comments varchar  " +
            ", eaves_soffits_fascia_comments varchar ,exteriordoors_comments varchar ,entry_driveways_comments varchar ,entrywalk_patios_comments varchar ,porch_decks_steps_railings_comments varchar" +
            ", overheadgaragedoors_comments varchar ,surfacedrainage_comments varchar ,retainingwalls_comments varchar ,fencing_comments varchar ,observations_comments varchar" +
            ", rexteriorwalls_comments varchar ,reaves_comments varchar , rexteriordoors_windows_comments varchar , rgrage_comments varchar , rporches_comments varchar" +
            ", rdriveway_comments varchar ,rexteriorsteps_walkways_comments varchar , rdeck_comments varchar ,rlotdrainage_comments varchar , rlandscaping_comments varchar " +
            ", retainwall_comments varchar ,rfencing_comments varchar" +
            ",addedby  INTEGER ,addedon  INTEGER );); ";
    static final String DATABASE_ELECTRICAL = "create table " + "electrical" + "( "
            + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "template_id  INTEGER, inspection_id INTEGER , client_id INTEGER , is_applicable INTEGER" +
            ",empty_fields INTEGER ,sizeofservice varchar ,servicedrop varchar ,entranceconductors varchar , maindisconnect varchar " +
            ",grounding varchar ,servicepanel varchar ,sub_panel varchar,wiring varchar " +
            ",wiring_method varchar ,switches_receptacles varchar,gfci varchar,smoke_codetector varchar,observation varchar" +
            ",serviceentrance varchar, mainpanel varchar ,subpanel varchar,distribution varchar" +
            ",outlets varchar,switches varchar,lights_ceiling_fans varchar," +
            " smoke_co_detectors varchar,sizeofservice_comments varchar,servicedrop_comments varchar," +
            " entranceconductors_comments varchar , maindisconnect_comments varchar , grounding_comments varchar " +
            ", servicepanel_comments varchar ,sub_panel_comments varchar ,wiring_comments varchar ,wiring_method_comments varchar ,switches_receptacles_comments varchar" +
            ", gfci_comments varchar ,smoke_codetector_comments varchar ,observation_comments varchar ,serviceentrance_comments varchar ,mainpanel_comments varchar" +
            ", subpanel_comments varchar ,distribution_comments varchar , outlets_comments varchar , switches_comments varchar , lights_ceiling_fans_comments varchar" +
            ", smoke_co_detectors_comments varchar" +
            ",addedby  INTEGER ,addedon  INTEGER );); ";
    static final String DATABASE_COOLING = "create table " + "cooling" + "( "
            + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "template_id  INTEGER, inspection_id INTEGER , client_id INTEGER , is_applicable INTEGER" +
            ",empty_fields INTEGER ,energysource varchar ,centralsystemtype varchar ,throughwallequipment varchar , othercomponents varchar " +
            ",heatobservation varchar ,recomndcentralaircondition varchar ,heatpumps varchar ,evaporator varchar " +
            ",housefans varchar ,energysource_comments varchar,centralsystemtype_comments varchar,throughwallequipment_comments varchar" +
            ",othercomponents_comments varchar ,heatobservation_comments varchar,recomndcentralaircondition_comments varchar,heatpumps_comments varchar" +
            ",evaporator_comments varchar ,housefans_comments varchar" +
            ",addedby  INTEGER ,addedon  INTEGER );); ";
    static final String DATABASE_APPLIANCE = "create table " + "appliance" + "( "
            + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "template_id  INTEGER, inspection_id INTEGER , client_id INTEGER , is_applicable INTEGER" +
            ",empty_fields INTEGER ,appliancestested varchar ,laundryfacility varchar ,othercomponentstested varchar , observations varchar " +
            ",relectricrange varchar ,rgasrange varchar ,rbuiltinelectricoven  varchar ,relectriccooktop varchar " +
            ",rgascooktop varchar ,rmicrowaveoven varchar,rdishwasher varchar,rwastedisposer varchar" +
            ",rrefrigerator varchar ,rwinecooler varchar,rtrashcompactor varchar,rclotheswasher varchar" +
            ",rclothesdryer varchar ,rcooktopexhaustfan varchar,rcentralvacuum varchar,rdoorbell varchar" +
            ",appliancestested_comments varchar ,laundryfacility_comments varchar,othercomponentstested_comments varchar,observations_comments varchar" +
            ",relectricrange_comments varchar ,rgasrange_comments varchar,rbuiltinelectricoven_comments varchar,relectriccooktop_comments varchar" +
            ",rgascooktop_comments varchar ,rmicrowaveoven_comments varchar,rwastedisposer_comments varchar" +
            ",rrefrigerator_comments varchar ,rwinecooler_comments varchar,rtrashcompactor_comments  varchar,rclotheswasher_comments varchar" +
            ",rclothesdryer_comments varchar ,rcooktopexhaustfan_comments varchar,rcentralvacuum_comments  varchar,rdoorbell_comments varchar" +
            ",addedby  INTEGER ,addedon  INTEGER );); ";
    private static final String PORTFOLIO_TABLE = "portfolio";
    private static final String ROOFING_TABLE = "roofing";
    private static final String PLUMBING_TABLE = "plumbing";
    private static final String INTERIOR_TABLE = "interior";
    private static final String INSULATION_TABLE = "insulation";
    private static final String HEATING_TABLE = "heating";
    private static final String FIREPLACE_TABLE = "fireplaces";
    private static final String EXTERIROR_TABLE = "exterior";
    private static final String ELECTRICAL_TABLE = "electrical";
    private static final String COOLING_TABLE = "cooling";
    private static final String APPLIANCE_TABLE = "appliance";
    public static SQLiteDatabase db;
    private final Context context;
    private DataBaseHelper dbHelper;


    public Database(Context _context) {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    public Database open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }


    public void insertEntry(String columnName, String Value, String tableName, String tempID) {
        String selectQuery = "SELECT  * FROM " + tableName + " WHERE template_id = " + tempID;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor != null && cursor.getCount() > 0) {
            // update
            ContentValues newValues = new ContentValues();
            newValues.put(columnName, Value);
            int res = db.update(tableName, newValues, "template_id" + "=" + tempID,
                    null);
          //  Toast.makeText(context, res + "/ updated", Toast.LENGTH_SHORT).show();

        } else {
            //save

            ContentValues newValues = new ContentValues();
            newValues.put(columnName, Value);
            newValues.put("template_id", tempID);
            long res = db.insert(tableName, null, newValues);
          //  Toast.makeText(context, res + " /  saved", Toast.LENGTH_SHORT).show();
        }
    }
    public void clearDb()
    {
        clearTable(PORTFOLIO_TABLE);
        clearTable(ROOFING_TABLE);
        clearTable(PLUMBING_TABLE);
        clearTable(INTERIOR_TABLE);
        clearTable(INSULATION_TABLE);
        clearTable(HEATING_TABLE);
        clearTable(FIREPLACE_TABLE);
        clearTable(EXTERIROR_TABLE);
        clearTable(ELECTRICAL_TABLE);
        clearTable(COOLING_TABLE);
        clearTable(APPLIANCE_TABLE);
    }
    public void prePopulateData(String columnName, String Value[], String tableName, String tempID) {

        String selectQuery = "SELECT  * FROM " + tableName + " WHERE template_id = " + tempID;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String valueInsert = "";

        for (int i = 0; i < Value.length; i++) {
            valueInsert += Value[i] + "^";
        }

        valueInsert = valueInsert.substring(0, valueInsert.length() - 1);


        if (cursor != null && cursor.getCount() > 0) {
            // update
            ContentValues newValues = new ContentValues();
            newValues.put(columnName, valueInsert);
            int res = db.update(tableName, newValues, "template_id" + "=" + tempID,
                    null);
           // Toast.makeText(context, res + "/ updated", Toast.LENGTH_SHORT).show();

        } else {
            //save

            ContentValues newValues = new ContentValues();
            newValues.put(columnName, valueInsert);
            newValues.put("template_id", tempID);
            long res = db.insert(tableName, null, newValues);
          //  Toast.makeText(context, res + " /  saved", Toast.LENGTH_SHORT).show();
        }






    }

    public  void updateIds()
    {
        ContentValues newValues = new ContentValues();
        newValues.put("template_id", StructureScreensActivity.template_id);
        newValues.put("inspection_id", StructureScreensActivity.inspectionID);

        int res = db.update(PLUMBING_TABLE, newValues, "template_id" + "=" + 0,
                null);
        int res1 = db.update(PORTFOLIO_TABLE, newValues, "template_id" + "=" + 0,
                null);
        int res2 = db.update(INSULATION_TABLE, newValues, "template_id" + "=" + 0,
                null);;
        int res3 = db.update(INTERIOR_TABLE, newValues, "template_id" + "=" + 0,
                null);;
        int res4 = db.update(ELECTRICAL_TABLE, newValues, "template_id" + "=" + 0,
                null);
        int res5 = db.update(EXTERIROR_TABLE, newValues, "template_id" + "=" + 0,
                null);
        int res6 = db.update(ROOFING_TABLE, newValues, "template_id" + "=" + 0,
                null);
        int res7 = db.update(APPLIANCE_TABLE, newValues, "template_id" + "=" + 0,
                null);
        int res8 = db.update(FIREPLACE_TABLE, newValues, "template_id" + "=" + 0,
                null);
        int res9 =db.update(HEATING_TABLE, newValues, "template_id" + "=" + 0,
                null);
        int res10 = db.update(COOLING_TABLE, newValues, "template_id" + "=" + 0,
                null);

    }


    public Cursor getData(String columnName, String tablename, String inspectionId) {


        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT " + columnName + " FROM " + tablename + " WHERE template_id = " + inspectionId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }


    public Cursor getTable(String tablename, String inspectionId) {


        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT " + "*" + " FROM " + tablename + " WHERE template_id = " + inspectionId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }



    public void clearTable(String TABLE_NAME)
    {
        dbHelper.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME );


    }



  /*  public void insert(String Productname, Integer Quantity , Integer Trade_Price , Integer Retail_Price ) {
        ContentValues newValues = new ContentValues();
        newValues.put("Productname", Productname);
        newValues.put("Quantity", Quantity);
        newValues.put("Trade_Price", Trade_Price);
        newValues.put("Retail_Price", Retail_Price);
        long res = db.insert(PRODUCTS_TABLE, null, newValues);



    }

    public void update(int amount_remaining, int amount_paid, String businnes_name) {
        ContentValues newValues = new ContentValues();

        newValues.put("amount_remaining", amount_remaining);
        newValues.put("amount_paid", amount_paid);
        newValues.put("isupdated", 1);
        int res = db.update(RECOVRY_TABLE, newValues, "businnes_name" + "=" + businnes_name,
                null);
    }


    public void insertRecovry(String businnes_name, Integer total_bill , Integer amount_remaining , Integer amount_paid , Integer salesman_id)
    {

        String selectQuery = "SELECT  * FROM " +RECOVRY_TABLE+" WHERE businnes_name = "+businnes_name;

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor != null && cursor.getCount() > 0)
        {
            ContentValues newValues = new ContentValues();
            newValues.put("businnes_name", businnes_name);
            newValues.put("total_bill", total_bill);
            newValues.put("amount_remaining", amount_remaining);
            newValues.put("amount_paid", amount_paid);
            newValues.put("salesman_id", salesman_id);
            newValues.put("isupdated", 0);
            int res = db.update(RECOVRY_TABLE, newValues, "businnes_name" + "="+businnes_name,
                    null);
            Toast.makeText(context, "Updated in local data base", Toast.LENGTH_SHORT).show();


       }
        else
        {

            ContentValues newValues = new ContentValues();
            newValues.put("businnes_name", businnes_name);
            newValues.put("total_bill", total_bill);
            newValues.put("amount_remaining", amount_remaining);
            newValues.put("amount_paid", amount_paid);
            newValues.put("salesman_id", salesman_id);
            newValues.put("isupdated", 0);
            long res =  db.insert(RECOVRY_TABLE, null, newValues);
            Toast.makeText(context, "Enterd in local data base", Toast.LENGTH_SHORT).show();


        }

    }



    public void insertBussines(String bussines_name, String id, String personal_name, String address , String distrcit , String mobile, String salesman )
    {
        ContentValues newValues = new ContentValues();
        newValues.put("bussiness_name", bussines_name);
        newValues.put("CustomerID", id);
        newValues.put("personal_name", personal_name);
        newValues.put("address", address);
        newValues.put("distrcit", distrcit);
        newValues.put("mobile", mobile);
        newValues.put("salesman", salesman);
        long test = db.insert(CUSTOMERS_TABLE, null, newValues);
    }



    public void clearTable(String TABLE_NAME)
    {
            dbHelper.getWritableDatabase();
            db.execSQL("delete from "+ TABLE_NAME );


    }



 *//*   public ArrayList<Products_model> getAllProductss() {
        ArrayList<Products_model> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + PRODUCTS_TABLE;

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Products_model products_model = new Products_model();
                products_model.setId(cursor.getString(0));
                products_model.setName(cursor.getString(1));
                products_model.setQuantay(cursor.getString(2));
                products_model.setTrade(cursor.getString(3));
                products_model.setRetail(cursor.getString(4));
                // Adding contact to list
                contactList.add(products_model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }*//*

   *//* public ArrayList<Customer_model> getAllCustomers() {
        ArrayList<Customer_model> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CUSTOMERS_TABLE;

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer_model Customer_model = new Customer_model();
                Customer_model.setId(cursor.getString(0));
                Customer_model.setB_name(cursor.getString(1));
                Customer_model.setPeronal_name(cursor.getString(2));
                Customer_model.setDistrcit(cursor.getString(3));
                Customer_model.setAdress(cursor.getString(4));
                Customer_model.setMobile(cursor.getString(5));
                Customer_model.setMobile(cursor.getString(6));
                Customer_model.setSalesman(cursor.getString(7));
                // Adding contact to list
                contactList.add(Customer_model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }*//*

    public ArrayList<String> getAllSales() {
        ArrayList<String> List = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ACCOUNTS_TABLE;

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String sales = "";
        // looping through all rows and adding to list
        if(cursor.equals(""))
        {


        }
        else
        {
            if (cursor.moveToFirst()) {
                do {

                    List.add(cursor.getString(1));

                } while (cursor.moveToNext());
            }
        }


        // return contact list
        return List;
    }

    public Integer insertSales(String salesString) {


                ContentValues newValues = new ContentValues();
                newValues.put("saleString", salesString);
                long result = db.insert("Sales", null, newValues);




        return 1;
    }


    public Cursor showRecovry() {


        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + RECOVRY_TABLE +" WHERE isupdated = 1" ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getData(String tablename) {


        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + tablename;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }


    public void updatestatus(String bussid)
    {
        ContentValues newValues = new ContentValues();


        newValues.put("isupdated", 0);
        int res = db.update(RECOVRY_TABLE, newValues, "businnes_name" + "="+bussid,
                null);
    }

    public Cursor getRecords(String tablename, String bussinesid) {


        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + tablename +" WHERE businnes_name = " + bussinesid;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Integer getqty(String product_name)
    {
        int entry = 0;
        Cursor mCursor = db.rawQuery("SELECT Quantity FROM " + "Products" + " WHERE  Productname=?", new String[]{product_name});
        if (mCursor.getCount() > 0) {
            mCursor.moveToFirst();

            entry = mCursor.getInt(mCursor.getColumnIndex("Quantity"));

        }

        return  entry;
    }*/


}



