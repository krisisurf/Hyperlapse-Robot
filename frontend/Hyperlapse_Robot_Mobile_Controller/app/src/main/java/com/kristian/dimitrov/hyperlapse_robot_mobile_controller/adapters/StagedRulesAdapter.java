package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.RuleEntity;

import java.util.List;

public class StagedRulesAdapter extends RecyclerView.Adapter<StagedRulesAdapter.ViewHolder> {

    private final Context context;
    private final List<RuleEntity> ruleEntities;

    public StagedRulesAdapter(Context context, List<RuleEntity> ruleEntities) {
        this.context = context;
        this.ruleEntities = ruleEntities;
    }

    @NonNull
    @Override
    public StagedRulesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rule_list_item, parent, false);

        return new StagedRulesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StagedRulesAdapter.ViewHolder holder, int position) {
        RuleEntity ruleEntity = ruleEntities.get(position);

        String ruleNumber = String.valueOf(position + 1);
        String description = ruleEntity.toString();
        String executionTime = ruleEntity.getExecutionTime() + " sec";

        holder.tvRuleNumber.setText(ruleNumber);
        holder.tvDescription.setText(description);
        holder.tvExecutionTime.setText(executionTime);
    }

    @Override
    public int getItemCount() {
        return ruleEntities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvRuleNumber;
        private TextView tvDescription;
        private TextView tvExecutionTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRuleNumber = itemView.findViewById(R.id.tvRuleNumber);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvExecutionTime = itemView.findViewById(R.id.tvExecutionTime);
        }
    }
}
