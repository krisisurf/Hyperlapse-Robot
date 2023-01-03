package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.R;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.RuleEntity;

import java.util.List;

public class StagedRulesAdapter extends RecyclerView.Adapter<StagedRulesAdapter.ViewHolder> {

    private final Context context;
    private final List<RuleEntity> ruleEntities;

    private OnEditClickListener onEditClickListener;

    private static StringBuilder descriptionBuilder;

    static {
        descriptionBuilder = new StringBuilder();
    }

    public interface OnEditClickListener {
        void onButtonEditClick(RuleEntity ruleEntity);
    }

    public StagedRulesAdapter(Context context, List<RuleEntity> ruleEntities) {
        this.context = context;
        this.ruleEntities = ruleEntities;
    }

    @NonNull
    @Override
    public StagedRulesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rule_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StagedRulesAdapter.ViewHolder holder, int position) {
        RuleEntity ruleEntity = ruleEntities.get(position);

        String ruleNumber = String.valueOf(position + 1);


        String description = descriptionBuilder
                .append("Left motor: ").append(ruleEntity.getLeftMotor().getMeasurementValue()).append("cm")
                .append(" / ").append((int) ruleEntity.getLeftMotor().getExecutionTime()).append("sec")

                .append("\nRight motor: ").append(ruleEntity.getRightMotor().getMeasurementValue()).append("cm")
                .append(" / ").append((int) ruleEntity.getRightMotor().getExecutionTime()).append("sec")

                .append("\n\nPan motor: ").append(context.getString(R.string.degree, (int) ruleEntity.getPanMotor().getMeasurementValue()))
                .append(" / ").append((int) ruleEntity.getPanMotor().getExecutionTime()).append("sec")

                .append("\nTilt motor: ").append(context.getString(R.string.degree, (int) ruleEntity.getTiltMotor().getMeasurementValue()))
                .append(" / ").append((int) ruleEntity.getTiltMotor().getExecutionTime()).append("sec")

                .toString();
        descriptionBuilder.setLength(0);

        String executionTime = ruleEntity.getExecutionTime() + " sec";

        holder.tvRuleNumber.setText(ruleNumber);
        holder.tvDescription.setText(description);
        holder.tvExecutionTime.setText(executionTime);

        holder.btnEdit.setOnClickListener(view -> {
            assert onEditClickListener != null : "ERROR, onEditClickListener is null and can't be invoked!";
            onEditClickListener.onButtonEditClick(ruleEntity);
        });

        holder.btnDelete.setOnClickListener((view -> {
            ruleEntities.remove(position);

            // TODO: It will always be more efficient to use more specific change events if you can. Rely on notifyDataSetChanged as a last resort.
            notifyDataSetChanged();
        }));
    }

    public void setOnEditClickListener(OnEditClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    @Override
    public int getItemCount() {
        return ruleEntities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvRuleNumber;
        private final TextView tvDescription;
        private final TextView tvExecutionTime;

        private final Button btnEdit;
        private final Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRuleNumber = itemView.findViewById(R.id.tvRuleNumber);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvExecutionTime = itemView.findViewById(R.id.tvExecutionTime);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
