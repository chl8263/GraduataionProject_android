package com.example.note.seoulddok.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.note.seoulddok.Adapter.HistoryRecyclerAdapter;
import com.example.note.seoulddok.Contact;
import com.example.note.seoulddok.Model.ExChild;
import com.example.note.seoulddok.Model.ExParent;
import com.example.note.seoulddok.Model.ExpandableItem;
import com.example.note.seoulddok.Model.RecvData;
import com.example.note.seoulddok.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by gyun_home on 2018-03-18.
 */

public class SecondFragment extends Fragment {
    public  final int HEADER = 0;
    public  final int CHILD = 1;
    private RecyclerView recyclerView;
    ArrayList<HistoryRecyclerAdapter.Item> data = new ArrayList<>();
    private HistoryRecyclerAdapter historyRecyclerAdapter ;
    public SecondFragment() {

    }
    public static SecondFragment getInstance(){
        Bundle args = new Bundle();
        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.secondfragment,container,false);
        setRecyclercView();
        historyRecyclerAdapter = new HistoryRecyclerAdapter(data,view.getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(historyRecyclerAdapter);
        return view;
    }



    public void setRecyclercView(){
        ArrayList<RecvData> recvData = Contact.dbManager.getRecvData();

        ArrayList<String> header = new ArrayList<String>();
        HashMap<String, String> checkTitle = new HashMap<String, String>();

        int check = -1;

        /*for(int i=0;i<recvData.size();i++){
            if(!checkTitle.containsKey(recvData.get(i).getDate())){
                String title = recvData.get(i).getDate();
                checkTitle.put(title,title);
                data.add(new HistoryRecyclerAdapter.Item(HEADER,title));
                check++;
                data.get(check).invisibleChildren = new ArrayList<>();
                data.get(check).invisibleChildren.add(new HistoryRecyclerAdapter.Item(CHILD,recvData.get(i).getTime(),recvData.get(i).getMessage()));
            }else{
                data.get(check).invisibleChildren.add(new HistoryRecyclerAdapter.Item(CHILD,recvData.get(i).getTime(),recvData.get(i).getMessage()));
            }

        }*/
        for(int i=recvData.size()-1;i>0;i--){
            if(!checkTitle.containsKey(recvData.get(i).getDate())){
                String title = recvData.get(i).getDate();
                checkTitle.put(title,title);
                data.add(new HistoryRecyclerAdapter.Item(HEADER,title));
                check++;
                data.get(check).invisibleChildren = new ArrayList<>();
                data.get(check).invisibleChildren.add(new HistoryRecyclerAdapter.Item(CHILD,recvData.get(i).getTime(),recvData.get(i).getMessage(),recvData.get(i).getDistinction(),recvData.get(i).getLatlang()));
            }else{
                data.get(check).invisibleChildren.add(new HistoryRecyclerAdapter.Item(CHILD,recvData.get(i).getTime(),recvData.get(i).getMessage(),recvData.get(i).getDistinction(),recvData.get(i).getLatlang()));
            }

        }

    }

    public void addRecyclerView(){
        AAA aaa = new AAA();
        aaa.execute();
    }
    private class AAA extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            setRecyclercView();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            historyRecyclerAdapter.notifyDataChange(data);
        }
    }
}
