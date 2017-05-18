package com.taylor.elasticsearch;

import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;

import java.io.Serializable;

/**
 * 附加排序
 * 
 * @author 张涛
 * 
 */
@Data
public class SortOrderExt implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5973416767318194117L;
	private String sortFiled;
	private SortOrder sortOrder;
}
