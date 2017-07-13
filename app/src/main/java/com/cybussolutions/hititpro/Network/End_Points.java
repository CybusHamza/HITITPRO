package com.cybussolutions.hititpro.Network;


public class End_Points {

    public static final String BASE_URL = "http://xfer.cybusservices.com/hititpro/Api_Controller/";
    public static final String IMAGE_BASE_URL = "http://xfer.cybusservices.com/hititpro/uploads/inspection/";
    public static final String PDF_BASRURL = "http://xfer.cybusservices.com/hititpro/uploads/reports/";
    public static final String UPLOAD="http://xfer.cybusservices.com/hititpro/upload_image_mobile.php";


    public static final String LOGIN = BASE_URL + "login";
    public static final String SIGN_UP = BASE_URL + "addUserSignUp";
    public static final String ADD_CLIENT = BASE_URL + "addClient";
    public static final String UPDATE_CLIENT = BASE_URL + "updateClient";
    public static final String UPDATE_PROFILE = BASE_URL + "updateProfile";
    public static final String DELETE_CLIENT = BASE_URL + "deleteClient";


    public static final String DELETE_PICTURE = BASE_URL + "deletePicture";

    public static final String GET_CLIENT = BASE_URL + "getClient";
    public static final String START_INSPECTION = BASE_URL + "addTemplate";
    public static final String PRE_POPULATE = BASE_URL + "insertSimpleEnteriesToAllReferenceTables";
    public static final String GET_TEMPLATES = BASE_URL + "getTemplates";
    public static final String GET_TEMPLATES_REVIEW = BASE_URL + "getTemplatesreview";
    public static final String GET_INSPECTION = BASE_URL + "getInspection";
    public static final String GET_TEMPLATE_DATA = BASE_URL + "getTemplatesData";
    public static final String GET_ARCHIVE_TEMPLATE_DATA = BASE_URL + "getArchiveAllTemplates";
    public static final String DELETE_TEMPLATE = BASE_URL + "deleteTemplate";
    public static final String CEHCK_PASS = BASE_URL + "checkpas";
    public static final String UPDATE_PASS = BASE_URL + "updatePass";
    public static final String ARCHHIVE_TEMPLATE = BASE_URL + "archiveTempate";
    public static final String UPLOAD_IMAGE=BASE_URL+"uploadimagetest";
    public static final String GENRATE_PDF=BASE_URL+"generateReport";
    public static final String GET_REVIEW_INSPECTION = BASE_URL + "reviewInspection";

    public static final String GET_IMAGES=BASE_URL+"getImages";
    public static final String GET_DEFAULT_COMMENTS=BASE_URL+"getDefaultComments";
    public static final String GET_DEFAULT_COMMENTS_IMAGES=BASE_URL+"getDefaultCommentsImages";
    public static final String SAVE_OBSERVATION_COMMENTS=BASE_URL+"saveObservationComments";
    public static final String GET_IMAGE_COMMENTS=BASE_URL+"getImageComments";

    public static final String GET_ALL_TEMPLATES = BASE_URL + "getAllTemplates";
    public static final String UPDATELIVE = BASE_URL + "updateOnBackPress";

    // sync Forms .
    public static final String SYNC_STRUCTURE = BASE_URL + "syncStructure";
    public static final String SYNC_ROOf = BASE_URL + "syncRoof";
    public static final String SYNC_EXTERIOR = BASE_URL + "syncExterior";
    public static final String SYNC_INTERIOR = BASE_URL + "syncInterior";
    public static final String SYNC_HEATING = BASE_URL + "syncHeating";
    public static final String SYNC_COOLING = BASE_URL + "syncColling";
    public static final String SYNC_ELECTRICAL = BASE_URL + "syncElectrical";
    public static final String SYNC_INSULATION = BASE_URL + "syncInsulation";
    public static final String SAVE_NOTEMP = BASE_URL + "saveNoTemplate";
    public static final String SYNC_PLUMBING = BASE_URL + "syncPlumbing";
    public static final String SYNC_APPLIANCES = BASE_URL + "syncAppliances";
    public static final String SYNC_FIREPLACE = BASE_URL + "syncFirePlace";

}
