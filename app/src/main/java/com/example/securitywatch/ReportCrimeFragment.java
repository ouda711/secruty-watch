package com.example.securitywatch;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportCrimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportCrimeFragment extends Fragment {
    public static final String KEY_User_Document1 = "doc1";
    ImageView IDProf;
    Button Upload_Btn;
    private DatabaseReference mDatabase;
    TextInputLayout crimeLocation, crimeTitle, crimeDescription;
    Button report;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String uniqueID = UUID.randomUUID().toString();

    private String Document_img1="";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USERNAME = "username";
    private static final String ARG_PARAM1 = "latitude";
    private static final String ARG_PARAM2 = "longitude";

    // TODO: Rename and change types of parameters
    private String username;
    private String latitude;
    private String longitude;

    public ReportCrimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportCrimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportCrimeFragment newInstance(String name, String param1, String param2) {
        ReportCrimeFragment fragment = new ReportCrimeFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, name);
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
            latitude = getArguments().getString(ARG_PARAM1);
            longitude = getArguments().getString(ARG_PARAM2);
        }

        System.out.println("username:"+username);
        System.out.println("latitude:"+latitude);
        System.out.println("password:"+longitude);
        geolocateValues(Double.parseDouble(latitude), Double.parseDouble(longitude));


        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        FloatingActionButton backFab = (FloatingActionButton) getView().findViewById(R.id.back_fab);
//        backFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), MainMapActivity.class);
//                startActivity(intent);
//            }
//        });
        View view = inflater.inflate(R.layout.fragment_report_crime, container, false);
        crimeLocation = (TextInputLayout) view.findViewById(R.id.crimeLocation);
        crimeTitle = view.findViewById(R.id.crimeTitle);
        crimeDescription = view.findViewById(R.id.crimeDescription);
        report = view.findViewById(R.id.report_crime);

        String locationale = geolocateValuesProvided(Double.parseDouble(latitude), Double.parseDouble(longitude));
        System.out.println("The address is :"+locationale.length());
        crimeLocation.getEditText().setText(locationale);

        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&key=AIzaSyAEWcMQQo6bfe27ih75v2a-8zp1gdUWZfg";
        //String url = "https://reqres.in/api/users?page=2";
        List<String> jsonResponses = new ArrayList<>();
        final String[] myAddress = {""};

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    JSONObject zero = jsonArray.getJSONObject(0);
                    String address_components = zero.getString("formatted_address");
                    System.out.println(zero.toString(4));
                    System.out.println("Final String Address:");
                    crimeLocation.getEditText().setText(zero.getString("formatted_address").toString());
                    crimeLocation.setEnabled(false);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String formatted_address = jsonObject.getString("formatted_address");
                        System.out.println(formatted_address.length());
                        jsonResponses.add(formatted_address);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });

        requestQueue.add(jsonObjectRequest);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = crimeLocation.getEditText().getText().toString();
                String title = crimeTitle.getEditText().getText().toString();
                String description = crimeDescription.getEditText().getText().toString();
                String reported_by = username;
                String respondent = "nobody";
                String respondentLocation = "nowhere";
                String respondentStatus = "nothing";
                String stat = "pending";

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("crimes");

                CrimeReportHelper crimeReportHelper = new CrimeReportHelper(location, title, description, reported_by, respondent, respondentLocation, respondentStatus);
                reference.child(uniqueID).setValue(crimeReportHelper);

                crimeTitle.getEditText().setText("");
                crimeDescription.getEditText().setText("");
            }
        });

        return view;
    }

    private void geolocateValues(double latitude, double longitude){
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&key=AIzaSyAEWcMQQo6bfe27ih75v2a-8zp1gdUWZfg";
        //String url = "https://reqres.in/api/users?page=2";
        List<String> jsonResponses = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    //System.out.println(jsonArray);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String formatted_address = jsonObject.getString("formatted_address");
                        System.out.println(formatted_address);
                        jsonResponses.add(formatted_address);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });

        requestQueue.add(jsonObjectRequest);
    }

    private String geolocateValuesProvided(double latitude, double longitude){
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&key=AIzaSyAEWcMQQo6bfe27ih75v2a-8zp1gdUWZfg";
        //String url = "https://reqres.in/api/users?page=2";
        List<String> jsonResponses = new ArrayList<>();
        final String[] myAddress = {""};

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    JSONObject zero = jsonArray.getJSONObject(0);
                    String address_components = zero.getString("formatted_address");
                    System.out.println(zero.toString(4));
                    System.out.println("Final String Address:");
                    myAddress[0] = (zero.getString("formatted_address").toString());
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String formatted_address = jsonObject.getString("formatted_address");
                        System.out.println(formatted_address.length());
                        jsonResponses.add(formatted_address);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });

        requestQueue.add(jsonObjectRequest);
        System.out.println("Lethal:"+myAddress[0]);
        return myAddress[0];
    }
}