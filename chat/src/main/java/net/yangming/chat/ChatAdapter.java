package net.yangming.chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class ChatAdapter extends BaseAdapter {
	ArrayList<MessageInfo> lsObj = new ArrayList<MessageInfo>();

	public abstract void OnIconClick(String sUid);

	public ChatAdapter(ArrayList<MessageInfo> lsObj) {
		this.lsObj = lsObj;
	}

	@Override
	public int getCount() {
		return lsObj.size();
	}

	@Override
	public Object getItem(int arg0) {
		arg0--;
		if (arg0 >= 0 && arg0 < lsObj.size()) {
			return lsObj.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int i, View vw, ViewGroup vg) {
		if (i >= 0 && i < lsObj.size()) {
			MessageInfo mi = lsObj.get(i);
			if (mi != null) {
				String from = mi.getFrom();
				String textContent = mi.getTextContent();

				if ("system".equals(from)) {
					vw = (LinearLayout) LayoutInflater.from(vg.getContext()).inflate(R.layout.chat_item_other, null);
				} else if ("user".equals(from)) {
					vw = (LinearLayout) LayoutInflater.from(vg.getContext()).inflate(R.layout.chat_item_me, null);
				}

				TextView chat_text_content = (TextView) vw.findViewById(R.id.chat_text_content);
				ImageView icon = (ImageView) vw.findViewById(R.id.icon);
				faceImageGrobal.showImage(vg.getContext(),
						chat_text_content, textContent);
			}
		}
		return vw;
	}

	// 打开图片
	private void openImage(Context ct, String filePath, String sPath) {

	}

	private void setImageWidth(ImageView iv, int iWidth) {
		ViewGroup.LayoutParams lp = iv.getLayoutParams();
		lp.width = iWidth;
		iv.setLayoutParams(lp);
	}

	private void dealImage(final ImageView iv, final Context ct, final String sPath) {
		// showImage(iv, (String) iv.getTag());
		// setImageWidth(iv, ViewGroup.LayoutParams.WRAP_CONTENT);
		iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openImage(ct, (String) iv.getTag(), sPath);
			}
		});
	}

	private int iSmallImageSize = 160;

	// private void showImage(ImageView iv, String fileName) {
	// String sSmallFile = fileName + ".small";
	// Bitmap bm = BitmapAction.getBitmapFromFile(sSmallFile);
	// if (bm == null) {
	// bm = BitmapAction.getBitmapFromFile(fileName, iSmallImageSize);
	// BitmapAction.saveBitmap(sSmallFile, bm);
	// }
	// LogGlobal2.logClass("setImageFromFile:" + bm.getWidth());
	// setImageWidth(iv, ViewGroup.LayoutParams.WRAP_CONTENT);
	// if (bm != null) {
	// try {
	// iv.setImageBitmap(bm);
	// } catch (Exception e) {
	// LogGlobal.log("setImageFromFile:" + StringAction.trim(e.toString()));
	// }
	// }
	// }

	public void removeAll() {
		lsObj.clear();
	}

	private MessageInfo addDateline(String sDateline) {
		MessageInfo mi = new MessageInfo();
		mi.setFlag("2");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 得到指定模范的时间
			Date last = sdf.parse(sDateline);
			Date now = new Date();
			int ilast = last.getYear() * 10000 + last.getMonth() * 100 + last.getDate();
			int inow = now.getYear() * 10000 + now.getMonth() * 100 + now.getDate();
			if (ilast == inow) {
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");
				sDateline = df.format(last);
			} else {
				if (last.getYear() == now.getYear()) {
					SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
					sDateline = df.format(last);
				} else {
					SimpleDateFormat df = new SimpleDateFormat("yyyyy-MM-dd HH:mm");
					sDateline = df.format(last);
				}
			}
		} catch (Exception e) {
		}
		mi.setDateline(sDateline);
		return mi;
	}

	public void addEndDateline() {
		if (lsObj.size() > 0) {
			MessageInfo mi = lsObj.get(0);
			if (mi.getFlag().compareTo("2") != 0) {
				MessageInfo dl = addDateline(mi.getDateline());
				lsObj.add(0, dl);
				this.notifyDataSetChanged();
			}
		}
	}

	private boolean compareDate(String sDate1, String sDate2) {
		boolean bMoreHour = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 得到指定模范的时间
			Date d1 = sdf.parse(sDate1);
			Date d2 = sdf.parse(sDate2);
			Date now = new Date();
			long timeLong = Math.abs(d1.getTime() - d2.getTime()) / 1000;
			long span = Math.abs(now.getTime() - d1.getTime()) / 1000;
			if (span < 60 * 60) {
				span = 60 * 10;
			} else {
				span = 60 * 60;
			}
			if (timeLong > span) {
				bMoreHour = true;
			}
		} catch (Exception e) {
		}
		return bMoreHour;
	}

}
