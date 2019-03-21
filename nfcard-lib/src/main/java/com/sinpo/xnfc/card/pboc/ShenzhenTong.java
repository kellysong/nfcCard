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

package com.sinpo.xnfc.card.pboc;

import android.content.res.Resources;

import com.sinpo.xnfc.R;
import com.sinpo.xnfc.tech.Iso7816;

import java.util.ArrayList;
/**
 * 
 * 深圳通实现类，本注释制作说明，源码并非本人写
 * @author Kelly
 * @version 1.0.0
 * @filename ShenzhenTong.java
 * @time 2017年6月27日 下午3:02:53
 * @copyright(C) 2017 song
 */
final class ShenzhenTong extends PbocCard {
	/**
	 * 深圳通名字，用字节表示
	 */
	private final static byte[] DFN_SRV = { (byte) 'P', (byte) 'A', (byte) 'Y',
			(byte) '.', (byte) 'S', (byte) 'Z', (byte) 'T' };

	private ShenzhenTong(Iso7816.Tag tag, Resources res) {
		super(tag);
		name = res.getString(R.string.name_szt);
	}

	@SuppressWarnings("unchecked")
	final static ShenzhenTong load(Iso7816.Tag tag, Resources res) {

		/*--------------------------------------------------------------*/
		// select PSF (1PAY.SYS.DDF01)
		/*--------------------------------------------------------------*/
		if (tag.selectByName(DFN_PSE).isOkey()) {

			Iso7816.Response INFO, CASH;

			/*--------------------------------------------------------------*/
			// select Main Application
			/*--------------------------------------------------------------*/
			if (tag.selectByName(DFN_SRV).isOkey()) {

				/*--------------------------------------------------------------*/
				// read card info file, binary (21)
				/*--------------------------------------------------------------*/
				INFO = tag.readBinary(SFI_EXTRA);

				/*--------------------------------------------------------------*/
				// read balance
				/*--------------------------------------------------------------*/
				CASH = tag.getBalance(true);

				/*--------------------------------------------------------------*/
				// read log file, record (24)
				/*--------------------------------------------------------------*/
				ArrayList<byte[]> LOG = readLog(tag, SFI_LOG);

				/*--------------------------------------------------------------*/
				// build result string
				/*--------------------------------------------------------------*/
				final ShenzhenTong ret = new ShenzhenTong(tag, res);
				ret.parseBalance(CASH);
				ret.parseInfo(INFO, 4, true);
				ret.parseLog(LOG);

				return ret;
			}
		}

		return null;
	}
}
