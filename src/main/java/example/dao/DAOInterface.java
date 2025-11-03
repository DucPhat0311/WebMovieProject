package example.dao;

import java.util.ArrayList;

public interface DAOInterface<T> {
	void add(T t);
	void update(T t);
	void delete(T t);
	ArrayList<T> selectAll();
}
