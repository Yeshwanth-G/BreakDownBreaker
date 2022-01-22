package com.example.googlesignin;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
    ArrayList<helperclass>a;
    CardView c;
    clickinterface ci;
Context context;
    public adapter(Context context, ArrayList<helperclass> a,clickinterface ci) {
        this.a = a;
        this.context=context;
        this.ci=ci;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.card,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(adapter.ViewHolder holder, int position){
        helperclass h=a.get(position);
        holder.salary.setText(h.getSalary());
        holder.jobttitle.setText(h.getTitle());
        holder.location.setText(h.getLocation());
        holder.company.setText(h.getCompany());
    }
    @Override
    public int getItemCount() {
        return a.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView company;
        TextView location;
        public TextView getLocation() {
            return location;
        }
        TextView jobttitle;
        TextView salary;
        public TextView getSalary() {
            return salary;
        }
        public ViewHolder(View view) {
            super(view);
            c=(CardView)view;
            company=(TextView) view.findViewById(R.id.company);
            location=(TextView) view.findViewById(R.id.location);
            jobttitle=(TextView)view.findViewById(R.id.jobtitle);
            salary=(TextView) view.findViewById(R.id.salary);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ci.onItemClick(getAdapterPosition());
                }
            });
        }

        public TextView getCompany() {
            return company;
        }
        public TextView getJobttitle() {
            return jobttitle;
        }
    }
}
