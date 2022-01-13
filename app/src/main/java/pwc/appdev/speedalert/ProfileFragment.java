package pwc.appdev.speedalert;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private CircleImageView circleImageView, circleImageViewedit;
    private StorageReference up;
    private DatabaseReference ref, ref1, ref2;
    private FirebaseAuth auth;
    private String email = " ";
    private String contactno = " ";
    private String fullname = " ";
    private String plateno = " ";
    private String urladd = " ";
    private String updatedname = " ";
    private String updatedcontactno = " ";
    private Uri mImageUri;
    private String[] parts = new String[2];
    private UploadTask uploadTask;
    private OnFragmentInteractionListener mListener;
    private ConstraintLayout inner, outer;
    private TextView tvfullname, tvcontactnumber, tvemail, tvplatenumber;
    private Button edit, save;
    private TextInputEditText editfullname, editcontactnumner;
    private static final int PICK_IMAGE_REQUEST = 1;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(String param1, String param2) {

        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        auth = FirebaseAuth.getInstance();
        FirebaseUser users = auth.getCurrentUser();
        if(users != null){

            email = users.getEmail();
            String[] part = email.split("@");
            parts[0] = part[0];
        }

        inner = view.findViewById(R.id.constraintinner);
        outer = view.findViewById(R.id.constraintouter);
        circleImageView = view.findViewById(R.id.mapprofilepic);
        circleImageViewedit = view.findViewById(R.id.mapprofilepicupdate);
        tvfullname = view.findViewById(R.id.mapfragment_fullname);
        tvcontactnumber = view.findViewById(R.id.mapfragment_contactnumber);
        tvemail = view.findViewById(R.id.mapfragment_email);
        tvplatenumber = view.findViewById(R.id.mapfragment_platenumber);
        edit = view.findViewById(R.id.mapfragmentbuttonedit);
        save = view.findViewById(R.id.mapfragmentbuttonsave);
        editfullname = view.findViewById(R.id.mapfragment_fullnameedit);
        editcontactnumner = view.findViewById(R.id.mapfragment_contactnumberedit);
        up = FirebaseStorage.getInstance().getReference("ProfilePictures");
        ref = FirebaseDatabase.getInstance().getReference("Users");
        ref1 = FirebaseDatabase.getInstance().getReference("Users");
        ref2 = FirebaseDatabase.getInstance().getReference("Users");

        getName();

        edit.setOnClickListener(v -> {

            outer.setVisibility(View.GONE);
            inner.setVisibility(View.VISIBLE);
            editfullname.setText(fullname);
            editcontactnumner.setText(contactno);
            getUrl1();

        });

        circleImageViewedit.setOnClickListener(v -> {

            openFileChooser();

        });

        save.setOnClickListener(v -> {

            try {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Please confirm action!");
                builder.setMessage("Are you sure you want to commit changes?");
                builder.setIcon(R.drawable.speedalert);
                builder.setPositiveButton("Yes", (dialog, which) -> {

                    updatedname = editfullname.getText().toString();
                    setUserFullName(updatedname);
                    tvfullname.setText(updatedname);
                    updatedcontactno = editcontactnumner.getText().toString();
                    setContactno(updatedcontactno);
                    tvcontactnumber.setText(updatedcontactno);
                    tvemail.setText(email);
                    getUrl();
                    inner.setVisibility(View.GONE);
                    outer.setVisibility(View.VISIBLE);
                    startActivity(new Intent(getActivity(), main.class));

                });
                builder.setNegativeButton("No", (dialog, which) -> {

                    dialog.dismiss();
                    Snackbar sn = Snackbar.make(getView(), "Cancelled", Snackbar.LENGTH_SHORT);
                    sn.show();

                });

                AlertDialog alert = builder.create();
                alert.show();

            }

            catch (Exception e) {

                Log.e("onClickSave", e.getMessage(), e);

            }

        });
    }

    private void setUserFullName(String newname){

        try{

            ref1.child(parts[0]).child("fullname").setValue(newname);
        }

        catch(Exception e){

            Log.e("SetUserFName", e.getMessage(), e);
        }
    }

    private void setContactno(String newcontact){

        try{

            ref2.child(parts[0]).child("contactnumber").setValue(newcontact);
        }

        catch(Exception e){

            Log.e("SetUserFContact", e.getMessage(), e);
        }
    }

    private void getName(){

        try{

            DatabaseReference getname = FirebaseDatabase.getInstance().getReference().child("Users");
            getname.child(parts[0]).child("fullname").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot != null){

                        try{

                            String names = dataSnapshot.getValue(String.class);
                            if(names!=null){

                                fullname = names;
                                getContactNo();
                            }
                            else{

                                Toast.makeText(getActivity(), "Name is not Found!", Toast.LENGTH_SHORT).show();
                            }

                        }

                        catch(Exception e){

                            Log.e("GetName", e.getMessage(), e);

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        catch(Exception e){

            Log.e("getName", e.getMessage(), e);

        }


    }

    private void getContactNo(){

        try{

            DatabaseReference getemail = FirebaseDatabase.getInstance().getReference().child("Users");
            getemail.child(parts[0]).child("contactnumber").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot != null){

                        try{

                            String cn = dataSnapshot.getValue(String.class);
                            if(cn!=null){

                                contactno = cn;
                                tvfullname.setText(fullname);
                                tvemail.setText(email);
                                tvcontactnumber.setText(contactno);
                                getPlateNo();

                            }
                            else{

                                Log.d("onGetEmail", "No Email Retrieved");

                            }


                        }

                        catch(Exception e){

                            Log.e("Emails", e.getMessage(), e);

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        catch(Exception e){

            Log.e("onRefresh", e.getMessage(), e);

        }
    }

    private void getPlateNo(){

        try{

            DatabaseReference getemail = FirebaseDatabase.getInstance().getReference().child("Users");
            getemail.child(parts[0]).child("platenumber").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot != null){

                        try{

                            String pn = dataSnapshot.getValue(String.class);
                            if(pn!=null){

                                plateno = pn;
                                tvplatenumber.setText(plateno);
                                getUrl();

                            }
                            else{

                                Log.d("onGetEmail", "No Email Retrieved");

                            }


                        }

                        catch(Exception e){

                            Log.e("Emails", e.getMessage(), e);

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        catch(Exception e){

            Log.e("onRefresh", e.getMessage(), e);

        }
    }

    private void getUrl(){

        try{

            DatabaseReference getuser = FirebaseDatabase.getInstance().getReference().child("Users").child(parts[0]);
            getuser.child("ProfilePicture").child("imageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if( dataSnapshot != null){

                        try{

                            String url = dataSnapshot.getValue(String.class);
                            if(url!=null){

                                urladd = url;
                                System.out.println(urladd);
                                Glide.with(getActivity()).load(urladd).into(circleImageView);

                            }

                            else{

                                Toast.makeText(getActivity(), "URL is EMPTY", Toast.LENGTH_SHORT).show();
                            }
                        }

                        catch(Exception e){

                            Log.e("No URLS found", e.getMessage(), e);

                        }


                    }
                    else {

                        Log.d("No URLS found", "No URLS found");

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {



                }
            });
        }

        catch(Exception e){

            Log.e("onGetURL", e.getMessage(), e);

        }

    }

    private void getUrl1(){

        try{

            DatabaseReference getuser = FirebaseDatabase.getInstance().getReference().child("Users").child(parts[0]);
            getuser.child("ProfilePicture").child("imageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if( dataSnapshot != null){

                        try{

                            String url = dataSnapshot.getValue(String.class);
                            if(url!=null){

                                urladd = url;
                                System.out.println(urladd);
                                Glide.with(getActivity()).load(urladd).into(circleImageViewedit);

                            }

                            else{

                                Toast.makeText(getActivity(), "URL is EMPTY", Toast.LENGTH_SHORT).show();
                            }
                        }

                        catch(Exception e){

                            Log.e("No URLS found", e.getMessage(), e);

                        }


                    }
                    else {

                        Log.d("No URLS found", "No URLS found");

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {



                }
            });
        }

        catch(Exception e){

            Log.e("onGetURL", e.getMessage(), e);

        }

    }

    private void openFileChooser() {

        try{

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE_REQUEST);
        }

        catch(Exception e){

            Log.e("onOpenFileChoose", e.getMessage(), e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {
                mImageUri = data.getData();
                circleImageViewedit.setImageURI(mImageUri);

            }
            if (uploadTask != null && uploadTask.isInProgress()) {

                Snackbar sn = Snackbar.make(getView(), "Upload in Progress", Snackbar.LENGTH_LONG);
                sn.show();

            }

            else {

                uploadFile();

            }
        }

        catch(Exception e){

            Log.e("ActivityResult", e.getMessage(), e);

        }

    }

    private void uploadFile(){

        try{

            if(mImageUri != null){

                final StorageReference fileReference = up.child(parts[0] + "." + getFileExtension(mImageUri));

                uploadTask = fileReference.putFile(mImageUri);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {

                            throw Objects.requireNonNull(task.getException());
                        }

                        return fileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downloadUri = task.getResult();
                            String miUrlOk = downloadUri.toString();

                            UploadImage upload = new UploadImage(parts[0], miUrlOk);
                            ref.child(parts[0]).child("ProfilePicture").setValue(upload);
                            getUrl();

                        }

                        else {

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
            else {

                Toast.makeText(getActivity(), "Image is not Found!", Toast.LENGTH_SHORT).show();

            }
        }

        catch(Exception e){

            Log.e("UploadFile", e.getMessage(), e);
        }

    }

    public String getFileExtension(Uri uri) {

        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        System.out.println(mime.getExtensionFromMimeType(cR.getType(uri)));
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    public void onButtonPressed(Uri uri) {

        if (mListener != null) {

            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }

        else {

            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;

    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
