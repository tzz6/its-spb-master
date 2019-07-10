package com.its.web.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.its.common.utils.Constants;
import com.its.common.utils.ImportError;
import com.its.common.utils.PrimaryKeyUtil;
import com.its.common.utils.poi.AbstractPoiSaxExcelHandler;
import com.its.model.mybatis.dao.domain.SysUser;

/**
 * 服务范围Excel导入
 * @author tzz
 *
 */
public class ImportExcelHandler extends AbstractPoiSaxExcelHandler {

	private static final Log logger = LogFactory.getLog(ImportExcelHandler.class);

	private List<ImportError> errors = new ArrayList<ImportError>();

	/** excel解析后的数据 */
	private List<SysUser> datas = new ArrayList<SysUser>();

	private String lang = null;

	public ImportExcelHandler(String lang) {
		this.lang = lang;
	}

	public List<SysUser> getDatas() {
		return datas;
	}

	public List<ImportError> getErrors() {
		return errors;
	}

	@Override
	public void getRows(int sheetIndex, int curRow, List<String> rowList) {
		if (curRow == 0) {
		    // 第一行,校验Excel表头
			if (!validateHeader(rowList)) {
				logger.info("Excel表头校验失败");
				throw new RuntimeException("Excel表头校验失败");
			}
		} else {
			// 数据封装
			SysUser sysUser = setSysUser(rowList, curRow);
			if (sysUser != null) {
				if (validateData(sysUser)) {
				    // 数据完整信息校验成功
					if (errors.size() == 0) {
						datas.add(sysUser);
					}
				}
			}
		}

		if (errors.size() >= Constants.Excel.MAXROW) {
		    // 如果错误信息超过限制行，则返回错误信息，不再继续解析数据
			logger.info("导入失败，错误信息超过" + Constants.Excel.MAXROW + "条");
			throw new RuntimeException("导入失败，错误信息超过" + Constants.Excel.MAXROW + "条");
		}
	}

	/**
	 * 校验Excel表头
	 * 
	 * @param rowList
	 * @return
	 */
	private boolean validateHeader(List<String> rowList) {
		boolean iFlag = true;
		UploadUtil<SysUser> uploadUtil = new UploadUtil<SysUser>();
		ImportError errorForm = null;
		try {
		    // 校验Excel表头
			errorForm = uploadUtil.checkHead(SysUser.class, rowList, this.lang);
		} catch (Exception e) {
			errors.add(new ImportError(1 + "", ResourceBundleHelper.get(this.lang, Constants.Excel.EXCEL_HEADER_ERROR)));
			iFlag = false;
		}
		if (errorForm != null) {
			errors.add(errorForm);
			iFlag = false;
		}
		return iFlag;
	}

	/**
	 * 校验数据
	 * 
	 * @param sysUser
	 * @return
	 */
	private boolean validateData(SysUser sysUser) {
		int rowNum = 1;
		String rowNumStr = rowNum + "";
		String stCode = sysUser.getStCode();
		if (StringUtils.isBlank(stCode)) {
			errors.add(new ImportError(rowNumStr, "用户名不能为空"));
			return false;
		}
		String stName = sysUser.getStName();
		if (StringUtils.isBlank(stName)) {
			errors.add(new ImportError(rowNumStr, "用户姓名不能为空"));
			return false;
		}
		String stPassword = sysUser.getStPassword();
		if (StringUtils.isBlank(stPassword)) {
			errors.add(new ImportError(rowNumStr, "用户密码不能为空"));
			return false;
		}
		return true;
	}

	/**
	 * Excel数据读取（装载Bean）
	 * 
	 * @param rowList
	 * @return
	 */
	public SysUser setSysUser(List<String> rowList, int rowNum) {
	    int end = 3;
		if (null == rowList || rowList.size() < end) {
			errors.add(new ImportError(rowNum + "", "数据行为空或缺少列"));
			return null;
		}
		SysUser sysUser = new SysUser();
		sysUser.setStId(PrimaryKeyUtil.genPrimaryKey());
		String stCode = rowList.get(0);
		String stName = rowList.get(1);
		String stPassword = rowList.get(2);
		sysUser.setStCode(stCode);
		sysUser.setStName(stName);
		sysUser.setStPassword(stPassword);
		sysUser.setCreateBy(stCode);
		sysUser.setUpdateBy(stCode);
		return sysUser;
	}
}
