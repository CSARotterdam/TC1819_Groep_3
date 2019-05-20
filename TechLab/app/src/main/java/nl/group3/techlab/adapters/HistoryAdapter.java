package nl.group3.techlab.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import nl.group3.techlab.R;
import nl.group3.techlab.models.RowListItem;

import java.util.List;


public class HistoryAdapter extends ArrayAdapter<RowListItem> {
    public HistoryAdapter(@NonNull Context context, List<RowListItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent,false);

        }
        TextView item_name = convertView.findViewById(R.id.item_name);
        TextView description = convertView.findViewById(R.id.description);
        RowListItem p = getItem(position);
        item_name.setText(p.getItem_name());
        description.setText(p.getDescription());
        return convertView;
    }
}
