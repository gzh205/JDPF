package connection;

import java.lang.reflect.Field;

public class Table {
	public String getDatas() {
		String dat = "";
		Field[] fields = this.getClass().getFields();
		for(Field f:fields) {
			try {
				dat += "," + f.getName() + ":" + f.get(this) ;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		dat = "[" + dat.substring(1) + "]";
		return dat;
	}
}
