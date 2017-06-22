package com.nisoft.instools.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.nisoft.instools.bean.Company;
import com.nisoft.instools.jdbc.JDBCUtil;

public class TestGosn {

	public static void main(String[] args) {
		ArrayList<String> structure = new ArrayList<>();
		structure.add("公司");
		structure.add("分公司/部门");
		structure.add("车间/科室");
		structure.add("班组");
		String companyName = "晋西车轴股份有限公司";
		String companyId = "JXCZ";
		Company company = new Company();
		company.setOrgCode(companyId);
		company.setOrgName(companyName);
		company.setOrgStructure(structure);
		Gson gson = new Gson();
		String json = gson.toJson(company);
		System.out.println(json);
		Company c = gson.fromJson(json, Company.class);
		System.out.println(c.getOrgCode());
		System.out.println(c.getOrgName());
		System.out.println(c.getOrgStructure().toString());
		String sql = "insert into company (companyId,company_name,structrue) values ('"
				+c.getOrgCode()+"','"+c.getOrgName()+"','"+c.getOrgStructure().toString()+"')";
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		try {
			st = conn.createStatement();
			int i = st.executeUpdate(sql);
			System.out.println("插入了i条数据");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
