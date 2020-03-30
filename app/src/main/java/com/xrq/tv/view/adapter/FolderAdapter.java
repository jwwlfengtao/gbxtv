package com.xrq.tv.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xrq.tv.R;
import com.xrq.tv.bean.FileBean;
import com.xrq.tv.utils.Log;

import java.util.ArrayList;

/**
 * @author 765773123
 * @date 2018/12/1
 * 文件夹列表
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private ArrayList<FileBean> folderList;
    private ItemOnclickListener onclickListener;
    //现在焦点位置
    private int selectedPositioX = 0;
    private int selectedPositioY = 0;
    //现在选中的item
    private int selectPostion = 0;
    //上一个选中的item
    private int lastSelectPostion = 0;
    //列表横向个数
    private int hoNum = 0;
    //列表纵向个数
    private int verNum = 0;
    private FoucsListener foucsListener;

    private SelectedListener selectListner;

    public FolderAdapter(ArrayList<FileBean> folderList, int hoNum) {
        this.folderList = folderList;
        this.hoNum = hoNum;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_foloer_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.text.setText(folderList.get(i).getFileName());
        viewHolder.image.setImageResource(folderList.get(i).getPicId());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onclickListener != null) {
                    if (folderList.get(i).getPicId() == R.drawable.filesystem_icon_folder) {
                        onclickListener.enterClick(i);
                        folderList.get(i).setSelected(true);
//                        notifyItemChanged(i);
                    } else {
                        onclickListener.fileClick(i);
                    }
                }
            }
        });
        if (folderList.get(i).isSelected()) {
            viewHolder.ll_item.setBackground(viewHolder.itemView.getResources().getDrawable(R.drawable.item_selected));
        } else {
            viewHolder.ll_item.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        if (folderList.size() / 2 == 0) {
            verNum = folderList.size() / 2;
        } else {
            verNum = (folderList.size() / 2) + 1;
        }
        return folderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        private ImageView image;
        private LinearLayout ll_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            image = itemView.findViewById(R.id.image);
            ll_item = itemView.findViewById(R.id.ll_item);
        }
    }

    public interface ItemOnclickListener {
        //点击文件夹进入子文件
        void enterClick(int position);

        //点击文件夹进行相关操作
        void fileClick(int position);
    }

    public void setOnclickListener(ItemOnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    //焦点上移动
    public void selectUp() {
        if (selectedPositioY == 0) {
            Log.iii("TAG", "焦点已在列表最上边");
            //把焦点交处去
            if (foucsListener != null) {
                foucsListener.outFocs();
                if (folderList.size() > lastSelectPostion) {
                    folderList.get(lastSelectPostion).setSelected(false);
                    notifyItemChanged(lastSelectPostion);
                }
            }
        } else {
            selectedPositioY--;
            if (!dealVerWithPosito(false)) {
                selectedPositioY++;
            }
        }
    }

    //焦点下移
    public void selectDown() {
        if (selectedPositioY == verNum - 1) {
            Log.iii("TAG", "焦点已在列表最下边");
        } else {
            selectedPositioY++;
            if (!dealVerWithPosito(true)) {
                selectedPositioY--;
            }
        }
    }

    //焦点左移
    public void selectLeft() {
        if (selectedPositioX == 0) {
            Log.iii("TAG", "焦点已在列表最左边");
        } else {
            selectedPositioX--;
            if (!dealHorWithPosito(false)) {
                selectedPositioX++;
            }
        }
    }

    //处理左右移动事件
    private boolean dealHorWithPosito(boolean right) {
        if (right) {
            selectPostion++;
        } else {
            selectPostion--;
        }
        if (selectPostion < folderList.size()) {
            folderList.get(selectPostion).setSelected(true);
            folderList.get(lastSelectPostion).setSelected(false);
            notifyItemChanged(selectPostion);
            notifyItemChanged(lastSelectPostion);
            lastSelectPostion = selectPostion;
            Log.iii("TAG", "改变位置成功");
            if (selectListner != null) {
                selectListner.selectListner(selectPostion);
            }
            return true;
        } else {
            selectPostion = lastSelectPostion;
            Log.iii("TAG", "改变位置失败");
            return false;
        }
    }

    //处理上下移动事件
    private boolean dealVerWithPosito(boolean down) {
        if (down) {
            selectPostion = selectPostion + hoNum;
        } else {
            selectPostion = selectPostion - hoNum;
        }
        if (selectPostion < folderList.size()) {
            folderList.get(selectPostion).setSelected(true);
            folderList.get(lastSelectPostion).setSelected(false);
            notifyItemChanged(selectPostion);
            notifyItemChanged(lastSelectPostion);
            lastSelectPostion = selectPostion;
            Log.iii("TAG", "改变位置成功");
            if (selectListner != null) {
                selectListner.selectListner(selectPostion);
            }
            return true;
        } else {
            selectPostion = lastSelectPostion;
            Log.iii("TAG", "改变位置失败");
            return false;
        }
    }

    //焦点右移动
    public void selectRight() {
        if (selectedPositioX == hoNum - 1) {
            Log.iii("TAG", "焦点已在列表最右边");

        } else {
            selectedPositioX++;
            if (!dealHorWithPosito(true)) {
                selectedPositioX--;
            }
        }
    }

    //把焦点移动到左边的控件
    public interface FoucsListener {
        void outFocs();
    }

    //重获焦点
    public void resettFouce() {
        if (folderList.size() > lastSelectPostion) {
            folderList.get(lastSelectPostion).setSelected(true);
            notifyItemChanged(lastSelectPostion);
        }
    }

    public interface SelectedListener {
        void selectListner(int positon);
    }

    public void setSelectedListener(SelectedListener selectListner) {
        this.selectListner = selectListner;
    }

    public void setFoucsListener(FoucsListener foucsListener) {
        this.foucsListener = foucsListener;
    }

    public int getLastSelectPostion() {
        return lastSelectPostion;
    }

    public void resetPosition() {
        lastSelectPostion = 0;
        selectPostion = 0;
        selectedPositioX = 0;
        selectedPositioY = 0;
    }

}
