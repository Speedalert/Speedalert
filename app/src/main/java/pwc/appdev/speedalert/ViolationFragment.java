package pwc.appdev.speedalert;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViolationFragment extends Fragment {

    private RecyclerView violationRequest;
    private DatabaseReference viol;
    private FirebaseAuth auth;
    private String currentUserID;

    public ViolationFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_violations, container, false);
        auth = FirebaseAuth.getInstance();
        FirebaseUser users = auth.getCurrentUser();
        currentUserID = users.getUid();
        System.out.println(currentUserID);
        viol = FirebaseDatabase.getInstance().getReference().child("Violation").child(currentUserID);
        violationRequest = (RecyclerView) v.findViewById(R.id.violation_RecyclerView);
        violationRequest.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    @Override
    public void onStart() {

        super.onStart();

        try{

            FirebaseRecyclerOptions<RetrieveViolations> options =
                    new FirebaseRecyclerOptions.Builder<RetrieveViolations>()
                            .setQuery(viol, snapshot -> new RetrieveViolations(
                                    snapshot.child("id").getValue().toString(),
                                    snapshot.child("address").getValue().toString(),
                                    snapshot.child("datetime").getValue().toString(),
                                    snapshot.child("violation").getValue().toString()))
                            .build();

            FirebaseRecyclerAdapter<RetrieveViolations, RequestsViewHolder> adapter = new FirebaseRecyclerAdapter<RetrieveViolations, RequestsViewHolder>(options) {


                @Override
                protected void onBindViewHolder(@NonNull RequestsViewHolder holder, int position, @NonNull RetrieveViolations model) {

                    holder.id.setText(model.getViolationid());
                    holder.address.setText(model.getAddress());
                    holder.datetime.setText(model.getDatetime());
                    holder.violation.setText(model.getViolation());
                }

                @NonNull
                @Override
                public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.violatiolist, parent, false);
                    RequestsViewHolder holder = new RequestsViewHolder(view);
                    return holder;
                }
            };

            violationRequest.setAdapter(adapter);
            adapter.startListening();
        }

        catch(Exception e){

            Log.e("onStart Violation Fragment", e.getMessage(), e);
        }
    }

    public static class RequestsViewHolder extends RecyclerView.ViewHolder
    {
        TextView id, address, datetime, violation;

        public RequestsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            id = itemView.findViewById(R.id.vl_violationID);
            address = itemView.findViewById(R.id.vl_address);
            datetime = itemView.findViewById(R.id.vl_datetime);
            violation = itemView.findViewById(R.id.vl_violation);
        }
    }
}
