package com.example.sushitime.ui;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.sushitime.R;
import com.example.sushitime.adapters.HomeHorAdapter;
import com.example.sushitime.adapters.HomeVerAdapter;
import com.example.sushitime.adapters.UpdateVerticalRec;
import com.example.sushitime.models.HomeHorModel;
import com.example.sushitime.models.HomeVerModel;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements UpdateVerticalRec {

    RecyclerView homeHorizontalRec,homeVerticalRec;
    ArrayList<HomeHorModel> homeHorModelList;
    HomeHorAdapter homeHorAdapter;
    ///////////// vertical
    ArrayList<HomeVerModel>homeVerModelList;
    HomeVerAdapter homeVerAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View root  = inflater.inflate(R.layout.fragment_home, container, false);

        homeHorizontalRec= root.findViewById(R.id.home_hor_rec);
        homeVerticalRec=root.findViewById(R.id.home_ver_rec);
//////////////  Horizontal Recyclerview

        homeHorModelList=new ArrayList<>();
        homeHorModelList.add(new HomeHorModel(R.drawable.sushi,"Sushi"));
        homeHorModelList.add(new HomeHorModel(R.drawable.yakitori,"Nos_Yakitoris"));
        homeHorModelList.add(new HomeHorModel(R.drawable.thai,"thai"));

        homeHorAdapter= new HomeHorAdapter(this,getActivity(),homeHorModelList);
        homeHorizontalRec.setAdapter(homeHorAdapter);
        homeHorizontalRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        homeHorizontalRec.setHasFixedSize(true);
        homeHorizontalRec.setNestedScrollingEnabled(false);

//////////////  vertical Recyclerview

        homeVerModelList=new ArrayList<>();

        homeVerAdapter= new HomeVerAdapter(getActivity(),homeVerModelList);
        homeVerticalRec.setAdapter(homeVerAdapter);
        homeVerticalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));


        return root;

    }


    @Override
    public void callBack(int position, ArrayList<HomeVerModel> list) {
        homeVerAdapter=new HomeVerAdapter(getContext(),list);
        homeVerAdapter.notifyDataSetChanged();
        homeVerticalRec.setAdapter(homeVerAdapter);
    }
}
