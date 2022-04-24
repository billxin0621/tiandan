package com.sinosoft.fragins.framework.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.datasource.DataSourceUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 使用JXLS模板文件进行大量数据导出到Excel的操作。因为数据量大，数据的来源目前以sql处理，允许中间加一层处理方法将sql的一行转换成JXLS模板中
 * 
 * @author panyu
 *
 */
@Slf4j
public class BigExcelExportUtils {

	@Data
	public static class SqlDataConfig {
		/** 在JXLS模板中对应集合的名称 */
		private String name;
		/** SQL语句 */
		private String sql;
		/** SQL参数 */
		private Object[] args;
		/** 转换ResultSet某一行到集合元素的方法 */
		Function<ResultSet, Object> rowMapFunction;

		/**
		 * 创建SQL数据读取配置
		 * 
		 * @param name           在JXLS模板中对应集合的名称
		 * @param sql            SQL语句
		 * @param args           SQL参数
		 * @param rowMapFunction 转换ResultSet某一行到集合元素的方法
		 */
		public SqlDataConfig(String name, String sql, Object[] args, Function<ResultSet, Object> rowMapFunction) {
			this.name = name;
			this.sql = sql;
			this.args = args;
			this.rowMapFunction = rowMapFunction;
		}
	}

	/**
	 * JXLS大数据导出
	 * 
	 * @param templateIn     JXLS模板输入流
	 * @param out            输出流
	 * @param context        除SQL读取数据以外的参数
	 * @param sqlDataConfigs SQL读取数据配置，每个配置用于产生一个集合，可以用多个产生多个集合
	 */
	public static void export(InputStream templateIn, OutputStream out, Map<String, Object> context,
			List<SqlDataConfig> sqlDataConfigs) {
		DataSource dataSource = SpringContextUtils.getBean(DataSource.class);
		Connection conn = DataSourceUtils.getConnection(dataSource);
		List<PreparedStatement> statements = new ArrayList<>();
		List<ResultSet> resultSets = new ArrayList<>();
		try {
			Map<String, Object> vars = context;
			if (context == null) {
				vars = new HashMap<String, Object>();
			}
			Context ctx = new Context(vars);
			for (SqlDataConfig sqlDataConfig : sqlDataConfigs) {
				PreparedStatement ps = conn.prepareStatement(sqlDataConfig.getSql());
				ArgumentPreparedStatementSetter setter = new ArgumentPreparedStatementSetter(sqlDataConfig.getArgs());
				setter.setValues(ps);
				ResultSet rs = ps.executeQuery();
				statements.add(ps);
				resultSets.add(rs);
				ctx.putVar(sqlDataConfig.getName(),
						new ResultSetIterable<Object>(rs, sqlDataConfig.getRowMapFunction()));
			}
			Workbook workbook = WorkbookFactory.create(templateIn);
			PoiTransformer transformer = new PoiTransformer(workbook, true);
			AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
			List<Area> xlsAreaList = areaBuilder.build();
			for (Area area : xlsAreaList) {
				String sheetName = area.getStartCellRef().getSheetName();
				area.applyAt(new CellRef(sheetName + "_导出!A1"), ctx);
			}
			for (Area area : xlsAreaList) {
				String sheetName = area.getStartCellRef().getSheetName();
				transformer.deleteSheet(sheetName);
			}
			SXSSFWorkbook workbook2 = (SXSSFWorkbook) transformer.getWorkbook();
			workbook2.write(out);
		} catch (Exception e) {
			log.error("Excel导出异常", e);
			throw new RuntimeException(e);
		} finally {
			for (ResultSet rs : resultSets) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			for (PreparedStatement st : statements) {
				try {
					st.close();
				} catch (Exception e) {
				}
			}
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}

}
