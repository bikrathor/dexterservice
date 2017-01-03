package com.ccec.dexterservice;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccec.dexterservice.managers.AppData;
import com.ccec.dexterservice.managers.QueryviewAdapter;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewOrderDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private TextView location, contact, name, company;
    private TextView locationD, contactD, nameD, companyD, locMoreD, carMoreD;
    private Object obj;
    private LinearLayout lin;
    private Object custobj;
    private GoogleMap mMap;
    private LatLng sydney;
    private ImageView navImg;
    private TextView CarCametv, CarWorkStartedtv, CarWorkCompletedtv, CarWorkPricePaidtv, CarWorkRequestCompletedtv;
    private SwitchCompat CarCamesv, CarWorkStartedsv, CarWorkCompletedsv, CarWorkPricePaidsv, CarWorkRequestCompletedsv;
    private String ProcessFlowUpdateText, key;
    private DatabaseReference firebaseprocessflowref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            obj = AppData.currentVeh;
            custobj = AppData.currentVehCust;

            appBarLayout.setTitle((String) ((HashMap) obj).get("key"));
        }

        firebaseprocessflowref = FirebaseDatabase.getInstance().getReference("processFlow/" + (String) ((HashMap) AppData.currentVeh).get("key"));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("geofire");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.getLocation(((String) ((HashMap) obj).get("issuedBy")), new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    sydney = new LatLng(location.latitude, location.longitude);
                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                } else {
                    System.out.println(String.format("There is no location for key %s in GeoFire", key));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("There was an error getting the GeoFire location: " + databaseError);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.neworder_detail, container, false);

        name = (TextView) view.findViewById(R.id.fullNameTitle);
        location = (TextView) view.findViewById(R.id.skypeNameTitle);
        company = (TextView) view.findViewById(R.id.companyNameTitle);

        nameD = (TextView) view.findViewById(R.id.fullNameDetail);
        locationD = (TextView) view.findViewById(R.id.skypeNameDetail);
        companyD = (TextView) view.findViewById(R.id.companyNameDetail);

        locMoreD = (TextView) view.findViewById(R.id.fullNameMore);
        SpannableString content = new SpannableString(locMoreD.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        locMoreD.setText(content);

        carMoreD = (TextView) view.findViewById(R.id.skypeNameMore);
        SpannableString content2 = new SpannableString(carMoreD.getText());
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        carMoreD.setText(content2);

        locMoreD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + sydney.latitude + ", " + sydney.longitude));
                startActivity(intent);
            }
        });

        carMoreD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show details in dialog
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialoglayout = inflater.inflate(R.layout.custom_show_cardetails, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setView(dialoglayout);


                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        lin = (LinearLayout) view.findViewById(R.id.linProf11);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/Customer/" + (String) ((HashMap) AppData.currentVeh).get("issuedBy"));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> itemMap = (HashMap<String, Object>) dataSnapshot.getValue();
                nameD.setText(((String) itemMap.get("name")) + "\n"
                        + ((String) itemMap.get("contact")) + "\n"
                        + ((String) itemMap.get("location")));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        locationD.setText((String) ((HashMap) custobj).get("make") + " " + (String) ((HashMap) custobj).get("model"));
        companyD.setText((String) ((HashMap) obj).get("openTime"));

        CarCametv = (TextView) view.findViewById(R.id.carcamestep);
        CarWorkStartedtv = (TextView) view.findViewById(R.id.carworkstartedstep);
        CarWorkCompletedtv = (TextView) view.findViewById(R.id.carworkcompletedstep);
        CarWorkPricePaidtv = (TextView) view.findViewById(R.id.workpricepaidstep);
        CarWorkRequestCompletedtv = (TextView) view.findViewById(R.id.workrequestcompletestep);

        CarCamesv = (SwitchCompat) view.findViewById(R.id.carcameswitchButton);
        CarWorkStartedsv = (SwitchCompat) view.findViewById(R.id.carworkstartedswitchButton);
        CarWorkCompletedsv = (SwitchCompat) view.findViewById(R.id.carworkcompletedswitchButton);
        CarWorkPricePaidsv = (SwitchCompat) view.findViewById(R.id.carworkpricepaidswitchButton);
        CarWorkRequestCompletedsv = (SwitchCompat) view.findViewById(R.id.carworkrequestcompleteswitchButton);

        CarCamesv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ProcessFlowUpdateText = CarCametv.getText().toString();
                    key = firebaseprocessflowref.push().getKey();
                    firebaseprocessflowref.child(key).setValue(ProcessFlowUpdateText);
                }

            }
        });
        CarWorkStartedsv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (CarCamesv.isChecked()) {
                        ProcessFlowUpdateText = CarWorkStartedtv.getText().toString();
                        key = firebaseprocessflowref.push().getKey();
                        firebaseprocessflowref.child(key).setValue(ProcessFlowUpdateText);
                    }
                    if (!CarCamesv.isChecked()) {
                        Toast.makeText(getContext(), "Please make sure that Car Came is checked before this action.", Toast.LENGTH_SHORT).show();
                        CarWorkStartedsv.setChecked(false);
                    }

                }
            }
        });

        CarWorkCompletedsv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (CarCamesv.isChecked() && CarWorkStartedsv.isChecked()) {
                        ProcessFlowUpdateText = CarWorkCompletedtv.getText().toString();
                        key = firebaseprocessflowref.push().getKey();
                        firebaseprocessflowref.child(key).setValue(ProcessFlowUpdateText);
                    }
                    if (!CarCamesv.isChecked()) {
                        Toast.makeText(getContext(), "Please make sure that Car Came is checked before this action.", Toast.LENGTH_SHORT).show();
                        CarWorkCompletedsv.setChecked(false);

                    }
                    if (!CarWorkStartedsv.isChecked()) {
                        Toast.makeText(getContext(), "Please make sure that Car Came and Work Started are checked before this action.", Toast.LENGTH_SHORT).show();
                        CarWorkCompletedsv.setChecked(false);
                    }

                }
            }
        });


        CarWorkPricePaidsv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (CarCamesv.isChecked() && CarWorkStartedsv.isChecked() && CarWorkCompletedsv.isChecked()) {
                        ProcessFlowUpdateText = CarWorkPricePaidtv.getText().toString();
                        key = firebaseprocessflowref.push().getKey();
                        firebaseprocessflowref.child(key).setValue(ProcessFlowUpdateText);
                    }
                    if (!CarCamesv.isChecked()) {
                        Toast.makeText(getContext(), "Please make sure that Car Came is checked before this action.", Toast.LENGTH_SHORT).show();
                        CarWorkPricePaidsv.setChecked(false);
                    }
                    if (!CarWorkStartedsv.isChecked()) {
                        Toast.makeText(getContext(), "Please make sure that Car Came and Work Started are checked before this action.", Toast.LENGTH_SHORT).show();
                        CarWorkPricePaidsv.setChecked(false);
                    }
                    if (!CarWorkCompletedsv.isChecked()) {
                        Toast.makeText(getContext(), "Please make sure that Car Came,Work Started and Completed are checked before this action.", Toast.LENGTH_SHORT).show();
                        CarWorkPricePaidsv.setChecked(false);
                    }

                }
            }
        });
        CarWorkRequestCompletedsv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (CarCamesv.isChecked() && CarWorkStartedsv.isChecked() && CarWorkCompletedsv.isChecked() && CarWorkPricePaidsv.isChecked()) {
                        ProcessFlowUpdateText = CarWorkRequestCompletedtv.getText().toString();
                        key = firebaseprocessflowref.push().getKey();
                        firebaseprocessflowref.child(key).setValue(ProcessFlowUpdateText);
                    }
                    if (!CarCamesv.isChecked()) {
                        Toast.makeText(getContext(), "Please make sure that Car Came is checked before this action.", Toast.LENGTH_SHORT).show();
                        CarWorkPricePaidsv.setChecked(false);
                    }
                    if (!CarWorkStartedsv.isChecked()) {
                        Toast.makeText(getContext(), "Please make sure that Car Came and Work Started are checked before this action.", Toast.LENGTH_SHORT).show();
                        CarWorkPricePaidsv.setChecked(false);
                    }
                    if (!CarWorkCompletedsv.isChecked()) {
                        Toast.makeText(getContext(), "Please make sure that Car Came,Work Started and Completed are checked before this action.", Toast.LENGTH_SHORT).show();
                        CarWorkPricePaidsv.setChecked(false);
                    }
                    if (!CarWorkPricePaidsv.isChecked()) {
                        Toast.makeText(getContext(), "Please make sure that Car Came,Work Started,Completed & Paid are checked before this action.", Toast.LENGTH_SHORT).show();
                        CarWorkPricePaidsv.setChecked(false);

                    }

                }
            }
        });

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.queriesList);
        final LinearLayout lrecyclerView = (LinearLayout) view.findViewById(R.id.queries_steps);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("queries");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    try {
                        List<String> itemMap = (ArrayList) dataSnapshot.getValue();
                        String temp = (String) ((HashMap) AppData.currentVeh).get("queries");

                        ArrayList<String> itemMap2 = new ArrayList<String>();
                        for (int i = 0; i < itemMap.size(); i++) {
                            if (temp.charAt(i) == '1')
                                itemMap2.add(itemMap.get(i));
                        }

                        if (itemMap2.size() > 0) {
                            RecyclerView.Adapter adapter = new QueryviewAdapter(itemMap2);
                            recyclerView.setAdapter(adapter);
                        } else
                            lrecyclerView.setVisibility(View.GONE);
                    } catch (Exception e) {
                        lrecyclerView.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference2.keepSynced(true);

//        Date d = new Date();
//        d.setDate(Integer.parseInt((String) ((HashMap) obj).get("approxDate")));
//        int mon = 0;
//        switch ((String) ((HashMap) obj).get("approxMonth")) {
//            case "Jan":
//                mon = 0;
//                break;
//            case "Feb":
//                mon = 1;
//                break;
//            case "Mar":
//                mon = 2;
//                break;
//            case "Apr":
//                mon = 3;
//                break;
//            case "May":
//                mon = 4;
//                break;
//            case "Jun":
//                mon = 5;
//                break;
//            case "Jul":
//                mon = 6;
//                break;
//            case "Aug":
//                mon = 7;
//                break;
//            case "Sep":
//                mon = 8;
//                break;
//            case "Oct":
//                mon = 9;
//                break;
//            case "Nov":
//                mon = 10;
//                break;
//            case "Dec":
//                mon = 11;
//                break;
//        }
//        d.setMonth(mon);
//
//        SimpleDateFormat format = new SimpleDateFormat("d");
//        String date = format.format(d);
//        if (date.endsWith("1") && !date.endsWith("11"))
//            format = new SimpleDateFormat("EE MMM d'st', yyyy");
//        else if (date.endsWith("2") && !date.endsWith("12"))
//            format = new SimpleDateFormat("EE MMM d'nd', yyyy");
//        else if (date.endsWith("3") && !date.endsWith("13"))
//            format = new SimpleDateFormat("EE MMM d'rd', yyyy");
//        else
//            format = new SimpleDateFormat("EE MMM d'th', yyyy");
//        String yourDate = format.format(d);
//
//        contactD.setText(yourDate);

        return view;
    }
}
