package com.shubham.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    CountryCodePicker countryCodePicker;
    TextView todaytotal,total,todayactive,active,todayrecovered,recovered,deaths,todaydeaths,filter;
    String country;
    Spinner spinner;
    String[] types={"cases","deaths","recovered","active"};
    private List<ModelClass> modelClassList;
    private List<ModelClass> modelClassList2;
    PieChart pieChart;
    private RecyclerView recyclerView;
    com.shubham.covidtracker.Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        countryCodePicker=findViewById(R.id.ccp);
        todayactive=findViewById(R.id.todayactive);
        active=findViewById(R.id.activecases);
        deaths=findViewById(R.id.deaths);
        todaydeaths=findViewById(R.id.todaydeaths);
        recovered=findViewById(R.id.recovered);
        todayrecovered=findViewById(R.id.todayrecovered);
        total=findViewById(R.id.totalcase);
        todaytotal=findViewById(R.id.todaytotal);
        pieChart=findViewById(R.id.piechart);
        spinner=findViewById(R.id.spinner);
        filter=findViewById(R.id.filter);
        recyclerView=findViewById(R.id.recyclerview);
        modelClassList=new ArrayList<>();
        modelClassList2=new ArrayList<>();

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,types);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


        ApiUtilities.getAPIInterface().getcountrydata().enqueue(new Callback<List<ModelClass>>() {
            @Override
            public void onResponse(Call<List<ModelClass>> call, Response<List<ModelClass>> response) {
                modelClassList2.addAll(response.body());

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<ModelClass>> call, Throwable t) {

            }
        });
        adapter=new Adapter(getApplicationContext(),modelClassList2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        countryCodePicker.setAutoDetectedCountry(true);
        country=countryCodePicker.getSelectedCountryName();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country=countryCodePicker.getSelectedCountryName();
                fetchdata();
            }
        });
        fetchdata();










    }

    private void fetchdata() {
        ApiUtilities.getAPIInterface().getcountrydata().enqueue(new Callback<List<ModelClass>>() {
            @Override
            public void onResponse(Call<List<ModelClass>> call, Response<List<ModelClass>> response) {
                modelClassList.addAll(response.body());
                for(int i=0;i<modelClassList.size();i++){
                    if(modelClassList.get(i).getCountry().equals(country)){
                        active.setText((modelClassList.get(i).getActive()));
                        todaydeaths.setText((modelClassList.get(i).getTodayDeaths()));
                        todayrecovered.setText((modelClassList.get(i).getTodayRecovered()));
                        todaytotal.setText((modelClassList.get(i).getTodayCases()));
                        total.setText((modelClassList.get(i).getCases()));
                        deaths.setText((modelClassList.get(i).getDeaths()));
                        recovered.setText((modelClassList.get(i).getRecovered()));



                        int active,total,recovered,deaths;
                        active=Integer.parseInt(modelClassList.get(i).getActive());
                        total=Integer.parseInt(modelClassList.get(i).getCases());
                        recovered=Integer.parseInt(modelClassList.get(i).getRecovered());
                        deaths=Integer.parseInt(modelClassList.get(i).getDeaths());

                        updateGraph(active,total,recovered,deaths);


                    }
                }
            }

            @Override
            public void onFailure(Call<List<ModelClass>> call, Throwable t) {

            }
        });



    }

    private void updateGraph(int active, int total, int recovered, int deaths) {
        pieChart.clearChart();
        pieChart.addPieSlice(new PieModel("Confirm",total,Color.parseColor("#FFB701")));
        pieChart.addPieSlice(new PieModel("Active",active,Color.parseColor("#FF4CAF50")));
        pieChart.addPieSlice(new PieModel("Recovered",recovered,Color.parseColor("#38ACCD")));
        pieChart.addPieSlice(new PieModel("Deaths",deaths,Color.parseColor("#F55C47")));
        pieChart.startAnimation();

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        int position = 0;
        String item=types[position];
        filter.setText(item);
        adapter.filter(item);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}