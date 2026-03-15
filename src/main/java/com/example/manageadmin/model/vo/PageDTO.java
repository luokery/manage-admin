package com.example.manageadmin.model.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页响应 DTO
 */
@Data
@Schema(description = "分页响应")
public class PageDTO<T, E> {

    @Schema(description = "当前页码（从0开始）", example = "0")
    private int pageNumber;

    @Schema(description = "每页大小", example = "10")
    private int pageSize;

    @Schema(description = "总记录数", example = "100")
    private long totalElements;

    @Schema(description = "总页数", example = "10")
    private int totalPages;

    @Schema(description = "是否第一页")
    private boolean first;

    @Schema(description = "是否最后一页")
    private boolean last;

    @Schema(description = "是否有下一页")
    private boolean hasNext;

    @Schema(description = "是否有上一页")
    private boolean hasPrevious;
    
    @Schema(description = "参数数据")
    private E dataParam;
    
    @Schema(description = "数据列表")
    private List<T> dataList;
    
    public static <T, E> PageDTO<T, E> of(List<T> dataList, E dataParam, int pageNumber, int pageSize, long totalElements) {
        PageDTO<T, E> pageDTO = new PageDTO<>();
        pageDTO.result(dataList, dataParam, pageNumber, pageSize, totalElements);
        return pageDTO;
    }
    
    /**
     * 获取OFFSET值（用于分页查询）
     */
    public int getOffset() {
        return pageNumber * pageSize;
    }
    
    /**
     * 验证分页参数是否有效
     */
    public boolean isValidPagination() {
        return pageNumber >= 0 && pageSize > 0 && pageSize <= 100;
    }

	public void result(List<T> dataList, E queryParamDTO, int pageNumber, int pageSize, long total) {
		this.setDataList(dataList);
		this.setDataParam(queryParamDTO);
		this.setPageNumber(pageNumber);
		this.setPageSize(pageSize);
		this.setTotalElements(total);
		
        int totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
        this.setTotalPages(totalPages);
        
        this.setFirst(pageNumber == 0);
        this.setLast(pageNumber >= totalPages - 1 || totalPages == 0);
        this.setHasNext(pageNumber < totalPages - 1);
        this.setHasPrevious(pageNumber > 0);
	}
}