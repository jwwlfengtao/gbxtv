package com.xrq.tv.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xrq.tv.R;
import com.xrq.tv.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 765773123
 * @date 2018/12/27
 * 正在下载列表
 */
public class DowningAdapter extends RecyclerView.Adapter<DowningAdapter.ViewHolder> {
    private List<ItemBean> mainBeanList=new ArrayList<>();

    private DeleteListener deleteListener;
    private FocseListner focseListner;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mian_loading_view_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.text_Name.setText(mainBeanList.get(i).getZHNAME().get(0));
        viewHolder.itemView.setFocusable(true);
        if (mainBeanList.get(i).isStop()) {
            viewHolder.text_State.setText("已暂停");
        } else {
            viewHolder.text_State.setText(mainBeanList.get(i).getStateName());
        }
        viewHolder.pb.setProgress(mainBeanList.get(i).getProgerss());
        viewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //获取焦点时变化
                if (hasFocus) {
                    viewHolder.rl_selectedBg.setBackground(viewHolder.itemView.getContext().getDrawable(R.drawable.downloading_focus));
                    if (focseListner != null) {
                        focseListner.scrollToPositon(i);
                    }
                } else {
                    viewHolder.rl_selectedBg.setBackground(null);
                }
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteListener != null) {
                    deleteListener.delete(mainBeanList.get(i).getInfoHash(),mainBeanList.get(i).getURL(),i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mainBeanList == null ? 0 : mainBeanList.size();
    }


    public void initItem(List<ItemBean> itemBean) {
        mainBeanList.clear();
        mainBeanList.addAll(itemBean);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text_Name;
        private TextView text_State;
        private RelativeLayout rl_selectedBg;
        private ProgressBar pb;
        private View view_20dp;

        public ViewHolder(View itemView) {
            super(itemView);
            text_Name = itemView.findViewById(R.id.text_Name);
            text_State = itemView.findViewById(R.id.text_State);
            rl_selectedBg = itemView.findViewById(R.id.rl_selectedBg);
            pb = itemView.findViewById(R.id.pb);
            view_20dp = itemView.findViewById(R.id.view_20dp);
        }
    }

    //删除列表监听
    public interface DeleteListener {
        /**
         * 删除任务
         *
         * @param position 位置
         */
        void delete(String infoHash,String path,int position);
    }

    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public interface FocseListner {
        /**
         * 监听焦点变化情况
         *
         * @param position 位置
         */
        void scrollToPositon(int position);

    }

    public void setFocseListner(FocseListner focseListner) {
        this.focseListner = focseListner;
    }
}
