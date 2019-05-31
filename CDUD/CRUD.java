package il.co.ilrd.crud;

public interface CRUD<T, ID> {
	   ID create(T entity);
	   T read(ID id);
	   void update(ID id, T entity);
	   void delete(ID id);
}