package titan.com.test.layercontrol;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;


import java.util.List;

import titan.com.test.R;

/**
 * Created by sp on 2018/11/22.
 * 影像图
 */
public class LayerManagerAdapter extends RecyclerView.Adapter<LayerManagerAdapter.MyViewHolder> {

    private Context mContext;

    public List<ItemLayer> getFileList() {
        return fileList;
    }

    private List<ItemLayer> fileList;

    private MyItemClickListener mItemClickListener;

    public LayerManagerAdapter(Context context, List<ItemLayer> fileList) {
        mContext = context;
        this.fileList = fileList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layer_control, parent,false), mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mCtv_layer.setText(fileList.get(position).toString());
        holder.mCtv_layer.setChecked(fileList.get(position).isSelect());
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mIv_location;
        private final CheckedTextView mCtv_layer;

        private MyItemClickListener mListener;

        MyViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);

            mCtv_layer = itemView.findViewById(R.id.item_imglayer);
            mIv_location = itemView.findViewById(R.id.item_img_location);

            mCtv_layer.setOnClickListener(this);
            mIv_location.setOnClickListener(this);

            this.mListener = myItemClickListener;

        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }
        }
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
