package fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gangjianwang.www.gangjianwang.R;

import utils.CodeUtils;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class PrepaidCardPayCashFragment extends Fragment implements View.OnClickListener{

    private ImageView mImgcodeIv;
    private CodeUtils codeUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prepaidcard_paycash,null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        initView(view);
        setListener();
        return view;
    }

    private void initView(View view){
        mImgcodeIv = (ImageView) view.findViewById(R.id.iv_prepaidcard_paycash_imgcode);
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        mImgcodeIv.setImageBitmap(bitmap);
    }

    private void setListener(){
        mImgcodeIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_prepaidcard_paycash_imgcode:
                mImgcodeIv.setImageBitmap(codeUtils.createBitmap());
                break;
            default:
                break;
        }
    }
}
