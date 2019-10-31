package titan.com.test.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import titan.com.test.R;


public class AttrRecycAdapter extends RecyclerView.Adapter<AttrRecycAdapter.MyViewHolder> {

    List<String> arrayList = new ArrayList<>();
    ItemClickListener mItemClickListener;


    public AttrRecycAdapter(List<String> list){
        this.arrayList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attri,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String nameValue = arrayList.get(position);
        holder.viewName.setText(nameValue.split(",")[0]);
        holder.viewValue.setText(nameValue.split(",")[1]);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    protected class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView viewName;
        private TextView viewValue;

        public MyViewHolder(View itemView) {
            super(itemView);
            viewName = itemView.findViewById(R.id.item_attri_name);
            viewValue = itemView.findViewById(R.id.item_attri_value);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    /**
     * 创建一个回调接口
     */
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     */
    public void setItemClickListener(ItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }

}
