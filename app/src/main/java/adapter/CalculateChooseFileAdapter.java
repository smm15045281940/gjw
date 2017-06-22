package adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.gangjianwang.www.gangjianwang.ListItemClickHelp;
import com.gangjianwang.www.gangjianwang.R;

import java.util.List;

import bean.ChooseFile;

/**
 * Created by Administrator on 2017/6/22.
 */

public class CalculateChooseFileAdapter extends BaseAdapter {

    private Context context;
    private List<ChooseFile> list;
    private ListItemClickHelp callback;
    private ViewHolder holder;

    public CalculateChooseFileAdapter(Context context, List<ChooseFile> list, ListItemClickHelp callback) {
        this.context = context;
        this.list = list;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_calculate_choosefile, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ChooseFile chooseFile = list.get(position);
        holder.fileIv.setImageBitmap(chooseFile.getBitmap());
        holder.contentEt.setText(chooseFile.getContent());
        final View view = convertView;
        final int p = position;
        final int id = holder.deleteIv.getId();
        holder.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, id, false);
            }
        });
        holder.contentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list.get(position).setContent(s.toString());
            }
        });
        return convertView;
    }

    class ViewHolder {

        private ImageView deleteIv;
        private ImageView fileIv;
        private EditText contentEt;

        public ViewHolder(View itemView) {
            deleteIv = (ImageView) itemView.findViewById(R.id.iv_item_calculate_choosefile_delete);
            fileIv = (ImageView) itemView.findViewById(R.id.iv_item_calculate_choosefile_content);
            contentEt = (EditText) itemView.findViewById(R.id.et_item_calculate_choosefile_content);
        }
    }
}
