package com.johnvincent.testlocator.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johnvincent.testlocator.R;
import com.johnvincent.testlocator.model.JsonResponse;


import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.MyHolderView> {

    private List<JsonResponse> responses;

    public NearbyAdapter(List<JsonResponse> jsonResponseList){
        responses = jsonResponseList;
    }

    @NonNull
    @Override
    public MyHolderView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.nearby_item, viewGroup, false);

        return new MyHolderView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolderView myHolderView, int i) {
        JsonResponse jsonResponse = responses.get(i);
        myHolderView.title.setText(jsonResponse.getmTitle());
        int addressSize = jsonResponse.mAddress.length;
        String[] add = jsonResponse.mAddress;
        for (int j = 0; j < addressSize ; j++) {
            myHolderView.address.append(add[j] + " ");
        }
        myHolderView.telephone.setText(jsonResponse.getmTelephone());
        myHolderView.distance.setText(jsonResponse.getmDistance()+" m");

    }

    @Override
    public int getItemCount() {
        return responses.size() > 0 ? responses.size() : 0;
    }

    public class  MyHolderView extends RecyclerView.ViewHolder{
        TextView title;
        TextView address;
        TextView telephone;
        TextView distance;
        public MyHolderView(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById( R.id.tv_title);
            address = (TextView) itemView.findViewById( R.id.tv_address);
            telephone = (TextView) itemView.findViewById( R.id.tv_telephone);
            distance = (TextView) itemView.findViewById( R.id.tv_distance);
        }
    }
}
