package pwc.appdev.speedalert;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    private TextInputLayout lname, ldob, lnumber, lemail, lplatenumber, lusername, lpassword, lcpassword;
    private TextInputEditText name, dob, number, email, platenumber, username, password, cpassword;
    private Button register;
    private FirebaseAuth auth;
    private ConstraintLayout layout;
    FirebaseDatabase firebaseDatabase, fd;
    DatabaseReference databaseReference, dr;
    private boolean monitoringConnectivity = false;
    private boolean isConnected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signup);

        auth = FirebaseAuth.getInstance();
        dob = findViewById(R.id.dateofbirth);
        register = findViewById(R.id.signupbutton);
        name = findViewById(R.id.completename);
        number = findViewById(R.id.contactnumber);
        email = findViewById(R.id.emailadd);
        platenumber = findViewById(R.id.platenumber);
        username = findViewById(R.id.regusername);
        password = findViewById(R.id.regpassword);
        cpassword = findViewById(R.id.regcpassword);
        layout = findViewById(R.id.constraintsignup);
        lname = findViewById(R.id.layoutcompletename);
        ldob = findViewById(R.id.layoutdateofbirth);
        lnumber = findViewById(R.id.layoutcontactnumber);
        lemail = findViewById(R.id.layoutemailadd);
        lplatenumber = findViewById(R.id.layoutplatenumber);
        lusername = findViewById(R.id.layoutusername);
        lpassword = findViewById(R.id.layoutpassword);
        lcpassword = findViewById(R.id.layoutcpassword);

        FirebaseApp.initializeApp(Signup.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        fd = FirebaseDatabase.getInstance();
        dr = fd.getReference();


        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        dob.setText(materialDatePicker.getHeaderText());
                    }
                });

        dob.setOnFocusChangeListener((v, hasFocus) -> {

            if (hasFocus) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        register.setOnClickListener(v -> {

            try {

                final String fname = name.getText().toString().trim();
                final String fdob = dob.getText().toString().trim();
                final String fnumber = number.getText().toString().trim();
                final String femail = email.getText().toString().trim();
                final String fplatenumber = platenumber.getText().toString().trim();
                final String fusername = username.getText().toString().trim();
                final String fpassword = password.getText().toString().trim();
                final String fcpassword = cpassword.getText().toString().trim();

                if (TextUtils.isEmpty(fname) && TextUtils.isEmpty(fdob) && TextUtils.isEmpty(fnumber) && TextUtils.isEmpty(femail) && TextUtils.isEmpty(fplatenumber) && TextUtils.isEmpty(fusername) && TextUtils.isEmpty(fpassword)) {

                    lname.setError("Complete Name is required");
                    ldob.setError("Date of Birth is required");
                    lnumber.setError("Contact Number is required");
                    lemail.setError("Contact Number is required");
                    lplatenumber.setError("Plate Number is required");
                    lusername.setError("Username is required");
                    lpassword.setError("Password is required");
                    lcpassword.setError("Password is required");

                }

                else if (TextUtils.isEmpty(fname)) {

                    lname.setError("Complete Name is required");

                }

                else if (TextUtils.isEmpty(fdob)) {

                    ldob.setError("Date of Birth is required");

                }

                else if (TextUtils.isEmpty(fnumber)) {

                    lnumber.setError("Contact Number is required");

                }

                else if (TextUtils.isEmpty(femail)) {

                    lemail.setError("Email Address is required");

                }

                else if (TextUtils.isEmpty(fplatenumber)) {

                    lplatenumber.setError("Plate Number is required");

                }

                else if (TextUtils.isEmpty(fusername)) {

                    lusername.setError("Username is required");

                }

                else if (TextUtils.isEmpty(fpassword)) {

                    lpassword.setError("Password is required");

                }

                else if (fpassword.length() < 8) {

                    lpassword.setError("Password is too short, input at least 8 characters");

                }

                else if (TextUtils.isEmpty(fcpassword)) {

                    lcpassword.setError("Please confirm your password");

                }

                else if (!Patterns.EMAIL_ADDRESS.matcher(femail).matches()) {

                    lemail.setError("Please input a valid email address");
                }

                else if (!fpassword.equals(fcpassword)){

                    lpassword.setError("Passwords do not match!");
                    lcpassword.setError("Passwords do not match!");
                    password.setText(null);
                    cpassword.setText(null);
                }

                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                    View root = getLayoutInflater().inflate((R.layout.dialogconfirmsignup), null);
                    TextView names = root.findViewById(R.id.confirmcompletename);
                    TextView dobs = root.findViewById(R.id.confirmdateofbirth);
                    TextView numbers = root.findViewById(R.id.confirmcontact);
                    TextView emails = root.findViewById(R.id.confirmemail);
                    TextView plates = root.findViewById(R.id.confirmplatenumber);
                    TextView usernames = root.findViewById(R.id.confirmusername);
                    names.setText(fname);
                    dobs.setText(fdob);
                    numbers.setText(fnumber);
                    emails.setText(femail);
                    plates.setText(fplatenumber);
                    usernames.setText(fusername);
                    builder.setView(root);
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            auth.createUserWithEmailAndPassword(femail, fpassword)
                                    .addOnCompleteListener(Signup.this, task -> {

                                        if (!task.isSuccessful()) {

                                            Log.w("Registration", "signInWithCredential", task.getException());
                                            Snackbar sn = Snackbar.make(layout, "Firebase Signup Failed.", Snackbar.LENGTH_SHORT);
                                            sn.show();

                                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                                                Snackbar sn1 = Snackbar.make(layout, "Account already exists", Snackbar.LENGTH_SHORT);
                                                sn1.show();
                                                name.setText(null);
                                                dob.setText(null);
                                                number.setText(null);
                                                email.setText(null);
                                                platenumber.setText(null);
                                                username.setText(null);
                                                password.setText(null);

                                            } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {

                                                Snackbar sn2 = Snackbar.make(layout, "Please input a stronger password", Snackbar.LENGTH_SHORT);
                                                sn2.show();
                                                password.setText(null);

                                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                                Snackbar sn3 = Snackbar.make(layout, "Please input a valid email address", Snackbar.LENGTH_SHORT);
                                                sn3.show();
                                                username.setText(null);
                                                password.setText(null);

                                            }


                                        } else if (task.isSuccessful()) {

                                            createNewUser(fusername, fname, fdob, fnumber, femail, fplatenumber);
                                            auth.signOut();
                                            Snackbar sn4 = Snackbar.make(layout, "Registration Successful", Snackbar.LENGTH_SHORT);
                                            sn4.show();
                                            startActivity(new Intent(Signup.this, login.class));
                                            finish();

                                        }

                                    });
                        }
                    });

                    builder.setNegativeButton("Cancel", (dialog, which) -> {

                        dialog.dismiss();
                        name.setText(null);
                        dob.setText(null);
                        number.setText(null);
                        email.setText(null);
                        platenumber.setText(null);
                        username.setText(null);
                        password.setText(null);

                    });

                    AlertDialog alert = builder.create();
                    alert.show();


                }

            } catch (Exception e) {

                Log.e("On Register Button Clicked: ", e.getMessage(), e);

            }
        });
    }

    private void createNewUser(String name, String full, String date, String num, String mail, String plate) {

        User u = new User();
        u.setFullname(full);
        u.setdob(date);
        u.setContactnumber(num);
        u.setEmailaddress(mail);
        u.setPlatenumber(plate);
        u.setUsername(name);

        String[] parts = mail.split("@");
        databaseReference.child("Users").child(parts[0]).setValue(u);
        dr.child("Emails").child(name).setValue(mail);

    }

    @Override
    protected void onResume() {

        super.onResume();
        checkConnectivity();

    }

    @Override
    protected void onPause() {

        if (monitoringConnectivity) {

            final ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(connectivityCallback);
            monitoringConnectivity = false;

        }

        super.onPause();

    }

    private final ConnectivityManager.NetworkCallback connectivityCallback
            = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            isConnected = true;

            Snackbar snonavail = Snackbar.make(layout, "Connection Established!", Snackbar.LENGTH_SHORT);
            snonavail.show();

        }
        @Override
        public void onLost(Network network) {
            isConnected = false;

            Snackbar snonlost = Snackbar.make(layout, "Connection Lost!", Snackbar.LENGTH_INDEFINITE);
            snonlost.show();
        }
    };

    private void checkConnectivity() {

        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);

        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if (!isConnected) {

            Snackbar sncc = Snackbar.make(layout, "No Connection!", Snackbar.LENGTH_INDEFINITE);
            sncc.show();
            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .build(), connectivityCallback);
            monitoringConnectivity = true;
        }
        else {

            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .build(), connectivityCallback);
            monitoringConnectivity = true;

        }

    }




}