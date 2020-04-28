package so.microcloud.common;

/**
 * 分页信息模型类，结合{@link com.ha.common.db.mybatis.plugin.page.PageInterceptor PageInterceptor}类
 * 使用以实现MyBatis查询的自动分页。
 *
 */
public class PageInfo extends BaseBean{

	private static final long serialVersionUID = 1;

	/**
	 * 每页显示条数
	 */
	private int page_size = 10;
	/**
	 * 当前页数
	 */
	private int current_page = 1;
	/**
	 * 总页数
	 */
	private int totalPage;
	/**
	 * 总记录数
	 */
	private int totalCount;

	
	
	public PageInfo() {
		super();
	}

	
	public PageInfo(int current_page,int page_size) {
		super();
		if(current_page > 0){
			this.current_page = current_page;
		}
		if(page_size > 0){
			this.page_size = page_size;
		}
	}



	public int getPageSize() {
		return page_size;
	}

	public PageInfo setPageSize(int pageSize) {
		this.page_size = pageSize;
		return this;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public PageInfo setTotalPage(int totalPage) {
		this.totalPage = totalPage;
		return this;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public PageInfo setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		return this;
	}

	public int getCurrentPage() {
		return current_page;
	}

	public PageInfo setCurrentPage(int currentPage) {
		this.current_page = currentPage;
		return this;
	}

	public int getRowOffset() {
		return current_page<=0?0:(current_page-1)*page_size;
	}

}
