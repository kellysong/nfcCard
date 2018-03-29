/* NFCard is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

NFCard is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wget.  If not, see <http://www.gnu.org/licenses/>.

Additional permission under GNU GPL version 3 section 7 */

package com.sinpo.xnfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinpo.xnfc.card.CardManager;

import org.xml.sax.XMLReader;

/**
 * 设置系统调度 -> 系统调用onNewIntent(Intent intent) -> 获取Tag -> 获取读写通道 -> 进行读写 -> 最后取消系统调度
 */
public final class NFCardActivity extends Activity implements OnClickListener,
		Html.ImageGetter, Html.TagHandler {
	private static final String TAG ="SIMPLE_LOGGER";
	private NfcAdapter nfcAdapter; //NFC适配器
	private PendingIntent pendingIntent; //传达意图
	private Resources res;
	private TextView board;
	private TextView mHint;
	private LinearLayout defaultBg;
	private ScrollView  sv_context;

	private enum ContentType {
		HINT, DATA, MSG
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfcard);
		defaultBg = (LinearLayout) findViewById(R.id.ll_default_bg);
		mHint = (TextView) findViewById(R.id.tv_hint);
		sv_context = (ScrollView) findViewById(R.id.sv_context);
		final Resources res = getResources();
		this.res = res;

		final View decor = getWindow().getDecorView();
		final TextView board = (TextView) decor.findViewById(R.id.board);
		this.board = board;

		decor.findViewById(R.id.btnCopy).setOnClickListener(this);
		decor.findViewById(R.id.btnNfc).setOnClickListener(this);
		decor.findViewById(R.id.btnExit).setOnClickListener(this);

		board.setMovementMethod(LinkMovementMethod.getInstance());
		board.setFocusable(false);
		board.setClickable(false);
		board.setLongClickable(false);

		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		onNewIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int i = item.getItemId();
		if (i == R.id.clear) {
			showData(null);
			return true;
		} else if (i == R.id.help) {
			showHelp(R.string.info_help);
			return true;
		} else if (i == R.id.about) {
			showHelp(R.string.info_about);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (nfcAdapter != null)
			nfcAdapter.disableForegroundDispatch(this);// 取消调度

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (nfcAdapter != null)
			// 这行代码是添加调度，效果是读标签的时候不会弹出候选程序，直接用本程序处理
			nfcAdapter.enableForegroundDispatch(this, pendingIntent,
					CardManager.FILTERS, CardManager.TECHLISTS);

//		refreshStatus();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		try {
			final Parcelable p = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			Log.e(TAG,"NFCTAG:"+ intent.getAction());
			showData((p != null) ? CardManager.load(p, res) : null);
		} catch (Exception e) {
			Log.e(TAG,"获取tag异常",e);
		}
	}

	@Override
	public void onClick(final View v) {
		int i = v.getId();
		if (i == R.id.btnCopy) {
			copyData();
		} else if (i == R.id.btnNfc) {
			startActivityForResult(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
		} else if (i == R.id.btnExit) {
			finish();
		} else {
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		refreshStatus();
	}

	private void refreshStatus() {
		final Resources r = this.res;

		final String tip;
		if (nfcAdapter == null)
			tip = r.getString(R.string.tip_nfc_notfound);
		else if (nfcAdapter.isEnabled())
			tip = r.getString(R.string.tip_nfc_enabled);
		else
			tip = r.getString(R.string.tip_nfc_disabled);

		Toast.makeText(this,tip,Toast.LENGTH_SHORT).show();
		final CharSequence text = mHint.getText();
		showHint();

	}

	private void copyData() {
		final CharSequence text = board.getText();
		if (TextUtils.isEmpty(text))
			return;

		((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE))
				.setText(text);

		final String msg = res.getString(R.string.msg_copied);
		final Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	/**
	 * 显示数据
	 * @param data
	 */
	private void showData(String data) {
		if (data == null || data.length() == 0) {
			showHint();
			return;
		}
		defaultBg.setVisibility(View.GONE);
		sv_context.setVisibility(View.VISIBLE);
		board.setText(Html.fromHtml(data));
	}

	private void showHelp(int id) {
		mHint.setText(Html.fromHtml(res.getString(id), this, this));
	}
	
	/**
	 * 提示信息，不支持nfc时，或者说支持，但被禁用
	 */
	private void showHint() {
		final TextView board = this.board;
		final Resources res = this.res;
		final String hint;
		
		if (nfcAdapter == null)
			hint = res.getString(R.string.msg_nonfc);
		else if (nfcAdapter.isEnabled())
			hint = res.getString(R.string.msg_nocard);
		else
			hint = res.getString(R.string.msg_nfcdisabled);
		mHint.setText(hint);

	}

	@Override
	public void handleTag(boolean opening, String tag, Editable output,
			XMLReader xmlReader) {
		if (!opening && "version".equals(tag)) {
			try {
				output.append(getPackageManager().getPackageInfo(
						getPackageName(), 0).versionName);
			} catch (NameNotFoundException e) {
			}
		}
	}

	@Override
	public Drawable getDrawable(String source) {
		final Resources r = getResources();

		final Drawable ret;
		final String[] params = source.split(",");
		if ("icon_main".equals(params[0])) {
			ret = r.getDrawable(R.drawable.ic_app_main);
		} else {
			ret = null;
		}

		if (ret != null) {
			final float f = r.getDisplayMetrics().densityDpi / 72f;
			final int w = (int) (Util.parseInt(params[1], 10, 16) * f + 0.5f);
			final int h = (int) (Util.parseInt(params[2], 10, 16) * f + 0.5f);
			ret.setBounds(0, 0, w, h);
		}

		return ret;
	}
}
