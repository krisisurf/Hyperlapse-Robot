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
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.TurnEntity;

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
        String description = createDescription(ruleEntity);
        String executionTime = ruleEntity.getExecutionTime() + " sec";

        holder.tvRuleNumber.setText(ruleNumber);
        holder.tvDescription.setText(description);
        holder.tvExecutionTime.setText(executionTime);

        holder.btnEdit.setOnClickListener(view -> {
            assert onEditClickListener != null : "ERROR, onEditClickListener is null and can't be invoked!";
            onEditClickListener.onButtonEditClick(ruleEntity);
        });

        holder.btnDuplicate.setOnClickListener(view -> {
            RuleEntity cloned = ruleEntity.clone();
            ruleEntities.add(cloned);

            // TODO 1000: It will always be more efficient to use more specific change events if you can. Rely on notifyDataSetChanged as a last resort.
            notifyDataSetChanged();
        });

        holder.btnDelete.setOnClickListener((view -> {
            ruleEntities.remove(position);

            // TODO 1000: It will always be more efficient to use more specific change events if you can. Rely on notifyDataSetChanged as a last resort.
            notifyDataSetChanged();
        }));
    }

    private String createDescription(RuleEntity ruleEntity) {
        // TODO: Center vertically the description
        if (ruleEntity.getLeftMotor().equalsByMeasurementValueAndExecutionTime(ruleEntity.getRightMotor())) {
            int distance = (int) ruleEntity.getLeftMotor().getMeasurementValue();
            if (distance != 0) {
                String direction = ruleEntity.getLeftMotor().getMeasurementValue() > 0 ? "Forward: " : "Backward: ";
                descriptionBuilder.append(direction).append(distance).append("cm")
                        .append(" / ").append((int) ruleEntity.getLeftMotor().getExecutionTime()).append("sec\n");
            }
        } else {
            TurnEntity turnEntity = ruleEntity.getTurnEntity();
            descriptionBuilder.append(turnEntity.getTurnAngle() > 0 ? "Right" : "Left")
                    .append(" Turn: ").append(context.getString(R.string.degree, turnEntity.getTurnAngle()))
                    .append("\nTurn Radius: ").append(turnEntity.getTurnRadius()).append("cm")
                    .append(" / ").append(turnEntity.getExecutionTime()).append("sec\n\n");
        }

        if (ruleEntity.getPanMotor().getMeasurementValue() != 0) {
            descriptionBuilder
                    .append("Camera Pan: ").append(context.getString(R.string.degree, (int) ruleEntity.getPanMotor().getMeasurementValue()))
                    .append(" / ").append((int) ruleEntity.getPanMotor().getExecutionTime()).append("sec\n");
        }

        if (ruleEntity.getTiltMotor().getMeasurementValue() != 0) {
            descriptionBuilder
                    .append("Camera Tilt: ").append(context.getString(R.string.degree, (int) ruleEntity.getTiltMotor().getMeasurementValue()))
                    .append(" / ").append((int) ruleEntity.getTiltMotor().getExecutionTime()).append("sec");
        }

        if (descriptionBuilder.length() == 0)
            descriptionBuilder.append("That rule will be used as delay.");

        String result = descriptionBuilder.toString();
        descriptionBuilder.setLength(0);
        return result;
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
        private final Button btnDuplicate;
        private final Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRuleNumber = itemView.findViewById(R.id.tvRuleNumber);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvExecutionTime = itemView.findViewById(R.id.tvExecutionTime);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDuplicate = itemView.findViewById(R.id.btnDuplicate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
