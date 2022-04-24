package com.sinosoft.fragins.framework.utils;

import java.util.List;

public abstract interface MybatisBaseDao<T, I>
{
  public abstract int insert(T paramT);

  public abstract int insertSelective(T paramT);

  public default int insertBatch(List<T> entityList)
  {
    int rows = 0;
    if (entityList != null) {
      for (T t : entityList) {
        rows += insert(t);
      }
    }
    return rows;
  }

  public default int insertSelectiveBatch(List<T> entityList)
  {
    int rows = 0;
    if (entityList != null) {
      for (T t : entityList) {
        rows += insertSelective(t);
      }
    }
    return rows;
  }

  public abstract int insertBatchBySQL(List<T> paramList);

  public abstract int deleteByPrimaryKey(I paramI);

  public abstract int deleteBatchByPrimaryKeys(List<I> paramList);

  public abstract int delete(T paramT);

  public abstract int deleteBatch(List<T> paramList);

  public abstract int updateByPrimaryKey(T paramT);

  public abstract int updateByPrimaryKeyWithVersion(T paramT);

  public default int updateBatch(List<T> entityList)
  {
    int rows = 0;
    if (entityList != null) {
      for (T t : entityList) {
        rows += updateSelectiveByPrimaryKey(t);
      }
    }
    return rows;
  }

  public default int updateBatchWithVersion(List<T> entityList)
  {
    int rows = 0;
    if (entityList != null) {
      for (T t : entityList) {
        rows += updateSelectiveByPrimaryKeyWithVersion(t);
      }
    }
    return rows;
  }

  public abstract int updateSelectiveByPrimaryKey(T paramT);

  public abstract int updateSelectiveByPrimaryKeyWithVersion(T paramT);

  public abstract T selectByPrimaryKey(I paramI);

  public abstract List<T> selectBatchByPrimaryKeys(List<I> paramList);

  public abstract List<T> selectBatch(List<T> paramList);

  public abstract Page<T> selectPage(PageParam paramPageParam, T paramT);

  public default int save(T entity)
  {
    int rows = 0;
    if (existsByEntity(entity)) {
      rows = updateSelectiveByPrimaryKey(entity);
    } else {
      rows = insertSelective(entity);
    }
    return rows;
  }

  public default int saveWithVersion(T entity)
  {
    int rows = 0;
    if (existsByEntity(entity)) {
      rows = updateSelectiveByPrimaryKeyWithVersion(entity);
    } else {
      rows = insertSelective(entity);
    }
    return rows;
  }

  public default int saveBatch(List<T> entityList)
  {
    int rows = 0;
    for (T po : entityList) {
      rows += save(po);
    }
    return rows;
  }

  public default int saveBatchWithVersion(List<T> entityList)
  {
    int rows = 0;
    for (T po : entityList) {
      rows += saveWithVersion(po);
    }
    return rows;
  }

  public abstract boolean exists(I paramI);

  public abstract boolean existsByEntity(T paramT);
}
