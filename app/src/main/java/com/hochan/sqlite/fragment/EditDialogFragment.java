package com.hochan.sqlite.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hochan.sqlite.R;
import com.hochan.sqlite.data.Worker;
import com.hochan.sqlite.sql.SqliteHelper;
import com.hochan.sqlite.tools.GetLocalImageByUri;
import com.hochan.sqlite.tools.MyApplication;
import com.hochan.sqlite.tools.ScreenTools;

/**
 * Created by Administrator on 2016/4/12.
 */
public class EditDialogFragment extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{

    public final static int ADD_DIALOG = 0;
    public final static int DELETEEDIT_DIALOG = 1;
    public final static int DELETE = 0;
    public final static int EDIT = 1;
    public final static int ADD = 2;
    public final static String POSITION = "position";

    public final static String TAG = "EditDialogFragment：";

    private View mView, mRootView;
    private Context mContext;
    private Button btnDelete, btnEdit, btnCancle, btnPicture, btnUpload;
    private EditText edId, edNickName, edPassword; // edWorkState;
    //private RadioGroup rgWorkState;
    private ImageView ivImage;
    private String mName, mPhoneNumber, mTowerNumber, mWorkState;
    private String mWorkStateFromRG = "在岗";
    public int mPosition;
    private int mTag;
    private final static String IMAGE = "image/*";

    private AnimationSet mDialogInAnim;

    public static EditDialogFragment newInstance(int tag, String name, String phoneNumber,
                                                 String towerNumber, String workState, int position){
        EditDialogFragment editDialogFragment = new EditDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG, tag);
        bundle.putString(SqliteHelper.NICKNAME, name);
        bundle.putString(SqliteHelper.PASSWORD, phoneNumber);
        //bundle.putString(SqliteHelper.TOWER_NUMBER, towerNumber);
        //bundle.putString(SqliteHelper.WORK_STATE, workState);
        bundle.putInt(POSITION, position);
        editDialogFragment.setArguments(bundle);
        return editDialogFragment;
    }

    public static EditDialogFragment newInstance(int tag){
        EditDialogFragment editDialogFragment = new EditDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG, tag);
        editDialogFragment.setArguments(bundle);
        return editDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        if(mContext == null){
            System.out.println("mContext is nll");
        }else{
            System.out.println("mContext:"+mContext.getClass());
            mDialogInAnim = (AnimationSet) AnimationUtils.loadAnimation(mContext, R.anim.modal_in);
        }
        System.out.println(TAG+"onCreate!");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_layout, container, false);
        mRootView = getDialog().getWindow().getDecorView().findViewById(android.R.id.content);
        mView = view;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println(TAG + "onStart!");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        //mRootView.startAnimation();
        //getDialog().getWindow().setLayout((int) (MyApplication.mWidthOfDialog), WindowManager.LayoutParams.WRAP_CONTENT);//这2行,和上面的一样,注意顺序就行;
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);//这2行,和上面的一样,注意顺序就行;
        //mRootView.startAnimation(mDialogInAnim);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println(TAG + "onActivityCreated!" + getTag());
        mView = getView();
        mContext = getContext();
        mTag = getArguments().getInt(TAG);

        btnDelete = (Button) mView.findViewById(R.id.btn_delete);
        btnEdit = (Button) mView.findViewById(R.id.btn_edit);
        btnCancle = (Button) mView.findViewById(R.id.btn_cancle);
        btnPicture = (Button) mView.findViewById(R.id.btn_picture);
        btnUpload = (Button) mView.findViewById(R.id.btn_upload);
        btnPicture = (Button) mView.findViewById(R.id.btn_picture);
        ivImage = (ImageView) mView.findViewById(R.id.iv_image);

        edId = (EditText) mView.findViewById(R.id.ed_id);
        edNickName = (EditText) mView.findViewById(R.id.ed_nick_name);
        edPassword = (EditText) mView.findViewById(R.id.ed_password);

        btnEdit.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
        btnPicture.setOnClickListener(this);
        //rgWorkState.setOnCheckedChangeListener(this);

        if(getArguments().getInt(TAG) == ADD_DIALOG){
            btnDelete.setVisibility(View.GONE);
            btnEdit.setText("添加");
            btnUpload.setVisibility(View.GONE);
            //Toast.makeText(mContext, "增加", Toast.LENGTH_LONG).show();
        }else{
            btnDelete.setVisibility(View.VISIBLE);
            btnUpload.setVisibility(View.VISIBLE);
            btnEdit.setText("修改");
            btnDelete.setOnClickListener(this);
            btnCancle.setOnClickListener(this);
            mPosition = getArguments().getInt(POSITION);
        }
    }

    @Override
    public void onClick(View v) {
        if(mOnDialogListener != null){
            switch (v.getId()){
                case R.id.btn_delete:
                    mOnDialogListener.clicked(DELETE, null, mPosition);
                    dismiss();
                    break;
                case R.id.btn_edit:
                    if(TextUtils.isEmpty(edId.getText())){
                        Toast.makeText(mContext, "请输入姓名", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(TextUtils.isEmpty(edNickName.getText())){
                        Toast.makeText(mContext, "请输入电话", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(TextUtils.isEmpty(edPassword.getText())){
                        Toast.makeText(mContext, "请输入电塔编号", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Worker worker = new Worker();
                    worker.setmName(edId.getText().toString());
                    worker.setmPhoneNumber(edNickName.getText().toString());
                    worker.setmTowerNumber(edPassword.getText().toString());
                    worker.setmWorkState(mWorkStateFromRG);
                    mOnDialogListener.clicked(EDIT, worker, mPosition);
                    if(mTag == DELETEEDIT_DIALOG)
                        dismiss();
                    else if(mTag == ADD_DIALOG){
                        edId.setText("");
                        edNickName.setText("");
                        edPassword.setText("");
//                        edWorkState.setText("");
                    }
                    break;
                case R.id.btn_cancle:
                    dismiss();
                    break;
                case R.id.btn_picture:
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 0);
                    break;
            }
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton rb = (RadioButton) mView.findViewById(checkedId);
        mWorkStateFromRG = rb.getText().toString();
        Log.i("radio button",rb.getText() + "");
    }

    private OnDialogListener mOnDialogListener;

    public void setOnDialogListener(OnDialogListener dialogListener){
        this.mOnDialogListener = dialogListener;
    }

    public interface OnDialogListener{
        public void clicked(int index, Worker worker, int position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(TAG + "onActivityResult!");
        System.out.println(TAG + "requestCode：" + requestCode);
        System.out.println(TAG + "resultCode：" + resultCode);
        if(resultCode == 0)
            return;
        System.out.println(TAG+ "data："+data.getDataString());
        Uri uri = data.getData();
        btnPicture.setText(uri.toString());
        ScreenTools screenTools = ScreenTools.instance(mContext);
        final int widthOfDialogWindow = MyApplication.mWidthOfScreen - screenTools.dip2px(15*2);
        GetLocalImageByUri getLocalImageByUri = new GetLocalImageByUri(mContext){
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                ivImage.setLayoutParams(new LinearLayout.LayoutParams(widthOfDialogWindow,
                        (int) (bitmap.getHeight() * 1.0 / bitmap.getWidth() * widthOfDialogWindow)));
                System.out.println(TAG+"bitmap.getWidth()"+bitmap.getWidth());
                ivImage.setImageBitmap(bitmap);
            }
        };
        getLocalImageByUri.execute(uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println(TAG + "onResume!");
    }

}
