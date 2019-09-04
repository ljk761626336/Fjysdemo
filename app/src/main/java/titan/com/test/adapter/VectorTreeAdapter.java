package titan.com.test.adapter;



import com.ahao.basetreeview.adapter.MultiLayoutTreeAdapter;
import com.ahao.basetreeview.model.TreeNode;
import com.ahao.basetreeview.util.DpUtil;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import titan.com.test.R;
import titan.com.test.bean.Vector;

public class VectorTreeAdapter extends MultiLayoutTreeAdapter<Vector> {


    public VectorTreeAdapter(List<TreeNode<Vector>> dataToBind) {
        super(dataToBind);
    }

    @Override
    protected void addItemTypes() {
        addItemType(-1, R.layout.view_tree_leaf);
        addItemType(0, R.layout.view_tree_level_0);
        addItemType(1, R.layout.view_tree_level_1);
    }

    @Override
    protected void convert(BaseViewHolder helper, TreeNode<Vector> item) {
        super.convert(helper, item);
        switch (item.getItemType()) {
            case -1:
                resolveLeaf(helper, item);
                break;
            case 0:
                resolveLevel0(helper, item);
                break;
            case 1:
                resolveLevel1(helper, item);
                break;
        }


    }

    private void resolveLevel1(BaseViewHolder helper, TreeNode<Vector> item) {
        helper.setText(R.id.level1_textView, "name:" + item.getData().getName());
        helper.setText(R.id.level1_id, "id:" + item.getId());
        helper.setText(R.id.level1_parentId, "pid:" + item.getPId());

        if (item.isExpand()) {
            helper.setImageResource(R.id.level1_icon, R.drawable.tree_icon_collapse);
        } else {
            helper.setImageResource(R.id.level1_icon, R.drawable.logo);
        }

    }

    private void resolveLevel0(BaseViewHolder helper, TreeNode<Vector> item) {
        Vector vector = item.getData();
        helper.setText(R.id.level_0_textView, vector.getName()!=null ? vector.getName() :"");

        if (item.isExpand()) {
            helper.setImageResource(R.id.level_0_icon, R.drawable.tree_icon_collapse);
        } else {
            helper.setImageResource(R.id.level_0_icon, R.drawable.tree_icon_expand);
        }
    }

    private void resolveLeaf(BaseViewHolder helper, TreeNode<Vector> item) {
        helper.setText(R.id.leaf_textView, item.getData().getName());
        helper.setVisible(R.id.leaf_checkboxText,true);
        if(item.getData().getParentId().equals("")){
            helper.setText(R.id.leaf_textView, item.getData().getName());
            helper.setVisible(R.id.leaf_checkboxText,false);
            helper.setVisible(R.id.leaf_zoomTent,false);
        }else{
            helper.setVisible(R.id.leaf_checkboxText,true);
            helper.setChecked(R.id.leaf_checkboxText,item.getData().isVisible());
            helper.setVisible(R.id.leaf_zoomTent,true);
        }

        helper.addOnClickListener(R.id.leaf_checkboxText);
        helper.addOnClickListener(R.id.leaf_zoomTent);


    }

    @Override
    protected int getTreeNodeMargin() {
        return DpUtil.dip2px(this.mContext, 30);
    }
}
