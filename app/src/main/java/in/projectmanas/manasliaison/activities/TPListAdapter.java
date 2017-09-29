package in.projectmanas.manasliaison.activities;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import in.projectmanas.manasliaison.R;

/**
 * Created by knnat on 9/29/2017.
 */

public class TPListAdapter extends ArrayAdapter<String[]> {

    @NonNull
    private final Context context;
    private final int resource;
    @NonNull
    private final List<String[]> objects;

    public TPListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String[]> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_tp_list, null);
        }

        String[] p = getItem(position);
        if (p != null) {
            TextView tvName = v.findViewById(R.id.tv_name_tp);
            TextView tvStatus = v.findViewById(R.id.tv_status_tp);
            tvName.setText(p[0]);
            tvStatus.setText(p[1]);
        }
        return v;
    }
}
