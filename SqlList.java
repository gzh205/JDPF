package connection;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class SqlList<T>{
	private List<T> list;
	private Class<?> type;
	public <T> SqlList() {
		this.list = new ArrayList<>();
		ParameterizedType ptype = (ParameterizedType) this.getClass().getGenericSuperclass();
		type = (Class<?>) ptype.getActualTypeArguments()[0].getClass();
	}
	public Class<?> getListClass() {
		return type;
	}
	public void add(T t) {
		list.add(t);
	}
	public T[] toArray() {
		return list.toArray();
	}
}
