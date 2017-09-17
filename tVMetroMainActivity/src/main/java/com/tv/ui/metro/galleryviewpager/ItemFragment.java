package com.tv.ui.metro.galleryviewpager;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.VolleyHelper;
import com.tv.ui.metro.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment {

	private TextView nameView;
	private ImageView ivPhoto,page_left,page_right;
	private ImageLoader mImageLoader;
	private static final String PARAMS_TITLE = "title";
	private static final String PARAMS_PIC = "pic";
	private static final String PARAMS_ID = "id";
	private static final String PARAMS_SIZE = "size";

	/**
	 * 创建并传值
	 * 
	 * @param title
	 * @return
	 */
	public static Fragment create(String title,String pic,int id,int size) {
		ItemFragment fragment = new ItemFragment();
		Bundle bundle = new Bundle();
		bundle.putString(PARAMS_TITLE, title);
		bundle.putString(PARAMS_PIC, pic);
		bundle.putInt(PARAMS_ID,id);
		bundle.putInt(PARAMS_SIZE,size);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_item, container, false);
		nameView = (TextView) view.findViewById(R.id.name);
		ivPhoto = (ImageView) view.findViewById(R.id.iv_photo);
		page_left=(ImageView) view.findViewById(R.id.page_left);
		page_right=(ImageView) view.findViewById(R.id.page_right);
		mImageLoader = VolleyHelper.getInstance(getActivity().getApplicationContext()).getImageLoader();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			if (PARAMS_TITLE.equals("")){
			}
			else {
				nameView.setVisibility(View.VISIBLE);
				nameView.setText(bundle.getString(PARAMS_TITLE));
			}
     mImageLoader.get(bundle.getString(PARAMS_PIC),ImageLoader.getImageListener(ivPhoto,R.drawable.main_bg,R.drawable.main_bg));
if (bundle.getInt(PARAMS_ID)==0){
	page_left.setVisibility(View.GONE);
}
else{
	page_left.setVisibility(View.VISIBLE);
}

if (bundle.getInt(PARAMS_SIZE)==(bundle.getInt(PARAMS_ID)+1)){
	page_right.setVisibility(View.GONE);
}
else {
	page_right.setVisibility(View.VISIBLE);
}
		}
	}

}
