package pwc.appdev.speedalert;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;
import com.intentfilter.androidpermissions.PermissionManager;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singleton;

public class Services extends Service {

    private static final String TAG = Services.class.getSimpleName();
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0;
    private LocationManager mLocationManager = null;
    Location locations;
    Location mCurrentLocation, lStart, lEnd;
    static double distance = 0;
    double speed, oldspeed = 0.00;
    private FirebaseAuth auth;
    private String email;
    public static String oras, newspeed, newdistance, newaverage;
    public static String stringaverage = "0.000";
    Double lat1, lat2, lng1, lng2;
    FirebaseDatabase fd, fd1;
    DatabaseReference dr, dr1;
    private long sTime, min;
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    public List<LatLng> stou = new ArrayList<>();
    public List<LatLng> ctou = new ArrayList<>();
    public List<LatLng> ltop = new ArrayList<>();
    public List<LatLng> cptomca = new ArrayList<>();
    public List<LatLng> utob = new ArrayList<>();
    public List<LatLng> utogb = new ArrayList<>();
    public List<LatLng> ptojpl = new ArrayList<>();
    public List<LatLng> mtomca = new ArrayList<>();
    public List<LatLng> etoma = new ArrayList<>();
    public List<LatLng> jpltob = new ArrayList<>();
    public List<LatLng> cpltoca = new ArrayList<>();
    public List<LatLng> jplaurel = new ArrayList<>();
    public List<LatLng> rcastillo = new ArrayList<>();
    public List<LatLng> dacudao = new ArrayList<>();
    public List<LatLng> lapulapu = new ArrayList<>();
    public List<LatLng> staana = new ArrayList<>();
    public List<LatLng> rmagsaysay = new ArrayList<>();
    public List<LatLng> sanped = new ArrayList<>();
    public List<LatLng> rizal = new ArrayList<>();
    public List<LatLng> bonifacio = new ArrayList<>();
    public List<LatLng> jpalmagil = new ArrayList<>();
    public List<LatLng> apichon = new ArrayList<>();
    public List<LatLng> bacacacir = new ArrayList<>();
    public List<LatLng> ejtor = new ArrayList<>();
    public List<LatLng> mabini = new ArrayList<>();
    public List<LatLng> tionko = new ArrayList<>();
    public List<LatLng> roxas = new ArrayList<>();
    public List<LatLng> claveria = new ArrayList<>();
    public List<LatLng> cbtocsp = new ArrayList<>();
    public List<LatLng> cbangoy = new ArrayList<>();
    public List<LatLng> bmtoo = new ArrayList<>();
    public List<LatLng> jpltoa = new ArrayList<>();
    public List<LatLng> qtogb = new ArrayList<>();
    public List<LatLng> tulip = new ArrayList<>();
    public List<LatLng> sandawa = new ArrayList<>();
    public List<LatLng> ftorres = new ArrayList<>();
    public List<LatLng> bacaca = new ArrayList<>();
    public List<LatLng> medical = new ArrayList<>();

    public Services() {
    }

    public Services(Context applicationContext) {

        super();
    }

    @Override
    public void onCreate() {

        PermissionManager permissionManager = PermissionManager.getInstance(getApplicationContext());
        permissionManager.checkPermissions(singleton(android.Manifest.permission.ACCESS_FINE_LOCATION), new PermissionManager.PermissionRequestListener(){
            @Override
            public void onPermissionGranted() {

                initializeLocationManager();
                boolean gps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if(gps){

                    try {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                                mLocationListeners[0]);
                    } catch (java.lang.SecurityException ex) {
                        Log.i(TAG, "Failed to request Location Update, ignore", ex);
                    } catch (IllegalArgumentException ex) {
                        Log.d(TAG, "GPS Provider does not exist " + ex.getMessage());
                    }
                }
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(getApplicationContext(), "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        });

        //Sirawan to Ulas Crossing (60 kph zone - Tolerance 50 M)

        stou.add(new LatLng(7.054685431337229, 125.54555624645215));
        stou.add(new LatLng(7.051534034455042, 125.54220119800928));
        stou.add(new LatLng(7.051200787271015, 125.54182837491763));
        stou.add(new LatLng(7.050999405205198, 125.54158610466372));
        stou.add(new LatLng(7.018730432651451, 125.49417701128505));
        stou.add(new LatLng(7.018478550586605, 125.49382952271327));
        stou.add(new LatLng(7.018174354367723, 125.49349765160083));
        stou.add(new LatLng(7.008210265172403, 125.48682726357397));
        stou.add(new LatLng(7.007587955318017, 125.4865611438439));
        stou.add(new LatLng(7.0071465969655184, 125.48641652017665));
        stou.add(new LatLng(7.0066157582745365, 125.4863109888562));
        stou.add(new LatLng(7.006055989098699, 125.48624921559349));
        stou.add(new LatLng(7.003736086370722, 125.48624424256757));
        stou.add(new LatLng(7.0033336405715065, 125.48609814161931));
        stou.add(new LatLng(7.002999626576, 125.48573042686692));
        stou.add(new LatLng(7.0028986076241635, 125.4844576888358));
        stou.add(new LatLng(7.002717751124172, 125.48400297014186));
        stou.add(new LatLng(7.002284346995179, 125.48364510489779));
        stou.add(new LatLng(7.000385500950497, 125.48234613254421));
        stou.add(new LatLng(6.99989599097396, 125.48212802255856));
        stou.add(new LatLng(6.999337135491537, 125.48195868070364));
        stou.add(new LatLng(6.999091982794085, 125.48190227308177));
        stou.add(new LatLng(6.9987932747857595, 125.48184705945182));
        stou.add(new LatLng(6.998306095472202, 125.48180295986955));
        stou.add(new LatLng(6.992589656943842, 125.4819582528338));
        stou.add(new LatLng(6.991530483667641, 125.48181847575269));

        //Calinan to Ulas Crossing (60 kph zone - Tolerance 50 M)

        ctou.add(new LatLng(7.054708746918049, 125.54555330234999));
        ctou.add(new LatLng(7.054976268079095, 125.54519053358399));
        ctou.add(new LatLng(7.055046142986069, 125.54504167098318));
        ctou.add(new LatLng(7.055599643898813, 125.54382054578281));
        ctou.add(new LatLng(7.055628259298851, 125.54367905925659));
        ctou.add(new LatLng(7.055628924773254, 125.54354293715097));
        ctou.add(new LatLng(7.055608960540586, 125.5432908095037));
        ctou.add(new LatLng(7.055582341561299, 125.54319559107853));
        ctou.add(new LatLng(7.055529769075499, 125.5430520928964));
        ctou.add(new LatLng(7.055089009647935, 125.54219301737015));
        ctou.add(new LatLng(7.054965231249348, 125.54187182284275));
        ctou.add(new LatLng(7.054955249119166, 125.54170418477659));
        ctou.add(new LatLng(7.055020465699701, 125.54150301910329));
        ctou.add(new LatLng(7.055145575035935, 125.5413514742935));
        ctou.add(new LatLng(7.055254712938958, 125.54128710127621));
        ctou.add(new LatLng(7.057851524412904, 125.53996404973535));
        ctou.add(new LatLng(7.058030626894493, 125.53978927021953));
        ctou.add(new LatLng(7.074086627454464, 125.51875325364406));
        ctou.add(new LatLng(7.074370383672672, 125.5183824344113));
        ctou.add(new LatLng(7.074776066081046, 125.51799597822856));
        ctou.add(new LatLng(7.092766103141711, 125.50212522586179));
        ctou.add(new LatLng(7.093129977462044, 125.50158533953359));
        ctou.add(new LatLng(7.093197632454709, 125.50137896660269));
        ctou.add(new LatLng(7.093209337302858, 125.50116646680448));
        ctou.add(new LatLng(7.0931706054313635, 125.49906992921484));
        ctou.add(new LatLng(7.093177065344416, 125.4989207947992));
        ctou.add(new LatLng(7.093267040520492, 125.49864977300957));
        ctou.add(new LatLng(7.0933980912889725, 125.49844970965212));
        ctou.add(new LatLng(7.093644311091629, 125.49822745733584));
        ctou.add(new LatLng(7.101588960581671, 125.49234116717163));
        ctou.add(new LatLng(7.11408430910201, 125.47983649714031));
        ctou.add(new LatLng(7.114428643499574, 125.47962999666932));
        ctou.add(new LatLng(7.114906063770528, 125.47947246022903));
        ctou.add(new LatLng(7.1154463935891235, 125.47942368890541));
        ctou.add(new LatLng(7.134776819333245, 125.48103579323396));
        ctou.add(new LatLng(7.1351297819584545, 125.48100837783468));
        ctou.add(new LatLng(7.135320655158294, 125.48098259341376));
        ctou.add(new LatLng(7.171979908842852, 125.47189039336973));
        ctou.add(new LatLng(7.17248729052629, 125.47165734232905));
        ctou.add(new LatLng(7.173115048544221, 125.4713124794615));
        ctou.add(new LatLng(7.173459197016127, 125.47107546418671));
        ctou.add(new LatLng(7.173875947292008, 125.47073649935471));
        ctou.add(new LatLng(7.205268257207183, 125.44210563804583));

        // Lasang to Panacan (60 kph zone - Tolerance 50 M)

        ltop.add(new LatLng(7.1487995122374635, 125.66040418921368));
        ltop.add(new LatLng(7.151462147149029, 125.6591260598877));
        ltop.add(new LatLng(7.154866640909979, 125.65755943439207));
        ltop.add(new LatLng(7.155011017658267, 125.6575104840775));
        ltop.add(new LatLng(7.155351666762796, 125.65743672332975));
        ltop.add(new LatLng(7.155603826779241, 125.65740788958242));
        ltop.add(new LatLng(7.1604646423632605, 125.65729830745613));
        ltop.add(new LatLng(7.161500290212615, 125.65724138520564));
        ltop.add(new LatLng(7.162802295508108, 125.65721467463099));
        ltop.add(new LatLng(7.164838503653755, 125.65717331151194));
        ltop.add(new LatLng(7.165314624498166, 125.65710466093701));
        ltop.add(new LatLng(7.174138990291514, 125.6544406537966));
        ltop.add(new LatLng(7.1750701476077445, 125.65420097301941));
        ltop.add(new LatLng(7.176926078569668, 125.65359020489265));
        ltop.add(new LatLng(7.177409114969871, 125.65340508752259));
        ltop.add(new LatLng(7.179133519394432, 125.65278222907602));
        ltop.add(new LatLng(7.179313149089753, 125.65268432844834));
        ltop.add(new LatLng(7.1793448431214735, 125.65267104534364));
        ltop.add(new LatLng(7.1801644537882074, 125.65241623461169));
        ltop.add(new LatLng(7.180654134092874, 125.65219461859027));
        ltop.add(new LatLng(7.192033819675718, 125.64818161974364));
        ltop.add(new LatLng(7.192923095002816, 125.6480153449819));
        ltop.add(new LatLng(7.193553737129592, 125.64794328495773));
        ltop.add(new LatLng(7.193791905226956, 125.6479110984498));
        ltop.add(new LatLng(7.194060230737712, 125.64788514642866));
        ltop.add(new LatLng(7.194885332175656, 125.64777876872627));
        ltop.add(new LatLng(7.19537192026182, 125.64772877820307));
        ltop.add(new LatLng(7.198742639163771, 125.64732039465227));
        ltop.add(new LatLng(7.199961604951802, 125.6471298917816));
        ltop.add(new LatLng(7.215335481312207, 125.64410981240161));
        ltop.add(new LatLng(7.215640827391614, 125.64402733447618));
        ltop.add(new LatLng(7.21614334971121, 125.64388842027529));
        ltop.add(new LatLng(7.226774255716406, 125.64012240912838));

        // C.P Garcia Highway to McArthur Highways (60 kph zone - Tolerance 50 M)

        cptomca.add(new LatLng(7.059844557987862, 125.55108380800277));
        cptomca.add(new LatLng(7.061410087961607, 125.55276121037991));
        cptomca.add(new LatLng(7.062679863804431, 125.5544276851047));
        cptomca.add(new LatLng(7.062997584445764, 125.55478642265975));
        cptomca.add(new LatLng(7.063305641211792, 125.55506242525122));
        cptomca.add(new LatLng(7.064082213832917, 125.55574118248063));
        cptomca.add(new LatLng(7.064254491541248, 125.55591276628877));
        cptomca.add(new LatLng(7.064376936532105, 125.5560522411583));
        cptomca.add(new LatLng(7.064557155243901, 125.55630845711582));
        cptomca.add(new LatLng(7.0646682873116315, 125.5565109638993));
        cptomca.add(new LatLng(7.0648100305672346, 125.55684959278439));
        cptomca.add(new LatLng(7.064858313072064, 125.55703148246751));
        cptomca.add(new LatLng(7.0649155427254495, 125.55732451380209));
        cptomca.add(new LatLng(7.0649907398172775, 125.55781133473121));
        cptomca.add(new LatLng(7.065046638532634, 125.55846512318307));
        cptomca.add(new LatLng(7.065075253348466, 125.55888287723472));
        cptomca.add(new LatLng(7.065192576503946, 125.56043564277178));
        cptomca.add(new LatLng(7.065379148227019, 125.56227580323801));
        cptomca.add(new LatLng(7.065454681385414, 125.56311720491848));
        cptomca.add(new LatLng(7.06549327808079, 125.56347729148725));
        cptomca.add(new LatLng(7.065583894805012, 125.56488039651676));
        cptomca.add(new LatLng(7.065753211575605, 125.56651361339547));
        cptomca.add(new LatLng(7.065752546116571, 125.56695550732674));
        cptomca.add(new LatLng(7.065721269501673, 125.56717477792266));
        cptomca.add(new LatLng(7.064871347960066, 125.57014738563574));
        cptomca.add(new LatLng(7.064836078520382, 125.57036665622165));
        cptomca.add(new LatLng(7.064822103836117, 125.57053027096485));
        cptomca.add(new LatLng(7.064925915767468, 125.57108548822913));
        cptomca.add(new LatLng(7.06497626698548, 125.5712114316547));
        cptomca.add(new LatLng(7.065173243385131, 125.57153597893887));
        cptomca.add(new LatLng(7.065346263127861, 125.57174921455749));
        cptomca.add(new LatLng(7.065738594695723, 125.57212100838441));
        cptomca.add(new LatLng(7.0669418415095, 125.57330859991721));
        cptomca.add(new LatLng(7.067263313557741, 125.57358574622302));
        cptomca.add(new LatLng(7.067397735983717, 125.5736823057436));
        cptomca.add(new LatLng(7.06770379285691, 125.57383202164685));
        cptomca.add(new LatLng(7.0689191539795635, 125.57429585728094));
        cptomca.add(new LatLng(7.069737048895083, 125.57459848511121));
        cptomca.add(new LatLng(7.070139606598934, 125.57466032941079));
        cptomca.add(new LatLng(7.070853019726049, 125.57472511969573));
        cptomca.add(new LatLng(7.071195783190176, 125.57471305339106));
        cptomca.add(new LatLng(7.07132782486912, 125.57469081766911));
        cptomca.add(new LatLng(7.0716632124554835, 125.5745982814595));
        cptomca.add(new LatLng(7.0718031625909195, 125.57453073525397));
        cptomca.add(new LatLng(7.072004181583303, 125.5744081070422));
        cptomca.add(new LatLng(7.0721740743259005, 125.57427672545586));
        cptomca.add(new LatLng(7.072634524610652, 125.57380867801464));
        cptomca.add(new LatLng(7.07282777201572, 125.57366639522995));
        cptomca.add(new LatLng(7.073255157924541, 125.57350240106638));
        cptomca.add(new LatLng(7.073506109473612, 125.57348419698224));
        cptomca.add(new LatLng(7.073761224375923, 125.57351470557008));
        cptomca.add(new LatLng(7.074831767385454, 125.5736283511467));
        cptomca.add(new LatLng(7.075341436142115, 125.57355281934177));
        cptomca.add(new LatLng(7.075736202919582, 125.5734404583393));
        cptomca.add(new LatLng(7.077696059714028, 125.57266130999336));
        cptomca.add(new LatLng(7.078143103908343, 125.57252897751091));
        cptomca.add(new LatLng(7.078627531808368, 125.57246018912338));
        cptomca.add(new LatLng(7.079390304081049, 125.57246185323874));
        cptomca.add(new LatLng(7.079697508595517, 125.57244230551537));
        cptomca.add(new LatLng(7.0815904119589606, 125.57213201172458));
        cptomca.add(new LatLng(7.081896365206538, 125.5720996129649));
        cptomca.add(new LatLng(7.082570664320715, 125.57208668473795));
        cptomca.add(new LatLng(7.083208279340286, 125.57209154267976));
        cptomca.add(new LatLng(7.083740911760499, 125.57203248668013));
        cptomca.add(new LatLng(7.085242811876168, 125.57198164873337));
        cptomca.add(new LatLng(7.0856684366007965, 125.5720320116044));
        cptomca.add(new LatLng(7.086336970757063, 125.57220216744668));
        cptomca.add(new LatLng(7.086952727607925, 125.57244616760258));
        cptomca.add(new LatLng(7.087552895046706, 125.5726032730486));
        cptomca.add(new LatLng(7.087888628738536, 125.572599889858));
        cptomca.add(new LatLng(7.088170644853156, 125.5724814782136));
        cptomca.add(new LatLng(7.088422189952737, 125.57220228633041));
        cptomca.add(new LatLng(7.088477420404051, 125.57204671820202));
        cptomca.add(new LatLng(7.0886644053722305, 125.57128698250361));
        cptomca.add(new LatLng(7.088732944328723, 125.57110928615451));
        cptomca.add(new LatLng(7.089000445784312, 125.57065532228279));
        cptomca.add(new LatLng(7.089165471483936, 125.57043739279578));
        cptomca.add(new LatLng(7.0893477981933435, 125.57022080441905));
        cptomca.add(new LatLng(7.089744996808022, 125.56989891697522));
        cptomca.add(new LatLng(7.09004200371969, 125.5697281798527));
        cptomca.add(new LatLng(7.090363366537009, 125.56958527191783));
        cptomca.add(new LatLng(7.0906475787731615, 125.56950507010968));
        cptomca.add(new LatLng(7.090801397670722, 125.56947632380742));
        cptomca.add(new LatLng(7.0910367059563155, 125.56944990678691));
        cptomca.add(new LatLng(7.091345368122365, 125.56944003706319));
        cptomca.add(new LatLng(7.093265254332789, 125.56960107592371));
        cptomca.add(new LatLng(7.093456895293809, 125.56965539065403));
        cptomca.add(new LatLng(7.093626577330282, 125.56973049250564));
        cptomca.add(new LatLng(7.093802248079029, 125.56984113362616));
        cptomca.add(new LatLng(7.093931339422169, 125.56996786800389));
        cptomca.add(new LatLng(7.094027159774032, 125.5700858851957));
        cptomca.add(new LatLng(7.09411156425909, 125.57023912146944));
        cptomca.add(new LatLng(7.094321171172062, 125.57074874116651));
        cptomca.add(new LatLng(7.09441965312115, 125.57088151052355));
        cptomca.add(new LatLng(7.09451949887547, 125.5710016008773));
        cptomca.add(new LatLng(7.094730436440565, 125.57117594446312));
        cptomca.add(new LatLng(7.095035863219311, 125.57133419480085));
        cptomca.add(new LatLng(7.0956281491021205, 125.57157574880283));
        cptomca.add(new LatLng(7.095788514509733, 125.57167029667094));
        cptomca.add(new LatLng(7.095983481504234, 125.57182184147858));
        cptomca.add(new LatLng(7.096109245154305, 125.5719666807681));
        cptomca.add(new LatLng(7.098066150257942, 125.57483000481075));
        cptomca.add(new LatLng(7.104717781149056, 125.58456589394272));
        cptomca.add(new LatLng(7.104940691297995, 125.5849259805056));
        cptomca.add(new LatLng(7.10507377193398, 125.58523309344285));
        cptomca.add(new LatLng(7.105140977639951, 125.58548320944162));
        cptomca.add(new LatLng(7.105863748817091, 125.58900642948049));
        cptomca.add(new LatLng(7.105886372481776, 125.58928604975884));
        cptomca.add(new LatLng(7.105898401786984, 125.58959926764456));
        cptomca.add(new LatLng(7.105735632707943, 125.59242092498751));
        cptomca.add(new LatLng(7.105579029797729, 125.59519125861743));
        cptomca.add(new LatLng(7.105457320229915, 125.59743578649342));
        cptomca.add(new LatLng(7.10523094289379, 125.60113264908506));
        cptomca.add(new LatLng(7.10522937024676, 125.60205919791476));
        cptomca.add(new LatLng(7.105261974985493, 125.60245348264301));
        cptomca.add(new LatLng(7.105317203414382, 125.60285447288788));
        cptomca.add(new LatLng(7.105506861389212, 125.60379504846665));
        cptomca.add(new LatLng(7.105650524684698, 125.6042805786442));
        cptomca.add(new LatLng(7.106280983735845, 125.60614644065987));
        cptomca.add(new LatLng(7.108686955987868, 125.61330205616238));
        cptomca.add(new LatLng(7.109434389457131, 125.61552723144224));
        cptomca.add(new LatLng(7.1095388042590715, 125.61578798556229));
        cptomca.add(new LatLng(7.109743786512629, 125.6163038550639));
        cptomca.add(new LatLng(7.10995442660949, 125.61671086706986));
        cptomca.add(new LatLng(7.111608437817589, 125.61903689174429));
        cptomca.add(new LatLng(7.114687866199678, 125.62337732321151));
        cptomca.add(new LatLng(7.1148162862849205, 125.62355904286484));
        cptomca.add(new LatLng(7.11539492423467, 125.62420373897041));
        cptomca.add(new LatLng(7.117076151563728, 125.62573134987879));
        cptomca.add(new LatLng(7.11950877575411, 125.62794650222679));
        cptomca.add(new LatLng(7.121930425200659, 125.63015519584651));
        cptomca.add(new LatLng(7.123509792514659, 125.63157335978374));
        cptomca.add(new LatLng(7.12472005432913, 125.63268830647819));
        cptomca.add(new LatLng(7.125334490305531, 125.63334614158602));
        cptomca.add(new LatLng(7.126930078767327, 125.63538877221953));
        cptomca.add(new LatLng(7.127681593446472, 125.63636439513593));
        cptomca.add(new LatLng(7.130100503431327, 125.63952899502115));
        cptomca.add(new LatLng(7.131993365149095, 125.64203067537744));
        cptomca.add(new LatLng(7.134218561327916, 125.64445292437755));
        cptomca.add(new LatLng(7.134666114815242, 125.64491469154034));
        cptomca.add(new LatLng(7.135374303605029, 125.64566758798924));
        cptomca.add(new LatLng(7.136099806983663, 125.6464485196283));
        cptomca.add(new LatLng(7.136434002893336, 125.64678204023488));
        cptomca.add(new LatLng(7.137313658452068, 125.64771676653622));
        cptomca.add(new LatLng(7.137915160874504, 125.64835711621842));
        cptomca.add(new LatLng(7.143602053430946, 125.65431829880688));
        cptomca.add(new LatLng(7.144284392247625, 125.65504873899695));
        cptomca.add(new LatLng(7.146424443501324, 125.65732061061054));
        cptomca.add(new LatLng(7.148340260467418, 125.65934311660502));
        cptomca.add(new LatLng(7.1488806298614636, 125.65991410489562));

        // Ulas to Bolton Bridge (40 kph zone - Tolerance 30 M)

        utob.add(new LatLng(7.054685431337229, 125.54555624645215));
        utob.add(new LatLng(7.054804643520338, 125.54569273918102));
        utob.add(new LatLng(7.056890250640595, 125.54788849243727));
        utob.add(new LatLng(7.058818657685022, 125.55005572347793));
        utob.add(new LatLng(7.059584239122321, 125.55078797621765));
        utob.add(new LatLng(7.05981480543187, 125.55104959255212));
        utob.add(new LatLng(7.060003132945639, 125.55178384726707));
        utob.add(new LatLng(7.0600550394568105, 125.5518804067929));
        utob.add(new LatLng(7.060201632284118, 125.55275718188761));
        utob.add(new LatLng(7.060290804963603, 125.55314140832823));
        utob.add(new LatLng(7.060960606958162, 125.55717123303737));
        utob.add(new LatLng(7.061683839742802, 125.56163171556766));
        utob.add(new LatLng(7.0616452014411655, 125.56196929469778));
        utob.add(new LatLng(7.061613924548632, 125.56208060637306));
        utob.add(new LatLng(7.061576892069501, 125.56218822233744));
        utob.add(new LatLng(7.055707264858493, 125.57580822488016));
        utob.add(new LatLng(7.055598127063991, 125.57614484210877));
        utob.add(new LatLng(7.0556094401294285, 125.57633058508387));
        utob.add(new LatLng(7.0556410740634234, 125.57652060976564));
        utob.add(new LatLng(7.0556736823081065, 125.57668020120252));
        utob.add(new LatLng(7.05573024762232, 125.5768069355767));
        utob.add(new LatLng(7.0558433782326, 125.57699066689456));
        utob.add(new LatLng(7.056001761038425, 125.57716970434585));
        utob.add(new LatLng(7.055940248984515, 125.57738503231177));
        utob.add(new LatLng(7.0558803563239145, 125.57759893848107));
        utob.add(new LatLng(7.055811855850174, 125.57774575074313));
        utob.add(new LatLng(7.050407920438289, 125.58271755829232));
        utob.add(new LatLng(7.050265785446998, 125.58286388826743));
        utob.add(new LatLng(7.050127827979412, 125.58305508647469));
        utob.add(new LatLng(7.04984184746582, 125.58372553613653));
        utob.add(new LatLng(7.049773440412272, 125.58408330913039));
        utob.add(new LatLng(7.049798728759283, 125.5846919453207));
        utob.add(new LatLng(7.049929303786921, 125.58519144991357));
        utob.add(new LatLng(7.049997490953355, 125.58537817076432));
        utob.add(new LatLng(7.050346300670646, 125.5864792988387));
        utob.add(new LatLng(7.050413514347063, 125.58664693690213));
        utob.add(new LatLng(7.05069767502907, 125.58744020021804));
        utob.add(new LatLng(7.050931822734561, 125.58818714353478));
        utob.add(new LatLng(7.051060260579511, 125.58848218652449));
        utob.add(new LatLng(7.05119668415762, 125.5887437019036));
        utob.add(new LatLng(7.051473811433267, 125.58931794731019));
        utob.add(new LatLng(7.051687833281407, 125.58984830676171));
        utob.add(new LatLng(7.051790075048722, 125.59006960671016));
        utob.add(new LatLng(7.052129711796805, 125.59137495504379));
        utob.add(new LatLng(7.0538344112278475, 125.59871453695982));
        utob.add(new LatLng(7.0540254030821465, 125.598809084828));
        utob.add(new LatLng(7.054157832927826, 125.59893045478465));
        utob.add(new LatLng(7.05431022702475, 125.59904243701233));
        utob.add(new LatLng(7.056775356435656, 125.60042737407144));
        utob.add(new LatLng(7.057054854906532, 125.600725769822));
        utob.add(new LatLng(7.057228748152501, 125.60100252671114));
        utob.add(new LatLng(7.057364504442057, 125.60123923165604));
        utob.add(new LatLng(7.057500926162098, 125.6016073648415));
        utob.add(new LatLng(7.057729549659219, 125.60325380152825));

        // Ulas to Generoso Bridge (40 kph zone - Tolerance 30 M)

        utogb.add(new LatLng(7.056001761038425, 125.57716970434585));
        utogb.add(new LatLng(7.057149724696437, 125.57867885871745));
        utogb.add(new LatLng(7.057769528156741, 125.5794481584458));
        utogb.add(new LatLng(7.05859369637712, 125.58050606276414));
        utogb.add(new LatLng(7.058893998987664, 125.58090141868155));
        utogb.add(new LatLng(7.059200114951948, 125.58138756906206));
        utogb.add(new LatLng(7.0593751333356485, 125.58179593538186));
        utogb.add(new LatLng(7.0594829393039324, 125.58217077409259));
        utogb.add(new LatLng(7.059526860247909, 125.58250739133248));
        utogb.add(new LatLng(7.059616033058191, 125.5835728988516));
        utogb.add(new LatLng(7.060641467853941, 125.58794013010474));
        utogb.add(new LatLng(7.061317941460608, 125.5909638909791));
        utogb.add(new LatLng(7.062755247094109, 125.59699524647121));
        utogb.add(new LatLng(7.063610115847232, 125.6005297601068));
        utogb.add(new LatLng(7.063668676579596, 125.60069873926781));
        utogb.add(new LatLng(7.063825725782316, 125.60095421967617));
        utogb.add(new LatLng(7.064014717125656, 125.60117483136732));
        utogb.add(new LatLng(7.064213623695385, 125.60135276695338));
        utogb.add(new LatLng(7.064699133523221, 125.60159233493862));
        utogb.add(new LatLng(7.065148804398314, 125.60168931218045));

        // Panacan to J.P Laurel Ave (40 kph zone - Tolerance 30 M)

        ptojpl.add(new LatLng(7.103806431689361, 125.64338946958817));
        ptojpl.add(new LatLng(7.10401830863805, 125.64398875087755));
        ptojpl.add(new LatLng(7.105161295625933, 125.64497178715267));
        ptojpl.add(new LatLng(7.107227038059706, 125.6466142481574));
        ptojpl.add(new LatLng(7.109425501659546, 125.64843710796258));
        ptojpl.add(new LatLng(7.109749513334799, 125.6486577596027));
        ptojpl.add(new LatLng(7.109886003962332, 125.64878741151885));
        ptojpl.add(new LatLng(7.112676899283591, 125.65106008816552));
        ptojpl.add(new LatLng(7.114320269053042, 125.65239237086358));
        ptojpl.add(new LatLng(7.114926239354973, 125.65294767962267));
        ptojpl.add(new LatLng(7.116406721235941, 125.6540990345468));
        ptojpl.add(new LatLng(7.11762343244369, 125.65510678813382));
        ptojpl.add(new LatLng(7.118931644390619, 125.65610805417494));
        ptojpl.add(new LatLng(7.119744088762839, 125.65684376658754));
        ptojpl.add(new LatLng(7.121320525390757, 125.65804432532575));
        ptojpl.add(new LatLng(7.125237150324857, 125.6612609730394));
        ptojpl.add(new LatLng(7.125874436625565, 125.66159552315018));
        ptojpl.add(new LatLng(7.126139572889323, 125.66170780556435));
        ptojpl.add(new LatLng(7.126431909132573, 125.66176146435072));
        ptojpl.add(new LatLng(7.126692201960566, 125.66177979984961));
        ptojpl.add(new LatLng(7.127358483329341, 125.66179247273976));
        ptojpl.add(new LatLng(7.128537038219664, 125.6617428108147));
        ptojpl.add(new LatLng(7.131738767216916, 125.66167297744892));
        ptojpl.add(new LatLng(7.134229563920749, 125.66159981792858));
        ptojpl.add(new LatLng(7.134579758570577, 125.66158441886923));
        ptojpl.add(new LatLng(7.134852929738881, 125.66155294205853));
        ptojpl.add(new LatLng(7.136844030283018, 125.66151336923875));
        ptojpl.add(new LatLng(7.137355263593381, 125.66151938094268));
        ptojpl.add(new LatLng(7.138796630058466, 125.66147288761503));
        ptojpl.add(new LatLng(7.139311189714145, 125.66146197580466));
        ptojpl.add(new LatLng(7.139660921767638, 125.66146048095554));
        ptojpl.add(new LatLng(7.140596733815977, 125.66143548681521));
        ptojpl.add(new LatLng(7.141399025663971, 125.66139648449843));
        ptojpl.add(new LatLng(7.141628822810999, 125.66137861021043));
        ptojpl.add(new LatLng(7.143216262875854, 125.66133607611553));
        ptojpl.add(new LatLng(7.144968368702953, 125.66129988185412));
        ptojpl.add(new LatLng(7.145331929047935, 125.66129303162577));
        ptojpl.add(new LatLng(7.14601307772399, 125.66124170823542));
        ptojpl.add(new LatLng(7.146506846649384, 125.66117239565283));
        ptojpl.add(new LatLng(7.147624460661396, 125.66089861844247));
        ptojpl.add(new LatLng(7.1487995122374635, 125.66040418921368));

        // Maa Diversion Road to McArthur Highway (40 kph zone - Tolerance 30 M)

        mtomca.add(new LatLng(7.1028452832045215, 125.58186927155816));
        mtomca.add(new LatLng(7.100990426925689, 125.58164109186269));
        mtomca.add(new LatLng(7.100185827534512, 125.58151541008259));
        mtomca.add(new LatLng(7.098605951746419, 125.58117829666914));
        mtomca.add(new LatLng(7.0969356821718526, 125.58079607703537));
        mtomca.add(new LatLng(7.094633961052843, 125.58027228634555));
        mtomca.add(new LatLng(7.0926734357925305, 125.57984285355529));
        mtomca.add(new LatLng(7.092007318209243, 125.57971279780658));
        mtomca.add(new LatLng(7.091826323399002, 125.57968999903085));
        mtomca.add(new LatLng(7.091635887715273, 125.57968610835296));
        mtomca.add(new LatLng(7.091412624847374, 125.57971607786645));
        mtomca.add(new LatLng(7.090173143307436, 125.58000130706439));
        mtomca.add(new LatLng(7.087693431378, 125.58056212842757));
        mtomca.add(new LatLng(7.087494554582728, 125.5806082067566));
        mtomca.add(new LatLng(7.0862492476049805, 125.58087311661943));
        mtomca.add(new LatLng(7.085931390912024, 125.58094403361589));
        mtomca.add(new LatLng(7.084900400378482, 125.58113244994841));
        mtomca.add(new LatLng(7.084490983606482, 125.58124297058414));
        mtomca.add(new LatLng(7.084227806875045, 125.58132833069132));
        mtomca.add(new LatLng(7.081793990420602, 125.58232452071428));
        mtomca.add(new LatLng(7.080770222589627, 125.5827420527294));
        mtomca.add(new LatLng(7.08068105386436, 125.58278697972854));
        mtomca.add(new LatLng(7.080491480372264, 125.58284899659067));
        mtomca.add(new LatLng(7.07963764644356, 125.58309703117612));
        mtomca.add(new LatLng(7.079539826780013, 125.58313860541418));
        mtomca.add(new LatLng(7.0787968100597745, 125.58331204839442));
        mtomca.add(new LatLng(7.078209007182122, 125.58343262529506));
        mtomca.add(new LatLng(7.077970894778826, 125.58348197431883));
        mtomca.add(new LatLng(7.077693230721487, 125.58358143223595));
        mtomca.add(new LatLng(7.077588090773447, 125.58362032426972));
        mtomca.add(new LatLng(7.077453671312715, 125.58370146109289));
        mtomca.add(new LatLng(7.075986099424252, 125.58493538413502));
        mtomca.add(new LatLng(7.075776869131776, 125.58511000710483));
        mtomca.add(new LatLng(7.072958372504793, 125.58673912634579));
        mtomca.add(new LatLng(7.071778191267491, 125.58745035743055));
        mtomca.add(new LatLng(7.068753196400854, 125.59029427092219));
        mtomca.add(new LatLng(7.0683538340154115, 125.590701218367));
        mtomca.add(new LatLng(7.06769348312929, 125.59129597448072));
        mtomca.add(new LatLng(7.0673823948222365, 125.59152228184423));
        mtomca.add(new LatLng(7.067196472950567, 125.59161796548106));
        mtomca.add(new LatLng(7.067013472033805, 125.59168233849479));
        mtomca.add(new LatLng(7.0651433112155315, 125.59213281382283));
        mtomca.add(new LatLng(7.063907176642228, 125.59240921918791));
        mtomca.add(new LatLng(7.061850241737059, 125.59290987821615));

        // Ecoland Drive to Matina Aplaya (40 kph zone - Tolerance 30 M)

        etoma.add(new LatLng(7.058704306170241, 125.56873346236974));
        etoma.add(new LatLng(7.055942533506551, 125.5669696575789));
        etoma.add(new LatLng(7.054440509091026, 125.56600429344324));
        etoma.add(new LatLng(7.054372382264331, 125.56594428638772));
        etoma.add(new LatLng(7.054084230980071, 125.56591612319376));
        etoma.add(new LatLng(7.053508665629064, 125.56601545586888));
        etoma.add(new LatLng(7.052428187283084, 125.56631830299054));
        etoma.add(new LatLng(7.052265144898622, 125.56637731158818));
        etoma.add(new LatLng(7.051977262393166, 125.56647754541578));
        etoma.add(new LatLng(7.051823536569755, 125.56655868223639));
        etoma.add(new LatLng(7.0516387826581175, 125.56667225155473));
        etoma.add(new LatLng(7.051525651021778, 125.56670577916724));
        etoma.add(new LatLng(7.051405199072434, 125.56672522518258));
        etoma.add(new LatLng(7.050187719920876, 125.56684808803278));
        etoma.add(new LatLng(7.049318044863779, 125.56694506733398));
        etoma.add(new LatLng(7.048931411881555, 125.56697560942881));
        etoma.add(new LatLng(7.048079433325672, 125.56709331867604));
        etoma.add(new LatLng(7.047858731004882, 125.5671335558105));
        etoma.add(new LatLng(7.047454352378453, 125.5672406417648));
        etoma.add(new LatLng(7.046736879008575, 125.56742564721348));
        etoma.add(new LatLng(7.046611101914696, 125.56747392697345));
        etoma.add(new LatLng(7.04545988396107, 125.56791989379751));
        etoma.add(new LatLng(7.045084625885398, 125.56807370555529));
        etoma.add(new LatLng(7.044316853444853, 125.56842275111246));
        etoma.add(new LatLng(7.043453562817023, 125.56874445835275));
        etoma.add(new LatLng(7.043296087660806, 125.56878721423223));
        etoma.add(new LatLng(7.042972931622305, 125.56893897714957));
        etoma.add(new LatLng(7.042782276503038, 125.56911042319807));
        etoma.add(new LatLng(7.04264461805782, 125.5693042586367));
        etoma.add(new LatLng(7.0419905693058285, 125.57073552247923));
        etoma.add(new LatLng(7.0415475700844965, 125.57175700481157));
        etoma.add(new LatLng(7.041504550964575, 125.57199328412253));
        etoma.add(new LatLng(7.041500557995963, 125.57211063076956));
        etoma.add(new LatLng(7.041452642375774, 125.57238622773573));
        etoma.add(new LatLng(7.041366236982609, 125.57285341343636));
        etoma.add(new LatLng(7.040903678523242, 125.57528296049179));
        etoma.add(new LatLng(7.040894474638163, 125.57552167092001));
        etoma.add(new LatLng(7.040913356582841, 125.57621269852793));
        etoma.add(new LatLng(7.040966979193767, 125.57690113835716));
        etoma.add(new LatLng(7.041070632295812, 125.57744302391973));
        etoma.add(new LatLng(7.0416616093239215, 125.57900876928665));
        etoma.add(new LatLng(7.042391118990386, 125.58103749727202));
        etoma.add(new LatLng(7.0425118007636085, 125.58130182027317));
        etoma.add(new LatLng(7.0434963282935605, 125.58397229407144));
        etoma.add(new LatLng(7.044304708841268, 125.58607684981287));
        etoma.add(new LatLng(7.044456683536918, 125.58649482667609));
        etoma.add(new LatLng(7.0447564849803115, 125.58738069282371));
        etoma.add(new LatLng(7.04481783696656, 125.58761346037966));
        etoma.add(new LatLng(7.0448757345876105, 125.58774019475327));
        etoma.add(new LatLng(7.044989734640018, 125.58793212052217));
        etoma.add(new LatLng(7.045051625175469, 125.58804007942798));
        etoma.add(new LatLng(7.045322186626814, 125.5884342644251));
        etoma.add(new LatLng(7.045676662218387, 125.58909249275814));
        etoma.add(new LatLng(7.04571178239441, 125.58925085720008));
        etoma.add(new LatLng(7.045870300216054, 125.58987635076933));
        etoma.add(new LatLng(7.045977980900725, 125.59034437418076));
        etoma.add(new LatLng(7.046171842970594, 125.5912789611021));
        etoma.add(new LatLng(7.046319982254335, 125.59188851449157));
        etoma.add(new LatLng(7.046403373351887, 125.5922268729614));
        etoma.add(new LatLng(7.046721703428845, 125.59361944642313));
        etoma.add(new LatLng(7.046873625038251, 125.59424711167001));
        etoma.add(new LatLng(7.046958221061834, 125.59445106118281));
        etoma.add(new LatLng(7.047082667088428, 125.59465356796622));
        etoma.add(new LatLng(7.047156536104891, 125.59476286798439));
        etoma.add(new LatLng(7.04734021036135, 125.59493855266952));
        etoma.add(new LatLng(7.047469314689372, 125.59504651158511));
        etoma.add(new LatLng(7.047941356837264, 125.59533752606326));
        etoma.add(new LatLng(7.048377249600823, 125.59557758377052));
        etoma.add(new LatLng(7.049611056818964, 125.59629775688438));
        etoma.add(new LatLng(7.049840593449442, 125.596439538171));
        etoma.add(new LatLng(7.050087311894728, 125.59658828847478));
        etoma.add(new LatLng(7.0505287052632, 125.59684958036758));
        etoma.add(new LatLng(7.0510229766072685, 125.59714332734491));
        etoma.add(new LatLng(7.052147230825878, 125.59780462725624));
        etoma.add(new LatLng(7.052348205559215, 125.59792331499739));
        etoma.add(new LatLng(7.05303009568152, 125.59830776329653));
        etoma.add(new LatLng(7.053300447087304, 125.59846405231835));
        etoma.add(new LatLng(7.053809483490739, 125.59870957109902));

        // J.P Laurel Ave. to Bolton Bridge (30 kph zone - Tolerance 20 M)

        jpltob.add(new LatLng(7.103806431689361, 125.64338946958817));
        jpltob.add(new LatLng(7.1037251309272404, 125.64314059338983));
        jpltob.add(new LatLng(7.103593997796643, 125.64232390550009));
        jpltob.add(new LatLng(7.103205109481223, 125.6399810202652));
        jpltob.add(new LatLng(7.103060944321485, 125.6391971640364));
        jpltob.add(new LatLng(7.102615522931026, 125.63751806673488));
        jpltob.add(new LatLng(7.1024249314493515, 125.63680479861627));
        jpltob.add(new LatLng(7.102354070493164, 125.6365355788804));
        jpltob.add(new LatLng(7.101443826711538, 125.63354191236483));
        jpltob.add(new LatLng(7.100787342935355, 125.63132495356056));
        jpltob.add(new LatLng(7.100762908037804, 125.63121742983179));
        jpltob.add(new LatLng(7.100514142847935, 125.63026501017099));
        jpltob.add(new LatLng(7.099560709454127, 125.62701825869225));
        jpltob.add(new LatLng(7.099364415213521, 125.62641497667676));
        jpltob.add(new LatLng(7.099025583463168, 125.62519199680769));
        jpltob.add(new LatLng(7.09811171389363, 125.6220852996384));
        jpltob.add(new LatLng(7.098022118738637, 125.6218152591115));
        jpltob.add(new LatLng(7.097721435887259, 125.62072462559047));
        jpltob.add(new LatLng(7.097703516843093, 125.6206474711591));
        jpltob.add(new LatLng(7.0972082993399725, 125.62033721183914));
        jpltob.add(new LatLng(7.094061303618848, 125.62145323133021));
        jpltob.add(new LatLng(7.089964218618825, 125.62291217438073));
        jpltob.add(new LatLng(7.087114049376475, 125.62386883188123));
        jpltob.add(new LatLng(7.086057514436627, 125.62415676680433));
        jpltob.add(new LatLng(7.085709357196147, 125.62424599242294));
        jpltob.add(new LatLng(7.0822591157687365, 125.62458098780412));
        jpltob.add(new LatLng(7.080742945251716, 125.6247115741378));
        jpltob.add(new LatLng(7.0802905977828035, 125.62474990054233));
        jpltob.add(new LatLng(7.07628076404898, 125.62511271056239));
        jpltob.add(new LatLng(7.0752541567474845, 125.62517231423548));
        jpltob.add(new LatLng(7.075107549398261, 125.6251779755631));
        jpltob.add(new LatLng(7.074842374162886, 125.62514902999605));
        jpltob.add(new LatLng(7.074053363487441, 125.62469386738118));
        jpltob.add(new LatLng(7.073916610880022, 125.62456936823496));
        jpltob.add(new LatLng(7.072699172607103, 125.62308396113508));
        jpltob.add(new LatLng(7.071875024087627, 125.62206585961762));
        jpltob.add(new LatLng(7.071576244526938, 125.6216511739022));
        jpltob.add(new LatLng(7.071444417130461, 125.62149266625886));
        jpltob.add(new LatLng(7.071094352399281, 125.62107433241275));
        jpltob.add(new LatLng(7.07097279472663, 125.62092395720938));
        jpltob.add(new LatLng(7.070184408484281, 125.6203122838951));
        jpltob.add(new LatLng(7.069868401499857, 125.62012176562644));
        jpltob.add(new LatLng(7.069362213088998, 125.61987902371875));
        jpltob.add(new LatLng(7.06919246750237, 125.61974054626904));
        jpltob.add(new LatLng(7.0690100294113245, 125.61952883706967));
        jpltob.add(new LatLng(7.066902753133194, 125.61696123215816));
        jpltob.add(new LatLng(7.066345434230222, 125.61628706172031));
        jpltob.add(new LatLng(7.066164632757986, 125.61609615936239));
        jpltob.add(new LatLng(7.065620624200625, 125.6154371163992));
        jpltob.add(new LatLng(7.064698167170509, 125.61429760128449));
        jpltob.add(new LatLng(7.064266926836869, 125.6138019738368));
        jpltob.add(new LatLng(7.06411187687492, 125.61360040852419));
        jpltob.add(new LatLng(7.064065539933519, 125.61351310155752));
        jpltob.add(new LatLng(7.063897478637751, 125.61300726920314));
        jpltob.add(new LatLng(7.063613895545262, 125.61231829374456));
        jpltob.add(new LatLng(7.062928079807654, 125.61117096681514));
        jpltob.add(new LatLng(7.062614321881929, 125.61080142938742));
        jpltob.add(new LatLng(7.062263713015474, 125.61045172756486));
        jpltob.add(new LatLng(7.061662849782737, 125.61001805365073));
        jpltob.add(new LatLng(7.061131666319616, 125.60965377135497));
        jpltob.add(new LatLng(7.060868006997758, 125.60944407096713));
        jpltob.add(new LatLng(7.059450643975386, 125.6081562732219));
        jpltob.add(new LatLng(7.059022081798848, 125.60770901486548));
        jpltob.add(new LatLng(7.058457097829918, 125.6069191043122));
        jpltob.add(new LatLng(7.0581543087276994, 125.60602056429497));
        jpltob.add(new LatLng(7.058065893270845, 125.60551921625985));
        jpltob.add(new LatLng(7.057998580378563, 125.60513024404412));
        jpltob.add(new LatLng(7.0578252107034585, 125.60398088380353));
        jpltob.add(new LatLng(7.057729549659219, 125.60325380152825));

        // C.P Garcia Highway to Corner Angliongto St. (30 kph zone - Tolerance 15 M)

        cpltoca.add(new LatLng(7.1183171530795635, 125.62688675259376));
        cpltoca.add(new LatLng(7.118062224521078, 125.62695968008113));
        cpltoca.add(new LatLng(7.117772782279647, 125.62704081690282));
        cpltoca.add(new LatLng(7.117728948360023, 125.62705619203794));
        cpltoca.add(new LatLng(7.1176377906417585, 125.62708703744121));
        cpltoca.add(new LatLng(7.117400611880683, 125.62716951807698));
        cpltoca.add(new LatLng(7.11726496656194, 125.62721086664072));
        cpltoca.add(new LatLng(7.116982451028597, 125.62729995866175));
        cpltoca.add(new LatLng(7.116879981599912, 125.6273401917989));
        cpltoca.add(new LatLng(7.116744908228194, 125.62739383598063));
        cpltoca.add(new LatLng(7.11671097353206, 125.62741127034096));
        cpltoca.add(new LatLng(7.116644249326585, 125.62744918462396));
        cpltoca.add(new LatLng(7.116582368395759, 125.62751489874572));
        cpltoca.add(new LatLng(7.1165031874078535, 125.62761615213434));
        cpltoca.add(new LatLng(7.116469252693604, 125.62768723067383));
        cpltoca.add(new LatLng(7.116433321817168, 125.62775897976756));
        cpltoca.add(new LatLng(7.116386457433417, 125.6279276465028));
        cpltoca.add(new LatLng(7.116361800162258, 125.62809628477747));
        cpltoca.add(new LatLng(7.116347932138771, 125.62829688302742));
        cpltoca.add(new LatLng(7.116350326541681, 125.62852034319285));
        cpltoca.add(new LatLng(7.11636696120988, 125.62871279169397));
        cpltoca.add(new LatLng(7.116419044352164, 125.62927992212767));
        cpltoca.add(new LatLng(7.116468282961081, 125.62981099951679));
        cpltoca.add(new LatLng(7.116526836975718, 125.63033268918372));
        cpltoca.add(new LatLng(7.116520183110965, 125.6304540591337));
        cpltoca.add(new LatLng(7.116480259920186, 125.63054860700423));
        cpltoca.add(new LatLng(7.11642783012697, 125.63063357384056));
        cpltoca.add(new LatLng(7.116322033641695, 125.63073750944045));
        cpltoca.add(new LatLng(7.115615639720477, 125.63140460416051));
        cpltoca.add(new LatLng(7.1155158315395495, 125.63144953116142));
        cpltoca.add(new LatLng(7.1154592735596465, 125.6314683066263));
        cpltoca.add(new LatLng(7.115306234288135, 125.63150317534158));
        cpltoca.add(new LatLng(7.115200437542438, 125.63153133853888));
        cpltoca.add(new LatLng(7.115018121084312, 125.63156620725495));
        cpltoca.add(new LatLng(7.114762166982558, 125.63161412023855));
        cpltoca.add(new LatLng(7.114585173462138, 125.63165904724008));
        cpltoca.add(new LatLng(7.11437757195525, 125.63173481964233));
        cpltoca.add(new LatLng(7.113637992032556, 125.63197657299827));
        cpltoca.add(new LatLng(7.113047630584878, 125.63219217813285));
        cpltoca.add(new LatLng(7.112916548435111, 125.63223174071607));
        cpltoca.add(new LatLng(7.112704001981188, 125.63228238062055));
        cpltoca.add(new LatLng(7.112617278652029, 125.63229867689819));
        cpltoca.add(new LatLng(7.11253304555747, 125.63230836443985));
        cpltoca.add(new LatLng(7.112431240530578, 125.63230970554427));
        cpltoca.add(new LatLng(7.112203624609642, 125.63230257005644));
        cpltoca.add(new LatLng(7.1119926950588574, 125.63227843017363));
        cpltoca.add(new LatLng(7.111499813209261, 125.63221798930326));
        cpltoca.add(new LatLng(7.111389357829296, 125.63221128378177));
        cpltoca.add(new LatLng(7.111240347234571, 125.632216072185));
        cpltoca.add(new LatLng(7.111137211130864, 125.63223082433464));
        cpltoca.add(new LatLng(7.110933281877284, 125.63227886382168));
        cpltoca.add(new LatLng(7.110736979562561, 125.63229128815233));
        cpltoca.add(new LatLng(7.110583356760674, 125.63229366991108));
        cpltoca.add(new LatLng(7.110050838104106, 125.63230204513397));
        cpltoca.add(new LatLng(7.10980009094711, 125.63231210142307));
        cpltoca.add(new LatLng(7.109213028723394, 125.63234064281322));
        cpltoca.add(new LatLng(7.10859710726758, 125.63238272626133));
        cpltoca.add(new LatLng(7.1081066891404205, 125.63241021434571));
        cpltoca.add(new LatLng(7.1080108717191095, 125.63242094318306));
        cpltoca.add(new LatLng(7.106498889135916, 125.63252519988785));
        cpltoca.add(new LatLng(7.105381111664386, 125.63260123892096));
        cpltoca.add(new LatLng(7.10428015222129, 125.63268455785418));
        cpltoca.add(new LatLng(7.103797304876107, 125.63271385480893));
        cpltoca.add(new LatLng(7.103534139150204, 125.63273406993272));
        cpltoca.add(new LatLng(7.10252663053665, 125.63279867750225));
        cpltoca.add(new LatLng(7.102233175263044, 125.6328301463979));
        cpltoca.add(new LatLng(7.102042868769185, 125.63284154578777));
        cpltoca.add(new LatLng(7.101736791851235, 125.63286410749235));
        cpltoca.add(new LatLng(7.101582417192747, 125.6328875768224));
        cpltoca.add(new LatLng(7.101287505739902, 125.63293266531356));

        // J.P Laurel Avenue (30 kph zone - Tolerance 20 M)

        jplaurel.add(new LatLng(7.097698368194061, 125.62058574833321));
        jplaurel.add(new LatLng(7.097569390258433, 125.6201723556964));
        jplaurel.add(new LatLng(7.0974409653511, 125.61975460164292));
        jplaurel.add(new LatLng(7.097309213336862, 125.61931874268122));
        jplaurel.add(new LatLng(7.0970875145399575, 125.61860178363054));
        jplaurel.add(new LatLng(7.096995021896762, 125.61831411671288));
        jplaurel.add(new LatLng(7.096810297049544, 125.61786120990485));
        jplaurel.add(new LatLng(7.0965720784081645, 125.617437420882));
        jplaurel.add(new LatLng(7.096306985368679, 125.61703769658894));
        jplaurel.add(new LatLng(7.096163920958548, 125.61683787201561));
        jplaurel.add(new LatLng(7.096136638902908, 125.61673058365477));
        jplaurel.add(new LatLng(7.095859825762874, 125.6163148412568));
        jplaurel.add(new LatLng(7.095697464129948, 125.61604796145643));
        jplaurel.add(new LatLng(7.095334304088191, 125.61549721955508));
        jplaurel.add(new LatLng(7.095197228131933, 125.61526319681646));
        jplaurel.add(new LatLng(7.095070806404857, 125.61505158694148));
        jplaurel.add(new LatLng(7.094915016576159, 125.61485472548948));

        // R. Castillo St. (30 kph zone - Tolerance 20 M)

        rcastillo.add(new LatLng(7.08359780861116, 125.62483090650365));
        rcastillo.add(new LatLng(7.0886847894994744, 125.630935797681));
        rcastillo.add(new LatLng(7.0900129794254925, 125.63250354885267));
        rcastillo.add(new LatLng(7.090209279712639, 125.63269867955692));
        rcastillo.add(new LatLng(7.101331441680142, 125.64181449958767));
        rcastillo.add(new LatLng(7.101406632834867, 125.64187015542443));
        rcastillo.add(new LatLng(7.103972468801073, 125.64396577824714));

        // Dacudao Avenue (30 kph zone - Tolerance 35 M)

        dacudao.add(new LatLng(7.09437915570126, 125.61572032679206));
        dacudao.add(new LatLng(7.093896698439289, 125.6160459251538));
        dacudao.add(new LatLng(7.093079905630189, 125.61662905548111));
        dacudao.add(new LatLng(7.092508176050977, 125.61702018121098));
        dacudao.add(new LatLng(7.091913828357629, 125.61744835426401));
        dacudao.add(new LatLng(7.09110017435231, 125.61802121406745));
        dacudao.add(new LatLng(7.090452994973924, 125.61848759258181));
        dacudao.add(new LatLng(7.089938291160394, 125.61891012941592));
        dacudao.add(new LatLng(7.089415091860494, 125.6193379725957));
        dacudao.add(new LatLng(7.088779860614241, 125.61985444227044));
        dacudao.add(new LatLng(7.088149772266959, 125.62038749425935));
        dacudao.add(new LatLng(7.087588880913684, 125.62084862271077));
        dacudao.add(new LatLng(7.08707925799187, 125.62127723035735));
        dacudao.add(new LatLng(7.08602046283754, 125.62215456790629));
        dacudao.add(new LatLng(7.0852778225479325, 125.622772991008));
        dacudao.add(new LatLng(7.0843699065366605, 125.6235276712914));
        dacudao.add(new LatLng(7.083628929484256, 125.62411620072713));
        dacudao.add(new LatLng(7.083507971121437, 125.62421929350751));
        dacudao.add(new LatLng(7.083444300586828, 125.62429311273088));

        // Lapu-lapu St. (30 kph zone - Tolerance 20 M)

        lapulapu.add(new LatLng(7.0830070580122735, 125.6241473971058));
        lapulapu.add(new LatLng(7.082661031700021, 125.62372159643586));
        lapulapu.add(new LatLng(7.082208070722848, 125.62317829552853));
        lapulapu.add(new LatLng(7.082101722565491, 125.62303962567172));
        lapulapu.add(new LatLng(7.081986454968164, 125.62290771608538));
        lapulapu.add(new LatLng(7.081172473378615, 125.62191291766489));
        lapulapu.add(new LatLng(7.081044043900838, 125.62176070230224));
        lapulapu.add(new LatLng(7.078251185213709, 125.61837350443018));
        lapulapu.add(new LatLng(7.077464782291459, 125.6174416163069));
        lapulapu.add(new LatLng(7.077400899763708, 125.61741814697956));
        lapulapu.add(new LatLng(7.077333024567666, 125.61740607703946));
        lapulapu.add(new LatLng(7.077098262241612, 125.61740926001609));
        lapulapu.add(new LatLng(7.076869716566366, 125.61741515080107));

        // Sta. Ana Avenue (30 kph zone - Tolerance 15 M)

        staana.add(new LatLng(7.07727703562159, 125.6249574542578));
        staana.add(new LatLng(7.077074225510169, 125.62175341168124));
        staana.add(new LatLng(7.07692185710237, 125.6193677765916));
        staana.add(new LatLng(7.076898858093454, 125.61852186833896));
        staana.add(new LatLng(7.076722329681955, 125.61523483818537));
        staana.add(new LatLng(7.076618834088802, 125.61357778504443));

        // R. Magsaysay Ave. (30 kph zone - Tolerance 15 M)

        rmagsaysay.add(new LatLng(7.074330615296918, 125.62463924543543));
        rmagsaysay.add(new LatLng(7.0742315623926935, 125.62302323739625));
        rmagsaysay.add(new LatLng(7.0741539283686095, 125.62186138620662));
        rmagsaysay.add(new LatLng(7.074118578124556, 125.62083252419102));
        rmagsaysay.add(new LatLng(7.073984813388015, 125.61837026882196));
        rmagsaysay.add(new LatLng(7.073879419065839, 125.61645680857136));
        rmagsaysay.add(new LatLng(7.073666395882418, 125.61283204583764));

        // San Pedro St. (30 kph zone - Tolerance 15 M)

        sanped.add(new LatLng(7.062983385678126, 125.61113423444135));
        sanped.add(new LatLng(7.063161065084021, 125.61100554948153));
        sanped.add(new LatLng(7.064046796248442, 125.60986359899998));
        sanped.add(new LatLng(7.064113342473105, 125.60977173334241));
        sanped.add(new LatLng(7.064384850971884, 125.60939689463393));
        sanped.add(new LatLng(7.064768822276759, 125.60881351417419));
        sanped.add(new LatLng(7.065059628755507, 125.60850506013665));
        sanped.add(new LatLng(7.065198710049494, 125.60829249507422));
        sanped.add(new LatLng(7.065671186861519, 125.60769570356725));
        sanped.add(new LatLng(7.066187583500814, 125.60700570529936));
        sanped.add(new LatLng(7.067182443954819, 125.60577255970664));
        sanped.add(new LatLng(7.06823253514181, 125.60444486623967));
        sanped.add(new LatLng(7.068285106184452, 125.60437512880307));
        sanped.add(new LatLng(7.068847936197379, 125.60359651337099));

        // Rizal St. (30 kph zone - Tolerance 15 M)

        rizal.add(new LatLng(7.0639528982861925, 125.61299831393451));
        rizal.add(new LatLng(7.063990487385942, 125.61297406696626));
        rizal.add(new LatLng(7.064522856979542, 125.61254893683966));
        rizal.add(new LatLng(7.065478458863803, 125.611729521987));
        rizal.add(new LatLng(7.06550108451075, 125.61170269989863));
        rizal.add(new LatLng(7.065874407535452, 125.61036830091746));
        rizal.add(new LatLng(7.065915000573846, 125.610246260407));
        rizal.add(new LatLng(7.066224439188482, 125.60941410506138));
        rizal.add(new LatLng(7.066288323263929, 125.60930882835754));
        rizal.add(new LatLng(7.066481306353912, 125.60898562217324));
        rizal.add(new LatLng(7.066525892091282, 125.60888235711589));
        rizal.add(new LatLng(7.06671954053992, 125.60844314539446));
        rizal.add(new LatLng(7.0671048408204475, 125.60768609190305));
        rizal.add(new LatLng(7.067159408364716, 125.60757008636192));
        rizal.add(new LatLng(7.06778803125269, 125.60623898363471));

        // Bonifacio St. (30 kph zone - Tolerance 15 M)

        bonifacio.add(new LatLng(7.065986859131302, 125.61579946972809));
        bonifacio.add(new LatLng(7.066075726256082, 125.61550086899454));
        bonifacio.add(new LatLng(7.066195222964324, 125.61516355800693));
        bonifacio.add(new LatLng(7.066255366990993, 125.61502560339409));
        bonifacio.add(new LatLng(7.066334503855841, 125.61475128902389));
        bonifacio.add(new LatLng(7.066702490101155, 125.61371623070337));
        bonifacio.add(new LatLng(7.0669177421333185, 125.61311975642671));
        bonifacio.add(new LatLng(7.067000044354681, 125.61285182145399));
        bonifacio.add(new LatLng(7.067177310627793, 125.61226332142415));
        bonifacio.add(new LatLng(7.067656878864354, 125.61083592975994));
        bonifacio.add(new LatLng(7.068100835154207, 125.60948588836621));
        bonifacio.add(new LatLng(7.068176806240811, 125.60928653199423));
        bonifacio.add(new LatLng(7.0684217012625465, 125.60863957712846));
        bonifacio.add(new LatLng(7.068934933773262, 125.60721167963789));

        // J. Palma Gil St. (30 kph zone - Tolerance 15 M)

        jpalmagil.add(new LatLng(7.068518805160456, 125.61270438862674));
        jpalmagil.add(new LatLng(7.068716607405163, 125.61199897589582));
        jpalmagil.add(new LatLng(7.068936207691121, 125.61121778253431));
        jpalmagil.add(new LatLng(7.06900874230915, 125.61097236039768));
        jpalmagil.add(new LatLng(7.069356398802987, 125.60983049596987));
        jpalmagil.add(new LatLng(7.069590397808411, 125.60911685241255));
        jpalmagil.add(new LatLng(7.069662266868784, 125.60894183827682));
        jpalmagil.add(new LatLng(7.069834994576037, 125.60850068859926));
        jpalmagil.add(new LatLng(7.070161033299891, 125.60762457509051));

        // A. Pichon St. (30 kph zone - Tolerance 15 M)

        apichon.add(new LatLng(7.068218710900135, 125.60284796275748));
        apichon.add(new LatLng(7.067880052004124, 125.60318866537615));
        apichon.add(new LatLng(7.067535125692536, 125.60354202131522));
        apichon.add(new LatLng(7.067425753563084, 125.60367881737035));
        apichon.add(new LatLng(7.067140185230602, 125.60401466251743));
        apichon.add(new LatLng(7.0666565492766935, 125.60457848551174));
        apichon.add(new LatLng(7.066393777558465, 125.60483641408156));
        apichon.add(new LatLng(7.0662839768255825, 125.6049457141034));
        apichon.add(new LatLng(7.0660816460625036, 125.60516575226848));
        apichon.add(new LatLng(7.064961478522128, 125.60641172247628));
        apichon.add(new LatLng(7.064443404955173, 125.60701143501073));
        apichon.add(new LatLng(7.0643428475045305, 125.60719551962755));
        apichon.add(new LatLng(7.064043369764342, 125.60775950039192));
        apichon.add(new LatLng(7.063873657676066, 125.60808845660384));
        apichon.add(new LatLng(7.063756494534064, 125.60832660355321));
        apichon.add(new LatLng(7.063670649837893, 125.60846540786068));
        apichon.add(new LatLng(7.063113453321583, 125.60936624375069));

        // Bacaca to Circumferential Road. (30 kph zone - Tolerance 15 M)

        bacacacir.add(new LatLng(7.10519855047095, 125.60172768007222));
        bacacacir.add(new LatLng(7.104773280453802, 125.60169036171366));
        bacacacir.add(new LatLng(7.104710354932144, 125.6016909835282));
        bacacacir.add(new LatLng(7.10463383351204, 125.60170439457403));
        bacacacir.add(new LatLng(7.104490960398011, 125.60178333193805));
        bacacacir.add(new LatLng(7.103340307023859, 125.60285811274137));
        bacacacir.add(new LatLng(7.103203131480196, 125.60296416870241));
        bacacacir.add(new LatLng(7.103062040413755, 125.60304789858085));
        bacacacir.add(new LatLng(7.102994834516136, 125.60307368855572));
        bacacacir.add(new LatLng(7.1027486342, 125.60310319285756));
        bacacacir.add(new LatLng(7.101878858824558, 125.60298140207907));
        bacacacir.add(new LatLng(7.101768401134327, 125.6030062125129));
        bacacacir.add(new LatLng(7.101643781583782, 125.60305274703731));
        bacacacir.add(new LatLng(7.101525188037055, 125.60307109690248));
        bacacacir.add(new LatLng(7.101301178894531, 125.60304375010737));
        bacacacir.add(new LatLng(7.100052842542032, 125.60282040521045));
        bacacacir.add(new LatLng(7.099741674474377, 125.60274575255855));
        bacacacir.add(new LatLng(7.09946170013471, 125.60268696454206));
        bacacacir.add(new LatLng(7.099388336073171, 125.60269288062642));
        bacacacir.add(new LatLng(7.099232629803102, 125.60271836161297));
        bacacacir.add(new LatLng(7.099012158513809, 125.60277726930865));
        bacacacir.add(new LatLng(7.0981236175744975, 125.60302135070441));
        bacacacir.add(new LatLng(7.097835500433275, 125.60307131637686));
        bacacacir.add(new LatLng(7.09757249225288, 125.6030783361311));
        bacacacir.add(new LatLng(7.097347582292182, 125.60307028950409));
        bacacacir.add(new LatLng(7.097149129114428, 125.60304776598844));
        bacacacir.add(new LatLng(7.096497418024316, 125.60295781984411));
        bacacacir.add(new LatLng(7.096262581136175, 125.60293384006492));
        bacacacir.add(new LatLng(7.096173415409077, 125.60292847564722));
        bacacacir.add(new LatLng(7.096096227152186, 125.60292914620118));
        bacacacir.add(new LatLng(7.09571347384149, 125.60296002694903));
        bacacacir.add(new LatLng(7.095126001257522, 125.60305545389961));
        bacacacir.add(new LatLng(7.094585201468488, 125.6031806205447));
        bacacacir.add(new LatLng(7.09412198755994, 125.60336200131711));
        bacacacir.add(new LatLng(7.093765561998841, 125.60358822748672));
        bacacacir.add(new LatLng(7.093565270707, 125.60373574897594));
        bacacacir.add(new LatLng(7.093419543764497, 125.60387790605925));
        bacacacir.add(new LatLng(7.093095681006581, 125.60435227606806));
        bacacacir.add(new LatLng(7.092973243637643, 125.60448772762209));
        bacacacir.add(new LatLng(7.092895389422499, 125.60454405401475));
        bacacacir.add(new LatLng(7.092645989275484, 125.60470266813512));
        bacacacir.add(new LatLng(7.09255111818986, 125.60476326906104));
        bacacacir.add(new LatLng(7.092054660945353, 125.60511476022313));
        bacacacir.add(new LatLng(7.091920911122928, 125.6052676461404));
        bacacacir.add(new LatLng(7.09164205371403, 125.60564695516763));
        bacacacir.add(new LatLng(7.091257540786571, 125.6060120998995));
        bacacacir.add(new LatLng(7.091152403932243, 125.60609189561954));
        bacacacir.add(new LatLng(7.090958759819922, 125.60623954338575));
        bacacacir.add(new LatLng(7.090835758093591, 125.60619010369837));
        bacacacir.add(new LatLng(7.089651876541557, 125.60553865977873));
        bacacacir.add(new LatLng(7.089436833122955, 125.60542512427261));
        bacacacir.add(new LatLng(7.089276465506038, 125.60535270463224));
        bacacacir.add(new LatLng(7.089209922908861, 125.60533996413784));
        bacacacir.add(new LatLng(7.089110017208095, 125.60533644241104));
        bacacacir.add(new LatLng(7.089010203275467, 125.60535052400746));
        bacacacir.add(new LatLng(7.088602296768586, 125.60550206881453));
        bacacacir.add(new LatLng(7.088296466835096, 125.60560557165462));
        bacacacir.add(new LatLng(7.087919831967439, 125.60569252734099));
        bacacacir.add(new LatLng(7.08749879052269, 125.6057583284941));
        bacacacir.add(new LatLng(7.086997581307155, 125.60587249552532));
        bacacacir.add(new LatLng(7.086677509748689, 125.60595966731992));
        bacacacir.add(new LatLng(7.086521320125768, 125.60597963686972));
        bacacacir.add(new LatLng(7.086376732908955, 125.60597727800264));
        bacacacir.add(new LatLng(7.085843962987678, 125.60593433094567));
        bacacacir.add(new LatLng(7.084906554436037, 125.60587398797159));
        bacacacir.add(new LatLng(7.084630617556832, 125.6058213480331));
        bacacacir.add(new LatLng(7.0843444814391185, 125.60575563391372));
        bacacacir.add(new LatLng(7.082769892504024, 125.60534291028112));
        bacacacir.add(new LatLng(7.082601537340067, 125.6052798783684));
        bacacacir.add(new LatLng(7.082429121349539, 125.60518872627725));
        bacacacir.add(new LatLng(7.082111723753251, 125.60497669860118));
        bacacacir.add(new LatLng(7.081696833800736, 125.60473710136574));
        bacacacir.add(new LatLng(7.080857947353334, 125.60421013665784));
        bacacacir.add(new LatLng(7.080590271227395, 125.60400899591583));
        bacacacir.add(new LatLng(7.080336495098396, 125.6037737678678));
        bacacacir.add(new LatLng(7.080088774253733, 125.60351969302206));
        bacacacir.add(new LatLng(7.079163004073264, 125.6026328922747));
        bacacacir.add(new LatLng(7.078179634391394, 125.60166184224732));
        bacacacir.add(new LatLng(7.077908480135379, 125.60134795785729));
        bacacacir.add(new LatLng(7.077770911116316, 125.60107109446868));
        bacacacir.add(new LatLng(7.077588579829504, 125.60020474095626));
        bacacacir.add(new LatLng(7.077529750295794, 125.60001398226066));
        bacacacir.add(new LatLng(7.077342858947941, 125.59954121336587));
        bacacacir.add(new LatLng(7.077223889695826, 125.59919667516222));
        bacacacir.add(new LatLng(7.077065859819069, 125.59877693522387));
        bacacacir.add(new LatLng(7.076959156261851, 125.59851766227852));
        bacacacir.add(new LatLng(7.076889284668912, 125.59837215244289));
        bacacacir.add(new LatLng(7.076768173882335, 125.59818506836505));
        bacacacir.add(new LatLng(7.076716269250871, 125.59814148247139));
        bacacacir.add(new LatLng(7.076639077734828, 125.59810460209931));
        bacacacir.add(new LatLng(7.076529944881063, 125.59807576835132));
        bacacacir.add(new LatLng(7.076272747188815, 125.59803193634204));

        // Emilio Jacinto Ext. to Roxas Ave cor. Quezon Blvd (30 kph zone - Tolerance 15 M)

        ejtor.add(new LatLng(7.082099862292275, 125.60498885773289));
        ejtor.add(new LatLng(7.081811432312929, 125.60545071683319));
        ejtor.add(new LatLng(7.081587845529695, 125.60585304818245));
        ejtor.add(new LatLng(7.081491357150042, 125.60605354331449));
        ejtor.add(new LatLng(7.081291060524272, 125.60644715747857));
        ejtor.add(new LatLng(7.081085440308466, 125.60670196733348));
        ejtor.add(new LatLng(7.081060819108207, 125.6067281188794));
        ejtor.add(new LatLng(7.08070547520275, 125.60703389069919));
        ejtor.add(new LatLng(7.080276857206485, 125.60735574958682));
        ejtor.add(new LatLng(7.079470115783598, 125.60800084322881));
        ejtor.add(new LatLng(7.079004347926981, 125.6083655008725));
        ejtor.add(new LatLng(7.078596805930154, 125.60871443266903));
        ejtor.add(new LatLng(7.078357086124659, 125.60893024024732));
        ejtor.add(new LatLng(7.077790919572713, 125.60935182143507));
        ejtor.add(new LatLng(7.077348529042273, 125.60972197352814));
        ejtor.add(new LatLng(7.076589450188371, 125.61032719471652));
        ejtor.add(new LatLng(7.076171621625473, 125.61065027674904));
        ejtor.add(new LatLng(7.075919515703253, 125.61086396921493));
        ejtor.add(new LatLng(7.075726036229509, 125.6110078726349));

        // Mabini St. (30 kph zone - Tolerance 15 M)

        mabini.add(new LatLng(7.080064617167506, 125.6035237026868));
        mabini.add(new LatLng(7.079922764385393, 125.6036898929572));
        mabini.add(new LatLng(7.079708492843151, 125.6039319623187));
        mabini.add(new LatLng(7.079685867890532, 125.60396079607145));
        mabini.add(new LatLng(7.079642614304889, 125.604019134114));
        mabini.add(new LatLng(7.079535478480825, 125.60417000836951));
        mabini.add(new LatLng(7.079424540128874, 125.60430637505371));
        mabini.add(new LatLng(7.0794041589829275, 125.60432857317691));
        mabini.add(new LatLng(7.079360467732594, 125.60436513908016));
        mabini.add(new LatLng(7.079190115003484, 125.60450059063253));
        mabini.add(new LatLng(7.078971850475711, 125.60468968636911));
        mabini.add(new LatLng(7.078866045377826, 125.6047755170572));
        mabini.add(new LatLng(7.078647115255489, 125.6049579072688));
        mabini.add(new LatLng(7.078575247593619, 125.60501490421078));
        mabini.add(new LatLng(7.078458129898485, 125.60511347539294));
        mabini.add(new LatLng(7.078293100369462, 125.60524423308284));
        mabini.add(new LatLng(7.07804688685069, 125.60544338709872));
        mabini.add(new LatLng(7.0778665519986514, 125.60558956749335));
        mabini.add(new LatLng(7.077625661794606, 125.60578067488113));
        mabini.add(new LatLng(7.077372128042027, 125.60598184056238));
        mabini.add(new LatLng(7.0772104253425905, 125.60611058659156));
        mabini.add(new LatLng(7.076745945664969, 125.60647603756561));
        mabini.add(new LatLng(7.076347344534604, 125.60679857320585));
        mabini.add(new LatLng(7.07594369968635, 125.60712605165534));
        mabini.add(new LatLng(7.074448004654078, 125.60836429307223));
        mabini.add(new LatLng(7.073702764800046, 125.60900428462433));

        // Tionko Ave. (30 kph zone - Tolerance 15 M)

        tionko.add(new LatLng(7.0785515354825, 125.60692453826346));
        tionko.add(new LatLng(7.078153582848202, 125.60722821657471));
        tionko.add(new LatLng(7.077702737440345, 125.60756988368053));
        tionko.add(new LatLng(7.077277789238692, 125.60791011715646));
        tionko.add(new LatLng(7.0768852174483445, 125.60822693884808));
        tionko.add(new LatLng(7.076098690377734, 125.6088758654438));
        tionko.add(new LatLng(7.0750061790095335, 125.60972611996092));

        // Roxas Ave. (30 kph zone - Tolerance 40 M)

        roxas.add(new LatLng(7.073298518580007, 125.61118245579257));
        roxas.add(new LatLng(7.072154679009645, 125.61210568744934));
        roxas.add(new LatLng(7.071385237355558, 125.61273818556407));
        roxas.add(new LatLng(7.070688508873485, 125.61329742614195));
        roxas.add(new LatLng(7.070083611947783, 125.61381241026744));
        roxas.add(new LatLng(7.0696064814689965, 125.61420535388919));
        roxas.add(new LatLng(7.069064801357871, 125.61464791837653));
        roxas.add(new LatLng(7.06669954542455, 125.61661480820777));

        // Claveria St. (30 kph zone - Tolerance 15 M)

        claveria.add(new LatLng(7.063196666243903, 125.60959062233668));
        claveria.add(new LatLng(7.0636379743835604, 125.60969436017959));
        claveria.add(new LatLng(7.064055973604336, 125.60981253581585));
        claveria.add(new LatLng(7.064315091887771, 125.6098849170425));
        claveria.add(new LatLng(7.065905837759626, 125.61034978006727));
        claveria.add(new LatLng(7.067140263706068, 125.61069042061177));
        claveria.add(new LatLng(7.067690597014064, 125.6108446476302));
        claveria.add(new LatLng(7.068277529758708, 125.6110102740373));
        claveria.add(new LatLng(7.068979585521162, 125.61120808695213));
        claveria.add(new LatLng(7.070687140390942, 125.61169021402067));
        claveria.add(new LatLng(7.071095728188054, 125.61180152569307));
        claveria.add(new LatLng(7.0720614536259365, 125.61207358916324));
        claveria.add(new LatLng(7.072280386870993, 125.612099070149));
        claveria.add(new LatLng(7.072354917314335, 125.61211717505971));
        claveria.add(new LatLng(7.072599802970903, 125.6121916063596));
        claveria.add(new LatLng(7.072772106741195, 125.61224797690481));
        claveria.add(new LatLng(7.073202790852449, 125.61240436555374));
        claveria.add(new LatLng(7.073610045528017, 125.61252975882365));

        // Ponciano / C. Bangoy St. to Cor. San Pedro St. (30 kph zone - Tolerance 15 M)

        cbtocsp.add(new LatLng(7.073631397763781, 125.61251167534996));
        cbtocsp.add(new LatLng(7.0736454263264, 125.6122009163695));
        cbtocsp.add(new LatLng(7.07361681204093, 125.61153438744105));
        cbtocsp.add(new LatLng(7.073605579764508, 125.61141915070574));
        cbtocsp.add(new LatLng(7.073547108730605, 125.6112323690932));
        cbtocsp.add(new LatLng(7.07354361269878, 125.61120514627581));
        cbtocsp.add(new LatLng(7.073581543267649, 125.61112333890419));
        cbtocsp.add(new LatLng(7.073574888782115, 125.61105091926377));
        cbtocsp.add(new LatLng(7.073534446054166, 125.61098575463247));
        cbtocsp.add(new LatLng(7.07349957805426, 125.61095903480555));
        cbtocsp.add(new LatLng(7.073477733281463, 125.61094765421814));
        cbtocsp.add(new LatLng(7.073426669859423, 125.61093823334957));
        cbtocsp.add(new LatLng(7.073336792058625, 125.61095296874169));
        cbtocsp.add(new LatLng(7.073283554356861, 125.61088555780287));
        cbtocsp.add(new LatLng(7.073251614588312, 125.61086512639737));
        cbtocsp.add(new LatLng(7.072952162427136, 125.61077862515926));
        cbtocsp.add(new LatLng(7.07269796066052, 125.6107035233076));
        cbtocsp.add(new LatLng(7.071258002700207, 125.6103125995812));
        cbtocsp.add(new LatLng(7.069382170173026, 125.6098108708207));
        cbtocsp.add(new LatLng(7.0685014431532425, 125.60958567498746));
        cbtocsp.add(new LatLng(7.06808389264395, 125.6094823095648));
        cbtocsp.add(new LatLng(7.067677615185055, 125.6093464945096));
        cbtocsp.add(new LatLng(7.067390018203374, 125.60926524554318));
        cbtocsp.add(new LatLng(7.066756984636795, 125.60909810271818));
        cbtocsp.add(new LatLng(7.06646490710074, 125.60897737015554));
        cbtocsp.add(new LatLng(7.065756095048109, 125.60864440348134));
        cbtocsp.add(new LatLng(7.065472224661217, 125.60851070128953));
        cbtocsp.add(new LatLng(7.065189085612189, 125.60833605458082));

        // C. Bangoy St. (30 kph zone - Tolerance 15 M)

        cbangoy.add(new LatLng(7.075341489789574, 125.61121693190213));
        cbangoy.add(new LatLng(7.07528551728493, 125.61123941224956));
        cbangoy.add(new LatLng(7.075072574483132, 125.61129238587769));
        cbangoy.add(new LatLng(7.07496975883663, 125.61130093127085));
        cbangoy.add(new LatLng(7.07475016142153, 125.61126874476385));
        cbangoy.add(new LatLng(7.074721670346945, 125.61126600345624));
        cbangoy.add(new LatLng(7.074252530032261, 125.6111406101848));
        cbangoy.add(new LatLng(7.0741041351958245, 125.61110305925939));
        cbangoy.add(new LatLng(7.07394509314194, 125.61104405066084));
        cbangoy.add(new LatLng(7.0738978463272, 125.61103667458661));
        cbangoy.add(new LatLng(7.073596398240217, 125.61104874452727));

        // Buhangin Milan to Overpass (30 kph zone - Tolerance 15 M)

        bmtoo.add(new LatLng(7.108590816178421, 125.61334217387638));
        bmtoo.add(new LatLng(7.106606145388121, 125.61371184062965));
        bmtoo.add(new LatLng(7.106479719207622, 125.61373598051054));
        bmtoo.add(new LatLng(7.1041120436860545, 125.61411501549216));
        bmtoo.add(new LatLng(7.1039084298748385, 125.61415793083627));
        bmtoo.add(new LatLng(7.103255667340611, 125.61426790140615));
        bmtoo.add(new LatLng(7.101134286815561, 125.61464545911316));
        bmtoo.add(new LatLng(7.100420968055401, 125.61473128980238));
        bmtoo.add(new LatLng(7.09972162181402, 125.61476481741505));
        bmtoo.add(new LatLng(7.098614339150097, 125.61486443964537));
        bmtoo.add(new LatLng(7.097933786637898, 125.6149227787299));
        bmtoo.add(new LatLng(7.097784068599495, 125.61492546093834));
        bmtoo.add(new LatLng(7.097643666303036, 125.61493417811724));
        bmtoo.add(new LatLng(7.096901788289628, 125.61498904900124));
        bmtoo.add(new LatLng(7.096724787896893, 125.61500983612108));
        bmtoo.add(new LatLng(7.09603782143539, 125.6150853055919));
        bmtoo.add(new LatLng(7.09575324477263, 125.61512569153109));
        bmtoo.add(new LatLng(7.095497724745143, 125.61518402957708));
        bmtoo.add(new LatLng(7.095203610370051, 125.61528461241471));
        bmtoo.add(new LatLng(7.095093151074098, 125.61532149278881));
        bmtoo.add(new LatLng(7.094859360352042, 125.61541902334467));
        bmtoo.add(new LatLng(7.0944550530879855, 125.61566976392636));

        // J.P Laurel to Acacia (30 kph zone - Tolerance 15 M)

        jpltoa.add(new LatLng(7.095137990339589, 125.61524389068417));
        jpltoa.add(new LatLng(7.0949089764070274, 125.61484811983183));
        jpltoa.add(new LatLng(7.094636697739822, 125.61441960766321));
        jpltoa.add(new LatLng(7.094167577689814, 125.61369339957447));
        jpltoa.add(new LatLng(7.0934116612552, 125.6124401374088));
        jpltoa.add(new LatLng(7.092998685766533, 125.61175446420093));
        jpltoa.add(new LatLng(7.092604841186748, 125.611120800907));
        jpltoa.add(new LatLng(7.092247603038113, 125.61069178373761));
        jpltoa.add(new LatLng(7.092095221460641, 125.6105388978217));
        jpltoa.add(new LatLng(7.0919919283103035, 125.61044988183869));
        jpltoa.add(new LatLng(7.091973961916443, 125.61043043582362));
        jpltoa.add(new LatLng(7.091895594864552, 125.61037930638588));
        jpltoa.add(new LatLng(7.091514307826255, 125.6101566830388));
        jpltoa.add(new LatLng(7.091235495634782, 125.61003330142083));
        jpltoa.add(new LatLng(7.091039963572822, 125.6099743228478));
        jpltoa.add(new LatLng(7.090780346020785, 125.60991796643074));
        jpltoa.add(new LatLng(7.090492392295483, 125.60991212157317));
        jpltoa.add(new LatLng(7.090270102149686, 125.60993409538506));
        jpltoa.add(new LatLng(7.089910263572266, 125.61001251498162));
        jpltoa.add(new LatLng(7.0897973242421335, 125.6100261963375));
        jpltoa.add(new LatLng(7.089652744024856, 125.61005006590676));
        jpltoa.add(new LatLng(7.089187626607206, 125.61010052390907));
        jpltoa.add(new LatLng(7.088731301128212, 125.61016181053617));
        jpltoa.add(new LatLng(7.087746871951391, 125.61026212812932));
        jpltoa.add(new LatLng(7.087438361928804, 125.61031417293378));
        jpltoa.add(new LatLng(7.08722342844839, 125.61037586374314));
        jpltoa.add(new LatLng(7.087052572339392, 125.6104460112779));
        jpltoa.add(new LatLng(7.08665942575789, 125.61069230659197));
        jpltoa.add(new LatLng(7.08655642337446, 125.61076411262492));
        jpltoa.add(new LatLng(7.085398686510121, 125.61147176157783));
        jpltoa.add(new LatLng(7.0848742166772745, 125.61175210808408));
        jpltoa.add(new LatLng(7.084233980713146, 125.61211078556009));
        jpltoa.add(new LatLng(7.0838992676960695, 125.61227507085835));
        jpltoa.add(new LatLng(7.083821964691151, 125.612293722307));
        jpltoa.add(new LatLng(7.083556473864145, 125.61238276426522));
        jpltoa.add(new LatLng(7.083386641222644, 125.61242519439094));
        jpltoa.add(new LatLng(7.082824165683961, 125.61250258205017));
        jpltoa.add(new LatLng(7.082053297446791, 125.61259949433254));
        jpltoa.add(new LatLng(7.081006896847136, 125.61274006980989));
        jpltoa.add(new LatLng(7.080300459668197, 125.61283596943794));
        jpltoa.add(new LatLng(7.078647640410753, 125.61309616817664));
        jpltoa.add(new LatLng(7.078517879349625, 125.61311092032561));
        jpltoa.add(new LatLng(7.078311592456989, 125.61314914180424));
        jpltoa.add(new LatLng(7.077603032541724, 125.61321861826798));
        jpltoa.add(new LatLng(7.076676698809227, 125.61333479464862));
        jpltoa.add(new LatLng(7.076590856513943, 125.61333881796206));
        jpltoa.add(new LatLng(7.076547842971188, 125.61333206823431));
        jpltoa.add(new LatLng(7.075731125670949, 125.61310515434602));
        jpltoa.add(new LatLng(7.075253700362607, 125.61296386370927));
        jpltoa.add(new LatLng(7.074747827624344, 125.61284347560067));
        jpltoa.add(new LatLng(7.074205122677891, 125.61267014256917));
        jpltoa.add(new LatLng(7.073953837179471, 125.61259875465134));
        jpltoa.add(new LatLng(7.073802115005334, 125.61256388593536));
        jpltoa.add(new LatLng(7.073653763514399, 125.61254007960233));

        // Quirino Ave. to Generoso Bridge (30 kph zone - Tolerance 15 M)

        qtogb.add(new LatLng(7.076618171036595, 125.61331609764294));
        qtogb.add(new LatLng(7.076611545239552, 125.61318534200362));
        qtogb.add(new LatLng(7.076600898132774, 125.61309749965812));
        qtogb.add(new LatLng(7.076594243691133, 125.61301703339004));
        qtogb.add(new LatLng(7.07658958558134, 125.61299892847968));
        qtogb.add(new LatLng(7.076576276696886, 125.61291913276624));
        qtogb.add(new LatLng(7.076534353707758, 125.61279642170379));
        qtogb.add(new LatLng(7.076496423381119, 125.61273406034277));
        qtogb.add(new LatLng(7.076173682754801, 125.61233642285683));
        qtogb.add(new LatLng(7.076061406861429, 125.61221554203595));
        qtogb.add(new LatLng(7.075741143416105, 125.61185094302186));
        qtogb.add(new LatLng(7.075707205697045, 125.61181674485722));
        qtogb.add(new LatLng(7.075691235005119, 125.61179998105185));
        qtogb.add(new LatLng(7.075668609857278, 125.61177315896329));
        qtogb.add(new LatLng(7.075624690448453, 125.61171750312477));
        qtogb.add(new LatLng(7.075564134893288, 125.61164508348044));
        qtogb.add(new LatLng(7.075545502413531, 125.6116182613919));
        qtogb.add(new LatLng(7.075504244775743, 125.61154919450799));
        qtogb.add(new LatLng(7.075483615956162, 125.61149957364415));
        qtogb.add(new LatLng(7.075467645256488, 125.61146604603346));
        qtogb.add(new LatLng(7.075451674555301, 125.61143251841418));
        qtogb.add(new LatLng(7.075430380287487, 125.61137619202823));
        qtogb.add(new LatLng(7.075423060382702, 125.61135071104408));
        qtogb.add(new LatLng(7.075417071369052, 125.61128969078568));
        qtogb.add(new LatLng(7.075409619821885, 125.61125386772055));
        qtogb.add(new LatLng(7.075409086018146, 125.61121257728111));
        qtogb.add(new LatLng(7.075409760344516, 125.61116431554932));
        qtogb.add(new LatLng(7.075403753797028, 125.61097807523022));
        qtogb.add(new LatLng(7.075406433114903, 125.61089140078704));
        qtogb.add(new LatLng(7.07540443677714, 125.61087195477285));
        qtogb.add(new LatLng(7.075389131520685, 125.61075125537437));
        qtogb.add(new LatLng(7.0753858042909465, 125.6107277860469));
        qtogb.add(new LatLng(7.075386469736901, 125.6107284565991));
        qtogb.add(new LatLng(7.075377818939188, 125.61067950627861));
        qtogb.add(new LatLng(7.075365840911748, 125.61061781547492));
        qtogb.add(new LatLng(7.075299961754411, 125.61038647494419));
        qtogb.add(new LatLng(7.0752839910484004, 125.6103435596025));
        qtogb.add(new LatLng(7.075264027665106, 125.61028052769441));
        qtogb.add(new LatLng(7.075252049632896, 125.61026309332361));
        qtogb.add(new LatLng(7.075224100894103, 125.61019804975885));
        qtogb.add(new LatLng(7.0751981484922855, 125.61014306447733));
        qtogb.add(new LatLng(7.075164210733852, 125.61007265649488));
        qtogb.add(new LatLng(7.075134265650818, 125.61000560127353));
        qtogb.add(new LatLng(7.075106982348896, 125.60996335647057));
        qtogb.add(new LatLng(7.075076371815748, 125.60991507671119));
        qtogb.add(new LatLng(7.075046426727015, 125.60986344419074));
        qtogb.add(new LatLng(7.074973893059577, 125.60977828405957));
        qtogb.add(new LatLng(7.074950602428746, 125.60975012086193));
        qtogb.add(new LatLng(7.074862098023074, 125.60964685581379));
        qtogb.add(new LatLng(7.0747522993024, 125.60954224966257));
        qtogb.add(new LatLng(7.074662463965963, 125.60946178339067));
        qtogb.add(new LatLng(7.074589930237027, 125.60940813920955));
        qtogb.add(new LatLng(7.074546676173754, 125.60937729380771));
        qtogb.add(new LatLng(7.074511407470802, 125.60935583613313));
        qtogb.add(new LatLng(7.074461498927416, 125.60932834349238));
        qtogb.add(new LatLng(7.074421572088819, 125.60930621526933));
        qtogb.add(new LatLng(7.074382976138864, 125.60928878090893));
        qtogb.add(new LatLng(7.0743443801883865, 125.60927000544692));
        qtogb.add(new LatLng(7.074171363816472, 125.6091975858043));
        qtogb.add(new LatLng(7.074079532022695, 125.60916405819164));
        qtogb.add(new LatLng(7.074035612463709, 125.60915332935622));
        qtogb.add(new LatLng(7.074018976263349, 125.60914863548832));
        qtogb.add(new LatLng(7.073916497272466, 125.60912114284757));
        qtogb.add(new LatLng(7.073865923214336, 125.60910572014429));
        qtogb.add(new LatLng(7.0737827421894055, 125.6090815802646));
        qtogb.add(new LatLng(7.073762778741189, 125.60907487474246));
        qtogb.add(new LatLng(7.07367826679861, 125.60905073486099));
        qtogb.add(new LatLng(7.073640089342426, 125.6090353099328));
        qtogb.add(new LatLng(7.073468270744021, 125.60894628559639));
        qtogb.add(new LatLng(7.0734157002914095, 125.60891409908864));
        qtogb.add(new LatLng(7.073317879308226, 125.60883296226848));
        qtogb.add(new LatLng(7.072578564941326, 125.60795655046796));
        qtogb.add(new LatLng(7.071885165514504, 125.6071418294843));
        qtogb.add(new LatLng(7.071799322326274, 125.60702180063119));
        qtogb.add(new LatLng(7.07143199315661, 125.60658594166759));
        qtogb.add(new LatLng(7.0714140259617855, 125.60656448399675));
        qtogb.add(new LatLng(7.070812457246831, 125.60584766364094));
        qtogb.add(new LatLng(7.070482392663943, 125.60544868505112));
        qtogb.add(new LatLng(7.070235509524357, 125.60514827764423));
        qtogb.add(new LatLng(7.070023530110689, 125.60489310647931));
        qtogb.add(new LatLng(7.068914582672184, 125.60354766941829));
        qtogb.add(new LatLng(7.068287057911184, 125.60280670917858));
        qtogb.add(new LatLng(7.068086213912386, 125.60256490151964));
        qtogb.add(new LatLng(7.067974417210211, 125.60245157819028));
        qtogb.add(new LatLng(7.067943140747689, 125.6024220738929));
        qtogb.add(new LatLng(7.067883249640764, 125.60238049965027));
        qtogb.add(new LatLng(7.067781434743809, 125.60232685546973));
        qtogb.add(new LatLng(7.067648343340783, 125.60227119963166));
        qtogb.add(new LatLng(7.067605041479968, 125.60225104696144));
        qtogb.add(new LatLng(7.067574430447523, 125.60223830646726));
        qtogb.add(new LatLng(7.067544484872408, 125.6022255659752));
        qtogb.add(new LatLng(7.067344182194919, 125.60215784020355));
        qtogb.add(new LatLng(7.067280963723158, 125.6021410763966));
        qtogb.add(new LatLng(7.066849746972109, 125.6020471990828));
        qtogb.add(new LatLng(7.066554283411574, 125.60198684938126));
        qtogb.add(new LatLng(7.0664444827164, 125.6019653917096));
        qtogb.add(new LatLng(7.066375988323321, 125.60195087754494));
        qtogb.add(new LatLng(7.065712525609799, 125.60181057380272));
        qtogb.add(new LatLng(7.065148804398314, 125.60168931218045));

        // Tulip Drive (30 kph zone - Tolerance 15 M)

        tulip.add(new LatLng(7.0605760450167505, 125.5879783690034));
        tulip.add(new LatLng(7.059459874019166, 125.58824852547711));
        tulip.add(new LatLng(7.058911668961029, 125.5883723954325));
        tulip.add(new LatLng(7.05887909765392, 125.5883803313816));
        tulip.add(new LatLng(7.058664150885555, 125.58841989396295));
        tulip.add(new LatLng(7.057534870502817, 125.58869090507774));
        tulip.add(new LatLng(7.056609676296144, 125.58891597071693));
        tulip.add(new LatLng(7.0561436824422135, 125.58903369770258));
        tulip.add(new LatLng(7.056044764684897, 125.58906219974394));
        tulip.add(new LatLng(7.055761431503653, 125.58912751078877));
        tulip.add(new LatLng(7.054660683206743, 125.58937286257101));
        tulip.add(new LatLng(7.054320909396727, 125.58944437754911));
        tulip.add(new LatLng(7.0537946503582525, 125.5895864339226));
        tulip.add(new LatLng(7.05271941164892, 125.58982647785692));
        tulip.add(new LatLng(7.052228099366457, 125.58993816057905));
        tulip.add(new LatLng(7.051924782898847, 125.58999720834181));
        tulip.add(new LatLng(7.05179485629813, 125.59002241426016));

        // Sandawa (30 kph zone - Tolerance 15 M)

        sandawa.add(new LatLng(7.063593254829351, 125.60069820861956));
        sandawa.add(new LatLng(7.06277997166559, 125.60093369014145));
        sandawa.add(new LatLng(7.062744808903619, 125.60094466653423));
        sandawa.add(new LatLng(7.062042319944147, 125.6011171814845));
        sandawa.add(new LatLng(7.061938193835887, 125.60114033874399));
        sandawa.add(new LatLng(7.0613821614654, 125.60127768573126));
        sandawa.add(new LatLng(7.060489799095082, 125.60148182766305));
        sandawa.add(new LatLng(7.060456038864867, 125.60148883542401));
        sandawa.add(new LatLng(7.060077283905769, 125.60154656310468));
        sandawa.add(new LatLng(7.05989364556524, 125.60156512989853));
        sandawa.add(new LatLng(7.059753415233521, 125.60153679797033));
        sandawa.add(new LatLng(7.059277745910742, 125.60138886757315));
        sandawa.add(new LatLng(7.05890707944351, 125.6012440282869));
        sandawa.add(new LatLng(7.058613607217588, 125.60116892643579));
        sandawa.add(new LatLng(7.058084000907858, 125.60119743460153));
        sandawa.add(new LatLng(7.057698821064428, 125.60122412253754));
        sandawa.add(new LatLng(7.057533819919349, 125.60123310733903));
        sandawa.add(new LatLng(7.057388628468098, 125.60124066086638));

        // F. Torres St. (30 kph zone - Tolerance 15 M)

        ftorres.add(new LatLng(7.082844214258236, 125.61247749404046));
        ftorres.add(new LatLng(7.0827900417092025, 125.61220388498367));
        ftorres.add(new LatLng(7.082790959491028, 125.6122056710078));
        ftorres.add(new LatLng(7.082672511996543, 125.61194549673809));
        ftorres.add(new LatLng(7.082527435508274, 125.61173791581479));
        ftorres.add(new LatLng(7.082231692537142, 125.61135966357106));
        ftorres.add(new LatLng(7.08182349668781, 125.6108327512117));
        ftorres.add(new LatLng(7.0808896782095685, 125.60974819873724));
        ftorres.add(new LatLng(7.07999526599219, 125.6086829577942));
        ftorres.add(new LatLng(7.078577368585896, 125.60692586445766));
        ftorres.add(new LatLng(7.077792042347535, 125.60595568409138));
        ftorres.add(new LatLng(7.077156448684151, 125.60517383734967));
        ftorres.add(new LatLng(7.0756160177097716, 125.60332901228715));
        ftorres.add(new LatLng(7.075596152165098, 125.60329958113478));
        ftorres.add(new LatLng(7.075369707158295, 125.60298517829476));
        ftorres.add(new LatLng(7.074729376612064, 125.60207235487196));
        ftorres.add(new LatLng(7.073897530846467, 125.60097639081245));
        ftorres.add(new LatLng(7.07367464721016, 125.60068951953221));
        ftorres.add(new LatLng(7.073557016920754, 125.6005457524216));

        // Bacaca (30 kph zone - Tolerance 15 M)

        bacaca.add(new LatLng(7.0887552061976935, 125.61013328521811));
        bacaca.add(new LatLng(7.088763229729631, 125.60975413813051));
        bacaca.add(new LatLng(7.088761977668951, 125.60960528258721));
        bacaca.add(new LatLng(7.0888111546739205, 125.60898136154223));
        bacaca.add(new LatLng(7.088824463205257, 125.60867827192524));
        bacaca.add(new LatLng(7.088845091428319, 125.60839865163678));
        bacaca.add(new LatLng(7.088905565332667, 125.60824076089804));
        bacaca.add(new LatLng(7.088962206486441, 125.60816328779387));
        bacaca.add(new LatLng(7.089217730138399, 125.60788098530008));
        bacaca.add(new LatLng(7.089457283437059, 125.60763623372807));
        bacaca.add(new LatLng(7.089430666411065, 125.6076563502919));
        bacaca.add(new LatLng(7.089654249390267, 125.60741562203066));
        bacaca.add(new LatLng(7.089815056946252, 125.60723590532274));
        bacaca.add(new LatLng(7.090015601300554, 125.60702317395155));
        bacaca.add(new LatLng(7.09043412726537, 125.60660760656937));
        bacaca.add(new LatLng(7.090514643593001, 125.60652982250762));
        bacaca.add(new LatLng(7.090708947398367, 125.60640308812548));
        bacaca.add(new LatLng(7.090863325705626, 125.60630652860264));
        bacaca.add(new LatLng(7.090928190891125, 125.6062625527992));

        // Medical School Drive (30 kph zone - Tolerance 15 M)

        medical.add(new LatLng(7.087059229651152, 125.61041641385361));
        medical.add(new LatLng(7.084210748639811, 125.60581428786344));
        medical.add(new LatLng(7.08418011599004, 125.60573064633986));

    }

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            locations = location;
            mLastLocation.set(location);
            speed = location.getSpeed() * 18 / 5;
            System.out.println("Current Speed is: "+speed);
            lat1 = location.getLatitude();
            lng1 = location.getLongitude();
            System.out.println("Current Latitude is: "+lat1);
            System.out.println("Current Longitude is: "+lng1);
            compare();
            setVal(location);

        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener((LocationManager.GPS_PROVIDER))
    };


    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        auth = FirebaseAuth.getInstance();
        if(intent != null){

            String action = intent.getAction();
            System.out.println("Current Action in Service Start: "+action);

            switch(action){

                case ACTION_START_FOREGROUND_SERVICE:

                    Log.i(TAG, "Service started");
                    showNotification(this);
                    FirebaseUser users = auth.getCurrentUser();
                    if (users != null) {

                        email = users.getEmail();

                    }
                    fd = FirebaseDatabase.getInstance();
                    dr = fd.getReference();
                    break;

                case ACTION_STOP_FOREGROUND_SERVICE:

                    stopForeground(true);
                    stopService(new Intent(this, Services.class));
                    stopSelf();
                    stopSelfResult(startId);
                    break;
            }
        }

        return START_STICKY;
    }

    public void compare(){

        MediaPlayer mp;
        mp = MediaPlayer.create(this, R.raw.siren);
        LatLng point = (new LatLng(lat1, lng1));

        try{

//            if(PolyUtil.isLocationOnPath(point, testLocation, true, 25));{
//
//                mp.start();
//            }

        }

        catch(Exception e){

            Toast.makeText(getApplicationContext(), "Can't Calculate Locations", Toast.LENGTH_LONG).show();
        }


    }

    private void setVal(Location location){

        Log.i(TAG, "Last location: " + location);
        mCurrentLocation = location;
        if (lStart == null) {
            lStart = mCurrentLocation;
            lEnd = mCurrentLocation;
        } else
            lEnd = mCurrentLocation;

        updateUI();
    }

    private void updateUI() {

        try{

            if (MapFragment.p == 0) {

                System.out.println("Updating User Interface");
                MapFragment.lat = lat1;
                MapFragment.lng = lng1;
                getStartTime();
                getAverage();
                distance = distance + (lStart.distanceTo(lEnd) / 1000.00);
                newspeed = (new DecimalFormat("#.###").format(speed));
                newdistance = (new DecimalFormat("#.###").format(distance));

                lStart = lEnd;
                setValues(newspeed, oras, newdistance, stringaverage);
            }
        }

        catch(Exception e){

            System.out.println("Services (updateUI): "+e);
        }
    }

    private void setValues (String speed, String time, String distance, String average) {

        try{

            Initial i = new Initial();
            i.setSpeed(speed);
            i.setTime(time);
            i.setDistance(distance);
            i.setAverage(average);
            i.setLocation(""+lat1+" ,"+lng1+"");

            String[] parts = email.split("@");
            dr.child("Users").child(parts[0]).child("Vehicle Data").setValue(i);
        }

        catch(Exception e){

            System.out.println("Services (setValues): "+e);
        }
    }

    private void getStartTime(){

        String[] parts = email.split("@");

        try{

            fd1 = FirebaseDatabase.getInstance();
            dr1 = fd1.getReference();
            dr1.child("Users").child(parts[0]).child("Start Time").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Long a = snapshot.getValue(Long.class);
                    try{

                        long endtime = System.currentTimeMillis();
                        long msec = endtime - a;
                        long sec = msec / 1000;
                        min = sec / 60;
                        oras = Long.toString(min);
                    }
                    catch(Exception e){

                        System.out.println("Services (getStartTime: snapshot): "+e);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e){

            System.out.println("Services (getStartTime): "+e);
        }
    }

    private void getAverage(){

        String[] parts = email.split("@");
        try{

            FirebaseDatabase f = FirebaseDatabase.getInstance();
            DatabaseReference d = f.getReference();
            d.child("Users").child(parts[0]).child("Vehicle Data").child("average").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String b = snapshot.getValue(String.class);
                    Double a = Double.parseDouble(b);
                    try{

                        if(a < speed){
                            newaverage = (new DecimalFormat("#.###").format(speed));
                            stringaverage = newaverage.toString();
                        }
                        else{
                            newaverage = (new DecimalFormat("#.###").format(a));
                            stringaverage = newaverage.toString();
                        }

                    }
                    catch(Exception e){

                        System.out.println("Services (getAverage: snapshot): "+e);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        catch(Exception e){

            System.out.println("Services (getAverage): "+e);
        }
    }

    public void showNotification(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_LOW;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("SpeedAlert")
                .setContentText("Your Location and Vehicle Speed is being monitored.")
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher).build();

        startForeground(notificationId, notification);

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        restartService.setAction(Services.ACTION_START_FOREGROUND_SERVICE);
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +100, restartServicePI);

    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }
}
