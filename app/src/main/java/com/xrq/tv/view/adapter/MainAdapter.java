package com.xrq.tv.view.adapter;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xrq.tv.R;
import com.xrq.tv.bean.ItemBean;

import java.util.List;
import java.util.PropertyResourceBundle;

/**
 * @author 765773123
 * @date 2018/11/26
 * 主页数据源列表
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<ItemBean> mainBeanList;
    private boolean init = false;
    private AddTaskListener addTaskListener;

    public MainAdapter(List<ItemBean> mainBeanList) {
        this.mainBeanList = mainBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_main_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int m) {
        final int i = viewHolder.getAdapterPosition();
        if (mainBeanList.get(i).isNull()) {
            viewHolder.item.setVisibility(View.INVISIBLE);
            viewHolder.itemView.setFocusable(false);
        } else {
            viewHolder.item.setVisibility(View.VISIBLE);
            if (!viewHolder.itemView.hasFocusable()) {
                viewHolder.itemView.setFocusable(true);
            }
        }
        viewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //获取焦点时变化
                if (hasFocus) {
                    viewHolder.item.setBackground(null);
                    viewHolder.item0.setBackground(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.item_selected));
                } else {
                    viewHolder.item0.setBackground(null);
                    viewHolder.item.setBackground(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.bg_main_item));
                }
            }
        });
        if (mainBeanList.get(i).getDATA() != null && !"".equals(mainBeanList.get(i).getDATA())) {
            if ("2".equals(mainBeanList.get(i).getSTATUS())) {
                viewHolder.text_time.setText(mainBeanList.get(i).getDATA() + "   即将下架");
                viewHolder.text_time.setVisibility(View.VISIBLE);
                viewHolder.text_time.setTextColor(Color.RED);
            } else if ("1".equals(mainBeanList.get(i).getSTATUS())) {
                viewHolder.text_time.setText(mainBeanList.get(i).getDATA());
                viewHolder.text_time.setVisibility(View.VISIBLE);
                viewHolder.text_time.setTextColor(Color.WHITE);
            }

        } else {
            if (mainBeanList.get(i).getIvisibleTime() == View.INVISIBLE) {
                viewHolder.text_time.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.text_time.setVisibility(View.GONE);
            }
        }
        if (mainBeanList.get(i).getSCORE() != null && !mainBeanList.get(i).getSCORE().isEmpty()) {
            viewHolder.text_source.setText(mainBeanList.get(i).getSCORE().get(0));
        } else {
            viewHolder.text_source.setText("");
        }
        if (mainBeanList.get(i).getSIZE() != null && !mainBeanList.get(i).getSIZE().isEmpty()) {
            viewHolder.text_size.setText(mainBeanList.get(i).getSIZE().get(0));
        } else {
            viewHolder.text_size.setText("");
        }
        if (mainBeanList.get(i).getZHNAME() != null && !mainBeanList.get(i).getZHNAME().isEmpty()) {
            viewHolder.text_Name.setText(mainBeanList.get(i).getZHNAME().get(0));
        } else {
            viewHolder.text_Name.setText("");
        }
        if (mainBeanList.get(i).getENNAME() != null && !mainBeanList.get(i).getENNAME().isEmpty()) {
            viewHolder.text_EnName.setText(mainBeanList.get(i).getENNAME().get(0));
        } else {
            viewHolder.text_EnName.setText("");
        }
        if (mainBeanList.get(i).getABSTRACT() != null && !mainBeanList.get(i).getABSTRACT().isEmpty()) {
            viewHolder.text_describe.setText(mainBeanList.get(i).getABSTRACT().get(0));
        } else {
            viewHolder.text_describe.setText("");
        }
        if (mainBeanList.get(i).getPOSTER() != null && !mainBeanList.get(i).getPOSTER().isEmpty()) {
            viewHolder.image.setImageURI(mainBeanList.get(i).getPOSTER().get(0));
        } else {
            //设置默认图片
            viewHolder.image.setImageResource(R.mipmap.ic_launcher);
        }


        if (mainBeanList.get(i).isAdd()) {
            viewHolder.text_size.setVisibility(View.INVISIBLE);
            viewHolder.fram_added.setVisibility(View.VISIBLE);
            if (mainBeanList.get(i).getState() == 1) {
                viewHolder.text_state.setText("已添加");
            } else if (mainBeanList.get(i).getState() == 5) {
                viewHolder.text_state.setText("已添加");
            } else if (mainBeanList.get(i).getState() == 8) {
                viewHolder.text_state.setText("已添加");
            } else if (mainBeanList.get(i).getState() == 10) {
                viewHolder.text_state.setText("已下载");
            } else {

            }
        } else {
            viewHolder.fram_added.setVisibility(View.INVISIBLE);
            viewHolder.text_size.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addTaskListener != null) {
                    addTaskListener.add(true, i);
                }
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        viewHolder.ll_tag.removeAllViews();
        if (mainBeanList.get(i).getTAGS() != null &&
                mainBeanList.get(i).getTAGS().get(0) != null &&
                mainBeanList.get(i).getTAGS().get(0).getTAG() != null) {
            for (int n = 0; n < mainBeanList.get(i).getTAGS().get(0).getTAG().size(); n++) {
                TextView textView = new TextView(viewHolder.itemView.getContext());
                layoutParams.setMargins(0, 0, 12, 0);
                textView.setTextSize(10);
                //设置背景
                textView.setBackgroundResource(R.drawable.bg_tag);
                textView.setTextColor(Color.WHITE);
                textView.setPadding(8, 2, 8, 2);
                textView.setText(mainBeanList.get(i).getTAGS().get(0).getTAG().get(n));
                textView.setLayoutParams(layoutParams);
                viewHolder.ll_tag.addView(textView);
            }
        }
        if (i == 0 && !init) {
            viewHolder.itemView.requestFocus();
            init = true;
        }
    }

    @Override
    public int getItemCount() {
        return mainBeanList == null ? 0 : mainBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        LinearLayout item0;
        private TextView text_time;
        private TextView text_source;
        private TextView text_size;
        private TextView text_Name;
        private TextView text_EnName;
        private TextView text_describe;
        private SimpleDraweeView image;
        private LinearLayout ll_tag;
        private FrameLayout fram_added;
        private TextView text_state;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            item0 = itemView.findViewById(R.id.item0);
            text_time = itemView.findViewById(R.id.text_time);
            text_Name = itemView.findViewById(R.id.text_Name);
            text_source = itemView.findViewById(R.id.text_source);
            text_size = itemView.findViewById(R.id.text_size);
            text_EnName = itemView.findViewById(R.id.text_EnName);
            text_describe = itemView.findViewById(R.id.text_describe);
            image = itemView.findViewById(R.id.image);
            ll_tag = itemView.findViewById(R.id.ll_tag);
            fram_added = itemView.findViewById(R.id.fram_added);
            text_state = itemView.findViewById(R.id.text_state);
        }
    }

    public interface AddTaskListener {
        /**
         * 添加一个任务
         *
         * @param notify   是否需要刷新
         * @param position 位置
         */
        void add(boolean notify, int position);
    }

    public void setAddTaskListener(AddTaskListener addTaskListener) {
        this.addTaskListener = addTaskListener;
    }
}
