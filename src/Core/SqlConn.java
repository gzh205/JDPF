package Core;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SqlConn {
	public static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static SqlConn conn = null;
	private Connection connection;
	private HostInfo info;
	private SqlConn() {
	}
	public static SqlConn getSession(HostInfo info) throws Exception {
		if(SqlConn.conn==null) {
			SqlConn.conn = new SqlConn();
			if(!info.valid()) throw new Exception("HostInfo������Ϊ��ʼ���ĳ�Ա");
			SqlConn.conn.info = info;
			return SqlConn.conn;
		}else {
			return SqlConn.conn;
		}
	}
	private ResultSet query(String sql) {
		ResultSet rst = null;
		try {
			rst = this.connection.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rst;
	}
	private boolean execute(String sql) {
		boolean result = false;
		try {
			result = this.connection.createStatement().execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	public void select(Object table) throws Exception {
		String sql = "select ";
		String selectDat = "";
		String whereDat = "";
		Field[] fields = table.getClass().getFields();
		for(Field f:fields) {
			selectDat += "," + f.getName();
			if(f.getAnnotation(Table.PrimaryKey.class)!=null) {
				try {
					if(f.getType()==String.class||f.getType()==Date.class)
						whereDat += " " + f.getName() + "='" + f.get(table) + "' ";
					else
						whereDat += " " + f.getName() + "=" + f.get(table) + " ";
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		sql += selectDat.substring(1) + " from " + table.getClass().getName() + " where " + whereDat;
		this.connect();
		ResultSet result = query(sql);
		if(result==null) 
			table = null;
		else {
			result.last();
			for(int i=0;i<fields.length;i++) {
				fields[i].set(table, SqlConn.getResult(result, i+1, fields[i].getType()));
			}
		}
		this.close();
	}
	public <T> List<T> selectSome(Class type,String where) {
		List<T> lst = new ArrayList<T>();
		String sql = "select ";
		String selectData = "";
		Field[] fields = null;
		try {
			fields = type.getFields();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(Field f:fields) {
			selectData += "," + f.getName();
		}
		sql += selectData.substring(1) + " from " + type.getName() + " " + where;
		this.connect();
		ResultSet result = query(sql);
		if(result==null)
			lst = null;
		else {
			try {
				while(result.next()) {
					T table = (T)type.newInstance();
					Field[] arr = type.getFields();
					for(int i=0;i<arr.length;i++) {
						arr[i].set(table, SqlConn.getResult(result, i+1, arr[i].getType()));
					}
					lst.add(table);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.close();
		return lst;
	}
	public boolean alter(Object table) {
		String sql = "update ";
		String setData = "";
		String whereData = "";
		Field[] fields = table.getClass().getFields();
		for(Field f:fields) {			
			try {
				if(f.getAnnotation(Table.PrimaryKey.class)!=null) {
					if(f.getType()==String.class||f.getType()==Date.class)
						whereData += " and " + f.getName() + " = '" + f.get(table) + "'";
					else
						whereData += " and " + f.getName() + " = " + f.get(table);
				}else {
					if(f.getType()==String.class||f.getType()==Date.class)
						setData += "," + f.getName() + " = '" + f.get(table) + "'";
					else
						setData += "," + f.getName() + " = " + f.get(table);
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		sql = sql + table.getClass().getName() + " set " + setData.substring(1) + " where " + whereData.substring(5);
		this.connect();
		boolean res = this.execute(sql);
		this.close();
		return res;
	}
	public boolean delete(Object table) {
		String sql = "delete from ";
		String whereData = "";
		Field[] fields = table.getClass().getFields();
		for(Field f:fields) {
			if(f.getAnnotation(Table.PrimaryKey.class)!=null) {
				try {
					if(f.getType()==String.class||f.getType()== Date.class) {
						whereData += " and " + f.getName() + " = '" + f.get(table) + "'";
					}else {
						whereData += " and " + f.getName() + " = " + f.get(table);
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}
		sql  = sql + table.getClass().getName() + " where " + whereData.substring(5);
		this.connect();
		boolean res = this.execute(sql);
		this.close();
		return res;
	}
	public boolean insert(Object table) {
		String sql = "insert into ";
		String data = "";
		String valuesData = "";
		Field[] fields = table.getClass().getFields();
		for(Field f:fields) {
			data += "," + f.getName();
			try {
				if(f.getType()==String.class||f.getType()==Date.class)
					valuesData += ",'" + f.get(table) + "'";
				else
					valuesData += "," + f.get(table);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		sql = sql + table.getClass().getName() + "(" + data.substring(1) + ") values(" + valuesData.substring(1) + ")";
		this.connect();
		boolean res = this.execute(sql);
		this.close();
		return res;
	}
	public <T> void insertSome(List<T> tables) {
		Class type = tables.get(0).getClass();
		Field[] fields = type.getFields();
		String data = "";		
		for(Field f:fields) {
			data += "," + f.getName();
		}
		String sql = "insert into " + type.getName() + "(" + data.substring(1) + ") values(";
		this.connect();
		for(T t:tables) {
			Field[] fs = t.getClass().getFields();
			String valuesData = "";
			for(int i=0;i<fs.length;i++) {				
					try {
						if(fs[i].getType()==String.class||fs[i].getType()==Date.class)
							valuesData += ",'" + fs[i].get(tables.get(i))+"'";
						else
							valuesData += "," + fs[i].get(tables.get(i));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
			}
			this.execute(sql + valuesData.substring(1) + ")");
		}
		this.close();
	}
	private void connect() {
		try {
			this.connection = DriverManager.getConnection(SqlConn.conn.info.host,SqlConn.conn.info.username,SqlConn.conn.info.password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void close() {
		try {
			if(!this.connection.isClosed())
				this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Object getResult(ResultSet res,int num,Type type) throws Exception {
		try {
			if(type==String.class) {
				return (Object)res.getString(num);
			}
			else if(type== Date.class) {
				return (Object)res.getDate(num);
			}
			else if(type==int.class) {
				return (Object)res.getInt(num);
			}
			else if(type==double.class) {
				return (Object)res.getDouble(num);
			}
			else if(type==float.class) {
				return (Object)res.getFloat(num);
			}
			else if(type== Blob.class) {
				return (Object)res.getBlob(num);
			}
			else if(type== Clob.class) {
				return (Object)res.getClob(num);
			}
			else if(type==byte.class) {
				return (Object)res.getByte(num);
			}
			else{
				throw new Exception("SqlConn.getResult�ĵ�������������Type���󲻺�Ҫ��(����Ϊnull)");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
